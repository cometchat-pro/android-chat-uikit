package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchatworkspace.R;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.BadgeCountConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ConversationListItemConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageReceiptConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.StatusIndicatorConfiguration;
import com.cometchatworkspace.databinding.CometchatConversationListRowBinding;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;

import org.json.JSONObject;

/**
 * Purpose - ConversationListAdapter is a subclass of RecyclerView Adapter which is used to display
 * the list of conversations. It helps to organize the list data in recyclerView.
 * It also help to perform search operation on list of conversation.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 23rd March 2020
 */

public class CometChatConversationListAdapter extends
        RecyclerView.Adapter<CometChatConversationListAdapter.ConversationViewHolder>
        implements Filterable {

    private final Context context;

    private boolean isAvatarHidden, isUserPresenceHidden, isTitleHidden, isSubtitleHidden,
            isHelperTextHidden = true, isTimeHidden, isDeleteMessageHidden, isGroupActionHidden,
            isBadgeCountHidden;
    private int titleColor, subTitleColor, helperTextColor, timeColor, backgroundColor,
            typingIndicatorColor;

    float cornerRadius;

    /**
     * ConversationListAdapter maintains two arrayList i.e conversationList and filterConversationList.
     * conversationList is a original list and it will not get modified while filterConversationList
     * will get modified as per search filter. In case if search field is empty then to retrieve
     * original list we set filerConversationList = conversationList.
     * Here filterConversationList will be main list for this adapter.
     */
    private List<Conversation> conversationList = new ArrayList<>();

    private List<Conversation> filterConversationList = new ArrayList<>();

    private final FontUtils fontUtils;
    private boolean isTypingVisible;
    private TypingIndicator typingIndicator;
    private CometChatConfigurations configuration;
    private List<CometChatConfigurations> configurations = new ArrayList();

    /**
     * It is constructor which takes conversationList as parameter and bind it with conversationList
     * and filterConversationList in adapter.
     *
     * @param context          is a object of Context.
     * @param conversationList is list of conversations used in this adapter.
     */
    public CometChatConversationListAdapter(Context context, List<Conversation> conversationList) {
        this.conversationList = conversationList;
        this.filterConversationList = conversationList;
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    /**
     * It is a constructor which is used to initialize wherever we needed.
     *
     * @param context
     */
    public CometChatConversationListAdapter(Context context) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CometchatConversationListRowBinding conversationListRowBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.cometchat_conversation_list_row,
                        parent, false);
        return new ConversationViewHolder(conversationListRowBinding);
    }

    /**
     * This method is used to bind the ConversationViewHolder contents with conversation at given
     * position. It set avatar, name, lastMessage, unreadMessageCount and messageTime of conversation
     * in a respective ConversationViewHolder content. It checks whether conversation type is user
     * or group and set name and avatar as accordingly. It also checks whether last message is text, media
     * or file and modify txtUserMessage view accordingly.
     *
     * @param viewHolder is a object of ConversationViewHolder.
     * @param position   is a position of item in recyclerView.
     * @see Conversation
     */
    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder viewHolder, int position) {
        Conversation conversation = filterConversationList.get(position);

        //set properties passed from parent
        viewHolder.conversationListRowBinding.conversationListItem.hideAvatar(isAvatarHidden);
        viewHolder.conversationListRowBinding.conversationListItem.hideStatusIndicator(isUserPresenceHidden);
        viewHolder.conversationListRowBinding.conversationListItem.hideThreadIndicator(isHelperTextHidden);
        viewHolder.conversationListRowBinding.conversationListItem.setThreadIndicatorColor(helperTextColor);
        viewHolder.conversationListRowBinding.conversationListItem.hideTime(isTimeHidden);
        viewHolder.conversationListRowBinding.conversationListItem.setTimeColor(timeColor);
        viewHolder.conversationListRowBinding.conversationListItem.setTypingIndicatorColor(typingIndicatorColor);
        viewHolder.conversationListRowBinding.conversationListItem.hideDeletedMessage(isDeleteMessageHidden);
        viewHolder.conversationListRowBinding.conversationListItem.hideGroupActionMessage(isGroupActionHidden);
        if (isBadgeCountHidden)
            viewHolder.conversationListRowBinding.conversationListItem.hideUnreadCount(true);

        if (typingIndicator!=null) {
            if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                viewHolder.conversationListRowBinding.conversationListItem.setTypingIndicator(context.getString(R.string.is_typing));
            } else {
                viewHolder.conversationListRowBinding.conversationListItem.setTypingIndicator(typingIndicator.getSender().getName()+" "+context.getString(R.string.is_typing));
            }
            if (isTypingVisible) {
                viewHolder.conversationListRowBinding.conversationListItem.hideSubTitle(false);
                viewHolder.conversationListRowBinding.conversationListItem.hideReceipt(false);
                viewHolder.conversationListRowBinding.conversationListItem.showTypingIndicator(false);
            } else {
                viewHolder.conversationListRowBinding.conversationListItem.hideSubTitle(true);
                viewHolder.conversationListRowBinding.conversationListItem.hideReceipt(true);
                viewHolder.conversationListRowBinding.conversationListItem.showTypingIndicator(true);
            }
        }
        viewHolder.conversationListRowBinding.conversationListItem.setConversation(conversation);

        /**
         *   set inputData method
         */
        String id, avatar, status, title, subtitle;
        long time;
        int unreadCount;
        id = conversation.getConversationId();
        if (conversation.getConversationType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            User conversationUser = ((User) conversation.getConversationWith());
            avatar = conversationUser.getAvatar();
            title = conversationUser.getName();
            status = conversationUser.getStatus();
        } else {
            Group conversationGroup = ((Group) conversation.getConversationWith());
            avatar = conversationGroup.getIcon();
            title = conversationGroup.getName();
            status = conversationGroup.getGroupType();
        }
        BaseMessage baseMessage = conversation.getLastMessage();
        if (baseMessage != null) {
            subtitle = Utils.getLastMessage(context, baseMessage);

            if (baseMessage.getDeletedAt() > 0) {
                subtitle = "";
            }

        } else {
            subtitle = context.getResources().getString(R.string.tap_to_start_conversation);
        }

        time = conversation.getUpdatedAt();
        unreadCount = conversation.getUnreadMessageCount();
        /**
         * @InputData is a class which is helpful to set data into the view and control visibility
         * as per value passed in constructor .
         * i.e we can control the visibility of the component inside the CometChatUserListItem,
         * and also decide what value i need to show in that particular view
         */
