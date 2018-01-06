package com.example.termproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

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
    private String bookName;
    private String bookTxtPath;
    //private RandomAccessFile randomAccessFile;
    private TxtPlayer txtPlayer;
    private int pageLineNum;

    private ScrollView ScrollContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcontent);
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
        book.setBookCurPlace(String.valueOf(txtPlayer.getCurrentPlace()));
        book.setBookSize(String.valueOf(txtPlayer.getBookSize()));
        bookDBHelper.updateByBook(book);
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
        pageLineNum = 200;

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
        if(!bookTxtPath.isEmpty()){
            File txtFile = new File(book.getBookTxtPath());
            txtPlayer = new TxtPlayer(txtFile,Long.parseLong(book.getBookCurPlace()));
            bookContent.setText(txtPlayer.read());
        }

        //左右翻页按键
        ImageButton lastPage = (ImageButton) this.findViewById(R.id.lastPage);
        ImageButton nextPage = (ImageButton) findViewById(R.id.nextPage);
        ScrollContent = (ScrollView) findViewById(R.id.ScrollContent);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookContent.setText(txtPlayer.getLastPage());
                ScrollContent.scrollTo(0,0);
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookContent.setText(txtPlayer.getNextPage());
                ScrollContent.scrollTo(0,0);
            }
        });

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
