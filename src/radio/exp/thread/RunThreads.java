package radio.exp.thread;

/**
 * @author wzy
 */
public class RunThreads implements Runnable {
    public static void main(String[] args) {
        Runnable runner = new RunThreads();
        Thread alpha = new Thread(runner);
        alpha.setName("Alpha thread");
        Thread beta = new Thread(runner);
        beta.setName("Beta thread");
        alpha.start();
        beta.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 25; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " is running");
        }
    }
}
