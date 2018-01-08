package com.example.termproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termproject.BDFY.TransCallBack;
import com.example.termproject.BDFY.TransResult;
import com.example.termproject.BDFY.Utils;
import com.example.termproject.R;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by qingming on 2018/1/8.
 */

public class TranslateActivity extends AppCompatActivity {
    private static final String APP_ID = "20180104000112113";
    private static final String SECURITY_KEY = "rqZAT15_NrEgO_bESngi";
    //private TransApi api;
    private Bundle bundle;
    private String query = "请输入翻译内容";
    private String from = "auto";
    private String To = "en";
    private Spinner mspinner;
    private ArrayAdapter<String> madapter;
    private TextView showT ;//= (TextView)findViewById(R.id.showT);
    private EditText Query ;//= (EditText)findViewById(R.id.query);
    private Button confirm;
    private Button clear;
    private String sign;
    private String  salt = "1345660288";

    final String[] language = { "英语","日语","中文","粤语","文言文","法语","俄语","韩语","繁体中文","西班牙语","阿拉伯语","意大利语","德语","泰语","希腊语","越南语" };
    final String[] languageId = { "en","jp","zh","yue","wyw","fra","ru","kor","cht","spa","ara","it","de","th","el","vie" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        initView();

    }

    private void initView() {
        Query = (EditText) findViewById(R.id.query);
        showT = (TextView) findViewById(R.id.showT);
        confirm = (Button)findViewById(R.id.t1) ;
        clear = (Button) findViewById(R.id.t2) ;

        bundle = getIntent().getExtras();
        String translateContent = bundle.getString("translateContent");
        if(translateContent!=null) Query.setText(bundle.getString("translateContent"));

        //创建下拉框，选择翻译语种
        mspinner = (Spinner) findViewById(R.id.spinner);
        madapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, language);
        madapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        mspinner.setAdapter(madapter);
        //下拉框监听器
        mspinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                To = languageId[position];
                //Toast.makeText(MainActivity.this,"TO:"+To,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showT.setText("");
                Query.setText("");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealWithField(Query.getText().toString());
                OkHttpUtils
                        .get()
                        .url("http://api.fanyi.baidu.com/api/trans/vip/translate")
                        .addParams("q", query)
                        .addParams("from", from)
                        .addParams("to", To)
                        .addParams("appid", APP_ID)
                        .addParams("salt", salt)
                        .addParams("sign", sign)
                        .build()
                        .execute(new TransCallBack() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e, int id) {
                                Toast.makeText(TranslateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onResponse(TransResult response, int id) {
                                showT.setText(response.getTrans_result().get(0).getDst());
                            }
                        });
            }
        });
    }

    private void dealWithField(String str) {
        if (str != null && str.length() > 0) {
            query = str;
        }
        query = Utils.toUtf8(query);
        String l = APP_ID + query + salt + SECURITY_KEY;
        sign = Utils.encrypt(l);

        query = Utils.toURLDecoded(query);
        from = Utils.toURLDecoded(from);
        To = Utils.toURLDecoded(To);
    }

}