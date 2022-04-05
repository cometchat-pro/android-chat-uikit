package com.cometchatworkspace.components.shared.primaryComponents.theme;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cometchatworkspace.R;

public class PaletteUtils {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void defaultPalette(Context context, @CometChatTheme.MODE String mode) {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT)) {
            lightPalette(context);
        } else {
            darkPalette(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void lightPalette(Context context) {
        Palette palette = Palette.getInstance(context);
        palette.background(context.getColor(R.color.background));
        palette.searchBackground(context.getColor(R.color.searchBackground));
        palette.primary(context.getColor(R.color.primary));
        palette.accent(context.getColor(R.color.accent));
        palette.accent50(context.getColor(R.color.accent50));
        palette.accent100(context.getColor(R.color.accent100));
        palette.accent200(context.getColor(R.color.accent200));
        palette.accent300(context.getColor(R.color.accent300));
        palette.accent400(context.getColor(R.color.accent400));
        palette.accent500(context.getColor(R.color.accent500));
        palette.accent600(context.getColor(R.color.accent600));
        palette.accent700(context.getColor(R.color.accent700));
        palette.accent800(context.getColor(R.color.accent800));
        palette.accent900(context.getColor(R.color.accent900));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void darkPalette(Context context) {
        Palette palette = Palette.getInstance(context);
        palette.background(context.getColor(R.color.background_dark));
        palette.primary(context.getColor(R.color.primary_dark));
        palette.accent(context.getColor(R.color.accent_dark));
        palette.accent50(context.getColor(R.color.accent50_dark));
        palette.accent100(context.getColor(R.color.accent100_dark));
        palette.accent200(context.getColor(R.color.accent200_dark));
        palette.accent300(context.getColor(R.color.accent300_dark));
        palette.accent400(context.getColor(R.color.accent400_dark));
        palette.accent500(context.getColor(R.color.accent500_dark));
        palette.accent600(context.getColor(R.color.accent600_dark));
        palette.accent700(context.getColor(R.color.accent700_dark));
    }
}
