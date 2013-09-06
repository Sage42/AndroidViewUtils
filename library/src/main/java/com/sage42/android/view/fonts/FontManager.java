package com.sage42.android.view.fonts;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
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
 */
/**
 * Reference: http://sriramramani.wordpress.com/2012/11/29/custom-fonts/
 */
public class FontManager
{
    public static final String      FONT_NAME_ROBOTO_THIN           = "Roboto-Thin";                    //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_LIGHT          = "Roboto-Light";                   //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_REGULAR        = "Roboto-Regular";                 //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_MEDIUM         = "Roboto-Medium";                  //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_BOLD           = "Roboto-Bold";                    //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_BLACK          = "Roboto-Black";                   //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_CONDENSED      = "Roboto-Condensed";               //$NON-NLS-1$
    public static final String      FONT_NAME_ROBOTO_BOLD_CONDENSED = "Roboto-Bold-Condensed";          //$NON-NLS-1$

    private static final String     TAG                             = FontManager.class.getSimpleName();

    private final Map<String, Font> mFonts;

    //Making FontManager a singleton class
    private static class InstanceHolder
    {
        private static final FontManager INSTANCE = new FontManager();
    }

    @SuppressWarnings("synthetic-access")
    public static FontManager getInstance()
    {
        return FontManager.InstanceHolder.INSTANCE;
    }

    protected FontManager()
    {
        // enforce singleton
        super();

        this.mFonts = new HashMap<String, Font>();
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
        if (!this.mFonts.containsKey(fontFamily))
        {
            // attempt to load font JIT at runtime
            this.loadFont(context, fontFamily);

            // return null, if load failed
            if (!this.mFonts.containsKey(fontFamily))
            {
                Log.d(FontManager.TAG, "Failed to load: " + fontFamily); //$NON-NLS-1$
                return null;
            }
        }

        // default to normal style if not was supplied
        final int style = (inStyle >= 0) ? inStyle : Typeface.NORMAL;

        final Font font = this.mFonts.get(fontFamily);
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
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_LIGHT))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_LIGHT, R.raw.roboto_light,
                            R.raw.roboto_light_italic));
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_REGULAR))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_REGULAR,
                            R.raw.roboto_regular, R.raw.roboto_regular_italic));
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_MEDIUM))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_MEDIUM, R.raw.roboto_medium,
                            R.raw.roboto_medium_italic));
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_BOLD))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_BOLD, R.raw.roboto_bold,
                            R.raw.roboto_bold_italic));
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_BLACK))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_BLACK, R.raw.roboto_black,
                            R.raw.roboto_black_italic));
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_CONDENSED))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_CONDENSED,
                            R.raw.roboto_condensed, R.raw.roboto_condensed_italic));
            return;
        }
        if (fontFamily.equals(FontManager.FONT_NAME_ROBOTO_BOLD_CONDENSED))
        {
            this.mFonts.put(fontFamily, this.addFont(context, FontManager.FONT_NAME_ROBOTO_BOLD_CONDENSED,
                            R.raw.roboto_bold_condensed, R.raw.roboto_bold_condensed_italic));
        }

        Log.d(FontManager.TAG, "Failed to load font, unknown fontFamily: " + fontFamily); //$NON-NLS-1$
    }

    /**
     * Extend this class and override this method for custom error handling
     * @param e 
     */
    public void reportError(final Exception e)
    {
        Log.e(FontManager.TAG, e.getMessage(), e);
    }

    private Typeface getFontFromRes(final Context context, final int resource)
    {
        Typeface tf = null;
        InputStream is = null;
        try
        {
            is = context.getResources().openRawResource(resource);
            if (is == null)
            {
                return null;
            }

            final String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw"; //$NON-NLS-1$ //$NON-NLS-2$

            final byte[] buffer = new byte[is.available()];
            final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

            int l = 0;
            while ((l = is.read(buffer)) > 0)
            {
                bos.write(buffer, 0, l);
            }

            bos.close();

            tf = Typeface.createFromFile(outPath);

            // clean up
            new File(outPath).delete();
        }
        catch (final NotFoundException e)
        {
            this.reportError(e);
            return null;
        }
        catch (final IOException e)
        {
            this.reportError(e);
            return null;
        }
        finally
        {
            // clean up
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (final IOException e)
                {
                    this.reportError(e);
                    return null;
                }
            }
        }

        return tf;
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

}