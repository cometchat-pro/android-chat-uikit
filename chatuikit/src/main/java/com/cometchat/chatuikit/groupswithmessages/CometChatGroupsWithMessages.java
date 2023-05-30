package com.cometchat.chatuikit.groupswithmessages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.creategroup.CometChatCreateGroup;
import com.cometchat.chatuikit.creategroup.CreateGroupConfiguration;
import com.cometchat.chatuikit.groups.CometChatGroupActivity;
import com.cometchat.chatuikit.groups.CometChatGroups;
import com.cometchat.chatuikit.groups.GroupsConfiguration;
import com.cometchat.chatuikit.joinprotectedgroup.CometChatJoinProtectedGroup;
import com.cometchat.chatuikit.joinprotectedgroup.JoinProtectedGroupConfiguration;
import com.cometchat.chatuikit.messages.MessageActivity;
import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;

/**
 * The custom implementation of the CometChatGroups class called CometChatGroupsWithMessages.
 * It extends the CometChatGroups class and adds additional functionality for opening the message screen for a selected group.
 * <br>
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *           <com.cometchat.chatuikit.groupswithmessages.CometChatGroupsWithMessages
 *             android:id="@+id/groupWithMessages"
 *             android:layout_width="match_parent"
 *             android:layout_height="match_parent" />
 *
 *  </LinearLayout>
 *  }
 *  </pre>
 */
public class CometChatGroupsWithMessages extends CometChatGroups {
    private JoinProtectedGroupConfiguration joinGroupConfiguration;
    private CreateGroupConfiguration createGroupConfiguration;
    private MessagesConfiguration messagesConfiguration;
    private GroupsWithMessagesViewModel groupsWithMessagesViewModel;
    private Context context;
    private CometChatTheme theme;
    private Group group;

    public CometChatGroupsWithMessages(Context context) {
        super(context);
        init(context);
    }

    public CometChatGroupsWithMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatGroupsWithMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        groupsWithMessagesViewModel = ViewModelProviders.of((FragmentActivity) context).get(GroupsWithMessagesViewModel.class);
        groupsWithMessagesViewModel.getOpenMessages().observe((AppCompatActivity) context, this::openMessages);
        groupsWithMessagesViewModel.getExceptionMutableLiveData().observe((AppCompatActivity) context, this::throwError);
        super.setItemClickListener(new OnItemClickListener<Group>() {
            @Override
            public void OnItemClick(Group group, int position) {
                if (group != null) {
                    if (!group.isJoined()) {
                        if (UIKitConstants.GroupType.PUBLIC.equals(group.getGroupType())) {
                            groupsWithMessagesViewModel.joinGroup(group.getGuid());
                        } else if (UIKitConstants.GroupType.PASSWORD.equals(group.getGroupType())) {
                            CometChatGroupActivity.launch(getContext(), CometChatJoinProtectedGroup.class, group, joinGroupConfiguration);
                        }
                    } else {
                        openMessages(group);
                    }
                }
            }
        });

        ImageView icon = new ImageView(context);
        icon.setImageResource(R.drawable.ic_create);
        icon.setImageTintList(ColorStateList.valueOf(theme.getPalette().getPrimary()));
        icon.setTag(R.string.create_group, icon);
        super.setMenu(icon);
        icon.setOnClickListener(v -> {
            CometChatGroupActivity.launch(context, CometChatCreateGroup.class, createGroupConfiguration);
        });
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private void throwError(CometChatException cometChatException) {
        if (getOnError() == null) {
            String errorMessage;
            if (getErrorText() != null) errorMessage = getErrorText();
            else errorMessage = getContext().getString(R.string.something_went_wrong);

            if (!isHideError() && getErrorView() != null) {
                getCustomLayout().removeAllViews();
                getCustomLayout().addView(getErrorView());
                getCustomLayout().setVisibility(VISIBLE);
            } else {
                getCustomLayout().setVisibility(GONE);
                if (!isHideError()) {
                    if (getContext() != null) {
                        new CometChatDialog(context, 0, getErrorStateTextAppearance(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, getErrorMessageColor(), errorMessage, "", getContext().getString(R.string.cancel), "", "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                alertDialog.dismiss();
                            }
                        }, 0, false);
                    }
                }
            }
        } else getOnError().onError(context, cometChatException);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        groupsWithMessagesViewModel.addListener();
        if (group != null && group.isJoined())
            MessageActivity.launch(context, group, messagesConfiguration);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        groupsWithMessagesViewModel.removeListener();
    }

    private void openMessages(Group group) {
        MessageActivity.launch(context, group, messagesConfiguration);
    }

    public void setJoinGroupConfiguration(JoinProtectedGroupConfiguration joinGroupConfiguration) {
        this.joinGroupConfiguration = joinGroupConfiguration;
    }

    public void setCreateGroupConfiguration(CreateGroupConfiguration createGroupConfiguration) {
        this.createGroupConfiguration = createGroupConfiguration;
    }

    public void setGroupsConfiguration(GroupsConfiguration groupsConfiguration) {
        if (groupsConfiguration != null) {
            super.setSubtitle(groupsConfiguration.getSubtitle());
            super.setListItemView(groupsConfiguration.getCustomView());
            super.setMenu(groupsConfiguration.getMenu());
            super.setOptions(groupsConfiguration.getOptions());
            super.hideSeparator(groupsConfiguration.isHideSeparator());
            super.setSearchPlaceholderText(groupsConfiguration.getSearchPlaceholderText());
            super.showBackButton(groupsConfiguration.isShowBackButton());
            super.backIcon(groupsConfiguration.getBackButtonIcon());
            super.setSelectionMode(groupsConfiguration.getSelectionMode());
            super.setOnSelection(groupsConfiguration.getOnSelection());
            super.setSearchBoxIcon(groupsConfiguration.getBackButtonIcon());
            super.hideSearch(groupsConfiguration.isHideSearch());
            super.setTitle(groupsConfiguration.getTitle());
            super.setEmptyStateView(groupsConfiguration.getEmptyStateView());
            super.setErrorStateView(groupsConfiguration.getErrorStateView());
            super.setLoadingStateView(groupsConfiguration.getLoadingStateView());
            super.setGroupsRequestBuilder(groupsConfiguration.getGroupsRequestBuilder());
            super.setSearchRequestBuilder(groupsConfiguration.getGroupsSearchRequestBuilder());
            super.setAvatarStyle(groupsConfiguration.getAvatarStyle());
            super.setListItemStyle(groupsConfiguration.getListItemStyle());
            super.setStatusIndicatorStyle(groupsConfiguration.getStatusIndicatorStyle());
            super.errorStateText(groupsConfiguration.getErrorStateText());
            super.emptyStateText(groupsConfiguration.getEmptyStateText());
            super.setStyle(groupsConfiguration.getStyle());
            super.setPrivateGroupIcon(groupsConfiguration.getPrivateGroupIcon());
            super.setPasswordGroupIcon(groupsConfiguration.getPasswordGroupIcon());
            super.setItemClickListener(groupsConfiguration.getItemClickListener());
            super.setOnError(groupsConfiguration.getOnError());
        }
    }

    public void setMessagesConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;
    }
}
