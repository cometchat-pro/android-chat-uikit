package com.cometchatworkspace.components.messages.message_list;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.StringDef;
import androidx.appcompat.app.AlertDialog;
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

import com.cometchatworkspace.components.messages.CometChatMessageEvents;
import com.cometchatworkspace.components.messages.MessageStatus;
import com.cometchatworkspace.components.messages.message_list.adapter.MessageAdapter;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageInputData;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatUIKitHelper;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.CometChatOptions;
import com.cometchatworkspace.components.messages.template.TemplateUtils;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.common.forward_message.CometChatForwardMessageActivity;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.Sound;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionSheet;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.CometChatActionSheet;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.CometChatActionSheetListener;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.onItemClick;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies.CometChatSmartReplies;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies.SmartRepliesStyle;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.MediaUtils;
import com.cometchatworkspace.resources.utils.custom_dialog.CometChatDialog;
import com.cometchatworkspace.resources.utils.custom_dialog.OnDialogButtonClickListener;
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
    private final int TRANSLATE_ERROR = 1;
    private final int FETCH_ERROR = 2;
    private final int DELETE_ERROR = 3;
    private int limit = 30;
    private boolean unreadOnly;
    private boolean hideMessagesFromBlockedUsers;
    private boolean hideDeleteMessage;
    private boolean withTag;
    private boolean hideReply;
    private boolean enableSoundForMessages = true;
    private boolean hideError;
    private @RawRes
    int customIncomingMessageSound = 0;
    private List<String> tags = new ArrayList<>();
    private MessageInputData sentMessageInputData, receivedMessageInputData;

    private String name;

    private String status;

    private MessagesRequest messagesRequest;    //Used to fetch messages.

    private RecyclerView rvChatListView;    //Used to display list of messages.

    private MessageAdapter messageAdapter;

    private LinearLayoutManager linearLayoutManager;

    private CometChatSmartReplies smartReplies;

    private ShimmerFrameLayout messageShimmer;

    private LinearLayout emptyLayout, customViewLayout;
    private TextView emptyTextView;

    private RelativeLayout messageListLayout;

    private int errorStateTextAppearance = 0;
    private int errorStateTextColor = 0;
    private String errorText = null;
    private View errorView = null;
    private View emptyView = null;

    private String Id;

    private Context context;

    private StickyHeaderDecoration stickyHeaderDecoration;

    private String type;

    private BaseMessage baseMessage;
    private final List<BaseMessage> messageList = new ArrayList<>();
    private List<BaseMessage> baseMessageList = new ArrayList<>();
    private final List<CometChatOptions> customMessageOptions = new ArrayList<>();
    private final List<String> excludeMessageOptions = new ArrayList<>();

    private List<CometChatMessageTemplate> messageTypes = new ArrayList<>();
    private final List<String> messageTypesToRetrieve = new ArrayList<>();
    private final List<String> messageCategoriesToRetrieve = new ArrayList<>();

    private View view;

    private boolean isNoMoreMessages;

    private final User loggedInUser = CometChat.getLoggedInUser();

    private String loggedInUserScope;

    private boolean isInProgress;

    public int count = 0;

    private View newMessageLayout;
    private TextView newMessageLayoutText;
    private int newMessageCount = 0;

    private boolean shareVisible, copyVisible, forwardVisible, threadVisible, replyVisible,
            translateVisible, reactionVisible, editVisible, retryVisible = false, replyPrivatelyVisible,
            sendMessagePrivatelyVisible, deleteVisible, messageInformationVisible;

    private onItemClick<BaseMessage> translateClick, deleteClick, editClick, replyClick, sendMessagePrivatelyClick,
            replyPrivatelyClick, forwardClick, shareClick, copyClick, messageInformationClick, threadClick;

    private ArrayList<CometChatOptions> customOption = new ArrayList<>();
    private int translateIcon, deleteIcon, editIcon, replyIcon, sendMessagePrivatelyIcon, replyPrivatelyIcon, forwardIcon,
            shareIcon, copyIcon, messageInformationIcon, threadIcon;


    private CometChatActionSheet cometChatActionSheet;
    private static final HashMap<String, CometChatMessageEvents> messageListEvents = CometChatMessageEvents.messageEvents;
    private boolean isBlockedByMe;

    private @ActionSheet.LayoutMode
    String actionSheetMode =
            ActionSheet.LayoutMode.listMode;
    private @MessageListAlignment
    String messageListAlignment;

    private CometChatSoundManager soundManager;

    boolean isText, isAudio, isVideo, isFile, isImage, isWhiteboard, isDocument, isLocation, isPoll,
            isSticker, isGroupAction, isGroupCall, isCallAction;

    private CometChatMessageList cometchatMessageList;
    private CometChatConfigurations messageBubbleConfig;
    private Palette palette;
    private Typography typography;

    public CometChatMessageList(Context context) {
        super(context);
        init(context, null);
    }

    public CometChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CometChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        cometchatMessageList = this;
        this.context = context;
        soundManager = new CometChatSoundManager(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet,
                R.styleable.MessageList, 0, 0);
        actionSheetMode = ActionSheet.getValue(a.getInt(R.styleable.MessageList_actionSheetMode, 0));
        messageListAlignment = getValue(a.getInt(R.styleable.MessageList_alignment, 0));

        view = LayoutInflater.from(context).inflate(R.layout.cometchat_messagelist, (ViewGroup) getParent(), false);
        initViewComponent(context, view);
        addView(view);
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setCustomMessageOptions(List<CometChatOptions> options) {
        customMessageOptions.addAll(options);
    }

    public void excludeMessageOptions(List<String> options) {
        excludeMessageOptions.addAll(options);
    }

    public void setMessageTypes(List<CometChatMessageTemplate> cometChatMessageTemplates) {
        if (cometChatMessageTemplates != null) {
            if (!cometChatMessageTemplates.isEmpty()) {
                messageTypesToRetrieve.clear();
                messageTypes = cometChatMessageTemplates;
            } else {
                messageTypes = new ArrayList<>();
            }
            fetchMessageFilter();
        }
    }

    public void excludeMessageTypes(List<String> excludeMessageTypes) {
        if (excludeMessageTypes != null && !excludeMessageTypes.isEmpty() && !messageTypes.isEmpty()) {
            messageTypesToRetrieve.clear();
            List<CometChatMessageTemplate> list = new ArrayList<>();
            list.addAll(messageTypes);
            for (CometChatMessageTemplate template : messageTypes) {
                if (excludeMessageTypes.contains(template.getId())) {
                    list.remove(template);
                }
            }
            messageTypes.clear();
            messageTypes.addAll(list);
            fetchMessageFilter();
        }
    }


    //Data Methods

    public void setUser(User user) {
        this.Id = user.getUid();
        this.name = user.getName();
        this.status = user.getStatus();
        this.type = UIKitConstants.ReceiverTypeConstants.USER;
        this.isBlockedByMe = user.isBlockedByMe();
    }

    public void setGroup(Group group) {
        this.Id = group.getGuid();
        this.name = group.getName();
        this.loggedInUserScope = group.getScope();
        this.type = UIKitConstants.ReceiverTypeConstants.GROUP;
    }

    public void setSentMessageInputData(MessageInputData messageInputData) {
        sentMessageInputData = messageInputData;
        if (messageAdapter != null && sentMessageInputData != null)
            messageAdapter.setSentMessageInputData(sentMessageInputData);
    }

    public void setReceivedMessageInputData(MessageInputData messageInputData) {
        receivedMessageInputData = messageInputData;
        if (messageAdapter != null && receivedMessageInputData != null)
            messageAdapter.setReceivedMessageInputData(receivedMessageInputData);
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
        if (rvChatListView != null && color != 0) {
            rvChatListView.setBackgroundColor(color);
        }
    }


    /**
     * This is a main method which is used to initialize the view for this fragment.
     *
     * @param view
     */
    private void initViewComponent(Context context, View view) {

        CometChatError.init(context);
        messageTypes = TemplateUtils.getDefaultList(context);
        fetchMessageFilter();
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        errorStateTextAppearance = typography.getText1();
        errorStateTextColor = palette.getAccent700();
        newMessageLayout = view.findViewById(R.id.new_message_layout);
        newMessageLayoutText = view.findViewById(R.id.new_message_tv);
        newMessageLayout.setVisibility(GONE);

        messageShimmer = view.findViewById(R.id.shimmer_layout);
        rvChatListView = view.findViewById(R.id.rv_message_list);
        emptyLayout = view.findViewById(R.id.empty_view);
        customViewLayout = view.findViewById(R.id.customView_lay);
        emptyTextView = view.findViewById(R.id.empty_text);
        emptyTextView.setTextColor(palette.getAccent400());
        emptyTextView.setTextAppearance(context, typography.getHeading());
        messageListLayout = view.findViewById(R.id.message_list_layout);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvChatListView.setLayoutManager(linearLayoutManager);
        rvChatListView.setItemAnimator(null);
//        rvChatListView.getItemAnimator().setChangeDuration(0);
        // Uses to fetch next list of messages if rvChatListView (RecyclerView) is scrolled in downward direction.
        rvChatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
                    messageListEvent.onScrollStateChanged();
                }
                //for toolbar elevation animation i.e stateListAnimator
