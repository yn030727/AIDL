package com.example.ipc_aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


//在Service中实现这些接口
public class BookManagerService extends Service {


    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    //存放书本的集合
    //存放对书感兴趣的人的集合
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    private CopyOnWriteArrayList<IOnNewBookArrivedListener> listenerList = new CopyOnWriteArrayList<IOnNewBookArrivedListener>();

    //在服务中通过Binder类实现AIDL中定义的接口
    private Binder mBinder = new IBookManager.Stub(){
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
        //如果当前书友实现了这个接口
        //但是还没有被添加到观察者集合当中
        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (!listenerList.contains(listener)) {
                listenerList.add(listener);
            }else{
                Log.d("Ning","already exists.");
            }
        }
        //当前书友不想观察新书状态了
        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if(listenerList.contains(listener)){
                listenerList.remove(listener);
            }else{
                Log.d("Ning","not found");
            }
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"IOS"));
        new Thread(new ServiceWorker()).start();
    }


    //构造方法
    public BookManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //添加新书的方法
    private void onNewBookArrived(Book book) throws RemoteException{
        //将书添加到书本集合
        mBookList.add(book);
        for(int i = 0;i < listenerList.size() ; i++){
            //每一个观察者都观察这本新书
            IOnNewBookArrivedListener listener = listenerList.get(i);
            Log.d("Ning","onNewBookArrived , notify listener :"+listener);
            listener.onNewBookArrived(book);
        }
    }

    //每隔5秒创建一本新书
    //并且将书通过我们写的方法添加到书本集合中
    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while(!mIsServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size()+1;
                Book newBook = new Book(bookId,"new Book#"+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}