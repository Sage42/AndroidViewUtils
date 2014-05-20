package com.sage42.android.view_samples.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sage42.android.view.animations.ClickToOpenCloseLayout;
import com.sage42.android.view.ui.ExpandAndShrinkCardView;
import com.sage42.android.view.ui.ExpandAndShrinkCardsListViewAdapter;
import com.sage42.android.view_samples.R;

public class SampleExpandShrinkListAdapter extends ExpandAndShrinkCardsListViewAdapter
{

    public SampleExpandShrinkListAdapter(final Context context, final Cursor cursor)
    {
        super(context, cursor);
    }

    static class ViewHolder
    {
        public TextView title;
        public TextView text;
    }

    @Override
    public SampleExpandAndShrinkCard newExpandAndShrinkView(final Context context, final Cursor cursor,
                    final ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(this.mContext);
        final SampleExpandAndShrinkCard rootView = (SampleExpandAndShrinkCard) inflater.inflate(
                        R.layout.sample_expand_shrink_card_view, parent, false);

        // Setup ViewHolder
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) rootView.findViewById(R.id.card_title);
        viewHolder.text = (TextView) rootView.findViewById(R.id.card_text);
        rootView.setTag(viewHolder);

        return rootView;
    }

    @Override
    public void bindExpandAndShrinkView(final View view, final Context arg1, final Cursor cursor)
    {

        if (view instanceof SampleExpandAndShrinkCard)
        {
            final SampleExpandAndShrinkCard sampleCard = (SampleExpandAndShrinkCard) view;
            sampleCard.bind(cursor);
        }
        return;

    }

    @Override
    public ClickToOpenCloseLayout setClickToOpenCloseLayout(final View view)
    {

        // return ClickToOpenCloseLayout from your layout by using findViewById
        return ((ClickToOpenCloseLayout) view.findViewById(R.id.main_container));
    }

    @Override
    public ExpandAndShrinkCardView setExpandAndSrinkView(final View view)
    {

        // return ExpandAndShrinkCardView from your layout by using findViewById
        return ((ExpandAndShrinkCardView) view.findViewById(R.id.root_view));
    }
}
