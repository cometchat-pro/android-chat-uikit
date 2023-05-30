package com.cometchat.chatuikit.extensions.collaborative;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

public class CometChatCollaborativeBoardBubble extends RelativeLayout {

    FontUtils fontUtils;
    private Context context;
    private View view;
    private ImageView icon;
    private TextView title;
    private TextView subtitle;
    private TextView separator;
    private TextView joinBtn;
    private MaterialCardView cvMessageBubble;
    private String url;
    private OnClick onClick;

    public CometChatCollaborativeBoardBubble(Context context) {
        super(context);
        initComponent(context);
    }

    public CometChatCollaborativeBoardBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public CometChatCollaborativeBoardBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    public CometChatCollaborativeBoardBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    private void initComponent(Context context) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_whiteboard_bubble, null);
        initView(view);
        joinBtn.setOnClickListener(view -> {
            if (onClick != null) onClick.onClick();
            else {
                Intent intent = new Intent(context, CometChatWebViewActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.URL, url);
                context.startActivity(intent);
            }
        });

    }

    private void initView(View view) {
        icon = view.findViewById(R.id.icon);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        joinBtn = view.findViewById(R.id.join_button);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        separator = view.findViewById(R.id.separator);
        addView(view);
    }

    public void borderColor(@ColorInt int color) {
        if (color != 0 && cvMessageBubble != null) cvMessageBubble.setStrokeColor(color);
    }

    public void setSeparatorColor(@ColorInt int color) {
        if (color != 0 && separator != null) separator.setBackgroundColor(color);
    }

    public void borderWidth(int width) {
        if (cvMessageBubble != null) cvMessageBubble.setStrokeWidth(width);
    }

    public void icon(Drawable imageDrawable) {
        if (icon != null && imageDrawable != null) icon.setImageDrawable(imageDrawable);
    }

    public void iconTint(@ColorInt int color) {
        if (icon != null && color != 0) icon.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setButtonText(String str) {
        if (joinBtn != null && str != null) joinBtn.setText(str);
        else {
            joinBtn.setText(context.getString(R.string.open_document));
        }
    }

    private void setButtonTextColor(int color) {
        if (joinBtn != null) joinBtn.setTextColor(color);
    }

    public void setButtonIcon(Drawable icon, int gravity) {
        if (joinBtn != null && icon != null) {
            if (gravity == Gravity.LEFT)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            else if (gravity == Gravity.TOP)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
            else if (gravity == Gravity.RIGHT)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            else if (gravity == Gravity.BOTTOM)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void buttonIconTint(@ColorInt int color) {
        if (joinBtn != null && color != 0)
            joinBtn.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }

    public void setButtonTextFont(String font) {
        if (font != null && joinBtn != null) joinBtn.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setButtonTextAppearance(@StyleRes int appearance) {
        if (appearance != 0 && joinBtn != null) joinBtn.setTextAppearance(context, appearance);
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble != null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel().toBuilder().setTopLeftCornerSize(topLeft).setTopRightCornerSize(topRight).setBottomLeftCornerSize(bottomLeft).setBottomRightCornerSize(bottomRight).build());
    }

    public void cornerRadius(float radius) {
        if (cvMessageBubble != null) cvMessageBubble.setRadius(radius);
    }

    public MaterialCardView getBubbleView() {
        return cvMessageBubble;
    }

    public void setGradientBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble != null) {
            GradientDrawable gd = new GradientDrawable(orientation, colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }

    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble != null && bgColor != 0) {
            cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }

    public void setBoardUrl(String url) {
        if (url != null)
            this.url = url;
    }

    public void setStyle(CollaborativeBoardBubbleStyle style) {
        if (style != null) {
            setTitleColor(style.getTitleColor());
            setSubtitleColor(style.getSubtitleColor());
            setSeparatorColor(style.getSeparatorColor());
            setTitleTextAppearance(style.getTitleTextAppearance());
            setSubtitleTextAppearance(style.getSubtitleTextAppearance());
            setButtonTextColor(style.getButtonTextColor());
            backgroundColor(style.getBackground());
            cornerRadius(style.getCornerRadius());
            borderWidth(style.getBorderWidth());
            borderColor(style.getBorderColor());
        }
    }

    public void setSubTitle(String str) {
        if (subtitle != null) subtitle.setText(str);
    }

    public void setTitle(String str) {
        if (str != null) title.setText(str);
    }

    public void setTitleColor(@ColorInt int color) {
        if (title != null && color != 0) title.setTextColor(color);
    }

    public void setTitleFont(String font) {
        title.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setTitleTextAppearance(@StyleRes int appearance) {
        title.setTextAppearance(context, appearance);
    }

    public void setSubtitleColor(@ColorInt int color) {
        if (title != null && color != 0) title.setTextColor(color);
    }

    public void setSubtitleFont(String font) {
        title.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setSubtitleTextAppearance(@StyleRes int appearance) {
        title.setTextAppearance(context, appearance);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

}
