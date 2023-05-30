package com.cometchat.chatuikit.messagecomposer;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

import org.jetbrains.annotations.NotNull;

/**
 * A class representing the style configuration for the MessageComposer component. It extends the {@link BaseStyle} class
 * <p>
 * and provides methods to customize various visual aspects of the MessageComposer.
 */
public class MessageComposerStyle extends BaseStyle {
    private final String TAG = MessageComposerStyle.class.getName();
    private @ColorRes
    int attachIconTint;
    private @ColorRes
    int sendIconTint;
    private @ColorRes
    int inputBackground;
    private @ColorRes
    int separatorTint;
    private @ColorRes
    int textColor;
    private @ColorRes
    int placeHolderTextColor;
    private @ColorRes
    int actionSheetSeparatorTint;
    private @ColorRes
    int actionSheetTitleColor;
    private @ColorRes
    int actionSheetLayoutModeIconTint;
    private Drawable actionSheetBackground;
    private @StyleRes
    int textAppearance;
    private @StyleRes
    int actionSheetTitleAppearance;
    private String actionSheetTitleFont;
    private String textFont;
    private Drawable inputGradient;
    private int inputCornerRadius = -1;

    /**
     * Sets the corner radius of the input field.
     *
     * @param inputCornerRadius The corner radius of the input field.
     */
    public MessageComposerStyle setInputCornerRadius(int inputCornerRadius) {
        this.inputCornerRadius = inputCornerRadius;
        return this;
    }

    /**
     * Sets the tint color of the attachment icon.
     *
     * @param attachIconTint The tint color of the attachment icon.
     */
    public MessageComposerStyle setAttachIconTint(int attachIconTint) {
        this.attachIconTint = attachIconTint;
        return this;
    }

    /**
     * Sets the tint color of the send button icon.
     *
     * @param sendIconTint The tint color of the send button icon.
     */
    public MessageComposerStyle setSendIconTint(int sendIconTint) {
        this.sendIconTint = sendIconTint;
        return this;
    }

    /**
     * Sets the background color of the input field.
     *
     * @param inputBackground The background color of the input field.
     */
    public MessageComposerStyle setInputBackgroundColor(int inputBackground) {
        this.inputBackground = inputBackground;
        return this;
    }

    /**
     * Sets the tint color of the separator line.
     *
     * @param separatorTint The tint color of the separator line.
     */
    public MessageComposerStyle setSeparatorTint(int separatorTint) {
        this.separatorTint = separatorTint;
        return this;
    }

    /**
     * Sets the text color of the input field.
     *
     * @param textColor The text color of the input field.
     */
    public MessageComposerStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Sets the text color of the placeholder in the input field.
     *
     * @param placeHolderTextColor The text color of the placeholder.
     */
    public MessageComposerStyle setPlaceHolderTextColor(int placeHolderTextColor) {
        this.placeHolderTextColor = placeHolderTextColor;
        return this;
    }

    /**
     * Sets the tint color of the separator line in the action sheet.
     *
     * @param actionSheetSeparatorTint The tint color of the action sheet separator line.
     */
    public MessageComposerStyle setActionSheetSeparatorTint(int actionSheetSeparatorTint) {
        this.actionSheetSeparatorTint = actionSheetSeparatorTint;
        return this;
    }

    /**
     * Sets the title color of the action sheet.
     *
     * @param actionSheetTitleColor The title color of the action sheet.
     */
    public MessageComposerStyle setActionSheetTitleColor(int actionSheetTitleColor) {
        this.actionSheetTitleColor = actionSheetTitleColor;
        return this;
    }

    /**
     * Sets the tint color for the icon in the action sheet layout mode.
     *
     * @param actionSheetLayoutModeIconTint The color for the icon.
     */
    public MessageComposerStyle setActionSheetLayoutModeIconTint(int actionSheetLayoutModeIconTint) {
        this.actionSheetLayoutModeIconTint = actionSheetLayoutModeIconTint;
        return this;
    }

    /**
     * Sets the background color for the action sheet.
     *
     * @param drawableBackground The background drawable representing the color.
     */
    public MessageComposerStyle setActionSheetBackgroundColor(Drawable drawableBackground) {
        actionSheetBackground = drawableBackground;
        return this;
    }

    /**
     * Sets the text appearance for the message composer.
     *
     * @param textAppearance The style resource ID representing the text appearance.
     */
    public MessageComposerStyle setTextAppearance(@StyleRes int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    /**
     * Sets the appearance of the title in the action sheet.
     *
     * @param actionSheetTitleAppearance The appearance for the action sheet title.
     */
    public MessageComposerStyle setActionSheetTitleAppearance(int actionSheetTitleAppearance) {
        this.actionSheetTitleAppearance = actionSheetTitleAppearance;
        return this;
    }

    /**
     * Sets the font for the title in the action sheet.
     *
     * @param actionSheetTitleFont The font name for the action sheet title.
     */
    public MessageComposerStyle setActionSheetTitleFont(@NotNull String actionSheetTitleFont) {
        this.actionSheetTitleFont = actionSheetTitleFont;
        return this;
    }

    /**
     * Sets the font for the text in the message composer.
     *
     * @param textFont The font name for the message composer text.
     */
    public MessageComposerStyle setTextFont(@NotNull String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Sets the gradient for the input field in the message composer.
     *
     * @param inputGradient The gradient drawable representing the input field gradient.
     */
    public MessageComposerStyle setInputGradient(@NotNull Drawable inputGradient) {
        this.inputGradient = inputGradient;
        return this;
    }

    @Override
    public MessageComposerStyle setBackground(@ColorRes int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessageComposerStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public MessageComposerStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public MessageComposerStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessageComposerStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessageComposerStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    public int getAttachIconTint() {
        return attachIconTint;
    }

    public int getSendIconTint() {
        return sendIconTint;
    }

    public int getInputBackground() {
        return inputBackground;
    }

    public int getSeparatorTint() {
        return separatorTint;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getPlaceHolderTextColor() {
        return placeHolderTextColor;
    }

    public int getActionSheetSeparatorTint() {
        return actionSheetSeparatorTint;
    }

    public int getActionSheetTitleColor() {
        return actionSheetTitleColor;
    }

    public int getActionSheetLayoutModeIconTint() {
        return actionSheetLayoutModeIconTint;
    }

    public Drawable getActionSheetBackgroundColor() {
        return actionSheetBackground;
    }

    public int getTextAppearance() {
        return textAppearance;
    }

    public int getActionSheetTitleAppearance() {
        return actionSheetTitleAppearance;
    }

    public String getActionSheetTitleFont() {
        return actionSheetTitleFont;
    }

    public String getTextFont() {
        return textFont;
    }

    public Drawable getInputGradient() {
        return inputGradient;
    }

    public int getInputCornerRadius() {
        return inputCornerRadius;
    }
}
