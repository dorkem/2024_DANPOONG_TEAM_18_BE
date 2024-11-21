package com.memorytree.forest.service;

import com.memorytree.forest.domain.Game;
import com.memorytree.forest.domain.User;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.response.GameRecordDto;
import com.memorytree.forest.dto.response.MissionDto;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.GameRepository;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MissionService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public MissionService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public MissionDto getMissionStateByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        // todo: 게임 테이블에 유저와 연결된 레코드가 없을 경우에 처리할 에러 코드가 필요?
        Game game = gameRepository.findByUser(user).orElseThrow(() -> new CommonException(ErrorCode.INTERNAL_DATA_ERROR));

        List<GameRecordDto> statistics = List.of(
                new GameRecordDto("숫자 순서 게임", game.getNumberSequenceGame()),
                new GameRecordDto("틀린 글자 찾기", game.getSpotDifferenceGame()),
                new GameRecordDto("카드 뒤집기", game.getFlipCardGame())
        );
        return new MissionDto(
                false, // todo: 테스트용도로 일부러 써놓음
                true,
                user.getLevel(),
                statistics
        );
    }
}
