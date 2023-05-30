package com.cometchat.chatuikit.conversations;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.utils.ConversationTailView;
import com.cometchat.chatuikit.shared.utils.ConversationsUtils;
import com.cometchat.chatuikit.shared.utils.MessageReceiptUtils;
import com.cometchat.chatuikit.shared.utils.SubtitleView;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatBadge.BadgeStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.Pattern;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.MyViewHolder> {

    private Context context;
    private List<Conversation> conversationsList;
    public boolean isTyping;
    private Function2<Context, Conversation, View> subtitle, tail, customView;
    private Palette palette;
    private Typography typography;
    private HashMap<Conversation, TypingIndicator> typingIndicatorHashMap;

    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private DateStyle dateStyle;
    private ListItemStyle listItemStyle;
    private BadgeStyle badgeStyle;

    private boolean disableUsersPresence;
    private boolean disableReadReceipt;
    private boolean disableTyping;
    private @DrawableRes
    int protectedGroupIcon;
    private @DrawableRes
    int privateGroupIcon;
    private Drawable readIcon;
    private Drawable deliveredIcon;
    private Drawable sentIcon;
    private boolean hideSeparator;
    private HashMap<Conversation, Boolean> selectedConversation;
    private Function1<Conversation, String> datePattern;
    private String lastMessageTextFont;
    private String typingIndicatorTextFont;
    private String threadIndicatorTextFont;
    private @ColorInt
    int onlineStatusColor;
    private @ColorInt
    int separatorColor;
    private @ColorInt
    int lastMessageTextColor;
    private @ColorInt
    int typingIndicatorTextColor;
    private @ColorInt
    int threadIndicatorTextColor;
    private @StyleRes
    int lastMessageTextAppearance;
    private @StyleRes
    int typingIndicatorTextAppearance;
    private @StyleRes
    int threadIndicatorTextAppearance;

    public ConversationsAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        conversationsList = new ArrayList<>();
        selectedConversation = new HashMap<>();
        typingIndicatorHashMap = new HashMap<>();

        setAvatarStyle(new AvatarStyle());
        setDateStyle(new DateStyle());
        setBadgeStyle(new BadgeStyle());
        setStatusIndicatorStyle(new StatusIndicatorStyle());
        setListItemStyle(new ListItemStyle());

        setLastMessageTextAppearance(typography.getSubtitle1());
        setThreadIndicatorTextAppearance(typography.getSubtitle1());
        setTypingIndicatorTextAppearance(typography.getSubtitle1());

        setOnlineStatusColor(palette.getSuccess());
        setSeparatorColor(palette.getAccent100());
        setLastMessageTextColor(palette.getAccent600());
        setTypingIndicatorTextColor(palette.getPrimary());
        setThreadIndicatorTextColor(palette.getAccent600());
    }

    @NonNull
    @Override
    public ConversationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsAdapter.MyViewHolder holder, int position) {
        Conversation conversation = conversationsList.get(position);
        if (getCustomView(conversation) == null) {
            String title = ConversationsUtils.getConversationTitle(conversation);
            holder.cometChatListItem.setAvatar(ConversationsUtils.getConversationIcon(conversation), title);
            holder.cometChatListItem.setTitle(title);
            holder.cometChatListItem.hideSeparator(hideSeparator);
            if (getSubtitle(conversation) == null) {
                SubtitleView subtitleView = new SubtitleView(context);
                subtitleView.setTypingIndicatorColor(typingIndicatorTextColor);
                subtitleView.setTypingIndicatorTextAppearance(typingIndicatorTextAppearance);
                subtitleView.setTypingIndicatorFont(typingIndicatorTextFont);
                if (conversation.getLastMessage() != null && conversation.getLastMessage().getParentMessageId() != 0) {
                    subtitleView.showHelperText(conversation.getLastMessage().getParentMessageId() != 0);
                    subtitleView.setHelperTextTextAppearance(threadIndicatorTextAppearance);
                    subtitleView.setHelperTextFont(threadIndicatorTextFont);
                    subtitleView.setHelperTextColor(threadIndicatorTextColor);
                }
                subtitleView.setSubtitleTextColor(lastMessageTextColor);
                subtitleView.setSubtitleTextAppearance(lastMessageTextAppearance);
                subtitleView.setSubtitleTextFont(lastMessageTextFont);
                if (!disableTyping) {
                    if (!typingIndicatorHashMap.isEmpty() && typingIndicatorHashMap.containsKey(conversation)) {
                        TypingIndicator typingIndicator = typingIndicatorHashMap.get(conversation);
                        if (typingIndicator != null) {
                            if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                                subtitleView.setTypingIndicatorText(context.getString(R.string.is_typing));
                            } else {
                                subtitleView.setTypingIndicatorText(typingIndicator.getSender().getName() + " " + context.getString(R.string.is_typing));
                            }
                            subtitleView.showTypingIndicator(true);
                            subtitleView.hideSubtitle(true);
                        } else {
                            subtitleView.showTypingIndicator(false);
                            subtitleView.hideSubtitle(false);
                        }
                    } else {
                        subtitleView.showTypingIndicator(false);
                        subtitleView.hideSubtitle(false);
                    }
                }
                subtitleView.getCometChatReceipt().setSentIcon(sentIcon);
                subtitleView.getCometChatReceipt().setDeliveredIcon(deliveredIcon);
                subtitleView.getCometChatReceipt().setReadIcon(readIcon);
                subtitleView.setReceipt(MessageReceiptUtils.MessageReceipt(conversation.getLastMessage()));
                if (!disableReadReceipt)
                    subtitleView.hideReceipt(MessageReceiptUtils.hideReceipt(conversation.getLastMessage()));
                else
                    subtitleView.hideReceipt(true);

                subtitleView.setSubtitleText(ChatConfigurator.getUtils().getLastConversationMessage(context, conversation));
                holder.cometChatListItem.setSubtitleView(subtitleView);
            } else holder.cometChatListItem.setSubtitleView(getSubtitle(conversation));

            if (getTailView(conversation) == null) {
                ConversationTailView tailView = new ConversationTailView(context);
                tailView.setBadgeCount(conversation.getUnreadMessageCount());
                tailView.getBadge().setStyle(badgeStyle);
                tailView.getDate().setDate(conversation.getUpdatedAt(), Pattern.DAY_DATE_TIME);
                tailView.getDate().setCustomDateString(getDatePattern(conversation));
                tailView.getDate().setStyle(dateStyle);
                holder.cometChatListItem.setTailView(tailView);
            } else holder.cometChatListItem.setTailView(getSubtitle(conversation));
            if (UIKitConstants.ConversationType.USERS.equalsIgnoreCase(conversation.getConversationType())) {
                if (((User) conversation.getConversationWith()).getStatus().equalsIgnoreCase(CometChatConstants.USER_STATUS_ONLINE)) {
                    holder.cometChatListItem.setStatusIndicatorColor(onlineStatusColor);
                    holder.cometChatListItem.hideStatusIndicator(disableUsersPresence);
                } else {
                    holder.cometChatListItem.hideStatusIndicator(true);
                }
            } else if (UIKitConstants.ConversationType.GROUPS.equalsIgnoreCase(conversation.getConversationType())) {
                if (((Group) conversation.getConversationWith()).getGroupType().equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    if (protectedGroupIcon == 0)
                        holder.cometChatListItem.setStatusIndicatorIcon(R.drawable.ic_password_group);
                    else
                        holder.cometChatListItem.setStatusIndicatorIcon(protectedGroupIcon);
                } else if (((Group) conversation.getConversationWith()).getGroupType().equals(CometChatConstants.GROUP_TYPE_PRIVATE)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    if (privateGroupIcon == 0)
                        holder.cometChatListItem.setStatusIndicatorIcon(R.drawable.ic_private_group);
                    else
                        holder.cometChatListItem.setStatusIndicatorIcon(privateGroupIcon);
                } else {
                    holder.cometChatListItem.hideStatusIndicator(true);
                }
            }
            if (!selectedConversation.isEmpty()) {
                if (selectedConversation.containsKey(conversation)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    holder.cometChatListItem.statusIndicatorDimensions(20, 20);
                    holder.cometChatListItem.setStatusIndicatorIcon(R.drawable.ic_circle_check);
                }
            }
            holder.cometChatListItem.setStyle(listItemStyle);
            holder.cometChatListItem.setSeparatorColor(separatorColor);
            holder.cometChatListItem.setAvatarStyle(avatarStyle);
            holder.cometChatListItem.setStatusIndicatorStyle(statusIndicatorStyle);
        } else {
            holder.parentLayout.removeAllViews();
            holder.parentLayout.addView(getCustomView(conversation));
        }
        holder.itemView.setTag(R.string.conversation, conversation);
    }

    @Override
    public int getItemCount() {
        return conversationsList.size();
    }

    public void setList(List<Conversation> conversations) {
        conversationsList = conversations;
        notifyDataSetChanged();
    }

    public Conversation getConversation(int position) {
        try {
            return conversationsList.get(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setLastMessageTextAppearance(int lastMessageTextAppearance) {
        if (lastMessageTextAppearance != 0) {
            this.lastMessageTextAppearance = lastMessageTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setTypingIndicatorTextAppearance(int typingIndicatorTextAppearance) {
        if (typingIndicatorTextAppearance != 0) {
            this.typingIndicatorTextAppearance = typingIndicatorTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setThreadIndicatorTextAppearance(int threadIndicatorTextAppearance) {
        if (threadIndicatorTextAppearance != 0) {
            this.threadIndicatorTextAppearance = threadIndicatorTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void addList(List<Conversation> conversations) {
        for (int i = 0; i < conversations.size(); i++) {
            if (conversationsList.contains(conversations.get(i))) {
                int index = conversationsList.indexOf(conversations.get(i));
                conversationsList.remove(conversations.get(i));
                conversationsList.add(index, conversations.get(i));
            } else {
                conversationsList.add(conversations.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void updateConversation(Conversation conversation) {
        if (conversationsList.contains(conversation)) {
            conversationsList.set(conversationsList.indexOf(conversation), conversation);
            notifyItemChanged(conversationsList.indexOf(conversation), conversation);
        }
    }

    public void notifyNewConversationAdded(Conversation conversation) {
        conversationsList.add(0, conversation);
        notifyItemInserted(0);
    }

    public void notifyMoveConversationToTop(Conversation conversation) {
        if (conversationsList.contains(conversation)) {
            notifyItemMoved(conversationsList.indexOf(conversation), 0);
            conversationsList.remove(conversation);
            conversationsList.add(0, conversation);
        }
    }

    public void add(Conversation conversation) {
        if (!conversationsList.contains(conversation)) {
            conversationsList.add(conversation);
            notifyItemInserted(conversationsList.size());
        }
    }

    public void addMessage(Conversation conversation) {
        if (conversationsList.contains(conversation)) {
            int oldIndex = conversationsList.indexOf(conversation);
            conversationsList.remove(conversation);
            conversationsList.add(0, conversation);
            notifyItemMoved(oldIndex, 0);
        } else {
            conversationsList.add(0, conversation);
            notifyItemInserted(0);
        }
        notifyItemChanged(0);
    }

    public void add(int position, Conversation conversation) {
        if (!conversationsList.contains(conversation)) {
            conversationsList.add(position, conversation);
        }
    }

    public void remove(int position) {
        conversationsList.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(Conversation conversation) {
        int position = conversationsList.indexOf(conversation);
        conversationsList.remove(conversation);
        notifyItemRemoved(position);
    }

    public void typing(HashMap<Conversation, TypingIndicator> hashMap) {
        for (Map.Entry map : hashMap.entrySet()) {
            if (conversationsList.contains(map.getKey())) {
                typingIndicatorHashMap = hashMap;
                notifyItemChanged(conversationsList.indexOf(map.getKey()));
            }
        }

    }

    public void setSubtitle(Function2<Context, Conversation, View> subtitle) {
        isTyping = false;
        if (subtitle != null) {
            this.subtitle = subtitle;
            notifyDataSetChanged();
        }
    }

    public void setTailView(Function2<Context, Conversation, View> tailView) {
        isTyping = false;
        if (tailView != null) {
            this.tail = tailView;
            notifyDataSetChanged();
        }
    }

    public void setCustomView(Function2<Context, Conversation, View> customView) {
        isTyping = false;
        if (customView != null) {
            this.customView = customView;
            notifyDataSetChanged();
        }
    }

    private View getSubtitle(Conversation conversation) {
        if (subtitle != null) return subtitle.apply(context, conversation);
        return null;
    }

    private View getTailView(Conversation conversation) {
        if (tail != null) return tail.apply(context, conversation);
        return null;
    }

    private View getCustomView(Conversation conversation) {
        if (customView != null) return customView.apply(context, conversation);
        return null;
    }

    public void clear() {
        conversationsList.clear();
        notifyDataSetChanged();
    }

    public void setLastMessageTextFont(String lastMessageTextFont) {
        if (lastMessageTextFont != null) {
            this.lastMessageTextFont = lastMessageTextFont;
            notifyDataSetChanged();
        }

    }

    public void setTypingIndicatorTextFont(String typingIndicatorTextFont) {
        if (typingIndicatorTextFont != null) {
            this.typingIndicatorTextFont = typingIndicatorTextFont;
            notifyDataSetChanged();
        }

    }

    public void setThreadIndicatorTextFont(String threadIndicatorTextFont) {
        if (threadIndicatorTextFont != null) {
            this.threadIndicatorTextFont = threadIndicatorTextFont;
            notifyDataSetChanged();
        }

    }

    public void setOnlineStatusColor(int onlineStatusColor) {
        if (onlineStatusColor != 0) {
            this.onlineStatusColor = onlineStatusColor;
            notifyDataSetChanged();
        }

    }

    public void setSeparatorColor(int separatorColor) {
        if (separatorColor != 0) {
            this.separatorColor = separatorColor;
            notifyDataSetChanged();
        }
    }

    public void setLastMessageTextColor(int lastMessageTextColor) {
        if (lastMessageTextColor != 0) {
            this.lastMessageTextColor = lastMessageTextColor;
            notifyDataSetChanged();
        }
    }

    public void setTypingIndicatorTextColor(int typingIndicatorTextColor) {
        if (typingIndicatorTextColor != 0) {
            this.typingIndicatorTextColor = typingIndicatorTextColor;
            notifyDataSetChanged();
        }
    }

    public void setThreadIndicatorTextColor(int threadIndicatorTextColor) {
        if (threadIndicatorTextColor != 0) {
            this.threadIndicatorTextColor = threadIndicatorTextColor;
            notifyDataSetChanged();
        }
    }

    public void setDisableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        notifyDataSetChanged();
    }

    public void setDisableReadReceipt(boolean disableReadReceipt) {
        this.disableReadReceipt = disableReadReceipt;
        notifyDataSetChanged();
    }

    public void setDisableTyping(boolean disableTyping) {
        this.disableTyping = disableTyping;
        notifyDataSetChanged();

    }

    public void setProtectedGroupIcon(int protectedGroupIcon) {
        if (protectedGroupIcon != 0) {
            this.protectedGroupIcon = protectedGroupIcon;
            notifyDataSetChanged();
        }
    }

    public void setPrivateGroupIcon(int privateGroupIcon) {
        if (privateGroupIcon != 0) {
            this.privateGroupIcon = privateGroupIcon;
            notifyDataSetChanged();
        }
    }

    public void setReadIcon(int readIcon) {
        if (readIcon != 0) {
            this.readIcon = context.getResources().getDrawable(readIcon);
            notifyDataSetChanged();
        }
    }

    public void setDeliveredIcon(int deliveredIcon) {
        if (deliveredIcon != 0) {
            this.deliveredIcon = context.getResources().getDrawable(deliveredIcon);
            notifyDataSetChanged();
        }
    }

    public void setSentIcon(int sentIcon) {
        if (sentIcon != 0) {
            this.sentIcon = context.getResources().getDrawable(sentIcon);
            notifyDataSetChanged();
        }
    }

    public void setDatePattern(Function1<Conversation, String> datePattern) {
        if (datePattern != null) {
            this.datePattern = datePattern;
            notifyDataSetChanged();
        }
    }

    private String getDatePattern(Conversation conversation) {
        if (datePattern != null) {
            return datePattern.apply(conversation);
        }
        return null;
    }

    public void hideSeparator(boolean hide) {
        hideSeparator = hide;
        notifyDataSetChanged();
    }

    public void selectConversation(HashMap<Conversation, Boolean> hashMap) {
        this.selectedConversation = hashMap;
        notifyDataSetChanged();
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

    public void setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        if (statusIndicatorStyle != null) {
            this.statusIndicatorStyle = statusIndicatorStyle;
            notifyDataSetChanged();
        }
    }

    public void setDateStyle(DateStyle dateStyle) {
        if (dateStyle != null) {
            if (dateStyle.getTextColor() == 0) {
                dateStyle.setTextColor(palette.getAccent600());
            }
            if (dateStyle.getTextAppearance() == 0) {
                dateStyle.setTextAppearance(typography.getSubtitle1());
            }
            this.dateStyle = dateStyle;
            notifyDataSetChanged();
        }
    }

    public void setBadgeStyle(BadgeStyle badgeStyle) {
        if (badgeStyle != null) {
            if (badgeStyle.getTextColor() == 0) {
                badgeStyle.setTextColor(palette.getAccent900());
            }
            if (badgeStyle.getBackground() == 0) {
                badgeStyle.setBackground(palette.getPrimary());
            }
            if (badgeStyle.getCornerRadius() < 0) {
                badgeStyle.setCornerRadius(100);
            }
            this.badgeStyle = badgeStyle;
            notifyDataSetChanged();
        }
    }

    public List<Conversation> getConversationsList() {
        return conversationsList;
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
