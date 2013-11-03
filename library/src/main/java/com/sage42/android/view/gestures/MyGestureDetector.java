package com.sage42.android.view.gestures;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.sage42.android.view.BuildConfig;

public class MyGestureDetector extends SimpleOnGestureListener
{
    private static final String   TAG                      = MyGestureDetector.class.getSimpleName();

    private static final int      SWIPE_MIN_DISTANCE       = 120;
    private static final int      SWIPE_MAX_OFF_PATH       = 250;
    private static final int      SWIPE_THRESHOLD_VELOCITY = 100;

    private final ISwipeCallbacks mCallback;

    public MyGestureDetector(final ISwipeCallbacks callback)
    {
        super();
        this.mCallback = callback;
    }

    @Override
    public boolean onFling(final MotionEvent event1, final MotionEvent event2, final float velocityX,
                    final float velocityY)
    {
        try
        {
            if (Math.abs(event1.getY() - event2.getY()) > MyGestureDetector.SWIPE_MAX_OFF_PATH)
            {
                return false;
            }
            if (((event1.getX() - event2.getX()) > MyGestureDetector.SWIPE_MIN_DISTANCE)
                            && (Math.abs(velocityX) > MyGestureDetector.SWIPE_THRESHOLD_VELOCITY))
            {
                // right to left swipe
                this.mCallback.onSwipe(SwipeDirection.LEFT);
            }
            else if (((event2.getX() - event1.getX()) > MyGestureDetector.SWIPE_MIN_DISTANCE)
                            && (Math.abs(velocityX) > MyGestureDetector.SWIPE_THRESHOLD_VELOCITY))
            {
                // left to right swipe
                this.mCallback.onSwipe(SwipeDirection.RIGHT);
            }
        }
        catch (final RuntimeException exception)
        {
            if (BuildConfig.DEBUG)
            {
                Log.e(MyGestureDetector.TAG, exception.getMessage(), exception);
            }
        }
        return false;
    }

    /**
     * This is needed so that we dont need to use an onClick listener in the activity.
     *  
     * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
     */
    @Override
    public boolean onDown(final MotionEvent event)
    {
        return true;
    }

    public interface ISwipeCallbacks
    {
        /**
         * Called on swipe, possible directions (LEFT, RIGHT)
         */
        void onSwipe(final SwipeDirection direction);
    }

    public enum SwipeDirection
    {
        LEFT, RIGHT;
    }

}
