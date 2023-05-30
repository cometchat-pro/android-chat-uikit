package com.cometchat.chatuikit.messages;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.events.CometChatUserEvents;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TransientMessage;
import com.cometchat.pro.models.User;

public class MessagesViewModel extends ViewModel {
    public MutableLiveData<Integer> sendLiveReaction;
    public MutableLiveData<Void> receiveLiveReaction;
    public String LISTENERS_TAG;
    public String id;
    private Void nothing;
    private MutableLiveData<Void> goBack;
    private MutableLiveData<User> updateUser;
    private MutableLiveData<Group> updateGroup;
    private User loggedInUser;
    private Group group;
    private User user;

    public MessagesViewModel() {
        this.sendLiveReaction = new MutableLiveData<>();
        this.receiveLiveReaction = new MutableLiveData<>();
        this.updateUser = new MutableLiveData<>();
        this.updateGroup = new MutableLiveData<>();
        this.goBack = new MutableLiveData<>();
        loggedInUser = CometChatUIKit.getLoggedInUser();
    }

    public MutableLiveData<Void> getReceiveLiveReaction() {
        return receiveLiveReaction;
    }

    public MutableLiveData<Void> getGoBack() {
        return goBack;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public MutableLiveData<User> getUpdateUser() {
        return updateUser;
    }

    public MutableLiveData<Group> getUpdateGroup() {
        return updateGroup;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addListener() {
        LISTENERS_TAG = System.currentTimeMillis() + "";
        CometChatMessageEvents.addListener(LISTENERS_TAG, new CometChatMessageEvents() {
            @Override
            public void ccLiveReaction(int icon) {
                receiveLiveReaction.setValue(nothing);
            }
        });

        CometChatUserEvents.addUserListener(LISTENERS_TAG, new CometChatUserEvents() {
            @Override
            public void ccUserBlocked(User user_) {
                if (user_ != null && user != null && user_.getUid() != null && user_.getUid().equalsIgnoreCase(user.getUid()))
                    updateUser.setValue(user_);
            }

            @Override
            public void ccUserUnblocked(User user_) {
                if (user_ != null && user != null && user_.getUid() != null && user_.getUid().equalsIgnoreCase(user.getUid()))
                    updateUser.setValue(user_);
            }
        });
        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupDeleted(Group group) {
                goBack.setValue(nothing);
            }

            @Override
            public void ccGroupLeft(Action actionMessage, User leftUser, Group leftGroup) {
                goBack.setValue(nothing);
            }

            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedUser != null && bannedUser.getUid().equalsIgnoreCase(loggedInUser.getUid()))
                    goBack.setValue(nothing);
            }

            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedUser != null && kickedUser.getUid().equalsIgnoreCase(loggedInUser.getUid()))
                    goBack.setValue(nothing);
            }
        });

        CometChat.addGroupListener(LISTENERS_TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group group_) {
                super.onGroupMemberKicked(action, kickedUser, kickedBy, group_);
                if (group != null && group_ != null) {
                    if (kickedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                        group.setHasJoined(false);
                        goBack.setValue(nothing);
                    }
                }
            }


            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group_) {
                super.onGroupMemberBanned(action, bannedUser, bannedBy, group_);
                if (group != null && group_ != null) {
                    if (bannedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                        goBack.setValue(nothing);
                    }
                }
            }
        });

        CometChat.addMessageListener(LISTENERS_TAG, new CometChat.MessageListener() {
            @Override
            public void onTransientMessageReceived(TransientMessage transientMessage) {
                if (transientMessage != null && transientMessage.getData() != null) {
                    if (transientMessage.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.USER) && transientMessage.getSender().getUid().equalsIgnoreCase(id)) {
                        receiveLiveReaction.setValue(nothing);
                    } else if (transientMessage.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.GROUP) && transientMessage.getReceiverId().equalsIgnoreCase(id)) {
                        receiveLiveReaction.setValue(nothing);
                    }
                }
            }
        });

    }

    public void removeListener() {
        CometChatMessageEvents.removeListener(LISTENERS_TAG);
        CometChat.removeMessageListener(LISTENERS_TAG);
        CometChatGroupEvents.removeListener(LISTENERS_TAG);
        CometChatUserEvents.removeListener(LISTENERS_TAG);
        CometChat.removeGroupListener(LISTENERS_TAG);
    }
}
