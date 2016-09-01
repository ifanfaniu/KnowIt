package com.work.ifanfan.knowit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.work.ifanfan.knowit.model.MyContent;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/4.
 */
public class ContentActivity extends AppCompatActivity {

    private String mPicUrl;
    private String mUrl;
    private Toolbar cToolbar;
    private Context mContext;
    private ImageView mImageView;
    private TextView mContentView;
    private String mShare;
    private String css;
    private static final String USE ="mContent";
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 判断并加载主题
         */

        if(MyApp.isLightMode()){
            this.setTheme(com.work.ifanfan.knowit.R.style.ThemeDay);
        }else{
            this.setTheme(com.work.ifanfan.knowit.R.style.ThemeNight);
        }
        setContentView(com.work.ifanfan.knowit.R.layout.m_content);

        mImageView = (ImageView) findViewById(com.work.ifanfan.knowit.R.id.content_ig);
        mContentView = (TextView) findViewById(com.work.ifanfan.knowit.R.id.content_tv);
        cToolbar = (Toolbar) findViewById(com.work.ifanfan.knowit.R.id.c_toolbar);
        mWebView = (WebView) findViewById(com.work.ifanfan.knowit.R.id.web_view);

        mContext = ContentActivity.this;
        mUrl = "http://news-at.zhihu.com/api/4/news/"+getIntent().getStringExtra(USE);
        //设置Toolbar
        setSupportActionBar(cToolbar);
        cToolbar.setNavigationIcon(com.work.ifanfan.knowit.R.drawable.mback);
        cToolbar.setTitleTextColor(Color.argb(0,255,255,255));
        cToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //对WebView的一系列操作
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        //优先使用缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        //new一个HttpUtil对象并实现其getHttp();方法
        HttpUtil cHttpUtil = new HttpUtil();
        cHttpUtil.getHttp(mUrl,cStringCallback);
    }

    /**
     * 获取新闻内容
     * 加载内容
     */
    StringCallback cStringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(String response, int id) {
            //将获取的Response类型数据转换成Gson
            Gson gson = new Gson();
            MyContent myContent = gson.fromJson(response,MyContent.class);
            //分享内容
            mShare = myContent.getShare_url();
            //头部容器图片
            mPicUrl = myContent.getImage();
            Picasso.with(mImageView.getContext())
                    .load(mPicUrl)
                    .into(mImageView);

            /**
             * 判断主题模式
             * 选择对应css样式
             */
            if (MyApp.isLightMode()){
                css = "<link rel=\"stylesheet\" href=\"file:///android_asset/style_day.css\" type=\"text/css\">";
            }else {
                css = "<link rel=\"stylesheet\" href=\"file:///android_asset/news.css\" type=\"text/css\">"
                        + "<link rel=\"stylesheet\" href=\"file:///android_asset/style_night.css\" type=\"text/css\">";
            }

            String newContent = "<html><head>" + css + "</head><body>" + myContent.getBody() + "</body></html>";
            newContent = newContent.replace("<div class=\"img-place-holder\">","");
            newContent = newContent.replace("<div class=\"headline\">","");
            mContentView.setText(myContent.getTitle());
            mWebView.loadDataWithBaseURL("x-data://base",newContent,"text/html","UTF-8",null);

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.work.ifanfan.knowit.R.menu.menu_content,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.work.ifanfan.knowit.R.id.share) {
            shareText(mContext);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 分享功能
     * @param view
     */
    public void shareText(Context view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,mShare );
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享"));
    }
    /**
     *传递参数id
     * @param context
     * @param id
     * @return
     */

    public static Intent from(Context context,String id){
        Intent intent = new Intent(context,ContentActivity.class);
        intent.putExtra(USE,id);
        return intent;
    }

}
