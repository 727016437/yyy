package com.example.mymirror.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.mymirror.R;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        handler.sendEmptyMessageDelayed(1,3000);
    }
private Handler handler=new Handler(new Handler.Callback(){
    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what==1){
            Intent intent=new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
});

public boolean onKeyDown(int keyCode, KeyEvent event){
    if (keyCode==KeyEvent.KEYCODE_BACK){
        return false;
    }
    return false;
}

}

