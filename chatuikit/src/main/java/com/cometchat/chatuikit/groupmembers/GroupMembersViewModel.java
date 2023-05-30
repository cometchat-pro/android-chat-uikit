package com.cometchat.chatuikit.groupmembers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.DetailsUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;

public class GroupMembersViewModel extends ViewModel {

    public final String TAG = GroupMembersViewModel.class.getName();

    public GroupMembersRequest.GroupMembersRequestBuilder groupMembersRequestBuilder;

    public GroupMembersRequest.GroupMembersRequestBuilder searchGroupMembersRequestBuilder;

    private GroupMembersRequest groupMembersRequest;

    public String LISTENERS_TAG;

    public User loggedInUser;

    public Group group;

    public String id = "";

    public MutableLiveData<List<GroupMember>> mutableGroupMembersList;

    public MutableLiveData<Integer> insertAtTop;

    public MutableLiveData<Integer> moveToTop;

    public List<GroupMember> groupMemberArrayList;

    public MutableLiveData<Integer> updateGroupMember;

    public MutableLiveData<Integer> removeGroupMember;

    public MutableLiveData<CometChatException> cometChatException;

    public MutableLiveData<UIKitConstants.States> states;

    public int limit = 30;

    public boolean hasMore = true;

    public GroupMembersViewModel() {
        init();
    }

    private void init() {
        mutableGroupMembersList = new MutableLiveData<>();
        insertAtTop = new MutableLiveData<>();
        moveToTop = new MutableLiveData<>();
        groupMemberArrayList = new ArrayList<>();
        updateGroupMember = new MutableLiveData<>();
        removeGroupMember = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        states = new MutableLiveData<>();
        loggedInUser = CometChatUIKit.getLoggedInUser();
    }

    public MutableLiveData<List<GroupMember>> getMutableGroupMembersList() {
        return mutableGroupMembersList;
    }

    public MutableLiveData<Integer> insertAtTop() {
        return insertAtTop;
    }

    public MutableLiveData<Integer> moveToTop() {
        return moveToTop;
    }

    public List<GroupMember> getGroupMemberArrayList() {
        return groupMemberArrayList;
    }

    public MutableLiveData<Integer> updateGroupMember() {
        return updateGroupMember;
    }

