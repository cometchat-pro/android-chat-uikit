package com.cometchatworkspace.components.messages.message_list.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.CustomMessage;
import com.cometchatworkspace.R;

import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatCallActionBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatGroupActionBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatAudioBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatFileBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatConferenceBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatImageBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatLocationBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatPollBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatStickerBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatTextBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatVideoBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatWhiteboardBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatDocumentBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatReactionView;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.sticker_header.StickyHeaderAdapter;
import com.cometchatworkspace.components.messages.common.media_view.CometChatMediaViewActivity;
import com.cometchatworkspace.components.calls.CallUtils;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.MediaUtils;
import com.cometchatworkspace.resources.utils.Utils;

/**
 * Purpose - MessageAdapter is a subclass of RecyclerView Adapter which is used to display
 * the list of messages. It helps to organize the messages based on its type i.e TextMessage,
 * MediaMessage, Actions. It also helps to manage whether message is sent or recieved and organizes
 * view based on it. It is single adapter used to manage group messages and user messages.
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 23rd March 2020
 *
 */


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderAdapter<MessageAdapter.DateItemHolder> {

    private static final int RIGHT_IMAGE_MESSAGE = 56;

    private static final int LEFT_IMAGE_MESSAGE = 89;

    private static final int RIGHT_STICKER_MESSAGE = 21;

    private static final int LEFT_STICKER_MESSAGE = 22;

    private static final int RIGHT_VIDEO_MESSAGE = 78;

    private static final int LEFT_VIDEO_MESSAGE = 87;

    private static final int RIGHT_AUDIO_MESSAGE = 39;

    private static final int LEFT_AUDIO_MESSAGE = 93;

    private static final int LEFT_CALL_MESSAGE = 234;

    private static final int RIGHT_CALL_MESSAGE = 235;

    private final List<BaseMessage> messageList = new ArrayList<>();

    private static final int LEFT_TEXT_MESSAGE = 1;

    private static final int RIGHT_TEXT_MESSAGE = 2;

    private static final int LEFT_WHITEBOARD_MESSAGE = 7;

    private static final int RIGHT_WHITEBOARD_MESSAGE = 8;

    private static final int LEFT_WRITEBOARD_MESSAGE = 9;

    private static final int RIGHT_WRITEBOARD_MESSAGE = 10;

    private static final int RIGHT_REPLY_TEXT_MESSAGE = 987;

    private static final int LEFT_REPLY_TEXT_MESSAGE = 789;

    private static final int RIGHT_FILE_MESSAGE = 23;

    private static final int LEFT_FILE_MESSAGE = 25;

    private static final int ACTION_MESSAGE = 99;

    private static final int RIGHT_LINK_MESSAGE = 12;

    private static final int LEFT_LINK_MESSAGE = 13;

    private static final int LEFT_DELETE_MESSAGE = 551;

    private static final int RIGHT_DELETE_MESSAGE = 552;

    private static final int RIGHT_CUSTOM_MESSAGE = 432;

    private static final int LEFT_CUSTOM_MESSAGE = 431;

    private static final int LEFT_LOCATION_CUSTOM_MESSAGE = 31;

    private static final int RIGHT_LOCATION_CUSTOM_MESSAGE = 32;

    private static final int RIGHT_POLLS_CUSTOM_MESSAGE = 41;

    private static final int LEFT_POLLS_CUSTOM_MESSAGE = 42;

    private static final int LEFT_GROUP_CALL_MESSAGE = 313;

    private static final int RIGHT_GROUP_CALL_MESSAGE = 314;

    public static double LATITUDE;
    public static double LONGITUDE;

    public Context context;

    private final User loggedInUser = CometChat.getLoggedInUser();

    private boolean isLongClickEnabled;

    private final List<Integer> selectedItemList = new ArrayList<>();

    public List<BaseMessage> longselectedItemList = new ArrayList<>();

    private final FontUtils fontUtils;

    private MediaPlayer mediaPlayer;

    private final int messagePosition=0;

    private final OnMessageLongClick messageLongClick;

    private final boolean isUserDetailVisible = true;

    private final String TAG = "MessageAdapter";

    private boolean isSent;

    private boolean isLeftAligned;

    private int leftMessageBubbleColor = 0;
    private int leftMessageTextColor = 0;

    private int rightMessageBubbleColor = 0;
    private int rightMessageTextColor = 0;


    /**
     * It is used to initialize the adapter wherever we needed. It has parameter like messageList
     * which contains list of messages and it will be used in adapter and paramter type is a String
     * whose values will be either CometChatConstants.RECEIVER_TYPE_USER
     * CometChatConstants.RECEIVER_TYPE_GROUP.
     *
     *
     * @param context is a object of Context.
     * @param messageList is a list of messages used in this adapter.
     *
     */
    public MessageAdapter(Context context, List<BaseMessage> messageList, OnMessageLongClick onMessageLongClick) {
        setMessageList(messageList);
        this.context = context;
        messageLongClick = onMessageLongClick;

        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
        }

        fontUtils=FontUtils.getInstance(context);
    }

    /**
     * This method is used to return the different view types to adapter based on item position.
     * It uses getItemViewTypes() method to identify the view type of item.
     * @see MessageAdapter#getItemViewTypes(int)
     *      *
     * @param position is a position of item in recyclerView.
     * @return It returns int which is value of view type of item.
     * @see MessageAdapter#onCreateViewHolder(ViewGroup, int)
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewTypes(position);
    }

    private void setMessageList(List<BaseMessage> messageList) {
        this.messageList.addAll(0, messageList);
        notifyItemRangeInserted(0,messageList.size());

    }

    /**
     * This method is used to inflate the view for item based on its viewtype.
     * It helps to differentiate view for different type of messages.
     * Based on view type it returns various ViewHolder
     * Ex :- For MediaMessage it will return ImageMessageViewHolder,
     *       For TextMessage it will return TextMessageViewHolder,etc.
     *
     * @param parent is a object of ViewGroup.
     * @param i is viewType based on it various view will be inflated by adapter for various type
     *          of messages.
     * @return It return different ViewHolder for different viewType.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        switch (i) {
            case LEFT_DELETE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_text_item,parent,false);
                view.setTag(LEFT_DELETE_MESSAGE);
                return new DeleteMessageViewHolder(view);
            case RIGHT_DELETE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_text_item,parent,false);
                view.setTag(RIGHT_DELETE_MESSAGE);
                return new DeleteMessageViewHolder(view);
            case LEFT_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_text_item, parent, false);
                view.setTag(LEFT_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case RIGHT_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_text_item, parent, false);
                view.setTag(RIGHT_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case LEFT_REPLY_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_text_item, parent, false);
                view.setTag(LEFT_REPLY_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case RIGHT_REPLY_TEXT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_text_item, parent, false);
                view.setTag(RIGHT_REPLY_TEXT_MESSAGE);
                return new TextMessageViewHolder(view);

            case RIGHT_LINK_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_link_item,parent,false);
                view.setTag(RIGHT_LINK_MESSAGE);
                return new LinkMessageViewHolder(view);

            case LEFT_LINK_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_link_item,parent,false);
                view.setTag(LEFT_LINK_MESSAGE);
                return new LinkMessageViewHolder(view);

            case RIGHT_AUDIO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_audio_item,parent,false);
                view.setTag(RIGHT_AUDIO_MESSAGE);
                return new AudioMessageViewHolder(view);

            case LEFT_AUDIO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_audio_item,parent,false);
                view.setTag(LEFT_AUDIO_MESSAGE);
                return new AudioMessageViewHolder(view);

            case LEFT_STICKER_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_sticker_item, parent, false);
                view.setTag(LEFT_STICKER_MESSAGE);
                return new StickerMessageViewHolder(view);

            case RIGHT_STICKER_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_sticker_item, parent, false);
                view.setTag(RIGHT_STICKER_MESSAGE);
                return new StickerMessageViewHolder(view);

            case LEFT_IMAGE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_list_image_item, parent, false);
                view.setTag(LEFT_IMAGE_MESSAGE);
                return new ImageMessageViewHolder(view);


            case RIGHT_IMAGE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_list_image_item, parent, false);
                view.setTag(RIGHT_IMAGE_MESSAGE);
                return new ImageMessageViewHolder(view);


            case LEFT_VIDEO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_list_video_item,parent,false);
                view.setTag(LEFT_VIDEO_MESSAGE);
                return new VideoMessageViewHolder(view);

            case RIGHT_VIDEO_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_list_video_item,parent,false);
                view.setTag(RIGHT_VIDEO_MESSAGE);
                return new VideoMessageViewHolder(view);
            case RIGHT_FILE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_file_item, parent, false);
                view.setTag(RIGHT_FILE_MESSAGE);
                return new FileMessageViewHolder(view);

            case LEFT_FILE_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_file_item, parent, false);
                view.setTag(LEFT_FILE_MESSAGE);
                return new FileMessageViewHolder(view);
            case ACTION_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_group_action_item, parent, false);
                view.setTag(ACTION_MESSAGE);
                return new GroupActionMessageViewHolder(view);

            case LEFT_CALL_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_call_item, parent, false);
                view.setTag(LEFT_CALL_MESSAGE);
                return new CallActionMessageViewHolder(view);

            case RIGHT_CALL_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_call_item, parent, false);
                view.setTag(RIGHT_CALL_MESSAGE);
                return new CallActionMessageViewHolder(view);

            case RIGHT_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_custom_item, parent, false);
                view.setTag(RIGHT_TEXT_MESSAGE);
                return new CustomMessageViewHolder(view);

            case LEFT_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_custom_item, parent, false);
                view.setTag(RIGHT_TEXT_MESSAGE);
                return new CustomMessageViewHolder(view);

            case RIGHT_LOCATION_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_location_item, parent, false);
                view.setTag(RIGHT_LOCATION_CUSTOM_MESSAGE);
                return new LocationMessageViewHolder(view);

            case LEFT_LOCATION_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_location_item, parent, false);
                view.setTag(LEFT_LOCATION_CUSTOM_MESSAGE);
                return new LocationMessageViewHolder(view);

            case RIGHT_POLLS_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_polls_item,parent,false);
                view.setTag(RIGHT_POLLS_CUSTOM_MESSAGE);
                return new PollMessageViewHolder(view);

            case LEFT_POLLS_CUSTOM_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_polls_item,parent,false);
                view.setTag(RIGHT_POLLS_CUSTOM_MESSAGE);
                return new PollMessageViewHolder(view);
            case LEFT_WHITEBOARD_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_whiteboard_item, parent, false);
                view.setTag(LEFT_WHITEBOARD_MESSAGE);
                return new WhiteBoardViewHolder(view);

            case RIGHT_WHITEBOARD_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_whiteboard_item, parent, false);
                view.setTag(RIGHT_WHITEBOARD_MESSAGE);
                return new WhiteBoardViewHolder(view);

            case LEFT_WRITEBOARD_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_writeboard_item, parent, false);
                view.setTag(LEFT_WRITEBOARD_MESSAGE);
                return new WriteBoardViewHolder(view);

            case RIGHT_WRITEBOARD_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_writeboard_item, parent, false);
                view.setTag(RIGHT_WRITEBOARD_MESSAGE);
                return new WriteBoardViewHolder(view);

            case RIGHT_GROUP_CALL_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_group_call_item, parent, false);
                view.setTag(RIGHT_GROUP_CALL_MESSAGE);
                return new GroupCallMessageViewHolder(view);

            case LEFT_GROUP_CALL_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_group_call_item, parent, false);
                view.setTag(LEFT_GROUP_CALL_MESSAGE);
                return new GroupCallMessageViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_text_item, parent, false);
                view.setTag(-1);
                return new TextMessageViewHolder(view);
        }
    }


    /**
     * This method is used to bind the various ViewHolder content with their respective view types.
     * Here different methods are being called for different view type and in each method different
     * ViewHolder are been passed as parameter along with position of item.
     *
     * Ex :- For TextMessage <code>setTextData((TextMessageViewHolder)viewHolder,i)</code> is been called.
     * where <b>viewHolder</b> is casted as <b>TextMessageViewHolder</b> and <b>i</b> is position of item.
     *
     * @see MessageAdapter#setTextData(TextMessageViewHolder, int)
     * @see MessageAdapter#setImageData(ImageMessageViewHolder, int)
     * @see MessageAdapter#setFileData(FileMessageViewHolder, int)
     * @see MessageAdapter#setActionData(CallActionMessageViewHolder, int)
     *
     * @param viewHolder is a object of RecyclerViewHolder.
     * @param i is position of item in recyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        BaseMessage baseMessage = messageList.get(i);
//        BaseMessage nextMessage = null, prevMessage = null;
//        boolean isNextMessage = false,isPreviousMessage = false,isPrevActionMessage = false;
//        if ((i + 1) < messageList.size()) {
//            if (messageList.get(i+1).getSender()!=null)
//                nextMessage = messageList.get(i + 1);
//        }
//
//        if ((i - 1) >= 0) {
//            if (messageList.get(i-1).getSender()!=null)
//                prevMessage = messageList.get(i -1);
//        }
//
//        isPrevActionMessage = (prevMessage!=null && (prevMessage.getCategory().equals(CometChatConstants.CATEGORY_ACTION) || prevMessage.getCategory().equals(CometChatConstants.CATEGORY_CALL)));
//        isNextMessage = (nextMessage!=null && baseMessage.getSender().getUid().equals(nextMessage.getSender().getUid()));
//        isPreviousMessage = (prevMessage!=null && baseMessage.getSender().getUid().equals(prevMessage.getSender().getUid()));
//
//        if (!isPreviousMessage && isNextMessage) {
//           isUserDetailVisible = true;
//        }
//
//        if (isPreviousMessage && isNextMessage) {
//            isUserDetailVisible = false;
//        }
//        else if (!isNextMessage && !isPreviousMessage) {
//            isUserDetailVisible = true;
//        }
//
//        else if(!isNextMessage) {
//            isUserDetailVisible = false;
//        }
//        if (isPrevActionMessage) {
//            isUserDetailVisible = true;
//        }


        switch (viewHolder.getItemViewType()) {

            case LEFT_DELETE_MESSAGE:
            case RIGHT_DELETE_MESSAGE:
                setDeleteData((DeleteMessageViewHolder)viewHolder,i);
                break;
            case LEFT_TEXT_MESSAGE:
            case LEFT_REPLY_TEXT_MESSAGE:
            case RIGHT_TEXT_MESSAGE:
            case RIGHT_REPLY_TEXT_MESSAGE:
                setTextData((TextMessageViewHolder) viewHolder, i);
                break;
            case LEFT_LINK_MESSAGE:
            case RIGHT_LINK_MESSAGE:
                setLinkData((LinkMessageViewHolder) viewHolder,i);
                break;
            case LEFT_IMAGE_MESSAGE:
            case RIGHT_IMAGE_MESSAGE:
                setImageData((ImageMessageViewHolder) viewHolder, i);
                break;

            case LEFT_STICKER_MESSAGE:
            case RIGHT_STICKER_MESSAGE:
                setStickerData((StickerMessageViewHolder) viewHolder,i);
                break;

            case LEFT_AUDIO_MESSAGE:
            case RIGHT_AUDIO_MESSAGE:
                setAudioData((AudioMessageViewHolder) viewHolder,i);
                break;
            case LEFT_VIDEO_MESSAGE:
            case RIGHT_VIDEO_MESSAGE:
                setVideoData((VideoMessageViewHolder) viewHolder,i);
                break;
            case LEFT_FILE_MESSAGE:
            case RIGHT_FILE_MESSAGE:
                setFileData((FileMessageViewHolder) viewHolder, i);
                break;
            case ACTION_MESSAGE:
                setGroupActionData((GroupActionMessageViewHolder)viewHolder,i);
                break;
            case LEFT_CALL_MESSAGE:
            case RIGHT_CALL_MESSAGE:
                setActionData((CallActionMessageViewHolder) viewHolder, i);
                break;
            case LEFT_CUSTOM_MESSAGE:
            case RIGHT_CUSTOM_MESSAGE:
                setCustomData((CustomMessageViewHolder) viewHolder, i);
                break;
            case LEFT_LOCATION_CUSTOM_MESSAGE:
            case RIGHT_LOCATION_CUSTOM_MESSAGE:
                setLocationData((LocationMessageViewHolder) viewHolder, i);
                break;

            case RIGHT_POLLS_CUSTOM_MESSAGE:
            case LEFT_POLLS_CUSTOM_MESSAGE:
                setPollsData((PollMessageViewHolder) viewHolder,i);
                break;

            case LEFT_WHITEBOARD_MESSAGE:
            case RIGHT_WHITEBOARD_MESSAGE:
                setWhiteBoardData((WhiteBoardViewHolder) viewHolder,i);
                break;

            case LEFT_WRITEBOARD_MESSAGE:
            case RIGHT_WRITEBOARD_MESSAGE:
                setWriteBoardData((WriteBoardViewHolder) viewHolder,i);
                break;

            case LEFT_GROUP_CALL_MESSAGE:
            case RIGHT_GROUP_CALL_MESSAGE:
                setGroupCallData((GroupCallMessageViewHolder)viewHolder,i);
                break;
        }
    }

    private void setPollsData(PollMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatPollBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatPollBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatPollBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatPollBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatPollBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatPollBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatPollBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatPollBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatPollBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatPollBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatPollBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatPollBubble.userName(baseMessage.getSender().getName());
//                    viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.cometChatPollBubble.backgroundColor(leftMessageBubbleColor);
                viewHolder.cometChatPollBubble.questionColor(leftMessageTextColor);
                viewHolder.cometChatPollBubble.voteTextColor(leftMessageTextColor);
                viewHolder.cometChatPollBubble.optionsTextColor(leftMessageTextColor);
            } else {
                viewHolder.cometChatPollBubble.backgroundColor(rightMessageBubbleColor);
                viewHolder.cometChatPollBubble.questionColor(rightMessageTextColor);
                viewHolder.cometChatPollBubble.voteTextColor(rightMessageTextColor);
                viewHolder.cometChatPollBubble.optionsTextColor(rightMessageTextColor);
            }
        }
        viewHolder.cometChatPollBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                isLongClickEnabled = true;
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setPollMessageLongClick(longselectedItemList);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {

            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });

        viewHolder.cometChatPollBubble.replyCount(baseMessage.getReplyCount());
    }

    private void setGroupCallData(GroupCallMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatConferenceBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatConferenceBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatConferenceBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatConferenceBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatConferenceBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatConferenceBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatConferenceBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatConferenceBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatConferenceBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatConferenceBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatConferenceBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatConferenceBubble.userName(baseMessage.getSender().getName());
//                    viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.cometChatConferenceBubble.title(baseMessage.getSender().getName()+" "+
                        context.getString(R.string.has_shared_group_call));
                viewHolder.cometChatConferenceBubble.backgroundColor(leftMessageBubbleColor);
                viewHolder.cometChatConferenceBubble.titleColor(leftMessageTextColor);
            } else {
                viewHolder.cometChatConferenceBubble.title(context.getString(R.string.you_created_group_call));
                viewHolder.cometChatConferenceBubble.backgroundColor(rightMessageBubbleColor);
                viewHolder.cometChatConferenceBubble.titleColor(rightMessageTextColor);
            }
        }


        viewHolder.cometChatConferenceBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                isLongClickEnabled = true;
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setConferenceMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                if (((CustomMessage)baseMessage).getCustomData()!=null) {
                    if (CometChat.getActiveCall()==null)
                        CallUtils.startDirectCall(context, Utils.getDirectCallData(baseMessage));
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle(context.getResources().getString(R.string.ongoing_call))
                                .setMessage(context.getResources().getString(R.string.ongoing_call_message))
                                .setPositiveButton(context.getResources().getString(R.string.join), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CallUtils.joinOnGoingCall(context,CometChat.getActiveCall());
                                    }
                                }).setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                }
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });

       viewHolder.cometChatConferenceBubble.replyCount(baseMessage.getReplyCount());
    }


    private void setWriteBoardData(WriteBoardViewHolder viewHolder,int i) {
        if (isLeftAligned)
            viewHolder.documentMessageBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.documentMessageBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.documentMessageBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.documentMessageBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.documentMessageBubble.userNameVisibility(View.GONE);
                    viewHolder.documentMessageBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.documentMessageBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.documentMessageBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.documentMessageBubble.userNameVisibility(View.GONE);
                        viewHolder.documentMessageBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.documentMessageBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.documentMessageBubble.userName(baseMessage.getSender().getName());
//                    viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.documentMessageBubble.title(baseMessage.getSender().getName()+" "+
                        context.getString(R.string.has_shared_document));
                viewHolder.documentMessageBubble.backgroundColor(leftMessageBubbleColor);
                viewHolder.documentMessageBubble.titleColor(leftMessageTextColor);
            } else {
                viewHolder.documentMessageBubble.title(context.getString(R.string.you_created_document));
                viewHolder.documentMessageBubble.backgroundColor(rightMessageBubbleColor);
                viewHolder.documentMessageBubble.titleColor(rightMessageTextColor);

            }
        }

        viewHolder.documentMessageBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                isLongClickEnabled = true;
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setDocumentMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                Extensions.openWriteBoard(baseMessage, context);
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });

       viewHolder.documentMessageBubble.replyCount(baseMessage.getReplyCount());
    }


    private void setWhiteBoardData(WhiteBoardViewHolder viewHolder,int i) {
        if (isLeftAligned)
            viewHolder.cometChatWhiteboardBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatWhiteboardBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatWhiteboardBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatWhiteboardBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatWhiteboardBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatWhiteboardBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatWhiteboardBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatWhiteboardBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatWhiteboardBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatWhiteboardBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatWhiteboardBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatWhiteboardBubble.userName(baseMessage.getSender().getName());
//                    viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.cometChatWhiteboardBubble.title(baseMessage.getSender().getName()+" "+
                        context.getString(R.string.has_shared_whiteboard));
                viewHolder.cometChatWhiteboardBubble.backgroundColor(leftMessageBubbleColor);
                viewHolder.cometChatWhiteboardBubble.titleColor(leftMessageTextColor);
            } else {
                viewHolder.cometChatWhiteboardBubble.title(context.getString(R.string.you_created_whiteboard));
                viewHolder.cometChatWhiteboardBubble.backgroundColor(rightMessageBubbleColor);
                viewHolder.cometChatWhiteboardBubble.titleColor(rightMessageTextColor);

            }
        }

        viewHolder.cometChatWhiteboardBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                isLongClickEnabled = true;
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setWhiteboardMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                Extensions.openWhiteBoard(baseMessage, context);
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });

        viewHolder.cometChatWhiteboardBubble.replyCount(baseMessage.getReplyCount());

    }

    /**
     * This method is called whenever viewType of item is customMessage and customType is Location.
     * It is used to bind LocationMessageViewHolder
     * contents with CustomMessage at a given position.
     *
     * @param viewHolder is a object of LocationMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see CustomMessage
     * @see BaseMessage
     */
    private void setLocationData(LocationMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatLocationBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatLocationBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatLocationBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatLocationBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatLocationBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatLocationBubble.avatarVisibility(View.GONE);

                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible ) {
                        viewHolder.cometChatLocationBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatLocationBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatLocationBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatLocationBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatLocationBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatLocationBubble.userName(baseMessage.getSender().getName());
//                     viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.cometChatLocationBubble.backgroundColor(leftMessageBubbleColor);
                viewHolder.cometChatLocationBubble.titleColor(leftMessageTextColor);
                viewHolder.cometChatLocationBubble.subtitleColor(leftMessageTextColor);
            } else {
                viewHolder.cometChatLocationBubble.backgroundColor(rightMessageBubbleColor);
                viewHolder.cometChatLocationBubble.titleColor(rightMessageTextColor);
                viewHolder.cometChatLocationBubble.subtitleColor(rightMessageTextColor);
            }
        }

        viewHolder.cometChatLocationBubble.replyCount(baseMessage.getReplyCount());


        viewHolder.cometChatLocationBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                isLongClickEnabled = true;
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setLocationMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                try {
                    double latitude = ((CustomMessage)baseMessage).getCustomData().getDouble("latitude");
                    double longitude = ((CustomMessage)baseMessage).getCustomData().getDouble("longitude");
                    String label = Utils.getAddress(context,latitude,longitude);
                    String uriBegin = "geo:" + latitude + "," + longitude;
                    String query = label;
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });
    }

    /**
     * This method is called whenever viewType of item is file. It is used to bind AudioMessageViewHolder
     * contents with MediaMessage at a given position.
     *
     * @param viewHolder is a object of AudioMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see MediaMessage
     * @see BaseMessage
     */
    private void setAudioData(AudioMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatAudioBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatAudioBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatAudioBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatAudioBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatAudioBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatAudioBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatAudioBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatAudioBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatAudioBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatAudioBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatAudioBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatAudioBubble.userName(baseMessage.getSender().getName());
//                     viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.cometChatAudioBubble.backgroundColor(leftMessageBubbleColor);
                viewHolder.cometChatAudioBubble.titleColor(leftMessageTextColor);
                viewHolder.cometChatAudioBubble.subtitleColor(leftMessageTextColor);
            } else {
                viewHolder.cometChatAudioBubble.backgroundColor(rightMessageBubbleColor);
                viewHolder.cometChatAudioBubble.titleColor(rightMessageTextColor);
                viewHolder.cometChatAudioBubble.subtitleColor(rightMessageTextColor);
            }
        }
        viewHolder.cometChatAudioBubble.replyCount(baseMessage.getReplyCount());

        viewHolder.cometChatAudioBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                 clearLongClickSelectedItem();
                 setLongClickSelectedItem(baseMessage);
                 messageLongClick.setAudioMessageLongClick(longselectedItemList);
                 notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                Intent intent = new Intent(context,CometChatMediaViewActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.MEDIA_SIZE,
                        ((MediaMessage)baseMessage).getAttachment().getFileSize());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, CometChatConstants.MESSAGE_TYPE_AUDIO);
                intent.putExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE,
                        ((MediaMessage)baseMessage).getAttachment().getFileUrl());
                intent.putExtra(UIKitConstants.IntentStrings.NAME,baseMessage.getSender().getName());
                intent.putExtra(UIKitConstants.IntentStrings.SENTAT,baseMessage.getSentAt());
                context.startActivity(intent);
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });
    }

    public void stopPlayingAudio() {
        if (mediaPlayer!=null)
            mediaPlayer.stop();
    }
    /**
     * This method is called whenever viewType of item is file. It is used to bind FileMessageViewHolder
     * contents with MediaMessage at a given position.
     * It shows FileName, FileType, FileSize.
     *
     * @param viewHolder is a object of FileMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see MediaMessage
     * @see BaseMessage
     */
    private void setFileData(FileMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatFileBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatFileBubble.secondaryBackgroundColor(context.getColor(R.color.grey));
        viewHolder.cometChatFileBubble.iconTint(context.getColor(R.color.textColorWhite));
        viewHolder.cometChatFileBubble.typeColor(context.getColor(R.color.textColorWhite));
        viewHolder.cometChatFileBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatFileBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatFileBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatFileBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatFileBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatFileBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatFileBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatFileBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatFileBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatFileBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatFileBubble.userName(baseMessage.getSender().getName());
//                     viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
            }
        }
        viewHolder.cometChatFileBubble.replyCount(baseMessage.getReplyCount());

        viewHolder.cometChatFileBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setFileMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                MediaUtils.openFile(((MediaMessage) baseMessage).getAttachment().getFileUrl(),context);
                setSelectedMessage(baseMessage.getId());
                notifyItemChanged(i);
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });
    }

    private void sendReaction(String reactionStr, int baseMessageID) {
        JSONObject body=new JSONObject();
        try {
            body.put("msgId", baseMessageID);
            body.put("emoji", reactionStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CometChat.callExtension("reactions", "POST", "/v1/react", body,
                new CometChat.CallbackListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject responseObject) {
                        // ReactionModel added successfully.
                    }

                    @Override
                    public void onError(CometChatException e) {
                        // Some error occured.
                    }
                });
    }


    /**
     * This method is called whenever viewType of item is media. It is used to bind ImageMessageViewHolder
     * contents with MediaMessage at a given position.
     * It loads image of MediaMessage using its url.
     *
     * @param viewHolder is a object of ImageMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see MediaMessage
     * @see BaseMessage
     */
    private void setImageData(ImageMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatImageBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatImageBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatImageBubble.messageObject(baseMessage);
//            viewHolder.cometChatImageBubble.setImageUrl("data:image/png;base64,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");
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatImageBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatImageBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatImageBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatImageBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatImageBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatImageBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatImageBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatImageBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
                    viewHolder.cometChatImageBubble.userName(baseMessage.getSender().getName());
                }
            }
        }
        viewHolder.cometChatImageBubble.replyCount(baseMessage.getReplyCount());

        viewHolder.cometChatImageBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setImageMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                setSelectedMessage(baseMessage.getId());
                notifyItemChanged(i);
                openMediaViewActivity(baseMessage);
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });
    }



    private void openMediaViewActivity(BaseMessage baseMessage) {
        if (((MediaMessage)baseMessage).getAttachment()!=null) {
            Intent intent = new Intent(context, CometChatMediaViewActivity.class);
            intent.putExtra(UIKitConstants.IntentStrings.NAME, baseMessage.getSender().getName());
            intent.putExtra(UIKitConstants.IntentStrings.UID, baseMessage.getSender().getUid());
            intent.putExtra(UIKitConstants.IntentStrings.SENTAT, baseMessage.getSentAt());
            intent.putExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE,
                    ((MediaMessage) baseMessage).getAttachment().getFileUrl());
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, baseMessage.getType());
            context.startActivity(intent);
        }
    }


    /**
     * This method is called whenever viewType of item is sticker. It is used to bind StickerMessageViewHolder
     * contents with CustomMessage at a given position.
     * It loads image from url availabl in CustomMessage.
     *
     * @param viewHolder is a object of StickerMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see CustomMessage
     * @see BaseMessage
     */
    private void setStickerData(StickerMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatStickerBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatStickerBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatStickerBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatStickerBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatStickerBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatStickerBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatStickerBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatStickerBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatStickerBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatStickerBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatStickerBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatStickerBubble.userName(baseMessage.getSender().getName());
//                     viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
            }
        }

       viewHolder.cometChatStickerBubble.replyCount(baseMessage.getReplyCount());

        viewHolder.cometChatStickerBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
               clearLongClickSelectedItem();
               setLongClickSelectedItem(baseMessage);
               messageLongClick.setStickerMessageLongClick(longselectedItemList);
               notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {

            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });
    }


    private void setVideoData(VideoMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatVideoBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatVideoBubble.cornerRadius(48,48,48,0);
        if (baseMessage!=null) {
            viewHolder.cometChatVideoBubble.messageObject(baseMessage);
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatVideoBubble.cornerRadius(48, 48, 0, 48);
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.cometChatVideoBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatVideoBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible) {
                        viewHolder.cometChatVideoBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.cometChatVideoBubble.avatarVisibility(View.VISIBLE);
                    } else {
                        viewHolder.cometChatVideoBubble.userNameVisibility(View.GONE);
                        viewHolder.cometChatVideoBubble.avatarVisibility(View.INVISIBLE);
                    }

                    viewHolder.cometChatVideoBubble.avatar(baseMessage.getSender().getAvatar(),
                            baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                    viewHolder.cometChatVideoBubble.userName(baseMessage.getSender().getName());
//                     viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
            }
        }

        viewHolder.cometChatVideoBubble.replyCount(baseMessage.getReplyCount());

        viewHolder.cometChatVideoBubble.setMessageBubbleListener(new MessageBubbleListener() {
            @Override
            public void onLongCLick(BaseMessage baseMessage) {
                clearLongClickSelectedItem();
                setLongClickSelectedItem(baseMessage);
                messageLongClick.setVideoMessageLongClick(longselectedItemList);
                notifyItemChanged(i);
            }

            @Override
            public void onClick(BaseMessage baseMessage) {
                openMediaViewActivity(baseMessage);
            }

            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                try {
                    Extensions.sendReactionOptimistic(reaction,baseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(i);
                sendReaction(reaction.getName(),baseMessageID);
            }
        });
    }





    private void setDeleteData(DeleteMessageViewHolder viewHolder, int i) {


        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatTextBubble.cornerRadius(48,48,48,0);
        if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            viewHolder.cometChatTextBubble.cornerRadius(48,48,0,48);
            if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                    && !isLeftAligned) {
                viewHolder.cometChatTextBubble.userNameVisibility(View.GONE);
                viewHolder.cometChatTextBubble.avatarVisibility(View.GONE);
            } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                if (isUserDetailVisible) {
                    viewHolder.cometChatTextBubble.userNameVisibility(View.VISIBLE);
                    viewHolder.cometChatTextBubble.avatarVisibility(View.VISIBLE);
                }
                else {
                    viewHolder.cometChatTextBubble.userNameVisibility(View.GONE);
                    viewHolder.cometChatTextBubble.avatarVisibility(View.INVISIBLE);
                }
                viewHolder.cometChatTextBubble.avatar(baseMessage.getSender().getAvatar(),baseMessage.getSender().getName());
