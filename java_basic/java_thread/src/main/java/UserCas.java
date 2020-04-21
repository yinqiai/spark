
public class UserCas {
    static enum ReadyRun{T1,T2};
    static volatile ReadyRun  r1=  ReadyRun.T1;

    public static void main(String[] args) {

        int[] i = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        new Thread(()->{
            for(Integer i1:i) {
                while (r1 == ReadyRun.T2) {
                };
                System.out.print(i1);
                r1= ReadyRun.T2;
            }
        },"t1").start();

        new Thread(()->{
            for(char c1:c) {
                while (r1 == ReadyRun.T1) {
                };
                System.out.print(c1);
                r1 = ReadyRun.T1;

            }
        },"t2").start();
    }
}
