package com.qf.xs.day29pulltorefreshdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private PtrClassicFrameLayout myPtrFL;
    private String[] urlStrings = {
            "http://img5.duitang.com/uploads/item/201410/04/20141004141720_vr23M.jpeg",
            "http://img1.imgtn.bdimg.com/it/u=3724834980,466973059&fm=11&gp=0.jpg",
            "http://i2.hexunimg.cn/2015-04-07/174742774.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2525689499,526305536&fm=21&gp=0.jpg"
    };
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPtrFL = (PtrClassicFrameLayout) findViewById(R.id.myPtrFL);
        textView = (TextView) findViewById(R.id.text_view);
        imageView = (ImageView) findViewById(R.id.image_view);


        setPtrFL();

    }

    private void setPtrFL() {

        //注册下拉监听器
        myPtrFL.setPtrHandler(new PtrDefaultHandler() {
            //一般在下拉时，开启异步任务或者是线程去执行耗时操作，
            //任务完成时调用 myPtrFL.refreshComplete()方法让下拉刷新的头部消失
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.e("print", "onRefreshBegin....");
//                myPtrFL.refreshComplete();
                //下拉时，开始下载图片
                LoadImage();
            }
        });

        //
        final StoreHouseHeader header = new StoreHouseHeader(this);
//        //设置布局参数，-1指匹配父窗体，-2指包裹内容
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        //设置内边距...PtrLocalDisplay框架自带
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);

        // 头部使用的字符串。这里的字符串只能是 [A-Z 0-9 - .]
//        header.initWithString("loading￥￥￥￥");
        header.initWithString("1000PHONE-Loading.....");
        header.setTextColor(Color.YELLOW);
        header.setBackgroundColor(Color.BLACK);
        //给下拉刷新设置下拉头部 StoreHouseHeader布局
        myPtrFL.setHeaderView(header);
//        //添加一个UI时间处理回调函数。为MaterialHeader的内部实现回调。
        myPtrFL.addPtrUIHandler(header);

/*   添加MD风格的头部
        final MaterialHeader header = new MaterialHeader(this);

//可以设置一组 颜色数组，改变进度显示的颜色变化
        header.setColorSchemeColors(new int[]{Color.RED, Color.BLUE,Color.YELLOW,Color.GREEN});
//设置布局参数，-1指匹配父窗体，-2指包裹内容
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//设置内边距...PtrLocalDisplay框架自带
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
//告诉创建一个MaterialHeader 布局绑定在那个下拉刷新控件上
        header.setPtrFrameLayout(myPtrFL);

//给下拉刷新设置下拉头部 MaterialHeader布局
        myPtrFL.setHeaderView(header);
//添加一个UI时间处理回调函数。为MaterialHeader的内部实现回调。
        myPtrFL.addPtrUIHandler(header);*/
    }

    private void LoadImage() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStrings[index % urlStrings.length]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    SystemClock.sleep(5000);
                    final Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    index++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                            textView.setText("下载完成" + index);
                            myPtrFL.refreshComplete();
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
