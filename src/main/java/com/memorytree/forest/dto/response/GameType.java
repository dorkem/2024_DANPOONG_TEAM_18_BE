package com.memorytree.forest.dto.response;

import java.util.Optional;

public enum GameType {
    숫자_순서_게임("숫자 순서 게임"),
    틀린_글자_찾기("틀린 단어 찾기 게임"),
    카드_뒤집기("카드 뒤집기 게임");

    private final String value;

    GameType(String value) {
        this.value = value;
    }

    public GameRecordDto toDto(Float highScore) {
        return new GameRecordDto(this.value, highScore);
    }

    public static Optional<GameType> from(String gameTypeString) {
        for(GameType gameType : GameType.values()) {
            if (gameType.value.equals(gameTypeString)) {
                return Optional.of(gameType);
            }
        }
        return Optional.empty();
    }
}