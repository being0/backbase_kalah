package com.bakcbase.kalah.service.model

import spock.lang.Specification

import static com.bakcbase.kalah.service.model.KalahGame.BoardSide.down
import static com.bakcbase.kalah.service.model.KalahGame.BoardSide.up
import static com.bakcbase.kalah.service.model.KalahGame.GameStatus.created
import static com.bakcbase.kalah.service.model.KalahGame.GameStatus.over
import static com.bakcbase.kalah.service.model.KalahGame.GameStatus.running

class KalahGameSpec extends Specification {

    def '"doCreate" should prepare KalahGame object for creation'() {

        given:
        Date now = new Date();

        when:
        KalahGame kalahGame = KalahGame.doCreate(6, 4);

        then:
        kalahGame.getId() == null
        kalahGame.getStatus() == created
        now.time - 1000l < kalahGame.getCreated().time
        kalahGame.getCreated().time < now.time + 1000l
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
                7,  // Kalah
                14, // Kalah
                0,  // zero
                -1, // minus
                15, // more than size of board
                null]
    }

    def '"doMove" Exception: Moving an empty pit will cause EmptyPitException'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.board = [1, 5, 6, 9, 7, 8, 6, 10, 6, 7, 5, 0, 6, 3] as int[]

        when:
        kalahGame.doMove(12)

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
        1     | up
        2     | up
        3     | up
        4     | up
        5     | up
        6     | up
        8     | down
        9     | down
        10    | down
        11    | down
        12    | down
        13    | down
    }

    def '"doMove" Check couple of normal moves'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.setPlayerSide(palyerSide)

        when:
        kalahGame.doMove(pitId as Integer)

        then:
        kalahGame.board == expectedBoard

        where:
        pitId | palyerSide | expectedBoard
        1     | down       | [0, 5, 5, 5, 5, 4, 0, 4, 4, 4, 4, 4, 4, 0] as int[]
        2     | down       | [4, 0, 5, 5, 5, 5, 0, 4, 4, 4, 4, 4, 4, 0] as int[]
        3     | down       | [4, 4, 0, 5, 5, 5, 1, 4, 4, 4, 4, 4, 4, 0] as int[]
        4     | down       | [4, 4, 4, 0, 5, 5, 1, 5, 4, 4, 4, 4, 4, 0] as int[]
        5     | down       | [4, 4, 4, 4, 0, 5, 1, 5, 5, 4, 4, 4, 4, 0] as int[]
        6     | down       | [4, 4, 4, 4, 4, 0, 1, 5, 5, 5, 4, 4, 4, 0] as int[]
        8     | up         | [4, 4, 4, 4, 4, 4, 0, 0, 5, 5, 5, 5, 4, 0] as int[]
        9     | up         | [4, 4, 4, 4, 4, 4, 0, 4, 0, 5, 5, 5, 5, 0] as int[]
        10    | up         | [4, 4, 4, 4, 4, 4, 0, 4, 4, 0, 5, 5, 5, 1] as int[]
        11    | up         | [5, 4, 4, 4, 4, 4, 0, 4, 4, 4, 0, 5, 5, 1] as int[]
        12    | up         | [5, 5, 4, 4, 4, 4, 0, 4, 4, 4, 4, 0, 5, 1] as int[]
        13    | up         | [5, 5, 5, 4, 4, 4, 0, 4, 4, 4, 4, 4, 0, 1] as int[]
    }

    def '"doMove" Check game over'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.status == running
        kalahGame.setPlayerSide(up)
        kalahGame.board = [3, 2, 6, 1, 7, 4, 18, 0, 0, 0, 0, 0, 1, 9] as int[]

        when:
        kalahGame.doMove(13)

        then:
        kalahGame.board == [0, 0, 0, 0, 0, 0, 41, 0, 0, 0, 0, 0, 0, 10] as int[]
        kalahGame.status == over
    }

    def '"doMove" Move last into kalah keep the turn as it is'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.status == running
        kalahGame.setPlayerSide(side)
        kalahGame.board = [3, 2, 6, 1, 2, 4, 18, 4, 3, 4, 7, 3, 1, 9] as int[]

        when:
        kalahGame.doMove(pitId)

        then:
        kalahGame.playerSide == side

        where:
        side | pitId
        up   | 10
        down | 5
    }

    def '"doMove" Move last into empty pit of the player side should catch the stone and its opponent opposite stones'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.status == running
        kalahGame.setPlayerSide(side)
        kalahGame.board = board

        when:
        kalahGame.doMove(pitId)

        then:
        kalahGame.board == expectedBoard as int[]
        kalahGame.playerSide == side.opponentSide

        where:
        board                                                | side | pitId | expectedBoard
        [3, 2, 6, 1, 2, 4, 18, 4, 3, 4, 7, 0, 1, 9] as int[] | up   | 9     | [3, 0, 6, 1, 2, 4, 18, 4, 0, 5, 8, 0, 1, 12] as int[]
        [3, 2, 6, 2, 2, 0, 18, 4, 3, 4, 7, 4, 1, 9] as int[] | down | 4     | [3, 2, 6, 0, 3, 0, 23, 0, 3, 4, 7, 4, 1, 9] as int[]
        [0, 2, 6, 2, 2, 3, 18, 4, 2, 4, 7, 4, 2, 9] as int[] | up   | 13    | [1, 2, 6, 2, 2, 3, 18, 4, 2, 4, 7, 4, 0, 10] as int[] // move to 0 of opponent works as normal
        [3, 2, 6, 2, 2, 3, 18, 4, 0, 4, 7, 4, 1, 9] as int[] | down | 6     | [3, 2, 6, 2, 2, 0, 19, 5, 1, 4, 7, 4, 1, 9] as int[] // move to 0 of opponent works as normal
    }

    def '"doMove" Check boundary of moving 1'() {

        given:
        KalahGame kalahGame = KalahGame.doCreate(6, 4)
        kalahGame.board = [1, 1, 0, 0, 10, 0, 16, 0, 0, 0, 0, 0, 1, 19] as int[]

        when:
        KalahGame movedGame = kalahGame.doMove(2)

        then:
        movedGame.board == [1, 0, 0, 0, 10, 0, 17, 0, 0, 0, 0, 0, 1, 19] as int[]
    }

}
