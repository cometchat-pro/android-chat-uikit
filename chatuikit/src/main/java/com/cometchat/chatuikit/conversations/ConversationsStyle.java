package com.cometchat.chatuikit.conversations;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Represents the style configuration for Conversations.
 */
public class ConversationsStyle extends BaseStyle {

    private final String TAG = ConversationsStyle.class.getName();

    private String titleFont;
    private String emptyTextFont;
    private String lastMessageTextFont;
    private String typingIndicatorTextFont;
    private String threadIndicatorTextFont;

    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int emptyTextAppearance;
    private @StyleRes
    int errorTextAppearance;
    private @StyleRes
    int lastMessageTextAppearance;
    private @StyleRes
    int typingIndicatorTextAppearance;
    private @StyleRes
    int threadIndicatorTextAppearance;

    private @ColorInt
    int titleColor;
    private @ColorInt
    int backIconTint;
    private @ColorInt
    int onlineStatusColor;
    private @ColorInt
    int separatorColor;
    private @ColorInt
    int loadingIconTint;
    private @ColorInt
    int emptyTextColor;
    private @ColorInt
    int errorTextColor;
    private @ColorInt
    int lastMessageTextColor;
    private @ColorInt
    int typingIndicatorTextColor;
    private @ColorInt
    int threadIndicatorTextColor;

