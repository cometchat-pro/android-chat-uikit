package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.Sound;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;


/**
 * Purpose - CometChatConversationList class is a subclass of recyclerview and used as component by
 * developer to display list of conversation. Developer just need to fetchConversation at their end
 * and pass it to this component to display list of conversation. It helps user to create conversation
 * list easily and saves their time.
 *
 * @see Conversation
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 23rd March 2022
 */

@BindingMethods(value = {@BindingMethod(type = CometChatConversationList.class, attribute = "app:conversationlist", method = "setConversationList")})
public class CometChatConversationList extends MaterialCardView {

    private final Context context;

    private CometChatConversationListViewModel conversationViewModel;

    private ConversationsRequest conversationsRequest;    //Uses to fetch Conversations.

    private final List<Conversation> conversationList = new ArrayList<>();

    private String conversationListType; // Used to set Conversation Type i.e Group or User

    private RecyclerView recyclerView;

    private ShimmerFrameLayout conversationShimmer;

    private LinearLayout noConversationView; // Used to display a information when no conversation are fetched.

    private View view;

    private CometChatSoundManager soundManager;

    private static final String TAG = "CometChatConversation";

    private onErrorCallBack onErrorCallBack;

    private boolean userAndGroupTags, hideError;

    private View emptyView = null;

    private LinearLayout custom_layout;

    private AttributeSet attrs;

    private int limit;

    private List<String> tags = new ArrayList<>();

    private TextView noListText;
    private FontUtils fontUtils;

    private String errorMessageFont = null;
    private int errorMessageColor = 0;
    private String error_text = null;
    private String empty_text = null;
    private View errorView = null;

    public CometChatConversationList(@NonNull Context context) {
        super(context);
        this.context = context;
        initComponentView();
    }

