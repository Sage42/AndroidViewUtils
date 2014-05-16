package com.sage42.android.view.ui;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.sage42.android.view.animations.ClickToOpenCloseLayout;
import com.sage42.android.view.animations.ClickToOpenCloseLayout.IClickToOpenCloseLayoutListener;

public abstract class ExpandAndShrinkCardsListViewAdapter extends CursorAdapter implements
                IClickToOpenCloseLayoutListener
{
    private final HashMap<Long, ExpandAndShrinkLayoutStates> mListItemsState;

    public ExpandAndShrinkCardsListViewAdapter(final Context context, final Cursor cursor)
    {
        super(context, cursor, true);
        this.mListItemsState = new HashMap<Long, ExpandAndShrinkLayoutStates>();
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.widget.CursorAdapter#bindView(android.view.View, android.content.Context,
     * android.database.Cursor)
     */
    @Override
    public final void bindView(final View view, final Context context, final Cursor cursor)
    {
        // Bind data first
        this.bindExpandAndShrinkView(view, context, cursor);
        
        //get the customview
        View customView = this.setExpandAndSrinkView(view);
        if (customView instanceof ExpandAndShrinkCardView)
        {
            final ExpandAndShrinkCardView expandAndShrinkView = (ExpandAndShrinkCardView) customView;
            // Prepare view for reuse
            final ClickToOpenCloseLayout parentContainer = expandAndShrinkView.getParentContainer();
            if (parentContainer != null)
            {               
                //expandAndShrinkView.setParentContainer(parentContainer);
                // Recalculate view properties
                parentContainer.setListener(this);
                parentContainer.onLayoutReuse(cursor.getPosition());
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor,
     * android.view.ViewGroup)
     */
    @Override
    public final View newView(final Context context, final Cursor cursor, final ViewGroup parent)
    {
        // Get a new View from implementing class
        final View newView = this.newExpandAndShrinkView(context, cursor, parent);
        
        //Get click to open close layout from the view
        final ClickToOpenCloseLayout clickToOpenCloseLayout = this.setClickToOpenCloseLayout(newView);
        
        //Get ExpandAndShrinkCardView layout from the view
        final ExpandAndShrinkCardView customView = this.setExpandAndSrinkView(newView);
        
        //Set parent layout
        customView.setParentContainer(clickToOpenCloseLayout);
        
        // Get unique id
        final long uniqueId = cursor.getPosition();

        // Save the current view state in memory
        this.mListItemsState.put(uniqueId, ExpandAndShrinkLayoutStates.INITIAL);
        customView.setUuid(uniqueId);
        final ClickToOpenCloseLayout parentContainer = customView.getParentContainer();
        if (parentContainer != null)
        {
            customView.setUuid(uniqueId);
            parentContainer.setListener(this);
        }

        return newView;
    }

    @Override
    public void onLayoutStateChange(final long index, final ExpandAndShrinkLayoutStates newState)
    {
        // Check if the layout already have an existing state, if no, set to initial
        final ExpandAndShrinkLayoutStates currentState = this.mListItemsState.get(index);

        if (currentState != null && newState == ExpandAndShrinkLayoutStates.INITIAL)
        {
            // State Restoration, ignore change
        }
        else
        {
            // Remember this state transition
            this.mListItemsState.put(index, newState);
        }
    }

    @Override
    public ExpandAndShrinkLayoutStates getLayoutState(final long index)
    {
        final ExpandAndShrinkLayoutStates currentState = this.mListItemsState.get(index);
        if (currentState == null)
        {
            return ExpandAndShrinkLayoutStates.INVALID;
        }

        return currentState;
    }

    public abstract View newExpandAndShrinkView(final Context context, final Cursor cursor, final ViewGroup parent);

    public abstract ExpandAndShrinkCardView setExpandAndSrinkView(View v);
    
    public abstract ClickToOpenCloseLayout setClickToOpenCloseLayout(View v);
    
    public abstract void bindExpandAndShrinkView(final View view, final Context context,
                    final Cursor cursor);

}
