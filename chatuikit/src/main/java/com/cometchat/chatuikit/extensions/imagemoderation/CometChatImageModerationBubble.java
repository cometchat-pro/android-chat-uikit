package com.cometchat.chatuikit.extensions.imagemoderation;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.google.android.material.card.MaterialCardView;

public class CometChatImageModerationBubble extends MaterialCardView {
    private Context context;
    private LinearLayout unsafeContent;
    private LinearLayout unsafeContentClick;
    private ImageView warningImageView;
    private TextView warningTextView;
    private OnClick onClick;

    public CometChatImageModerationBubble(Context context) {
        super(context);
        init(context);
    }

    public CometChatImageModerationBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatImageModerationBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        View view = View.inflate(context, R.layout.image_moderation_layout, null);
        unsafeContent = view.findViewById(R.id.sensitive_content);
        unsafeContentClick = view.findViewById(R.id.sensitive_content_click_holder);
        warningImageView = view.findViewById(R.id.warning_image);
        warningTextView = view.findViewById(R.id.warning_text);
        unsafeContentClick.setOnClickListener(view1 -> {
            if (onClick != null) onClick.onClick();
        });
        addView(view);

    }

    public void setWaringIconTint(@ColorInt int color) {
        if (color != 0) warningImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setWarningTextColor(@ColorInt int color) {
        if (color != 0) warningTextView.setTextColor(color);
    }

    public void setWarningTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) warningTextView.setTextAppearance(context, appearance);
    }

    public void setSensitiveContentBackgroundImage(int sensitiveContentBackgroundImage) {
        if (sensitiveContentBackgroundImage != 0)
            unsafeContent.setBackground(context.getResources().getDrawable(sensitiveContentBackgroundImage));
    }

    public void setWarningImageIcon(int warningImageIcon) {
        if (warningImageIcon != 0) warningImageView.setImageResource(warningImageIcon);
    }

    public void setWarningText(String warningText) {
        if (warningText != null && !warningText.isEmpty()) {
            warningTextView.setText(warningText);
        }
    }

    public void setStyle(ImageModerationStyle style) {
        if (style != null) {
            setWaringIconTint(style.getWarningIconTint());
            setWarningTextColor(style.getWarningTextColor());
            setWarningTextAppearance(style.getWarningTextAppearance());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }


}
