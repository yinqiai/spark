package com.transsnet.jvm;

import java.util.concurrent.ConcurrentHashMap;

public class TestObject {

    public static void main(String[] args) {
        T t = new T();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

    }

    static  class T{
        int i=1;
    }
}
