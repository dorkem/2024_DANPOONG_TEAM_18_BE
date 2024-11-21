package com.memorytree.forest.dto.response;

public record LoginResponseDto(
        Long id,
        String nickname,
        String email,
        String profileImage
) {}