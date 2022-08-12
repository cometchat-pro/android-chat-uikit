package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroupMemberList;

import android.content.Context;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;

import java.util.List;

public class CometChatGroupMembersViewModel {
    private Context context;
    private CometChatGroupMemberAdapter groupMemberAdapter;

    private CometChatGroupMembersViewModel() {

    }

    public CometChatGroupMembersViewModel(Context context, CometChatGroupMemberList cometChatGroupMemberList) {
        this.context = context;
        setGroupMemberListAdapter(cometChatGroupMemberList);
    }


    private CometChatGroupMemberAdapter getAdapter() {
        if (groupMemberAdapter == null) {
            groupMemberAdapter = new CometChatGroupMemberAdapter(context);
        }
        return groupMemberAdapter;
    }


    private void setGroupMemberListAdapter(CometChatGroupMemberList cometChatGroupMemberList) {
        groupMemberAdapter = new CometChatGroupMemberAdapter(context);
        cometChatGroupMemberList.getRecyclerView().setAdapter(groupMemberAdapter);
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
            getAdapter().removeGroupMember(groupMember);
    }


    public void update(GroupMember groupMember) {
        if (getAdapter() != null)
            getAdapter().updateGroupMember(groupMember);
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
            getAdapter().searchGroupMember(groupMembers);
    }

    public void clear() {
        if (getAdapter() != null)
            getAdapter().clear();
    }


    public void setConfiguration(CometChatConfigurations configuration) {
        groupMemberAdapter.setConfiguration(configuration);

    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        groupMemberAdapter.setConfigurations(configurations);

    }

    public void setGroup(Group group) {
        groupMemberAdapter.setGroup(group);
    }


    public void setSelectedMember(GroupMember groupMember) {
        groupMemberAdapter.setSelectedMember(groupMember);
    }

    public void allowPromoteDemoteMembers(boolean allowPromoteDemoteMembers) {
        if (groupMemberAdapter != null)
            groupMemberAdapter.allowPromoteDemoteMembers(allowPromoteDemoteMembers);

    }
}
