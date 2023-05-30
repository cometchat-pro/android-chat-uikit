package com.cometchat.chatuikit.shared.views.CometChatStatusIndicator;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.google.android.material.card.MaterialCardView;

/**
 * CometChatStatusIndicator is a custom MaterialCardView that represents the status indicator
 * <p>
 * It provides methods to customize the appearance of the status indicator
 * <p>
 * such as setting the border color, border width, corner radius, background color, and background image.
 */

public class CometChatStatusIndicator extends MaterialCardView {

    /**
     * Constructs a new CometChatStatusIndicator with the given context.
     *
     * @param context The context of the view.
     */
    public CometChatStatusIndicator(Context context) {
        super(context);
    }

    /**
     * Constructs a new CometChatStatusIndicator with the given context and attribute set.
     *
     * @param context The context of the view.
     * @param attrs   The attribute set for the view.
     */
    public CometChatStatusIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a new CometChatStatusIndicator with the given context, attribute set, and default style attribute.
     *
     * @param context      The context of the view.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute.
     */
    public CometChatStatusIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Sets the border color of the status indicator.
     *
     * @param color The color to set for the border.
     */
    public void setBorderColor(@ColorInt int color) {
        if (color != 0) {
            setStrokeColor(color);
        }
    }

    /**
     * Sets the border width of the status indicator.
     *
     * @param width The width of the border.
     */
    public void setBorderWidth(int width) {
        if (width >= 0) setStrokeWidth(width);
    }

    /**
     * Sets the corner radius of the status indicator.
     *
     * @param radius The corner radius to set.
     */
    public void setCornerRadius(float radius) {
        if (radius >= 0) setRadius(radius);
    }

    /**
     * Sets the background color of the status indicator.
     *
     * @param color The color to set for the background.
     */
    public void setBackgroundColor(@ColorInt int color) {
        if (color != 0) setCardBackgroundColor(color);
    }

    /**
     * Sets the background image of the status indicator.
     *
     * @param image The drawable resource ID of the background image.
     */
    public void setBackgroundImage(@DrawableRes int image) {
        if (image != 0) setBackgroundDrawable(getResources().getDrawable(image));
    }

    /**
     * Sets the style for the status indicator using the provided StatusIndicatorStyle object.
     *
     * @param style The style object to apply to the status indicator.
     */
    public void setStyle(StatusIndicatorStyle style) {
        if (style != null) {
            setBorderColor(style.getBorderColor());
            setBorderWidth(style.getBorderWidth());
            setCornerRadius(style.getCornerRadius());
            setBackgroundColor(style.getBackground());
            if (style.getDrawableBackground() != null)
                setBackgroundDrawable(style.getDrawableBackground());
        }
    }

}
