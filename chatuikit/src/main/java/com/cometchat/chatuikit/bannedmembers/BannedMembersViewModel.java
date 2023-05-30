package com.cometchat.chatuikit.bannedmembers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.DetailsUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;

public class BannedMembersViewModel extends ViewModel {

    public final String TAG = BannedMembersViewModel.class.getName();

    public BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedGroupMembersRequestBuilder;

    public BannedGroupMembersRequest.BannedGroupMembersRequestBuilder searchBannedGroupMembersRequestBuilder;

    private BannedGroupMembersRequest bannedGroupMembersRequest;

    public String LISTENERS_TAG;

    public Group group;

    public String id = "";

    public MutableLiveData<List<GroupMember>> mutableBannedGroupMembersList;

    public MutableLiveData<Integer> insertAtTop;

    public MutableLiveData<Integer> moveToTop;

    public List<GroupMember> groupMemberArrayList;

    public MutableLiveData<Integer> updateGroupMember;

    public MutableLiveData<Integer> removeGroupMember;

    public MutableLiveData<CometChatException> cometChatException;

    public MutableLiveData<UIKitConstants.States> states;

    public int limit = 30;

    public boolean hasMore = true;

    Action action;

    public BannedMembersViewModel() {
        init();
    }

    private void init() {
        mutableBannedGroupMembersList = new MutableLiveData<>();
        insertAtTop = new MutableLiveData<>();
        moveToTop = new MutableLiveData<>();
        groupMemberArrayList = new ArrayList<>();
        updateGroupMember = new MutableLiveData<>();
        removeGroupMember = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        action = new Action();
        action.setOldScope(UIKitConstants.GroupMemberScope.PARTICIPANTS);
        states = new MutableLiveData<>();
    }

