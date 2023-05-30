package com.cometchat.chatuikit.messagelist;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The MessageListStyle class represents the visual style configuration for the message list.
 * <p>
 * It extends the BaseStyle class and provides additional style properties for customizing the appearance of the message list.
 */
public class MessageListStyle extends BaseStyle {

    public final String TAG = MessageListStyle.class.getName();

    private String emptyTextFont;

    private @StyleRes
    int emptyTextAppearance;
    private @StyleRes
    int errorTextAppearance;

    private @ColorInt
    int loadingIconTint;
    private @ColorInt
    int emptyTextColor;
    private @ColorInt
    int errorTextColor;

    private @ColorInt
    int nameTextColor;
    private @ColorInt
    int timeStampTextColor;
    private @ColorInt
    int threadReplySeparatorColor;
    private @ColorInt
    int threadReplyTextColor;
    private @ColorInt
    int threadReplyIconTint;
    private @StyleRes
    int nameTextAppearance;
    private @StyleRes
    int timeStampTextAppearance;
    private @StyleRes
    int threadReplyTextAppearance;

    @Override
    public MessageListStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessageListStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;

    }

    @Override
    public MessageListStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;

    }

    @Override
    public MessageListStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessageListStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessageListStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the text color for the name in the message list.
     *
     * @param nameTextColor The integer value representing the color of the name text.
     */
    public MessageListStyle setNameTextColor(int nameTextColor) {
        this.nameTextColor = nameTextColor;
        return this;
    }

    /**
     * Sets the text color for the timestamp in the message list.
     *
     * @param timeStampTextColor The integer value representing the color of the timestamp text.
     */
    public MessageListStyle setTimeStampTextColor(int timeStampTextColor) {
        this.timeStampTextColor = timeStampTextColor;
        return this;
    }

    /**
     * Sets the color for the separator between thread replies in the message list.
     *
     * @param threadReplySeparatorColor The integer value representing the color of the thread reply separator.
     */
    public MessageListStyle setThreadReplySeparatorColor(int threadReplySeparatorColor) {
        this.threadReplySeparatorColor = threadReplySeparatorColor;
        return this;
    }

    /**
     * Sets the text color for the thread replies in the message list.
     *
     * @param threadReplyTextColor The integer value representing the color of the thread reply text.
     */
    public MessageListStyle setThreadReplyTextColor(int threadReplyTextColor) {
        this.threadReplyTextColor = threadReplyTextColor;
        return this;
    }

    /**
     * Sets the tint color for the thread reply icon in the message list.
     *
     * @param threadReplyIconTint The integer value representing the tint color of the thread reply icon.
     */
    public MessageListStyle setThreadReplyIconTint(int threadReplyIconTint) {
        this.threadReplyIconTint = threadReplyIconTint;
        return this;
    }

    /**
     * Sets the text appearance for the name in the message list.
     *
     * @param nameTextAppearance The resource ID representing the text appearance of the name.
     */
    public MessageListStyle setNameTextAppearance(int nameTextAppearance) {
        this.nameTextAppearance = nameTextAppearance;
        return this;
    }

    /**
     * Sets the text appearance for the timestamp in the message list.
     *
     * @param timeStampTextAppearance The resource ID representing the text appearance of the timestamp.
     */
    public MessageListStyle setTimeStampTextAppearance(int timeStampTextAppearance) {
        this.timeStampTextAppearance = timeStampTextAppearance;
        return this;
    }

    /**
     * Sets the text appearance for the thread replies in the message list.
     *
     * @param threadReplyTextAppearance The resource ID representing the text appearance of the thread replies.
     */
    public MessageListStyle setThreadReplyTextAppearance(int threadReplyTextAppearance) {
        this.threadReplyTextAppearance = threadReplyTextAppearance;
        return this;
    }

    /**
     * Sets the font for the empty text in the message list.
     *
     * @param emptyTextFont The string representing the font of the empty text.
     */
    public MessageListStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }

    /**
     * Sets the text appearance for the empty text in the message list.
     *
     * @param emptyTextAppearance The resource ID representing the text appearance of the empty text.
     */
    public MessageListStyle setEmptyTextAppearance(int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;
    }

    /**
     * Sets the text appearance for the error text in the message list.
     *
     * @param errorTextAppearance The resource ID representing the text appearance of the error text.
     */
    public MessageListStyle setErrorTextAppearance(int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;
    }

    /**
     * Sets the tint color for the loading icon in the message list.
     *
     * @param loadingIconTint The integer value representing the tint color of the loading icon.
     */
    public MessageListStyle setLoadingIconTint(int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }


    /**
     * Sets the text color for the empty text in the message list.
     *
     * @param emptyTextColor The integer value representing the color of the empty text.
     */
    public MessageListStyle setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }

    /**
     * Sets the text color for the error text in the message list.
     *
     * @param errorTextColor The integer value representing the color of the error text.
     */
    public MessageListStyle setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }

    public String getEmptyTextFont() {
        return emptyTextFont;
    }

    public int getEmptyTextAppearance() {
        return emptyTextAppearance;
    }

    public int getErrorTextAppearance() {
        return errorTextAppearance;
    }

    public int getLoadingIconTint() {
        return loadingIconTint;
    }

    public int getEmptyTextColor() {
        return emptyTextColor;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public int getNameTextColor() {
        return nameTextColor;
    }

    public int getTimeStampTextColor() {
        return timeStampTextColor;
    }

    public int getThreadReplySeparatorColor() {
        return threadReplySeparatorColor;
    }

    public int getThreadReplyTextColor() {
        return threadReplyTextColor;
    }

    public int getThreadReplyIconTint() {
        return threadReplyIconTint;
    }

    public int getNameTextAppearance() {
        return nameTextAppearance;
    }

    public int getTimeStampTextAppearance() {
        return timeStampTextAppearance;
    }

    public int getThreadReplyTextAppearance() {
        return threadReplyTextAppearance;
    }
}
