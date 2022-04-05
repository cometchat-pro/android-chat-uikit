package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroups;

import android.content.Context;

import com.cometchat.pro.models.Group;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;

import java.util.List;

public class CometChatGroupsViewModel {

    private Context context;
    private CometChatGroupAdapter groupListAdapter;

    private CometChatGroupList cometChatGroupList;


    private CometChatGroupsViewModel() {

    }

    public CometChatGroupsViewModel(Context context, CometChatGroupList cometChatGroupList) {
        this.cometChatGroupList = cometChatGroupList;
        this.context = context;
        setGroupListAdapter(cometChatGroupList);
    }


    private CometChatGroupAdapter getAdapter() {
        if (groupListAdapter == null) {
            groupListAdapter = new CometChatGroupAdapter(context);
        }
        return groupListAdapter;
    }


    private void setGroupListAdapter(CometChatGroupList cometChatGroupList) {
        groupListAdapter = new CometChatGroupAdapter(context);

        cometChatGroupList.getRecyclerView().setAdapter(groupListAdapter);
    }


    public void setGroupList(List<Group> groupList) {

        getAdapter().updateList(groupList);
    }

    public Group getGroup(int position) {
        Group group = null;
        if (getAdapter() != null)
            group = getAdapter().getItemAtPosition(position);
        return group;
    }

    public void remove(Group group) {
        if (getAdapter() != null)
            getAdapter().removeGroup(group);
    }


    public void update(Group group) {
        if (getAdapter() != null)
            getAdapter().updateGroup(group);
    }

    public void add(Group group) {
        if (getAdapter() != null)
            getAdapter().add(group);
    }

    public int size() {
        if (getAdapter() != null)
            return getAdapter().getItemCount();
        else
            return 0;
    }

    public void searchGroupList(List<Group> groups) {
        if (getAdapter() != null)
            getAdapter().searchGroup(groups);
    }

    public void clear() {
        if (getAdapter() != null)
            getAdapter().clear();
    }


    public void setConfiguration(CometChatConfigurations configuration) {
        groupListAdapter.setConfiguration(configuration);

    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        groupListAdapter.setConfiguration(configurations);

    }

    public void setConversationListItemProperty(boolean hideAvatar, boolean hideTitleListItem, int titleColorListItem, boolean hideSubtitleListItem, int subTitleColorListItem, int backgroundColorListItem, float cornerRadiusListItem) {
        if (groupListAdapter!=null)
            groupListAdapter.setConversationListItemProperty(hideAvatar,
                    hideTitleListItem,titleColorListItem,
                    hideSubtitleListItem,subTitleColorListItem,
                    backgroundColorListItem,cornerRadiusListItem);

    }
}
