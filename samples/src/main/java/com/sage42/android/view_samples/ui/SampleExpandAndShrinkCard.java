package com.sage42.android.view_samples.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;

import com.sage42.android.view.ui.ExpandAndShrinkCardView;
import com.sage42.android.view_samples.ui.SampleExpandShrinkListAdapter.ViewHolder;

public class SampleExpandAndShrinkCard extends ExpandAndShrinkCardView
{

    /**
     * Default constructor.
     * 
     * @param context
     */
    public SampleExpandAndShrinkCard(final Context context)
    {
        super(context);
    }

    /**
     * Default constructor.
     * 
     * @param context
     * @param attrs
     */
    public SampleExpandAndShrinkCard(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Default constructor.
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SampleExpandAndShrinkCard(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /*
     * Bind the sample title and text into our card view
     */
    public void bind(final Cursor cursor)
    {
        final Object tag = this.getTag();
        if (tag != null && tag instanceof ViewHolder)
        {
            final ViewHolder viewHolder = (ViewHolder) tag;

            // Get Title and Text
            if (cursor != null)
            {
                final String title = cursor.getString(cursor
                                .getColumnIndexOrThrow(ExpandShrinkListViewActivity.COLUMN_NAME_TITLE));
                final String text = cursor.getString(cursor
                                .getColumnIndexOrThrow(ExpandShrinkListViewActivity.COLUMN_NAME_TEXT));
                viewHolder.title.setText(title);
                viewHolder.text.setText(text);
            }
        }
    }
}
