package com.cometchat.chatuikit.extensions.sticker.bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.cometchat.chatuikit.R;
import com.google.android.material.imageview.ShapeableImageView;

public class CometChatStickerBubble extends RelativeLayout {

    private Context context;
    private View view;
    private ShapeableImageView imageMessage;

    public CometChatStickerBubble(Context context) {
        super(context);
        initComponent(context, null);
    }

    public CometChatStickerBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs);
    }

    public CometChatStickerBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs);
    }

    public CometChatStickerBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context, attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.StickerMessageBubble, 0, 0);
        float cornerRadius = a.getFloat(R.styleable.StickerMessageBubble_corner_radius, 12);
        int backgroundColor = a.getColor(R.styleable.StickerMessageBubble_backgroundColor, getResources().getColor(android.R.color.transparent));
        Drawable imageDrawable = a.getDrawable(R.styleable.StickerMessageBubble_stickerDrawable);
        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_sticker_bubble, null);
        initView(view);
        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        if (imageDrawable != null) imageMessage.setImageDrawable(imageDrawable);
    }

    private void initView(View view) {
        addView(view);
        imageMessage = view.findViewById(R.id.image_message);
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (imageMessage != null)
            imageMessage.setShapeAppearanceModel(imageMessage.getShapeAppearanceModel().toBuilder().setBottomLeftCornerSize(bottomLeft).setBottomRightCornerSize(bottomRight).setTopLeftCornerSize(topLeft).setTopRightCornerSize(topRight).build());
    }

    public void cornerRadius(float radius) {
        if (imageMessage != null)
            imageMessage.setShapeAppearanceModel(imageMessage.getShapeAppearanceModel().toBuilder().setBottomLeftCornerSize(radius).setBottomRightCornerSize(radius).setTopLeftCornerSize(radius).setTopRightCornerSize(radius).build());
    }

    public View getBubbleView() {
        return imageMessage;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (imageMessage != null) {
            GradientDrawable gd = new GradientDrawable(orientation, colorArray);
            imageMessage.setBackgroundDrawable(gd);
        }
    }

    public void backgroundColor(@ColorInt int bgColor) {
        if (imageMessage != null) {
            if (bgColor != 0) imageMessage.setBackgroundColor(bgColor);
        }
    }

    public void setDrawable(Drawable drawable) {
        if (imageMessage != null) imageMessage.setImageDrawable(drawable);
    }

    public void setImageUrl(String url) {
        if (imageMessage != null && url != null) {
            Glide.with(context).load(url).into(imageMessage);
        }
    }

    public void setPadding(int padding) {
        if (imageMessage != null) {
            imageMessage.setPadding(padding, padding, padding, padding);
        }
    }

    public void setPadding(int left, int right, int top, int bottom) {
        if (imageMessage != null) imageMessage.setPadding(left, top, right, bottom);
    }
}
