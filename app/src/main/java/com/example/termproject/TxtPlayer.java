package com.example.termproject;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * Created by qingming on 2018/1/2.
 */

public class TxtPlayer  {
    private long bookSize;//总字节数
    private String bookName;//书名
    public  int pageBtyeNum = 5000;//每一页的字节数 字节数固定
    private long CurrentPlace = 1;//当前页面
    private RandomAccessFile randomAccessFile;
    private String encoding = "GBK";
    //构造方法，为了实现记录功能传入当前页 ，记录用户读取的位置
    public  TxtPlayer(File file,String bookName ,long CurrentPlace)  {
        this.bookName = bookName;
        try {
            randomAccessFile = new RandomAccessFile(file,"r");
            bookSize = randomAccessFile.length();//获得字节总数
            byte[] content = new byte[32];
            randomAccessFile.read(content);
            encoding = getFileIncode(content);
            //Log.i("encoding",encoding);
            this.CurrentPlace=CurrentPlace;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //定位字节位置 根据页数定位文本的位置
    private void seekTo(long CurrentPlace){
        if(CurrentPlace<0) CurrentPlace = 0;
        else if(CurrentPlace > bookSize) CurrentPlace = bookSize -1;
        try {
            randomAccessFile.seek(CurrentPlace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String read(){
        //内容重叠防止 末尾字节乱码
        int curSizeInt = 0;
        String readStr = "";
        String lineStr = "";
        try {
            //先到当前位置
            seekTo(CurrentPlace);
            lineStr = new String(randomAccessFile.readLine().getBytes("iso8859-1"),encoding);
            curSizeInt += lineStr.getBytes().length;
            readStr += lineStr + "\n";
            while(curSizeInt<pageBtyeNum){
                //lineStr = new String(randomAccessFile.readLine().getBytes("iso8859-1"),encoding);
                Log.i("curSizeInt",String.valueOf(curSizeInt));

                lineStr = new String(randomAccessFile.readLine().getBytes("iso8859-1"),encoding);
                curSizeInt += lineStr.getBytes(encoding).length;
                readStr += lineStr + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("readStrerror",e.getMessage());
        }
        return readStr;
    }

    //上一页功能的实现
    public String getLastPage(){
        String content ;
        CurrentPlace -= pageBtyeNum;
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        if(CurrentPlace <= 0){
            CurrentPlace = 0;
            content = read();
        }
        else{
            //其它页 则定位到当前页-2 处 再读取指定字节内容
            content = read();
        }
        return content;
    }

    //下一页功能的实现
    public String getNextPage(){
        String content ;
        CurrentPlace += pageBtyeNum;
        if(CurrentPlace >= bookSize){//当前页为最后一页时候,显示的还是 最后一页
            CurrentPlace = bookSize;
            content = read();
        }
        else{
            content = read();

        }
        return content;
    }

    public long getCurrentPlace(){
        return CurrentPlace;
    }
    public long getBookSize(){
        return bookSize;
    }

    //判断TXT文件的编码
    public static String getFileIncode(byte[] bytes) {
        String DEFAULT_ENCODING = "UTF-8";
        org.mozilla.universalchardet.UniversalDetector detector =
                new org.mozilla.universalchardet.UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }

        return encoding;
    }
}
