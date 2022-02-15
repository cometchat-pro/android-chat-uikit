package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.EmojiSpan;

import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TextMessageType;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TimeAlignment;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.pattern_utils.PatternUtils;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CometChatTextBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;
    private ShapeableImageView image;
    private TextView title;
    private TextView subtitle;
    private TextView paragraph;

    private ImageView videoPlayBtn;
    private TextView visitBtn;

    private TextView txtMessage;            //Text Message

    private TextView tvThreadReplyCount;    //Thread Reply Count
    private CometChatDate txtTime;                //Message time.
    private CometChatMessageReceipt receipt;
    private ImageView imgStatus;

    private CometChatAvatar ivUser;                  //sender avatar
    private TextView tvUser;                 //sender name

    private View receiptLayout;

    private RelativeLayout sentimentVw;     //sentiment extension layout
    private TextView viewSentimentMessage;  //sentiment extension text

    private MaterialCardView replyLayout;     //reply message layout
    private TextView replyUser;             //reply message sender name
    private TextView replyMessage;          //reply message text
    private ImageView replyMessageImage;

    private CometChatMessageReaction reactionLayout;

    private String alignment = Alignment.RIGHT;

    private int borderColor = 0;

    private int borderWidth = 0;

    private final String TAG = "TextMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private int reactionStrokeColor = Color.parseColor(CometChatTheme.primaryColor);

    private @TextMessageType.Type String type = TextMessageType.Type.TEXT_MESSSAGE;

    private int layoutId;

    public CometChatTextBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatTextBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatTextBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatTextBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.TextMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.TextMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.TextMessageBubble_backgroundColor,0);
        Drawable textMessageAvatar = a.getDrawable(R.styleable.TextMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.TextMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.TextMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.TextMessageBubble_userName);
        int color = a.getColor(R.styleable.TextMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.TextMessageBubble_messageAlignment,0));
        type = TextMessageType.getValue(a.getInt(R.styleable.TextMessageBubble_messageType,0));
        String textMessage = a.getString(R.styleable.TextMessageBubble_text);
        int textMessageColor = a.getColor(R.styleable.TextMessageBubble_text_color,0);

        String titleStr = a.getString(R.styleable.TextMessageBubble_title);
        int titleColor = a.getColor(R.styleable.TextMessageBubble_titleColor,0);

        String subtitleStr = a.getString(R.styleable.TextMessageBubble_subtitle);
        int subtitleColor = a.getColor(R.styleable.TextMessageBubble_subtitleColor,0);

        String paragraphStr = a.getString(R.styleable.TextMessageBubble_paragraph);

        Drawable imageDrawable = a.getDrawable(R.styleable.TextMessageBubble_image);

        String buttonStr = a.getString(R.styleable.TextMessageBubble_buttonText);
        int playBtnVisible = a.getInt(R.styleable.TextMessageBubble_iconVisibility,View.GONE);

        borderColor = a.getColor(R.styleable.TextMessageBubble_borderColor,0);
        borderWidth = a.getInt(R.styleable.TextMessageBubble_borderWidth,0);

        if(type.equalsIgnoreCase(TextMessageType.Type.LINK_MESSAGE)) {
            if (alignment.equals(Alignment.LEFT))
                view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_link_bubble, null);
            else
                view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_link_bubble, null);
        } else {
            if (alignment.equalsIgnoreCase(Alignment.LEFT))
                view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_text_bubble, null);
            else
                view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_text_bubble, null);
        }

        initView(view);

        //Common Layout
        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        avatar(textMessageAvatar);
        avatarVisibility(hideAvatar);
        userName(userName);
        userNameVisibility(hideUserName);
        userNameColor(color);

        //Set LinkMessage
        title(titleStr);
        titleColor(titleColor);
        subtitle(subtitleStr);
        subtitleColor(subtitleColor);
        paragraph(paragraphStr);
        image(imageDrawable);
        buttonText(buttonStr);
        playButtonVisible(playBtnVisible);

        borderColor(borderColor);
        borderWidth(borderWidth);
        if (textMessageColor!=0)
            textColor(textMessageColor);

        //set message
        String strMessage = null;
        if(textMessage!=null)
            strMessage = textMessage;
        if (txtMessage!=null) {
            txtMessage.setText(strMessage);
            txtMessage.setTextSize(16f);
        }

        if (strMessage!=null) {
            checkForEmoji(strMessage);
            PatternUtils.setHyperLinkSupport(context, txtMessage);
        }

