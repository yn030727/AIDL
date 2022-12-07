// IBookManager.aidl
package com.example.ipc_aidl;
import com.example.ipc_aidl.Book1;
import com.example.ipc_aidl.IOnNewBookArrivedListener;
interface IBookManager {
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}