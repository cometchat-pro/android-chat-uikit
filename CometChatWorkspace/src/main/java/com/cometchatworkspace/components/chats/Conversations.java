package com.cometchatworkspace.components.chats;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.messages.template.TemplateUtils;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ComposerConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ConversationListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ConversationListItemConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.HeaderConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList.ConversationInputData;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.onItemClick;
import com.cometchatworkspace.resources.constants.UIKitConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**

* Purpose - CometChatConversationList class is a fragment used to display list of conversations and perform certain action on click of item.
            It also provide search bar to perform search operation on the list of conversations.User can search by username, groupname, last message of conversation.

* Created on - 20th December 2019

* Modified on  - 23rd March 2020

**/

public class Conversations extends Fragment {

    String TAG = "CometChatConversationList";

    View view;

    public Conversations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(
                 R.layout.fragment_cometchat_conversationlist,
                 container, false);

         CometChatConversationsWithMessages conversationsWithMessages =
                 view.findViewById(R.id.conversation);
//
//         int[] colorArray = new int[] {
//                 getContext().getColor(R.color.purple),
//                 getContext().getColor(R.color.dark_blue)
//         };

//         Palette palette = Palette.getInstance(getContext());
//         palette.gradientBackground(colorArray,
//                 GradientDrawable.Orientation.LEFT_RIGHT);

//         CometChatMessagesConfigurations messagesConfigurations =
//                 new CometChatMessagesConfigurations();
//         messagesConfigurations.background(colorArray,
//                 GradientDrawable.Orientation.LEFT_RIGHT);
//         conversationsWithMessages.setConfiguration(messagesConfigurations);

//        CometChatMessageTemplate giphy = new CometChatMessageTemplate()
//                .setId("Giphy")
//                .setName("Giphy")
//                .setIcon(R.drawable.ic_hand)
//                .setActionClick(new onItemClick<Object>() {
//                    @Override
//                    public void onClick(Object appEntity, Context context) {
//                        Toast.makeText(context,"Giphy",Toast.LENGTH_SHORT).show();
//                    }
//                });
//        CometChatMessageTemplate giphy1 = new CometChatMessageTemplate()
//                .setId("jv")
//                .setName("Giphy1")
//                .setIcon(R.drawable.ic_hand)
//                .setActionClick(new onItemClick<Object>() {
//                    @Override
//                    public void onClick(Object appEntity, Context context) {
//                        Toast.makeText(context,"Giphy1",Toast.LENGTH_SHORT).show();
//                    }
//                });
////
//        List<CometChatMessageTemplate>ll=TemplateUtils.getDefaultList(getContext());
//        ll.add(giphy);
//        ll.add(giphy1);
//       CometChatMessagesConfigurations configurations=new CometChatMessagesConfigurations().setMessageFilter(ll).excludeMessageTypes(Arrays.asList(CometChatMessageTemplate.DefaultList.sticker));
//       ConversationListConfiguration conversationListConfiguration=new ConversationListConfiguration().setConversationType(UIKitConstants.ReceiverTypeConstants.USER);
//       List<CometChatConfigurations> conf=new ArrayList<>();
//       conf.add(configurations);
//       conf.add(conversationListConfiguration);
//       conversationsWithMessages.setConfiguration(conf);

         return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}
