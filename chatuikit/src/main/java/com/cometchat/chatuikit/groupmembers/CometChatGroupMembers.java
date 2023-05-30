package com.cometchat.chatuikit.groupmembers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.ClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerTouchListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.cometchat.chatuikit.shared.utils.DetailsUtils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The CometChatGroupMembers class represents a view that displays a list of group members.
 * <p>
 * It extends the CometChatListBase class.
 */
public class CometChatGroupMembers extends CometChatListBase {
    private Context context;

    private View view;

    private static final String TAG = CometChatGroupMembers.class.getName();

    private GroupMembersViewModel groupMembersViewModel;

    private GroupMembersAdapter groupMembersAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private LinearLayout loadingLayout;

    private LinearLayout noGroupMemberView;

    private boolean hideError;

    private View emptyView = null;

    public LinearLayout customLayout;

    private TextView emptyStateText;

    private int errorStateTextAppearance = 0;

    private int errorMessageColor = 0;

    private String errorText = null;

    private View errorView = null;

    private View loadingView = null;

    private View menuView = null;

    private OnItemClickListener<GroupMember> onItemClickListener;

    private OnError onError;

    private OnSelection onSelection;

    private Palette palette;

    private Typography typography;

    private UIKitConstants.SelectionMode selectionMode = UIKitConstants.SelectionMode.NONE;

    private HashMap<GroupMember, Boolean> hashMap = new HashMap<>();

    private Function3<Context, GroupMember, Group, List<CometChatOption>> options;

    private RecyclerViewSwipeListener swipeHelper;

    private ImageView loadingIcon;

    private Group group;

    private @DrawableRes int submitIcon;

    private ImageView icon;

    /**
     * Constructor for creating a CometChatGroupMembers instance.
     *
     * @param context The context of the activity or fragment.
     */
    public CometChatGroupMembers(Context context) {
        super(context);
        init(context, null, -1);
    }

    /**
     * Constructor for creating a CometChatGroupMembers instance.
     *
     * @param context The context of the activity or fragment.
     * @param attrs   The attribute set for the view.
     */
    public CometChatGroupMembers(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    /**
     * Constructor for creating a CometChatGroupMembers instance.
     *
     * @param context      The context of the activity or fragment.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute.
     */
    public CometChatGroupMembers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the CometChatGroupMembers view.
     *
     * @param context      The context of the activity or fragment.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute.
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        view = View.inflate(context, R.layout.cometchat_list_view, null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CometChatGroupMembers, 0, 0);

        String title = a.getString(R.styleable.CometChatGroupMembers_title) == null ? getResources().getString(R.string.members) : a.getString(R.styleable.CometChatGroupMembers_title);
        int titleColor = a.getColor(R.styleable.CometChatGroupMembers_titleColor, palette.getAccent());
        boolean hideSearch = a.getBoolean(R.styleable.CometChatGroupMembers_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.CometChatGroupMembers_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.CometChatGroupMembers_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.CometChatGroupMembers_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.CometChatGroupMembers_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.CometChatGroupMembers_searchBorderColor, 0);
        int backgroundColor = a.getColor(R.styleable.CometChatGroupMembers_backgroundColor, palette.getBackground());
        boolean showBackButton = a.getBoolean(R.styleable.CometChatGroupMembers_showBackButton, true);

        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatGroupMembers_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatGroupMembers_backButtonIcon) : getResources().getDrawable(R.drawable.ic_arrow_back);
        String searchPlaceholder = a.getString(R.styleable.CometChatGroupMembers_searchPlaceholder);
        int backIconTint = a.getColor(R.styleable.CometChatGroupMembers_backIconTint, palette.getPrimary());

