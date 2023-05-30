package com.cometchat.chatuikit.extensions.textmoderation;

import android.content.Context;
import android.view.View;

import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.extensions.Extensions;

public class TextModerationExtensionDecorator extends DataSourceDecorator {
    private TextBubbleStyle textBubbleStyle;

    public TextModerationExtensionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    public TextModerationExtensionDecorator(DataSource dataSource, TextBubbleStyle textBubbleStyle) {
        super(dataSource);
        this.textBubbleStyle = textBubbleStyle;
    }

    @Override
    public View getTextBubbleContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, int gravity, UIKitConstants.MessageBubbleAlignment alignment) {
        TextMessage message = textMessage;
        message.setText(getContentText(context, textMessage));
        if (textBubbleStyle == null) textBubbleStyle = bubbleStyle;
        return super.getTextBubbleContentView(context, message, textBubbleStyle, gravity, alignment);
    }

    public String getContentText(Context context, TextMessage textMessage) {
        String text = Extensions.checkProfanityMessage(context, textMessage);
        if (text.equalsIgnoreCase(textMessage.getText()))
            text = Extensions.checkDataMasking(context, textMessage);
        return text + "";
    }

    @Override
    public String getLastConversationMessage(Context context, Conversation conversation) {
        return getLastConversationMessage_(context, conversation);
    }

    public String getLastConversationMessage_(Context context, Conversation conversation) {
        String lastMessageText;
        BaseMessage baseMessage = conversation.getLastMessage();
        if (baseMessage != null) {
            String message = getLastMessage(context, baseMessage);
            if (message != null) {
                lastMessageText = message;
            } else lastMessageText = super.getLastConversationMessage(context, conversation);
            if (baseMessage.getDeletedAt() > 0) {
                lastMessageText = context.getString(R.string.this_message_deleted);
            }
        } else {
            lastMessageText = context.getResources().getString(R.string.tap_to_start_conversation);
        }
        return lastMessageText;
    }

    public String getLastMessage(Context context, BaseMessage lastMessage) {
        String message = null;
        if (UIKitConstants.MessageCategory.MESSAGE.equals(lastMessage.getCategory()) && UIKitConstants.MessageType.TEXT.equalsIgnoreCase(lastMessage.getType()))
            message = Utils.getMessagePrefix(lastMessage, context) + getContentText(context, (TextMessage) lastMessage);
        return message;
    }

    @Override
    public String getId() {
        return TextModerationExtensionDecorator.class.getName();
    }


}
