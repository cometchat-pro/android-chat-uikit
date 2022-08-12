package com.cometchatworkspace.components.users;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;

import java.util.HashMap;

public abstract class CometChatUserEvents {
    public static final HashMap<String, CometChatUserEvents> userEvents = new HashMap<>();

    public abstract void onError(CometChatException error);

    public abstract void onItemClick(User user, int position);

    public abstract void onItemLongClick(User user, int position);

    public abstract void onUserBlock(User user);

    public abstract void onUserUnblock(User user);

    public static void addUserListener(String TAG, CometChatUserEvents chatUserEvents) {
        userEvents.put(TAG, chatUserEvents);
    }

    public static void removeListener() {
        userEvents.clear();
    }

}
