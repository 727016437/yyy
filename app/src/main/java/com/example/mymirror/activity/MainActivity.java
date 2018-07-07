package com.example.mymirror.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.mymirror.utils.SetBrightness;
import com.example.mymirror.R;
import com.example.mymirror.view.DrawView;
import com.example.mymirror.view.FunctionView;
import com.example.mymirror.view.PictureView;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,SeekBar.OnSeekBarChangeListener,View.OnTouchListener,View.OnClickListener,FunctionView.onFunctionViewItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SurfaceHolder holder;
    private SurfaceView surfaceView;
    private PictureView pictureView;
    private FunctionView functionView;
    private SeekBar seekBar;
    private ImageView add, minus;
    private LinearLayout bottom;
    private ProgressDialog dialog;
    private DrawView drawView;
    private boolean havaCamera;
    private int mCurrentCamIndex;
    private int ROTATE;
    private int minFcous;
    private int maxFocus;
    private int everyFcous;
    private int nowFcous;
    private Camera camera;
    private int frame_index;
    private int[] frame_index_ID;
    private static final int PHOTO=1;
    private int brightnessValue;
    private boolean isAutoBrightness;
    private int SegmentLengh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setViews();
        frame_index=0;
        frame_index_ID=new int[]{R.mipmap.mag_0001,R.mipmap.mag_0003,R.mipmap.mag_0005,R.mipmap.mag_0006,R.mipmap.mag_0007,R.mipmap.mag_0008,R.mipmap.mag_0009,R.mipmap.mag_0011,R.mipmap.mag_0012,R.mipmap.mag_0014};
        getBrightnessFromWindow();
    }

    private void initViews() {
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        pictureView = (PictureView) findViewById(R.id.picture);

        //ImageView imageView = (ImageView) findViewById(R.id.picture);
        functionView = (FunctionView) findViewById(R.id.function);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        add = (ImageView) findViewById(R.id.add);
        minus = (ImageView) findViewById(R.id.minus);
        bottom = (LinearLayout) findViewById(R.id.bottom_bar);
        drawView = (DrawView) findViewById(R.id.draw_glasses);


    }
    private  void setViews(){
        holder=surfaceView.getHolder();
        holder.addCallback(this);
        add.setOnTouchListener(this);
        minus.setOnTouchListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        functionView.setonFunctionViewItemClickListener(this);
    }
    private boolean checkCameraHardware() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount;
        Camera mCamera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    mCamera = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (Exception e) {
                    Log.e(TAG, "相机打开失败：" + e.getLocalizedMessage());

                }
            }
        }
        return mCamera;
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees) % 360;
        }
        ROTATE = result +180;
        camera.setDisplayOrientation(result);
    }
private void setCamera(){
    if(checkCameraHardware()){
        camera=openFrontFacingCameraGingerbread();
        setCameraDisplayOrientation(this,mCurrentCamIndex,camera);
        Camera.Parameters parameters=camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        List<String> list=parameters.getSupportedFocusModes();
        for (String str:list){
            Log.e(TAG,"支持的对焦模式："+str);
        }
        List<Camera.Size> pictureList=parameters.getSupportedPictureSizes();
        List<Camera.Size> previewList=parameters.getSupportedPreviewSizes();
        parameters.setPictureSize(pictureList.get(0).width,pictureList.get(0).height);
        parameters.setPictureSize(previewList.get(0).width,previewList.get(0).height);
        minFcous=parameters.getZoom();
        maxFocus=parameters.getMaxZoom();
        everyFcous=1;
        nowFcous=minFcous;
        seekBar.setMax(maxFocus);
        Log.e(TAG,"当前镜头距离："+ minFcous+"获取最大距离为"+maxFocus);
        camera.setParameters(parameters);
    }
}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
   Log.e("surfaceCreated","绘制开始");
    try {
        setCamera();
        camera.setPreviewDisplay(holder);
        camera.startPreview();
    } catch (IOException e) {
        camera.release();
        camera=null;
        e.printStackTrace();
    }
}

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        try {

            camera.stopPreview();
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e("surfaceDestroyed","绘制结束");
        toRelease();
}
private void toRelease(){
    camera.setPreviewCallback(null);
    camera.stopPreview();
    camera.release();
    camera=null;
}

