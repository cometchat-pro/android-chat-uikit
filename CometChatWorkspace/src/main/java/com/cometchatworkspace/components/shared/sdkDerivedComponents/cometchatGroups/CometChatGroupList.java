package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroups;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.GroupsListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
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

public class CometChatGroupList extends MaterialCardView {

    private static final String TAG = "cometchatgrouplist";

    private Context context;

    private CometChatGroupsViewModel groupListViewModel;

    private View view;

    private RecyclerView recyclerView;

    private ShimmerFrameLayout groupShimmer;

    private LinearLayout noGoupsView; // Used to display a information when no Groups are fetched.

    private View emptyView = null;
    private View errorView = null;

    private CometChatSoundManager soundManager;


    private onErrorCallBack onErrorCallBack;

    private LinearLayout custom_layout;

    private AttributeSet attrs;

    private TextView noListText;

    private boolean isHideError, isJoinedOnly;

    private String searchKeyWord = "";

    private List<String> tags = new ArrayList<>();

    private GroupsRequest groupsRequest;    //Uses to fetch Groups.

    private int limit;

    private final List<Group> groupList = new ArrayList<>();

    private FontUtils fontUtils;

    private String errorMessageFont = null;
    private int errorMessageColor = 0;
    private String error_text = null;
    private String empty_text = null;

