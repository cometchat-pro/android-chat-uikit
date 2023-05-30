package com.cometchat.chatuikit.shared.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.GravityInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.CometChatAudioBubble.AudioBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatAudioBubble.CometChatAudioBubble;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.CometChatAvatar;
import com.cometchat.chatuikit.shared.views.CometChatDate.CometChatDate;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatFileBubble.CometChatFileBubble;
import com.cometchat.chatuikit.shared.views.CometChatFileBubble.FileBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.CometChatImageBubble;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.ImageBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.CometChatReceipt;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.Receipt;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.CometChatTextBubble;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.CometChatVideoBubble;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.VideoBubbleStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

public class MessageBubbleUtils {

    public static View getTopView(Context context, boolean showTime, boolean showName, String name, String time, @ColorInt int nameColor, @StyleRes int nameAppearance, DateStyle dateStyle) {
        if (context != null) {
            View view = View.inflate(context, R.layout.top_view, null);
            CometChatTextBubble nameText = view.findViewById(R.id.name);
            CometChatDate cometChatDate = view.findViewById(R.id.time);
            cometChatDate.setVisibility(showTime ? View.VISIBLE : View.GONE);
            nameText.setVisibility(!showName ? View.GONE : View.VISIBLE);
            nameText.getTextView().setMaxLines(1);
            nameText.getTextView().setEllipsize(TextUtils.TruncateAt.END);
            nameText.setText(name + "");
            cometChatDate.setCustomDateString(time + "");
            nameText.setStyle(new TextBubbleStyle().setTextColor(nameColor).setTextAppearance(nameAppearance));
            cometChatDate.setStyle(dateStyle);
            return view;
        }
        return null;
    }

    public static View getBottomView(Context context, boolean showTime, boolean showReceipt, @DrawableRes int readIcon, @DrawableRes int deliverIcon, @DrawableRes int sentIcon, @DrawableRes int waitIconIcon, Receipt receipt, String time, DateStyle dateStyle) {
        if (context != null) {
            View view = View.inflate(context, R.layout.bottom_view, null);
            CometChatReceipt cometChatReceipt = view.findViewById(R.id.receipt);
            CometChatDate cometChatDate = view.findViewById(R.id.date_time);
            cometChatDate.setVisibility(showTime ? View.VISIBLE : View.GONE);
            cometChatReceipt.setVisibility(!showReceipt ? View.GONE : View.VISIBLE);
            if (readIcon != 0)
                cometChatReceipt.setReadIcon(context.getResources().getDrawable(readIcon));
            if (deliverIcon != 0)
                cometChatReceipt.setDeliveredIcon(context.getResources().getDrawable(deliverIcon));
            if (sentIcon != 0)
                cometChatReceipt.setSentIcon(context.getResources().getDrawable(sentIcon));
            if (waitIconIcon != 0)
                cometChatReceipt.setWaitIcon(context.getResources().getDrawable(waitIconIcon));
            cometChatReceipt.setReceipt(receipt);
            cometChatDate.setCustomDateString(time + "");
            cometChatDate.setStyle(dateStyle);
            return view;
        }
        return null;
    }

    public static View getDeletedBubble(Context context) {
        if (context != null) {
            CometChatTheme cometChatTheme = CometChatTheme.getInstance(context);
            View view = View.inflate(context, R.layout.text_bubble_layout, null);
            CometChatTextBubble cometChatTextBubble = view.findViewById(R.id.text_bubble);
            cometChatTextBubble.setText(context.getResources().getString(R.string.this_message_deleted));
            cometChatTextBubble.setStyle(new TextBubbleStyle().setBackground(context.getResources().getDrawable(R.drawable.dotted_border)).setTextColor(cometChatTheme.getPalette().getAccent()).setTextAppearance(cometChatTheme.getTypography().getText1()));
            return view;
        }
        return null;
    }

