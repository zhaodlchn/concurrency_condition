package com.lock.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 例子描述的是一个周期性处理任务的例子，在这个例子中有一对的任务（100个），希望每10个为一组进行处理，当前仅当上一组任务处理完成后才能进行下一组，另外在每一组任务中，当任务剩下50
 * %，所有任务执行完成时向观察者发出通知。
 * 在这个例子中，CyclicBarrierDemo
 * 构建了一个count+1的任务组（其中一个任务时为了外界方便挂起主线程）。每一个子任务里，任务本身执行完毕后都需要等待同组内其它任务执行完成后才能继续。同时在剩下任务50%、30%已经0
 * 时执行特殊的其他任务（发通知）。
 */
public class CyclicBarrierDemo {
    final CyclicBarrier barrier;//屏障锁
    final int MAX_TASK;//屏障锁可共享数

    /**
     * @param cnt
     */
    public CyclicBarrierDemo(int cnt) {
        //主线程也需要锁一次，所以cnt + 1，主线程等待一组10个线程执行完，
        //同时主线程释放锁，达到屏障点,执行下一组线程
        barrier = new CyclicBarrier(cnt + 1);
        //每一次，可运行的任务锁
        MAX_TASK = cnt;
    }

    public void doWork(final Runnable work) {
        new Thread() {
            public void run() {
                work.run();
                try {
                    //释放屏障共享锁
                    int index = barrier.await();
                    //检查进度
                    doWithIndex(index);
                } catch (InterruptedException e) {
                    return;
                } catch (BrokenBarrierException e) {
                    return;
                }
            }
        }.start();
    }

    /**
     * 检查线程组完成进度，根据不通过的进度，可以做一些通知工作
     *
     * @param index
     */
    private void doWithIndex(int index) {
        if (index == MAX_TASK / 2) {
            System.out.println("Left 50%");
        }
        if (index == 0) {
            System.out.println("run over");
        }
    }

    public void waitForNext() {
        try {
            //主线程释放屏障共享锁，检查进度
            doWithIndex(barrier.await());
        } catch (InterruptedException e) {
            return;
        } catch (BrokenBarrierException e) {
            return;
        }
    }

    public static void main(String[] args) {
        final int count = 10;
        CyclicBarrierDemo demo = new CyclicBarrierDemo(count);
        for (int i = 0; i < 100; i++) {
            demo.doWork(new Runnable() {
                public void run() {
                    // do something
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception e) {
                        return;
                    }
                }
            });

            System.out.println("=== i === : " + i);

            if ((i + 1) % count == 0) {
                /*
                 每10个线程一组，或者锁一个屏障点，当每组的10个线程，都完成时， 才执行下一组线程
                 */
                demo.waitForNext();
            }
        }
    }
}
