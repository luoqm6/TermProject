package com.example.termproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by qingming on 2017/12/31.
 */

public class BookContentActivity extends Activity {
    private BookDBHelper bookDBHelper;
    private ProgressBar progressBar;
    private TextView bookContent;
    private TextView bookTitle;
    private ImageView backInContent;
    private Bundle bundle;
    private Book book;
    private RandomAccessFile randomAccessFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookcontent);
        init();
    }
    private void init(){

        bundle = getIntent().getExtras();
        String bookName = bundle.getString("bookName");
        String bookTxtPath = bundle.getString("bookTxtPath");

        bookDBHelper = new BookDBHelper(this, BookDBHelper.DB_NAME, null, 1);

        book = bookDBHelper.selectByBookName(bookName);

        progressBar = (ProgressBar) findViewById(R.id.progressBarInContent);
        progressBar.setVisibility(View.VISIBLE);
        bookContent = (TextView) this.findViewById(R.id.contentText);
        bookTitle = (TextView) this.findViewById(R.id.titleText);
        backInContent = (ImageView) this.findViewById(R.id.backInContent);
        backInContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bookTitle.setText(bookName.substring(0,bookName.indexOf(".")));
        if(!bookTxtPath.isEmpty()){
            bookContent.setText(readTxtFile(bookTxtPath));
        }

    }

    private String readTxtFile(String bookTxtPath){
        StringBuilder txtContentStr = new StringBuilder("");
        try{
            File txtFile = new File(bookTxtPath);
            randomAccessFile = new RandomAccessFile(txtFile,"r");
            while(randomAccessFile.getFilePointer()<randomAccessFile.length()){
                String ns = new String(randomAccessFile.readLine().getBytes("iso8859-1"),"UTF-8");
                txtContentStr.append(ns).append("\n\n");
            }
        }
        catch (IOException e){
            Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return txtContentStr.toString();
    }
}
