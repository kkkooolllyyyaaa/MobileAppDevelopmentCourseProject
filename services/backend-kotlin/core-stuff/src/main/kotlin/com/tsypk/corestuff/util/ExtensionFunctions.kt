package com.tsypk.corestuff.util

import com.tsypk.corestuff.controller.dto.airpods.AirPodsDto
import com.tsypk.corestuff.controller.dto.airpods.SupplierAirPodsDto
import com.tsypk.corestuff.controller.dto.iphone.IphoneDto
import com.tsypk.corestuff.controller.dto.iphone.SupplierIphoneDto
import com.tsypk.corestuff.controller.dto.macbook.MacbookDto
import com.tsypk.corestuff.controller.dto.macbook.SupplierMacbookDto
import com.tsypk.corestuff.exception.NotAirPodsIdException
import com.tsypk.corestuff.exception.NotIphoneIdException
import com.tsypk.corestuff.model.apple.SupplierAirPods
import com.tsypk.corestuff.model.apple.SupplierIphone
import com.tsypk.corestuff.model.apple.SupplierMacbook
import recognitioncommons.exception.InvalidAirPodsIdException
import recognitioncommons.exception.InvalidIphoneIdException
import recognitioncommons.exception.NotPlayStationException
import recognitioncommons.exception.PlaystationInvalidIdException
import recognitioncommons.models.Money
import recognitioncommons.models.apple.airpods.AirPodsFullModel
import recognitioncommons.models.apple.iphone.IphoneFullModel
import recognitioncommons.models.sony.PlayStationFullModel
import recognitioncommons.util.extractor.airPodsFullModelFromId
import recognitioncommons.util.extractor.playStationFullModelFromId

/**
 * Iphone
 */
fun IphoneDto.toSupplierIphone(supplierId: Long): SupplierIphone =
    SupplierIphone(
        id = this.id,
        supplierId = supplierId,
        country = this.country,
        priceAmount = this.money.amount,
        priceCurrency = this.money.currency,
    )

fun SupplierIphone.toSupplierIphoneDto(): SupplierIphoneDto =
    SupplierIphoneDto(
        id = this.id,
        supplierId = this.supplierId,
        country = this.country,
        money = Money(
            amount = this.priceAmount,
            currency = this.priceCurrency,
        ),
    )

fun iphoneFullModelFromId(input: String): IphoneFullModel {
    try {
        return recognitioncommons.util.extractor.iphoneFullModelFromId(input)
    } catch (e: InvalidIphoneIdException) {
        throw NotIphoneIdException(input)
    }
}

/**
 * AirPods
 */
fun AirPodsDto.toSupplierAirPods(supplierId: Long): SupplierAirPods =
    SupplierAirPods(
        supplierId = supplierId,
        id = this.id,
        country = this.country,
        priceAmount = this.money.amount,
        priceCurrency = this.money.currency
    )

fun SupplierAirPods.toSupplierAirPodsDto(): SupplierAirPodsDto =
    SupplierAirPodsDto(
        id = this.id,
        supplierId = this.supplierId,
        country = this.country,
        money = Money(
            amount = this.priceAmount,
            currency = this.priceCurrency,
        ),
    )

fun airPodsFullModelFromId(input: String): AirPodsFullModel {
    try {
        return airPodsFullModelFromId(input)
    } catch (e: InvalidAirPodsIdException) {
        throw NotAirPodsIdException(input)
    }
}


/**
 * Playstation
 */
fun playStationFullModelFromid(input: String): PlayStationFullModel {
    try {
        return playStationFullModelFromId(input)
    } catch (e: PlaystationInvalidIdException) {
        throw NotPlayStationException(input)
    }
}

/**
 * Macbook
 */
fun MacbookDto.toSupplierMacbookMapper(supplierId: Long): SupplierMacbook =
    SupplierMacbook(
        supplierId = supplierId,
        macId = this.id,
        country = this.country,
        priceAmount = this.money.amount,
        priceCurrency = this.money.currency
    )

fun SupplierMacbook.toSupplierMacbookDtoMapper(): SupplierMacbookDto =
    SupplierMacbookDto(
        id = this.macId,
        supplierId = this.supplierId,
        country = this.country,
        money = Money(this.priceAmount, this.priceCurrency)
    )