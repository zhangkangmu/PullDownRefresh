package com.hong.zyh.pulldownrefresh.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
        layout_header_list = View.inflate(getContext(), R.layout.layout_header_list, null);
        addHeaderView(layout_header_list);
    }
}
