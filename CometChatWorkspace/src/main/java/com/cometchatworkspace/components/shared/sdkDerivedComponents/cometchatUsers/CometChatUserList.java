package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatUsers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.components.users.CometChatUserEvents;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


/**
 * Purpose - CometChatUserList class is a subclass of cardview and used as component by
 * developer to display list of users. Developer just need to fetchuser at their end
 * and pass it to this component to display list of user. It helps user to create user
 * list easily and saves their time.
 *
 * @see User
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 23rd March 2022
 */

public class CometChatUserList extends MaterialCardView {

    private final Context context;

    private CometChatUserListViewModel cometChatUserListViewModel;


    private UsersRequest usersRequest;    //Uses to fetch Users.

    private final List<User> userList = new ArrayList<>();

    private boolean showHeader = true;

    private boolean isFriendOnly;

    private boolean hideBlockedUsers;
    private boolean hideError;

    private String searchKeyword = "";

    private String status = null;

    private int limit;

    private List<String> tags = new ArrayList<>();

    private List<String> roles = new ArrayList<>();
    private List<String> uidS = new ArrayList<>();

    private RecyclerView recyclerView;

    private ShimmerFrameLayout userShimmer;

    private LinearLayout noUserView; // Used to display a information when no users are fetched.

    private View view;

    private View emptyView = null;

    private CometChatSoundManager soundManager;


    private LinearLayout custom_layout;

    private AttributeSet attrs;

    private TextView noListText;

    private FontUtils fontUtils;
    private static final String TAG = "CometChatUserList";
    private String errorMessageFont = null;
    private int errorMessageColor = 0;
    private String error_text = null;
    private String empty_text = null;
    private View errorView = null;
    private Typography typography;
    private Palette palette;
    private OnItemClickListener<User> onItemClickListener;

    public CometChatUserList(@NonNull Context context) {
        super(context);
        this.context = context;
        initComponentView();
    }

