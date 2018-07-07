package com.example.mymirror.utils;

/**
 * Created by 杜东赫 on 2018/4/8.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;




/**
 * Created by 杜东赫 on 2018/4/8.
 */

public class SetBrightness {
    public static boolean isAutoBrigtness(ContentResolver aContenResolver){
        boolean automBrigtness=false;
        try {
            automBrigtness= Settings.System.getInt(aContenResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)== Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automBrigtness;
    }
//    一、在有Activity和Service的情况下
//
//    getContext().getContentResolver().insert(...);
//
//    1.getContext()是获得一个上下文对象（Context），一般在四大组件中都会获取上下文对象。
//
//            2.在Activity和Service中，就没必要获取Context了，因为他本身就是，所以可以直接调用getContentResolver()。
//
//            3.在ContentProvider中，就需要先调用getContext()获取到Context，然后调用getContentResolver()
//    获得ContentResolver对 象，也就是，getContext().getContentResolver().
//
//    另外：
//
//            （1）getContext().getContentResolver()返回的是ContentResolver
//    对象，ContentResolver负责获取ContentProvider提供的数据。
//
//            （2） MainActivity.this.getContentResolver()+数据库操作
//            等同于
//    getContext().getContentResolver()+数据操作。
//getContext().getContentResolver()返回的当然是ContentResolver 对象了，ContentResolver负责获取ContentProvider提供的数据
    public static int getScreenBrightness(Activity activity){
        int nowBrightnessValue=0;
        ContentResolver contentResolver=activity.getContentResolver();
        try {
            nowBrightnessValue= Settings.System.getInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }
//    2.WindowManager介绍
//    WindowManager的方法很简单，有三个方法，AddView(),removeView(),updateViewLayout();
//    AddView();    //添加View
//    removeView();            //移除VIew
//    updateViewLayout()    //更新View
//WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);    //获取WindowManage
//    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//    //设置LayoutParams的属性
//    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;       //该Type描述的是形成的窗口的层级关系，下面会详细列出它的属性
//    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |       //该flags描述的是窗口的模式，是否可以触摸，可以聚焦等
//    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
//    layoutParams.gravity = Gravity.CENTER;                                       //设置窗口的位置
//    layoutParams.format = PixelFormat.TRANSLUCENT;                               //不设置这个弹出框的透明遮罩显示为黑色
//    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;                //窗口的宽
//    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;               //窗口的高
//    layoutParams.token = ((View)findViewById(R.id.linearlayout)).getWindowToken();           //获取当前Activity中的View中的TOken,来依附Activity，因为设置了该值，纳闷写的这些代码不能出现在onCreate();否则会报错

    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();//获取窗体属性
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);		//设置屏幕亮度
        activity.getWindow().setAttributes(lp);				//设置窗口属性
       // screenBrightness亮度属性
        //screen brightness 荧光屏亮度
    }
//    WindowManager.LayoutParams 是 WindowManager 接口的嵌套类；它继承于 ViewGroup.LayoutParams； 它用于向WindowManager描述Window的管理策略。
    public static void stopAutoBrightness(Activity activity) {
        //设置Activity亮度手工模式
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }
    public static void startAutoBrightness(Activity activity) {
        //设置Activity亮度自动调节模式
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        // SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
        // SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度

    }
    public static void saveBrightness(ContentResolver resolver, int brightness) {
        //获取屏幕亮度
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        //设置屏幕亮度
        android.provider.Settings.System.putInt(resolver, "screen_brightness",brightness);
        resolver.notifyChange(uri, null);				//保存状态
    }
    //什么啊， 你写的是什么啊？你要说的是getContext()与getContextResolver().notifyChange()吗，getContex()在不同的类下面有不同的作用，一般是返回一个带有相应资源的Context对象。getContextResolver().notifyChange()是获得一个ContextResolver对象并且更新里面的内容。
    public static ContentResolver getResolver(Activity activity){
        ContentResolver cr = activity.getContentResolver();		//获取activity解析内容
        return cr;
    }



}
