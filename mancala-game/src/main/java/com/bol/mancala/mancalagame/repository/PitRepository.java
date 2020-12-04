package com.bol.mancala.mancalagame.repository;

import com.bol.mancala.mancalagame.model.Pit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PitRepository extends JpaRepository<Pit, Integer> {
    Optional<Pit> findByPosition(Integer position);
}
