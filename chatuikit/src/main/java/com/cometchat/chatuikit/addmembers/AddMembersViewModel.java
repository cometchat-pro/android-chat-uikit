package com.cometchat.chatuikit.addmembers;

import androidx.lifecycle.MutableLiveData;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.DetailsUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.users.UsersViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum AddMemberStates {
    ERROR, SUCCESS, SHOW_PROGRESS, DISMISS_PROGRESS
}

public class AddMembersViewModel extends UsersViewModel {

    public MutableLiveData<AddMemberStates> addMemberStatesMutableLiveData;
    public MutableLiveData<CometChatException> exceptionMutableLiveData;
    private Group group;

    public AddMembersViewModel() {
        addMemberStatesMutableLiveData = new MutableLiveData<>();
        exceptionMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<AddMemberStates> addMemberStates() {
        return addMemberStatesMutableLiveData;
    }

    public MutableLiveData<CometChatException> getException() {
        return exceptionMutableLiveData;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void addMembersToGroup(List<User> users) {
        addMemberStatesMutableLiveData.setValue(AddMemberStates.SHOW_PROGRESS);
        List<GroupMember> groupMembers = new ArrayList<>();
        for (User user : users) {
            groupMembers.add(DetailsUtils.userToGroupMember(user, false, ""));
        }
        if (group != null) {
            CometChat.addMembersToGroup(group.getGuid(), groupMembers, null, new CometChat.CallbackListener<HashMap<String, String>>() {
                @Override
                public void onSuccess(HashMap<String, String> successMap) {
                    int i = 0;
                    for (Map.Entry<String, String> entry : successMap.entrySet()) {
                        if ("success".equals(entry.getValue())) i++;
                    }
                    group.setMembersCount(group.getMembersCount() + i);
                    List<User> members = new ArrayList<>(groupMembers);
                    List<Action> actions = new ArrayList<>();
                    for (User user : members) {
                        Action action = Utils.getGroupActionMessage(user, group, group, group.getGuid());
                        action.setAction(CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED);
                        actions.add(action);
                    }
                    for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                        events.ccGroupMemberAdded(actions, members, group, CometChatUIKit.getLoggedInUser());
                    }
                    addMemberStatesMutableLiveData.setValue(AddMemberStates.SUCCESS);
                }

                @Override
                public void onError(CometChatException e) {
                    addMemberStatesMutableLiveData.setValue(AddMemberStates.ERROR);
                    exceptionMutableLiveData.setValue(e);
                }
            });
        }
    }
}
