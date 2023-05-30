package com.cometchat.chatuikit.extensions.reaction;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.extensions.reaction.emojikeyboard.EmojiKeyboardStyle;

public class ReactionExtension implements ExtensionsDataSource {
    private MessageReactionsStyle messageReactionsStyle;
    private EmojiKeyboardStyle emojiKeyboardStyle;

    public ReactionExtension(MessageReactionsStyle messageReactionsStyle, EmojiKeyboardStyle emojiKeyboardStyle) {
        this.messageReactionsStyle = messageReactionsStyle;
        this.emojiKeyboardStyle = emojiKeyboardStyle;
    }

    public ReactionExtension() {
    }

    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new ReactionExtensionDecorator(messageReactionsStyle, emojiKeyboardStyle, var1));
    }
}
