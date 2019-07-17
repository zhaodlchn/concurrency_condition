package com.lock.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 流水线上有3个worker: worker1、worker2、worker3,只有当worker1和worker2执行完时才可以执行worker3
 */
public class CountDownLatchSecond {

    private static final int COUNT = 2;

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(COUNT);

        WorkerCount worker1 = new WorkerCount("worker-1", (long) (Math.random() * 10000), countDownLatch);
        WorkerCount worker2 = new WorkerCount("worker-2", (long) (Math.random() * 10000), countDownLatch);

        worker1.start();
        worker2.start();

        countDownLatch.await();
        System.out.println("=== 准备工作就绪 ===");

        WorkerCount worker3 = new WorkerCount("worker-3", (long) (Math.random() * 10000), countDownLatch);
        worker3.start();
        Thread.sleep(10000);
    }


}
