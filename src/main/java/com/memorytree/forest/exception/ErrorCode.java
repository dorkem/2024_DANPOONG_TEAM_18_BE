package com.memorytree.forest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400
    WRONG_USER(40000, HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    MISSING_REQUEST_PARAMETER(40001, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_FORMAT(40002, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    BAD_REQUEST_JSON(40003, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),

    //402

    //403
    FORBIDDEN_ROLE(40300, HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),

    //404
    NOT_FOUND_USER(40400, HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_FOUND_DIARY(40401, HttpStatus.NOT_FOUND, "일기가 존재하지 않습니다."),
    // 409 (Conflict)
    DIARY_ALREADY_EXISTS(40900, HttpStatus.CONFLICT, "이미 오늘의 일기가 존재합니다."),
    NOT_ENOUGH_DIARY(40901, HttpStatus.CONFLICT, "질문을 생성하기 위한 일기가 부족합니다."),
    //500
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다"),
    INTERNAL_DATA_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "데이터 처리 중 오류가 발생하였습니다."),
    EXTERNAL_SERVER_ERROR(50002, HttpStatus.INTERNAL_SERVER_ERROR, "외부 서버 오류입니다"),
    STT_NO_SPEECH(50003, HttpStatus.BAD_REQUEST, "사용자가 발화하지 않았습니다."),
    STT_SERVICE_ERROR(50004, HttpStatus.INTERNAL_SERVER_ERROR, "STT 서비스 중 오류가 발생했습니다.");


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
