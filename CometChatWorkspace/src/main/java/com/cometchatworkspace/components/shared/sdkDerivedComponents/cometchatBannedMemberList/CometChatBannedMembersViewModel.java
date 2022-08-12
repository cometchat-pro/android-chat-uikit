package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatBannedMemberList;

import android.content.Context;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;

import java.util.List;

public class CometChatBannedMembersViewModel {
    private Context context;
    private CometChatBannedMemberAdapter bannedMemberAdapter;

    private CometChatBannedMembersViewModel() {

    }

    public CometChatBannedMembersViewModel(Context context, CometChatBannedMemberList cometChatBannedMemberList) {
        this.context = context;
        setGroupMemberListAdapter(cometChatBannedMemberList);
    }


    private CometChatBannedMemberAdapter getAdapter() {
        if (bannedMemberAdapter == null) {
            bannedMemberAdapter = new CometChatBannedMemberAdapter(context);
        }
        return bannedMemberAdapter;
    }


    private void setGroupMemberListAdapter(CometChatBannedMemberList cometChatBannedMemberList) {
        bannedMemberAdapter = new CometChatBannedMemberAdapter(context);
        cometChatBannedMemberList.getRecyclerView().setAdapter(bannedMemberAdapter);
    }


    public void setGroupMemberList(List<GroupMember> groupList) {

        getAdapter().updateList(groupList);
    }

    public GroupMember getGroupMember(int position) {
        GroupMember groupMember = null;
        if (getAdapter() != null)
            groupMember = getAdapter().getItemAtPosition(position);
        return groupMember;
    }

    public void remove(GroupMember groupMember) {
        if (getAdapter() != null)
            getAdapter().removeGroup(groupMember);
    }


    public void update(GroupMember groupMember) {
        if (getAdapter() != null)
            getAdapter().updateGroup(groupMember);
    }

    public void add(GroupMember groupMember) {
        if (getAdapter() != null)
            getAdapter().add(groupMember);
    }

    public int size() {
        if (getAdapter() != null)
            return getAdapter().getItemCount();
        else
            return 0;
    }

    public void searchGroupMemberList(List<GroupMember> groupMembers) {
        if (getAdapter() != null)
            getAdapter().searchGroup(groupMembers);
    }

    public void clear() {
        if (getAdapter() != null)
            getAdapter().clear();
    }


    public void setConfiguration(CometChatConfigurations configuration) {
        bannedMemberAdapter.setConfiguration(configuration);

    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        bannedMemberAdapter.setConfigurations(configurations);

    }

    public void setGroup(Group group) {
        bannedMemberAdapter.setGroup(group);
    }


}
