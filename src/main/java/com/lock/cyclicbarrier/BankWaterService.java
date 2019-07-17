package com.lock.cyclicbarrier;

import org.apache.commons.lang3.RandomUtils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * CyclicBarrier可以用于多线程计算数据，最后合并计算结果的场景。例如，用一个Excel保
 * 存了用户所有银行流水，每个Sheet保存一个账户近一年的每笔银行流水，现在需要统计用户
 * 的日均银行流水，先用多线程处理每个sheet里的银行流水，都执行完之后，得到每个sheet的日
 * 均银行流水，最后，再用barrierAction用这些线程的计算结果，计算出整个Excel的日均银行流水。
 */
public class BankWaterService implements Runnable {

    /**
     * sheet的个数
     */
    private final static int SHEET_COUNT = 4;

    /**
     * 创建4个线程的屏障，处理完之后执行当前类的run方法
     */
    private CyclicBarrier barrier = new CyclicBarrier(4, this);

    /**
     * 假设只有SHEET_COUNT个sheet，所以只启动SHEET_COUNT个线程
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(SHEET_COUNT);

    /**
     * 保存每个sheet计算出的银行流水结果
     */
    private Map<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

    public void count() {
        for (int i = 0; i < SHEET_COUNT; i++) {
            executorService.execute(() -> {
                // 计算当前sheet的银流数据
                int result = RandomUtils.nextInt(10000, 99999);
                // 将计算结果放入map
                sheetBankWaterCount.put(Thread.currentThread().getName(), result);
                System.out.println("=== " + Thread.currentThread().getName() + " count, result : " + result + " ===");

                // 银流计算完成，插入一个屏障
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 所有线程都到达时，最后一个到达的线程触发Runnable运行
     */
    @Override
    public void run() {
        System.out.println("=== " + Thread.currentThread().getName() + " run ===");
        int result = 0;

        // 汇总计算的结果
        for (Map.Entry<String, Integer> map : sheetBankWaterCount.entrySet()) {
            result += map.getValue();
        }

        System.out.println("=== result : " + result + " ===");
        executorService.shutdown();
    }

    public static void main(String[] args) {
        BankWaterService service = new BankWaterService();
        service.count();
    }
}
