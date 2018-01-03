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
    private String bookCurPage;//Book的阅读进度，阅读当前页面
    private String bookPageSum;//Book的阅读进度，阅读当前进度，0-100的数字对应字符串，即没有百分号的百分数

    public Book(){
        this.bookName="";
        this.bookTxtPath="";
        this.bookImgPath="";
        this.bookCurPage="0";
        this.bookPageSum="1";
    }
    public Book(Bundle bundle){
        setByBundle(bundle);
    }
    public Book(String bookName, String bookTxtPath,String bookImgPath,String bookCurPage ,String bookPageSum){
        this.bookName=bookName;
        this.bookTxtPath=bookTxtPath;
        this.bookImgPath=bookImgPath;
        this.bookCurPage=bookCurPage;
        this.bookPageSum=bookPageSum;

        //bookName不能为空，其他null时变为空字符串
        if(this.bookTxtPath == null) this.bookTxtPath = "";
        if(this.bookImgPath == null) this.bookImgPath = "";
        if(this.bookCurPage == null ||this.bookCurPage.isEmpty()) this.bookCurPage = "0";
        if(this.bookPageSum == null ||this.bookPageSum.isEmpty()) this.bookPageSum = "1";
    }
    public Book(Book book){
        this.bookName=book.getBookName();
        this.bookTxtPath=book.getBookTxtPath();
        this.bookImgPath=book.getBookImgPath();
        this.bookCurPage=book.getBookCurPage();
        this.bookPageSum=book.getBookPageSum();
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
    public String getBookCurPage(){
        return bookCurPage;
    }
    public String getBookPageSum(){
        return bookPageSum;
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

    public Book setBookPageSum(String bookPageSum){
        this.bookPageSum = bookPageSum;
        return this;
    }

    public Book setBookCurPage(String bookCurPage){
        this.bookCurPage = bookCurPage;
        return this;
    }

    public Bundle putInBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("bookName",bookName);
        bundle.putString("bookTxtPath",bookTxtPath);
        bundle.putString("bookImgPath",bookImgPath);
        bundle.putString("bookCurPage",bookCurPage);
        bundle.putString("bookPageSum",bookPageSum);
        return bundle;
    }
    public void setByBundle(Bundle bundle){
        this.bookName=bundle.getString("bookName");
        this.bookTxtPath=bundle.getString("bookTxtPath");
        this.bookImgPath=bundle.getString("bookImgPath");
        this.bookCurPage=bundle.getString("bookCurPage");
        this.bookPageSum=bundle.getString("bookPageSum");
    }
    public static List<Map<String,Object>> getSimpleList(List<Map<String,Book>> listItems){
        List<Map<String,Object>> simpleListItems=new ArrayList<>();
        for(int i=0;i<listItems.size();i++){
            Map<String,Object> tmp=new LinkedHashMap<>();
            tmp.put("bookName",listItems.get(i).get("Book").getBookName());
            tmp.put("bookTxtPath",listItems.get(i).get("Book").getBookTxtPath());
            tmp.put("bookImgPath",listItems.get(i).get("Book").getBookImgPath());
            tmp.put("bookCurPage",listItems.get(i).get("Book").getBookCurPage());
            tmp.put("bookPageSum",listItems.get(i).get("Book").getBookPageSum());
            simpleListItems.add(tmp);
        }
        return simpleListItems;
    }
    public String getPercentage(){
        int percent = (Integer.parseInt(this.bookCurPage))/Integer.parseInt(this.bookPageSum);
        return String.valueOf(percent);
    }
}