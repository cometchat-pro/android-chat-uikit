package com.cometchat.chatuikit.messageheader;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * MessageHeaderStyle is a style class that defines the appearance and customization options for a message header.
 * It extends the BaseStyle class and provides methods to set various style attributes such as typing indicator text color,
 * typing indicator text appearance, subtitle text color, subtitle text appearance, back icon tint, and online status color.
 */
public class MessageHeaderStyle extends BaseStyle {

    public final String TAG = MessageHeaderStyle.class.getName();

    private @ColorInt
    int typingIndicatorTextColor;
    private @StyleRes
    int typingIndicatorTextAppearance;
    private String typingIndicatorTextFont;

    private @ColorInt
    int subtitleTextColor;
    private @StyleRes
    int subtitleTextAppearance;
    private String subtitleTextFont;

    private @ColorInt
    int backIconTint;
    private @ColorInt
    int onlineStatusColor;

    @Override
    public MessageHeaderStyle setBackground(@ColorInt int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessageHeaderStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public MessageHeaderStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public MessageHeaderStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessageHeaderStyle setBorderColor(@ColorInt int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessageHeaderStyle setActiveBackground(@ColorInt int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the text color for the typing indicator in the message header.
     *
     * @param typingIndicatorTextColor The color for the typing indicator text.
     */
    public MessageHeaderStyle setTypingIndicatorTextColor(@ColorInt int typingIndicatorTextColor) {
        this.typingIndicatorTextColor = typingIndicatorTextColor;
        return this;
    }

    /**
     * Sets the text appearance for the typing indicator in the message header.
     *
     * @param typingIndicatorTextAppearance The text appearance resource for the typing indicator text.
     */
    public MessageHeaderStyle setTypingIndicatorTextAppearance(@StyleRes int typingIndicatorTextAppearance) {
        this.typingIndicatorTextAppearance = typingIndicatorTextAppearance;
        return this;
    }

    /**
     * Sets the text font for the typing indicator in the message header.
     *
     * @param typingIndicatorTextFont The font for the typing indicator text.
     */
    public MessageHeaderStyle setTypingIndicatorTextFont(String typingIndicatorTextFont) {
        this.typingIndicatorTextFont = typingIndicatorTextFont;
        return this;
    }

    /**
     * Sets the text color for the subtitle in the message header.
     *
     * @param subtitleTextColor The color for the subtitle text.
     */
    public MessageHeaderStyle setSubtitleTextColor(@ColorInt int subtitleTextColor) {
        this.subtitleTextColor = subtitleTextColor;
        return this;
    }

    /**
     * Sets the text appearance for the subtitle in the message header.
     *
     * @param subtitleTextAppearance The text appearance resource for the subtitle text.
     */
    public MessageHeaderStyle setSubtitleTextAppearance(@StyleRes int subtitleTextAppearance) {
        this.subtitleTextAppearance = subtitleTextAppearance;
        return this;
    }

    /**
     * Sets the text font for the subtitle in the message header.
     *
     * @param subtitleTextFont The font for the subtitle text.
     */
    public MessageHeaderStyle setSubtitleTextFont(String subtitleTextFont) {
        this.subtitleTextFont = subtitleTextFont;
        return this;
    }

    /**
     * Sets the tint color for the back icon in the message header.
     *
     * @param backIconTint The tint color for the back icon.
     */
    public MessageHeaderStyle setBackIconTint(@ColorInt int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    /**
     * Sets the color for the online status indicator in the message header.
     *
     * @param onlineStatusColor The color for the online status indicator.
     */
    public MessageHeaderStyle setOnlineStatusColor(@ColorInt int onlineStatusColor) {
        this.onlineStatusColor = onlineStatusColor;
        return this;
    }

    public int getTypingIndicatorTextColor() {
        return typingIndicatorTextColor;
    }

    public int getTypingIndicatorTextAppearance() {
        return typingIndicatorTextAppearance;
    }

    public String getTypingIndicatorTextFont() {
        return typingIndicatorTextFont;
    }

    public int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public int getSubtitleTextAppearance() {
        return subtitleTextAppearance;
    }

    public String getSubtitleTextFont() {
        return subtitleTextFont;
    }

    public int getBackIconTint() {
        return backIconTint;
    }

    public int getOnlineStatusColor() {
        return onlineStatusColor;
    }
}
