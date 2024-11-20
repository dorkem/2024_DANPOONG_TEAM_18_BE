package com.memorytree.forest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DiaryTextRequestDto(
        @JsonProperty("who")
        String diary_who,
        @JsonProperty("where")
        String diary_where,
        @JsonProperty("what")
        String diary_what
) {
}
