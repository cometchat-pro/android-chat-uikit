package com.cometchatworkspace.components.messages.template;

import android.app.Activity;
import android.view.View;

import androidx.annotation.StringDef;
import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.BaseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.CometChatOptions;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.OnOptionClick;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;

public class CometChatMessageTemplate {
    private String id;
    private int icon;
    private String title;
    private String description;
    private String name;
    private int view; //In Progress
    private OnOptionClick<Object> clickListener;
    private BaseMessage baseMessage;
    private List<CometChatOptions> options;
    private String receiverId;
    private String receiverType;
    public AppCompatActivity activity;

    public CometChatMessageTemplate setBaseMessage(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
        return this;
    }

    public BaseMessage getBaseMessage() {
        return baseMessage;
    }


    @StringDef({DefaultList.text, DefaultList.image, DefaultList.audio, DefaultList.video,
            DefaultList.file, DefaultList.location, DefaultList.whiteboard, DefaultList.document,
            DefaultList.sticker, DefaultList.poll, DefaultList.groupAction, DefaultList.groupCall,
            DefaultList.callAction})

    public @interface DefaultList {
        String text = UIKitConstants.MessageTypeConstants.TEXT;
        String image = UIKitConstants.MessageTypeConstants.IMAGE;
        String video = UIKitConstants.MessageTypeConstants.VIDEO;
        String audio = UIKitConstants.MessageTypeConstants.AUDIO;
        String file = UIKitConstants.MessageTypeConstants.FILE;
        String location = UIKitConstants.MessageTypeConstants.EXTENSION_LOCATION;
        String whiteboard = UIKitConstants.MessageTypeConstants.EXTENSION_WHITEBOARD;
        String document = UIKitConstants.MessageTypeConstants.EXTENSION_DOCUMENT;
        String sticker = UIKitConstants.MessageTypeConstants.EXTENSION_STICKER;
        String poll = UIKitConstants.MessageTypeConstants.EXTENSION_POLL;
        String groupAction = CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER;
        String groupCall = "groupCall";
        String callAction = "callAction";
        String meeting = "meeting";
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

    public List<CometChatOptions> getOptions() {
        return options;
    }

    public CometChatMessageTemplate setOptions(HashMap<String, CometChatOptions> options) {
        this.options = new ArrayList<>(options.values());
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

    public CometChatMessageTemplate setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    public AppCompatActivity getActivity() {
        return this.activity;
    }

    public CometChatMessageTemplate setView(int view) {
        this.view = view;
        return this;
    }

    public OnOptionClick getClickListener() {
        return clickListener;
    }

    public CometChatMessageTemplate setActionClick(
            OnOptionClick<Object> clickListener) {
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