    public static View getTextContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, @GravityInt int gravity) {
        if (context != null && textMessage != null) {
            View view = View.inflate(context, R.layout.text_bubble_layout, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = gravity;
            CometChatTextBubble cometChatTextBubble = view.findViewById(R.id.text_bubble);
            cometChatTextBubble.setLayoutParams(params);
            cometChatTextBubble.setText(textMessage.getText() + "");
            cometChatTextBubble.setStyle(bubbleStyle);
            return view;
        }
        return null;
    }

    public static View getImageContentView(Context context, MediaMessage mediaMessage, ImageBubbleStyle imageBubbleStyle) {
        if (context != null && mediaMessage != null) {
            View view = View.inflate(context, R.layout.image_bubble_layout, null);
            CometChatImageBubble cometChatImageBubble = view.findViewById(R.id.image_bubble);
            Attachment attachment = mediaMessage.getAttachment();
            if (attachment != null)
                cometChatImageBubble.setImageUrl(attachment.getFileUrl(), 0, attachment.getFileExtension().equalsIgnoreCase(".gif"));
            cometChatImageBubble.setCaption(mediaMessage.getCaption());
            cometChatImageBubble.setStyle(imageBubbleStyle);
            return view;
        }
        return null;
    }

    public static View getVideoContentView(Context context, String thumbnailUrl, VideoBubbleStyle videoBubbleStyle, MediaMessage mediaMessage) {
        if (context != null && mediaMessage != null) {
            View view = View.inflate(context, R.layout.video_bubble_layout, null);
            Attachment attachment = mediaMessage.getAttachment();
            CometChatVideoBubble cometChatVideoBubble = view.findViewById(R.id.video_bubble);
            if (attachment != null) cometChatVideoBubble.setVideoUrl(attachment.getFileUrl(), 0);
            if (thumbnailUrl != null && !thumbnailUrl.isEmpty())
                cometChatVideoBubble.setThumbnailUrl(thumbnailUrl, 0);
            cometChatVideoBubble.setStyle(videoBubbleStyle);
            return view;
        }
        return null;
    }

    public static View getFileContentView(Context context, MediaMessage mediaMessage, FileBubbleStyle fileBubbleStyle) {
        if (context != null && mediaMessage != null) {
            View view = View.inflate(context, R.layout.file_bubble_layout, null);
            Attachment attachment = mediaMessage.getAttachment();
            CometChatFileBubble cometChatFileBubble = view.findViewById(R.id.file_bubble);
            if (attachment != null) {
                int size = attachment.getFileSize();
                cometChatFileBubble.setFileUrl(attachment.getFileUrl(), attachment.getFileName(), Utils.getFileSize(size));
            } else cometChatFileBubble.setFileUrl(null, context.getString(R.string.uploading), "-");
            cometChatFileBubble.setStyle(fileBubbleStyle);
            return view;
        }
        return null;
    }

    public static View getAudioContentView(Context context, MediaMessage mediaMessage, AudioBubbleStyle audioBubbleStyle) {
        if (context != null && mediaMessage != null) {
            View view = View.inflate(context, R.layout.audio_bubble_layout, null);
            Attachment attachment = mediaMessage.getAttachment();
            CometChatAudioBubble cometChatAudioBubble = view.findViewById(R.id.audio_bubble);
            if (attachment != null) {
                int size = attachment.getFileSize();
                cometChatAudioBubble.setAudioUrl(attachment.getFileUrl(), attachment.getFileName(), Utils.getFileSize(size));
            } else
                cometChatAudioBubble.setAudioUrl(null, context.getString(R.string.uploading), "-");
            cometChatAudioBubble.setStyle(audioBubbleStyle);
            return view;
        }
        return null;
    }

    public static View getActionContentView(Context context, String text, Action action, TextBubbleStyle textBubbleStyle, @DrawableRes int startIcon) {
        if (context != null) {
            View view = View.inflate(context, R.layout.text_bubble_layout, null);
            CometChatTextBubble cometChatTextBubble = view.findViewById(R.id.text_bubble);
            cometChatTextBubble.setCompoundDrawable(startIcon, 0, 0, 0);
            String actionMessage = "";
            cometChatTextBubble.getTextView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            if (text == null && action != null) {
                if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_JOINED))
                    actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.joined);
                else if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED))
                    actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.added) + " " + ((User) action.getActionOn()).getName();
                else if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_KICKED))
                    actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.kicked_by) + " " + ((User) action.getActionOn()).getName();
                else if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_BANNED))
                    actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.ban) + " " + ((User) action.getActionOn()).getName();
                else if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_UNBANNED))
                    actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.unban) + " " + ((User) action.getActionOn()).getName();
                else if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_LEFT))
                    actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.left);
                else if (action.getAction().equals(CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED)) {
                    if (action.getNewScope().equals(CometChatConstants.SCOPE_MODERATOR)) {
                        actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.made) + " " + ((User) action.getActionOn()).getName() + " " + context.getString(R.string.moderator);
                    } else if (action.getNewScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                        actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.made) + " " + ((User) action.getActionOn()).getName() + " " + context.getString(R.string.admin);
                    } else if (action.getNewScope().equals(CometChatConstants.SCOPE_PARTICIPANT)) {
                        actionMessage = ((User) action.getActionBy()).getName() + " " + context.getString(R.string.made) + " " + ((User) action.getActionOn()).getName() + " " + context.getString(R.string.participant);
                    } else actionMessage = action.getMessage();
                }
            } else actionMessage = text;
            cometChatTextBubble.setText(actionMessage);
            cometChatTextBubble.setStyle(textBubbleStyle);
            return view;
        }
        return null;
    }

    public static View getAvatarLeadingView(Context context, boolean showAvatar, String url, String name, AvatarStyle avatarStyle) {
        if (context != null) {
            View view = View.inflate(context, R.layout.avatar_leading_view, null);
            CometChatAvatar avatar = view.findViewById(R.id.avatar);
            avatar.setVisibility(showAvatar ? View.VISIBLE : View.GONE);
            avatar.setImage(url, name);
            avatar.setStyle(avatarStyle);
            avatar.setRadius(100);
            if (avatarStyle.getWidth() > 0 && avatarStyle.getHeight() > 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.convertDpToPx(context, avatarStyle.getWidth()), Utils.convertDpToPx(context, avatarStyle.getHeight()));
                params.rightMargin = Utils.convertDpToPx(context, 8);
                params.topMargin = Utils.convertDpToPx(context, 19);
                avatar.setLayoutParams(params);
            }
            return view;
        }
        return null;
    }

    public static View getThreadView(Context context, BaseMessage baseMessage, String text, @ColorInt int separatorColor, @ColorInt int textColor, @StyleRes int textAppearance, @ColorInt int iconTint, @DrawableRes int icon) {
        if (context != null) {
            View view = View.inflate(context, R.layout.thread_view, null);
            TextView separator = view.findViewById(R.id.separator);
            TextView textView = view.findViewById(R.id.reply_count);
            ImageView imageView = view.findViewById(R.id.next);
            if (baseMessage != null && text == null && baseMessage.getDeletedAt() == 0 && baseMessage.getReplyCount() > 0) {
                if (baseMessage.getReplyCount() == 1)
                    textView.setText(context.getResources().getString(R.string.view) + " " + baseMessage.getReplyCount() + " " + context.getResources().getString(R.string.reply_txt));
                else
                    textView.setText(context.getResources().getString(R.string.view) + " " + baseMessage.getReplyCount() + " " + context.getResources().getString(R.string.replies));
            }
            if (baseMessage == null & text != null && !text.isEmpty()) textView.setText(text + "");
            if (separatorColor != 0) separator.setBackgroundColor(separatorColor);
            if (textColor != 0) textView.setTextColor(textColor);
            if (textAppearance != 0) textView.setTextAppearance(context, textAppearance);
            if (iconTint != 0) imageView.setImageTintList(ColorStateList.valueOf(iconTint));
            if (icon != 0) imageView.setImageResource(icon);
            return view;
        }
        return null;
    }
}