private void setZoomValues(int want){
    Camera.Parameters parameters=camera.getParameters();
    seekBar.setProgress(want);
    parameters.setZoom(want);
    camera.setParameters(parameters);
}
private  int getZoomValues(){
    Camera.Parameters parameters=camera.getParameters();
    int values=parameters.getZoom();
    return values;
}
private void addZoomValues(){
    if(nowFcous==maxFocus){
        Log.e(TAG,"大于maxFocus是不可能的");

    }else if(nowFcous==maxFocus){

    }else {
        setZoomValues(getZoomValues()+everyFcous);
        Log.i("ddd","当前焦距为");
    }
}
private void minusZoomValues(){
    if(nowFcous<0){
        Log.e(TAG,"小于0是不可能的");
    }else if(nowFcous==0){

    }else {
        setZoomValues(getZoomValues()-everyFcous);
    }
}
public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
    Camera.Parameters parameters=camera.getParameters();
    nowFcous=progress;
    parameters.setZoom(progress);
    camera.setParameters(parameters);
}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
    switch (v.getId()){
        case R.id.add:
            addZoomValues();
            break;
        case R.id.minus:
            minusZoomValues();
            break;
        case R.id.picture:

            break;
        default:
            break;
    }
    return true;
}

    @Override
    public void onClick(View view) {

    }

    @Override
    public void hint() {
        Intent intent=new Intent(this,HintActivity.class);
        startActivity(intent);
    }

    @Override
    public void choose() {
      Intent intent=new Intent(MainActivity.this,PhotoFrameActivity.class);
        startActivityForResult(intent,PHOTO);
        Toast.makeText(this, "选择！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void down() {
downCurrentActivityBrightnessValues();
    }

    @Override
    public void up() {
upCurrentActivityBrightnessValues();
    }
    @Override
  protected void onActivityResult(int requsetCode,int resultCode,Intent data){
    super.onActivityResult(requsetCode,resultCode,data);
        Log.e(TAG,"返回值"+resultCode+"\t\t请求值"+requsetCode);
    if(resultCode==RESULT_OK&&requsetCode==PHOTO){
        int position=data.getIntExtra("POSITION",0);
        frame_index=position;
        Log.i("接收到的为","position="+position);


        pictureView.setPhotoFrame(position);

    }
}
    private void setMyActivityBright(int brightnessValues) {
        SetBrightness.setBrightness(this, brightnessValues);//调用SetBrightness类方法设置亮度
        //保存亮度
        SetBrightness.saveBrightness(SetBrightness.getResolver(this), brightnessValues);
    }
    private void getAfterMySetBrightnessValues() {
        brightnessValue = SetBrightness.getScreenBrightness(this);	//获得亮度
        Log.e(TAG, "当前手机屏幕亮度值:"+ brightnessValue);
    }
    public void getBrightnessFromWindow() {
        //获得是否自动调节亮度
        isAutoBrightness = SetBrightness.isAutoBrigtness(SetBrightness.getResolver(this));
        Log.e(TAG, "当前手机是否是自动调节屏幕亮度:"+ isAutoBrightness);
        if (isAutoBrightness) {				//如果是自动调节亮度
            SetBrightness.stopAutoBrightness(this);		//关闭自动调节亮度
            Log.e(TAG, "关闭了自动调节!");
            setMyActivityBright(255 / 2 + 1);
        }
        //亮度值0~256
        SegmentLengh = (255 / 2 + 1) / 4;			//每32为一个区间
        getAfterMySetBrightnessValues();			//获取设置后的亮度
    }
    private void downCurrentActivityBrightnessValues(){
        if (brightnessValue >0) {
            setMyActivityBright(brightnessValue - SegmentLengh);  //减少亮度
        }
        getAfterMySetBrightnessValues();			//获取设置后的屏幕亮度
    }
    private void upCurrentActivityBrightnessValues(){
        if (brightnessValue <255) {
            if (brightnessValue + SegmentLengh >= 256){ 		//最大值256
                return;
            }
            setMyActivityBright(brightnessValue + SegmentLengh );//调高亮度
        }
        getAfterMySetBrightnessValues();			//获取设置后的屏幕亮度
    }


}
