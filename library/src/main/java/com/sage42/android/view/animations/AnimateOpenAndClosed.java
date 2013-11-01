package com.sage42.android.view.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

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