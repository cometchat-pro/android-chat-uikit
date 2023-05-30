package com.cometchat.chatuikit.bannedmembers;

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

import com.cometchat.chatuikit.R;
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
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * CometChatBannedMembers is a class that extends CometChatListBase and provides functionality
 * for displaying a list of banned members in a group chat.
 * <p>
 * The class includes methods for setting various options and customizing the appearance and behavior
 * of the banned members list. It also provides callback interfaces for handling item click events and selection events.
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *              <com.cometchat.chatuikit.bannedmembers.CometChatBannedMembers
 *                 android:id="@+id/banned_members"
 *                 android:layout_width="match_parent"
 *                 android:layout_height="match_parent" />
 *
 *  </LinearLayout>
 *
 *  //now in activity or fragment
 * CometChatBannedMembers bannedMembers = view.findViewById(R.id.banned_members);
 * bannedMembers.setGroup(group);
 *  }
 *  </pre>
 */
public class CometChatBannedMembers extends CometChatListBase {
    private Context context;

    private View view;

    private static final String TAG = CometChatBannedMembers.class.getName();

    private BannedMembersViewModel bannedMembersViewModel;

    private BannedMembersAdapter bannedMembersAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private LinearLayout loadingLayout;

    private LinearLayout noGroupMemberView;

    private boolean hideError;

    private View emptyView = null;

    private LinearLayout customLayout;

    private TextView emptyStateText;

    private int errorStateTextAppearance = 0;

    private int errorMessageColor = 0;

    private String errorText = null;

    private View errorView = null;

    private View loadingView = null;

    private View menuView = null;

    private OnItemClickListener<GroupMember> onItemClickListener;

    private OnSelection onSelection;

    private Palette palette;

    private Typography typography;

    private UIKitConstants.SelectionMode selectionMode = UIKitConstants.SelectionMode.NONE;

    private HashMap<GroupMember, Boolean> hashMap = new HashMap<>();

    private Function3<Context, GroupMember, Group, List<CometChatOption>> options;

    private RecyclerViewSwipeListener swipeHelper;

    private ImageView loadingIcon;

    private Group group;

    private @DrawableRes
    int submitIcon;

    private ImageView icon;
    private OnError onError;

    public CometChatBannedMembers(Context context) {
        super(context);
        init(context, null, -1);
    }

    public CometChatBannedMembers(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CometChatBannedMembers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        view = View.inflate(context, R.layout.cometchat_list_view, null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CometChatBannedMembers, 0, 0);

        String title = a.getString(R.styleable.CometChatGroupMembers_title) == null ? getResources().getString(R.string.banned_members) : a.getString(R.styleable.CometChatGroupMembers_title);
        int titleColor = a.getColor(R.styleable.CometChatBannedMembers_titleColor, palette.getAccent());
        boolean hideSearch = a.getBoolean(R.styleable.CometChatBannedMembers_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.CometChatBannedMembers_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.CometChatBannedMembers_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.CometChatBannedMembers_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.CometChatBannedMembers_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.CometChatBannedMembers_searchBorderColor, 0);
        int backgroundColor = a.getColor(R.styleable.CometChatBannedMembers_backgroundColor, palette.getBackground());
        boolean showBackButton = a.getBoolean(R.styleable.CometChatBannedMembers_showBackButton, true);

        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatBannedMembers_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatBannedMembers_backButtonIcon) : getResources().getDrawable(R.drawable.ic_arrow_back);
        String searchPlaceholder = a.getString(R.styleable.CometChatBannedMembers_searchPlaceholder);
        int backIconTint = a.getColor(R.styleable.CometChatBannedMembers_backIconTint, palette.getPrimary());

