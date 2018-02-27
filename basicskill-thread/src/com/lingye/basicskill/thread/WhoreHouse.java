package com.lingye.basicskill.thread;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 线程操作
 * run():实现Runnable方法时重写run(),线程启动会jvm执行就是执行run里面的逻辑
 * sleep():Thread类中的静态方法，使目前正在执行的线程对象休眠?毫秒，当前线程进入了睡眠状态。
 * （1）首先，调用sleep()之后，会引起当前执行的线程进入暂时中断状态，也即睡眠状态。
   （2）其次，虽然当前线程进入了睡眠状态，但是依然持有monitor对象。
   （3）在中断完成之后，自动进入唤醒状态从而继续执行代码。
 * 监视器monitor:Java中的每个对象都有一个监视器，来监测【并发代码的重入】
 *              在非多线程编码时该监视器不发挥作用，反之如果在synchronized 范围内，监视器发挥作用。
 *              wait/notify必须存在于synchronized块中。并且，这三个关键字针对的是同一个监视器（某对象的监视器）。
 *              这意味着wait之后，其他线程可以进入同步块执行。
 *              即一个对象中存在共享资源及公共数据，如个人账户对象，不能多线程并发操作这个对象，只能一个一个的操作。那么就需要用到监视器了。
 *              monitor可以理解为监视权操作权，谁拥有监视权？是一个线程。
 *              每个对象都有唯一的一个monitor，对象可以理解为女人，对象一般情况下（单线程）如和男人去约会，不需要有monit9or的变化。
 *              但是如果在多线程的情况下，如和多个男人同时去约会就会造成混乱，那么就需要有监视权的概念，每次只能有一个男人（线程）去监视享受这个女人。
 *              其他男人在这位男人享受期间，只能在大厅乖乖等着。
 *              sleep():一个线程（男人）在约会期间上了内急，上了一个大号花了2个小时，但是这并不影响，那位女士会等他上完大号继续执行后续的约会步骤（代码逻辑）。
 *              wait():男人老婆打电话了，吓得男人赶快推到等候大厅接电话去了，但是老鸨不会等你，请下位男士进来进行服务。
 *              notify():第二个男人享受完了，告诉第一个男的，我弄完了，你进去把。
 * wait():（1）首先，调用了wait()之后会引起当前线程处于等待状状态。
          （2）其次，每个线程必须持有该对象的monitor。如果在当前线程中调用wait()方法之后，该线程就会释放monitor的持有对象并让自己处于等待状态。
          （3）如果想唤醒一个正在等待的线程，那么需要开启一个线程通过notify()或者notifyAll()方法去通知正在等待的线程获取monitor对象。如此，该线程即可打破等待的状态继续执行代码。
 * notify():通知正在等待的线程获取monitor对象。如此，该线程即可打破等待的状态继续执行代码。
 * 
 * @author chao03.li
 * @date 2018年2月27日
 */
public class WhoreHouse {
    // 妓院中拥有的妓女数量
    private static final int WHORE_COUNT=3;
    // 妓院最大容纳嫖客数量
    private static final int YHY_LIMIT_COUNT=5;
    // 妓女作为关键的共享资源，每次接客和等待都需要锁定
    private static Queue<PersonModel> whoreQueue = new LinkedList<PersonModel>();
    // 嫖客队列
    private static Queue<PersonModel> guestQueue = new LinkedList<PersonModel>();
    
    // 妓院类的构造方法初始化10位妓女
    public static void initWhores() {
        PersonModel whore=null;
        // 构造出10位妓女
        for(int i=0;i < WHORE_COUNT;i++) {
            whore=new PersonModel("霜雪飞儿"+(i+1));
            whoreQueue.add(whore);
        }
        System.out.println("已初始化"+WHORE_COUNT+"位妓女");
    }
    // 添加嫖客
    public synchronized void addWhoreMaster(String name) {
        if(guestQueue.size()>=YHY_LIMIT_COUNT) {
            try {
                // 超额等待
                System.out.println("【节假日需求旺盛】，请各位客官老爷在店外稍作等候！");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PersonModel p = new PersonModel(name);
        guestQueue.add(p);
        System.out.println("欢迎客官"+name+"，接客了姑娘们...");
        notifyAll();
    }
    // 妓女接客
    public synchronized void serviceForMaster() {
        if(whoreQueue.isEmpty()) {
            try {
                System.out.println("妈妈，我们正在【鏖战中】不信你听，啊！啊！！啊！！！");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if(guestQueue.size()==0) {
            try {
                wait();
                System.out.println("姑娘们先吃点东西，养足精神等着接客...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 安排队列首的妓女出来接客
        PersonModel pw = whoreQueue.poll();
        // 安排第一号客户享受服务
        PersonModel pm = guestQueue.poll();
        System.out.println("妈妈，我来了 请"+pm.getName()+"客官随我"+pw.getName()+"楼上包厢请");
        try {
            // 行事4s
            System.out.println("啊！客官用力，客官不要停噢😯！");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 给姑娘2s的喘息时间
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        whoreQueue.add(pw);
        notifyAll();
    }
    static class AddGuestThread extends Thread{
        private WhoreHouse th;
        private String guestName;
        public AddGuestThread(String name) {
            th = new WhoreHouse();
            guestName=name;
        }
        @Override
        public void run() {
            th.addWhoreMaster(guestName);
        }
    }
    static class GetGuestThread extends Thread{
        private WhoreHouse th ;
        public GetGuestThread() {
            th = new WhoreHouse();
        }
        @Override
        public void run() {
            th.serviceForMaster();
        }
    }
    
    public static void main(String[] args) {
    System.out.println("妓院现已开门");
    WhoreHouse.initWhores();
    // 模拟陆陆续续20个客人今天嫖娼
    for(int i=0;i<20;i++) {
        new AddGuestThread("屌丝"+ (i+1) +"号").start();
        try {
            // 每隔3s进店一名客人
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        new GetGuestThread().start();
    }
    }
}
class PersonModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonModel(String name) {
        this.name = name;
    }
    
    
}
