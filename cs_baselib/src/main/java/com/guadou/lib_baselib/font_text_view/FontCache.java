package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by C02TVKSDHV27 on 02/08/2017.
 */

public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "SF-UI-Text/" + fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }

}
