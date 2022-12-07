package com.example.ipc_aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


//在Service中实现这些接口
public class BookManagerService extends Service {

    //这个集合支持并发读\写
    //前面我们提到AIDL是运行在Binder线程池中的
    //所以多个客户端同时连接的时候会存在多个线程访问的情形
    //所以我们用CopyOnWriteArrayList来进行自动的线程同步
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    //在服务中通过Binder类实现AIDL中定义的接口
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"IOS"));
    }

    //构造方法
    public BookManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}