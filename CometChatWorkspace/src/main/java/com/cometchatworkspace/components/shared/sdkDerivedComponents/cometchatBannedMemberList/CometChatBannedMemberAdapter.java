package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatBannedMemberList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;

import java.util.ArrayList;
import java.util.List;

public class CometChatBannedMemberAdapter extends RecyclerView.Adapter<CometChatBannedMemberAdapter.myViewHolder> {
    private final Context context;
    private List<GroupMember> groupMemberList = new ArrayList<>();
    private Group group;
    private CometChatConfigurations configuration;
    private List<CometChatConfigurations> configurations = new ArrayList();
    private final User loggedInUser = CometChat.getLoggedInUser();
    Palette palette;

    public CometChatBannedMemberAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cometchat_data_item_row, parent, false);

        return new myViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        GroupMember groupMember = groupMemberList.get(position);
        holder.dataItem.groupMember(groupMember);

        checkWithConfigurations(holder.dataItem);
        holder.dataItem.setTag(R.string.banned_members, groupMember);
    }

    private void checkWithConfigurations(CometChatDataItem dataItem) {
//        if (!configurations.isEmpty()) {
//            dataItem.setConfigurations(configurations);
//        } else if (configuration != null && configuration instanceof DataItemConfiguration) {
//            dataItem.setConfiguration(configuration);
//        }
    }

    @Override
    public int getItemCount() {
        return groupMemberList.size();
    }

    public GroupMember getItemAtPosition(int position) {
        return groupMemberList.get(position);
    }

    public void clear() {
        groupMemberList.clear();
        notifyDataSetChanged();
    }

    public void add(GroupMember groupMember) {
        groupMemberList.add(groupMember);
        notifyDataSetChanged();
    }

    public void searchGroup(List<GroupMember> groupMember) {
        this.groupMemberList = groupMember;
        notifyDataSetChanged();
    }

    public void removeGroup(GroupMember groupMember) {
        if (groupMemberList.contains(groupMember)) {
            int index = groupMemberList.indexOf(groupMember);
            this.groupMemberList.remove(groupMember);
            notifyItemRemoved(index);
        }

    }

    public void updateGroup(GroupMember groupMember) {
        if (groupMemberList.contains(groupMember)) {
            int index = groupMemberList.indexOf(groupMember);
            groupMemberList.remove(index);
            groupMemberList.add(index, groupMember);
            notifyItemChanged(index);
        } else {
            groupMemberList.add(groupMember);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<GroupMember> groupMembers) {
        for (int i = 0; i < groupMembers.size(); i++) {
            if (groupMemberList.contains(groupMembers.get(i))) {
                int index = groupMemberList.indexOf(groupMembers.get(i));
                groupMemberList.remove(index);
                groupMemberList.add(index, groupMembers.get(i));
            } else {
                groupMemberList.add(groupMembers.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        this.configuration = configuration;
    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        this.configurations = configurations;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CometChatDataItem dataItem;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            dataItem = itemView.findViewById(R.id.dataItem);
        }
    }
}
