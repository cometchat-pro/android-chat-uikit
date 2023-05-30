package com.cometchat.chatuikit.calls.callmanger;

import android.content.Intent;
import android.os.Build;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.DisconnectCause;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.cometchat.chatuikit.calls.outgoingcall.CometChatCallActivity;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CallConnection extends Connection {

    MyConnectionService service;
    Call call;
    String TAG = "CallConnection";

    public CallConnection(MyConnectionService service, Call call) {
        this.service = service;
        this.call = call;
        Log.e(TAG, "CallConnection: "+call.toString());
    }


    @Override
    public void onCallAudioStateChanged(CallAudioState state) {
        Log.i(TAG, "onCallAudioStateChanged"+state);
    }


    @Override
    public void onDisconnect() {
        Log.i(TAG, "onDisconnect");
        super.onDisconnect();
        destroyConnection();
        Log.e(TAG,"onDisconnect");
        setDisconnected(new DisconnectCause(DisconnectCause.LOCAL, "Missed"));
        if (CometChat.getActiveCall()!=null)
            onDisconnect(CometChat.getActiveCall());
    }

    void onDisconnect(Call call) {
        CometChat.rejectCall(call.getSessionId(), CometChatConstants.CALL_STATUS_CANCELLED,
                new CometChat.CallbackListener<Call>() {
                    @Override
                    public void onSuccess(Call call) {
                        for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                            events.ccCallRejected(call);
                        }
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Toast.makeText(service,"Unable to end call due to ${p0?.code}",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void destroyConnection() {
        setDisconnected(new DisconnectCause(DisconnectCause.REMOTE, "Rejected"));
        Log.e(TAG, "destroyConnection" );
        super.destroy();
    }

    @Override
    public void onAnswer(int videoState) {
        Log.e(TAG, "onAnswerVideo: " );
        if (call.getSessionId()!=null) {
            CometChat.acceptCall(call.getSessionId(), new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                        events.ccCallAccepted(call);
                    }
                    Log.e(TAG, "onSuccess: accept");
                    service.sendBroadcast(getCallIntent("StartCall"));
                    destroyConnection();
                }

                @Override
                public void onError(CometChatException e) {
                    destroyConnection();
                    Toast.makeText(service, "Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onShowIncomingCallUi() {
        Log.e(TAG, "onShowIncomingCallUi: " );
    }

    @Override
    public void onAnswer() {
        Log.i(TAG, "onAnswer"+call.getSessionId());
        if (call.getSessionId()!=null) {
            CometChat.acceptCall(call.getSessionId(), new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                        events.ccCallAccepted(call);
                    }
                    Log.e(TAG, "onSuccess: accept");
                    service.sendBroadcast(getCallIntent("StartCall"));
                    destroyConnection();
                }

                @Override
                public void onError(CometChatException e) {
                    destroyConnection();
                    e.printStackTrace();
                }
            });
        }
    }


    @Override
    public void onHold() {
        Log.i(TAG, "onHold");
    }

    @Override
    public void onUnhold() {
        Log.i(TAG, "onUnhold");
    }

    @Override
    public void onReject() {
        Log.i(TAG, "onReject"+call.getSessionId());
        if (call.getSessionId()!=null) {
            CometChat.rejectCall(call.getSessionId(), CometChatConstants.CALL_STATUS_REJECTED, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                        events.ccCallRejected(call);
                    }
                    destroyConnection();
                    setDisconnected(new DisconnectCause(DisconnectCause.REJECTED,"Rejected"));
                }

                @Override
                public void onError(CometChatException e) {
                    destroyConnection();
                    Log.e(TAG, "onErrorReject: "+e.getMessage());
                }
            });
        }
    }

    public void onOutgoingReject() {
        Log.e(TAG,"onDisconnect");
        destroyConnection();
        setDisconnected(new DisconnectCause(DisconnectCause.REMOTE, "REJECTED"));
    }

    private Intent getCallIntent(String title){
        Intent callIntent = new Intent(service, CallNotificationAction.class);
        callIntent.putExtra(UIKitConstants.IntentStrings.SESSION_ID,call.getSessionId());
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setAction(title);
        return callIntent;
    }
}
