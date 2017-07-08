package com.imooc.project.com.imooc.project.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.imooc.project.R;

/**
 * Created by Administrator on 2016/11/23.
 */

public class GuaGuaKaView extends View {


    /*定义画板*/

    private Bitmap mBitmap;
    private  Canvas mCanvas;

    private Path mPath;
    private Paint mOutterPaint;
    private int mLastX;
    private  int mLastY;

    private Bitmap bitmap;

    /*文本画笔*/
    private Paint mTextPaint;
    private String mText = "谢谢惠顾";
    private Rect mTextBound;

    private static final String TAG = "GuaGuaKaView";

    public GuaGuaKaView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaGuaKaView(Context context) {
        this(context,null);
    }

    public GuaGuaKaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mOutterPaint = new Paint();
        mPath = new Path();
        mOutterPaint.setColor(Color.parseColor("#c0c0c0"));
        mOutterPaint.setDither(true);
        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND);
        mOutterPaint.setStrokeWidth(30);

        mTextPaint = new Paint();
        mTextBound = new Rect();

       bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fg_guaguaka);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Log.e(TAG,"widthMeasureSpec =" + widthMeasureSpec + " heightMeasureSpec = " + heightMeasureSpec);
        Log.e(TAG,"width =" + width + " height = " + height);
        mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
       // mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        mOutterPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawRoundRect(new RectF(0,0,width,height),30,30,mOutterPaint);
        mCanvas.drawBitmap(bitmap,null,new RectF(0,0,width,height),null);

        mTextPaint.setColor(Color.RED);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(40);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {

       // canvas.drawBitmap(bitmap,0,0,null);
        canvas.drawText(mText,getWidth()/2-mTextBound.width()/2,getHeight()/2+mTextBound.height()/2,mTextPaint);
        drawPath();
        canvas.drawBitmap(mBitmap,0,0,null);
        super.onDraw(canvas);
    }

    private void drawPath() {
        mOutterPaint.setStyle(Paint.Style.STROKE);
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath,mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = Math.abs(x-mLastX);
                int dy = Math.abs(y-mLastY);
                if(dx >3 || dy >3) {
                    mPath.lineTo(x,y);
                }
                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }


}
