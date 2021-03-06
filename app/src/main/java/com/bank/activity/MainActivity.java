package com.bank.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bank.R;
import com.bank.adapter.RecycleAdapter;
import com.bank.model.IndexModel;
import com.bank.model.UserModel;
import com.bank.net.EBankRequest;
import com.bank.util.SharedPreferencesFactory;
import com.bank.widget.LoadMoreRecyclerView;
import com.bank.widget.LoginDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //------------My------------------
    private static final String TAG = "MainActivity";
    private LoadMoreRecyclerView recyclerView;
    private ArrayList<IndexModel> mDatas = new ArrayList<IndexModel>();
    private RecycleAdapter recycleAdapter;
    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private StringCallback EntryListDataBack,LoginBack;
    private int page;
    private ImageView userface,login_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //初始化登陆按钮
        View headview = (View) navigationView.getHeaderView(0);
        userface = (ImageView) headview.findViewById(R.id.imageView);
        login_out = (ImageView) headview.findViewById(R.id.login_out);
        login_out.setOnClickListener(this);
        userface.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //-------------------------My-------------------------------------
        //初始化RecyclerView
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.id_recyclerview);
        //设置布局管理器 线性
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //卡片布局
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //横向卡片布局 支持瀑布流
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        recycleAdapter = new RecycleAdapter(mDatas, this);
        recyclerView.setAdapter(recycleAdapter);
        //设置分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemAnimator(null);
        recyclerView.setAutoLoadMoreEnable(true);
        recycleAdapter.setOnItemClickLitener(new RecycleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,EntryDetailsActivity.class);
                intent.putExtra("index_content",mDatas.get(position).getIndex_content());
                intent.putExtra("index_title",mDatas.get(position).getIndex_title());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, (position + 1) + " 长按点击了", Toast.LENGTH_LONG).show();
            }
        });
        //初始化刷新布局
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EBankRequest.getEnteryList(page, EntryListDataBack);
            }
        });
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                EBankRequest.getEnteryList(page, EntryListDataBack);
            }
        });
        //请求数据
        InitDataBack();
        EBankRequest.getEnteryList(page, EntryListDataBack);
    }

    private void InitDataBack() {
        EntryListDataBack = new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                JSONObject jo = JSON.parseObject(response);
                if (page == 0) {
                    mDatas.clear();
                }
                recyclerView.notifyMoreFinish(true);
                mDatas.addAll(JSON.parseArray(jo.getJSONArray("rows").toJSONString(), IndexModel.class));
                recycleAdapter.notifyDataSetChanged();
           }
        };

        LoginBack = new StringCallback(){
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jo = JSON.parseObject(response);
                if (jo.getInteger("error")==200){
                    UserModel um = JSON.parseObject(jo.getString("userinfo"), UserModel.class);

                    SharedPreferencesFactory.
                            getSharedPreferencesUtils(MainActivity.this,
                                    SharedPreferencesFactory.USERINFO)
                                        .setObject("user",um);
//                    Log.d(TAG, SharedPreferencesFactory.
//                            getSharedPreferencesUtils(MainActivity.this,
//                                    SharedPreferencesFactory.USERINFO)
//                                        .getObject("user", UserModel.class)+"");
                }else{
                    Toast.makeText(MainActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                }

            }
        };
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bank/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bank/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                LoginDialog.Builder builder = new LoginDialog.Builder(this);
                builder.setLoginButton(new LoginDialog.Builder.LoginButtonClickListener() {
                    @Override
                    public void Login(String account, String password, String code) {
                        Log.d(TAG, "Login() called with: " + "account = [" + account + "], password = [" + password + "], code = [" + code + "]");
                        EBankRequest.login(LoginBack,account,password,code);
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.login_out:
                AlertDialog.Builder islogout = new AlertDialog.Builder(this);
                islogout.setMessage("确认退出吗?");
                islogout.setTitle("退出登陆");
                islogout.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                islogout.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                islogout.create().show();
                break;
        }
    }

}
