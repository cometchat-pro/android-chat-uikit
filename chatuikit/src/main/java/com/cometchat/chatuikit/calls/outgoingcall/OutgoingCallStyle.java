package com.cometchat.chatuikit.calls.outgoingcall;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.views.card.CardStyle;

/**
 * OutgoingCallStyle is a class that represents the style for the outgoing call view.
 * It extends the CardStyle class and provides additional customization options for the outgoing call view.
 */
public class OutgoingCallStyle extends CardStyle {

    private @ColorInt
    int subTitleColor;

    private @StyleRes
    int subTitleAppearance;

    @Override
    public OutgoingCallStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public OutgoingCallStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public OutgoingCallStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public OutgoingCallStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public OutgoingCallStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public OutgoingCallStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    @Override
    public OutgoingCallStyle setTitleFont(String titleFont) {
        super.setTitleFont(titleFont);
        return this;
    }

    @Override
    public OutgoingCallStyle setTitleColor(int titleColor) {
        super.setTitleColor(titleColor);
        return this;
    }

    @Override
    public OutgoingCallStyle setTitleAppearance(int titleAppearance) {
        super.setTitleAppearance(titleAppearance);
        return this;
    }

    public OutgoingCallStyle setSubTitleColor(int subTitleColor) {
        this.subTitleColor = subTitleColor;
        return this;
    }

    public OutgoingCallStyle setSubTitleAppearance(int subTitleAppearance) {
        this.subTitleAppearance = subTitleAppearance;
        return this;
    }

    public int getSubTitleColor() {
        return subTitleColor;
    }

    public int getSubTitleAppearance() {
        return subTitleAppearance;
    }
}
