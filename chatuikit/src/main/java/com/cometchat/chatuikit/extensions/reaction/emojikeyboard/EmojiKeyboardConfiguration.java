package com.cometchat.chatuikit.extensions.reaction.emojikeyboard;

public class EmojiKeyboardConfiguration {
    private CometChatEmojiKeyboard.onClick onClick;

    private EmojiKeyboardStyle emojiKeyboardStyle;

    public EmojiKeyboardConfiguration setOnClick(CometChatEmojiKeyboard.onClick onClick) {
        this.onClick = onClick;
        return this;
    }

    public EmojiKeyboardConfiguration setEmojiKeyboardStyle(EmojiKeyboardStyle emojiKeyboardStyle) {
        this.emojiKeyboardStyle = emojiKeyboardStyle;
        return this;
    }

    public CometChatEmojiKeyboard.onClick getOnClick() {
        return onClick;
    }

    public EmojiKeyboardStyle getEmojiKeyboardStyle() {
        return emojiKeyboardStyle;
    }
}
