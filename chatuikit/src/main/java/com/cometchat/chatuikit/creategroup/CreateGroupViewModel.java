package com.cometchat.chatuikit.creategroup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;

enum Status {
    SUCCESS, IN_PROGRESS, ERROR
}

public class CreateGroupViewModel extends ViewModel {
    public MutableLiveData<Status> createSuccess;
    public MutableLiveData<CometChatException> cometChatException;
    public MutableLiveData<Group> createdGroup;

    public CreateGroupViewModel() {
        createSuccess = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        createdGroup = new MutableLiveData<>();
    }

    public MutableLiveData<Status> getCreateSuccess() {
        return createSuccess;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public MutableLiveData<Group> getCreatedGroup() {
        return createdGroup;
    }

    public void createGroup(Group group) {
        createSuccess.setValue(Status.IN_PROGRESS);
        CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                createdGroup.setValue(group);
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.ccGroupCreated(group);
                }
                createSuccess.setValue(Status.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                createSuccess.setValue(Status.ERROR);
                cometChatException.setValue(e);
            }
        });
    }
}
