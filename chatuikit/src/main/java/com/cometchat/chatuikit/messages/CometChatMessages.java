package com.cometchat.chatuikit.messages;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.details.CometChatDetails;
import com.cometchat.chatuikit.details.DetailsConfiguration;
import com.cometchat.chatuikit.groups.CometChatGroupActivity;
import com.cometchat.chatuikit.messagecomposer.CometChatMessageComposer;
import com.cometchat.chatuikit.messagecomposer.MessageComposerConfiguration;
import com.cometchat.chatuikit.messageheader.CometChatMessageHeader;
import com.cometchat.chatuikit.messageheader.MessageHeaderConfiguration;
import com.cometchat.chatuikit.messagelist.CometChatMessageList;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.AudioPlayer;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.keyboard_utils.KeyBoardUtils;
import com.cometchat.chatuikit.threadedmessages.ThreadedMessagesActivity;
import com.cometchat.chatuikit.threadedmessages.ThreadedMessagesConfiguration;
import com.cometchat.chatuikit.users.CometChatUserActivity;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

/**
 * The CometChatMessages class is a custom view that represents a chat conversation screen.
 * It extends the MaterialCardView class to provide a card-like appearance.
 * The purpose of this class is to display a chat conversation between users or within a group.
 * <br>
 * The CometChatMessages class provides methods to initialize and set up the chat conversation view, handle user interactions, and update the UI based on incoming messages and other events. It also interacts with the MessagesViewModel to retrieve and update data related to the chat conversation.
 * <p>
 * Overall, the CometChatMessages class encapsulates the necessary components and logic for displaying and managing a chat conversation within an application.
 * <br>
 * The class contains several components:
 * <br>
 * {@link  CometChatMessageList } : Represents the message list view where the chat messages are displayed.
 * <br>
 * {@link  CometChatMessageHeader } : Represents the header view of the chat conversation, which typically contains information about the participants or the group.
 * <br>
 * {@link  CometChatMessageComposer } : Represents the composer view where users can type and send new messages.
 * <br>
 * Example :
 * <pre>
 * {@code
 *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
 *  if (user != null) cometChatMessages.setUser(user);
 *  else if (group != null) cometChatMessages.setGroup(group);
 * }
 * </pre>
 */
public class CometChatMessages extends MaterialCardView {

    private static final String TAG = CometChatMessages.class.getName();
    private Context context;
    private CometChatMessageList messageList;
    private CometChatMessageHeader messageHeader;
    private CometChatMessageComposer messageComposer;
    private LinearLayout messageListContainer, messageHeaderContainer, messageComposerContainer;
    private User user;
    private FrameLayout reactionLayout;
    private Group group;
    private String id;
    private String type;
    private int count = 0;
    public ObjectAnimator animation;
    private MessagesViewModel messagesViewModel;
    private ImageView imageView;
    private CometChatTheme theme;
    private MessageListConfiguration messageListConfiguration;
    private ThreadedMessagesConfiguration threadedMessagesConfiguration;
    private DetailsConfiguration detailsConfiguration;
    private ImageView detailIcon;
    private LinearLayout layout;
    private LinearLayout.LayoutParams params;

    public CometChatMessages(Context context) {
        super(context);
        init(context);
    }

    public CometChatMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        ((FragmentActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        theme = CometChatTheme.getInstance(context);
        View view = View.inflate(context, R.layout.messages_layout, null);
        layout = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        layout.setLayoutParams(params);
        messageList = view.findViewById(R.id.message_list);
        messageHeader = view.findViewById(R.id.header);
        messageComposer = view.findViewById(R.id.composer);
        messageComposerContainer = view.findViewById(R.id.message_bottom_container);
        messageHeaderContainer = view.findViewById(R.id.message_header_view);
        messageListContainer = view.findViewById(R.id.message_body_container);
        reactionLayout = view.findViewById(R.id.reactions_container);
        messagesViewModel = new MessagesViewModel();
        messagesViewModel = ViewModelProviders.of((FragmentActivity) context).get(messagesViewModel.getClass());
        messagesViewModel.getReceiveLiveReaction().observe((AppCompatActivity) context, this::receiveLiveReaction);
        messagesViewModel.getUpdateGroup().observe((AppCompatActivity) context, this::setGroup);
        messagesViewModel.getUpdateUser().observe((AppCompatActivity) context, this::setUser);
        messagesViewModel.getGoBack().observe((AppCompatActivity) context, this::goBack);
        KeyBoardUtils.setKeyboardVisibilityListener((Activity) context, view, keyboardVisible -> {
            if (keyboardVisible) {
                if (messageList.atBottom()) messageList.scrollToBottom();
                for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                    events.hidePanel(messageComposer.getComposerViewModel().getIdMap(), UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM);
                }
            }
        });
        createInfoIcon();
        messageList.setOnThreadRepliesClick((context1, baseMessage, messageBubble) -> ThreadedMessagesActivity.launch(context1, baseMessage, messageBubble, threadedMessagesConfiguration));
        addView(view);
        setStyle(new MessagesStyle().setBackground(theme.getPalette().getGradientBackground()).setBackground(theme.getPalette().getBackground()));
    }

