package com.cometchat.chatuikit.calls.ongoingcall;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CallSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.AudioMode;
import com.cometchat.pro.models.User;

import java.util.List;

public class OngoingCallViewModel extends ViewModel {

    private MutableLiveData<Call> endCall;
    private MutableLiveData<CometChatException> exception;
    private CallSettings.CallSettingsBuilder callSettingsBuilder;
    private CallSettings callSettings;
    private String sessionId;
    private boolean isEnded = false;

    public OngoingCallViewModel() {
        endCall = new MutableLiveData<>();
        exception = new MutableLiveData<>();
    }

    public void setCallSettingsBuilder(CallSettings.CallSettingsBuilder builder) {
        if (builder != null) {
            this.callSettingsBuilder = builder;
            callSettings = callSettingsBuilder.build();
        }
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public void startCall() {
        if (callSettings != null) {
            callSettings = callSettingsBuilder.setSessionId(sessionId).build();
            CometChat.startCall(callSettings, new CometChat.OngoingCallListener() {
                @Override
                public void onRecordingStarted(User user) {

                }

                @Override
                public void onRecordingStopped(User user) {

                }

                @Override
                public void onUserMuted(User user, User user1) {

                }

                @Override
                public void onCallSwitchedToVideo(String s, User user, User user1) {

                }

                @Override
                public void onUserListUpdated(List<User> list) {
                }

                @Override
                public void onAudioModesUpdated(List<AudioMode> list) {
                }

                @Override
                public void onUserJoined(User user) {

                }

                @Override
                public void onUserLeft(User user) {
                    if (user != null) {
                        if (callSettings.getMode().equals(CallSettings.MODE_SINGLE)) {
                            endCall();
                        }
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    exception.postValue(e);
                }

                @Override
                public void onCallEnded(Call call) {
                    if (!isEnded) {
                        for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                            events.ccCallEnded(call);
                        }
                        endCall.postValue(call);
                        isEnded = true;
                    }
                }
            });
        }

    }


    public MutableLiveData<Call> getEndCall() {
        return endCall;
    }

    public MutableLiveData<CometChatException> getException() {
        return exception;
    }

    public void endCall() {
        if (callSettings != null) {
            CometChat.endCall(callSettings.getSessionId(), new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    endCall.setValue(call);
                }

                @Override
                public void onError(CometChatException e) {
                    exception.setValue(e);
                }
            });
        }
    }

}
