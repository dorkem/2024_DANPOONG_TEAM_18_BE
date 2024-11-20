package com.memorytree.forest.exception;

import com.memorytree.forest.dto.global.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 메소드의 인자 타입이 일치하지 않을 때 발생하는 예외
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseDto<?> handleArgumentNotValidException(MethodArgumentTypeMismatchException e) {
        log.error("handleArgumentNotValidException() in GlobalExceptionHandler throw MethodArgumentTypeMismatchException : {}", e.getMessage());
        return ResponseDto.fail(e);
    }

    // 필수 파라미터가 누락되었을 때 발생하는 예외
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseDto<?> handleArgumentNotValidException(MissingServletRequestParameterException e) {
        log.error("handleArgumentNotValidException() in GlobalExceptionHandler throw MethodArgumentNotValidException : {}", e.getMessage());
        return ResponseDto.fail(e);
    }
    /**
     * 커스텀 exception
     */
    @ExceptionHandler(value = {CommonException.class})
    public ResponseDto<?> handleCustomException(CommonException e) {
        log.error("handleCommonException() in GlobalExceptionHandler throw CommonException : {}", e.getMessage());
        return ResponseDto.fail(new CommonException(e.getErrorCode()));
    }
    /**
     * 서버 exception
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseDto<?> handleServerException(Exception e){
        log.info("occurred exception in handleServerError = {}", e.getMessage());
        e.printStackTrace();
        return ResponseDto.fail(new CommonException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
