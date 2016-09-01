package com.work.ifanfan.knowit;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.work.ifanfan.knowit.model.News;
import com.work.ifanfan.knowit.model.Storys;

import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ZhihuHolder> {
    private List<Storys> mList;
    private MainActivity.CallBack mCallBack;

    public ZhihuAdapter(News mNews, MainActivity.CallBack callBack){
        mList = mNews.getStories();
        mCallBack = callBack;
    }

    public void addItem(Storys mStory){
        mList.add(mStory);
        notifyItemInserted(mList.size()-1);
    }

    @Override
    public ZhihuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(com.work.ifanfan.knowit.R.layout.item_main,parent,false);
        return new ZhihuHolder(itemView,mCallBack);
    }

    @Override
    public void onBindViewHolder(ZhihuHolder holder, int position) {
       holder.bindTo(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ZhihuHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        ImageView mImageView;
        String mUrl;
        int mId;
        public ZhihuHolder(View itemView , final MainActivity.CallBack callBack) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(com.work.ifanfan.knowit.R.id.tv_1);
            mImageView = (ImageView) itemView.findViewById(com.work.ifanfan.knowit.R.id.ig);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onItemClicked(String.valueOf(mId));
                    mTitle.setTextColor(Color.argb(255,170,183,184));
                }
            });

        }
        public void bindTo(Storys story){
            mUrl = story.getImages().get(0);
            mTitle.setText(story.getTitle());
            mId = story.getId();
            Picasso.with(mImageView.getContext())
                    .load(mUrl)
                    .into(mImageView);
        }
    }
}
