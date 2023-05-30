package com.cometchat.chatuikit.messageheader;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;

import java.util.HashMap;
import java.util.List;

public class MessageHeaderViewModel extends ViewModel {
    private String LISTENERS_TAG;
    public User user;
    public Group group;
    public String id;
    public MutableLiveData<Integer> memberCount;
    public MutableLiveData<HashMap<TypingIndicator, Boolean>> typing;
    public MutableLiveData<String> userPresenceStatus;
    public HashMap<TypingIndicator, Boolean> typingHash;
    public MutableLiveData<Group> updateGroup;

    public MessageHeaderViewModel() {
        memberCount = new MutableLiveData<>();
        typing = new MutableLiveData<>();
        userPresenceStatus = new MutableLiveData<>();
        updateGroup = new MutableLiveData<>();
        typingHash = new HashMap<>();
    }

    public MutableLiveData<Integer> getMemberCount() {
        return memberCount;
    }

    public MutableLiveData<HashMap<TypingIndicator, Boolean>> getTyping() {
        return typing;
    }

    public MutableLiveData<String> getUserPresenceStatus() {
        return userPresenceStatus;
    }

    public MutableLiveData<Group> getUpdateGroup() {
        return updateGroup;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            this.id = user.getUid() + "";
        }
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            this.id = group.getGuid() + "";
        }
    }

    public void addListener() {
        LISTENERS_TAG = System.currentTimeMillis() + "";

        CometChat.addGroupListener(LISTENERS_TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                super.onGroupMemberJoined(action, joinedUser, joinedGroup);
                if (joinedGroup.getGuid().equals(id)) {
                    group.setMembersCount(joinedGroup.getMembersCount());
                    updateGroup.setValue(group);
                }
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                if (leftGroup.getGuid().equals(id)) {
                    group.setMembersCount(leftGroup.getMembersCount());
                    updateGroup.setValue(group);
                }
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom.getGuid().equals(id)) {
                    group.setMembersCount(kickedFrom.getMembersCount());
                    updateGroup.setValue(group);
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom.getGuid().equals(id)) {
                    group.setMembersCount(bannedFrom.getMembersCount());
                    updateGroup.setValue(group);
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedBy, User userAdded, Group addedTo) {
                if (addedTo.getGuid().equals(id)) {
                    group.setMembersCount(addedTo.getMembersCount());
                    updateGroup.setValue(group);
                }
            }
        });
        CometChat.addMessageListener(LISTENERS_TAG, new CometChat.MessageListener() {
            @Override
            public void onTypingStarted(TypingIndicator typingIndicator) {
                setTypingIndicator(typingIndicator, true);
            }

            @Override
            public void onTypingEnded(TypingIndicator typingIndicator) {
                setTypingIndicator(typingIndicator, false);
            }
        });

        CometChat.addUserListener(LISTENERS_TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                if (user.getUid().equals(id)) {
                    userPresenceStatus.setValue(user.getStatus());
                }
            }

            @Override
            public void onUserOffline(User user) {
                if (user.getUid().equals(id)) {
                    userPresenceStatus.setValue(user.getStatus());
                }
            }
        });

        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom != null) updateGroup.setValue(kickedFrom);
            }

            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom != null) updateGroup.setValue(bannedFrom);
            }

            @Override
            public void ccOwnershipChanged(Group group, GroupMember newOwner) {
               if(group!=null) updateGroup.setValue(group);
            }

            @Override
            public void ccGroupMemberAdded(List<Action> actionMessages, List<User> usersAdded, Group userAddedIn, User addedBy) {
                if(userAddedIn!=null) updateGroup.setValue(userAddedIn);
            }
        });
    }

    public void removeListeners() {
        CometChat.removeUserListener(LISTENERS_TAG);
        CometChat.removeGroupListener(LISTENERS_TAG);
        CometChat.removeMessageListener(LISTENERS_TAG);
        CometChatGroupEvents.removeListener(LISTENERS_TAG);
    }

    public void setTypingIndicator(TypingIndicator typingIndicator, boolean show) {
        if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (id != null && id.equalsIgnoreCase(typingIndicator.getSender().getUid())) {
                typingHash.clear();
                typingHash.put(typingIndicator, show);
                typing.setValue(typingHash);
            }
        } else {
            if (id != null && id.equalsIgnoreCase(typingIndicator.getReceiverId())) {
                typingHash.clear();
                typingHash.put(typingIndicator, show);
                typing.setValue(typingHash);
            }
        }
    }
}
