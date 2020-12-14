package com.bakcbase.kalah.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Create response for the controller
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/14/2020
 */
@Getter
@Setter
@AllArgsConstructor
public class ControllerResponse {

    /**
     * Game id
     */
    private Long id;

    /**
     * game url like http://<host>:<port>/games/1234
     */
    private String url;

    /**
     * Game status
     */
    private Map status;
}
