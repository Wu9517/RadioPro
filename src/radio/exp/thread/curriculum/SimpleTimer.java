package radio.exp.thread.curriculum;

/**
 * 简易的倒计时器
 *
 * @author wzy
 */
public class SimpleTimer {
    private static int count;

    public static void main(String[] args) {
        count = args.length > 1 ? Integer.valueOf(args[0]) : 60;
        int remaining;
        while (true) {
            remaining = countDown();
            if (remaining == 0) {
                break;
            } else {
                System.out.println("Remaining " + count + " secound(s)");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done.");
    }

    private static int countDown() {
        return count--;
    }
}
