package com.memorytree.forest.stt;

import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class SttService {
    public String recognizeSpeechFor3Seconds() {
        String transcript = "";
        try {
            // 3초 동안 STT 실행
            transcript = StreamingSpeechRecognizer.streamingMicRecognize(3);

        } catch (Exception e){
            throw new CommonException(ErrorCode.STT_SERVICE_ERROR);
        }
        return transcript;
    }
}
