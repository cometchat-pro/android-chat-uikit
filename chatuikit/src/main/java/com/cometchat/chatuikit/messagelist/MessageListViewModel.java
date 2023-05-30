package com.cometchat.chatuikit.messagelist;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKitHelper;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

enum MessageDeleteState {
    INITIATED_DELETE, SUCCESS_DELETE, FAILURE_DELETE
}

public class MessageListViewModel extends ViewModel {

    private MessagesRequest.MessagesRequestBuilder messagesRequestBuilder = null;

    private MessagesRequest messagesRequest;

    private String LISTENERS_TAG;

    private MutableLiveData<List<BaseMessage>> mutableMessageList;

    private MutableLiveData<Integer> mutableMessagesRangeChanged;

    private List<BaseMessage> messageArrayList;

    private MutableLiveData<Integer> updateMessage;

    private MutableLiveData<Integer> removeMessage;

    private MutableLiveData<BaseMessage> addMessage;

    private MutableLiveData<BaseMessage> readMessage;

    private MutableLiveData<MessageDeleteState> messageDeleteState;

    private MutableLiveData<CometChatException> cometChatException;

    private MutableLiveData<UIKitConstants.States> states;

    private int limit = 30;

    public boolean firstFetch = true;

    private boolean hasMore = true;

    private MutableLiveData<Boolean> mutableHasMore;

    private boolean isInProgress;

    private boolean disableReceipt;

    private MutableLiveData<Boolean> mutableIsInProgress;

    private Group group;

    private User user;

    private String id;

    private String type;

    private boolean isBlockedByMe;

    private boolean hideDeleteMessage;

    private MutableLiveData<Void> notifyUpdate;

    private Void unused = null;

    private List<String> messagesTypes;

    private List<String> messagesCategories;

    private int parentMessageId = -1;

    public Void aVoid;

    public HashMap<String, String> idMap;

    public MutableLiveData<HashMap<String, String>> mutableHashMap;

    public MutableLiveData<Function1<Context, View>> showTopPanel;

    public MutableLiveData<Function1<Context, View>> showBottomPanel;

    public MutableLiveData<Void> closeBottomPanel;

    public MutableLiveData<Void> closeTopPanel;

    public MessageListViewModel() {
        mutableMessageList = new MutableLiveData<>();
        mutableMessagesRangeChanged = new MutableLiveData<>();
        updateMessage = new MutableLiveData<>();
        removeMessage = new MutableLiveData<>();
        readMessage = new MutableLiveData<>();
        addMessage = new MutableLiveData<>();
        messageDeleteState = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        mutableIsInProgress = new MutableLiveData<>();
        mutableHasMore = new MutableLiveData<>();
        states = new MutableLiveData<>();
        messageArrayList = new ArrayList<>();
        messagesTypes = new ArrayList<>();
        messagesCategories = new ArrayList<>();
        notifyUpdate = new MutableLiveData<>();
        mutableHashMap = new MutableLiveData<>();
        closeBottomPanel = new MutableLiveData<>();
        closeTopPanel = new MutableLiveData<>();
        showTopPanel = new MutableLiveData<>();
        showBottomPanel = new MutableLiveData<>();
        idMap = new HashMap<>();
    }

    public MutableLiveData<List<BaseMessage>> getMutableMessageList() {
        return mutableMessageList;
    }

    public MutableLiveData<Integer> messagesRangeChanged() {
        return mutableMessagesRangeChanged;
    }

    public MutableLiveData<Integer> updateMessage() {
        return updateMessage;
    }

    public MutableLiveData<Integer> removeMessage() {
        return removeMessage;
    }

    public MutableLiveData<BaseMessage> addMessage() {
        return addMessage;
    }

