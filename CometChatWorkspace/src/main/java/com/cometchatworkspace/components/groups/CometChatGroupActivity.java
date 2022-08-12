package com.cometchatworkspace.components.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.createGroup.CometChatCreateGroup;
import com.cometchatworkspace.components.groups.members.CometChatAddMembers;
import com.cometchatworkspace.components.groups.members.CometChatBannedMembers;
import com.cometchatworkspace.components.groups.members.CometChatGroupMembers;
import com.cometchatworkspace.components.groups.members.CometChatTransferOwnership;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDetails.CometChatDetails;
import com.cometchatworkspace.resources.constants.UIKitConstants;

import java.util.List;
import java.util.Objects;

public class CometChatGroupActivity extends AppCompatActivity {

    private static Group group;
    private static Class component;
    private static Bundle bundle = new Bundle();
    private boolean allowBanUnbanMembers,allowKickMembers,allowPromoteDemoteMembers;

    public void setGroup(Group g) {
        group = g;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);
        RelativeLayout root = findViewById(R.id.root);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        allowBanUnbanMembers = getIntent()
                .getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_BAN_UNBAN_MEMBERS, true);
        allowKickMembers = getIntent()
                .getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_KICK_MEMBERS, true);
        allowPromoteDemoteMembers = getIntent()
                .getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_PROMOTE_DEMOTE_MEMBERS, true);

        if (component==CometChatCreateGroup.class) {
            CometChatCreateGroup createGroup = new CometChatCreateGroup(this);
            createGroup.setLayoutParams(params);
            root.addView(createGroup);
        }
        else if (component==CometChatGroupMembers.class) {
            CometChatGroupMembers groupMembers = new CometChatGroupMembers(this);
            groupMembers.setLayoutParams(params);
            root.addView(groupMembers);
            if(group!=null)
                groupMembers.setGroup(group)
                    .allowBanUnbanMembers(allowBanUnbanMembers)
                    .allowKickMembers(allowKickMembers)
                    .allowPromoteDemoteMembers(allowPromoteDemoteMembers);

        }
        else if (component==CometChatJoinProtectedGroup.class) {
            CometChatJoinProtectedGroup joinProtectedGroup = new CometChatJoinProtectedGroup(this);
            joinProtectedGroup.setLayoutParams(params);
            root.addView(joinProtectedGroup);
            if (group != null) {
                joinProtectedGroup.setGroup(group);
            }
        }
        else if (component== CometChatDetails.class) {
            CometChatDetails cometChatDetails = new CometChatDetails(this);
            cometChatDetails.setLayoutParams(params);
            root.addView(cometChatDetails);
            if (group != null)
                cometChatDetails.setGroup(group);
        }
        else if (component== CometChatAddMembers.class) {
            CometChatAddMembers addMembers = new CometChatAddMembers(this);
            addMembers.setLayoutParams(params);
            root.addView(addMembers);
            if (group!=null)
                addMembers.setGroup(group);
        }
        else if (component== CometChatBannedMembers.class) {
            CometChatBannedMembers bannedMembers = new CometChatBannedMembers(this);
            bannedMembers.allowBanUnbanMembers(allowBanUnbanMembers);
            bannedMembers.setTitle(getResources().getString(R.string.banned_members));
            root.addView(bannedMembers);
            if (group != null)
                bannedMembers.setGroup(group);
        } else if (component== CometChatTransferOwnership.class) {
            CometChatTransferOwnership transferOwnership = new CometChatTransferOwnership(this);
            root.addView(transferOwnership);
            if (group!=null)
                transferOwnership.setGroup(group);
        }

        CometChatGroupEvents.addGroupListener("GroupActivity", new CometChatGroupEvents() {
            @Override
            public void onGroupMemberAdd(User addedBy, List<User> usersAdded, Group group) {
                onBackPressed();
            }
        });
    }

    private void handleIntent() {
        if (getIntent()!=null) {
            group = (Group) getIntent().getSerializableExtra(UIKitConstants.IntentStrings.GROUP);
            allowBanUnbanMembers = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_BAN_UNBAN_MEMBERS, true);
            allowKickMembers = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_KICK_MEMBERS, true);
            allowPromoteDemoteMembers = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_PROMOTE_DEMOTE_MEMBERS, true);
        }
    }

    public static void launch(Context context,Class componentToLaunch) {
        component = componentToLaunch;
        context.startActivity(new Intent(context,CometChatGroupActivity.class));
    }

    public static void launch(Context context,Class componentToLaunch, Group g) {
        group = g;
        component = componentToLaunch;
        context.startActivity(new Intent(context,CometChatGroupActivity.class));
    }

    public void launchComponent(Context context,Class componentToLaunch) {
        component = componentToLaunch;
        context.startActivity(new Intent(context,CometChatGroupActivity.class));
    }

    public void launchComponent(Context context,Class componentToLaunch, Group g) {
        group = g;
        component = componentToLaunch;
        context.startActivity(new Intent(context,CometChatGroupActivity.class));
    }

    public void addParameters(String key, Object value) {
        if (value instanceof Boolean) {
            if (Objects.equals(key, UIKitConstants.IntentStrings.ALLOW_BAN_UNBAN_MEMBERS))
                allowBanUnbanMembers = (boolean) value;
            else if (Objects.equals(key, UIKitConstants.IntentStrings.ALLOW_KICK_MEMBERS))
                allowKickMembers = (boolean) value;
            else if (Objects.equals(key, UIKitConstants.IntentStrings.ALLOW_PROMOTE_DEMOTE_MEMBERS))
                allowPromoteDemoteMembers = (boolean) value;
        }
    }
}
