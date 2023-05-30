package com.cometchat.chatuikit.shared.views.cometchatActionSheet;

import com.cometchat.chatuikit.shared.models.Reaction;

/**
 * An abstract class that defines methods for handling action item clicks and reaction clicks in CometChat action sheet.
 */
public abstract class CometChatActionSheetListener {
    /**
     * Method called when an action item is clicked in the action sheet.
     *
     * @param actionItem The clicked action item object.
     */
    public abstract void onActionItemClick(ActionItem actionItem);

    /**
     * Method called when a reaction is clicked in the action sheet.
     *
     * @param reaction The clicked reaction object.
     */
    public void onReactionClick(Reaction reaction) {
    }
}