package com.cometchat.chatuikit.groups;

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

import com.cometchat.chatuikit.shared.Interfaces.Function2;
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
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * CometChatGroups is a custom view that displays a list of groups.
 * <p>
 * It extends the CometChatListBase class and provides additional functionality
 * specific to group lists.
 * <br>
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *     <com.cometchat.chatuikit.groups.CometChatGroups
 *         android:id="@+id/group"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 *  </LinearLayout>
 *  }
 *  </pre>
 */
public class CometChatGroups extends CometChatListBase {
    private Context context;

    private View view;

    private static final String TAG = CometChatGroups.class.getName();

    private GroupsViewModel groupsViewModel;

    private GroupsAdapter groupsAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private LinearLayout loadingLayout;

    private LinearLayout noGroupView;

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

    private OnItemClickListener<Group> onItemClickListener;

    private OnSelection onSelection;

    private Palette palette;

    private Typography typography;

    private UIKitConstants.SelectionMode selectionMode = UIKitConstants.SelectionMode.NONE;

    private HashMap<Group, Boolean> hashMap = new HashMap<>();

    private Function2<Context, Group, List<CometChatOption>> options;

    private RecyclerViewSwipeListener swipeHelper;

    ImageView icon;
    private @DrawableRes int submitIcon;

    private ImageView loadingIcon;
    private OnError onError;

    /**
     * Constructs a new CometChatGroups instance with the given context.
     *
     * @param context The context of the calling component.
     */
    public CometChatGroups(Context context) {
        super(context);
        init(context, null, -1);
    }

    /**
     * Constructs a new CometChatGroups instance with the given context and attributes.
     *
     * @param context The context of the calling component.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public CometChatGroups(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatGroups instance with the given context, attributes, and style.
     *
     * @param context      The context of the calling component.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource
     *                     that supplies default values for the view. Can be 0 to not look for defaults.
     */
    public CometChatGroups(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the CometChatGroups view.
     *
     * @param context      The context of the calling component.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource
     *                     that supplies default values for the view. Can be 0 to not look for defaults.
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        view = View.inflate(context, R.layout.cometchat_list_view, null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CometChatGroups, 0, 0);

        //Start of Handling Attributes
        String title = a.getString(R.styleable.CometChatGroups_title) != null ? a.getString(R.styleable.CometChatGroups_title) : getResources().getString(R.string.groups);
        String searchHint = a.getString(R.styleable.CometChatGroups_searchPlaceholder);

        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatGroups_backButtonIcon);

        boolean showBackButton = a.getBoolean(R.styleable.CometChatGroups_showBackButton, false);
        boolean searchBoxVisible = a.getBoolean(R.styleable.CometChatGroups_hideSearch, false);

        float searchBoxRadius = a.getDimension(R.styleable.CometChatGroups_searchCornerRadius, 0f);

        int titleColor = a.getColor(R.styleable.CometChatGroups_titleColor, palette.getAccent());
        int searchBoxColor = a.getColor(R.styleable.CometChatGroups_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.CometChatGroups_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.CometChatGroups_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.CometChatGroups_searchBorderColor, 0);
        int backgroundColor = a.getColor(R.styleable.CometChatGroups_backgroundColor, palette.getBackground());

