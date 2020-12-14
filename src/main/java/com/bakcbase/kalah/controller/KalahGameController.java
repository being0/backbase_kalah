package com.bakcbase.kalah.controller;

import com.bakcbase.kalah.service.KalahGameService;
import com.bakcbase.kalah.to.KalahGameTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This controller provides post api on /games and put api to play with the Kalah and move the specified pit
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@RestController
@RequestMapping(value = "/games")
public class KalahGameController {

    @Resource
    private KalahGameService kalahGameService;

    /**
     * Creates a new game
     *
     * @param request http request
     * @return controller response with the game id and game url
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ControllerResponse create(HttpServletRequest request) {

        KalahGameTo kalahGameTo = kalahGameService.create();

        return kalahGameTo == null ? null : new ControllerResponse(kalahGameTo.getId(),
                request.getRequestURL() + "/" + kalahGameTo.getId(), null);
    }

    /**
     * Moves game to the next step.
     *
     * @param gameId game id
     * @param pitId pit id starting from 1
     * @param request http request
     * @return controller response with the game id and game url and status of game
     */
    @PutMapping("/{gameId}/pits/{pitId}")
    public ControllerResponse move(@PathVariable("gameId") Long gameId, @PathVariable("pitId") Integer pitId, HttpServletRequest request) {

        KalahGameTo kalahGameTo = kalahGameService.move(gameId, pitId);

        return new ControllerResponse(gameId, request.getRequestURL().toString().replace("/pits/" + pitId, ""),
                kalahGameTo.getStatus());
    }

}
