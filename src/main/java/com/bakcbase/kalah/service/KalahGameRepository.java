package com.bakcbase.kalah.service;

import com.bakcbase.kalah.service.model.KalahGame;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
public interface KalahGameRepository extends JpaRepository<KalahGame, Long> {
}
