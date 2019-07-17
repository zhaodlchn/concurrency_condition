package com.lock.semaphore;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 打饭学生
 */
public class Student implements Runnable {

    // 学生姓名
    private String name;

    // 打饭许可
    private Semaphore semaphore;

    /**
     * 打饭方式
     * 0：一直等待直到打到饭；
     * 1：等了一会不耐烦了，回宿舍吃泡面了；
     * 2：打饭中途被其他同学叫走了，不再等待
     */
    private int type;

    public Student(String name, Semaphore semaphore, int type) {
        this.name = name;
        this.semaphore = semaphore;
        this.type = type;
    }

    /**
     * 打饭
     */
    @Override
    public void run() {
        try {
            switch (type) {
                // 这个学生很有耐心，它会一直排队直到打到饭
                case 0:
                    // 排队
                    semaphore.acquireUninterruptibly();
                    // 打饭
                    Thread.sleep(RandomUtils.nextLong(1000, 3000));
                    System.out.println("=== " + this.name + " 终于打到饭了！ ===");
                    // 将打饭的机会让给后面的同学
                    semaphore.release();
                    break;

                // 这个学生没有耐心，等了一段时间没打到饭，就回宿舍泡面了
                case 1:
                    // 排队打饭
                    if (semaphore.tryAcquire(RandomUtils.nextInt(4000, 10000), TimeUnit.MILLISECONDS)) {
                        // 打饭
                        Thread.sleep(RandomUtils.nextLong(1000, 3000));
                        System.out.println("=== " + this.name + " 终于打到饭了！ ===");
                        // 将打饭的机会让给后面的同学
                        semaphore.release();
                    } else {
                        // 回宿舍泡面
                        System.out.println("=== " + this.name + " 回宿舍吃泡面！ ===");
                    }
                    break;

                // 这个学生也很有耐心，但是他们班突然宣布聚餐，它只能放弃打饭了
                case 2:
                    try {
                        // 排队
                        semaphore.acquire();

                        // 打饭
                        Thread.sleep(RandomUtils.nextLong(1000, 3000));
                        System.out.println("=== " + this.name + " 终于打到饭了！ ===");

                        // 将打饭的机会让给后面的同学
                        semaphore.release();
                    } catch (InterruptedException e) {
                        // 被叫去聚餐，不再打饭
                        System.out.println("=== " + this.name + " 全部聚餐，不再打饭！ ===");
                    }
                    break;

                default:
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
