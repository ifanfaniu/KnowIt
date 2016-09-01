package com.work.ifanfan.knowit;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.work.ifanfan.knowit.model.Story;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class BannerAdapter extends PagerAdapter {


    private Context context;
    private List<Story> newsList;
    private String bUrl;
    private ViewPager mViewPager;
    private final int DEFAULT_BANNER_SIZE;
    private final int FAKE_BANNER_SIZE;

    public BannerAdapter(Context context, List<Story> newsList, ViewPager mViewPager,int mdefault,int fake) {
        this.context = context;
        this.newsList = newsList;
        this.mViewPager = mViewPager;
        this.DEFAULT_BANNER_SIZE = mdefault;
        this.FAKE_BANNER_SIZE = fake;

    }
    public void goContentActivity(String id) {
        context.startActivity(ContentActivity.from(context, id));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= DEFAULT_BANNER_SIZE;

        View view = LayoutInflater.from(context).inflate(com.work.ifanfan.knowit.R.layout.item_banner, container, false);
        ImageView image = (ImageView) view.findViewById(com.work.ifanfan.knowit.R.id.image);
        bUrl = String.valueOf(newsList.get(position).getImage());

        Picasso.with(image.getContext())
                .load(bUrl)
                .into(image);
        final int pos = position;
        final String mId = String.valueOf(newsList.get(position).getId());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goContentActivity(mId);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return FAKE_BANNER_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        if (position == 0) {
            position = DEFAULT_BANNER_SIZE;
            mViewPager.setCurrentItem(position, false);
        } else if (position == FAKE_BANNER_SIZE - 1) {
            position = DEFAULT_BANNER_SIZE - 1;
            mViewPager.setCurrentItem(position, false);
        }
    }
}
