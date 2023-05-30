package com.cometchat.chatuikit.extensions.textmoderation;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;

public class TextModerationExtension implements ExtensionsDataSource {
    private TextBubbleStyle textBubbleStyle;

    public TextModerationExtension(TextBubbleStyle textBubbleStyle) {
        this.textBubbleStyle = textBubbleStyle;
    }

    public TextModerationExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new TextModerationExtensionDecorator(var1, textBubbleStyle));
    }

}
