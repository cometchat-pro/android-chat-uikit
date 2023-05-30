package com.cometchat.chatuikit.users;

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
import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderDecoration;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * CometChatUsers is a custom view that displays a list of CometChat users.
 * It extends the CometChatListBase class and provides functionality to fetch and display users,
 * handle user selections, and customize the appearance of the list.
 * <br>
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *     <com.cometchat.chatuikit.users.CometChatUsers
 *         android:id="@+id/users"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 *  </LinearLayout>
 *  }
 *  </pre>
 */
public class CometChatUsers extends CometChatListBase {
    private Context context;

    private View view;

    private static final String TAG = CometChatUsers.class.getName();

    private UsersViewModel usersViewModel;

    private UsersAdapter usersAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private LinearLayout loadingLayout;

    private LinearLayout noUserView;

    public boolean hideError;

    private View emptyView = null;

    public LinearLayout customLayout;

    private TextView emptyStateText;

    public int errorStateTextAppearance = 0;

    public int errorMessageColor = 0;

    public String errorText = null;

    public View errorView = null;

    private View loadingView = null;

    private View menuView = null;

    private OnItemClickListener<User> onItemClickListener;

    private OnError onError;

    private OnSelection onSelection;

    private Palette palette;

    private Typography typography;

    private UIKitConstants.SelectionMode selectionMode = UIKitConstants.SelectionMode.NONE;

    private HashMap<User, Boolean> hashMap = new HashMap<>();

    private Function2<Context, User, List<CometChatOption>> options;

    private RecyclerViewSwipeListener swipeHelper;

    private ImageView loadingIcon;

    private StickyHeaderDecoration stickyHeaderDecoration;

    private @DrawableRes int submitIcon;

    private ImageView icon;

    public CometChatUsers(Context context) {
        super(context);
        init(context, null, -1);
    }

    public CometChatUsers(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CometChatUsers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        usersViewModel = new UsersViewModel();
        view = View.inflate(context, R.layout.cometchat_list_view, null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Users, 0, 0);

        //Start of Handling Attributes
        String title = a.getString(R.styleable.Users_title) != null ? a.getString(R.styleable.Users_title) : getResources().getString(R.string.users);

        int titleColor = a.getColor(R.styleable.Users_titleColor, palette.getAccent());

        Drawable backButtonIcon = a.getDrawable(R.styleable.Users_backButtonIcon);

        boolean showBackButton = a.getBoolean(R.styleable.Users_showBackButton, false);
        boolean searchBoxVisible = a.getBoolean(R.styleable.Users_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.Users_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.Users_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.Users_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.Users_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.Users_searchBorderColor, 0);
        String searchHint = a.getString(R.styleable.Users_searchHint);
        int backgroundColor = a.getColor(R.styleable.Users_backgroundColor, palette.getBackground());

        customLayout = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noUserView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        usersAdapter = new UsersAdapter(context);
        recyclerView.setAdapter(usersAdapter);
        usersViewModel = ViewModelProviders.of((FragmentActivity) context).get(usersViewModel.getClass());
        usersViewModel.getMutableUsersList().observe((AppCompatActivity) context, ListObserver);
        usersViewModel.getStates().observe((AppCompatActivity) context, userStates);
        stickyHeaderDecoration = new StickyHeaderDecoration(usersAdapter);
        recyclerView.addItemDecoration(stickyHeaderDecoration, 0);

        usersViewModel.insertAtTop().observe((AppCompatActivity) context, insertAtTop);
        usersViewModel.moveToTop().observe((AppCompatActivity) context, moveToTop);
        usersViewModel.updateUser().observe((AppCompatActivity) context, update);
        usersViewModel.removeUser().observe((AppCompatActivity) context, remove);
        usersViewModel.getCometChatException().observe((AppCompatActivity) context, exceptionObserver);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    usersViewModel.fetchUser();
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
        emptyStateText(context.getResources().getString(R.string.no_user));
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

                User User = usersAdapter.getUser(viewHolder.getLayoutPosition());
                getOption(User, underlayButtons);

            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);

