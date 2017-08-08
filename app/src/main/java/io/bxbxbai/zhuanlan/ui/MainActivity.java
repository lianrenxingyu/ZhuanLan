package io.bxbxbai.zhuanlan.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.InputEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.bxbxbai.common.Tips;
import io.bxbxbai.common.utils.CommonExecutor;
import io.bxbxbai.common.utils.PrefUtils;
import io.bxbxbai.zhuanlan.R;
import io.bxbxbai.zhuanlan.widget.DrawerMenuContent;
import io.bxbxbai.zhuanlan.widget.MenuAdapter;
import io.bxbxbai.zhuanlan.widget.OnMenuListClickListener;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawer_list)
    protected ListView listView;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;

//    一个抽屉型的actionbar，是原始抽屉和toolbar的结合实现，toggle，有切换的意思
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        ChoreographerHelper.getInstance(this).start();
        initToolbarAndDrawer();

//      自定义的一个类，抽屉菜单内容
        DrawerMenuContent content = new DrawerMenuContent(this);

//      设置listView适配器，以及监听接口，listview真是贼难
        listView.setAdapter(new MenuAdapter(this, content.getItems()));
        listView.setOnItemClickListener(new OnMenuListClickListener(this, drawerLayout));

//        这是一个fragment添加，把主界面的人物信息fragment碎片加入主布局中的帧布局
        getSupportFragmentManager().beginTransaction().add(R.id.container,
                PeopleListFragment.newInstance()).commit();

        //第一次启动，会打开抽屉菜单
        CommonExecutor.MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if ((boolean) PrefUtils.getValue(MainActivity.this, PrefUtils.KEY_FIRST_ENTER, true)) {
                    CommonExecutor.MAIN_HANDLER.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openDrawer();
                        }
                    }, 1500);
                    PrefUtils.setValue(MainActivity.this, PrefUtils.KEY_FIRST_ENTER, false);
                }
            }
        });
//        NewsDetailActivity.startActivity(this, "http://music.163.com/m/topic/194001?type=android");
    }

    private void initToolbarAndDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//       这一个设置toolbar的监听没有意义，因为点击事件并不会执行这个方法，
//       我也不知道为什么,这是一个bug，toolbar的监听方法由onOptionsItemSelected实现
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Tips.showToast("Coming soon...");  //自定义的一个toast类
                        break;
                    case R.id.action_about:
//                        打开一个“关于我的界面”
                        return WebActivity.start(MainActivity.this, WebActivity.URL_BXBXBAI, "About Me");
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        closeDrawer();
        // Otherwise, it may return to the previous fragment stack
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Lastly, it will rely on the system behavior for back
            super.onBackPressed();
        }
    }

    public boolean closeDrawer() {
        // If the drawer is open, back will close it
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    private boolean openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }
//  显示toolbar中的菜单选项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Tips.showToast("Coming soon...");  //自定义的一个toast类
                break;
            case R.id.action_about:
//   打开一个“关于我的界面”
                return WebActivity.start(MainActivity.this, WebActivity.URL_BXBXBAI, "About Me");
        }
        return true;
    }

    //    这是一个启动activity的很好的写法，书上见过，简化更容易理解
    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }
}