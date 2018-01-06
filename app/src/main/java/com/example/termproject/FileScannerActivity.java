package com.example.termproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingming on 2017/12/31.
 *
 */

public class FileScannerActivity extends Activity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<File> listItems = new ArrayList<>();
    private String mRootPath ;  //sdcard 根路径
    private String mLastFilePath ;    //当前显示的路径
    private BookDBHelper bookDBHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filescanner);
        init();
    }
    private void init(){
        //变量实例化赋值
        bookDBHelper = new BookDBHelper(this, BookDBHelper.DB_NAME, null, 1);
        mRootPath  =  Environment.getExternalStorageDirectory().toString();



        //点击回到根目录
        TextView rootFolderName = (TextView) this.findViewById(R.id.rootFolderName);
        rootFolderName.setText(mRootPath);
        CardView  rootFolderHint = (CardView) this.findViewById(R.id.rootFolderHint);
        rootFolderHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFileItems(mRootPath);
            }
        });
        ImageView backHomeHintImg = this.findViewById(R.id.backHomeHintImg);
        backHomeHintImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击上面返回上级
        CardView  curFolderHint = (CardView) this.findViewById(R.id.curFolderHint);
        curFolderHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backProcess();
            }
        });

        initRecyclerView();
        updateFileItems(mRootPath);
    }

    //中间文件列表的初始化设置
    private void initRecyclerView(){
        //RecyclerView实现文件夹列表，实例化LayoutManager和RecycleAdapter
        layoutManager = new LinearLayoutManager(this);
        recycleAdapter = new RecyclerViewAdapter<File>(this,R.layout.cardview_in_list,listItems) {
            //重写convert函数设置每个条目中的
            @Override
            public void convert(ViewHolder holder, File f) {
                TextView folderName = holder.getView(R.id.folderName);
                ImageView folderImg = holder.getView(R.id.folderImg);
                if(!f.isDirectory()&&f.getName().endsWith(".txt")){
                    folderImg.setImageResource(R.mipmap.txtfileicon2);
                }
                else folderImg.setImageResource(R.mipmap.filefoldicon);
                folderName.setText(f.getName());
                folderName.setSingleLine();
                folderName.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            }
        };
        recyclerView = (RecyclerView) findViewById(R.id.folderList);
        recycleAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onClick(View view, int position){
                /*点击文件夹进入文件夹，点击文件加入首页*/
                //当前的文件
                final File curFile =  listItems.get(position);
                String fileAbsolutePath = listItems.get(position).getAbsolutePath();
                if(curFile.isDirectory())   //点击项为文件夹, 显示该文件夹下所有文件
                    updateFileItems(fileAbsolutePath) ;
                else if(curFile.getName().endsWith(".txt")) {
                    //是txt文件 ，存入数据库
                    Toast.makeText(FileScannerActivity.this,fileAbsolutePath, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(FileScannerActivity.this);
                    builder.setMessage("将文件加入列表?")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //加入数据库中
                                    String pageSumStr = "1";
                                    try{
                                        RandomAccessFile randomAccessFile;
                                        File txtFile = new File(curFile.getAbsolutePath());
                                        randomAccessFile = new RandomAccessFile(txtFile,"r");
                                        long pageSum = randomAccessFile.length();//获得字节总数
                                        if(pageSum<=0) pageSum =1;
                                        pageSum +=1;
                                        pageSumStr = String.valueOf(pageSum);//TODO 为什么是0？？

                                        Toast.makeText(getApplication(),pageSumStr,Toast.LENGTH_SHORT).show();
                                        randomAccessFile.close();
                                    }
                                    catch (IOException e){
                                        e.printStackTrace();
                                        Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                    bookDBHelper.insertBookByItem(curFile.getName(),curFile.getAbsolutePath(),"","0",pageSumStr,"yes");
                                    Toast.makeText(FileScannerActivity.this,"已加入", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create().show();
                }
                else { //其他文件.....
                    Toast.makeText(FileScannerActivity.this, "请选择一个txt文件！", Toast.LENGTH_SHORT).show();
                    Toast.makeText(FileScannerActivity.this,"其他文件", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean onLongClick(View view,final int position){
                AlertDialog.Builder builder=new AlertDialog.Builder(FileScannerActivity.this);
                builder.setMessage("从列表中删除目录?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listItems.remove(position);
                                recycleAdapter.notifyItemRemoved(position);
                                recycleAdapter.notifyItemChanged(position);
                                recycleAdapter.notifyDataSetChanged();
                                Toast.makeText(FileScannerActivity.this,"已删除", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
                return true;
            }
        });
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        // 设置adapter
        recyclerView.setAdapter(recycleAdapter);
    }

    //根据路径更新数据，并且通知Adapter数据改变
    private void updateFileItems(String filePath) {
        mLastFilePath = filePath ;
        TextView curFileName = (TextView) findViewById(R.id.curFolderName);
        curFileName.setText(mLastFilePath);

        if(listItems == null)
            listItems = new ArrayList<>() ;
        //清空目录列表
        if(!listItems.isEmpty())
            listItems.clear() ;

        File[] files = folderScan(filePath);
        if(files == null)
            return ;
        for (int i = 0; i < files.length; i++) {
            if(files[i].isHidden())  // 不显示隐藏文件
                continue ;
            //添加至列表
            if(files[i].isDirectory()||files[i].getName().endsWith(".txt"))
                listItems.add(files[i]);
        }
        //When first enter , the object of recycleAdapter don't initialized
        if(recycleAdapter != null)
            recycleAdapter.notifyDataSetChanged();  //重新刷新
    }

    //获得当前路径的所有文件
    private File[] folderScan(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return files;
    }

    //重写自带返回键的返回事件
    public boolean onKeyDown(int keyCode , KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()
                == KeyEvent.KEYCODE_BACK) {
            backProcess();
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }

    //返回上一层目录的操作
    public void backProcess() {
        //判断当前路径是不是根路径 ， 如果不是，则返回到上一层。
        if (!mLastFilePath.equals(mRootPath)) {
            File thisFile = new File(mLastFilePath);
            String parentFilePath = thisFile.getParent();
            updateFileItems(parentFilePath);
        }
        //是根路径 ，直接结束，退出activity
        else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}