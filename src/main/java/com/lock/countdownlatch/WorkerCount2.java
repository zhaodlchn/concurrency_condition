package com.lock.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class WorkerCount2 extends Thread {

    private String name;

    private long time;

    private CountDownLatch countDownLatch;

    public WorkerCount2(String name, long time, CountDownLatch countDownLatch) {
        this.name = name;
        this.time = time;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println("=== " + this.name + " 开始阶段一工作 ===");
            Thread.sleep(this.time);
            System.out.println("=== " + this.name + " 阶段一工作完成，耗时：" + this.time + " ===");
            countDownLatch.countDown();
            System.out.println("=== countDownLatch.getCount() : " + countDownLatch.getCount() + " ===");

            System.out.println("=== " + this.name + " 开始阶段二工作 ===");
            Thread.sleep(this.time * 2);
            System.out.println("=== " + this.name + " 阶段二工作完成，耗时：" + this.time + " ===");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
