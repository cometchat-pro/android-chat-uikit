package com.cometchatworkspace.components.messages;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;

import java.util.HashMap;

public abstract class CometChatMessageEvents {
    public static final HashMap<String,CometChatMessageEvents> messageEvents = new HashMap();
    public void onMessageSent(BaseMessage baseMessage, @MessageStatus int status){}
    public void onMessageError(CometChatException e, BaseMessage message){}
    public void onMessageEdit(BaseMessage baseMessage, @MessageStatus int status) {}
    public void onMessageReply(BaseMessage baseMessage, @MessageStatus int status) {}
    public void onMessageForward(BaseMessage baseMessage, CometChatMessageList cometChatMessageList){}
    public void onMessageDelete(BaseMessage baseMessage, @MessageStatus int status){}
    public void onMessageReact(BaseMessage baseMessage, Reaction emoji){}
    public void onLiveReaction(String type, Reaction emoji) {}
    public void onStartThread(BaseMessage baseMessage,CometChatMessageList cometChatMessageList){}
    public void onMessageRead(BaseMessage baseMessage){}
    public void onScrollStateChanged() {}
    public void onVoiceCall(AppEntity user_or_group) {}
    public void onVideoCall(AppEntity user_or_group){}
    public void onViewInformation(AppEntity user_or_group){}
    public void onReplyMessagePrivately(BaseMessage baseMessage,CometChatMessageList cometChatMessageList){}
    public static void addListener(String TAG,CometChatMessageEvents events) {
        messageEvents.put(TAG,events);
    }
}