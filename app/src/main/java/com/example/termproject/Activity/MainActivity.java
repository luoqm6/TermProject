package com.example.termproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //变量实例化赋值
        bookDBHelper = new BookDBHelper(this, BookDBHelper.DB_NAME, null, 1);

        /*浮动按钮添加文件方式弹出对话框*/
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final String mitems[] = {"从文件夹中选择txt", "自行设置"};
        alertDialog.setTitle("添加电子书")
                .setItems(mitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mitems[i].equals("从文件夹中选择txt")) {
                            /*跳转至Repository model*/
                            Intent intent=new Intent(MainActivity.this,FileScannerActivity.class);
                            startActivity(intent);
                        }
                        else if (mitems[i].equals("自行设置")) {

                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplication(), "你点击了取消", Toast.LENGTH_SHORT).show();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
                /*跳转至Repository model*/
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
                builder.setMessage("从列表中删除图书?")
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
}
