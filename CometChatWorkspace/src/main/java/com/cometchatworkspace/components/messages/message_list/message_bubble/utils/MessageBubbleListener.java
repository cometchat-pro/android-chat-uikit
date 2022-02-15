package com.cometchatworkspace.components.messages.message_list.message_bubble.utils;

import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;

public interface MessageBubbleListener {
    void onLongCLick(BaseMessage baseMessage);
    void onClick(BaseMessage baseMessage);
    void onReactionClick(Reaction reaction, int baseMessageID);
}
