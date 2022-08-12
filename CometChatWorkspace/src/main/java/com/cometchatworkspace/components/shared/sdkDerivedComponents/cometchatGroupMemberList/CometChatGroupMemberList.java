package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroupMemberList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;

import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CometChatGroupMemberList extends MaterialCardView {

    private Context context;

    private CometChatGroupMembersViewModel groupMembersViewModel;

    private View view;

    private static final String TAG = "CometChatGroupMemberList";

    private RecyclerView recyclerView;


    private ShimmerFrameLayout groupMemberListShimmer;
    private LinearLayout emptyStateView; // Used to display a information when no Groups are fetched.

    private GroupMembersRequest groupMembersRequest;

    private View emptyView = null;
    private View errorView = null;

    private LinearLayout custom_layout;
    private TextView noListText;

    private boolean isHideError;
    private boolean allowKickMember, allowBanMember;
    private FontUtils fontUtils;
    private onErrorCallBack onErrorCallBack;

    private  String errorMessageFont = null;
    private  int errorMessageColor = 0;
    private String error_text = null;
    private String empty_text = null;

    private List<String> scopes = new ArrayList<>();
    private String searchKeyword = null;
    private int limit;
    private String GUID;
    private final List<GroupMember> groupMembers = new ArrayList<>();
    private RecyclerViewSwipeListener swipeHelper;

    private Group group_;
    private final User loggedInUser = CometChat.getLoggedInUser();


    private GroupMember groupMember; // to get the swiped member

    public CometChatGroupMemberList(Context context) {
        super(context);
        initViewComponent(context, null, -1);

    }

    public CometChatGroupMemberList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);

    }

    public CometChatGroupMemberList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);

    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {

        this.context = context;
        view = View.inflate(context, R.layout.cometchat_list, null);
        fontUtils = FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatGroupMemberList,
                0, 0);

        recyclerView = view.findViewById(R.id.list_recyclerview);
        groupMemberListShimmer = view.findViewById(R.id.shimmer_layout);
        emptyStateView = view.findViewById(R.id.no_list_view);
        noListText = view.findViewById(R.id.no_list_text);
        custom_layout = view.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        isHideError = a.getBoolean(R.styleable.CometChatGroupMemberList_hideError, false);
        searchKeyword = a.getString(R.styleable.CometChatGroupMemberList_searchKeyword);
        empty_text = a.getString(R.styleable.CometChatGroupMemberList_empty_text);
        error_text = a.getString(R.styleable.CometChatGroupMemberList_error_text);
        limit = a.getInt(R.styleable.CometChatGroupMemberList_limit, 30);
        allowKickMember = a.getBoolean(R.styleable.CometChatGroupMemberList_allowKickMember, true);
        allowBanMember = a.getBoolean(R.styleable.CometChatGroupMemberList_AllowBanMember, true);

        setEmptyStateMessage(empty_text);
        addView(view);
        setViewModel();
        allowKickMembers(allowKickMember);
        allowBanUnbanMembers(allowBanMember);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    makeGroupMemberList();
                }

            }
        });


        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                groupMember = groupMembersViewModel.getGroupMember(viewHolder.getAdapterPosition());
                handleSwipe(groupMember, underlayButtons);
            }
        };


        swipeHelper.attachToRecyclerView(getRecyclerView());
    }

    private void handleSwipe(GroupMember groupMember, List<RecyclerViewSwipeListener.UnderlayButton> underlayButtons) {
        if (group_ != null) {
            if (CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group_.getScope())) {
                if (CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(groupMember.getScope())) {
                    underlayButtons.addAll(getOptions());
                }
            } else if (CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group_.getScope())) {
                if (!group_.getOwner().equalsIgnoreCase(groupMember.getUid()) && !CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(groupMember.getScope())) {
                    underlayButtons.addAll(getOptions());
                } else if (group_.getOwner() != null && group_.getOwner().equalsIgnoreCase(loggedInUser.getUid()) && !groupMember.getUid().equals(group_.getOwner())) {
                    underlayButtons.addAll(getOptions());
                }
            }
        } else {
            Toast.makeText(context, group_ + "", Toast.LENGTH_SHORT).show();
        }
    }


    private List<RecyclerViewSwipeListener.UnderlayButton> getOptions() {

        List<RecyclerViewSwipeListener.UnderlayButton> options = new ArrayList<>();

        options.add(new RecyclerViewSwipeListener.UnderlayButton(
                null,
                Utils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_delete_conversation)),
                getResources().getColor(R.color.red),
                new RecyclerViewSwipeListener.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        handleKick();
                    }
                }
        ));
        options.add(new RecyclerViewSwipeListener.UnderlayButton(
                null,
                Utils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_block)),
                getResources().getColor(R.color.yellow),
                new RecyclerViewSwipeListener.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        handleBan();
                    }
                }
        ));

        if (!allowBanMember && !allowKickMember)
            options = new ArrayList<>();
        else {
            if (!allowKickMember)
                options.remove(0);
            if (!allowBanMember)
                options.remove(1);
        }

        return options;
    }

    private void handleBan() {
        if (groupMember != null) {
            CometChat.banGroupMember(groupMember.getUid(), GUID, new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String successMessage) {
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.onGroupMemberBan(groupMember, loggedInUser, group_);
                    }
                    groupMembersViewModel.remove(groupMember);
                    groupMember = null;
                }

                @Override
                public void onError(CometChatException e) {
                    for (CometChatGroupEvents ex : CometChatGroupEvents.groupEvents.values()) {
                        ex.onError(e);
                    }
                    throwError(e);
                    hideError(e.getMessage(), 1);
                }
            });
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
    private void handleKick() {
        if (groupMember != null) {
            CometChat.kickGroupMember(groupMember.getUid(), GUID, new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String successMessage) {

                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.onGroupMemberKick(groupMember, loggedInUser, group_);
                    }
                    groupMembersViewModel.remove(groupMember);
                    groupMember = null;
                }

                @Override
                public void onError(CometChatException e) {
                    for (CometChatGroupEvents ex : CometChatGroupEvents.groupEvents.values()) {
                        ex.onError(e);
                    }
                    throwError(e);
                    hideError(e.getMessage(), 2);

                }
            });

        }
    }

    public void setEmptyStateMessage(String message) {
        if (message != null)
            noListText.setText(message);
        else
            noListText.setText(getResources().getString(R.string.no_members));

    }

    private void setViewModel() {
        if (groupMembersViewModel == null) {
            groupMembersViewModel = new CometChatGroupMembersViewModel(context, this);
        }
    }

    public void emptyTextColor(int color) {
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
    /**
     * @setEmptyView is method allows you to set layout show when the list is empty
     * if user want to set Empty layout other wise it will load default layout
     */
    public void setEmptyView(View view) {
        if (view != null)
            emptyView = view;
    }

    /**
     * @setErrorView is method allows you to set layout, show when there is a error
     * if user want to set Error layout other wise it will load default layout
     */
    public void setErrorView(View view) {
        if (view != null)
            errorView = view;
    }

    public void allowKickMembers(boolean allowKickMember) {
        this.allowKickMember = allowKickMember;
    }


    public void allowBanUnbanMembers(boolean allowBanMember) {
        this.allowBanMember = allowBanMember;
    }


    public void setGroup(Group group_) {
        this.group_ = group_;
        this.GUID = group_.getGuid();
        groupMembersViewModel.setGroup(group_);
    }

    /**
     * This method is used to handle onResume state of CometChatgroupMembers
     *
     * @author CometChat Team
     * @copyright © 2021 CometChat Inc.
     * @see CometChatGroupMemberList
     */

    public void onResume() {
        groupMembersRequest = null;
        groupMembersViewModel.clear();
        makeGroupMemberList();
        addGroupListener();
    }

    public RecyclerView getRecyclerView() {
        if (recyclerView != null)
            return recyclerView;
        return null;
    }

    public void searchGroups(String searchString) {
        groupMembersViewModel.clear();
        groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(GUID)
                .setLimit(limit)
                .setSearchKeyword(searchString)
                .setScopes(scopes)
                .build();
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                groupMembersViewModel.setGroupMemberList(groupMembers);

                if (groupMembers.size() == 0) {
                    if (emptyView != null) {
                        custom_layout.setVisibility(VISIBLE);
                        custom_layout.removeAllViews();
                        custom_layout.addView(emptyView);
                    } else {
                        emptyStateView.setVisibility(View.VISIBLE);
                    }
                } else {
                    custom_layout.removeAllViews();
                    custom_layout.setVisibility(GONE);
                    emptyStateView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError(null, 0);
                e.printStackTrace();
            }
        });
    }

    private void makeGroupMemberList() {
        if (groupMembersRequest == null) {
            groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(GUID)
                    .setLimit(limit)
                    .setSearchKeyword(searchKeyword)
                    .setScopes(scopes)
                    .build();
        }
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> list) {
                groupMembers.addAll(list);
                if (groupMembers.size() != 0) {
                    stopHideShimmer();
                    emptyStateView.setVisibility(View.GONE);
                    custom_layout.setVisibility(GONE);
                    setGroupMembers(list);
                } else {
                    checkNoGroups();
                }

            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError(null, 0);
            }

        });

    }

    private void throwError(CometChatException e) {
        if (onErrorCallBack != null)
            onErrorCallBack.onError(e);

    }

    private void hideError(String error, int num) {
        String error_message;
        if (error != null)
            error_message = error;
        else if (error_text != null)
            error_message = error_text;
        else
            error_message = getContext().getString(R.string.error_cant_load_group_members);

        if (!isHideError && errorView != null) {
            custom_layout.removeAllViews();
            custom_layout.addView(errorView);
            custom_layout.setVisibility(VISIBLE);
        } else {
            if (!isHideError) {
                custom_layout.setVisibility(GONE);
                String positiveButton = getContext().getString(R.string.try_again);
                String negativeButton = getResources().getString(R.string.cancel);
                if (num != 0) {
                    positiveButton = getContext().getString(R.string.okay);
                    negativeButton = "";
                }
                if (getContext() != null) {
                    new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, error_message, null, positiveButton, "", negativeButton, new OnAlertDialogButtonClickListener() {
                        @Override
                        public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                if (num == 0)
                                    makeGroupMemberList();

                                alertDialog.dismiss();
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                alertDialog.dismiss();
                            }
                        }
                    }, 0, false);
                }
            }
        }

    }

    private void setGroupMembers(List<GroupMember> list) {
        if (groupMembersViewModel != null)
            groupMembersViewModel.setGroupMemberList(list);

    }

    private void stopHideShimmer() {
        groupMemberListShimmer.stopShimmer();
        groupMemberListShimmer.setVisibility(View.GONE);
    }

    public void setItemClickListener(OnItemClickListener<GroupMember> onItemClickListener) {

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                GroupMember groupMember = (GroupMember) var1.getTag(R.string.member);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(groupMember, var2);
                else
                    throw new NullPointerException("OnItemClickListener<group> is null");
            }

            @Override
            public void onLongClick(View var1, int var2) {
                GroupMember groupMember = (GroupMember) var1.getTag(R.string.member);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(groupMember, var2);
                else
                    throw new NullPointerException("OnItemClickListener<group> is null");
            }
        }));

    }

    private void checkNoGroups() {
        if (size() == 0) {
            stopHideShimmer();
            if (emptyView != null) {
                custom_layout.setVisibility(VISIBLE);
                custom_layout.removeAllViews();
                custom_layout.addView(emptyView);
            } else {
                emptyStateView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            custom_layout.setVisibility(GONE);
        }
    }

    public int size() {
        return groupMembersViewModel.size();
    }

    public void addOnErrorListener(onErrorCallBack onErrorCallBack) {
        this.onErrorCallBack = onErrorCallBack;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setConfigurations(CometChatConfigurations configuration) {
        groupMembersViewModel.setConfiguration(configuration);
    }

    public void clearList() {
        groupMembers.clear();
        groupMembersRequest = null;
        if (groupMembersViewModel != null)
            groupMembersViewModel.clear();
        makeGroupMemberList();
    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        groupMembersViewModel.setConfigurations(configurations);
    }


    public void allowPromoteDemoteMembers(boolean allowPromoteDemoteMembers) {
        if (groupMembersViewModel != null)
            groupMembersViewModel.allowPromoteDemoteMembers(allowPromoteDemoteMembers);
    }

    public void setSelectedMember(GroupMember groupMember) {
        groupMembersViewModel.setSelectedMember(groupMember);
    }

    public interface onErrorCallBack {
        void onError(CometChatException exception);
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

    public void onPause() {
        removeGroupListener();
    }

    public void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    public void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(joinedUser, false, false, action);
                }
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(leftUser, true, false, action);
                }


            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(kickedUser, true, false, action);
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(bannedUser, true, false, action);
                }
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(updatedUser, false, true, action);
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedBy, User userAdded, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(userAdded, false, false, action);
                }
            }
        });
    }

    private void updateGroupMember(User user, boolean isRemoved, boolean isScopeUpdate, Action action) {
        if (groupMembersViewModel != null) {
            if (!isRemoved && !isScopeUpdate) {
                groupMembersViewModel.add(UserToGroupMember(user, false, action.getOldScope()));

            } else if (isRemoved && !isScopeUpdate) {
                groupMembersViewModel.remove(UserToGroupMember(user, false, action.getOldScope()));

            } else if (!isRemoved) {
                groupMembersViewModel.update(UserToGroupMember(user, true, action.getNewScope()));
            }
        }
    }

    public static GroupMember UserToGroupMember(User user, boolean isScopeUpdate, String newScope) {
        GroupMember groupMember;
        if (isScopeUpdate)
            groupMember = new GroupMember(user.getUid(), newScope);
        else
            groupMember = new GroupMember(user.getUid(), CometChatConstants.SCOPE_PARTICIPANT);

        groupMember.setAvatar(user.getAvatar());
        groupMember.setName(user.getName());
        groupMember.setStatus(user.getStatus());
        return groupMember;
    }
}
