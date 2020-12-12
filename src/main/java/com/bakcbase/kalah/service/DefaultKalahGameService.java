package com.bakcbase.kalah.service;

import com.bakcbase.kalah.service.mapper.KalahGameMapper;
import com.bakcbase.kalah.service.model.KalahGame;
import com.bakcbase.kalah.to.KalahGameTo;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public class DefaultKalahGameService implements KalahGameService {

    @Resource
    private KalahGameMapper kalahGameMapper;
    @Resource
    private KalahGameRepository kalahGameRepository;

    @Value("${kalah_game.noOfPits:6}")
    private int noOfPits;
    @Value("${kalah_game.noOfStones:6}")
    private int noOfStones;

    @Override
    public KalahGameTo create() {

        // Create a new kalah game
        KalahGame kalahGame = KalahGame.doCreate(noOfPits, noOfStones);

        // Create kalah game on repository(we can raise async message here after creation for other parities interest, however for simplicity of this project async message is ignored)
        KalahGame createdKalahGame = kalahGameRepository.save(kalahGame);

        return kalahGameMapper.mapToDto(createdKalahGame);
    }

    @Override
    public KalahGameTo move(Long gameId, Integer pitId) {
        if (gameId == null || gameId <= 0) throw new IllegalArgumentException("Make sure game id is positive number.");

        // Find the game by id
        KalahGame kalahGame = kalahGameRepository.findById(gameId)
                .orElseThrow(() -> new KalahGameNotFoundException("No Kalah game found for the specified id!"));

        // Move the Kalah game
        KalahGame movedKalahGame = kalahGame.doMove(pitId);

        return kalahGameMapper.mapToDto(movedKalahGame);
    }
}