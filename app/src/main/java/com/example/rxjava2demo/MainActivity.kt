package com.example.rxjava2demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "RxJava"
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 方式一：分步骤实现
        methodOne()
        // 方式二：基于事件流的链式调用方式
        methodTow()
        // 运行结果：
        // com.example.rxjava2demo D/RxJava: 开始采用subscribe连接
        // com.example.rxjava2demo D/RxJava: 对Next事件作出响应 1
        // com.example.rxjava2demo D/RxJava: 对Next事件作出响应 2
        // com.example.rxjava2demo D/RxJava: 对Next事件作出响应 3
        // com.example.rxjava2demo D/RxJava: 对Next事件作出响应 4
        // com.example.rxjava2demo D/RxJava: 对Complete事件作出响应
        // com.example.rxjava2demo D/RxJava: 开始采用subscribe连接

        // Disposable.dispose()方法示例,方法作用：切断观察者与被观察者之间的连接
        disposeTest()
        // 运行结果：


    }

    /**
     * 方式一：分步骤实现
     */
    private fun methodOne() {
        /**
         * 步骤一：创建被观察者Observable和生产事件
         * 即：顾客进入饭店 - 坐下餐桌 - 点菜
         */
        // 1.创建被观察者 Observable 对象
        val observable = Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                // 通过ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍：
                // 1.定义：事件发射器
                // 2.作用：定义需要发送的事件和像观察者发送事件
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
                emitter.onNext(4)
                // 发出完成信号
                emitter.onComplete()
            }
        })

        /**
         * 步骤二：创建观察者Observer并定义响应事件行为
         * 即：开厨房 - 确定对应菜式
         */
        val observer = object : Observer<Int> {
            /**
             * 观察者接收事件前，默认最先调用复写onSubscribe()
             */
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "开始采用subscribe连接")
            }

            /**
             * 当被观察者生产Next事件和观察者接收到时，会调用该复写方法进行响应
             */
            override fun onNext(t: Int) {
                Log.d(TAG, "对Next事件作出响应 $t")
            }

            /**
             * 当被观察者生产Error事件和观察者接收到时，会调用该复写方法进行响应
             */
            override fun onError(e: Throwable) {
                Log.d(TAG, "对Error事件作出响应")
            }

            /**
             * 当被观察者生产Complete事件和观察者接收到时，会调用该复写方法进行响应
             */
            override fun onComplete() {
                Log.d(TAG, "对Complete事件作出响应")
            }
        }
        // 步骤三：通过订阅(Subscribe)连接观察者和被观察者
        // 即：顾客找到服务员
        observable.subscribe(observer)
    }

    /**
     * 方式二：基于事件流的链式调动
     */
    private fun methodTow() {
        Observable.create(object : ObservableOnSubscribe<Int> {
            // 1.创建被观察者和生产事件
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
                emitter.onNext(4)
                // 发出完成信号
                emitter.onComplete()
            }
        }).subscribe(object : Observer<Int> {
            // 2. 通过通过订阅(subscribe)连接观察者和被观察者
            // 3. 创建观察者和定义响应事件的行为
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "开始采用subscribe连接")
            }

            override fun onNext(t: Int) {
                Log.d(TAG, "对Next事件作出响应 $t")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "对Error事件作出响应")
            }

            override fun onComplete() {
                Log.d(TAG, "对Complete事件作出响应")
            }
        })
    }

    /**
     * Disposable.dispose()方法示例
     * 方法作用：切断观察者与被观察者之间的连接
     */
    private fun disposeTest() {
        // 主要在观察者 Observer中 实现
         object : Observer<Int> {
            // 1. 定义Disposable类变量
            private var disposable: Disposable? = null
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "开始采用subscribe连接")
                // 2. 对Disposable类变量赋值
                disposable = d
            }

            override fun onNext(value: Int) {
                Log.d(TAG, "对Next事件" + value + "作出响应")
                if (value == 2) {
                    // 设置在接收到第二个事件后切断观察者和被观察者的连接
                    disposable!!.dispose()
                    Log.d(TAG, "已经切断了连接：" + disposable!!.isDisposed)
                }
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "对Error事件作出响应")
            }

            override fun onComplete() {
                Log.d(TAG, "对Complete事件作出响应")
            }
        }
    }
}