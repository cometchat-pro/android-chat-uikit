package com.cometchat.chatuikit.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.utils.SubtitleView;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {
    private List<Group> groupList;
    private Context context;
    HashMap<Group, Boolean> selectedGroups;
    private Function2<Context, Group, View> subtitle, tail, customView;
    private Palette palette;
    private Typography typography;
    private boolean hideItemSeparator;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private String subTitleTextFont;
    private @StyleRes
    int subTitleTextAppearance;
    private @ColorInt
    int subTitleTextColor;
    private @ColorInt
    int separatorColor;
    private @DrawableRes
    int privateGroupIcon;
    private @DrawableRes
    int passwordGroupIcon;
    private ListItemStyle listItemStyle;

    public GroupsAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        groupList = new ArrayList<>();
        selectGroup(new HashMap<>());
        setAvatarStyle(new AvatarStyle());
        setListItemStyle(new ListItemStyle());
        setStatusIndicatorStyle(new StatusIndicatorStyle());
        setSeparatorColor(palette.getAccent100());
        setSubTitleTextColor(palette.getAccent500());
        setSubTitleTextAppearance(typography.getSubtitle1());
        privateGroupIcon = R.drawable.ic_private_group;
        passwordGroupIcon = R.drawable.ic_password_group;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Group group = groupList.get(position);
        if (getCustomView(group) == null) {
            holder.cometChatListItem.setAvatar(group.getIcon(), group.getName());
            holder.cometChatListItem.setTitle(group.getName());
            holder.cometChatListItem.hideSeparator(hideItemSeparator);
            if (getSubtitle(group) == null) {
                SubtitleView subtitleView = new SubtitleView(context);
                subtitleView.hideReceipt(true);
                subtitleView.setSubtitleText(group.getMembersCount() > 1 ? group.getMembersCount() + " " + context.getString(R.string.members).toLowerCase() : group.getMembersCount() + " " + context.getString(R.string.member).toLowerCase());
                subtitleView.setSubtitleTextColor(subTitleTextColor);
                subtitleView.setSubtitleTextAppearance(subTitleTextAppearance);
                if (subTitleTextFont != null) subtitleView.setSubtitleTextFont(subTitleTextFont);
                holder.cometChatListItem.setSubtitleView(subtitleView);
            } else holder.cometChatListItem.setSubtitleView(getSubtitle(group));

            holder.cometChatListItem.setTailView(getTailView(group));
            if (group.getGroupType().equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
                holder.cometChatListItem.hideStatusIndicator(false);
                holder.cometChatListItem.setStatusIndicatorIcon(passwordGroupIcon);
            } else if (group.getGroupType().equals(CometChatConstants.GROUP_TYPE_PRIVATE)) {
                holder.cometChatListItem.hideStatusIndicator(false);
                holder.cometChatListItem.setStatusIndicatorIcon(privateGroupIcon);
            } else {
                holder.cometChatListItem.hideStatusIndicator(true);
            }
            if (!selectedGroups.isEmpty()) {
                if (selectedGroups.containsKey(group)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    holder.cometChatListItem.statusIndicatorDimensions(20, 20);
                    holder.cometChatListItem.setStatusIndicatorIcon(R.drawable.ic_circle_check);
                }
            }
            holder.cometChatListItem.setAvatarStyle(avatarStyle);
            holder.cometChatListItem.setStatusIndicatorStyle(statusIndicatorStyle);
            holder.cometChatListItem.setStyle(listItemStyle);
            holder.cometChatListItem.setSeparatorColor(separatorColor);
        } else {
            holder.parentLayout.removeAllViews();
            holder.parentLayout.addView(getCustomView(group));
        }
        holder.itemView.setTag(R.string.group, group);

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void setGroupList(List<Group> list) {
        if (list != null) {
            this.groupList = list;
            notifyDataSetChanged();
        }
    }

    public void setProtectedGroupIcon(int protectedGroupIcon) {
        if (protectedGroupIcon != 0) {
            this.passwordGroupIcon = protectedGroupIcon;
            notifyDataSetChanged();
        }
    }

    public void setPrivateGroupIcon(int privateGroupIcon) {
        if (privateGroupIcon != 0) {
            this.privateGroupIcon = privateGroupIcon;
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

    public void selectGroup(HashMap<Group, Boolean> hashMap) {
        if (hashMap != null) {
            this.selectedGroups = hashMap;
            notifyDataSetChanged();
        }
    }

    public void setSubtitle(Function2<Context, Group, View> subtitle) {
        if (subtitle != null) {
            this.subtitle = subtitle;
            notifyDataSetChanged();
        }
    }

    public void setTailView(Function2<Context, Group, View> tailView) {
        if (tailView != null) {
            this.tail = tailView;
            notifyDataSetChanged();
        }
    }

    public void setSubTitleTextFont(String sectionHeaderTextFont) {
        if (sectionHeaderTextFont != null) {
            this.subTitleTextFont = sectionHeaderTextFont;
            notifyDataSetChanged();
        }
    }

    public void setSubTitleTextAppearance(int sectionHeaderTextAppearance) {
        if (sectionHeaderTextAppearance != 0) {
            this.subTitleTextAppearance = sectionHeaderTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setSubTitleTextColor(int sectionHeaderTextColor) {
        if (sectionHeaderTextColor != 0) {
            this.subTitleTextColor = sectionHeaderTextColor;
            notifyDataSetChanged();
        }
    }

    public void setSeparatorColor(int separatorColor) {
        if (separatorColor != 0) {
            this.separatorColor = separatorColor;
            notifyDataSetChanged();
        }
    }

    public void hideSeparator(boolean hide) {
        hideItemSeparator = hide;
        notifyDataSetChanged();
    }

    public void setCustomView(Function2<Context, Group, View> customView) {
        if (customView != null) {
            this.customView = customView;
            notifyDataSetChanged();
        }
    }

    private View getSubtitle(Group group) {
        if (subtitle != null) return subtitle.apply(context,group);
        return null;
    }

    private View getTailView(Group group) {
        if (tail != null) return tail.apply(context,group);
        return null;
    }

    private View getCustomView(Group group) {
        if (customView != null) return customView.apply(context,group);
        return null;
    }

    public Group getGroup(int pos) {
        return groupList.get(pos);
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
