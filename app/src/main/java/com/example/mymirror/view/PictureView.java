package com.example.mymirror.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import com.example.mymirror.R;
/**
 * Created by 杜东赫 on 2018/3/27.
 */
public class PictureView extends ImageView {
    private int[] bitmap_ID_Array;			//图片资源ID的数组
    private Canvas mCanvas;					//画布
    private int draw_Width;					//要画的长度
    private int draw_Height;					//要画的高度
    private Bitmap mBitmap;					//镜框
    private int bitmap_index;					//图片标记

    public PictureView(Context context) {
        super(context,null);
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }


    public PictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);//调用父类的有参构造方法

        getTheWindowSize((Activity)context);
        init();

        //System.out.println("被调用了");
        //Log.e("eee","构造方法被调用");
        //onDraw(mCanvas);

    }


    private void initBitmaps() {
        //获取mipmap资源中的图片
        bitmap_ID_Array = new int[]{R.mipmap.mag_0001,R.mipmap.mag_0003,R.mipmap.mag_0005,
                R.mipmap.mag_0006,R.mipmap.mag_0007,R.mipmap.mag_0008,R.mipmap.mag_0009,
                R.mipmap.mag_0011,R.mipmap.mag_0012, R.mipmap.mag_0014};
    }

    private void init() {
        initBitmaps();						//初始化图片集合
        bitmap_index = 0; 					//图片索引
        mBitmap = Bitmap.createBitmap(draw_Width, draw_Height, Bitmap.Config.ARGB_8888);//初始化画布
        mCanvas = new Canvas(mBitmap);			//实例化Canvas对象
        mCanvas.drawColor(Color.TRANSPARENT);  //设置背景色为透明色
    }
    public void setPhotoFrame(int index){
        bitmap_index = index;		//ID传递
        invalidate();			//窗口无效，需要重绘
    }

    private void getTheWindowSize(Activity activity){
        DisplayMetrics dm = new DisplayMetrics(); 	//获取屏幕分辨率的类
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);//获取屏幕显示属性
        draw_Width = dm.widthPixels;		//宽度
        draw_Height = dm.heightPixels;		//高度
        Log.e("1、屏幕宽度：", draw_Width + "\t\t屏幕高度：" + draw_Height);
    }

    private Bitmap getNewBitmap(){
        //根据bitmap_index获取图片
       Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                    bitmap_ID_Array[bitmap_index]).copy(Bitmap.Config.ARGB_8888, true);
        //根据长、宽设置图片
        bitmap = Bitmap.createScaledBitmap(bitmap,draw_Width,draw_Height,true);//缩放图片
        return bitmap;		//返回获取的图片对象
    }

    @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas); 			//父类方法

           canvas.drawColor(Color.TRANSPARENT);	//设置画布颜色
            canvas.drawBitmap(mBitmap, 0, 0, null);	//绘制canvas图片
            Rect rect1 = new Rect(0,0,this.getWidth(),this.getHeight());
            Log.e("屏幕宽度：", this.getWidth() + "\t\t屏幕高度：" + this.getHeight());
            canvas.drawBitmap(getNewBitmap(),null,rect1,null); //绘制图片
       }


}


