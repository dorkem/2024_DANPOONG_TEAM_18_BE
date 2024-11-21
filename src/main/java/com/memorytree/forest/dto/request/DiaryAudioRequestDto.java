package com.memorytree.forest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiaryAudioRequestDto(
        @JsonProperty("type")
        String type
){

}