//                toolbar.setSelected(rvChatListView.canScrollVertically(-1));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (!isNoMoreMessages && !isInProgress) {
                    if (linearLayoutManager.findLastVisibleItemPosition() == (messageAdapter.getItemCount() - 1) || !rvChatListView.canScrollVertically(1)) {
                        markLastMessageAsRead();
                    }
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

        smartReplies = view.findViewById(R.id.rv_smartReply);
        SmartRepliesStyle style = new SmartRepliesStyle()
                .setBackground(android.R.color.transparent)
                .setTextAppearance(typography.getSubtitle1())
                .setCloseIconTint(palette.getAccent400())
                .setTextColor(palette.getAccent());
        smartReplies.setStyle(style);
        smartReplies.setElevation(0);
        smartReplies.setOnClose(view1 ->
                smartReplies.setVisibility(GONE)
        );

    }

    private void markLastMessageAsRead() {
        if (messageAdapter != null) {
            BaseMessage lastMessage = messageAdapter.getLastMessage();
            if (lastMessage.getReadAt() == 0) {
                if (type != null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (!lastMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                        markMessageAsRead(lastMessage);
                    }
                } else {
                    if (lastMessage != null && lastMessage.getSender() != null && lastMessage.getSender().getUid() != null && lastMessage.getSender().getUid().equalsIgnoreCase(Id)) {
                        markMessageAsRead(lastMessage);
                    }
                }
            }
        }

    }

    public void setUnreadOnly(boolean unreadOnly) {
        this.unreadOnly = unreadOnly;
    }

    public void setHideMessagesFromBlockedUsers(boolean hideMessagesFromBlockedUsers) {
        this.hideMessagesFromBlockedUsers = hideMessagesFromBlockedUsers;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
        this.withTag = true;
    }

    public void hideDeleteMessage(boolean isDeleteMessageVisible) {
        hideDeleteMessage = isDeleteMessageVisible;
    }

    private void setHideReply(boolean hideReply) {
        this.hideReply = hideReply;
    }

    private void fetchMessageFilter() {
//        List<CometChatMessageTemplate> list =
//                CometChatMessagesConfigurations.getMessageFilterList();
//        if (list.isEmpty()) {
//            list = TemplateUtils.getDefaultList(context);
//            CometChatMessagesConfigurations config = new CometChatMessagesConfigurations();
//            config.setMessageFilter(list);
//        }

        for (CometChatMessageTemplate template : messageTypes) {
            if (template.getId() != null) {
                if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.text)) {
                    isText = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.text);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.text);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.video)) {
                    isVideo = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.video);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.video);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.image)) {
                    isImage = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.image);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.image);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.file)) {
                    isFile = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.file);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.file);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.audio)) {
                    isAudio = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.audio);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.audio);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.location)) {
                    isLocation = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.location);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.location);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.whiteboard)) {
                    isWhiteboard = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.whiteboard);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.whiteboard);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.document)) {
                    isDocument = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.document);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.document);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.poll)) {
                    isPoll = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.poll);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.poll);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.sticker)) {
                    isSticker = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.sticker);
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.sticker);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.callAction)) {
                    isCallAction = true;
//                    messageTypesForUser.add(CometChatMessageTemplate.DefaultList.callAction);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.groupAction)) {
                    isGroupAction = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.groupAction);
                } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.groupCall)) {
                    isGroupCall = true;
                    messageTypesToRetrieve.add(CometChatMessageTemplate.DefaultList.groupCall);
                } else {
//                    messageTypesForUser.add(template.getId());
                    messageTypesToRetrieve.add(template.getId());
                }
            }
            if (isText || isAudio || isVideo || isFile || isImage) {
//                messageCategoriesForUser.add(CometChatConstants.CATEGORY_MESSAGE);
                messageCategoriesToRetrieve.add(CometChatConstants.CATEGORY_MESSAGE);
            }
            if (isWhiteboard || isDocument || isLocation || isPoll || isSticker || isGroupCall) {
//                messageCategoriesForUser.add(CometChatConstants.CATEGORY_CUSTOM);
                messageCategoriesToRetrieve.add(CometChatConstants.CATEGORY_CUSTOM);
            }
            if (isCallAction) {
//                messageCategoriesForUser.add(CometChatConstants.CATEGORY_CALL);
                messageCategoriesToRetrieve.add(CometChatConstants.CATEGORY_CALL);
            }
            if (isGroupAction) {
                messageCategoriesToRetrieve.add(CometChatConstants.CATEGORY_ACTION);
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
//        fetchMessageFilterConfiguration();
        if (messagesRequest == null) {
            if (type != null && Id != null) {

                if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
                    messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(limit)
                            .setTypes(messageTypesToRetrieve)
                            .setCategories(messageCategoriesToRetrieve).hideMessagesFromBlockedUsers(hideMessagesFromBlockedUsers).setTags(tags).setUnread(unreadOnly).withTags(withTag)
                            .hideReplies(hideReply).hideDeletedMessages(hideDeleteMessage).setUID(Id).build();
                else if (type.equals(CometChatConstants.RECEIVER_TYPE_GROUP))
                    messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(limit)
                            .setTypes(messageTypesToRetrieve)
                            .setCategories(messageCategoriesToRetrieve).hideMessagesFromBlockedUsers(hideMessagesFromBlockedUsers).setTags(tags).setUnread(unreadOnly).withTags(withTag)
                            .hideReplies(hideReply).hideDeletedMessages(hideDeleteMessage).setGUID(Id).build();
                else
                    return;
            } else {
                return;
            }
        }
        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List<BaseMessage> baseMessages) {
                baseMessageList.addAll(baseMessages);
                isInProgress = false;
                initMessageAdapter(baseMessages);
                if (baseMessages.size() != 0) {
                    stopHideShimmer();
                }
                if (baseMessages.size() == 0) {
                    stopHideShimmer();
                    isNoMoreMessages = true;
                }
                checkNoMessages();
            }

            @Override
            public void onError(CometChatException e) {
                stopHideShimmer();
                CometChatUIKitHelper.onMessageError(e, null);
                hideError(FETCH_ERROR);
                Log.d(TAG, "onError: " + e.getMessage());

            }
        });
    }

    private void hideError(int code) {
        String errorMessage;
        if (errorText != null)
            errorMessage = errorText;
        else
            errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!hideError && errorView != null) {
            customViewLayout.removeAllViews();
            customViewLayout.addView(errorView);
            customViewLayout.setVisibility(VISIBLE);
        } else {
            customViewLayout.setVisibility(GONE);
            if (!hideError) {
                if (getContext() != null) {
                    new CometChatDialog(
                            context,
                            0,
                            errorStateTextAppearance,
                            typography.getText2(),
                            palette.getAccent900(),
                            0,
                            errorStateTextColor,
                            errorMessage,
                            "",
                            getContext().getString(R.string.try_again),
                            getResources().getString(R.string.cancel),
                            "",
                            palette.getPrimary(),
                            palette.getPrimary(),
                            0,
                            new OnDialogButtonClickListener() {
                                @Override
                                public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        if (code == FETCH_ERROR)
                                            fetchMessage();
                                        else if (code == TRANSLATE_ERROR)
                                            translateMessage(baseMessage);
                                        else if (code == DELETE_ERROR)
                                            deleteMessage(baseMessage);

                                        alertDialog.dismiss();
                                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                        alertDialog.dismiss();
                                    }
                                }
                            }, 0, false);
                }

            }
        }
    }

    public void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0)
            this.errorStateTextAppearance = appearance;
    }

    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0)
            this.errorStateTextColor = errorMessageColor;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText + "";
    }

    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0)
            emptyTextView.setTextAppearance(context, appearance);

    }

    public void emptyStateText(String emptyText) {
        if (emptyText != null)
            emptyTextView.setText(emptyText);
        else
            emptyTextView.setText(getResources().getString(R.string.no_user));
    }

    public void setErrorView(int id) {
        try {
            errorView = View.inflate(context, id, null);
        } catch (Exception e) {
            errorView = null;
        }
    }

    public void setEmptyView(int id) {
        try {
            emptyView = View.inflate(context, id, null);
        } catch (Exception e) {
            emptyView = null;
        }
    }

    private void checkNoMessages() {
        if (baseMessageList.size() == 0) {
            stopHideShimmer();
            if (emptyView != null) {
                customViewLayout.setVisibility(VISIBLE);
                customViewLayout.removeAllViews();
                customViewLayout.addView(emptyView);
                emptyLayout.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.VISIBLE);
            }
            messageListLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            messageListLayout.setVisibility(View.VISIBLE);
            customViewLayout.setVisibility(GONE);
        }
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
            messageAdapter = new MessageAdapter(context, messageList, this);
            messageAdapter.setMessageTemplateList(messageTypes);
            rvChatListView.setAdapter(messageAdapter);
            stickyHeaderDecoration = new StickyHeaderDecoration(messageAdapter);
            rvChatListView.addItemDecoration(stickyHeaderDecoration, 0);
            messageAdapter.setLeftAligned(messageListAlignment.equalsIgnoreCase(LEFT_ALIGNED));
            if (messageBubbleConfig == null) {
                messageBubbleConfig = new MessageBubbleConfiguration();
                MessageInputData messageInputData = new MessageInputData(
                        false, false, false, true, true);
                ((MessageBubbleConfiguration) messageBubbleConfig)
                        .setSentMessageInputData(messageInputData);
            }
            messageAdapter.setMessageBubbleConfiguration(messageBubbleConfig);
            messageAdapter.notifyDataSetChanged();
            scrollToBottom();

        } else {
            messageAdapter.setLeftAligned(messageListAlignment.equalsIgnoreCase(LEFT_ALIGNED));
            messageAdapter.updateList(messageList);
        }
        if (!isBlockedByMe) {
            BaseMessage lastMessage = messageAdapter.getLastMessage();
            checkSmartReply(lastMessage);
        }
    }

    public void scrollToBottom() {
        if (messageAdapter != null && messageAdapter.getItemCount() > 0) {
            rvChatListView.scrollToPosition(messageAdapter.getItemCount() - 1);
            BaseMessage lastMessage = messageAdapter.getLastMessage();
            checkSmartReply(lastMessage);
            markLastMessageAsRead();
        }
    }


    private void checkSmartReply(BaseMessage lastMessage) {
        smartReplies.setVisibility(VISIBLE);
        smartReplies.setMessageObject(lastMessage);
    }

    /**
     * This method is used to mark users & group message as read.
     *
     * @param lastMessage is object of BaseMessage.class. It is message which is been marked as read.
     */
    private void markMessageAsRead(BaseMessage lastMessage) {
        CometChat.markAsRead(lastMessage, new CometChat.CallbackListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                lastMessage.setReadAt(System.currentTimeMillis() / 1000);
                for (CometChatMessageEvents events : messageListEvents.values()) {
                    events.onMessageRead(lastMessage);
                }
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }


    public void setMessageReceipt(MessageReceipt messageReceipt) {
        if (messageAdapter != null) {
            if (messageReceipt != null && messageReceipt.getReceiptType() != null &&
                    messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (Id != null && messageReceipt.getSender() != null && messageReceipt.getSender().getUid().equals(Id)) {
                    if (messageReceipt.getReceiptType().equals(MessageReceipt.RECEIPT_TYPE_DELIVERED))
                        messageAdapter.setDeliveryReceipts(messageReceipt);
                    else
                        messageAdapter.setReadReceipts(messageReceipt);
                }
            }
        }
    }

    public void enableSoundForMessages(boolean enableSoundForMessages) {
        this.enableSoundForMessages = enableSoundForMessages;
    }

    public void setCustomIncomingMessageSound(@RawRes int customIncomingMessageSound) {
        this.customIncomingMessageSound = customIncomingMessageSound;
    }

    public void onMessageReceived(BaseMessage message) {
        if (enableSoundForMessages) {
            soundManager.play(Sound.incomingMessage, customIncomingMessageSound);
        }
        if (message.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (Id != null && Id.equalsIgnoreCase(message.getSender().getUid())) {
                setMessage(message);
            } else if (Id != null && Id.equalsIgnoreCase(message.getReceiverUid()) && message.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                setMessage(message);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(message.getReceiverUid())) {
                setMessage(message);
            }
        }
        checkSmartReply(message);
    }

    /**
     * This method is used to update edited message by calling <code>setEditMessage()</code> of adapter
     *
     * @param message is an object of BaseMessage and it will replace with old message.
     * @see BaseMessage
     */
    public void updateMessage(BaseMessage message) {
        if (messageAdapter != null)
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
                add(message);

                if ((messageAdapter.getItemCount() - 1) - ((LinearLayoutManager) rvChatListView.getLayoutManager()).findLastVisibleItemPosition() < 5)
                    scrollToBottom();
                else {
                    showNewMessage(++newMessageCount);
                }
                checkSmartReply(message);
            } else {
                messageList.add(message);
                initMessageAdapter(messageList);
            }
        } else {
            if (messageAdapter != null) {
                messageAdapter.updateReplyCount(message.getParentMessageId());
            }
        }
    }

    private void showNewMessage(int messageCount) {
        newMessageLayout.setVisibility(View.VISIBLE);
        if (messageCount == 1)
            newMessageLayoutText.setText(messageCount + " " + context.getString(R.string.new_message));
        else
            newMessageLayoutText.setText(messageCount + " " + context.getString(R.string.new_messages));
        newMessageLayout.setOnClickListener(v -> {
            newMessageCount = 0;
            scrollToBottom();
            newMessageLayout.setVisibility(GONE);
        });
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == View.GONE || visibility == View.INVISIBLE)
            onPause();
        else
            onResume();
    }

    private void onPause() {
        Log.d(TAG, "onPause: ");
        if (messageAdapter != null)
            messageAdapter.stopPlayingAudio();
        removeGroupListener();
        removeMessageListener();
    }

    private void onResume() {
        Log.d(TAG, "onResume: ");
        fetchMessage();
        addMessageListener();
        if (isGroupAction)
            addGroupListener();
        dismissActionSheet();
    }

    private void dismissActionSheet() {
        if (cometChatActionSheet != null && cometChatActionSheet.getFragmentManager() != null)
            cometChatActionSheet.dismiss();
    }


    public void forwardMessage(BaseMessage baseMessage) {
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
                        ((CustomMessage) baseMessage).getCustomData().getDouble("latitude"));
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,
                        ((CustomMessage) baseMessage).getCustomData().getDouble("longitude"));
            } catch (Exception e) {
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context != null)
            context.startActivity(intent);
    }

    private void shareMessage() {
        if (baseMessage != null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, ((TextMessage) baseMessage).getText());
            shareIntent.setType("text/plain");
            Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_message));
            if (context != null)
                context.startActivity(intent);
        } else if (baseMessage != null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
            String mediaName = ((MediaMessage) baseMessage).getAttachment().getFileName();
            Glide.with(context).asBitmap().load(((MediaMessage) baseMessage).getAttachment().getFileUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, mediaName, null);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                    shareIntent.setType(((MediaMessage) baseMessage).getAttachment().getFileMimeType());
                    Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_message));
                    if (context != null)
                        context.startActivity(intent);
                }
            });
        } else if (baseMessage != null &&
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE) ||
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            MediaUtils.downloadAndShareFile(context, ((MediaMessage) baseMessage));
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
        if (documentMessageList.size() == 1) {
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
        if (whiteBoardMessageList.size() == 1) {
            BaseMessage basemessage = whiteBoardMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
        }
        showActionSheet(whiteBoardMessageList);
    }

    @Override
    public void setPollMessageLongClick(List<BaseMessage> pollsMessageList) {
        if (pollsMessageList.size() == 1) {
            BaseMessage basemessage = pollsMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();
        }
        showActionSheet(pollsMessageList);
    }

    @Override
    public void setStickerMessageLongClick(List<BaseMessage> stickerMessageList) {
        if (stickerMessageList.size() == 1) {
            BaseMessage basemessage = stickerMessageList.get(0);
            baseMessage = basemessage;
            fetchOptions();

        }
        showActionSheet(stickerMessageList);
    }

    @Override
    public void setConferenceMessageLongClick(List<BaseMessage> conferenceMessageList) {
        if (conferenceMessageList.size() == 1) {
            baseMessage = conferenceMessageList.get(0);
            if (baseMessage != null && baseMessage.getSender() != null) {
                fetchOptions();
            }
        }
        showActionSheet(conferenceMessageList);
    }

    @Override
    public void setLocationMessageLongClick(List<BaseMessage> locationMessageList) {
        if (locationMessageList.size() == 1) {
            BaseMessage basemessage = locationMessageList.get(0);
            baseMessage = basemessage;
            translateVisible = false;
            if (basemessage != null && basemessage.getSender() != null) {
                fetchOptions();
                if (basemessage.getSentAt() == -1) {
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
                    if (basemessage.getSentAt() == -1) {
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
                }
                if (basemessage.getSentAt() == -1) {
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
        cometChatActionSheet = new CometChatActionSheet();
        List<ActionItem> itemList = new ArrayList<>();
        if (baseMessage.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP) &&
                !baseMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
            if (replyVisible)
                itemList.add(new ActionItem(getContext().getString(R.string.send_message_privately),
                        sendMessagePrivatelyIcon != 0 ?
                                sendMessagePrivatelyIcon : R.drawable.ic_send_message_in_private));
            if (replyPrivatelyVisible)
                itemList.add(new ActionItem(getContext().getString(R.string.reply_message_privately),
                        replyPrivatelyIcon != 0 ?
                                replyPrivatelyIcon : R.drawable.ic_reply_message_in_private));
        }
        if (translateVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.TRANSLATE, getContext().getString(R.string.translate_message),
                    translateIcon != 0 ?
                            translateIcon : R.drawable.ic_translate));
        if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP) &&
                baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            if (messageInformationVisible) {
                itemList.add(new ActionItem(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION, getContext().getString(R.string.message_information),
                        messageInformationIcon != 0 ?
                                messageInformationIcon : R.drawable.ic_info));
            }

        }
        if (threadVisible) {
            if (baseMessage.getReplyCount() == 0)
                itemList.add(new ActionItem(getContext().getString(R.string.start_thread),
                        threadIcon != 0 ?
                                threadIcon : R.drawable.ic_threaded_message));
            else
                itemList.add(new ActionItem(getContext().getString(R.string.reply_in_thread),
                        threadIcon != 0 ?
                                threadIcon : R.drawable.ic_threaded_message));

        }
        if (replyVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.REPLY, getContext().getString(R.string.reply_message),
                    replyIcon != 0 ?
                            replyIcon : R.drawable.ic_reply_message));
        if (forwardVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.FORWARD, getContext().getString(R.string.forward_message),
                    forwardIcon != 0 ?
                            forwardIcon : R.drawable.ic_forward));
        if (copyVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.COPY, getContext().getString(R.string.copy_message),
                    copyIcon != 0 ?
                            copyIcon : R.drawable.ic_copy_paste));
        if (shareVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.SHARE, getContext().getString(R.string.share_message),
                    shareIcon != 0 ?
                            shareIcon : R.drawable.ic_share));
        if (editVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.EDIT, getContext().getString(R.string.edit_message),
                    editIcon != 0 ?
                            editIcon : R.drawable.ic_edit));
        if (deleteVisible)
            itemList.add(new ActionItem(UIKitConstants.DefaultOptions.DELETE, getContext().getString(R.string.delete_message),
                    deleteIcon != 0 ?
                            deleteIcon : R.drawable.ic_delete));
        if (retryVisible)
            itemList.add(new ActionItem(getContext().getString(R.string.retry),
                    R.drawable.ic_baseline_retry_24));

        if (!customOption.isEmpty()) {
            for (CometChatOptions options : customOption) {
                itemList.add(new ActionItem(options.getId(), options.getTitle(),
                        options.getIcon()));
            }
        }
        cometChatActionSheet.setList(itemList);

        cometChatActionSheet.showReactions(CometChat.isExtensionEnabled("reactions"));

        cometChatActionSheet.setLayoutMode(actionSheetMode);
