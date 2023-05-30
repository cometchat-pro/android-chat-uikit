package com.cometchat.chatuikit.transferownership;

import androidx.lifecycle.MutableLiveData;

import com.cometchat.chatuikit.shared.events.CometChatGroupEvents;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.chatuikit.groupmembers.GroupMembersViewModel;

enum TransferOwnershipStates {
    ERROR, SUCCESS, SHOW_PROGRESS,
}

public class TransferOwnershipViewModel extends GroupMembersViewModel {
    public MutableLiveData<TransferOwnershipStates> transferOwnershipStatesMutableLiveData;
    public MutableLiveData<CometChatException> exceptionMutableLiveData;
    private Group group;

    public TransferOwnershipViewModel() {
        transferOwnershipStatesMutableLiveData = new MutableLiveData<>();
        exceptionMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<TransferOwnershipStates> transferOwnershipStatesMutableLiveData() {
        return transferOwnershipStatesMutableLiveData;
    }

    public MutableLiveData<CometChatException> getExceptionMutableLiveData() {
        return exceptionMutableLiveData;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void transferOwnership(GroupMember groupMember) {
        if (group != null && groupMember != null) {
            transferOwnershipStatesMutableLiveData.setValue(TransferOwnershipStates.SHOW_PROGRESS);
            CometChat.transferGroupOwnership(group.getGuid(), groupMember.getUid(), new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
//                    Action action=Utils.getGroupActionMessage(groupMember,group,group,group.getGuid());
//                    action.setNewScope(UIKitConstants.GroupMemberScope.ADMIN);
//                    action.setAction(CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED);
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                            group.setOwner(groupMember.getUid());
                            e.ccOwnershipChanged(group, groupMember);
                    }
                    transferOwnershipStatesMutableLiveData.setValue(TransferOwnershipStates.SUCCESS);
                }

                @Override
                public void onError(CometChatException e) {
                    transferOwnershipStatesMutableLiveData.setValue(TransferOwnershipStates.ERROR);
                    exceptionMutableLiveData.setValue(e);
                }
            });
        }
    }

}
