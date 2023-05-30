package com.cometchat.chatuikit.shared.views.CometChatImageBubble;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * A style class for customizing the appearance of an ImageBubble.
 * <p>
 * The ImageBubbleStyle class extends the BaseStyle class and provides additional options for styling the image bubble.
 */
public class ImageBubbleStyle extends BaseStyle {
    private final String TAG = ImageBubbleStyle.class.getName();

    private @ColorInt
    int textColor;
    private String textFont;
    private @StyleRes
    int textAppearance;

    @Override
    public ImageBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public ImageBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public ImageBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public ImageBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public ImageBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public ImageBubbleStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the text color for the caption in the image bubble.
     *
     * @param textColor The text color resource or color value to be set.
     */
    public ImageBubbleStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Sets the font for the caption text in the image bubble.
     *
     * @param textFont The font name or path to be set.
     */
    public ImageBubbleStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Sets the text appearance for the caption in the image bubble.
     *
     * @param textAppearance The text appearance style resource to be set.
     */
    public ImageBubbleStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
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
}
