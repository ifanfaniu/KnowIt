package com.work.ifanfan.knowit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.work.ifanfan.knowit.model.News;
import com.yalantis.phoenix.PullToRefreshView;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.Calendar;


import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView yRecyclerView;
    private ZhihuAdapter mZhihuAdapter;
    private ZhihuAdapter yZhihuAdapter;
    private HttpUtil mHttpUtil;
    private Toolbar mainToolbar;

    private PullToRefreshView mPullToRefreshView;
    private Context mContext;
    private String url;
    private String yUrl;

    private Gson tGson;
    private Gson yGson;
    private News daily;
    private News yesterdayNews;

    private ViewPager mViewPager;
    private ImageView[] mIndicator;
    private TextView mTitle;
    private TextView yesterday;
    private int mBannerPosition = 0;
    private BannerAdapter mBannerAdapter;
    private final int DEFAULT_BANNER_SIZE = 5;
    private final int FAKE_BANNER_SIZE = 100;
    private boolean mIsUserTouched = false;
    private Timer mTimer = new Timer();
    private final int REFRESH_DELAY = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MyApp.isLightMode()){
            this.setTheme(com.work.ifanfan.knowit.R.style.ThemeDay);
        }else{
            this.setTheme(com.work.ifanfan.knowit.R.style.ThemeNight);
        }
        setContentView(com.work.ifanfan.knowit.R.layout.activity_main);

        init();

        mContext = MainActivity.this;
        mainToolbar.setTitle("首页");
        mainToolbar.setTitleTextColor(ContextCompat.getColor(this, com.work.ifanfan.knowit.R.color.cardview_light_background));
        setSupportActionBar(mainToolbar);
        mainToolbar.setNavigationIcon(com.work.ifanfan.knowit.R.drawable.mmenu);

        /**
         * 下拉刷新
         */
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
                HttpUtil rHttpUtil = new HttpUtil();
                rHttpUtil.getHttp(url, mStringCallback);
            }
        });


        //设置两个RecycleView
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        yRecyclerView.setNestedScrollingEnabled(false);
        yRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //轮播控件的定时器
        mTimer.schedule(mTimerTask, 10000, 10000);

        //网络请求
        getUrl();
        mHttpUtil = new HttpUtil();
        mHttpUtil.getHttp(url, mStringCallback);
        mHttpUtil.getHttp(yUrl,yStringCallback);


        setmViewPager();
    }
    private void getUrl(){

        url = "http://news-at.zhihu.com/api/4/news/latest";

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);//获取年份
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH) - 1;
        int dayUrl = date.get(Calendar.DAY_OF_MONTH);
        String mDate1 = String.valueOf(year);
        String mDate2 = String.valueOf(month);
        String mDate3 = String.valueOf(day);
        String mDate4 = String.valueOf(dayUrl);
        yesterday = (TextView) findViewById(com.work.ifanfan.knowit.R.id.tv_yesterday);
        yesterday.setText(mDate1 + "年" + mDate2 + "月" + mDate3 + "日");
        if (month < 10) {
            yUrl = "http://news.at.zhihu.com/api/4/news/before/" + mDate1 + "0" + mDate2 + mDate4;
        } else {
            yUrl = "http://news.at.zhihu.com/api/4/news/before/" + mDate1 + mDate2 + mDate4;
        }

    }

    /**
     * 数据类型转化并传给Adapter
     */
    StringCallback mStringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
        }
        @Override
        public void onResponse(String response, int id) {
            tGson = new Gson();
            daily = tGson.fromJson(response, News.class);
            mBannerAdapter = new BannerAdapter(MainActivity.this, daily.getTop_stories(),mViewPager,DEFAULT_BANNER_SIZE,FAKE_BANNER_SIZE);
            mViewPager.setAdapter(mBannerAdapter);
            mZhihuAdapter = new ZhihuAdapter(daily, new CallBack() {
                @Override
                public void onItemClicked(String name) {
                    goContent(name);
                }
            });
            mRecyclerView.setAdapter(mZhihuAdapter);
        }
    };
    StringCallback yStringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
        }
        @Override
        public void onResponse(String response, int id) {
            yGson = new Gson();
            yesterdayNews = yGson.fromJson(response, News.class);

            yZhihuAdapter = new ZhihuAdapter(yesterdayNews, new CallBack() {
                @Override
                public void onItemClicked(String name) {
                    goContent(name);
                }
            });
            yRecyclerView.setAdapter(yZhihuAdapter);

        }
    };

    /**
     * 监听回调接口
     */
    public interface CallBack {
        void onItemClicked(String name);
    }

    /**
     * 连接MainActivity于ContentActivity
     * @param name
     */
    public void goContent(String name) {
        startActivity(ContentActivity.from(mContext, name));

    }

    /**
     * 初始化
     */

    private void init() {
        mIndicator = new ImageView[]{
                (ImageView) findViewById(com.work.ifanfan.knowit.R.id.dot_indicator1),
                (ImageView) findViewById(com.work.ifanfan.knowit.R.id.dot_indicator2),
                (ImageView) findViewById(com.work.ifanfan.knowit.R.id.dot_indicator3),
                (ImageView) findViewById(com.work.ifanfan.knowit.R.id.dot_indicator4),
                (ImageView) findViewById(com.work.ifanfan.knowit.R.id.dot_indicator5),
        };
        mViewPager = (ViewPager) findViewById(com.work.ifanfan.knowit.R.id.view_pager);
        mTitle = (TextView) findViewById(com.work.ifanfan.knowit.R.id.title);
        mainToolbar = (Toolbar) findViewById(com.work.ifanfan.knowit.R.id.main_toolbar);
        mPullToRefreshView = (PullToRefreshView) findViewById(com.work.ifanfan.knowit.R.id.pull_to_refresh);
        mRecyclerView = (RecyclerView) findViewById(com.work.ifanfan.knowit.R.id.rv_main);
        yRecyclerView = (RecyclerView) findViewById(com.work.ifanfan.knowit.R.id.rv_yesterday);
    }

    /**
     * 设置轮播控件的ViewPager
     */
    private void setmViewPager() {
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mBannerPosition = position;
                setIndicator(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 定时器
     */
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
                /**
                 * Android在子线程更新UI的几种方法
                 * Handler，AsyncTask,view.post,runOnUiThread
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
                            mViewPager.setCurrentItem(DEFAULT_BANNER_SIZE - 1, false);
                        } else {
                            mViewPager.setCurrentItem(mBannerPosition);
                        }
                    }
                });
            }
        }
    };

    /**
     * 聚焦点的动画效果实现
     * @param position
     */

    private void setIndicator(int position) {
        position %= DEFAULT_BANNER_SIZE;
        //遍历mIndicator重置src为normal
        for (ImageView indicator : mIndicator) {
            indicator.setImageResource(com.work.ifanfan.knowit.R.drawable.dot_normal);
        }
        mIndicator[position].setImageResource(com.work.ifanfan.knowit.R.drawable.dot_focused);
        mTitle.setText(daily.getTop_stories().get(position).getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.work.ifanfan.knowit.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.work.ifanfan.knowit.R.id.action_settings) {
            MyApp.changeIt(!MyApp.isLightMode());
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }
}