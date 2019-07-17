package com.lock.countdownlatch;

public class JoinTest {

    public static void main(String[] args) throws InterruptedException {
        Worker worker1 = new Worker("worker-1", (long) (Math.random() * 10000));
        Worker worker2 = new Worker("worker-2", (long) (Math.random() * 10000));

        worker1.start();
        worker2.start();

        worker1.join();
        worker2.join();
        System.out.println("=== 准备工作就绪 ===");

        Worker worker3 = new Worker("worker-3", (long) (Math.random() * 10000));
        worker3.start();
        Thread.sleep(10000);
    }
}
