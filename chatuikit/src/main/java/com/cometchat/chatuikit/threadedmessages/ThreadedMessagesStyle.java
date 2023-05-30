package com.cometchat.chatuikit.threadedmessages;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Style class for customizing the appearance of ThreadedMessages.
 */
public class ThreadedMessagesStyle extends BaseStyle {

    private final String TAG = ThreadedMessagesStyle.class.getName();
    private String titleFont;
    private String replyCountFont;
    private @StyleRes int titleAppearance;
    private @StyleRes int replyCountAppearance;
    private @ColorInt int titleColor;
    private @ColorInt int replyCountColor;
    private @ColorInt int closeIconTint;
    private @ColorInt int separatorColor;

    /**
     * Sets the font for the title in ThreadedMessages.
     *
     * @param titleFont The font to set for the title.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the font for the reply count in ThreadedMessages.
     *
     * @param replyCountFont The font to set for the reply count.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setReplyCountFont(String replyCountFont) {
        this.replyCountFont = replyCountFont;
        return this;
    }

    /**
     * Sets the text appearance for the title in ThreadedMessages.
     *
     * @param titleAppearance The text appearance to set for the title.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the text appearance for the reply count in ThreadedMessages.
     *
     * @param replyCountAppearance The text appearance to set for the reply count.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setReplyCountAppearance(int replyCountAppearance) {
        this.replyCountAppearance = replyCountAppearance;
        return this;
    }

    /**
     * Sets the color for the title in ThreadedMessages.
     *
     * @param titleColor The color to set for the title.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the color for the reply count in ThreadedMessages.
     *
     * @param replyCountColor The color to set for the reply count.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setReplyCountColor(int replyCountColor) {
        this.replyCountColor = replyCountColor;
        return this;
    }

    /**
     * Sets the tint color for the close icon in ThreadedMessages.
     *
     * @param closeIconTint The tint color to set for the close icon.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setCloseIconTint(int closeIconTint) {
        this.closeIconTint = closeIconTint;
        return this;
    }

    /**
     * Sets the color for the separator in ThreadedMessages.
     *
     * @param separatorColor The color to set for the separator.
     * @return The updated ThreadedMessagesStyle object.
     */
    public ThreadedMessagesStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    @Override
    public ThreadedMessagesStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public ThreadedMessagesStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public ThreadedMessagesStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public ThreadedMessagesStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public ThreadedMessagesStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public ThreadedMessagesStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public String getReplyCountFont() {
        return replyCountFont;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getReplyCountAppearance() {
        return replyCountAppearance;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getReplyCountColor() {
        return replyCountColor;
    }

    public int getCloseIconTint() {
        return closeIconTint;
    }

    public int getSeparatorColor() {
        return separatorColor;
    }
}
