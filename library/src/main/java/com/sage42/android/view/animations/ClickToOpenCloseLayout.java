package com.sage42.android.view.animations;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.sage42.android.view.R;
import com.sage42.android.view.ui.ExpandAndShrinkLayoutStates;

/**
 * Copyright (C) 2013- Sage 42 App Sdn Bhd Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 * 
 * @author Corey Scott (corey.scott@sage42.com)
 */
public class ClickToOpenCloseLayout extends LinearLayout
{
    private static final int                INITIAL_ANIMATION_DELAY            = 100;

    private static final int                DEFAULT_HEIGHT_CLOSED_DP           = 50;
    private static final int                DEFAULT_MAX_HEIGHT_OPENED_DP       = 500;
    private static final int                DEFAULT_INITIAL_ANIMATION_DURATION = 100;
    private static final int                DEFAULT_ANIMATION_DURATION         = 300;

    // unique index for this layout. Important for state preservations
    private long                            mUuid;

    // dimensions (in px) when closed
    protected int                           mHeightClosed;

    // max dimensions (in px) when opened
    protected int                           mMaxHeightOpen;

    // dimensions (in px) when open (default: 50dp)
    protected int                           mHeightOpen;

    // close animation on load (default: 100ms)
    protected long                          mInitialAnimationDuration;

    // open/close toggle animation duration (default: 300ms)
    protected long                          mAnimationDuration;

    private AnimateOpenAndClosed            mCloseAnimation;

    private AnimateOpenAndClosed            mOpenAnimation;

    private IClickToOpenCloseLayoutListener mListener;

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
        final int closedHeight = args.getInt(R.styleable.clickToOpenClosedLayout_closedHeight,
                        ClickToOpenCloseLayout.DEFAULT_HEIGHT_CLOSED_DP);

