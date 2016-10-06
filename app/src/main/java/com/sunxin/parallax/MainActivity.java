package com.sunxin.parallax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView mListView;

    private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();

        mListView = (ParallaxListView) findViewById(R.id.listview);
        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//设置不显示蓝色阴影

        //添加头部View
        View headerView = View.inflate(this,R.layout.header_view,null);

        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageview);

        //设置图片
        mListView.setImageView(imageView);

        mListView.addHeaderView(headerView);
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,indexArr));


    }
}
