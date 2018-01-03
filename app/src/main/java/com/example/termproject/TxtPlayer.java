package com.example.termproject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * Created by qingming on 2018/1/2.
 */

public class TxtPlayer  {
    private long pageSum;//总页数
    public static long pageSize = 1000;//每一页的字节数 字节数固定
    private long bytesCount;//字节总数
    private long currentPage;//当前页面
    private RandomAccessFile randomAccessFile;
    //构造方法，为了实现记录功能传入当前页 ，记录用户读取的位置
    public  TxtPlayer(File file, long currentPage)  {
        try {
            randomAccessFile = new RandomAccessFile(file,"r");
            bytesCount = randomAccessFile.length();//获得字节总数
            pageSum = bytesCount/pageSize;//计算得出文本的页数
            this.currentPage=currentPage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //定位字节位置 根据页数定位文本的位置
    private void seekTo(long page){
        try {
            randomAccessFile.seek(page*pageSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //固定读取 SIZE+30个字节的内容 为什么+30 ？
    //读取的为字节 需要进行转码 转码不可能刚好转的就是文本内容,
    //一页的末尾 和开始出有可能乱码 每一次多读30个字节 是为了第一页乱码位置
    //在第二页就可以正常显示出内容 不影响阅读
    public String read(){
        //内容重叠防止 末尾字节乱码
        int pageSizeInt= Long.valueOf(pageSize).intValue();
        byte[] content = new byte[pageSizeInt+30];
        try {
            //先到当前页
            seekTo(currentPage);
            randomAccessFile.read(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(content, Charset.forName("utf-8"));
    }

    //上一页功能的实现
    public String getLastPage(){
        String content;
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        if(currentPage <= 1){
            seekTo(currentPage-1);
            content =read();
        }
        else{
            //其它页 则定位到当前页-2 处 再读取指定字节内容 例如当前定位到第二页的末尾 2*SIZE
            // 上一页应该是第一页 也就是从0位置 开始读取SIZE个字节
            seekTo(currentPage-2);
            content = read();
            currentPage = currentPage - 1;
        }
        return content;
    }

    //下一页功能的实现
    public String getNextPage(){
        String content = null;
        if(currentPage >= pageSum){//当前页为最后一页时候,显示的还是 最后一页
            seekTo(currentPage-1);
            content = read();
        }
        else{
            seekTo(currentPage);
            content = read();
            currentPage = currentPage +1;
        }
        return content;
    }
}
