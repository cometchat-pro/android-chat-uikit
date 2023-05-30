package com.cometchat.chatuikit.groupmembers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.MyViewHolder> {
    private List<GroupMember> groupMemberList;
    private Context context;
    HashMap<GroupMember, Boolean> selectedGroupMembers;
    private Function3<Context,GroupMember, Group, View> subtitle, tail, customView;
    private Palette palette;
    private Typography typography;
    private Group group;
    private boolean hideItemSeparator = true;
    private boolean disableGroupMembersPresence;
    private @ColorInt
    int onlineStatusColor;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private @ColorInt
    int separatorColor;
    private ListItemStyle listItemStyle;
    private @DrawableRes
    int selectionIcon;
    private GroupMembersViewModel groupMemberViewModel;

    public GroupMembersAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        groupMemberList = new ArrayList<>();
        selectGroupMember(new HashMap<>());
        setOnlineStatusColor(palette.getSuccess());
        setAvatarStyle(new AvatarStyle());
        setListItemStyle(new ListItemStyle());
        setStatusIndicatorStyle(new StatusIndicatorStyle());
        setSeparatorColor(palette.getAccent100());
        selectionIcon = R.drawable.ic_circle_check;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GroupMember groupMember = groupMemberList.get(position);
        if (getCustomView(groupMember, group) == null) {
            holder.cometChatListItem.setAvatar(groupMember.getAvatar(), groupMember.getName());
            holder.cometChatListItem.setTitle(groupMember.getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()) ? "Me" : groupMember.getName());
            holder.cometChatListItem.hideSeparator(hideItemSeparator);
            holder.cometChatListItem.setSubtitleView(getSubtitle(groupMember, group));
            if (getTailView(groupMember, group) == null) {
                setTailView(groupMember, holder);
            } else holder.cometChatListItem.setTailView(getTailView(groupMember, group));
            if (groupMember.getStatus().equalsIgnoreCase(CometChatConstants.USER_STATUS_ONLINE)) {
                holder.cometChatListItem.setStatusIndicatorColor(onlineStatusColor);
                holder.cometChatListItem.hideStatusIndicator(disableGroupMembersPresence);
            } else {
                holder.cometChatListItem.hideStatusIndicator(true);
            }
            if (!selectedGroupMembers.isEmpty()) {
                if (selectedGroupMembers.containsKey(groupMember)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    holder.cometChatListItem.statusIndicatorDimensions(20, 20);
                    holder.cometChatListItem.setStatusIndicatorIcon(selectionIcon);
                }
            }
            holder.cometChatListItem.setAvatarStyle(avatarStyle);
            holder.cometChatListItem.setStatusIndicatorStyle(statusIndicatorStyle);
            holder.cometChatListItem.setStyle(listItemStyle);
            holder.cometChatListItem.setSeparatorColor(separatorColor);
        } else {
            holder.parentLayout.removeAllViews();
            holder.parentLayout.addView(getCustomView(groupMember, group));
        }
        holder.itemView.setTag(R.string.member, groupMember);

    }

    private void setTailView(GroupMember groupMember, MyViewHolder holder) {
        if (group != null) {
            TextView textView = new TextView(context);
            textView.setTextColor(palette.getAccent600());

            if (groupMember.getUid().equals(group.getOwner()))
                textView.setText(context.getResources().getString(R.string.owner));
            else textView.setText(groupMember.getScope());

            if (group != null) {
                if (CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(group.getScope())) {
                    holder.cometChatListItem.setTailView(textView);
                } else {
                    if (CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group.getScope()) && CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(groupMember.getScope())) {
                        holder.cometChatListItem.setTailView(setSpinner(groupMember));
                    } else if ((CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group.getScope())) && (CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(groupMember.getScope()) || CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(groupMember.getScope()))) {
                        holder.cometChatListItem.setTailView(setSpinner(groupMember));
                    } else if (CometChatUIKit.getLoggedInUser().getUid().equals(group.getOwner()) && groupMember.getUid() != null && !groupMember.getUid().equals(group.getOwner())) {
                        holder.cometChatListItem.setTailView(setSpinner(groupMember));
                    } else {
                        holder.cometChatListItem.setTailView(textView);
                    }
                }

            }
        }
    }

    public View setSpinner(GroupMember scope) {
        Spinner spinner = new Spinner(context);
        ScopeChangeAdapter scopeChangeAdapter = new ScopeChangeAdapter(context, scope, group, getScopes(scope));
        spinner.setAdapter(scopeChangeAdapter);
        spinner.setBackground(context.getDrawable(R.drawable.spinner_style));
        spinner.setDropDownVerticalOffset(100);
        spinner.setPopupBackgroundDrawable(context.getDrawable(R.drawable.curved_bg));
        scopeChangeAdapter.setClickListener((groupMember, group, scope1) -> changeScope(groupMember, scope1));
        return spinner;

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

    private void changeScope(GroupMember groupMember, String scope) {
        groupMemberViewModel.scopeChange(groupMember,scope);
    }

    public void setViewModel(GroupMembersViewModel viewModel){
        if(viewModel!=null) this.groupMemberViewModel=viewModel;
    }

    @Override
    public int getItemCount() {
        return groupMemberList.size();
    }

    public void setGroupMemberList(List<GroupMember> list) {
        if (list != null) {
            this.groupMemberList = list;
            notifyDataSetChanged();
        }
    }

    public void disableUsersPresence(boolean disableGroupMembersPresence) {
        this.disableGroupMembersPresence = disableGroupMembersPresence;
        notifyDataSetChanged();
    }

    public void setOnlineStatusColor(int onlineStatusColor) {
        if (onlineStatusColor != 0) {
            this.onlineStatusColor = onlineStatusColor;
            notifyDataSetChanged();
        }

    }

    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            if (avatarStyle.getCornerRadius() < 0) {
                avatarStyle.setOuterCornerRadius(100);
            }
            if (avatarStyle.getInnerBackgroundColor() == 0) {
                avatarStyle.setInnerBackgroundColor(palette.getAccent600());
            }
            if (avatarStyle.getTextColor() == 0) {
                avatarStyle.setTextColor(palette.getAccent900());
            }
            if (avatarStyle.getTextAppearance() == 0) {
                avatarStyle.setTextAppearance(typography.getName());
            }
            this.avatarStyle = avatarStyle;
            notifyDataSetChanged();
        }
    }

    public void setListItemStyle(ListItemStyle listItemStyle) {
        if (listItemStyle != null) {
            if (listItemStyle.getTitleColor() == 0) {
                listItemStyle.setTitleColor(palette.getAccent());
            }
            if (listItemStyle.getTitleAppearance() == 0) {
                listItemStyle.setTitleAppearance(typography.getName());
            }
            if (listItemStyle.getSeparatorColor() == 0) {
                listItemStyle.setSeparatorColor(palette.getAccent100());
            }
            this.listItemStyle = listItemStyle;
            notifyDataSetChanged();
        }
    }

    public void setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        if (statusIndicatorStyle != null) {
            this.statusIndicatorStyle = statusIndicatorStyle;
            notifyDataSetChanged();
        }
    }

    public void selectGroupMember(HashMap<GroupMember, Boolean> hashMap) {
        if (hashMap != null) {
            this.selectedGroupMembers = hashMap;
            notifyDataSetChanged();
        }
    }

    public void setSubtitle(Function3<Context,GroupMember, Group, View> subtitle) {
        if (subtitle != null) {
            this.subtitle = subtitle;
            notifyDataSetChanged();
        }
    }

    public void setTailView(Function3<Context,GroupMember, Group, View> tailView) {
        if (tailView != null) {
            this.tail = tailView;
            notifyDataSetChanged();
        }
    }

    public void setSeparatorColor(int separatorColor) {
        if (separatorColor != 0) {
            this.separatorColor = separatorColor;
            notifyDataSetChanged();
        }
    }

    public void setCustomView(Function3<Context,GroupMember, Group, View> customView) {
        if (customView != null) {
            this.customView = customView;
            notifyDataSetChanged();
        }
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            notifyDataSetChanged();
        }
    }

    private View getSubtitle(GroupMember groupMember, Group group) {
        if (subtitle != null) return subtitle.apply(context,groupMember, group);
        return null;
    }

    private View getTailView(GroupMember groupMember, Group group) {
        if (tail != null) return tail.apply(context,groupMember, group);
        return null;
    }

    private View getCustomView(GroupMember groupMember, Group group) {
        if (customView != null) return customView.apply(context,groupMember, group);
        return null;
    }

    public GroupMember getGroupMember(int pos) {
        return groupMemberList.get(pos);
    }

    public void hideSeparator(boolean hide) {
        hideItemSeparator = hide;
        notifyDataSetChanged();
    }

    public void setSelectionIcon(int selectionIcon) {
        if (selectionIcon != 0) {
            this.selectionIcon = selectionIcon;
            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CometChatListItem cometChatListItem;
        private LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cometChatListItem = itemView.findViewById(R.id.list_item);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
