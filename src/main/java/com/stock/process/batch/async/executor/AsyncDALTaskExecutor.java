package com.stock.process.batch.async.executor;

import com.stock.process.util.ExceptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * @author Nabeel Ahmed
 */
public class AsyncDALTaskExecutor {

    public static Logger logger = LogManager.getLogger(AsyncDALTaskExecutor.class);

    private static ThreadPoolExecutor threadPool;
    private static LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);

    /**
     * Method use to add task
     * @param task
     * */
    public static void addTask(Runnable task) {
        try {
            logger.debug("Submitting Task of type : {}", task.getClass().getCanonicalName());
            threadPool.submit(task);
        } catch (RejectedExecutionException ex) {
            logger.error("Failed to submit Task in queue {}", ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    /**
     * Method use to create async dal task
     * @param corePoolSize,
     * @param maximumPoolSize
     * @param keepAliveTime
     * */
    public AsyncDALTaskExecutor(Integer corePoolSize, Integer maximumPoolSize, Integer keepAliveTime) {
        logger.info(">============AsyncDALTaskExecutor Start Successful============<");
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue);
        threadPool.setRejectedExecutionHandler((task, executor) -> {
            logger.error("Task Rejected : {}", task.getClass().getCanonicalName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                logger.error("DAL Task Interrupted {}", ExceptionUtil.getRootCauseMessage(ex));
            }
            // if task reject then add same take for execution again
            executor.execute(task);
        });
        // scheduler use to check how man thread are active and other pool size detail
        (new Timer())
            .schedule(new TimerTask() {
                @Override public void run() {
                logger.info("AsyncDAL Active No Threads: {} Core no of Threads: {} Current no of threads: {} Current Worker Queue Size: {} Max allowed Threads: {}",
                    threadPool.getActiveCount(), threadPool.getCorePoolSize(), threadPool.getPoolSize(), workQueue.size(), threadPool.getMaximumPoolSize());
                }
            }, 5 * 60 * 1000, 60000);
        logger.info(">============AsyncDALTaskExecutor End Successful============<");
    }

}
