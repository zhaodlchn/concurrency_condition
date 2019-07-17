package com.lock.condition;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueueTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        BlockingQueue blockingQueue = new BlockingQueue(100);
        executorService.execute(() -> {
            char c = 'A';
            int index = 0;
            while (true) {
                blockingQueue.put(String.valueOf(c) + index);
                c = (char) (c + 1);
                if (c > 90) {
                    c = 'A';
                    index++;
                }
            }
        });

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.execute(() -> {
            String str;
            while ((str = blockingQueue.take()) != null) {
                System.out.println("=== 取得元素 === : " + str);
            }
        });

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}