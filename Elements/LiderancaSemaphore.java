package Elements;

import java.util.concurrent.Semaphore;

public class LiderancaSemaphore {
    private static LiderancaSemaphore instance;
    private Semaphore semaphore;

    private LiderancaSemaphore() {
        semaphore = new Semaphore(1);
    }

    public static LiderancaSemaphore getInstance() {
        if (instance == null) {
            synchronized (LiderancaSemaphore.class) {
                if (instance == null) {
                    instance = new LiderancaSemaphore();
                }
            }
        }
        return instance;
    }

    public void adquirir() throws InterruptedException {
        semaphore.acquire();
    }

    public void liberar() {
        semaphore.release();
    }
}

