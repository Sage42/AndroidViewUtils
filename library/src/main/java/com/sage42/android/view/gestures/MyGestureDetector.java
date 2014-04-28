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
            final boolean isXDirectionOffpath = Math.abs(event1.getX() - event2.getX()) > MyGestureDetector.SWIPE_MAX_OFF_PATH;
            final boolean isYDirectionOffpath = Math.abs(event1.getY() - event2.getY()) > MyGestureDetector.SWIPE_MAX_OFF_PATH;

            final boolean isXDirectionMinDistance = Math.abs(event1.getX() - event2.getX()) > MyGestureDetector.SWIPE_MIN_DISTANCE;
            final boolean isYDirectionMinDistance = Math.abs(event1.getY() - event2.getY()) > MyGestureDetector.SWIPE_MIN_DISTANCE;

            final boolean isXVelocityExceedThreshold = Math.abs(velocityX) > MyGestureDetector.SWIPE_THRESHOLD_VELOCITY;
            final boolean isYVelocityExceedThreshold = Math.abs(velocityY) > MyGestureDetector.SWIPE_THRESHOLD_VELOCITY;

            final boolean swipeInXDirection = false;
            final boolean swipeInYDirection = false;

            SwipeDirection swipeDirection = null;

            // Check whether we have a swipe in the X direction
            if (isXDirectionMinDistance && isXVelocityExceedThreshold && !isYDirectionOffpath)
            {
                if (event1.getX() > event2.getX())
                {
                    // right to left swipe
                    swipeDirection = SwipeDirection.LEFT;
                }
                else if (event2.getX() > event1.getX())
                {
                    // left to right swipe
                    swipeDirection = SwipeDirection.RIGHT;
                }
            }

            // Check whether we have a swipe in the Y direction
            if (isYDirectionMinDistance && isYVelocityExceedThreshold && !isXDirectionOffpath)
            {
                if (event1.getY() > event2.getY())
                {
                    // right to left swipe
                    swipeDirection = SwipeDirection.UP;
                }
                else if (event2.getY() > event1.getY())
                {
                    // left to right swipe
                    swipeDirection = SwipeDirection.DOWN;
                }
            }

            if (swipeDirection != null)
            {
                this.mCallback.onSwipe(swipeDirection);
            }

            return (swipeDirection == null) ? false : true;
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
        LEFT, RIGHT, UP, DOWN;
    }

}
