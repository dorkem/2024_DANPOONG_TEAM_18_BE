package com.memorytree.forest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiaryAudioResponseDto (
        @JsonProperty("type")
        String type,
        @JsonProperty("content")
        String content
){
}
