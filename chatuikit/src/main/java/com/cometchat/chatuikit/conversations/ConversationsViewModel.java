package com.cometchat.chatuikit.conversations;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.chatuikit.shared.events.CometChatConversationEvents;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.events.CometChatUserEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

enum ConversationDeleteState {
    INITIATED_DELETE, SUCCESS_DELETE, FAILURE_DELETE
}

public class ConversationsViewModel extends ViewModel {

    public ConversationsRequest conversationsRequest = null;

    public String LISTENERS_TAG;

    public MutableLiveData<List<Conversation>> mutableConversationList;

    public MutableLiveData<Integer> insertAtTop;

    public MutableLiveData<Integer> moveToTop;

    public List<Conversation> conversationList;

    public MutableLiveData<Integer> updateConversation;

    public MutableLiveData<Integer> removeConversation;

    public MutableLiveData<ConversationDeleteState> conversationDeleteState;

    public MutableLiveData<CometChatException> cometChatException;

    public MutableLiveData<UIKitConstants.States> states;

    public HashMap<Conversation, TypingIndicator> typingIndicatorHashMap;

    public MutableLiveData<HashMap<Conversation, TypingIndicator>> typing;

    public boolean disableReceipt;

    public int limit = 30;

    public boolean hasMore = true;

    public MutableLiveData<Boolean> playSound;

    public ConversationsViewModel() {
        mutableConversationList = new MutableLiveData<>();
        conversationList = new ArrayList<>();
        removeConversation = new MutableLiveData<>();
        updateConversation = new MutableLiveData<>();
        conversationDeleteState = new MutableLiveData<>();
        typingIndicatorHashMap = new HashMap<>();
        typing = new MutableLiveData<>();
        moveToTop = new MutableLiveData<>();
        insertAtTop = new MutableLiveData<>();
        states = new MutableLiveData<>();
        playSound = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
    }

