package com.sage42.android.view.pager;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Original Code: http://stackoverflow.com/questions/12737222/viewpager-and-database
 */
public abstract class CursorFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    protected Cursor mCursor;

    public CursorFragmentPagerAdapter(final FragmentManager fragmentManager, final Cursor cursor)
    {
        super(fragmentManager);
        this.mCursor = cursor;
    }

    @Override
    public Fragment getItem(final int position)
    {
        if (this.mCursor == null)
        {
            return null;
        }

        this.mCursor.moveToPosition(position);
        return this.getItem(this.mCursor);
    }

    public abstract Fragment getItem(final Cursor cursor);

    @Override
    public int getCount()
    {
        if (this.mCursor == null)
        {
            return 0;
        }
        return this.mCursor.getCount();
    }

    public void swapCursor(final Cursor cursor)
    {
        if (this.mCursor == cursor)
        {
            return;
        }

        this.mCursor = cursor;
        this.notifyDataSetChanged();
    }

    public Cursor getCursor()
    {
        return this.mCursor;
    }

}
