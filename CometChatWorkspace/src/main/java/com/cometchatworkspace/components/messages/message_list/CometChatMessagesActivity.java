package com.cometchatworkspace.components.messages.message_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;


import com.cometchatworkspace.R;

import com.cometchatworkspace.resources.constants.UIKitConstants;

/**

* Purpose - CometChatMessageListActivity.class is a Activity used to display messages using CometChatMessageScreen.class. It takes
            parameter like TYPE to differentiate between User MessageScreen &amp; Group MessageScreen.

            It passes parameters like UID (userID) ,AVATAR (userAvatar) ,NAME (userName) ,STATUS (userStatus) to CometChatMessageScreen.class
            if TYPE is CometChatConstant.RECEIVER_TYPE_USER

            It passes parameters like GUID (groupID) ,AVATAR (groupIcon) ,NAME (groupName) ,GROUP_OWNER (groupOwner) to CometChatMessageScreen.class
            if TYPE is CometChatConstant.RECEIVER_TYPE_GROUP

            @see com.cometchat.pro.constants.CometChatConstants
            @see com.cometchatworkspace.components.messages.message_list.CometChatMessages
*/

public class CometChatMessagesActivity extends AppCompatActivity {

    private static final String TAG = "CometChatMessageListAct";

    CometChatMessages fragment = new CometChatMessages();

    private static AppCompatActivity activity;

    private static User user;

    private static Group group;

    public static void launch(Context context,User user_) {
        group = null;
        user = user_;
        context.startActivity(new Intent(context,CometChatMessagesActivity.class));
    }

    public static void launch(Context context,Group group_) {
        user = null;
        group = group_;
        context.startActivity(new Intent(context,CometChatMessagesActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_message_list);
        activity = this;

        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

         if (getIntent()!=null) {
             if (user!=null)
                 fragment.setUser(user);
             if (group!=null)
                 fragment.setGroup(group);


//             Bundle bundle = new Bundle();
//             bundle.putString(UIKitConstants.IntentStrings.AVATAR, getIntent().getStringExtra(UIKitConstants.IntentStrings.AVATAR));
//             bundle.putString(UIKitConstants.IntentStrings.NAME, getIntent().getStringExtra(UIKitConstants.IntentStrings.NAME));
//             bundle.putString(UIKitConstants.IntentStrings.TYPE,getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE));

//              if (getIntent().hasExtra(UIKitConstants.IntentStrings.TYPE)&&
//                      getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE).equals(CometChatConstants.RECEIVER_TYPE_USER)) {
//                  bundle.putString(UIKitConstants.IntentStrings.LINK,getIntent().getStringExtra(UIKitConstants.IntentStrings.LINK));
//                  bundle.putString(UIKitConstants.IntentStrings.UID, getIntent().getStringExtra(UIKitConstants.IntentStrings.UID));
//                  bundle.putString(UIKitConstants.IntentStrings.STATUS, getIntent().getStringExtra(UIKitConstants.IntentStrings.STATUS));
//
//              }else {
//                  bundle.putString(UIKitConstants.IntentStrings.GUID, getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID));
//                  bundle.putString(UIKitConstants.IntentStrings.GROUP_OWNER,getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_OWNER));
//                  bundle.putInt(UIKitConstants.IntentStrings.MEMBER_COUNT,getIntent().getIntExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,0));
//                  bundle.putString(UIKitConstants.IntentStrings.GROUP_TYPE,getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_TYPE));
//                  bundle.putString(UIKitConstants.IntentStrings.GROUP_DESC,getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_DESC));
//                  bundle.putString(UIKitConstants.IntentStrings.GROUP_PASSWORD,getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD));
//              }

              if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE)) {
                  fragment.setReplyMessage(getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE));
//                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE,
//                          getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE));
              }

//              fragment.setArguments(bundle);
             getSupportFragmentManager().beginTransaction().replace(R.id.chat_fragment, fragment).commit();
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d(TAG, "onActivityResult: ");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @VisibleForTesting
    public static AppCompatActivity getCometChatMessageListActivity() {
        return activity;
    }
}
