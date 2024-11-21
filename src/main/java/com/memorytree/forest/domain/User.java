package com.memorytree.forest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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
    private int levelEXP = 0;

    @Column(name = "coin", nullable = false)
    private int coin = 0;

    @Builder
    public User(Long id, String name, String profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }
}
