package com.bakcbase.kalah.service.mapper

import com.bakcbase.kalah.service.model.KalahGame
import com.bakcbase.kalah.to.KalahGameTo
import spock.lang.Specification

class KalahGameMapperSpec extends Specification {

    def 'mapToDto boundary null'() {

        given:
        KalahGameMapper kalahGameMapper = new KalahGameMapper()

        when:
        KalahGameTo kalahGameTo = kalahGameMapper.mapToDto(null);

        then:
        kalahGameTo == null
    }

    def 'mapToDto should convert domain to a dto with status as a map of indexes(starting by 1) to values'() {

        given:
        KalahGameMapper kalahGameMapper = new KalahGameMapper()
        KalahGame kalahGame = new KalahGame()
        int[] board = [6, 8, 5, 4, 1, 7, 9, 0, 11, 13, 16, 18, 20, 23]
        kalahGame.setBoard(board)

        when:
        KalahGameTo kalahGameTo = kalahGameMapper.mapToDto(kalahGame);

        then:
        kalahGameTo.getStatus().size() == board.size()
        for (int i = 1; i <= board.length; i++)
            assert kalahGameTo.getStatus().get(String.valueOf(i)) == String.valueOf(board[i - 1])
    }

}
