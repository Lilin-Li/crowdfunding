package inc.lilin.crowd.common.api;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// How to use:
// @Bean 裝配 CurrentTimeMillisClock.getInstance();
// 之後以此指標 call now() 取得時間。
public class CurrentTimeMillisClock {
    private volatile long now;

    private CurrentTimeMillisClock() {
        this.now = System.currentTimeMillis();
        scheduleTick();
    }

    private void scheduleTick() {
        new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "current-time-millis");
            thread.setDaemon(true); // Java 中的守護線程不會阻止 JVM 退出。具體來說，當只剩下守護線程時，JVM 將退出。
            return thread;
        }).scheduleAtFixedRate(() -> {
            now = System.currentTimeMillis();
        }, 1, 1, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return now;
    }

    public static CurrentTimeMillisClock getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
        private static final CurrentTimeMillisClock INSTANCE = new CurrentTimeMillisClock();
    }
}
