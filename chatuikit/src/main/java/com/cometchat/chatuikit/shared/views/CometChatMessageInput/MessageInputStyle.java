package com.cometchat.chatuikit.shared.views.CometChatMessageInput;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * MessageInputStyle is a class that defines the style properties for the message input component.
 * <p>
 * It extends the BaseStyle class to inherit common style properties.
 */
public class MessageInputStyle extends BaseStyle {

    private final String TAG = MessageInputStyle.class.getName();

    private @ColorInt
    int inputBackground;
    private @ColorInt
    int dividerTint;
    private @ColorInt
    int textColor;
    private @ColorInt
    int placeHolderColor;
    private @StyleRes
    int inputTextAppearance;
    private String textFont;

    @Override
    public MessageInputStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessageInputStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public MessageInputStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public MessageInputStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessageInputStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessageInputStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the input background color of the message input.
     *
     * @param inputBackground The input background color to set.
     */
    public MessageInputStyle setInputBackground(int inputBackground) {
        this.inputBackground = inputBackground;
        return this;
    }

    /**
     * Sets the separator tint color of the message input.
     *
     * @param dividerTint The separator tint color to set.
     */
    public MessageInputStyle setSeparatorTint(int dividerTint) {
        this.dividerTint = dividerTint;
        return this;
    }

    /**
     * Sets the text color of the message input.
     *
     * @param textColor The text color to set.
     */
    public MessageInputStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Sets the placeholder text color of the message input.
     *
     * @param placeHolderColor The placeholder text color to set.
     */
    public MessageInputStyle setPlaceHolderColor(int placeHolderColor) {
        this.placeHolderColor = placeHolderColor;
        return this;
    }

    /**
     * Sets the text appearance of the message input.
     *
     * @param inputTextAppearance The text appearance to set.
     */
    public MessageInputStyle setInputTextAppearance(int inputTextAppearance) {
        this.inputTextAppearance = inputTextAppearance;
        return this;
    }

    /**
     * Sets the text font of the message input.
     *
     * @param textFont The text font to set.
     */
    public MessageInputStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    public int getInputBackground() {
        return inputBackground;
    }

    public int getSeparatorColor() {
        return dividerTint;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getPlaceHolderColor() {
        return placeHolderColor;
    }

    public int getInputTextAppearance() {
        return inputTextAppearance;
    }

    public String getTextFont() {
        return textFont;
    }
}
