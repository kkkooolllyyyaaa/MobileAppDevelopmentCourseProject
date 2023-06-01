package redis

import (
	"corenotif/log"
	"corenotif/model/entity"
	"corenotif/service"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis"
)

type Redis struct {
	stuffUpdatesWorkers int
	buyRequestsWorkers  int
	service             service.NotificationService
	client              redis.Client
	stuffUpdates        chan redis.Message
	buyRequests         chan redis.Message
}

func GetRedis(stuffUpdatesWorkers, stuffUpdatesCap int, buyRequestsWorkers, buyRequestsCap int, service service.NotificationService) *Redis {
	defer func() {
		log.Log(log.SUCCESS, "Redis Client was set")
	}()

	return &Redis{
		stuffUpdatesWorkers: stuffUpdatesWorkers,
		buyRequestsWorkers:  buyRequestsWorkers,
		service:             service,
		client: *redis.NewClient(&redis.Options{
			Addr:     "localhost:6379",
			Password: "",
			DB:       0,
		}),
		stuffUpdates: make(chan redis.Message, stuffUpdatesCap),
		buyRequests:  make(chan redis.Message, buyRequestsCap),
	}
}

func (r *Redis) Start() {
	for i := 0; i < r.stuffUpdatesWorkers; i++ {
		go r.processStuffUpdates(i)
	}

	for i := 0; i < r.buyRequestsWorkers; i++ {
		go r.processBuyRequests(i)
	}

	r.consume()
}

func (r *Redis) consume() {
	redisPubSub := r.client.Subscribe("stuff_update_channel", "buy_request_channel")

	defer func(redisPubSub *redis.PubSub) {
		err := redisPubSub.Close()
		if err != nil {
			fmt.Println(log.Err("Error closing redisPubSub", err))
		}
	}(redisPubSub)

	for {
		msg, err := redisPubSub.ReceiveMessage()
		if err != nil {
			fmt.Println(log.Err(fmt.Sprintf("Error receiving msg from %s", msg.Channel), err))
			time.Sleep(100 * time.Millisecond)
			continue
		}

		switch msg.Channel {
		case "stuff_update_channel":
			r.stuffUpdates <- *msg
		case "buy_request_channel":
			r.buyRequests <- *msg
		}
	}
}

func (r *Redis) processStuffUpdates(gr int) {
	for {
		message := <-r.stuffUpdates
		log.Log(log.INFO, fmt.Sprintf("Gorutine: %d: Got stuff updates message", gr))

		value := entity.StuffUpdateEventsBatch{}

		if err := json.Unmarshal([]byte(message.Payload), &value); err != nil {
			fmt.Println(log.Err("Can't unmarshal json value", err))
			continue
		}
		r.service.ProcessStuffUpdate(value)
	}
}

func (r *Redis) processBuyRequests(gr int) {
	for {
		message := <-r.buyRequests
		log.Log(log.INFO, fmt.Sprintf("Gorutine: %d: Got buy request message", gr))

		value := entity.BuyRequestEvent{}

		if err := json.Unmarshal([]byte(message.Payload), &value); err != nil {
			fmt.Println(log.Err("Can't unmarshal json value", err))
			continue
		}
		r.service.ProcessBuyRequest(value)
	}
}
