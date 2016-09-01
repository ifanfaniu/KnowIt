package com.work.ifanfan.knowit;

import android.app.Application;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MyApp extends Application {

    public static boolean MM = true;

    public static boolean isLightMode(){
        return MM;
    }

    public static void changeIt(boolean mm){
        MM = mm;
    }

}
