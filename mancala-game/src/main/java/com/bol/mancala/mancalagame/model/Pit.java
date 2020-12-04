package com.bol.mancala.mancalagame.model;

import com.bol.mancala.mancalagame.enums.PitType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    private int position;
    private int numberOfStones;
    @Enumerated(EnumType.STRING)
    private PitType pitType;

    public Pit(Game game, int position, int numberOfStones, PitType pitType) {
        this.game = game;
        this.position = position;
        this.numberOfStones = numberOfStones;
        this.pitType = pitType;
    }

    public void sow () {
        this.numberOfStones++;
    }

    public Boolean isEmpty (){
        return this.numberOfStones == 0;
    }

    public void clear (){
        this.numberOfStones = 0;
    }

    public void addStones (int numberOfStones){
        this.numberOfStones += numberOfStones;
    }

}
