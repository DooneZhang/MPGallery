package cn.mepstudio.mpgallery.album.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.beiing.gifmaker.gifmake.GifMakeActivity;
import com.beiing.gifmaker.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.album.adapter.FragmnetAdapter;
import cn.mepstudio.mpgallery.album.base.BaseActivity;
import cn.mepstudio.mpgallery.album.fragment.AlbumFragment;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,EasyPermissions.PermissionCallbacks {

    private static final int RC_EXTERNAL_STORAGE = 0x02;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected int getContentView() {
        return R.layout.album_activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //询问是否有读取相册的权限
        requestExternalStorage();
    }

    private void handleView() {
        try {
            Fragment fragment = Fragment.instantiate(this, AlbumFragment.class.getName());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, fragment)
                    .commitNowAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            handleView();
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        handleView();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_main);
        mToolbar = findViewById(R.id.toolbar);
        initToolbar();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


       // initViewPager();

        FloatingActionButton fab = findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText Etext = new EditText(MainActivity.this);
              //  Etext.setText("搜索");//这里更改输入框里的内容
                Etext.requestFocus(); //输入框激活
                Etext.setSelection(Etext.getText().length()); //将输入点挪到最后
                new  AlertDialog.Builder(MainActivity.this)

                        .setTitle("搜索" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(Etext)
                        .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 点击确认传递文件夹名
                              //  rename_path = Etext.getText().toString();
                                Toast tot = Toast.makeText(MainActivity.this,"请先等待软件自动分析扫描后再搜索！",Toast.LENGTH_LONG);tot.show();
                            }
                        })
                        .setNegativeButton("取消" ,  null )
                        .show();


            }
        });

    }
    /*

    select_album0= findViewById(R.id.select_album);
    public void snow_album() {
        if (select_album0.getVisibility() == View.GONE) {
            select_album0.setVisibility(View.VISIBLE);
        } else {
            select_album0.setVisibility(View.GONE);
        }
    }
*/
    private void initToolbar() {
        //设置menu
        mToolbar.inflateMenu(R.menu.menu_toolbar);
        //设置menu的点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int menuItemId = item.getItemId();
                if (menuItemId == R.id.menu_photo) {

                } else if (menuItemId == R.id.menu_class) {
                    startActivity(new Intent(MainActivity.this, cn.mepstudio.mpgallery.optimize.activity.MainActivity.class));
                    //点击弹出相册按钮
               //     snow_album();

                } else if (menuItemId == R.id.menu_album) {
                    RecyclerView rv_class= findViewById(R.id.rv_imageslist);

                    if (rv_class.getVisibility() == View.GONE) {
                            rv_class.setVisibility(View.VISIBLE);
                        } else {
                            rv_class.setVisibility(View.GONE);
                        }


                } else if (menuItemId == R.id.menu_story) {
                    startActivity(new Intent(MainActivity.this, GifMakeActivity.class));
                }else if (menuItemId == R.id.menu_settings) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                }

                return true;
            }
        });
    }

        /*
        //设置左侧NavigationIcon点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了左侧按钮", Toast.LENGTH_SHORT).show();
            }
        });
*/
    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        ArrayList<String> titles = new ArrayList<>();
        titles.add("精选");
        titles.add("体育");
        titles.add("巴萨");
        titles.add("购物");
        titles.add("明星");
        titles.add("视屏");
        titles.add("健康");

        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
            fragments.add(new ListFragment());
        }
        FragmnetAdapter testFragmnetAdapter = new FragmnetAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(testFragmnetAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(testFragmnetAdapter);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_optimize) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, cn.mepstudio.mpgallery.optimize.activity.MainActivity.class);
            startActivity(intent);
            return true;
        }

*/
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_backup) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,WebActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_today) {
            Toast tot = Toast.makeText(MainActivity.this,"暂无去年今日！",Toast.LENGTH_LONG);tot.show();
        } else if (id == R.id.nav_compress) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,cn.mepstudio.mpgallery.compressor.CompressActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
