package com.memorytree.forest.dto.response;

import java.util.List;

public record MissionDto(
        Boolean gamePlayed,
        Boolean diaryWrote,
        Integer level,
        List<GameRecordDto> statistics) {
}
