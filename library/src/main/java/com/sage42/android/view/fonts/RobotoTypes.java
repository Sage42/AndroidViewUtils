package com.sage42.android.view.fonts;

import java.util.Locale;

import com.sage42.android.view.R;

public enum RobotoTypes
{
    BLACK("roboto-black", R.raw.roboto_black, R.raw.roboto_black_italic), //$NON-NLS-1$
    BOLD("roboto-bold", R.raw.roboto_bold, R.raw.roboto_bold_italic), //$NON-NLS-1$
    BOLD_CONDENSED("roboto-bold-condensed", R.raw.roboto_bold_condensed, R.raw.roboto_bold_condensed_italic), //$NON-NLS-1$
    CONDENSED("roboto-condensed", R.raw.roboto_condensed, R.raw.roboto_condensed_italic), //$NON-NLS-1$
    CONDENSED_LIGHT("roboto-condensed-light", R.raw.roboto_condensed_light, R.raw.roboto_condensed_light_italic), //$NON-NLS-1$
    LIGHT("roboto-light", R.raw.roboto_light, R.raw.roboto_light_italic), //$NON-NLS-1$
    MEDIUM("roboto-medium", R.raw.roboto_medium, R.raw.roboto_medium_italic), //$NON-NLS-1$
    REGULAR("roboto-regular", R.raw.roboto_regular, R.raw.roboto_regular_italic), //$NON-NLS-1$
    THIN("roboto-thin", R.raw.roboto_thin, R.raw.roboto_thin_italic); //$NON-NLS-1$

    private final String mFontName;
    private int          mNormalResId;
    private int          mItalicResId;

    private RobotoTypes(final String fontName, final int normalResId, final int italicResId)
    {
        this.mFontName = fontName;
        this.mNormalResId = normalResId;
        this.mItalicResId = italicResId;
    }

    public static RobotoTypes getByFamilyName(final String familyName)
    {
        if ((familyName == null) || (familyName.length() == 0))
        {
            // sanity check
            return null;
        }

        final String familyNameLower = familyName.toLowerCase(Locale.US);
        for (final RobotoTypes thisFamily : RobotoTypes.values())
        {
            if (thisFamily.getFontName().toLowerCase(Locale.US).equals(familyNameLower))
            {
                return thisFamily;
            }
        }

        return null;
    }

    /**
     * @return the fontName
     */
    public String getFontName()
    {
        return this.mFontName;
    }

    /**
     * @return the normalResId
     */
    public int getNormalResId()
    {
        return this.mNormalResId;
    }

    /**
     * @return the italicResId
     */
    public int getItalicResId()
    {
        return this.mItalicResId;
    }
}
