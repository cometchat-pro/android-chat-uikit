package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList;

import android.content.Context;
import android.util.Log;

import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TypingIndicator;

import java.util.List;

import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;

public class CometChatConversationListViewModel {

    private  Context context;

    private CometChatConversationListAdapter conversationListAdapter;

    private CometChatConversationListViewModel(){

    }
    public CometChatConversationListViewModel(Context context, CometChatConversationList cometChatConversationList){
        this.context=context;
        setAdapter(cometChatConversationList);
    }

    private CometChatConversationListAdapter getAdapter(){
       if (conversationListAdapter==null){
           conversationListAdapter=new CometChatConversationListAdapter(context);
       }
       return conversationListAdapter;
    }

    public void add(Conversation conversation){
        if (conversationListAdapter!=null)
            conversationListAdapter.add(conversation);
    }

    private void setAdapter(CometChatConversationList cometChatConversationList){
        if (conversationListAdapter==null)
           conversationListAdapter=new CometChatConversationListAdapter(context);
        cometChatConversationList.getRecyclerView().setAdapter(conversationListAdapter);
    }


    public void setConversationList(List<Conversation> conversationList) {
        if (conversationListAdapter!=null) {
                conversationListAdapter.updateList(conversationList);
        }
        else
        {
            Log.e("ERROR", "setConversationList: ERROR " );
        }
    }


    public void update(Conversation conversation,boolean isActionMessage) {
        if (conversationListAdapter!=null)
            conversationListAdapter.update(conversation,isActionMessage);
    }

    public void remove(Conversation conversation) {
        if (conversationListAdapter!=null)
            conversationListAdapter.remove(conversation);
    }

    public void searchConversation(String searchString) {
        if (conversationListAdapter!=null)
            conversationListAdapter.getFilter().filter(searchString);
    }

    public void setDeliveredReceipts(MessageReceipt messageReceipt) {
        if (conversationListAdapter!=null)
            conversationListAdapter.setDeliveredReceipts(messageReceipt);
    }

    public void setReadReceipts(MessageReceipt messageReceipt) {
        if (conversationListAdapter!=null)
            conversationListAdapter.setReadReceipts(messageReceipt);
    }

    public void clear() {
        if (conversationListAdapter!=null)
            conversationListAdapter.resetAdapterList();
    }

    public Conversation getConversation(int position) {
        Conversation conversation = null;
        if (conversationListAdapter!=null)
            conversation = conversationListAdapter.getItemAtPosition(position);
        return conversation;
    }
    public int size() {
        return conversationListAdapter.getItemCount();
    }


    public void setConversationListItemProperty(boolean hideAvatar, boolean hideUserPresenceListItem,
                                                boolean hideTitleListItem, int titleColorListItem,
                                                boolean hideSubtitleListItem, int subTitleColorListItem,
                                                boolean hideHelperTextListItem, int helperTextColorListItem,
                                                boolean hideTimeListItem, int timeTextColorListItem,
                                                int backgroundColorListItem, float cornerRadiusListItem,
                                                int typingIndicatorColorListItem) {
        if (conversationListAdapter!=null)
            conversationListAdapter.setConversationListItemProperty(hideAvatar,
                    hideUserPresenceListItem,hideTitleListItem,titleColorListItem,
                    hideSubtitleListItem,subTitleColorListItem,hideHelperTextListItem,
                    helperTextColorListItem,hideTimeListItem,timeTextColorListItem,
                    backgroundColorListItem,cornerRadiusListItem,typingIndicatorColorListItem);
    }

    public void setTypingIndicator(TypingIndicator typingIndicator, boolean b) {
        if (conversationListAdapter!=null)
            conversationListAdapter.setTypingIndicator(typingIndicator,b);
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        conversationListAdapter.setConfiguration(configuration);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        conversationListAdapter.setConfiguration(configurations);
    }
}