//        bundle.putString("type", CometChatMessageListActivity.class.getName());

        if (baseMessage.getSentAt() != 0) {
            if (retryVisible || editVisible || copyVisible || threadVisible || shareVisible || deleteVisible
                    || replyVisible || forwardVisible || reactionVisible) {
//               new CometChatHoverDialog(context,baseMessage, new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        messageAdapter.clearLongClickSelectedItem();
//                        messageAdapter.clearLongClickSelectedItem();
//                    }
//                });
                cometChatActionSheet.show(((FragmentActivity) context).getSupportFragmentManager(), cometChatActionSheet.getTag());
            }
        }

        cometChatActionSheet.setEventListener(new CometChatActionSheetListener() {
            @Override
            public void onReactionClick(Reaction reaction) {

                for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
                    messageListEvent.onMessageReact(baseMessage, reaction);
                }
            }

            @Override
            public void onActionItemClick(ActionItem actionItem) {
                if (UIKitConstants.DefaultOptions.REPLY
                        .equalsIgnoreCase(actionItem.getId())) {
                    if (replyClick != null)
                        replyClick.onClick(baseMessage, context);
                    else {
                        for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
                            messageListEvent.onMessageReply(baseMessage, MessageStatus.IN_PROGRESS);
                        }
                    }
                    dismissActionSheet();
                } else if (actionItem.getText()
                        .equalsIgnoreCase(context.getString(R.string.start_thread))) {
                    if (threadClick != null)
                        threadClick.onClick(baseMessage, context);
                    else {
                        for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
                            messageListEvent.onStartThread(baseMessage, cometchatMessageList);
                        }
                    }
                    dismissActionSheet();
                } else if (actionItem.getId()
                        .equalsIgnoreCase(UIKitConstants.DefaultOptions.FORWARD)) {
                    for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
                        messageListEvent.onMessageForward(baseMessage, cometchatMessageList);
                    }
                    dismissActionSheet();
                    if (forwardClick != null)
                        forwardClick.onClick(baseMessage, context);
                } else if (actionItem.getId()
                        .equalsIgnoreCase(UIKitConstants.DefaultOptions.DELETE)) {
                    CometChatUIKitHelper.onMessageDelete(baseMessage,MessageStatus.IN_PROGRESS);
                    dismissActionSheet();
                    if (deleteClick != null)
                        deleteClick.onClick(baseMessage, context);
                } else if (actionItem.getId()
                        .equalsIgnoreCase(UIKitConstants.DefaultOptions.EDIT)) {
                    CometChatUIKitHelper.onMessageEdit(baseMessage, MessageStatus.IN_PROGRESS);
                    dismissActionSheet();
                    if (editClick != null)
                        editClick.onClick(baseMessage, context);
                } else if (actionItem.getText()
                        .equalsIgnoreCase(context.getString(R.string.reply_message_privately))) {
                    if (replyPrivatelyClick != null)
                        replyPrivatelyClick.onClick(baseMessage, context);
                    else {
                        for (CometChatMessageEvents e : messageListEvents.values()) {
                            e.onReplyMessagePrivately(baseMessage, cometchatMessageList);
                        }
                    }
                    dismissActionSheet();
                } else if (context.getString(R.string.retry)
                        .equalsIgnoreCase(actionItem.getText())) {
                    if (baseMessage != null) {
                        remove(baseMessage);
                        if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_TEXT))
                            sendMessage(((TextMessage) baseMessage).getText());
                    }
                } else if (actionItem.getId()
                        .equalsIgnoreCase(UIKitConstants.DefaultOptions.COPY)) {
                    if (copyClick != null)
                        copyClick.onClick(baseMessage, context);
                    else
                        copyMessage(baseMessage);
                    dismissActionSheet();
                } else if (actionItem.getId()
                        .equalsIgnoreCase(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION)) {

                } else if (actionItem.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.TRANSLATE)) {
                    if (translateClick != null)
                        translateClick.onClick(baseMessage, context);
                    else {
                        translateMessage(baseMessage);
                    }
                    dismissActionSheet();
                } else {
                    if (!customOption.isEmpty()) {
                        for (CometChatOptions options : customOption) {
                            if (options.getId() != null && actionItem.getId() != null && actionItem.getId().equalsIgnoreCase(options.getId())) {
                                if (options.getOnClick() != null)
                                    options.getOnClick().onClick(baseMessage, context);
                                break;
                            }
                        }
                        dismissActionSheet();
                    }
                }
            }
        });