    public CometChatConversationList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initComponentView();
    }

    public CometChatConversationList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        initComponentView();
    }

    private void setViewModel() {
        if (conversationViewModel == null)
            conversationViewModel = new CometChatConversationListViewModel(context, this);
    }

    /**
     * This
     */

    private void initComponentView() {
        view = View.inflate(context, R.layout.cometchat_list, null);
        fontUtils = FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatConversationList,
                0, 0);

        limit = a.getInt(R.styleable.CometChatConversationList_limit, 30);
        conversationListType = a.getString(R.styleable.CometChatConversationList_conversationType);
        hideError = a.getBoolean(R.styleable.CometChatConversationList_hideError, false);
        userAndGroupTags = a.getBoolean(R.styleable.CometChatConversationList_userAndGroupTags, false);
        empty_text = a.getString(R.styleable.CometChatConversationList_empty_text);
        error_text = a.getString(R.styleable.CometChatConversationList_error_text);

        custom_layout = view.findViewById(R.id.empty_view);
        soundManager = new CometChatSoundManager(context);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        conversationShimmer = view.findViewById(R.id.shimmer_layout);
        noConversationView = view.findViewById(R.id.no_list_view);
        noListText = view.findViewById(R.id.no_list_text);
        setNoListMessage(empty_text);
        addView(view);
        setViewModel();

        // Uses to fetch next list of conversations if rvConversationList (RecyclerView) is scrolled in upward direction.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    makeConversationList();
                }

            }
        });

    }

    public void setNoListMessage(String message) {
        if (message != null)
            noListText.setText(message);
        else
            noListText.setText(getResources().getString(R.string.no_chats));

    }

    public void setStyle(Style style) {
        if (style.getBackground() != 0) {
            setCardBackgroundColor(style.getBackground());
        }
        if (style.getBorder() != 0) {
            setStrokeWidth(style.getBorder());
        }
        if (style.getCornerRadius() != 0) {
            setRadius(style.getCornerRadius());
        }

        if (style.getEmptyStateTextFont() != null) {
            setNoListTextFont(style.getEmptyStateTextFont());
        }
        if (style.getEmptyStateTextColor() != 0) {
            setNoListTextColor(style.getEmptyStateTextColor());
        }
        if (style.getErrorStateTextFont() != null) {
            errorMessageFont = style.getErrorStateTextFont();
        }
        if (style.getErrorStateTextColor() != 0) {
            errorMessageColor = style.getErrorStateTextColor();
        }
    }

    public void setNoListTextColor(int color) {
        if (color != 0)
            noListText.setTextColor(color);
    }

    public void setNoListTextFont(String font) {
        if (font != null)
            noListText.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setNoListTextAppearance(int appearance) {
        if (appearance != 0)
            noListText.setTextAppearance(context, appearance);

    }

    /**
     * This method set the fetched list into the CometChatConversationList Component.
     *
     * @param conversationList to set into the view CometChatConversationList
     */
    public void setConversationList(List<Conversation> conversationList) {
        if (conversationViewModel != null)
            conversationViewModel.setConversationList(conversationList);
    }

    /**
     * This methods updates the conversation item or add if not present in the list
     *
     * @param conversation to be added or updated
     */
    public void update(Conversation conversation, boolean isActionMessage) {
        if (conversationViewModel != null)
            conversationViewModel.update(conversation, isActionMessage);
    }

    /**
     * provide way to remove a particular conversation from the list
     *
     * @param conversation to be removed
     */
    public void remove(Conversation conversation) {
        if (conversationViewModel != null)
            conversationViewModel.remove(conversation);
    }


    /**
     * This method helps to get Click events of CometChatConversationList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener<Conversation> onItemClickListener) {

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                Conversation conversation = (Conversation) var1.getTag(R.string.conversation);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(conversation, var2);
                else
                    throw new NullPointerException("OnItemClickListener<Conversation> is null");
            }

            @Override
            public void onLongClick(View var1, int var2) {
                Conversation conversation = (Conversation) var1.getTag(R.string.conversation);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(conversation, var2);
                else
                    throw new NullPointerException("OnItemClickListener<Conversation> is null");

            }
        }));

    }

    /**
     * This method is used to perform search operation in a list of conversations.
     *
     * @param searchString is a String object which will be searched in conversation.
     * @see CometChatConversationListViewModel#searchConversation(String)
     */
    public void searchConversation(String searchString) {
        conversationViewModel.searchConversation(searchString);
    }

    /**
     * This method is used to refresh conversation list if any new conversation is initiated or updated.
     * It converts the message recieved from message listener using <code>CometChatHelper.getConversationFromMessage(message)</code>
     *
     * @param message
     * @see CometChatHelper#getConversationFromMessage(BaseMessage)
     * @see Conversation
     */
    public void refreshSingleConversation(BaseMessage message, boolean isActionMessage) {
        Conversation newConversation = CometChatHelper.getConversationFromMessage(message);
        JSONObject metadata = message.getMetadata();
        if (metadata != null && metadata.has("incrementUnreadCount")) {
            soundManager.play(Sound.incomingMessageFromOther);
        }
        update(newConversation, isActionMessage);
    }

    public void setUserAndGroupTags(boolean userAndGroupTags) {
        this.userAndGroupTags = userAndGroupTags;
    }

    public void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setEmptyView(int id) {
        try {
            emptyView = View.inflate(context, id, null);

        } catch (Exception e) {
            emptyView = null;
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @setErrorView is method allows you to set layout, show when there is a error
     * if user want to set Error layout other wise it will load default layout
     */
    public void setErrorView(int id) {
        try {
            errorView = View.inflate(context, id, null);
        } catch (Exception e) {
            errorView = null;
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public Conversation getConversation(int position) {
        Conversation conversation = null;
        if (conversationViewModel != null)
            conversation = conversationViewModel.getConversation(position);
        return conversation;
    }

    /**
     * This method is used to update Reciept of conversation from conversationList.
     *
     * @param messageReceipt is object of MessageReceipt which is recieved in real-time.
     * @see MessageReceipt
     */
    public void setReciept(MessageReceipt messageReceipt) {
        if (conversationViewModel != null && messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (messageReceipt.getReceiptType().equals(MessageReceipt.RECEIPT_TYPE_DELIVERED))
                conversationViewModel.setDeliveredReceipts(messageReceipt);
            else
                conversationViewModel.setReadReceipts(messageReceipt);
        }
    }

    /**
     * This method is used to clear a list of conversation present in CometChatConversationList Component
     *
     * @see CometChatConversationListViewModel#clear()
     */
    public void clearList() {
        conversationList.clear();
        conversationsRequest = null;
        if (conversationViewModel != null)
            conversationViewModel.clear();
        makeConversationList();
    }

    public int size() {
        return conversationViewModel.size();
    }

    public void setConversationListItemProperty(boolean hideAvatar, boolean hideUserPresenceListItem,
                                                boolean hideTitleListItem, int titleColorListItem,
                                                boolean hideSubtitleListItem, int subTitleColorListItem,
                                                boolean hideHelperTextListItem, int helperTextColorListItem,
                                                boolean hideTimeListItem, int timeTextColorListItem,
                                                int backgroundColorListItem, int backroundColorListItemPosition,
                                                float cornerRadiusListItem,
                                                int typingIndicatorColorListItem) {
        if (conversationViewModel != null)
            conversationViewModel.setConversationListItemProperty(hideAvatar,
                    hideUserPresenceListItem, hideTitleListItem, titleColorListItem,
                    hideSubtitleListItem, subTitleColorListItem, hideHelperTextListItem,
                    helperTextColorListItem, hideTimeListItem, timeTextColorListItem,
                    backgroundColorListItem, cornerRadiusListItem, typingIndicatorColorListItem);
    }

    public void setTypingIndicator(TypingIndicator typingIndicator, boolean b) {
        if (conversationViewModel != null)
            conversationViewModel.setTypingIndicator(typingIndicator, b);
    }

    /**
     * This method is used to retrieve list of conversations you have done.
     * For more detail please visit our official documentation {@link "https://prodocs.cometchat.com/docs/android-messaging-retrieve-conversations" }
     *
     * @see ConversationsRequest
     */

    private void makeConversationList() {
        if (conversationsRequest == null) {
            conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().
                    setConversationType(conversationListType)
                    .setTags(tags)
                    .withUserAndGroupTags(userAndGroupTags)
                    .setLimit(limit)
                    .build();
        }
        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                conversationList.addAll(conversations);
                if (conversationList.size() != 0) {
                    stopHideShimmer();
                    noConversationView.setVisibility(View.GONE);
                    setConversationList(conversations);
                    custom_layout.setVisibility(GONE);
                } else {
                    checkNoConversation();
                }
            }

            @Override
            public void onError(CometChatException e) {
                hideError();
                throwError(e);
                stopHideShimmer();
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    /**
     * This method is used to hide shimmer effect if the list is loaded.
     */
    private void stopHideShimmer() {
        conversationShimmer.stopShimmer();
        conversationShimmer.setVisibility(View.GONE);
    }

    private void checkNoConversation() {
        if (size() == 0) {
            stopHideShimmer();
            if (emptyView != null) {
                custom_layout.setVisibility(VISIBLE);
                custom_layout.removeAllViews();
                custom_layout.addView(emptyView);
            } else {
                noConversationView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else {
            noConversationView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            custom_layout.setVisibility(GONE);
        }
    }

    /**
     * This method is used to refresh the Conversation List shown in CometChatConversationList.
     * It clears the CometChatConversationList and fetches the fresh list of conversation and set it
     * in CometChatConversationList. Also same conversation list is returned in callback.
     *
     * @param callbackListener - It is object of CallBackListener which returns two events
     *                         <code>OnSuccess(List<Conversation> conversationList)</code>
     *                         <code>OnError(CometChatException e)</code>
     * @see ConversationsRequest
     */

    public void refreshConversation(CometChat.CallbackListener callbackListener) {
        clearList();
        conversationList.clear();
        conversationsRequest = null;
        conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().
                setConversationType(conversationListType)
                .setTags(tags).withUserAndGroupTags(userAndGroupTags)
                .setLimit(limit)
                .build();
        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                conversationList.addAll(conversations);
                if (conversationList.size() != 0) {
                    stopHideShimmer();
                    noConversationView.setVisibility(View.GONE);
                    setConversationList(conversations);
                    custom_layout.setVisibility(GONE);
                } else {
                    checkNoConversation();
                }
                callbackListener.onSuccess(conversationList);
            }

            @Override
            public void onError(CometChatException e) {
                stopHideShimmer();
                throwError(e);
                hideError();
                callbackListener.onSuccess(conversationList);
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    public RecyclerView getRecyclerView() {
        if (recyclerView != null)
            return recyclerView;
        return null;
    }

    /**
     * This method has message listener which recieve real time message and based on these messages, conversations are updated.
     *
     * @see CometChat#addMessageListener(String, CometChat.MessageListener)
     */
    public void addConversationListener() {
        CometChat.addMessageListener(TAG, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage message) {
                if (!message.getSender().getUid().equalsIgnoreCase(CometChat.getLoggedInUser().getUid()))
                    CometChat.markAsDelivered(message);
                if (recyclerView != null) {
                    refreshSingleConversation(message, false);
                    checkNoConversation();
                }
            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                if (!message.getSender().getUid().equalsIgnoreCase(CometChat.getLoggedInUser().getUid()))
                    CometChat.markAsDelivered(message);
                if (recyclerView != null) {
                    refreshSingleConversation(message, false);
                    checkNoConversation();
                }
            }

            @Override
            public void onCustomMessageReceived(CustomMessage message) {
                if (!message.getSender().getUid().equalsIgnoreCase(CometChat.getLoggedInUser().getUid()))
                    CometChat.markAsDelivered(message);
                if (recyclerView != null) {
                    refreshSingleConversation(message, false);
                    checkNoConversation();
                }
            }

            @Override
            public void onMessagesDelivered(MessageReceipt messageReceipt) {
                if (recyclerView != null)
                    setReciept(messageReceipt);
            }

            @Override
            public void onMessagesRead(MessageReceipt messageReceipt) {
                if (recyclerView != null)
                    setReciept(messageReceipt);
            }

            @Override
            public void onMessageEdited(BaseMessage message) {
                if (recyclerView != null)
                    refreshSingleConversation(message, true);
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                if (recyclerView != null)
                    refreshSingleConversation(message, true);
            }

            @Override
            public void onTypingStarted(TypingIndicator typingIndicator) {
                if (recyclerView != null)
                    setTypingIndicator(typingIndicator, false);
            }

            @Override
            public void onTypingEnded(TypingIndicator typingIndicator) {
                if (recyclerView != null)
                    setTypingIndicator(typingIndicator, true);
            }
        });

        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                Log.e(TAG, "onGroupMemberKicked: " + kickedUser);
                if (kickedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (recyclerView != null)
                        updateConversationForGroup(action, true);
                } else {
                    updateConversationForGroup(action, false);
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                Log.e(TAG, "onMemberAddedToGroup: ");
                updateConversationForGroup(action, false);
            }

            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                Log.e(TAG, "onGroupMemberJoined: ");
                updateConversationForGroup(action, false);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                Log.e(TAG, "onGroupMemberLeft: ");
                updateConversationForGroup(action, leftUser.getUid().equals(CometChat.getLoggedInUser().getUid()));
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                updateConversationForGroup(action, false);
            }
        });
    }

    /**
     * This method is used to update conversation received in real-time.
     *
     * @param baseMessage is object of BaseMessage.class used to get respective Conversation.
     * @param isRemove    is boolean used to check whether conversation needs to be removed or not.
     * @see CometChatHelper#getConversationFromMessage(BaseMessage) This method return the conversation
     * of receiver using baseMessage.
     */
    private void updateConversationForGroup(BaseMessage baseMessage, boolean isRemove) {
        if (recyclerView != null) {
            Conversation conversation = CometChatHelper.getConversationFromMessage(baseMessage);
            if (isRemove)
                remove(conversation);
            else
                update(conversation, false);
            checkNoConversation();
        }
    }

    /**
     * This method is used to remove the conversationlistener.
     */
    public void removeConversationListener() {
        CometChat.removeMessageListener(TAG);
        CometChat.removeGroupListener(TAG);
    }

    public void setConversationType(String conversationListType) {
        this.conversationListType = conversationListType;
        clearList();
    }

    public void onPause() {
        removeConversationListener();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
    }

    private void hideError() {
        String error_message;
        if (error_text != null)
            error_message = error_text;
        else
            error_message = getContext().getString(R.string.error_cant_load_conversation);


        if (!hideError && errorView != null) {
            custom_layout.removeAllViews();
            custom_layout.addView(errorView);
            custom_layout.setVisibility(VISIBLE);
        } else {
            custom_layout.setVisibility(GONE);
            if (!hideError) {
                if (getContext() != null) {
                    new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, error_message, null, getContext().getString(R.string.try_again), "", getResources().getString(R.string.cancel), new OnAlertDialogButtonClickListener() {
                        @Override
                        public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                alertDialog.dismiss();
                                makeConversationList();
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                alertDialog.dismiss();
                            }
                        }
                    }, 0, false);
                }
            }

        }

    }


    private void throwError(CometChatException e) {
        if (onErrorCallBack != null)
            onErrorCallBack.onError(e);

    }

    /**
     * This method is used to handle onResume state of CometChatConversationList
     *
     * @author CometChat Team
     * @copyright © 2021 CometChat Inc.
     * @see CometChatConversationList
     */
    public void onResume() {
        conversationsRequest = null;
        conversationViewModel.clear();
        makeConversationList();
        addConversationListener();
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        conversationViewModel.setConfiguration(configuration);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        conversationViewModel.setConfiguration(configurations);
    }

    public void addOnErrorListener(onErrorCallBack onErrorCallBack) {
        this.onErrorCallBack = onErrorCallBack;
    }

    public interface onErrorCallBack {
        void onError(CometChatException exception);
    }
}
