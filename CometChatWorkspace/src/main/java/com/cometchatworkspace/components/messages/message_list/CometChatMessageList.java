package com.cometchatworkspace.components.messages.message_list;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import com.cometchatworkspace.components.messages.message_list.adapter.MessageAdapter;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.common.forward_message.CometChatForwardMessageActivity;
import com.cometchatworkspace.components.messages.common.message_information.CometChatMessageInfoActivity;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.Sound;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionSheet;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.CometChatActionSheet;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatReactionDialog;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.listener.OnReactionClickListener;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies.CometChatSmartReplies;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.MediaUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.sticker_header.StickyHeaderDecoration;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Purpose - CometChatMessageScreen class is a fragment used to display list of messages and perform certain action on click of message.
 * It also provide search bar to perform search operation on the list of messages. User can send text,images,video and file as messages
 * to each other and in groups. User can also perform actions like edit message,delete message and forward messages to other user and groups.
 *
 * @see CometChat
 * @see User
 * @see Group
 * @see TextMessage
 * @see MediaMessage
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 16th January 2020
 */


public class CometChatMessageList extends RelativeLayout implements
        MessageAdapter.OnMessageLongClick {

    private static final String TAG = "MessageList";

    private static final int LIMIT = 30;

    private String name;

    private String status;

    private MessagesRequest messagesRequest;    //Used to fetch messages.

    private RecyclerView rvChatListView;    //Used to display list of messages.

    private MessageAdapter messageAdapter;

    private LinearLayoutManager linearLayoutManager;

    private CometChatSmartReplies rvSmartReply;

    private ShimmerFrameLayout messageShimmer;

    private String Id;

    private Context context;

    private StickyHeaderDecoration stickyHeaderDecoration;

    private String type;

    private BaseMessage baseMessage;

    private final List<BaseMessage> baseMessages = new ArrayList<>();

    private final List<BaseMessage> messageList = new ArrayList<>();

    private final List<String> messageTypesForUser = new ArrayList<>();
    private final List<String> messageCategoriesForUser = new ArrayList<>();

    private final List<String> messageTypesForGroup = new ArrayList<>();
    private final List<String> messageCategoriesForGroup = new ArrayList<>();

    private View view;

    private boolean isNoMoreMessages;

    private final User loggedInUser = CometChat.getLoggedInUser();

    private String loggedInUserScope;

    private boolean isInProgress;

    public int count = 0;


    private View newMessageLayout;
    private TextView newMessageLayoutText;
    private int newMessageCount = 0;

    private boolean shareVisible;
    private boolean copyVisible;
    private boolean forwardVisible;
    private boolean threadVisible;
    private boolean replyVisible;
    private boolean translateVisible;
    private boolean reactionVisible;
    private boolean editVisible;
    private boolean retryVisible = false;
    private boolean replyPrivately;
    private boolean deleteVisible;


    private boolean hideDeleteMessage;


    private CometChatActionSheet cometChatActionSheet;
    private static HashMap<String,Events> messageListEvents = new HashMap<>();
    private boolean isBlockedByMe;

    private @ActionSheet.LayoutMode String actionSheetMode =
            CometChatTheme.MessageList.actionSheetMode;
    private @MessageListAlignment String messageListAlignment;

    private CometChatSoundManager soundManager;

    boolean isText ,isAudio,isVideo,isFile,isImage,isWhiteboard,isDocument,isLocation,isPoll,
            isSticker,isGroupAction,isGroupCall,isCallAction;


    public CometChatMessageList(Context context) {
        super(context);
        init(context,null);
    }

    public CometChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CometChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attributeSet) {
        this.context = context;
        soundManager = new CometChatSoundManager(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet,
                R.styleable.MessageList, 0, 0);
        actionSheetMode = ActionSheet.getValue(a.getInt(R.styleable.MessageList_actionSheetMode,0));
        messageListAlignment = getValue(a.getInt(R.styleable.MessageList_alignment,0));

        view = LayoutInflater.from(context).inflate(R.layout.cometchat_messagelist, (ViewGroup) getParent(), false);
        initViewComponent(context,view);
        addView(view);
    }

    //Data Methods

    public void setUser(User user) {
        this.Id = user.getUid();
//        this.avatarUrl = user.getAvatar();
        this.name = user.getName();
        this.status = user.getStatus();
        this.type = CometChatConstants.RECEIVER_TYPE_USER;
        this.isBlockedByMe = user.isBlockedByMe();
//        this.profileLink = user.getLink();
    }

    public void setGroup(Group group) {
        this.Id = group.getGuid();
        this.name = group.getName();
//        this.avatarUrl = group.getIcon();
//        this.groupDesc = group.getDescription();
//        this.groupPassword = group.getPassword();
//        this.groupType = group.getGroupType();
        this.loggedInUserScope = group.getScope();
//        this.memberCount = group.getMembersCount();
        this.type =CometChatConstants.RECEIVER_TYPE_GROUP;
    }
    public void isBlockedByMe(boolean isBlocked) {
        this.isBlockedByMe = isBlocked;
    }
    public void uid(String id) {
        this.Id = id;
    }
