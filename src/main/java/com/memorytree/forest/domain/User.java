package com.memorytree.forest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    // PK
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id; // 카카오의 고유 ID 저장

    @Column(name = "name", nullable = false)
    private String name; // 카카오 닉네임 저장

    @Column(name = "profile")
    private String profile; // 카카오 프로필 이미지 URL 저장

    @Column(name = "walk", nullable = false)
    private int walk = 0;

    @Column(name = "mission_num", nullable = false)
    private int missionNum = 0;

    @Column(name = "diary_num_month", nullable = false)
    private int diaryNumMonth = 0;

    @Column(name = "level", nullable = false)
    private int level = 1;

    @Column(name = "level_exp", nullable = false)
    private float levelEXP = 0.0F;

    @Column(name = "coin", nullable = false)
    private int coin = 0;

    @Column(name = "last_login_time", nullable = false)
    private LocalDate lastLoginDate = LocalDate.now();

    @Column(name = "login_streaks", nullable = false)
    private Integer loginStreaks = 1;

    @Column(name = "last_game_played_date")
    private LocalDate lastGamePlayedDate;

    @Column(name = "last_quiz_played_date")
    private LocalDate lastQuizPlayedDate;

    @Builder
    public User(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }
}
