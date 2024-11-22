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
    private float levelEXP;

    @Column(name = "coin")
    private int coin;

    @Column(name = "last_game_played_date")
    private LocalDate lastGamePlayedDate;

    @Column(name = "last_quiz_played_date")
    private LocalDate lastQuizPlayedDate;

    @Builder
    public User(Long id, String name){
        this.id = id;
        this.name = name;
    }

}
