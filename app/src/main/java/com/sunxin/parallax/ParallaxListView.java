package com.sunxin.parallax;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by sunxin on 2016/10/6.
 * 具有头部视差效果的ListView
 */
public class ParallaxListView extends ListView {
    private static final String TAG = "ParallaxListView";
    private int mImageViewHeight;//ImageView的原始高度

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //设置图片的最大高度
    private int maxHeight;

    private ImageView mImageView;

    public void setImageView(final ImageView imageView) {
        this.mImageView = imageView;
        //设置图片的最大高度   Intrinsic:本质的，固有的

        //设置一个全局的布局监听，在onLayout方法执行完之后执行。保证getHeight不等于0
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //一定要移除
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //需要兼容小图片
                int drawableHeight = imageView.getDrawable().getIntrinsicHeight();//图片的高度
                //ImageView的高度，不过这个height可能为0
                mImageViewHeight = imageView.getHeight();
                //如果图片的高度小于ImageView的高度，那么最大高度设置为ImageView高度的2倍。否则就设置为图片高度
                maxHeight = drawableHeight < mImageViewHeight ? mImageViewHeight * 2 : drawableHeight;
            }
        });



    }


    /**
     * 控制滑动到边缘的处理方法。在ListView滑动到头的时候执行，可以获取继续滑动的距离和方向
     *
     * @param deltaX         x轴继续滑动的距离
     * @param deltaY         y轴继续滑动的距离  负值：顶部到头，  正值：底部到头
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX x方向最大的可以滑动的距离
     * @param maxOverScrollY y方向最大的可以滑动的距离 修改这个值就可以让ListView具有弹性
     * @param isTouchEvent   true表示是手指的滑动，false表示是惯性滑动
     * @return
     */
    @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {


        Log.d(TAG, "overScrollBy: deltaY    " + deltaY + "   isTouchEvent    " + isTouchEvent);

        if (deltaY < 0 && isTouchEvent) {
            //说明是顶部到头并且是手指继续拖动
            //让ImageView的height不断的增大
            if (mImageView != null) {
                int newHeight = mImageView.getHeight() - deltaY/3;
                //判断是否超过最大的高度
                if (newHeight > maxHeight) {
                    newHeight = maxHeight;
                }

                mImageView.getLayoutParams().height = newHeight;
                mImageView.requestLayout();//使布局生效
            }
        }


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //设置手松开图片高度回到原来的高度
        if (ev.getAction() == MotionEvent.ACTION_UP){
            //使用属性动画，nineoldandroid.jar
            //设置从当前高度到原始高度
            ValueAnimator animator = ValueAnimator.ofFloat(mImageView.getHeight(),mImageViewHeight);
            //添加动画更新的监听
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    //获取的动画的值
                    float animatorValue = (Float) animator.getAnimatedValue();
                    mImageView.getLayoutParams().height = (int) animatorValue;
                    mImageView.requestLayout();//使布局生效
                }
            });
            animator.setInterpolator(new OvershootInterpolator());//弹性差值器
            animator.setDuration(350);
            animator.start();
        }

        return super.onTouchEvent(ev);
    }
}
