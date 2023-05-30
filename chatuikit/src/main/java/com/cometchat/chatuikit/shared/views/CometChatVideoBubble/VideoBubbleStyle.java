package com.cometchat.chatuikit.shared.views.CometChatVideoBubble;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Represents a style configuration for the VideoBubble component.
 * <p>
 * The VideoBubbleStyle defines the visual attributes of the video bubble, such as play icon tint color and background color.
 */
public class VideoBubbleStyle extends BaseStyle {
    private final String TAG = VideoBubbleStyle.class.getName();

    private @ColorInt
    int playIconTint;
    private @ColorInt
    int playIconBackgroundColor;

    @Override
    public VideoBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public VideoBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public VideoBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public VideoBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public VideoBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public VideoBubbleStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the tint color for the play icon in the VideoBubbleStyle.
     *
     * @param playIconTint The tint color for the play icon.
     * @return The updated VideoBubbleStyle object.
     */
    public VideoBubbleStyle setPlayIconTint(int playIconTint) {
        this.playIconTint = playIconTint;
        return this;
    }

    /**
     * Sets the background color for the play icon in the VideoBubbleStyle.
     *
     * @param playIconBackgroundColor The background color for the play icon.
     * @return The updated VideoBubbleStyle object.
     */
    public VideoBubbleStyle setPlayIconBackgroundColor(int playIconBackgroundColor) {
        this.playIconBackgroundColor = playIconBackgroundColor;
        return this;
    }

    public int getPlayIconTint() {
        return playIconTint;
    }

    public int getPlayIconBackgroundColor() {
        return playIconBackgroundColor;
    }
}
