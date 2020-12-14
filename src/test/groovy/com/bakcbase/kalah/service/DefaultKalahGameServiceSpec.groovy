package com.bakcbase.kalah.service

import com.bakcbase.kalah.service.mapper.KalahGameMapper
import com.bakcbase.kalah.service.model.KalahGame
import com.bakcbase.kalah.to.KalahGameTo
import spock.lang.Specification

import java.util.function.Function

import static com.bakcbase.kalah.service.model.KalahGame.BoardSide.up
import static com.bakcbase.kalah.service.model.KalahGame.GameStatus.created

class DefaultKalahGameServiceSpec extends Specification {

    DefaultKalahGameService kalahGameService
    KalahGameMapper kalahGameMapper

    def setup() {
        kalahGameMapper = new KalahGameMapper() // a little integration is okay

        kalahGameService = new DefaultKalahGameService()
        kalahGameService.kalahGameMapper = kalahGameMapper
        kalahGameService.noOfPits = 6
        kalahGameService.noOfStones = 6
    }

    def '"create" should create an initial game'() {

        given:
        KalahGameRepository kalahGameRepository = Stub()
        kalahGameService.kalahGameRepository = kalahGameRepository
        kalahGameRepository.save(_) >> { KalahGame kalahGame ->
            kalahGame.id = 100
            return kalahGame
        }

        when:
        KalahGameTo kalahGameTo = kalahGameService.create()

        then: 'KalahGameTo is created with an id'
        kalahGameTo.id == 100L
        kalahGameTo.status == ["1": "6", "2": "6", "3": "6", "4": "6", "5": "6", "6": "6", "7": "0",
                               "8": "6", "9": "6", "10": "6", "11": "6", "12": "6", "13": "6", "14": "0"]

    }

    def '"move" If game not found throw KalahGameNotFoundException'() {

        given:
        KalahGameRepository kalahGameRepository = Stub()
        kalahGameService.kalahGameRepository = kalahGameRepository
        kalahGameRepository.findById(_) >> Optional.empty()

        when:
        kalahGameService.move(100L, 1)

        then:
        thrown(KalahGameNotFoundException)
    }

    def '"move" Normal case that moves game to next state'() {

        given:
        KalahGameRepository kalahGameRepository = Stub()
        kalahGameService.kalahGameRepository = kalahGameRepository
        LockProvider lockProvider = Stub()
        lockProvider.doInLock(_, _) >> { KalahGame kalahGame, Function<KalahGame, KalahGame> kalahGameFunc ->
            kalahGameFunc.apply(kalahGame)
        }
        kalahGameService.lockProvider = lockProvider
        kalahGameRepository.findById(_) >> Optional.of(new KalahGame(100L, created, new Date(), new Date(), 6,
                6, up, [6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0] as int[]))
        kalahGameRepository.save(_) >> { KalahGame kalahGame -> kalahGame }

        when:
        KalahGameTo kalahGameTo = kalahGameService.move(100L, 11)

        then:
        kalahGameTo.id == 100L
        kalahGameTo.status == ["1": "7", "2": "7", "3": "7", "4": "6", "5": "6", "6": "6", "7": "0",
                               "8": "6", "9": "6", "10": "6", "11": "0", "12": "7", "13": "7", "14": "1"] as Map
    }


}
