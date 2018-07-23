package com.example.yangdiansheng.myapplication;

import android.app.Activity;
import android.app.ListActivity;
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
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangdiansheng.myapplication.pullrefresh.PullRefreshFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewActivity extends AppCompatActivity {

    public static void show(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity,ListViewActivity.class);
        activity.startActivity(intent);
    }

    @BindView(R.id.prfl_framelayout)
    PullRefreshFrameLayout framelayout;
    @BindView(R.id.lv_list)
    ListView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ButterKnife.bind(this);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            list.add("1" + i);
        }
        mLvList.setAdapter(new Adapter(list,this));
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

    private class Adapter extends BaseAdapter {

        private List<String> mlist;
        private Context mContext;

        public Adapter(List<String> list, Context context){
            mlist = list;
            mContext = context;
        }


        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            TextView textView = (TextView) view.findViewById(R.id.tv_item);
            textView.setText(mlist.get(position));
            return view;
        }
    }
}
