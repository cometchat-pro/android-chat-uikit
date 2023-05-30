package com.cometchat.chatuikit.conversations;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatConversationEvents;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.soundManager.CometChatSoundManager;
import com.cometchat.chatuikit.shared.resources.soundManager.Sound;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.OnDialogButtonClickListener;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.ClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerTouchListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.cometchat.chatuikit.shared.utils.ConversationsUtils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatBadge.BadgeStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.TypingIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * CometChatConversations is a custom view that displays a list of conversations in the CometChat UI.
 * <p>
 * It extends the CometChatListBase class and provides additional functionalities and customization options.
 * <p>
 * The CometChatConversations class supports different constructors to initialize the view with various parameters.
 * <p>
 * This view can be used to display a list of conversations and provides methods to customize its appearance and behavior.
 * <br>
 * Example :<pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *     <com.cometchat.chatuikit.conversations.CometChatConversations
 *         android:id="@+id/conversations"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 *  </LinearLayout>
 * }
 * </pre>
 */
public class CometChatConversations extends CometChatListBase {
    private Context context;

    private View view;

    private static final String TAG = CometChatConversations.class.getName();

    private static final HashMap<String, CometChatConversationEvents> events = CometChatConversationEvents.conversationEvents;

    private ConversationsViewModel conversationsViewModel;

    private ConversationsAdapter conversationsAdapter;

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private LinearLayout loadingLayout;

    private LinearLayout noConversationView;

    private CometChatSoundManager soundManager;

    private boolean hideError;

    private View emptyView = null;

    private LinearLayout customLayout;

    private TextView emptyStateText;

    private int errorStateTextAppearance = 0;

    private int errorMessageColor = 0;

    private String errorText = null;

    private String emptyText = null;

    private View errorView = null;

    private View loadingView = null;

    private View menuView = null;

    private OnItemClickListener<Conversation> onItemClickListener;

    private OnError onError;

    private OnSelection onSelection;

    private Palette palette;

    private Typography typography;

    private boolean disableSoundForMessages;
    private @RawRes
    int customSoundForMessage = 0;

    private UIKitConstants.SelectionMode selectionMode = UIKitConstants.SelectionMode.NONE;

    private HashMap<Conversation, Boolean> hashMap = new HashMap<>();

    private Function2<Context, Conversation, List<CometChatOption>> options;

    private RecyclerViewSwipeListener swipeHelper;

    private ProgressDialog progressDialog;

    private Conversation conversationTemp;

    private ImageView loadingIcon;

    /**
     * Constructs a new CometChatConversations object with the given context.
     *
     * @param context The context of the view.
     */
    public CometChatConversations(Context context) {
        super(context);
        init(context, null, -1);
    }

