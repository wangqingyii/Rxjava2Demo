###RxJava入门
 #定义
  Rxjava是一个在Java VM 上使用可观测的序列来组成一步的、基于事件的程序的库
  总结：Rxjava是一个基于事件流、实现异步操作的库
  作用：实现异步操作
  特点：
  由于RxJava的使用方式是：基于事件流的链式调用，所以使得RxJava：
  1.逻辑简洁
  2.实现优雅
  3.使用简单
  更重要的是，随着程序逻辑的复杂性提高，它依然能够保持简洁 & 优雅
  
 #原理
  RxJava原理基于一种扩展的观察者模式
  RxJava的扩展模式中有四种角色：
  ————————————————————————————————————————————————————
 |        角色           |            作用             |
  ————————————————————————————————————————————————————
 |  被观察者(Observable)  |          产生事件           |
 |  订阅(Subscribe)       |    连接被观察者 & 观察者     |
 |  观察者(Observer)      |   接收事件，并给出响应动作    |
 |  事件(Event)           |  被观察者 & 观察者沟通的载体  |
  ————————————————————————————————————————————————————
  
  #总结：被观察者(Observable)通过订阅(Subscribe)按顺序发送事件给观察者(Observer)按顺序接收事件(Event)&做出对应的响应动作。
  
 ##基本使用
  使用步骤：
  #方式一分步骤实现：
  步骤1：创建被观察者(Observable) & 生产事件
   (1).创建被观察者Observable对象
       还有其他方法用于创建观察者(Observable)对象
       a.Observable.just(T...)：直接将传入的参数依次发送出来
       b.Observable.from(T[]) / from(Iterable<? extends T>) : 将传入的数组 / Iterable 拆分成具体对象后，依次发送出来
   (2).在复写的subscribe()方法里定义需要发送的事件

  步骤2：创建观察者(Observer)并定义响应事件行为
   发生的事件类型包括：Next事件、Complete事件和Error事件 详情看图：[icon_event.png]
   具体实现：
   方式一：采用Observer接口
   方式二：采用Subscriber抽象类，说明：Subscriber类 = RxJava 内置的一个实现了 Observer 的抽象类，对 Observer 接口进行了扩展
  #注意：两种方式的区别：即Subscriber 抽象类与Observer 接口的区别
  相同点：二者基本使用方式完全一致（实质上，在RxJava的 subscribe 过程中，Observer总是会先被转换成Subscriber再使用）
  不同点：Subscriber抽象类对 Observer 接口进行了扩展，新增了两个方法：
   1.onStart()：在还未响应事件前调用，用于做一些初始化工作
   2.unsubscribe()：用于取消订阅。在该方法被调用后，观察者将不再接收 & 响应事件；调用该方法前，先使用isUnsubscribed()
  判断状态确定被观察者Observable是否还持有观察者Subscriber的引用，如果引用不能及时释放，就会出现内存泄露

步骤三：通过订阅(Subscribe)链接观察者和被观察者
  方式一：observable.subscribe(observer)或者observable.subscribe(subscriber)
  方式二：基于事件流的链式调用
  上述的实现方式时为了说明RxJava的原理和使用
  在实际应用中，会将上述步骤和代码连在一起，从而更加简洁、更加优雅，即RxJava基于事件流的链式调用

 #特别注意:RxJava提供了多个函数式接口，用于实现简便式的观察者模式[icon_interface.png]
 以Consumer为例：实现简便式的观察者模式
     Observable.just("hello").subscribe(new Consumer<String>() {
     // 每次接收到Observable的事件都会调用Consumer.accept（）
     @Override
     public void accept(String s) throws Exception {
     System.out.println(s);
     }
     });

 #观察者Observer的subscribe()的重载方法：
    // 表示观察者不对被观察者发送的事件作出任何响应（但被观察者还是可以继续发送事件）
    public final Disposable subscribe() {}
    // 表示观察者只对被观察者发送的Next事件作出响应
    public final Disposable subscribe(Consumer<? super T> onNext) {}
    // 表示观察者只对被观察者发送的Next事件 & Error事件作出响应
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {}
    // 表示观察者只对被观察者发送的Next事件、Error事件 & Complete事件作出响应
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
    // 表示观察者只对被观察者发送的Next事件、Error事件、Complete事件 & onSubscribe事件作出响应
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
    // 表示观察者对被观察者发送的任何事件都作出响应
    public final void subscribe(Observer<? super T> observer) {}

