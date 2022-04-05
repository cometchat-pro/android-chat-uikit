package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroups;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.GroupListItemConfiguration;
import com.cometchatworkspace.resources.utils.FontUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CometChatGroupAdapter extends RecyclerView.Adapter<CometChatGroupAdapter.myGroupViewHolder> {

    private final Context context;

    private List<Group> groupList = new ArrayList<>();

    private final FontUtils fontUtils;

    private CometChatConfigurations configuration;
    private List<CometChatConfigurations> configurations = new ArrayList();

    private boolean isAvatarHidden, isTitleHidden, isSubtitleHidden;
    private int titleColor, subTitleColor, backgroundColor;
    private float cornerRadius;

    public CometChatGroupAdapter(Context context) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    @NonNull
    @Override
    public myGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cometchat_grouplist_row, parent, false);

        return new CometChatGroupAdapter.myGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myGroupViewHolder holder, int position) {
        Group group = groupList.get(position);

        String memberMessage;
        if (group.getMembersCount() <= 1)
            memberMessage = group.getMembersCount() + " " + context.getString(R.string.member);
        else
            memberMessage = group.getMembersCount() + "" + context.getString(R.string.members);

        holder.group_list_item.setGroup(group);
        /**
         * @InputData is a class which is helpful to set data into the view and control visibility
         * as per value passed in constructor .
         * i.e we can control the visibility of the component inside the CometChatUserListItem,
         * and also decide what value i need to show in that particular view
         */
//        InputData inputData = new InputData(group.getGuid(), group.getIcon(), group.getName(), memberMessage);
//        holder.group_list_item.inputData(inputData);

        holder.group_list_item.setTag(R.string.group, group);

        if (configuration instanceof GroupListItemConfiguration) {
            holder.group_list_item.inputData(((GroupListItemConfiguration) configuration).get());
        }

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public Group getItemAtPosition(int position) {
        return groupList.get(position);
    }

    public void clear() {
        groupList.clear();
        notifyDataSetChanged();
    }

    public void updateGroup(int index, Group group) {
        if (groupList.contains(group)) {
            groupList.remove(group);
            groupList.add(index, group);
            notifyDataSetChanged();
        }
    }

    public void add(int index, Group group) {
        groupList.add(index, group);
        notifyItemInserted(index);

    }

    public void add(Group group) {
        groupList.add(group);
    }

    public void searchGroup(List<Group> groups) {
        this.groupList = groups;
        notifyDataSetChanged();
    }

    public void removeGroup(Group group) {
        if (groupList.contains(group)) {
            int index = groupList.indexOf(group);
            this.groupList.remove(group);
            notifyItemRemoved(index);
        }

    }

    public void updateGroup(Group group) {
        if (groupList.contains(group)) {
            int index = groupList.indexOf(group);
            groupList.remove(index);
            groupList.add(index, group);
            notifyItemChanged(index);
        } else {
            groupList.add(group);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Group> groups) {
        for (int i = 0; i < groups.size(); i++) {
            if (groupList.contains(groups.get(i))) {
                int index = groupList.indexOf(groups.get(i));
                groupList.remove(index);
                groupList.add(index, groups.get(i));
            } else {
                groupList.add(groups.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        this.configurations = configurations;

    }

    public void setConversationListItemProperty(boolean hideAvatar, boolean hideTitleListItem, int titleColorListItem, boolean hideSubtitleListItem, int subTitleColorListItem, int backgroundColorListItem, float cornerRadiusListItem) {

        isAvatarHidden = hideAvatar;
        isTitleHidden = hideTitleListItem;
        titleColor = titleColorListItem;
        isSubtitleHidden = hideSubtitleListItem;
        subTitleColor = subTitleColorListItem;
        backgroundColor = backgroundColorListItem;
        cornerRadius = cornerRadiusListItem;
        notifyDataSetChanged();
    }

    public class myGroupViewHolder extends RecyclerView.ViewHolder {
        CometChatGroupListItem group_list_item;

        public myGroupViewHolder(View view) {
            super(view);
            group_list_item = view.findViewById(R.id.group_list_item);
        }
    }
}
