package com.example.termproject.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termproject.Model.Book;
import com.example.termproject.Model.Chapter;
import com.example.termproject.R;
import com.example.termproject.Tools.BookDBHelper;
import com.example.termproject.ViewAdapter.RecyclerViewAdapter;
import com.example.termproject.ViewAdapter.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Book> listItems = new ArrayList<>();
    private FloatingActionButton fab;
    private BookDBHelper bookDBHelper;

    //分享对话框alertDialogShare的VIew声明
    private View view_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //变量实例化赋值
        bookDBHelper = new BookDBHelper(this, BookDBHelper.DB_NAME, null, 1);

        /*添加小说弹出对话框*/
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final String mitems[] = {"从文件夹中选择小说"};
        alertDialog.setTitle("添加小说")
                .setItems(mitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mitems[i].equals("从文件夹中选择小说")) {
                            /*跳转至Repository model*/
                            Intent intent=new Intent(MainActivity.this,FileScannerActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(getApplication(), "你点击了取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                .create();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listItems.clear();
        listItems.addAll(bookDBHelper.selectAllToBookList());
        recycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.text_size) {
            final AlertDialog.Builder sizeAlertDialog = new AlertDialog.Builder(this);
            final String mitems[] = {"字体：超大", "字体：大", "字体：适中","字体：小","字体：超小"};
            sizeAlertDialog.setTitle("设置字体大小")
                    .setItems(mitems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            if (mitems[i].equals("字体：超大")) {
                                editor.putString("字体","超大");
                            }
                            else if (mitems[i].equals("字体：大")) {
                                editor.putString("字体","大");
                            }
                            else if (mitems[i].equals("字体：适中")) {
                                editor.putString("字体","适中");
                            }
                            else if (mitems[i].equals("字体：小")) {
                                editor.putString("字体","小");
                            }
                            else if (mitems[i].equals("字体：超小")) {
                                editor.putString("字体","超小");
                            }
                            editor.apply();
                        }
                    })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(getApplication(), "你点击了取消", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create().show();
            return true;
        }
        else if(id == R.id.day_night){
            final AlertDialog.Builder day_nightAlertDialog = new AlertDialog.Builder(this);
            final String mitems[] = {"日间模式", "夜间模式"};
            day_nightAlertDialog.setTitle("设置阅读模式")
                    .setItems(mitems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            if (mitems[i].equals("日间模式")) {
                                /*跳转至Repository model*/
                                editor.putString("模式","日间模式");
                            }
                            else if (mitems[i].equals("夜间模式")) {
                                editor.putString("模式","夜间模式");
                            }
                            editor.apply();
                        }
                    })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(getApplication(), "你点击了取消", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_file) {
            // 导入小说
            /*跳转至Repository model*/
            Intent intent=new Intent(MainActivity.this,FileScannerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_translator) {
            //跳转至翻译助手
            /*跳转至Repository model*/
            Intent intent=new Intent(MainActivity.this,TranslateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("translateContent","");
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            //显示对话框，选择文件分享
            //TODO
            //设置对话框的布局
            //短按点击事件显示的修改信息的对话框alertDialogUpdate声明
            LayoutInflater inflater = LayoutInflater.from(this);// 渲染器
            View customDialogView = inflater.inflate(R.layout.recycler_in_dialog,null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });

            RecyclerView recyclerInDialog = (RecyclerView) customDialogView
                    .findViewById(R.id.RecyclerViewInDialog);
            //RecyclerView实现book列表，实例化LayoutManager和RecycleAdapter
            RecyclerView.LayoutManager layoutManagerInDialog = new LinearLayoutManager(this);
            final RecyclerViewAdapter adapterInDialog = new RecyclerViewAdapter<Book>(this,R.layout.cardview_in_list,listItems) {
                //重写convert函数设置每个条目中的
                @Override
                public void convert(ViewHolder holder, Book b) {
                    TextView folderName = holder.getView(R.id.folderName);
                    ImageView folderImg = holder.getView(R.id.folderImg);
                    folderImg.setImageResource(R.mipmap.bookicon1);
                    folderName.setText(b.getBookName());
                    folderName.setSingleLine();
                    folderName.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                }
            };
            adapterInDialog.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
                @Override
                public void onClick(View view, int position){
                /*跳转至阅读界面*/
                    if(listItems.get(position).getBookTxtPath()!=null && !listItems.get(position).getBookTxtPath().isEmpty()) {
                        share(listItems.get(position).getBookTxtPath());
                    }
                    else{
                        Toast.makeText(getApplication(),"文件不存在!",Toast.LENGTH_LONG);
                        listItems.remove(position);
                        adapterInDialog.notifyDataSetChanged();
                    }
                }
                @Override
                public boolean onLongClick(View view,final int position){

                    return true;
                }
            });
            // 设置布局管理器
            recyclerInDialog.setLayoutManager(layoutManagerInDialog);
            // 设置adapter
            recyclerInDialog.setAdapter(adapterInDialog);
            builder.setTitle("选择小说");
            // builder.setTitle("意见模版");
            builder.setView(customDialogView);
            builder.create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRecyclerView(){
        //RecyclerView实现book列表，实例化LayoutManager和RecycleAdapter
        layoutManager = new GridLayoutManager(this,3);
        recycleAdapter = new RecyclerViewAdapter<Book>(this,R.layout.cardview_in_grid,listItems) {
            //重写convert函数设置每个条目中的
            @Override
            public void convert(ViewHolder holder, Book b) {
                TextView bookName = holder.getView(R.id.bookName);
                ImageView bookImg = holder.getView(R.id.bookImg);
                TextView bookProgress = holder.getView(R.id.bookProgress);
                bookName.setText(b.getBookName());
                if(b.getBookImgPath().isEmpty())bookImg.setImageResource(R.mipmap.bookicon1);
                else bookImg.setImageURI(Uri.fromFile(new File(b.getBookImgPath())));
                String percent = b.getPercentage()+"%";
                bookProgress.setText(percent);
                bookName.setSingleLine();
                bookName.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            }
        };
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewInMain);
        recycleAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){
            @Override
            public void onClick(View view, int position){
                /*跳转至阅读界面*/
                if(listItems.get(position).getBookTxtPath()!=null && !listItems.get(position).getBookTxtPath().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, BookContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bookName", listItems.get(position).getBookName());
                    bundle.putString("bookTxtPath", listItems.get(position).getBookTxtPath());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplication(),"文件不存在!",Toast.LENGTH_LONG);
                    listItems.remove(position);
                    recycleAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public boolean onLongClick(View view,final int position){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("从列表中删除小说?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                bookDBHelper.delete(listItems.get(position).getBookName());
                                bookDBHelper.deleteChapter(listItems.get(position).getBookName());
                                listItems.remove(position);
                                recycleAdapter.notifyItemRemoved(position);
                                recycleAdapter.notifyItemChanged(position);
                                recycleAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this,"已删除", Toast.LENGTH_SHORT).show();
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

    //分享文件
    public void share(String path){
        File file = new File(path);
        /** * 分享文字内容 */
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType( "*/*" );//设置分享内容的类型
        //share_intent.setType("text/plain");//设置分享内容的类型
        // 比如发送二进制文件数据流内容（比如图片、视频、音频文件等等）
        // 指定发送的内容 (EXTRA_STREAM 对于文件 Uri )
        share_intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享至...");
        startActivity(share_intent);
    }
}
