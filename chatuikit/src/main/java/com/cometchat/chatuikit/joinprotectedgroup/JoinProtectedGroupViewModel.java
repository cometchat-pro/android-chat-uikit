package com.cometchat.chatuikit.joinprotectedgroup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;

public class JoinProtectedGroupViewModel extends ViewModel {

    public Group group;

    public MutableLiveData<Group> groupMutableLiveData;

    public MutableLiveData<CometChatException> cometChatException;

    public JoinProtectedGroupViewModel() {
        groupMutableLiveData = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
    }

    public MutableLiveData<Group> getGroupMutableLiveData() {
        return groupMutableLiveData;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void joinGroup(String password) {
        if (group != null && group.getGuid() != null && group.getGroupType() != null) {
            CometChat.joinGroup(group.getGuid(), group.getGroupType(), password, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group joinedGroup) {
                    joinedGroup.setHasJoined(true);
                    joinedGroup.setScope(CometChatConstants.SCOPE_PARTICIPANT);
                    for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                        events.ccGroupMemberJoined(CometChatUIKit.getLoggedInUser(), joinedGroup);
                    }
                    groupMutableLiveData.setValue(joinedGroup);
                }

                @Override
                public void onError(CometChatException e) {
                    cometChatException.setValue(e);
                }
            });
        }
    }

}