    public Conversation typing(TypingIndicator typingIndicator) {
        for (Conversation conversation : conversationList) {
            if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (conversation.getConversationId().contains(typingIndicator.getSender().getUid()))
                    return conversation;
            } else {
                if (conversation.getConversationId().contains(typingIndicator.getReceiverId()))
                    return conversation;
            }
        }
        return null;
    }

    public MutableLiveData<Integer> remove() {
        return removeConversation;
    }

    public MutableLiveData<List<Conversation>> getMutableConversationList() {
        return mutableConversationList;
    }

    public MutableLiveData<HashMap<Conversation, TypingIndicator>> getTyping() {
        return typing;
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public MutableLiveData<Integer> insertAtTop() {
        return insertAtTop;
    }

    public MutableLiveData<Integer> moveToTop() {
        return moveToTop;
    }

    public MutableLiveData<Integer> updateConversation() {
        return updateConversation;
    }

    public MutableLiveData<ConversationDeleteState> progressState() {
        return conversationDeleteState;
    }

    public MutableLiveData<Boolean> playSound() {
        return playSound;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public void disableReceipt(boolean disableReceipt) {
        this.disableReceipt = disableReceipt;
    }

    private Conversation convertMessageToConversation(BaseMessage message) {
        if (message != null)
            return CometChatHelper.getConversationFromMessage(message);
        return null;
    }

    public void update(Conversation conversation, boolean isActionMessage) {
        if(conversation!=null) {
            if (conversationList.contains(conversation)) {
                Conversation oldConversation = conversationList.get(conversationList.indexOf(conversation));
                conversation.setConversationWith(oldConversation.getConversationWith());
                JSONObject metadata = conversation.getLastMessage().getMetadata();
                boolean incrementUnreadCount = false;
                boolean isCategoryMessage = conversation.getLastMessage().getCategory().equalsIgnoreCase(CometChatConstants.CATEGORY_MESSAGE);
                try {
                    if (metadata.has("incrementUnreadCount"))
                        incrementUnreadCount = metadata.getBoolean("incrementUnreadCount");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int index = conversationList.indexOf(oldConversation);
                if (!conversation.getLastMessage().getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid())) {
                    if (conversation.getLastMessage().getReadAt() == 0) {
                        if (isActionMessage) {
                            conversation.setUnreadMessageCount(oldConversation.getUnreadMessageCount());
                            updateConversationObject(oldConversation, conversation);
                        } else if (incrementUnreadCount || isCategoryMessage) {
                            handleUnreadCount(oldConversation, conversation, false);
                        }
                    } else {
                        conversation.setUnreadMessageCount(0);
                        conversationList.set(index, conversation);
                        updateConversation.setValue(index);
                    }
                } else {
                    handleUnreadCount(oldConversation, conversation, true);
                }
            } else {
                itemInsertedAtTop(conversation);
            }
        }
    }


    private void itemInsertedAtTop(Conversation conversation) {
        conversation.setUnreadMessageCount(1);
        conversationList.add(0, conversation);
        insertAtTop.setValue(0);
    }

    private void handleUnreadCount(Conversation oldConversation, Conversation conversation, boolean isSent) {
        if ((oldConversation.getLastMessage().getId() != conversation.getLastMessage().getId())) {
            if (conversation.getLastMessage().getEditedAt() != 0 || conversation.getLastMessage().getDeletedAt() != 0) {
                return;
            }
            if (isSent) conversation.setUnreadMessageCount(0);
            else conversation.setUnreadMessageCount(oldConversation.getUnreadMessageCount() + 1);
        } else {
            conversation.setUnreadMessageCount(oldConversation.getUnreadMessageCount());
        }
        updateConversationObject(oldConversation, conversation);
    }

    private void updateConversationObject(Conversation oldConversation, Conversation conversation) {
        int oldIndex = conversationList.indexOf(oldConversation);
        conversationList.remove(oldConversation);
        conversationList.add(0, conversation);
        moveToTop.setValue(oldIndex);
    }

    public void addListener() {
        LISTENERS_TAG = System.currentTimeMillis() + "";

        CometChat.addMessageListener(LISTENERS_TAG, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage message) {
                markAsDeliverInternally(message);
                update(convertMessageToConversation(message), false);
                playSound.setValue(true);
            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                markAsDeliverInternally(message);
                update(convertMessageToConversation(message), false);
                playSound.setValue(true);
            }

            @Override
            public void onCustomMessageReceived(CustomMessage message) {
                markAsDeliverInternally(message);
                update(convertMessageToConversation(message), false);
                playSound.setValue(true);
            }

            @Override
            public void onMessagesDelivered(MessageReceipt messageReceipt) {
                setDeliveredReceipts(messageReceipt);
            }

            @Override
            public void onMessagesRead(MessageReceipt messageReceipt) {
                setReadReceipts(messageReceipt);
            }

            @Override
            public void onMessageEdited(BaseMessage message) {
                update(convertMessageToConversation(message), false);
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                update(convertMessageToConversation(message), false);
            }

            @Override
            public void onTypingStarted(TypingIndicator typingIndicator) {
                typingIndicatorHashMap.put(typing(typingIndicator), typingIndicator);
                typing.setValue(typingIndicatorHashMap);
            }

            @Override
            public void onTypingEnded(TypingIndicator typingIndicator) {
                Conversation conversation = typing(typingIndicator);
                typingIndicatorHashMap.put(conversation, null);
                typing.setValue(typingIndicatorHashMap);
                typingIndicatorHashMap.remove(conversation);
            }
        });

        CometChat.addGroupListener(LISTENERS_TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedUser.getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                    updateConversationForGroup(action, true);
                } else {
                    updateConversationForGroup(action, false);
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedBy, User userAdded, Group addedTo) {
                addedTo.setScope(CometChatConstants.SCOPE_PARTICIPANT);
                action.setActionFor(addedTo);
                updateConversationForGroup(action, false);
            }

            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                updateConversationForGroup(action, false);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                updateConversationForGroup(action, leftUser.getUid().equals(CometChatUIKit.getLoggedInUser().getUid()));
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                updateConversationForGroup(action, false);
            }
        });

        CometChat.addUserListener(LISTENERS_TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                updateUserConversation(user);
            }

            @Override
            public void onUserOffline(User user) {
                updateUserConversation(user);
            }
        });

        CometChatConversationEvents.addListener(LISTENERS_TAG, new CometChatConversationEvents() {
            @Override
            public void ccConversationDeleted(Conversation conversation) {
                remove(conversation);
            }
        });

        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupLeft(Action actionMessage, User leftUser, Group leftGroup) {
                if (leftUser.getUid().equals(CometChatUIKit.getLoggedInUser().getUid()))
                    removeGroup(leftGroup);
            }

            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom != null) {
                    updateGroupConversation(bannedFrom);
                    updateConversationForGroup(actionMessage, false);
                }
            }

            @Override
            public void ccGroupMemberAdded(List<Action> actionMessages, List<User> usersAdded, Group userAddedIn, User addedBy) {
                if (userAddedIn != null) {
                    updateGroupConversation(userAddedIn);
                    for (Action action : actionMessages) {
                        updateConversationForGroup(action, false);
                    }
                }
            }

            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom != null) {
                    updateGroupConversation(kickedFrom);
                    updateConversationForGroup(actionMessage, false);
                }
            }

            @Override
            public void ccGroupMemberJoined(User joinedUser, Group joinedGroup) {
                if (joinedGroup != null) updateGroupConversation(joinedGroup);
            }

            @Override
            public void ccGroupDeleted(Group group) {
                if (group != null) removeGroup(group);
            }

            @Override
            public void ccOwnershipChanged(Group group, GroupMember newOwner) {
                if (group != null) {
                    updateGroupConversation(group);
                }
            }

            @Override
            public void ccGroupMemberScopeChanged(Action actionMessage, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                if (group != null) {
                    updateGroupConversation(group);
                    updateConversationForGroup(actionMessage, false);
                }
            }

            @Override
            public void ccGroupMemberUnBanned(Action actionMessage, User unbannedUser, User unBannedBy, Group unBannedFrom) {
                if (unBannedFrom != null) {
                    updateGroupConversation(unBannedFrom);
                    updateConversationForGroup(actionMessage, false);
                }
            }
        });

        CometChatUserEvents.addUserListener(LISTENERS_TAG, new CometChatUserEvents() {
            @Override
            public void ccUserBlocked(User user) {
                removeUser(user);
            }
        });

        CometChatMessageEvents.addListener(LISTENERS_TAG, new CometChatMessageEvents() {
            @Override
            public void ccMessageSent(BaseMessage baseMessage, int status) {
                if (status == MessageStatus.SUCCESS && baseMessage != null) {
                    update(convertMessageToConversation(baseMessage), false);
                }
            }

            @Override
            public void ccMessageRead(BaseMessage baseMessage) {
                if (baseMessage != null) update(convertMessageToConversation(baseMessage), false);
            }

            @Override
            public void ccMessageEdited(BaseMessage baseMessage, int status) {
                if (status == MessageStatus.SUCCESS && baseMessage != null)
                    update(convertMessageToConversation(baseMessage), false);
            }

            @Override
            public void ccMessageDeleted(BaseMessage baseMessage) {
                if (baseMessage != null)
                    update(convertMessageToConversation(baseMessage), false);
            }
        });

        CometChatCallEvents.addListener(LISTENERS_TAG, new CometChatCallEvents() {
            @Override
            public void ccOutgoingCall(Call call) {
                update(convertMessageToConversation(call), false);
            }

            @Override
            public void ccCallAccepted(Call call) {
                update(convertMessageToConversation(call), false);
            }

            @Override
            public void ccCallRejected(Call call) {
                update(convertMessageToConversation(call), false);
            }

            @Override
            public void ccCallEnded(Call call) {
                update(convertMessageToConversation(call), false);
            }

            @Override
            public void ccMessageSent(BaseMessage baseMessage, int status) {
                if (status == MessageStatus.SUCCESS && baseMessage != null) {
                    update(convertMessageToConversation(baseMessage), false);
                }
            }
        });
    }

    public void removeListener() {
        CometChat.removeUserListener(LISTENERS_TAG);
        CometChat.removeMessageListener(LISTENERS_TAG);
        CometChat.removeGroupListener(LISTENERS_TAG);
        CometChatConversationEvents.removeListener(LISTENERS_TAG);
        CometChatGroupEvents.removeListener(LISTENERS_TAG);
        CometChatUserEvents.removeListener(LISTENERS_TAG);
        CometChatCallEvents.removeListener(LISTENERS_TAG);
    }

    private void updateConversationForGroup(BaseMessage baseMessage, boolean isRemove) {
        Conversation conversation = CometChatHelper.getConversationFromMessage(baseMessage);
        if (isRemove) remove(conversation);
        else update(conversation, true);
    }

    private void markAsDeliverInternally(BaseMessage message) {
        if (!message.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()) && !disableReceipt)
            CometChat.markAsDelivered(message);
    }

    public void remove(Conversation conversation) {
        int oldIndex = conversationList.indexOf(conversation);
        conversationList.remove(conversation);
        removeConversation.setValue(oldIndex);
        states.setValue(checkIsEmpty(conversationList));
    }

    public void setDeliveredReceipts(MessageReceipt deliveryReceipts) {
        for (int i = 0; i < conversationList.size() - 1; i++) {
            Conversation conversation = conversationList.get(i);
            if (conversation.getConversationType().equals(CometChatConstants.RECEIVER_TYPE_USER) && deliveryReceipts.getSender().getUid().equals(((User) conversation.getConversationWith()).getUid())) {
                BaseMessage baseMessage = conversationList.get(i).getLastMessage();
                if (baseMessage != null && baseMessage.getDeliveredAt() == 0) {
                    baseMessage.setDeliveredAt(deliveryReceipts.getDeliveredAt());
                    int index = conversationList.indexOf(conversationList.get(i));
                    conversationList.remove(index);
                    conversation.setLastMessage(baseMessage);
                    conversationList.add(index, conversation);
                    updateConversation.setValue(conversationList.indexOf(conversation));
                }
            }
        }
    }

    public void setReadReceipts(MessageReceipt readReceipts) {
        for (int i = 0; i < conversationList.size() - 1; i++) {
            Conversation conversation = conversationList.get(i);
            if (conversation.getConversationType().equals(CometChatConstants.RECEIVER_TYPE_USER) && readReceipts.getSender().getUid().equals(((User) conversation.getConversationWith()).getUid())) {
                BaseMessage baseMessage = conversationList.get(i).getLastMessage();
                if (baseMessage != null && baseMessage.getReadAt() == 0) {
                    baseMessage.setReadAt(readReceipts.getReadAt());
                    int index = conversationList.indexOf(conversationList.get(i));
                    conversationList.remove(index);
                    conversation.setLastMessage(baseMessage);
                    conversationList.add(index, conversation);
                    updateConversation.setValue(conversationList.indexOf(conversation));
                }
            }
        }
    }

    public void deleteConversation(Conversation conversation) {
        if (conversation != null) {
            conversationDeleteState.setValue(ConversationDeleteState.INITIATED_DELETE);
            String conversationUid = "";
            String type = "";
            if (conversation.getConversationType().equalsIgnoreCase(CometChatConstants.CONVERSATION_TYPE_GROUP)) {
                conversationUid = ((Group) conversation.getConversationWith()).getGuid();
                type = CometChatConstants.CONVERSATION_TYPE_GROUP;
            } else {
                conversationUid = ((User) conversation.getConversationWith()).getUid();
                type = CometChatConstants.CONVERSATION_TYPE_USER;
            }
            String finalConversationUid = conversationUid;
            String finalType = type;
            CometChat.deleteConversation(finalConversationUid, finalType, new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    conversationDeleteState.setValue(ConversationDeleteState.SUCCESS_DELETE);
                    for (CometChatConversationEvents e : CometChatConversationEvents.conversationEvents.values()) {
                        e.ccConversationDeleted(conversation);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    conversationDeleteState.setValue(ConversationDeleteState.FAILURE_DELETE);
                    cometChatException.setValue(e);
                    e.printStackTrace();
                }
            });
        }
    }

    public void fetchConversation() {
        if (conversationsRequest == null) {
            conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().setLimit(limit).build();
        }
        if (hasMore) {
            if (conversationList.size() == 0) states.setValue(UIKitConstants.States.LOADING);
            conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
                @Override
                public void onSuccess(List<Conversation> conversations) {
                    hasMore = conversations.size() != 0;
                    if (hasMore) addList(conversations);
                    states.setValue(UIKitConstants.States.LOADED);
                    states.setValue(checkIsEmpty(conversationList));
                }

                @Override
                public void onError(CometChatException e) {
                    cometChatException.setValue(e);
                    states.setValue(UIKitConstants.States.ERROR);
                }
            });
        }
    }

    public void addList(List<Conversation> conversations) {
        for (int i = 0; i < conversations.size(); i++) {
            if (conversationList.contains(conversations.get(i))) {
                int index = conversationList.indexOf(conversations.get(i));
                conversationList.remove(conversations.get(i));
                conversationList.add(index, conversations.get(i));
            } else {
                conversationList.add(conversations.get(i));
            }
        }
        mutableConversationList.setValue(conversationList);
    }

    public void updateGroupConversation(Group group) {
        for (int i = 0; i < conversationList.size(); i++) {
            Conversation conversation = conversationList.get(i);
            if (conversation.getConversationType().equalsIgnoreCase(UIKitConstants.ConversationType.GROUPS)) {
                Group conversationGroup = ((Group) conversation.getConversationWith());
                if (conversationGroup != null && group != null && conversationGroup.getGuid().equals(group.getGuid())) {
                    conversation.setConversationWith(group);
                    updateConversation.setValue(conversationList.indexOf(conversation));
                    break;
                }
            }
        }
    }

    public void updateUserConversation(User user) {
        for (int i = 0; i < conversationList.size(); i++) {
            Conversation conversation = conversationList.get(i);
            if (conversation.getConversationType().equalsIgnoreCase(UIKitConstants.ConversationType.USERS)) {
                User userConversation = ((User) conversation.getConversationWith());
                if (userConversation != null && user != null && userConversation.getUid().equals(user.getUid())) {
                    userConversation.setStatus(user.getStatus());
                    conversation.setConversationWith(userConversation);
                    updateConversation.setValue(conversationList.indexOf(conversation));
                    break;
                }
            }
        }
    }

    public void removeGroup(Group group) {
        for (int i = 0; i < conversationList.size(); i++) {
            Conversation conversation = conversationList.get(i);
            if (conversation.getConversationType().equalsIgnoreCase(UIKitConstants.ConversationType.GROUPS)) {
                Group conversationGroup = ((Group) conversation.getConversationWith());
                if (conversationGroup != null && group != null && conversationGroup.getGuid().equals(group.getGuid())) {
                    remove(conversation);
                    break;
                }
            }
        }
    }

    public void removeUser(User user) {
        for (int i = 0; i < conversationList.size(); i++) {
            Conversation conversation = conversationList.get(i);
            if (conversation.getConversationType().equalsIgnoreCase(UIKitConstants.ConversationType.USERS)) {
                User conversationUser = ((User) conversation.getConversationWith());
                if (conversationUser != null && user != null && conversationUser.getUid().equals(conversationUser.getUid())) {
                    remove(conversation);
                    break;
                }
            }
        }
    }

    public void setConversationsRequestBuilder(ConversationsRequest conversationsRequest) {
        this.conversationsRequest = conversationsRequest;
    }

    private UIKitConstants.States checkIsEmpty(List<Conversation> conversations) {
        if (conversations.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

}
