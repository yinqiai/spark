
public class Sync_notify_wait {

    final static Object o = new Object();

    public static void main(String[] args) {

        int[] i = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        new Thread(() -> {
            synchronized (o) {
                for (Integer i1 : i) {

                    System.out.print(i1);

                    try {
                        o.notify();
                        o.wait();//出让锁（只有执行了这个代码，才真正让出了锁（sleep也是睡眠但是不让出锁），这时候下一个线程才有可能拿到锁，虽然notify代码写在前面，但是notify以后，另外一个线程还没拿到锁，所以还是执行不了程序的） 这里面wait 和notify不能互换位置，因为wait等于是让睡了一样，就不能够去唤醒别人
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                o.notify();//必须，否则无法停止程序，因为最后不用这个总有一个线程会卡在wait哪里，因为睡了，不能停止线程，程序当然无法终止
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (o) {
                for (char c1 : c) {

                    System.out.print(c1);



                    try {
                        o.notify();
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                o.notify();
            }
        }, "t2").start();

    }
}
