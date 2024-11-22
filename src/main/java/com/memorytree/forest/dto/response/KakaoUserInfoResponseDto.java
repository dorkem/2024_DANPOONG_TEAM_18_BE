package com.memorytree.forest.dto.response;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserInfoResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;
}