package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
import com.tsypk.corestuff.controller.dto.stuff.request.SearchByTextRequest
import com.tsypk.corestuff.controller.dto.stuff.response.StuffSearchResponse
import com.tsypk.corestuff.services.StuffService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/stuff")
class StuffController(
    private val stuffService: StuffService
) {

    @PostMapping(
        value = ["search-by-text"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun searchByText(@RequestBody request: SearchByTextRequest): ResponseEntity<List<StuffSearchResponse>> {
        val response = stuffService.searchByText(request)
        return ResponseEntity.ok(response)
    }

    fun buyStuff(@RequestHeader userId: Long, @RequestBody buyStuffRequest: BuyStuffRequest) {

    }

}