import java.util.concurrent.locks.LockSupport;

public class JdkLockSupport {
    static Thread t1, t2 = null;

    public static void main(String[] args) {
        int[] i = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        t1 = new Thread(() -> {
            for (Integer i1 : i) {
                System.out.print(i1);
                LockSupport.unpark(t2);
                LockSupport.park();
            }
        }, "t1");
        t2 = new Thread(() -> {
            for (char c1 : c) {
                LockSupport.park();
                System.out.print(c1);
                LockSupport.unpark(t1);
            }
        }, "t1");


        t1.start();
        t2.start();


    }
}
