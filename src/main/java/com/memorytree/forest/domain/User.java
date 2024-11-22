package com.memorytree.forest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    //PK
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile")
    private String profile;

    @Column(name = "walk")
    private int walk;

    @Column(name = "mission_num")
    private int missionNum;

    @Column(name = "diary_num_month")
    private int diaryNumMonth;

    @Column(name = "level")
    private int level;

    @Column(name = "level_exp")
    private int levelXP;

    @Column(name = "coin")
    private int coin;

    @Column(name = "diary_answer")
    private String diaryAnswer;

    @Builder
    public User(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public void updateDiaryAnswer(String diaryAnswer){
        this.diaryAnswer = diaryAnswer;
    }

}
