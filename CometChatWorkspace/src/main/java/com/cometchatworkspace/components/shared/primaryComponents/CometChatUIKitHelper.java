package com.cometchatworkspace.components.shared.primaryComponents;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.components.messages.CometChatMessageEvents;
import com.cometchatworkspace.components.messages.MessageStatus;
import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;

public class CometChatUIKitHelper {

    public static void onMessageSent(BaseMessage baseMessage, @MessageStatus int status) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageSent(baseMessage,status);
        }
    }

    public static void onMessageError(CometChatException e) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageError(e);
        }
    }

    public static void onMessageEdit(BaseMessage baseMessage, @MessageStatus int status) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageSent(baseMessage,status);
        }
    }

    public static void onMessageDelete(BaseMessage baseMessage) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageDelete(baseMessage);
        }
    }

    public static void onMessageReply(BaseMessage baseMessage, @MessageStatus int status) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageReply(baseMessage,status);
        }
    }


    public static void onMessageReact(BaseMessage baseMessage, Reaction emoji) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageReact(baseMessage,emoji);
        }
    }

    public static void onMessageRead(BaseMessage baseMessage) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onMessageRead(baseMessage);
        }
    }

    public static void onLiveReaction(String type, Reaction emoji) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.onLiveReaction(type,emoji);
        }
    }


}
