package com.lock.semaphore;

import java.util.concurrent.Semaphore;

/**
 * Semaphore经常用于限制获取某种资源的线程数量。
 * <p>
 * <p>
 * 场景说明：
 * 模拟学校食堂的窗口打饭过程
 * 学校食堂有2个打饭窗口
 * 学校中午有20个学生 按次序 排队打饭
 * 每个人打饭时耗费时间不一样
 * 有的学生耐心很好，他们会一直等待直到打到饭
 * 有的学生耐心不好，他们等待时间超过了心里预期，就不再排队，而是回宿舍吃泡面了
 * 有的学生耐心很好，但是突然接到通知，说是全班聚餐，所以也不用再排队，而是去吃大餐了
 * <p>
 * 重点分析：
 * 食堂有2个打饭窗口：需要定义一个permits=2的Semaphore对象。
 * 学生 按次序 排队打饭：此Semaphore对象是公平的。
 * 有20个学生：定义20个学生线程。
 * 打到饭的学生：调用了acquireUninterruptibly()方法，无法被中断
 * 吃泡面的学生：调用了tryAcquire(timeout,TimeUnit)方法，并且等待时间超时了
 * 吃大餐的学生：调用了acquire()方法，并且被中断了
 */
public class SemaphoreDemo {

    /**
     * 打饭窗口
     * 2:2个打饭窗口
     * true：公平队列-FIFO
     */
    private static Semaphore semaphore = new Semaphore(2, true);

    /**
     * 食堂打饭
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread[] dinnerStudents = new Thread[5];
        for (int i = 0; i < 20; i++) {
            if (i < 10) {
                new Thread(new Student("打饭学生-" + i, semaphore, 0)).start();
            } else if (i >= 10 && i < 15) {
                new Thread(new Student("泡面学生-" + i, semaphore, 1)).start();
            } else {
                dinnerStudents[i - 15] = new Thread(new Student("聚餐学生-" + i, semaphore, 2));
                dinnerStudents[i - 15].start();
            }
        }

        Thread.sleep(2000);
        for (Thread thread : dinnerStudents) {
            thread.interrupt();
        }
    }
}
