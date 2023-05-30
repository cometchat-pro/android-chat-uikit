package com.cometchat.chatuikit.calls.callbutton;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CallButtonsViewModel extends ViewModel {
    private User user;
    private Group group;
    private String LISTENER_ID;
    private MutableLiveData<BaseMessage> startDirectCall;
    private String receiverType;
    private String receiverId;
    private MutableLiveData<Call> callInitiated;
    private MutableLiveData<Call> callRejected;
    private MutableLiveData<CometChatException> error;

    public CallButtonsViewModel() {
        startDirectCall = new MutableLiveData<>();
        callInitiated = new MutableLiveData<>();
        callRejected = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public MutableLiveData<Call> getCallInitiated() {
        return callInitiated;
    }

    public MutableLiveData<CometChatException> getError() {
        return error;
    }

    public MutableLiveData<BaseMessage> getStartDirectCall() {
        return startDirectCall;
    }

    public MutableLiveData<Call> getCallRejected() {
        return callRejected;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            receiverType = UIKitConstants.ReceiverType.USER;
            receiverId = user.getUid();
        }
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            receiverType = UIKitConstants.ReceiverType.GROUP;
            receiverId = group.getGuid();
        }
    }

    public void addListener() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChatCallEvents.addListener(LISTENER_ID, new CometChatCallEvents() {
            @Override
            public void ccCallRejected(Call call) {
                callRejected.setValue(call);
            }

            @Override
            public void ccCallEnded(Call call) {
                try {
                    callRejected.postValue(call);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public void removeListener() {
        CometChatCallEvents.removeListener(LISTENER_ID);
    }

    public void initiateCall(String callType) {
        if (CometChat.getActiveCall() == null) {
            if (receiverType.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                JSONObject customData = new JSONObject();
                try {
                    customData.put("callType", CometChatConstants.CALL_TYPE_VIDEO);
                    customData.put("sessionID", receiverId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomMessage customMessage = new CustomMessage(receiverId, CometChatConstants.RECEIVER_TYPE_GROUP, UIKitConstants.MessageType.MEETING, customData);
                customMessage.setConversationId("group_" + group.getGuid());
                customMessage.setReceiver(group);
                customMessage.setSentAt(System.currentTimeMillis() / 1000);
                customMessage.setMuid("" + System.currentTimeMillis());
                customMessage.setCategory(CometChatConstants.CATEGORY_CUSTOM);
                customMessage.setSender(CometChatUIKit.getLoggedInUser());
                JSONObject jsonObject = null;
                try {
                    jsonObject = customMessage.getMetadata();
                    if (jsonObject == null) {
                        jsonObject = new JSONObject();
                        jsonObject.put("incrementUnreadCount", true);
                        jsonObject.put("pushNotification", UIKitConstants.MessageType.MEETING);
                    } else {
                        jsonObject.accumulate("incrementUnreadCount", true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                customMessage.setMetadata(jsonObject);
                for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                    events.ccMessageSent(customMessage, MessageStatus.IN_PROGRESS);
                }
                CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
                    @Override
                    public void onSuccess(CustomMessage customMessage) {
                        startDirectCall.setValue(customMessage);
                        for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                            events.ccMessageSent(customMessage, MessageStatus.SUCCESS);
                        }
                    }

                    @Override
                    public void onError(CometChatException e) {
                        customMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                        for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                            events.ccMessageSent(customMessage, MessageStatus.ERROR);
                        }
                    }
                });
            } else {
                Call call = new Call(receiverId, CometChatConstants.RECEIVER_TYPE_USER, callType);
                CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
                    @Override
                    public void onSuccess(Call call) {
                        callInitiated.setValue(call);
                        for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                            events.ccOutgoingCall(call);
                        }
                    }

                    @Override
                    public void onError(CometChatException e) {
                        error.setValue(e);
                    }
                });
            }
        }
    }

}
