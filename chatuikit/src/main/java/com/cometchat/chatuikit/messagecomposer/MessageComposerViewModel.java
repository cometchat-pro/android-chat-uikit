package com.cometchat.chatuikit.messagecomposer;


import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKitHelper;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TransientMessage;
import com.cometchat.pro.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


public class MessageComposerViewModel extends ViewModel {

    public String LISTENERS_TAG;
    public MutableLiveData<BaseMessage> sentMessage;
    public MutableLiveData<BaseMessage> processEdit;
    public MutableLiveData<CometChatException> exception;
    public MutableLiveData<BaseMessage> successEdit;
    public MutableLiveData<HashMap<String, String>> mutableHashMap;
    public User user;
    public Group group;
    public String id;
    public String type;
    public HashMap<String, String> idMap;
    public int parentMessageId = -1;
    public MutableLiveData<Void> closeBottomPanel;
    public MutableLiveData<Void> closeTopPanel;
    public MutableLiveData<Function1<Context, View>> showTopPanel;
    public MutableLiveData<Function1<Context, View>> showBottomPanel;
    public Void aVoid;

    public MessageComposerViewModel() {
        sentMessage = new MutableLiveData<>();
        exception = new MutableLiveData<>();
        processEdit = new MutableLiveData<>();
        successEdit = new MutableLiveData<>();
        mutableHashMap = new MutableLiveData<>();
        closeBottomPanel = new MutableLiveData<>();
        closeTopPanel = new MutableLiveData<>();
        showTopPanel = new MutableLiveData<>();
        showBottomPanel = new MutableLiveData<>();
        idMap = new HashMap<>();
    }

    public MutableLiveData<BaseMessage> sentMessage() {
        return sentMessage;
    }

    public MutableLiveData<BaseMessage> processEdit() {
        return processEdit;
    }

    public MutableLiveData<CometChatException> getException() {
        return exception;
    }

