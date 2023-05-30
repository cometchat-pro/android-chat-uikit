package com.cometchat.chatuikit.joinprotectedgroup;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.conversations.ConversationsStyle;
import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Style class for customizing the appearance of the JoinProtectedGroup view.
 */
public class JoinProtectedGroupStyle extends BaseStyle {
    private final String TAG = ConversationsStyle.class.getName();

    private @ColorRes
    int titleColor, backIconTint, joinGroupIconTint, editBoxBackgroundColor, editBoxCornerRadius, editBoxTextColor, editBoxBorderColor, editBoxBorderWidth, editTextPlaceHolderColor, descriptionColor;
    private @StyleRes
    int descriptionTextAppearance, titleTextAppearance, passwordTextAppearance;
    private String titleFont;

    @Override
    public JoinProtectedGroupStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public JoinProtectedGroupStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public JoinProtectedGroupStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public JoinProtectedGroupStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public JoinProtectedGroupStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public JoinProtectedGroupStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the color of the title in the JoinProtectedGroup view.
     *
     * @param titleColor The color to set for the title.
     */
    public JoinProtectedGroupStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the tint color of the back icon in the JoinProtectedGroup view.
     *
     * @param backIconTint The tint color to set for the back icon.
     */
    public JoinProtectedGroupStyle setBackIconTint(int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    /**
     * Sets the tint color of the join group icon in the JoinProtectedGroup view.
     *
     * @param joinGroupIconTint The tint color to set for the join group icon.
     */
    public JoinProtectedGroupStyle setJoinGroupIconTint(int joinGroupIconTint) {
        this.joinGroupIconTint = joinGroupIconTint;
        return this;
    }

    /**
     * Sets the background color of the edit box in the JoinProtectedGroup view.
     *
     * @param editBoxBackgroundColor The background color to set for the edit box.
     */
    public JoinProtectedGroupStyle setEditBoxBackgroundColor(int editBoxBackgroundColor) {
        this.editBoxBackgroundColor = editBoxBackgroundColor;
        return this;
    }

    /**
     * Sets the corner radius of the edit box in the JoinProtectedGroup view.
     *
     * @param editBoxCornerRadius The corner radius to set for the edit box.
     */
    public JoinProtectedGroupStyle setEditBoxCornerRadius(int editBoxCornerRadius) {
        this.editBoxCornerRadius = editBoxCornerRadius;
        return this;
    }

    /**
     * Sets the text color of the edit box in the JoinProtectedGroup view.
     *
     * @param editBoxTextColor The text color to set for the edit box.
     */
    public JoinProtectedGroupStyle setEditBoxTextColor(int editBoxTextColor) {
        this.editBoxTextColor = editBoxTextColor;
        return this;
    }

    /**
     * Sets the border color of the edit box in the JoinProtectedGroup view.
     *
     * @param editBoxBorderColor The border color to set for the edit box.
     */

    public JoinProtectedGroupStyle setEditBoxBorderColor(int editBoxBorderColor) {
        this.editBoxBorderColor = editBoxBorderColor;
        return this;
    }

    /**
     * Sets the border width of the edit box in the JoinProtectedGroup view.
     *
     * @param editBoxBorderWidth The border width to set for the edit box.
     */
    public JoinProtectedGroupStyle setEditBoxBorderWidth(int editBoxBorderWidth) {
        this.editBoxBorderWidth = editBoxBorderWidth;
        return this;
    }

    /**
     * Sets the color of the placeholder text in the edit box of the JoinProtectedGroup view.
     *
     * @param editTextPlaceHolderColor The color to set for the placeholder text.
     */
    public JoinProtectedGroupStyle setEditTextPlaceHolderColor(int editTextPlaceHolderColor) {
        this.editTextPlaceHolderColor = editTextPlaceHolderColor;
        return this;
    }

    /**
     * Sets the color of the description text in the JoinProtectedGroup view.
     *
     * @param descriptionColor The color to set for the description text.
     */
    public JoinProtectedGroupStyle setDescriptionColor(int descriptionColor) {
        this.descriptionColor = descriptionColor;
        return this;
    }

    /**
     * Sets the text appearance of the description text in the JoinProtectedGroup view.
     *
     * @param descriptionTextAppearance The text appearance to set for the description text.
     */
    public JoinProtectedGroupStyle setDescriptionTextAppearance(int descriptionTextAppearance) {
        this.descriptionTextAppearance = descriptionTextAppearance;
        return this;
    }

    /**
     * Sets the text appearance of the title in the JoinProtectedGroup view.
     *
     * @param titleTextAppearance The text appearance to set for the title.
     */
    public JoinProtectedGroupStyle setTitleTextAppearance(int titleTextAppearance) {
        this.titleTextAppearance = titleTextAppearance;
        return this;
    }

    /**
     * Sets the text appearance of the password in the JoinProtectedGroup view.
     *
     * @param passwordTextAppearance The text appearance to set for the password.
     */
    public JoinProtectedGroupStyle setPasswordTextAppearance(int passwordTextAppearance) {
        this.passwordTextAppearance = passwordTextAppearance;
        return this;
    }

    /**
     * Sets the font for the title in the JoinProtectedGroup view.
     *
     * @param titleFont The font to set for the title.
     */
    public JoinProtectedGroupStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public String getTAG() {
        return TAG;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getBackIconTint() {
        return backIconTint;
    }

    public int getJoinGroupIconTint() {
        return joinGroupIconTint;
    }

    public int getEditBoxBackgroundColor() {
        return editBoxBackgroundColor;
    }

    public int getEditBoxCornerRadius() {
        return editBoxCornerRadius;
    }

    public int getEditBoxTextColor() {
        return editBoxTextColor;
    }

    public int getEditBoxBorderColor() {
        return editBoxBorderColor;
    }

    public int getEditBoxBorderWidth() {
        return editBoxBorderWidth;
    }

    public int getEditTextPlaceHolderColor() {
        return editTextPlaceHolderColor;
    }

    public int getDescriptionColor() {
        return descriptionColor;
    }

    public int getDescriptionTextAppearance() {
        return descriptionTextAppearance;
    }

    public int getTitleTextAppearance() {
        return titleTextAppearance;
    }

    public int getPasswordTextAppearance() {
        return passwordTextAppearance;
    }

    public String getTitleFont() {
        return titleFont;
    }
}
