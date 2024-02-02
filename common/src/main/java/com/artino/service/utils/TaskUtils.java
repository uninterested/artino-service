package com.artino.service.utils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class TaskUtils {
    private TaskUtils() {}

    /**
     * 操作延迟默认1000毫秒
     */
    private int EXECUTE_DELAY_TIME = 1000;
    /**
     * 异步操作任务调度线程池
     */
    private static ScheduledExecutorService executor;

    static {
        TaskUtils.executor = SpringUtils.getBean("scheduledExecutorService");
    }

    /**
     * 静态方法构造
     * @return
     */
    public static TaskUtils beginTask() {
        return beginTask(1000);
    }

    /**
     * 静态方法构造
     * @param delay 延时执行(毫秒)
     * @return
     */
    public static TaskUtils beginTask(int delay) {
        TaskUtils task = new TaskUtils();
        task.EXECUTE_DELAY_TIME = delay;
        return task;
    }

    /**
     * 执行任务
     * @param task
     */
    public void start(TimerTask task) {
        executor.schedule(task, EXECUTE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * task取消执行
     */
    public void cancel() {
        if (executor != null && !executor.isShutdown())
        {
            executor.shutdown();
            try
            {
                if (!executor.awaitTermination(120, TimeUnit.SECONDS))
                    executor.shutdownNow();
            }
            catch (InterruptedException ie)
            {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}