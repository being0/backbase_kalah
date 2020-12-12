package com.bakcbase.kalah.controller;

import com.bakcbase.kalah.service.KalahGameService;
import com.bakcbase.kalah.to.KalahGameTo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@RestController
@RequestMapping(value = "/games")
public class KalahGameController {

    @Resource
    private KalahGameService kalahGameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KalahGameTo create() {

        return kalahGameService.create();
    }

    @PutMapping("/{gameId}/pits/{pitId}")
    public KalahGameTo move(@PathVariable("gameId") Long gameId, @PathVariable("pitId") Integer pitId) {

        return kalahGameService.move(gameId, pitId);
    }

}
