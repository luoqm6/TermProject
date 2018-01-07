package com.example.termproject.Tools;

/**
 * Created by 画面太美 on 2018/1/6.
 */

public class IsChapter {
    private String input;
    private String ZWZ = "〇一二三四五六七八九零十百千万第章节";
    private String ALB = "1234567890第章节";
    private boolean isTitle = false;
    public IsChapter(String input){
        this.input = input;
        istitle();
    }
    public void istitle(){
        if (input.indexOf("第")!=-1 ){
            if (input.indexOf("章")!= -1 && (input.indexOf("章")> (input.indexOf("第")+1) )){
                String subs = input.substring(input.indexOf("第"),input.indexOf("章")+1);
                find(subs);
            }
            else if(input.indexOf("节")!= -1 &&( input.indexOf("节")> (input.indexOf("第")+1))){
                String subs = input.substring(input.indexOf("第"),input.indexOf("节")+1);
                find(subs);
            }
        }
    }
    public void find(String subs){
        for(int i = 0 ;i<subs.length();i++){
            if ( ZWZ.indexOf( subs.substring(i,i+1) )== -1 ){
                break;
            }
            if (i == (subs.length()-1) ){
                isTitle = true;
            }
        }
        for(int i = 0 ;i<subs.length();i++){
            if ( ALB.indexOf( subs.substring(i,i+1) )== -1 ){
                break;
            }
            if (i == (subs.length()-1) ){
                isTitle = true;
            }
        }
    }
    public boolean getIsTitle(){
        return isTitle;
    }

    public String getInput() {
        return input;
    }
}
