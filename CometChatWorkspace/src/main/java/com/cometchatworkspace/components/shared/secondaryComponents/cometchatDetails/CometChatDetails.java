package com.cometchatworkspace.components.shared.secondaryComponents.cometchatDetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupActivity;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.groups.members.CometChatAddMembers;
import com.cometchatworkspace.components.groups.members.CometChatBannedMembers;
import com.cometchatworkspace.components.groups.members.CometChatGroupMembers;
import com.cometchatworkspace.components.groups.members.CometChatTransferOwnership;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSectionList.CometChatSectionList;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSectionList.SectionData;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSharedMedia.CometChatSharedMedia;
import com.cometchatworkspace.components.users.CometChatUserEvents;
import com.cometchatworkspace.components.users.CometChatUsers;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CometChatDetails extends CometChatListBase {

    private static final String TAG = "CometChatDetails";
    private Context context;
    private View view;
    private CometChatSectionList cometChatSectionList;
    private CometChatDataItem cometChatDataItem;
    private CometChatSharedMedia sharedMedia;
    private User user;
    private Group group;
    private String type;
    private String id;
    private Palette palette;
    private Typography typography;

    //user options
    public static final String BLOCK = "block_user";
    public static final String UNBLOCK = "unblock_user";
    public static final String VIEW_PROFILE = "view_profile";

    //group options
    public static final String VIEW_GROUP_MEMBERS = "view_members";
    public static final String ADD_GROUP_MEMBERS = "add_members";
    public static final String BANNED_GROUP_MEMBERS = "banned_members";
    public static final String LEAVE_GROUP = "leave_group";
    public static final String DELETE_EXIT_GROUP = "delete_exit_group";

    //for user to set error text,font and color according to his/her choice
    private String errorMessageFont = null;
    private int errorMessageColor = 0;
    private String errorText = null;
    private boolean hideError = false;

    private final boolean hideGroupDetail = true;
    private final boolean hideUserDetail = true;
    private boolean isOwner = false;
    private final User loggedInUser = CometChat.getLoggedInUser();

    private addOnOptionClickListener onOptionClickListener;

    //for options and component visibility
    private boolean allowBanUnbanMembers, allowKickMembers, allowPromoteDemoteMembers, allowAddMembers, allowDeleteGroup, allowLeaveGroup, viewSharedMedia, allowBlockUnblockUser, viewProfile;
    private onErrorCallBack onErrorCallBack;

    /**
     * Instantiates a new CometChatDetails
     *
     * @param context the context
     */
    public CometChatDetails(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null, -1);
    }

    /**
     * Instantiates a new CometChatDetails
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CometChatDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    /**
     * Instantiates a new CometChatDetails
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CometChatDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        view = View.inflate(context, R.layout.cometchat_details, null);
        cometChatSectionList = view.findViewById(R.id.sectionList);
        cometChatDataItem = view.findViewById(R.id.dataItem);
        sharedMedia = view.findViewById(R.id.sharedMedia);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Users,
                0, 0);

        allowBanUnbanMembers = a.getBoolean(R.styleable.CometChatDetails_allowBanUnbanMembers, true);
        allowKickMembers = a.getBoolean(R.styleable.CometChatDetails_allowKickMembers, true);
        allowPromoteDemoteMembers = a.getBoolean(R.styleable.CometChatDetails_allowPromoteDemoteMembers, true);
        allowAddMembers = a.getBoolean(R.styleable.CometChatDetails_allowAddMembers, true);
        allowDeleteGroup = a.getBoolean(R.styleable.CometChatDetails_allowDeleteGroup, true);
        allowLeaveGroup = a.getBoolean(R.styleable.CometChatDetails_allowLeaveGroup, true);
        viewSharedMedia = a.getBoolean(R.styleable.CometChatDetails_viewSharedMedia, true);
        allowBlockUnblockUser = a.getBoolean(R.styleable.CometChatDetails_allowBlockUnblockUser, true);
        viewProfile = a.getBoolean(R.styleable.CometChatDetails_viewProfile, true);
        boolean showBackButton = a.getBoolean(R.styleable.CometChatDetails_showBackButton, true);
        int backGroundColor = a.getColor(R.styleable.CometChatDetails_backgroundColor, palette.getBackground());
        int backIconTint = a.getColor(R.styleable.CometChatDetails_backIconTint, palette.getPrimary());
        int titleColor = a.getColor(R.styleable.CometChatDetails_titleColor, palette.getAccent());
        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatDetails_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatDetails_backButtonIcon) : getResources().getDrawable(R.drawable.ic_close_24dp);

        setStatusColor(palette.getBackground());

        super.setTitle(getContext().getString(R.string.details));
        super.setTitleAppearance(typography.getHeading());
        super.setTitleColor(titleColor);
        super.backIcon(backButtonIcon);
        super.backIconTint(backIconTint);
        super.showBackButton(showBackButton);
        super.hideSearch(true);

        if (palette.getGradientBackground() != null)
            setBackground(palette.getGradientBackground());
        else
            setBackgroundColor(backGroundColor);

        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {

            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
            }
        });

        cometChatSectionList.addOnItemClickListener(new CometChatSectionList.ItemActionListener() {
            @Override
            public void onClick(ActionItem actionItem, int pos) {
                if (onOptionClickListener == null)
                    performActionOnClick(actionItem, pos);
                else
                    onOptionClickListener.onOptionClick(actionItem, pos);
            }

            @Override
            public void onLongClick(ActionItem actionItem, int pos) {
                performActionOnClick(actionItem, pos);

            }
        });
        super.addListView(view);
    }
    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context, color);

    }

    public void setErrorMessageFont(String errorMessageFont) {
        this.errorMessageFont = errorMessageFont;
    }

    public void setErrorMessageColor(int errorMessageColor) {
        this.errorMessageColor = errorMessageColor;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    public CometChatDetails allowBanUnbanMembers(boolean allowBanUnbanMembers) {
        this.allowBanUnbanMembers = allowBanUnbanMembers;
        return this;

    }

    public CometChatDetails allowKickMembers(boolean allowKickMembers) {
        this.allowKickMembers = allowKickMembers;
        return this;

    }

    public CometChatDetails allowPromoteDemoteMembers(boolean allowPromoteDemoteMembers) {
        this.allowPromoteDemoteMembers = allowPromoteDemoteMembers;
        return this;

    }

    public CometChatDetails allowAddMembers(boolean allowAddMembers) {
        this.allowAddMembers = allowAddMembers;
        return this;
    }

    public CometChatDetails allowDeleteGroup(boolean allowDeleteGroup) {
        this.allowDeleteGroup = allowDeleteGroup;
        return this;

    }

    public CometChatDetails allowLeaveGroup(boolean allowLeaveGroup) {
        this.allowLeaveGroup = allowLeaveGroup;
        return this;

    }

    public CometChatDetails viewSharedMedia(boolean viewSharedMedia) {
        this.viewSharedMedia = viewSharedMedia;
        return this;

    }

    public CometChatDetails allowBlockUnblockUser(boolean allowBlockUnblockUser) {
        this.allowBlockUnblockUser = allowBlockUnblockUser;
        return this;

    }

    public CometChatDetails viewProfile(boolean viewProfile) {
        this.viewProfile = viewProfile;
        return this;
    }

    public void setUser(User user_) {
        this.user = user_;
        this.id = user_.getUid();
        type = CometChatConstants.RECEIVER_TYPE_USER;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, Utils.convertDpToPx(context, 24));
        cometChatDataItem.setLayoutParams(params);
        cometChatDataItem.user(user);
        setUserData(user);
        setOptions(null);
    }

    public void setGroup(Group group_) {
        this.group = group_;
        this.id = group_.getGuid();
        type = CometChatConstants.RECEIVER_TYPE_GROUP;
        isOwner = group_.getOwner().equalsIgnoreCase(loggedInUser.getUid());
        cometChatDataItem.group(group);
        cometChatDataItem.hideSubTitle(true);
        setOptions(null);
    }

    public void setOptions(List<SectionData> dataItems) {
        if (dataItems == null) {
            cometChatSectionList.setDataItems(getOptions());
        } else if (dataItems.size() == 0) {
            hideSectionList(true);
        } else {
            cometChatSectionList.setDataItems(dataItems);
        }
    }

    public List<SectionData> getOptions() {
        List<SectionData> sectionDataList = new ArrayList<>();
        if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (user != null) {
                List<ActionItem> actionItems = new ArrayList<>();

                if (viewProfile) {
                    if (user.getLink() != null && !user.getLink().isEmpty()) {
                        actionItems.add(new ActionItem(VIEW_PROFILE, getContext().getString(R.string.view_profile), 0, 0, 0, 0, typography.getName(), palette.getPrimary()));
                    }
                }
                if (allowBlockUnblockUser) {
                    if (user.isBlockedByMe()) {
                        actionItems.add(new ActionItem(UNBLOCK, getContext().getString(R.string.unblock_user), 0, 0, 0, 0, typography.getName(), palette.getPrimary()));
                    } else {
                        actionItems.add(new ActionItem(BLOCK, getContext().getString(R.string.block_user), 0, 0, 0, 0, typography.getName(), palette.getError()));
                    }
                }
                sectionDataList.add(new SectionData("", actionItems));
            }
        } else if (type.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {

            List<ActionItem> actionItems = new ArrayList<>(Arrays.asList(
                    new ActionItem(VIEW_GROUP_MEMBERS, getContext().getString(R.string.view_members), 0, R.drawable.ic_next, 0, 0, typography.getName(), palette.getAccent()),
                    new ActionItem(ADD_GROUP_MEMBERS, getContext().getString(R.string.add_members), 0, R.drawable.ic_next, 0, 0, typography.getName(), palette.getAccent()),
                    new ActionItem(BANNED_GROUP_MEMBERS, getContext().getString(R.string.banned_members), 0, R.drawable.ic_next, 0, 0, typography.getName(), palette.getAccent())
            ));
            List<ActionItem> actionItems2 = new ArrayList<>(Arrays.asList(
                    new ActionItem(LEAVE_GROUP, getContext().getString(R.string.leave_group), 0, 0, 0, 0, typography.getName(), palette.getError()),
                    new ActionItem(DELETE_EXIT_GROUP, getContext().getString(R.string.delete_and_exit), 0, 0, 0, 0, typography.getName(), palette.getError())
            ));

            List<ActionItem> actionItems_temp = new ArrayList<>();
            actionItems_temp.add(actionItems.get(0));
            List<ActionItem> actionItems_temp2 = new ArrayList<>();

            if (group != null && group.isJoined()) {
                if (group.getScope() != null && group.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_PARTICIPANT)) {
                    sectionDataList.add(new SectionData(getContext().getString(R.string.more_option), Collections.singletonList(actionItems.get(0)), true, false, null, palette.getAccent100(), 0, palette.getAccent100(), 0));
                } else if (group.getScope() != null && group.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_MODERATOR)) {
                    if (allowBanUnbanMembers)
                        actionItems_temp.add(actionItems.get(2));
                    sectionDataList.add(new SectionData(getContext().getString(R.string.more_option), actionItems_temp, true, false, null, palette.getAccent100(), 0, palette.getAccent100(), 0));
                } else if (group.getScope() != null && group.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_ADMIN)) {
                    if (allowAddMembers)
                        actionItems_temp.add(actionItems.get(1));
                    if (allowBanUnbanMembers)
                        actionItems_temp.add(actionItems.get(2));
                    sectionDataList.add(new SectionData(getContext().getString(R.string.more_option), actionItems_temp, true, false, null, palette.getAccent100(), 0, palette.getAccent100(), 0));
                } else if (isOwner) {
                    if (allowAddMembers)
                        actionItems_temp.add(actionItems.get(1));
                    if (allowBanUnbanMembers)
                        actionItems_temp.add(actionItems.get(2));
                    sectionDataList.add(new SectionData(getContext().getString(R.string.more_option), actionItems_temp, true, false, null, palette.getAccent100(), 0, palette.getAccent100(), 0));
                }
                if (loggedInUser.getUid().equals(group.getOwner()) || CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group.getScope())) {
                    if (allowLeaveGroup)
                        actionItems_temp2.add(actionItems2.get(0));
                    if (allowDeleteGroup)
                        actionItems_temp2.add(actionItems2.get(1));
                    sectionDataList.add(new SectionData("", actionItems_temp2, true, false, null, palette.getAccent100(), 0, palette.getAccent100(), 0));
                } else {
                    if (allowLeaveGroup)
                        sectionDataList.add(new SectionData("", Collections.singletonList(actionItems2.get(0)), true, false, null, palette.getAccent100(), 0, palette.getAccent100(), 0));
                }
            }
        }

        return sectionDataList;


    }

    private void addOnOptionClickListener(addOnOptionClickListener onOptionClickListener) {
        this.onOptionClickListener = onOptionClickListener;
    }

    public void addOnErrorListener(onErrorCallBack onErrorCallBack) {
        this.onErrorCallBack = onErrorCallBack;
    }


    private void performActionOnClick(ActionItem actionItem, int pos) {

        if (user != null && actionItem.getId() != null &&
                actionItem.getId().equalsIgnoreCase(BLOCK)) {
            blockUser(user.getUid(), pos);
        } else if (user != null && actionItem.getId() != null &&
                actionItem.getId().equalsIgnoreCase(UNBLOCK)) {
            unblockUser(user.getUid(), pos);
        } else if (user != null && user.getLink() != null &&
                actionItem.getId() != null && actionItem.getId().equalsIgnoreCase(VIEW_PROFILE)) {
            viewUserProfile(user.getLink());
        } else if (group != null && actionItem.getId() != null &&
                actionItem.getId().equalsIgnoreCase(VIEW_GROUP_MEMBERS)) {
            CometChatGroupActivity cometChatGroupActivity = new CometChatGroupActivity();
            cometChatGroupActivity.addParameters(UIKitConstants.IntentStrings.ALLOW_BAN_UNBAN_MEMBERS, allowBanUnbanMembers);
            cometChatGroupActivity.addParameters(UIKitConstants.IntentStrings.ALLOW_KICK_MEMBERS, allowKickMembers);
            cometChatGroupActivity.addParameters(UIKitConstants.IntentStrings.ALLOW_PROMOTE_DEMOTE_MEMBERS, allowPromoteDemoteMembers);
            CometChatGroupActivity.launch(context,CometChatGroupMembers.class,group);
        } else if (group != null && actionItem.getId() != null &&
                actionItem.getId().equalsIgnoreCase(ADD_GROUP_MEMBERS)) {
            CometChatGroupActivity.launch(context, CometChatAddMembers.class,group);
        } else if (group != null && actionItem.getId() != null &&
                actionItem.getId().equalsIgnoreCase(BANNED_GROUP_MEMBERS)) {
            CometChatGroupActivity groupActivity = new CometChatGroupActivity();
            groupActivity.addParameters(UIKitConstants.IntentStrings.ALLOW_BAN_UNBAN_MEMBERS,allowBanUnbanMembers);
            groupActivity.launchComponent(context,CometChatBannedMembers.class,group);
        } else if (group != null && actionItem.getId() != null && actionItem.getId().equalsIgnoreCase(LEAVE_GROUP)) {
            if (!isOwner) {
                leaveGroupDialog(group.getGuid(), pos);
            } else
                showTransferOwnerShipDialog();
        } else if (group != null && actionItem.getId() != null && actionItem.getId().equalsIgnoreCase(DELETE_EXIT_GROUP)) {
            deleteGroupDialog(group.getGuid(), pos);
        }

    }

    private void deleteGroupDialog(String guid, int pos) {
        if (getContext() != null) {
            new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, getResources().getString(R.string.delete_group_text), null, null, getContext().getString(R.string.delete_and_exit), "", getResources().getString(R.string.cancel), palette.getError(), 0, 0, new OnAlertDialogButtonClickListener() {
                @Override
                public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        deleteGroup(guid, pos);
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, false);
        }

    }

    private void leaveGroupDialog(String guid, int pos) {

        if (getContext() != null) {
            new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, getResources().getString(R.string.leave_group_text), null, null, getContext().getString(R.string.leave_group), "", getResources().getString(R.string.cancel), palette.getError(), 0, 0, new OnAlertDialogButtonClickListener() {
                @Override
                public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        leaveGroup(guid, pos);
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, false);
        }

    }

    private void viewUserProfile(String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private void hideSectionList(boolean value) {
        if (value)
            cometChatSectionList.setVisibility(GONE);
        else
            cometChatSectionList.setVisibility(VISIBLE);
    }

    private void showTransferOwnerShipDialog() {

        if (getContext() != null) {
            new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, getResources().getString(R.string.transfer_ownership), null, getContext().getString(R.string.transfer_ownership_text), "", getResources().getString(R.string.cancel), new OnAlertDialogButtonClickListener() {
                @Override
                public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        CometChatGroupActivity groupActivity = new CometChatGroupActivity();
                        groupActivity.setGroup(group);
                        groupActivity.launchComponent(context, CometChatTransferOwnership.class);
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, false);
        }


    }

    private void deleteGroup(String guid, int pos) {

        CometChat.deleteGroup(guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Log.d(TAG, "Group deleted successfully: ");
                ((Activity)context).onBackPressed();
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupDelete(group);
                }
            }

            @Override
            public void onError(CometChatException e) {

                throwError(e);
                hideError(DELETE_EXIT_GROUP, pos);
                if (CometChatGroupEvents.groupEvents != null) {
                    for (CometChatGroupEvents ev : CometChatGroupEvents.groupEvents.values()) {
                        ev.onError(e);
                    }
                }
                Log.d(TAG, "Group delete failed with exception: " + e.getMessage());
            }
        });
    }

    private void leaveGroup(String guid, int pos) {

        CometChat.leaveGroup(guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                group.setHasJoined(false);
                group.setMembersCount(group.getMembersCount() - 1);
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupMemberLeave(loggedInUser, group);
                }
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError(LEAVE_GROUP, pos);
                for (CometChatGroupEvents ev : CometChatGroupEvents.groupEvents.values()) {
                    ev.onError(e);
                }
            }
        });
    }

    private void blockUser(String uid, int pos) {
        CometChat.blockUsers(Arrays.asList(uid), new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> resultMap) {
                if (resultMap != null && "success".equalsIgnoreCase(resultMap.get(user.getUid()))) {
                    ActionItem actionItem1 = new ActionItem(UNBLOCK, getContext().getString(R.string.unblock_user), 0, 0, 0, 0, typography.getName(), palette.getPrimary());
                    cometChatSectionList.updateRow(actionItem1, pos);
                    user.setBlockedByMe(true);
                    setUser(user);
                    for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
                        events.onUserBlock(user);
                    }
                } else {
                    hideError(BLOCK, pos);
                }
            }

            @Override
            public void onError(CometChatException e) {
                // Handle block users failure
                throwError(e);
                hideError(BLOCK, pos);
                for (CometChatUserEvents ev : CometChatUserEvents.userEvents.values()) {
                    ev.onError(e);
                }
            }
        });
    }

    private void unblockUser(String uid, int pos) {

        CometChat.unblockUsers(Arrays.asList(uid), new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> resultMap) {

                if (resultMap != null && "success".equalsIgnoreCase(resultMap.get(user.getUid()))) {
                    ActionItem actionItem1 = new ActionItem(BLOCK, getContext().getString(R.string.block_user), 0, 0, 0, 0, typography.getName(), palette.getError());
                    cometChatSectionList.updateRow(actionItem1, pos);
                    user.setBlockedByMe(false);
                    setUser(user);
                    for (CometChatUserEvents events : CometChatUserEvents.userEvents.values()) {
                        events.onUserUnblock(user);
                    }
                } else {
                    hideError(BLOCK, pos);
                }
                // Handle block users success.
            }

            @Override
            public void onError(CometChatException e) {
                throwError(e);
                hideError(UNBLOCK, pos);
                for (CometChatUserEvents ev : CometChatUserEvents.userEvents.values()) {
                    ev.onError(e);
                }
                // Handle block users failure
            }
        });
    }

    private void hideError(String action, int pos) {
        String error_message;
        if (errorText != null)
            error_message = errorText;
        else
            error_message = getContext().getString(R.string.something_went_wrong);

        if (!hideError) {
            if (getContext() != null) {
                new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, error_message, null, getContext().getString(R.string.try_again), "", getResources().getString(R.string.cancel), new OnAlertDialogButtonClickListener() {
                    @Override
                    public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (user != null && action.equals(BLOCK))
                                blockUser(user.getUid(), pos);
                            else if (user != null && action.equals(UNBLOCK))
                                unblockUser(user.getUid(), pos);
                            else if (group != null && action.equals(LEAVE_GROUP))
                                leaveGroup(group.getGuid(), pos);
                            else if (group != null && action.equals(DELETE_EXIT_GROUP))
                                deleteGroup(group.getGuid(), pos);
                            alertDialog.dismiss();
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }
                }, 0, false);
            }
        }
    }

    private void throwError(CometChatException e) {
        if (onErrorCallBack != null)
            onErrorCallBack.onError(e);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (user != null) {
            setUser(user);
            adduserListener();
        } else if (group != null) {
            setGroup(group);
            addGroupListener();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeUserListener();
        removeGroupListener();
    }

    private void addGroupListener() {
        CometChatGroupEvents.addGroupListener("CometChatDetails", new CometChatGroupEvents() {
            @Override
            public void onItemClick(Group group, int position) {

            }

            @Override
            public void onGroupCreate(Group group) {

            }

            @Override
            public void onError(CometChatException error) {

            }

            @Override
            public void onGroupDelete(Group group) {

            }

            @Override
            public void onGroupMemberLeave(User leftUser, Group leftGroup) {

            }

            @Override
            public void onGroupMemberChangeScope(User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {

            }

            @Override
            public void onGroupMemberBan(User bannedUser, User bannedBy, Group bannedFrom) {

            }

            @Override
            public void onGroupMemberAdd(User addedBy, List<User> usersAdded, Group group) {
                if (group != null) {
                    setGroup(group);
                }
            }

            @Override
            public void onGroupMemberKick(User kickedUser, User kickedBy, Group kickedFrom) {

            }

            @Override
            public void onGroupMemberUnban(User unbannedUser, User unbannedBy, Group unBannedFrom) {

            }

            @Override
            public void onGroupMemberJoin(User joinedUser, Group joinedGroup) {

            }

            @Override
            public void onOwnershipChange(Group group, GroupMember member) {
                setGroup(group);
            }
        });
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group group_) {
                super.onGroupMemberKicked(action, kickedUser, kickedBy, group_);
                if (kickedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.onGroupMemberKick(kickedUser, kickedBy, group_);
                    }
                    ((Activity) context).onBackPressed();
                }
            }

            @Override
            public void onGroupMemberLeft(Action action, User user, Group group_) {
                super.onGroupMemberLeft(action, user, group_);
                group.setMembersCount(group.getMembersCount() - 1);
                if (!user.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                    group_.setHasJoined(true);
                }
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupMemberLeave(user, group_);
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group group_) {
                super.onGroupMemberBanned(action, bannedUser, bannedBy, group_);
                if (bannedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.onGroupMemberBan(bannedUser,bannedBy, group);
                    }
                    ((Activity) context).onBackPressed();

                }
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group_) {
                super.onGroupMemberScopeChanged(action, updatedBy, updatedUser, scopeChangedTo, scopeChangedFrom, group_);
                if (updatedUser.getUid().equals(loggedInUser.getUid()) && group_.getGuid().equals(group.getGuid())) {
                    group.setScope(scopeChangedTo);
                    setGroup(group);
                }
            }
        });
    }

    private void adduserListener() {
        CometChat.addUserListener(TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user_) {
                setUserData(user_);
            }

            @Override
            public void onUserOffline(User user_) {
                setUserData(user_);
            }
        });
    }

    private void setUserData(User user_) {
        if (user_.equals(user)) {
            if (user.isBlockedByMe() || user.isHasBlockedMe()) {
                cometChatDataItem.hideStatusIndicator(true);
                cometChatDataItem.getAvatar().setAvatar("");
                cometChatDataItem.hideSubTitle(true);
            } else {
                cometChatDataItem.hideSubTitle(false);
                cometChatDataItem.hideStatusIndicator(false);
                cometChatDataItem.subTitle(user_.getStatus());
                cometChatDataItem.getAvatar().setAvatar(user);
            }


        }

    }

    /**
     * This method is used to remove the userlistener.
     */
    private void removeUserListener() {
        CometChat.removeUserListener(TAG);
    }

    /**
     * This method is used to remove the grouplistener.
     */
    private void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    public interface onErrorCallBack {
        void onError(CometChatException exception);
    }

    public interface addOnOptionClickListener {
        void onOptionClick(ActionItem actionItem, int pos);
    }


}
