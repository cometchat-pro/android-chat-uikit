package com.cometchat.chatuikit.messagelist;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKitHelper;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.resources.soundManager.CometChatSoundManager;
import com.cometchat.chatuikit.shared.resources.soundManager.Sound;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.CometChatError;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderDecoration;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.CometChatMessageBubble;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.MessageBubbleStyle;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionItem;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionSheet;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionSheetStyle;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.CometChatActionSheet;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.CometChatActionSheetListener;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * CometChatMessageList is a custom view that extends MaterialCardView to provide a visually appealing card-like representation of a list of messages.
 * <br>
 * Features:
 * <br>
 * <b>Efficient Message Display:</b> Utilizes RecyclerView for smooth scrolling and efficient rendering of messages.<br>
 * <b>Message Long Click Handling:</b> Implements the OnMessageLongClick interface to handle long click events on messages.<br>
 * <b>Customizable Styling:</b> Allows easy customization of the view's appearance through {@link  MessageListStyle}. <br>
 * <b>User Interaction:</b> Provides listeners for handling user interactions such as click events, link clicks, and reply message interactions.<br>
 * <b>Action Sheet Integration:</b> Supports an action sheet mode for displaying additional actions related to messages.<br>
 * Example:<br>
 * <pre>
 * {@code
 *  CometChatMessageList messageList = view.findViewById(R.id.message_list);
 *  if(user!=null) messageList.setUser(user);
 *  else if(group!=null) messageList.setGroup(group);
 *  }
 * </pre>
 */
public class CometChatMessageList extends MaterialCardView implements MessageAdapter.OnMessageLongClick {
    private MessageListViewModel messageListViewModel;
    private Context context;
    private View view;
    private User user;
    private Group group;
    private String errorStateText;
    private boolean disableSoundForMessages;
    private @RawRes
    int customSoundForMessages;
    private List<CometChatMessageTemplate> messageTemplates = new ArrayList<>();
    private String newMessageIndicatorText;
    private boolean scrollToBottomOnNewMessage;
    private LinearLayout headerView, footerView;
    private ThreadReplyClick onThreadRepliesClick;
    private CometChatActionSheet cometChatActionSheet;
    private ActionSheetStyle actionSheetStyle;
    private View newMessageLayout;
    private TextView newMessageLayoutText;
    private int newMessageCount = 0;
    private @ActionSheet.LayoutMode
    String actionSheetMode = ActionSheet.LayoutMode.listMode;
    private CometChatSoundManager soundManager;
    private Palette palette;
    private Typography typography;
    private int errorStateTextAppearance = 0;
    private int errorStateTextColor = 0;
    private LinearLayout loadingLayout;
    private ImageView loadingIcon;
    private LinearLayout emptyLayout, customViewLayout;
    private TextView emptyTextView;
    private RelativeLayout messageListLayout;
    private StickyHeaderDecoration stickyHeaderDecoration;
    private boolean hasMore;
    private RecyclerView rvChatListView;
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private View emptyView;
    private View errorView;
    private View loadingView;
    private boolean hideError;
    private boolean isInProgress;
    private BaseMessage baseMessage;
    private HashMap<String, CometChatMessageTemplate> messageTemplateHashMap = new HashMap<>();
    private final HashMap<String, String> messageTypesToRetrieve = new HashMap<>();
    private final HashMap<String, String> messageCategoriesToRetrieve = new HashMap<>();
    private List<CometChatMessageOption> customOption = new ArrayList<>();
    private CometChatMessageBubble messageBubble;
    private RelativeLayout parent;
    private ImageView newMessageIndicatorIcon;
    private OnError onError;
    private View internalBottomPanel, internalTopPanel;

    /**
     * Constructs a new CometChatMessageList with the given context.
     *
     * @param context The context in which the view is created.
     */
    public CometChatMessageList(Context context) {
        super(context);
        init(context, null, -1);
    }

