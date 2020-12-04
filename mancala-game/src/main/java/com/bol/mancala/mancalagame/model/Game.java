package com.bol.mancala.mancalagame.model;

import com.bol.mancala.mancalagame.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "game")
    private List<Pit> pits;

    @ManyToOne
    private Player firstPlayer;

    @ManyToOne
    private Player secondPlayer;

    @ManyToOne
    private Player playerTurn;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    public Game(Player player1, Player player2, Player playerTurn, GameStatus gameStatus) {
        this.firstPlayer = player1;
        this.secondPlayer = player2;
        this.playerTurn = playerTurn;
        this.gameStatus = gameStatus;
    }
}
