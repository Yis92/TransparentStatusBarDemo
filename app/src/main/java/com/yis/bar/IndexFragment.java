package com.yis.bar;


import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by yis on 2018/5/21.
 */

public class IndexFragment extends Fragment {

    private WebView mWeb;
    private View viewBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        mWeb = view.findViewById(R.id.web_view);
        viewBar = view.findViewById(R.id.view_bar);


        // 设置可以支持缩放
        mWeb.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWeb.getSettings().setBuiltInZoomControls(true);

        // 设置WebViewClient，保证新的链接地址不打开系统的浏览器窗口
        mWeb.setWebViewClient(new WebViewClient());
        // 设置WebView支持运行普通的Javascript
        mWeb.getSettings().setJavaScriptEnabled(true);
        //加载视频需要
        mWeb.getSettings().setPluginState(WebSettings.PluginState.ON);// 支持插件
        mWeb.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小  无效

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWeb.getSettings().setMixedContentMode(mWeb.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWeb.getSettings().setLoadsImagesAutomatically(true);  //支持自动加载图片
        mWeb.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //填充一个状态栏
                if (url.equals("https://www.jianshu.com/p/80298c02dfc5?utm_campaign=hugo&utm_medium=reader_share&utm_content=note&utm_source=weixin-friends")) {
                    viewBar.setVisibility(View.GONE);
                } else {
                    viewBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });


        mWeb.loadUrl("https://www.jianshu.com/p/80298c02dfc5?utm_campaign=hugo&utm_medium=reader_share&utm_content=note&utm_source=weixin-friends");
        return view;
    }
}
