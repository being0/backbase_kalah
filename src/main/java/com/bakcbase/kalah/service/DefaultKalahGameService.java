package com.bakcbase.kalah.service;

import com.bakcbase.kalah.service.mapper.KalahGameMapper;
import com.bakcbase.kalah.service.model.KalahGame;
import com.bakcbase.kalah.to.KalahGameTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Kalah game service implementation. It persist kalah game into kalahGameRepository and do the movement in the lockProvider
 * doInLock block to do the actions in synchronized block.
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@Service
@Slf4j
public class DefaultKalahGameService implements KalahGameService {

    @Resource
    private KalahGameMapper kalahGameMapper;
    @Resource
    private KalahGameRepository kalahGameRepository;
    @Resource
    private LockProvider lockProvider;

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

        // Find the game by id
        KalahGame kalahGame = kalahGameRepository.findById(gameId)
                .orElseThrow(() -> new KalahGameNotFoundException("No Kalah game found for the specified id!"));

        // Do the movement in a distributed synchronized block
        KalahGame movedGame = lockProvider.doInLock(kalahGame, (kg) -> {

            // Move the Kalah game
            KalahGame movedKalahGame = kg.doMove(pitId);

            // Save on repository
            return kalahGameRepository.save(movedKalahGame);
        });

        return kalahGameMapper.mapToDto(movedGame);
    }
}
