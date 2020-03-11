package viewmodel;

import android.content.Context;

import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.uikit.CometChatConversationList;

import java.util.List;

import adapter.ConversationListAdapter;

public class ConversationViewModel {

    private  Context context;

    private ConversationListAdapter conversationListAdapter;

    private CometChatConversationList conversationListView;


    private ConversationViewModel(){

    }
    public ConversationViewModel(Context context,CometChatConversationList cometChatConversationList){
        this.conversationListView=cometChatConversationList;
        this.context=context;
        setAdapter();
    }

    private ConversationListAdapter getAdapter(){
       if (conversationListAdapter==null){
           conversationListAdapter=new ConversationListAdapter(context);
       }
       return conversationListAdapter;
    }

    public void add(Conversation conversation){
        if (conversationListAdapter!=null)
            conversationListAdapter.add(conversation);
    }

    private void setAdapter(){
        conversationListAdapter=new ConversationListAdapter(context);
        conversationListView.setAdapter(conversationListAdapter);
    }


    public void setConversationList(List<Conversation> conversationList) {
        if (conversationListAdapter!=null)
            conversationListAdapter.updateList(conversationList);
    }


    public void update(Conversation conversation) {
        if (conversationListAdapter!=null)
            conversationListAdapter.update(conversation);
    }

    public void remove(Conversation conversation) {
        if (conversationListAdapter!=null)
            conversationListAdapter.remove(conversation);
    }
}