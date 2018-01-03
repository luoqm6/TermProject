package com.example.termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingming on 2017/12/30.
 */

public class BookDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "BookData.db";
    private static final String TABLE_NAME = "BookData";
    private static final int DB_VERSION = 1;
    //调用父类构造器
    public BookDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                + " (_id integer primary key autoincrement, "
                //TXT的名字
                + "bookName text unique, "
                //TXT的路径
                + "bookTxtPath text , "
                //封面图像的路径
                + "bookImgPath text , "
                //阅读当前页
                + "bookCurPage text,"
                //阅读当前页
                + "bookPageSum text);";
        db.execSQL(CREATE_TABLE);
    }

    //当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // To Do
    }

    //插入
    public void insertByItem(String bookName, String bookTxtPath,String bookImgPath,String bookCurPage, String bookPageSum) {

        //bookName不能为空，其他null时变为空字符串
        if(bookTxtPath == null) bookTxtPath = "";
        if(bookImgPath == null) bookImgPath = "";
        if(bookCurPage == null||bookCurPage.isEmpty()) bookCurPage = "0";
        if(bookPageSum == null||bookPageSum.isEmpty()) bookPageSum = "1";

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName", bookName);
        values.put("bookTxtPath", bookTxtPath);
        values.put("bookImgPath", bookImgPath);
        values.put("bookCurPage", bookCurPage);
        values.put("bookPageSum", bookPageSum);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //插入
    public void insertByBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookName", book.getBookName());
        values.put("bookTxtPath", book.getBookTxtPath());
        values.put("bookImgPath", book.getBookImgPath());
        values.put("bookCurPage", book.getBookCurPage());
        values.put("bookPageSum", book.getBookPageSum());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void update(String bookName, String bookTxtPath, String bookImgPath, String bookCurPage,String bookPageSum) {

        //bookName不能为空，其他null时变为空字符串
        if(bookTxtPath == null) bookTxtPath = "";
        if(bookImgPath == null) bookImgPath = "";
        if(bookCurPage == null||bookCurPage.isEmpty()) bookCurPage = "0";
        if(bookPageSum == null||bookPageSum.isEmpty()) bookPageSum = "1";

        SQLiteDatabase db = getWritableDatabase();
        String insert_sql = "update "+ TABLE_NAME
                +" set bookTxtPath = '"+ bookTxtPath
                +"',bookImgPath = '"+ bookImgPath
                +"',bookCurPage = '"+ bookCurPage
                +"',bookPageSum = '"+ bookPageSum
                +"' where bookName = '"+ bookName+"'" ;
        db.execSQL(insert_sql);
    }

    public void updateByBook(Book book) {

        SQLiteDatabase db = getWritableDatabase();
        String insert_sql = "update "+ TABLE_NAME
                +" set bookTxtPath = '"+ book.getBookTxtPath()
                +"',bookImgPath = '"+ book.getBookImgPath()
                +"',bookCurPage = '"+ book.getBookCurPage()
                +"',bookPageSum = '"+ book.getBookPageSum()
                +"' where bookName = '"+ book.getBookName()+"'" ;
        db.execSQL(insert_sql);
    }

    public void delete(String bookName) {
        SQLiteDatabase db = getWritableDatabase();
        String delete_sql = "DELETE FROM "+ TABLE_NAME +"  WHERE bookName = '"+ bookName +"'";
        db.execSQL(delete_sql);
    }

    //返回含有每本书对应属性的map的list
    public List<Map<String,Object>> selectAllToMapList(){
        //用于返回的装有sqlite数据库内所有书目对应属性内容的map的List
        List<Map<String,Object>> listItems = new ArrayList<>();
        //得到数据库以及数据库的cursor，遍历选出sqlite数据库内所有人物
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);

        while(cursor.moveToNext()){
            //遍历Cursor对象，取出数据存入List中
            Map<String,Object> tmp = new LinkedHashMap<>();
            tmp.put("bookName",cursor.getString(cursor.getColumnIndex("bookName")));
            tmp.put("bookTxtPath",cursor.getString(cursor.getColumnIndex("bookTxtPath")));
            tmp.put("bookImgPath",cursor.getString(cursor.getColumnIndex("bookImgPath")));
            tmp.put("bookCurPage",cursor.getString(cursor.getColumnIndex("bookCurPage")));
            tmp.put("bookPageSum",cursor.getString(cursor.getColumnIndex("bookPageSum")));
            listItems.add(tmp);
        }
        cursor.close();
        return listItems;
    }

    //返回含有每本书Book类的list
    public List<Book> selectAllToBookList(){
        //用于返回的装有sqlite数据库内所有书目对应的book类的List
        List<Book> listItems = new ArrayList<>();

        //得到数据库以及数据库的cursor，遍历选出sqlite数据库内所有人物
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);

        while(cursor.moveToNext()){
            //遍历Cursor对象，取出数据存入List中
            Book book = new Book();
            book.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
            book.setBookTxtPath(cursor.getString(cursor.getColumnIndex("bookTxtPath")));
            book.setBookImgPath(cursor.getString(cursor.getColumnIndex("bookImgPath")));
            book.setBookCurPage(cursor.getString(cursor.getColumnIndex("bookCurPage")));
            book.setBookPageSum(cursor.getString(cursor.getColumnIndex("bookPageSum")));
            listItems.add(book);
        }
        cursor.close();
        return listItems;
    }

    //按照书的名字查找书目
    public Book selectByBookName(String bookName){
        //得到数据库以及数据库的cursor，遍历选出sqlite数据库内所有人物
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME+" where bookName = '"+bookName +"'", null);

        Book book = new Book();
        while(cursor.moveToNext()){
            //遍历Cursor对象，取出数据存入List中
            book.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
            book.setBookTxtPath(cursor.getString(cursor.getColumnIndex("bookTxtPath")));
            book.setBookImgPath(cursor.getString(cursor.getColumnIndex("bookImgPath")));
            book.setBookCurPage(cursor.getString(cursor.getColumnIndex("bookCurPage")));
            book.setBookPageSum(cursor.getString(cursor.getColumnIndex("bookPageSum")));
        }
        cursor.close();
        return book;
    }
}