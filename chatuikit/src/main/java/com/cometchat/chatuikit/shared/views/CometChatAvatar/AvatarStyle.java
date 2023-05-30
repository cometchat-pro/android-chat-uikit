package com.cometchat.chatuikit.shared.views.CometChatAvatar;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The AvatarStyle class is a model class that stores the styling properties of the CometChatAvatar view.
 * It extends the BaseStyle class, which contains common styling properties.
 */
public class AvatarStyle extends BaseStyle {

    private String textFont;
    private @ColorInt
    int textColor;
    private @StyleRes
    int textAppearance;
    private int outerViewSpacing = -1;
    private int innerViewWidth = -1;
    private @ColorInt
    int innerViewBorderColor;
    private @ColorInt
    int innerBackgroundColor;
    private int innerViewRadius = -1;
    private int textSize = -1;
    private Drawable innerBackgroundDrawable;
    private int width = 0;
    private int height = 0;

    /**
     * Set the font for the text displayed in the CometChatAvatar view.
     *
     * @param textFont A string representing the font name.
     */
    public AvatarStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Set the text color for the text displayed in the CometChatAvatar view.
     *
     * @param textColor An integer value representing the color of the text.
     */
    public AvatarStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Set the text appearance for the text displayed in the CometChatAvatar view.
     *
     * @param textAppearance A style resource identifier for the text appearance.
     */
    public AvatarStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    /**
     * Set the spacing between the outer and inner views of the CometChatAvatar view.
     *
     * @param outerViewSpacing An integer value representing the spacing between the outer and inner views.
     */
    public AvatarStyle setOuterViewSpacing(int outerViewSpacing) {
        this.outerViewSpacing = outerViewSpacing;
        return this;
    }

    /**
     * Set the width of the inner view in the CometChatAvatar view.
     *
     * @param innerViewWidth An integer value representing the width of the inner view.
     */
    public AvatarStyle setInnerViewWidth(int innerViewWidth) {
        this.innerViewWidth = innerViewWidth;
        return this;
    }

    /**
     * Set the border color of the inner view in the CometChatAvatar view.
     *
     * @param innerViewBorderColor An integer value representing the color of the border of the inner view.
     * @return The AvatarStyle instance with updated inner view border color.
     */
    public AvatarStyle setInnerViewBorderColor(int innerViewBorderColor) {
        this.innerViewBorderColor = innerViewBorderColor;
        return this;
    }

    /**
     * Set the radius of the inner view in the CometChatAvatar view.
     *
     * @param innerViewRadius An integer value representing the radius of the inner view.
     * @return The AvatarStyle instance with updated inner view radius.
     */
    public AvatarStyle setInnerViewRadius(int innerViewRadius) {
        this.innerViewRadius = innerViewRadius;
        return this;
    }

    /**
     * Sets the background color for the inner view of the avatar.
     *
     * @param innerViewBackgroundColor the background color to set.
     */
    public AvatarStyle setInnerBackgroundColor(int innerViewBackgroundColor) {
        this.innerBackgroundColor = innerViewBackgroundColor;
        return this;
    }

    /**
     * Sets the text size for the inner view of the avatar.
     *
     * @param textSize the text size to set.
     */
    public AvatarStyle setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * Sets the background drawable for the inner view of the avatar.
     *
     * @param innerBackgroundDrawable the drawable to set.
     */
    public AvatarStyle setInnerBackgroundDrawable(Drawable innerBackgroundDrawable) {
        this.innerBackgroundDrawable = innerBackgroundDrawable;
        return this;
    }

    /**
     * Sets the background color for the outer view of the avatar.
     *
     * @param background the background color to set.
     */
    public AvatarStyle setOuterBackgroundColor(int background) {
        super.setBackground(background);
        return this;
    }

    /**
     * Sets the background drawable for the outer view of the avatar.
     *
     * @param drawableBackground the drawable to set.
     * @return the AvatarStyle object for chaining method calls.
     */
    public AvatarStyle setOuterBackgroundDrawable(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    /**
     * Sets the corner radius for the outer view of the avatar.
     *
     * @param cornerRadius the corner radius to set.
     * @return the AvatarStyle object for chaining method calls.
     */
    public AvatarStyle setOuterCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    /**
     * Sets the border width for the outer view of the avatar.
     *
     * @param borderWidth the border width to set.
     * @return the AvatarStyle object for chaining method calls.
     */
    public AvatarStyle setOuterBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    /**
     * Sets the border color for the outer view of the avatar.
     *
     * @param borderColor the border color to set.
     * @return the AvatarStyle object for chaining method calls.
     */
    public AvatarStyle setOuterBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    /**
     * Sets the width for the avatar view.
     *
     * @param width the width to set for the avatar view.
     * @return the AvatarStyle object for chaining method calls.
     */
    public AvatarStyle setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the height for the avatar view.
     *
     * @param height the height to set for the avatar view.
     * @return the AvatarStyle object for chaining method calls.
     */
    public AvatarStyle setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getInnerBackgroundColor() {
        return innerBackgroundColor;
    }

    public Drawable getInnerBackgroundDrawable() {
        return innerBackgroundDrawable;
    }

    public String getTextFont() {
        return textFont;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextAppearance() {
        return textAppearance;
    }

    public int getOuterViewSpacing() {
        return outerViewSpacing;
    }

    public int getInnerViewWidth() {
        return innerViewWidth;
    }

    public int getInnerViewBorderColor() {
        return innerViewBorderColor;
    }

    public int getInnerViewRadius() {
        return innerViewRadius;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
