package com.example.ipc_aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //将服务器返回的Binder对象转换成AIDL接口
            IBookManager iBookManager = IBookManager.Stub.asInterface(iBinder);

            try {
                //通过IBookManager去调用getBookList方法
                List<Book> bookList = iBookManager.getBookList();
                Book newBook = new Book(3,"艺术开发探索");
                iBookManager.addBook(newBook);
                Log.d("Ning","add book:"+ newBook);
                List<Book> newList = iBookManager.getBookList();



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

        }
    };

    //protected修饰的成员变量为保护类型，可以被当前类的子类访问，同一个包中访问，同一个类中访问
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(BookManagerActivity.this,BookManagerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }
}