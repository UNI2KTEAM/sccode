package com.uni2k.demo;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.uni2k.demo.utils.DensityUtils;


/**
 * Created by lynn.shao
 */

public class GraphicOverlay extends View {

    private static final String TAG = "Graphic overlay";

    private Context mContext;
    private Paint mBackgroundPaint;
    private Paint mLinePaint;
    private Paint mTargetPaint;
    private Rect mTargetRect = new Rect();
    private int model=3;
    private int   h=0 ;
    private int   w=0;
    private int x=0;
    private int y=0;
    public GraphicOverlay(Context context) {
        super(context);
        init(context);
    }

    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.parseColor("#593D6390"));


        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(10.0f);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTargetPaint = new Paint();
        mTargetPaint.setColor(Color.WHITE);
        mTargetPaint.setStrokeWidth(2.0f);
        mTargetPaint.setStyle(Paint.Style.STROKE);
    }
    public void changeBackgroundColor(int color){
        if(mBackgroundPaint!=null){
            mBackgroundPaint.setColor(color);
        }
    }

    public void clear() {
        postInvalidate();
    }

    public void setModel(int model){
        this.model=model;
    }
    public void setBackgroundAlpha(int a){
        mBackgroundPaint.setAlpha(a);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int middleHeight = getHeight() / 2;
        int middleWidth = getWidth() / 2;
        if(model==11){
            h= DensityUtils.dp2px(getContext(),240);
            w=DensityUtils.dp2px(getContext(),240);
        }else if(model==1){
            int _height= DensityUtils.SCREEN_HEIGHT(getContext());
            int screenW=DensityUtils.SCREEN_WIDTH(getContext());
            if(screenW/_height==1){
                //如果屏幕宽高比 接近1 设置固定的高度 不取1/3 比如折叠屏展开的时候
                _height=DensityUtils.dp2px(getContext(),180);
            }else {
                _height = _height / 3;
                _height = _height + (_height / 10)*2;
            }
            h = _height;
            double limit=(double) getDimensionForVersionWidth()/getDimensionForVersionHeight();
            w= (int) (_height*limit);
        }else if(model==2) {
            int _height= DensityUtils.SCREEN_HEIGHT(getContext());
            int screenW=DensityUtils.SCREEN_WIDTH(getContext());
            if(screenW/_height==1){
                //如果屏幕宽高比 接近1 设置固定的高度 不取1/3 比如折叠屏展开的时候
                _height=DensityUtils.dp2px(getContext(),120);
            }else {
                _height = _height / 3;
                _height = _height - (_height / 10);
            }
            h = _height;
            double limit=(double) getDimensionForVersionWidth()/getDimensionForVersionHeight();
            w= (int) (_height*limit);
        } else {
            int _height= DensityUtils.SCREEN_HEIGHT(getContext());
            int screenW=DensityUtils.SCREEN_WIDTH(getContext());
            if(screenW/_height==1){
                //如果屏幕宽高比 接近1 设置固定的高度 不取1/3 比如折叠屏展开的时候
                _height=DensityUtils.dp2px(getContext(),120);
            }else {
                _height = _height / 3;
                _height = _height - (_height / 10);
            }
            h = _height;
            double limit=(double) getDimensionForVersionWidth()/getDimensionForVersionHeight();
            w= (int) (_height*limit);
        }
        mTargetRect.set(middleWidth - w / 2,middleHeight - h / 2, middleWidth + w / 2, middleHeight + h / 2);
        int size=h;
        canvas.save();
        canvas.clipRect(mTargetRect, Region.Op.DIFFERENCE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);
        canvas.drawRect(mTargetRect, mLinePaint);
        canvas.restore();
        size = Math.min(mTargetRect.width(), mTargetRect.height());
        canvas.drawPath(createCornersPath(mTargetRect, (int)(0.1f * size), (int)(0.1f * size)), mTargetPaint);
    }


    private Path createCornersPath(Rect rect, int margin, int cornerWidth){
        Path path = new Path();

        path.moveTo(rect.left + margin, rect.top + margin + cornerWidth);
        path.lineTo(rect.left + margin, rect.top + margin);
        path.lineTo(rect.left + margin + cornerWidth, rect.top + margin);

        path.moveTo(rect.right - margin - cornerWidth, rect.top + margin);
        path.lineTo(rect.right - margin, rect.top + margin);
        path.lineTo(rect.right - margin , rect.top + margin + cornerWidth);

        path.moveTo(rect.left + margin, rect.bottom - margin - cornerWidth);
        path.lineTo(rect.left + margin, rect.bottom - margin);
        path.lineTo(rect.left + margin + cornerWidth, rect.bottom - margin);

        path.moveTo(rect.right - margin - cornerWidth, rect.bottom - margin);
        path.lineTo(rect.right - margin, rect.bottom - margin);
        path.lineTo(rect.right - margin, rect.bottom - margin - cornerWidth);


        return path;
    }
    public int getRectWith(){
        return w;
    }
    public int getRectHeight(){
        return h;
    }
    public int getRectX(){
        return mTargetRect.left;
    }
    public int getRectY(){
        return mTargetRect.top;
    }

    public boolean isPortraitMode() {
        int orientation = mContext.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    public int getDimensionForVersionWidth()
    {
        return  160+( model-1)*149+4;
    }
    public int getDimensionForVersionHeight()
    {
        return  108+4;
    }
}
