package com.cometchat.chatuikit.shared.views.CometChatMessageBubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.google.android.material.card.MaterialCardView;

/**
 * The CometChatMessageBubble class is a custom view that represents a message bubble used in a chat interface. It extends the MaterialCardView class, providing additional customization and functionality.
 * <p>
 * The message bubble can be aligned to the left, right, or center of the screen using the setMessageAlignment method. It supports various views such as leading view, header view, reply view, content view, bottom view, thread view, and footer view. These views can be set using the corresponding setter methods (setLeadingView, setHeaderView, etc.) to customize the appearance and content of the message bubble.
 * <p>
 * The style of the message bubble can be customized using the setStyle method, which takes a MessageBubbleStyle object as a parameter. The style allows customization of background color, border width, corner radius, and border color.
 * <p>
 * Overall, the CometChatMessageBubble class provides a flexible and customizable message bubble component for chat interfaces.
 */
public class CometChatMessageBubble extends MaterialCardView {
    private View view;
    private Context context;
    private UIKitConstants.MessageBubbleAlignment alignment = UIKitConstants.MessageBubbleAlignment.RIGHT;
    private LinearLayout parent, leadingView, headerView, replyView, contentView, bottomView, threadView, footerView;
    private MaterialCardView messageBubble;
    private View customLeadingView, customHeaderView, customReplyView, customContentView, customBottomView, customThreadView, customFooterView;
    private MessageBubbleStyle messageBubbleStyle;

    /**
     * Constructs a new CometChatMessageBubble with the provided context.
     *
     * @param context The context in which the view is created.
     */
    public CometChatMessageBubble(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new CometChatMessageBubble with the provided context and attribute set.
     *
     * @param context The context in which the view is created.
     * @param attrs   The attribute set to apply to the view.
     */
    public CometChatMessageBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new CometChatMessageBubble with the provided context, attribute set, and default style.
     *
     * @param context      The context in which the view is created.
     * @param attrs        The attribute set to apply to the view.
     * @param defStyleAttr The default style resource ID to apply to the view.
     */
    public CometChatMessageBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        if (UIKitConstants.MessageBubbleAlignment.LEFT.equals(alignment))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_left, null);
        else if (UIKitConstants.MessageBubbleAlignment.RIGHT.equals(alignment))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_right, null);
        else if (UIKitConstants.MessageBubbleAlignment.CENTER.equals(alignment))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_center, null);
        else view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_left, null);
        initView(view);
    }

    /**
     * Sets the alignment of the message bubble.
     *
     * @param mAlignment The alignment of the message bubble.
     * @see UIKitConstants.MessageBubbleAlignment
     */
    public void setMessageAlignment(UIKitConstants.MessageBubbleAlignment mAlignment) {
        if (UIKitConstants.MessageBubbleAlignment.LEFT.equals(mAlignment)) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_left, null);
            alignment = mAlignment;
        } else if (UIKitConstants.MessageBubbleAlignment.RIGHT.equals(mAlignment)) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_right, null);
            alignment = mAlignment;
        } else if (UIKitConstants.MessageBubbleAlignment.CENTER.equals(mAlignment)) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_bubble_center, null);
            alignment = mAlignment;
        }
        removeAllViewsInLayout();
        initView(view);
    }

    private void initView(View view) {
        parent = view.findViewById(R.id.parent);
        messageBubble = view.findViewById(R.id.message_bubble);
        messageBubble.setCardBackgroundColor(Color.TRANSPARENT);
        messageBubble.setCardElevation(0);
        messageBubble.setRadius(0);
        leadingView = view.findViewById(R.id.leading_view);
        headerView = view.findViewById(R.id.header_view);
        replyView = view.findViewById(R.id.reply_bubble);
        contentView = view.findViewById(R.id.content_view);
        bottomView = view.findViewById(R.id.bottom_view);
        threadView = view.findViewById(R.id.view_replies);
        footerView = view.findViewById(R.id.footer_view);
        setLeadingView(customLeadingView);
        setHeaderView(customHeaderView);
        setReplyView(customReplyView);
        setContentView(customContentView);
        setThreadView(customThreadView);
        setFooterView(customFooterView);
        setStyle(messageBubbleStyle);
        addView(view);
    }

    /**
     * Sets a custom view as the leading view.
     *
     * @param view The custom leading view to set.
     */
    public void setLeadingView(View view) {
        this.customLeadingView = view;
        handleView(leadingView, view);
    }

    /**
     * Sets a custom view as the header view.
     *
     * @param view The custom header view to set.
     */
    public void setHeaderView(View view) {
        this.customHeaderView = view;
        handleView(headerView, view);
    }

    /**
     * Sets a custom view as the reply view.
     *
     * @param view The custom reply view to set.
     */
    public void setReplyView(View view) {
        this.customReplyView = view;
        handleView(replyView, view);
    }

    /**
     * Sets a custom view as the content view.
     *
     * @param view The custom content view to set.
     */
    public void setContentView(View view) {
        this.customContentView = view;
        handleView(contentView, view);
    }

    /**
     * Sets a custom view as the bottom view.
     *
     * @param view The custom bottom view to set.
     */
    public void setBottomView(View view) {
        this.customBottomView = view;
        handleView(bottomView, view);
    }

    /**
     * Sets a custom view as the thread view.
     *
     * @param view The custom thread view to set.
     */
    public void setThreadView(View view) {
        this.customThreadView = view;
        handleView(threadView, view);
    }

    /**
     * Sets a custom view as the footer view.
     *
     * @param view The custom footer view to set.
     */
    public void setFooterView(View view) {
        this.customFooterView = view;
        handleView(footerView, view);
    }

    /**
     * Sets the style for the message bubble.
     *
     * @param style The style to apply to the message bubble.
     * @see MessageBubbleStyle
     */
    public void setStyle(MessageBubbleStyle style) {
        if (style != null) {
            messageBubbleStyle = style;
            if (style.getDrawableBackground() != null)
                messageBubble.setBackgroundDrawable(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                messageBubble.setCardBackgroundColor(ColorStateList.valueOf(style.getBackground()));
            if (style.getBorderWidth() >= 0) messageBubble.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) messageBubble.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) messageBubble.setStrokeColor(style.getBorderColor());
        }
    }

    private void handleView(LinearLayout layout, View view) {
        if (view != null) {
            layout.removeAllViews();
            removeParentFromView(view);
            layout.setVisibility(VISIBLE);
            layout.addView(view);
        } else layout.setVisibility(GONE);
    }

    private void removeParentFromView(View view) {
        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
    }

    public LinearLayout getView() {
        return parent;
    }

    public LinearLayout getLeadingView() {
        return leadingView;
    }

    public LinearLayout getHeaderView() {
        return headerView;
    }

    public LinearLayout getReplyView() {
        return replyView;
    }

    public LinearLayout getContentView() {
        return contentView;
    }

    public LinearLayout getThreadView() {
        return threadView;
    }

    public LinearLayout getFooterView() {
        return footerView;
    }

}
