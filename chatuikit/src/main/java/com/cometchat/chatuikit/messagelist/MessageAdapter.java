package com.cometchat.chatuikit.messagelist;

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

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderAdapter;
import com.cometchat.chatuikit.shared.utils.MessageBubbleUtils;
import com.cometchat.chatuikit.shared.utils.MessageReceiptUtils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.CometChatDate;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.Pattern;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.CometChatMessageBubble;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.MessageBubbleStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> implements StickyHeaderAdapter<MessageAdapter.DateItemHolder> {
    private List<BaseMessage> baseMessageList;
    private Context context;
    private static final int LEFT_MESSAGE = 1;
    private static final int RIGHT_MESSAGE = 2;
    private static final int CENTER_MESSAGE = 3;
    private static final int RIGHT_TEXT_MESSAGE = 4;
    private final User loggedInUser = CometChatUIKit.getLoggedInUser();
    private CometChatMessageList.ThreadReplyClick threadReplyClick;
    private boolean disableReadReceipt;
    private @DrawableRes
    int readIcon;
    private @DrawableRes
    int deliverIcon;
    private @DrawableRes
    int sentIcon;
    private @DrawableRes
    int waitIconIcon;
    private @ColorInt
    int nameTextColor;
    private @ColorInt
    int timeStampTextColor;
    private @ColorInt
    int threadReplySeparatorColor;
    private @ColorInt
    int threadReplyTextColor;
    private @ColorInt
    int threadReplyIconTint;
    private @StyleRes
    int nameTextAppearance;
    private @StyleRes
    int timeStampTextAppearance;
    private @StyleRes
    int threadReplyTextAppearance;
    int bubbleCornerRadius = 16;
    private UIKitConstants.MessageListAlignment listAlignment = UIKitConstants.MessageListAlignment.STANDARD;
    private boolean showAvatar = false;
    private boolean showLeftBubbleUserAvatar = false;
    private boolean showLeftBubbleGroupAvatar = true;
    private Function1<BaseMessage, String> datePattern;
    private Function1<BaseMessage, String> dateSeparatorPattern;
    private UIKitConstants.TimeStampAlignment timeStampAlignment = UIKitConstants.TimeStampAlignment.BOTTOM;
    private OnMessageLongClick onMessageLongClick;
    private HashMap<String, CometChatMessageTemplate> messageTemplateHashMap;
    private CometChatTheme cometChatTheme;
    private DateStyle dateSeparatorStyle;
    private AvatarStyle avatarStyle;
    private MessageBubbleStyle wrapperMessageBubbleStyle;
    private String type;
    private User user;
    private Group group;

    public MessageAdapter(Context context, HashMap<String, CometChatMessageTemplate> messageTemplateHashMap, OnMessageLongClick onMessageLongClick) {
        this.context = context;
        baseMessageList = new ArrayList<>();
        this.messageTemplateHashMap = messageTemplateHashMap;
        this.onMessageLongClick = onMessageLongClick;
        cometChatTheme = CometChatTheme.getInstance(context);
        nameTextColor = cometChatTheme.getPalette().getAccent();
        nameTextAppearance = cometChatTheme.getTypography().getText3();
        timeStampTextAppearance = cometChatTheme.getTypography().getCaption1();
        timeStampTextColor = cometChatTheme.getPalette().getAccent500();
        setAvatarStyle(new AvatarStyle());
        setDateSeparatorStyle(new DateStyle());
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewTypes(position);
    }

    private int getItemViewTypes(int position) {
        BaseMessage baseMessage = baseMessageList.get(position);
        if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_ACTION) || baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CALL)) {
            return CENTER_MESSAGE;
        } else {
            if (!UIKitConstants.MessageListAlignment.LEFT_ALIGNED.equals(listAlignment)) {
                if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                    return RIGHT_MESSAGE;
                } else {
                    return LEFT_MESSAGE;
                }
            } else return LEFT_MESSAGE;
        }
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RIGHT_MESSAGE:
            case RIGHT_TEXT_MESSAGE:
                return new MyViewHolder(getRightView(parent));
            case CENTER_MESSAGE:
                return new MyViewHolder(getCenterView(parent));
            default:
                return new MyViewHolder(getLeftView(parent));
        }
    }

    @Override
    public MessageAdapter.DateItemHolder onCreateHeaderViewHolder(ViewGroup var1) {
        return new DateItemHolder(LayoutInflater.from(var1.getContext()).inflate(R.layout.cometchat_messagedate_header, var1, false));
    }

    private View getLeftView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_message_row, parent, false);
        view.setTag(LEFT_MESSAGE);
        return view;
    }


    private View getCenterView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.center_message_row, parent, false);
        view.setTag(CENTER_MESSAGE);
        return view;
    }

    private View getRightView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_message_row, parent, false);
        view.setTag(RIGHT_MESSAGE);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        BaseMessage baseMessage = baseMessageList.get(position);
        switch (holder.getItemViewType()) {
            case RIGHT_MESSAGE:
                setBubbleData(holder, baseMessage, UIKitConstants.MessageBubbleAlignment.RIGHT);
                break;
            case CENTER_MESSAGE:
                setBubbleData(holder, baseMessage, UIKitConstants.MessageBubbleAlignment.CENTER);
                break;
            default:
                setBubbleData(holder, baseMessage, UIKitConstants.MessageBubbleAlignment.LEFT);
        }
    }

    @Override
    public void onBindHeaderViewHolder(MessageAdapter.DateItemHolder var1, int var2, long var3) {
        BaseMessage baseMessage = baseMessageList.get(var2);
        if (context != null) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.cc_rounded_button);
            drawable.setTint(dateSeparatorStyle.getBackground());
            var1.parent.setBackground(drawable);
        }
        if (getDateSeparatorPattern(baseMessage) == null) {
            if (baseMessage.getSentAt() > 0) {
                var1.txtMessageDate.setDate(baseMessage.getSentAt(), Pattern.DAY_DATE);
            } else {
                var1.txtMessageDate.text(context.getString(R.string.updating));
            }
        } else {
            var1.txtMessageDate.setCustomDateString(getDateSeparatorPattern(baseMessage));
        }
        var1.txtMessageDate.setStyle(dateSeparatorStyle);
    }

    @Override
    public int getItemCount() {
        return baseMessageList.size();
    }

    @Override
    public long getHeaderId(int var1) {
        BaseMessage baseMessage = baseMessageList.get(var1);
        return Long.parseLong(Utils.getDateId(baseMessage.getSentAt() * 1000));
    }

    public void setMessageTemplateHashMap(HashMap<String, CometChatMessageTemplate> messageTemplateHashMap) {
        this.messageTemplateHashMap = messageTemplateHashMap;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parent;
        private CometChatMessageBubble cometChatMessageBubble;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            cometChatMessageBubble = itemView.findViewById(R.id.message_bubble);
        }
    }

    public class DateItemHolder extends RecyclerView.ViewHolder {
        CometChatDate txtMessageDate;
        LinearLayout parent;

        DateItemHolder(@NonNull View itemView) {
            super(itemView);
            txtMessageDate = itemView.findViewById(R.id.txt_message_date);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public void setBubbleData(MyViewHolder holder, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        CometChatMessageTemplate template = messageTemplateHashMap.get(baseMessage.getCategory() + "_" + baseMessage.getType());
        holder.cometChatMessageBubble.setMessageAlignment(alignment);
        MessageBubbleStyle messageBubbleStyle = null;
        View headerView = null;
        View footerView = null;
        String time = getDatePattern(baseMessage);
        boolean hideName = (listAlignment.equals(UIKitConstants.MessageListAlignment.STANDARD) && (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP) || baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER))) && (alignment.equals(UIKitConstants.MessageBubbleAlignment.RIGHT) || baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER));
        boolean showReadReceipt = !disableReadReceipt && !alignment.equals(UIKitConstants.MessageBubbleAlignment.LEFT) && baseMessage.getDeletedAt() == 0;
        if (template != null) {
            View bubbleView = template.getBubbleView(context, baseMessage, alignment);
            headerView = template.getHeaderView(context, baseMessage, alignment);
            footerView = template.getFooterView(context, baseMessage, alignment);

            if (bubbleView != null) {
                holder.parent.removeAllViews();
                holder.parent.addView(bubbleView);
            } else {
                if (!alignment.equals(UIKitConstants.MessageBubbleAlignment.CENTER)) {


                    if (wrapperMessageBubbleStyle == null) {
                        if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT) && baseMessage.getCategory().equals(CometChatConstants.CATEGORY_MESSAGE) && alignment.equals(UIKitConstants.MessageBubbleAlignment.RIGHT))
                            messageBubbleStyle = new MessageBubbleStyle().setBackground(cometChatTheme.getPalette().getPrimary()).setCornerRadius(bubbleCornerRadius);
                        else
                            messageBubbleStyle = new MessageBubbleStyle().setBackground(cometChatTheme.getPalette().getAccent50()).setCornerRadius(bubbleCornerRadius);

                    } else {
                        if(wrapperMessageBubbleStyle.getBackground()==0){
                            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT) && baseMessage.getCategory().equals(CometChatConstants.CATEGORY_MESSAGE) && alignment.equals(UIKitConstants.MessageBubbleAlignment.RIGHT))
                                messageBubbleStyle = new MessageBubbleStyle().setBackground(cometChatTheme.getPalette().getPrimary()).setBackground(wrapperMessageBubbleStyle.getDrawableBackground()).setCornerRadius(wrapperMessageBubbleStyle.getCornerRadius()).setBorderColor(wrapperMessageBubbleStyle.getBorderColor()).setBorderWidth(wrapperMessageBubbleStyle.getBorderWidth());
                            else
                                messageBubbleStyle = new MessageBubbleStyle().setBackground(cometChatTheme.getPalette().getAccent50()).setBackground(wrapperMessageBubbleStyle.getDrawableBackground()).setCornerRadius(wrapperMessageBubbleStyle.getCornerRadius()).setBorderColor(wrapperMessageBubbleStyle.getBorderColor()).setBorderWidth(wrapperMessageBubbleStyle.getBorderWidth());
                        }
                    }

                    holder.cometChatMessageBubble.setStyle(messageBubbleStyle);
                    holder.cometChatMessageBubble.setContentView(template.getContentView(context, baseMessage, alignment));
                    holder.cometChatMessageBubble.setBottomView(template.getBottomView(context, baseMessage, alignment));

                    if (baseMessage.getReplyCount() > 0 && baseMessage.getDeletedAt() == 0) {
                        @ColorInt int threadReplySeparatorColorTemp = threadReplySeparatorColor == 0 ? cometChatTheme.getPalette().getAccent100() : threadReplySeparatorColor;
                        @ColorInt int threadReplyTextColorTemp = threadReplyTextColor == 0 ? baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT) && baseMessage.getCategory().equals(CometChatConstants.CATEGORY_MESSAGE) && alignment.equals(UIKitConstants.MessageBubbleAlignment.RIGHT) ? context.getResources().getColor(R.color.textColorWhite) : cometChatTheme.getPalette().getPrimary() : threadReplyTextColor;
                        @ColorInt int threadReplyIconTintTemp = threadReplyIconTint == 0 ? cometChatTheme.getPalette().getAccent500() : threadReplyIconTint;
                        @StyleRes int threadReplyTextAppearanceTemp = threadReplyTextAppearance == 0 ? cometChatTheme.getTypography().getName() : threadReplyTextAppearance;
                        View view = MessageBubbleUtils.getThreadView(context, baseMessage, null, threadReplySeparatorColorTemp, threadReplyTextColorTemp, threadReplyTextAppearanceTemp, threadReplyIconTintTemp, 0);
                        holder.cometChatMessageBubble.setThreadView(view);
                        if (view != null) {
                            MessageBubbleStyle finalMessageBubbleStyle = messageBubbleStyle;
                            view.setOnClickListener(view1 -> {
                                if (threadReplyClick != null)
                                    threadReplyClick.onThreadReplyClick(context, baseMessage, getMessageBubble(baseMessage, template, alignment, time, hideName, showReadReceipt, finalMessageBubbleStyle));
                            });
                        }
                    } else holder.cometChatMessageBubble.setThreadView(null);
                } else {
                    holder.cometChatMessageBubble.setContentView(template.getContentView(context, baseMessage, alignment));
                    holder.cometChatMessageBubble.setHeaderView(template.getHeaderView(context, baseMessage, alignment));
                    holder.cometChatMessageBubble.setFooterView(template.getFooterView(context, baseMessage, alignment));
                }
            }

            MessageBubbleStyle finalMessageBubbleStyle1 = messageBubbleStyle;
            holder.parent.setOnLongClickListener(view -> {
                List<CometChatMessageOption> options = template.getOptions(context, baseMessage, group);
                if (options != null && !options.isEmpty()) {
                    onMessageLongClick.onLongClick(options, baseMessage, getMessageBubble(baseMessage, template, alignment, time, hideName, showReadReceipt, finalMessageBubbleStyle1));
                } else return false;
                return true;
            });
        }
        if (!alignment.equals(UIKitConstants.MessageBubbleAlignment.CENTER)) {
            holder.cometChatMessageBubble.setHeaderView(headerView != null ? headerView : MessageBubbleUtils.getTopView(context, timeStampAlignment.equals(UIKitConstants.TimeStampAlignment.TOP), !hideName, baseMessage.getSender().getName(), time, nameTextColor, nameTextAppearance, new DateStyle().setTextAppearance(timeStampTextAppearance).setTextColor(timeStampTextColor)));
            holder.cometChatMessageBubble.setFooterView(footerView != null ? footerView : MessageBubbleUtils.getBottomView(context, timeStampAlignment.equals(UIKitConstants.TimeStampAlignment.BOTTOM), showReadReceipt, readIcon, deliverIcon, sentIcon, waitIconIcon, MessageReceiptUtils.MessageReceipt(baseMessage), time, new DateStyle().setTextAppearance(timeStampTextAppearance).setTextColor(timeStampTextColor)));
        }
        if (alignment.equals(UIKitConstants.MessageBubbleAlignment.LEFT)) {
            if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER))
                setLeadingView(showLeftBubbleUserAvatar, holder, baseMessage);
            else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP))
                setLeadingView(showLeftBubbleGroupAvatar, holder, baseMessage);
        } else setLeadingView(false, holder, baseMessage);
    }

    public CometChatMessageBubble getMessageBubble(BaseMessage baseMessage, CometChatMessageTemplate template, UIKitConstants.MessageBubbleAlignment alignment, String time, boolean hideName, boolean showReadReceipt, MessageBubbleStyle style) {
        CometChatMessageBubble messageBubble = new CometChatMessageBubble(context);
        messageBubble.setMessageAlignment(UIKitConstants.MessageBubbleAlignment.LEFT);
        messageBubble.setContentView(template.getContentView(context, baseMessage, alignment));
        messageBubble.setFooterView(template.getFooterView(context, baseMessage, alignment) != null ? template.getFooterView(context, baseMessage, alignment) : MessageBubbleUtils.getBottomView(context, timeStampAlignment.equals(UIKitConstants.TimeStampAlignment.BOTTOM), false, readIcon, deliverIcon, sentIcon, waitIconIcon, MessageReceiptUtils.MessageReceipt(baseMessage), time, new DateStyle().setTextAppearance(timeStampTextAppearance).setTextColor(timeStampTextColor)));
        messageBubble.setStyle(style);
        return messageBubble;
    }

    public void setLeadingView(boolean showAvatar, MyViewHolder holder, BaseMessage baseMessage) {
        holder.cometChatMessageBubble.setLeadingView(MessageBubbleUtils.getAvatarLeadingView(context, showAvatar, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName(), avatarStyle));
    }

    public void setBaseMessageList(List<BaseMessage> baseMessageList) {
        if (baseMessageList != null) {
            this.baseMessageList = baseMessageList;
            notifyDataSetChanged();
        }
    }

    public void setThreadReplyClick(CometChatMessageList.ThreadReplyClick threadReplyClick) {
        if (threadReplyClick != null) {
            this.threadReplyClick = threadReplyClick;
            notifyDataSetChanged();
        }
    }


    public void setType(String type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void disableReadReceipt(boolean disableReadReceipt) {
        this.disableReadReceipt = disableReadReceipt;
        notifyDataSetChanged();
    }

    public void setReadIcon(@DrawableRes int readIcon) {
        if (readIcon != 0) {
            this.readIcon = readIcon;
            notifyDataSetChanged();
        }
    }

    public void setNameTextColor(int nameTextColor) {
        if (nameTextColor != 0) {
            this.nameTextColor = nameTextColor;
            notifyDataSetChanged();
        }
    }

    public void setTimeStampTextColor(int timeStampTextColor) {
        if (timeStampTextColor != 0) {
            this.timeStampTextColor = timeStampTextColor;
            notifyDataSetChanged();
        }
    }

    public void setThreadReplySeparatorColor(int threadReplySeparatorColor) {
        if (threadReplySeparatorColor != 0) {
            this.threadReplySeparatorColor = threadReplySeparatorColor;
            notifyDataSetChanged();
        }
    }

    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            if (avatarStyle.getCornerRadius() < 0) {
                avatarStyle.setOuterCornerRadius(100);
            }
            if (avatarStyle.getInnerBackgroundColor() == 0) {
                avatarStyle.setInnerBackgroundColor(cometChatTheme.getPalette().getAccent700());
            }
            if (avatarStyle.getTextColor() == 0) {
                avatarStyle.setTextColor(cometChatTheme.getPalette().getAccent900());
            }
            if (avatarStyle.getTextAppearance() == 0) {
                avatarStyle.setTextAppearance(cometChatTheme.getTypography().getName());
            }
            this.avatarStyle = avatarStyle;
            notifyDataSetChanged();
        }
    }

    public void setDateSeparatorStyle(DateStyle dateStyle) {
        if (dateStyle != null) {
            if (dateStyle.getTextColor() == 0) {
                dateStyle.setTextColor(cometChatTheme.getPalette().getAccent700());
            }
            if (dateStyle.getTextAppearance() == 0) {
                dateStyle.setTextAppearance(cometChatTheme.getTypography().getSubtitle2());
            }
            if (dateStyle.getBackground() == 0) {
                dateStyle.setBackground(cometChatTheme.getPalette().getAccent100());
            }
            this.dateSeparatorStyle = dateStyle;
            notifyDataSetChanged();
        }
    }

    public void setWrapperMessageBubbleStyle(MessageBubbleStyle style) {
        if (style != null) {
            wrapperMessageBubbleStyle = style;
            notifyDataSetChanged();
        }
    }

    public void setThreadReplyTextColor(int threadReplyTextColor) {
        if (threadReplyTextColor != 0) {
            this.threadReplyTextColor = threadReplyTextColor;
            notifyDataSetChanged();
        }
    }

    public void setThreadReplyIconTint(int threadReplyIconTint) {
        if (threadReplyIconTint != 0) {
            this.threadReplyIconTint = threadReplyIconTint;
            notifyDataSetChanged();
        }
    }

    public void setNameTextAppearance(int nameTextAppearance) {
        if (nameTextAppearance != 0) {
            this.nameTextAppearance = nameTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setTimeStampTextAppearance(int timeStampTextAppearance) {
        if (timeStampTextAppearance != 0) {
            this.timeStampTextAppearance = timeStampTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setThreadReplyTextAppearance(int threadReplyTextAppearance) {
        if (threadReplyTextAppearance != 0) {
            this.threadReplyTextAppearance = threadReplyTextAppearance;
            notifyDataSetChanged();
        }
    }

    public void setDeliverIcon(int deliverIcon) {
        if (deliverIcon != 0) {
            this.deliverIcon = deliverIcon;
            notifyDataSetChanged();
        }
    }

    public void setSentIcon(int sentIcon) {
        if (sentIcon != 0) {
            this.sentIcon = sentIcon;
            notifyDataSetChanged();
        }
    }

    public void setWaitIconIcon(int waitIconIcon) {
        if (waitIconIcon != 0) {
            this.waitIconIcon = waitIconIcon;
            notifyDataSetChanged();
        }
    }

    public void setAlignment(UIKitConstants.MessageListAlignment listAlignment) {
        if (listAlignment != null) {
            this.listAlignment = listAlignment;
            notifyDataSetChanged();
        }
    }

    public void showAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
        this.showLeftBubbleUserAvatar = showAvatar;
        this.showLeftBubbleGroupAvatar = showAvatar;
        notifyDataSetChanged();
    }

    public void setDatePattern(Function1<BaseMessage, String> datePattern) {
        if (datePattern != null) {
            this.datePattern = datePattern;
            notifyDataSetChanged();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setDateSeparatorPattern(Function1<BaseMessage, String> dateSeparatorPattern) {
        if (dateSeparatorPattern != null) {
            this.dateSeparatorPattern = dateSeparatorPattern;
            notifyDataSetChanged();
        }
    }

    public void setTimeStampAlignment(UIKitConstants.TimeStampAlignment timeStampAlignment) {
        if (timeStampAlignment != null) {
            this.timeStampAlignment = timeStampAlignment;
            notifyDataSetChanged();
        }
    }

    public String getDatePattern(BaseMessage baseMessage) {
        if (datePattern != null) return datePattern.apply(baseMessage) + "";
        return "";
    }

    public String getDateSeparatorPattern(BaseMessage baseMessage) {
        if (dateSeparatorPattern != null) return dateSeparatorPattern.apply(baseMessage) + "";
        return null;
    }

    public interface OnMessageLongClick {
        void onLongClick(List<CometChatMessageOption> list, BaseMessage message, CometChatMessageBubble cometChatMessageBubble);
    }
}
