package com.bakcbase.kalah.service;

import com.bakcbase.kalah.to.KalahGameTo;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public interface KalahGameService {

    /**
     * Create a Kalah game
     *
     * @return Kalah game
     */
    KalahGameTo create();

    /**
     * Moves the Kalah game to next state according to the selected pit id
     *
     * @param gameId game id
     * @param pitId  pit id starting from 1
     * @return Kalah game
     */
    KalahGameTo move(Long gameId, Integer pitId);

}