    private void createInfoIcon() {
        detailIcon = new ImageView(context);
        detailIcon.setImageResource(R.drawable.ic_info);
        detailIcon.setImageTintList(ColorStateList.valueOf(theme.getPalette().getPrimary()));
        detailIcon.setOnClickListener(view -> {
            if (user != null) {
                CometChatUserActivity.launch(context, CometChatDetails.class, user, detailsConfiguration);
            } else if (group != null) {
                CometChatGroupActivity.launch(context, CometChatDetails.class, group, detailsConfiguration);
            }
        });
        int val = Utils.convertDpToPx(context, 5);
        params.leftMargin = val;
        detailIcon.setLayoutParams(params);
    }

    /**
     * The setAuxiliaryHeaderMenu method is a public method within the CometChatMessages class. It takes a {@link Function3 } object as a parameter, which represents a function that accepts three arguments (Context, User, and Group) and returns a View object.
     * <br>
     * Example :
     * <pre>{@code
     *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
     *  if (user != null) cometChatMessages.setUser(user);
     *  else if (group != null) cometChatMessages.setGroup(group);
     *  cometChatMessages.setAuxiliaryHeaderMenu((context, user, group) -> {
     *  View view = View.inflate(context,R.layout.example_layout,null);
     *  return view;
     * });
     * }
     * </pre>
     *
     * @param auxiliaryOption
     */
    public void setAuxiliaryHeaderMenu(Function3<Context, User, Group, View> auxiliaryOption) {
        if (auxiliaryOption != null) {
            layout.addView(auxiliaryOption.apply(context, user, group));
        }
    }

    public void goBack(Void v) {
        ((AppCompatActivity) context).onBackPressed();
    }

