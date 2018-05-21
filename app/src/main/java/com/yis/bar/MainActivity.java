package com.yis.bar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvIndex;
    private TextView tvUser;

    private List<Fragment> fragments;
    private Fragment fragment;
    private FragmentTransaction ft;

    private List<TextView> textViews;


    private int currentTab; // 当前Tab页面索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //解决7.0+状态栏灰色背景 最好在setContentView前处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {

            }
        }

        //6.0以上的操作系统字体默认是白色，使用以下代码设置成黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_main);

        setTranslucentStatus(true);


        initView();
        initData();
        initListener();
    }


    public void initView() {
        tvIndex = findViewById(R.id.tv_index);
        tvUser = findViewById(R.id.tv_user);
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new IndexFragment());
        fragments.add(new UserFragment());

        textViews = new ArrayList<>();
        textViews.add(tvIndex);
        textViews.add(tvUser);

        show(tvIndex, 0);

    }

    private void initListener() {
        //切换底部菜单
        tvIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置为默认状态栏字体颜色
//                getWindow().getDecorView().setSystemUiVisibility(0);
                show(tvIndex, 0);
            }
        });


        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                show(tvUser, 1);
            }
        });
    }


    /**
     * 切换底部菜单栏
     *
     * @param textView 点击的tab
     * @param idx      当前位置
     */
    public void show(TextView textView, int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            fragment = fragments.get(i);
            ft = getSupportFragmentManager().beginTransaction();

            getCurrentFragment().onPause(); // 暂停当前tab
            if (idx == i) {
                if (fragment.isAdded()) {
                    fragment.onResume(); // 启动目标tab的onResume()
                } else {
                    ft.add(R.id.fl_content, fragment);
                }

                Drawable drawable = null;
                if (idx == 0) {
                    drawable = getResources().getDrawable(R.drawable.tab_index);
                } else {
                    drawable = getResources().getDrawable(R.drawable.tab_user);
                }
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, drawable, null, null);
                textView.setTextColor(getResources().getColor(R.color.color_main));
                textView.setSelected(true);
                ft.show(fragment);
            } else {
                textViews.get(i).setTextColor(getResources().getColor(R.color.color_666666));
                textViews.get(i).setSelected(false);
                ft.hide(fragment);
            }

            ft.commit();
            currentTab = idx; // 更新目标tab为当前tab
        }
    }

    /**
     * 获取当前Fragment
     *
     * @return
     */
    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    /***********************************************透明式状态栏*****************************************************/
    protected void setTranslucentStatus(boolean on) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            //透明状态栏
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }


}