        super.addOnSearchListener((state, text) -> {
            if (state.equals(SearchState.TextChange)) {
                if (text.length() == 0) {
                    usersViewModel.searchUsers(null);
                } else {
                    usersViewModel.searchUsers(text);
                }
            }
        });
    }

    /**
     * Sets the error listener for handling errors that occur in the CometChatUsers class.
     *
     * @param onError The error listener to be set.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    private void getOption(User user, List<RecyclerViewSwipeListener.UnderlayButton> buttons) {
        List<CometChatOption> optionsArrayList;
        if (options != null) {
            optionsArrayList = options.apply(context, user);
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
     * Retrieves a list of selected users from the HashMap of users.
     *
     * @return The list of selected users.
     */
    public List<User> getSelectedUsers() {
        List<User> userList = new ArrayList<>();
        for (HashMap.Entry<User, Boolean> entry : hashMap.entrySet()) {
            userList.add(entry.getKey());
        }
        return userList;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        usersViewModel.addListeners();
        usersViewModel.fetchUser();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        usersViewModel.removeListeners();
    }

    /**
     * Sets the options for displaying CometChat options in the context menu for a user.
     *
     * @param options The function that provides the list of CometChat options.
     */
    public void setOptions(Function2<Context, User, List<CometChatOption>> options) {
        this.options = options;
    }

    /**
     * Disables or enables the presence status of users .
     *
     * @param disable {@code true} to disable the presence status, {@code false} to enable it.
     */
    public void disableUsersPresence(boolean disable) {
        usersAdapter.setDisableUsersPresence(disable);
    }

    /**
     * Sets the empty state text for displaying a message when there are no users.
     *
     * @param message The message to be displayed in the empty state.
     */
    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.no_user));

    }

    /**
     * Sets the text color of the empty state message.
     *
     * @param color The color to be set for the empty state text.
     */
    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    /**
     * Sets the font for the empty state text.
     *
     * @param font The font name or path to be set for the empty state text.
     */
    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance of the empty state text.
     *
     * @param appearance The style resource ID for the text appearance of the empty state text.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text appearance for the error state text.
     *
     * @param appearance The style resource ID for the text appearance of the error state text.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the text color for the error state message.
     *
     * @param errorMessageColor The color to be set for the error state text.
     */
    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error state text.
     *
     * @param errorText The error state text to be set.
     */
    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    /**
     * Sets the empty state view layout resource.
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
     * Sets the tint color for the loading icon.
     *
     * @param color The color to be set as the tint color for the loading icon.
     */
    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * setErrorStateView is method allows you to set layout, show when there is a error
     * if user want to set Error layout other wise it will load default layout
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
     * Sets the loading state view layout resource.
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
     * Sets the background of the view with a gradient using the specified color array and orientation.
     *
     * @param colorArray  The array of colors to use for the gradient.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    /**
     * Sets the subtitle function for the user adapter.
     *
     * @param subtitle The function to generate the subtitle view for a user.
     */
    public void setSubtitle(Function2<Context, User, View> subtitle) {
        usersAdapter.setSubtitle(subtitle);
    }

    /**
     * Sets the tail function for the user adapter.
     *
     * @param tail The function to generate the tail view for a user.
     */
    public void setTail(Function2<Context, User, View> tail) {
        usersAdapter.setTailView(tail);
    }

    /**
     * Sets the custom list item view function for the user adapter.
     *
     * @param listItemView The function to generate the custom list item view for a user.
     */
    public void setListItemView(Function2<Context, User, View> listItemView) {
        usersAdapter.setCustomView(listItemView);
    }

    /**
     * Sets the style for the users view.
     *
     * @param style The style to apply to the users view.
     */
    public void setStyle(UsersStyle style) {
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

            usersAdapter.setOnlineStatusColor(style.getOnlineStatusColor());
            usersAdapter.setSeparatorColor(style.getSeparatorColor());
            usersAdapter.setSectionHeaderTextColor(style.getSectionHeaderTextColor());
            usersAdapter.setSectionHeaderTextAppearance(style.getSectionHeaderTextAppearance());

            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the avatar style for the user adapter.
     *
     * @param style The style to apply to the avatars in the user adapter.
     */
    public void setAvatarStyle(AvatarStyle style) {
        usersAdapter.setAvatarStyle(style);
    }

    /**
     * Sets the status indicator style for the user adapter.
     *
     * @param style The style to apply to the status indicators in the user adapter.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        usersAdapter.setStatusIndicatorStyle(style);
    }

    /**
     * Sets the list item style for the user adapter.
     *
     * @param style The style to apply to the list items in the user adapter.
     */
    public void setListItemStyle(ListItemStyle style) {
        usersAdapter.setListItemStyle(style);
    }

    /**
     * Sets the listener for user selection events.
     *
     * @param onSelection The listener to handle user selection events.
     */
    public void setOnSelection(OnSelection onSelection) {
        this.onSelection = onSelection;
    }

    /**
     * Sets the users request builder for the users view model.
     *
     * @param usersRequestBuilder The users request builder to set.
     */
    public void setUsersRequestBuilder(UsersRequest.UsersRequestBuilder usersRequestBuilder) {
        usersViewModel.setUsersRequestBuilder(usersRequestBuilder);
    }

    /**
     * Sets the search request builder for the users view model.
     *
     * @param usersRequestBuilder The search request builder to set.
     */
    public void setSearchRequestBuilder(UsersRequest.UsersRequestBuilder usersRequestBuilder) {
        usersViewModel.setSearchRequestBuilder(usersRequestBuilder);
    }

    /**
     * Sets the selection mode for the users view.
     *
     * @param selectionMode The selection mode to set.
     */
    public void setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        hashMap.clear();
        usersAdapter.selectUser(hashMap);
        this.selectionMode = selectionMode;
        if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode) && selectionMode != null) {
            setMenuIcon(true);
        }
    }

    /**
     * Sets the submit icon for the users view.
     *
     * @param submitIcon The resource ID of the submit icon.
     */
    public void setSubmitIcon(@DrawableRes int submitIcon) {
        if (submitIcon != 0 && icon != null) {
            icon.setImageResource(submitIcon);
        }
        this.submitIcon = submitIcon;
    }

    /**
     * Sets the selection icon for the users adapter.
     *
     * @param selectionIcon The resource ID of the selection icon.
     */
    public void setSelectionIcon(@DrawableRes int selectionIcon) {
        usersAdapter.setSelectionIcon(selectionIcon);
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
                        onSelection.onSelection(getSelectedUsers());
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

    public UsersViewModel getViewModel() {
        return usersViewModel;
    }

    public UsersAdapter getConversationsAdapter() {
        return usersAdapter;
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void clickEvents() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = (User) view.getTag(R.string.user);
                if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode)) {
                    selectUser(user, selectionMode);
                }
                if (onItemClickListener != null) onItemClickListener.OnItemClick(user, position);
            }

            @Override
            public void onLongClick(View view, int position) {
                User user = (User) view.getTag(R.string.user);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(user, position);
            }
        }));
    }

    /**
     * Hides or shows the section separator in the users view.
     *
     * @param hide {@code true} to hide the separator, {@code false} to show the separator.
     */
    public void hideSeparator(boolean hide) {
        if (hide) recyclerView.removeItemDecoration(stickyHeaderDecoration);
        else recyclerView.addItemDecoration(stickyHeaderDecoration, 0);
    }

    Observer<UIKitConstants.States> userStates = states -> {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            noUserView.setVisibility(View.GONE);
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
                noUserView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            noUserView.setVisibility(View.GONE);
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
                            usersViewModel.fetchUser();
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
            usersAdapter.notifyItemInserted(integer);
            scrollToTop();
        }
    };

    Observer<Integer> moveToTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            usersAdapter.notifyItemMoved(integer, 0);
            usersAdapter.notifyItemChanged(0);
            scrollToTop();
        }
    };

    private void scrollToTop() {
        if (layoutManager.findFirstVisibleItemPosition() < 5) layoutManager.scrollToPosition(0);
    }

    Observer<Integer> update = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            usersAdapter.notifyItemChanged(integer);
        }
    };

    Observer<Integer> remove = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            usersAdapter.notifyItemRemoved(integer);
        }
    };

    Observer<CometChatException> exceptionObserver = exception -> {
        if (onError != null) onError.onError(context, exception);
    };

    /**
     * This method helps to get Click events of CometChatUserList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener<User> onItemClickListener) {
        if (onItemClickListener != null) this.onItemClickListener = onItemClickListener;
    }

    /**
     * Selects a user in the users view with the specified selection mode.
     *
     * @param user The user to select.
     * @param mode The selection mode to apply.
     */
    public void selectUser(User user, UIKitConstants.SelectionMode mode) {
        if (mode != null) {
            this.selectionMode = mode;
            if (UIKitConstants.SelectionMode.SINGLE.equals(selectionMode)) {
                hashMap.clear();
                hashMap.put(user, true);
                usersAdapter.selectUser(hashMap);
            } else if (UIKitConstants.SelectionMode.MULTIPLE.equals(selectionMode)) {
                if (hashMap.containsKey(user)) hashMap.remove(user);
                else hashMap.put(user, true);
                usersAdapter.selectUser(hashMap);
            }
        }
    }

    Observer<List<User>> ListObserver = users -> usersAdapter.setUserList(users);

    public OnError getOnError() {
        return onError;
    }

    public interface OnSelection {
        void onSelection(List<User> userList);
    }
}
