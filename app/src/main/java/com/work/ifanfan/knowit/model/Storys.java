package com.work.ifanfan.knowit.model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Storys {

    /**
     * images : ["http://pic4.zhimg.com/3078e8e89d118ddbfbaa4c3cca7d51d3.jpg"]
     * type : 0
     * id : 8640277
     * ga_prefix : 080222
     * title : 小事 · 骗子也在进步
     */
    public Storys(int type, int id, String title, String ga_prefix, List<String> images) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.ga_prefix = ga_prefix;

        this.images = images;
    }

    private int type;
    private int id;
    private String ga_prefix;
    private String title;
    private List<String> images;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
