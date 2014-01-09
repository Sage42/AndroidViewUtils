package com.sage42.android.view.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

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
/**
 * Draw a ProgressBar but instead of being solid make it "segmented". This class also supports adding a "gradient" to
 * the colors.
 */
public class SegmentedProgressBar extends ProgressBar
{
    private static final int DEFAULT_SEGMENT_COUNT = 40;

    // start of the color gradient (defaults to black)
    private final int        mColorStartA;
    private final int        mColorStartR;
    private final int        mColorStartG;
    private final int        mColorStartB;

    // end of the color gradient (defaults to startColor)
    private final int        mColorEndA;
    private final int        mColorEndR;
    private final int        mColorEndG;
    private final int        mColorEndB;

    // unfilled color gradient (defaults to gray)
    private final int        mColorUnfilled;

    // interpolator used to calculate the color gradient (defaults to linear)
    private int              mGradientInterpolatorResId;

    // number of visible segments to draw (default: 40)
    private int              mSegmentCount;

    // paint used for drawing segments
    private Paint            mPaintSegment;

    /**
     * Default constructor.
     * 
     * @param context
     * @param attrs
     */
    public SegmentedProgressBar(final Context context, final AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    /**
     * Default constructor.
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SegmentedProgressBar(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);

        // extract params (if provided)
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.segmentedProgressBar);

        try
        {
            // colors
            final int startColor = args.getColor(R.styleable.segmentedProgressBar_startColor, R.color.black);
            int endColor = args.getColor(R.styleable.segmentedProgressBar_endColor, -1);
            if (endColor == -1)
            {
                endColor = startColor;
            }
            final int unfilledColor = args.getColor(R.styleable.segmentedProgressBar_unfilledColor, R.color.gray);

            // interpolator
            this.mGradientInterpolatorResId = args.getResourceId(R.styleable.segmentedProgressBar_android_interpolator,
                            -1);
            if (this.mGradientInterpolatorResId != -1)
            {
                this.setInterpolator(context, this.mGradientInterpolatorResId);
            }
            else
            {
                this.setInterpolator(new LinearInterpolator());
            }

            // other settings
            this.mSegmentCount = args.getInt(R.styleable.segmentedProgressBar_segmentCount,
                            SegmentedProgressBar.DEFAULT_SEGMENT_COUNT);

            // create colors based on supplied args
            this.mColorStartA = Color.alpha(startColor);
            this.mColorStartR = Color.red(startColor);
            this.mColorStartG = Color.green(startColor);
            this.mColorStartB = Color.blue(startColor);

            this.mColorEndA = Color.alpha(endColor);
            this.mColorEndR = Color.red(endColor);
            this.mColorEndG = Color.green(endColor);
            this.mColorEndB = Color.blue(endColor);

            this.mColorUnfilled = Color.argb(Color.alpha(unfilledColor), Color.red(unfilledColor),
                            Color.blue(unfilledColor), Color.green(unfilledColor));

            // init paints
            this.mPaintSegment = new Paint();
            this.mPaintSegment.setColor(startColor);
            this.mPaintSegment.setStyle(Style.FILL);
            this.mPaintSegment.setAntiAlias(true);
        }
        finally
        {
            args.recycle();
        }
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas)
    {
        // retrieve the overall dimensions
        final int totalWidth = this.getWidth();
        final int segmentHeight = this.getHeight();
        final int top = this.getTop();

        // calculate the size of a "segment"
        // NOTE: there is no "invisible segment drawn after the last, hence the -1
        final int totalSegments = (this.mSegmentCount * 2) - 1;
        final double segmentWidth = totalWidth / (double) totalSegments;

        // calculate how much of the bar to fill
        final double progressPercentage = this.getProgress() / (double) this.getMax();

        // draw segments
        for (int currentSegment = 0; currentSegment < this.mSegmentCount; currentSegment++)
        {
            // calculate lateral position
            final double left = currentSegment * segmentWidth * 2;

            // get color for this segment
            final double position = currentSegment / (double) this.mSegmentCount;
            if (position < progressPercentage)
            {
                this.mPaintSegment.setColor(this.getColor(currentSegment));
            }
            else
            {
                this.mPaintSegment.setColor(this.mColorUnfilled);
            }

            // draw segment
            canvas.drawRect(Double.valueOf(left).floatValue(), Double.valueOf(top).floatValue(),
                            Double.valueOf(left + segmentWidth).floatValue(), Double.valueOf(top + segmentHeight)
                                            .floatValue(), this.mPaintSegment);
        }
    }

    private int getColor(final int currentSegment)
    {
        final float percentage = this.getInterpolator().getInterpolation((float) currentSegment / this.mSegmentCount);

        final float alpha = ((this.mColorEndA - this.mColorStartA) * percentage) + this.mColorStartA;
        final float red = ((this.mColorEndR - this.mColorStartR) * percentage) + this.mColorStartR;
        final float green = ((this.mColorEndG - this.mColorStartG) * percentage) + this.mColorStartG;
        final float blue = ((this.mColorEndB - this.mColorStartB) * percentage) + this.mColorStartB;

        return Color.argb((int) alpha, (int) red, (int) green, (int) blue);
    }
}
