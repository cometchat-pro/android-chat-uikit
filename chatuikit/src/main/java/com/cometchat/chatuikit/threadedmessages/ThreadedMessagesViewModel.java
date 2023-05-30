package com.cometchat.chatuikit.threadedmessages;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;

public class ThreadedMessagesViewModel extends ViewModel {

    private String LISTENER_ID;
    public MutableLiveData<BaseMessage> sentMessage;
    public MutableLiveData<BaseMessage> receiveMessage;
    public MutableLiveData<BaseMessage> updateParentMessage;
    public BaseMessage parentMessage;

    public ThreadedMessagesViewModel() {
        sentMessage = new MutableLiveData<>();
        receiveMessage = new MutableLiveData<>();
        updateParentMessage = new MutableLiveData<>();
    }

    public MutableLiveData<BaseMessage> getSentMessage() {
        return sentMessage;
    }

    public MutableLiveData<BaseMessage> getReceiveMessage() {
        return receiveMessage;
    }

    public MutableLiveData<BaseMessage> getUpdateParentMessage() {
        return updateParentMessage;
    }

    public void setParentMessage(BaseMessage parentMessage) {
        this.parentMessage = parentMessage;
    }

    public void addListener() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChatMessageEvents.addListener(LISTENER_ID, new CometChatMessageEvents() {
            @Override
            public void ccMessageSent(BaseMessage baseMessage, int status) {
                if (MessageStatus.IN_PROGRESS == status && baseMessage != null && parentMessage != null && parentMessage.getId() == baseMessage.getParentMessageId()) {
                    sentMessage.setValue(baseMessage);
                }
            }

            @Override
            public void ccMessageEdited(BaseMessage baseMessage, int status) {
                if (parentMessage.getId() == baseMessage.getId()) {
                    parentMessage = baseMessage;
                    updateParentMessage.setValue(baseMessage);
                }
            }

            @Override
            public void ccMessageDeleted(BaseMessage baseMessage) {
                if (parentMessage.getId() == baseMessage.getId()) {
                    parentMessage = baseMessage;
                    updateParentMessage.setValue(baseMessage);
                }
            }
        });

        CometChat.addMessageListener(LISTENER_ID, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage textMessage) {
                receivedMessage(textMessage);
            }

            @Override
            public void onMediaMessageReceived(MediaMessage mediaMessage) {
                receivedMessage(mediaMessage);
            }

            @Override
            public void onCustomMessageReceived(CustomMessage customMessage) {
                receivedMessage(customMessage);
            }

            @Override
            public void onMessageEdited(BaseMessage baseMessage) {
                if (parentMessage.getId() == baseMessage.getId()) {
                    parentMessage = baseMessage;
                    updateParentMessage.setValue(baseMessage);
                }
            }

            @Override
            public void onMessageDeleted(BaseMessage baseMessage) {
                if (parentMessage.getId() == baseMessage.getId()) {
                    parentMessage = baseMessage;
                    updateParentMessage.setValue(baseMessage);
                }
            }
        });
    }

    public void removeListener() {
        CometChatMessageEvents.removeListener(LISTENER_ID);
        CometChat.removeMessageListener(LISTENER_ID);
    }

    public void receivedMessage(BaseMessage baseMessage) {
        if (baseMessage != null && parentMessage != null && parentMessage.getId() == baseMessage.getParentMessageId()) {
            receiveMessage.setValue(baseMessage);
        }
    }
}
