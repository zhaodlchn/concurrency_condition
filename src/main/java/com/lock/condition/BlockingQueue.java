package com.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue {

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition putCondition = lock.newCondition();

    private final Condition takeCondition = lock.newCondition();

    private String[] items;

    // 元素个数，放入元素索引，取元素索引
    private int count, putIdx, takeIdx;

    public BlockingQueue(int size) {
        items = new String[size];
    }

    /**
     * 将元素放入队列
     *
     * @param str
     */
    public void put(String str) {
        lock.lock();
        try {
            // 队列已满，释放锁，进行等待
            while (count == items.length) {
                System.out.println("=== 队列已满，等待取值中 ===");
                putCondition.await();
            }

            items[putIdx] = str;

            // 如果添加到数组的最后一个元素，下标从头开始，防止下标越界
            if (++putIdx == items.length) {
                putIdx = 0;
            }

            // 元素个数增加
            count++;

            // 通知 take 线程,可以取数据了,不必继续阻塞
            takeCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 取元素
     *
     * @return
     */
    public String take() {
        lock.lock();
        String str = null;
        try {
            // 队列中没有数据，则等待新数据的放入
            while (count == 0) {
                System.out.println("=== 目前队列已空，等待入值中 ===");
                takeCondition.await();
            }

            // 取数据
            str = items[takeIdx];

            // 清空原有的值
            items[takeIdx] = null;

            // 如果下标到数组尽头了，就从头开始
            if (++takeIdx == items.length) {
                takeIdx = 0;
            }

            // 元素数量递减
            count--;

            // 通知阻塞的 put 线程可以装填数据了
            putCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return str;
    }
}