    public MutableLiveData<BaseMessage> successEdit() {
        return successEdit;
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

    public User getUser() {
        return user;
    }

    public Group getGroup() {
        return group;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public HashMap<String, String> getIdMap() {
        return idMap;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            this.id = user.getUid() + "";
            this.type = UIKitConstants.ReceiverType.USER;
            setIdMap();
        }
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            this.id = group.getGuid() + "";
            this.type = UIKitConstants.ReceiverType.GROUP;
            setIdMap();
        }
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

    public void setParentMessageId(int id) {
        this.parentMessageId = id;
        setIdMap();
    }

    public void addListeners() {
        LISTENERS_TAG = System.currentTimeMillis() + "";
        CometChatMessageEvents.addListener(LISTENERS_TAG, new CometChatMessageEvents() {
            @Override
            public void ccMessageEdited(BaseMessage baseMessage, int status) {
                if (status == MessageStatus.IN_PROGRESS && baseMessage != null) {
                    if (baseMessage instanceof TextMessage) processEdit.setValue(baseMessage);
                }
            }
        });

        CometChatUIEvents.addListener(LISTENERS_TAG, new CometChatUIEvents() {
            @Override
            public void showPanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment, Function1<Context, View> view) {
                if (UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM.equals(alignment) && idMap.equals(id))
                    showBottomPanel.setValue(view);
                else if (UIKitConstants.CustomUIPosition.COMPOSER_TOP.equals(alignment) && idMap.equals(id))
                    showTopPanel.setValue(view);
            }

            @Override
            public void hidePanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment) {
                if (UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM.equals(alignment) && idMap.equals(id))
                    closeBottomPanel.setValue(aVoid);
                else if (UIKitConstants.CustomUIPosition.COMPOSER_TOP.equals(alignment) && idMap.equals(id))
                    closeTopPanel.setValue(aVoid);
            }
        });
    }

    public void removeListeners() {
        CometChatMessageEvents.removeListener(LISTENERS_TAG);
        CometChatUIEvents.removeListener(LISTENERS_TAG);
    }

    public void sendCustomMessage(CustomMessage baseMessage) {
        CometChat.sendCustomMessage(baseMessage, new CometChat.CallbackListener<CustomMessage>() {
            @Override
            public void onSuccess(CustomMessage mediaMessage) {
                sentMessage.setValue(mediaMessage);
            }

            @Override
            public void onError(CometChatException e) {
                exception.setValue(e);
            }
        });
    }

    public void sendMediaMessage(File file, String contentType, boolean isRetry) {
        MediaMessage mediaMessage = getMediaMessage(file, contentType);
        if (mediaMessage != null) {
            if (!isRetry) CometChatUIKitHelper.onMessageSent(mediaMessage, MessageStatus.IN_PROGRESS);
            CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
                @Override
                public void onSuccess(MediaMessage mediaMessage) {
                    sentMessage.setValue(mediaMessage);
                    CometChatUIKitHelper.onMessageSent(mediaMessage, MessageStatus.SUCCESS);
                }

                @Override
                public void onError(CometChatException e) {
                    exception.setValue(e);
                    mediaMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                    CometChatUIKitHelper.onMessageSent(mediaMessage, MessageStatus.ERROR);
                }
            });
        }
    }

    public void sendTextMessage(String text, boolean isRetry) {
        TextMessage textMessage = getTextMessage(text);
        if (textMessage != null) {
            if (!isRetry) CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.IN_PROGRESS);
            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.SUCCESS);
                    sentMessage.setValue(textMessage);
                }

                @Override
                public void onError(CometChatException e) {
                    exception.setValue(e);
                    textMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                    CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.ERROR);
                }
            });
        }
    }

    public void editMessage(TextMessage textMessage) {
        CometChat.editMessage(textMessage, new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage message) {
                CometChatUIKitHelper.onMessageEdited(message, MessageStatus.SUCCESS);
                successEdit.setValue(message);
            }

            @Override
            public void onError(CometChatException e) {
                exception.setValue(e);
                textMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                CometChatUIKitHelper.onMessageEdited(textMessage, MessageStatus.ERROR);
            }
        });
    }

    public void sendTransientMessage(@DrawableRes int liveReactionIcon) {
        try {
            JSONObject metaData = new JSONObject();
            metaData.put("reaction", "heart");
            metaData.put("type", "live_reaction");
            TransientMessage transientMessage = new TransientMessage(id, type, metaData);
            CometChat.sendTransientMessage(transientMessage);
            for (CometChatMessageEvents e : CometChatMessageEvents.messageEvents.values()) {
                e.ccLiveReaction(liveReactionIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextMessage getTextMessage(String text) {
        if (text != null && !text.isEmpty()) {
            TextMessage message = new TextMessage(id, text.trim(), type);
            if (parentMessageId > -1) message.setParentMessageId(parentMessageId);
            message.setCategory(CometChatConstants.CATEGORY_MESSAGE);
            message.setSender(CometChatUIKit.getLoggedInUser());
            message.setSentAt(System.currentTimeMillis() / 1000);
            message.setMuid(System.currentTimeMillis() + "");
            if (user != null) {
                message.setConversationId(CometChatUIKit.getLoggedInUser().getUid() + "_user_" + user.getUid());
                message.setReceiver(user);
            } else if (group != null) {
                message.setConversationId("group_" + group.getGuid());
                message.setReceiver(group);
            }
            return message;
        }
        return null;
    }

    public MediaMessage getMediaMessage(File file, String contentType) {
        if (file != null && contentType != null) {
            MediaMessage mediaMessage = new MediaMessage(id, file, contentType, type);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("path", file.getAbsolutePath());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (parentMessageId > -1) mediaMessage.setParentMessageId(parentMessageId);
            mediaMessage.setMetadata(jsonObject);
            mediaMessage.setSentAt(System.currentTimeMillis() / 1000);
            mediaMessage.setMuid("" + System.currentTimeMillis());
            mediaMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
            mediaMessage.setSender(CometChatUIKit.getLoggedInUser());
            if (user != null) {
                mediaMessage.setConversationId(CometChatUIKit.getLoggedInUser().getUid() + "_user_" + user.getUid());
                mediaMessage.setReceiver(user);
            } else if (group != null) {
                mediaMessage.setConversationId("group_" + group.getGuid());
                mediaMessage.setReceiver(group);
            }
            return mediaMessage;
        }
        return null;
    }

}
