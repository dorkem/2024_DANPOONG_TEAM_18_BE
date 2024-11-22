package com.memorytree.forest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiaryQuizAnswerResponseDto (
        @JsonProperty("correct")
        boolean correct,
        @JsonProperty("content")
        String content
){
}