//        setColorFilter(baseMessage,cvMessageView);
    }

    private void initView(View view) {

        addView(view);
        //LinkMessage
        image = view.findViewById(R.id.link_img);
        title = view.findViewById(R.id.link_title);
        subtitle = view.findViewById(R.id.link_subtitle);
        paragraph = view.findViewById(R.id.message);

        visitBtn = view.findViewById(R.id.visitLink);
        videoPlayBtn = view.findViewById(R.id.videoLink);

        //TextMessage
        txtMessage = view.findViewById(R.id.text_message);



        //Reply Message Layout
        replyLayout = view.findViewById(R.id.replyLayout);
        replyUser = view.findViewById(R.id.reply_user);
        replyMessageImage = view.findViewById(R.id.reply_image);
        replyMessage = view.findViewById(R.id.reply_message);

        //Sentiment Analysis
        sentimentVw = view.findViewById(R.id.sentiment_layout);
        viewSentimentMessage = view.findViewById(R.id.view_sentiment);

        tvUser = view.findViewById(R.id.tv_user);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);
        txtTime = view.findViewById(R.id.time);
        receipt = view.findViewById(R.id.receiptsIcon);
        receiptLayout = view.findViewById(R.id.receipt_layout);
        imgStatus = view.findViewById(R.id.img_pending);
        ivUser = view.findViewById(R.id.iv_user);
        tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);

        reactionLayout = view.findViewById(R.id.reactions_group);
        reactionLayout.setReactionEventListener(new CometChatMessageReaction.OnReactionClickListener() {
            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                messageBubbleListener.onReactionClick(reaction,baseMessageID);
            }
        });

        //CustomView
        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.text);
        if(messageTemplate!=null)
            layoutId = messageTemplate.getView();
