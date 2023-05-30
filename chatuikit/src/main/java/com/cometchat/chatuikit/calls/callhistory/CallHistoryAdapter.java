package com.cometchat.chatuikit.calls.callhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderAdapter;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.CometChatDate;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.Pattern;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.calls.utils.CallUtils;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.MyViewHolder> implements StickyHeaderAdapter<CallHistoryAdapter.InitialHolder> {
    private List<BaseMessage> baseMessageList;
    private Context context;
    HashMap<BaseMessage, Boolean> selectedCalls;
    private Function2<Context, BaseMessage, View> subtitle, tail, customView;
    private CometChatTheme theme;
    private boolean hideItemSeparator = false;
    private @DrawableRes
    int incomingAudioCallIcon, incomingVideoCallIcon, infoIcon;
    private @ColorInt
    int incomingAudioCallIconTint, incomingVideoCallIconTint, callStatusColor, missedCallTitleColor, infoIconTint;
    private AvatarStyle avatarStyle;
    private ListItemStyle listItemStyle;
    private @DrawableRes
    int selectionIcon;
    private User loggedInUser;
    private DateStyle dateStyle, headerDate;
    private CometChatCallHistory.OnInfoIconClick infoIconClick;
    private OnItemClickListener<BaseMessage> onItemClickListener;


    public void setIncomingAudioCallIcon(int incomingAudioCallIcon) {
        if (incomingAudioCallIcon != 0) {
            this.incomingAudioCallIcon = incomingAudioCallIcon;
            notifyDataSetChanged();
        }
    }

    public void setIncomingVideoCallIcon(int incomingVideoCallIcon) {
        if (incomingVideoCallIcon != 0) {
            this.incomingVideoCallIcon = incomingVideoCallIcon;
            notifyDataSetChanged();
        }
    }

    public void setInfoIcon(int infoIcon) {
        if (infoIcon != 0) {
            this.infoIcon = infoIcon;
            notifyDataSetChanged();
        }
    }

    public void setIncomingAudioCallIconTint(int incomingAudioCallIconTint) {
        if (incomingAudioCallIconTint != 0) {
            this.incomingAudioCallIconTint = incomingAudioCallIconTint;
            notifyDataSetChanged();
        }
    }

    public void setIncomingVideoCallIconTint(int incomingVideoCallIconTint) {
        if (incomingVideoCallIconTint != 0) {
            this.incomingVideoCallIconTint = incomingVideoCallIconTint;
            notifyDataSetChanged();
        }
    }

    public void setCallStatusColor(int callStatusColor) {
        if (callStatusColor != 0) {
            this.callStatusColor = callStatusColor;
            notifyDataSetChanged();
        }
    }

    public void setMissedCallTitleColor(int missedCallTitleColor) {
        if (missedCallTitleColor != 0) {
            this.missedCallTitleColor = missedCallTitleColor;
            notifyDataSetChanged();
        }
    }

    public void setInfoIconTint(int infoIconTint) {
        if (infoIconTint != 0) {
            this.infoIconTint = infoIconTint;
            notifyDataSetChanged();
        }
    }

    public CallHistoryAdapter(Context context) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        baseMessageList = new ArrayList<>();
        selectBaseMessage(new HashMap<>());
        setAvatarStyle(new AvatarStyle());
        setListItemStyle(new ListItemStyle());
        setDateStyle(new DateStyle());
        selectionIcon = R.drawable.ic_circle_check;
        incomingAudioCallIcon = R.drawable.ic_audiocall;
        incomingVideoCallIcon = R.drawable.ic_video_call;
        infoIcon = R.drawable.ic_info;
        infoIconTint = theme.getPalette().getPrimary();
        incomingAudioCallIconTint = theme.getPalette().getAccent600();
        incomingVideoCallIconTint = theme.getPalette().getAccent600();
        missedCallTitleColor = theme.getPalette().getError();
        callStatusColor = theme.getPalette().getAccent600();
        loggedInUser = CometChatUIKit.getLoggedInUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BaseMessage baseMessage = baseMessageList.get(position);
        if (getCustomView(baseMessage) == null) {
            holder.cometChatListItem.hideStatusIndicator(hideItemSeparator);
            if (baseMessage instanceof Call) {
                holder.cometChatListItem.setAvatar(CallUtils.getCallerImage((Call) baseMessage), CallUtils.getCallerName((Call) baseMessage));
                holder.cometChatListItem.setTitle(CallUtils.getCallerName((Call) baseMessage));
                View subtitleView = getSubtitle(baseMessage);
                View tailView = getTailView(baseMessage);
                if (subtitleView == null) {
                    CallSubtitleView callSubtitleView = new CallSubtitleView(context);
                    callSubtitleView.setSubtitleText(CallUtils.getCallStatus(context, (Call) baseMessage));
                    callSubtitleView.setSubtitleTextColor(callStatusColor);
                    holder.cometChatListItem.setSubtitleView(callSubtitleView);
                    if (CallUtils.isVideoCall((Call) baseMessage)) {
                        callSubtitleView.setCallIcon(incomingVideoCallIcon);
                        callSubtitleView.setIconTint(incomingVideoCallIconTint);
                    } else {
                        callSubtitleView.setCallIcon(incomingAudioCallIcon);
                        callSubtitleView.setIconTint(incomingAudioCallIconTint);
                    }
                } else holder.cometChatListItem.setSubtitleView(subtitleView);

                if (tailView == null) {
                    CallTailView callTailView = new CallTailView(context);
                    callTailView.setCallIcon(infoIcon);
                    callTailView.setIconTint(infoIconTint);
                    callTailView.getChatDate().setDate(((Call) baseMessage).getInitiatedAt(), Pattern.DAY_DATE_TIME);
                    callTailView.getChatDate().setStyle(dateStyle);
                    Call call = (Call) baseMessage;
                    User user = null;
                    if (((User) call.getCallInitiator()).getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                        if (call.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.USER))
                            user = ((User) call.getCallReceiver());
                    } else {
                        if (call.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.USER))
                            user = ((User) call.getCallInitiator());
                    }
                    User finalUser = user;
                    callTailView.setOnClick(() -> {
                        if (infoIconClick != null) {
                            infoIconClick.onClick(context, finalUser, null, baseMessage);
                        }
                    });
                    holder.cometChatListItem.setTailView(callTailView);
                } else holder.cometChatListItem.setTailView(tailView);

                holder.cometChatListItem.setAvatarStyle(avatarStyle);
                holder.cometChatListItem.setStyle(listItemStyle);
                if (CallUtils.isMissedCall((Call) baseMessage))
                    holder.cometChatListItem.setTitleColor(missedCallTitleColor);
            } else if (baseMessage instanceof CustomMessage) {
                holder.cometChatListItem.setAvatar(CallUtils.getCallerImage((CustomMessage) baseMessage), CallUtils.getCallerName((CustomMessage) baseMessage));
                holder.cometChatListItem.setTitle(CallUtils.getCallerName((CustomMessage) baseMessage));
                View subtitleView = getSubtitle(baseMessage);
                View tailView = getTailView(baseMessage);
                if (subtitleView == null) {
                    CallSubtitleView callSubtitleView = new CallSubtitleView(context);
                    callSubtitleView.setSubtitleText(context.getResources().getString(R.string.conference_call));
                    callSubtitleView.setSubtitleTextColor(callStatusColor);
                    holder.cometChatListItem.setSubtitleView(callSubtitleView);
                    callSubtitleView.setCallIcon(incomingVideoCallIcon);
                    callSubtitleView.setIconTint(incomingVideoCallIconTint);
                } else holder.cometChatListItem.setSubtitleView(subtitleView);

                if (tailView == null) {
                    CallTailView callTailView = new CallTailView(context);
                    callTailView.setCallIcon(infoIcon);
                    callTailView.setIconTint(infoIconTint);
                    callTailView.getChatDate().setDate(baseMessage.getSentAt(), Pattern.DAY_DATE_TIME);
                    callTailView.getChatDate().setStyle(dateStyle);
                    Group group = (Group) baseMessage.getReceiver();
                    callTailView.setOnClick(() -> {
                        if (infoIconClick != null) {
                            infoIconClick.onClick(context, null, group, baseMessage);
                        }
                    });
                    holder.cometChatListItem.setTailView(callTailView);
                } else holder.cometChatListItem.setTailView(tailView);
                holder.cometChatListItem.setAvatarStyle(avatarStyle);
                holder.cometChatListItem.setStyle(listItemStyle);
            }
            if (!selectedCalls.isEmpty()) {
                if (selectedCalls.containsKey(baseMessage)) {
                    holder.cometChatListItem.hideStatusIndicator(false);
                    holder.cometChatListItem.statusIndicatorDimensions(20, 20);
                    holder.cometChatListItem.setStatusIndicatorIcon(selectionIcon);
                } else holder.cometChatListItem.hideStatusIndicator(true);
            } else holder.cometChatListItem.hideStatusIndicator(true);
        } else {
            holder.parentLayout.removeAllViews();
            holder.parentLayout.addView(getCustomView(baseMessage));
        }
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) onItemClickListener.OnItemClick(baseMessage, position);
        });
        holder.itemView.setOnLongClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemLongClick(baseMessage, position);
                return true;
            } else return false;
        });
    }

    @Override
    public int getItemCount() {
        return baseMessageList.size();
    }

    public void setBaseMessageList(List<BaseMessage> list) {
        if (list != null) {
            this.baseMessageList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public long getHeaderId(int var1) {
        BaseMessage baseMessage = baseMessageList.get(var1);
        return Long.parseLong(Utils.getDateId(baseMessage.getSentAt() * 1000));
    }

    @Override
    public CallHistoryAdapter.InitialHolder onCreateHeaderViewHolder(ViewGroup var1) {
        return new CallHistoryAdapter.InitialHolder(LayoutInflater.from(var1.getContext()).inflate(R.layout.call_logs_header, var1, false));
    }

    @Override
    public void onBindHeaderViewHolder(CallHistoryAdapter.InitialHolder var1, int var2, long var3) {
        BaseMessage baseMessage = getBaseMessageList().get(var2);
        if (baseMessage.getSentAt() > 0) {
            var1.textView.setDate(baseMessage.getSentAt(), Pattern.DAY_DATE);
        } else {
            var1.textView.text(context.getString(R.string.updating));
        }
        var1.textView.setStyle(headerDate);
        var1.separator.setBackgroundColor(theme.getPalette().getAccent100());
    }

    public void setHeaderDateStyle(DateStyle dateStyle) {
        if (dateStyle != null) {
            if (dateStyle.getTextColor() == 0) {
                dateStyle.setTextColor(theme.getPalette().getAccent());
            }
            if (dateStyle.getTextAppearance() == 0) {
                dateStyle.setTextAppearance(theme.getTypography().getText2());
            }
            if (dateStyle.getBackground() == 0) {
                dateStyle.setBackground(theme.getPalette().getAccent100());
            }
            this.headerDate = dateStyle;
            notifyDataSetChanged();
        }
    }

    public class InitialHolder extends RecyclerView.ViewHolder {

        private final CometChatDate textView;
        private final View separator;
        private final RelativeLayout stickyView;

        public InitialHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_char);
            separator = itemView.findViewById(R.id.list_item_separator);
            stickyView = itemView.findViewById(R.id.sticky_view);
        }
    }

    public List<BaseMessage> getBaseMessageList() {
        return baseMessageList;
    }

    public void setDateStyle(DateStyle dateStyle) {
        if (dateStyle != null) {
            if (dateStyle.getTextColor() == 0) {
                dateStyle.setTextColor(theme.getPalette().getAccent600());
            }
            if (dateStyle.getTextAppearance() == 0) {
                dateStyle.setTextAppearance(theme.getTypography().getSubtitle1());
            }
            this.dateStyle = dateStyle;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener<BaseMessage> onItemClickListener) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener;
            notifyDataSetChanged();
        }
    }

    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            if (avatarStyle.getCornerRadius() < 0) {
                avatarStyle.setOuterCornerRadius(100);
            }
            if (avatarStyle.getInnerBackgroundColor() == 0) {
                avatarStyle.setInnerBackgroundColor(theme.getPalette().getAccent600());
            }
            if (avatarStyle.getTextColor() == 0) {
                avatarStyle.setTextColor(theme.getPalette().getAccent900());
            }
            if (avatarStyle.getTextAppearance() == 0) {
                avatarStyle.setTextAppearance(theme.getTypography().getName());
            }
            this.avatarStyle = avatarStyle;
            notifyDataSetChanged();
        }
    }

    public void setListItemStyle(ListItemStyle listItemStyle) {
        if (listItemStyle != null) {
            if (listItemStyle.getTitleColor() == 0) {
                listItemStyle.setTitleColor(theme.getPalette().getAccent());
            }
            if (listItemStyle.getTitleAppearance() == 0) {
                listItemStyle.setTitleAppearance(theme.getTypography().getName());
            }
            if (listItemStyle.getSeparatorColor() == 0) {
                listItemStyle.setSeparatorColor(theme.getPalette().getAccent100());
            }
            this.listItemStyle = listItemStyle;
            notifyDataSetChanged();
        }
    }

    public void selectBaseMessage(HashMap<BaseMessage, Boolean> hashMap) {
        if (hashMap != null) {
            this.selectedCalls = hashMap;
            notifyDataSetChanged();
        }
    }

    public void setSubtitle(Function2<Context, BaseMessage, View> subtitle) {
        if (subtitle != null) {
            this.subtitle = subtitle;
            notifyDataSetChanged();
        }
    }

    public void setTailView(Function2<Context, BaseMessage, View> tailView) {
        if (tailView != null) {
            this.tail = tailView;
            notifyDataSetChanged();
        }
    }

    public void setCustomView(Function2<Context, BaseMessage, View> customView) {
        if (customView != null) {
            this.customView = customView;
            notifyDataSetChanged();
        }
    }

    private View getSubtitle(BaseMessage BaseMessage) {
        if (subtitle != null) return subtitle.apply(context, BaseMessage);
        return null;
    }

    private View getTailView(BaseMessage BaseMessage) {
        if (tail != null) return tail.apply(context, BaseMessage);
        return null;
    }

    private View getCustomView(BaseMessage BaseMessage) {
        if (customView != null) return customView.apply(context, BaseMessage);
        return null;
    }

    public BaseMessage getBaseMessage(int pos) {
        return baseMessageList.get(pos);
    }

    public void setSelectionIcon(int selectionIcon) {
        if (selectionIcon != 0) {
            this.selectionIcon = selectionIcon;
            notifyDataSetChanged();
        }
    }

    public void setInfoIconClick(CometChatCallHistory.OnInfoIconClick click) {
        if (click != null) {
            this.infoIconClick = click;
            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CometChatListItem cometChatListItem;
        private LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cometChatListItem = itemView.findViewById(R.id.list_item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPx(context, 72));
            cometChatListItem.setLayoutParams(params);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
