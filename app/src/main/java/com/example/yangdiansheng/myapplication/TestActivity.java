package com.example.yangdiansheng.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.yangdiansheng.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {

    public static void show(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity,TestActivity.class);
        activity.startActivity(intent);
    }


    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;

    private int mI;

    @OnClick(R.id.bt_click)
    void click(){
        if (mI % 2 == 0){
            Utils.reserveView(mIvArrow,0,180);
        } else {
            Utils.reserveView(mIvArrow,180,360);
        }
        mI++;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

}
