package com.sage42.android.view_samples.ui;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.sage42.android.view_samples.R;

public class ExpandShrinkListViewActivity extends ListActivity
{
    public static final String COLUMN_NAME_TITLE = "title"; //$NON-NLS-1$
    public static final String COLUMN_NAME_TEXT  = "text"; //$NON-NLS-1$

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable the back btn on newer phones
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            this.enableUpButton();
        }

        // Setup adapter
        final SampleExpandShrinkListAdapter adapter = new SampleExpandShrinkListAdapter(this, this.getHardCodedCursor());
        this.setListAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void enableUpButton()
    {
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onListItemClick(final ListView listView, final View view, final int position, final long id)
    {
        if (view instanceof SampleExpandAndShrinkCard)
        {
            final SampleExpandAndShrinkCard card = (SampleExpandAndShrinkCard) view;
            card.toggleDisplay();
        }
    }

    private Cursor getHardCodedCursor()
    {
        final String[] columns = new String[]
        { "_id", ExpandShrinkListViewActivity.COLUMN_NAME_TITLE, ExpandShrinkListViewActivity.COLUMN_NAME_TEXT }; //$NON-NLS-1$ 

        final MatrixCursor matrixCursor = new MatrixCursor(columns);
        this.startManagingCursor(matrixCursor);

        // Get String resources
        final String[] titles = this.getResources().getStringArray(R.array.expand_shrink_list_view_title);
        final String[] texts = this.getResources().getStringArray(R.array.expand_shrink_list_view_text);

        if (titles.length == texts.length)
        {
            for (int i = 0; i < titles.length; i++)
            {
                matrixCursor.addRow(new Object[]
                { i, titles[i], texts[i] });
            }
        }

        return matrixCursor;
    }
}
