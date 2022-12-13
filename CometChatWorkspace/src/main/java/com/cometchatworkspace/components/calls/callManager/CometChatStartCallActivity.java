//package com.cometchatworkspace.components.calls.callManager;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.cometchat.pro.constants.CometChatConstants;
//import com.cometchat.pro.core.Call;
//import com.cometchat.pro.core.CallSettings;
//import com.cometchat.pro.core.CometChat;
//import com.cometchat.pro.exceptions.CometChatException;
//import com.cometchat.pro.models.AudioMode;
//import com.cometchat.pro.models.User;
//import com.cometchatworkspace.R;
//import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
//import com.cometchatworkspace.resources.utils.CometChatError;
//
//import com.cometchatworkspace.resources.constants.UIKitConstants;
//
//import java.util.List;
//
///**
// * CometChatStartCallActivity is activity class which is used to start a call. It takes sessionID
// * as a parameter and start call for particular sessionID.
// *
// * Created On - 22nd August 2020
// *
// * Modified On -  4th February 2022
// *
// * @author CometChat Team
// *
// * Copyright &copy; 2021 CometChat Inc.
// *
// */
//public class CometChatStartCallActivity extends AppCompatActivity {
//
//    /**
//     * Static variable to check the instance of CometChatStartCallActivity,
//     * It used to check if it's visible in current window or not.
//     */
//    public static CometChatStartCallActivity activity;
//
//    /**
//     * It is used to load the calling view in it.
//     */
//    private RelativeLayout mainView;
//
//    /**
//     * It is used to store unique session ID.
//     */
//    private String sessionID;
//
//    private String type;
//
//    private CallSettings callSettings;
//
//    private LinearLayout connectingLayout;
//
//
//    private String callType;
//
//    private boolean inPiPMode, hideControls;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//        setContentView(R.layout.activity_cometchat_start_call);
//
//        mainView = findViewById(R.id.call_view);
//        connectingLayout = findViewById(R.id.connecting_to_call);
//        sessionID = getIntent().getStringExtra(UIKitConstants.IntentStrings.SESSION_ID);
//        type = getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE);
//        callType = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_CALL_TYPE);
//
//        if (type!=null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
//            callSettings = new CallSettings.CallSettingsBuilder(this,mainView)
//                    .setSessionId(sessionID)
//                    .enableDefaultLayout(!hideControls)
//                    .setMode(CallSettings.MODE_SPOTLIGHT)
//                    .build();
//        else {
//            if (callType.equalsIgnoreCase(CometChatConstants.CALL_TYPE_AUDIO))
//                callSettings = new CallSettings.CallSettingsBuilder(this, mainView)
//                        .setSessionId(sessionID)
//                        .setAudioOnlyCall(true)
//                        .enableDefaultLayout(!hideControls)
//                        .build();
//            else
//                callSettings = new CallSettings.CallSettingsBuilder(this, mainView)
//                        .setSessionId(sessionID)
//                        .enableDefaultLayout(!hideControls)
//                        .build();
//        }
//
//        CometChatError.init(this);
//        CometChat.startCall(callSettings, new CometChat.OngoingCallListener() {
//
//            @Override
//            public void onRecordingStarted(User user) {
//
//            }
//
//            @Override
//            public void onRecordingStopped(User user) {
//
//            }
//
//            @Override
//            public void onUserMuted(User user, User user1) {
//
//            }
//
//            @Override
//            public void onCallSwitchedToVideo(String s, User user, User user1) {
//
//            }
//
//            @Override
//            public void onUserListUpdated(List<User> list) {
//            }
//
//            @Override
//            public void onAudioModesUpdated(List<AudioMode> list) {
//            }
//
//            @Override
//            public void onUserJoined(User user) {
//                connectingLayout.setVisibility(View.GONE);
//                CometChatSnackBar.show(CometChatStartCallActivity.this,
//                        mainView, getString(R.string.user_joined)+":"+ user.getName(),
//                        CometChatSnackBar.INFO);
//            }
//
//            @Override
//            public void onUserLeft(User user) {
//                if (user!=null) {
//                    CometChatSnackBar.show(CometChatStartCallActivity.this,
//                            mainView, getString(R.string.user_left)+":"+ user.getName(),
//                            CometChatSnackBar.INFO);
//                    if (callSettings.getMode().equals(CallSettings.CALL_MODE_DEFAULT)) {
//                        endCall();
//                    }
//                } else {
//                }
//            }
//
//            @Override
//            public void onError(CometChatException e) {
//                CometChatSnackBar.show(CometChatStartCallActivity.this,
//                        mainView,CometChatError.localized(e), CometChatSnackBar.ERROR);
//            }
//
//            @Override
//            public void onCallEnded(Call call) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (type!=null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
//            endCall();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    private void endCall() {
//        CometChat.endCall(sessionID, new CometChat.CallbackListener<Call>() {
//            @Override
//            public void onSuccess(Call call) {
//                finish();
//            }
//
//            @Override
//            public void onError(CometChatException e) {
//                CometChatSnackBar.show(CometChatStartCallActivity.this,
//                        mainView, CometChatError.localized(e), CometChatSnackBar.ERROR);
//            }
//        });
//    }
//}