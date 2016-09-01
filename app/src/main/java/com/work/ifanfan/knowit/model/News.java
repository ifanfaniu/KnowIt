package com.work.ifanfan.knowit.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class News {
    private String date;
    private List<Storys> stories;
    private List<Story> top_stories;

    public News(String date, List<Storys> stories, List<Story> top_stories) {
        this.date = date;
        this.stories = stories;
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Storys> getStories() {
        return stories;
    }

    public void setStories(List<Storys> stories) {
        this.stories = stories;
    }

    public List<Story> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Story> top_stories) {
        this.top_stories = top_stories;
    }
}
