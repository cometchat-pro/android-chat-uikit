package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroupMemberList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;
import com.cometchatworkspace.resources.constants.UIKitConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CometChatGroupMemberAdapter extends
        RecyclerView.Adapter<CometChatGroupMemberAdapter.GroupMemberViewHolder> {

    private final Context context;
    private List<GroupMember> groupMemberList = new ArrayList<>();
    private Group group;
    private CometChatConfigurations configuration;
    private List<CometChatConfigurations> configurations = new ArrayList();
    private final User loggedInUser = CometChat.getLoggedInUser();
    Palette palette;
    private boolean allowPromoteDemoteMembers;

    public CometChatGroupMemberAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);

    }

    @NonNull
    @Override
    public GroupMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cometchat_data_item_row, parent, false);

        return new GroupMemberViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull GroupMemberViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        GroupMember groupMember = groupMemberList.get(position);
        holder.groupMemberItem.groupMember(groupMember);
        setTailView(groupMember, holder);
        checkWithConfigurations(holder.groupMemberItem);
        holder.itemView.setTag(R.string.member, groupMember);
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
    private void setTailView(GroupMember groupMember, GroupMemberViewHolder holder) {
        TextView textView = new TextView(context);
        textView.setTextColor(palette.getAccent600());

        if (groupMember.getUid().equals(group.getOwner()))
            textView.setText(context.getResources().getString(R.string.owner));
        else
            textView.setText(groupMember.getScope());

        if (group != null) {
            if (allowPromoteDemoteMembers) {
                if (CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(group.getScope())) {
                    holder.groupMemberItem.setTailView(textView);
                } else {
                    if (CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group.getScope()) && CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(groupMember.getScope())) {
                        holder.groupMemberItem.setTailView(setSpinner(groupMember));
                    } else if ((CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group.getScope())) && (CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(groupMember.getScope()) || CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(groupMember.getScope()))) {
                        holder.groupMemberItem.setTailView(setSpinner(groupMember));
                    } else if (loggedInUser.getUid().equals(group.getOwner()) && groupMember.getUid() != null && !groupMember.getUid().equals(group.getOwner())) {
                        holder.groupMemberItem.setTailView(setSpinner(groupMember));
                    } else {
                        holder.groupMemberItem.setTailView(textView);
                    }
                }
            } else {
                holder.groupMemberItem.setTailView(textView);
            }

        }


    }

    public View setSpinner(GroupMember scope) {
        Spinner spinner = new Spinner(context);
        CustomAdapter customAdapter = new CustomAdapter(context, scope, group, getScopes(scope));
        spinner.setAdapter(customAdapter);
        spinner.setBackground(context.getDrawable(R.drawable.spinner_style));
        spinner.setDropDownVerticalOffset(100);
        spinner.setPopupBackgroundDrawable(context.getDrawable(R.drawable.curved_bg));
        customAdapter.setClickListener((groupMember, group, scope1) ->
                changeScope(groupMember, group.getGuid(), scope1)
        );
        return spinner;

    }

    private void changeScope(GroupMember groupMember, String group_id, String scope) {
        CometChat.updateGroupMemberScope(groupMember.getUid(), group_id, scope, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                //emit scope change event
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupMemberChangeScope(loggedInUser, groupMember, scope, groupMember.getScope(), group);
                }
                groupMember.setScope(scope);
                updateGroupMember(groupMember);
            }

            @Override
            public void onError(CometChatException e) {
                //emit error while changing scope
                for (CometChatGroupEvents ex : CometChatGroupEvents.groupEvents.values()) {
                    ex.onError(e);
                }
                updateGroupMember(groupMember);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public List<String> getScopes(GroupMember groupMember) {

        List<String> scopes = new ArrayList<>(Arrays.asList(context.getResources().getString(R.string.participant).toLowerCase(), context.getResources().getString(R.string.moderator).toLowerCase(), context.getResources().getString(R.string.admin).toLowerCase()));

        if (groupMember.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_MODERATOR)) {
            Collections.swap(scopes, 1, 0);
        } else if (groupMember.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_ADMIN)) {
            Collections.swap(scopes, 2, 0);
        }

        return scopes;


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

    public void searchGroupMember(List<GroupMember> groupMember) {
        this.groupMemberList = groupMember;
        notifyDataSetChanged();
    }

    public void removeGroupMember(GroupMember groupMember) {
        if (groupMemberList.contains(groupMember)) {
            int index = groupMemberList.indexOf(groupMember);
            this.groupMemberList.remove(groupMember);
            notifyItemRemoved(index);
        }

    }

    public void updateGroupMember(GroupMember groupMember) {
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
            GroupMember groupMember = groupMembers.get(i);
            if (selectedGroupMember != null && groupMember.equals(selectedGroupMember)) {
                groupMember.setStatus(UIKitConstants.SELECTED);
            }
            if (groupMemberList.contains(groupMember)) {
                int index = groupMemberList.indexOf(groupMember);
                groupMemberList.remove(index);
                groupMemberList.add(index, groupMember);
            } else {
                groupMemberList.add(groupMember);
            }
        }
        notifyDataSetChanged();
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private GroupMember selectedGroupMember;

    public void setSelectedMember(GroupMember groupMember) {
        if (groupMember != null) {
            if (!groupMember.equals(selectedGroupMember)) {
                if (selectedGroupMember != null) {
                    selectedGroupMember.setStatus(CometChatConstants.USER_STATUS_OFFLINE);
                    updateGroupMember(selectedGroupMember);
                }
                selectedGroupMember = groupMember;
                groupMember.setStatus(UIKitConstants.SELECTED);

            } else {
                selectedGroupMember = null;
                groupMember.setStatus(CometChatConstants.USER_STATUS_OFFLINE);
            }
            updateGroupMember(groupMember);
        }

    }


    public void setConfiguration(CometChatConfigurations configuration) {
        this.configuration = configuration;
    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        this.configurations = configurations;
    }


    public void allowPromoteDemoteMembers(boolean allowPromoteDemoteMembers) {
        this.allowPromoteDemoteMembers = allowPromoteDemoteMembers;
    }

    public class GroupMemberViewHolder extends RecyclerView.ViewHolder {
        CometChatDataItem groupMemberItem;

        public GroupMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            groupMemberItem = itemView.findViewById(R.id.dataItem);
        }
    }
}
