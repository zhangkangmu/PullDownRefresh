package com.hong.zyh.pulldownrefresh.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.hong.zyh.pulldownrefresh.R;

/**
 * Created by shuaihong on 2019/2/28.
 * 包含下拉刷新的自定义listview
 */

public class RefreshListView extends ListView {

    private View layout_header_list;
    private float downY;
    private float upY;
    private float moveY;
    private int measuredHeight;
    //头部完全显示的状态，下拉刷新
    public static final int PULL_TO_REFRESH =0;
    //刷新完毕状态
    public static final int RELEASE_REFRESH=1;
    //正在刷新的状态，这时候头部完全显示的状态
    public static final int REFRESHING=2;
    //当前的刷新模式
    private int currentState= PULL_TO_REFRESH;

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
        measuredHeight = layout_header_list.getMeasuredHeight();
        Log.d("initHeaderView", measuredHeight +"");

        //设置头布局的内边距，让他默认移出页面，也就是在页面最上方，但是移出了手机页面
        layout_header_list.setPadding(0,-measuredHeight,0,0);
        addHeaderView(layout_header_list);
    }


    /**
     * 下落显示，通过判断偏移量实现
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                Log.d("zhangyuhong-downY",""+ downY);
                break;
            case MotionEvent.ACTION_MOVE:
                //往下拖动的时候moveY会慢慢变大
                moveY = ev.getY();
                Log.d("zhangyuhong-moveY", "" + moveY);
                //向下划动的时候的偏移量
                float offset = moveY - downY;
                //向下滑动=-自身高对+偏移量
                int paddingTop = (int) (-measuredHeight + offset);
                // 只有 偏移量>0, 并且当前第一个可见条目索引是0, 才放大头部
                if (offset > 0 && getFirstVisiblePosition() == 0) {
                    layout_header_list.setPadding(0, paddingTop, 0, 0);

                    if (paddingTop>=0 && currentState!=RELEASE_REFRESH){
                        currentState=RELEASE_REFRESH;
                        Log.d("偏移量paddingTop>=0---释放模式",""+currentState);
                    }else if (paddingTop<0 && currentState!= PULL_TO_REFRESH){
                        currentState=PULL_TO_REFRESH;
                        Log.d("偏移量paddingTop<0下落刷新模式模式",""+currentState);
                    }
                    //true表示当前事件被消费
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                upY = ev.getY();
                Log.d("zhangyuhong-upY", "" + upY);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
