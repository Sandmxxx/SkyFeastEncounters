package com.sky.context;

public class BaseContext {

    // 线程变量隔离
    //ThreadLocal变量的真正含义是每个线程都会持有这个变量的一个副本，而不是共享同一个实例。
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
