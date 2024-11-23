package com.memorytree.forest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HomeResponceDto(
        @JsonProperty("connection")
        int connection
) {
}
