package com.sage42.android.view.fonts;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.sage42.android.view.R;

/**
 * Copyright (C) 2013- Sage 42 App Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Corey Scott (corey.scott@sage42.com)
 *
 * Reference: http://sriramramani.wordpress.com/2012/11/29/custom-fonts/
 */
public class FontManager
{
    public static final String      FONT_NAME_ROBOTO_THIN           = "roboto-thin";                    //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_LIGHT          = "roboto-light";                   //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_REGULAR        = "roboto-regular";                 //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_MEDIUM         = "roboto-medium";                  //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_BOLD           = "roboto-bold";                    //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_BLACK          = "roboto-black";                   //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_CONDENSED      = "roboto-condensed";               //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_BOLD_CONDENSED = "roboto-bold-condensed";          //$NON-NLS-1$

    private static final String     TAG                             = FontManager.class.getSimpleName();

    private final Map<String, Font> mFonts;

    // singleton instance
    private static FontManager      mInstance;

    private FontManager()
    {
        // enforce singleton
        super();

        this.mFonts = new HashMap<String, Font>();
    }

    public static synchronized FontManager getInstance()
    {
        if (FontManager.mInstance == null)
        {
            FontManager.mInstance = new FontManager();
        }
        return FontManager.mInstance;
    }

    @SuppressWarnings("synthetic-access")
    private Font addFont(final Context context, final String fontName, final Integer normalResId,
                    final Integer italicResId)
    {
        final Font font = new Font();

        // a list of font-family names supported.
        font.families = new ArrayList<String>();
        font.families.add(fontName);

        // a list of files specifying the different styles.
        font.styles = new ArrayList<FontStyle>();

        if (normalResId != null)
        {
            final FontStyle fontStyle = new FontStyle();
            fontStyle.font = this.getFontFromRes(context, normalResId);
            fontStyle.style = Typeface.NORMAL;
            font.styles.add(fontStyle);
            Log.d(FontManager.TAG, "Loaded (Normal): " + fontName); //$NON-NLS-1$
        }

        if (italicResId != null)
        {
            final FontStyle fontStyle = new FontStyle();
            fontStyle.font = this.getFontFromRes(context, italicResId);
            fontStyle.style = Typeface.ITALIC;
            font.styles.add(fontStyle);
            Log.d(FontManager.TAG, "Loaded (Italic): " + fontName); //$NON-NLS-1$
        }

        return font;
    }

    public Typeface get(final Context context, final String fontFamily, final int inStyle)
    {
        final String fontFamilyLower = fontFamily.toLowerCase(Locale.US);

        if (!this.mFonts.containsKey(fontFamilyLower))
        {
            // attempt to load font JIT at runtime
            this.loadFont(context, fontFamilyLower);

            // return null, if load failed
            if (!this.mFonts.containsKey(fontFamilyLower))
            {
                Log.e(FontManager.TAG, "Failed to load: " + fontFamilyLower); //$NON-NLS-1$
                return null;
            }
        }

        // default to normal style if not was supplied
        final int style = (inStyle >= 0) ? inStyle : Typeface.NORMAL;

        final Font font = this.mFonts.get(fontFamilyLower);
        for (final FontStyle fontStyle : font.styles)
        {
            if (fontStyle.style == style)
            {
                return fontStyle.font;
            }
        }

        return null;
    }

    private void loadFont(final Context context, final String fontFamily)
    {
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_THIN))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_THIN, R.raw.roboto_thin,
                            R.raw.roboto_thin_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_LIGHT))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_LIGHT, R.raw.roboto_light,
                            R.raw.roboto_light_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_REGULAR))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_REGULAR,
                            R.raw.roboto_regular, R.raw.roboto_regular_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_MEDIUM))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_MEDIUM, R.raw.roboto_medium,
                            R.raw.roboto_medium_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_BOLD))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_BOLD, R.raw.roboto_bold,
                            R.raw.roboto_bold_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_BLACK))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_BLACK, R.raw.roboto_black,
                            R.raw.roboto_black_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_CONDENSED))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_CONDENSED,
                            R.raw.roboto_condensed, R.raw.roboto_condensed_italic));
        }
        else if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_BOLD_CONDENSED))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_BOLD_CONDENSED,
                            R.raw.roboto_bold_condensed, R.raw.roboto_bold_condensed_italic));
        }
        else
        {
            Log.e(FontManager.TAG, "Failed to load font, unknown fontFamily: " + fontFamily); //$NON-NLS-1$
        }
    }

    /**
     * Extend this class and override this method for custom error handling.
     * 
     * @param exception 
     */
    public void logError(final Exception exception)
    {
        Log.e(FontManager.TAG, exception.getMessage(), exception);
    }

    private Typeface getFontFromRes(final Context context, final int resource)
    {
        Typeface typeface = null;
        InputStream inputStream = null;
        try
        {
            inputStream = context.getResources().openRawResource(resource);
            if (inputStream == null)
            {
                return null;
            }

            final String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw"; //$NON-NLS-1$ //$NON-NLS-2$

            final byte[] buffer = new byte[inputStream.available()];
            final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

            int length = 0;
            while ((length = inputStream.read(buffer)) > 0)
            {
                bos.write(buffer, 0, length);
            }

            bos.close();

            typeface = Typeface.createFromFile(outPath);

            // clean up
            new File(outPath).delete();
        }
        catch (final NotFoundException exception)
        {
            this.logError(exception);
        }
        catch (final IOException exception)
        {
            this.logError(exception);
        }
        finally
        {
            // clean up
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (final IOException exception)
                {
                    this.logError(exception);
                }
            }
        }

        return typeface;
    }

    public void addCustomFont(final Context context, final String fontFamily, final Integer normalFontRes,
                    final Integer italicFontRes)
    {
        if (this.mFonts.containsKey(fontFamily))
        {
            // not need to load it again
            return;
        }

        this.mFonts.put(fontFamily, this.addFont(context, fontFamily, normalFontRes, italicFontRes));
    }

    public static Typeface extractTypeface(final Context context, final AttributeSet attrs)
    {
        // Fonts work as a combination of particular family and the style. 
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.fonts);
        final String family = args.getString(R.styleable.fonts_fontFamily);
        final int style = args.getInt(R.styleable.fonts_android_textStyle, -1);
        args.recycle();

        if (family == null)
        {
            return null;
        }
        // Set the typeface based on the family and the style combination.
        return FontManager.getInstance().get(context, family, style);
    }

    private static class FontStyle
    {
        int      style;
        Typeface font;
    }

    private static class Font
    {
        // different font-family names that this Font will respond to.
        List<String>    families;

        // different styles for this font.
        List<FontStyle> styles;
    }

}
