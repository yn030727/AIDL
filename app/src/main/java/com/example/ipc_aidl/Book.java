package com.example.ipc_aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{
    public int bookId;
    public String bookName;
    public Book(int bookId , String bookName){
        this.bookId=bookId;
        this.bookName=bookName;
    }


    protected Book(Parcel in) {
        bookName=in.readString();
        bookId=in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    //序列化
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);
    }
}