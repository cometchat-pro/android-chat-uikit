package com.cometchat.chatuikit.bannedmembers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BannedMembersAdapter extends RecyclerView.Adapter<BannedMembersAdapter.MyViewHolder> {
    private List<GroupMember> groupMemberList;
    private Context context;
    HashMap<GroupMember, Boolean> selectedGroupMembers;
    private Function3<Context, GroupMember, Group, View> subtitle, tail, listItemView;
    private Palette palette;
    private Typography typography;
    private boolean hideItemSeparator = true;
    private boolean disableGroupMembersPresence=true;
    private @ColorInt
    int onlineStatusColor;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private @ColorInt
    int separatorColor;
    private ListItemStyle listItemStyle;
    private @DrawableRes
    int selectionIcon;
    private Group group;

    public BannedMembersAdapter(Context context) {
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
        if (getCustomView(groupMember) == null) {
            holder.cometChatListItem.setAvatar(groupMember.getAvatar(), groupMember.getName());
            holder.cometChatListItem.setTitle(groupMember.getName());
            holder.cometChatListItem.hideSeparator(hideItemSeparator);
            holder.cometChatListItem.setSubtitleView(getSubtitle(groupMember));
            if (getTailView(groupMember) == null) {
                TextView textView = new TextView(context);
                textView.setText(context.getResources().getString(R.string.banned));
                textView.setTextColor(palette.getAccent500());
                textView.setTextAppearance(context, typography.getSubtitle1());
                holder.cometChatListItem.setTailView(textView);
            } else holder.cometChatListItem.setTailView(getTailView(groupMember));
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
            holder.parentLayout.addView(getCustomView(groupMember));
        }
        holder.itemView.setTag(R.string.member, groupMember);

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

    public void setSubtitle(Function3<Context, GroupMember, Group, View> subtitle) {
        if (subtitle != null) {
            this.subtitle = subtitle;
            notifyDataSetChanged();
        }
    }

    public void setTailView(Function3<Context, GroupMember, Group, View> tailView) {
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

    public void setListItemView(Function3<Context, GroupMember, Group, View> listItemView) {
        if (listItemView != null) {
            this.listItemView = listItemView;
            notifyDataSetChanged();
        }
    }

    private View getSubtitle(GroupMember groupMember) {
        if (subtitle != null) return subtitle.apply(context, groupMember, group);
        return null;
    }

    private View getTailView(GroupMember groupMember) {
        if (tail != null) return tail.apply(context, groupMember, group);
        return null;
    }

    private View getCustomView(GroupMember groupMember) {
        if (listItemView != null) return listItemView.apply(context, groupMember, group);
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

    public void setGroup(Group group) {
        this.group = group;
        notifyDataSetChanged();
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