        customLayout = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noGroupMemberView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        bannedMembersAdapter = new BannedMembersAdapter(context);
        recyclerView.setAdapter(bannedMembersAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    bannedMembersViewModel.fetchGroupMember();
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
        bannedMembersViewModel = new BannedMembersViewModel();
        bannedMembersViewModel = ViewModelProviders.of((FragmentActivity) context).get(bannedMembersViewModel.getClass());
        bannedMembersViewModel.getMutableBannedGroupMembersList().observe((AppCompatActivity) context, listObserver);
        bannedMembersViewModel.getStates().observe((AppCompatActivity) context, bannedGroupMemberStates);
        bannedMembersViewModel.insertAtTop().observe((AppCompatActivity) context, insertAtTop);
        bannedMembersViewModel.moveToTop().observe((AppCompatActivity) context, moveToTop);
        bannedMembersViewModel.updateGroupMember().observe((AppCompatActivity) context, update);
        bannedMembersViewModel.removeGroupMember().observe((AppCompatActivity) context, remove);
        bannedMembersViewModel.getCometChatException().observe((AppCompatActivity) context, exceptionObserver);
        setOptions(null);
        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                GroupMember groupMember = bannedMembersAdapter.getGroupMember(viewHolder.getLayoutPosition());
                getOption(groupMember, group, underlayButtons);
            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);

