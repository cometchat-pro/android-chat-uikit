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
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;
import com.cometchatworkspace.resources.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

public class CometChatGroupAdapter extends RecyclerView.Adapter<CometChatGroupAdapter.myGroupViewHolder> {

    private final Context context;

    private List<Group> groupList = new ArrayList<>();

    private final FontUtils fontUtils;

    private CometChatConfigurations configuration;
    private List<CometChatConfigurations> configurations = new ArrayList();



    public CometChatGroupAdapter(Context context) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    @NonNull
    @Override
    public myGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cometchat_data_item_row, parent, false);

        return new CometChatGroupAdapter.myGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myGroupViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        Group group = groupList.get(position);
        holder.dataItem.group(group);
        holder.dataItem.hideSeparator(false);
        checkWithConfigurations(holder.dataItem);
        holder.itemView.setTag(R.string.group, group);

    }
    private void checkWithConfigurations(CometChatDataItem listItem) {
        if (configurations != null && !configurations.isEmpty()) {
            for (CometChatConfigurations cometChatConfigurations : configurations) {
                configuration = cometChatConfigurations;
                setConfiguration(listItem);
            }
        } else if (configuration != null) {
            setConfiguration(listItem);
        }
    }

    private void setConfiguration(CometChatDataItem listItem) {
        if (configuration instanceof DataItemConfiguration) {
            listItem.inputData(((DataItemConfiguration) configuration).getInputData());
        }else {
            listItem.setConfiguration(configuration);
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
        notifyItemInserted(groupList.size() - 1);
    }

    public Group getGroup(Group group) {
        if (groupList.contains(group))
            return groupList.get(groupList.indexOf(group));
        else
            return null;
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

    public class myGroupViewHolder extends RecyclerView.ViewHolder {
        CometChatDataItem dataItem;

        public myGroupViewHolder(View view) {
            super(view);
            dataItem = view.findViewById(R.id.dataItem);
        }
    }
}
