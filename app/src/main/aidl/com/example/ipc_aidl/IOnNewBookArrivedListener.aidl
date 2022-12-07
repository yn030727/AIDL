// IOnNewBookArrivedListener.aidl
package com.example.ipc_aidl;
import com.example.ipc_aidl.Book1;
// Declare any non-default types here with import statements

//我们所期望的是当有新书到的时候，就会通知每一个已经申请提醒功能的用户。
//从程序上来说，就是调用所有IOnNewBookArrivedListener对象中的onNewBookArrived方法
//并把新书的对象通过参数传递给客户端
interface IOnNewBookArrivedListener {
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void onNewBookArrived(in Book newBook);
}