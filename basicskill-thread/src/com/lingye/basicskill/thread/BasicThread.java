package com.lingye.basicskill.thread;

import java.util.concurrent.Callable;
/**
 * java.lang.Runnable 接口代表一个线程任务,我们启动的每一个线程都是针对一个具体任务，即这个线程去做什么。
 * 实现 Runnable 就是要覆写其中的 run 方法，这个任务具体做什么的逻辑就是在run中指定的。
 * 
 * @author chao03.li
 * @date 2018年2月27日
 */
public class BasicThread {

    // main是主线程
    public static void main(String[] args) {
        System.out.println("Main 主线程开始执行");
        //实现Runnable接口的实现类的实例对象作为Thread构造函数的target
        ImplRunnableMethod implRunnable = new ImplRunnableMethod();
        Thread thread1=new Thread(implRunnable,"RunnableThread");
        Thread thread2=new ExtendThreadMethod();
        thread1.start();
        thread2.start();
        try {
            // 模拟主线程处理逻辑
            Thread.sleep(2000);
        } catch (Exception e) {
            
        }
        System.out.println("Main 主线程执行完毕");
    }
}
/** 方式1：继承Thread类（Thread实现了Runnable接口）的线程实现方式如下，这种方式【没有返回结果】 **/
class ExtendThreadMethod extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"继承Thread类来实现多线程 run()");
    }
} 
/** 方式2：通过实现Runnable接口，实现run方法，这种方式【没有返回结果】 **/
class ImplRunnableMethod implements Runnable{
    // 实现Runnable接口的实现类的实例对象作为Thread构造函数的target
    @Override
    public void run() {
        System.out.println("线程开始执行自己的业务逻辑");
        System.out.println(Thread.currentThread().getName()+"通过实现Runnable接口来实现多线程 run()");
        try {
            // 模拟执行耗时2s的业务逻辑
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("线程执行自己的业务逻辑完毕");
    }
}
/** 方式3：通过Callable和FutureTask创建线程 这种方式是【有返回结果的】 **/
class ImplCallable implements Callable<Object>{
    // Step1.创建Callable接口的实现类 ，并实现Call方法 
    // Step2.创建Callable的实例对象，使用FutureTask类包装Callable对象，该FutureTask对象封装了Callable对象的Call方法的返回值
    // Step3.使用FutureTask对象作为Thread对象的target创建并启动线程
    // Step4.调用FutureTask对象的get()来获取子线程执行结束的返回值
    @Override
    public Object call() throws Exception {
        System.out.println(Thread.currentThread().getName()+"通过实现Callable接口来实现多线程 call()");
        return null;
    }
    
}
/** 方式4：通过线程池创建线程 **/
class ThreadPool{
    
}