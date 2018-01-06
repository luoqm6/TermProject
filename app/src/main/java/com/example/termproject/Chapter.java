package com.example.termproject;

import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingming on 2018/1/6.
 */

public class Chapter implements Serializable {
    private String BookName;//Chapter的Path地址
    private String ChapterName;//Chapter的名称
    private String ChapterCurPlace;//Chapter的阅读进度，阅读当前所处字节处
    private String ChapterSize;//Chapter的总的字节数

    public Chapter(){
        this.ChapterName="";
        this.BookName="";
        this.ChapterCurPlace="0";
        this.ChapterSize="1";
    }
    public Chapter(Bundle bundle){
        setByBundle(bundle);
    }
    public Chapter(String BookName, String ChapterName, String ChapterCurPlace ,String ChapterSize){
        this.ChapterName=ChapterName;
        this.BookName=BookName;
        this.ChapterCurPlace=ChapterCurPlace;
        this.ChapterSize=ChapterSize;

        //ChapterName不能为空，其他null时变为空字符串
        if(this.BookName == null) this.BookName = "";
        if(this.ChapterName == null) this.ChapterName = "";
        if(this.ChapterCurPlace == null ||this.ChapterCurPlace.isEmpty()) this.ChapterCurPlace = "0";
        if(this.ChapterSize == null ||this.ChapterSize.isEmpty()) this.ChapterSize = "1";
    }
    public Chapter(Chapter Chapter){
        this.ChapterName=Chapter.getChapterName();
        this.BookName=Chapter.getBookName();
        this.ChapterCurPlace=Chapter.getChapterCurPlace();
        this.ChapterSize=Chapter.getChapterSize();
    }
    public String getChapterName(){
        return ChapterName;
    }
    public String getBookName(){
        return BookName;
    }
    public String getChapterCurPlace(){
        return ChapterCurPlace;
    }
    public String getChapterSize(){
        return ChapterSize;
    }

    public Chapter setChapterName(java.lang.String ChapterName) {
        this.ChapterName = ChapterName;
        return this;
    }

    public Chapter setBookName(String BookName) {
        this.BookName = BookName;
        return this;
    }


    public Chapter setChapterSize(String ChapterSize){
        this.ChapterSize = ChapterSize;
        return this;
    }

    public Chapter setChapterCurPlace(String ChapterCurPlace){
        this.ChapterCurPlace = ChapterCurPlace;
        return this;
    }

    public Bundle putInBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("ChapterName",ChapterName);
        bundle.putString("BookName",BookName);
        bundle.putString("ChapterCurPlace",ChapterCurPlace);
        bundle.putString("ChapterSize",ChapterSize);
        return bundle;
    }
    public void setByBundle(Bundle bundle){
        this.ChapterName=bundle.getString("ChapterName");
        this.BookName=bundle.getString("BookName");
        this.ChapterCurPlace=bundle.getString("ChapterCurPlace");
        this.ChapterSize=bundle.getString("ChapterSize");
    }
    public static List<Map<String,Object>> getSimpleList(List<Map<String,Chapter>> listItems){
        List<Map<String,Object>> simpleListItems=new ArrayList<>();
        for(int i=0;i<listItems.size();i++){
            Map<String,Object> tmp=new LinkedHashMap<>();
            tmp.put("ChapterName",listItems.get(i).get("Chapter").getChapterName());
            tmp.put("BookName",listItems.get(i).get("Chapter").getBookName());
            tmp.put("ChapterCurPlace",listItems.get(i).get("Chapter").getChapterCurPlace());
            tmp.put("ChapterSize",listItems.get(i).get("Chapter").getChapterSize());
            simpleListItems.add(tmp);
        }
        return simpleListItems;
    }
}