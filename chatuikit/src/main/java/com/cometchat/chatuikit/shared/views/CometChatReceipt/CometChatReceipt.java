package com.cometchat.chatuikit.shared.views.CometChatReceipt;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.cometchat.chatuikit.R;

/**
 * CometChatReceipt is a custom ImageView component that represents the receipt status of a message.
 * It provides methods to set different receipt icons based on the message status.
 */
public class CometChatReceipt extends AppCompatImageView {

    private Drawable waitIcon, sentIcon, deliveredIcon, errorIcon, readIcon;

    private Context context;

    /**
     * Constructor for creating a CometChatReceipt without any attribute.
     *
     * @param context The context in which the CometChatReceipt is created.
     */
    public CometChatReceipt(Context context) {
        super(context);
        initViewComponent(context, null);
    }

    /**
     * Constructor for creating a CometChatReceipt with attributes.
     *
     * @param context The context in which the CometChatReceipt is created.
     * @param attrs   The attribute set containing the custom attributes.
     */
    public CometChatReceipt(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs);
    }

    /**
     * Constructor for creating a CometChatReceipt with attributes and a default style.
     *
     * @param context      The context in which the CometChatReceipt is created.
     * @param attrs        The attribute set containing the custom attributes.
     * @param defStyleAttr The default style resource.
     */
    public CometChatReceipt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs);
    }

    /**
     * Initializes the view components and retrieves custom attributes from the attribute set.
     *
     * @param context The context in which the CometChatReceipt is created.
     * @param attrs   The attribute set containing the custom attributes.
     */
    private void initViewComponent(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatMessageReceipt,
                0, 0);

        waitIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageProgressIcon);
        readIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageReadIcon);
        deliveredIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageDeliveredIcon);
        sentIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageSentIcon);
        errorIcon = a.getDrawable(R.styleable.CometChatMessageReceipt_messageErrorIcon);
        if (readIcon == null)
            readIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_message_read, null);
        if (deliveredIcon == null)
            deliveredIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_message_delivered, null);
        if (sentIcon == null)
            sentIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_message_sent, null);
        if (waitIcon == null)
            waitIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_wait, null);
        if (errorIcon == null)
            errorIcon = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_error, null);
    }

    /**
     * Sets the receipt icon based on the receipt status.
     *
     * @param receipt The receipt status.
     */
    public void setReceipt(Receipt receipt) {
        if (Receipt.SENT.equals(receipt))
            setReceiptIcon(sentIcon);
        else if (Receipt.IN_PROGRESS.equals(receipt))
            setReceiptIcon(waitIcon);
        else if (Receipt.ERROR.equals(receipt))
            setReceiptIcon(errorIcon);
        else if (Receipt.READ.equals(receipt))
            setReceiptIcon(readIcon);
        else if (Receipt.DELIVERED.equals(receipt))
            setReceiptIcon(deliveredIcon);
    }

    /**
     * Sets the receipt icon to the specified drawable.
     *
     * @param drawable The drawable to set as the receipt icon.
     */
    private void setReceiptIcon(Drawable drawable) {
        if (drawable != null)
            setImageDrawable(drawable);
    }

    /**
     * Sets the wait icon drawable for the receipt.
     *
     * @param icon The drawable for the wait icon.
     */
    public void setWaitIcon(Drawable icon) {
        if (icon != null) {
            this.waitIcon = icon;
            setImageDrawable(icon);
        }
    }

    /**
     * Sets the read icon drawable for the receipt.
     *
     * @param icon The drawable for the read icon.
     */
    public void setReadIcon(Drawable icon) {
        if (icon != null) {
            this.readIcon = icon;
            setImageDrawable(icon);
        }
    }

    /**
     * Sets the delivered icon drawable for the receipt.
     *
     * @param icon The drawable for the delivered icon.
     */
    public void setDeliveredIcon(Drawable icon) {
        if (icon != null) {
            this.deliveredIcon = icon;
            setImageDrawable(icon);
        }
    }

    /**
     * Sets the sent icon drawable for the receipt.
     *
     * @param icon The drawable for the sent icon.
     */
    public void setSentIcon(Drawable icon) {
        if (icon != null) {
            this.sentIcon = icon;
            setImageDrawable(icon);
        }
    }

    /**
     * Sets the error icon drawable for the receipt.
     *
     * @param icon The drawable for the error icon.
     */
    public void setErrorIcon(Drawable icon) {
        if (icon != null) {
            this.errorIcon = icon;
            setImageDrawable(icon);
        }
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

    public Drawable getWaitIcon() {
        return waitIcon;
    }
}