    public CometChatGroupList(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public CometChatGroupList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);

    }

    public CometChatGroupList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);

    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this.context = context;
        view = View.inflate(context, R.layout.cometchat_list, null);
        fontUtils = FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatGroupList,
                0, 0);

        soundManager = new CometChatSoundManager(context);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        groupShimmer = view.findViewById(R.id.shimmer_layout);
        noGoupsView = view.findViewById(R.id.no_list_view);
        noListText = view.findViewById(R.id.no_list_text);
        custom_layout = view.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        isHideError = a.getBoolean(R.styleable.CometChatGroupList_hideError, false);
        isJoinedOnly = a.getBoolean(R.styleable.CometChatGroupList_joinedOnly, false);
        searchKeyWord = a.getString(R.styleable.CometChatGroupList_searchKeyword);
        empty_text = a.getString(R.styleable.CometChatGroupList_empty_text);
        error_text = a.getString(R.styleable.CometChatGroupList_error_text);
        limit = a.getInt(R.styleable.CometChatGroupList_limit, 30);

        setNoListMessage(empty_text);
        addView(view);
        setViewModel();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    makeGroupList();
                }

            }
        });

    }

    public void setNoListMessage(String message) {
        if (message != null)
            noListText.setText(message);
        else
            noListText.setText(getResources().getString(R.string.no_groups));

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
            emptyTextColor(style.getEmptyStateTextColor());
        }

        if (style.getErrorStateTextFont() != null) {
            errorMessageFont = style.getErrorStateTextFont();
        }
        if (style.getErrorStateTextColor() != 0) {
            errorMessageColor = style.getErrorStateTextColor();
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

    /**
     * @setEmptyView is method allows you to set layout show when the list is empty
     * if user want to set Empty layout other wise it will load default layout
     */
    public void setEmptyView(int id) {
        try {
            emptyView = View.inflate(context, id, null);

        } catch (Exception e) {
            emptyView = null;
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method is used to handle onResume state of CometChatGroupList
     *
     * @author CometChat Team
     * @copyright © 2021 CometChat Inc.
     * @see CometChatGroupList
     */
    public void onResume() {
        groupsRequest = null;
        groupListViewModel.clear();
        makeGroupList();
        addGroupListener();
    }

    private void updateGroup(Group group) {
        if (groupListViewModel != null)
            groupListViewModel.update(group);
    }

    public void removeGroup(Group group) {
        if (groupListViewModel != null)
            groupListViewModel.remove(group);
    }

    public void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User user, Group group) {
                super.onGroupMemberJoined(action, user, group);
                updateGroup(group);
            }

            @Override
            public void onGroupMemberLeft(Action action, User user, Group group) {
                super.onGroupMemberLeft(action, user, group);
                updateGroup(group);

            }

            @Override
            public void onGroupMemberKicked(Action action, User user, User user1, Group group) {
                super.onGroupMemberKicked(action, user, user1, group);
                updateGroup(group);

            }

            @Override
            public void onGroupMemberBanned(Action action, User user, User user1, Group group) {
                super.onGroupMemberBanned(action, user, user1, group);
                updateGroup(group);

            }

            @Override
            public void onGroupMemberUnbanned(Action action, User user, User user1, Group group) {
                super.onGroupMemberUnbanned(action, user, user1, group);
                updateGroup(group);

            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User user, User user1, String s, String s1, Group group) {
                super.onGroupMemberScopeChanged(action, user, user1, s, s1, group);
                updateGroup(group);

            }

            @Override
            public void onMemberAddedToGroup(Action action, User user, User user1, Group group) {
                super.onMemberAddedToGroup(action, user, user1, group);
                updateGroup(group);

            }
        });
    }

    public void onPause() {
        removeGroupListener();
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

    public void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    private void setViewModel() {
        if (groupListViewModel == null) {
            groupListViewModel = new CometChatGroupsViewModel(context, this);
        }
    }

    public RecyclerView getRecyclerView() {
        if (recyclerView != null)
            return recyclerView;
        return null;
    }

    private void makeGroupList() {
        groupsRequest = new GroupsRequest.GroupsRequestBuilder()
                .setLimit(limit)
                .setTags(tags)
                .joinedOnly(isJoinedOnly).setSearchKeyWord(searchKeyWord)
                .build();

        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                groupList.addAll(groups);
                if (groupList.size() != 0) {
                    stopHideShimmer();
                    noGoupsView.setVisibility(View.GONE);
                    custom_layout.setVisibility(GONE);
                    setGroupList(groups);
                } else {
                    checkNoGroups();
                }
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                stopHideShimmer();
                hideError();
            }
        });

    }

    private void throwError(CometChatException e) {
        if (onErrorCallBack != null)
            onErrorCallBack.onError(e);

    }

    private void hideError() {
        String error_message;
        if (error_text != null)
            error_message = error_text;
        else
            error_message = getContext().getString(R.string.error_cant_load_group);

        if (!isHideError && errorView != null) {
            custom_layout.removeAllViews();
            custom_layout.addView(errorView);
            custom_layout.setVisibility(VISIBLE);
        } else {
            if (!isHideError) {
                custom_layout.setVisibility(GONE);
                if (getContext() != null) {
                    new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, error_message, null, getContext().getString(R.string.try_again), "", getResources().getString(R.string.cancel), new OnAlertDialogButtonClickListener() {
                        @Override
                        public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                alertDialog.dismiss();
                                makeGroupList();
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                alertDialog.dismiss();
                            }
                        }
                    }, 0, false);
                }
            }
        }

    }


    private void checkNoGroups() {
        if (size() == 0) {
            stopHideShimmer();
            if (emptyView != null) {
                custom_layout.setVisibility(VISIBLE);
                custom_layout.removeAllViews();
                custom_layout.addView(emptyView);
            } else {
                noGoupsView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else {
            noGoupsView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            custom_layout.setVisibility(GONE);
        }
    }

    public int size() {
        return groupListViewModel.size();
    }

    /**
     * This methods sets/update the list of group provided by the developer
     *
     * @param groupList list of groups
     */
    public void setGroupList(List<Group> groupList) {
        if (groupListViewModel != null)
            groupListViewModel.setGroupList(groupList);

        Log.e("check", "setGroupList: " + groupListViewModel.size());
    }

    public void searchGroups(String searchString) {
        groupListViewModel.clear();
        groupsRequest = new GroupsRequest.GroupsRequestBuilder()
                .setLimit(limit)
                .setTags(tags)
                .joinedOnly(isJoinedOnly).setSearchKeyWord(searchString)
                .build();
        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                groupListViewModel.setGroupList(groups);

                if (groups.size() == 0) {
                    if (emptyView != null) {
                        custom_layout.setVisibility(VISIBLE);
                        custom_layout.removeAllViews();
                        custom_layout.addView(emptyView);
                    } else {
                        noGoupsView.setVisibility(View.VISIBLE);
                    }
                } else {
                    custom_layout.removeAllViews();
                    custom_layout.setVisibility(GONE);
                    noGoupsView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError();
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is used to hide shimmer effect if the list is loaded.
     */
    private void stopHideShimmer() {
        groupShimmer.stopShimmer();
        groupShimmer.setVisibility(View.GONE);
    }

    /**
     * This method helps to get Click events of CometChatGroupList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener<Group> onItemClickListener) {

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                Group group = (Group) var1.getTag(R.string.group);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(group, var2);
                else
                    throw new NullPointerException("OnItemClickListener<group> is null");
            }

            @Override
            public void onLongClick(View var1, int var2) {
                Group group = (Group) var1.getTag(R.string.group);
                if (onItemClickListener != null)
                    onItemClickListener.OnItemLongClick(group, var2);
                else
                    throw new NullPointerException("OnItemClickListener<group> is null");
            }
        }));

    }

    //to set boolean for hiding or showing error
    public void setHideError(boolean hideError) {
        isHideError = hideError;
    }

    //to set boolean for showing groups in which user is joined or not
    public void setJoinedOnly(boolean joinedOnly) {
        isJoinedOnly = joinedOnly;
    }

    //to set search keyword for filtering groups

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }
    //to set tags  for filtering groups

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    //to set limit for fetching groups

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Group getGroup(int position) {
        Group group = null;
        if (groupListViewModel != null)
            group = groupListViewModel.getGroup(position);
        return group;
    }

    public void refreshGroup(CometChat.CallbackListener callbackListener) {
        clearList();
        groupList.clear();
        groupsRequest = null;
        groupsRequest = new GroupsRequest.GroupsRequestBuilder()
                .setLimit(limit)
                .setTags(tags)
                .joinedOnly(isJoinedOnly).setSearchKeyWord(searchKeyWord)
                .build();
        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                groupList.addAll(groups);
                if (groupList.size() != 0) {
                    stopHideShimmer();
                    setGroupList(groups);
                } else {
                    checkNoGroups();
                }
                callbackListener.onSuccess(groupList);
            }

            @Override
            public void onError(CometChatException e) {
                stopHideShimmer();
                Log.d(TAG, "onError: " + e.getMessage());
                throwError(e);
                callbackListener.onError(e);
                hideError();
            }
        });
    }

    /**
     * This method is used to clear a list of user present in CometChatUserList Component
     *
     * @CometChatGroupListViewModelClear()
     */
    public void clearList() {
        groupList.clear();
        groupsRequest = null;
        if (groupListViewModel != null)
            groupListViewModel.clear();
        makeGroupList();
    }

    public void addOnErrorListener(onErrorCallBack onErrorCallBack) {
        this.onErrorCallBack = onErrorCallBack;
    }

    public interface onErrorCallBack {
        void onError(CometChatException exception);
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof GroupsListConfiguration) {
            isHideError = ((GroupsListConfiguration) configuration).isHideError();
            isJoinedOnly = ((GroupsListConfiguration) configuration).isJoinedOnly();
            searchKeyWord = ((GroupsListConfiguration) configuration).getSearchKeyWord();
            tags = ((GroupsListConfiguration) configuration).getTags();
            emptyView = ((GroupsListConfiguration) configuration).getEmptyView();
            limit = ((GroupsListConfiguration) configuration).getLimit();

        } else
            groupListViewModel.setConfiguration(configuration);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        groupListViewModel.setConfiguration(configurations);
    }

    public void setGroupListItemProperty(boolean hideAvatar,
                                         boolean hideTitleListItem, int titleColorListItem,
                                         boolean hideSubtitleListItem, int subTitleColorListItem,
                                         int backgroundColorListItem, int backroundColorListItemPosition,
                                         float cornerRadiusListItem
    ) {
        if (groupListViewModel != null)
            groupListViewModel.setConversationListItemProperty(hideAvatar,
                    hideTitleListItem, titleColorListItem,
                    hideSubtitleListItem, subTitleColorListItem,
                    backgroundColorListItem, cornerRadiusListItem);
    }
}
