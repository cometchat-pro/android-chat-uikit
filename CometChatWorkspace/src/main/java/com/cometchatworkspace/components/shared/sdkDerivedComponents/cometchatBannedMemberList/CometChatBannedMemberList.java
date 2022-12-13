package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatBannedMemberList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.custom_dialog.CometChatDialog;
import com.cometchatworkspace.resources.utils.custom_dialog.OnDialogButtonClickListener;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CometChatBannedMemberList extends MaterialCardView {

    private Context context;

    private CometChatBannedMembersViewModel groupBannedMembersViewModel;

    private View view;

    private static final String TAG = "CometChatBannedMemberList";

    private RecyclerView recyclerView;


    private ShimmerFrameLayout groupShimmer;
    private LinearLayout emptyStateView; // Used to display a information when no Groups are fetched.

    private BannedGroupMembersRequest bannedGroupMembersRequest;
    private View emptyView = null;
    private View errorView = null;

    private LinearLayout custom_layout;
    private TextView noListText;

    private boolean isHideError;
    private FontUtils fontUtils;
    private onErrorCallBack onErrorCallBack;

    private int errorStateTextAppearance = 0;
    private final int errorMessageColor = 0;
    private String errorText = null;
    private String empty_text = null;

    private String searchKeyword = null;
    private int limit;
    private String GUID;
    private final List<GroupMember> groupMembers = new ArrayList<>();
    private RecyclerViewSwipeListener swipeHelper;

    private Group group_;
    private final User loggedInUser = CometChat.getLoggedInUser();


    private GroupMember groupMember; // to get the swiped member

    private Palette palette;
    private Typography typography;

    public CometChatBannedMemberList(Context context) {
        super(context);
        initViewComponent(context, null, -1);

    }

    public CometChatBannedMemberList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);

    }

    public CometChatBannedMemberList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);

    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {

        this.context = context;
        view = View.inflate(context, R.layout.cometchat_list, null);
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        errorStateTextAppearance = typography.getText1();
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatBannedMemberList,
                0, 0);

        recyclerView = view.findViewById(R.id.list_recyclerview);
        groupShimmer = view.findViewById(R.id.shimmer_layout);
        emptyStateView = view.findViewById(R.id.no_list_view);
        noListText = view.findViewById(R.id.no_list_text);
        custom_layout = view.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        isHideError = a.getBoolean(R.styleable.CometChatBannedMemberList_hideError, false);
        searchKeyword = a.getString(R.styleable.CometChatBannedMemberList_searchKeyword);
        empty_text = a.getString(R.styleable.CometChatBannedMemberList_empty_text);
        errorText = a.getString(R.styleable.CometChatBannedMemberList_error_text);
        limit = a.getInt(R.styleable.CometChatBannedMemberList_limit, 30);
        allowBanUnbanMembers = a.getBoolean(R.styleable.CometChatBannedMemberList_allowBanUnbanMembers, true);

        setEmptyStateMessage(empty_text);
        addView(view);
        setViewModel();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    makeBannedMemberList();
                }

            }
        });


        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                groupMember = groupBannedMembersViewModel.getGroupMember(viewHolder.getAdapterPosition());
                handleSwipe(groupMember, underlayButtons);
            }
        };


        swipeHelper.attachToRecyclerView(getRecyclerView());
    }

    private void handleSwipe(GroupMember groupMember, List<RecyclerViewSwipeListener.UnderlayButton> underlayButtons) {
        if (group_ != null) {
            if (CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group_.getScope())) {
                underlayButtons.addAll(getOptions());
            } else if (CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group_.getScope())) {
                underlayButtons.addAll(getOptions());
            } else if (group_.getOwner() != null && group_.getOwner().equalsIgnoreCase(loggedInUser.getUid())) {
                underlayButtons.addAll(getOptions());
            }
        } else {
            Toast.makeText(context, group_ + "", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean allowBanUnbanMembers;

    public void allowBanUnbanMembers(boolean allowBanUnbanMembers) {
        this.allowBanUnbanMembers = allowBanUnbanMembers;
    }

    private List<RecyclerViewSwipeListener.UnderlayButton> getOptions() {

        List<RecyclerViewSwipeListener.UnderlayButton> options = new ArrayList<>();
        Drawable closeDrawble = getResources().getDrawable(R.drawable.ic_close_24dp);
        closeDrawble.setTint(getResources().getColor(R.color.textColorWhite));
        options.add(new RecyclerViewSwipeListener.UnderlayButton(
                null,
                Utils.drawableToBitmap(closeDrawble),
                getResources().getColor(R.color.red),
                new RecyclerViewSwipeListener.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        handleUnban();
                    }
                }
        ));


        if (!allowBanUnbanMembers)
            options = new ArrayList<>();


        return options;
    }

    private void handleUnban() {

        CometChat.unbanGroupMember(groupMember.getUid(), GUID, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupMemberUnban(groupMember, loggedInUser, group_);
                }
                groupBannedMembersViewModel.remove(groupMember);
                groupMember = null;
            }

            @Override
            public void onError(CometChatException e) {
                for (CometChatGroupEvents ex : CometChatGroupEvents.groupEvents.values()) {
                    ex.onError(e);
                }
                throwError(e);
                hideError();
            }
        });

    }


    public void setEmptyStateMessage(String message) {
        if (message != null)
            noListText.setText(message);
        else
            noListText.setText(getResources().getString(R.string.no_members));

    }

    private void setViewModel() {
        if (groupBannedMembersViewModel == null) {
            groupBannedMembersViewModel = new CometChatBannedMembersViewModel(context, this);
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

    public void errorStateTextAppearance(int errorStateTextAppearance) {
        if (errorStateTextAppearance != 0)
            this.errorStateTextAppearance = errorStateTextAppearance;
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


    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public void setGroup(Group group_) {
        this.group_ = group_;
        this.GUID = group_.getGuid();
        groupBannedMembersViewModel.setGroup(group_);
    }

    /**
     * This method is used to handle onResume state of CometChatgroupMembers
     *
     * @author CometChat Team
     * @copyright © 2021 CometChat Inc.
     * @see CometChatBannedMemberList
     */

    public void onResume() {
        bannedGroupMembersRequest = null;
        groupBannedMembersViewModel.clear();
        makeBannedMemberList();
        addGroupListener();
    }

    public RecyclerView getRecyclerView() {
        if (recyclerView != null)
            return recyclerView;
        return null;
    }

    public void searchBannedMembers(String searchString) {
        groupBannedMembersViewModel.clear();

        bannedGroupMembersRequest = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(GUID)
                .setLimit(limit)
                .setSearchKeyword(searchString)
                .build();

        bannedGroupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                groupBannedMembersViewModel.setGroupMemberList(groupMembers);

                checkNoMembers();
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError();
                e.printStackTrace();
            }
        });
    }

    private void makeBannedMemberList() {
        if (bannedGroupMembersRequest == null) {
            bannedGroupMembersRequest = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(GUID)
                    .setLimit(limit)
                    .setSearchKeyword(searchKeyword)
                    .build();

        }
        bannedGroupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> list) {
                groupMembers.addAll(list);
                if (groupMembers.size() != 0) {
                    stopHideShimmer();
                    emptyStateView.setVisibility(View.GONE);
                    custom_layout.setVisibility(GONE);
                    setGroupMembers(list);
                } else {
                    checkNoMembers();
                }

            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError();
            }

        });

    }

    private void throwError(CometChatException e) {
        if (onErrorCallBack != null)
            onErrorCallBack.onError(e);

    }

    private void hideError() {
        String errorMessage;
        if (errorText != null)
            errorMessage = errorText;
        else
            errorMessage = getContext().getString(R.string.error_cant_load_group);

        if (!isHideError && errorView != null) {
            custom_layout.removeAllViews();
            custom_layout.addView(errorView);
            custom_layout.setVisibility(VISIBLE);
        } else {
            if (!isHideError) {
                custom_layout.setVisibility(GONE);
                if (getContext() != null) {
                    new CometChatDialog(context,
                            0,
                            errorStateTextAppearance,
                            typography.getText2(),
                            palette.getAccent900(),
                            0,
                            palette.getAccent700(),
                            errorMessage,
                            "",
                            getContext().getString(R.string.try_again),
                            getResources().getString(R.string.cancel),
                            "",
                            palette.getPrimary(),
                            palette.getPrimary(),
                            0, new OnDialogButtonClickListener() {
                        @Override
                        public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                alertDialog.dismiss();
                                makeBannedMemberList();
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
        if (groupBannedMembersViewModel != null)
            groupBannedMembersViewModel.setGroupMemberList(list);

    }

    private void stopHideShimmer() {
        groupShimmer.stopShimmer();
        groupShimmer.setVisibility(View.GONE);
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

    private void checkNoMembers() {
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
        return groupBannedMembersViewModel.size();
    }

    public void addOnErrorListener(onErrorCallBack onErrorCallBack) {
        this.onErrorCallBack = onErrorCallBack;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setConfigurations(CometChatConfigurations configuration) {
        groupBannedMembersViewModel.setConfiguration(configuration);
    }

    public void clearList() {
        groupMembers.clear();
        bannedGroupMembersRequest = null;
        if (groupBannedMembersViewModel != null)
            groupBannedMembersViewModel.clear();
        makeBannedMemberList();
    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        groupBannedMembersViewModel.setConfigurations(configurations);
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
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(bannedUser, true, false, action);
                }
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupMemberBan(bannedUser, bannedBy, group);
                }
            }

            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group group) {
                if (group != null && group.equals(group_)) {
                    setGroup(group);
                    updateGroupMember(unbannedUser, false, false, action);
                }
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupMemberUnban(unbannedUser, unbannedBy, group);
                }
            }
        });
    }

    private void updateGroupMember(User user, boolean isRemoved, boolean isScopeUpdate, Action action) {
        if (groupBannedMembersViewModel != null) {
            if (!isRemoved && !isScopeUpdate) {
                groupBannedMembersViewModel.add(UserToGroupMember(user, false, action.getOldScope()));

            } else if (isRemoved && !isScopeUpdate) {
                groupBannedMembersViewModel.remove(UserToGroupMember(user, false, action.getOldScope()));

            } else if (!isRemoved) {
                groupBannedMembersViewModel.update(UserToGroupMember(user, true, action.getNewScope()));
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
