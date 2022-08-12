package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;

public abstract class CometChatActionSheetListener {
    public abstract void onActionItemClick(ActionItem actionItem);
    public void onReactionClick(Reaction reaction) {}
}