    /**
     * Sets the title appearance.
     *
     * @param titleAppearance The style resource for the title appearance.
     */
    public ConversationsStyle setTitleAppearance(@StyleRes int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the empty text appearance.
     *
     * @param emptyTextAppearance The style resource for the empty text appearance.
     */
    public ConversationsStyle setEmptyTextAppearance(@StyleRes int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;
    }

    /**
     * Sets the error text appearance.
     *
     * @param errorTextAppearance The style resource for the error text appearance.
     */
    public ConversationsStyle setErrorTextAppearance(@StyleRes int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;
    }

    /**
     * Sets the last message text appearance.
     *
     * @param lastMessageTextAppearance The style resource for the last message text appearance.
     */
    public ConversationsStyle setLastMessageTextAppearance(@StyleRes int lastMessageTextAppearance) {
        this.lastMessageTextAppearance = lastMessageTextAppearance;
        return this;
    }

    /**
     * Sets the typing indicator text appearance.
     *
     * @param typingIndicatorTextAppearance The style resource for the typing indicator text appearance.
     */
    public ConversationsStyle setTypingIndicatorTextAppearance(@StyleRes int typingIndicatorTextAppearance) {
        this.typingIndicatorTextAppearance = typingIndicatorTextAppearance;
        return this;
    }

    /**
     * Sets the thread indicator text appearance.
     *
     * @param threadIndicatorTextAppearance The style resource for the thread indicator text appearance.
     */
    public ConversationsStyle setThreadIndicatorTextAppearance(@StyleRes int threadIndicatorTextAppearance) {
        this.threadIndicatorTextAppearance = threadIndicatorTextAppearance;
        return this;
    }

    /**
     * Sets the title font.
     *
     * @param titleFont The font name for the title.
     */
    public ConversationsStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the title color.
     *
     * @param titleColor The color for the title.
     */
    public ConversationsStyle setTitleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the back icon tint.
     *
     * @param backIconTint The color for the back icon.
     */
    public ConversationsStyle setBackIconTint(@ColorInt int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    /**
     * Sets the online status color.
     *
     * @param onlineStatusColor The color for the online status.
     */
    public ConversationsStyle setOnlineStatusColor(@ColorInt int onlineStatusColor) {
        this.onlineStatusColor = onlineStatusColor;
        return this;
    }

    /**
     * Sets the separator color.
     *
     * @param separatorColor The color for the separator.
     */
    public ConversationsStyle setSeparatorColor(@ColorInt int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    /**
     * Sets the loading icon tint.
     *
     * @param loadingIconTint The color for the loading icon.
     */
    public ConversationsStyle setLoadingIconTint(@ColorInt int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }

    /**
     * Sets the text color for empty state text in conversations.
     *
     * @param emptyTextColor The color value for empty state text.
     */
    public ConversationsStyle setEmptyTextColor(@ColorInt int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }

    /**
     * Sets the font for empty state text in conversations.
     *
     * @param emptyTextFont The font name for empty state text.
     */
    public ConversationsStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }

    /**
     * Sets the text color for error state text in conversations.
     *
     * @param errorTextColor The color value for error state text.
     */
    public ConversationsStyle setErrorTextColor(@ColorInt int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }

    /**
     * Sets the text color for last message in conversations.
     *
     * @param lastMessageTextColor The color value for last message text.
     */
    public ConversationsStyle setLastMessageTextColor(@ColorInt int lastMessageTextColor) {
        this.lastMessageTextColor = lastMessageTextColor;
        return this;
    }

    /**
     * Sets the font for last message in conversations.
     *
     * @param lastMessageTextFont The font name for last message text.
     */
    public ConversationsStyle setLastMessageTextFont(String lastMessageTextFont) {
        this.lastMessageTextFont = lastMessageTextFont;
        return this;
    }

    /**
     * Sets the text color for typing indicator in conversations.
     *
     * @param typingIndicatorTextColor The color value for typing indicator text.
     */
    public ConversationsStyle setTypingIndicatorTextColor(@ColorInt int typingIndicatorTextColor) {
        this.typingIndicatorTextColor = typingIndicatorTextColor;
        return this;
    }

    /**
     * Sets the font for typing indicator in conversations.
     *
     * @param typingIndicatorTextFont The font name for typing indicator text.
     */
    public ConversationsStyle setTypingIndicatorTextFont(String typingIndicatorTextFont) {
        this.typingIndicatorTextFont = typingIndicatorTextFont;
        return this;
    }

    /**
     * Sets the font for thread indicator in conversations.
     *
     * @param threadIndicatorTextFont The font name for thread indicator text.
     */
    public ConversationsStyle setThreadIndicatorTextFont(String threadIndicatorTextFont) {
        this.threadIndicatorTextFont = threadIndicatorTextFont;
        return this;
    }

    /**
     * Sets the text color for thread indicator in conversations.
     *
     * @param threadIndicatorTextColor The color value for thread indicator text.
     */
    public ConversationsStyle setThreadIndicatorTextColor(@ColorInt int threadIndicatorTextColor) {
        this.threadIndicatorTextColor = threadIndicatorTextColor;
        return this;
    }

    @Override
    public ConversationsStyle setBackground(@ColorInt int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public ConversationsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public ConversationsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public ConversationsStyle setBorderWidth(@ColorInt int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public ConversationsStyle setBorderColor(@ColorInt int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public ConversationsStyle setActiveBackground(@ColorInt int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getEmptyTextAppearance() {
        return emptyTextAppearance;
    }

    public int getErrorTextAppearance() {
        return errorTextAppearance;
    }

    public int getLastMessageTextAppearance() {
        return lastMessageTextAppearance;
    }

    public int getTypingIndicatorTextAppearance() {
        return typingIndicatorTextAppearance;
    }

    public int getThreadIndicatorTextAppearance() {
        return threadIndicatorTextAppearance;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getBackIconTint() {
        return backIconTint;
    }

    public int getOnlineStatusColor() {
        return onlineStatusColor;
    }

    public int getSeparatorColor() {
        return separatorColor;
    }

    public int getLoadingIconTint() {
        return loadingIconTint;
    }

    public int getEmptyTextColor() {
        return emptyTextColor;
    }

    public String getEmptyTextFont() {
        return emptyTextFont;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public int getLastMessageTextColor() {
        return lastMessageTextColor;
    }

    public String getLastMessageTextFont() {
        return lastMessageTextFont;
    }

    public int getTypingIndicatorTextColor() {
        return typingIndicatorTextColor;
    }

    public String getTypingIndicatorTextFont() {
        return typingIndicatorTextFont;
    }

    public String getThreadIndicatorTextFont() {
        return threadIndicatorTextFont;
    }

    public int getThreadIndicatorTextColor() {
        return threadIndicatorTextColor;
    }
}
