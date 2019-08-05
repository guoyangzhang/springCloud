package com.zhang.tread.controller;


/**
 * 继承thread类实现,重写run方法
 * 1.继承Thread类，重写run方法
 * 2.实现Runnable接口，重写run方法，实现Runnable接口的实现类的实例对象作为Thread构造函数的target
 * 3.通过Callable和FutureTask创建线程
 *
 * @Author: Mr.ZHANG
 * @Date: 2019/1/16  下午 4:16
 */

public class TreadController extends Thread {

    @Override
    public void run() {
        int count = 0;
//        while (true) {
        while (count / 10 == 0) {
            count++;
            System.out.println(Thread.currentThread().getName() + " is running ... " + count); // 打印当前线程的名字
            try {
                Thread.sleep(1000); // 休息1000ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 指定线程名称的构造方法
     *
     * @param name
     */
    public TreadController(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TreadController td = new TreadController("first");
        TreadController td1 = new TreadController("second");
        int count = 0;
        td.start(); // 启动线程
        td1.start();
        while (count / 10 == 0) {
            count++;
            System.out.println(count); // 打印当前线程的名字
            try {
                Thread.sleep(1000); // 休息1000ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
