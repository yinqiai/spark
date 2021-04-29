package Serialization;

import java.io.Serializable;

/**
 * @author yinqi
 * @date 2021/4/29
 */
public class SimpleTask extends Task {
    @Override
    public void run() {
        System.out.println("run simple task!");
    }

}