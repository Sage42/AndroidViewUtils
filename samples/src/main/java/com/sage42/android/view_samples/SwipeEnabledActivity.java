package com.sage42.android.view_samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sage42.android.view.gestures.MyGestureDetector;
import com.sage42.android.view.gestures.MyGestureDetector.ISwipeCallbacks;
import com.sage42.android.view.gestures.MyGestureDetector.SwipeDirection;

public class SwipeEnabledActivity extends Activity implements ISwipeCallbacks
{
    // Gesture Detection
    private View                 mGestureOverlay;
    private GestureDetector      mGestureDetector;
    private View.OnTouchListener mGestureListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // wire up the layout
        this.setContentView(R.layout.swipe_enabled_activity);

        // Gesture detection
        this.mGestureOverlay = this.findViewById(R.id.gesture_overlay);
        this.mGestureDetector = new GestureDetector(this, new MyGestureDetector(this));
        this.mGestureListener = new View.OnTouchListener()
        {
            @SuppressWarnings("synthetic-access")
            @Override
            public boolean onTouch(final View v, final MotionEvent event)
            {
                return SwipeEnabledActivity.this.mGestureDetector.onTouchEvent(event);
            }
        };
        // wire gestures to the gesture detection view
        this.mGestureOverlay.setOnTouchListener(this.mGestureListener);
    }

    @Override
    public void onSwipe(final SwipeDirection direction)
    {
        switch (direction)
        {
            case LEFT:
                Toast.makeText(this, R.string.swipe_left, Toast.LENGTH_SHORT).show();
                break;

            case RIGHT:
                Toast.makeText(this, R.string.swipe_right, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

}
