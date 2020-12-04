package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.enums.PitType;
import com.bol.mancala.mancalagame.exception.GameNotFoundException;
import com.bol.mancala.mancalagame.exception.PitNotFoundException;
import com.bol.mancala.mancalagame.model.Game;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.repository.PitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PitService {

    private PitRepository pitRepository;

    public PitService(PitRepository pitRepository) {
        this.pitRepository = pitRepository;
    }

    public Pit createPit(Game game, int position, int numberOfStones, PitType pitType) {
        Pit pit = new Pit(game, position, numberOfStones, pitType);
        pitRepository.save(pit);
        return pit;
    }

    public Pit getPit(int position) {
        return pitRepository.findByPosition(position).orElseThrow(() -> new PitNotFoundException("Pit not found on the given position"));
    }

    public Pit updatePitByStones(int position, int nrOfStones) {
        Pit pit = getPit(position);
        pit.setNumberOfStones(nrOfStones);
        pitRepository.save(pit);
        return pit;
    }

    public List<Pit> getAllPits() {
        return pitRepository.findAll();
    }

    public void clearPitDetails() {
        pitRepository.deleteAll();
    }
}
