package com.hong.zyh.pulldownrefresh.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.hong.zyh.pulldownrefresh.R;

/**
 * Created by shuaihong on 2019/2/28.
 * 包含下拉刷新的自定义listview
 */

public class RefreshListView extends ListView {

    private View layout_header_list;

    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化头布局，脚布局
     * 滚动监听
     */
    private void init() {
      initHeaderView();
    }

    private void initHeaderView() {
        //layout_header_list整个头布局的变量
        layout_header_list = View.inflate(getContext(), R.layout.layout_header_list, null);

        //真实的头布局高度，就算加上padding后也不会改变
        layout_header_list.getHeight();

        //根据测量规则测量，（不要忘了这个，没有这个则每次都是0）
        layout_header_list.measure(0,0);

        //测量高度，显示在页面中的高度
        int measuredHeight = layout_header_list.getMeasuredHeight();
        Log.d("initHeaderView",measuredHeight+"");

        //设置头布局的内边距，让他默认移出页面，也就是在页面最上方，但是移出了手机页面
        layout_header_list.setPadding(0,-measuredHeight,0,0);
        addHeaderView(layout_header_list);
    }
}