    public MutableLiveData<MessageDeleteState> getMessageDeleteState() {
        return messageDeleteState;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public MutableLiveData<BaseMessage> getReadMessage() {
        return readMessage;
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public MutableLiveData<Boolean> getMutableIsInProgress() {
        return mutableIsInProgress;
    }

    public MutableLiveData<Boolean> getMutableHasMore() {
        return mutableHasMore;
    }

    public MutableLiveData<Void> notifyUpdate() {
        return notifyUpdate;
    }

    public MutableLiveData<HashMap<String, String>> getMutableHashMap() {
        return mutableHashMap;
    }

    public MutableLiveData<Void> closeBottomPanel() {
        return closeBottomPanel;
    }

    public MutableLiveData<Void> closeTopPanel() {
        return closeTopPanel;
    }

    public MutableLiveData<Function1<Context, View>> showTopPanel() {
        return showTopPanel;
    }

    public MutableLiveData<Function1<Context, View>> showBottomPanel() {
        return showBottomPanel;
    }

    public void setGroup(Group group, List<String> messagesTypes, List<String> messagesCategories) {
        if (group != null) {
            this.group = group;
            this.type = UIKitConstants.ReceiverType.GROUP;
            this.id = group.getGuid();
            this.messagesTypes = messagesTypes;
            this.messagesCategories = messagesCategories;
            setIdMap();
        }
        initializeGroupRequestBuilder();
    }

    public void setUser(User user, List<String> messagesTypes, List<String> messagesCategories) {
        if (user != null) {
            this.user = user;
            this.id = user.getUid();
            this.type = UIKitConstants.ReceiverType.USER;
            this.isBlockedByMe = user.isBlockedByMe();
            this.messagesTypes = messagesTypes;
            this.messagesCategories = messagesCategories;
            setIdMap();
        }
        initializeUserRequestBuilder();
    }


    private void initializeUserRequestBuilder() {
        if (messagesRequestBuilder == null)
            messagesRequestBuilder = new MessagesRequest.MessagesRequestBuilder().setTypes(this.messagesTypes).setLimit(limit).setCategories(this.messagesCategories);
        messagesRequest = messagesRequestBuilder.setUID(id).build();
    }

    private void initializeGroupRequestBuilder() {
        if (messagesRequestBuilder == null)
            messagesRequestBuilder = new MessagesRequest.MessagesRequestBuilder().setTypes(this.messagesTypes).setLimit(limit).setCategories(this.messagesCategories);
        messagesRequest = messagesRequestBuilder.setGUID(id).build();
    }

    public void setMessagesTypesAndCategories(List<String> messagesTypes, List<String> messagesCategories) {
        this.messagesTypes = messagesTypes;
        this.messagesCategories = messagesCategories;
        messageArrayList.clear();
        if (user != null) initializeUserRequestBuilder();
        else if (group != null) initializeGroupRequestBuilder();
    }

    public void setIdMap() {
        if (parentMessageId > 0)
            idMap.put(UIKitConstants.MapId.PARENT_MESSAGE_ID, String.valueOf(parentMessageId));
        if (user != null) {
            idMap.put(UIKitConstants.MapId.RECEIVER_ID, user.getUid());
            idMap.put(UIKitConstants.MapId.RECEIVER_TYPE, UIKitConstants.ReceiverType.USER);
        } else if (group != null) {
            idMap.put(UIKitConstants.MapId.RECEIVER_ID, group.getGuid());
            idMap.put(UIKitConstants.MapId.RECEIVER_TYPE, UIKitConstants.ReceiverType.GROUP);
        }
        mutableHashMap.setValue(idMap);
    }

    public void addListener() {
        LISTENERS_TAG = System.currentTimeMillis() + "";

        CometChat.addMessageListener(LISTENERS_TAG, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage message) {
                onMessageReceived(message);
            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                onMessageReceived(message);
            }

            @Override
            public void onCustomMessageReceived(CustomMessage message) {
                onMessageReceived(message);
            }

            @Override
            public void onMessagesDelivered(MessageReceipt messageReceipt) {
                setMessageReceipt(messageReceipt);
            }

            @Override
            public void onMessagesRead(MessageReceipt messageReceipt) {
                setMessageReceipt(messageReceipt);
            }

            @Override
            public void onMessageEdited(BaseMessage message) {
                updateMessage(message);
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                if (hideDeleteMessage) removeMessage(message);
                else updateMessage(message);
            }
        });

        CometChat.addGroupListener(LISTENERS_TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group unbannedFrom) {
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                onMessageReceived(action);
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                onMessageReceived(action);
            }
        });

        CometChatMessageEvents.addListener(LISTENERS_TAG, new CometChatMessageEvents() {
            @Override
            public void ccMessageSent(BaseMessage message, int status) {
                if (status == MessageStatus.IN_PROGRESS) setMessage(message);
                else if (status == MessageStatus.SUCCESS || status == MessageStatus.ERROR)
                    updateOptimisticMessage(message);
            }

            @Override
            public void ccMessageEdited(BaseMessage baseMessage, @MessageStatus int status) {
                if (status == MessageStatus.SUCCESS) updateMessage(baseMessage);
            }

            @Override
            public void ccMessageDeleted(BaseMessage baseMessage) {
                if (hideDeleteMessage) removeMessage(baseMessage);
                else updateMessage(baseMessage);
            }
        });

        CometChatGroupEvents.addGroupListener(LISTENERS_TAG, new CometChatGroupEvents() {
            @Override
            public void ccGroupMemberKicked(Action actionMessage, User kickedUser, User kickedBy, Group kickedFrom) {
                onMessageReceived(actionMessage);
            }

            @Override
            public void ccGroupMemberBanned(Action actionMessage, User bannedUser, User bannedBy, Group bannedFrom) {
                onMessageReceived(actionMessage);
            }

            @Override
            public void ccOwnershipChanged(Group group, GroupMember newOwner) {
            }

            @Override
            public void ccGroupMemberScopeChanged(Action actionMessage, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                onMessageReceived(actionMessage);
            }

            @Override
            public void ccGroupMemberUnBanned(Action actionMessage, User unbannedUser, User unBannedBy, Group unBannedFrom) {
                onMessageReceived(actionMessage);
            }

            @Override
            public void ccGroupMemberAdded(List<Action> actionMessages, List<User> usersAdded, Group userAddedIn, User addedBy) {
                for (Action action : actionMessages) {
                    onMessageReceived(action);
                }
            }
        });

        CometChatUIEvents.addListener(LISTENERS_TAG, new CometChatUIEvents() {
            @Override
            public void showPanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment, Function1<Context, View> view) {
                if (UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM.equals(alignment) && idMap.equals(id))
                    showBottomPanel.setValue(view);
                else if (UIKitConstants.CustomUIPosition.MESSAGE_LIST_TOP.equals(alignment) && idMap.equals(id))
                    showTopPanel.setValue(view);
            }

            @Override
            public void hidePanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment) {
                if (UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM.equals(alignment) && idMap.equals(id))
                    closeBottomPanel.setValue(aVoid);
                else if (UIKitConstants.CustomUIPosition.MESSAGE_LIST_TOP.equals(alignment) && idMap.equals(id))
                    closeTopPanel.setValue(aVoid);
            }
        });
        if (isCallingAdded()) {
            CometChatCallEvents.addListener(LISTENERS_TAG, new CometChatCallEvents() {
                @Override
                public void ccOutgoingCall(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void ccCallAccepted(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void ccCallRejected(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void ccCallEnded(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void ccMessageSent(BaseMessage message, int status) {
                    if (status == MessageStatus.IN_PROGRESS) setMessage(message);
                    if (status == MessageStatus.SUCCESS || status == MessageStatus.ERROR)
                        updateOptimisticMessage(message);
                }
            });

            CometChat.addCallListener(LISTENERS_TAG, new CometChat.CallListener() {
                @Override
                public void onIncomingCallReceived(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void onOutgoingCallAccepted(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void onOutgoingCallRejected(Call call) {
                    onMessageReceived(call);
                }

                @Override
                public void onIncomingCallCancelled(Call call) {
                    onMessageReceived(call);
                }
            });
        }
    }

    public boolean isCallingAdded() {
        return messagesCategories.contains(CometChatConstants.CATEGORY_CALL) && (messagesTypes.contains(CometChatConstants.CALL_TYPE_VIDEO) || messagesTypes.contains(CometChatConstants.CALL_TYPE_AUDIO));
    }

    private void setMessage(BaseMessage message) {
        if (message.getParentMessageId() == 0 && parentMessageId == -1) {
            addMessage(message);
        } else if (parentMessageId > -1 && parentMessageId == message.getParentMessageId()) {
            addMessage(message);
        } else {
            updateReplyCount(message.getParentMessageId());
        }
    }

    public void hideDeleteMessages(boolean hide) {
        this.hideDeleteMessage = hide;
    }

    public void updateOptimisticMessage(BaseMessage baseMessage) {
        if (baseMessage != null) {
            updateMessageFromMuid(baseMessage);
            if (!messageArrayList.isEmpty() && messageArrayList.contains(baseMessage)) {
                messageArrayList.set(messageArrayList.indexOf(baseMessage), baseMessage);
            } else {
                if (baseMessage.getParentMessageId() == 0 || parentMessageId > -1) {
                    addMessage(baseMessage);
                }
            }
            checkIsEmpty(messageArrayList);
        }
    }

    public void onMessageEdit(BaseMessage baseMessage) {
        CometChatUIKitHelper.onMessageEdited(baseMessage, MessageStatus.IN_PROGRESS);
    }

    public HashMap<String, String> getIdMap() {
        HashMap<String, String> idMap = new HashMap<>();
        if (parentMessageId > 0)
            idMap.put(UIKitConstants.MapId.PARENT_MESSAGE_ID, String.valueOf(parentMessageId));
        if (user != null) {
            idMap.put(UIKitConstants.MapId.RECEIVER_ID, user.getUid());
            idMap.put(UIKitConstants.MapId.RECEIVER_TYPE, UIKitConstants.ReceiverType.USER);
        } else if (group != null) {
            idMap.put(UIKitConstants.MapId.RECEIVER_ID, group.getGuid());
            idMap.put(UIKitConstants.MapId.RECEIVER_TYPE, UIKitConstants.ReceiverType.GROUP);
        }
        return idMap;
    }

    public void setParentMessageId(int message) {
        parentMessageId = message;
        setIdMap();
    }

    public void updateMessageFromMuid(BaseMessage baseMessage) {
        for (int i = messageArrayList.size() - 1; i >= 0; i--) {
            String mUid = messageArrayList.get(i).getMuid();
            if (mUid != null && mUid.equals(baseMessage.getMuid())) {
                messageArrayList.remove(i);
                messageArrayList.add(i, baseMessage);
                updateMessage.setValue(i);
            }
        }
    }

    public void updateMessage(BaseMessage message) {
        if (message != null) {
            if (messageArrayList.contains(message)) {
                int index = messageArrayList.indexOf(message);
                BaseMessage oldMessage = messageArrayList.get(index);
                messageArrayList.remove(message);
                JSONObject metaData = message.getMetadata();
                try {
                    if (oldMessage.getMetadata() != null) {
                        if (oldMessage.getMetadata().has("values"))
                            metaData.accumulate("values", oldMessage.getMetadata().get("values"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                messageArrayList.add(index, message);
                updateMessage.setValue(index);
            }
        }
    }

    public void updateReplyCount(int parentMessageId) {
        for (int i = 0; i < messageArrayList.size(); i++) {
            BaseMessage baseMessage = messageArrayList.get(i);
            int replyCount = baseMessage.getReplyCount();
            if (baseMessage.getId() == parentMessageId) {
                baseMessage.setReplyCount(++replyCount);
                updateMessage(baseMessage);
                break;
            }
        }
    }

    public void setMessageReceipt(MessageReceipt messageReceipt) {
        if (messageReceipt != null && messageReceipt.getReceiptType() != null && messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (id != null && messageReceipt.getSender() != null && messageReceipt.getSender().getUid().equals(id)) {
                if (messageReceipt.getReceiptType().equals(MessageReceipt.RECEIPT_TYPE_DELIVERED))
                    setDeliveryReceipts(messageReceipt);
                else setReadReceipts(messageReceipt);
            }
        }
    }

    public void setDeliveryReceipts(MessageReceipt messageReceipt) {
        for (int i = messageArrayList.size() - 1; i >= 0; i--) {
            BaseMessage baseMessage = messageArrayList.get(i);
            if (baseMessage.getDeliveredAt() == 0) {
                int index = messageArrayList.indexOf(baseMessage);
                messageArrayList.get(index).setDeliveredAt(messageReceipt.getDeliveredAt());
                break;
            }
        }
        notifyUpdate.setValue(unused);
    }

    public void setReadReceipts(MessageReceipt messageReceipt) {
        boolean isRead = false;
        for (int i = messageArrayList.size() - 1; i >= 0; i--) {
            BaseMessage baseMessage = messageArrayList.get(i);
            if (baseMessage.getReadAt() == 0 || baseMessage.getId() == messageReceipt.getMessageId()) {
                isRead = true;
                int index = messageArrayList.indexOf(baseMessage);
                messageArrayList.get(index).setReadAt(messageReceipt.getReadAt());
            } else if (isRead) {
                break;
            }
        }
        notifyUpdate.setValue(unused);
    }

    public void fetchMessages() {
        if (messagesRequestBuilder != null && messagesRequest != null) {
            if (messageArrayList.size() == 0) states.setValue(UIKitConstants.States.LOADING);
            if (hasMore) {
                messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                    @Override
                    public void onSuccess(List<BaseMessage> messageList) {
                        mutableIsInProgress.setValue(false);
                        hasMore = messageList.size() != 0;
                        mutableHasMore.setValue(hasMore);
                        if (hasMore) addList(messageList);
                        states.setValue(UIKitConstants.States.LOADED);
                        states.setValue(checkIsEmpty(messageArrayList));
                        if (firstFetch) {
                            for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                                if (messageList.size() > 0)
                                    events.ccActiveChatChanged(getIdMap(), messageList.get(messageList.size() - 1), user, group);
                                else events.ccActiveChatChanged(getIdMap(), null, user, group);
                            }
                            firstFetch = false;
                        }
                    }

                    @Override
                    public void onError(CometChatException exception) {
                        cometChatException.setValue(exception);
                        states.setValue(UIKitConstants.States.LOADED);
                        states.setValue(UIKitConstants.States.ERROR);
                    }
                });
            }
        }
    }

    public void addList(List<BaseMessage> messageList) {
        if (messageArrayList.size() == 0) {
            this.messageArrayList.addAll(0, messageList);
            mutableMessageList.setValue(messageArrayList);
        } else {
            this.messageArrayList.addAll(0, messageList);
            mutableMessagesRangeChanged.setValue(messageList.size());
        }
    }

    private UIKitConstants.States checkIsEmpty(List<BaseMessage> baseMessageList) {
        if (baseMessageList.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

    public void markLastMessageAsRead(BaseMessage lastMessage) {
        boolean markAsRead = false;
        if (lastMessage != null) {
            if (lastMessage.getReadAt() == 0 && lastMessage.getParentMessageId() == 0)
                markAsRead = true;
            else if (parentMessageId > -1 && parentMessageId == lastMessage.getParentMessageId() && lastMessage.getReadAt() == 0)
                markAsRead = true;

            if (markAsRead && !disableReceipt) {
                if (type != null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (!lastMessage.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()))
                        markMessageAsRead(lastMessage);
                } else {
                    if (lastMessage.getSender() != null && lastMessage.getSender().getUid() != null && lastMessage.getSender().getUid().equalsIgnoreCase(id))
                        markMessageAsRead(lastMessage);
                }
            }
        }
    }

    public void disableReceipt(boolean disableReceipt) {
        this.disableReceipt = disableReceipt;
    }

    public void markMessageAsRead(BaseMessage lastMessage) {
        CometChat.markAsRead(lastMessage, new CometChat.CallbackListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                lastMessage.setReadAt(System.currentTimeMillis() / 1000);
                readMessage.setValue(lastMessage);
                updateMessage(lastMessage);
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public void markAsDeliverInternally(BaseMessage message) {
        if (message != null && !message.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()) && !disableReceipt)
            CometChat.markAsDelivered(message);
    }

    public void removeMessage(BaseMessage message) {
        if (message != null) {
            int index = messageArrayList.indexOf(message);
            removeMessage.setValue(index);
            messageArrayList.remove(message);
            states.setValue(checkIsEmpty(messageArrayList));
        }
    }

    public void addMessage(BaseMessage message) {
        if (message != null) {
            if (messageArrayList.size() == 0) addList(messageArrayList);
            messageArrayList.add(message);
            addMessage.setValue(message);
            states.setValue(checkIsEmpty(messageArrayList));
        }
    }

    public void onMessageReceived(BaseMessage message) {
        if (message != null) {
            markAsDeliverInternally(message);
            if (message.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (id != null && id.equalsIgnoreCase(message.getSender().getUid())) {
                    setMessage(message);
                } else if (id != null && id.equalsIgnoreCase(message.getReceiverUid()) && message.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid())) {
                    setMessage(message);
                }
            } else {
                if (id != null && id.equalsIgnoreCase(message.getReceiverUid())) {
                    setMessage(message);
                }
            }
        }
    }

    public void setMessagesRequestBuilder(MessagesRequest.MessagesRequestBuilder builder) {
        if (builder != null) {
            this.messagesRequestBuilder = builder;
            if (user != null) initializeUserRequestBuilder();
            else if (group != null) initializeGroupRequestBuilder();
        }
    }

    public void deleteMessage(BaseMessage baseMessage) {
        CometChat.deleteMessage(baseMessage.getId(), new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                CometChatUIKitHelper.onMessageDeleted(baseMessage);
            }

            @Override
            public void onError(CometChatException e) {
                cometChatException.setValue(e);
                messageDeleteState.setValue(MessageDeleteState.FAILURE_DELETE);
            }
        });
    }

    public void removeListener() {
        CometChat.removeMessageListener(LISTENERS_TAG);
        CometChat.removeGroupListener(LISTENERS_TAG);
        CometChatMessageEvents.removeListener(LISTENERS_TAG);
        CometChatGroupEvents.removeListener(LISTENERS_TAG);
        CometChatUIEvents.removeListener(LISTENERS_TAG);
        if (isCallingAdded()) {
            CometChatCallEvents.removeListener(LISTENERS_TAG);
            CometChat.removeCallListener(LISTENERS_TAG);
        }
    }

    public List<BaseMessage> getMessageList() {
        return messageArrayList;
    }

    public BaseMessage getLastMessage() {
        return messageArrayList.get(messageArrayList.size() - 1);
    }
}
