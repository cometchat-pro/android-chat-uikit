package com.cometchatworkspace.components.groups;

import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class CometChatGroupEvents {

    public static final HashMap<String, CometChatGroupEvents> groupEvents = new HashMap<>();

    public void onItemClick(Group group, int position){}

    public void onItemLongClick(Group group, int position) {}

    public void onGroupCreate(Group group){}

    public void onCreateGroupIconClick() {}

    public void onError(CometChatException error){}

    public void onGroupDelete(Group group){}

    public void onGroupMemberLeave(User leftUser, Group leftGroup){}

    public void onGroupMemberChangeScope(User updatedBy, User updatedUser,
                                         String scopeChangedTo, String scopeChangedFrom, Group group){}

    public void onGroupMemberBan(User bannedUser, User bannedBy, Group bannedFrom){}

    public void onGroupMemberAdd(User addedBy, List<User> usersAdded, Group group){}

    public void onGroupMemberKick(User kickedUser, User kickedBy, Group kickedFrom){}

    public void onGroupMemberUnban(User unbannedUser, User unbannedBy, Group unBannedFrom){}

    public void onGroupMemberJoin(User joinedUser, Group joinedGroup){}

    public void onOwnershipChange(Group group, GroupMember member){}

    public static void addGroupListener(String TAG, CometChatGroupEvents chatGroupEvents) {
        groupEvents.put(TAG, chatGroupEvents);
    }

    public static void removeListener() {
        groupEvents.clear();
    }
}
