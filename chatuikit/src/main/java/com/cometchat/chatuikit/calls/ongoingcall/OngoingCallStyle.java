package com.cometchat.chatuikit.calls.ongoingcall;

import android.graphics.drawable.Drawable;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * OngoingCallStyle is a class that represents the style configuration for the CometChatOngoingCall view.
 * It extends the BaseStyle class, inheriting common style properties.
 */
public class OngoingCallStyle extends BaseStyle {

    @Override
    public OngoingCallStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public OngoingCallStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public OngoingCallStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public OngoingCallStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public OngoingCallStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public OngoingCallStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }
}
