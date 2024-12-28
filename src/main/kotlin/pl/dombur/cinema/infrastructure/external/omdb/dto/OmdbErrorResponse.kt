package pl.dombur.cinema.infrastructure.external.omdb.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OmdbErrorResponse(
    @JsonProperty("Response")
    val response: String,
    @JsonProperty("Error")
    val error: String,
)
