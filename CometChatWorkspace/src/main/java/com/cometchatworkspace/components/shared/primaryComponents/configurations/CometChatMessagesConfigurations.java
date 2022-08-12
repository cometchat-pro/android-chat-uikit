package com.cometchatworkspace.components.shared.primaryComponents.configurations;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageInputData;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;


public class CometChatMessagesConfigurations extends CometChatConfigurations {
    private static List<CometChatMessageTemplate> cometChatMessageTemplates = new ArrayList<>();
    private boolean isDeleteMessageHidden,isUserChatEnabled,isGroupChatEnabled;
    private Drawable backgroundDrawable;
    private int backgroundColor;
    /**
     * Below are boolean value of Compose Actions which are used to hide/show each actions.
     */
    private AvatarConfiguration avatarConfig;

    public CometChatMessagesConfigurations setMessageFilter(List<CometChatMessageTemplate> listOfMessageTemplate) {
        cometChatMessageTemplates = listOfMessageTemplate;
        return this;
    }

    public static List<CometChatMessageTemplate> getMessageFilterList() {
        return cometChatMessageTemplates;
    }

    public static CometChatMessageTemplate getMessageTemplateById(String id) {
        for (CometChatMessageTemplate cometChatMessageTemplate : cometChatMessageTemplates) {
            if (cometChatMessageTemplate.getId() != null){
                if (cometChatMessageTemplate.getId().equalsIgnoreCase(id)) {
                    return cometChatMessageTemplate;
                }
             }
        }
        return null;
    }

    public CometChatMessagesConfigurations hideDeletedMessage(boolean isHidden) {
        isDeleteMessageHidden= isHidden;
        return this;
    }

    public boolean isDeleteMessageHidden() {
        return isDeleteMessageHidden;
    }

    public CometChatMessagesConfigurations setUserChatEnabled(boolean isEnabled){
        isUserChatEnabled= isEnabled;
        return this;
    }

    public boolean isUserChatEnabled() {
        return isUserChatEnabled;
    }

    public CometChatMessagesConfigurations setGroupChatEnabled(boolean isEnabled) {
        isGroupChatEnabled = isEnabled;
        return this;
    }
    public boolean isGroupChatEnabled() {
        return isGroupChatEnabled;
    }

    public CometChatMessagesConfigurations background(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public CometChatMessagesConfigurations background(int[] colors, GradientDrawable.Orientation orientation) {
        this.backgroundDrawable = new GradientDrawable(orientation,colors);
        return this;
    }
    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }
}
