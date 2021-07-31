package com.siklusdev.qiscuschat.model.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class Response<out T>(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "data") val data: T,
    @Json(name = "meta") val meta: Meta?
)

@JsonClass(generateAdapter = true)
class ErrorResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "count_data") val countData: Int,
    @Json(name = "count_page") val countPage: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "page") val page: Int
)

@JsonClass(generateAdapter = true)
class ResponseStatus(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String
)