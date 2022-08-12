package com.cometchatworkspace.components.calls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cometchat.pro.core.Call;

import com.cometchatworkspace.components.calls.callManager.CometChatStartCallActivity;
import com.cometchatworkspace.resources.constants.UIKitConstants;

/**
 * Purpose - This class contains all the static methods which are useful for hanlde calling in UIKit.
 * Developers can use these method to intiate,join or start call.
 *
 * Created On - 7th October 2020
 *
 * Modified On - 4th February 2022
 *
 * @author CometChat Team
 * Copyright &copy; 2021 CometChat Inc.
 */
public class CallUtils {

    private static final String TAG = "CallUtils";

    /**
     * This method is used to load <code>CometChatStartCallActivity</code> which starts a call in
     * seperate screen
     * @param context is object of Context
     * @param call is object of Call
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public static void startCall(Context context, Call call) {
        Intent intent = new Intent(context, CometChatStartCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(UIKitConstants.IntentStrings.TYPE,call.getReceiverType());
        intent.putExtra(UIKitConstants.IntentStrings.SESSION_ID,call.getSessionId());
        ((Activity)context).finish();
        intent.putExtra(UIKitConstants.IntentStrings.IS_DEFAULT_CALL,true);
        context.startActivity(intent);
    }

    /**
     * This method is used to load <code>CometChatStartCallActivity</code> whenever a group call is
     * initiated.
     * @param context is object of Context
     * @param call is object of Call
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */

    public static void startDirectCall(Context context, Call call) {
        Intent intent = new Intent(context, CometChatStartCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(UIKitConstants.IntentStrings.TYPE,call.getReceiverType());
        intent.putExtra(UIKitConstants.IntentStrings.SESSION_ID,call.getSessionId());
        intent.putExtra(UIKitConstants.IntentStrings.GROUP_CALL_TYPE,call.getType());
        intent.putExtra(UIKitConstants.IntentStrings.IS_DEFAULT_CALL,false);
        context.startActivity(intent);
    }

    /**
     * This method is used to join an ongoing call.
     * @param context
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public static void joinOnGoingCall(Context context,Call call) {
        Intent intent = new Intent(context, CometChatStartCallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(UIKitConstants.IntentStrings.TYPE,call.getReceiverType());
        intent.putExtra(UIKitConstants.IntentStrings.SESSION_ID,call.getSessionId());
        context.startActivity(intent);
    }

}
