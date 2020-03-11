package screen;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.ConversationListAdapter;
import listeners.ClickListener;
import listeners.OnItemClickListener;
import listeners.RecyclerTouchListener;
import utils.FontUtils;
import utils.Utils;

/*

* Purpose - CometChatConversationList class is a fragment used to display list of conversations and perform certain action on click of item.
            It also provide search bar to perform search operation on the list of conversations.User can search by username, groupname, last message of conversation.

* Created on - 20th December 2019

* Modified on  - 16th January 2020

*/

public class CometChatConversationListScreen extends Fragment {

    private RecyclerView rvConversationList;    //Uses to display list of conversations.

    private ConversationListAdapter conversationListAdapter;

    private ConversationsRequest conversationsRequest;    //Uses to fetch Conversations.

    private static OnItemClickListener events;

    private EditText searchEdit;    //Uses to perform search operations.

    private TextView tvTitle;

    private ShimmerFrameLayout conversationShimmer;

    private RelativeLayout rlSearchBox;

    private static final String TAG = "ConversationList";

    private View view;

    private List<Conversation> conversationList = new ArrayList<>();

    public CometChatConversationListScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.conversation_screen, container, false);

        rvConversationList = view.findViewById(R.id.rv_conversation_list);

        searchEdit = view.findViewById(R.id.search_bar);

        tvTitle = view.findViewById(R.id.tv_title);

        tvTitle.setTypeface(FontUtils.getInstance(getActivity()).getTypeFace(FontUtils.robotoMedium));

        rlSearchBox = view.findViewById(R.id.rl_search_box);

        conversationShimmer = view.findViewById(R.id.shimmer_layout);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    // if searchEdit is empty then fetch all conversations.
                    conversationsRequest = null;

                    if (conversationListAdapter!=null)
                        conversationListAdapter.resetAdapterList();

                    makeConversationList();
                } else {
                    // Search conversation based on text in searchEdit field.
                   if (conversationListAdapter!=null)
                       conversationListAdapter.getFilter().filter(editable.toString());
                }
            }
        });

        searchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (conversationListAdapter!=null)
                    conversationListAdapter.getFilter().filter(textView.getText().toString());
                return true;
            }
            return false;
        });


        // Uses to fetch next list of conversations if rvConversationList (RecyclerView) is scrolled in upward direction.
        rvConversationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    makeConversationList();
                }

            }
        });

        // Used to trigger event on click of conversation item in rvConversationList (RecyclerView)
        rvConversationList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvConversationList, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {

                Conversation conversation = (Conversation) var1.getTag(R.string.conversation);

                RelativeLayout vw = var1.findViewById(R.id.conversationView);
                vw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (events != null)
                            events.OnItemClick(conversation, var2);
                    }
                });
            }

            @Override
            public void onLongClick(View var1, int var2) {
                Conversation conversation = (Conversation) var1.getTag(R.string.conversation);
                if (events != null)
                    events.OnItemLongClick(conversation, var2);
            }
        }));

        return view;
    }

    /**
     * This method is used to retrieve list of conversations you have done.
     * For more detail please visit our official documentation {@link "https://prodocs.cometchat.com/docs/android-messaging-retrieve-conversations" }
     *
     * @see ConversationsRequest
     */
    private void makeConversationList() {

        if (conversationsRequest == null) {
            conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().setLimit(50).build();
        }
        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {

                stopHideShimmer();
                setAdapter(conversations);
            }

            @Override
            public void onError(CometChatException e) {
                stopHideShimmer();
                if (getActivity()!=null)
                    Toast.makeText(getActivity(),"Unable to load conversations",Toast.LENGTH_LONG).show();
                Log.d(TAG, "onError: "+e.getMessage());
            }
        });
    }

    private void stopHideShimmer() {
        conversationShimmer.stopShimmer();
        conversationShimmer.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        rlSearchBox.setVisibility(View.VISIBLE);
    }



    /**
     * It sets the adapter for rvConversationList.
     *
     * @param conversations
     */
    private void setAdapter(List<Conversation> conversations) {
        if (conversationListAdapter == null) {
            conversationList = conversations;
            conversationListAdapter = new ConversationListAdapter(getContext(), conversations);
            rvConversationList.setAdapter(conversationListAdapter);
        } else {
            conversationListAdapter.updateList(conversations);
        }
    }

    /**
     * @param onItemClickListener An object of <code>OnItemClickListener&lt;T&gt;</code> abstract class helps to initialize with events
     *                            to perform onItemClick & onItemLongClick.
     * @see OnItemClickListener
     */
    public static void setItemClickListener(OnItemClickListener<Conversation> onItemClickListener) {
        events = onItemClickListener;
    }


    /**
     * This method is used to refresh conversation list if any new conversation is initiated or updated.
     * It converts the message recieved from message listener using <code>CometChatHelper.getConversationFromMessage(message)</code>
     *
     * @param message
     * @see CometChatHelper#getConversationFromMessage(BaseMessage)
     * @see Conversation
     */
    private void refreshConversation(BaseMessage message) {
        if (conversationListAdapter != null) {
            Conversation newConversation = CometChatHelper.getConversationFromMessage(message);
            conversationListAdapter.update(newConversation);
        }
    }

    /**
     * This method has message listener which recieve real time message and based on these messages, conversations are updated.
     *
     * @see CometChat#addMessageListener(String, CometChat.MessageListener)
     */
    private void addConversationListener() {
        CometChat.addMessageListener(TAG, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage message) {
                refreshConversation(message);
            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                refreshConversation(message);
            }

            @Override
            public void onCustomMessageReceived(CustomMessage message) {
                refreshConversation(message);
            }

            @Override
            public void onMessagesDelivered(MessageReceipt messageReceipt) {
                if (conversationListAdapter!=null&&messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER))
                    conversationListAdapter.setDelivered(messageReceipt);
            }

            @Override
            public void onMessagesRead(MessageReceipt messageReceipt) {
                if (conversationListAdapter!=null&&messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER))
                    conversationListAdapter.setReadReceipts(messageReceipt);
            }

            @Override
            public void onMessageEdited(BaseMessage message) {
                refreshConversation(message);
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                refreshConversation(message);
            }
        });
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                Log.e(TAG, "onGroupMemberKicked: "+kickedUser);
                if (kickedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    removeConversation(action);
                }
                else {
                    updateConversation(action);
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                Log.e(TAG, "onMemberAddedToGroup: " );
                updateConversation(action);
            }

            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                Log.e(TAG, "onGroupMemberJoined: " );
                updateConversation(action);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                Log.e(TAG, "onGroupMemberLeft: " );
                if (leftUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    removeConversation(action);
                }
                else {
                   updateConversation(action);
                }
            }
        });
    }

    private void updateConversation(BaseMessage baseMessage) {
        if (conversationListAdapter != null) {
            Conversation conversation = CometChatHelper.getConversationFromMessage(baseMessage);
            conversationListAdapter.update(conversation);
        }
    }

    private void removeConversation(BaseMessage baseMessage) {
        if (conversationListAdapter != null) {
            Conversation conversation = CometChatHelper.getConversationFromMessage(baseMessage);
            conversationListAdapter.remove(conversation);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        conversationsRequest = null;
        if (conversationListAdapter != null) {
            conversationListAdapter.resetAdapterList();
        }
        else {
            setAdapter(conversationList);
        }
        makeConversationList();
        addConversationListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        removeConversationListener();
    }

    private void removeConversationListener() {
        CometChat.removeMessageListener(TAG);
        CometChat.removeGroupListener(TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

    }

}
