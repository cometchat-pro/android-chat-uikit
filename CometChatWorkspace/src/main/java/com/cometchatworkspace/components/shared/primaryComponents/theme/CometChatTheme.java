package com.cometchatworkspace.components.shared.primaryComponents.theme;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import com.cometchatworkspace.R;

public class CometChatTheme {

    @StringDef({MODE.LIGHT, MODE.DARK})
    public @interface MODE {
        String LIGHT = "light";
        String DARK = "dark";
    }

    Palette palette;
    Typography typography;

//    public CometChatTheme(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            PaletteUtils.defaultPalette(context, MODE.LIGHT);
//        }
//    }

    public CometChatTheme(Context context, @MODE String mode) {
//        if (mode.equalsIgnoreCase(MODE.LIGHT)) {
//            PaletteUtils.lightPalette(context);
//        } else {
//            PaletteUtils.darkPalette(context);
//        }
    }

    public CometChatTheme(Context context, Palette palette,Typography typography) {
        this.palette = palette;
        this.typography = typography;
    }

    public Palette getPalette() {
        return palette;
    }

    public Typography getTypography() {
        return typography;
    }
}