//    public void avatar(String url) {
//        this.avatarUrl = url;
//    }
    public void status(String statusStr) {
        this.status = statusStr;
    }
    public void name(String nameStr) {
        this.name = nameStr;
    }
//    public void profileLink(String profileStr) {
//        this.profileLink = profileStr;
//    }
    public void type(String typeStr) {
        this.type = typeStr;
    }
//    public void membersCount(int memberCount) {
//        this.memberCount = memberCount;
//    }
    public void guid(String id) {
        this.Id = id;
    }
//    public void groupDescription(String description) {
//        this.groupDesc = description;
//    }
//    public void groupPassword(String password) {
//        this.groupPassword = password;
//    }
//    public void groupType(String groupType) {
//        this.groupType = groupType;
//    }
    public void loggedInUserScope(String scope) {
        this.loggedInUserScope = scope;
    }


    public void backgroundColor(@ColorInt int color) {
        if (rvChatListView!=null && color!=0) {
            rvChatListView.setBackgroundColor(color);
        }
    }


    /**
     * This is a main method which is used to initialize the view for this fragment.
     *
     * @param view
     */
    private void initViewComponent(Context context,View view) {

        CometChatError.init(context);

        newMessageLayout = view.findViewById(R.id.new_message_layout);
        newMessageLayoutText = view.findViewById(R.id.new_message_tv);
        newMessageLayout.setVisibility(GONE);

        messageShimmer = view.findViewById(R.id.shimmer_layout);
        rvChatListView = view.findViewById(R.id.rv_message_list);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvChatListView.setLayoutManager(linearLayoutManager);
        rvChatListView.getItemAnimator().setChangeDuration(0);
        // Uses to fetch next list of messages if rvChatListView (RecyclerView) is scrolled in downward direction.
        rvChatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                for (Events messageListEvent : messageListEvents.values()) {
                    messageListEvent.onScrollStateChanged();
                }
                //for toolbar elevation animation i.e stateListAnimator
//                toolbar.setSelected(rvChatListView.canScrollVertically(-1));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (!isNoMoreMessages && !isInProgress) {
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 10 || !rvChatListView.canScrollVertically(-1)) {
                        isInProgress = true;
                        fetchMessage();
                    }
                }
                if (messageAdapter != null && ((messageAdapter.getItemCount() - 1) - linearLayoutManager.findLastVisibleItemPosition() < 5)) {
                    newMessageLayout.setVisibility(GONE);
                    newMessageCount = 0;
                }
            }

        });

        rvSmartReply = view.findViewById(R.id.rv_smartReply);
        rvSmartReply.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void OnItemClick(String var, int position) {
                rvSmartReply.setVisibility(GONE);
                sendMessage(var);
            }
        });
    }

    public void hideDeleteMessage(boolean isDeleteMessageVisible) {
            hideDeleteMessage = isDeleteMessageVisible;
    }

    private void fetchMessageFilterConfiguration() {
        List<CometChatMessageTemplate> list = CometChatMessagesConfigurations.getMessageFilterList();
        for (CometChatMessageTemplate template : list) {
            if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.text)) {
                isText = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.text);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.text);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.video)) {
                isVideo = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.video);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.video);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.image)) {
                isImage = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.image);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.image);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.file)) {
                isFile = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.file);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.file);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.audio)) {
                isAudio = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.audio);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.audio);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.location)) {
                isLocation = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.location);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.location);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.whiteboard)) {
                isWhiteboard = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.whiteboard);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.whiteboard);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.document)) {
                isDocument = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.document);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.document);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.poll)) {
                isPoll = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.poll);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.poll);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.sticker)) {
                isSticker = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.sticker);
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.sticker);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.callAction)) {
                isCallAction = true;
                messageTypesForUser.add(CometChatMessageTemplate.DefaultList.callAction);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.groupAction)) {
                isGroupAction = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.groupAction);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.groupCall)) {
                isGroupCall = true;
                messageTypesForGroup.add(CometChatMessageTemplate.DefaultList.groupCall);
            } else {
                messageTypesForUser.add(template.getId());
                messageTypesForGroup.add(template.getId());
            }

            if (isText || isAudio || isVideo || isFile || isImage) {
                messageCategoriesForUser.add(CometChatConstants.CATEGORY_MESSAGE);
                messageCategoriesForGroup.add(CometChatConstants.CATEGORY_MESSAGE);
            }
            if (isWhiteboard || isDocument || isLocation || isPoll || isSticker || isGroupCall) {
                messageCategoriesForUser.add(CometChatConstants.CATEGORY_CUSTOM);
                messageCategoriesForGroup.add(CometChatConstants.CATEGORY_CUSTOM);
            }
            if (isCallAction) {
                messageCategoriesForUser.add(CometChatConstants.CATEGORY_CALL);
                messageCategoriesForGroup.add(CometChatConstants.CATEGORY_CALL);
            }
            if (isGroupAction) {
                messageCategoriesForGroup.add(CometChatConstants.CATEGORY_ACTION);
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return rvChatListView;
    }
    /**
     * This method is used to fetch message of users & groups. For user it fetches previous 100 messages at
     * a time and for groups it fetches previous 30 messages. You can change limit of messages by modifying
     * number in <code>setLimit()</code>
     * This method also mark last message as read using markMessageAsRead() present in this class.
     * So all the above messages get marked as read.
     *
     * @see MessagesRequest#fetchPrevious(CometChat.CallbackListener)
     */
    private void fetchMessage() {
        fetchMessageFilterConfiguration();
        if (messagesRequest == null) {
            if (type != null) {

                if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
                    messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(LIMIT)
                            .setTypes(messageTypesForUser)
                            .setCategories(messageCategoriesForUser)
                            .hideReplies(true).hideDeletedMessages(hideDeleteMessage).setUID(Id).build();
                else
                    messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(LIMIT)
                            .setTypes(messageTypesForGroup)
                            .setCategories(messageCategoriesForGroup)
                            .hideReplies(true).hideDeletedMessages(hideDeleteMessage).setGUID(Id).build();
            }
        }
        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {

            @Override
            public void onSuccess(List<BaseMessage> baseMessages) {
                isInProgress = false;
//                List<BaseMessage> filteredMessageList = filterBaseMessages(baseMessages);
//                initMessageAdapter(filteredMessageList);
                Log.e(TAG, "onSuccess:fetchMessage "+baseMessages );
                initMessageAdapter(baseMessages);
                if (baseMessages.size() != 0) {
                    stopHideShimmer();
                    BaseMessage baseMessage = baseMessages.get(baseMessages.size() - 1);
                    markMessageAsRead(baseMessage);
                }

                if (baseMessages.size() == 0) {
                    stopHideShimmer();
                    isNoMoreMessages = true;
                }
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void stopHideShimmer() {
        messageShimmer.stopShimmer();
        messageShimmer.setVisibility(GONE);
    }



    /**
     * This method is used to initialize the message adapter if it is empty else it helps
     * to update the messagelist in adapter.
     *
     * @param messageList is a list of messages which will be added.
     */
    private void initMessageAdapter(List<BaseMessage> messageList) {
        if (messageAdapter == null) {
            messageAdapter = new MessageAdapter(context, messageList,this);
            rvChatListView.setAdapter(messageAdapter);
            stickyHeaderDecoration = new StickyHeaderDecoration(messageAdapter);
            rvChatListView.addItemDecoration(stickyHeaderDecoration, 0);
            if (messageListAlignment.equalsIgnoreCase(LEFT_ALIGNED))
                messageAdapter.setLeftAligned(true);
            messageAdapter.notifyDataSetChanged();
            scrollToBottom();

        } else {
            if (messageListAlignment.equalsIgnoreCase(LEFT_ALIGNED))
                messageAdapter.setLeftAligned(true);
            messageAdapter.updateList(messageList);
        }
        if (!isBlockedByMe && rvSmartReply.getAdapter().getItemCount()==0) {
            BaseMessage lastMessage = messageAdapter.getLastMessage();
            checkSmartReply(lastMessage);
        }
    }

    public void scrollToBottom() {
        if (messageAdapter != null && messageAdapter.getItemCount() > 0) {
            rvChatListView.scrollToPosition(messageAdapter.getItemCount() - 1);

        }
    }


    private void checkSmartReply(BaseMessage lastMessage) {
        if (lastMessage != null && !lastMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            if (lastMessage.getMetadata() != null) {
                List<String> smartReplies = Extensions.getSmartReplyList(lastMessage);
                setSmartReplyAdapter(smartReplies);
            }
        }
    }


    private void setSmartReplyAdapter(List<String> replyList) {
        rvSmartReply.setSmartReplyList(replyList);
//        scrollToBottom();
    }


    /**
     * This method is used to mark users & group message as read.
     *
     * @param baseMessage is object of BaseMessage.class. It is message which is been marked as read.
     */
    private void markMessageAsRead(BaseMessage baseMessage) {
        CometChat.markAsRead(baseMessage);
    }



    public void setMessageReceipt(MessageReceipt messageReceipt) {
        if (messageAdapter != null) {
            if (messageReceipt!=null && messageReceipt.getReceiptType()!=null &&
                    messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (Id!=null && messageReceipt.getSender()!=null && messageReceipt.getSender().getUid().equals(Id)) {
                    if (messageReceipt.getReceiptType().equals(MessageReceipt.RECEIPT_TYPE_DELIVERED))
                        messageAdapter.setDeliveryReceipts(messageReceipt);
                    else
                        messageAdapter.setReadReceipts(messageReceipt);
                }
            }
        }
    }


    public void onMessageReceived(BaseMessage message) {

        soundManager.play(Sound.incomingMessage);
        if (message.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (Id != null && Id.equalsIgnoreCase(message.getSender().getUid())) {
                setMessage(message);
            }
            else if(Id != null && Id.equalsIgnoreCase(message.getReceiverUid()) && message.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                setMessage(message);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(message.getReceiverUid())) {
                setMessage(message);
            }
        }
    }

    /**
     * This method is used to update edited message by calling <code>setEditMessage()</code> of adapter
     *
     * @param message is an object of BaseMessage and it will replace with old message.
     * @see BaseMessage
     */
    public void updateMessage(BaseMessage message) {
        if (messageAdapter!=null)
            messageAdapter.updateMessage(message);
    }


    /**
     * This method is used to mark message as read before adding them to list. This method helps to
     * add real time message in list.
     *
     * @param message is an object of BaseMessage, It is recieved from message listener.
     * @see BaseMessage
     */
    private void setMessage(BaseMessage message) {
        if (message.getParentMessageId() == 0) {
            if (messageAdapter != null) {
                messageAdapter.addMessage(message);
                if(message.getSender().getUid().equalsIgnoreCase(Id))
                    markMessageAsRead(message);
                if ((messageAdapter.getItemCount() - 1) - ((LinearLayoutManager) rvChatListView.getLayoutManager()).findLastVisibleItemPosition() < 5)
                    scrollToBottom();
                else {
                    showNewMessage(++newMessageCount);
                }
            } else {
                messageList.add(message);
                initMessageAdapter(messageList);
            }
        } else {
            if (messageAdapter!=null) {
                messageAdapter.updateReplyCount(message.getParentMessageId());
            }
        }
    }

    private void showNewMessage(int messageCount) {
        rvSmartReply.setVisibility(GONE);
        newMessageLayout.setVisibility(View.VISIBLE);
        if (messageCount==1)
            newMessageLayoutText.setText(messageCount+context.getString(R.string.new_message));
        else
            newMessageLayoutText.setText(messageCount+context.getString(R.string.new_messages));
        newMessageLayout.setOnClickListener(v-> {
            newMessageCount = 0;
            scrollToBottom();
            newMessageLayout.setVisibility(GONE);
        });
    }


    public void onPause() {
        Log.d(TAG, "onPause: ");
        if (messageAdapter!=null)
            messageAdapter.stopPlayingAudio();
    }

    public void onResume() {
        Log.d(TAG, "onResume: ");
        rvChatListView.removeItemDecoration(stickyHeaderDecoration);
        fetchMessage();
        dismissActionSheet();
    }

    private void dismissActionSheet() {
        if(cometChatActionSheet!=null && cometChatActionSheet.getFragmentManager()!=null)
            cometChatActionSheet.dismiss();
    }

    private void startForwardMessageActivity() {
        Intent intent = new Intent(getContext(), CometChatForwardMessageActivity.class);
        if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_MESSAGE)) {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY, CometChatConstants.CATEGORY_MESSAGE);
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                intent.putExtra(CometChatConstants.MESSAGE_TYPE_TEXT, ((TextMessage) baseMessage).getText());
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.MESSAGE_TYPE_TEXT);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME, ((MediaMessage) baseMessage).getAttachment().getFileName());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL, ((MediaMessage) baseMessage).getAttachment().getFileUrl());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE, ((MediaMessage) baseMessage).getAttachment().getFileMimeType());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION, ((MediaMessage) baseMessage).getAttachment().getFileExtension());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE, ((MediaMessage) baseMessage).getAttachment().getFileSize());
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, baseMessage.getType());
            }
        } else if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)) {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY, CometChatConstants.CATEGORY_CUSTOM);
            intent.putExtra(UIKitConstants.IntentStrings.TYPE, UIKitConstants.IntentStrings.LOCATION);
            try {
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LATITUDE,
                        ((CustomMessage)baseMessage).getCustomData().getDouble("latitude"));
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,
                        ((CustomMessage)baseMessage).getCustomData().getDouble("longitude"));
            } catch (Exception e) {
                Log.e(TAG, "startForwardMessageActivityError: "+e.getMessage());
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context!=null)
            context.startActivity(intent);
    }

    private void shareMessage() {
        if (baseMessage!=null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TITLE,getResources().getString(R.string.app_name));
                shareIntent.putExtra(Intent.EXTRA_TEXT, ((TextMessage)baseMessage).getText());
                shareIntent.setType("text/plain");
                Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_message));
                if (context!=null)
                    context.startActivity(intent);
        } else if (baseMessage!=null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                String mediaName = ((MediaMessage)baseMessage).getAttachment().getFileName();
                Glide.with(context).asBitmap().load(((MediaMessage)baseMessage).getAttachment().getFileUrl()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, mediaName, null);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                        shareIntent.setType(((MediaMessage)baseMessage).getAttachment().getFileMimeType());
                        Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_message));
                        if (context!=null)
                            context.startActivity(intent);
                    }
                });
        } else if (baseMessage!=null && 
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE) || 
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            MediaUtils.downloadAndShareFile(context,((MediaMessage)baseMessage));
        }
    }

    @Override
    public void setTextMessageLongClick(List<BaseMessage> textMessageList) {
        setTextAndLinkMessageLongClick(textMessageList);
    }

    @Override
    public void setImageMessageLongClick(List<BaseMessage> baseMessage) {
        setMediaMessageLongClick(baseMessage);
    }

    @Override
    public void setVideoMessageLongClick(List<BaseMessage> baseMessage) {
        setMediaMessageLongClick(baseMessage);
    }

    @Override
    public void setAudioMessageLongClick(List<BaseMessage> baseMessage) {
        setMediaMessageLongClick(baseMessage);
    }

    @Override
    public void setFileMessageLongClick(List<BaseMessage> baseMessage) {
        setMediaMessageLongClick(baseMessage);
    }

    @Override
    public void setDocumentMessageLongClick(List<BaseMessage> documentMessageList) {
        if (documentMessageList.size()==1) {
            BaseMessage basemessage = documentMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
            forwardVisible = false;
            copyVisible = false;
            editVisible = false;
            translateVisible = false;
            shareVisible = false;
        }
        showActionSheet(documentMessageList);
    }

    @Override
    public void setWhiteboardMessageLongClick(List<BaseMessage> whiteBoardMessageList) {
        if (whiteBoardMessageList.size()==1) {
            BaseMessage basemessage = whiteBoardMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
            forwardVisible = false;
            copyVisible = false;
            translateVisible = false;
            editVisible = false;
            shareVisible = false;
        }
        showActionSheet(whiteBoardMessageList);
    }

    @Override
    public void setPollMessageLongClick(List<BaseMessage> pollsMessageList) {
        if (pollsMessageList.size() == 1){
            BaseMessage basemessage = pollsMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
            forwardVisible = false;
            translateVisible = false;
            copyVisible = false;
            editVisible = false;
            shareVisible = false;
        }
        showActionSheet(pollsMessageList);
    }

    @Override
    public void setStickerMessageLongClick(List<BaseMessage> stickerMessageList) {
        if (stickerMessageList.size()==1) {
            BaseMessage basemessage = stickerMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
            forwardVisible = false;
            copyVisible = false;
            editVisible = false;
            translateVisible = false;
            shareVisible = false;

        }
        showActionSheet(stickerMessageList);
    }

    @Override
    public void setConferenceMessageLongClick(List<BaseMessage> conferenceMessageList) {
        if (conferenceMessageList.size()==1) {
            baseMessage = conferenceMessageList.get(0);
            forwardVisible = false;
            copyVisible = false;
            editVisible = false;
            shareVisible = false;
            replyVisible = true;
            translateVisible = false;
            threadVisible = false;
        }
        showActionSheet(conferenceMessageList);
    }

    @Override
    public void setLocationMessageLongClick(List<BaseMessage> locationMessageList) {
        if (locationMessageList.size() == 1){
            BaseMessage basemessage = locationMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
            translateVisible = false;
            if (basemessage != null && basemessage.getSender() != null) {
                if (basemessage.getSentAt()==-1) {
                    translateVisible = false;
                    threadVisible = false;
                    deleteVisible = false;
                    editVisible = false;
                    copyVisible = false;
                    forwardVisible = false;
                    reactionVisible = false;
                    replyVisible = false;
                    shareVisible = false;
                    retryVisible = true;
                }
            }
        }
        showActionSheet(locationMessageList);
    }

    @Override
    public void setLinkMessageLongClick(List<BaseMessage> linkMessageList) {
      setTextAndLinkMessageLongClick(linkMessageList);
    }

    public void setTextAndLinkMessageLongClick(List<BaseMessage> textMessageList) {
        if (textMessageList.size() == 1) {
            BaseMessage basemessage = textMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    fetchOptions();

                    if (basemessage.getSentAt()==-1) {
                        translateVisible = false;
                        threadVisible = false;
                        deleteVisible = false;
                        editVisible = false;
                        copyVisible = false;
                        forwardVisible = false;
                        reactionVisible = false;
                        replyVisible = false;
                        shareVisible = false;
                        retryVisible = true;
                    }
                }
            }
        }
        showActionSheet(textMessageList);
    }

    public void setMediaMessageLongClick(List<BaseMessage> mediaMessageList) {

        if (mediaMessageList.size() == 1) {
            translateVisible = false;
            BaseMessage basemessage = mediaMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    fetchOptions();
                    replyVisible = true;
                    forwardVisible = true;
                    copyVisible = false;
                    editVisible = false;
                }
                if (basemessage.getSentAt()==-1) {
                    translateVisible = false;
                    threadVisible = false;
                    deleteVisible = false;
                    editVisible = false;
                    copyVisible = false;
                    forwardVisible = false;
                    reactionVisible = false;
                    replyVisible = false;
                    shareVisible = false;
                    retryVisible = true;
                }
                showActionSheet(mediaMessageList);
            }
        }
    }

    public void showActionSheet(List<BaseMessage> baseMessagesList) {
        Log.e(TAG, "setLongMessageClick: " + baseMessagesList);
        cometChatActionSheet = new CometChatActionSheet();
        List<ActionItem> itemList = new ArrayList<>();
        if (baseMessage.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP) &&
                !baseMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
            if (replyVisible)
                itemList.add(new ActionItem(getContext().getString(R.string.send_message_privately),
                        R.drawable.ic_send_message_in_private));
            if(replyPrivately)
                itemList.add(new ActionItem(getContext().getString(R.string.reply_message_privately),
                        R.drawable.ic_reply_message_in_private));
        }
        if (translateVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.translate_message),
                    R.drawable.ic_translate));
        if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP) &&
                baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            itemList.add(new ActionItem(getContext().getString(R.string.message_information),
                                R.drawable.ic_info));

        }
        if (threadVisible) {
            if (baseMessage.getReplyCount() == 0)
                itemList.add(new ActionItem(getContext().getString(R.string.start_thread),
                        R.drawable.ic_threaded_message));
            else
                itemList.add(new ActionItem(getContext().getString(R.string.reply_in_thread),
                        R.drawable.ic_threaded_message));

        }
        if (replyVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.reply_message),
                    R.drawable.ic_reply_message));
        if (forwardVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.forward_message),
                    R.drawable.ic_forward));
        if (copyVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.copy_message),
                    R.drawable.ic_copy_paste));
        if (shareVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.share_message),
                    R.drawable.ic_share));
        if (editVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.edit_message),
                    R.drawable.ic_edit));
        if (deleteVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.delete_message),
                    R.drawable.ic_delete));
        if (retryVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.retry),
                    R.drawable.ic_baseline_retry_24));

        cometChatActionSheet.setList(itemList);
        cometChatActionSheet.showReactions(CometChat.isExtensionEnabled("reactions"));

        cometChatActionSheet.setLayoutMode(actionSheetMode);
