package com.cometchat.chatuikit.calls.callmanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.chatuikit.calls.outgoingcall.CometChatCallActivity;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

public class CallNotificationAction extends BroadcastReceiver {

    String TAG = "CallNotificationAction";

    @Override
    public void onReceive(Context context, Intent intent) {
        String sessionID = intent.getStringExtra(UIKitConstants.IntentStrings.SESSION_ID);
        Log.e(TAG, "onReceive: " + intent.getStringExtra(UIKitConstants.IntentStrings.SESSION_ID));
        if (intent.getAction().equals("Answer_")) {
            CometChat.acceptCall(sessionID, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                        events.ccCallAccepted(call);
                    }
                    CometChatCallActivity.launch(context, sessionID, UIKitConstants.ReceiverType.USER);
                }

                @Override
                public void onError(CometChatException e) {
                    e.printStackTrace();
                }
            });
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(UIKitConstants.Notification.ID);
        } else if (intent.getAction().equals("Decline_")) {
            CometChat.rejectCall(sessionID, CometChatConstants.CALL_STATUS_REJECTED, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                        events.ccCallRejected(call);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    e.printStackTrace();
                }
            });
        } else if (intent.getAction().equals("StartCall")) {
            CometChatCallActivity.launch(context, sessionID, UIKitConstants.ReceiverType.USER);
        }
    }
}