    public MutableLiveData<Integer> removeGroupMember() {
        return removeGroupMember;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            this.id = group.getGuid();
        }
        if (groupMembersRequestBuilder == null)
            groupMembersRequestBuilder = new GroupMembersRequest.GroupMembersRequestBuilder(id).setLimit(limit);
        if (searchGroupMembersRequestBuilder == null)
            searchGroupMembersRequestBuilder = new GroupMembersRequest.GroupMembersRequestBuilder(id);
        groupMembersRequest = groupMembersRequestBuilder.build();
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public void addListeners() {
        LISTENERS_TAG = System.currentTimeMillis() + "";
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(joinedUser, false, false, action);
                }
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(leftUser, true, false, action);
                }
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(kickedUser, true, false, action);
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(bannedUser, true, false, action);
                }
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(updatedUser, false, true, action);
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedBy, User userAdded, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(userAdded, false, false, action);
                }
            }
        });

        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom != null && bannedFrom.equals(group)) {
                    removeGroupMember(DetailsUtils.userToGroupMember(bannedUser, false, ""));
                }
            }

            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom != null && kickedFrom.equals(group)) {
                    removeGroupMember(DetailsUtils.userToGroupMember(kickedUser, false, ""));
                }
            }

            @Override
            public void ccGroupMemberScopeChanged(Action actionMessage, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group_) {
                if (group_.equals(group)) {
                    updateGroupMember(DetailsUtils.userToGroupMember(updatedUser, true, scopeChangedTo));
                }
            }

            @Override
            public void ccGroupMemberAdded(List<Action> actionMessages, List<User> usersAdded, Group group_, User addedBy) {
                if (group_ != null && group_.equals(group)) {
                    for (User user : usersAdded)
                        addToTop(DetailsUtils.userToGroupMember(user, false, ""));
                }
            }

            @Override
            public void ccGroupMemberUnBanned(Action actionMessage, User unbannedUser, User unBannedBy, Group unBannedFrom) {
                if (unBannedFrom.equals(group)) {
                    setGroup(unBannedFrom);
                    addToTop(DetailsUtils.userToGroupMember(unbannedUser, false, ""));
                }
            }

            @Override
            public void ccGroupMemberJoined(User joinedUser, Group joinedGroup) {
                if (joinedGroup.equals(group)) {
                    setGroup(group);
                    addToTop(DetailsUtils.userToGroupMember(joinedUser, false, ""));
                }
            }

            @Override
            public void ccOwnershipChanged(Group group_, GroupMember newOwner) {
                if (group_.equals(group)) {
                    setGroup(group_);
                    updateGroupMember(newOwner);
                }
            }
        });
        CometChat.addUserListener(LISTENERS_TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                updateGroupMember(getGroupMemberWithUpdatedStatus(user,UIKitConstants.UserStatus.ONLINE));
            }

            @Override
            public void onUserOffline(User user) {
                updateGroupMember(getGroupMemberWithUpdatedStatus(user,UIKitConstants.UserStatus.OFFLINE));
            }
        });
    }

    public GroupMember getGroupMemberWithUpdatedStatus(User user,String status) {
        GroupMember tempGroupMember = null;
        for (GroupMember groupMember : groupMemberArrayList) {
            if (groupMember.getUid().equalsIgnoreCase(user.getUid())) {
                tempGroupMember = groupMember;
                tempGroupMember.setStatus(status);
            }
        }
        return tempGroupMember;
    }

    public void scopeChange(GroupMember updateMember, String scopeChangedTo) {
        CometChat.updateGroupMemberScope(updateMember.getUid(), id, scopeChangedTo, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                updateMember.setScope(scopeChangedTo);
                Action action = Utils.getGroupActionMessage(updateMember, group, group, group.getGuid());
                action.setNewScope(scopeChangedTo);
                action.setAction(CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED);
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.ccGroupMemberScopeChanged(action, updateMember, scopeChangedTo, updateMember.getScope(), group);
                }
            }

            @Override
            public void onError(CometChatException e) {
                states.setValue(UIKitConstants.States.ERROR);
                updateGroupMember(updateMember);
                onErrorTrigger(e);
            }
        });
    }

    private GroupMember searchMemberById(String id) {
        GroupMember member = null;
        for (GroupMember groupMember : groupMemberArrayList) {
            if (groupMember.getUid().equalsIgnoreCase(id)) {
                member = groupMember;
                break;
            }
        }
        return member;
    }

    private void updateGroupMember(User user, boolean isRemoved, boolean isScopeUpdate, Action action) {
        if (!isRemoved && !isScopeUpdate) {
            addToTop(DetailsUtils.userToGroupMember(user, false, action.getOldScope()));
        } else if (isRemoved && !isScopeUpdate) {
            removeGroupMember(DetailsUtils.userToGroupMember(user, false, action.getOldScope()));
        } else if (!isRemoved) {
            updateGroupMember(DetailsUtils.userToGroupMember(user, true, action.getNewScope()));
        }
    }

    public void removeListeners() {
        CometChat.removeGroupListener(LISTENERS_TAG);
        CometChatGroupEvents.removeListener(LISTENERS_TAG);
        CometChat.removeUserListener(LISTENERS_TAG);
    }

    public void updateGroupMember(GroupMember GroupMember) {
        if (groupMemberArrayList.contains(GroupMember)) {
            groupMemberArrayList.set(groupMemberArrayList.indexOf(GroupMember), GroupMember);
            updateGroupMember.setValue(groupMemberArrayList.indexOf(GroupMember));
        }
    }

    public void moveToTop(GroupMember GroupMember) {
        if (groupMemberArrayList.contains(GroupMember)) {
            int oldIndex = groupMemberArrayList.indexOf(GroupMember);
            groupMemberArrayList.remove(GroupMember);
            groupMemberArrayList.add(0, GroupMember);
            moveToTop.setValue(oldIndex);
        }
    }

    public void addToTop(GroupMember GroupMember) {
        if (GroupMember != null && !groupMemberArrayList.contains(GroupMember)) {
            groupMemberArrayList.add(0, GroupMember);
            insertAtTop.setValue(0);
        }
    }

    public void removeGroupMember(GroupMember GroupMember) {
        if (groupMemberArrayList.contains(GroupMember)) {
            int index = groupMemberArrayList.indexOf(GroupMember);
            this.groupMemberArrayList.remove(GroupMember);
            removeGroupMember.setValue(index);
            states.setValue(checkIsEmpty(groupMemberArrayList));
        }
    }

    public void fetchGroupMember() {
        if (groupMemberArrayList.size() == 0) states.setValue(UIKitConstants.States.LOADING);
        if (hasMore) {
            groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> GroupMembers) {
                    hasMore = GroupMembers.size() != 0;
                    if (hasMore) addList(GroupMembers);
                    states.setValue(UIKitConstants.States.LOADED);
                    states.setValue(checkIsEmpty(groupMemberArrayList));
                }

                @Override
                public void onError(CometChatException e) {
                    onErrorTrigger(e);
                    states.setValue(UIKitConstants.States.ERROR);
                }
            });
        }
    }

    public void searchGroupMembers(String search) {
        clear();
        hasMore = true;
        if (search != null)
            groupMembersRequest = searchGroupMembersRequestBuilder.setSearchKeyword(search).build();
        else groupMembersRequest = groupMembersRequestBuilder.build();
        fetchGroupMember();
    }

    public void addList(List<GroupMember> GroupMemberList) {
        for (GroupMember GroupMember : GroupMemberList) {
            if (groupMemberArrayList.contains(GroupMember)) {
                int index = groupMemberArrayList.indexOf(GroupMember);
                groupMemberArrayList.remove(index);
                groupMemberArrayList.add(index, GroupMember);
            } else {
                groupMemberArrayList.add(GroupMember);
            }
        }
        mutableGroupMembersList.setValue(groupMemberArrayList);
    }

    private UIKitConstants.States checkIsEmpty(List<GroupMember> GroupMembers) {
        if (GroupMembers.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

    public void setGroupMembersRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder GroupMembersRequest) {
        if (GroupMembersRequest != null) {
            this.groupMembersRequestBuilder = GroupMembersRequest;
            this.groupMembersRequest = groupMembersRequestBuilder.build();
        }
    }

    public void setSearchRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder GroupMembersRequestBuilder) {
        if (GroupMembersRequestBuilder != null)
            this.searchGroupMembersRequestBuilder = GroupMembersRequestBuilder;

    }

    public void clear() {
        groupMemberArrayList.clear();
        mutableGroupMembersList.setValue(groupMemberArrayList);
    }

    public void banGroupMember(GroupMember groupMember) {
        CometChat.banGroupMember(groupMember.getUid(), id, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Action action = Utils.getGroupActionMessage(groupMember, group, group, group.getGuid());
                action.setAction(CometChatConstants.ActionKeys.ACTION_BANNED);
                for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                    events.ccGroupMemberBanned(action, groupMember, loggedInUser, group);
                }
            }

            @Override
            public void onError(CometChatException e) {
                onErrorTrigger(e);
                states.setValue(UIKitConstants.States.ERROR);
            }
        });
    }

    public void kickGroupMember(GroupMember groupMember) {
        CometChat.kickGroupMember(groupMember.getUid(), id, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                group.setMembersCount(group.getMembersCount() - 1);
                Action action = Utils.getGroupActionMessage(groupMember, group, group, group.getGuid());
                action.setAction(CometChatConstants.ActionKeys.ACTION_KICKED);
                for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                    events.ccGroupMemberKicked(action, groupMember, loggedInUser, group);
                }
            }

            @Override
            public void onError(CometChatException e) {
                onErrorTrigger(e);
                states.setValue(UIKitConstants.States.ERROR);
            }
        });
    }

    private void onErrorTrigger(CometChatException e) {
        cometChatException.setValue(e);
    }
}
