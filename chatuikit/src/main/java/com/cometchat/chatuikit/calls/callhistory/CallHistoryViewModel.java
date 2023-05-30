package com.cometchat.chatuikit.calls.callhistory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

 class CallHistoryViewModel extends ViewModel {

    public MessagesRequest.MessagesRequestBuilder messagesRequestBuilder;

    private MessagesRequest messagesRequest;

    public String LISTENERS_TAG;

    public MutableLiveData<List<BaseMessage>> mutableCallsList;

    public MutableLiveData<Integer> insertAtTop;

    public MutableLiveData<Integer> moveToTop;

    public List<BaseMessage> callsArrayList;

    public MutableLiveData<Integer> updateCall;

    public MutableLiveData<Integer> removeCall;

    public MutableLiveData<CometChatException> cometChatException;

    public MutableLiveData<UIKitConstants.States> states;

    public int limit = 30;

    public boolean hasMore = true;

    public String LISTENER_ID;

    public CallHistoryViewModel() {
        mutableCallsList = new MutableLiveData<>();
        insertAtTop = new MutableLiveData<>();
        moveToTop = new MutableLiveData<>();
        callsArrayList = new ArrayList<>();
        updateCall = new MutableLiveData<>();
        removeCall = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        states = new MutableLiveData<>();
        messagesRequestBuilder = new MessagesRequest.MessagesRequestBuilder().setCategories(Arrays.asList(UIKitConstants.MessageCategory.CALL, UIKitConstants.MessageCategory.CUSTOM)).setTypes(Arrays.asList(UIKitConstants.MessageType.MEETING, CometChatConstants.CALL_TYPE_AUDIO, CometChatConstants.CALL_TYPE_VIDEO)).setLimit(limit);
        messagesRequest = messagesRequestBuilder.build();
    }

    public void addListeners() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChatCallEvents.addListener(LISTENER_ID, new CometChatCallEvents() {
            @Override
            public void ccOutgoingCall(Call call) {
                addToTop(call);
            }

            @Override
            public void ccCallAccepted(Call call) {
                addToTop(call);
            }

            @Override
            public void ccCallRejected(Call call) {
                addToTop(call);
            }

            @Override
            public void ccCallEnded(Call call) {
                addToTop(call);
            }

            @Override
            public void ccMessageSent(BaseMessage message, int status) {
                if (status == MessageStatus.SUCCESS) addToTop(message);
            }
        });
        CometChat.addCallListener(LISTENER_ID, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {
                addToTop(call);
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {
                addToTop(call);
            }

            @Override
            public void onOutgoingCallRejected(Call call) {
                addToTop(call);
            }

            @Override
            public void onIncomingCallCancelled(Call call) {
                addToTop(call);
            }
        });
    }

    public void removeListeners() {
        CometChatCallEvents.removeListener(LISTENER_ID);
        CometChat.removeCallListener(LISTENER_ID);
    }

    public MutableLiveData<List<BaseMessage>> getMutableCallsList() {
        return mutableCallsList;
    }

    public MutableLiveData<Integer> insertAtTop() {
        return insertAtTop;
    }

    public MutableLiveData<Integer> moveToTop() {
        return moveToTop;
    }

    public List<BaseMessage> getCallsArrayList() {
        return callsArrayList;
    }

    public MutableLiveData<Integer> updateCall() {
        return updateCall;
    }

    public MutableLiveData<Integer> removeCall() {
        return removeCall;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public void updateCall(BaseMessage call) {
        if (callsArrayList.contains(call)) {
            callsArrayList.set(callsArrayList.indexOf(call), call);
            updateCall.setValue(callsArrayList.indexOf(call));
        }
    }

    public void moveToTop(BaseMessage call) {
        if (callsArrayList.contains(call)) {
            int oldIndex = callsArrayList.indexOf(call);
            callsArrayList.remove(call);
            callsArrayList.add(0, call);
            moveToTop.setValue(oldIndex);
        }
    }

    public void addToTop(BaseMessage call) {
        if (call != null && !callsArrayList.contains(call)) {
            callsArrayList.add(0, call);
            insertAtTop.setValue(0);
        }
    }

    public void removeCall(BaseMessage call) {
        if (callsArrayList.contains(call)) {
            int index = callsArrayList.indexOf(call);
            this.callsArrayList.remove(call);
            removeCall.setValue(index);
            states.setValue(checkIsEmpty(callsArrayList));
        }
    }

    public void fetchCalls() {
        if (callsArrayList.size() == 0) states.setValue(UIKitConstants.States.LOADING);
        if (hasMore) {
            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> calls) {
                    hasMore = calls.size() != 0;
                    Collections.reverse(calls);
                    if (hasMore) addList(calls);
                    states.setValue(UIKitConstants.States.LOADED);
                    states.setValue(checkIsEmpty(callsArrayList));
                }

                @Override
                public void onError(CometChatException e) {
                    cometChatException.setValue(e);
                    states.setValue(UIKitConstants.States.ERROR);
                }
            });
        }
    }

    public void addList(List<BaseMessage> callList) {
        for (BaseMessage call : callList) {
            if (callsArrayList.contains(call)) {
                int index = callsArrayList.indexOf(call);
                callsArrayList.remove(index);
                callsArrayList.add(index, call);
            } else {
                callsArrayList.add(call);
            }
        }
        mutableCallsList.setValue(callsArrayList);
    }

    private UIKitConstants.States checkIsEmpty(List<BaseMessage> calls) {
        if (calls.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

    public void setMessagesRequestBuilder(MessagesRequest.MessagesRequestBuilder callsRequest) {
        if (callsRequest != null) {
            this.messagesRequestBuilder = callsRequest;
            this.messagesRequest = messagesRequestBuilder.build();
        }
    }

    public void clear() {
        callsArrayList.clear();
        mutableCallsList.setValue(callsArrayList);
    }

}
