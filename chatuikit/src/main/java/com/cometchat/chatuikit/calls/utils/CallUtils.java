package com.cometchat.chatuikit.calls.utils;

import android.content.Context;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import org.json.JSONException;

public class CallUtils {

    public static String getCallerName(Call call) {
        String name;
        if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (((User) call.getCallInitiator()).getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                name = ((User) call.getCallReceiver()).getName();
            } else {
                name = ((User) call.getCallInitiator()).getName();
            }
        } else {
            name = ((Group) call.getCallReceiver()).getName();
        }
        return name;
    }

    public static User getCallingUser(Call call) {
        User user = null;
        if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (((User) call.getCallInitiator()).getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                user = ((User) call.getCallReceiver());
            } else {
                user = ((User) call.getCallInitiator());
            }
        }
        return user;
    }

    public static Group getCallingGroup(Call call) {
        Group group = null;
        if (call.getReceiverType().equals(UIKitConstants.ReceiverType.GROUP)) {
            group = ((Group) call.getCallReceiver());
        }
        return group;
    }

    public static Group getCallingGroup(CustomMessage customMessage) {
        Group group = null;
        if (customMessage != null && UIKitConstants.MessageType.MEETING.equalsIgnoreCase(customMessage.getType())) {
            group = (Group) customMessage.getReceiver();
        }
        return group;
    }


    public static String getCallerImage(Call call) {
        String avatar;
        if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (((User) call.getCallInitiator()).getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                avatar = ((User) call.getCallReceiver()).getAvatar();
            } else {
                avatar = ((User) call.getCallInitiator()).getAvatar();
            }
        } else {
            avatar = ((Group) call.getCallReceiver()).getIcon();
        }
        return avatar;
    }

    public static String getCallStatus(Context context, BaseMessage baseMessage) {
        String callMessageText = "";
        if (baseMessage instanceof Call && baseMessage.getReceiverType().equals(UIKitConstants.ReceiverType.USER)) {
            Call call = (Call) baseMessage;
            User initiator = (User) call.getCallInitiator();
            if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.INITIATED)) {
                if (!Utils.isLoggedInUser(initiator))
                    callMessageText = context.getString(R.string.incoming) + " " + context.getString(R.string.call);
                else
                    callMessageText = context.getString(R.string.outgoing) + " " + context.getString(R.string.call);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.ONGOING)) {
                callMessageText = context.getString(R.string.call) + " " + context.getString(R.string.accepted);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.ENDED)) {
                callMessageText = context.getString(R.string.call) + " " + context.getString(R.string.ended);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.UNANSWERED)) {
                if (Utils.isLoggedInUser(initiator))
                    callMessageText = context.getString(R.string.call) + " " + context.getString(R.string.unanswered);
                else
                    callMessageText = context.getString(R.string.missed_call) + " " + context.getString(R.string.call);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.CANCELLED)) {
                if (Utils.isLoggedInUser(initiator))
                    callMessageText = context.getString(R.string.call) + " " + context.getString(R.string.cancel_call);
                else
                    callMessageText = context.getString(R.string.missed_call) + " " + context.getString(R.string.call);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.REJECTED)) {
                if (Utils.isLoggedInUser(initiator))
                    callMessageText = context.getString(R.string.call) + " " + context.getString(R.string.rejected_call);
                else
                    callMessageText = context.getString(R.string.missed_call) + " " + context.getString(R.string.call);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.BUSY)) {
                if (Utils.isLoggedInUser(initiator))
                    callMessageText = context.getString(R.string.call) + " " + context.getString(R.string.rejected_call);
                else
                    callMessageText = context.getString(R.string.missed_call) + " " + context.getString(R.string.call);
            }
        }
        return " " + callMessageText;
    }

    public static boolean isVideoCall(Call call) {
        return call.getType().equals(CometChatConstants.CALL_TYPE_VIDEO);
    }

    public static boolean isMissedCall(Call call) {
        if (call.getReceiverType().equals(UIKitConstants.ReceiverType.USER)) {
            User initiator = (User) call.getCallInitiator();
            if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.UNANSWERED)) {
                return !Utils.isLoggedInUser(initiator);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.CANCELLED)) {
                return !Utils.isLoggedInUser(initiator);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.REJECTED)) {
                return !Utils.isLoggedInUser(initiator);
            } else if (call.getCallStatus().equals(UIKitConstants.CallStatusConstants.BUSY)) {
                return !Utils.isLoggedInUser(initiator);
            }
        }
        return false;
    }

    public static boolean isVideoCall(CustomMessage customMessage) {
        if (customMessage.getCustomData() != null) {
            if (customMessage.getCustomData().has("callType")) {
                try {
                    if (customMessage.getCustomData().getString("callType").equalsIgnoreCase("video"))
                        return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String getCallerName(CustomMessage customMessage) {
        String name = null;
        if (customMessage != null) {
            Group group = (Group) customMessage.getReceiver();
            name = group.getName();
        }
        return name;
    }

    public static String getCallerImage(CustomMessage customMessage) {
        String image = null;
        if (customMessage != null) {
            Group group = (Group) customMessage.getReceiver();
            image = group.getIcon();
        }
        return image;
    }

}
