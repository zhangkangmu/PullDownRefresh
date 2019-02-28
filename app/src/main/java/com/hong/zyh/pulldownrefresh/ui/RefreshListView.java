package com.hong.zyh.pulldownrefresh.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private RotateAnimation rotateUpAnim;
    private RotateAnimation rotateDownAnim;
    //箭头
    private ImageView mArrowView;
    //加载图标
    private ProgressBar pb_loadding;
    //刷新文字
    private TextView tv_title;
    //刷新描述
    private TextView tv_desc_last_refresh;

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
     * 初始化头布局的动画
     * 滚动监听
     */
    private void init() {
      initHeaderView();
      initAnimation();
    }

    private void initHeaderView() {
        //layout_header_list整个头布局的变量
        layout_header_list = View.inflate(getContext(), R.layout.layout_header_list, null);
        //箭头
        mArrowView = layout_header_list.findViewById(R.id.iv_arrow);
        //加载图标
        pb_loadding = layout_header_list.findViewById(R.id.pb_loadding);
        //刷新文字
        tv_title = layout_header_list.findViewById(R.id.tv_title);
        //刷新描述
        tv_desc_last_refresh = layout_header_list.findViewById(R.id.tv_desc_last_refresh);



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


//    	a. 触摸动态修改头布局, 根据paddingTop.
//		 paddingTop = 0 完全显示
//		 paddingTop < 不完全显示 -64(自身高度)完全隐藏
//		 paddingTop > 0 顶部空白
//        b. 松手之后根据当前的paddingTop决定是否执行刷新
//		 paddingTop < 0 不完全显示, 恢复
//		 paddingTop >= 0 完全显示, 执行正在刷新...
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

                if(currentState == REFRESHING){
                    return super.onTouchEvent(ev);
                }

                //向下划动的时候的偏移量
                float offset = moveY - downY;
                //向下滑动=-自身高对+偏移量
                int paddingTop = (int) (-measuredHeight + offset);
                // 只有 偏移量>0, 并且当前第一个可见条目索引是0, 才放大头部
                if (offset > 0 && getFirstVisiblePosition() == 0) {
                    layout_header_list.setPadding(0, paddingTop, 0, 0);

                    if (paddingTop>=0 && currentState!=RELEASE_REFRESH){
                        // 切换成释放刷新模式
                        currentState=RELEASE_REFRESH;
                        Log.d("偏移量paddingTop>=0---释放模式",""+currentState);
                        Log.d("paddingTop", "" + paddingTop);

                        // 根据最新的状态值更新头布局内容
                        updateHeader();

                    }else if (paddingTop<0 && currentState!= PULL_TO_REFRESH){
                        //切换成下拉刷新模式,也就是向下拉到一半，但是没有显示全部
                        currentState = PULL_TO_REFRESH;
                        Log.d("偏移量paddingTop<0下落刷新模式模式", "" + currentState);
                        Log.d("paddingTop", "" + paddingTop);

                        // 根据最新的状态值更新头布局内容
                        updateHeader();
                    }
                    //true表示当前事件被消费
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                upY = ev.getY();
                Log.d("zhangyuhong-upY", "" + upY);
                // 根据刚刚设置状态
                if(currentState == PULL_TO_REFRESH){
                //paddingTop < 0 不完全显示, 恢复
                    layout_header_list.setPadding(0, -measuredHeight, 0, 0);
                }else if(currentState == RELEASE_REFRESH){
                //paddingTop >= 0 完全显示, 执行正在刷新...
                    layout_header_list.setPadding(0, 0, 0, 0);
                    currentState = REFRESHING;
                    updateHeader();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 调整头布局的方法
     */
    private void updateHeader() {
        switch (currentState) {
            // 切换回下拉刷新，也就是拉到一半的时候
            case PULL_TO_REFRESH:
                mArrowView.startAnimation(rotateDownAnim);
                tv_title.setText("下拉刷新");
                break;
            case RELEASE_REFRESH:
                mArrowView.startAnimation(rotateUpAnim);
                tv_title.setText("释放刷新");
                break;
            //正在刷新
            case REFRESHING:

                mArrowView.clearAnimation();
                mArrowView.setVisibility(View.INVISIBLE);
                pb_loadding.setVisibility(View.VISIBLE);
                tv_title.setText("正在刷新....");
                break;
            default:
                break;
        }
    }

    private void initAnimation() {
        // 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.
        rotateUpAnim = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(300);
        // 动画停留在结束位置
        rotateUpAnim.setFillAfter(true);

        // 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360
        rotateDownAnim = new RotateAnimation(-180f, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(300);
        // 动画停留在结束位置
        rotateDownAnim.setFillAfter(true);
    }
}
