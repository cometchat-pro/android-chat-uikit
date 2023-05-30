package com.cometchat.chatuikit.calls.callbubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

/**
 * A custom widget representing a call bubble for the Calling feature.
 * <p>
 * It extends the MaterialCardView class and provides methods to customize its appearance and behavior.
 */
public class CometChatCallBubble extends MaterialCardView {

    private Context context;
    ImageView icon;
    TextView title;
    MaterialButton joinCallButton;
    OnClick onClick;

    /**
     * Constructs a new CometChatCallBubble object with the given context.
     *
     * @param context The context in which the call bubble will be used.
     */
    public CometChatCallBubble(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new CometChatCallBubble object with the given context and attribute set.
     *
     * @param context The context in which the call bubble will be used.
     * @param attrs   The attribute set for initializing the call bubble.
     */
    public CometChatCallBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new CometChatCallBubble object with the given context, attribute set, and default style.
     *
     * @param context      The context in which the call bubble will be used.
     * @param attrs        The attribute set for initializing the call bubble.
     * @param defStyleAttr The default style attribute for the call bubble.
     */
    public CometChatCallBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the call bubble by setting up its internal components and behaviors.
     *
     * @param context The context in which the call bubble will be used.
     */
    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        View view = View.inflate(context, R.layout.group_call_bubble, null);
        icon = view.findViewById(R.id.icon);
        title = view.findViewById(R.id.title);
        joinCallButton = view.findViewById(R.id.join_call);
        joinCallButton.setElevation(0);
        joinCallButton.setOnClickListener(view1 -> {
            if (onClick != null) onClick.onClick();
        });
        addView(view);
    }

    /**
     * Sets the title text of the call bubble.
     *
     * @param title The text to be displayed as the title.
     */
    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) this.title.setText(title);
    }

    /**
     * Sets the color of the title text.
     *
     * @param color The color to be applied to the title text.
     */
    public void setTitleColor(@ColorInt int color) {
        if (color != 0) this.title.setTextColor(color);
    }

    /**
     * Sets the appearance of the title text using a text style resource.
     *
     * @param appearance The text style resource defining the appearance of the title text.
     */
    public void setTitleAppearance(@StyleRes int appearance) {
        if (appearance != 0) this.title.setTextAppearance(context, appearance);
    }

    /**
     * Sets the tint color of the icon.
     *
     * @param color The color to be applied as the tint of the icon.
     */
    public void setIconTint(@ColorInt int color) {
        if (color != 0) this.icon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the icon drawable resource for the call bubble.
     *
     * @param drawable The drawable resource to be displayed as the icon.
     */
    public void setIcon(@DrawableRes int drawable) {
        if (drawable != 0) this.icon.setImageResource(drawable);
    }

    /**
     * Sets the background color of the join call button.
     *
     * @param color The color to be applied as the background color of the button.
     */
    public void setButtonBackgroundColor(@ColorInt int color) {
        if (color != 0) this.joinCallButton.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the text of the join call button.
     *
     * @param text The text to be displayed on the button.
     */
    public void setButtonText(String text) {
        if (text != null && !text.isEmpty()) this.joinCallButton.setText(text);
    }

    /**
     * Sets the text color of the join call button.
     *
     * @param color The color to be applied to the button text.
     */
    public void setButtonTextColor(@ColorInt int color) {
        if (color != 0) this.joinCallButton.setTextColor(color);
    }

    /**
     * Sets the appearance of the join call button text using a text style resource.
     *
     * @param appearance The text style resource defining the appearance of the button text.
     */
    public void setButtonTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) this.joinCallButton.setTextAppearance(context, appearance);
    }

    /**
     * Sets the background drawable resource of the join call button.
     *
     * @param drawable The drawable resource to be used as the background of the button.
     */
    public void setButtonBackgroundDrawable(@DrawableRes int drawable) {
        if (drawable != 0) this.joinCallButton.setBackgroundResource(drawable);
    }

    /**
     * Sets the background drawable of the join call button.
     *
     * @param drawable The drawable object to be used as the background of the button.
     */
    public void setButtonBackgroundDrawable(Drawable drawable) {
        if (drawable != null) this.joinCallButton.setBackgroundDrawable(drawable);
    }

    /**
     * Sets the click listener for the join call button.
     *
     * @param onClick The callback to be invoked when the button is clicked.
     */
    public void setOnClick(OnClick onClick) {
        if (onClick != null) this.onClick = onClick;
    }
}