//        dataView = messageTemplate.getDataView();
        if (layoutId != 0) {
            View customView = LayoutInflater.from(context).inflate(layoutId,null);
            cvMessageBubbleLayout.removeAllViewsInLayout();
            if (customView.getParent()!=null)
                ((ViewGroup)customView.getParent()).removeAllViewsInLayout();
            cvMessageBubbleLayout.addView(customView);
            //TextMessage
            txtMessage = customView.findViewById(R.id.text_message);

            customView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
        }
    }

    private void checkForExtensions(String strMessage) {
        String message = strMessage;
        if(CometChat.isExtensionEnabled("profanity-filter")) {
            message = Extensions.checkProfanityMessage(context,baseMessage);
        }

        if(CometChat.isExtensionEnabled("data-masking")) {
            message = Extensions.checkDataMasking(context,baseMessage);
        }


        if (baseMessage.getMetadata()!=null && baseMessage.getMetadata().has("values")) {
            try {
                if (Extensions.isMessageTranslated(baseMessage.getMetadata().getJSONObject("values"), ((TextMessage) baseMessage).getText())) {
                    String translatedMessage = Extensions.getTranslatedMessage(baseMessage);
                    message = message + "\n(" + translatedMessage + ")";
                }
            } catch (JSONException e) {
                Toast.makeText(context, context.getString(R.string.no_translation_available), Toast.LENGTH_SHORT).show();
            }
        }

        if (txtMessage!=null) {
            txtMessage.setText(message);
            PatternUtils.setHyperLinkSupport(context,txtMessage);
            PatternUtils.setHtmlSupport(txtMessage);
        }
    }

    private void checkForEmoji(String strMessage) {
        int count = 0;
        CharSequence processed = EmojiCompat.get().process(strMessage, 0,
                strMessage.length() -1, Integer.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL);
        if (processed instanceof Spannable) {
            Spannable spannable = (Spannable) processed;
            count = spannable.getSpans(0, spannable.length() - 1, EmojiSpan.class).length;
            if (PatternUtils.removeEmojiAndSymbol(strMessage).trim().length() == 0) {
                int tempcount = count;
                if (tempcount == 1) {
                    txtMessage.setTextSize((int) Utils.dpToPx(context, 32));
                } else if (tempcount == 2) {
                    txtMessage.setTextSize((int) Utils.dpToPx(context, 24));
                }
            }
        }
    }

    private void checkFoReplyMessage() {
        if (baseMessage.getMetadata()!=null) {
            try {
                JSONObject metaData = baseMessage.getMetadata();
                if (metaData.has("reply-message")) {
                    JSONObject replyMessageJSON = metaData.getJSONObject("reply-message");
                    BaseMessage replyBaseMessage = CometChatHelper.processMessage(replyMessageJSON);
                    String messageType = replyBaseMessage.getType();
                    replyLayout.setVisibility(View.VISIBLE);
                    replyUser.setVisibility(View.VISIBLE);
                    if (replyBaseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER))
                        replyUser.setText(replyBaseMessage.getSender().getName());
                    else
                        replyUser.setText(replyBaseMessage.getSender().getName()+
                                " ~ "+((Group)replyBaseMessage.getReceiver()).getName());
                    if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                        String message = ((TextMessage)replyBaseMessage).getText();
                        if(CometChat.isExtensionEnabled("profanity-filter")) {
                            message = Extensions.checkProfanityMessage(context,replyBaseMessage);
                        }
                        if(CometChat.isExtensionEnabled("data-masking")) {
                            message = Extensions.checkDataMasking(context,replyBaseMessage);
                        }
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(message);
                    } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                        replyMessage.setText(context.getResources().getString(R.string.message_image));
                        replyMessageImage.setVisibility(View.GONE);
                    } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(context.getResources()
                                .getString(R.string.message_audio));
                    } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(context.getResources().getString(R.string.message_video));
                    } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                        replyMessage.setText(context.getResources().getString(R.string.message_file));
                        replyMessageImage.setVisibility(View.GONE);
                    } else if (messageType.equals(UIKitConstants.IntentStrings.LOCATION)) {
                        replyMessage.setText(R.string.custom_message_location);
                        replyMessageImage.setImageResource(R.drawable.default_map);
                    } else if (messageType.equals(UIKitConstants.IntentStrings.POLLS)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(context.getString(R.string.custom_message_poll));
                    } else if (messageType.equals(UIKitConstants.IntentStrings.STICKERS)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(String.format(context.getString(R.string.custom_message_sticker)));
                    } else if (messageType.equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(context.getString(R.string.custom_message_whiteboard));
                    } else if (messageType.equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(context.getString(R.string.custom_message_document));
                    } else if (messageType.equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
                        replyMessageImage.setVisibility(View.GONE);
                        replyMessage.setText(context.getString(R.string.custom_message_meeting));
                    }
                } else {
                    replyLayout.setVisibility(View.GONE);
                }
            } catch(Exception e){
                Log.e(TAG, "setTextData: " + e.getMessage());
            }
        }
    }

    private void checkSentimentAnalysis() {
        boolean isSentimentNegative = Extensions.checkSentiment(baseMessage);
        if (isSentimentNegative) {
           txtMessage.setVisibility(View.GONE);
           sentimentVw.setVisibility(View.VISIBLE);
        }
        else {
           txtMessage.setVisibility(View.VISIBLE);
           sentimentVw.setVisibility(View.GONE);
        }
        viewSentimentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder sentimentAlert = new AlertDialog.Builder(context)
                        .setTitle(context.getResources().getString(R.string.sentiment_alert))
                        .setMessage(context.getResources().getString(R.string.sentiment_alert_message))
                        .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtMessage.setVisibility(View.VISIBLE);
                                sentimentVw.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                sentimentAlert.create().show();
            }
        });
    }


    public void title(String str) {
        if (title!=null)
            title.setText(str);
    }
    public void titleColor(@ColorInt int color) {
        if (title!=null && color!=0)
            title.setTextColor(color);
    }
    public void titleFont(String font) {
        if (title!=null)
            title.setTypeface(fontUtils.getTypeFace(font));
    }

    public void subtitle(String subStr) {
        if (subtitle!=null)
            subtitle.setText(subStr);
    }
    public void subtitleColor(@ColorInt int color) {
        if (subtitle!=null && color!=0)
            subtitle.setTextColor(color);
    }
    public void subtitleFont(String font) {
        if (subtitle!=null)
            subtitle.setTypeface(fontUtils.getTypeFace(font));
    }

    public void paragraph(String str) {
        if (paragraph!=null)
            paragraph.setText(str);
    }

    public void paragraphColor(@ColorInt int color) {
        if (paragraph!=null)
            paragraph.setTextColor(color);
    }

    public void paragraphFont(String font) {
        if (paragraph!=null)
            paragraph.setTypeface(fontUtils.getTypeFace(font));
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight).build());

        if (image!=null)
            image.setShapeAppearanceModel(image.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopRightCornerSize(topLeft)
                    .setTopLeftCornerSize(topRight).build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);

        if (image!=null)
            image.setShapeAppearanceModel(image.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopRightCornerSize(radius)
                    .setTopLeftCornerSize(radius).build());
    }

    public void buttonText(String buttonStr) {
        if (visitBtn!=null)
            visitBtn.setText(buttonStr);
    }

    public void buttonTextColor(@ColorInt int color) {
        if (visitBtn!=null)
            visitBtn.setTextColor(color);
    }
    public void buttonBackground(@ColorInt int color) {
        if (visitBtn!=null)
            visitBtn.setBackgroundColor(color);
    }

    public void playButtonVisible(int visibility) {
        if (videoPlayBtn!=null)
            videoPlayBtn.setVisibility(visibility);
    }

    public void image(Drawable drawable) {
        if (image!=null && drawable!=null)
            image.setImageDrawable(drawable);
    }
    public void image(String url) {
        if (image!=null && url!=null)
            Glide.with(context).load(url).into(image);
    }

    public View getBubbleView() {
        return cvMessageBubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble!=null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }
    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble!=null) {
            if (bgColor != 0)
                cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }

    public void borderColor(@ColorInt int color) {
        borderColor=color;
        if (cvMessageBubble!=null) {
            if (color!=0)
                cvMessageBubble.setStrokeColor(borderColor);
        }
    }

    public void borderWidth(int width) {
        borderWidth= width;
        if (cvMessageBubble!=null) {
            cvMessageBubble.setStrokeWidth(width);
        }
    }

    public void avatar(Drawable avatarDrawable) {
        if (ivUser!=null)
            ivUser.setDrawable(avatarDrawable);
    }

    public void avatar(String url,String initials) {
        if (ivUser!=null) {
            ivUser.setInitials(initials);
            if (url != null)
                ivUser.setAvatar(url);
        }
    }

    public void avatarVisibility(int visibility) {
        if (ivUser!=null) {
            ivUser.setVisibility(visibility);
        }
    }

    public void userName(String username) {
        if (tvUser!=null)
            tvUser.setText(username);
    }
    public void userNameFont(String font) {
        if (tvUser!=null)
            tvUser.setTypeface(fontUtils.getTypeFace(font));
    }

    public void userNameColor(@ColorInt int color){
        if (tvUser!=null && color!=0)
            tvUser.setTextColor(color);
    }

    public void userNameVisibility(int visibility) {
        if (tvUser!=null) {
            tvUser.setVisibility(visibility);
        }
    }

    public void setReactionBorderColor(@ColorInt int strokeColor) {
        if (strokeColor!=0) {
            this.reactionStrokeColor = strokeColor;
            reactionLayout.setBorderColor(strokeColor);
        }
    }
    public void messageAlignment(@Alignment.MessageAlignment String alignment) {
        if (alignment!=null && alignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_text_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_text_bubble,null);

        removeAllViewsInLayout();
        initView(view);
    }

    public void text(String str) {
        if (txtMessage!=null)
            txtMessage.setText(str);
    }

    public void textFont(String font) {
        if (txtMessage!=null)
            txtMessage.setTypeface(fontUtils.getTypeFace(font));
    }

    public void textColor(@ColorInt int color) {
        if (txtMessage!=null) {
            if (color != 0)
                txtMessage.setTextColor(color);
        }
    }

    public void messageReceiptIcon(CometChatMessageReceipt messageReceipt) {
        if (receipt!=null) {
            receipt.messageDeliveredIcon(messageReceipt.getDeliveredIcon());
            receipt.messageReadIcon(messageReceipt.getReadIcon());
            receipt.messageSentIcon(messageReceipt.getSentIcon());
            receipt.messageErrorIcon(messageReceipt.getErrorIcon());
            receipt.messageProgressIcon(messageReceipt.getProgressIcon());
        }
    }

    public void replyCount(int count) {
        if (count!=0) {
            tvThreadReplyCount.setVisibility(View.VISIBLE);
            tvThreadReplyCount.setText(baseMessage.getReplyCount()+" "+context.getResources().getString(R.string.replies));
        } else {
            tvThreadReplyCount.setVisibility(View.GONE);
        }
    }

    public void setReplyCountColor(@ColorInt int color) {
        if (tvThreadReplyCount!=null)
            tvThreadReplyCount.setTextColor(color);
    }

    public void messageTimeAlignment(TimeAlignment timeAlignment) {
        if (timeAlignment == TimeAlignment.TOP) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) receiptLayout.getLayoutParams();
            params.addRule(RelativeLayout.END_OF, R.id.tv_user);
            params.addRule(RelativeLayout.ALIGN_START,0);
            params.addRule(RelativeLayout.BELOW, 0);
            params.topMargin = 0;
            params.leftMargin = 8;

            RelativeLayout.LayoutParams messageBubbleParam = (RelativeLayout.LayoutParams)cvMessageBubble.getLayoutParams();
            messageBubbleParam.topMargin = 8;
            messageBubbleParam.bottomMargin = 8;
