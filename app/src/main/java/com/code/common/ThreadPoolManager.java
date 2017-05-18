package com.code.common;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ansen on 2015/8/10.
 *
 * @E-mail: ansen360@126.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: CodeRepository
 * @PACKAGE_NAME: com.tomorrow_p.common
 * @Description: TODO
 */
public class ThreadPoolManager {
    private static ExecutorService mFixedThread;
    private static ExecutorService mSingleThread;
    private static ExecutorService mCachedThread;
    private static ExecutorService mScheduledThread;
    private ThreadPoolExecutor mThreadPoolExecutor;


    private static final int CORE_POOL_SIZE = 5;

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。Runtime.getRuntime().availableProcessors();
     */
    public static ExecutorService getThreadPool() {
        if (mFixedThread == null) {
            synchronized (ThreadPoolManager.class) {
                if (mFixedThread == null) {
                    mFixedThread = Executors.newFixedThreadPool(CORE_POOL_SIZE);
                }
            }
        }
        return mFixedThread;
    }

    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     */
    public static ExecutorService getSingleThread() {
        if (mSingleThread == null) {
            synchronized (ThreadPoolManager.class) {
                if (mSingleThread == null) {
                    mSingleThread = Executors.newSingleThreadExecutor();
                }
            }
        }
        return mSingleThread;
    }

    /**
     * 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
     */
    public static ExecutorService getCacheThread() {
        if (mCachedThread == null) {
            synchronized (ThreadPoolManager.class) {
                if (mCachedThread == null) {
                    mCachedThread = Executors.newCachedThreadPool();
                }
            }
        }
        return mCachedThread;
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行
     */
    public static ExecutorService getScheduledThread() {
        if (mScheduledThread == null) {
            synchronized (ThreadPoolManager.class) {
                if (mScheduledThread == null) {
                    mScheduledThread = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
                }
            }
        }
        return mScheduledThread;
    }

    /**
     * @param corePoolSize    核心的线程数
     * @param maximumPoolSize 最大的线程数
     * @param keepAliveTime   保持存活的时间
     * @return 自定义线程池
     */
    public ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        if (mThreadPoolExecutor == null) {
            synchronized (ThreadPoolManager.class) {
                if (mThreadPoolExecutor == null) {
                    TimeUnit unit = TimeUnit.MILLISECONDS;// 保持时间对应的单位
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// 无界队列; 缓存队列/阻塞队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();// 线程工厂
                    // 异常捕获器
//                        RejectedExecutionHandler handler1 = new ThreadPoolExecutor.AbortPolicy();//队列中加不进去时，抛出异常 RejectedExecutionException
//                        RejectedExecutionHandler handler2 = new ThreadPoolExecutor.CallerRunsPolicy();//队列中加不进去时，直接在当前线程中执行
//                        RejectedExecutionHandler handler3 = new ThreadPoolExecutor.DiscardOldestPolicy();//队列中加不进去时,移除队列中的第一个，将任务加到队列的中
                    RejectedExecutionHandler handler4 = new ThreadPoolExecutor.DiscardPolicy();// 队列中加不进去时,不处理
                    mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler4);
                }
            }
        }
        return mThreadPoolExecutor;
    }

    /**
     * 关闭线程池后判断所有任务是否都已完成
     */
    public boolean isTerminated() {
//        return mExecutorService.isTerminated();
        return false;
    }

    /**
     * 请求关闭、发生超时或者当前线程中断
     * <p>无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。</p>
     *
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return {@code true}: 请求成功<br>{@code false}: 请求超时
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
//        return mExecutorService.awaitTermination(timeout, unit);
        return false;
    }

    /**
     * 提交一个Callable任务用于执行
     * <p>如果想立即阻塞任务的等待，则可以使用{@code result = exec.submit(aCallable).get();}形式的构造。</p>
     *
     * @param task 任务
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(Callable<T> task) {
//        return mExecutorService.submit(task);
        return null;
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task   任务
     * @param result 返回的结果
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(Runnable task, T result) {
//        return mExecutorService.submit(task, result);
        return null;
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task 任务
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回null结果。
     */
    public Future<?> submit(Runnable task) {
//        return mExecutorService.submit(task);
        return null;
    }

    /**
     * 执行给定的任务
     * <p>当所有任务完成时，返回保持任务状态和结果的Future列表。
     * 返回列表的所有元素的{@link Future#isDone}为{@code true}。
     * 注意，可以正常地或通过抛出异常来终止已完成任务。
     * 如果正在进行此操作时修改了给定的 collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks 任务集合
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同，每个任务都已完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务。
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
//        return mExecutorService.invokeAll(tasks);
        return null;
    }

    /**
     * 执行给定的任务
     * <p>当所有任务完成或超时期满时(无论哪个首先发生)，返回保持任务状态和结果的Future列表。
     * 返回列表的所有元素的{@link Future#isDone}为{@code true}。
     * 一旦返回后，即取消尚未完成的任务。
     * 注意，可以正常地或通过抛出异常来终止已完成任务。
     * 如果此操作正在进行时修改了给定的 collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同。如果操作未超时，则已完成所有任务。如果确实超时了，则某些任务尚未完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
            InterruptedException {
//        return mExecutorService.invokeAll(tasks, timeout, unit);
        return null;
    }

    /**
     * 执行给定的任务
     * <p>如果某个任务已成功完成（也就是未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务。
     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks 任务集合
     * @return 某个任务返回的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
//        return mExecutorService.invokeAny(tasks);
        return null;
    }

    /**
     * 执行给定的任务
     * <p>如果在给定的超时期满前某个任务已成功完成（也就是未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务。
     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return 某个任务返回的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
     * @throws TimeoutException     如果在所有任务成功完成之前给定的超时期满
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
            InterruptedException, ExecutionException, TimeoutException {
//        return mExecutorService.invokeAny(tasks, timeout, unit);
        return null;
    }

    /**
     * 延迟执行Runnable命令
     *
     * @param command 命令
     * @param delay   延迟时间
     * @param unit    单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在完成后将返回{@code null}
     */
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
//        return mScheduledThreadPool.schedule(command, delay, unit);
        return null;
    }

    /**
     * 延迟执行Callable命令
     *
     * @param callable 命令
     * @param delay    延迟时间
     * @param unit     时间单位
     * @return 可用于提取结果或取消的ScheduledFuture
     */
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
//        return mScheduledThreadPool.schedule(callable, delay, unit);
        return null;
    }

    /**
     * 延迟并循环执行命令
     *
     * @param command      命令
     * @param initialDelay 首次执行的延迟时间
     * @param period       连续执行之间的周期
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在取消后将抛出异常
     */
    public ScheduledFuture<?> scheduleWithFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
//        return mScheduledThreadPool.scheduleAtFixedRate(command, initialDelay, period, unit);
        return null;
    }

    /**
     * 延迟并以固定休息时间循环执行命令
     *
     * @param command      命令
     * @param initialDelay 首次执行的延迟时间
     * @param delay        每一次执行终止和下一次执行开始之间的延迟
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在取消后将抛出异常
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
//        return mScheduledThreadPool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        return null;
    }
}
