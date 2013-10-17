package com.sage42.android.view.pager;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseIntArray;
import android.view.ViewGroup;

/**
 * Original Code: http://stackoverflow.com/questions/12737222/viewpager-and-database
 */
public abstract class CursorFragmentPagerAdapter extends FragmentPagerAdapter
{
    protected boolean                  mDataValid;
    protected Cursor                   mCursor;
    protected Context                  mContext;
    protected SparseIntArray           mItemPositions;
    protected HashMap<Object, Integer> mObjectMap;
    protected int                      mRowIDColumn;

    public CursorFragmentPagerAdapter(final Context context, final FragmentManager fragmentManager, final Cursor cursor)
    {
        super(fragmentManager);

        this.init(context, cursor);
    }

    @SuppressWarnings("null")
    void init(final Context context, final Cursor cursor)
    {
        this.mObjectMap = new HashMap<Object, Integer>();
        final boolean cursorPresent = cursor != null;
        this.mCursor = cursor;
        this.mDataValid = cursorPresent;
        this.mContext = context;
        this.mRowIDColumn = cursorPresent ? cursor.getColumnIndexOrThrow("_id") : -1; //$NON-NLS-1$
    }

    public Cursor getCursor()
    {
        return this.mCursor;
    }

    @Override
    public int getItemPosition(final Object object)
    {
        final Integer rowId = this.mObjectMap.get(object);
        if ((rowId != null) && (this.mItemPositions != null))
        {
            return this.mItemPositions.get(rowId, PagerAdapter.POSITION_NONE);
        }
        return PagerAdapter.POSITION_NONE;
    }

    public void setItemPositions()
    {
        this.mItemPositions = null;

        if (this.mDataValid)
        {
            final int count = this.mCursor.getCount();
            this.mItemPositions = new SparseIntArray(count);
            this.mCursor.moveToPosition(-1);
            while (this.mCursor.moveToNext())
            {
                final int rowId = this.mCursor.getInt(this.mRowIDColumn);
                final int cursorPos = this.mCursor.getPosition();
                this.mItemPositions.append(rowId, cursorPos);
            }
        }
    }

    @Override
    public Fragment getItem(final int position)
    {
        if (this.mDataValid)
        {
            this.mCursor.moveToPosition(position);
            return this.getItem(this.mContext, this.mCursor);
        }
        return null;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object)
    {
        this.mObjectMap.remove(object);
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
        if (!this.mDataValid)
        {
            throw new IllegalStateException("this should only be called when the cursor is valid"); //$NON-NLS-1$
        }
        if (!this.mCursor.moveToPosition(position))
        {
            throw new IllegalStateException("couldn't move cursor to position " + position); //$NON-NLS-1$
        }

        final int rowId = this.mCursor.getInt(this.mRowIDColumn);
        final Object obj = super.instantiateItem(container, position);
        this.mObjectMap.put(obj, Integer.valueOf(rowId));

        return obj;
    }

    public abstract Fragment getItem(Context context, Cursor cursor);

    @Override
    public int getCount()
    {
        if (this.mDataValid)
        {
            return this.mCursor.getCount();
        }
        return 0;
    }

    public void changeCursor(final Cursor cursor)
    {
        final Cursor old = this.swapCursor(cursor);
        if (old != null)
        {
            old.close();
        }
    }

    public Cursor swapCursor(final Cursor newCursor)
    {
        if (newCursor == this.mCursor)
        {
            return null;
        }
        final Cursor oldCursor = this.mCursor;
        this.mCursor = newCursor;
        if (newCursor != null)
        {
            this.mRowIDColumn = newCursor.getColumnIndexOrThrow("_id"); //$NON-NLS-1$
            this.mDataValid = true;
        }
        else
        {
            this.mRowIDColumn = -1;
            this.mDataValid = false;
        }

        this.setItemPositions();
        this.notifyDataSetChanged();

        return oldCursor;
    }

    @Override
    public long getItemId(final int position)
    {
        if (!this.mDataValid || !this.mCursor.moveToPosition(position))
        {
            return super.getItemId(position);
        }
        final int rowId = this.mCursor.getInt(this.mRowIDColumn);
        return rowId;
    }
}