        this.mHeightClosed = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, closedHeight, this
                        .getResources().getDisplayMetrics());

        // extract max open height from attribs (or use default) and convert to px
        final int maxHeightOpen = args.getInt(R.styleable.clickToOpenClosedLayout_maxCardHeight,
                        ClickToOpenCloseLayout.DEFAULT_MAX_HEIGHT_OPENED_DP);

        this.mMaxHeightOpen = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, maxHeightOpen, this
                        .getResources().getDisplayMetrics());

        // extra initial close animation duration from attribs
        this.mInitialAnimationDuration = args.getInt(R.styleable.clickToOpenClosedLayout_initialAnimationDuration,
                        ClickToOpenCloseLayout.DEFAULT_INITIAL_ANIMATION_DURATION);

        // extra open/close animation duration from attribs
        this.mAnimationDuration = args.getInt(R.styleable.clickToOpenClosedLayout_animationDuration,
                        ClickToOpenCloseLayout.DEFAULT_ANIMATION_DURATION);
        args.recycle();
    }

    public void onLayoutReuse(final long newIndex)
    {
        // Reset uuid
        this.mUuid = newIndex;

        // Allow Recalculation of Layout Height based on state
        this.mHeightOpen = 0;

        if (this.getListener() != null)
        {
            // Reset state
            this.getListener().onLayoutStateChange(newIndex, ExpandAndShrinkLayoutStates.INITIAL);
        }

        // Reset Animation
        if (this.mOpenAnimation != null)
        {
            this.mOpenAnimation.cancel();
            this.mOpenAnimation = null;
        }
        if (this.mCloseAnimation != null)
        {
            this.mCloseAnimation.cancel();
            this.mCloseAnimation = null;
        }

        this.requestLayout();
    }

    protected void requestShrink()
    {
        // Cache current UUID, State
        final long currentUuid = this.getUuid();
        final ExpandAndShrinkLayoutStates currentState = this.getListener().getLayoutState(currentUuid);
        final int openHeight = this.mHeightOpen;
        final int closeHeight = this.mHeightClosed;

        if (this.getListener() == null)
        {
            return;
        }

        // animate closed (so that people know they can click to toggle it)
        this.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (currentState == ExpandAndShrinkLayoutStates.OPEN
                                || currentState == ExpandAndShrinkLayoutStates.INITIAL)
                {

                    if (openHeight > closeHeight)
                    {
                        final AnimateOpenAndClosed animation = new AnimateOpenAndClosed(ClickToOpenCloseLayout.this,
                                        openHeight, closeHeight, false);
                        animation.setDuration(ClickToOpenCloseLayout.this.mInitialAnimationDuration);
                        ClickToOpenCloseLayout.this.startAnimation(animation);

                        // Set State to closed
                        ClickToOpenCloseLayout.this.getListener().onLayoutStateChange(currentUuid,
                                        ExpandAndShrinkLayoutStates.CLOSE);
                    }
                }
            }
        }, ClickToOpenCloseLayout.INITIAL_ANIMATION_DELAY);
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        ExpandAndShrinkLayoutStates currentState = null;
        if (this.getListener() != null)
        {
            currentState = this.getListener().getLayoutState(this.getUuid());
        }

        if (this.mHeightOpen == 0)
        {
            // Let Child decide how much space they actually need to show a fully opened card
            final int realHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mMaxHeightOpen,
                            View.MeasureSpec.AT_MOST);

            super.onMeasure(widthMeasureSpec, realHeightMeasureSpec);

            this.mHeightOpen = this.getMeasuredHeight();

            // Re-init animation
            this.initAnimations();

            // Restore current card height (Important after recycling)
            if (currentState == ExpandAndShrinkLayoutStates.INITIAL)
            {
                this.getLayoutParams().height = this.getMeasuredHeight();
                // Initial pass. Request shrink
                this.requestShrink();
            }
            else if (currentState == ExpandAndShrinkLayoutStates.OPEN)
            {
                this.getLayoutParams().height = this.getMeasuredHeight();
            }
            else if (currentState == ExpandAndShrinkLayoutStates.CLOSE)
            {
                this.getLayoutParams().height = this.mHeightClosed;
            }

        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void toggleDisplay()
    {
        if (!this.isAnimationInitialized() || this.mListener == null)
        {
            // Not Setup Yet
            return;
        }

        final long currentUuid = this.getUuid();
        final ExpandAndShrinkLayoutStates currentState = this.mListener.getLayoutState(currentUuid);

        if (currentState == null)
        {
            return;
        }

        switch (currentState)
        {
            case OPEN:
                this.mListener.onLayoutStateChange(currentUuid, ExpandAndShrinkLayoutStates.CLOSE);
                this.startAnimation(this.mCloseAnimation);
                break;

            case CLOSE:
                this.mListener.onLayoutStateChange(currentUuid, ExpandAndShrinkLayoutStates.OPEN);
                this.startAnimation(this.mOpenAnimation);
                break;
            case INITIAL:
                this.requestShrink();
            default:
                // Do nothing
                break;
        }
    }

    /**
     * Set the animation duration. This should normally be done in the XML as setting it here will cause the animations
     * to be re-defined.
     * 
     * @param animationDuration
     *            the animationDuration to set
     */
    public void setAnimationDuration(final long animationDuration)
    {
        this.mAnimationDuration = animationDuration;

        // re-init the animations
        this.initAnimations();
    }

    private void initAnimations()
    {
        // must be done here so that we have the open dimension
        // setup the open and close animations
        this.mCloseAnimation = new AnimateOpenAndClosed(this, this.mHeightOpen, this.mHeightClosed, false);
        this.mCloseAnimation.setDuration(this.mAnimationDuration);

        this.mOpenAnimation = new AnimateOpenAndClosed(this, this.mHeightOpen, this.mHeightClosed, true);
        this.mOpenAnimation.setDuration(this.mAnimationDuration);
    }

    private boolean isAnimationInitialized()
    {
        if (this.mCloseAnimation == null || this.mOpenAnimation == null)
        {
            return false;
        }

        return true;
    }

    public long getUuid()
    {
        return this.mUuid;
    }

    public void setUuid(final long uuid)
    {
        this.mUuid = uuid;
    }

    public IClickToOpenCloseLayoutListener getListener()
    {
        return this.mListener;
    }

    public void setListener(final IClickToOpenCloseLayoutListener listener)
    {
        this.mListener = listener;
    }

    public static interface IClickToOpenCloseLayoutListener
    {
        void onLayoutStateChange(final long index, final ExpandAndShrinkLayoutStates newState);

        ExpandAndShrinkLayoutStates getLayoutState(final long index);
    }

}
