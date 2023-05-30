package com.cometchat.chatuikit.shared.utils;

import android.content.Context;

import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.List;

public class ConversationsUtils {

    public static String getConversationTitle(Conversation conversation) {
        if (UIKitConstants.ConversationType.USERS.equals(conversation.getConversationType())) {
            return ((User) conversation.getConversationWith()).getName();
        } else {
            return ((Group) conversation.getConversationWith()).getName();
        }
    }

    public static String getConversationIcon(Conversation conversation) {
        if (UIKitConstants.ConversationType.USERS.equals(conversation.getConversationType())) {
            return ((User) conversation.getConversationWith()).getAvatar();
        } else {
            return ((Group) conversation.getConversationWith()).getIcon();
        }
    }

    public static List<CometChatOption> getDefaultOptions(Context context, OnClick click) {
        List<CometChatOption> options = new ArrayList<>();
        options.add(new CometChatOption(UIKitConstants.ConversationOption.DELETE, context.getResources().getString(R.string.delete), R.drawable.ic_delete_conversation, Palette.getInstance(context).getError(), click));
        return options;
    }
}
