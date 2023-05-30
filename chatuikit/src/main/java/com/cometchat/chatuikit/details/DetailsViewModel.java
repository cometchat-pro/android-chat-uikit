package com.cometchat.chatuikit.details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.events.CometChatUserEvents;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

enum States {
    PROGRESS, SUCCESS, ERROR
}

public class DetailsViewModel extends ViewModel {

    private User user;
    private Group group;
    private final String LISTENER_ID;
    private MutableLiveData<User> userMutableLiveData;
    private MutableLiveData<Group> groupMutableLiveData;
    private MutableLiveData<States> actionUser;
    private MutableLiveData<CometChatException> cometChatExceptionMutableLiveData;
    private MutableLiveData<Void> blockUser;
    private MutableLiveData<Void> unblockUser;
    private MutableLiveData<Void> deleteGroup;
    private MutableLiveData<Void> leaveGroup;
    private MutableLiveData<Boolean> kickedGroup;
    private MutableLiveData<Boolean> bannedGroup;
    private User loggedInUser;
    private Void aVoid = null;

    public DetailsViewModel() {
        userMutableLiveData = new MutableLiveData<>();
        groupMutableLiveData = new MutableLiveData<>();
        cometChatExceptionMutableLiveData = new MutableLiveData<>();
        blockUser = new MutableLiveData<>();
        unblockUser = new MutableLiveData<>();
        deleteGroup = new MutableLiveData<>();
        leaveGroup = new MutableLiveData<>();
        actionUser = new MutableLiveData<>();
        bannedGroup = new MutableLiveData<>();
        kickedGroup = new MutableLiveData<>();
        loggedInUser = CometChatUIKit.getLoggedInUser();
        LISTENER_ID = System.currentTimeMillis() + "";
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Group> getGroupMutableLiveData() {
        return groupMutableLiveData;
    }

    public MutableLiveData<States> getActionUser() {
        return actionUser;
    }

    public MutableLiveData<CometChatException> getCometChatExceptionMutableLiveData() {
        return cometChatExceptionMutableLiveData;
    }

    public MutableLiveData<Void> getBlockUser() {
        return blockUser;
    }

    public MutableLiveData<Void> getUnblockUser() {
        return unblockUser;
    }

    public MutableLiveData<Void> getDeleteGroup() {
        return deleteGroup;
    }

    public MutableLiveData<Void> getLeaveGroup() {
        return leaveGroup;
    }

    public MutableLiveData<Boolean> isKicked() {
        return kickedGroup;
    }

    public MutableLiveData<Boolean> isBanned() {
        return bannedGroup;
    }

    public void addGroupListener() {
        CometChat.addGroupListener(LISTENER_ID, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group group_) {
                super.onGroupMemberKicked(action, kickedUser, kickedBy, group_);
                if (group != null && group_ != null) {
                    group.setMembersCount(group_.getMembersCount());
                    group.setUpdatedAt(group_.getUpdatedAt());
                    if (kickedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                        kickedGroup.setValue(true);
                    } else {
                        kickedGroup.setValue(false);
                    }
                }
            }

            @Override
            public void onGroupMemberLeft(Action action, User user, Group group_) {
                super.onGroupMemberLeft(action, user, group_);
                if (group != null && group_ != null) {
                    group.setMembersCount(group_.getMembersCount());
                    group.setUpdatedAt(group_.getUpdatedAt());
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group_) {
                super.onGroupMemberBanned(action, bannedUser, bannedBy, group_);
                if (group != null && group_ != null) {
                    if (bannedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                        bannedGroup.setValue(true);
                    } else {
                        bannedGroup.setValue(false);
                    }
                }
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group_) {
                super.onGroupMemberScopeChanged(action, updatedBy, updatedUser, scopeChangedTo, scopeChangedFrom, group_);
                if (group != null && group_ != null) {
                    if (updatedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                        group.setScope(scopeChangedTo);
                        updateGroup(group);
                    }
                }
            }
        });

        CometChatGroupEvents.addGroupListener(LISTENER_ID, new CometChatGroupEvents() {
            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                updateGroup(bannedFrom);
            }

            @Override
            public void ccGroupMemberAdded(List<Action> actionMessages, List<User> usersAdded, Group userAddedIn, User addedBy) {
                updateGroup(userAddedIn);
            }

            @Override
            public void ccOwnershipChanged(Group group_, GroupMember newOwner) {
                if (group_ != null) updateGroup(group_);
            }

            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom != null) updateGroup(kickedFrom);
            }
        });
    }

    public void addUserListener() {
        CometChat.addUserListener(LISTENER_ID, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user_) {
                updateUserStatus(user_);
            }

            @Override
            public void onUserOffline(User user_) {
                updateUserStatus(user_);
            }
        });
    }

    public void removeGroupListener() {
        CometChat.removeGroupListener(LISTENER_ID);
        CometChatGroupEvents.removeListener(LISTENER_ID);
    }

    public void removeUserListener() {
        CometChat.removeUserListener(LISTENER_ID);
    }

    public void updateUserStatus(User user_) {
        if (user_ != null && user != null) {
            user.setStatus(user_.getStatus());
            userMutableLiveData.setValue(user);
        }
    }

    public void updateGroup(Group group_) {
        if (group_ != null && group != null) {
            groupMutableLiveData.setValue(group_);
        }
    }

    public void blockUser() {
        if (user != null) {
            actionUser.setValue(States.PROGRESS);
            CometChat.blockUsers(Collections.singletonList(user.getUid()), new CometChat.CallbackListener<HashMap<String, String>>() {
                @Override
                public void onSuccess(HashMap<String, String> resultMap) {
                    if (resultMap != null && "success".equalsIgnoreCase(resultMap.get(user.getUid()))) {
                        user.setBlockedByMe(true);
                        actionUser.setValue(States.SUCCESS);
                        blockUser.setValue(aVoid);
                        for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
                            events.ccUserBlocked(user);
                        }
                    } else actionUser.setValue(States.ERROR);
                }

                @Override
                public void onError(CometChatException e) {
                    actionUser.setValue(States.ERROR);
                    cometChatExceptionMutableLiveData.setValue(e);
                }
            });
        }
    }

    public void unblockUser() {
        if (user != null) {
            actionUser.setValue(States.PROGRESS);
            CometChat.unblockUsers(Collections.singletonList(user.getUid()), new CometChat.CallbackListener<HashMap<String, String>>() {
                @Override
                public void onSuccess(HashMap<String, String> resultMap) {
                    if (resultMap != null && "success".equalsIgnoreCase(resultMap.get(user.getUid()))) {
                        user.setBlockedByMe(false);
                        actionUser.setValue(States.SUCCESS);
                        unblockUser.setValue(aVoid);
                        for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
                            events.ccUserUnblocked(user);
                        }
                    } else actionUser.setValue(States.ERROR);
                }

                @Override
                public void onError(CometChatException e) {
                    actionUser.setValue(States.ERROR);
                    cometChatExceptionMutableLiveData.setValue(e);
                }
            });
        }
    }

    public void deleteGroup() {
        if (group != null) {
            actionUser.setValue(States.PROGRESS);
            CometChat.deleteGroup(group.getGuid(), new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String successMessage) {
                    actionUser.setValue(States.SUCCESS);
                    deleteGroup.setValue(aVoid);
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.ccGroupDeleted(group);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    actionUser.setValue(States.ERROR);
                    cometChatExceptionMutableLiveData.setValue(e);
                }
            });
        }
    }

    public void leaveGroup() {
        if (group != null) {
            actionUser.setValue(States.PROGRESS);
            CometChat.leaveGroup(group.getGuid(), new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String successMessage) {
                    group.setHasJoined(false);
                    group.setMembersCount(group.getMembersCount() - 1);
                    actionUser.setValue(States.SUCCESS);
                    Action action= Utils.getGroupActionMessage(loggedInUser,group,group,group.getGuid());
                    action.setAction(CometChatConstants.ActionKeys.ACTION_LEFT);
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.ccGroupLeft(action, loggedInUser, group);
                    }
                    leaveGroup.setValue(aVoid);
                }

                @Override
                public void onError(CometChatException e) {
                    actionUser.setValue(States.ERROR);
                    cometChatExceptionMutableLiveData.setValue(e);
                }
            });
        }
    }

}
