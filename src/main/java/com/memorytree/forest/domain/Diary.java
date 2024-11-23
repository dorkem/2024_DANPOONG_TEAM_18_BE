package com.memorytree.forest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "diary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {
    // PK로 날짜 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "diary_when", nullable = false)
    private LocalDate when;

    //FK
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "diary_what")
    private String what;

    @Column(name = "diary_where")
    private String where;

    @Column(name = "diary_who")
    private String who;

    // 생성자
    public Diary(User user) {
        this.user = user;
        this.when = LocalDate.now(); // PK를 현재 날짜로 설정
    }

    // 필드 값 설정 메서드
    public void updateWhat(String what) {
        this.what = what;
    }

    public void updateWho(String who) {
        this.who = who;
    }

    public void updateWhere(String where) {
        this.where = where;
    }
}

