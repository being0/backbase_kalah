package com.bakcbase.kalah.service;

import com.bakcbase.kalah.service.model.KalahGame;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/**
 * Provides distributed lock using Redis and Redisson client
 *
 * @author <a href="mailto:raliakbari@gmail.com">Reza Aliakbari</a>
 * @version 1, 12/12/2020
 */
@Component
public class RedissionLockProvider implements LockProvider {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public KalahGame doInLock(KalahGame kalahGame, Function<KalahGame, KalahGame> kalahGameFunc) {

        // Do the action in fair lock so the order is preserved
        Lock lock = redissonClient.getFairLock("kalah_game_" + kalahGame.getId());
        lock.lock();
        try {
            return kalahGameFunc.apply(kalahGame);
        } finally {
            lock.unlock();
        }
    }
}
