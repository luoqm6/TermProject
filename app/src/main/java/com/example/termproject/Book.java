package com.example.termproject;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingming on 2017/12/30.
 */

public class Book implements Serializable {
    private String bookName;//Book的名称
    private String bookTxtPath;//Book的Path地址
    private String bookImgPath;//Book的图片地址
    private String bookProgress;//Book的阅读进度

    public Book(){
        this.bookName="";
        this.bookTxtPath="";
        this.bookImgPath="";
        this.bookProgress="0%";
    }
    public Book(Bundle bundle){
        setByBundle(bundle);
    }
    public Book(String bookName, String bookTxtPath,String bookImgPath, String bookProgress){
        this.bookName=bookName;
        this.bookTxtPath=bookTxtPath;
        this.bookImgPath=bookImgPath;
        this.bookProgress=bookProgress;

        //bookName不能为空，其他null时变为空字符串
        if(this.bookTxtPath == null) this.bookTxtPath = "";
        if(this.bookImgPath == null) this.bookImgPath = "";
        if(this.bookProgress == null) this.bookProgress = "0%";
    }
    public Book(Book book){
        this.bookName=book.getBookName();
        this.bookTxtPath=book.getBookTxtPath();
        this.bookImgPath=book.getBookImgPath();
        this.bookProgress=book.getBookProgress();
    }
    public String getBookName(){
        return bookName;
    }
    public String getBookTxtPath(){
        return bookTxtPath;
    }
    public String getBookImgPath(){
        return bookImgPath;
    }
    public String getBookProgress(){
        return bookProgress;
    }

    public void setBookName(java.lang.String bookName) {
        this.bookName = bookName;
    }

    public void setBookTxtPath(String bookTxtPath) {
        this.bookTxtPath = bookTxtPath;
    }

    public void setBookImgPath(java.lang.String bookImgPath) {
        this.bookImgPath = bookImgPath;
    }

    public void setBookProgress(String bookProgress) {
        this.bookProgress = bookProgress;
    }

    public Bundle putInBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("bookName",bookName);
        bundle.putString("bookTxtPath",bookTxtPath);
        bundle.putString("bookImgPath",bookImgPath);
        bundle.putString("bookProgress",bookProgress);
        return bundle;
    }
    public void setByBundle(Bundle bundle){
        this.bookName=bundle.getString("bookName");
        this.bookTxtPath=bundle.getString("bookTxtPath");
        this.bookImgPath=bundle.getString("bookImgPath");
        this.bookProgress=bundle.getString("bookProgress");
    }
    public static List<Map<String,Object>> getSimpleList(List<Map<String,Book>> listItems){
        List<Map<String,Object>> simpleListItems=new ArrayList<>();
        for(int i=0;i<listItems.size();i++){
            Map<String,Object> tmp=new LinkedHashMap<>();
            tmp.put("bookName",listItems.get(i).get("Book").getBookName());
            tmp.put("bookTxtPath",listItems.get(i).get("Book").getBookTxtPath());
            tmp.put("bookImgPath",listItems.get(i).get("Book").getBookImgPath());
            tmp.put("bookProgress",listItems.get(i).get("Book").getBookProgress());
            simpleListItems.add(tmp);
        }
        return simpleListItems;
    }
}