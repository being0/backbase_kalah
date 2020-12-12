package com.bakcbase.kalah.service;

import com.bakcbase.kalah.to.KalahGameTo;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public interface KalahGameService {

    KalahGameTo create();

    KalahGameTo move(Long gameId, Integer pitId);

}
