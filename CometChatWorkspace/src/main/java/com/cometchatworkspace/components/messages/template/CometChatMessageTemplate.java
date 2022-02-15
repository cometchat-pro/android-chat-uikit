package com.cometchatworkspace.components.messages.template;

import android.view.View;

import androidx.annotation.StringDef;
import androidx.databinding.ViewDataBinding;

import com.cometchat.pro.models.BaseMessage;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;

public class CometChatMessageTemplate {
    private String id;
    private int icon;
    private String title;
    private String description;
    private String name;
    private int view; //In Progress
    private OnItemClickListener clickListener;
    private BaseMessage baseMessage;
    private List<ActionItem> options;
    private String receiverId;
    private String receiverType;

    public CometChatMessageTemplate setBaseMessage(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
        return this;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }


    @StringDef({DefaultList.text,DefaultList.image,DefaultList.audio,DefaultList.video,
            DefaultList.file,DefaultList.location,DefaultList.whiteboard,DefaultList.document,
            DefaultList.sticker,DefaultList.poll,DefaultList.groupAction,DefaultList.groupCall,
            DefaultList.callAction})

    public @interface DefaultList {
        String text  = "text";
        String image = "image";
        String video = "video";
        String audio = "audio";
        String file = "file";
        String location = "location";
        String whiteboard = "extension_whiteboard";
        String document = "extension_document";
        String sticker = "extension_sticker";
        String poll = "extension_poll";
        String groupAction = "groupAction";
        String groupCall = "groupCall";
        String callAction = "callAction";
    }

    /**
     * Default Constructor
     */
    public CometChatMessageTemplate() {
    }

    public String getId() {
        return id;
    }

    public CometChatMessageTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public CometChatMessageTemplate setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public String getName() {
        return name;
    }

    public CometChatMessageTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public List<ActionItem> getOptions() {
        return options;
    }

    public CometChatMessageTemplate setOptions(List<ActionItem> options) {
        this.options = options;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CometChatMessageTemplate setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getView() {
        return view;
    }

    public CometChatMessageTemplate setView(int view) {
        this.view = view;
        return this;
    }

    public OnItemClickListener getClickListener() {
        return clickListener;
    }

    public CometChatMessageTemplate setActionCallBack(
            OnItemClickListener<CometChatMessageTemplate> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }
}
