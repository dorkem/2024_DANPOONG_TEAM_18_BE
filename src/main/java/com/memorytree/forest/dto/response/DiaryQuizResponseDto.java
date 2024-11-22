package com.memorytree.forest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DiaryQuizResponseDto (
        @JsonProperty("question")
        String question,
        @JsonProperty("choices")
        List<String> choices
){
}
