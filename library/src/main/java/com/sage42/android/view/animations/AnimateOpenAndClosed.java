package com.sage42.android.view.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimateOpenAndClosed extends Animation
{
    private final int     mHeightOpen;
    private final int     mHeightClose;
    private final View    mView;
    private final boolean mIsOpening;

    public AnimateOpenAndClosed(final View view, final int openHeight, final int closedHeight, final boolean isOpening)
    {
        this.mView = view;
        this.mHeightOpen = openHeight;
        this.mHeightClose = closedHeight;
        this.mIsOpening = isOpening;
    }

    @Override
    protected void applyTransformation(final float interpolatedTime, final Transformation t)
    {
        int newHeight;
        final int diff = this.mHeightOpen - this.mHeightClose;
        if (this.mIsOpening)
        {
            newHeight = this.mHeightClose + (int) (diff * interpolatedTime);
        }
        else
        {
            newHeight = this.mHeightClose + (int) (diff * (1 - interpolatedTime));
        }
        this.mView.getLayoutParams().height = newHeight;
        this.mView.requestLayout();
    }

    @Override
    public void initialize(final int width, final int height, final int parentWidth, final int parentHeight)
    {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds()
    {
        return true;
    }

}