    /**
     * Constructs a new CometChatMessageList with the given context and attribute set.
     *
     * @param context The context in which the view is created.
     * @param attrs   The attribute set for the view.
     */
    public CometChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatMessageList with the given context, attribute set, and default style attribute.
     *
     * @param context      The context in which the view is created.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute for the view.
     */
    public CometChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the CometChatMessageList view with the given context, attribute set, and default style attribute.
     *
     * @param context      The context in which the view is created.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute for the view.
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        view = View.inflate(context, R.layout.cometchat_messagelist, null);
        messageListViewModel = new MessageListViewModel();
        messageListViewModel = ViewModelProviders.of((FragmentActivity) context).get(messageListViewModel.getClass());
        soundManager = new CometChatSoundManager(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MessageList, 0, 0);
        actionSheetMode = ActionSheet.getValue(a.getInt(R.styleable.MessageList_actionSheetMode, 0));
        messageAdapter = new MessageAdapter(context, messageTemplateHashMap, this);
        view = LayoutInflater.from(context).inflate(R.layout.cometchat_messagelist, (ViewGroup) getParent(), false);
        initViewComponent(context, view);
        addView(view);
    }

    private void initViewComponent(Context context, View view) {
        CometChatError.init(context);
        messageTemplates = ChatConfigurator.getUtils().getMessageTemplates();
        fetchMessageFilter();
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        Drawable drawable = getContext().getDrawable(R.drawable.cc_action_item_top_curve);
        drawable.setTint(palette.getAccent900());
        actionSheetStyle = new ActionSheetStyle().setBackground(drawable).setTitleColor(palette.getAccent()).setTitleAppearance(typography.getName()).setLayoutModeIconTint(palette.getPrimary()).setListItemSeparatorColor(android.R.color.transparent);
        cometChatActionSheet = new CometChatActionSheet(context);
        cometChatActionSheet.hideTitle(true);
        cometChatActionSheet.hideLayoutMode(true);
        cometChatActionSheet.setStyle(actionSheetStyle);
        errorStateTextAppearance = typography.getText1();
        errorStateTextColor = palette.getAccent700();
        newMessageLayout = view.findViewById(R.id.new_message_layout);
        newMessageLayoutText = view.findViewById(R.id.new_message_tv);
        newMessageIndicatorIcon = view.findViewById(R.id.image_view);
        newMessageLayout.setVisibility(GONE);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        parent = view.findViewById(R.id.parent);
        setLoadingIconTintColor(palette.getAccent());
        rvChatListView = view.findViewById(R.id.rv_message_list);
        messageListLayout = view.findViewById(R.id.message_list_layout);
        emptyLayout = view.findViewById(R.id.empty_view);
        customViewLayout = view.findViewById(R.id.customView_lay);
        emptyTextView = view.findViewById(R.id.empty_text);
        headerView = view.findViewById(R.id.header_view);
        footerView = view.findViewById(R.id.footer_view);
        emptyTextView.setTextColor(palette.getAccent400());
        emptyTextView.setTextAppearance(context, typography.getHeading());
        messageListLayout = view.findViewById(R.id.message_list_layout);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvChatListView.setLayoutManager(linearLayoutManager);
        cometChatActionSheet.setTitle(null);
        messageListViewModel.getMutableMessageList().observe((AppCompatActivity) context, this::setList);
        messageListViewModel.messagesRangeChanged().observe((AppCompatActivity) context, this::notifyRangeChanged);
        messageListViewModel.updateMessage().observe((AppCompatActivity) context, this::updateMessage);
        messageListViewModel.addMessage().observe((AppCompatActivity) context, this::addMessage);
        messageListViewModel.getCometChatException().observe((AppCompatActivity) context, this::throwError);
        messageListViewModel.getReadMessage().observe((AppCompatActivity) context, this::markedAsRead);
        messageListViewModel.removeMessage().observe((AppCompatActivity) context, this::removeMessage);
        messageListViewModel.getMutableIsInProgress().observe((AppCompatActivity) context, this::isInProgress);
        messageListViewModel.getMutableHasMore().observe((AppCompatActivity) context, this::hasMore);
        messageListViewModel.notifyUpdate().observe((AppCompatActivity) context, this::notifyDataChanged);
        messageListViewModel.getStates().observe((AppCompatActivity) context, messageStates);
        messageListViewModel.getMessageDeleteState().observe((AppCompatActivity) context, messageDeleteObserver);
        messageListViewModel.closeTopPanel().observe((AppCompatActivity) context, this::closeInternalTopPanel);
        messageListViewModel.closeBottomPanel().observe((AppCompatActivity) context, this::closeInternalBottomPanel);
        messageListViewModel.showTopPanel().observe((AppCompatActivity) context, this::showInternalTopPanel);
        messageListViewModel.showBottomPanel().observe((AppCompatActivity) context, this::showInternalBottomPanel);
        rvChatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (hasMore && !isInProgress) {
                    if (linearLayoutManager.findLastVisibleItemPosition() == (messageAdapter.getItemCount() - 1) || !rvChatListView.canScrollVertically(1)) {
                        messageListViewModel.markLastMessageAsRead(messageListViewModel.getLastMessage());
                    }
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 10 || !rvChatListView.canScrollVertically(-1)) {
                        isInProgress = true;
                        messageListViewModel.fetchMessages();
                    }
                }
                if (messageAdapter != null && ((messageAdapter.getItemCount() - 1) - linearLayoutManager.findLastVisibleItemPosition() < 5)) {
                    newMessageLayout.setVisibility(GONE);
                    newMessageCount = 0;
                }
            }
        });

        cometChatActionSheet.setEventListener(new CometChatActionSheetListener() {
            @Override
            public void onActionItemClick(ActionItem actionItem) {
                for (CometChatMessageOption option : customOption) {
                    if (option != null && option.getId() != null && option.getId().equals(actionItem.getId())) {
                        if (option.getClick() != null) {
                            option.getClick().onClick();
                            cometChatActionSheet.dismiss();
                            break;
                        } else {
                            switch (actionItem.getId()) {
                                case UIKitConstants.MessageOption.DELETE:
                                    messageListViewModel.deleteMessage(baseMessage);
                                    cometChatActionSheet.dismiss();
                                    break;
                                case UIKitConstants.MessageOption.REPLY_IN_THREAD:
                                    if (onThreadRepliesClick != null) {
                                        onThreadRepliesClick.onThreadReplyClick(context, baseMessage, messageBubble);
                                    }
                                    cometChatActionSheet.dismiss();
                                    break;
                                case UIKitConstants.MessageOption.COPY:
                                    copyMessage(baseMessage);
                                    cometChatActionSheet.dismiss();
                                    break;
                                case UIKitConstants.MessageOption.EDIT:
                                    messageListViewModel.onMessageEdit(baseMessage);
                                    cometChatActionSheet.dismiss();
                                    break;
                                default:
                                    cometChatActionSheet.dismiss();
                                    return;
                            }
                        }
                    }
                }
            }
        });

    }

    private void showInternalBottomPanel(Function1<Context, View> view) {
        if (view != null) {
            Utils.removeParentFromView(internalBottomPanel);
            if (internalBottomPanel != null) footerView.removeView(internalBottomPanel);
            this.internalBottomPanel = view.apply(context);
            footerView.addView(internalBottomPanel);
        }
    }

    private void showInternalTopPanel(Function1<Context, View> view) {
        if (view != null) {
            Utils.removeParentFromView(internalTopPanel);
            if (internalTopPanel != null) headerView.removeView(internalTopPanel);
            this.internalTopPanel = view.apply(context);
            headerView.addView(internalTopPanel);
        }
    }

    private void closeInternalTopPanel(Void avoid) {
        headerView.removeView(internalTopPanel);
        internalTopPanel = null;
    }

    private void closeInternalBottomPanel(Void avoid) {
        footerView.removeView(internalBottomPanel);
        internalBottomPanel = null;
    }

    private void markedAsRead(BaseMessage baseMessage) {
        CometChatUIKitHelper.onMessageRead(baseMessage);
    }

    private void copyMessage(BaseMessage baseMessage) {
        String message = ((TextMessage) baseMessage).getText();
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("MessageAdapter", message);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
    }

    Observer<MessageDeleteState> messageDeleteObserver = new Observer<MessageDeleteState>() {
        @Override
        public void onChanged(MessageDeleteState progressState) {
            if (MessageDeleteState.SUCCESS_DELETE.equals(progressState)) {
                baseMessage = null;
            } else if (MessageDeleteState.FAILURE_DELETE.equals(progressState)) {
                showError(true);
            }
        }
    };

    /**
     * Sets the avatar style for the message list.
     *
     * @param avatarStyle The AvatarStyle object defining the style of avatars.
     */
    public void setAvatarStyle(AvatarStyle avatarStyle) {
        messageAdapter.setAvatarStyle(avatarStyle);
    }

    /**
     * Sets the action sheet style for the message list.
     *
     * @param style The ActionSheetStyle object defining the style of the action sheet.
     */
    public void setActionSheetStyle(ActionSheetStyle style) {
        if (style != null) cometChatActionSheet.setStyle(style);
    }

    /**
     * Sets the date separator style for the message list.
     *
     * @param dateStyle The DateStyle object defining the style of date separators.
     */
    public void setDateSeparatorStyle(DateStyle dateStyle) {
        messageAdapter.setDateSeparatorStyle(dateStyle);
    }

    /**
     * Sets the message bubble style for the wrapper message in the message list.
     *
     * @param style The MessageBubbleStyle object defining the style of the message bubble.
     */
    public void setWrapperMessageBubbleStyle(MessageBubbleStyle style) {
        messageAdapter.setWrapperMessageBubbleStyle(style);
    }

    /**
     * Sets the text color for the name of the sender in the message list.
     *
     * @param nameTextColor The color value for the name text.
     */
    public void setNameTextColor(int nameTextColor) {
        messageAdapter.setNameTextColor(nameTextColor);
    }

    /**
     * Sets the text color for the timestamp of the message in the message list.
     *
     * @param timeStampTextColor The color value for the timestamp text.
     */
    public void setTimeStampTextColor(int timeStampTextColor) {
        messageAdapter.setTimeStampTextColor(timeStampTextColor);
    }

    /**
     * Sets the color for the separator line of thread reply messages in the message list.
     *
     * @param threadReplySeparatorColor The color value for the thread reply separator line.
     */
    public void setThreadReplySeparatorColor(int threadReplySeparatorColor) {
        messageAdapter.setThreadReplySeparatorColor(threadReplySeparatorColor);
    }

    /**
     * Sets the text color for thread reply messages in the message list.
     *
     * @param threadReplyTextColor The color value for the thread reply text.
     */
    public void setThreadReplyTextColor(int threadReplyTextColor) {
        messageAdapter.setThreadReplyTextColor(threadReplyTextColor);
    }

    /**
     * Sets the tint color for the icons of thread reply messages in the message list.
     *
     * @param threadReplyIconTint The color value for the thread reply icon tint.
     */
    public void setThreadReplyIconTint(int threadReplyIconTint) {
        messageAdapter.setThreadReplyIconTint(threadReplyIconTint);
    }

    /**
     * Sets the text appearance for the name of the sender in the message list.
     *
     * @param nameTextAppearance The text appearance style resource for the name text.
     */
    public void setNameTextAppearance(int nameTextAppearance) {
        messageAdapter.setNameTextAppearance(nameTextAppearance);
    }

    /**
     * Sets the text appearance for the timestamp of the message in the message list.
     *
     * @param timeStampTextAppearance The text appearance style resource for the timestamp text.
     */
    public void setTimeStampTextAppearance(int timeStampTextAppearance) {
        messageAdapter.setTimeStampTextAppearance(timeStampTextAppearance);
    }

    /**
     * Sets the text appearance for the thread reply messages in the message list.
     *
     * @param threadReplyTextAppearance The text appearance style resource for the thread reply text.
     */
    public void setThreadReplyTextAppearance(int threadReplyTextAppearance) {
        messageAdapter.setThreadReplyTextAppearance(threadReplyTextAppearance);
    }

    /**
     * Hides or shows the deleted messages in the message list.
     *
     * @param hide Pass true to hide deleted messages, false to show them.
     */
    public void hideDeletedMessages(boolean hide) {
        messageListViewModel.hideDeleteMessages(hide);
    }

    private void notifyDataChanged(Void unused) {
        messageAdapter.notifyDataSetChanged();
    }

    private void notifyRangeChanged(int finalRange) {
        messageAdapter.notifyItemRangeInserted(0, finalRange);
    }

    private void removeMessage(Integer integer) {
        messageAdapter.notifyItemRemoved(integer);
    }

    private void hasMore(Boolean aBoolean) {
        hasMore = aBoolean;
    }

    private void isInProgress(Boolean aBoolean) {
        isInProgress = aBoolean;
    }

    private void showNewMessage(int messageCount) {
        newMessageLayout.setVisibility(VISIBLE);
        newMessageLayoutText.setVisibility(VISIBLE);
        if (messageCount == 1)
            newMessageLayoutText.setText(messageCount + " " + (newMessageIndicatorText != null ? newMessageIndicatorText : context.getString(R.string.new_message)));
        else
            newMessageLayoutText.setText(messageCount + " " + (newMessageIndicatorText != null ? newMessageIndicatorText : context.getString(R.string.new_messages)));
        newMessageLayout.setOnClickListener(v -> {
            newMessageCount = 0;
            scrollToBottom();
            newMessageLayout.setVisibility(GONE);
        });
    }

    /**
     * Adds a new message to the message list.
     *
     * @param baseMessage The BaseMessage object representing the new message.
     */
    public void addMessage(BaseMessage baseMessage) {
        ((Activity) context).runOnUiThread(() -> {
            if (!disableSoundForMessages)
                soundManager.play(Sound.incomingMessage, customSoundForMessages);

            if (!scrollToBottomOnNewMessage) {
                if ((messageAdapter.getItemCount() - 1) - ((LinearLayoutManager) rvChatListView.getLayoutManager()).findLastVisibleItemPosition() < 5)
                    scrollToBottom();
                else {
                    if (baseMessage != null && baseMessage.getSender() != null && CometChatUIKit.getLoggedInUser() != null && !CometChatUIKit.getLoggedInUser().getUid().equalsIgnoreCase(baseMessage.getSender().getUid()))
                        showNewMessage(++newMessageCount);
                }
            } else scrollToBottom();
        });
    }

    /**
     * Sets a custom header view for the message list.
     *
     * @param headerView The View object representing the custom header view.
     */
    public void setHeaderView(View headerView) {
        Utils.handleView(this.headerView, headerView, false);
    }

    /**
     * Sets a custom footer view for the message list.
     *
     * @param footerView The View object representing the custom footer view.
     */
    public void setFooterView(View footerView) {
        Utils.handleView(this.footerView, footerView, false);
    }

    /**
     * Sets the text to be displayed in the empty state of the message list.
     *
     * @param message The text to be displayed in the empty state.
     */
    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyTextView.setText(message);
        else emptyTextView.setText(getResources().getString(R.string.cc_no_message));
    }

    /**
     * Sets the text color for the empty state text of the message list.
     *
     * @param color The color value for the empty state text.
     */
    public void emptyStateTextColor(int color) {
        if (color != 0) emptyTextView.setTextColor(color);
    }

    /**
     * Sets the font for the empty state text of the message list.
     *
     * @param font The font name or path for the empty state text.
     */
    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyTextView.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance for the empty state text of the message list.
     *
     * @param appearance The style resource for the empty state text appearance.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyTextView.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text appearance for the error state text of the message list.
     *
     * @param appearance The style resource for the error state text appearance.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the text color for the error state text of the message list.
     *
     * @param errorMessageColor The color value for the error state text.
     */
    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorStateTextColor = errorMessageColor;
    }

    /**
     * Sets the text for the error state of the message list.
     *
     * @param errorText The text to be displayed in the error state.
     */
    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorStateText = errorText;
    }

    /**
     * Sets the layout resource for the empty state view.
     *
     * @param id The resource ID of the layout for the empty state view.
     */
    public void setEmptyStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                emptyView = View.inflate(context, id, null);
            } catch (Exception e) {
                emptyView = null;
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the layout resource for the error state view.
     *
     * @param id The resource ID of the layout for the error state view.
     */
    public void setErrorStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                errorView = View.inflate(context, id, null);
            } catch (Exception e) {
                errorView = null;
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the layout resource for the loading state view.
     *
     * @param id The resource ID of the layout for the loading state view.
     */
    public void setLoadingStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                loadingView = View.inflate(context, id, null);
            } catch (Exception e) {
                loadingView = null;
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the text for the new message indicator.
     *
     * @param text The text to be displayed as the new message indicator.
     */
    public void setNewMessageIndicatorText(String text) {
        if (text != null && !text.isEmpty()) {
            this.newMessageIndicatorText = text;
            newMessageLayoutText.setText(text);
        }
    }

    /**
     * Sets the text color for the new message indicator.
     *
     * @param color The color to be applied to the new message indicator text.
     */
    public void setNewMessageIndicatorTextColor(@ColorInt int color) {
        if (color != 0) newMessageLayoutText.setTextColor(color);
    }

    /**
     * Sets the font for the new message indicator text.
     *
     * @param font The name of the font to be applied to the new message indicator text.
     */
    public void setNewMessageIndicatorTextFont(String font) {
        if (font != null && !font.isEmpty())
            newMessageLayoutText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance for the new message indicator text.
     *
     * @param appearance The resource ID of the text appearance to be applied to the new message indicator text.
     */
    public void setNewMessageIndicatorTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) newMessageLayoutText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the icon color for the new message indicator.
     *
     * @param color The color to be applied to the new message indicator icon.
     */
    public void setNewMessageIndicatorIconColor(@ColorInt int color) {
        if (color != 0) newMessageIndicatorIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets whether to scroll to the bottom of the message list automatically on new messages.
     *
     * @param value {@code true} to enable auto-scrolling to the bottom on new messages, {@code false} otherwise.
     */
    public void scrollToBottomOnNewMessage(boolean value) {
        scrollToBottomOnNewMessage = value;
    }

    /**
     * Sets the tint color for the loading icon.
     *
     * @param color The color value to be applied to the loading icon.
     */
    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Checks if the message list is currently scrolled to the bottom.
     *
     * @return {@code true} if the message list is at the bottom, {@code false} otherwise.
     */
    public boolean atBottom() {
        if (messageAdapter != null && ((messageAdapter.getItemCount() - 1) - linearLayoutManager.findLastVisibleItemPosition() < 5))
            return true;
        else return false;
    }

    /**
     * Sets the user for the chat.
     *
     * @param user The User object representing the chat user.
     */
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            messageAdapter.setType(UIKitConstants.ReceiverType.USER);
            messageAdapter.setUser(user);
        }
    }

    /**
     * Sets the group for the message list. The group represents the conversation group to which the messages belong.
     *
     * @param group The group to set.
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            messageAdapter.setType(UIKitConstants.ReceiverType.GROUP);
            messageAdapter.setGroup(group);
        }
    }

    /**
     * Sets the parent message ID for the message list. This is used to display threaded replies.
     *
     * @param parentMessage The ID of the parent message.
     */
    public void setParentMessage(int parentMessage) {
        messageListViewModel.setParentMessageId(parentMessage);
    }

    /**
     * Sets the style for the message list.
     *
     * @param style The style to apply to the message list.
     */
    public void setStyle(MessageListStyle style) {
        if (style != null) {
            setLoadingIconTintColor(style.getLoadingIconTint());
            emptyStateTextAppearance(style.getEmptyTextAppearance());
            errorStateTextAppearance(style.getErrorTextAppearance());
            emptyStateTextFont(style.getEmptyTextFont());
            emptyStateTextColor(style.getEmptyTextColor());
            errorStateTextColor(style.getErrorTextColor());

            setNameTextAppearance(style.getNameTextAppearance());
            setNameTextColor(style.getNameTextColor());
            setTimeStampTextAppearance(style.getTimeStampTextAppearance());
            setTimeStampTextColor(style.getTimeStampTextColor());
            setThreadReplyIconTint(style.getThreadReplyIconTint());
            setThreadReplyTextAppearance(style.getThreadReplyTextAppearance());
            setThreadReplySeparatorColor(style.getThreadReplySeparatorColor());
            setThreadReplyTextColor(style.getThreadReplyTextColor());

            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the MessagesRequestBuilder for fetching messages in the message list.
     *
     * @param builder The MessagesRequestBuilder to set.
     */
    public void setMessagesRequestBuilder(MessagesRequest.MessagesRequestBuilder builder) {
        messageListViewModel.setMessagesRequestBuilder(builder);
    }

    /**
     * Sets whether to disable sound for messages in the message list.
     *
     * @param disableSoundForMessages true to disable sound, false to enable sound.
     */
    public void disableSoundForMessages(boolean disableSoundForMessages) {
        this.disableSoundForMessages = disableSoundForMessages;
    }

    /**
     * Sets a custom sound resource for incoming messages in the message list.
     *
     * @param sound The resource ID of the custom sound. Pass 0 to use the default sound.
     */
    public void setCustomSoundForMessages(@RawRes int sound) {
        if (sound != 0) this.customSoundForMessages = sound;
    }

    /**
     * Disables read receipt for messages in the message list.
     *
     * @param disableReceipt True to disable read receipt, false to enable read receipt.
     */
    public void disableReceipt(boolean disableReceipt) {
        messageAdapter.disableReadReceipt(disableReceipt);
        messageListViewModel.disableReceipt(disableReceipt);
    }

    /**
     * Sets the icon resource for the read receipt in the message list.
     *
     * @param readIcon The resource ID of the read receipt icon.
     */
    public void setReadIcon(@DrawableRes int readIcon) {
        messageAdapter.setReadIcon(readIcon);
    }

    /**
     * Sets the icon resource for the deliver receipt in the message list.
     *
     * @param deliverIcon The resource ID of the deliver receipt icon.
     */
    public void setDeliverIcon(int deliverIcon) {
        messageAdapter.setDeliverIcon(deliverIcon);
    }

    /**
     * Sets the icon resource for the sent receipt in the message list.
     *
     * @param sentIcon The resource ID of the sent receipt icon.
     */
    public void setSentIcon(int sentIcon) {
        messageAdapter.setDeliverIcon(sentIcon);
    }

    /**
     * Sets the icon resource for the waiting receipt in the message list.
     *
     * @param waitIconIcon The resource ID of the waiting receipt icon.
     */
    public void setWaitIconIcon(int waitIconIcon) {
        messageAdapter.setWaitIconIcon(waitIconIcon);
    }

    /**
     * Sets the alignment of messages in the message list.
     *
     * @param alignment The MessageListAlignment enum representing the alignment.
     */
    public void setAlignment(UIKitConstants.MessageListAlignment alignment) {
        messageAdapter.setAlignment(alignment);
    }

    /**
     * Shows or hides the avatar in the message list.
     *
     * @param showAvatar True to show the avatar, false to hide the avatar.
     */
    public void showAvatar(boolean showAvatar) {
        messageAdapter.showAvatar(showAvatar);
    }

    /**
     * Sets the date pattern for displaying message dates in the message list.
     *
     * @param datePattern The Function1 object representing the date pattern.
     */
    public void setDatePattern(Function1<BaseMessage, String> datePattern) {
        messageAdapter.setDatePattern(datePattern);
    }

    /**
     * Sets the date separator pattern for displaying date separators in the message list.
     *
     * @param dateSeparatorPattern The Function1 object representing the date separator pattern.
     */
    public void setDateSeparatorPattern(Function1<BaseMessage, String> dateSeparatorPattern) {
        messageAdapter.setDateSeparatorPattern(dateSeparatorPattern);
    }

    /**
     * Sets the alignment of the timestamp in messages in the message list.
     *
     * @param timeStampAlignment The TimeStampAlignment enum representing the timestamp alignment.
     */
    public void setTimeStampAlignment(UIKitConstants.TimeStampAlignment timeStampAlignment) {
        messageAdapter.setTimeStampAlignment(timeStampAlignment);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (user != null) {
            messageListViewModel.setUser(user, ChatConfigurator.getUtils().getDefaultMessageTypes(), ChatConfigurator.getUtils().getDefaultMessageCategories());
//            messageListViewModel.setUser(user, new ArrayList<>(messageTypesToRetrieve.values()), new ArrayList<>(messageCategoriesToRetrieve.values()));
        } else if (group != null) {
            messageListViewModel.setGroup(group, new ArrayList<>(messageTypesToRetrieve.values()), new ArrayList<>(messageCategoriesToRetrieve.values()));
//            messageListViewModel.setGroup(group, ChatConfigurator.getUtils().getDefaultMessageTypes(), ChatConfigurator.getUtils().getDefaultMessageCategories());
        }
        messageListViewModel.addListener();
        messageListViewModel.fetchMessages();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        messageListViewModel.removeListener();
    }

    /**
     * Sets the message templates for the message list.
     *
     * @param cometChatMessageTemplates The list of CometChatMessageTemplate objects representing the message templates. Fetches the message filter based on the message templates.
     *                                  This method is called internally to update the message filter.
     *                                  You do not need to call this method directly.
     */
    public void setTemplates(List<CometChatMessageTemplate> cometChatMessageTemplates) {
        if (cometChatMessageTemplates != null) {
            if (!cometChatMessageTemplates.isEmpty()) {
                messageTemplates = cometChatMessageTemplates;
            } else {
                messageTemplates = new ArrayList<>();
            }
            fetchMessageFilter();
        }
    }

    private void fetchMessageFilter() {
        messageTypesToRetrieve.clear();
        messageCategoriesToRetrieve.clear();
        messageTemplateHashMap.clear();
        for (CometChatMessageTemplate template : messageTemplates) {
            if (template != null && template.getCategory() != null && template.getType() != null) {
                messageTypesToRetrieve.put(template.getCategory() + "_" + template.getType(), template.getType());
                messageCategoriesToRetrieve.put(template.getCategory(), template.getCategory());
                messageTemplateHashMap.put(template.getCategory() + "_" + template.getType(), template);
            }
        }
        messageAdapter.setMessageTemplateHashMap(messageTemplateHashMap);
//        messageListViewModel.setMessagesTypesAndCategories(new ArrayList<>(messageTypesToRetrieve.values()), new ArrayList<>(messageCategoriesToRetrieve.values()));
        messageListViewModel.setMessagesTypesAndCategories(ChatConfigurator.getUtils().getDefaultMessageTypes(), ChatConfigurator.getUtils().getDefaultMessageCategories());
        messageListViewModel.fetchMessages();
    }

    Observer<UIKitConstants.States> messageStates = states -> {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customViewLayout.setVisibility(VISIBLE);
                customViewLayout.removeAllViews();
                customViewLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            emptyLayout.setVisibility(View.GONE);
            customViewLayout.setVisibility(GONE);
            messageListLayout.setVisibility(View.VISIBLE);
        } else if (UIKitConstants.States.ERROR.equals(states)) showError(false);
        else if (UIKitConstants.States.EMPTY.equals(states)) {
            if (emptyView != null) {
                customViewLayout.setVisibility(VISIBLE);
                customViewLayout.removeAllViews();
                customViewLayout.addView(emptyView);
            } else emptyLayout.setVisibility(View.VISIBLE);
            messageListLayout.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            emptyLayout.setVisibility(View.GONE);
            messageListLayout.setVisibility(View.VISIBLE);
            customViewLayout.setVisibility(GONE);
        }
    };

    private void showError(boolean forDelete) {
        String errorMessage;
        if (errorStateText != null) errorMessage = errorStateText;
        else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!hideError && errorView != null) {
            customViewLayout.removeAllViews();
            customViewLayout.addView(errorView);
            customViewLayout.setVisibility(VISIBLE);
        } else {
            customViewLayout.setVisibility(GONE);
            if (!hideError) {
                if (getContext() != null) {
                    new CometChatDialog(context, 0, errorStateTextAppearance, typography.getText3(), palette.getAccent900(), 0, errorStateTextColor, errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", palette.getPrimary(), palette.getPrimary(), 0, (alertDialog, which, popupId) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            alertDialog.dismiss();
                            if (forDelete) CometChatUIKitHelper.onMessageDeleted(baseMessage);
                            else messageListViewModel.fetchMessages();
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }, 0, false);
                }
            }
        }
    }

    /**
     * Sets the listener for handling thread reply clicks in the message list.
     *
     * @param onThreadRepliesClick The ThreadReplyClick object representing the listener.
     */
    public void setOnThreadRepliesClick(ThreadReplyClick onThreadRepliesClick) {
        if (onThreadRepliesClick != null) {
            this.onThreadRepliesClick = onThreadRepliesClick;
            messageAdapter.setThreadReplyClick(onThreadRepliesClick);
        }
    }

    private void setList(List<BaseMessage> messageList) {
        messageAdapter.setBaseMessageList(messageList);
        messageAdapter.setMessageTemplateHashMap(messageTemplateHashMap);
        rvChatListView.setAdapter(messageAdapter);
        stickyHeaderDecoration = new StickyHeaderDecoration(messageAdapter);
        rvChatListView.addItemDecoration(stickyHeaderDecoration, 0);
        messageAdapter.setDatePattern(baseMessage -> new SimpleDateFormat("h:mm a").format(new java.util.Date(baseMessage.getSentAt() * 1000)) + "");
        scrollToBottom();
    }

    private void updateMessage(int index) {
        messageAdapter.notifyItemChanged(index);
    }

    /**
     * Scrolls the message list to the bottom.
     * This method is useful to automatically scroll to the latest message in the list.
     * It also marks the last message as read.
     */
    public void scrollToBottom() {
        if (messageAdapter != null && messageAdapter.getItemCount() > 0) {
            rvChatListView.scrollToPosition(messageAdapter.getItemCount() - 1);
            messageListViewModel.markLastMessageAsRead(messageListViewModel.getLastMessage());
        }
    }

    public RecyclerView getRecyclerView() {
        return this.rvChatListView;
    }

    public MessageAdapter getMessageAdapter() {
        return messageAdapter;
    }

    public void setMessageAdapter(RecyclerView.Adapter adapter) {
        rvChatListView.setAdapter(adapter);
    }

    public MessageListViewModel getViewModel() {
        return messageListViewModel;
    }

    public RelativeLayout getView() {
        return parent;
    }

    public View getNewMessageLayout() {
        return newMessageLayout;
    }

    public TextView getNewMessageLayoutTextView() {
        return newMessageLayoutText;
    }

    public ImageView getNewMessageImageView() {
        return newMessageIndicatorIcon;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * Sets whether to hide or show the error in the message list.
     *
     * @param hide True to hide the error, false to show the error.
     */
    public void hideError(boolean hide) {
        hideError = hide;
    }

    /**
     * Throws a CometChatException and handles the error by invoking the provided onError callback.
     *
     * @param cometChatException The CometChatException to be thrown and handled.
     */
    public void throwError(CometChatException cometChatException) {
        if (onError != null) onError.onError(context, cometChatException);
    }

    /**
     * Sets the callback for handling errors in the message list.
     *
     * @param onError The OnError object representing the error callback.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    @Override
    public void onLongClick(List<CometChatMessageOption> list, BaseMessage baseMessage, CometChatMessageBubble cometChatMessageBubble) {
        this.customOption = list;
        this.baseMessage = baseMessage;
        this.messageBubble = cometChatMessageBubble;
        List<ActionItem> itemList = new ArrayList<>();
        for (CometChatMessageOption option : customOption) {
            if (option != null) {
                ActionItem actionItem = new ActionItem(option.getId(), option.getTitle(), option.getIcon(), 0, option.getIconTintColor(), 0, option.getTitleAppearance(), option.getTitleColor());
                actionItem.setBackground(option.getBackgroundColor());
                itemList.add(actionItem);
            }
        }
        cometChatActionSheet.setList(itemList);
        cometChatActionSheet.show();
    }

    public interface ThreadReplyClick {
        void onThreadReplyClick(Context context, BaseMessage baseMessage, CometChatMessageBubble messageBubble);
    }
}