//        InputData data = new InputData(id, avatar, title, subtitle, status, time, unreadCount);
//        viewHolder.conversationListRowBinding.conversationListItem.inputData(data);
        /**
         *   end set inputData
         */

        checkWithConfigurations(viewHolder.conversationListRowBinding.conversationListItem);

        viewHolder.conversationListRowBinding.executePendingBindings();
        viewHolder.conversationListRowBinding.getRoot().setTag(R.string.conversation, conversation);

    }

    private void checkWithConfigurations(CometChatConversationListItem listItem) {
        if (!configurations.isEmpty()) {
            for (CometChatConfigurations cometChatConfigurations : configurations) {
                configuration = cometChatConfigurations;
                setConfiguration(listItem);
            }
        } else if (configuration != null) {
            setConfiguration(listItem);
        }
    }

    private void setConfiguration(CometChatConversationListItem listItem) {
        if (configuration instanceof AvatarConfiguration) {
            AvatarConfiguration avatarConfig = (AvatarConfiguration) configuration;
            if (avatarConfig.getCornerRadius() != -1)
                listItem.getAvatar().setCornerRadius(avatarConfig.getCornerRadius());
            if (avatarConfig.getBorderWidth() != -1)
                listItem.getAvatar().setBorderWidth(avatarConfig.getBorderWidth());
            if (avatarConfig.getOuterViewColor() != 0)
                listItem.getAvatar().setOuterViewColor(avatarConfig.getOuterViewColor());
            if (avatarConfig.getOuterViewWidth() != -1)
                listItem.getAvatar().setOuterViewSpacing(avatarConfig.getOuterViewWidth());
            if (avatarConfig.getWidth() != -1 && avatarConfig.getHeight() != -1) {
                int avWidth = (int) Utils.dpToPx(context, avatarConfig.getWidth());
                int avHeight = (int) Utils.dpToPx(context, avatarConfig.getHeight());
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(avWidth, avHeight);
                listItem.getAvatar().setLayoutParams(layoutParams);
            }
        }
        if (configuration instanceof StatusIndicatorConfiguration) {
            StatusIndicatorConfiguration statusConfiguration = (
                    StatusIndicatorConfiguration) configuration;
            listItem.getStatusIndicator().status(statusConfiguration.getStatus());
            listItem.getStatusIndicator().color(statusConfiguration.getColor());

            if (statusConfiguration.getCornerRadius() != -1)
                listItem.getStatusIndicator().cornerRadius(statusConfiguration.getCornerRadius());

            if (statusConfiguration.getWidth() != -1 && statusConfiguration.getHeight() != -1) {
                int avWidth = (int) Utils.dpToPx(context, statusConfiguration.getWidth());
                int avHeight = (int) Utils.dpToPx(context, statusConfiguration.getHeight());
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(avWidth, avHeight);
                listItem.getStatusIndicator().setLayoutParams(layoutParams);
            }
            listItem.getStatusIndicator().borderWidth(statusConfiguration.getBorderWidth());
        }
        if (configuration instanceof BadgeCountConfiguration) {
            BadgeCountConfiguration badgeCountConfiguration =
                    (BadgeCountConfiguration) configuration;
            if (badgeCountConfiguration.getCornerRadius() != -1)
                listItem.getBadgeCount().cornerRadius(badgeCountConfiguration.getCornerRadius());
            if (badgeCountConfiguration.getTextSize() != -1)
                listItem.getBadgeCount().setTextSize(badgeCountConfiguration.getTextSize());
            if (badgeCountConfiguration.getBorderWidth() != -1)
                listItem.getBadgeCount().borderWidth(badgeCountConfiguration.getBorderWidth());
            if (badgeCountConfiguration.getWidth() != -1 && badgeCountConfiguration.getHeight() != -1) {
                int avWidth = (int) Utils.dpToPx(context, badgeCountConfiguration.getWidth());
                int avHeight = (int) Utils.dpToPx(context, badgeCountConfiguration.getHeight());
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(avWidth, avHeight);
                listItem.getBadgeCount().setLayoutParams(layoutParams);
            }
        }
        if (configuration instanceof MessageReceiptConfiguration) {
            MessageReceiptConfiguration receiptConfiguration =
                    (MessageReceiptConfiguration) configuration;
            if (receiptConfiguration.getReadIcon() != null)
                listItem.getMessageReceipt().messageReadIcon(receiptConfiguration.getReadIcon());
            if (receiptConfiguration.getDeliveredIcon() != null)
                listItem.getMessageReceipt().messageDeliveredIcon(receiptConfiguration.getDeliveredIcon());
            if (receiptConfiguration.getSentIcon() != null)
                listItem.getMessageReceipt().messageSentIcon(receiptConfiguration.getSentIcon());
            if (receiptConfiguration.getInProgressIcon() != null)
                listItem.getMessageReceipt().messageProgressIcon(receiptConfiguration.getInProgressIcon());
        }

        if (configuration instanceof ConversationListItemConfiguration) {
            ConversationListItemConfiguration listItemConfiguration =
                    (ConversationListItemConfiguration) configuration;
            isAvatarHidden = listItemConfiguration.isAvatarHidden();
            isUserPresenceHidden = listItemConfiguration.isStatusIndicatorHidden();
            isTypingVisible = listItemConfiguration.isTypingIndicatorVisible();
            isHelperTextHidden = listItemConfiguration.isThreadIndicatorHidden();
            isDeleteMessageHidden = listItemConfiguration.isDeleteMessageHidden();
            isGroupActionHidden = listItemConfiguration.isGroupActionMessageHidden();
            isBadgeCountHidden = listItemConfiguration.isBadgeCountHidden();

        }
    }

    @Override
    public int getItemCount() {
        return filterConversationList.size();
    }

    /**
     * This method is used to update the filterConversationList with new conversations and avoid
     * duplicates conversations.
     *
     * @param conversations is a list of conversation which will be updated in adapter.
     */
    public void updateList(List<Conversation> conversations) {

        for (int i = 0; i < conversations.size(); i++) {
            if (filterConversationList.contains(conversations.get(i))) {
                int index = filterConversationList.indexOf(conversations.get(i));
                filterConversationList.remove(conversations.get(i));
                filterConversationList.add(index, conversations.get(i));
            } else {
                filterConversationList.add(conversations.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setReadReceipts(MessageReceipt readReceipts) {
        for (int i = 0; i < filterConversationList.size() - 1; i++) {
            Conversation conversation = filterConversationList.get(i);
            if (conversation.getConversationType().equals(CometChatConstants.RECEIVER_TYPE_USER) &&
                    readReceipts.getSender().getUid().equals(((User) conversation.getConversationWith()).getUid())) {
                BaseMessage baseMessage = filterConversationList.get(i).getLastMessage();
                if (baseMessage != null && baseMessage.getReadAt() == 0) {
                    baseMessage.setReadAt(readReceipts.getReadAt());
                    int index = filterConversationList.indexOf(filterConversationList.get(i));
                    filterConversationList.remove(index);
                    conversation.setLastMessage(baseMessage);
                    filterConversationList.add(index, conversation);

                }
            }
        }
        notifyDataSetChanged();
    }

    public void setDeliveredReceipts(MessageReceipt deliveryReceipts) {
        for (int i = 0; i < filterConversationList.size() - 1; i++) {
            Conversation conversation = filterConversationList.get(i);
            if (conversation.getConversationType().equals(CometChatConstants.RECEIVER_TYPE_USER) &&
                    deliveryReceipts.getSender().getUid().equals(((User) conversation.getConversationWith()).getUid())) {

                BaseMessage baseMessage = filterConversationList.get(i).getLastMessage();
                if (baseMessage != null && baseMessage.getDeliveredAt() == 0) {
                    baseMessage.setReadAt(deliveryReceipts.getDeliveredAt());
                    int index = filterConversationList.indexOf(filterConversationList.get(i));

                    filterConversationList.remove(index);
                    conversation.setLastMessage(baseMessage);
                    filterConversationList.add(index, conversation);

                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * This method is used to remove the conversation from filterConversationList
     *
     * @param conversation is a object of conversation.
     * @see Conversation
     */
    public void remove(Conversation conversation) {
        int position = filterConversationList.indexOf(conversation);
        filterConversationList.remove(conversation);
        notifyItemRemoved(position);
    }


    /**
     * This method is used to update conversation in filterConversationList.
     *
     * @param conversation is an object of Conversation. It is used to update the previous conversation
     *                     in list
     * @see Conversation
     */
    public void update(Conversation conversation, boolean isActionMessage) {

        if (filterConversationList.contains(conversation)) {
            Conversation oldConversation = filterConversationList.get(filterConversationList.indexOf(conversation));
            filterConversationList.remove(oldConversation);
            JSONObject metadata = conversation.getLastMessage().getMetadata();
            boolean incrementUnreadCount = false;
            boolean isCategoryMessage = conversation.getLastMessage().getCategory()
                    .equalsIgnoreCase(CometChatConstants.CATEGORY_MESSAGE);
            try {
                if (metadata.has("incrementUnreadCount")) {
                    incrementUnreadCount = metadata.getBoolean("incrementUnreadCount");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isActionMessage)
                conversation.setUnreadMessageCount(oldConversation.getUnreadMessageCount());
            else if (incrementUnreadCount || isCategoryMessage)
                conversation.setUnreadMessageCount(oldConversation.getUnreadMessageCount() + 1);
            filterConversationList.add(0, conversation);
        } else {
            filterConversationList.add(0, conversation);
        }
        notifyDataSetChanged();

    }

    /**
     * This method is used to add conversation in list.
     *
     * @param conversation is an object of Conversation. It will be added to filterConversationList.
     * @see Conversation
     */
    public void add(Conversation conversation) {
        if (filterConversationList != null)
            filterConversationList.add(conversation);
    }

    /**
     * This method is used to reset the adapter by clearing filterConversationList.
     */
    public void resetAdapterList() {
        filterConversationList.clear();
        notifyDataSetChanged();
    }

    /**
     * It is used to perform search operation in filterConversationList. It will check
     * whether searchKeyword is similar to username or group name and modify filterConversationList
     * accordingly. In case if searchKeyword is empty it will set filterConversationList = conversationList
     *
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchKeyword = charSequence.toString();
                if (searchKeyword.isEmpty()) {
                    filterConversationList = conversationList;
                } else {
                    List<Conversation> tempFilter = new ArrayList<>();
                    for (Conversation conversation : filterConversationList) {

                        if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_USER) &&
                                ((User) conversation.getConversationWith()).getName().toLowerCase().contains(searchKeyword)) {

                            tempFilter.add(conversation);
                        } else if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP) &&
                                ((Group) conversation.getConversationWith()).getName().toLowerCase().contains(searchKeyword)) {
                            tempFilter.add(conversation);
                        } else if (conversation.getLastMessage() != null &&
                                conversation.getLastMessage().getCategory().equals(CometChatConstants.CATEGORY_MESSAGE) &&
                                conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)
                                && ((TextMessage) conversation.getLastMessage()).getText() != null
                                && ((TextMessage) conversation.getLastMessage()).getText().contains(searchKeyword)) {
                            tempFilter.add(conversation);
                        }
                    }
                    filterConversationList = tempFilter;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterConversationList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterConversationList = (List<Conversation>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Conversation getItemAtPosition(int position) {
        return filterConversationList.get(position);
    }


    public void setTypingIndicator(TypingIndicator typingIndicator, boolean b) {
        for(Conversation conversation : filterConversationList) {
            if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (conversation.getConversationId()
                        .contains(typingIndicator.getSender().getUid())) {
                    this.typingIndicator = typingIndicator;
                    isTypingVisible = b;
                    int index = filterConversationList.indexOf(conversation);
                    notifyItemChanged(index);
                }
            } else {
                if (conversation.getConversationId()
                        .contains(typingIndicator.getReceiverId())) {
                    this.typingIndicator = typingIndicator;
                    isTypingVisible = b;
                    int index = filterConversationList.indexOf(conversation);
                    notifyItemChanged(index);
                }
            }
        }
    }

    public void setConversationListItemProperty(boolean hideAvatar, boolean hideUserPresenceListItem,
                                                boolean hideTitleListItem, int titleColorListItem,
                                                boolean hideSubtitleListItem, int subTitleColorListItem,
                                                boolean hideHelperTextListItem, int helperTextColorListItem,
                                                boolean hideTimeListItem, int timeTextColorListItem,
                                                int backgroundColorListItem, float cornerRadiusListItem,
                                                int typingIndicatorColorListItem) {
        isAvatarHidden = hideAvatar;
        isUserPresenceHidden = hideUserPresenceListItem;
        isTitleHidden = hideTitleListItem;
        titleColor = titleColorListItem;
        isSubtitleHidden = hideSubtitleListItem;
        subTitleColor = subTitleColorListItem;
        isHelperTextHidden = hideHelperTextListItem;
        helperTextColor = helperTextColorListItem;
        isTimeHidden = hideTimeListItem;
        timeColor = timeTextColorListItem;
        backgroundColor = backgroundColorListItem;
        cornerRadius = cornerRadiusListItem;
        typingIndicatorColor = typingIndicatorColorListItem;
        notifyDataSetChanged();
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        this.configurations = configurations;
    }


    static class ConversationViewHolder extends RecyclerView.ViewHolder {

        CometchatConversationListRowBinding conversationListRowBinding;

        ConversationViewHolder(CometchatConversationListRowBinding conversationListRowBinding) {
            super(conversationListRowBinding.getRoot());
            this.conversationListRowBinding = conversationListRowBinding;
        }

    }
}
