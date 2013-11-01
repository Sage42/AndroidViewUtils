package com.sage42.android.view.animations;

import com.sage42.android.view.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

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
public class ClickToOpenCloseLayout extends RelativeLayout
{
    private static final int     INITIAL_ANIMATION_DELAY            = 100;

    private static final int     DEFAULT_HEIGHT_CLOSED_DP           = 50;
    private static final int     DEFAULT_INITIAL_ANIMATION_DURATION = 100;
    private static final int     DEFAULT_ANIMATION_DURATION         = 300;

    // flag to help the animations
    protected boolean            mOpen                              = true;

    // dimensions (in px) when closed
    protected int                mHeightClosed;

    // dimensions (in px) when open (default: 50dp)
    protected int                mHeightOpen;

    // close animation on load (default: 100ms)
    protected long               mInitialAnimationDuration;

    // open/close toggle animation duration (default: 300ms)
    protected long               mAnimationDuration;

    private AnimateOpenAndClosed mCloseAnimation;

    private AnimateOpenAndClosed mOpenAnimation;

    public ClickToOpenCloseLayout(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        this.init(context, attrs);
    }

    public ClickToOpenCloseLayout(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs)
    {
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.clickToOpenClosedLayout);

        // extract closed height from attribs (or use default) and convert to px
        final int closedHeight = args
                        .getInt(R.styleable.clickToOpenClosedLayout_closedHeight, DEFAULT_HEIGHT_CLOSED_DP);

        this.mHeightClosed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, closedHeight, this
                        .getResources().getDisplayMetrics());

        // extra initial close animation duration from attribs
        this.mInitialAnimationDuration = args.getInt(R.styleable.clickToOpenClosedLayout_initialAnimationDuration,
                        DEFAULT_INITIAL_ANIMATION_DURATION);

        // extra open/close animation duration from attribs
        this.mAnimationDuration = args.getInt(R.styleable.clickToOpenClosedLayout_animationDuration,
                        DEFAULT_ANIMATION_DURATION);
        args.recycle();
    }

    private void calculateOpenSizeAndShrink()
    {
        final int height = this.getHeight();
        if (this.mHeightOpen == 0 && height > 0)
        {
            // update open size to required space
            this.mHeightOpen = height;

            // animate closed (so that people know they can click to toggle it)
            this.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ClickToOpenCloseLayout.this.mOpen = false;

                    final AnimateOpenAndClosed animation = new AnimateOpenAndClosed(ClickToOpenCloseLayout.this,
                                    ClickToOpenCloseLayout.this.mHeightOpen, ClickToOpenCloseLayout.this.mHeightClosed,
                                    false);
                    animation.setDuration(ClickToOpenCloseLayout.this.mInitialAnimationDuration);
                    ClickToOpenCloseLayout.this.startAnimation(animation);
                }
            }, INITIAL_ANIMATION_DELAY);

            // initialize animations
            // must be done here so that we have the open dimension
            // setup the open and close animations
            this.mCloseAnimation = new AnimateOpenAndClosed(this, this.mHeightOpen, this.mHeightClosed, false);
            this.mCloseAnimation.setDuration(this.mAnimationDuration);

            this.mOpenAnimation = new AnimateOpenAndClosed(this, this.mHeightOpen, this.mHeightClosed, true);
            this.mOpenAnimation.setDuration(this.mAnimationDuration);
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        // force calculation of the "open" dimension
        this.calculateOpenSizeAndShrink();
    }

    public void toggleDisplay()
    {
        if (this.mOpen)
        {
            // hide
            this.mOpen = false;

            this.startAnimation(this.mCloseAnimation);
        }
        else
        {
            // show
            this.mOpen = true;

            this.startAnimation(this.mOpenAnimation);
        }
    }
}