//                setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                viewHolder.cometChatTextBubble.userName(baseMessage.getSender().getName());
//                viewHolder.tvUser.setText(baseMessage.getSender().getName());
            }
        }
        if (isLeftAligned)
            viewHolder.cometChatTextBubble.messageAlignment(Alignment.LEFT);

        viewHolder.cometChatTextBubble.userName(baseMessage.getSender().getName());
        viewHolder.cometChatTextBubble.avatar(baseMessage.getSender().getAvatar(),
                baseMessage.getSender().getName());
        if (baseMessage.getDeletedAt()!=0) {
            viewHolder.cometChatTextBubble.replyCount(0);
            viewHolder.cometChatTextBubble.text(context.getString(R.string.this_message_deleted));
            viewHolder.cometChatTextBubble.textColor(context.getResources().getColor(R.color.secondaryTextColor));
            viewHolder.cometChatTextBubble.textFontStyle(Typeface.ITALIC);
        }
    }





    /**
     * This method is called whenever viewType of item is Action. It is used to bind
     * ActionMessageViewHolder contents with Action at a given position. It shows action message
     * or call status based on message category
     *
     * @param viewHolder is a object of ActionMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see Action
     * @see Call
     * @see BaseMessage
     */

    private void setGroupActionData(GroupActionMessageViewHolder viewHolder,int i) {
        BaseMessage baseMessage = messageList.get(i);
        if(Utils.isDarkMode(context))
            viewHolder.actionMessageBubble.titleColor(context.getResources().getColor(R.color.textColorWhite));
        else
            viewHolder.actionMessageBubble.titleColor(context.getResources().getColor(R.color.primaryTextColor));

        viewHolder.actionMessageBubble.titleFont(FontUtils.robotoMedium);
        if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)) {
            viewHolder.actionMessageBubble.groupActionObject(baseMessage);
        }
    }
    /**
     * This method is called whenever viewType of item is Action. It is used to bind
     * CallActionMessageViewHolder contents with Action at a given position. It shows action message
     * or call status based on message category
     *
     * @param viewHolder is a object of CallActionMessageViewHolder.
     * @param i is a position of item in recyclerView.
     * @see Action
     * @see Call
     * @see BaseMessage
     */
    private void setActionData(CallActionMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.actionMessageBubble.messageAlignment(Alignment.LEFT);

        BaseMessage baseMessage = messageList.get(i);
        if (baseMessage instanceof Call) {
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.actionMessageBubble.userNameVisibility(View.GONE);
                    viewHolder.actionMessageBubble.avatarVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
//                    if (isUserDetailVisible) {
                        viewHolder.actionMessageBubble.userNameVisibility(View.VISIBLE);
                        viewHolder.actionMessageBubble.avatarVisibility(View.VISIBLE);
//                    }
//                    else {
//                        viewHolder.actionMessageBubble.userNameVisibility(View.GONE);
//                        viewHolder.actionMessageBubble.avatarVisibility(View.INVISIBLE);
//                    }
//                    viewHolder.actionMessageBubble.avatar(baseMessage.getSender().getAvatar(),baseMessage.getSender().getName());
////                setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
//                    viewHolder.actionMessageBubble.userName(baseMessage.getSender().getName());
////                viewHolder.tvUser.setText(baseMessage.getSender().getName());
                }
                viewHolder.actionMessageBubble.cornerRadius(48,48,0,48);
            } else {
                viewHolder.actionMessageBubble.cornerRadius(48,48,48,0);
            }

            viewHolder.actionMessageBubble.messageObject(baseMessage);
        }
    }


    /**
     * This method is called whenever viewType of item is TextMessage. It is used to bind
     * TextMessageViewHolder content with TextMessage at given position.
     * It shows text of a message if deletedAt = 0 else it shows "message deleted"
     *
     * @param viewHolder is a object of TextMessageViewHolder.
     * @param i is postion of item in recyclerView.
     * @see TextMessage
     * @see BaseMessage
     */
    private void setTextData(TextMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.cometChatTextBubble.messageAlignment(Alignment.LEFT);

        BaseMessage baseMessage = messageList.get(i);
        viewHolder.cometChatTextBubble.cornerRadius(24,24,24,0);
        if (baseMessage!=null) {
            viewHolder.cometChatTextBubble.messageObject(baseMessage);
             if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.cometChatTextBubble.cornerRadius(24,24,0,24);
                 if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                         && !isLeftAligned) {
                     viewHolder.cometChatTextBubble.userNameVisibility(View.GONE);
                     viewHolder.cometChatTextBubble.avatarVisibility(View.GONE);
                 } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                     if (isUserDetailVisible) {
                         viewHolder.cometChatTextBubble.userNameVisibility(View.VISIBLE);
                         viewHolder.cometChatTextBubble.avatarVisibility(View.VISIBLE);
                     }
                     else {
                         viewHolder.cometChatTextBubble.userNameVisibility(View.GONE);
                         viewHolder.cometChatTextBubble.avatarVisibility(View.INVISIBLE);
                     }

                     viewHolder.cometChatTextBubble.avatar(baseMessage.getSender().getAvatar(),
                             baseMessage.getSender().getName());
                     viewHolder.cometChatTextBubble.userName(baseMessage.getSender().getName());
                 }
             }

             viewHolder.cometChatTextBubble.replyCount(baseMessage.getReplyCount());

             viewHolder.cometChatTextBubble.setMessageBubbleListener(new MessageBubbleListener() {
                 @Override
                 public void onClick(BaseMessage baseMessage) {

                 }

                 @Override
                 public void onLongCLick(BaseMessage baseMessage) {
                     setLongClickSelectedItem(baseMessage);
                     messageLongClick.setTextMessageLongClick(longselectedItemList);
                     notifyItemChanged(i);
                 }

                 @Override
                 public void onReactionClick(Reaction reaction, int baseMessageID) {
                     try {
                         Extensions.sendReactionOptimistic(reaction,baseMessage);
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     notifyItemChanged(i);
                     sendReaction(reaction.getName(),baseMessageID);
                 }
             });

             viewHolder.itemView.setTag(R.string.message, baseMessage);
         }
    }


    private void setCustomData(CustomMessageViewHolder viewHolder, int i) {

        BaseMessage baseMessage = messageList.get(i);
        if (baseMessage!=null) {
            if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                        && !isLeftAligned) {
                    viewHolder.userName.setVisibility(View.GONE);
                    viewHolder.avatar.setVisibility(View.GONE);
                } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    if (isUserDetailVisible)
                    {
                        viewHolder.userName.setVisibility(View.VISIBLE);
                        viewHolder.avatar.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        viewHolder.userName.setVisibility(View.GONE);
                        viewHolder.avatar.setVisibility(View.INVISIBLE);
                    }
                }
                viewHolder.userName.setText(baseMessage.getSender().getName());
                viewHolder.avatar.setAvatar(baseMessage.getSender().getAvatar(),baseMessage.getSender().getName());
            }
            viewHolder.title.setText(context.getResources().getString(R.string.custom_message)+"("+baseMessage.getType()+")");
            viewHolder.title.setTypeface(fontUtils.getTypeFace(FontUtils.robotoLight));

            viewHolder.customMessageLayout.setOnClickListener(view -> {
                setSelectedMessage(baseMessage.getId());
                notifyDataSetChanged();

            });

            viewHolder.itemView.setTag(R.string.message, baseMessage);
        }
    }



    private void setLinkData(LinkMessageViewHolder viewHolder, int i) {
        if (isLeftAligned)
            viewHolder.linkMessageBubble.messageAlignment(Alignment.LEFT);
        BaseMessage baseMessage = messageList.get(i);
        viewHolder.linkMessageBubble.cornerRadius(24,24,24,0);
        if (baseMessage!=null) {
            viewHolder.linkMessageBubble.messageObject(baseMessage);
             if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                viewHolder.linkMessageBubble.cornerRadius(24,24,0,24);
                 if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)
                         && !isLeftAligned) {
                     viewHolder.linkMessageBubble.userNameVisibility(View.GONE);
                     viewHolder.linkMessageBubble.avatarVisibility(View.GONE);
                 } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                     if (isUserDetailVisible) {
                         viewHolder.linkMessageBubble.userNameVisibility(View.VISIBLE);
                         viewHolder.linkMessageBubble.avatarVisibility(View.VISIBLE);
                     }
                     else {
                         viewHolder.linkMessageBubble.userNameVisibility(View.GONE);
                         viewHolder.linkMessageBubble.avatarVisibility(View.INVISIBLE);
                     }

                     viewHolder.linkMessageBubble.avatar(baseMessage.getSender().getAvatar(),
                             baseMessage.getSender().getName());
