package com.tomorrow_p.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*  java线程池:
    newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
    newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。Runtime.getRuntime().availableProcessors();
    newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
    newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 */
public class ThreadPoolManager {
    private ExecutorService service;
    private static ThreadPoolProxy mNormalPool;//得到一个普通的线程池
    private static ThreadPoolProxy mDownLoadPool;//得到一个下载的线程池
    private static ExecutorService mSingleThread;//得到一个单一线程池

    public static ExecutorService getSingleThread() {
        if (mSingleThread == null) {
            synchronized (ExecutorService.class) {
                if (mSingleThread == null)
                    mSingleThread = Executors.newSingleThreadExecutor();
            }
        }
        return mSingleThread;
    }

    public static ThreadPoolProxy getNormalPool() {
        if (mNormalPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mNormalPool == null)
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
            }
        }
        return mNormalPool;
    }

    public static ThreadPoolProxy getDownLoadPool() {
        if (mDownLoadPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mDownLoadPool == null)
                    mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
            }
        }
        return mDownLoadPool;
    }

    static class ThreadPoolProxy {
        private ThreadPoolExecutor mThreadPoolExecutor;
        private int mCorePoolSize;// 核心的线程数
        private int mMaximumPoolSize;// 最大的线程数
        private long mKeepAliveTime;// 保持存活的时间

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            mCorePoolSize = corePoolSize;
            mMaximumPoolSize = maximumPoolSize;
            mKeepAliveTime = keepAliveTime;
        }

        private ThreadPoolExecutor initThreadPoolExecutor() {//双重检查加锁
            if (mThreadPoolExecutor == null) {
                synchronized (ThreadPoolProxy.class) {
                    if (mThreadPoolExecutor == null) {
                        TimeUnit unit = TimeUnit.MILLISECONDS;// 保持时间对应的单位
                        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// 无界队列; 缓存队列/阻塞队列
                        ThreadFactory threadFactory = Executors.defaultThreadFactory();// 线程工厂
                        // 异常捕获器
//                        RejectedExecutionHandler handler1 = new ThreadPoolExecutor.AbortPolicy();//队列中加不进去时，抛出异常 RejectedExecutionException
//                        RejectedExecutionHandler handler2 = new ThreadPoolExecutor.CallerRunsPolicy();//队列中加不进去时，直接在当前线程中执行
//                        RejectedExecutionHandler handler3 = new ThreadPoolExecutor.DiscardOldestPolicy();//队列中加不进去时,移除队列中的第一个，将任务加到队列的中
                        RejectedExecutionHandler handler4 = new ThreadPoolExecutor.DiscardPolicy();// 队列中加不进去时,不处理
                        mThreadPoolExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, mKeepAliveTime, unit, workQueue, threadFactory, handler4);
                    }
                }
            }
            return mThreadPoolExecutor;
        }

        public void execute(Runnable task) {
            if (task == null)
                return;
            initThreadPoolExecutor();
            mThreadPoolExecutor.execute(task);
        }

        public Future<?> submit(Runnable task) {
            if (task == null)
                return null;
            initThreadPoolExecutor();
            return mThreadPoolExecutor.submit(task);
        }

        public void removeTask(Runnable task) {
            if (task == null)
                return;
            initThreadPoolExecutor();
            mThreadPoolExecutor.remove(task);
        }
    }
}
