package com.example.EECS_581.ecc_android_app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.view.WindowManager;

/**
 * Created by Jiaxiang Li on 2/6/2015.
 */
/*public class SlidingLayout extends RelativeLayout implements View.OnTouchListener{

    //THE VELOCITY OF MOVE FINGER TO MANIPULATE THE SLIDING BAR
    public static final int SNAP_VELOCITY=200;

    //THE WIDTH OF THE SCREEN
    private int screenWidth;

    //THE WIDTH TO LEFT OF RIGHT CONTENT
    private int leftEdge=0;

    //THE WIDTH TO RIGHT OF RIGHT CONTENT
    private int rightEdge=0;

    //THE MAXIMUM VELOCITY OF FINGER MOVING WHICH WILL NOT CHANGE THE SLIDING BAR
    private int touchSlop;

    //THE X-COORDINATE OF THE FINGER TOUCH
    private float xTouch;

    //THE Y-COORDINATE OF THE FINGER TOUCH
    private float yTouch;

    //THE X-COORDINATE OF THE FINGER MOVING
    private float xMove;

    //THE Y-COORDINATE OF THE FINGER MOVING
    private float yMove;

    //THE X-COORDINATE OF THE FINGER UP
    private float xUp;


    //THE LEFT LAYOUT IS VISIBLE OR NOT
    private boolean isLeftLayoutVisible;

    //THE BOOLEAN OF SLIDING OR NOT
    private boolean isSliding;

    //THE LEFT LAYOUT OBJECT;
    private View leftLayout;

    //THE RIGHT LAYOUT OBJECT;
    private View rightLayout;

    //THE SLIDING LISTENER
    private View mBindView;

    //THE PARAMS OF LEFT LAYOUT(CHANGE THE WIDTH OF LEFT LAYOUT AND THE MARGIN OF LEFT)
    private MarginLayoutParams leftLayoutParams;

    //THE PARAMS OF RIGHT LAYOUT(CHANGE THE WIDTH OF LEFT LAYOUT AND THE MARGIN OF RIGHT)
    private MarginLayoutParams rightLayoutParams;

    //TO CALCULATE THE VELOCITY OF FINGER MOVING
    private VelocityTracker mVelocityTracker;

    public SlidingLayout(Context context,AttributeSet attrs){
        super(context,attrs);
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth=wm.getDefaultDisplay().getWidth();
        touchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
    }

    //BIND THE VIEW OF SLIDING LISTENER,TO MAKE SURE THE LAYOUT ONLY SHOW AFTER FINGER MOVING
    public void setScrollEvent(View bindView){
        mBindView=bindView;
        mBindView.setOnTouchListener(this);
    }

    //WINDOW MOVE TO LEFT, THE VELOCITY OF SCROLL IS 30
    public void scrollToLeftLayout(){
        new ScrollTask().execute(-30);
    }

    // WINDOW MOVE TO RIGHT,THE VELOCITY OF SCROLL IS 30
    public void scrollToRightLayout(){
        new ScrollTask().execute(30);
    }

    public boolean isLeftLayoutVisible(){
        return isLeftLayoutVisible;

    }

    //RESET THE PARAMS OF RIGHTLAYOUT AND LEFTLAYOUT
    protected void onLayout(boolean changed,int l,int t,int r,int b){
        super.onLayout(changed,l,t,r,b);
        
        if(changed){
            leftLayout=getChildAt(0);
            leftLayoutParams=(MarginLayoutParams) leftLayout.getLayoutParams();
            rightEdge=-leftLayoutParams.width;
            
            rightLayout=getChildAt(1);
            rightLayoutParams=(MarginLayoutParams) rightLayout.getLayoutParams();
            rightLayoutParams.width=screenWidth;
            rightLayout.setLayoutParams(rightLayoutParams);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        if (leftLayout.getVisibility() != View.VISIBLE) {
            leftLayout.setVisibility(View.VISIBLE);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xTouch = event.getRawX();
                yTouch = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整右侧布局的leftMargin值，从而显示和隐藏左侧布局
                xMove = event.getRawX();
                yMove = event.getRawY();
                int moveDistanceX = (int) (xMove - xTouch);
                int distanceY = (int) (yMove - yTouch);
                if (!isLeftLayoutVisible && moveDistanceX >= touchSlop
                        && (isSliding || Math.abs(distanceY) <= touchSlop)) {
                    isSliding = true;
                    rightLayoutParams.rightMargin = -moveDistanceX;
                    if (rightLayoutParams.rightMargin > leftEdge) {
                        rightLayoutParams.rightMargin = leftEdge;
                    }
                    rightLayout.setLayoutParams(rightLayoutParams);
                }
                if (isLeftLayoutVisible && -moveDistanceX >= touchSlop) {
                    isSliding = true;
                    rightLayoutParams.rightMargin = rightEdge - moveDistanceX;
                    if (rightLayoutParams.rightMargin < rightEdge) {
                        rightLayoutParams.rightMargin = rightEdge;
                    }
                    rightLayout.setLayoutParams(rightLayoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                xUp = event.getRawX();
                int upDistanceX = (int) (xUp - xTouch);
                if (isSliding) {

                    if (wantToShowLeftLayout()) {
                        if (shouldScrollToLeftLayout()) {
                            scrollToLeftLayout();
                        } else {
                            scrollToRightLayout();
                        }
                    } else if (wantToShowRightLayout()) {
                        if (shouldScrollToRightLayout()) {
                            scrollToRightLayout();
                        } else {
                            scrollToLeftLayout();
                        }
                    }
                } else if (upDistanceX < touchSlop && isLeftLayoutVisible) {
                    scrollToRightLayout();
                }
                recycleVelocityTracker();
                break;
        }
        if (v.isEnabled()) {
            if (isSliding) {
                unFocusBindView();
                return true;
            }
            if (isLeftLayoutVisible) {
                return true;
            }
            return false;
        }
        return true;
    }


    private boolean wantToShowRightLayout() {
        return xUp - xTouch < 0 && isLeftLayoutVisible;
    }

    private boolean wantToShowLeftLayout() {
        return xUp - xTouch > 0 && !isLeftLayoutVisible;
    }


    private boolean shouldScrollToLeftLayout() {
        return xUp - xTouch > leftLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }


    private boolean shouldScrollToRightLayout() {
        return xTouch - xUp > leftLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }


    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

   
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

   
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

 
    private void unFocusBindView() {
        if (mBindView != null) {
            mBindView.setPressed(false);
            mBindView.setFocusable(false);
            mBindView.setFocusableInTouchMode(false);
        }
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int rightMargin = rightLayoutParams.rightMargin;
           
            while (true) {
                rightMargin = rightMargin + speed[0];
                if (rightMargin < rightEdge) {
                    rightMargin = rightEdge;
                    break;
                }
                if (rightMargin > leftEdge) {
                    rightMargin = leftEdge;
                    break;
                }
                publishProgress(rightMargin);
                
                sleep(15);
            }
            if (speed[0] > 0) {
                isLeftLayoutVisible = false;
            } else {
                isLeftLayoutVisible = true;
            }
            isSliding = false;
            return rightMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... rightMargin) {
            rightLayoutParams.rightMargin = rightMargin[0];
            rightLayout.setLayoutParams(rightLayoutParams);
            unFocusBindView();
        }

        @Override
        protected void onPostExecute(Integer rightMargin) {
            rightLayoutParams.rightMargin = rightMargin;
            rightLayout.setLayoutParams(rightLayoutParams);
        }
    }

    
    
    
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}*/
