package com.cometchat.chatuikit.groups;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;

public class GroupsViewModel extends ViewModel {

    public GroupsRequest.GroupsRequestBuilder groupsRequestBuilder;

    public GroupsRequest.GroupsRequestBuilder searchGroupsRequestBuilder;

    private GroupsRequest groupsRequest;

    public String LISTENERS_TAG;

    public MutableLiveData<List<Group>> mutableGroupsList;

    public MutableLiveData<Group> joinedGroupMutableLiveData;

    public MutableLiveData<Integer> insertAtTop;

    public MutableLiveData<Integer> moveToTop;

    public List<Group> groupArrayList;

    public MutableLiveData<Integer> updateGroup;

    public MutableLiveData<Integer> removeGroup;

    public MutableLiveData<CometChatException> cometChatException;

    public MutableLiveData<Group> createdGroup;

    public MutableLiveData<UIKitConstants.States> states;

    public int limit = 30;

    public boolean hasMore = true;

    public GroupsViewModel() {
        mutableGroupsList = new MutableLiveData<>();
        insertAtTop = new MutableLiveData<>();
        moveToTop = new MutableLiveData<>();
        groupArrayList = new ArrayList<>();
        updateGroup = new MutableLiveData<>();
        removeGroup = new MutableLiveData<>();
        createdGroup = new MutableLiveData<>();
        joinedGroupMutableLiveData = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        states = new MutableLiveData<>();
        groupsRequestBuilder = new GroupsRequest.GroupsRequestBuilder().setLimit(limit);
        searchGroupsRequestBuilder = new GroupsRequest.GroupsRequestBuilder();
        groupsRequest = groupsRequestBuilder.build();
        LISTENERS_TAG = System.currentTimeMillis() + "";
    }

    public MutableLiveData<List<Group>> getMutableGroupsList() {
        return mutableGroupsList;
    }

    public MutableLiveData<Integer> insertAtTop() {
        return insertAtTop;
    }

    public MutableLiveData<Integer> moveToTop() {
        return moveToTop;
    }

    public List<Group> getGroupArrayList() {
        return groupArrayList;
    }

    public MutableLiveData<Group> getCreatedGroup() {
        return createdGroup;
    }

    public MutableLiveData<Group> getJoinedGroupMutableLiveData() {
        return joinedGroupMutableLiveData;
    }

    public MutableLiveData<Integer> updateGroup() {
        return updateGroup;
    }

    public MutableLiveData<Integer> removeGroup() {
        return removeGroup;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public void addListeners() {
        CometChat.addGroupListener(LISTENERS_TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User user, Group group) {
                if (user.getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                    group.setHasJoined(true);
                }
                updateGroup(group);
            }

            @Override
            public void onGroupMemberLeft(Action action, User user, Group group) {
                super.onGroupMemberLeft(action, user, group);
                updateGroup(group);
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group group) {
                super.onGroupMemberKicked(action, kickedUser, kickedBy, group);
                updateGroup(group);
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group) {
                super.onGroupMemberBanned(action, bannedUser, bannedBy, group);
                updateGroup(group);
            }

            @Override
            public void onGroupMemberUnbanned(Action action, User user, User user1, Group group) {
                super.onGroupMemberUnbanned(action, user, user1, group);
                updateGroup(group);
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User user, User user1, String s, String s1, Group group) {
                super.onGroupMemberScopeChanged(action, user, user1, s, s1, group);
                updateGroup(group);
            }

            @Override
            public void onMemberAddedToGroup(Action action, User user, User user1, Group group) {
                super.onMemberAddedToGroup(action, user, user1, group);
                updateGroup(group);
            }
        });
        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupCreated(Group group) {
                if (group != null) {
                    createdGroup.setValue(group);
                    addToTop(group);
                }
            }

