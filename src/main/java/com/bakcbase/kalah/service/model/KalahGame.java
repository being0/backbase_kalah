package com.bakcbase.kalah.service.model;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/11/2020
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "kalah_game")
public class KalahGame {

    /**
     * Game Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Game status
     */
    @Column(name = "status")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Integer statusId;

    /**
     * Date of order creation
     */
    @Column(name = "created")
    private Date created;

    /**
     * Date order modified
     */
    @Column(name = "modified")
    private Date modified;

    /**
     * Number of stones at start, could be for example 4 or 6
     */
    @Column(name = "no_of_stones")
    private Integer noOfStones;

    /**
     * Number of pits, normally 6
     */
    @Column(name = "no_of_pits")
    private Integer noOfPits;

    /**
     * Keep turn as 0/1 to show which side turn is it(up side or down side). At first it is null.
     */
    @Column(name = "turn_side")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Boolean turnSideId;

    @Transient
    private int kalah1Index;

    @Transient
    private int kalah2Index;

    /**
     * Board model
     */
    private int[] board;

    public KalahGame(Long id, GameStatus status, Date created, Date modified,
                     Integer noOfPits, Integer noOfStones, BoardSide turnSide, int[] board) {
        this.id = id;
        setStatus(status);
        this.created = created;
        this.modified = modified;
        this.noOfPits = noOfPits;
        this.noOfStones = noOfStones;
        setTurnSide(turnSide);
        this.board = board;

        // Set kalah indexes
        kalah1Index = kalah1Index(noOfPits);
        kalah2Index = kalah2Index(noOfPits);
    }

    private static int kalah1Index(Integer noOfPits) {
        return noOfPits;
    }

    private static int kalah2Index(Integer noOfPits) {
        return noOfPits * 2 + 1;
    }

    public void setStatus(GameStatus status) {

        this.statusId = status == null ? null : status.getValue();
    }

    public GameStatus getStatus() {
        if (statusId == null) return null;

        return GameStatus.of(statusId);
    }

    public void setTurnSide(BoardSide turnSide) {

        this.turnSideId = turnSide == null ? null : turnSide.getValue();
    }

    public BoardSide getTurnSide() {
        if (turnSideId == null) return null;

        return BoardSide.of(turnSideId);
    }

    /**
     * Creates a Kalah game model
     *
     * @param noOfPits   Number of pits, normally 6
     * @param noOfStones Number of stones in each pit, exp. 4 or 6
     * @return KalahGame model
     */
    public static KalahGame doCreate(int noOfPits, int noOfStones) {
        if (noOfPits < 1 || noOfStones < 1) throw new IllegalArgumentException("Use positive noOfPits and noOfStones.");

        // Setup board with pits equal to numberOfStones and Kalahs with 0, no of pits will noOfPits * 2 + 2 kalahs
        int[] board = new int[noOfPits * 2 + 2];
        Arrays.fill(board, noOfStones);
        board[kalah1Index(noOfPits)] = 0;
        board[kalah2Index(noOfPits)] = 0;

        Date currentTime = getCurrentTime();

        return new KalahGame(null, GameStatus.created, currentTime, currentTime, noOfPits, noOfStones, null, board);
    }

    /**
     * Moves stones from the specified pit id. This method is not thread safe, should be used in a synchronized environment.
     *
     * @param pitId pit id
     * @return Kalah game
     */
    public KalahGame doMove(Integer pitId) {

        // If game is over then return this
        if (getStatus() == GameStatus.over) return this;

        // Validate move
        validateMove(pitId);

        // Every checks passed, so move should be applied
        setStatus(GameStatus.running);

        // Make this pit empty
        board[pitId] = 0;


        setModified(getCurrentTime());

        return this;
    }

    private void validateMove(Integer pitId) {
        // Validate pitId
        if (pitId == null || pitId < 0 || pitId > 13)
            throw new ValidationException("Pit Id should be valid positive number under 13.");

        if (pitId == 0 || pitId == 13) throw new ValidationException("Stones in kalah can not be moved.");

        // Pit should not be empty
        if (board[pitId] == 0) throw new EmptyPitException("The pit is empty!");

        // Make sure it is correct pit id turn
        if (getTurnSide() != null && getTurnSide() != actionSide(pitId)) {
            throw new BadTurnException("It is not your turn. Wait for your opponent to move.");
        }
    }

    /**
     * Which side tries to move its pit, if pitId is under 7 then it is down side(0), else it is up side(1)
     *
     * @param pitId Pit id
     * @return Board side that is trying to the action
     */
    private BoardSide actionSide(Integer pitId) {

        return pitId < kalah1Index ? BoardSide.down : BoardSide.up;
    }

    private static Date getCurrentTime() {

        return new Date();
    }


    /**
     * Game status could be created, running or over
     */
    public enum GameStatus {

        created(1), running(2), over(3);

        private Integer value;

        GameStatus(Integer value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }

        public static GameStatus of(Integer value) {
            if (value == null) return null;

            return Stream.of(GameStatus.values())
                    .filter(p -> p.getValue() == value)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }

    /**
     * Shows the side of board that games is running on it could be up or down
     */
    public enum BoardSide {

        up(true), down(false);

        private Boolean value;

        BoardSide(Boolean value) {
            this.value = value;
        }

        Boolean getValue() {
            return value;
        }

        public static BoardSide of(Boolean value) {
            if (value == null) return null;

            return Stream.of(BoardSide.values())
                    .filter(p -> p.getValue() == value)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }

}
