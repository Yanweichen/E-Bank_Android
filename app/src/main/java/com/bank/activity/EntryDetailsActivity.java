package com.bank.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.bank.R;

/**
 * Created by yanwe on 2016/5/3.
 */
public class EntryDetailsActivity extends AppCompatActivity {

    private WebView index_content;
    private static final String TAG = "EntryDetailsActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrydetails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        toolbar.setTitle(getIntent().getStringExtra("index_title"));
        setSupportActionBar(toolbar);//设置标题变色
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置默认返回键
        index_content = (WebView) findViewById(R.id.index_content);
        String content = getIntent().getStringExtra("index_content").replace("<img s", "<img width=\"100%\" s");
        index_content.loadData(content, "text/html; charset=UTF-8",null);
    }
}
