package com.bakcbase.kalah.service;

import com.bakcbase.kalah.service.model.KalahGame;

import java.util.function.Function;

/**
 * Provides lock and doInLock does the function in a locked and synchronized block
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public interface LockProvider {

    /**
     * Do the function in lock
     *
     * @param kalahGame kalah game
     * @param kalahGameFunc function that should be done in lock
     * @return kalah game
     */
    KalahGame doInLock(KalahGame kalahGame, Function<KalahGame, KalahGame> kalahGameFunc);
}