    public CometChatUserList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initComponentView();
    }

    public CometChatUserList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.attrs = attrs;
        initComponentView();
    }

    private void setViewModel() {
        if (cometChatUserListViewModel == null)
            cometChatUserListViewModel = new CometChatUserListViewModel(context, this, showHeader);

        cometChatUserListViewModel.setHeaderColor(palette.getAccent500());
        cometChatUserListViewModel.setHeaderTextAppearance(typography.getText2());
    }

    private void initComponentView() {
        view = View.inflate(context, R.layout.cometchat_list, null);
        fontUtils = FontUtils.getInstance(context);
        typography = Typography.getInstance();
        palette = Palette.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatUserList,
                0, 0);

        isFriendOnly = a.getBoolean(R.styleable.CometChatUserList_friendsonly, false);
        hideError = a.getBoolean(R.styleable.CometChatUserList_hideError, false);
        hideBlockedUsers = a.getBoolean(R.styleable.CometChatUserList_hideBlockedUsers, false);
        searchKeyword = a.getString(R.styleable.CometChatUserList_searchKeyword);
        status = a.getString(R.styleable.CometChatUserList_status);
        limit = a.getInt(R.styleable.CometChatUserList_limit, 50);
        empty_text = a.getString(R.styleable.CometChatUserList_empty_text);
        error_text = a.getString(R.styleable.CometChatUserList_error_text);

        recyclerView = view.findViewById(R.id.list_recyclerview);
        userShimmer = view.findViewById(R.id.shimmer_layout);
        noUserView = view.findViewById(R.id.no_list_view);
        noListText = view.findViewById(R.id.no_list_text);
        custom_layout = view.findViewById(R.id.empty_view);

        emptyStateText(empty_text);
        addView(view);
        setViewModel();
        // Uses to fetch next list of users if rvuserList (RecyclerView) is scrolled in upward direction.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    makeUserList();
                }

            }
        });
        clickEvent();
    }

    public void emptyStateText(String message) {
        if (message != null)
            noListText.setText(message);
        else
            noListText.setText(getResources().getString(R.string.no_user));

    }

    public void setLimit(int limit) {
        this.limit = limit;
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
            emptyStateTextFont(style.getEmptyStateTextFont());
        }
        if (style.getEmptyStateTextColor() != 0) {
            emptyStateTextColor(style.getEmptyStateTextColor());
        }
        if (style.getErrorStateTextFont() != null) {
            errorMessageFont = style.getErrorStateTextFont();
        }
        if (style.getErrorStateTextColor() != 0) {
            errorMessageColor = style.getErrorStateTextColor();
        }
    }

    public void emptyStateTextColor(int color) {
        if (color != 0)
            noListText.setTextColor(color);
    }

    public void emptyStateTextFont(String font) {
        if (font != null)
            noListText.setTypeface(fontUtils.getTypeFace(font));
    }

    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0)
            noListText.setTextAppearance(context, appearance);

    }

    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(
                orientation,
                colorArray);
        setBackground(gd);
    }

    public void showHeader(boolean isVisible) {
        showHeader = isVisible;
        if (!showHeader)
            recyclerView.removeItemDecorationAt(0);
    }

    /**
     * @setEmptyView is method allows you to set layout show when the list is empty
     * if user want to set Empty layout other wise it will load default layout
     */
    public void setEmptyView(int id) {
        try {
            emptyView = View.inflate(context, id, null);
        } catch (Exception e) {
            emptyView = null;
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method set the fetched list into the CometChatUserList Component.
     *
     * @param userList to set into the view CometChatUserList
     */
    public void setUserList(List<User> userList) {
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.setUsersList(userList);
    }

    /**
     * This methods updates the user item or add if not present in the list
     *
     * @param user to be added or updated
     */
    public void update(User user) {
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.update(user);
    }

    public void add(User user) {
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.add(user);
    }

    /**
     * provide way to remove a particular user from the list
     *
     * @param user to be removed
     */
    public void remove(User user) {
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.remove(user);
    }


    /**
     * This method helps to get Click events of CometChatUserList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener<User> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private void clickEvent() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                User user = (User) var1.getTag(R.string.user);
                for (CometChatUserEvents e : CometChatUserEvents.userEvents.values()) {
                    e.onItemClick(user, var2);
                }
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(user, var2);

            }

            @Override
            public void onLongClick(View var1, int var2) {
                User user = (User) var1.getTag(R.string.user);
                for (CometChatUserEvents e : CometChatUserEvents.userEvents.values()) {
                    e.onItemLongClick(user, var2);
                }
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(user, var2);
            }
        }));
    }

    /**
     * This method is used to perform search operation in a list of users.
     *
     * @param searchString is a String object which will be searched in user.
     * @see CometChatUserListViewModel#searchUserList(List)
     */
    public void searchUser(String searchString) {
        cometChatUserListViewModel.clear();
        usersRequest = new UsersRequest.UsersRequestBuilder().setLimit(limit).setUIDs(uidS)
                .friendsOnly(isFriendOnly).hideBlockedUsers(hideBlockedUsers).setSearchKeyword(searchString).setUserStatus(getStatus(status)).setTags(tags).setRoles(roles).build();
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                cometChatUserListViewModel.setUsersList(users);
                checkNoUser();
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError();
                e.printStackTrace();
            }
        });
    }


    public User getUser(int position) {
        User user = null;
        if (cometChatUserListViewModel != null)
            user = cometChatUserListViewModel.getUser(position);
        return user;
    }

    public void setSelectedUser(User user) {
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.setSelectedUser(user);
    }

    /**
     * This method is used to clear a list of user present in CometChatUserList Component and fetch
     * new list.
     *
     * @see CometChatUserListViewModel#clear()
     */
    public void refreshList() {
        userList.clear();
        usersRequest = null;
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.clear();
        makeUserList();
    }

    public void clearList() {
        userList.clear();
        if (cometChatUserListViewModel != null)
            cometChatUserListViewModel.clear();
    }

    public int size() {
        return cometChatUserListViewModel.size();
    }


    /**
     * This method is used to retrieve list of users you have done.
     * For more detail please visit our official documentation {@link "https://prodocs.cometchat.com/docs/android-messaging-retrieve-users" }
     *
     * @see UsersRequest
     */

    private void makeUserList() {

        if (usersRequest == null) {
            usersRequest = new UsersRequest.UsersRequestBuilder().setLimit(limit).setUIDs(uidS)
                    .friendsOnly(isFriendOnly).hideBlockedUsers(hideBlockedUsers)
                    .setSearchKeyword(searchKeyword).setUserStatus(getStatus(status))
                    .setTags(tags).setRoles(roles).build();
        }
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                userList.addAll(users);
                if (userList.size() != 0) {
                    stopHideShimmer();
                    noUserView.setVisibility(View.GONE);
                    custom_layout.setVisibility(GONE);
                    setUserList(users);
                } else {
                    checkNoUser();
                }
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                stopHideShimmer();
                hideError();
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void throwError(CometChatException e) {
        for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
            events.onError(e);
        }
    }

    public void errorStateTextFont(String errorMessageFont) {
        this.errorMessageFont = errorMessageFont;
    }

    public void errorStateTextColor(int errorMessageColor) {
        this.errorMessageColor = errorMessageColor;
    }

    public void errorStateText(String error_text) {
        this.error_text = error_text;
    }

    /**
     * to check is user have hidden the error or not and accordingly
     * handle error
     */
    private void hideError() {
        String error_message;
        if (error_text != null)
            error_message = error_text;
        else
            error_message = getContext().getString(R.string.error_cant_load_user);

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
                                makeUserList();
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                alertDialog.dismiss();
                            }
                        }
                    }, 0, false);
                }
            }
        }
    }

    /**
     * filter user based on there status  online/offline
     */
    private String getStatus(String status) {
        return status != null ?
                status.equalsIgnoreCase("online") ?
                        UsersRequest.USER_STATUS_ONLINE :
                        status.equalsIgnoreCase("offline") ?
                                UsersRequest.USER_STATUS_OFFLINE : status
                : "";
    }

    /**
     * This method is used to hide shimmer effect if the list is loaded.
     */
    private void stopHideShimmer() {
        userShimmer.stopShimmer();
        userShimmer.setVisibility(View.GONE);
    }

    private void checkNoUser() {
        if (size() == 0) {
            stopHideShimmer();
            if (emptyView != null) {
                custom_layout.setVisibility(VISIBLE);
                custom_layout.removeAllViews();
                custom_layout.addView(emptyView);
            } else {
                noUserView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else {
            noUserView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            custom_layout.setVisibility(GONE);
        }
    }

    /**
     * This method is used to refresh the user List shown in CometChatUserList.
     * It clears the CometChatUserList and fetches the fresh list of user and set it
     * in CometChatUserList. Also same user list is returned in callback.
     *
     * @param callbackListener - It is object of CallBackListener which returns two events
     *                         <code>OnSuccess(List<user> userList)</code>
     *                         <code>OnError(CometChatException e)</code>
     * @see UsersRequest
     */
    public void refreshUser(CometChat.CallbackListener callbackListener) {
        refreshList();
        userList.clear();
        usersRequest = null;
        usersRequest = new UsersRequest.UsersRequestBuilder().setLimit(limit)
                .friendsOnly(isFriendOnly).hideBlockedUsers(hideBlockedUsers).setSearchKeyword(searchKeyword).setUserStatus(status).setTags(tags).setRoles(roles).build();
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                userList.addAll(users);
                if (userList.size() != 0) {
                    stopHideShimmer();
                    setUserList(users);
                } else {
                    checkNoUser();
                }
                callbackListener.onSuccess(userList);
            }

            @Override
            public void onError(CometChatException e) {
                stopHideShimmer();
                Log.d(TAG, "onError: " + e.getMessage());
                callbackListener.onError(e);
                throwError(e);
                hideError();
            }
        });
    }

    public RecyclerView getRecyclerView() {
        if (recyclerView != null)
            return recyclerView;
        return null;
    }

    /**
     * This method has message listener which recieve real time message and based on these messages, users are updated.
     *
     * @see CometChat#addMessageListener(String, CometChat.MessageListener)
     */
    private void adduserListener() {
        CometChat.addUserListener(TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                if (cometChatUserListViewModel != null)
                    cometChatUserListViewModel.update(user);
            }

            @Override
            public void onUserOffline(User user) {
                if (cometChatUserListViewModel != null)
                    cometChatUserListViewModel.update(user);
            }
        });
    }


    /**
     * This method is used to remove the userlistener.
     */
    public void removeUserListener() {
        CometChat.removeUserListener(TAG);
    }

    public void isFriendsOnly(boolean isTrue) {
        this.isFriendOnly = isTrue;
    }

    public void isHideBlockUsers(boolean isTrue) {
        this.hideBlockedUsers = isTrue;
    }

    public boolean isBlockedUsersHidden() {
        return hideBlockedUsers;
    }

    public void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public void setStatus(@CometChatStatusIndicator.STATUS String status) {
        this.status = status;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;

    }

    public void setUidS(List<String> uidS) {
        this.uidS = uidS;
    }

    private void onPause() {
        removeUserListener();
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

    /**
     * This method is used to handle onResume state of CometChatUserList
     *
     * @author CometChat Team
     * @copyright © 2021 CometChat Inc.
     * @see CometChatUserList
     */
    private void onResume() {
        usersRequest = null;
        cometChatUserListViewModel.clear();
        makeUserList();
        adduserListener();
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        cometChatUserListViewModel.setConfiguration(configuration);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        cometChatUserListViewModel.setConfiguration(configurations);
    }
}