    /**
     * Constructs a new CometChatConversations object with the given context and attribute set.
     *
     * @param context The context of the view.
     * @param attrs   The attribute set for the view.
     */
    public CometChatConversations(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatConversations object with the given context, attribute set, and default style attribute.
     *
     * @param context      The context of the view.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute.
     */
    public CometChatConversations(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the view with the given context, attribute set, and default style attribute.
     *
     * @param context      The context of the view.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute.
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        conversationsViewModel = new ConversationsViewModel();
        view = View.inflate(context, R.layout.cometchat_list_view, null);
        progressDialog = new ProgressDialog(getContext());
        customLayout = view.findViewById(R.id.empty_view);
        soundManager = new CometChatSoundManager(context);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noConversationView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        conversationsAdapter = new ConversationsAdapter(context);
        recyclerView.setAdapter(conversationsAdapter);
        conversationsViewModel = ViewModelProviders.of((FragmentActivity) context).get(conversationsViewModel.getClass());
        conversationsViewModel.getMutableConversationList().observe((AppCompatActivity) context, conversationListObserver);
        conversationsViewModel.getStates().observe((AppCompatActivity) context, conversationStates);
        conversationsViewModel.insertAtTop().observe((AppCompatActivity) context, insertAtTop);
        conversationsViewModel.moveToTop().observe((AppCompatActivity) context, moveToTop);
        conversationsViewModel.getTyping().observe((AppCompatActivity) context, typing);
        conversationsViewModel.updateConversation().observe((AppCompatActivity) context, updateConversation);
        conversationsViewModel.playSound().observe((AppCompatActivity) context, this::playSound);
        conversationsViewModel.remove().observe((AppCompatActivity) context, remove);
        conversationsViewModel.progressState().observe((AppCompatActivity) context, conversationDeleteObserver);
        conversationsViewModel.getCometChatException().observe((AppCompatActivity) context, cometChatExceptionObserver);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    conversationsViewModel.fetchConversation();
                }
            }
        });
        clickEvents();
        super.addListView(view);
        super.hideSearch(true);
        setOptions(null);
        super.setSearchTextAppearance(typography.getText1());
        super.setTitleAppearance(typography.getHeading());
        emptyStateTextAppearance(typography.getHeading());
        emptyStateTextColor(palette.getAccent400());
        errorStateTextAppearance(typography.getText1());
        errorStateTextColor(palette.getAccent700());
        setLoadingIconTintColor(palette.getAccent());
        super.setTitleColor(palette.getAccent());
        if (palette.getGradientBackground() != null) {
            super.setBackground(palette.getGradientBackground());
        } else {
            super.setBackground(palette.getBackground());
        }
        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                Conversation conversation = conversationsAdapter.getConversation(viewHolder.getLayoutPosition());
                getOption(conversation, underlayButtons);

            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);
    }

    private void playSound(Boolean play) {
        if (play) playSound();
    }

    private void clickEvents() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Conversation conversation = (Conversation) view.getTag(R.string.conversation);
                if (UIKitConstants.SelectionMode.SINGLE.equals(selectionMode)) {
                    hashMap.clear();
                    hashMap.put(conversation, true);
                    conversationsAdapter.selectConversation(hashMap);
                } else if (UIKitConstants.SelectionMode.MULTIPLE.equals(selectionMode)) {
                    if (hashMap.containsKey(conversation)) hashMap.remove(conversation);
                    else hashMap.put(conversation, true);
                    conversationsAdapter.selectConversation(hashMap);
                }
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(conversation, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                Conversation conversation = (Conversation) view.getTag(R.string.conversation);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(conversation, position);
            }
        }));
    }

    /**
     * Sets the options for the conversation item.
     *
     * @param options The function to provide the options for the conversation item.
     */
    public void setOptions(Function2<Context, Conversation, List<CometChatOption>> options) {
        if (options != null) {
            this.options = options;
        } else {
            this.options = (context, conversation) -> ConversationsUtils.getDefaultOptions(context, () -> handleDeleteConversation(conversation));
        }
    }

    private void handleDeleteConversation(Conversation conversation) {
        new CometChatDialog(context, 0, typography.getText1(), typography.getText3(), palette.getAccent900(), 0, palette.getAccent700(), context.getString(R.string.delete_conversation_message), "", getContext().getString(R.string.yes), getResources().getString(R.string.no), "", palette.getPrimary(), palette.getPrimary(), 0, new OnDialogButtonClickListener() {
            @Override
            public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                    conversationTemp = conversation;
                    conversationsViewModel.deleteConversation(conversation);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    alertDialog.dismiss();
                }
            }
        }, 1, true);

    }

    private void getOption(Conversation conversation, List<RecyclerViewSwipeListener.UnderlayButton> buttons) {
        List<CometChatOption> optionsArrayList;
        if (options != null) {
            optionsArrayList = options.apply(context, conversation);
            for (int i = 0; i < optionsArrayList.size(); i++) {
                CometChatOption cometChatOption = optionsArrayList.get(i);
                buttons.add(new RecyclerViewSwipeListener.UnderlayButton(cometChatOption.getTitle(), Utils.drawableToBitmap(getResources().getDrawable(cometChatOption.getIcon())), cometChatOption.getBackgroundColor(), new RecyclerViewSwipeListener.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        if (cometChatOption.getClick() != null) {
                            cometChatOption.getClick().onClick();
                        } else {
                            if (cometChatOption.getId().equalsIgnoreCase(UIKitConstants.ConversationOption.DELETE)) {
                                handleDeleteConversation(conversation);
                            }
                        }
                    }
                }));
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        conversationsViewModel.addListener();
        conversationsViewModel.fetchConversation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        conversationsViewModel.removeListener();
    }

    /**
     * Retrieves the selected conversations from the view.
     *
     * @return The list of selected Conversation objects.
     */
    public List<Conversation> getSelectedConversation() {
        List<Conversation> conversationList = new ArrayList<>();
        for (HashMap.Entry<Conversation, Boolean> entry : hashMap.entrySet()) {
            conversationList.add(entry.getKey());
        }
        return conversationList;
    }

    /**
     * Sets the text for the empty state message of the view.
     *
     * @param message The message to be displayed in the empty state.
     */
    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.no_chats));
    }

    /**
     * Sets the text color for the empty state message of the view.
     *
     * @param color The color to be applied to the empty state text.
     */
    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    /**
     * Sets the font for the empty state message of the view.
     *
     * @param font The font to be applied to the empty state text.
     */
    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance for the empty state message of the view.
     *
     * @param appearance The text appearance to be applied to the empty state text.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text appearance for the error state message of the view.
     *
     * @param appearance The text appearance to be applied to the error state text.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the text color for the error state message of the view.
     *
     * @param errorMessageColor The color to be applied to the error state text.
     */
    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error state message of the view.
     *
     * @param errorText The error message to be displayed.
     */
    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    /**
     * Sets the layout resource for the empty state view.
     *
     * @param id The layout resource ID for the empty state view.
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
     * @param id The layout resource ID for the error state view.
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
     * @param id The layout resource ID for the loading state view.
     */
    public void setLoadingStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                loadingView = View.inflate(context, id, null);
            } catch (Exception e) {
                loadingView = null;
            }
        }
    }

    /**
     * Sets the tint color for the loading icon.
     *
     * @param color The color to be applied to the loading icon.
     */
    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the background color for the view using a color array and gradient orientation.
     *
     * @param colorArray  The array of colors for the gradient background.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    /**
     * Sets the style for the conversations view.
     *
     * @param style The style to be applied to the conversations view.
     */
    public void setStyle(ConversationsStyle style) {
        if (style != null) {
            super.setTitleAppearance(style.getTitleAppearance());
            super.setTitleFont(style.getTitleFont());
            super.setTitleColor(style.getTitleColor());
            super.backIconTint(style.getBackIconTint());
            setLoadingIconTintColor(style.getLoadingIconTint());
            emptyStateTextAppearance(style.getEmptyTextAppearance());
            errorStateTextAppearance(style.getErrorTextAppearance());
            emptyStateTextFont(style.getEmptyTextFont());
            emptyStateTextColor(style.getEmptyTextColor());
            errorStateTextColor(style.getErrorTextColor());

            conversationsAdapter.setLastMessageTextFont(style.getLastMessageTextFont());
            conversationsAdapter.setLastMessageTextColor(style.getLastMessageTextColor());
            conversationsAdapter.setLastMessageTextAppearance(style.getLastMessageTextAppearance());

            conversationsAdapter.setThreadIndicatorTextColor(style.getThreadIndicatorTextColor());
            conversationsAdapter.setThreadIndicatorTextAppearance(style.getThreadIndicatorTextAppearance());
            conversationsAdapter.setThreadIndicatorTextFont(style.getThreadIndicatorTextFont());

            conversationsAdapter.setTypingIndicatorTextColor(style.getTypingIndicatorTextColor());
            conversationsAdapter.setTypingIndicatorTextFont(style.getTypingIndicatorTextFont());
            conversationsAdapter.setTypingIndicatorTextAppearance(style.getTypingIndicatorTextAppearance());

            conversationsAdapter.setOnlineStatusColor(style.getOnlineStatusColor());
            conversationsAdapter.setSeparatorColor(style.getSeparatorColor());

            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());

            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());

            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());

        }
    }

    /**
     * Hides or shows the error state message.
     *
     * @param hideError true to hide the error state message, false to show it.
     */
    public void hideError(boolean hideError) {
        this.hideError = hideError;
    }

    /**
     * Disables or enables sound for incoming messages.
     *
     * @param disableSoundForMessages true to disable sound for incoming messages, false to enable it.
     */
    public void disableSoundForMessages(boolean disableSoundForMessages) {
        this.disableSoundForMessages = disableSoundForMessages;
    }

    /**
     * Sets a custom sound for incoming messages.
     *
     * @param customSoundForMessages The resource ID of the custom sound for incoming messages.
     */
    public void setCustomSoundForMessages(int customSoundForMessages) {
        this.customSoundForMessage = customSoundForMessages;
    }

    /**
     * Disables or enables users' presence indicators in the conversations view.
     *
     * @param disableUsersPresence true to disable users' presence indicators, false to enable them.
     */
    public void disableUsersPresence(boolean disableUsersPresence) {
        conversationsAdapter.setDisableUsersPresence(disableUsersPresence);
    }

    /**
     * Sets the callback for handling errors in the conversations view.
     *
     * @param onError The OnError callback for handling errors.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    /**
     * Disables or enables the read receipt feature in the conversations view.
     *
     * @param disableReceipt true to disable the read receipt feature, false to enable it.
     */
    public void disableReceipt(boolean disableReceipt) {
        conversationsAdapter.setDisableReadReceipt(disableReceipt);
        conversationsViewModel.disableReceipt(disableReceipt);
    }

    /**
     * Disables or enables the typing indicator in the conversations view.
     *
     * @param disableTyping true to disable the typing indicator, false to enable it.
     */
    public void disableTyping(boolean disableTyping) {
        conversationsAdapter.setDisableTyping(disableTyping);
    }

    /**
     * Sets the icon for protected group conversations.
     *
     * @param protectedGroupIcon The resource ID of the icon for protected group conversations.
     */
    public void setProtectedGroupIcon(int protectedGroupIcon) {
        conversationsAdapter.setProtectedGroupIcon(protectedGroupIcon);
    }

    /**
     * Sets the icon for private group conversations.
     *
     * @param privateGroupIcon The resource ID of the icon for private group conversations.
     */
    public void setPrivateGroupIcon(int privateGroupIcon) {
        conversationsAdapter.setPrivateGroupIcon(privateGroupIcon);
    }

    /**
     * Sets the icon for read status in conversations.
     *
     * @param readIcon The resource ID of the icon for read status.
     */
    public void setReadIcon(int readIcon) {
        conversationsAdapter.setReadIcon(readIcon);
    }

    /**
     * Sets the icon for delivered status in conversations.
     *
     * @param deliveredIcon The resource ID of the icon for delivered status.
     */
    public void setDeliveredIcon(int deliveredIcon) {
        conversationsAdapter.setDeliveredIcon(deliveredIcon);
    }

    /**
     * Sets the icon for sent status in conversations.
     *
     * @param sentIcon The resource ID of the icon for sent status.
     */
    public void setSentIcon(int sentIcon) {
        conversationsAdapter.setSentIcon(sentIcon);
    }

    /**
     * Sets the date pattern for displaying dates in the conversations view.
     *
     * @param datePattern The function that formats the date pattern for conversations.
     */
    public void setDatePattern(Function1<Conversation, String> datePattern) {
        conversationsAdapter.setDatePattern(datePattern);
    }

    /**
     * Sets the custom subtitle view for conversations.
     *
     * @param subtitle The function that provides the custom subtitle view for conversations.
     */
    public void setSubtitleView(Function2<Context, Conversation, View> subtitle) {
        conversationsAdapter.setSubtitle(subtitle);
    }

    /**
     * Sets the custom tail view for conversations.
     *
     * @param tail The function that provides the custom tail view for conversations.
     */
    public void setTail(Function2<Context, Conversation, View> tail) {
        conversationsAdapter.setTailView(tail);
    }

    /**
     * Sets the custom list item view for conversations.
     *
     * @param listItemView The function that provides the custom list item view for conversations.
     */
    public void setListItemView(Function2<Context, Conversation, View> listItemView) {
        conversationsAdapter.setCustomView(listItemView);
    }

    /**
     * Sets the avatar style for conversations.
     *
     * @param style The style to be applied to the avatars in conversations.
     */
    public void setAvatarStyle(AvatarStyle style) {
        conversationsAdapter.setAvatarStyle(style);
    }

    /**
     * Sets the status indicator style for conversations.
     *
     * @param style The style to be applied to the status indicators in conversations.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        conversationsAdapter.setStatusIndicatorStyle(style);
    }

    /**
     * Sets the list item style for conversations.
     *
     * @param style The style to be applied to the list items in conversations.
     */
    public void setListItemStyle(ListItemStyle style) {
        conversationsAdapter.setListItemStyle(style);
    }

    /**
     * Sets the badge style for conversations.
     *
     * @param badgeStyle The style to be applied to the badges in conversations.
     */
    public void setBadgeStyle(BadgeStyle badgeStyle) {
        conversationsAdapter.setBadgeStyle(badgeStyle);
    }

    /**
     * Sets the date style for conversations.
     *
     * @param dateStyle The style to be applied to the dates in conversations.
     */
    public void setDateStyle(DateStyle dateStyle) {
        conversationsAdapter.setDateStyle(dateStyle);
    }

    /**
     * Sets the listener for conversation item selection events.
     *
     * @param onSelection The listener to handle conversation item selection events.
     */
    public void setOnSelection(OnSelection onSelection) {
        this.onSelection = onSelection;
    }

    /**
     * Sets the selection mode for conversations.
     *
     * @param selectionMode The selection mode to be applied to conversations.
     */
    public void setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        hashMap.clear();
        conversationsAdapter.selectConversation(hashMap);
        this.selectionMode = selectionMode;
        if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode) && selectionMode != null) {
            setMenuIcon(true);
        }
    }

    private void setMenuIcon(boolean value) {
        if (value && menuView == null) {
            ImageView icon = new ImageView(context);
            icon.setImageResource(R.drawable.ic_check_primary);
            icon.setImageTintList(ColorStateList.valueOf(palette.getPrimary()));
            super.setMenu(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelection != null) {
                        onSelection.onSelection(getSelectedConversation());
                    }
                }
            });
        } else if (!value) {
            super.hideMenuIcon(true);
        }
    }

    @Override
    public void setMenu(View view) {
        this.menuView = view;
        super.setMenu(view);
    }
    /**
     * Sets whether to hide the separator between conversation items.
     *
     * @param hideSeparator {@code true} to hide the separator, {@code false} to show it.
     */
    public void hideSeparator(boolean hideSeparator) {
        conversationsAdapter.hideSeparator(hideSeparator);
    }

    /**
     * Returns the RecyclerView used in the conversations view.
     *
     * @return The RecyclerView instance.
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Returns the ViewModel associated with the conversations view.
     *
     * @return The ConversationsViewModel instance.
     */
    public ConversationsViewModel getViewModel() {
        return conversationsViewModel;
    }

    /**
     * Returns the ConversationsAdapter used in the conversations view.
     *
     * @return The ConversationsAdapter instance.
     */
    public ConversationsAdapter getConversationsAdapter() {
        return conversationsAdapter;
    }

    /**
     * Sets the adapter for the conversations view.
     *
     * @param adapter The RecyclerView.Adapter to be set.
     */
    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void playSound() {
        if (!disableSoundForMessages)
            soundManager.play(Sound.incomingMessageFromOther, customSoundForMessage);
    }

    Observer<List<Conversation>> conversationListObserver = new Observer<List<Conversation>>() {
        @Override
        public void onChanged(List<Conversation> conversations) {
            conversationsAdapter.setList(conversations);
        }
    };

    //notify ui when there is a change in a conversationList
    Observer<Integer> updateConversation = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            conversationsAdapter.notifyItemChanged(integer);
        }
    };

    //notify ui when there is a new conversation added to the list
    Observer<Integer> insertAtTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            conversationsAdapter.notifyItemInserted(integer);
            scrollToTop();
        }
    };

    //move the existing conversation object to the top when new message is received
    Observer<Integer> moveToTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            conversationsAdapter.notifyItemMoved(integer, 0);
            conversationsAdapter.notifyItemChanged(0);
            scrollToTop();
        }
    };

    private void scrollToTop() {
        if (((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() < 5)
            layoutManager.scrollToPosition(0);
    }

    Observer<HashMap<Conversation, TypingIndicator>> typing = new Observer<HashMap<Conversation, TypingIndicator>>() {
        @Override
        public void onChanged(HashMap<Conversation, TypingIndicator> typingIndicatorHashMap) {
            conversationsAdapter.typing(typingIndicatorHashMap);
        }
    };

    Observer<Integer> remove = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            conversationsAdapter.notifyItemRemoved(integer);
        }
    };

    Observer<UIKitConstants.States> conversationStates = states -> {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            noConversationView.setVisibility(View.GONE);
            customLayout.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (UIKitConstants.States.ERROR.equals(states)) {
            showError(false);
        } else if (UIKitConstants.States.EMPTY.equals(states)) {
            if (emptyView != null) {
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(emptyView);
            } else {
                noConversationView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            noConversationView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            customLayout.setVisibility(GONE);
        }
    };

    Observer<ConversationDeleteState> conversationDeleteObserver = new Observer<ConversationDeleteState>() {
        @Override
        public void onChanged(ConversationDeleteState progressState) {
            if (ConversationDeleteState.SUCCESS_DELETE.equals(progressState)) {
                progressDialog.dismiss();
                conversationTemp = null;
            } else if (ConversationDeleteState.FAILURE_DELETE.equals(progressState)) {
                progressDialog.dismiss();
                showError(true);
            } else if (ConversationDeleteState.INITIATED_DELETE.equals(progressState)) {
                progressDialog.setMessage(context.getString(R.string.deleting_conversation));
                progressDialog.show();
            }
        }
    };

    Observer<CometChatException> cometChatExceptionObserver = exception -> {
        if (onError != null) onError.onError(context, exception);
    };

    private void showError(boolean forDelete) {
        String errorMessage;
        if (errorText != null) errorMessage = errorText;
        else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!hideError && errorView != null) {
            customLayout.removeAllViews();
            customLayout.addView(errorView);
            customLayout.setVisibility(VISIBLE);
        } else {
            customLayout.setVisibility(GONE);
            if (!hideError) {
                if (getContext() != null) {
                    new CometChatDialog(context, 0, errorStateTextAppearance, typography.getText3(), palette.getAccent900(), 0, errorMessageColor, errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", palette.getPrimary(), palette.getPrimary(), 0, (alertDialog, which, popupId) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            alertDialog.dismiss();
                            if (forDelete)
                                conversationsViewModel.deleteConversation(conversationTemp);
                            else conversationsViewModel.fetchConversation();
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }, 0, false);
                }
            }

        }
    }

    /**
     * Sets the ConversationsRequestBuilder for fetching conversations.
     *
     * @param conversationsRequestBuilder The ConversationsRequestBuilder instance.
     */
    public void setConversationsRequestBuilder(ConversationsRequest conversationsRequestBuilder) {
        conversationsViewModel.setConversationsRequestBuilder(conversationsRequestBuilder);
    }

    /**
     * Sets the click event listener for the conversations list.
     *
     * @param onItemClickListener The OnItemClickListener instance.
     */
    public void setItemClickListener(OnItemClickListener<Conversation> onItemClickListener) {
        if (onItemClickListener != null) this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void hideSearch(boolean hideSearch) {
        Log.v(TAG, "hideSearch feature is not supported");
    }

    @Override
    public void setSearchPlaceholderText(String placeholder) {
        Log.v(TAG, "setSearchPlaceholder feature is not supported");
    }

    @Override
    public void setSearchCornerRadius(float searchBoxRadius) {
        Log.v(TAG, "setSearchCornerRadius feature is not supported");
    }

    @Override
    public void setSearchBackground(int searchBoxColor) {
        Log.v(TAG, "setSearchBackground feature is not supported");
    }

    @Override
    public void setSearchText(String text) {
        Log.v(TAG, "setSearchText search feature is not supported");
    }

    @Override
    public void setSearchPlaceHolderColor(int color) {
        Log.v(TAG, "setSearchPlaceHolderColor feature is not supported");
    }

    @Override
    public void setSearchTextColor(int color) {
        Log.v(TAG, "setSearchTextColor feature is not supported");
    }

    @Override
    public void setSearchTextFont(String fontName) {
        Log.v(TAG, "setSearchTextFont feature is not supported");
    }

    @Override
    public void setSearchTextAppearance(int appearance) {
        Log.v(TAG, "setSearchTextAppearance feature is not supported");
    }

    @Override
    public void setSearchBorderColor(int color) {
        Log.v(TAG, "setSearchBorderColor feature is not supported");
    }

    @Override
    public void setSearchBorderWidth(int width) {
        Log.v(TAG, "setSearchBorderWidth feature is not supported");
    }

    @Override
    public void setSearchIconTint(int color) {
        Log.v(TAG, "setStartIconTint feature is not supported");
    }

    @Override
    public void setSearchBoxIcon(int res) {
        Log.v(TAG, "setSearchBoxStartIcon feature is not supported");
    }

    public interface OnSelection {
        void onSelection(List<Conversation> conversations);
    }

}
