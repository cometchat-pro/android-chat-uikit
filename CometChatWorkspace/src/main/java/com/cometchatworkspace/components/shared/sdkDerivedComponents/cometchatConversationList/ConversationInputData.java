package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList;

import com.cometchat.pro.models.Conversation;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;

import java.util.function.Function;

public class ConversationInputData extends InputData {
    private boolean unreadCount;
    private boolean readReceipt;
    private boolean timestamp;

    public ConversationInputData(boolean unreadCount, boolean readReceipt, boolean timestamp, boolean thumbnail, boolean status, boolean title, Function<Conversation,Object> subTitle) {
        super(thumbnail, status, title, subTitle);
        this.unreadCount=unreadCount;
        this.readReceipt=readReceipt;
        this.timestamp=timestamp;

    }

    public ConversationInputData(boolean unreadCount, boolean readReceipt, boolean timestamp, boolean thumbnail, boolean status, boolean title, String metaData) {
        super(thumbnail, status, title, metaData);
        this.unreadCount=unreadCount;
        this.readReceipt=readReceipt;
        this.timestamp=timestamp;
    }

    public ConversationInputData(boolean unreadCount, boolean readReceipt, boolean timestamp){
        super(false,false,false,"");
        this.unreadCount=unreadCount;
        this.readReceipt=readReceipt;
        this.timestamp=timestamp;
    }

    public boolean isUnreadCount() {
        return unreadCount;
    }

    public boolean isReadReceipt() {
        return readReceipt;
    }

    public boolean isTimestamp() {
        return timestamp;
    }
}
