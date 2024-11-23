package com.memorytree.forest.service;

import com.memorytree.forest.domain.Game;
import com.memorytree.forest.domain.User;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.request.GameResultRequestDto;
import com.memorytree.forest.dto.response.GameRecordDto;
import com.memorytree.forest.dto.response.GameResultResponseDto;
import com.memorytree.forest.dto.response.GameType;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.GameRepository;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public GameRecordDto generateRandomGameRecordDto(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        Game game = gameRepository.findByUser(user).orElseThrow(() -> new CommonException(ErrorCode.INTERNAL_SERVER_ERROR));

        GameType selectedGameType = getRandomGameType();
        // todo: if-else로 안하는 방법...?
        Float highScore = getHighScoreByGameType(selectedGameType, game);

        return selectedGameType.toDto(highScore);
    }

    private GameType getRandomGameType() {
        GameType[] gameTypes = GameType.values();
        Random random = new Random();
        int index = random.nextInt(gameTypes.length);
        return gameTypes[index];
    }

    private Float getHighScoreByGameType(GameType selectedGameType, Game game) {
        if (selectedGameType.equals(GameType.숫자_순서_게임)) {
            return game.getNumberSequenceGame();
        } else if(selectedGameType.equals(GameType.카드_뒤집기)) {
            return game.getFlipCardGame();
        } else if (selectedGameType.equals(GameType.틀린_글자_찾기)) {
            return game.getSpotDifferenceGame();
        }
        return 0.0f;
    }

    public GameResultResponseDto updateHighScore(Long id, GameResultRequestDto gameResult) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        // todo: 사용자와 연결된 Game 정보가 없을 경우 발생하는 에러는?
        //       INTERNAL_SERVER_ERROR여야 하나?
        Game game = gameRepository.findByUser(user).orElseThrow(() -> new CommonException(ErrorCode.INTERNAL_SERVER_ERROR));
        GameType gameType = GameType.from(gameResult.gameType()).orElseThrow(() -> new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT));
        float highScore = getHighScoreByGameType(gameType, game);

        boolean updated = false;
        if (highScore > gameResult.score()) {
            updated = true;
            updateHighScoreByGameType(gameType, game, gameResult.score());
            gameRepository.save(game);
        }
        return new GameResultResponseDto(updated);
    }

    private void updateHighScoreByGameType(GameType gameType, Game game, float newHighScore) {
        if (gameType.equals(GameType.숫자_순서_게임)) {
            game.setNumberSequenceGame(newHighScore);
        } else if(gameType.equals(GameType.카드_뒤집기)) {
            game.setFlipCardGame(newHighScore);
        } else if (gameType.equals(GameType.틀린_글자_찾기)) {
            game.setSpotDifferenceGame(newHighScore);
        }
    }
}
