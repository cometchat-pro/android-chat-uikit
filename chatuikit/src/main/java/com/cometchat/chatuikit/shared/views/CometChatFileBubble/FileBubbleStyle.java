package com.cometchat.chatuikit.shared.views.CometChatFileBubble;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The FileBubbleStyle class represents the style for the CometChatFileBubble class.
 * It provides methods to set various styling attributes such as title and subtitle text color, font, appearance, download icon tint, and background.
 * It also extends the BaseStyle class and inherits its methods for setting background, corner radius, border width, and border color.
 */
public class FileBubbleStyle extends BaseStyle {

    private String TAG = FileBubbleStyle.class.getName();

    private String titleTextFont;
    private String subtitleTextFont;

    private @StyleRes
    int titleTextAppearance;
    private @StyleRes
    int subtitleTextAppearance;

    private @ColorInt
    int downloadIconTint;
    private @ColorInt
    int subtitleTextColor;
    private @ColorInt
    int titleTextColor;

    /**
     * Sets the text color for the title of the file bubble.
     *
     * @param titleTextColor The color to set for the title text.
     */
    public FileBubbleStyle setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }
    /**
     * Sets the font for the title of the file bubble.
     *
     * @param titleTextFont The font to set for the title text.
     */
    public FileBubbleStyle setTitleTextFont(String titleTextFont) {
        this.titleTextFont = titleTextFont;
        return this;
    }

    /**
     * Sets the text appearance for the title of the file bubble.
     *
     * @param titleTextAppearance The text appearance resource ID to set for the title.
     */
    public FileBubbleStyle setTitleTextAppearance(int titleTextAppearance) {
        this.titleTextAppearance = titleTextAppearance;
        return this;
    }

    /**
     * Sets the text color for the subtitle of the file bubble.
     *
     * @param subtitleTextColor The color to set for the subtitle text.
     */
    public FileBubbleStyle setSubtitleTextColor(int subtitleTextColor) {
        this.subtitleTextColor = subtitleTextColor;
        return this;
    }

    /**
     * Sets the font for the subtitle of the file bubble.
     *
     * @param subtitleTextFont The font to set for the subtitle text.
     */
    public FileBubbleStyle setSubtitleTextFont(String subtitleTextFont) {
        this.subtitleTextFont = subtitleTextFont;
        return this;
    }

    /**
     * Sets the text appearance for the subtitle of the file bubble.
     *
     * @param subtitleTextAppearance The text appearance resource ID to set for the subtitle.
     */
    public FileBubbleStyle setSubtitleTextAppearance(int subtitleTextAppearance) {
        this.subtitleTextAppearance = subtitleTextAppearance;
        return this;
    }

    /**
     * Sets the tint color for the download icon of the file bubble.
     *
     * @param downloadIconTint The color to set as the tint for the download icon.
     */
    public FileBubbleStyle setDownloadIconTint(int downloadIconTint) {
        this.downloadIconTint = downloadIconTint;
        return this;
    }

    @Override
    public FileBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public FileBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public FileBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public FileBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public FileBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public FileBubbleStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
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

    public int getDownloadIconTint() {
        return downloadIconTint;
    }

    public int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }
}
