package screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.uikit.Avatar;
import com.cometchat.pro.uikit.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import constant.StringContract;
import utils.FontUtils;

public class CometChatUserDetailScreen extends AppCompatActivity {
    private Avatar userAvatar;

    private TextView userStatus, userName, addBtn;

    private String name;

    private String TAG = "CometChatUserDetailScreen";

    private String avatar;

    private String uid;

    private String guid;

    private String groupName;

    private boolean isAddMember;

    private boolean isAlreadyAdded;

    private TextView tvSendMessage;

    private TextView tvBlockUser;

    private MaterialToolbar toolbar;

    private boolean isBlocked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_detail_screen);
        initComponent();
        new FontUtils(this);
    }

    private void initComponent() {

        userAvatar = findViewById(R.id.iv_user);
        userName = findViewById(R.id.tv_name);
        userStatus = findViewById(R.id.tv_status);
        addBtn = findViewById(R.id.btn_add);
        tvSendMessage=findViewById(R.id.tv_send_message);
        toolbar=findViewById(R.id.user_detail_toolbar);

         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addBtn.setTypeface(FontUtils.robotoRegular);

        tvBlockUser = findViewById(R.id.tv_blockUser);

        tvBlockUser.setTypeface(FontUtils.robotoMedium);

        userName.setTypeface(FontUtils.robotoMedium);


        handleIntent();

        addBtn.setOnClickListener(view -> {

            if (guid != null) {
                if (isAddMember) {
                    if (isAlreadyAdded)
                        kickGroupMember();
                    else
                        addMember();
                }
            }
        });

        tvSendMessage.setOnClickListener(view -> {
              if (isAddMember){
                  Intent intent=new Intent(CometChatUserDetailScreen.this,CometChatMessageListActivity.class);
                  intent.putExtra(StringContract.IntentStrings.TYPE,CometChatConstants.RECEIVER_TYPE_USER);
                  intent.putExtra(StringContract.IntentStrings.UID,uid);
                  intent.putExtra(StringContract.IntentStrings.NAME,name);
                  intent.putExtra(StringContract.IntentStrings.AVATAR,avatar);
                  intent.putExtra(StringContract.IntentStrings.STATUS,CometChatConstants.USER_STATUS_OFFLINE);
                  startActivity(intent);
              }else
                  onBackPressed();
        });

        tvBlockUser.setOnClickListener(view -> {
            if (isBlocked)
               unblockUser();
            else
                blockUser();
        });
    }


    private void handleIntent() {

        if (getIntent().hasExtra(StringContract.IntentStrings.IS_ADD_MEMBER)) {
            isAddMember = getIntent().getBooleanExtra(StringContract.IntentStrings.IS_ADD_MEMBER, false);
        }

        if (getIntent().hasExtra(StringContract.IntentStrings.IS_BLOCKED_BY_ME)){
            isBlocked=getIntent().getBooleanExtra(StringContract.IntentStrings.IS_BLOCKED_BY_ME,false);
             setBlockUnblock();
        }

        if (getIntent().hasExtra(StringContract.IntentStrings.GUID)) {
            guid = getIntent().getStringExtra(StringContract.IntentStrings.GUID);
        }

        if (getIntent().hasExtra(StringContract.IntentStrings.UID)) {
            uid = getIntent().getStringExtra(StringContract.IntentStrings.UID);
        }
        if (getIntent().hasExtra(StringContract.IntentStrings.GROUP_NAME)) {
            groupName = getIntent().getStringExtra(StringContract.IntentStrings.GROUP_NAME);
        }
        if (getIntent().hasExtra(StringContract.IntentStrings.NAME)) {
            name = getIntent().getStringExtra(StringContract.IntentStrings.NAME);
            userName.setText(name);
        }

        if (getIntent().hasExtra(StringContract.IntentStrings.AVATAR)) {
            avatar = getIntent().getStringExtra(StringContract.IntentStrings.AVATAR);
        }
        if (getIntent().hasExtra(StringContract.IntentStrings.STATUS)) {
            String status = getIntent().getStringExtra(StringContract.IntentStrings.STATUS);

            if (status != null && status.equals(CometChatConstants.USER_STATUS_ONLINE))
                userStatus.setTextColor(getResources().getColor(R.color.colorPrimary));

            userStatus.setText(status);
        }

        if (avatar != null && !avatar.isEmpty())
            userAvatar.setAvatar(avatar);
        else {
            if (name != null && !name.isEmpty())
                userAvatar.setInitials(name);
            else
                userAvatar.setInitials("Unknown");
        }

        if (isAddMember) {
            addBtn.setText("Add " + name + " to " + groupName);
        } else {
            addBtn.setVisibility(View.GONE);
        }
    }

    private void setBlockUnblock() {
        if (isBlocked) {
            tvBlockUser.setTextColor(getResources().getColor(R.color.online_green));
            tvBlockUser.setText("Unblock User");
        } else{
            tvBlockUser.setText("Block User");
            tvBlockUser.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId()==android.R.id.home){
             onBackPressed();
         }
        return super.onOptionsItemSelected(item);
    }

    private void addMember() {
        List<GroupMember> userList = new ArrayList<>();
        userList.add(new GroupMember(uid, CometChatConstants.SCOPE_PARTICIPANT));
        CometChat.addMembersToGroup(guid, userList, null, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                Log.e(TAG, "onSuccess: " + uid + "Group" + guid);
                Snackbar.make(addBtn, userName.getText().toString() + " is added to " + groupName, Snackbar.LENGTH_LONG).show();
                addBtn.setText("Remove from " + groupName);
                isAlreadyAdded = true;


            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(CometChatUserDetailScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void kickGroupMember() {

        CometChat.kickGroupMember(uid, guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Snackbar.make(addBtn, userName.getText().toString() + " is removed from " + groupName, Snackbar.LENGTH_LONG).show();
                addBtn.setText("Add in Group \"" + groupName + "\"");
                addBtn.setVisibility(View.VISIBLE);
                isAlreadyAdded = false;

            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(CometChatUserDetailScreen.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void unblockUser() {
        ArrayList<String> uids = new ArrayList<>();
        uids.add(uid);

      CometChat.unblockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
          @Override
          public void onSuccess(HashMap<String, String> stringStringHashMap) {
              Snackbar.make(tvBlockUser,userName.getText().toString() + " is Unblocked",Snackbar.LENGTH_SHORT).show();
              isBlocked=false;
              setBlockUnblock();
          }

          @Override
          public void onError(CometChatException e) {
              Log.d(TAG, "onError: "+e.getMessage());
              Snackbar.make(tvBlockUser,userName.getText().toString() + "Unblock Action Failed",Snackbar.LENGTH_SHORT).show();
          }
      });
    }


    private void blockUser() {

        ArrayList<String> uids = new ArrayList<>();
        uids.add(uid);
        CometChat.blockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                Snackbar.make(tvBlockUser,userName.getText().toString() + " is Blocked",Snackbar.LENGTH_SHORT).show();
                isBlocked=true;
                setBlockUnblock();
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(tvBlockUser,userName.getText().toString() + "Block Action Failed",Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "onError: "+e.getMessage());
            }
        });
    }

}
