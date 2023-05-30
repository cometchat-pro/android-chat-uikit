package com.cometchat.chatuikit.calls.outgoingcall;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;

import org.json.JSONException;

public class CometChatCallActivity extends AppCompatActivity {
    private CometChatOutgoingCall outgoingCall;
    private static BaseMessage baseMessage;
    private static Call call;
    private static String callType;
    private static User user;
    private static String sessionId;
    private String LISTENER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LISTENER_ID = System.currentTimeMillis() + "";
        setContentView(R.layout.activity_comet_chat_call);
        outgoingCall = findViewById(R.id.outgoing_call);
        if (user != null && call != null) {
            outgoingCall.setUser(user);
            outgoingCall.setCall(call);
        } else if (baseMessage instanceof CustomMessage && baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.MEETING)) {
            CustomMessage customMessage = (CustomMessage) baseMessage;
            try {
                String id = customMessage.getCustomData().getString("sessionID");
                outgoingCall.launchOnGoingScreen(id, UIKitConstants.ReceiverType.GROUP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (sessionId != null && !sessionId.isEmpty() && callType != null && !callType.isEmpty()) {
            outgoingCall.launchOnGoingScreen(sessionId, UIKitConstants.ReceiverType.GROUP);
        }
    }

    public static void launch(Context context, Call call_, User user_, String callType_) {
        baseMessage = null;
        call = call_;
        sessionId=null;
        user = user_;
        callType = callType_;
        context.startActivity(new Intent(context, CometChatCallActivity.class));
    }

    public static void launch(Context context, BaseMessage baseMessage_, String callType_) {
        call = null;
        user = null;
        sessionId=null;
        baseMessage = baseMessage_;
        callType = callType_;
        context.startActivity(new Intent(context, CometChatCallActivity.class));
    }

    public static void launch(Context context, String sessionId_, String type) {
        callType = type;
        sessionId = sessionId_;
        user=null;
        baseMessage=null;
        call=null;
        Intent intent = new Intent(context, CometChatCallActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseMessage=null;
        call=null;
        callType=null;
        user=null;
        sessionId=null;
    }
}