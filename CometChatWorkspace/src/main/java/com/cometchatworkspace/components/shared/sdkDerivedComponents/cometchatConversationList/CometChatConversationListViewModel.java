package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TypingIndicator;

import java.util.List;

import com.cometchat.pro.models.User;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;

public class CometChatConversationListViewModel {

    private Context context;

    private RecyclerView.LayoutManager layoutManager;

    private CometChatConversationListAdapter conversationListAdapter;

    private CometChatConversationListViewModel() {

    }

    public CometChatConversationListViewModel(Context context, CometChatConversationList cometChatConversationList) {
        this.context = context;
        setAdapter(cometChatConversationList);
    }

    private CometChatConversationListAdapter getAdapter() {
        if (conversationListAdapter == null) {
            conversationListAdapter = new CometChatConversationListAdapter(context,layoutManager);
        }
        return conversationListAdapter;
    }

    public void add(Conversation conversation) {
        if (conversationListAdapter != null)
            conversationListAdapter.add(conversation);
    }

    public void removeGroup(Group group) {
        if (conversationListAdapter != null)
            conversationListAdapter.removeGroup(group);
    }

    public void removeUser(User user) {
        if (conversationListAdapter != null)
            conversationListAdapter.removeUser(user);
    }

    public void updateGroupConversation(Group group) {
        if (conversationListAdapter != null)
            conversationListAdapter.updateGroupConversation(group);
    }

    public void updateUserConversation(User user) {
        if (conversationListAdapter != null && user != null)
            conversationListAdapter.updateUserConversation(user);
    }


    private void setAdapter(CometChatConversationList cometChatConversationList) {
        if (layoutManager==null)
            layoutManager = cometChatConversationList.getRecyclerView().getLayoutManager();
        if (conversationListAdapter == null)
            conversationListAdapter = new CometChatConversationListAdapter(context,layoutManager);
        cometChatConversationList.getRecyclerView().setAdapter(conversationListAdapter);
    }


    public void setConversationList(List<Conversation> conversationList) {
        if (conversationListAdapter != null) {
            conversationListAdapter.updateList(conversationList);
        } else {
        }
    }


    public void update(Conversation conversation, boolean isActionMessage) {
        if (conversationListAdapter != null)
            conversationListAdapter.update(conversation, isActionMessage);
    }
    public void updateConversationForSentMessages(BaseMessage baseMessage) {
        if (conversationListAdapter != null)
            conversationListAdapter.updateConversationForSentMessages(baseMessage);
    }


    public void remove(Conversation conversation) {
        if (conversationListAdapter != null)
            conversationListAdapter.remove(conversation);
    }

    public void searchConversation(String searchString) {
        if (conversationListAdapter != null)
            conversationListAdapter.getFilter().filter(searchString);
    }

    public void searchConversations(List<Conversation> conversations) {
        if (conversationListAdapter != null)
            conversationListAdapter.filterList(conversations);
    }

    public void setDeliveredReceipts(MessageReceipt messageReceipt) {
        if (conversationListAdapter != null)
            conversationListAdapter.setDeliveredReceipts(messageReceipt);
    }

    public void setReadReceipts(MessageReceipt messageReceipt) {
        if (conversationListAdapter != null)
            conversationListAdapter.setReadReceipts(messageReceipt);
    }

    public void clear() {
        if (conversationListAdapter != null)
            conversationListAdapter.resetAdapterList();
    }

    public Conversation getConversation(int position) {
        Conversation conversation = null;
        if (conversationListAdapter != null)
            conversation = conversationListAdapter.getItemAtPosition(position);
        return conversation;
    }

    public int size() {
        return conversationListAdapter.getItemCount();
    }


    public void setTypingIndicator(TypingIndicator typingIndicator, boolean b) {
        if (conversationListAdapter != null)
            conversationListAdapter.setTypingIndicator(typingIndicator, b);
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        conversationListAdapter.setConfiguration(configuration);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        conversationListAdapter.setConfiguration(configurations);
    }
}