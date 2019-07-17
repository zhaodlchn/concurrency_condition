package com.lock.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模拟10人赛跑，10人跑完后才喊"Game Over."
 */
public class CountDownLatchFirst {

    private static final int RUNNER_COUNT = 20;

    public static void race(int runnerCount) throws InterruptedException {
        final CountDownLatch begin = new CountDownLatch(1);
        final CountDownLatch runnerLatch = new CountDownLatch(runnerCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < runnerCount; i++) {
            int runnerNum = i + 1;
            Runnable service = new Runnable() {
                @Override
                public void run() {
                    try {
                        begin.await();
                        Thread.sleep((long) Math.random() * 10000);
                        System.out.println("=== No." + runnerNum + " arrived ===");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        runnerLatch.countDown();
                    }
                }
            };
            executorService.submit(service);
        }

        System.out.println("=== Game Start ... ===");
        begin.countDown();
        runnerLatch.await();
        System.out.println("=== Game Over ... ===");
        executorService.shutdown();
    }

    public static void main(String[] args) {
        try {
            race(RUNNER_COUNT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
