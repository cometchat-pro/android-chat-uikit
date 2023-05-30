package com.cometchat.chatuikit.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.addmembers.CometChatAddMembers;
import com.cometchat.chatuikit.bannedmembers.CometChatBannedMembers;
import com.cometchat.chatuikit.creategroup.CometChatCreateGroup;
import com.cometchat.chatuikit.creategroup.CreateGroupConfiguration;
import com.cometchat.chatuikit.details.CometChatDetails;
import com.cometchat.chatuikit.details.DetailsConfiguration;
import com.cometchat.chatuikit.groupmembers.CometChatGroupMembers;
import com.cometchat.chatuikit.joinprotectedgroup.CometChatJoinProtectedGroup;
import com.cometchat.chatuikit.joinprotectedgroup.JoinProtectedGroupConfiguration;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.transferownership.CometChatTransferOwnership;
import com.cometchat.pro.models.Group;

public class CometChatGroupActivity extends AppCompatActivity {

    private static Group group;
    private static Class component;
    private static Bundle bundle = new Bundle();
    private static DetailsConfiguration detailsConfiguration;
    private static JoinProtectedGroupConfiguration joinProtectedGroupConfiguration;
    private static CreateGroupConfiguration createGroupConfiguration;

    public void setGroup(Group g) {
        group = g;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);
        RelativeLayout root = findViewById(R.id.root);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (component == CometChatCreateGroup.class) {
            CometChatCreateGroup createGroup = new CometChatCreateGroup(this);
            createGroup.setLayoutParams(params);
            if (createGroupConfiguration != null) {
                createGroup.setTitle(createGroupConfiguration.getTitle());
                createGroup.setCloseButtonIcon(getResources().getDrawable(createGroupConfiguration.getCloseIcon()));
                createGroup.setCreateGroupIcon(getResources().getDrawable(createGroupConfiguration.getCreateIcon()));
                createGroup.setNameInputBoxPlaceHolderText(createGroupConfiguration.getNamePlaceHolderText());
                createGroup.setPasswordInputBoxPlaceHolderText(createGroupConfiguration.getPasswordPlaceHolderText());
                createGroup.setOnCreateGroup(createGroupConfiguration.getOnCreateGroup());
                createGroup.setOnError(createGroupConfiguration.getOnError());
                createGroup.setStyle(createGroupConfiguration.getStyle());
                createGroup.addOnBackPressListener(createGroupConfiguration.getOnBackPress());
            }
            root.addView(createGroup);
        } else if (component == CometChatGroupMembers.class) {
            CometChatGroupMembers groupMembers = new CometChatGroupMembers(this);
            groupMembers.setLayoutParams(params);
            root.addView(groupMembers);
            if (group != null) groupMembers.setGroup(group);
        } else if (component == CometChatJoinProtectedGroup.class) {
            CometChatJoinProtectedGroup joinProtectedGroup = new CometChatJoinProtectedGroup(this);
            joinProtectedGroup.setLayoutParams(params);
            if (group != null) joinProtectedGroup.setGroup(group);
            if (joinProtectedGroupConfiguration != null) {
                joinProtectedGroup.setTitle(joinProtectedGroupConfiguration.getTitle());
                joinProtectedGroup.backIcon(joinProtectedGroupConfiguration.getCloseIcon());
                joinProtectedGroup.setJoinGroupIcon(getResources().getDrawable(joinProtectedGroupConfiguration.getJoinIcon()));
                joinProtectedGroup.setEditTextPlaceHolderText(joinProtectedGroupConfiguration.getPlaceHolderText());
                joinProtectedGroup.setDescription(joinProtectedGroupConfiguration.getDescription());
                joinProtectedGroup.setOnJoinClick(joinProtectedGroupConfiguration.getOnJoinClick());
                joinProtectedGroup.addOnBackPressListener(joinProtectedGroupConfiguration.getOnBackPress());
                joinProtectedGroup.setOnError(joinProtectedGroupConfiguration.getOnError());
                joinProtectedGroup.setStyle(joinProtectedGroupConfiguration.getStyle());
            }
            root.addView(joinProtectedGroup);

        } else if (component == CometChatDetails.class) {
            CometChatDetails cometChatDetails = new CometChatDetails(this);
            cometChatDetails.setLayoutParams(params);
            root.addView(cometChatDetails);
            if (group != null) cometChatDetails.setGroup(group);
            if (detailsConfiguration != null) {
                cometChatDetails.showCloseButton(detailsConfiguration.isShowCloseButton());
                cometChatDetails.setCloseButtonIcon(detailsConfiguration.getCloseButtonIcon());
                cometChatDetails.hideProfile(detailsConfiguration.isHideProfile());
                cometChatDetails.setSubtitleView(detailsConfiguration.getSubtitleView());
                cometChatDetails.setCustomProfileView(detailsConfiguration.getCustomProfileView());
                cometChatDetails.setData(detailsConfiguration.getData());
                cometChatDetails.setMenu(detailsConfiguration.getMenu());
                cometChatDetails.disableUserPresence(detailsConfiguration.isDisableUsersPresence());
                cometChatDetails.setProtectedGroupIcon(detailsConfiguration.getProtectedGroupIcon());
                cometChatDetails.setPrivateGroupIcon(detailsConfiguration.getPrivateGroupIcon());
                cometChatDetails.setStyle(detailsConfiguration.getStyle());
                cometChatDetails.setListItemStyle(detailsConfiguration.getListItemStyle());
                cometChatDetails.setAvatarStyle(detailsConfiguration.getAvatarStyle());
                cometChatDetails.setStatusIndicatorStyle(detailsConfiguration.getStatusIndicatorStyle());
                cometChatDetails.setGroupMembersConfiguration(detailsConfiguration.getGroupMembersConfiguration());
                cometChatDetails.setAddMembersConfiguration(detailsConfiguration.getAddMembersConfiguration());
                cometChatDetails.setBannedMembersConfiguration(detailsConfiguration.getBannedMembersConfiguration());
                cometChatDetails.setTransferOwnershipConfiguration(detailsConfiguration.getTransferOwnershipConfiguration());
                cometChatDetails.setOnError(detailsConfiguration.getOnError());
            }
        } else if (component == CometChatAddMembers.class) {
            CometChatAddMembers addMembers = new CometChatAddMembers(this);
            addMembers.setLayoutParams(params);
            root.addView(addMembers);
            if (group != null) addMembers.setGroup(group);
        } else if (component == CometChatBannedMembers.class) {
            CometChatBannedMembers bannedMembers = new CometChatBannedMembers(this);
            bannedMembers.setTitle(getResources().getString(R.string.banned_members));
            root.addView(bannedMembers);
            if (group != null) bannedMembers.setGroup(group);
        } else if (component == CometChatTransferOwnership.class) {
            CometChatTransferOwnership transferOwnership = new CometChatTransferOwnership(this);
            root.addView(transferOwnership);
            if (group != null) transferOwnership.setGroup(group);
        }

    }

    private void handleIntent() {
        if (getIntent() != null) {
            group = (Group) getIntent().getSerializableExtra(UIKitConstants.IntentStrings.GROUP);
        }
    }

    public static void launch(Context context, Class componentToLaunch) {
        component = componentToLaunch;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public static void launch(Context context, Class componentToLaunch, CreateGroupConfiguration createGroupConfiguration1) {
        component = componentToLaunch;
        createGroupConfiguration = createGroupConfiguration1;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public static void launch(Context context, Class componentToLaunch, Group g, DetailsConfiguration detailsConfiguration1) {
        group = g;
        component = componentToLaunch;
        detailsConfiguration = detailsConfiguration1;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public static void launch(Context context, Class componentToLaunch, Group g, JoinProtectedGroupConfiguration joinProtectedGroupConfiguration1) {
        group = g;
        component = componentToLaunch;
        joinProtectedGroupConfiguration = joinProtectedGroupConfiguration1;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public void launchComponent(Context context, Class componentToLaunch) {
        component = componentToLaunch;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public void launchComponent(Context context, Class componentToLaunch, Group g) {
        group = g;
        component = componentToLaunch;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        group = null;
        component = null;
        detailsConfiguration = null;
        joinProtectedGroupConfiguration = null;
        createGroupConfiguration = null;
    }
}