        customLayout = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noGroupView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        groupsAdapter = new GroupsAdapter(context);
        recyclerView.setAdapter(groupsAdapter);
        groupsViewModel = ViewModelProviders.of((FragmentActivity) context).get(GroupsViewModel.class);
        groupsViewModel.getMutableGroupsList().observe((AppCompatActivity) context, ListObserver);
        groupsViewModel.getStates().observe((AppCompatActivity) context, groupStates);
        groupsViewModel.insertAtTop().observe((AppCompatActivity) context, insertAtTop);
        groupsViewModel.moveToTop().observe((AppCompatActivity) context, moveToTop);
        groupsViewModel.updateGroup().observe((AppCompatActivity) context, update);
        groupsViewModel.removeGroup().observe((AppCompatActivity) context, remove);
        groupsViewModel.getCometChatException().observe((AppCompatActivity) context, exceptionObserver);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    groupsViewModel.fetchGroup();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }
        });
        clickEvents();
        super.setSearchPlaceholderText(searchHint);
        super.setTitle(title);
        super.backIcon(backButtonIcon);
        super.showBackButton(showBackButton);
        super.setSearchTextAppearance(typography.getText1());
        super.setTitleAppearance(typography.getHeading());
        emptyStateTextAppearance(typography.getHeading());
        emptyStateText(context.getResources().getString(R.string.no_groups));
        errorStateTextAppearance(typography.getText1());
        emptyStateTextColor(palette.getAccent400());
        setLoadingIconTintColor(palette.getAccent());
        super.setTitleColor(titleColor);
        super.setSearchTextColor(searchTextColor);
        super.setSearchBorderColor(searchBorderColor);
        super.setSearchBorderWidth(searchBorderWidth);
        super.setSearchPlaceHolderColor(searchTextColor);
        super.setSearchBackground(searchBoxColor);
        super.hideSearch(searchBoxVisible);
        super.setSearchCornerRadius(searchBoxRadius);
        if (palette.getGradientBackground() != null) setBackground(palette.getGradientBackground());
        else setBackgroundColor(backgroundColor);

        super.addListView(view);

        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                Group group = groupsAdapter.getGroup(viewHolder.getLayoutPosition());
                getOption(group, underlayButtons);
            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);

        super.addOnSearchListener((state, text) -> {
            if (state.equals(SearchState.TextChange)) {
                if (text.length() == 0) {
                    groupsViewModel.searchGroups(null);
                } else {
                    groupsViewModel.searchGroups(text);
                }
            }
        });
    }

    private void getOption(Group group, List<RecyclerViewSwipeListener.UnderlayButton> buttons) {
        List<CometChatOption> optionsArrayList;
        if (options != null) {
            optionsArrayList = options.apply(context, group);
            for (int i = 0; i < optionsArrayList.size(); i++) {
                CometChatOption cometChatOption = optionsArrayList.get(i);
                buttons.add(new RecyclerViewSwipeListener.UnderlayButton(cometChatOption.getTitle(), Utils.drawableToBitmap(getResources().getDrawable(cometChatOption.getIcon())), cometChatOption.getBackgroundColor(), pos -> {
                    if (cometChatOption.getClick() != null) {
                        cometChatOption.getClick().onClick();
                    }
                }));
            }
        }
    }

    /**
     * Retrieves the selected groups from the group list.
     *
     * @return A list of selected Group objects.
     */
    public List<Group> getSelectedGroups() {
        List<Group> groupList = new ArrayList<>();
        for (HashMap.Entry<Group, Boolean> entry : hashMap.entrySet()) {
            groupList.add(entry.getKey());
        }
        return groupList;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        groupsViewModel.addListeners();
        groupsViewModel.fetchGroup();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        groupsViewModel.removeListeners();
    }

    /**
     * Sets the options for the group list.
     *
     * @param options The function that provides the options for each group.
     */
    public void setOptions(Function2<Context, Group, List<CometChatOption>> options) {
        this.options = options;
    }

    /**
     * Sets the empty state text for the group list.
     *
     * @param message The text to be displayed in the empty state view.
     */
    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.groups));
    }

    /**
     * Hides or shows the separator in the group list.
     *
     * @param hide True to hide the separator, false to show it.
     */
    public void hideSeparator(boolean hide) {
        groupsAdapter.hideSeparator(hide);
    }

    /**
     * Sets the text color of the empty state text in the group list.
     *
     * @param color The color value to set for the empty state text.
     */
    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    /**
     * Sets the font for the empty state text in the group list.
     *
     * @param font The name of the font file to use for the empty state text.
     */
    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance for the empty state text in the group list.
     *
     * @param appearance The resource ID of the text appearance to apply to the empty state text.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text appearance for the error state text in the group list.
     *
     * @param appearance The resource ID of the text appearance to apply to the error state text.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the text color for the error state text in the group list.
     *
     * @param errorMessageColor The color value to set for the error state text.
     */
    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error state text for the group list.
     *
     * @param errorText The text to be displayed in the error state view.
     */
    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    /**
     * Sets the layout resource for the empty state view in the group list.
     *
     * @param id The resource ID of the layout to be used as the empty state view.
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
     * Sets the tint color for the loading icon in the group list.
     *
     * @param color The color value to set for the loading icon.
     */
    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the layout resource for the error state view in the group list.
     *
     * @param id The resource ID of the layout to be used as the error state view.
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
     * Sets the layout resource for the loading state view in the group list.
     *
     * @param id The resource ID of the layout to be used as the loading state view.
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
     * Sets the background color for the group list.
     *
     * @param colorArray  An array of color values to create a gradient background.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    /**
     * Sets the subtitle view for each group in the group list.
     *
     * @param subtitle The function that provides the subtitle view for each group.
     */
    public void setSubtitle(Function2<Context, Group, View> subtitle) {
        groupsAdapter.setSubtitle(subtitle);
    }

    /**
     * Sets the tail view for each group in the group list.
     *
     * @param tail The function that provides the tail view for each group.
     */
    public void setTail(Function2<Context, Group, View> tail) {
        groupsAdapter.setTailView(tail);
    }

    /**
     * Sets the custom view for each item in the group list.
     *
     * @param listItemView The function that provides the custom view for each item.
     */
    public void setListItemView(Function2<Context, Group, View> listItemView) {
        groupsAdapter.setCustomView(listItemView);
    }

    /**
     * Sets the style for the group list.
     *
     * @param style The style configuration for the group list.
     */
    public void setStyle(GroupsStyle style) {
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

            groupsAdapter.setSeparatorColor(style.getSeparatorColor());
            groupsAdapter.setSubTitleTextAppearance(style.getSubTitleTextAppearance());
            groupsAdapter.setSubTitleTextColor(style.getSubTitleTextColor());
            groupsAdapter.setSubTitleTextFont(style.getSubTitleTextFont());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the avatar style for the group list.
     *
     * @param style The avatar style configuration for the group list.
     */
    public void setAvatarStyle(AvatarStyle style) {
        groupsAdapter.setAvatarStyle(style);
    }

    /**
     * Sets the status indicator style for the group list.
     *
     * @param style The status indicator style configuration for the group list.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        groupsAdapter.setStatusIndicatorStyle(style);
    }

    /**
     * Sets the list item style for the group list.
     *
     * @param style The list item style configuration for the group list.
     */
    public void setListItemStyle(ListItemStyle style) {
        groupsAdapter.setListItemStyle(style);
    }

    /**
     * Sets the selection listener for the group list.
     *
     * @param onSelection The selection listener to be set for the group list.
     */
    public void setOnSelection(OnSelection onSelection) {
        this.onSelection = onSelection;
    }

    /**
     * Sets the request builder for fetching groups in the group list.
     *
     * @param groupsRequestBuilder The GroupsRequestBuilder object to be set as the request builder.
     */
    public void setGroupsRequestBuilder(GroupsRequest.GroupsRequestBuilder groupsRequestBuilder) {
        groupsViewModel.setGroupsRequestBuilder(groupsRequestBuilder);
    }

    /**
     * Sets the icon resource for the protected group.
     *
     * @param protectedGroupIcon The protected group icon resource to be set.
     */
    public void setPasswordGroupIcon(@DrawableRes int protectedGroupIcon) {
        groupsAdapter.setProtectedGroupIcon(protectedGroupIcon);
    }

    /**
     * Sets the icon resource for the private group.
     *
     * @param privateGroupIcon The private group icon resource to be set.
     */
    public void setPrivateGroupIcon(@DrawableRes int privateGroupIcon) {
        groupsAdapter.setPrivateGroupIcon(privateGroupIcon);
    }

    /**
     * Sets the request builder for searching groups in the group list.
     *
     * @param groupsRequestBuilder The GroupsRequestBuilder object to be set as the search request builder.
     */
    public void setSearchRequestBuilder(GroupsRequest.GroupsRequestBuilder groupsRequestBuilder) {
        groupsViewModel.setSearchRequestBuilder(groupsRequestBuilder);
    }

    /**
     * Sets the selection mode for the group list.
     *
     * @param selectionMode The selection mode to set for the group list.
     */
    public void setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        hashMap.clear();
        groupsAdapter.selectGroup(hashMap);
        this.selectionMode = selectionMode;
        if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode) && selectionMode != null) {
            setMenuIcon(true);
        }
    }

    /**
     * Sets the icon for the submit action in the group list.
     *
     * @param submitIcon The resource ID of the icon to set for the submit action.
     */
    public void setSubmitIcon(@DrawableRes int submitIcon) {
        if (submitIcon != 0 && icon != null) {
            icon.setImageResource(submitIcon);
        }
        this.submitIcon = submitIcon;
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
                        onSelection.onSelection(getSelectedGroups());
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

    public GroupsViewModel getViewModel() {
        return groupsViewModel;
    }

    public GroupsAdapter getConversationsAdapter() {
        return groupsAdapter;
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void clickEvents() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Group group = (Group) view.getTag(R.string.group);
                if (UIKitConstants.SelectionMode.NONE.equals(selectionMode)) {
                } else {
                    selectGroup(group, selectionMode);
                }
                if (onItemClickListener != null) onItemClickListener.OnItemClick(group, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                Group group = (Group) view.getTag(R.string.group);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(group, position);
            }
        }));

    }

    Observer<UIKitConstants.States> groupStates = states -> {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            noGroupView.setVisibility(View.GONE);
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
                noGroupView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            noGroupView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            customLayout.setVisibility(GONE);
        }
    };

    /**
     * Hides or shows the error state view in the group list.
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
                            groupsViewModel.fetchGroup();
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
            groupsAdapter.notifyItemInserted(integer);
            scrollToTop();
        }
    };

    Observer<Integer> moveToTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupsAdapter.notifyItemMoved(integer, 0);
            groupsAdapter.notifyItemChanged(0);
            scrollToTop();
        }
    };

    private void scrollToTop() {
        if (layoutManager.findFirstVisibleItemPosition() < 5) layoutManager.scrollToPosition(0);
    }

    Observer<Integer> update = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupsAdapter.notifyItemChanged(integer);
        }
    };

    Observer<Integer> remove = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            groupsAdapter.notifyItemRemoved(integer);
        }
    };
    Observer<CometChatException> exceptionObserver = exception -> {
        if (onError != null) onError.onError(context, exception);
    };

    /**
     * Sets the callback for handling errors in the group list.
     *
     * @param onError The callback to be invoked when an error occurs.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public OnError getOnError() {
        return onError;
    }

    /**
     * Sets the click listener for the group list items.
     *
     * @param onItemClickListener The click listener to be set for the group list items.
     */
    public void setItemClickListener(OnItemClickListener<Group> onItemClickListener) {
        if (onItemClickListener != null) this.onItemClickListener = onItemClickListener;
    }

    /**
     * Selects or deselects a group in the group list based on the given Group object and selection mode.
     *
     * @param group The Group object to be selected or deselected.
     * @param mode  The selection mode to apply for the group list.
     */
    public void selectGroup(Group group, UIKitConstants.SelectionMode mode) {
        if (mode != null) {
            this.selectionMode = mode;
            if (UIKitConstants.SelectionMode.SINGLE.equals(selectionMode)) {
                hashMap.clear();
                hashMap.put(group, true);
                groupsAdapter.selectGroup(hashMap);
            } else if (UIKitConstants.SelectionMode.MULTIPLE.equals(selectionMode)) {
                if (hashMap.containsKey(group)) hashMap.remove(group);
                else hashMap.put(group, true);
                groupsAdapter.selectGroup(hashMap);
            }
        }
    }

    Observer<List<Group>> ListObserver = groups -> {
        groupsAdapter.setGroupList(groups);
    };

    public boolean isHideError() {
        return hideError;
    }

    public LinearLayout getCustomLayout() {
        return customLayout;
    }

    public TextView getEmptyStateText() {
        return emptyStateText;
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

    public interface OnSelection {
        void onSelection(List<Group> groupList);
    }
}
