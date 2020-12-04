package com.bol.mancala.mancalagame.repository;

import com.bol.mancala.mancalagame.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
