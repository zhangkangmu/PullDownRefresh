package com.hong.zyh.pulldownrefresh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hong.zyh.pulldownrefresh.ui.RefreshListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private RefreshListView listview_refresh;
    private ArrayList<String> listDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏的第二种方式
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listview_refresh = findViewById(R.id.listview_refresh);

        //增加一个listview头,一定要在适配器前面
//        Button button = new Button(this);
//        button.setText("head");
//        listview_refresh.addHeaderView(button);

        listDatas = new ArrayList<>();
        for (int i =0;i<50;i++){
            listDatas.add("this is "+i+"datas");
        }

        listview_refresh.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return listDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoder viewHoder;
            if (convertView==null){
                viewHoder=new ViewHoder();
                convertView=View.inflate(getApplicationContext(),R.layout.listview_item_resresh,null);
                viewHoder.tv_item_data=convertView.findViewById(R.id.tv_item_data);
                convertView.setTag(viewHoder);
            }else{
                viewHoder=(ViewHoder)convertView.getTag();
            }
            String listData = listDatas.get(position);
            viewHoder.tv_item_data.setText(listData);
            //注意，返回的是convertView，不是viewHoder
            return convertView;
        }
    }


    /**
     * 对listview进行优化建立的类
     */
    public static class ViewHoder{
        TextView  tv_item_data;
    }
}
