package com.cometchatworkspace.components.messages.composer.listener;

import android.widget.ImageView;

import androidx.core.view.inputmethod.InputContentInfoCompat;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;

import com.cometchatworkspace.components.messages.composer.CometChatComposer;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;

public abstract class Events {

    public void onMoreActionClicked(ImageView moreIcon) {}

    public void onPollActionClicked() {}

    public void onCameraActionClicked() {}

    public void onGalleryActionClicked() {}

    public void onAudioActionClicked() {}

    public void onFileActionClicked() {}

    public void onVoiceNoteComplete(String string) {}

    public void onEditTextMediaSelected(InputContentInfoCompat inputContentInfo) {}


    public void onLocationActionClicked() {}
    
    public void onStickerClicked() {}

    public void onWhiteboardClicked() {}

    public void onDocumentClicked() { }

    public void onMessageSent(BaseMessage message, @CometChatComposer.MessageStatus int status) {}

    public void onMessageError(CometChatException e) {

    }
    public void onEditMessageSuccess(BaseMessage message) {}

    public void onEditMessageError(CometChatException e) {}

    public void onViewClosed() {}

    public void onCustomUserAction(CometChatMessageTemplate template){}
}
