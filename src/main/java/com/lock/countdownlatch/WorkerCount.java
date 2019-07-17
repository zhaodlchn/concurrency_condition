package com.lock.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class WorkerCount extends Thread {

    private String name;

    private long time;

    private CountDownLatch countDownLatch;

    public WorkerCount(String name, long time, CountDownLatch countDownLatch) {
        this.name = name;
        this.time = time;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println("=== " + this.name + " 开始工作 ===");
            Thread.sleep(this.time);
            System.out.println("=== " + this.name + " 工作完成，耗时：" + this.time + " ===");
            countDownLatch.countDown();
            System.out.println("=== countDownLatch.getCount() : " + countDownLatch.getCount() + " ===");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
