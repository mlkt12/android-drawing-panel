package com.mlkt.development.drawingpanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class DrawingPanel extends View implements View.OnTouchListener {

    private LinkedHashMap<Path, Paint> ppMap = new LinkedHashMap<>();

    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;

    private int brushColor = getRandomColor();
    private float strokeWidth = 30; // Default
    public boolean rainbowMode = true;

    private void initSettings(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(brushColor);
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setBrushColor(int color) {
        brushColor = color;
        initSettings();
        invalidate();
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
        initSettings();
    }

    public void setRainbowMode(boolean rainbowMode) {
        this.rainbowMode = rainbowMode;
    }

    public void undo(){
        if (ppMap.size() > 0) {
            try {
                ppMap.remove(Objects.requireNonNull(ppMap.keySet().toArray())[ppMap.size()-1]);
                invalidate();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public DrawingPanel(Context context) {
        super(context);
        init();
    }

    public DrawingPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        mCanvas = new Canvas();
        mPath = new Path();
        initSettings();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (ppMap != null) {
            for (Map.Entry<Path, Paint> entry : ppMap.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    canvas.drawPath(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 0;

    private void touchStart(float x, float y) {
        mPath = new Path();
        ppMap.put(mPath,mPaint);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
        if (mPaint != null) {
            mCanvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            if (mPaint != null) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchStart(x, y);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchMove(x, y);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        touchUp();
                        if (rainbowMode) {
                            brushColor = getRandomColor();
                            initSettings();
                        }
                        invalidate();
                        break;
                }
            }

        return true;
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

}
