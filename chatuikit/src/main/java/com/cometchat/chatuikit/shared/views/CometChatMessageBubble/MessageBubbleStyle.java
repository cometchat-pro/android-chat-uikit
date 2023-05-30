package com.cometchat.chatuikit.shared.views.CometChatMessageBubble;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The MessageBubbleStyle class is a subclass of the BaseStyle class and represents the style for a message bubble in a chat interface.
 * It provides methods to customize the background, corner radius, border width, and border color of the message bubble.
 */
public class MessageBubbleStyle extends BaseStyle {
    private final String TAG = MessageBubbleStyle.class.getName();

    @Override
    public MessageBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessageBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public MessageBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public MessageBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessageBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessageBubbleStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }
}
