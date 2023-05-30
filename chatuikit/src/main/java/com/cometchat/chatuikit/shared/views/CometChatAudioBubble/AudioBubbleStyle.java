package com.cometchat.chatuikit.shared.views.CometChatAudioBubble;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;
/**
 * AudioBubbleStyle is a class that represents the style of an audio bubble view. It extends BaseStyle, which provides basic styling properties.
 */
public class AudioBubbleStyle extends BaseStyle {

    private String TAG = AudioBubbleStyle.class.getName();

    private String titleTextFont;
    private String subtitleTextFont;

    private @StyleRes
    int titleTextAppearance;
    private @StyleRes
    int subtitleTextAppearance;

    private @ColorInt
    int playIconTint;
    private @ColorInt
    int pauseIconTint;
    private @ColorInt
    int subtitleTextColor;
    private @ColorInt
    int titleTextColor;

    /**
     * Sets the background color or drawable for the badge.
     *
     * @param background The color or drawable to set as the background for the badge.
     */
    @Override
    public AudioBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    /**
     * Sets the drawable background for the badge.
     *
     * @param drawableBackground The drawable to set as the background for the badge.
     */
    @Override
    public AudioBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    /**
     * Sets the corner radius for the badge.
     *
     * @param cornerRadius The corner radius to set for the badge.
     */
    @Override
    public AudioBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    /**
     * Sets the border width for the badge.
     *
     * @param borderWidth The border width to set for the badge.
     */
    @Override
    public AudioBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    /**
     * Sets the border color for the badge.
     *
     * @param borderColor The color to set as the border color for the badge.
     */
    @Override
    public AudioBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public AudioBubbleStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the font to use for the title text.
     *
     * @param titleTextFont the font to use for the title text
     */
    public AudioBubbleStyle setTitleTextFont(String titleTextFont) {
        this.titleTextFont = titleTextFont;
        return this;
    }

    /**
     * Sets the font to use for the subtitle text.
     *
     * @param subtitleTextFont the font to use for the subtitle text
     */
    public AudioBubbleStyle setSubtitleTextFont(String subtitleTextFont) {
        this.subtitleTextFont = subtitleTextFont;
        return this;
    }

    /**
     * Sets the appearance to apply to the title text.
     *
     * @param titleTextAppearance the appearance to apply to the title text
     */
    public AudioBubbleStyle setTitleTextAppearance(int titleTextAppearance) {
        this.titleTextAppearance = titleTextAppearance;
        return this;
    }

    /**
     * Sets the appearance to apply to the subtitle text.
     *
     * @param subtitleTextAppearance the appearance to apply to the subtitle text
     */
    public AudioBubbleStyle setSubtitleTextAppearance(int subtitleTextAppearance) {
        this.subtitleTextAppearance = subtitleTextAppearance;
        return this;
    }

    /**
     * Sets the tint color to apply to the play icon.
     *
     * @param playIconTint the tint color to apply to the play icon
     */
    public AudioBubbleStyle setPlayIconTint(int playIconTint) {
        this.playIconTint = playIconTint;
        return this;
    }

    /**
     * Sets the tint color to apply to the pause icon.
     *
     * @param pauseIconTint the tint color to apply to the pause icon
     */
    public AudioBubbleStyle setPauseIconTint(int pauseIconTint) {
        this.pauseIconTint = pauseIconTint;
        return this;
    }

    /**
     * Sets the color of the subtitle text in this AudioBubbleStyle.
     * @param subtitleTextColor The color to set the subtitle text to.
     */
    public AudioBubbleStyle setSubtitleTextColor(int subtitleTextColor) {
        this.subtitleTextColor = subtitleTextColor;
        return this;
    }

    /**
     * Sets the color of the title text in this AudioBubbleStyle.
     * @param titleTextColor The color to set the title text to.
     */
    public AudioBubbleStyle setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public String getTitleTextFont() {
        return titleTextFont;
    }

    public String getSubtitleTextFont() {
        return subtitleTextFont;
    }

    public int getTitleTextAppearance() {
        return titleTextAppearance;
    }

    public int getSubtitleTextAppearance() {
        return subtitleTextAppearance;
    }

    public int getPlayIconTint() {
        return playIconTint;
    }

    public int getPauseIconTint() {
        return pauseIconTint;
    }

    public int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }
}
