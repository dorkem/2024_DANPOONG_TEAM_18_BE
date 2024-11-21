package com.memorytree.forest.stt;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StreamingSpeechRecognizer {

    public static String streamingMicRecognize(int durationInSeconds) throws Exception {
        final StringBuilder transcript = new StringBuilder();

        ResponseObserver<StreamingRecognizeResponse> responseObserver =
                new ResponseObserver<StreamingRecognizeResponse>() {
                    ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

                    public void onStart(StreamController controller) {}

                    public void onResponse(StreamingRecognizeResponse response) {
                        responses.add(response);
                    }

                    public void onComplete() {
                        String previousTranscript = "";
                        for (StreamingRecognizeResponse response : responses) {
                            for (StreamingRecognitionResult result : response.getResultsList()) {
                                for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {
                                    String currentTranscript = alternative.getTranscript().trim();
                                    if (!currentTranscript.equals(previousTranscript)) {
                                        transcript.append(currentTranscript).append(" ");
                                        previousTranscript = currentTranscript;
                                    }
                                }
                            }
                        }
                    }

                    public void onError(Throwable t) {
                        System.out.println(t);
                    }
                };

        try (SpeechClient client = SpeechClient.create()) {
            ClientStream<StreamingRecognizeRequest> clientStream =
                    client.streamingRecognizeCallable().splitCall(responseObserver);

            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode("ko-KR")
                            .setSampleRateHertz(16000)
                            .build();
            StreamingRecognitionConfig streamingRecognitionConfig =
                    StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();

            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                            .setStreamingConfig(streamingRecognitionConfig)
                            .build(); // The first request in a streaming call has to be a config

            clientStream.send(request);
            // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true, bigEndian: false
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info targetInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat); // Set the system information to read from the microphone audio stream

            if (!AudioSystem.isLineSupported(targetInfo)) {
                throw new UnsupportedOperationException("Microphone not supported");
            }
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            AudioInputStream audio = new AudioInputStream(targetDataLine);
            long startTime = System.currentTimeMillis();

            while (true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                byte[] data = new byte[6400];
                audio.read(data);

                if (estimatedTime > durationInSeconds * 1000) { // Specified duration
                    System.out.println("Stop speaking.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    break;
                }

                request =
                        StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(data))
                                .build();
                clientStream.send(request);
            }
            clientStream.closeSend();
            responseObserver.onComplete();

            // Allow time for the response to complete
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }

        return transcript.toString().trim(); // Return the transcript
    }
}
