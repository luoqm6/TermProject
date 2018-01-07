package com.example.termproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termproject.Model.Book;
import com.example.termproject.Model.Chapter;
import com.example.termproject.R;
import com.example.termproject.Tools.BookDBHelper;
import com.example.termproject.Tools.IsChapter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingming on 2017/12/31.
 */

public class BookContentActivity extends AppCompatActivity {
    private BookDBHelper bookDBHelper;
    private ProgressBar progressBar;
    private TextView bookContent;
    private TextView bookTitle;
    private ImageButton backInContent;
    private Bundle bundle;
    private Book book;
    private String bookTxtPath;
    //private TxtPlayer txtPlayer;

    private long bookSize;//总字节数
    private String bookName;//书名

    private long CurrentPlace = 0;//当前字节位置
    private int LastPageIndex = 0;//上一个章节下标位置
    private int CurrentPageIndex = 0;//当前的章节下标位置
    private int NextPageIndex = 1;//下一个章节下标位置


    private RandomAccessFile randomAccessFile;
    private String encoding = "GBK";

    //书本对应的所有章节信息的list
    private List<Chapter> chapterList = new ArrayList<>();

    private ScrollView ScrollContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcontent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        book.setBookCurPlace(String.valueOf(CurrentPageIndex));
        book.setBookSize(String.valueOf(chapterList.size()));
        bookDBHelper.updateByBook(book);
        try{
            randomAccessFile.close();
        }
        catch (Exception e){
            Log.i("fileCloseException",e.getMessage());
        }

    }

    private void init(){

        bundle = getIntent().getExtras();
        bookName = bundle.getString("bookName");
        bookTxtPath = bundle.getString("bookTxtPath");




        bookDBHelper = new BookDBHelper(this, BookDBHelper.DB_NAME, null, 1);

        book = bookDBHelper.selectByBookName(bookName);


        progressBar = (ProgressBar) findViewById(R.id.progressBarInContent);
        progressBar.setVisibility(View.GONE);
        //主要内容
        bookContent = (TextView) this.findViewById(R.id.contentText);
        //当前页可以显示的字符数
        //pageLineNum = 200;

        bookTitle = (TextView) this.findViewById(R.id.titleText);
        //返回按键的事件设置
        backInContent = (ImageButton) this.findViewById(R.id.backInContent);
        backInContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置顶部标题
        bookTitle.setText(bookName.substring(0,bookName.indexOf(".")));

        //文件处理,得到文件
        final File txtFile = new File(book.getBookTxtPath());

        //初始化RandomAccessFile,检测设置文件编码类型
        //txtPlayer = new TxtPlayer(txtFile,book.getBookName(),Long.parseLong(book.getBookCurPlace()));
        setRandomAccessFile(txtFile,book.getBookName(),Long.parseLong(book.getBookCurPlace()));

        final Handler divChtrHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==2){//分章节完毕
                    Log.i("finished dividing","finished dividing");
                    if(bookContent!=null){
                        chapterList = bookDBHelper.selectAllToChapterList(bookName);
                        bookContent.setText(read());
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else if (msg.what==1){
                    Log.i("dividing","dividing");
                    if(bookContent!=null){
                        chapterList = bookDBHelper.selectAllToChapterList(bookName);
                        bookContent.setText(read());
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else if (msg.what==0){//已经分了10章
                    Log.i("dividing","dividing");
                    Log.i("第一次","第一次发送");
                    if(bookContent!=null){
                        chapterList = bookDBHelper.selectAllToChapterList(bookName);
                        bookContent.setText(read());
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        };
        //判断是否第一次阅读，是的话进行分章节
        if(book.isFirstTime()){
            bookContent.setText("正在分章节，请不要操作");
            progressBar.setVisibility(View.VISIBLE);
            book.setFirstTime("0");

            //新建一个线程进行分章节操作
            Runnable runnable = new Runnable(){
                String encodingInRun;
                String bookNameInRun;
                BookDBHelper DBHelperInRun ;
                @Override
                public void run() {
                    bookContent.setText("正在分章节，请不要操作");
                    progressBar.setVisibility(View.VISIBLE);
                    book.setFirstTime("0");
                    bookNameInRun = bookName;
                    divideChapter(txtFile);
                    //show=decodeUnicode(show);
                    divChtrHandler.obtainMessage(2).sendToTarget();
                    //book.setFirstTime("0");
                }
                //分章节的操作
                public void divideChapter(File file){//TODO divide the chapter
                    //bookSize;
                    long chapterCurPlace ;
                    long lastChapter = 0;
                    long chapterSize ;
                    long curPlace = 0;
                    long lastLine = 0;
                    long bookSizeInRun = 1;
                    String chapterName = "标题";
                    String lastchapterName = "标题";
                    String chapterContent = "";
                    DBHelperInRun = new BookDBHelper(BookContentActivity.this, BookDBHelper.DB_NAME, null, 1);
                    int chapterSum = 0;
                    try{
                        RandomAccessFile divFile = new RandomAccessFile(file,"r");
                        divFile.seek(0);
                        bookSizeInRun = divFile.length();//获得字节总数
                        byte[] content = new byte[32];
                        divFile.read(content);
                        encodingInRun = getFileIncode(content);
                        String line;
                        IsChapter isChapter ;
                        while (curPlace<bookSizeInRun){
                            String readline = divFile.readLine();
                            if(readline!=null) line = new String(readline.getBytes("iso8859-1"),encodingInRun);
                            else line = "";
                            curPlace = divFile.getFilePointer();
                            isChapter = new IsChapter(line);
                            /*Log.i("chapterDiv",String.valueOf(isChapter.getIsTitle()));
                            Log.i("chapterDiv",line);*/
                            if(isChapter.getIsTitle()){
                                chapterCurPlace = lastChapter;
                                chapterName = lastchapterName;
                                chapterSize = chapterContent.getBytes().length;
                                DBHelperInRun.insertChapterByItem(bookNameInRun,chapterName,String.valueOf(chapterCurPlace),String.valueOf(chapterSize));
                                lastChapter = lastLine;
                                lastchapterName = line;
                                chapterContent = "";
                            }
                            lastLine = divFile.getFilePointer();
                            chapterContent += line;
                        }
                        Log.i("停止时已读字节数",String.valueOf(curPlace));
                        Log.i("停止时字节总数",String.valueOf(bookSizeInRun));
                        chapterCurPlace = lastChapter;
                        chapterName = lastchapterName;
                        chapterSize = chapterContent.getBytes().length;
                        DBHelperInRun.insertChapterByItem(bookNameInRun,chapterName,String.valueOf(chapterCurPlace),String.valueOf(chapterSize));

                    }
                    catch (IOException e){
                        Log.i("停止时已读字节数",String.valueOf(curPlace));
                        Log.i("停止时字节总数",String.valueOf(bookSizeInRun));
                        Log.i("分章",e.getMessage());
                    }
                }
            };
            new Thread(runnable).start();

        }
        else {
            chapterList = bookDBHelper.selectAllToChapterList(bookName);
            if(!bookTxtPath.isEmpty()){
                bookContent.setText(read());
            }
            else{
                Toast.makeText(getApplication(),"文件已不存在",Toast.LENGTH_LONG).show();
            }
        }

        //左右翻页按键
        ImageButton lastPage = (ImageButton) this.findViewById(R.id.lastPage);
        ImageButton nextPage = (ImageButton) findViewById(R.id.nextPage);
        ScrollContent = (ScrollView) findViewById(R.id.ScrollContent);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookContent.setText(getLastPage());
                ScrollContent.smoothScrollTo(0,0);
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookContent.setText(getNextPage());
                ScrollContent.scrollTo(0,0);
            }
        });
    }

    public void setRandomAccessFile(File file,String bookName ,long CurrentPlace)  {
        this.bookName = bookName;
        try {
            randomAccessFile = new RandomAccessFile(file,"r");
            bookSize = randomAccessFile.length();//获得字节总数
            Log.i("获得字节总数",String.valueOf(bookSize));
            byte[] content = new byte[32];
            randomAccessFile.read(content);
            encoding = getFileIncode(content);
            //Log.i("encoding",encoding);
            this.CurrentPlace=CurrentPlace;
            CurrentPageIndex = Long.valueOf(CurrentPlace).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定位字节位置 根据页数定位文本的位置
    private void seekTo(int PageIndex){
        if(PageIndex<=0) {
            LastPageIndex = 0;
            CurrentPageIndex = 0;
            NextPageIndex = 1;
        }
        else if(PageIndex>=chapterList.size()-1) {
            chapterList = bookDBHelper.selectAllToChapterList(bookName);
            CurrentPageIndex = chapterList.size()-1;
            LastPageIndex = CurrentPageIndex - 1;
            NextPageIndex = CurrentPageIndex;
        }
        else{
            LastPageIndex = CurrentPageIndex-1;
            NextPageIndex = CurrentPageIndex+1;
        }

        CurrentPlace = Long.parseLong(chapterList.get(CurrentPageIndex).getChapterCurPlace());
        Log.i("seekTo",chapterList.get(CurrentPageIndex).getChapterName()+"\n"+String.valueOf(chapterList.get(CurrentPageIndex).getChapterCurPlace()));
        try {
            randomAccessFile.seek(CurrentPlace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //定位字节位置 根据页数定位文本的位置
    private void findIdxByPlace(long CurrentPlace){
        if(CurrentPlace<0) CurrentPlace = 0;
        else if(CurrentPlace > bookSize) CurrentPlace = bookSize - 1;
        for(int i = 0; i< chapterList.size();i++){
            if(chapterList.get(i).getChapterCurPlace().equals(String.valueOf(CurrentPlace))){
                LastPageIndex = i-1;
                CurrentPageIndex = i;
                NextPageIndex = i+1;
            }
        }
        try {
            randomAccessFile.seek(CurrentPlace);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(){
        int curSizeInt = 0;
        String readStr = "";
        String lineStr ;
        try {
            //先到当前位置
            seekTo(CurrentPageIndex);
            lineStr = new String(randomAccessFile.readLine().getBytes("iso8859-1"),encoding);
            curSizeInt += lineStr.getBytes().length;
            readStr += lineStr + "\n";
            Log.i("CurrentPageIndex",String.valueOf(CurrentPageIndex));
            //Log.i("chapterList.size()",String.valueOf(chapterList.size()));
            if(CurrentPageIndex == chapterList.size()-1){//最后一个章节的处理
                //一直读行直到全书完毕
                while(randomAccessFile.getFilePointer() < bookSize){
                    lineStr = new String(randomAccessFile.readLine().getBytes("iso8859-1"),encoding);
                    readStr += lineStr + "\n";
                }
            }
            else{
                //一直读行直到下一个章节
                while(randomAccessFile.getFilePointer()
                        < Long.parseLong(chapterList.get(NextPageIndex).getChapterCurPlace())){
                    lineStr = new String(randomAccessFile.readLine().getBytes("iso8859-1"),encoding);
                    readStr += lineStr + "\n";
                }
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
        CurrentPageIndex = LastPageIndex;
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        content = read();
        return content;
    }

    //下一页功能的实现
    public String getNextPage(){
        String content ;
        CurrentPageIndex = NextPageIndex;
        content = read();
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



   /* public int getLineNum(){
        Layout layout = bookContent.getLayout();
        int topOfLastLine = bookContent.getHeight()-bookContent.getPaddingTop()
                -bookContent.getPaddingBottom()-bookContent.getLineHeight();
        return layout.getLineForVertical(topOfLastLine);
    }
    public int getpageLineNum(){
        return bookContent.getLayout().getLineEnd(getLineNum());
    }*/
}
