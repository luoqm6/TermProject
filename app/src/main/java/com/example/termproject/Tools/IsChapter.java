package com.example.termproject.Tools;

/**
 * Created by 画面太美 on 2018/1/6.
 */

public class IsChapter {
    private String input;
    private String ouput;
    private String ZWZ = "〇一二三四五六七八九零十百千万";
    private String ALB = "1234567890";
    private boolean isTitle = false;
    public IsChapter(String input){
        this.input = input;
        this.ouput = "";
        istitle();
    }
    public void istitle(){
        int indexOfD = input.indexOf("第");
        int indexOfZ = input.indexOf("章");
        int indexOfJie = input.indexOf("节");
        int indexOfP = input.indexOf("篇");
        int indexOfJuan = input.indexOf("卷");
        int len = input.length();//取得输入字符串的长度
        String[] Tname = {"篇","卷","章","节"};
        int[] Tindex = {indexOfP, indexOfJuan, indexOfZ, indexOfJie};
        for (int i = 0;i<Tname.length;i++){
            for (int j = i+1; j<Tname.length;j++){
                if (Tindex[i]>Tindex[j]){
                    int tem = Tindex[j];
                    Tindex[j] = Tindex[i];
                    Tindex[i] = tem;

                    String  temS = Tname[j];
                    Tname[j] = Tname[i];
                    Tname[i] = temS;
                }
            }
        }
      /*  for (int i =0 ;i<Tname.length;i++){
            Log.i("TXT:", Tname[i]);
            Log.i("int:", ""+Tindex[i]);
        }*/

        boolean pp = true;
        if (indexOfJuan != -1){//如：“卷一”
            String ps = input.substring(0,indexOfD+1);
            for (int i =0 ;i<ps.length();i++){
                if (" 卷".indexOf(ps.substring(i,i+1)) == -1){//遍历ps
                    pp = false;
                    break;
                }
            }
        }//Log.i("选中:", pp+" | "+indexOfJuan);
        if (pp && indexOfJuan != -1  && len < 40){
            String subs = input.substring(indexOfJuan);
            //Log.i("选中:", subs);
            if (subs.indexOf(" ") != -1 ){
                subs = subs.substring(1,subs.indexOf(" "));
                find(subs);
                if (getIsTitle())ouput = input.substring(indexOfJuan);
            }
        }
        else if (indexOfD != -1 && len < 40){
            String preSubs = input.substring(0,indexOfD+1);
            boolean preIsEmpty = true;
            for (int i =0 ;i<preSubs.length();i++){
                if (" 第".indexOf(preSubs.substring(i,i+1)) == -1){
                    preIsEmpty = false;
                    break;
                }
            }
            if ( preIsEmpty){
                for (int i = 0 ; i<Tname.length;i++){
                    if (Tindex[i]>-1){
                        String subs = input.substring(indexOfD+1,Tindex[i]);
                        find(subs);//Log.i("选中:", ""+Tname[i]);
                        if (getIsTitle())ouput = input.substring(indexOfD);
                        break;
                    }
                }
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

    public String getOuput() {
        return ouput;
    }
}
