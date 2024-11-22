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
    private Integer walk;

    @Column(name = "mission_num")
    private Integer missionNum;

    @Column(name = "diary_num_month")
    private Integer diaryNumMonth;

    @Column(name = "level")
    private Integer level;

    @Column(name = "level_exp")
    private Integer levelXP;

    @Column(name = "coin")
    private Integer coin;

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