    /**
     * The setUser method is a public method within a class that takes a {@link User} object as a parameter.
     * This method is responsible for setting the user for the chat screen.
     * <br>
     * Example :
     * <pre>{@code
     *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
     *  cometChatMessages.setUser(user);
     * }
     * </pre>
     *
     * @param user
     */
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            id = user.getUid();
            type = UIKitConstants.ReceiverType.USER;
            messageHeader.setUser(user);
            messageList.setUser(user);
            messageComposer.setUser(user);
        }
    }

    /**
     * The setGroup method is a public method within a class that takes a {@link Group} object as a parameter.
     * This method is responsible for setting the group for the chat screen.
     * <br>
     * Example :
     * <pre>{@code
     *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
     *  cometChatMessages.setGroup(group);
     * }
     * </pre>
     *
     * @param group
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            id = group.getGuid();
            type = UIKitConstants.ReceiverType.GROUP;
            messageHeader.setGroup(group);
            messageList.setGroup(group);
            messageComposer.setGroup(group);
        }
    }

    /**
     * The setStyle method sets the visual style of the CometChatMessages using a provided MessagesStyle object.
     * It allows customization of attributes such as background, border width, corner radius, and border color.
     * If the style parameter is not null, the specified style attributes are applied to the object.
     * If style is null, no changes to the style will be made.
     *
     * @param style The {@link MessagesStyle} object containing the style attributes to be applied.
     *              If null, no style changes will be made.
     */
    public void setStyle(MessagesStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Hides or shows the details icon based on the provided flag.
     *
     * @param hideDetails A boolean flag indicating whether to hide or show the details icon.
     *                    If true, the details icon will be hidden. If false, the details icon will be shown.
     */
    public void hideDetails(boolean hideDetails) {
        detailIcon.setVisibility(hideDetails ? GONE : VISIBLE);
    }

    /**
     * Sets a custom message header view using the provided function.
     * <br>
     * Example :
     * <pre>{@code
     *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
     *  if (user != null) cometChatMessages.setUser(user);
     *  else if (group != null) cometChatMessages.setGroup(group);
     *  cometChatMessages.setMessageHeaderView((context, user, group) -> {
     *             TextView textView = new TextView(context);
     *             textView.setText("custom header view");
     *             return textView;
     *         });
     * }
     * </pre>
     *
     * @param messageHeaderView A function that takes a Context, User, and Group as parameters and returns a View.
     *                          This function is responsible for creating and returning the custom message header view.
     *                          If null is passed, the existing message header view will not be modified.
     */
    public void setMessageHeaderView(Function3<Context, User, Group, View> messageHeaderView) {
        if (messageHeaderView != null)
            Utils.handleView(messageHeaderContainer, messageHeaderView.apply(context, user, group), false);
    }

    /**
     * Sets a custom message composer view using the provided function.
     * <br>
     * Example :
     * <pre>
     *     {@code
     *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
     *  if (user != null) cometChatMessages.setUser(user);
     *  else if (group != null) cometChatMessages.setGroup(group);
     *  cometChatMessages.setMessageComposerView(new Function3< Context, User, Group, View>() { @Override
     *  public View apply(Context context, User user, Group group) {
     *    EditText editText = new EditText(context);
     *    return editText;
     * }
     * });
     * }
     * </pre>
     *
     * @param messageComposerView A function that takes a Context, User, and Group as parameters and returns a View.
     *                            This function is responsible for creating and returning the custom message composer view.
     *                            If null is passed, the existing message composer view will not be modified.
     */
    public void setMessageComposerView(Function3<Context, User, Group, View> messageComposerView) {
        if (messageComposerView != null) {
            Utils.handleView(messageComposerContainer, messageComposerView.apply(context, user, group), false);
        }
    }

    /**
     * Sets a custom message list view using the provided function.
     * <br>
     * Example :
     * <pre>{@code
     *  CometChatMessages  cometChatMessages = findViewById(R.id.message_component);
     *  if (user != null) cometChatMessages.setUser(user);
     *  else if (group != null) cometChatMessages.setGroup(group);
     *  cometChatMessages.setMessageListView(new Function3< Context, User, Group, View>() { @Override
     *  public View apply(Context context, User user, Group group) {
     *     CometChatMessageList messageList = new CometChatMessageList(context);
     *     messageList.setUser(user);
     *     return messageList;
     * }
     * });
     * }
     * </pre>
     *
     * @param messageListView A function that takes a Context, User, and Group as parameters and returns a View.
     *                        This function is responsible for creating and returning the custom message list view.
     *                        If null is passed, the existing message list view will not be modified.
     */
    public void setMessageListView(Function3<Context, User, Group, View> messageListView) {
        if (messageListView != null)
            Utils.handleView(messageListContainer, messageListView.apply(context, user, group), false);
    }

    /**
     * Sets the typing status in the message composer to enable or disable typing events.
     *
     * @param disableTyping {@code true} to disable typing events, {@code false} to enable typing events.
     */
    public void disableTyping(boolean disableTyping) {
        messageComposer.disableTypingEvents(disableTyping);
    }

    /**
     * Sets the visibility of the message composer.
     *
     * @param hide {@code true} to hide the message composer, {@code false} to show the message composer.
     */
    public void hideMessageComposer(boolean hide) {
        if (hide) messageComposer.setVisibility(GONE);
        else messageComposer.setVisibility(VISIBLE);
    }

    /**
     * Sets the visibility of the message header.
     *
     * @param hide {@code true} to hide the message header, {@code false} to show the message header.
     */
    public void hideMessageHeader(boolean hide) {
        if (hide) messageHeader.setVisibility(GONE);
        else messageHeader.setVisibility(VISIBLE);
    }

    /**
     * Sets a custom sound resource for incoming messages in the message list.
     *
     * @param sound The resource ID of the custom sound to be played for incoming messages.
     */
    public void setCustomSoundForIncomingMessages(@RawRes int sound) {
        if (sound != 0) messageList.setCustomSoundForMessages(sound);
    }

    /**
     * Sets a custom sound resource for outgoing messages in the message composer.
     *
     * @param sound The resource ID of the custom sound to be played for outgoing messages.
     *              Use the {@code @RawRes} annotation to indicate the resource type.
     */
    public void setCustomSoundForOutgoingMessages(@RawRes int sound) {
        if (sound != 0) messageComposer.setCustomSoundForMessages(sound);
    }

    /**
     * Enables or disables the sound for messages in both the message list and message composer.
     *
     * @param disable {@code true} to disable the sound for messages, {@code false} to enable the sound for messages.
     */
    public void disableSoundForMessages(boolean disable) {
        messageList.disableSoundForMessages(disable);
        messageComposer.disableSoundForMessages(disable);
    }

    private void receiveLiveReaction(Void nothing) {
        setLiveReaction(messageComposer.getLiveReactionIcon());
    }

    private void stopLiveReaction() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(reactionLayout, "alpha", 0.2f);
        animator.setDuration(800);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (imageView != null) imageView.clearAnimation();
                reactionLayout.removeAllViews();
                count = 0;
            }
        });
    }

    private void setLiveReaction(int resId) {
        count++;
        reactionLayout.setAlpha(1.0f);
        animateLiveReaction(resId);
        if (count == 1) new Handler().postDelayed(this::stopLiveReaction, 1500);
    }

    private void animateLiveReaction(final int resId) {
        imageView = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        layoutParams.rightMargin = 16;
        imageView.setLayoutParams(layoutParams);
        reactionLayout.addView(imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if (bitmap != null) {
            try {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2f), (int) (bitmap.getHeight() * 0.2f), false);
                imageView.setImageBitmap(scaledBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        animation = ObjectAnimator.ofFloat(imageView, "translationY", -700f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setDuration(500);
        animation.start();
    }

    /**
     * Sets the configuration for threaded messages.
     *
     * @param configuration The configuration object specifying the settings for threaded messages.
     */
    public void setThreadedMessagesConfiguration(ThreadedMessagesConfiguration configuration) {
        this.threadedMessagesConfiguration = configuration;
    }

    /**
     * Sets the configuration for message details.
     *
     * @param detailsConfiguration The configuration object specifying the settings for message details.
     */
    public void setDetailsConfiguration(DetailsConfiguration detailsConfiguration) {
        this.detailsConfiguration = detailsConfiguration;
    }

    /**
     * Sets the configuration for the message list.
     *
     * @param configuration The configuration object specifying the settings for the message list.
     */
    public void setMessageListConfiguration(MessageListConfiguration configuration) {
        if (configuration != null && messageList != null) {
            this.messageListConfiguration = configuration;
            messageList.emptyStateText(configuration.getEmptyStateText());
            messageList.errorStateText(configuration.getErrorStateText());
            messageList.setLoadingStateView(configuration.getLoadingStateView());
            messageList.setEmptyStateView(configuration.getEmptyStateView());
            messageList.setErrorStateView(configuration.getErrorStateView());
            messageList.disableReceipt(configuration.isDisableReceipt());
            messageList.setReadIcon(configuration.getReadIcon());
            messageList.setDeliverIcon(configuration.getDeliverIcon());
            messageList.setSentIcon(configuration.getSentIcon());
            messageList.setWaitIconIcon(configuration.getWaitIconIcon());
            messageList.setAlignment(configuration.getListAlignment());
            messageList.showAvatar(configuration.isShowAvatar());
            messageList.setDatePattern(configuration.getDatePattern());
            messageList.setTimeStampAlignment(configuration.getTimeStampAlignment());
            messageList.setTemplates(configuration.getMessageTemplates());
            messageList.setNewMessageIndicatorText(configuration.getNewMessageIndicatorText());
            messageList.scrollToBottomOnNewMessage(configuration.isScrollToBottomOnNewMessages());
            messageList.setOnThreadRepliesClick(configuration.getOnThreadRepliesClick());
            messageList.setHeaderView(configuration.getHeaderView());
            messageList.setFooterView(configuration.getFooterView());
            messageList.setWrapperMessageBubbleStyle(configuration.getWrapperMessageBubbleStyle());
            messageList.setAvatarStyle(configuration.getAvatarStyle());
            messageList.setDateSeparatorStyle(configuration.getDateSeparatorStyle());
            messageList.setActionSheetStyle(configuration.getActionSheetStyle());
            messageList.setStyle(configuration.getMessageListStyle());
        }
    }

    /**
     * Sets the configuration for the message header.
     *
     * @param configuration The configuration object specifying the settings for the message header.
     */
    public void setMessageHeaderConfiguration(MessageHeaderConfiguration configuration) {
        if (configuration != null && messageHeader != null) {
            messageHeader.setSubtitleView(configuration.getSubtitle());
            messageHeader.setMenu(configuration.getMenu());
            messageHeader.disableUsersPresence(configuration.isDisableUsersPresence());
            messageHeader.setProtectedGroupIcon(configuration.getProtectedGroupIcon());
            messageHeader.setPrivateGroupIcon(configuration.getPrivateGroupIcon());
            messageHeader.setStyle(configuration.getStyle());
            messageHeader.setBackIcon(configuration.getBackIconView());
            messageHeader.hideBackIcon(configuration.isHideBackIcon());
            messageHeader.setListItemView(configuration.getListItemView());
            messageHeader.setAvatarStyle(configuration.getAvatarStyle());
            messageHeader.setStatusIndicatorStyle(configuration.getStatusIndicatorStyle());
        }
    }

    /**
     * Sets the configuration for the message composer.
     *
     * @param configuration The configuration object specifying the settings for the message composer.
     */
    public void setMessageComposerConfiguration(MessageComposerConfiguration configuration) {
        if (configuration != null && messageComposer != null) {
            messageComposer.setText(configuration.getText());
            messageComposer.setPlaceHolderText(configuration.getPlaceHolderText());
            messageComposer.setHeaderView(configuration.getHeaderView());
            messageComposer.setFooterView(configuration.getFooterView());
            messageComposer.setOnTextChangedListener(configuration.getTextChangedListener());
            messageComposer.setMaxLine(configuration.getMaxLine());
            messageComposer.setAttachmentIcon(configuration.getAttachmentIcon());
            messageComposer.setSendButtonIcon(configuration.getSendButtonIcon());
            messageComposer.setAttachmentOptions(configuration.getAttachmentOption());
            messageComposer.setSecondaryButtonView(configuration.getSecondaryButtonView());
            messageComposer.setSendButtonView(configuration.getSendButtonView());
            messageComposer.setAuxiliaryButtonView(configuration.getAuxiliaryButtonView());
            messageComposer.setAuxiliaryButtonAlignment(configuration.getAuxiliaryButtonAlignment());
            messageComposer.hideLiveReaction(configuration.isHideLiveReaction());
            messageComposer.setLiveReactionIcon(configuration.getLiveReactionIcon());
            messageComposer.setStyle(configuration.getStyle());
            messageComposer.setOnError(configuration.getOnError());
            messageComposer.setOnSendButtonClick(configuration.getSendButtonClick());
        }
    }

    private void setRequestBuilder() {
        if (messageListConfiguration != null) {
            configuredBuilder(messageListConfiguration.getMessagesRequestBuilder() != null);
        } else configuredBuilder(false);
    }

    private void configuredBuilder(boolean isConfigured) {
        if (isConfigured) {
            messageList.setMessagesRequestBuilder(messageListConfiguration.getMessagesRequestBuilder().hideReplies(true));
        } else {
            messageList.setMessagesRequestBuilder(new MessagesRequest.MessagesRequestBuilder().setTypes(ChatConfigurator.getUtils().getDefaultMessageTypes()).setCategories(ChatConfigurator.getUtils().getDefaultMessageCategories()).hideReplies(true));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        messagesViewModel.setId(id);
        messagesViewModel.addListener();
        View extensionView = ChatConfigurator.getUtils().getAuxiliaryHeaderMenu(context, user, group);
        if (extensionView != null) layout.addView(extensionView);
        layout.addView(detailIcon);
        messageHeader.setMenu((context, var1, var2) -> layout);
        setRequestBuilder();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        messagesViewModel.removeListener();
        if (AudioPlayer.getAudioPlayer().isPlaying()) AudioPlayer.getAudioPlayer().stopPlayer();
    }

    public CometChatMessageList getMessageList() {
        return messageList;
    }

    public CometChatMessageHeader getMessageHeader() {
        return messageHeader;
    }

    public CometChatMessageComposer getMessageComposer() {
        return messageComposer;
    }
}
