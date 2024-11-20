package com.memorytree.forest.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name = "diary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "diary_when")
    private Date when;

    @Column(name = "diary_what")
    private String what;

    @Column(name = "diary_who")
    private String who;
}

