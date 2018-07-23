package com.example.yangdiansheng.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yangdiansheng.myapplication.pullrefresh.PullRefreshFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleViewActivity extends AppCompatActivity {

    public static void show(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity,RecycleViewActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.rlv_list)
    RecyclerView recyclerView;
    @BindView(R.id.prfl_framelayout)
    PullRefreshFrameLayout framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        ButterKnife.bind(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            list.add("1" + i);
        }
        recyclerView.setAdapter(new Adapter(list,this));
        framelayout.setCallBack(new PullRefreshFrameLayout.CallBack() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        framelayout.refreshComplete();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        framelayout.loadMoreComplete();
                    }
                },2000);
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<MyHolder>{

        private List<String> mlist;
        private Context mContext;

        public Adapter(List<String> list, Context context){
            mlist = list;
            mContext = context;
        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
//            holder.tv_item.setText(mlist.get(position));

        }

        @Override
        public int getItemCount() {
            return mlist.size();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item)
        TextView tv_item;

        //实现的方法
        public MyHolder(View itemView) {
            super(itemView);
//            TextView tv_item = (TextView) itemView.findViewById(R.id.tv_item);
            ButterKnife.bind(itemView);
        }
    }
}
