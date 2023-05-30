package com.cometchat.chatuikit.messages;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cometchat.chatuikit.shared.models.BaseStyle;


/**
 * The MessagesStyle class represents the visual style configuration for the Messages component in a chat UI kit.
 * <p>
 * It extends the BaseStyle class and provides additional methods to customize the background, corner radius,
 * <p>
 * border width, and border color of the Messages component.
 * <p>
 * in a concise and readable manner. Each style property can be set individually by calling the corresponding method,
 * <p>
 * and the updated MessagesStyle object is returned for further customization.
 * <p>
 * The MessagesStyle object can be used in conjunction with the Messages component to apply a specific visual style to
 * <p>
 * the chat interface. By customizing the background, border, and other style properties, developers can achieve a
 * <p>
 * consistent and visually appealing look for the Messages component that matches the overall theme of the application.
 * <p>
 * Note: The setActiveBackground method is not supported in MessagesStyle and will log a warning message when called.
 */
public class MessagesStyle extends BaseStyle {
    private final String TAG = MessagesStyle.class.getName();

    @Override
    public MessagesStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessagesStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public MessagesStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public MessagesStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessagesStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessagesStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }
}
