package com.sage42.android.view.shadows;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowView;

import com.sage42.android.view.fonts.MyTextView;

/**
 * Shadow class to allow Robolectric to be able to "handle" the custom view component that matches this shadow class
 *
 */
@Implements(value = MyTextView.class)
public class MyTextViewShadow extends ShadowView
{

}