//        cometChatActionSheet.setCometChatMessageActionListener(new CometChatActionSheet.CometChatMessageActionListener() {
//            @Override
//            public void onReplyMessagePrivately() {
//                replyMessagePrivately(baseMessage);
//            }
//
//            @Override
//            public void onSendMessagePrivately() {
//
//            }
//
//            @Override
//            public void onThreadMessageClick() {
//                //TODO(In Progress)
//            }
//
//            @Override
//            public void onEditMessageClick() {
//                for(CometChatMessageEvents messageListEvent : messageListEvents.values()) {
//                    messageListEvent.onMessageEdit(baseMessage,MessageStatus.IN_PROGRESS);
//                }
//                dismissActionSheet();
//            }
//
//            @Override
//            public void onReplyMessageClick() {
//                for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
//                    messageListEvent.onReplyMessage(baseMessage,cometchatMessageList);
//                }
//                dismissActionSheet();
//            }
//
//            @Override
//            public void onForwardMessageClick() {
//                for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
//                    messageListEvent.onReplyMessage(baseMessage,cometchatMessageList);
//                }
//                dismissActionSheet();
//                forwardMessage(baseMessage);
//            }
//
//            @Override
//            public void onDeleteMessageClick() {
//                for (CometChatMessageEvents messageListEvent : messageListEvents.values()) {
//                    messageListEvent.onReplyMessage(baseMessage,cometchatMessageList);
//                }
//                dismissActionSheet();
//                deleteMessage(baseMessage);
//            }
//
//            @Override
//            public void onCopyMessageClick() {
//                String message = "";
//                    if (baseMessage.getDeletedAt() == 0 && baseMessage instanceof TextMessage) {
//                        message = message + "[" + Utils.getLastMessageDate(context,
//                                baseMessage.getSentAt()) + "] " +
//                                baseMessage.getSender().getName() + ": " +
//                                ((TextMessage) baseMessage).getText();
//                    }
//                Log.e(TAG, "onCopy: " + message);
//                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clipData = ClipData.newPlainText("MessageAdapter", message);
//                clipboardManager.setPrimaryClip(clipData);
//                Toast.makeText(context, getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onShareMessageClick() { shareMessage(); }
//
//            @Override
//            public void onMessageInfoClick() {
//                Intent intent = new Intent(context, CometChatMessageInfoActivity.class);
//                intent.putExtra(UIKitConstants.IntentStrings.ID,baseMessage.getId());
//                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,baseMessage.getType());
//                intent.putExtra(UIKitConstants.IntentStrings.SENTAT,baseMessage.getSentAt());
//                if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
//                    intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE,
//                            Extensions.checkProfanityMessage(context,baseMessage));
//                } else if(baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)){
//                    if (((CustomMessage)baseMessage).getCustomData()!=null)
//                        intent.putExtra(UIKitConstants.IntentStrings.CUSTOM_MESSAGE,
//                                ((CustomMessage) baseMessage).getCustomData().toString());
//                    if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
//                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
//                                UIKitConstants.IntentStrings.LOCATION);
//                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
//                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.STICKERS);
//                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
//                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
//                                UIKitConstants.IntentStrings.WHITEBOARD);
//                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWhiteBoardUrl(baseMessage));
//                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
//                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
//                                UIKitConstants.IntentStrings.WRITEBOARD);
//                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWriteBoardUrl(baseMessage));
//                    }  else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
//                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
//                                UIKitConstants.IntentStrings.GROUP_CALL);
//                    }  else {
//                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
//                                UIKitConstants.IntentStrings.POLLS);
//                    }
//                } else {
//                    boolean isImageNotSafe = Extensions.getImageModeration(context,baseMessage);
//                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,
//                            ((MediaMessage)baseMessage).getAttachment().getFileUrl());
//                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,
//                            ((MediaMessage)baseMessage).getAttachment().getFileName());
//                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE,
//                            ((MediaMessage)baseMessage).getAttachment().getFileSize());
//                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION,
//                            ((MediaMessage)baseMessage).getAttachment().getFileExtension());
//                    intent.putExtra(UIKitConstants.IntentStrings.IMAGE_MODERATION,isImageNotSafe);
//                }
//                context.startActivity(intent);
//            }
//
//            @Override
//            public void onReactionClick(Reaction reaction) {
//                if (reaction.getName().equals("add_emoji")) {
//                    CometChatReactionDialog reactionDialog = new CometChatReactionDialog();
//                    reactionDialog.setOnEmojiClick(new OnReactionClickListener() {
//                        @Override
//                        public void onEmojiClicked(Reaction emojicon) {
//                            sendReaction(emojicon);
//                            reactionDialog.dismiss();
//                        }
//                    });
//                    reactionDialog.show(((FragmentActivity)context).getSupportFragmentManager(),"ReactionDialog");
//                } else {
//                    sendReaction(reaction);
//                }
//            }
//
//            @Override
//            public void onTranslateMessageClick() {
//                try {
//                    String localeLanguage = Locale.getDefault().getLanguage();
//                    JSONObject body = new JSONObject();
//                    JSONArray languages = new JSONArray();
//                    languages.put(localeLanguage);
//                    body.put("msgId", baseMessage.getId());
//                    body.put("languages", languages);
//                    body.put("text", ((TextMessage)baseMessage).getText());
//
//                    CometChat.callExtension("message-translation", "POST", "/v2/translate", body,
//                            new CometChat.CallbackListener<JSONObject>() {
//                                @Override
//                                public void onSuccess(JSONObject jsonObject) {
//                                    if (Extensions.isMessageTranslated(jsonObject,((TextMessage)baseMessage).getText())) {
//                                        if (baseMessage.getMetadata()!=null) {
//                                            JSONObject meta = baseMessage.getMetadata();
//                                            try {
//                                                meta.accumulate("values",jsonObject);
//                                                baseMessage.setMetadata(meta);
//                                                updateMessage(baseMessage);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        } else {
//                                            baseMessage.setMetadata(jsonObject);
//                                            updateMessage(baseMessage);
//                                        }
//                                    } else {
//                                        CometChatSnackBar.show(context,rvChatListView,
//                                                context.getString(R.string.no_translation_available), CometChatSnackBar.WARNING);
//                                    }
//                                }
//
//                                @Override
//                                public void onError(CometChatException e) {
//                                    Toast.makeText(context,e.getCode(),Toast.LENGTH_LONG).show();
//                                }
//                            });
//                } catch (Exception e) {
//                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onRetryClick() {
//                if (baseMessage!=null) {
//                    messageAdapter.remove(baseMessage);
//                    if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_TEXT))
//                        sendMessage(((TextMessage)baseMessage).getText());
//                }
//            }
//        });
    }

    public void add(BaseMessage baseMessage) {
        if (messageAdapter != null)
            messageAdapter.addMessage(baseMessage);
    }

    public void remove(BaseMessage baseMessage) {
        baseMessageList.remove(baseMessage);
        checkNoMessages();
        if (messageAdapter != null)
            messageAdapter.remove(baseMessage);
    }

    /**
     * This method is used to delete the message.
     *
     * @param baseMessage is an object of BaseMessage which is being used to delete the message.
     * @see BaseMessage
     * @see CometChat#deleteMessage(int, CometChat.CallbackListener)
     */
    public void deleteMessage(BaseMessage baseMessage) {
        CometChat.deleteMessage(baseMessage.getId(), new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                if (messageAdapter != null) {
                    if (hideDeleteMessage)
                        remove(baseMessage);
                    else
                        messageAdapter.updateMessage(baseMessage);
                }
                CometChatUIKitHelper.onMessageDelete(baseMessage,MessageStatus.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                Log.e("awseedesd", "delete_onError: " + e.getMessage());
                Log.d(TAG, "onError: " + e.getMessage());
                CometChatUIKitHelper.onMessageError(e, baseMessage);
                hideError(DELETE_ERROR);
            }
        });
    }

    public void actionSheetMode(@ActionSheet.LayoutMode String actionSheetMode) {
        this.actionSheetMode = actionSheetMode;
    }

    public void messageListAlignment(@MessageListAlignment String messageListAlignment) {
        this.messageListAlignment = messageListAlignment;
        if (messageAdapter != null) {
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

        textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
        textMessage.setSender(loggedInUser);
        textMessage.setMuid(System.currentTimeMillis() + "");
        if (messageAdapter != null) {
            soundManager.play(Sound.outgoingMessage);
            messageAdapter.addMessage(textMessage);
            scrollToBottom();
        }

        smartReplies.setVisibility(GONE);
        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                if (messageAdapter != null)
                    messageAdapter.updateMessageFromMuid(textMessage);
                CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                CometChatUIKitHelper.onMessageError(e, textMessage);
                if (!e.getCode().equalsIgnoreCase("ERR_BLOCKED_BY_EXTENSION")) {
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

    private CometChatMessageTemplate getMessageTemplateById(String id) {
        for (CometChatMessageTemplate cometChatMessageTemplate : messageTypes) {
            if (cometChatMessageTemplate.getId() != null) {
                if (cometChatMessageTemplate.getId().equalsIgnoreCase(id)) {
                    return cometChatMessageTemplate;
                }
            }
        }
        return null;
    }

    private void fetchOptions() {
        resetOptions();
        CometChatMessageTemplate messageTemplate =
                getMessageTemplateById(baseMessage.getType());
        if (messageTemplate != null) {
            if (!customMessageOptions.isEmpty()) {
                messageTemplate.getOptions().addAll(customMessageOptions);
            }
            List<CometChatOptions> options = new ArrayList<>();
            if (messageTemplate.getOptions() != null)
                options.addAll(messageTemplate.getOptions());
            if (!excludeMessageOptions.isEmpty()) {
                for (CometChatOptions option : messageTemplate.getOptions()) {
                    if (option.getId() != null && excludeMessageOptions.contains(option.getId())) {
                        options.remove(option);
                    }
                }
            }
            if (options != null) {
                for (CometChatOptions item : options) {
                    if (item != null && item.getId() != null) {
                        if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.TRANSLATE)) {
                            translateVisible = true;
                            translateIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                translateClick = item.getOnClick();
                        } else if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.REPLY)) {
                            replyVisible = true;
                            replyIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                replyClick = item.getOnClick();
                        } else if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.FORWARD)) {
                            forwardVisible = true;
                            forwardIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                forwardClick = item.getOnClick();
                        } else if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.SHARE)) {
                            shareVisible = true;
                            shareIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                shareClick = item.getOnClick();
                        } else if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.COPY)) {
                            copyVisible = true;
                            copyIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                copyClick = item.getOnClick();
                        } else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.reply_message_privately))) {
                            replyPrivatelyVisible = true;
                            replyPrivatelyIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                replyPrivatelyClick = item.getOnClick();
                        } else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.send_message_privately))) {
                            sendMessagePrivatelyVisible = true;
                            sendMessagePrivatelyIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                sendMessagePrivatelyClick = item.getOnClick();
                        } else if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.EDIT)) {
                            editVisible = true;
                            editIcon = item.getIcon();
                            if (item.getOnClick() != null)
                                editClick = item.getOnClick();
                        } else if (item.getId().equalsIgnoreCase(UIKitConstants.DefaultOptions.DELETE)) {
                            deleteIcon = item.getIcon();
                            if (baseMessage != null) {
                                if (baseMessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                                    deleteVisible = true;
                                } else {
                                    editVisible = false;
                                    deleteVisible = loggedInUserScope != null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN)
                                            || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR));
                                }
                                if (item.getOnClick() != null)
                                    deleteClick = item.getOnClick();
                            }
                        } else {
                            if (!customOption.contains(item))
                                customOption.add(item);
                        }
                    }
                }
            }
        }
        reactionVisible = true;
    }

    private void resetOptions() {
        translateVisible = false;
        editVisible = false;
        replyVisible = false;
        forwardVisible = false;
        deleteVisible = false;
        sendMessagePrivatelyVisible = false;
        reactionVisible = false;
        copyVisible = false;
        messageInformationVisible = false;
        replyPrivatelyVisible = false;
        shareVisible = false;
        threadVisible = false;
    }

    private void copyMessage(BaseMessage baseMessage) {
        String message = ((TextMessage) baseMessage).getText();
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("MessageAdapter", message);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
    }

    private void translateMessage(BaseMessage baseMessage) {
        try {
            Log.e(TAG, "translateMessage: "+baseMessage.getId());
            String localeLanguage = Locale.getDefault().getLanguage();
            JSONObject body = new JSONObject();
            JSONArray languages = new JSONArray();
            languages.put(localeLanguage);
            body.put("msgId", baseMessage.getId());
            body.put("languages", languages);
            body.put("text", ((TextMessage) baseMessage).getText());
            CometChat.callExtension("message-translation", "POST", "/v2/translate", body,
                    new CometChat.CallbackListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            if (Extensions.isMessageTranslated(jsonObject, ((TextMessage) baseMessage).getText())) {
                                if (baseMessage.getMetadata() != null) {
                                    JSONObject meta = baseMessage.getMetadata();
                                    try {
                                        meta.accumulate("values", jsonObject);
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
                                CometChatSnackBar.show(context, rvChatListView,
                                        context.getString(R.string.translation_error), CometChatSnackBar.WARNING);
                            }
                        }

                        @Override
                        public void onError(CometChatException e) {
                            CometChatUIKitHelper.onMessageError(e, baseMessage);
                            hideError(TRANSLATE_ERROR);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replyMessagePrivately(BaseMessage baseMessage) {
        if (baseMessage != null) {
            User user = baseMessage.getSender();
            Intent intent = new Intent(context, CometChatMessagesActivity.class);
            intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
            intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
            intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
            intent.putExtra(UIKitConstants.IntentStrings.LINK, user.getLink());
            intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
            intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE, baseMessage.getRawMessage().toString());
            if (context != null) {
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }
    }

    public static void addListener(String TAG, CometChatMessageEvents messageListEvent) {
        messageListEvents.put(TAG, messageListEvent);
    }

    public void updateOptimisticMessage(BaseMessage baseMessage) {
        if (baseMessage != null) {
            if (messageAdapter != null)
                messageAdapter.updateMessageFromMuid(baseMessage);
            if (!baseMessageList.isEmpty() && baseMessageList.contains(baseMessage)) {
                baseMessageList.set(baseMessageList.indexOf(baseMessage), baseMessage);
            } else {
                baseMessageList.add(baseMessage);
            }
            checkNoMessages();
        }
    }

    public void highLightMessage(int id) {
        if (messageAdapter != null) {
            messageAdapter.setSelectedMessage(id);
            messageAdapter.notifyDataSetChanged();
        }
    }


    public static final String LEFT_ALIGNED = "LEFT";
    public static final String STANDARD = "STANDARD";

    @StringDef({LEFT_ALIGNED, STANDARD})
    public @interface MessageListAlignment {
    }

    public static String getValue(int pos) {
        if (pos == 0)
            return STANDARD;
        else
            return LEFT_ALIGNED;
    }

    /**
     * This method is used to remove group listener from message list component.
     *
     * @see CometChat#removeGroupListener(String)
     */
    public void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    /**
     * This method is used to receive real time group events like onMemberAddedToGroup, onGroupMemberJoined,
     * onGroupMemberKicked, onGroupMemberLeft, onGroupMemberBanned, onGroupMemberUnbanned,
     * onGroupMemberScopeChanged.
     *
     * @see CometChat#addGroupListener(String, CometChat.GroupListener)
     */
    private void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
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
    }

    /**
     * This method is used to add message listener to recieve real time messages between users &
     * groups. It also give real time events for typing indicators, edit message, delete message,
     * message being read & delivered.
     *
     * @see CometChat#addMessageListener(String, CometChat.MessageListener)
     */
    private void addMessageListener() {

        CometChat.addMessageListener(TAG, new CometChat.MessageListener() {
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
                if (hideDeleteMessage)
                    remove(message);
                else
                    updateMessage(message);

            }
        });
    }

    /**
     * This method is used to remove message listener
     *
     * @see CometChat#removeMessageListener(String)
     */
    private void removeMessageListener() {
        CometChat.removeMessageListener(TAG);
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof MessageListConfiguration) {
            MessageListConfiguration messageListConfiguration = (MessageListConfiguration) configuration;
            messageListAlignment(messageListConfiguration.getMessageListAlignment());
            hideDeleteMessage(messageListConfiguration.isDeleteMessageHidden());
        } else if (configuration instanceof MessageBubbleConfiguration) {
            messageBubbleConfig = configuration;
            if (messageAdapter != null)
                messageAdapter.setMessageBubbleConfiguration(configuration);
        }
    }

}
