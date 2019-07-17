package com.lock.countdownlatch;

public class Worker extends Thread {

    private String name;

    private long time;

    public Worker(String name, long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            System.out.println("=== " + this.name + " 开始工作 ===");
            Thread.sleep(this.time);
            System.out.println("=== " + this.name + " 工作完成，耗时：" + this.time + " ===");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