            @Override
            public void ccGroupLeft(Action actionMessage, User leftUser, Group leftGroup) {
                if (leftUser != null && leftUser.getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()) && leftGroup != null) {
                    if (leftGroup.getGroupType().equalsIgnoreCase(UIKitConstants.GroupType.PRIVATE))
                        removeGroup(leftGroup);
                    else
                        updateGroup(leftGroup);
                }
            }

            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom != null) {
                    updateGroup(bannedFrom);
                }
            }

            @Override
            public void ccGroupMemberAdded(List<Action> actionMessages, List<User> usersAdded, Group userAddedIn, User addedBy) {
                if (userAddedIn != null) {
                    updateGroup(userAddedIn);
                }
            }

            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom != null) {
                    updateGroup(kickedFrom);
                }
            }

            @Override
            public void ccGroupMemberUnBanned(Action actionMessage, User unbannedUser, User unBannedBy, Group unBannedFrom) {
            }

            @Override
            public void ccGroupMemberJoined(User joinedUser, Group joinedGroup) {
                if (joinedGroup != null) {
                    updateGroup(joinedGroup);
                }
            }

            @Override
            public void ccOwnershipChanged(Group group, GroupMember newOwner) {
                updateGroup(group);
            }

            @Override
            public void ccGroupDeleted(Group group) {
                if (group != null) {
                    removeGroup(group);
                }
            }
        });
    }

    public void removeListeners() {
        CometChat.removeGroupListener(LISTENERS_TAG);
        CometChatGroupEvents.removeListeners();
    }

    public void updateGroup(Group group) {
        if (groupArrayList.contains(group)) {
            groupArrayList.set(groupArrayList.indexOf(group), group);
            updateGroup.setValue(groupArrayList.indexOf(group));
        }
    }

    public void moveToTop(Group group) {
        if (groupArrayList.contains(group)) {
            int oldIndex = groupArrayList.indexOf(group);
            groupArrayList.remove(group);
            groupArrayList.add(0, group);
            moveToTop.setValue(oldIndex);
        }
    }

    public void addToTop(Group group) {
        if (group != null && !groupArrayList.contains(group)) {
            groupArrayList.add(0, group);
            insertAtTop.setValue(0);
        }
    }

    public void removeGroup(Group group) {
        if (groupArrayList.contains(group)) {
            int index = groupArrayList.indexOf(group);
            this.groupArrayList.remove(group);
            removeGroup.setValue(index);
            states.setValue(checkIsEmpty(groupArrayList));
        }
    }

    public void fetchGroup() {
        if (groupArrayList.size() == 0) states.setValue(UIKitConstants.States.LOADING);
        if (hasMore) {
            groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
                @Override
                public void onSuccess(List<Group> groups) {
                    hasMore = groups.size() != 0;
                    if (hasMore) addList(groups);
                    states.setValue(UIKitConstants.States.LOADED);
                    states.setValue(checkIsEmpty(groupArrayList));
                }

                @Override
                public void onError(CometChatException exception) {
                    cometChatException.setValue(exception);
                    states.setValue(UIKitConstants.States.ERROR);
                }
            });
        }
    }

    public void joinGroup(String groupId) {
        CometChat.joinGroup(groupId, CometChatConstants.GROUP_TYPE_PUBLIC, "", new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group joinedGroup) {
                joinedGroup.setHasJoined(true);
                joinedGroup.setScope(UIKitConstants.GroupMemberScope.PARTICIPANTS);
                joinedGroupMutableLiveData.setValue(joinedGroup);
            }

            @Override
            public void onError(CometChatException e) {
                cometChatException.setValue(e);
            }
        });
    }

    public void searchGroups(String search) {
        clear();
        hasMore = true;
        if (search != null)
            groupsRequest = searchGroupsRequestBuilder.setSearchKeyWord(search).build();
        else groupsRequest = groupsRequestBuilder.build();
        fetchGroup();
    }

    public void addList(List<Group> groupList) {
        for (Group group : groupList) {
            if (groupArrayList.contains(group)) {
                int index = groupArrayList.indexOf(group);
                groupArrayList.remove(index);
                groupArrayList.add(index, group);
            } else {
                groupArrayList.add(group);
            }
        }
        mutableGroupsList.setValue(groupArrayList);
    }

    private UIKitConstants.States checkIsEmpty(List<Group> groupList) {
        if (groupList.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

    public void setGroupsRequestBuilder(GroupsRequest.GroupsRequestBuilder groupsRequestBuilder) {
        if (groupsRequestBuilder != null) {
            this.groupsRequestBuilder = groupsRequestBuilder;
            this.groupsRequest = this.groupsRequestBuilder.build();
        }
    }

    public void setSearchRequestBuilder(GroupsRequest.GroupsRequestBuilder groupsRequestBuilder) {
        if (groupsRequestBuilder != null) this.searchGroupsRequestBuilder = groupsRequestBuilder;
    }

    public void clear() {
        groupArrayList.clear();
        mutableGroupsList.setValue(groupArrayList);
    }

}
