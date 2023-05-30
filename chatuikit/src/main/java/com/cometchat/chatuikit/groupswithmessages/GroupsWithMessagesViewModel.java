package com.cometchat.chatuikit.groupswithmessages;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

public class GroupsWithMessagesViewModel extends ViewModel {

    public MutableLiveData<Group> openMessages;
    public MutableLiveData<CometChatException> exceptionMutableLiveData;
    public String LISTENER_TAG;

    public GroupsWithMessagesViewModel() {
        openMessages = new MutableLiveData<>();
        exceptionMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Group> getOpenMessages() {
        return openMessages;
    }

    public MutableLiveData<CometChatException> getExceptionMutableLiveData() {
        return exceptionMutableLiveData;
    }

    public void addListener() {
        LISTENER_TAG = System.currentTimeMillis() + "";
        CometChatGroupEvents.addGroupListener(LISTENER_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupCreated(Group group) {
                openMessages.setValue(group);
            }

            @Override
            public void ccGroupMemberJoined(User joinedUser, Group joinedGroup) {
                openMessages.setValue(joinedGroup);
            }
        });
    }

    public void removeListener() {
        CometChatGroupEvents.removeListener(LISTENER_TAG);
    }

    public void joinGroup(String groupId) {
        CometChat.joinGroup(groupId, CometChatConstants.GROUP_TYPE_PUBLIC, "", new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group joinedGroup) {
                joinedGroup.setHasJoined(true);
                joinedGroup.setScope(UIKitConstants.GroupMemberScope.PARTICIPANTS);
                for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                    events.ccGroupMemberJoined(CometChatUIKit.getLoggedInUser(), joinedGroup);
                }
            }

            @Override
            public void onError(CometChatException e) {
                exceptionMutableLiveData.setValue(e);
            }
        });
    }
}
