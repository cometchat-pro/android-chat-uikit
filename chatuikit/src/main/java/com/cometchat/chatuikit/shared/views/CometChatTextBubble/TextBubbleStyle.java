package com.cometchat.chatuikit.shared.views.CometChatTextBubble;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Represents a style configuration for the CometChatTextBubble view.
 * <p>
 * It extends the BaseStyle class to inherit common styling properties.
 */
public class TextBubbleStyle extends BaseStyle {
    private final String TAG = TextBubbleStyle.class.getName();

    private @ColorInt
    int textColor;
    private @ColorInt
    int compoundDrawableIconTint;
    private String textFont;
    private @StyleRes
    int textAppearance;

    @Override
    public TextBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public TextBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public TextBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public TextBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public TextBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public TextBubbleStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the text color for the text bubble.
     *
     * @param textColor The text color value to be set.
     */
    public TextBubbleStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Sets the font for the text in the text bubble.
     *
     * @param textFont The font name to be applied.
     */
    public TextBubbleStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Sets the text appearance for the text bubble.
     *
     * @param textAppearance The style resource ID defining the text appearance.
     */
    public TextBubbleStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    /**
     * Sets the tint color for the compound drawables (icons) on the text bubble.
     *
     * @param compoundDrawableIconTint The color to be applied to the compound drawables.
     * @return The updated TextBubbleStyle object.
     */
    public TextBubbleStyle setCompoundDrawableIconTint(@ColorInt int compoundDrawableIconTint) {
        this.compoundDrawableIconTint = compoundDrawableIconTint;
        return this;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getTextFont() {
        return textFont;
    }

    public int getTextAppearance() {
        return textAppearance;
    }

    public int getCompoundDrawableIconTint() {
        return compoundDrawableIconTint;
    }
}
