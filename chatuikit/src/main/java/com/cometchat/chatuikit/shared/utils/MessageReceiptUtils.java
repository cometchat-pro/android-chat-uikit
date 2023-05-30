package com.cometchat.chatuikit.shared.utils;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.Receipt;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageReceiptUtils {
    public static Receipt MessageReceipt(BaseMessage baseMessage) {
        if (baseMessage != null) {
            if (baseMessage.getMetadata() != null) {
                {
                    JSONObject jsonObject = baseMessage.getMetadata();
                    try {
                        String exception = jsonObject.getString("error");
                        if (!exception.isEmpty()) return Receipt.ERROR;
                        else return getReceipt(baseMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return getReceipt(baseMessage);
                    }
                }
            } else {
                return getReceipt(baseMessage);
            }
        } else return Receipt.ERROR;
    }

    private static Receipt getReceipt(BaseMessage baseMessage) {
        if (baseMessage.getReceiverType() != null && baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (baseMessage.getId() == 0) return Receipt.IN_PROGRESS;
            else if (baseMessage.getReadAt() != 0) return Receipt.READ;
            else if (baseMessage.getDeliveredAt() != 0) return Receipt.DELIVERED;
            else if (baseMessage.getSentAt() > 0) return Receipt.SENT;
            else return Receipt.IN_PROGRESS;
        } else {
            if (baseMessage.getId() == 0) return Receipt.IN_PROGRESS;
            else return Receipt.SENT;
        }
    }

    public static boolean hideReceipt(BaseMessage baseMessage) {
        if (baseMessage != null && UIKitConstants.ConversationType.USERS.equalsIgnoreCase(baseMessage.getReceiverType())) {
            if (baseMessage.getCategory().equals(UIKitConstants.MessageCategory.MESSAGE)) {
                return !baseMessage.getSender().getUid().equals(CometChatUIKit.getLoggedInUser().getUid());
            } else if (baseMessage.getMetadata() != null && baseMessage.getMetadata().has("incrementUnreadCount")) {
                return !baseMessage.getSender().getUid().equals(CometChatUIKit.getLoggedInUser().getUid());
            } else return true;
        } else return true;
    }

}
