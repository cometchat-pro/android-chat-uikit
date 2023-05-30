package com.cometchat.chatuikit.extensions.smartreplies.view;

import androidx.annotation.DrawableRes;

public class SmartRepliesConfiguration {
    private @DrawableRes
    int closeButtonIcon = 0;

    private CometChatSmartReplies.onClose onClose = null;

    private CometChatSmartReplies.onClick onClick = null;

    private SmartRepliesStyle style;

    public SmartRepliesConfiguration setCloseButtonIcon(int closeButtonIcon) {
        this.closeButtonIcon = closeButtonIcon;
        return this;
    }

    public SmartRepliesConfiguration setOnClose(CometChatSmartReplies.onClose onClose) {
        this.onClose = onClose;
        return this;
    }

    public SmartRepliesConfiguration setOnClick(CometChatSmartReplies.onClick onClick) {
        this.onClick = onClick;
        return this;
    }

    public SmartRepliesConfiguration setStyle(SmartRepliesStyle style) {
        this.style = style;
        return this;
    }

    public int getCloseButtonIcon() {
        return closeButtonIcon;
    }

    public CometChatSmartReplies.onClose getOnClose() {
        return onClose;
    }

    public CometChatSmartReplies.onClick getOnClick() {
        return onClick;
    }

    public SmartRepliesStyle getStyle() {
        return style;
    }
}
