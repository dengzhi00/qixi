package com.qixi.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.qixi.R;
import com.qixi.pingbao.PingbaoActivity;


/**
 * Created by 83642 on 2016/8/4.
 */
public class HuaPingView extends LinearLayout {

    private float mStartX;

    private View mMoveView;

    private CoverView coverView;

    private float mWidth;

    private Handler mainHandler;

    private InsetsPercentRelativeLayout layout;

    public HuaPingView(Context context) {
        this(context, null);
    }

    public HuaPingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HuaPingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mMoveView = LayoutInflater.from(context).inflate(R.layout.huaping_move, this);
        coverView = (CoverView) mMoveView.findViewById(R.id.cover);
        layout = (InsetsPercentRelativeLayout) mMoveView.findViewById(R.id.insets);
        mWidth = getScreenWidth(context);
    }

    public CoverView getCoverView() {
        return coverView;
    }

    public InsetsPercentRelativeLayout getInsetsPercentRelativeLayout(){
        return layout;
    }

    /**
     * 获得屏幕宽度
     */
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float nx = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = nx;
                onAnimationEnd();
            case MotionEvent.ACTION_MOVE:
                handleMoveView(nx);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doTriggerEvent(nx);
                break;
        }
        return true;
    }

    private void handleMoveView(float x) {
        float movex = x - mStartX;
        if (movex < 0)
            movex = 0;
        mMoveView.setTranslationX(movex);
        float mWidthFloat = (float) mWidth;//屏幕显示宽度
        if (getBackground() != null) {
            getBackground().setAlpha((int) ((mWidthFloat - mMoveView.getTranslationX()) / mWidthFloat * 200));//初始透明度的值为200
        }
    }

    private void doTriggerEvent(float x) {
        float movex = x - mStartX;
        if (movex > (mWidth * 0.4)) {
            moveMoveView(mWidth - mMoveView.getLeft(), true);//自动移动到屏幕右边界之外，并finish掉

        } else {
            moveMoveView(-mMoveView.getLeft(), false);//自动移动回初始位置，重新覆盖
        }
    }

    private void moveMoveView(float to, boolean exit) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mMoveView, "translationX", to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (getBackground() != null) {
                    getBackground().setAlpha((int) (((float) mWidth - mMoveView.getTranslationX()) / (float) mWidth * 200));
                }
            }
        });//随移动动画更新背景透明度
        animator.setDuration(250).start();
        if (exit) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (null != mainHandler)
                        mainHandler.obtainMessage(PingbaoActivity.MSG_LAUNCH_HOME).sendToTarget();
                    super.onAnimationEnd(animation);
                }
            });
        }//监听动画结束，利用Handler通知Activity退出}
    }

    public void setMainHandler(Handler handler) {
        this.mainHandler = handler;
    }
}
