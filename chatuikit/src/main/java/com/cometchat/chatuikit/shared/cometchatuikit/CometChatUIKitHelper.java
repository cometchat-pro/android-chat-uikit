package com.cometchat.chatuikit.shared.cometchatuikit;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.events.CometChatUserEvents;

import java.util.HashMap;
import java.util.List;

public class CometChatUIKitHelper {

    public static void onMessageSent(BaseMessage message, @MessageStatus int status) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.ccMessageSent(message, status);
        }
    }

    public static void onMessageEdited(BaseMessage message, @MessageStatus int status) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.ccMessageEdited(message, status);
        }
    }

    public static void onMessageDeleted(BaseMessage message) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.ccMessageDeleted(message);
        }
    }

    public static void onMessageRead(BaseMessage message) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.ccMessageRead(message);
        }
    }

    public static void onLiveReaction(@DrawableRes int icon) {
        for (CometChatMessageEvents event : CometChatMessageEvents.messageEvents.values()) {
            event.ccLiveReaction(icon);
        }
    }

    public static void onUserBlocked(User user) {
        for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
            events.ccUserBlocked(user);
        }
    }

    public static void onUserUnblocked(User user) {
        for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
            events.ccUserUnblocked(user);
        }
    }

    public static void onGroupCreated(Group group) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupCreated(group);
        }
    }

    public static void onGroupDeleted(Group group) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupDeleted(group);
        }
    }

    public static void onGroupLeft(Action message, User leftUser, Group leftGroup) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupLeft(message, leftUser, leftGroup);
        }
    }

    public static void onGroupMemberScopeChanged(Action message, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupMemberScopeChanged(message, updatedUser, scopeChangedTo, scopeChangedFrom, group);
        }
    }

    public static void onGroupMemberBanned(Action message, User bannedUser, User bannedBy, Group bannedFrom) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupMemberBanned(message, bannedUser, bannedBy, bannedFrom);
        }
    }

    public static void onGroupMemberKicked(Action message, User kickedUser, User kickedBy, Group kickedFrom) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupMemberKicked(message, kickedUser, kickedBy, kickedFrom);
        }
    }

    public static void onGroupMemberUnbanned(Action message, User unbannedUser, User unbannedBy, Group unbannedFrom) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupMemberUnBanned(message, unbannedUser, unbannedBy, unbannedFrom);
        }
    }

    public static void onGroupMemberJoined(User joinedUser, Group joinedGroup) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupMemberJoined(joinedUser, joinedGroup);
        }
    }

    public static void onGroupMemberAdded(List<Action> messages, List<User> usersAdded, Group groupAddedIn, User addedBy) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccGroupMemberAdded(messages, usersAdded, groupAddedIn, addedBy);
        }
    }

    public static void onOwnershipChanged(Group group, GroupMember newOwner) {
        for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
            events.ccOwnershipChanged(group, newOwner);
        }
    }

    public static void showPanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment, Function1<Context, View> view) {
        for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
            events.showPanel(id, alignment, view);
        }
    }

    public static void hidePanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment) {
        for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
            events.hidePanel(id, alignment);
        }
    }

    public static void onActiveChatChanged(HashMap<String, String> id, BaseMessage message, User user, Group group) {
        for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
            events.ccActiveChatChanged(id, message, user, group);
        }
    }
}
