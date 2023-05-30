package com.cometchat.chatuikit.shared.views.CometChatBadge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

/**
 * CometChatBadge is a custom view that represents a badge with a count displayed inside a MaterialCardView.
 * It provides methods to customize the appearance of the badge, such as setting the count, text size, text color,
 * background color, corner radius, border color, and more.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 17th May 2023
 */

public class CometChatBadge extends MaterialCardView {

    private TextView tvCount;   //Used to display count

    private Context context;

    private MaterialCardView countView;

    private int count;      //Used to store value of count

    private float countSize;    //Used to store size of count

    private int countColor;     //Used to store color of count

    private int countBackgroundColor;      //Used to store background color of count.

    private FontUtils fontUtils;

    /**
     * Constructs a new CometChatBadge with the given context.
     *
     * @param context The context in which the CometChatBadge is created.
     */
    public CometChatBadge(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    /**
     * Constructs a new CometChatBadge with the given context and attribute set.
     *
     * @param context The context in which the CometChatBadge is created.
     * @param attrs   The attribute set to apply to the CometChatBadge.
     */
    public CometChatBadge(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatBadge with the given context, attribute set, and default style attribute.
     *
     * @param context      The context in which the CometChatBadge is created.
     * @param attrs        The attribute set to apply to the CometChatBadge.
     * @param defStyleAttr The default style attribute to apply to the CometChatBadge.
     */
    public CometChatBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * This method is used to initialize the view present in component and set the value if it is
     * available.
     *
     * @param context
     * @param attributeSet
     * @param defStyleAttr
     */
    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        View view = View.inflate(context, R.layout.cometchat_badge_count, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.BadgeCount,
                0, 0);
        count = a.getInt(R.styleable.BadgeCount_count, 0);
        countSize = a.getDimension(R.styleable.BadgeCount_count_size, 12);
        countColor = a.getColor(R.styleable.BadgeCount_count_color, 0);
        countBackgroundColor = a.getColor(R.styleable.BadgeCount_count_background_color, 0);

        addView(view);

        countView = view.findViewById(R.id.count_parent);
        tvCount = view.findViewById(R.id.tvSetCount);
        tvCount.setTextSize(countSize);
        tvCount.setTextColor(countColor);
        tvCount.setText(String.valueOf(count));
        setBackground(countBackgroundColor);
    }

    /**
     * Sets the background color of the count.
     *
     * @param color The color to set as the background color.
     */
    public void setBackground(@ColorInt int color) {
        if (color != 0) {
            countView.setCardBackgroundColor(color);
            setCardBackgroundColor(color);
        }
    }

    /**
     * Sets the background gradient for the count.
     *
     * @param colorArray   The array of colors to use for the gradient.
     * @param orientation The orientation of the gradient.
     */
    public void setBackgroundGradient(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (countView != null) {
            GradientDrawable gd = new GradientDrawable(orientation, colorArray);
            gd.setCornerRadius(countView.getRadius());
            countView.setBackgroundDrawable(gd);
        }
    }

    /**
     * Sets the background drawable for the count.
     *
     * @param drawable The drawable to set as the background.
     */
    public void setBackgroundDrawable(Drawable drawable) {
        if (countView != null && drawable != null) {
            countView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Sets the corner radius for the count view.
     *
     * @param radius The corner radius to set.
     */
    public void cornerRadius(float radius) {
        if (radius >= 0) {
            countView.setRadius(radius);
            setRadius(radius);
        }
    }

    /**
     * Sets the text color of the count.
     *
     * @param color The color to set for the text.
     */
    public void setTextColor(@ColorInt int color) {
        if (color != 0)
            tvCount.setTextColor(color);
    }

    /**
     * Sets the text size of the count.
     *
     * @param size The text size to set.
     */
    public void setTextSize(int size) {
        if (size > 0)
            tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

    }

    /**
     * Sets the text appearance of the count.
     *
     * @param appearance The style resource ID to set as the text appearance.
     */
    public void setTextAppearance(@StyleRes int appearance) {
        if (appearance != 0)
            tvCount.setTextAppearance(context, appearance);
    }

    /**
     * Sets the border color for the count view.
     *
     * @param color The color to set as the border color.
     */
    public void borderColor(@ColorInt int color) {
        if (color != 0)
            setStrokeColor(color);
    }

    /**
     * Sets the border width for the count view.
     *
     * @param width The width to set as the border width.
     */
    public void borderWidth(int width) {
        if (width >= 0)
            setStrokeWidth(width);
    }

    /**
     * Sets the text font for the count.
     *
     * @param font The font to set for the text.
     */
    public void setTextFont(String font) {
        if (font != null)
            tvCount.setTypeface(fontUtils.getTypeFace(font));
    }

    /**
     * Sets the count value and displays it in the count view. If the count is more than 1,
     * it will display the value. The limit of the count shown will be 999.
     *
     * @param count The count value to set.
     */
    public void setCount(int count) {
        this.count = count;
        if (count < 999)
            tvCount.setText(String.valueOf(count));
        else
            tvCount.setText("999+");
    }

    public int getCount() {
        if (tvCount != null)
            return Integer.parseInt(tvCount.getText().toString());
        else
            return 0;
    }

    /**
     * Sets the style of the badge using a BadgeStyle object.
     *
     * @param badgeStyle The BadgeStyle object to set the style.
     */
    public void setStyle(BadgeStyle badgeStyle) {
        if(badgeStyle!=null) {
            setTextAppearance(badgeStyle.getTextAppearance());
            setTextColor(badgeStyle.getTextColor());
            setTextFont(badgeStyle.getTextFont());
            setTextSize(badgeStyle.getTextSize());

            setBackground(badgeStyle.getBackground());
            borderWidth(badgeStyle.getBorderWidth());
            borderColor(badgeStyle.getBorderColor());
            cornerRadius(badgeStyle.getCornerRadius());
            setBackgroundDrawable(badgeStyle.getDrawableBackground());
        }
    }

}
