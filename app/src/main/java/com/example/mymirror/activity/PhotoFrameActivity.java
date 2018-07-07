package com.example.mymirror.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymirror.R;

/**
 * Created by 杜东赫 on 2018/3/31.
 */

public class PhotoFrameActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private GridView gridView;
    private TextView textView;
    private int[] photo_styles;
    private String[] photo_name;
    private Bitmap[] bitmaps;
    private static final int RESULT_OK=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_frame);
        textView=(TextView)findViewById(R.id.back_to_main);
        gridView=(GridView)findViewById(R.id.photo_fram_list);
        initDatas();
        textView.setOnClickListener(this);
        PhotoFrameAdapter adapter=new PhotoFrameAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }


    private void initDatas(){
        photo_styles=new int[]{R.mipmap.mag_0001,R.mipmap.mag_0003,R.mipmap.mag_0005,R.mipmap.mag_0006,R.mipmap.mag_0007,R.mipmap.mag_0008,R.mipmap.mag_0009,R.mipmap.mag_0011,R.mipmap.mag_0012,R.mipmap.mag_0014};
        photo_name=new String[]{"Beautiful","Special","Wishes","Forever","Journey","Love","River","Wonderful","Birthday","Nice"};
        bitmaps=new Bitmap[photo_styles.length];
        for(int i=0;i<photo_styles.length;i++){
             Bitmap bitmap= BitmapFactory.decodeResource(getResources(),photo_styles[i]);
            bitmaps[i]=bitmap;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_to_main:
                finish();
                break;
            default:
                break;
        }
    }



    class PhotoFrameAdapter extends BaseAdapter{

    @Override
    public int getCount() {
        return photo_name.length;
    }

    @Override
    public Object getItem(int position) {
        return photo_name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view=getLayoutInflater().inflate(R.layout.item_gridview,null);
            holder.image=(ImageView)view.findViewById(R.id.item_pic);
            holder.txt=(TextView)view.findViewById(R.id.item_txt);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        setData(holder,position);
        return view;
    }
}
private void setData(ViewHolder holder,int position){
    holder.image.setImageBitmap(bitmaps[position]);
    holder.txt.setText(photo_name[position]);
}
    class ViewHolder{
    ImageView image;
    TextView txt;
}
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i("jjj","position="+position);
        Intent intent=new Intent();
        intent.putExtra("POSITION",position);

        setResult(RESULT_OK,intent);
        Toast.makeText(this, "关闭选择功能", Toast.LENGTH_SHORT).show();
        finish();
    }
}

