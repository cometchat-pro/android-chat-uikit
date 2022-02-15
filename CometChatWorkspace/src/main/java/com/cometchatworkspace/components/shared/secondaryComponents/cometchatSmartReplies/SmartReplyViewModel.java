package com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies;

import android.content.Context;

import java.util.List;

public class SmartReplyViewModel {

    private static final String TAG = "SmartReplyViewModel";

    private final Context context;

    private SmartRepliesAdapter smartReplyListAdapter;

    private final CometChatSmartReplies smartReplyList;

    public SmartReplyViewModel(Context context, CometChatSmartReplies smartReplyList) {
        this.context = context;
        this.smartReplyList = smartReplyList;
         setSmartReplyAdapter(smartReplyList);
    }

    private void setSmartReplyAdapter(CometChatSmartReplies smartReplyList) {
        smartReplyListAdapter=new SmartRepliesAdapter(context);
        smartReplyList.setAdapter(smartReplyListAdapter);
    }

    private SmartRepliesAdapter getAdapter(){
        if (smartReplyListAdapter==null){
            smartReplyListAdapter=new SmartRepliesAdapter(context);
        }
        return smartReplyListAdapter;
    }

    public void setSmartReplyList(List<String> replyList){
        getAdapter().updateList(replyList);
    }


}
