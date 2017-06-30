package com.xmx.mh.core.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xmx.mh.base.activity.BaseActivity;
import com.xmx.mh.core.fragments.HistoryFragment;
import com.xmx.mh.core.fragments.HotFragment;
import com.xmx.mh.core.fragments.InternationalFragment;
import com.xmx.mh.core.fragments.MilitaryFragment;
import com.xmx.mh.core.fragments.ProductsFragment;
import com.xmx.mh.module.collect.CollectActivity;
import com.xmx.mh.module.log.OperationLogActivity;
import com.xmx.mh.core.Constants;
import com.xmx.mh.R;
import com.xmx.mh.core.fragments.RecommendFragment;
import com.xmx.mh.core.PagerAdapter;
import com.xmx.mh.module.user.LoginActivity;
import com.xmx.mh.module.user.LoginEvent;
import com.xmx.mh.module.user.UserCallback;
import com.xmx.mh.module.user.UserManager;
import com.xmx.mh.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // 第一次点击返回键时间，在一定时间内再次点击则退出程序
    private long mExitTime = 0;
    // 分页Fragment容器
    ViewPager vp;
    // 侧滑菜单登录菜单项
    MenuItem login;

    int LOGIN_REQUEST_CODE = 101;

    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        // 初始化侧滑菜单
        initDrawerNavigation();

        // Fragment列表
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new HotFragment());
        fragments.add(new MilitaryFragment());
        fragments.add(new InternationalFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new ProductsFragment());

        // Fragment对应的标题
        List<String> titles = new ArrayList<>();
        titles.add("推荐");
        titles.add("热点");
        titles.add("军事");
        titles.add("国际");
        titles.add("历史");
        titles.add("军品");

        // 分页Fragment适配器
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);

        vp = getViewById(R.id.view_pager);
        vp.setAdapter(adapter);
        // 设置标签页底部选项卡
        TabLayout tabLayout = getViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vp);

//        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_launcher);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 设置侧滑菜单
        NavigationView navigation = getViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        login = menu.findItem(R.id.nav_logout);
        userManager.autoLogin(new UserCallback() {
            @Override
            public void success(int id, String nickname) {
                login.setTitle(nickname + " 点击注销");
            }

            @Override
            public void fail(String prompt) {
                showToast(prompt);
            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.normalException(e, MainActivity.this);
            }
        });
    }

    /**
     * 第一次点击返回键弹出确认信息，再次点击则退出程序
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * 初始化侧边栏
     */
    protected void initDrawerNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // 处理登录返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            // 登录成功
            checkLogin();
        }
    }

    private void checkLogin() {
        userManager.checkLogin(new UserCallback() {
            @Override
            public void success(int id, String nickname) {
                login.setTitle(nickname + " 点击注销");
            }

            @Override
            public void fail(String prompt) {
                showToast(prompt);
            }

            @Override
            public void error(Exception e) {
                ExceptionUtil.normalException(e, MainActivity.this);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_recommend: // 推荐
                vp.setCurrentItem(0);
                break;
            case R.id.nav_hot: // 热点
                vp.setCurrentItem(1);
                break;
            case R.id.nav_military: // 军事
                vp.setCurrentItem(2);
                break;
            case R.id.nav_international: // 国际
                vp.setCurrentItem(3);
                break;
            case R.id.nav_history: // 历史
                vp.setCurrentItem(4);
                break;
            case R.id.nav_products: // 军品
                vp.setCurrentItem(5);
                break;
            case R.id.nav_log: // 设置
                startActivity(OperationLogActivity.class);
                break;
            case R.id.nav_logout: // 登录/注销
                if (userManager.isLogin()) {
                    new AlertDialog.Builder(this)
                            .setMessage("确定要注销吗")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    userManager.logout();
                                    login.setTitle("登录");
                                }
                            })
                            .show();
                } else {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                            LOGIN_REQUEST_CODE);
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // 设置菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_collection) {
            startActivity(CollectActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEvent(LoginEvent event) {
        checkLogin();
    }
}
