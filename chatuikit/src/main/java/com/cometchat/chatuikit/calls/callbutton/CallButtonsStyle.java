package com.cometchat.chatuikit.calls.callbutton;

import android.graphics.drawable.Drawable;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The style configuration for the call buttons.
 * It extends the BaseStyle class, which provides basic styling options.
 * Use this class to customize the appearance of the call buttons.
 */
public class CallButtonsStyle extends BaseStyle {

    @Override
    public CallButtonsStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CallButtonsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public CallButtonsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public CallButtonsStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CallButtonsStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CallButtonsStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

}
