package com.bakcbase.kalah.service.mapper;

import com.bakcbase.kalah.service.model.KalahGame;
import com.bakcbase.kalah.to.KalahGameTo;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@Component
public class KalahGameMapper implements BaseDtoDomainMapper<KalahGameTo, KalahGame> {

    @Override
    public KalahGameTo mapToDto(KalahGame kalahGame) {
        if (kalahGame == null) return null;

        return new KalahGameTo(IntStream.range(0, kalahGame.getBoard().length).boxed().collect(toMap(i -> i + 1, i -> kalahGame.getBoard()[i])));
    }

}