//        bundle.putString("type", CometChatMessageListActivity.class.getName());

        if (baseMessage.getSentAt()!=0) {
            if (retryVisible || editVisible || copyVisible || threadVisible || shareVisible || deleteVisible
                    || replyVisible || forwardVisible || reactionVisible) {
//               new CometChatHoverDialog(context,baseMessage, new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        messageAdapter.clearLongClickSelectedItem();
//                    }
//                });
                cometChatActionSheet.show(((FragmentActivity)context).getSupportFragmentManager(), cometChatActionSheet.getTag());
            }
        }

        cometChatActionSheet.setCometChatMessageActionListener(new CometChatActionSheet.CometChatMessageActionListener() {
            @Override
            public void onReplyMessagePrivately() {
                if (baseMessage!=null) {
                    User user = baseMessage.getSender();
                    Intent intent = new Intent(context, CometChatMessagesActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.LINK,user.getLink());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE,baseMessage.getRawMessage().toString());
                    if (context!=null) {
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }

            @Override
            public void onSendMessagePrivately() {
                if (baseMessage!=null) {
                    User user = baseMessage.getSender();
                    Intent intent = new Intent(context, CometChatMessagesActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.LINK,user.getLink());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                    if (context!=null) {
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }

            @Override
            public void onThreadMessageClick() {
                //TODO(In Progress)
            }

            @Override
            public void onEditMessageClick() {
                for(Events messageListEvent : messageListEvents.values()) {
                    messageListEvent.onEditMessage(baseMessage);
                }
                dismissActionSheet();
            }

            @Override
            public void onReplyMessageClick() {
                for (Events messageListEvent : messageListEvents.values()) {
                    messageListEvent.onReplyMessage(baseMessage);
                }
                dismissActionSheet();
            }

            @Override
            public void onForwardMessageClick() {
                startForwardMessageActivity();
            }

            @Override
            public void onDeleteMessageClick() {
                deleteMessage(baseMessage);
                dismissActionSheet();
            }

            @Override
            public void onCopyMessageClick() {
                String message = "";
                    if (baseMessage.getDeletedAt() == 0 && baseMessage instanceof TextMessage) {
                        message = message + "[" + Utils.getLastMessageDate(context,
                                baseMessage.getSentAt()) + "] " +
                                baseMessage.getSender().getName() + ": " +
                                ((TextMessage) baseMessage).getText();
                    }
                Log.e(TAG, "onCopy: " + message);
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("MessageAdapter", message);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onShareMessageClick() { shareMessage(); }

            @Override
            public void onMessageInfoClick() {
                Intent intent = new Intent(context, CometChatMessageInfoActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.ID,baseMessage.getId());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,baseMessage.getType());
                intent.putExtra(UIKitConstants.IntentStrings.SENTAT,baseMessage.getSentAt());
                if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                    intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE,
                            Extensions.checkProfanityMessage(context,baseMessage));
                } else if(baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)){
                    if (((CustomMessage)baseMessage).getCustomData()!=null)
                        intent.putExtra(UIKitConstants.IntentStrings.CUSTOM_MESSAGE,
                                ((CustomMessage) baseMessage).getCustomData().toString());
                    if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.LOCATION);
                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.STICKERS);
                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.WHITEBOARD);
                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWhiteBoardUrl(baseMessage));
                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.WRITEBOARD);
                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWriteBoardUrl(baseMessage));
                    }  else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.GROUP_CALL);
                    }  else {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.POLLS);
                    }
                } else {
                    boolean isImageNotSafe = Extensions.getImageModeration(context,baseMessage);
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,
                            ((MediaMessage)baseMessage).getAttachment().getFileUrl());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,
                            ((MediaMessage)baseMessage).getAttachment().getFileName());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE,
                            ((MediaMessage)baseMessage).getAttachment().getFileSize());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION,
                            ((MediaMessage)baseMessage).getAttachment().getFileExtension());
                    intent.putExtra(UIKitConstants.IntentStrings.IMAGE_MODERATION,isImageNotSafe);
                }
                context.startActivity(intent);
            }

            @Override
            public void onReactionClick(Reaction reaction) {
                if (reaction.getName().equals("add_emoji")) {
                    CometChatReactionDialog reactionDialog = new CometChatReactionDialog();
                    reactionDialog.setOnEmojiClick(new OnReactionClickListener() {
                        @Override
                        public void onEmojiClicked(Reaction emojicon) {
                            sendReaction(emojicon);
                            reactionDialog.dismiss();
                        }
                    });
                    reactionDialog.show(((FragmentActivity)context).getSupportFragmentManager(),"ReactionDialog");
                } else {
                    sendReaction(reaction);
                }
            }

            @Override
            public void onTranslateMessageClick() {
                try {
                    String localeLanguage = Locale.getDefault().getLanguage();
                    JSONObject body = new JSONObject();
                    JSONArray languages = new JSONArray();
                    languages.put(localeLanguage);
                    body.put("msgId", baseMessage.getId());
                    body.put("languages", languages);
                    body.put("text", ((TextMessage)baseMessage).getText());

                    CometChat.callExtension("message-translation", "POST", "/v2/translate", body,
                            new CometChat.CallbackListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    if (Extensions.isMessageTranslated(jsonObject,((TextMessage)baseMessage).getText())) {
                                        if (baseMessage.getMetadata()!=null) {
                                            JSONObject meta = baseMessage.getMetadata();
                                            try {
                                                meta.accumulate("values",jsonObject);
                                                baseMessage.setMetadata(meta);
                                                updateMessage(baseMessage);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            baseMessage.setMetadata(jsonObject);
                                            updateMessage(baseMessage);
                                        }
                                    } else {
                                        CometChatSnackBar.show(context,rvChatListView,
                                                context.getString(R.string.no_translation_available), CometChatSnackBar.WARNING);
                                    }
                                }

                                @Override
                                public void onError(CometChatException e) {
                                    Toast.makeText(context,e.getCode(),Toast.LENGTH_LONG).show();
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRetryClick() {
                if (baseMessage!=null) {
                    messageAdapter.remove(baseMessage);
                    if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_TEXT))
                        sendMessage(((TextMessage)baseMessage).getText());
                }
            }
        });
    }

    public void add(BaseMessage baseMessage) {
        if (messageAdapter!=null)
            messageAdapter.addMessage(baseMessage);
    }

    public void remove(BaseMessage baseMessage) {
        if (messageAdapter!=null)
            messageAdapter.remove(baseMessage);
    }
    /**
     * This method is used to delete the message.
     *
     * @param baseMessage is an object of BaseMessage which is being used to delete the message.
     * @see BaseMessage
     * @see CometChat#deleteMessage(int, CometChat.CallbackListener)
     */
    private void deleteMessage(BaseMessage baseMessage) {
        CometChat.deleteMessage(baseMessage.getId(), new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                if (messageAdapter != null) {
                    if (hideDeleteMessage)
                        messageAdapter.remove(baseMessage);
                    else
                        messageAdapter.updateMessage(baseMessage);
                }
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    public void actionSheetMode(@ActionSheet.LayoutMode String actionSheetMode) {
        this.actionSheetMode = actionSheetMode;
    }

    public void messageListAlignment(@MessageListAlignment String messageListAlignment) {
        this.messageListAlignment = messageListAlignment;
        if (messageAdapter!=null) {
            messageAdapter.setLeftAligned(messageListAlignment.equalsIgnoreCase(LEFT_ALIGNED));
        }
    }

    /**
     * This method is used to send Text Message to other users and groups.
     *
     * @param message is a String which is been sent as message.
     * @see TextMessage
     * @see CometChat#sendMessage(TextMessage, CometChat.CallbackListener)
     */
    private void sendMessage(String message) {
        TextMessage textMessage;
        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_USER);
        else
            textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_GROUP);

        Log.e(TAG, "sendMessage: "+textMessage );
        textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
        textMessage.setSender(loggedInUser);
        textMessage.setMuid(System.currentTimeMillis()+"");
        if (messageAdapter != null) {
            soundManager.play(Sound.outgoingMessage);
            messageAdapter.addMessage(textMessage);
            scrollToBottom();
        }

        rvSmartReply.setVisibility(GONE);
        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                if (messageAdapter!=null)
                    messageAdapter.updateMessageFromMuid(textMessage);
            }

            @Override
            public void onError(CometChatException e) {
                if (e.getCode().equalsIgnoreCase("ERROR_INTERNET_UNAVAILABLE")) {
                    CometChatSnackBar.show(context, rvChatListView,
                            context.getString(R.string.no_internet_available), CometChatSnackBar.ERROR);
                } else if (!e.getCode().equalsIgnoreCase("ERR_BLOCKED_BY_EXTENSION")) {
                    if (messageAdapter == null) {
                        Log.e(TAG, "onError: MessageAdapter is null");
                    } else {
                        textMessage.setSentAt(-1);
                        messageAdapter.updateMessageFromMuid(textMessage);
                    }
                } else if (messageAdapter != null) {
                    messageAdapter.remove(textMessage);
                }
            }
        });

    }

    private void sendReaction(Reaction reaction) {
        try {
            Extensions.sendReactionOptimistic(reaction,baseMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageAdapter.notifyItemChanged(messageAdapter.getPosition(baseMessage));
        JSONObject body = new JSONObject();
        try {
            body.put("msgId", baseMessage.getId());
            body.put("emoji", reaction.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CometChat.callExtension("reactions", "POST", "/v1/react", body,
                new CometChat.CallbackListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject responseObject) {
                        Log.e(TAG, "onSuccess: " + responseObject.toString());
                        // ReactionModel added successfully.
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onError: " + e.getCode() + e.getMessage() + e.getDetails());
                    }
                });
    }

     private void fetchOptions() {
        CometChatMessageTemplate messageTemplate =
                CometChatMessagesConfigurations.getMessageTemplateById(baseMessage.getType());
        if (messageTemplate!=null) {
            List<ActionItem> options = messageTemplate.getOptions();
            for (ActionItem item : options) {
                if (item.getTitle().equalsIgnoreCase(context.getString(R.string.reply_message)))
                    replyVisible = true;
                else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.forward_message)))
                    forwardVisible = true;
                else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.share_message)))
                    shareVisible = true;
                else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.copy_message)))
                    copyVisible = true;
                else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.reply_message_privately)))
                    replyPrivately = true;
                else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.edit_message)))
                    editVisible = true;
                else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.delete_message))) {
                    if (baseMessage != null) {
                        if (baseMessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                            deleteVisible = true;
                        } else {
                            editVisible = false;
                            if (loggedInUserScope != null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN)
                                    || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))) {
                                deleteVisible = true;
                            } else {
                                deleteVisible = false;
                            }
                        }
                    }
                }
            }
            reactionVisible = true;
        }
    }

    public static void addListener(String TAG, Events messageListEvent) {
        messageListEvents.put(TAG,messageListEvent);
    }

    public void updateOptimisticMessage(BaseMessage baseMessage) {
        if (messageAdapter!=null)
            messageAdapter.updateMessageFromMuid(baseMessage);
    }

    public void highLightMessage(int id) {
        if (messageAdapter!=null) {
            messageAdapter.setSelectedMessage(id);
            messageAdapter.notifyDataSetChanged();
        }
    }

    public interface Events {
        void onEditMessage(BaseMessage baseMessage);
        void onReplyMessage(BaseMessage baseMessage);

        void onScrollStateChanged();
    }

    public static final String LEFT_ALIGNED = "LEFT";
    public static final String STANDARD = "STANDARD";

    @StringDef({LEFT_ALIGNED, STANDARD})
    public @interface MessageListAlignment {
    }
    public static String getValue(int pos) {
        if (pos==0)
            return STANDARD;
        else
            return LEFT_ALIGNED;
    }
}