        super.addOnSearchListener((state, text) -> {
            if (state.equals(SearchState.TextChange)) {
                if (text.length() == 0) {
                    bannedMembersViewModel.searchBannedGroupMembers(null);
                } else {
                    bannedMembersViewModel.searchBannedGroupMembers(text);
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
                        if (cometChatOption.getId().equalsIgnoreCase(UIKitConstants.GroupMemberOption.UNBAN)) {
                            bannedMembersViewModel.unBanGroupMember(groupMember);
                        }
                    }
                }));
            }
        }
    }

    /**
     * Retrieves the list of selected banned group members.
     *
     * @return A List of GroupMember objects representing the selected banned group members.
     */
    public List<GroupMember> getSelectedBannedGroupMembers() {
        List<GroupMember> groupMemberList = new ArrayList<>();
        for (HashMap.Entry<GroupMember, Boolean> entry : hashMap.entrySet()) {
            groupMemberList.add(entry.getKey());
        }
        return groupMemberList;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        bannedMembersViewModel.setGroup(group);
        bannedMembersViewModel.addListeners();
        bannedMembersViewModel.fetchGroupMember();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bannedMembersViewModel.removeListeners();
    }

    /**
     * Sets the group associated with the banned members.
     *
     * @param group The Group object representing the group.
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            bannedMembersAdapter.setGroup(group);
        }
    }

    /**
     * Sets the options for handling CometChat actions related to group members.
     *
     * @param options A Function3 that defines the options based on the context, group member, and group. It should return a List of CometChatOption objects.
     */
    public void setOptions(Function3<Context, GroupMember, Group, List<CometChatOption>> options) {
        if (options != null) this.options = options;
        else
            this.options = (context, groupMember, group) -> DetailsUtils.getDefaultBannedMemberOptions(context, group, null);
    }

    /**
     * Disables or enables the presence status of users in the banned members list.
     *
     * @param disable {@code true} to disable users' presence, {@code false} to enable it.
     */
    public void disableUsersPresence(boolean disable) {
        bannedMembersAdapter.disableUsersPresence(disable);
    }

    /**
     * Hides or shows the separator in the banned members list.
     *
     * @param hide {@code true} to hide the separator, {@code false} to show it.
     */
    public void hideSeparator(boolean hide) {
        bannedMembersAdapter.hideSeparator(hide);
    }

    /**
     * Sets the text to display when the banned members list is empty.
     *
     * @param message The message to display.
     *                If {@code null} or empty, the default message "No members" will be used.
     */
    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.no_members));
    }

    /**
     * Sets the color of the text displayed when the banned members list is empty.
     *
     * @param color The color value to set.
     *              If {@code 0}, no color will be set.
     */
    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    /**
     * Sets the font of the text displayed when the banned members list is empty.
     *
     * @param font The name of the font to use.
     *             If {@code null} or empty, the default font will be used.
     */
    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the appearance of the text displayed when the banned members list is empty.
     *
     * @param appearance The resource ID of the text appearance to apply.
     *                   If {@code 0}, no appearance will be set.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the appearance of the error state text.
     *
     * @param appearance The resource ID of the text appearance to apply.
     *                   If {@code 0}, no appearance will be set.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the color of the error state text.
     *
     * @param errorMessageColor The color value to set.
     *                          If {@code 0}, no color will be set.
     */
    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error state text.
     *
     * @param errorText The error text to set.
     *                  If {@code null} or empty, no text will be set.
     */
    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    /**
     * Sets the empty state view for the banned members list.
     *
     * @param id The layout resource ID of the empty state view.
     *           If {@code 0}, no empty state view will be set.
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
     * Sets the tint color of the loading icon.
     *
     * @param color The color value to set.
     *              If {@code 0} or {@code loadingIcon} is {@code null}, no color will be set.
     */
    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the error state view for the banned members list.
     *
     * @param id The layout resource ID of the error state view.
     *           If {@code 0}, no error state view will be set.
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
     * Sets the loading state view for the banned members list.
     *
     * @param id The layout resource ID of the loading state view.
     *           If {@code 0}, no loading state view will be set.
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
     * Sets the background of the banned members list with a gradient color.
     *
     * @param colorArray  An array of colors to create the gradient.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    /**
     * Sets the subtitle view for each item in the banned members list.
     *
     * @param subtitle A function that takes the context, a group member, and a group as parameters
     *                 and returns a custom subtitle view for the item.
     */
    public void setSubtitleView(Function3<Context, GroupMember, Group, View> subtitle) {
        bannedMembersAdapter.setSubtitle(subtitle);
    }

    /**
     * Sets the tail view for each item in the banned members list.
     *
     * @param tail A function that takes the context, a group member, and a group as parameter
     *             and returns a custom tail view for the item.
     */
    public void setTail(Function3<Context, GroupMember, Group, View> tail) {
        bannedMembersAdapter.setTailView(tail);
    }

    /**
     * Sets the custom list item view for each item in the banned members list.
     *
     * @param listItemView A function that takes the context, a group member, and a group as parameters
     *                     and returns a custom view for the item.
     */
    public void setListItemView(Function3<Context, GroupMember, Group, View> listItemView) {
        bannedMembersAdapter.setListItemView(listItemView);
    }

    /**
     * Sets the style for the banned members list.
     *
     * @param style The style object containing various style attributes for the banned members list.
     */
    public void setStyle(BannedMembersStyle style) {
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

            bannedMembersAdapter.setOnlineStatusColor(style.getOnlineStatusColor());
            bannedMembersAdapter.setSeparatorColor(style.getSeparatorColor());

            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the avatar style for the banned members list.
     *
     * @param style The style object containing various style attributes for the avatars of banned members.
     */
    public void setAvatarStyle(AvatarStyle style) {
        bannedMembersAdapter.setAvatarStyle(style);
    }

    /**
     * Sets the status indicator style for the banned members list.
     *
     * @param style The style object containing various style attributes for the status indicators of banned members.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        bannedMembersAdapter.setStatusIndicatorStyle(style);
    }

    /**
     * Sets the list item style for the banned members list.
     *
     * @param style The style object containing various style attributes for the list items of banned members.
     */
    public void setListItemStyle(ListItemStyle style) {
        bannedMembersAdapter.setListItemStyle(style);
    }

    /**
     * Sets the selection listener for the banned members list.
     *
     * @param onSelection The listener to be invoked when a member is selected or deselected.
     */
    public void setOnSelection(OnSelection onSelection) {
        this.onSelection = onSelection;
    }

    /**
     * Sets the request builder for fetching banned group members.
     *
     * @param bannedGroupMembersRequestBuilder The request builder for configuring the parameters of the banned group members request.
     */
    public void setBannedGroupMembersRequestBuilder(BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedGroupMembersRequestBuilder) {
        bannedMembersViewModel.setBannedGroupMembersRequestBuilder(bannedGroupMembersRequestBuilder);
    }

    /**
     * Sets the request builder for searching banned group members.
     *
     * @param bannedGroupMembersRequestBuilder The request builder for configuring the parameters of the search request for banned group members.
     */
    public void setSearchRequestBuilder(BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedGroupMembersRequestBuilder) {
        bannedMembersViewModel.setSearchRequestBuilder(bannedGroupMembersRequestBuilder);
    }

    /**
     * Sets the selection mode for the banned members list.
     *
     * @param selectionMode The selection mode to be set for the banned members list.
     */
    public void setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        hashMap.clear();
        bannedMembersAdapter.selectGroupMember(hashMap);
        this.selectionMode = selectionMode;
        if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode) && selectionMode != null) {
            setMenuIcon(true);
        }
    }

    /**
     * Sets the submit icon for the selection mode.
     *
     * @param submitIcon The drawable resource ID of the submit icon to be set.
     */
    public void setSubmitIcon(@DrawableRes int submitIcon) {
        if (submitIcon != 0 && icon != null) {
            icon.setImageResource(submitIcon);
        }
        this.submitIcon = submitIcon;
    }

    /**
     * Sets the selection icon for the banned members list items.
     *
     * @param selectionIcon The drawable resource ID of the selection icon to be set.
     */
    public void setSelectionIcon(@DrawableRes int selectionIcon) {
        bannedMembersAdapter.setSelectionIcon(selectionIcon);
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
                        onSelection.onSelection(getSelectedBannedGroupMembers());
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

    public BannedMembersViewModel getViewModel() {
        return bannedMembersViewModel;
    }

    public BannedMembersAdapter getConversationsAdapter() {
        return bannedMembersAdapter;
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

    Observer<UIKitConstants.States> bannedGroupMemberStates = states -> {
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
                            bannedMembersViewModel.fetchGroupMember();
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
            bannedMembersAdapter.notifyItemInserted(integer);
            scrollToTop();
        }
    };

    Observer<Integer> moveToTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            bannedMembersAdapter.notifyItemMoved(integer, 0);
            bannedMembersAdapter.notifyItemChanged(0);
            scrollToTop();
        }
    };

    private void scrollToTop() {
        if (layoutManager.findFirstVisibleItemPosition() < 5) layoutManager.scrollToPosition(0);
    }

    Observer<Integer> update = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            bannedMembersAdapter.notifyItemChanged(integer);
        }
    };

    Observer<Integer> remove = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            bannedMembersAdapter.notifyItemRemoved(integer);
        }
    };
    Observer<CometChatException> exceptionObserver = exception -> {
        if (onError != null) onError.onError(context, exception);
    };


    /**
     * This method helps to get Click events of CometChatGroupMemberList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener<GroupMember> onItemClickListener) {
        if (onItemClickListener != null) this.onItemClickListener = onItemClickListener;
    }

    /**
     * Selects a group member in the banned members list based on the selection mode.
     *
     * @param groupMember The group member to be selected.
     * @param mode        The selection mode to be applied.
     */
    public void selectGroupMember(GroupMember groupMember, UIKitConstants.SelectionMode mode) {
        if (mode != null) {
            this.selectionMode = mode;
            if (UIKitConstants.SelectionMode.SINGLE.equals(selectionMode)) {
                hashMap.clear();
                hashMap.put(groupMember, true);
                bannedMembersAdapter.selectGroupMember(hashMap);
            } else if (UIKitConstants.SelectionMode.MULTIPLE.equals(selectionMode)) {
                if (hashMap.containsKey(groupMember)) hashMap.remove(groupMember);
                else hashMap.put(groupMember, true);
                bannedMembersAdapter.selectGroupMember(hashMap);
            }
        }
    }

    Observer<List<GroupMember>> listObserver = groupMembers -> bannedMembersAdapter.setGroupMemberList(groupMembers);

    /**
     * Sets the error handler for handling errors.
     *
     * @param onError The error handler to be set.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public interface OnSelection {
        void onSelection(List<GroupMember> groupMemberList);
    }
}
