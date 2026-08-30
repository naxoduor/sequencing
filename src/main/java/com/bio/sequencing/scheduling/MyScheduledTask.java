package com.bio.sequencing.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduledTask {

    @Scheduled(fixedRate = 1000)
    public void executeEverySecond() {
        System.out.println("Running...");
    }

    @Scheduled(fixedRate = 60_000)
    public void executeEveryMinute() {
        // business logic
    }
}