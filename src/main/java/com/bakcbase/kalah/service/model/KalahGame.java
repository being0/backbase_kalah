package com.bakcbase.kalah.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/11/2020
 */
@Entity
@AllArgsConstructor
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
    @Column(name = "number_of_stones")
    private Integer numberOfStones;

    private static final int KALAH_1_INDEX = 7;
    private static final int KALAH_2_INDEX = 13;

    /**
     * Board model
     */
    private int[] board;

    public static KalahGame toCreate(int numberOfStones) {

        // Setup board with pits equal to numberOfStones and Kalahs with 0
        int[] board = new int[14];
        Arrays.fill(board, numberOfStones);
        board[KALAH_1_INDEX] = 0;
        board[KALAH_2_INDEX] = 0;

        Date currentTime = getCurrentTime();

        return new KalahGame(null, currentTime, currentTime, numberOfStones, board);
    }

    public static Date getCurrentTime() {

        return new Date();
    }

}
