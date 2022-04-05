package com.cometchatworkspace.components.shared.primaryComponents;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class CometChatPalette {

    private int light;
    private int dark;

    public CometChatPalette(@ColorInt int light, @ColorInt int dark) {
        this.light = light;
        this.dark = dark;
    }

    public @ColorInt int getLight() {
        return light;
    }

    public @ColorInt int getDark() {
        return dark;
    }
}
