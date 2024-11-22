package com.memorytree.forest.dto.request;

import com.memorytree.forest.dto.response.GameType;

public record GameResultRequestDto(String gameType, Float score) {
}
