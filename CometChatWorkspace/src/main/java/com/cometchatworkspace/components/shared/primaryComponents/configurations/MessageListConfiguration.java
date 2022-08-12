package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageInputData;

public class MessageListConfiguration extends CometChatConfigurations {
    private boolean isDeleteMessageHidden;
    private @CometChatMessageList.MessageListAlignment String messageListAlignment;


    public MessageListConfiguration setDeleteMessageHidden(boolean isHidden) {
        isDeleteMessageHidden = isHidden;
        return this;
    }

    public boolean isDeleteMessageHidden() {
        return isDeleteMessageHidden;
    }

    public MessageListConfiguration setMessageAlignment(@CometChatMessageList.MessageListAlignment String alignment) {
        messageListAlignment = alignment;
        return this;
    }

    public String getMessageListAlignment() {
        return messageListAlignment;
    }

}
