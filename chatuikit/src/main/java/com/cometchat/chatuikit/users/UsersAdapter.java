package com.cometchat.chatuikit.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderAdapter;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements StickyHeaderAdapter<UsersAdapter.InitialHolder> {
    private List<User> userList;
    private Context context;
    HashMap<User, Boolean> selectedUsers;
    private Function2<Context,User, View> subtitle, tail, customView;
    private Palette palette;
    private Typography typography;
    private boolean hideItemSeparator = true;
    private boolean disableUsersPresence;
    private @ColorInt
    int onlineStatusColor;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private String sectionHeaderTextFont;
    private @StyleRes
    int sectionHeaderTextAppearance;
    private @ColorInt
    int sectionHeaderTextColor;
    private @ColorInt
    int separatorColor;
    private ListItemStyle listItemStyle;
    private @DrawableRes int selectionIcon;

    public UsersAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        userList = new ArrayList<>();
        selectUser(new HashMap<>());
        setOnlineStatusColor(palette.getSuccess());
        setAvatarStyle(new AvatarStyle());
        setListItemStyle(new ListItemStyle());
        setStatusIndicatorStyle(new StatusIndicatorStyle());
        setSeparatorColor(palette.getAccent100());
        setSectionHeaderTextColor(palette.getAccent500());
        setSectionHeaderTextAppearance(typography.getText3());
        selectionIcon=R.drawable.ic_circle_check;
    }

    @NonNull
    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.MyViewHolder holder, int position) {
        User user = userList.get(position);
        if (getCustomView(user) == null) {
            holder.cometChatListItem.setAvatar(user.getAvatar(), user.getName());
            holder.cometChatListItem.setTitle(user.getName());
            holder.cometChatListItem.hideSeparator(hideItemSeparator);
            holder.cometChatListItem.setSubtitleView(getSubtitle(user));
            holder.cometChatListItem.setTailView(getTailView(user));
            if (user.getStatus().equalsIgnoreCase(CometChatConstants.USER_STATUS_ONLINE)) {
                holder.cometChatListItem.setStatusIndicatorColor(onlineStatusColor);
                holder.cometChatListItem.hideStatusIndicator(disableUsersPresence);
            } else {
                holder.cometChatListItem.hideStatusIndicator(true);
            }
            if (!selectedUsers.isEmpty()) {
                if (selectedUsers.containsKey(user)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    holder.cometChatListItem.statusIndicatorDimensions(20, 20);
                    holder.cometChatListItem.setStatusIndicatorIcon(selectionIcon);
                }
            }
            holder.cometChatListItem.setAvatarStyle(avatarStyle);
            holder.cometChatListItem.setStatusIndicatorStyle(statusIndicatorStyle);
            holder.cometChatListItem.setStyle(listItemStyle);
        } else {
            holder.parentLayout.removeAllViews();
            holder.parentLayout.addView(getCustomView(user));
        }
        holder.itemView.setTag(R.string.user, user);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setUserList(List<User> list) {
        if (list != null) {
            this.userList = list;
            notifyDataSetChanged();
        }
    }

    public void setDisableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
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
            if (listItemStyle.getSeparatorColor() != 0) {
                hideItemSeparator = false;
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

    public void selectUser(HashMap<User, Boolean> hashMap) {
        if (hashMap != null) {
            this.selectedUsers = hashMap;
            notifyDataSetChanged();
        }
    }

    public void setSubtitle(Function2<Context,User, View>subtitle) {
        if (subtitle != null) {
            this.subtitle = subtitle;
            notifyDataSetChanged();
        }
    }

    public void setTailView(Function2<Context,User, View> tailView) {
        if (tailView != null) {
            this.tail = tailView;
            notifyDataSetChanged();
        }
    }

    public void setSectionHeaderTextFont(String sectionHeaderTextFont) {
        if (sectionHeaderTextFont != null) {
            this.sectionHeaderTextFont = sectionHeaderTextFont;
            notifyDataSetChanged();
        }
    }

    public void setSectionHeaderTextAppearance(int sectionHeaderTextAppearance) {
        if (sectionHeaderTextAppearance != 0) {
            this.sectionHeaderTextAppearance = sectionHeaderTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setSectionHeaderTextColor(int sectionHeaderTextColor) {
        if (sectionHeaderTextColor != 0) {
            this.sectionHeaderTextColor = sectionHeaderTextColor;
            notifyDataSetChanged();
        }
    }

    public void setSeparatorColor(int separatorColor) {
        if (separatorColor != 0) {
            this.separatorColor = separatorColor;
            hideItemSeparator = true;
            notifyDataSetChanged();
        }
    }

    public void setCustomView(Function2<Context,User, View> customView) {
        if (customView != null) {
            this.customView = customView;
            notifyDataSetChanged();
        }
    }

    private View getSubtitle(User user) {
        if (subtitle != null) return subtitle.apply(context,user);
        return null;
    }

    private View getTailView(User user) {
        if (tail != null) return tail.apply(context,user);
        return null;
    }

    private View getCustomView(User user) {
        if (customView != null) return customView.apply(context,user);
        return null;
    }

    @Override
    public long getHeaderId(int var1) {
        User user = this.userList.get(var1);
        char name = user.getName() != null && !user.getName().isEmpty() ? user.getName().substring(0, 1).toUpperCase().toCharArray()[0] : '#';
        return (int) name;
    }

    @Override
    public UsersAdapter.InitialHolder onCreateHeaderViewHolder(ViewGroup var1) {
        return new UsersAdapter.InitialHolder(LayoutInflater.from(var1.getContext())
                .inflate(R.layout.cometchat_userlist_header, var1, false));
    }

    @Override
    public void onBindHeaderViewHolder(UsersAdapter.InitialHolder var1, int var2, long var3) {
        User user = userList.get(var2);
        char name = user.getName() != null && !user.getName().isEmpty() ? user.getName().substring(0, 1).toCharArray()[0] : '#';
        var1.separator.setBackgroundColor(separatorColor);
        var1.textView.setTextColor(sectionHeaderTextColor);
        var1.textView.setTextAppearance(context, sectionHeaderTextAppearance);
        if (sectionHeaderTextFont != null)
            var1.textView.setTypeface(FontUtils.getInstance(context).getTypeFace(sectionHeaderTextFont));
        var1.textView.setText(String.valueOf(name));
    }

    public User getUser(int pos) {
        return userList.get(pos);
    }

    public void setSelectionIcon(int selectionIcon) {
        if(selectionIcon!=0){
            this.selectionIcon=selectionIcon;
            notifyDataSetChanged();
        }
    }

    class InitialHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final View separator;
        private final RelativeLayout stickyView;

        InitialHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_char);
            separator = itemView.findViewById(R.id.list_item_separator);
            stickyView = itemView.findViewById(R.id.sticky_view);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CometChatListItem cometChatListItem;
        private LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cometChatListItem = itemView.findViewById(R.id.list_item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPx(context, 56));
            cometChatListItem.setLayoutParams(params);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
