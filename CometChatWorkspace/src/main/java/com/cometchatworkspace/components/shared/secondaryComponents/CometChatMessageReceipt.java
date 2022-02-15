package com.cometchatworkspace.components.shared.secondaryComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;

public class CometChatMessageReceipt extends AppCompatImageView {

    private final User loggedInUser = CometChat.getLoggedInUser();
    private Drawable progressIcon,sentIcon,deliveredIcon,errorIcon,readIcon;

    private Context context;

    public CometChatMessageReceipt(Context context) {
        super(context);
        initViewComponent(context,null);
    }

    public CometChatMessageReceipt(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context,attrs);
    }

    public CometChatMessageReceipt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context,attrs);
    }


    private void initViewComponent(Context context,AttributeSet attrs) {
        this.context = context;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatMessageReceipt,
                0, 0);

        progressIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageProgressIcon);
        readIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageReadIcon);
        deliveredIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageDeliveredIcon);
        sentIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageSentIcon);
        errorIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageErrorIcon);
        if (readIcon==null)
            readIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_message_read,null);
        if (deliveredIcon==null)
            deliveredIcon = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_message_delivered, null);
        if (sentIcon==null)
            sentIcon = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_message_sent, null);
        if (progressIcon ==null)
            progressIcon = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_wait, null);
        if (errorIcon==null)
            errorIcon = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_error, null);
    }

    /**
     * This method is used set message time i.e sentAt, deliveredAt & readAt in <b>txtTime</b>.
     * If sender of baseMessage is user then for user side messages it will show readAt, deliveredAt
     * time along with respective icon. For reciever side message it will show only deliveredAt time
     *
     * @param baseMessage is a object of BaseMessage used to identify baseMessage sender.
     * @see BaseMessage
     */
    public void messageObject(BaseMessage baseMessage) {
        if (baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            if (baseMessage.getReceiverType()!=null && baseMessage.getReceiverType()
                    .equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                messageProgressIcon(progressIcon);
                if (baseMessage.getReadAt() != 0) {
                    messageReadIcon(readIcon);
                } else if (baseMessage.getDeliveredAt() != 0) {
                    messageDeliveredIcon(deliveredIcon);
                } else if (baseMessage.getSentAt()>0){
                    messageSentIcon(sentIcon);
                } else if (baseMessage.getSentAt()==-1) {
                    messageErrorIcon(errorIcon);
                }
            } else {
                messageSentIcon(sentIcon);
            }
        } else {
            setVisibility(View.GONE);
        }
        invalidate();
    }

    public void messageProgressIcon(Drawable icon) {
        this.progressIcon = icon;
        if (icon!=null)
            setImageDrawable(icon);
    }

    public void messageReadIcon(Drawable icon) {
        this.readIcon = icon;
        if (icon!=null)
            setImageDrawable(icon);
    }

    public void messageDeliveredIcon(Drawable icon) {
        this.deliveredIcon = icon;
        if (icon!=null)
            setImageDrawable(icon);
    }

    public void messageSentIcon(Drawable icon) {
        this.sentIcon = icon;
        if (icon!=null)
            setImageDrawable(icon);
    }

    public void messageErrorIcon(Drawable icon) {
        this.errorIcon = icon;
        if (icon!=null)
            setImageDrawable(icon);
    }

    public Drawable getDeliveredIcon() {
        return deliveredIcon;
    }

    public Drawable getReadIcon() {
        return readIcon;
    }

    public Drawable getSentIcon() {
        return sentIcon;
    }

    public Drawable getErrorIcon() {
        return errorIcon;
    }

    public Drawable getProgressIcon() {
        return progressIcon;
    }
}