    public MutableLiveData<List<GroupMember>> getMutableBannedGroupMembersList() {
        return mutableBannedGroupMembersList;
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
        if (bannedGroupMembersRequestBuilder == null)
            bannedGroupMembersRequestBuilder = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(id).setLimit(limit);
        if (searchBannedGroupMembersRequestBuilder == null)
            searchBannedGroupMembersRequestBuilder = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(id);
        bannedGroupMembersRequest = bannedGroupMembersRequestBuilder.build();
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public void addListeners() {
        LISTENERS_TAG = System.currentTimeMillis() + "";
        CometChat.addGroupListener(LISTENERS_TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group_) {
                if (group_ != null && group_.equals(group)) {
                    updateGroupMember(bannedUser, false, false, action);
                }
            }

            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group group_) {
                removeGroupMember(DetailsUtils.userToGroupMember(unbannedUser, false, ""));
            }
        });
        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom != null && bannedFrom.equals(group)) {
                    updateGroupMember(bannedUser, false, false, actionMessage);
                }
            }

            @Override
            public void ccGroupMemberUnBanned(Action actionMessage, User unbannedUser, User unBannedBy, Group unBannedFrom) {
                if (unBannedFrom != null && unBannedFrom.equals(group)) {
                    updateGroupMember(unbannedUser, true, false, actionMessage);
                }
            }
        });
        CometChat.addUserListener(LISTENERS_TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                updateGroupMember(DetailsUtils.userToGroupMember(user, false, action.getOldScope()));
            }

            @Override
            public void onUserOffline(User user) {
                updateGroupMember(DetailsUtils.userToGroupMember(user, false, action.getOldScope()));
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

    public void updateGroupMember(GroupMember groupMember) {
        if (groupMemberArrayList.contains(groupMember)) {
            groupMemberArrayList.set(groupMemberArrayList.indexOf(groupMember), groupMember);
            updateGroupMember.setValue(groupMemberArrayList.indexOf(groupMember));
        }
    }

    public void moveToTop(GroupMember groupMember) {
        if (groupMemberArrayList.contains(groupMember)) {
            int oldIndex = groupMemberArrayList.indexOf(groupMember);
            groupMemberArrayList.remove(groupMember);
            groupMemberArrayList.add(0, groupMember);
            moveToTop.setValue(oldIndex);
        }
    }

    public void addToTop(GroupMember groupMember) {
        if (groupMember != null && !groupMemberArrayList.contains(groupMember)) {
            groupMemberArrayList.add(0, groupMember);
            insertAtTop.setValue(0);
            states.setValue(checkIsEmpty(groupMemberArrayList));
        }
    }

    public void removeGroupMember(GroupMember groupMember) {
        if (groupMemberArrayList.contains(groupMember)) {
            int index = groupMemberArrayList.indexOf(groupMember);
            this.groupMemberArrayList.remove(groupMember);
            removeGroupMember.setValue(index);
            states.setValue(checkIsEmpty(groupMemberArrayList));
        }
    }

    public void fetchGroupMember() {
        if (groupMemberArrayList.size() == 0) states.setValue(UIKitConstants.States.LOADING);
        if (hasMore) {
            bannedGroupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> bannedGroupMembers) {
                    hasMore = bannedGroupMembers.size() != 0;
                    if (hasMore) addList(bannedGroupMembers);
                    states.setValue(UIKitConstants.States.LOADED);
                    states.setValue(checkIsEmpty(groupMemberArrayList));
                }

                @Override
                public void onError(CometChatException exception) {
                    cometChatException.setValue(exception);
                    states.setValue(UIKitConstants.States.ERROR);
                }
            });
        }
    }

    public void searchBannedGroupMembers(String search) {
        clear();
        hasMore = true;
        if (search != null)
            bannedGroupMembersRequest = searchBannedGroupMembersRequestBuilder.setSearchKeyword(search).build();
        else bannedGroupMembersRequest = bannedGroupMembersRequestBuilder.build();
        fetchGroupMember();
    }

    public void addList(List<GroupMember> groupMemberList) {
        for (GroupMember groupMember : groupMemberList) {
            if (groupMemberArrayList.contains(groupMember)) {
                int index = groupMemberArrayList.indexOf(groupMember);
                groupMemberArrayList.remove(index);
                groupMemberArrayList.add(index, groupMember);
            } else {
                groupMemberArrayList.add(groupMember);
            }
        }
        mutableBannedGroupMembersList.setValue(groupMemberArrayList);
    }

    private UIKitConstants.States checkIsEmpty(List<GroupMember> bannedGroupMembers) {
        if (bannedGroupMembers.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

    public void setBannedGroupMembersRequestBuilder(BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedGroupMembersRequestBuilder) {
        if (bannedGroupMembersRequestBuilder != null) {
            this.bannedGroupMembersRequestBuilder = bannedGroupMembersRequestBuilder;
            this.bannedGroupMembersRequest = this.bannedGroupMembersRequestBuilder.build();
        }
    }

    public void setSearchRequestBuilder(BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedGroupMembersRequestBuilder) {
        if (bannedGroupMembersRequestBuilder != null)
            this.searchBannedGroupMembersRequestBuilder = bannedGroupMembersRequestBuilder;
    }

    public void clear() {
        groupMemberArrayList.clear();
        mutableBannedGroupMembersList.setValue(groupMemberArrayList);
    }

    public void unBanGroupMember(GroupMember groupMember) {
        CometChat.unbanGroupMember(groupMember.getUid(), id, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Action action = Utils.getGroupActionMessage(groupMember, group, group, group.getGuid());
                action.setAction(CometChatConstants.ActionKeys.ACTION_UNBANNED);
                for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                    events.ccGroupMemberUnBanned(action, groupMember, CometChatUIKit.getLoggedInUser(), group);
                }
            }

            @Override
            public void onError(CometChatException exception) {
                cometChatException.setValue(exception);
                states.setValue(UIKitConstants.States.ERROR);
            }
        });

    }
}