//                     setAvatar(viewHolder.ivUser, baseMessage.getSender().getAvatar(), baseMessage.getSender().getName());
                     viewHolder.linkMessageBubble.userName(baseMessage.getSender().getName());
//                     viewHolder.tvUser.setText(baseMessage.getSender().getName());
                 }
             }
            if (baseMessage.getDeletedAt() == 0) {

            }

            viewHolder.linkMessageBubble.replyCount(baseMessage.getReplyCount());

            viewHolder.linkMessageBubble.setMessageBubbleListener(new MessageBubbleListener() {
                @Override
                public void onLongCLick(BaseMessage baseMessage) {
                        isLongClickEnabled = true;
                        clearLongClickSelectedItem();
                        setLongClickSelectedItem(baseMessage);
                        messageLongClick.setLinkMessageLongClick(longselectedItemList);
                        notifyItemChanged(i);
                }

                @Override
                public void onClick(BaseMessage baseMessage) {

                }

                @Override
                public void onReactionClick(Reaction reaction, int baseMessageID) {
                    try {
                        Extensions.sendReactionOptimistic(reaction,baseMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyItemChanged(i);
                    sendReaction(reaction.getName(),baseMessageID);
                }
            });
        }
    }

    public void setSelectedMessage(Integer id)
    {
        if (selectedItemList.contains(id))
            selectedItemList.remove(id);
        else
            selectedItemList.add(id);
    }

    public void setLongClickSelectedItem(BaseMessage baseMessage) {
        clearLongClickSelectedItem();
        if (longselectedItemList.contains(baseMessage))
            longselectedItemList.remove(baseMessage);
        else
            longselectedItemList.add(baseMessage);
    }
    /**
     * This method is used to set avatar of groupMembers to show in groupMessages. If avatar of
     * group member is not available then it calls <code>setInitials(String name)</code> to show
     * first two letter of group member name.
     *
     * @param avatar is a object of Avatar
     * @param avatarUrl is a String. It is url of avatar.
     * @param name is a String. It is a name of groupMember.
     * @see CometChatAvatar
     *
     */
    private void setAvatar(CometChatAvatar avatar, String avatarUrl, String name) {

        if (avatarUrl != null && !avatarUrl.isEmpty())
            avatar.setAvatar(avatarUrl);
//            Glide.with(context).load(avatarUrl).into(avatar);
        else
            avatar.setInitials(name);

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public long getHeaderId(int var1) {
        BaseMessage baseMessage = messageList.get(var1);
        return Long.parseLong(Utils.getDateId(baseMessage.getSentAt() * 1000));
    }

    @Override
    public DateItemHolder onCreateHeaderViewHolder(ViewGroup var1) {
        View view = LayoutInflater.from(var1.getContext()).inflate(R.layout.cometchat_messagedate_header,
                var1, false);

        return new DateItemHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(DateItemHolder var1, int var2, long var3) {
        BaseMessage baseMessage = messageList.get(var2);
        if (context!=null) {
            var1.txtMessageDate.setBackground(context.getResources().getDrawable(R.drawable.cc_rounded_button));
        }
        if (baseMessage.getSentAt()>0) {
            var1.txtMessageDate.setHeaderDate(baseMessage.getSentAt(),"dd MMM yyyy");
//            Date date = new Date(baseMessage.getSentAt());
//            String formattedDate = Utils.getDate(context,date.getTime());
//            var1.txtMessageDate.setText(formattedDate);
        } else {
            var1.txtMessageDate.text(context.getString(R.string.updating));
        }
    }

    /**
     * This method is used to maintain different viewType based on message category and type and
     * returns the different view types to adapter based on it.
     *
     * Ex :- For message with category <b>CometChatConstants.CATEGORY_MESSAGE</b> and type
     * <b>CometChatConstants.MESSAGE_TYPE_TEXT</b> and message is sent by a <b>Logged-in user</b>,
     * It will return <b>RIGHT_TEXT_MESSAGE</b>
     *
     *
     * @param position is a position of item in recyclerView.
     * @return It returns int which is value of view type of item.
     *
     * @see MessageAdapter#onCreateViewHolder(ViewGroup, int)
     * @see BaseMessage
     *
     */
    private int getItemViewTypes(int position) {
        BaseMessage baseMessage = messageList.get(position);
        HashMap<String,JSONObject> extensionList = Extensions.extensionCheck(baseMessage);
        if (baseMessage.getDeletedAt()==0) {
            if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_MESSAGE)) {
                switch (baseMessage.getType()) {

                    case CometChatConstants.MESSAGE_TYPE_TEXT:
                        if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                            if (extensionList != null && extensionList.containsKey("linkPreview") && extensionList.get("linkPreview") != null)
                                return RIGHT_LINK_MESSAGE;
                            else if (baseMessage.getMetadata()!=null && baseMessage.getMetadata().has("reply"))
                                return RIGHT_REPLY_TEXT_MESSAGE;
                            else
                                return RIGHT_TEXT_MESSAGE;
                        } else {
                            if (extensionList != null && extensionList.containsKey("linkPreview") && extensionList.get("linkPreview") != null)
                                return LEFT_LINK_MESSAGE;
                            else if (baseMessage.getMetadata()!=null && baseMessage.getMetadata().has("reply"))
                                return LEFT_REPLY_TEXT_MESSAGE;
                            else
                                return LEFT_TEXT_MESSAGE;
                        }
                    case CometChatConstants.MESSAGE_TYPE_AUDIO:
                        if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                            return RIGHT_AUDIO_MESSAGE;
                        } else {
                            return LEFT_AUDIO_MESSAGE;
                        }
                    case CometChatConstants.MESSAGE_TYPE_IMAGE:
                        if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                            return RIGHT_IMAGE_MESSAGE;
                        } else {
                            return LEFT_IMAGE_MESSAGE;
                        }
                    case CometChatConstants.MESSAGE_TYPE_VIDEO:
                        if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                            return RIGHT_VIDEO_MESSAGE;
                        } else {
                            return LEFT_VIDEO_MESSAGE;
                        }
                    case CometChatConstants.MESSAGE_TYPE_FILE:
                        if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                            return RIGHT_FILE_MESSAGE;
                        } else {
                            return LEFT_FILE_MESSAGE;
                        }
                    default:
                        return -1;
                }
            } else {
                if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_ACTION)) {
                    return ACTION_MESSAGE;
                } else if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CALL)) {
                    Call call = (Call)baseMessage;
                    if(call.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid()))
                        return RIGHT_CALL_MESSAGE;
                    else
                        return LEFT_CALL_MESSAGE;
                } else if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)){
                    if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                        if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.LOCATION))
                            return RIGHT_LOCATION_CUSTOM_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.POLLS))
                            return RIGHT_POLLS_CUSTOM_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.STICKERS))
                            return RIGHT_STICKER_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.WHITEBOARD))
                            return RIGHT_WHITEBOARD_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.WRITEBOARD))
                            return RIGHT_WRITEBOARD_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.GROUP_CALL))
                            return RIGHT_GROUP_CALL_MESSAGE;
                        else
                            return RIGHT_CUSTOM_MESSAGE;
                    }
                    else
                        if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.LOCATION))
                            return LEFT_LOCATION_CUSTOM_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.POLLS))
                            return LEFT_POLLS_CUSTOM_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.STICKERS))
                            return LEFT_STICKER_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.WHITEBOARD))
                            return LEFT_WHITEBOARD_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.WRITEBOARD))
                            return LEFT_WRITEBOARD_MESSAGE;
                        else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.IntentStrings.GROUP_CALL))
                            return LEFT_GROUP_CALL_MESSAGE;
                        else
                            return LEFT_CUSTOM_MESSAGE;
                }
            }
        }
        else
        {
            if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
                return RIGHT_DELETE_MESSAGE;
            } else {
                return LEFT_DELETE_MESSAGE;
            }
        }
        return -1;

    }

    /**
     * This method is used to update message list of adapter.
     *
     * @param baseMessageList is list of baseMessages.
     *
     */
    public void updateList(List<BaseMessage> baseMessageList) {
        setMessageList(baseMessageList);
    }

    /**
     * This method is used to set real time delivery receipt of particular message in messageList
     * of adapter by updating message.
     *
     * @param messageReceipt is a object of MessageReceipt.
     * @see MessageReceipt
     */
    public void setDeliveryReceipts(MessageReceipt messageReceipt) {

        for (int i = messageList.size() - 1; i >= 0; i--) {
            BaseMessage baseMessage = messageList.get(i);
            if (baseMessage.getDeliveredAt() == 0) {
                int index = messageList.indexOf(baseMessage);
                messageList.get(index).setDeliveredAt(messageReceipt.getDeliveredAt());
            }
        }
        notifyDataSetChanged();
    }

    /**
     * This method is used to set real time read receipt of particular message in messageList
     * of adapter by updating message.
     *
     * @param messageReceipt is a object of MessageReceipt.
     * @see MessageReceipt
     */
    public void setReadReceipts(MessageReceipt messageReceipt) {
        for (int i = messageList.size() - 1; i >= 0; i--) {
            BaseMessage baseMessage = messageList.get(i);
            if (baseMessage.getReadAt() == 0) {
                int index = messageList.indexOf(baseMessage);
                messageList.get(index).setReadAt(messageReceipt.getReadAt());
            }
        }

        notifyDataSetChanged();
    }

    /**
     * This method is used to add message in messageList when send by a user or when received in
     * real time.
     *
     * @param baseMessage is a object of BaseMessage. It is new message which will added.
     * @see BaseMessage
     *
     */
    public void addMessage(BaseMessage baseMessage) {
//        if (!messageList.contains(baseMessage)) {
            messageList.add(baseMessage);
            selectedItemList.clear();
//        }
        notifyItemInserted(messageList.size()-1);
    }

    /**
     * THis method is used to update the old message with the new message
     * @param baseMessage
     */
    public void updateMessageFromMuid(BaseMessage baseMessage) {
        for (int i = messageList.size() - 1; i >= 0; i--) {
            String muid = messageList.get(i).getMuid();
            if (muid!=null && muid.equals(baseMessage.getMuid())) {
                messageList.remove(i);
                messageList.add(i,baseMessage);
                notifyItemChanged(i);
            }
        }
    }
    /**
     * This method is used to update previous message with new message in messageList of adapter.
     *
     * @param baseMessage is a object of BaseMessage. It is new message which will be updated.
     */
    public void updateMessage(BaseMessage baseMessage) {

        if (messageList.contains(baseMessage)) {
            int index = messageList.indexOf(baseMessage);
            messageList.remove(baseMessage);
            messageList.add(index, baseMessage);
            notifyItemChanged(index);
        }
    }

    public void remove(BaseMessage baseMessage) {
        int index = messageList.indexOf(baseMessage);
        messageList.remove(baseMessage);
        notifyItemRemoved(index);
    }
    public void resetList() {
        messageList.clear();
        notifyDataSetChanged();
    }

    private void clearLongClickSelectedItem() {
        isLongClickEnabled = false;
        longselectedItemList.clear();
        notifyDataSetChanged();
    }

    public BaseMessage getLastMessage() {
        if (messageList.size()>0)
        {
            Log.e(TAG, "getLastMessage: "+messageList.get(messageList.size()-1 ));
            return messageList.get(messageList.size()-1);
        }
        else
            return null;
    }

    public int getPosition(BaseMessage baseMessage){
        return messageList.indexOf(baseMessage);
    }

    public void updateReplyCount(int parentMessageId) {
        for(BaseMessage baseMessage : messageList) {
            if (baseMessage.getId()==parentMessageId) {
                int replyCount = baseMessage.getReplyCount();
                baseMessage.setReplyCount(++replyCount);
                notifyItemChanged(messageList.indexOf(baseMessage));
            }
        }
    }

    public void setLeftAligned(boolean b) {
        isLeftAligned = b;
    }

    public void setRightMessageBubbleColor(@ColorInt int color) {
        rightMessageBubbleColor = color;
    }

    public void setRightMessageTextColor(@ColorInt int color) {
        rightMessageTextColor = color;
    }

    public void setLeftMessageBubbleColor(@ColorInt int color) {
        leftMessageBubbleColor = color;
    }

    public void setLeftMessageTextColor(@ColorInt int color) { leftMessageTextColor = color; }

    class ImageMessageViewHolder extends RecyclerView.ViewHolder {

        private final CometChatImageBubble cometChatImageBubble;

        public ImageMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            cometChatImageBubble = view.findViewById(R.id.image_message_bubble);
        }
    }

    public class GroupActionMessageViewHolder extends RecyclerView.ViewHolder {

        private final CometChatGroupActionBubble actionMessageBubble;

        public GroupActionMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            actionMessageBubble = view.findViewById(R.id.action_message_bubble);
        }
    }


    public class CallActionMessageViewHolder extends RecyclerView.ViewHolder {

        private final CometChatCallActionBubble actionMessageBubble;

        public CallActionMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            actionMessageBubble = view.findViewById(R.id.action_message_bubble);
        }
    }

    class VideoMessageViewHolder extends RecyclerView.ViewHolder {

        private final CometChatVideoBubble cometChatVideoBubble;

        public VideoMessageViewHolder(@NonNull View view) {
            super(view);
            int type = (int) view.getTag();
            cometChatVideoBubble = view.findViewById(R.id.video_message_bubble);
        }
    }

    public class FileMessageViewHolder extends RecyclerView.ViewHolder {

        public CometChatFileBubble cometChatFileBubble;

        FileMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            cometChatFileBubble = itemView.findViewById(R.id.file_message_bubble);
        }
    }

    public class DeleteMessageViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final int type;
        private final CometChatTextBubble cometChatTextBubble;
        DeleteMessageViewHolder(@NonNull View view) {
            super(view);
            type = (int) view.getTag();
            cometChatTextBubble = view.findViewById(R.id.textMessageBubble);
            this.view = view;
        }
    }

    /**
     * This is TextMessageViewHolder which is used to handle TextMessage.
     */
    public class TextMessageViewHolder extends RecyclerView.ViewHolder {

        private final CometChatTextBubble cometChatTextBubble;
        private CometChatReactionView cometChatReactionView;
        private final int type;
        private final View view;
        TextMessageViewHolder(@NonNull View view) {
            super(view);
            type = (int) view.getTag();
            cometChatTextBubble = view.findViewById(R.id.textMessageBubble);
            this.view = view;
        }
    }

    public class CustomMessageViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView userName;
        private final CometChatAvatar avatar;
        private final CometChatMessageReceipt receipt;
        private final CometChatMessageReaction reactions;
        private final CometChatDate time;
        private final int type;
        private final LinearLayout customMessageLayout;


        CustomMessageViewHolder(@NonNull View view) {
            super(view);
            type = (int) view.getTag();
            title = view.findViewById(R.id.text);
            userName = view.findViewById(R.id.tv_user);
            avatar = view.findViewById(R.id.iv_user);
            receipt = view.findViewById(R.id.receiptsIcon);
            time = view.findViewById(R.id.time);
            reactions = view.findViewById(R.id.reactions_group);
            customMessageLayout = view.findViewById(R.id.custom_layout);
        }
    }

    public class StickerMessageViewHolder extends RecyclerView.ViewHolder {
        public CometChatStickerBubble cometChatStickerBubble;
        public int type;
        public View view;

        public StickerMessageViewHolder(View itemView) {
            super(itemView);

            type =  (int) itemView.getTag();
            cometChatStickerBubble = itemView.findViewById(R.id.sticker_message_bubble);
            this.view = itemView;

        }
    }
    public class LocationMessageViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final int type;
        public CometChatLocationBubble cometChatLocationBubble;

        public LocationMessageViewHolder(View itemView) {
            super(itemView);

            type = (int) itemView.getTag();
            cometChatLocationBubble = itemView.findViewById(R.id.location_message_bubble);
            this.view = itemView;
        }
    }

    public class PollMessageViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final int type;
        public CometChatPollBubble cometChatPollBubble;

        public PollMessageViewHolder(View itemView) {
            super(itemView);

            type = (int) itemView.getTag();
            cometChatPollBubble = itemView.findViewById(R.id.polls_message_bubble);
            this.view = itemView;
        }
    }


    public class GroupCallMessageViewHolder extends RecyclerView.ViewHolder {


        private final View view;
        private final int type;
        public CometChatConferenceBubble cometChatConferenceBubble;

        GroupCallMessageViewHolder(@NonNull View view) {
            super(view);
            type = (int) view.getTag();
            cometChatConferenceBubble = view.findViewById(R.id.group_call_bubble);
            this.view = view;

        }
    }

    public class WhiteBoardViewHolder extends RecyclerView.ViewHolder {


        private final View view;

        private final int type;

        public CometChatWhiteboardBubble cometChatWhiteboardBubble;

        WhiteBoardViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            cometChatWhiteboardBubble = view.findViewById(R.id.whiteboard_message_bubble);
            this.view = view;

        }
    }


    public class WriteBoardViewHolder extends RecyclerView.ViewHolder {


        private final View view;
        private final int type;
        private final CometChatDocumentBubble documentMessageBubble;

        WriteBoardViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            documentMessageBubble = view.findViewById(R.id.writeboard_message_bubble);
            this.view = view;

        }
    }


    public class AudioMessageViewHolder extends RecyclerView.ViewHolder {
       public CometChatAudioBubble cometChatAudioBubble;
       private final int type;
       public AudioMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            type = (int)itemView.getTag();
            cometChatAudioBubble = itemView.findViewById(R.id.audio_message_bubble);
        }
    }
    public class LinkMessageViewHolder extends RecyclerView.ViewHolder {


        private final View view;
        private final int type;
        private final CometChatTextBubble linkMessageBubble;

        LinkMessageViewHolder(@NonNull View view) {
            super(view);

            type = (int) view.getTag();
            linkMessageBubble = view.findViewById(R.id.link_message_bubble);
            this.view = view;
        }
    }

    public class DateItemHolder extends RecyclerView.ViewHolder {

        CometChatDate txtMessageDate;

        DateItemHolder(@NonNull View itemView) {
            super(itemView);
            txtMessageDate = itemView.findViewById(R.id.txt_message_date);
        }
    }

    public interface OnMessageLongClick
    {
        void setTextMessageLongClick(List<BaseMessage> baseMessage);
        void setImageMessageLongClick(List<BaseMessage> baseMessage);
        void setVideoMessageLongClick(List<BaseMessage> baseMessage);
        void setAudioMessageLongClick(List<BaseMessage> baseMessage);
        void setFileMessageLongClick(List<BaseMessage> baseMessage);
        void setDocumentMessageLongClick(List<BaseMessage> baseMessage);
        void setWhiteboardMessageLongClick(List<BaseMessage> baseMessage);
        void setPollMessageLongClick(List<BaseMessage> baseMessage);
        void setStickerMessageLongClick(List<BaseMessage> baseMessage);
        void setConferenceMessageLongClick(List<BaseMessage> baseMessage);
        void setLocationMessageLongClick(List<BaseMessage> baseMessage);
        void setLinkMessageLongClick(List<BaseMessage> baseMessage);
    }


}



