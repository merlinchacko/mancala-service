package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player createPlayer(String playerName) {
        Player player = new Player(playerName);
        playerRepository.save(player);
        return player;
    }

    public void clearPlayerDetails() {
        playerRepository.deleteAll();
    }
}
