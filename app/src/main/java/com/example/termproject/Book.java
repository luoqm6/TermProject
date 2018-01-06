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
    private String bookImgPath;//Book的图片地址，封面图像的路径
    private String bookCurPlace;//Book的阅读进度，阅读当前所处字节处
    private String bookSize;//Book的总的字节数

    public Book(){
        this.bookName="";
        this.bookTxtPath="";
        this.bookImgPath="";
        this.bookCurPlace="0";
        this.bookSize="1";
    }
    public Book(Bundle bundle){
        setByBundle(bundle);
    }
    public Book(String bookName, String bookTxtPath,String bookImgPath,String bookCurPlace ,String bookSize){
        this.bookName=bookName;
        this.bookTxtPath=bookTxtPath;
        this.bookImgPath=bookImgPath;
        this.bookCurPlace=bookCurPlace;
        this.bookSize=bookSize;

        //bookName不能为空，其他null时变为空字符串
        if(this.bookTxtPath == null) this.bookTxtPath = "";
        if(this.bookImgPath == null) this.bookImgPath = "";
        if(this.bookCurPlace == null ||this.bookCurPlace.isEmpty()) this.bookCurPlace = "0";
        if(this.bookSize == null ||this.bookSize.isEmpty()) this.bookSize = "1";
    }
    public Book(Book book){
        this.bookName=book.getBookName();
        this.bookTxtPath=book.getBookTxtPath();
        this.bookImgPath=book.getBookImgPath();
        this.bookCurPlace=book.getBookCurPlace();
        this.bookSize=book.getBookSize();
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
    public String getBookCurPlace(){
        return bookCurPlace;
    }
    public String getBookSize(){
        return bookSize;
    }

    public Book setBookName(java.lang.String bookName) {
        this.bookName = bookName;
        return this;
    }

    public Book setBookTxtPath(String bookTxtPath) {
        this.bookTxtPath = bookTxtPath;
        return this;
    }

    public Book setBookImgPath(java.lang.String bookImgPath) {
        this.bookImgPath = bookImgPath;
        return this;
    }

    public Book setBookSize(String bookSize){
        this.bookSize = bookSize;
        return this;
    }

    public Book setBookCurPlace(String bookCurPlace){
        this.bookCurPlace = bookCurPlace;
        return this;
    }

    public Bundle putInBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("bookName",bookName);
        bundle.putString("bookTxtPath",bookTxtPath);
        bundle.putString("bookImgPath",bookImgPath);
        bundle.putString("bookCurPlace",bookCurPlace);
        bundle.putString("bookSize",bookSize);
        return bundle;
    }
    public void setByBundle(Bundle bundle){
        this.bookName=bundle.getString("bookName");
        this.bookTxtPath=bundle.getString("bookTxtPath");
        this.bookImgPath=bundle.getString("bookImgPath");
        this.bookCurPlace=bundle.getString("bookCurPlace");
        this.bookSize=bundle.getString("bookSize");
    }
    public static List<Map<String,Object>> getSimpleList(List<Map<String,Book>> listItems){
        List<Map<String,Object>> simpleListItems=new ArrayList<>();
        for(int i=0;i<listItems.size();i++){
            Map<String,Object> tmp=new LinkedHashMap<>();
            tmp.put("bookName",listItems.get(i).get("Book").getBookName());
            tmp.put("bookTxtPath",listItems.get(i).get("Book").getBookTxtPath());
            tmp.put("bookImgPath",listItems.get(i).get("Book").getBookImgPath());
            tmp.put("bookCurPlace",listItems.get(i).get("Book").getBookCurPlace());
            tmp.put("bookSize",listItems.get(i).get("Book").getBookSize());
            simpleListItems.add(tmp);
        }
        return simpleListItems;
    }
    public String getPercentage(){
        int percent = 0;
        double tmp = Double.parseDouble(this.bookCurPlace)/Double.parseDouble(this.bookSize)*100;
        if (tmp>0&&tmp<1.0)
            percent = 1;
        else
            percent = Double.valueOf(tmp).intValue();

        return String.valueOf(percent);
    }
}