package com.cometchat.chatuikit.threadedmessages;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.messagecomposer.CometChatMessageComposer;
import com.cometchat.chatuikit.messagecomposer.MessageComposerConfiguration;
import com.cometchat.chatuikit.messagelist.CometChatMessageList;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.AudioPlayer;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.keyboard_utils.KeyBoardUtils;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.card.MaterialCardView;

/**
 * CometChatThreadedMessages is a custom view class that extends MaterialCardView.
 * It represents a threaded message view that displays messages and their replies in a threaded conversation format.
 * This view provides various customization options to configure its appearance and behavior.
 * <p>Usage:</p>
 * <pre>
 * // Adding custom view into xml file
 * {@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *   <com.cometchat.chatuikit.threadedmessages.CometChatThreadedMessages
 *         android:id="@+id/threaded_messages"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 *  </LinearLayout>
 *  }
 * // Creating an instance of CometChatThreadedMessages by assigning view id
 * {@code
 * CometChatThreadedMessages threadedMessages = findViewById(R.id.threaded_messages);
 * threadedMessages.setParentMessage(baseMessage);
 * }
 * </pre>
 * <p>Note: This class extends MaterialCardView, which provides a material-styled card view container for the threaded messages.</p>
 *
 * @see MaterialCardView
 * @see MessageListConfiguration
 * @see MessageComposerConfiguration
 * @see ThreadedMessagesStyle
 * @see CometChatMessageComposer
 * @see CometChatMessageList
 */
public class CometChatThreadedMessages extends MaterialCardView {

    private final String TAG = CometChatThreadedMessages.class.getName();
    private Context context;
    private LinearLayout messageBubble;
    private CometChatMessageList messageList;
    private CometChatMessageComposer messageComposer;
    private TextView replyCount;
    private TextView upperBorder;
    private TextView lowerBorder;
    private LinearLayout tailView;
    private LinearLayout actionView;
    private Function2<Context, BaseMessage, View> tail;
    private Function2<Context, BaseMessage, View> messageBubbleView;
    private Function2<Context, BaseMessage, View> messageActionView;
    private BaseMessage parentMessage;
    private MessageListConfiguration messageListConfiguration;
    private CometChatTheme cometChatTheme;
    private TextView tvTitle;
    private LinearLayout optionIconsList;
    private ImageView closeIcon;
    private OnBackPress onBackPress;
    private ThreadedMessagesViewModel threadedMessagesViewModel;
    private AppBarLayout appBarLayout;
    private int width, height;

    public CometChatThreadedMessages(Context context) {
        super(context);
        init(context);
    }

    public CometChatThreadedMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatThreadedMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        ((FragmentActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        cometChatTheme = CometChatTheme.getInstance(context);
        View view = View.inflate(context, R.layout.threaded_messages, null);
        messageBubble = view.findViewById(R.id.bubble_view);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        actionView = view.findViewById(R.id.action_view);
        messageComposer = view.findViewById(R.id.message_composer);
        messageList = view.findViewById(R.id.message_list);
        tailView = view.findViewById(R.id.tail_view);
        replyCount = view.findViewById(R.id.reply_count);
        upperBorder = view.findViewById(R.id.upper_border);
        lowerBorder = view.findViewById(R.id.lower_border);
        tvTitle = view.findViewById(R.id.tv_title);
        closeIcon = view.findViewById(R.id.back_icon);
        optionIconsList = view.findViewById(R.id.icons_layout);
        threadedMessagesViewModel = new ThreadedMessagesViewModel();
        setTitle(context.getResources().getString(R.string.thread_title));
        threadedMessagesViewModel = ViewModelProviders.of((FragmentActivity) context).get(threadedMessagesViewModel.getClass());
        threadedMessagesViewModel.getSentMessage().observe((AppCompatActivity) context, this::sentMessage);
        threadedMessagesViewModel.getReceiveMessage().observe((AppCompatActivity) context, this::receivedMessage);
        threadedMessagesViewModel.getReceiveMessage().observe((AppCompatActivity) context, this::updateParentMessageView);
        messageComposer.hideLiveReaction(true);
        hideCloseButton(false);
        closeIcon.setOnClickListener(v -> {
            if (onBackPress != null) onBackPress.onBack();
            else ((Activity) context).onBackPressed();
        });
        setCloseIcon(R.drawable.ic_close_24dp);
        setStyle(new ThreadedMessagesStyle().setBackground(cometChatTheme.getPalette().getBackground()).setBackground(cometChatTheme.getPalette().getGradientBackground()).setTitleColor(cometChatTheme.getPalette().getAccent()).setTitleAppearance(cometChatTheme.getTypography().getHeading()).setCloseIconTint(cometChatTheme.getPalette().getPrimary()).setSeparatorColor(cometChatTheme.getPalette().getAccent100()));
        KeyBoardUtils.setKeyboardVisibilityListener((Activity) context, view, keyboardVisible -> {
            if (keyboardVisible) {
                if (messageList.atBottom()) messageList.scrollToBottom();
                for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                    events.hidePanel(messageComposer.getComposerViewModel().getIdMap(), UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM);
                }
            }
        });
        addView(view);
    }

