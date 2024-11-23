package com.memorytree.forest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "game")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {
    //PK
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //FK
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "number_sequence_game")
    private Float numberSequenceGame;

    @Column(name = "spot_difference_game")
    private Float spotDifferenceGame;

    @Column(name = "flip_card_game")
    private Float flipCardGame;

    @Column(name = "tangram_puzzle")
    private Float tangramPuzzle;
}
