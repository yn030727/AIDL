package com.example.ipc_aidl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {
    //1.定义了一个Handler类来切换线程
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteBookManager;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:{
                    Log.d("Ning","receive new book :"+ msg.obj);
                    break;
                }
                default:{
                    super.handleMessage(msg);
                }
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IBookManager iBookManager = IBookManager.Stub.asInterface(iBinder);

            try {
                mRemoteBookManager = iBookManager;

                List<Book> bookList = iBookManager.getBookList();
                Book newBook = new Book(3,"艺术开发探索");
                iBookManager.addBook(newBook);
                Log.d("Ning","add book:"+ newBook);
                List<Book> newList = iBookManager.getBookList();

                //注册观察者
                iBookManager.registerListener(mIOnNewBookArrivedListener);

                System.out.println("list type : " + bookList.getClass().getCanonicalName());
                System.out.println("list : " + bookList.get(2).bookName+" " + bookList.get(2).bookId);
                Log.d("Ning", "list type : " + bookList.getClass().getCanonicalName());
                Log.d("Ning","list : " + bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteBookManager = null;
        }
    };

    //实现IOnNewBookArrivedListener接口对象
    private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        //实现新书到了的方法
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    } ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }


    //取消观察者的注册
    @Override
    protected void onDestroy() {
        if(mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()){
            try {
                mRemoteBookManager.unregisterListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}