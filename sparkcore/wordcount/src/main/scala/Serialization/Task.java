package Serialization;

import java.io.Serializable;

/**
 * @author yinqi
 * @date 2021/4/29
 */
public abstract class Task implements Serializable {
    public void run() {
        System.out.println("run task!");
    }
}