package pt.vow.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExtraInfoApp {
    // Create a Executor Service with a fixed number of threads
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
