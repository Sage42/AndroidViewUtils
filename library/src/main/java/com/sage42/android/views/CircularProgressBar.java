package com.sage42.android.views;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sage42.android.view.R;

/**
 * Copyright (C) 2013- Sage 42 App Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Corey Scott (corey.scott@sage42.com)
 *
 */
public class CircularProgressBar extends View
{
    private static final float ADJUST_FOR_12_OCLOCK = 270f;

    // properties for the background circle
    private final int          mBgColor;
    private final int          mBgStrokeWidth;
    private final Paint        mBgPaint;

    // properties for the progress circle
    private final int          mProgressColor;
    private final int          mProgressStrokeWidth;
    private final Paint        mProgressPaint;

    // text properties for the countdown text
    private boolean            mShowText;
    private final int          mTextSize;
    private final int          mTextColor;
    private final Paint        mTextPaint;

    // maximum number of points in the circle default is 100
    private int                mMax;

    // current progress between 0 and mMax
    private int                mProgress            = 0;

    // diameter (in dp) of the circle
    private float              mDiameter;

    // margin between circle and edges (default is 4dp)
    // NOTE: you will need to include some margin to account for the stroke width, so min padding is strokeWidth/2
    private int                mLayoutMargin;

    // area to draw the progress arc
    private RectF              mArcBounds;

    // height taken to draw text with the current settings
    private Rect               mTextBounds;

    public CircularProgressBar(final Context context, final AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    public CircularProgressBar(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        // extract params (if provided)
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.circularProgressBar);

        final float defaultDiameter = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, this.getResources()
                        .getDisplayMetrics());
        final float defaultStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, this.getResources()
                        .getDisplayMetrics());
        final float defaultMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, this.getResources()
                        .getDisplayMetrics());
        final float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, this.getResources()
                        .getDisplayMetrics());

        try
        {
            this.mBgColor = args.getColor(R.styleable.circularProgressBar_bgColor, R.color.black);
            this.mBgStrokeWidth = args.getDimensionPixelSize(R.styleable.circularProgressBar_bgStrokeWidth,
                            (int) defaultStrokeWidth);

            this.mProgressColor = args.getColor(R.styleable.circularProgressBar_progressColor, R.color.white);
            this.mProgressStrokeWidth = args.getDimensionPixelSize(R.styleable.circularProgressBar_progressStrokeWidth,
                            (int) defaultStrokeWidth);

            this.mShowText = args.getBoolean(R.styleable.circularProgressBar_showText, false);
            this.mTextSize = args.getDimensionPixelSize(R.styleable.circularProgressBar_android_textSize,
                            (int) defaultTextSize);
            this.mTextColor = args.getInt(R.styleable.circularProgressBar_android_textColor, R.color.white);

            this.mLayoutMargin = args.getDimensionPixelSize(R.styleable.circularProgressBar_android_layout_margin,
                            (int) defaultMargin);

            this.mMax = args.getInt(R.styleable.circularProgressBar_max, 100);

            this.mDiameter = args.getDimension(R.styleable.circularProgressBar_diameter, defaultDiameter);
        }
        finally
        {
            args.recycle();
        }

        // create paint settings based on supplied args
        this.mBgPaint = new Paint();
        this.mBgPaint.setColor(this.mBgColor);
        this.mBgPaint.setStyle(Style.STROKE);
        this.mBgPaint.setStrokeWidth(this.mBgStrokeWidth);

        this.mProgressPaint = new Paint();
        this.mProgressPaint.setColor(this.mProgressColor);
        this.mProgressPaint.setStyle(Style.STROKE);
        this.mProgressPaint.setStrokeWidth(this.mProgressStrokeWidth);

        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setStyle(Style.STROKE);
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mTextPaint.setTextSize(this.mTextSize);
    }

    @Override
    protected void onDraw(final Canvas canvas)
    {
        if (this.mArcBounds == null)
        {
            // set view bounds for arc drawing
            this.mArcBounds = new RectF(this.mLayoutMargin, this.mLayoutMargin, this.mLayoutMargin + this.mDiameter,
                            this.mLayoutMargin + this.mDiameter);
        }

        // draw bg circle in the center
        final float radius = (this.mDiameter / 2);
        final float center = radius + this.mLayoutMargin;
        canvas.drawCircle(center, center, radius, this.mBgPaint);

        // draw any progress over the top
        // why is this BigDecimal crap even needed? java why?
        final BigDecimal percentage = BigDecimal.valueOf(this.mProgress).divide(BigDecimal.valueOf(this.mMax), 4,
                        RoundingMode.HALF_DOWN);
        final BigDecimal sweepAngle = percentage.multiply(BigDecimal.valueOf(360));

        // bounds are same as the bg circle, so diameter width and height moved in by margin
        canvas.drawArc(this.mArcBounds, ADJUST_FOR_12_OCLOCK, sweepAngle.floatValue(), false, this.mProgressPaint);

        if (this.mShowText)
        {
            if (this.mTextBounds == null)
            {
                // Reference: http://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android
                // answer #2
                this.mTextBounds = new Rect();
                this.mTextPaint.getTextBounds("0", 0, 1, this.mTextBounds); //$NON-NLS-1$
            }

            // draw text in the center
            canvas.drawText(String.valueOf(this.mProgress), center, center + (this.mTextBounds.height() >> 1),
                            this.mTextPaint);
        }
    }

    public void setProgress(final int progress)
    {
        this.mProgress = progress;

        // force redraw
        this.invalidate();
        this.requestLayout();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        // size will always be diameter + margin on add sides
        final int size = (int) this.mDiameter + (this.mLayoutMargin * 2);
        this.setMeasuredDimension(size, size);
    }
}
