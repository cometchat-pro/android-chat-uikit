package com.cometchat.chatuikit.calls.outgoingcall;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

public class OutgoingViewModel extends ViewModel {
    private MutableLiveData<Call> rejectCall;
    private MutableLiveData<Call> acceptedCall;
    private MutableLiveData<CometChatException> exception;
    private String LISTENER_ID;

    public OutgoingViewModel() {
        rejectCall = new MutableLiveData<>();
        acceptedCall = new MutableLiveData<>();
        exception = new MutableLiveData<>();
    }

    public MutableLiveData<Call> getRejectCall() {
        return rejectCall;
    }

    public MutableLiveData<Call> getAcceptedCall() {
        return acceptedCall;
    }

    public MutableLiveData<CometChatException> getException() {
        return exception;
    }

    public void rejectCall(String sessionId) {
        CometChat.rejectCall(sessionId, CometChatConstants.CALL_STATUS_CANCELLED, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                for (CometChatCallEvents callEvents : CometChatCallEvents.callingEvents.values()) {
                    callEvents.ccCallRejected(call);
                }

                rejectCall.setValue(call);
            }

            @Override
            public void onError(CometChatException e) {
                exception.setValue(e);
            }
        });
    }

    public void addListeners() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChat.addCallListener(LISTENER_ID, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {
                acceptedCall.setValue(call);
            }

            @Override
            public void onOutgoingCallRejected(Call call) {
                rejectCall.setValue(call);
            }

            @Override
            public void onIncomingCallCancelled(Call call) {

            }
        });
    }

    public void removeListeners() {
        CometChat.removeCallListener(LISTENER_ID);
    }

}