    private void sentMessage(BaseMessage baseMessage) {
        showReplyCount();
        appBarLayout.setExpanded(false);
        messageList.scrollToBottom();
    }

    private void receivedMessage(BaseMessage baseMessage) {
        showReplyCount();
        collapseAndScrollToTop();
        if (messageList.atBottom()) messageList.scrollToBottom();
    }

    private void collapseAndScrollToTop() {
        if (appBarLayout.getHeight() > (height / 2)) appBarLayout.setExpanded(false);
    }

    /**
     * Sets the background of the view with a gradient.
     *
     * @param colorArray  An array of colors to create the gradient.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    /**
     * Sets the tint color of the close icon.
     *
     * @param color The color to set for the close icon.
     */
    public void setCloseIconTint(@ColorInt int color) {
        if (color != 0 && closeIcon != null)
            closeIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the resource ID of the close icon.
     *
     * @param res The resource ID of the close icon.
     */
    public void setCloseIcon(@DrawableRes int res) {
        if (res != 0 && closeIcon != null) closeIcon.setImageResource(res);
    }

    /**
     * Sets the drawable of the close icon.
     *
     * @param res The drawable of the close icon.
     */
    public void setCloseIcon(Drawable res) {
        if (res != null && closeIcon != null) closeIcon.setImageDrawable(res);
    }

    /**
     * Hides or shows the close button.
     *
     * @param isVisible True to hide the close button, false to show it.
     */
    public void hideCloseButton(boolean isVisible) {
        if (isVisible) closeIcon.setVisibility(View.GONE);
        else closeIcon.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the menu view.
     *
     * @param view The menu view to set.
     */
    public void setMenu(View view) {
        if (view != null && optionIconsList != null) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.rightMargin = 16;
            view.setLayoutParams(param);
            optionIconsList.addView(view);
        }
    }

    /**
     * Sets the title of the threaded messages view.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        if (tvTitle != null && title != null) {
            if (!title.isEmpty()) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            } else tvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the color of the title text.
     *
     * @param color The color to set for the title text.
     */
    public void setTitleColor(@ColorInt int color) {
        if (tvTitle != null && color != 0) tvTitle.setTextColor(color);
    }

    /**
     * Sets the font for the title text.
     *
     * @param font The font to set for the title text.
     */
    public void setTitleFont(String font) {
        if (tvTitle != null && font != null)
            tvTitle.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance of the title.
     *
     * @param appearanceStyle The text appearance style to set for the title.
     */
    public void setTitleAppearance(int appearanceStyle) {
        if (tvTitle != null && appearanceStyle != 0)
            tvTitle.setTextAppearance(context, appearanceStyle);
    }

    /**
     * Sets the color of the reply count.
     *
     * @param color The color to set for the reply count.
     */
    public void setReplyCountColor(@ColorInt int color) {
        if (replyCount != null && color != 0) replyCount.setTextColor(color);
    }

    /**
     * Sets the font for the reply count.
     *
     * @param font The font to set for the reply count.
     */
    public void setReplyCountFont(String font) {
        if (replyCount != null && font != null)
            replyCount.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance of the reply count.
     *
     * @param appearanceStyle The text appearance style to set for the reply count.
     */
    public void setReplyCountAppearance(int appearanceStyle) {
        if (replyCount != null && appearanceStyle != 0)
            replyCount.setTextAppearance(context, appearanceStyle);
    }

    /**
     * Sets the tail view for the threaded messages.
     *
     * @param tail The function that returns the tail view.
     */
    public void setTailView(Function2<Context, BaseMessage, View> tail) {
        this.tail = tail;
    }

    /**
     * Sets the message bubble view for the threaded messages.
     *
     * @param messageBubbleView The function that returns the message bubble view.
     */
    public void setMessageBubbleView(Function2<Context, BaseMessage, View> messageBubbleView) {
        if (messageBubbleView != null)
            this.messageBubbleView = messageBubbleView;
    }

    /**
     * Sets the message action view for the threaded messages.
     *
     * @param messageActionView The function that returns the message action view.
     */
    public void setMessageActionView(Function2<Context, BaseMessage, View> messageActionView) {
        if (messageActionView != null)
            this.messageActionView = messageActionView;
    }

    /**
     * Sets the color of the action view separator.
     *
     * @param color The color to set for the action view separator.
     */
    public void setActionViewSeparatorColor(@ColorInt int color) {
        if (color != 0) {
            upperBorder.setBackgroundColor(color);
            lowerBorder.setBackgroundColor(color);
        }
    }

    /**
     * Adds an OnBackPressListener to handle back press events.
     *
     * @param onBackPress The OnBackPressListener to add.
     */
    public void addOnBackPressListener(OnBackPress onBackPress) {
        this.onBackPress = onBackPress;
    }

    /**
     * Sets the parent message of the threaded messages.
     *
     * @param parentMessage The parent message to set.
     */
    public void setParentMessage(BaseMessage parentMessage) {
        if (parentMessage != null) {
            this.parentMessage = parentMessage;
            if (UIKitConstants.ReceiverType.USER.equalsIgnoreCase(parentMessage.getReceiverType())) {
                User user = parentMessage.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()) ? (User) parentMessage.getReceiver() : parentMessage.getSender();
                messageList.setUser(user);
                messageComposer.setUser(user);
            } else if (UIKitConstants.ReceiverType.GROUP.equalsIgnoreCase(parentMessage.getReceiverType())) {
                Group group = (Group) parentMessage.getReceiver();
                messageList.setGroup(group);
                messageComposer.setGroup(group);
            }
            showReplyCount();
            threadedMessagesViewModel.setParentMessage(parentMessage);
            messageComposer.setParentMessageId(parentMessage.getId());
            messageList.setParentMessage(parentMessage.getId());
        }
    }

    private void showReplyCount() {
        if (parentMessage != null) {
            if (parentMessage.getReplyCount() < 2 && parentMessage.getReplyCount() > -1)
                replyCount.setText(parentMessage.getReplyCount() + " " + context.getResources().getString(R.string.reply_txt));
            else
                replyCount.setText(parentMessage.getReplyCount() + " " + context.getResources().getString(R.string.replies));
        }
    }

    private void showReplyCount(int reply) {
        if (reply < 2 && reply > -1)
            replyCount.setText(parentMessage.getReplyCount() + " " + context.getResources().getString(R.string.reply_txt));
        else
            replyCount.setText(parentMessage.getReplyCount() + " " + context.getResources().getString(R.string.replies));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setViews();
        setRequestBuilder();
        threadedMessagesViewModel.addListener();
    }

    private void setRequestBuilder() {
        if (parentMessage != null) {
            if (messageListConfiguration != null) {
                configuredBuilder(messageListConfiguration.getMessagesRequestBuilder() != null);
            } else configuredBuilder(false);
        } else configuredBuilder(false);
    }

    private void configuredBuilder(boolean isConfigured) {
        if (isConfigured) {
            messageList.setMessagesRequestBuilder(messageListConfiguration.getMessagesRequestBuilder().setParentMessageId(parentMessage.getId()));
        } else {
            messageList.setMessagesRequestBuilder(new MessagesRequest.MessagesRequestBuilder().setParentMessageId(parentMessage.getId()).setTypes(ChatConfigurator.getUtils().getDefaultMessageTypes()).setCategories(ChatConfigurator.getUtils().getDefaultMessageCategories()).hideReplies(true));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        threadedMessagesViewModel.removeListener();
        if (AudioPlayer.getAudioPlayer().isPlaying()) AudioPlayer.getAudioPlayer().stopPlayer();
    }

    private void setViews() {
        if (messageBubbleView != null)
            Utils.handleView(messageBubble, messageBubbleView.apply(context, parentMessage), false);
        if (messageActionView != null)
            Utils.handleView(actionView, messageActionView.apply(context, parentMessage), false);
        if (tail != null) Utils.handleView(tailView, tail.apply(context, parentMessage), false);
    }

    private void updateParentMessageView(BaseMessage baseMessage) {
        if (messageBubbleView != null) {
            Utils.handleView(messageBubble, messageBubbleView.apply(context, baseMessage), false);
        }
    }

    /**
     * Sets the configuration for the message list.
     *
     * @param configuration The MessageListConfiguration to set.
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
     * Sets the configuration for the message composer.
     *
     * @param configuration The MessageComposerConfiguration to set.
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
            messageComposer.setLiveReactionIcon(configuration.getLiveReactionIcon());
            messageComposer.setStyle(configuration.getStyle());
            messageComposer.setOnError(configuration.getOnError());
            messageComposer.setOnSendButtonClick(configuration.getSendButtonClick());
        }
    }

    /**
     * Sets the style for the threaded messages.
     *
     * @param style The ThreadedMessagesStyle to set.
     */
    public void setStyle(ThreadedMessagesStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
            setTitleFont(style.getTitleFont());
            setTitleAppearance(style.getTitleAppearance());
            setTitleColor(style.getTitleColor());
            setReplyCountColor(style.getReplyCountColor());
            setReplyCountFont(style.getReplyCountFont());
            setReplyCountAppearance(style.getReplyCountAppearance());
            setActionViewSeparatorColor(style.getSeparatorColor());
            setCloseIconTint(style.getCloseIconTint());
        }
    }

    public interface OnBackPress {
        void onBack();
    }

}
