package com.cometchat.chatuikit.shared.views.CometChatTextBubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;
import androidx.core.widget.TextViewCompat;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

/**
 * Represents a custom text bubble view used for displaying text messages in a chat interface.
 * <p>
 * It extends the MaterialCardView class to provide additional styling and customization options.
 */
public class CometChatTextBubble extends MaterialCardView {
    private Context context;
    private TextView textView;
    private View view;

    /**
     * Constructs a new CometChatTextBubble instance with the provided context.
     *
     * @param context The context in which the text bubble is created.
     */
    public CometChatTextBubble(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new CometChatTextBubble instance with the provided context and attribute set.
     *
     * @param context The context in which the text bubble is created.
     * @param attrs   The attribute set for initializing the view.
     */
    public CometChatTextBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new CometChatTextBubble instance with the provided context, attribute set, and default style attribute.
     *
     * @param context      The context in which the text bubble is created.
     * @param attrs        The attribute set for initializing the view.
     * @param defStyleAttr The default style attribute for the view.
     */
    public CometChatTextBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the text bubble view by setting up its appearance and retrieving necessary child views.
     *
     * @param context The context in which the text bubble is created.
     */
    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        view = View.inflate(context, R.layout.text_bubble, null);
        textView = view.findViewById(R.id.text_view);
        addView(view);
    }

    /**
     * Sets the text content of the text bubble.
     *
     * @param text The text to be displayed.
     */
    public void setText(String text) {
        textView.setText(text + "");
    }

    /**
     * Sets the text color of the text bubble.
     *
     * @param color The text color to be set.
     */
    public void setTextColor(@ColorInt int color) {
        if (color != 0) {
            textView.setTextColor(color);
        }
    }

    /**
     * Sets the font of the text in the text bubble.
     *
     * @param font The name of the font to be applied.
     */
    public void setTextFont(String font) {
        if (font != null && !font.isEmpty()) {
            textView.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
        }
    }

    /**
     * Sets the text appearance of the text bubble.
     *
     * @param appearance The style resource ID defining the text appearance.
     */
    public void setTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) {
            textView.setTextAppearance(context, appearance);
        }
    }

    public TextView getTextView() {
        return textView;
    }

    /**
     * Sets compound drawables (icons) on the text bubble.
     *
     * @param start  The drawable resource ID for the start (left) compound drawable.
     * @param top    The drawable resource ID for the top compound drawable.
     * @param end    The drawable resource ID for the end (right) compound drawable.
     * @param bottom The drawable resource ID for the bottom compound drawable.
     */
    public void setCompoundDrawable(@DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    /**
     * Sets the tint color for the compound drawables (icons) on the text bubble.
     *
     * @param color The color to be applied to the compound drawables.
     */
    public void setCompoundDrawableIconTint(@ColorInt int color) {
        TextViewCompat.setCompoundDrawableTintList(textView, ColorStateList.valueOf(color));
    }

    /**
     * Applies a style to the text bubble view.
     *
     * @param style The style object containing the desired appearance settings.
     * @see TextBubbleStyle
     */
    public void setStyle(TextBubbleStyle style) {
        if (style != null) {
            setTextColor(style.getTextColor());
            setTextFont(style.getTextFont());
            setTextAppearance(style.getTextAppearance());
            if (style.getCompoundDrawableIconTint() != 0)
                setCompoundDrawableIconTint(style.getCompoundDrawableIconTint());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }
}
