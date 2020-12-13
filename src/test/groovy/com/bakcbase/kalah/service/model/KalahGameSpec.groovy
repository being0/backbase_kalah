package com.bakcbase.kalah.service.model

import spock.lang.Specification

import static com.bakcbase.kalah.service.model.KalahGame.BoardSide.down
import static com.bakcbase.kalah.service.model.KalahGame.BoardSide.up
import static com.bakcbase.kalah.service.model.KalahGame.GameStatus.created
import static com.bakcbase.kalah.service.model.KalahGame.GameStatus.over

class KalahGameSpec extends Specification {

    def '"doCreate" should prepare KalahGame object for creation'() {

        given:
        Date now = new Date();

        when:
        KalahGame kalahGame = KalahGame.doCreate(6, 4);

        then:
        kalahGame.getId() == null
        kalahGame.getStatus() == created
        now.time - 100l < kalahGame.getCreated().time
        kalahGame.getCreated().time < now.time + 100l
        kalahGame.getCreated() == kalahGame.getModified()
        kalahGame.getPlayerSide() == null
        kalahGame.getNoOfPits() == 6
        kalahGame.getNoOfStones() == 4
        kalahGame.getBoard() == [4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0] as int[]
    }

    def '"doMove" when status of game is over just return the game'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4);
        kalahGame.setStatus(over)
        int[] board = [0, 0, 0, 0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 32] as int[];
        kalahGame.board = board

        when:
        KalahGame afterMove = kalahGame.doMove(3)

        then:
        afterMove.status == over
        afterMove.board == board
    }

    def '"doMove" Exception: if Kalah ids or invalid pit id selected then throw ValidationException'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4);

        when:
        kalahGame.doMove(invalidKalahId as Integer)

        then:
        thrown(ValidationException)

        where:
        invalidKalahId << [
                6,  // Kalah
                13, // Kalah
                -1, // minus
                14, // more than size of board
                null]
    }

    def '"doMove" Exception: Moving an empty pit will cause EmptyPitException'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.board = [1, 5, 6, 9, 7, 8, 6, 10, 6, 7, 5, 0, 6, 3] as int[]

        when:
        kalahGame.doMove(11)

        then:
        thrown(EmptyPitException)
    }

    def '"doMove" Exception: If not your turn then throw NotYourTurnException'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.setPlayerSide(palyerSide)

        when:
        kalahGame.doMove(pitId as Integer)

        then:
        thrown(NotYourTurnException)

        where:
        pitId | palyerSide
        0     | up
        1     | up
        2     | up
        3     | up
        4     | up
        5     | up
        7     | down
        8     | down
        9     | down
        10    | down
        11    | down
        12    | down
    }

    def '"doMove" Check couple of normal moves'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.setPlayerSide(palyerSide)

        when:
        kalahGame.doMove(pitId as Integer)

        then:

        where:
        pitId | palyerSide
        0     | up
        1     | up
        2     | up
        3     | up
        4     | up
        5     | up
        7     | down
        8     | down
        9     | down
        10    | down
        11    | down
        12    | down
    }

}