//            receiptLayout.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) receiptLayout.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.thread_reply_count);
//            receiptLayout.setLayoutParams(params);
        }
    }
    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
        receipt.messageObject(baseMessage);
        txtTime.setDate(baseMessage.getSentAt(),"hh:mm a");
        txtTime.setTransparentBackground(true);

        if (tvUser!=null)
            tvUser.setText(baseMessage.getSender().getName());
        if (ivUser!=null)
            ivUser.setAvatar(baseMessage.getSender().getAvatar());
        String strMessage = ((TextMessage) baseMessage).getText().trim();
        reactionLayout.setMessage(baseMessage);
        checkForExtensions(strMessage);
        if (!baseMessage.getSender().getUid().equals(loggedInUser.getUid()) && sentimentVw!=null)
            checkSentimentAnalysis();
        checkFoReplyMessage();

        String url="";
        HashMap<String, JSONObject> extensionList = Extensions.extensionCheck(baseMessage);
        if (extensionList!=null) {
            if (extensionList.containsKey("linkPreview")) {
                JSONObject linkPreviewJsonObject = extensionList.get("linkPreview");
                try {
                    String strDescription = linkPreviewJsonObject.getString("description");
                    String strImage = linkPreviewJsonObject.getString("image");
                    String strTitle = linkPreviewJsonObject.getString("title");
                    url = linkPreviewJsonObject.getString("url");
                    Log.e("setLinkData: ", baseMessage.toString() + "\n\n" + url + "\n" + strDescription + "\n" + image);
                    title.setText(strTitle);
                    subtitle.setText(strDescription);
                    Glide.with(context).load(Uri.parse(strImage)).placeholder(R.drawable.ic_defaulf_image).into(image);
                    if (url.contains("youtu.be") || url.contains("youtube")) {
                        videoPlayBtn.setVisibility(View.VISIBLE);
                        visitBtn.setText(context.getResources().getString(R.string.view_on_youtube));
                    } else {
                        videoPlayBtn.setVisibility(View.GONE);
                        visitBtn.setText(context.getResources().getString(R.string.visit));
                    }
                    String messageStr = ((TextMessage) baseMessage).getText();
                    if (((TextMessage) baseMessage).getText().equals(url) || ((TextMessage) baseMessage).getText().equals(url + "/")) {
                        paragraph.setVisibility(View.GONE);
                    } else {
                        paragraph.setVisibility(View.VISIBLE);
                    }
                    paragraph.setText(messageStr);
                } catch (Exception e) {
                    Log.e("setLinkData: ", e.getMessage());
                }
            }
        }
        if (paragraph!=null) {
            PatternUtils.setHyperLinkSupport(context,paragraph);
            PatternUtils.setHtmlSupport(paragraph);
        }
        String finalUrl = url;

        cvMessageBubble.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                messageBubbleListener.onClick(baseMessage);
            }
        });

        cvMessageBubble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
        txtMessage.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });

        if (visitBtn!=null) {
            visitBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalUrl != null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(finalUrl));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    public void textFontStyle(int style) {
        txtMessage.setTypeface(null,style);
    }
}
