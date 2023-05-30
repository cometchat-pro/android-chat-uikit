package com.cometchat.chatuikit.shared.models;

import android.content.Context;
import android.view.View;

import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * The CometChatMessageTemplate class represents a template for creating customized message views in a chat application.
 * <br>
 * It provides a flexible way to define and customize various components of a message bubble view, such as the message bubble,
 * header, content, bottom, and footer views, as well as options associated with the message.
 * <br>
 * By using this template, developers can create rich and dynamic chat message views that align with their application's design
 * and functionality requirements.
 * <br>
 * example :
 * <pre>
 * {@code
 * CometChatMessageTemplate template = new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_MESSAGE).setType(CometChatConstants.MESSAGE_TYPE_TEXT).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getTextMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
 *             if (baseMessage.getDeletedAt() == 0) {
 *                 if (UIKitConstants.MessageBubbleAlignment.LEFT.equals(isLeftAlign)) {
 *                     return ChatConfigurator.getUtils().getTextBubbleContentView(context, (TextMessage) baseMessage, null, Gravity.START, isLeftAlign);
 *                 } else {
 *                     return ChatConfigurator.getUtils().getTextBubbleContentView(context, (TextMessage) baseMessage, null, Gravity.END, isLeftAlign);
 *                 }
 *             } else return ChatConfigurator.getUtils().getDeleteMessageBubble(context, isLeftAlign);
 *         });
 *}
 * </pre>
 */
public class CometChatMessageTemplate {
    private String category;
    private String type;
    private Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> bubbleView;
    private Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> headerView;
    private Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> contentView;
    private Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> bottomView;
    private Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> footerView;
    private Function3<Context, BaseMessage, Group, List<CometChatMessageOption>> options;

    /**
     * Sets the category of the message.
     *
     * @param category the category of the message
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * Sets the type of the message.
     *
     * @param type the type of the message
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the functional interface to create the message bubble view.
     *
     * @param bubbleView the functional interface to create the message bubble view
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setBubbleView(Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> bubbleView) {
        this.bubbleView = bubbleView;
        return this;
    }

    /**
     * Sets the functional interface to create the header view.
     *
     * @param headerView the functional interface to create the header view
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setHeaderView(Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> headerView) {
        this.headerView = headerView;
        return this;
    }

    /**
     * Sets the functional interface to create the content view.
     *
     * @param contentView the functional interface to create the content view
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setContentView(Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> contentView) {
        this.contentView = contentView;
        return this;
    }

    /**
     * Sets the functional interface to create the bottom view.
     *
     * @param bottomView the functional interface to create the bottom view
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setBottomView(Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> bottomView) {
        this.bottomView = bottomView;
        return this;
    }

    /**
     * Sets the functional interface to create the footer view.
     *
     * @param footerView the functional interface to create the footer view
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setFooterView(Function3<Context, BaseMessage, UIKitConstants.MessageBubbleAlignment, View> footerView) {
        this.footerView = footerView;
        return this;
    }

    /**
     * Sets the functional interface to retrieve the options associated with the message.
     *
     * @param options the functional interface to retrieve the options
     * @return the current instance of CometChatMessageTemplate
     */
    public CometChatMessageTemplate setOptions(Function3<Context, BaseMessage, Group, List<CometChatMessageOption>> options) {
        this.options = options;
        return this;
    }

    /**
     * Retrieves the category of the message.
     *
     * @return the category of the message
     */
    public String getCategory() {
        return category;
    }

    /**
     * Retrieves the type of the message.
     *
     * @return the type of the message
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the message bubble view.
     *
     * @param context   the context of the application
     * @param baseMessage the base message for which the bubble view is created
     * @param alignment the alignment of the message bubble
     * @return the message bubble view
     */
    public View getBubbleView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        View view = null;
        if (bubbleView != null) {
            view = bubbleView.apply(context, baseMessage, alignment);
        }
        return view;
    }

    /**
     * Retrieves the header view.
     *
     * @param context   the context of the application
     * @param baseMessage the base message for which the header view is created
     * @param alignment the alignment of the header view
     * @return the header view
     */
    public View getHeaderView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        View view = null;
        if (headerView != null) {
            view = headerView.apply(context, baseMessage, alignment);
        }
        return view;
    }

    /**
     * Retrieves the content view.
     *
     * @param context   the context of the application
     * @param baseMessage the base message for which the content view is created
     * @param alignment the alignment of the content view
     * @return the content view
     */
    public View getContentView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        View view = null;
        if (contentView != null) {
            view = contentView.apply(context, baseMessage, alignment);
        }
        return view;
    }

    /**
     * Retrieves the bottom view.
     *
     * @param context   the context of the application
     * @param baseMessage the base message for which the bottom view is created
     * @param alignment the alignment of the bottom view
     * @return the bottom view
     */
    public View getBottomView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        View view = null;
        if (bottomView != null) {
            view = bottomView.apply(context, baseMessage, alignment);
        }
        return view;
    }

    /**
     * Retrieves the footer view.
     *
     * @param context   the context of the application
     * @param baseMessage the base message for which the footer view is created
     * @param alignment the alignment of the footer view
     * @return the footer view
     */
    public View getFooterView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        View view = null;
        if (footerView != null) {
            view = footerView.apply(context, baseMessage, alignment);
        }
        return view;
    }

    /**
     * Retrieves the options associated with the message.
     *
     * @param context   the context of the application
     * @param baseMessage the base message for which the options are retrieved
     * @param group     the group associated with the message
     * @return the list of options associated with the message
     */
    public List<CometChatMessageOption> getOptions(Context context, BaseMessage baseMessage, Group group) {
        List<CometChatMessageOption> optionList = new ArrayList<>();
        if (options != null) {
            optionList = options.apply(context, baseMessage, group);
        }
        return optionList;
    }
}
