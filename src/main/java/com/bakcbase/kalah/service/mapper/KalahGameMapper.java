package com.bakcbase.kalah.service.mapper;

import com.bakcbase.kalah.service.model.KalahGame;
import com.bakcbase.kalah.to.KalahGameTo;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.TreeMap;
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

        // Use a linked hash map to keep the order of insertion
        LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
        for (int i = 0; i < kalahGame.getBoard().length; i++)
            linkedMap.put(String.valueOf(i + 1), String.valueOf(kalahGame.getBoard()[i]));

        return new KalahGameTo(kalahGame.getId(), linkedMap);
    }
}
