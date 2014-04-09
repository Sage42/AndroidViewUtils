package com.sage42.android.view.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sage42.android.view.animations.ClickToOpenCloseLayout;

public class ExpandAndShrinkCardView extends RelativeLayout
{
    // unique index for this layout. Important for state preservations
    private long                   mUuid;

    private ClickToOpenCloseLayout mParentContainer;

    /**
     * Default constructor.
     * 
     * @param context
     */
    public ExpandAndShrinkCardView(final Context context)
    {
        super(context);
        this.mUuid = 0;
    }

    /**
     * Default constructor.
     * 
     * @param context
     * @param attrs
     */
    public ExpandAndShrinkCardView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        this.mUuid = 0;
    }

    /**
     * Default constructor.
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExpandAndShrinkCardView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        this.mUuid = 0;
    }

    public long getUuid()
    {
        return this.mUuid;
    }

    public void setUuid(final long uuid)
    {
        this.mUuid = uuid;
    }

    public ClickToOpenCloseLayout getParentContainer()
    {
        return this.mParentContainer;
    }

    public void setParentContainer(final ClickToOpenCloseLayout parentContainer)
    {
        this.mParentContainer = parentContainer;
    }

    /**
     * Show/Hide display of this view.
     */
    public void toggleDisplay()
    {
        if (this.mParentContainer != null)
        {
            this.mParentContainer.toggleDisplay();
        }
    }

}
