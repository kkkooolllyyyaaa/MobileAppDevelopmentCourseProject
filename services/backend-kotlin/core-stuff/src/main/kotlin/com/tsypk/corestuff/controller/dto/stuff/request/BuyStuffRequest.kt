package com.tsypk.corestuff.controller.dto.stuff.request

import com.fasterxml.jackson.annotation.JsonProperty

data class BuyStuffRequest(
        @JsonProperty(value = "supplier_id")
        val supplierId: Long,
        @JsonProperty(value = "model_id")
        val modelId: String,
        @JsonProperty(value = "count")
        val count: Int,
) {
}
