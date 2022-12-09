/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package org.openapitools.client.models


import com.squareup.moshi.Json

/**
 * An order for a pets from the pet store
 *
 * @param id 
 * @param petId 
 * @param quantity 
 * @param shipDate 
 * @param status Order Status
 * @param complete 
 */


data class Order (

    @Json(name = "id")
    var id: kotlin.Long? = null,

    @Json(name = "petId")
    var petId: kotlin.Long? = null,

    @Json(name = "quantity")
    var quantity: kotlin.Int? = null,

    @Json(name = "shipDate")
    var shipDate: java.time.OffsetDateTime? = null,

    /* Order Status */
    @Json(name = "status")
    var status: Order.Status? = null,

    @Json(name = "complete")
    var complete: kotlin.Boolean? = false

) {

    /**
     * Order Status
     *
     * Values: placed,approved,delivered
     */
    enum class Status(val value: kotlin.String) {
        @Json(name = "placed") placed("placed"),
        @Json(name = "approved") approved("approved"),
        @Json(name = "delivered") delivered("delivered");
    }
}

