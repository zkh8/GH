package com.example.gh.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.gh.R;

import java.util.ArrayList;

public class NineLuckPan extends View {
    private Paint mPaint;
    private ArrayList<RectF> mRects;//存储矩形的集合
    private float mStrokWidth = 1;//矩形的描边宽度
    private int[] mItemColor = {Color.parseColor("#8C0405"), Color.parseColor("#8C0405")};//矩形的颜色
    private int mRectSize;//矩形的宽和高（矩形为正方形）
    private boolean mClickStartFlag = false;//是否点击中间矩形的标记
    private int mRepeatCount = 3;//转的圈数
    private int mLuckNum = 3;//最终中奖位置
    private int mPosition = -1;//抽奖块的位置
    private int mStartLuckPosition = 0;//开始抽奖的位置
    private int [] mImgs;
    private String[] mLuckStr;
    private OnLuckPanAnimEndListener onLuckPanAnimEndListener;


    public OnLuckPanAnimEndListener getOnLuckPanAnimEndListener() {
        return onLuckPanAnimEndListener;
    }

    public void setOnLuckPanAnimEndListener(OnLuckPanAnimEndListener onLuckPanAnimEndListener) {
        this.onLuckPanAnimEndListener = onLuckPanAnimEndListener;
    }

    public NineLuckPan(Context context) {
        this(context,null);
    }

    public NineLuckPan(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NineLuckPan(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getmLuckNum() {
        return mLuckNum;
    }

    public void setmLuckNum(int mLuckNum) {
        this.mLuckNum = mLuckNum;
    }


    public void setmRepeatCount(int mRepeatCount) {
        this.mRepeatCount = mRepeatCount;
    }

    public int[] getmImgs() {
        return mImgs;
    }

    public void setmImgs(int[] mImgs) {
        this.mImgs = mImgs;
        invalidate();
    }

    public String[] getmLuckStr() {
        return mLuckStr;
    }

    public void setmLuckStr(String[] mLuckStr) {
        this.mLuckStr = mLuckStr;
        invalidate();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mStrokWidth);

        mRects = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectSize = Math.min(w, h)/3;//获取矩形的宽和高
        mRects.clear();//当控件大小改变的时候清空数据
        initRect();//重新加载矩形数据
    }

    /**
     * 加载矩形数据
     */
    private void initRect() {
        //加载前三个矩形
        for(int x = 0;x<3;x++){
            float left = x * mRectSize;
            float top = 0;
            float right  = (x + 1) * mRectSize;
            float bottom = mRectSize;
            RectF rectF = new RectF(left,top,right,bottom);
            mRects.add(rectF);
        }
        //加载第四个
        mRects.add(new RectF(getWidth()-mRectSize,mRectSize,getWidth(),mRectSize * 2));
        //加载第五~七个
        for(int y= 3;y>0;y--){
            float left = getWidth() - (4-y) * mRectSize;
            float top = mRectSize * 2;
            float right  = (y - 3) * mRectSize+getWidth();
            float bottom = mRectSize * 3;
            RectF rectF = new RectF(left,top,right,bottom);
            mRects.add(rectF);
        }
        //加载第八个
        mRects.add(new RectF(0,mRectSize,mRectSize,mRectSize * 2));
        //加载第九个
        mRects.add(new RectF(mRectSize,mRectSize,mRectSize*2,mRectSize * 2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(mLuckNum < 0){

            return false;
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(mRects.get(8).contains(event.getX(),event.getY())){
                mClickStartFlag = true;
            }else {
                mClickStartFlag = false;
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){

            if(mClickStartFlag){
                if(mRects.get(8).contains(event.getX(),event.getY())){

                    startAnim();//判断只有手指落下和抬起都在中间的矩形内才开始抽奖
                }
                mClickStartFlag = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRects(canvas);
        drawImages(canvas);
        drawNineText(canvas);
    }

    /**
     * 画图片
     * @param canvas
     */
    private void drawImages(Canvas canvas) {
        for (int x = 0;x<mRects.size();x++){
            RectF rectF = mRects.get(x);
            float left = rectF.centerX() - mRectSize / 4;
            float top = rectF.centerY() - mRectSize / 4;

            if(x == 8){

                canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.cjb4),mRectSize,mRectSize,false),rectF.centerX() - mRectSize / 2,rectF.centerY() - mRectSize / 2,null);

            }else{

                canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.cjb1),mRectSize,mRectSize,false),rectF.centerX() - mRectSize / 2,rectF.centerY() - mRectSize / 2,null);
                canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),mImgs[x]),mRectSize/2,mRectSize/2,false),left,top,null);
            }
        }
    }
    private void drawNineText(Canvas canvas) {
        for (int i = 0; i < mRects.size(); i++) {
            RectF rectF = mRects.get(i);
            float x = rectF.left + mRectSize / 4; // 将文字设置在每个矩形中央
            float y = rectF.top + mRectSize - 40;
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(32); // unit px
            if (null != mLuckStr[i]) {
                if (i == mRects.size() - 1) {

                    mPaint.setTextSize(64);
                    mPaint.setColor(Color.RED);

                    String[] strings = mLuckStr[i].split("\n");

                    if(strings.length > 1){

                        Paint.FontMetrics fm = mPaint.getFontMetrics();
                        float offsetY = fm.descent - fm.ascent;

                        canvas.drawText(strings[0], rectF.left + mRectSize / 4, rectF.top + mRectSize / 2 - offsetY / 3, mPaint);
                        canvas.drawText(strings[1], rectF.left + mRectSize / 4, rectF.top + mRectSize / 2 + offsetY * 2 / 3, mPaint);
                    }else{

                        canvas.drawText(mLuckStr[i], rectF.left + mRectSize / 4, rectF.top + mRectSize / 4, mPaint);
                    }

                } else {

                    canvas.drawText(mLuckStr[i], x, y, mPaint);
                }
            }
        }
    }

    /**
     * 画矩形
     * @param canvas
     */
    private void drawRects(Canvas canvas) {
        for (int x = 0;x<mRects.size();x++){
            RectF rectF = mRects.get(x);
            if(x == 8){
                mPaint.setColor(mItemColor[x%2]);
                canvas.drawRect(rectF, mPaint);
            }else {
                mPaint.setColor(mItemColor[x%2]);
                if(mPosition == x){
                    mPaint.setColor(Color.parseColor("#FFD600"));
                }
                canvas.drawRect(rectF, mPaint);
            }
        }
    }
    public void setPosition(int position){
        mPosition = position;
        invalidate();
    }
    /**
     * 开始动画
     */
    private void startAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mStartLuckPosition, mRepeatCount * 8 + mLuckNum).setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int position = (int) animation.getAnimatedValue();
                setPosition(position%8);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mStartLuckPosition = mLuckNum;
                if(onLuckPanAnimEndListener!=null){
                    onLuckPanAnimEndListener.onAnimEnd(mPosition,mLuckStr[mPosition]);
                }
            }
        });
        valueAnimator.start();
    }
    public interface OnLuckPanAnimEndListener{
        void onAnimEnd(int position,String msg);
    }
}