        customLayout = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noGroupMemberView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        groupMembersAdapter = new GroupMembersAdapter(context);
        recyclerView.setAdapter(groupMembersAdapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    groupMembersViewModel.fetchGroupMember();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }
        });
        clickEvents();
        super.setSearchPlaceholderText(searchPlaceholder);
        super.setTitle(title);
        super.backIcon(backButtonIcon);
        super.backIconTint(backIconTint);
        super.showBackButton(showBackButton);
        super.setSearchTextAppearance(typography.getText1());
        super.setTitleAppearance(typography.getHeading());
        emptyStateTextAppearance(typography.getHeading());
        emptyStateText(context.getResources().getString(R.string.no_members));
        errorStateTextAppearance(typography.getText1());
        emptyStateTextColor(palette.getAccent400());
        setLoadingIconTintColor(palette.getAccent());
        super.setTitleColor(titleColor);
        super.setSearchTextColor(searchTextColor);
        super.setSearchBorderColor(searchBorderColor);
        super.setSearchBorderWidth(searchBorderWidth);
        super.setSearchPlaceHolderColor(searchTextColor);
        super.setSearchBackground(searchBoxColor);
        super.hideSearch(hideSearch);
        super.setSearchCornerRadius(searchBoxRadius);
        if (palette.getGradientBackground() != null) setBackground(palette.getGradientBackground());
        else setBackgroundColor(backgroundColor);
        super.addListView(view);
        groupMembersViewModel = new GroupMembersViewModel();
        groupMembersViewModel = ViewModelProviders.of((FragmentActivity) context).get(groupMembersViewModel.getClass());
        groupMembersViewModel.getMutableGroupMembersList().observe((AppCompatActivity) context, ListObserver);
        groupMembersViewModel.getStates().observe((AppCompatActivity) context, groupMemberStates);
        groupMembersViewModel.insertAtTop().observe((AppCompatActivity) context, insertAtTop);
        groupMembersViewModel.moveToTop().observe((AppCompatActivity) context, moveToTop);
        groupMembersViewModel.updateGroupMember().observe((AppCompatActivity) context, update);
        groupMembersViewModel.removeGroupMember().observe((AppCompatActivity) context, remove);
        groupMembersAdapter.setViewModel(groupMembersViewModel);
        groupMembersViewModel.getCometChatException().observe((AppCompatActivity) context, exceptionObserver);
        setOptions(null);
        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                GroupMember GroupMember = groupMembersAdapter.getGroupMember(viewHolder.getLayoutPosition());
                getOption(GroupMember, group, underlayButtons);

            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);

        super.addOnSearchListener((state, text) -> {
            if (state.equals(SearchState.TextChange)) {
                if (text.length() == 0) {
                    groupMembersViewModel.searchGroupMembers(null);
                } else {
                    groupMembersViewModel.searchGroupMembers(text);
                }
            }
        });
    }

    private void getOption(GroupMember groupMember, Group group, List<RecyclerViewSwipeListener.UnderlayButton> buttons) {
        List<CometChatOption> optionsArrayList;
        if (options != null) {
            optionsArrayList = options.apply(context, groupMember, group);
            for (int i = 0; i < optionsArrayList.size(); i++) {
                CometChatOption cometChatOption = optionsArrayList.get(i);
                buttons.add(new RecyclerViewSwipeListener.UnderlayButton(cometChatOption.getTitle(), Utils.drawableToBitmap(getResources().getDrawable(cometChatOption.getIcon())), cometChatOption.getBackgroundColor(), pos -> {
                    if (cometChatOption.getClick() != null) {
                        cometChatOption.getClick().onClick();
                    } else {
                        if (cometChatOption.getId().equalsIgnoreCase(UIKitConstants.GroupMemberOption.BAN)) {
                            groupMembersViewModel.banGroupMember(groupMember);
                        } else if (cometChatOption.getId().equalsIgnoreCase(UIKitConstants.GroupMemberOption.KICK)) {
                            groupMembersViewModel.kickGroupMember(groupMember);
                        }
                    }
                }));
            }
        }
    }

    /**
     * Retrieves the selected group members from the list.
     *
     * @return A list of selected GroupMember objects.
     */
    public List<GroupMember> getSelectedGroupMembers() {
        List<GroupMember> GroupMemberList = new ArrayList<>();
        for (HashMap.Entry<GroupMember, Boolean> entry : hashMap.entrySet()) {
            GroupMemberList.add(entry.getKey());
        }
        return GroupMemberList;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        groupMembersViewModel.setGroup(group);
        groupMembersViewModel.addListeners();
        groupMembersViewModel.fetchGroupMember();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        groupMembersViewModel.removeListeners();
    }

    /**
     * Sets the group for the CometChatGroupMembers view.
     *
     * @param group The Group object to be set.
     */
    public void setGroup(Group group) {
        this.group = group;
        groupMembersAdapter.setGroup(group);
    }

    /**
     * Sets the options for the CometChatGroupMembers view.
     *
     * @param options The function that provides the options for each group member.
     */
    public void setOptions(Function3<Context, GroupMember, Group, List<CometChatOption>> options) {
        if (options != null) this.options = options;
        else
            this.options = (context, groupMember, group) -> DetailsUtils.getDefaultGroupMemberOptions(context, groupMember, group, null);
    }

    /**
     * Disables or enables the presence status of users in the group members list.
     *
     * @param disable If true, users' presence will be disabled. If false, users' presence will be enabled.
     */
    public void disableUsersPresence(boolean disable) {
        groupMembersAdapter.disableUsersPresence(disable);
    }

    /**
     * Hides or shows the separator between group members in the list.
     *
     * @param hide True to hide the separator, false to show it.
     */
    public void hideSeparator(boolean hide) {
        groupMembersAdapter.hideSeparator(hide);
    }

    /**
     * Sets the text to be displayed when the member list is empty.
     *
     * @param message The text message to be displayed.
     */
    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.no_members));
    }

    /**
     * Sets the text color for the empty state text.
     *
     * @param color The text color.
     */
    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    /**
     * Sets the font for the empty state text.
     *
     * @param font The font to set.
     */
    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the appearance for the empty state text.
     *
     * @param appearance The appearance to set.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the appearance for the error state text.
     *
     * @param appearance The appearance to set.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the text color for the error state text.
     *
     * @param errorMessageColor The text color to set.
     */
    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error state text.
     *
     * @param errorText The error text to set.
     */
    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    /**
     * Sets the layout resource for the empty state view.
     *
     * @param id The layout resource ID.
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
     * Sets the tint color for the loading icon.
     *
     * @param color The tint color to set.
     */
    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the layout resource for the error state view.
     *
     * @param id The layout resource ID.
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
     * @param id The layout resource ID.
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
     * Sets the background color for the group members list.
     *
     * @param colorArray  The array of colors to set as the background gradient.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    /**
     * Sets the subtitle view for the group members.
     *
     * @param subtitle The function to retrieve the subtitle view.
     */
    public void setSubtitleView(Function3<Context, GroupMember, Group, View> subtitle) {
        groupMembersAdapter.setSubtitle(subtitle);
    }

    /**
     * Sets the tail view for the group members.
     *
     * @param tail The function to retrieve the tail view.
     */
    public void setTailView(Function3<Context, GroupMember, Group, View> tail) {
        groupMembersAdapter.setTailView(tail);
    }

    /**
     * Sets the custom list item view for the group members.
     *
     * @param listItemView The function to retrieve the custom list item view.
     */
    public void setListItemView(Function3<Context, GroupMember, Group, View> listItemView) {
        groupMembersAdapter.setCustomView(listItemView);
    }

    /**
     * Sets the style for the group members list.
     *
     * @param style The style to set.
     */
    public void setStyle(GroupMembersStyle style) {
        if (style != null) {
            super.setTitleAppearance(style.getTitleAppearance());
            super.setTitleFont(style.getTitleFont());
            super.setTitleColor(style.getTitleColor());
            super.backIconTint(style.getBackIconTint());

            super.setSearchBackground(style.getSearchBackground());
            super.setSearchBorderWidth(style.getSearchBorderWidth());
            super.setSearchCornerRadius(style.getSearchBorderRadius());
            super.setSearchTextFont(style.getSearchTextFont());
            super.setSearchTextAppearance(style.getSearchTextAppearance());
            super.setSearchTextColor(style.getSearchTextColor());
            super.setSearchIconTint(style.getSearchIconTint());

            setLoadingIconTintColor(style.getLoadingIconTint());
            emptyStateTextAppearance(style.getEmptyTextAppearance());
            errorStateTextAppearance(style.getErrorTextAppearance());
            emptyStateTextFont(style.getEmptyTextFont());
            emptyStateTextColor(style.getEmptyTextColor());
            errorStateTextColor(style.getErrorTextColor());

            groupMembersAdapter.setOnlineStatusColor(style.getOnlineStatusColor());
            groupMembersAdapter.setSeparatorColor(style.getSeparatorColor());

            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the avatar style for the group members.
     *
     * @param style The avatar style to set.
     */
    public void setAvatarStyle(AvatarStyle style) {
        groupMembersAdapter.setAvatarStyle(style);
    }

    /**
     * Sets the status indicator style for the group members.
     *
     * @param style The status indicator style to set.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        groupMembersAdapter.setStatusIndicatorStyle(style);
    }

    /**
     * Sets the list item style for the group members.
     *
     * @param style The list item style to set.
     */
    public void setListItemStyle(ListItemStyle style) {
        groupMembersAdapter.setListItemStyle(style);
    }

    /**
     * Sets the callback for selection events in the group members list.
     *
     * @param onSelection The callback to set.
     */
    public void setOnSelection(OnSelection onSelection) {
        this.onSelection = onSelection;
    }

    /**
     * Sets the request builder for fetching group members.
     *
     * @param groupMembersRequestBuilder The request builder to set.
     */
    public void setGroupMembersRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder groupMembersRequestBuilder) {
        groupMembersViewModel.setGroupMembersRequestBuilder(groupMembersRequestBuilder);
    }

    /**
     * Sets the request builder for searching group members.
     *
     * @param groupMembersRequestBuilder The request builder to set.
     */
    public void setSearchRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder groupMembersRequestBuilder) {
        groupMembersViewModel.setSearchRequestBuilder(groupMembersRequestBuilder);
    }

    /**
     * Sets the selection mode for the group members.
     *
     * @param selectionMode The selection mode to set.
     */
    public void setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        hashMap.clear();
        groupMembersAdapter.selectGroupMember(hashMap);
        this.selectionMode = selectionMode;
        if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode) && selectionMode != null) {
            setMenuIcon(true);
        }
    }

    /**
     * Sets the submit icon for the group members.
     *
     * @param submitIcon The submit icon to set.
     */
    public void setSubmitIcon(@DrawableRes int submitIcon) {
        if (submitIcon != 0 && icon != null) {
            icon.setImageResource(submitIcon);
        }
        this.submitIcon = submitIcon;
    }

    /**
     * Sets the selection icon for the group members.
     *
     * @param selectionIcon The selection icon to set.
     */
    public void setSelectionIcon(@DrawableRes int selectionIcon) {
        groupMembersAdapter.setSelectionIcon(selectionIcon);
    }

    private void setMenuIcon(boolean value) {
        if (value && menuView == null) {
            icon = new ImageView(context);
            if (submitIcon == 0) {
                icon.setImageResource(R.drawable.ic_check_primary);
                icon.setImageTintList(ColorStateList.valueOf(palette.getPrimary()));
            } else icon.setImageResource(submitIcon);
            super.setMenu(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelection != null) {
                        onSelection.onSelection(getSelectedGroupMembers());
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

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public GroupMembersViewModel getViewModel() {
        return groupMembersViewModel;
    }

    public GroupMembersAdapter getConversationsAdapter() {
        return groupMembersAdapter;
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void clickEvents() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                GroupMember groupMember = (GroupMember) view.getTag(R.string.member);
                if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode)) {
                    selectGroupMember(groupMember, selectionMode);
                }
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(groupMember, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                GroupMember groupMember = (GroupMember) view.getTag(R.string.member);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(groupMember, position);
            }
        }));
    }

    Observer<UIKitConstants.States> groupMemberStates = states -> {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            noGroupMemberView.setVisibility(View.GONE);
            customLayout.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (UIKitConstants.States.ERROR.equals(states)) {
            showError();
        } else if (UIKitConstants.States.EMPTY.equals(states)) {
            if (emptyView != null) {
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(emptyView);
            } else {
                noGroupMemberView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            noGroupMemberView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            customLayout.setVisibility(GONE);
        }
    };

    /**
     * Hides or shows the error state view.
     *
     * @param hide True to hide the error state view, false to show it.
     */
    public void hideError(boolean hide) {
        hideError = hide;
    }

    private void showError() {
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
                            groupMembersViewModel.fetchGroupMember();
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }, 0, false);
                }
            }

        }
    }

    Observer<Integer> insertAtTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupMembersAdapter.notifyItemInserted(integer);
            scrollToTop();
        }
    };

    Observer<Integer> moveToTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupMembersAdapter.notifyItemMoved(integer, 0);
            groupMembersAdapter.notifyItemChanged(0);
            scrollToTop();
        }
    };

    private void scrollToTop() {
        if (layoutManager.findFirstVisibleItemPosition() < 5) layoutManager.scrollToPosition(0);
    }

    Observer<Integer> update = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupMembersAdapter.notifyItemChanged(integer);
        }
    };

    Observer<Integer> remove = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupMembersAdapter.notifyItemRemoved(integer);
        }
    };

    Observer<CometChatException> exceptionObserver = exception -> {
        if (onError != null) onError.onError(context, exception);
    };

    /**
     * Sets the error callback for handling errors.
     *
     * @param onError The error callback to set.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public OnError getOnError() {
        return onError;
    }

    public boolean isHideError() {
        return hideError;
    }

    public int getErrorStateTextAppearance() {
        return errorStateTextAppearance;
    }

    public int getErrorMessageColor() {
        return errorMessageColor;
    }

    public String getErrorText() {
        return errorText;
    }

    public View getErrorView() {
        return errorView;
    }

    /**
     * Sets the item click listener for the group members list.
     *
     * @param onItemClickListener The item click listener to set.
     */
    public void setItemClickListener(OnItemClickListener<GroupMember> onItemClickListener) {
        if (onItemClickListener != null) this.onItemClickListener = onItemClickListener;
    }

    /**
     * Selects a group member in the group members list.
     *
     * @param groupMember The group member to select.
     * @param mode        The selection mode.
     */
    public void selectGroupMember(GroupMember groupMember, UIKitConstants.SelectionMode mode) {
        if (mode != null) {
            this.selectionMode = mode;
            if (UIKitConstants.SelectionMode.SINGLE.equals(selectionMode)) {
                hashMap.clear();
                hashMap.put(groupMember, true);
                groupMembersAdapter.selectGroupMember(hashMap);
            } else if (UIKitConstants.SelectionMode.MULTIPLE.equals(selectionMode)) {
                if (hashMap.containsKey(groupMember)) hashMap.remove(groupMember);
                else hashMap.put(groupMember, true);
                groupMembersAdapter.selectGroupMember(hashMap);
            }
        }
    }

    Observer<List<GroupMember>> ListObserver = GroupMembers -> {
        groupMembersAdapter.setGroupMemberList(GroupMembers);
    };

    public interface OnSelection {
        void onSelection(List<GroupMember> GroupMemberList);
    }
}
