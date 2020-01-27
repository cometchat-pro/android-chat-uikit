package screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.Avatar;
import com.cometchat.pro.uikit.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import adapter.GroupMemberAdapter;
import constant.StringContract;
import listeners.ClickListener;
import listeners.RecyclerTouchListener;
import utils.FontUtils;

import static utils.Utils.UserToGroupMember;


public class CometChatGroupDetailScreen extends AppCompatActivity {

    private String TAG = "CometChatGroupDetail";

    private Avatar groupIcon;

    private String ownerId;

    private TextView tvGroupName;

    private TextView tvAdminCount;

    private ArrayList<String> groupMemberUids = new ArrayList<>();

    private RecyclerView rvMemberList;

    private String guid, gName;

    private GroupMembersRequest groupMembersRequest;

    private GroupMemberAdapter groupMemberAdapter;

    private int adminCount;

    String[] s = new String[0];

    private RelativeLayout rlAddMemberView;

    private String loggedInUserScope;

    private GroupMember groupMember;

    private TextView tvDelete;

    private User loggedInUser = CometChat.getLoggedInUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comet_chat_group_detail_screen);
        new FontUtils(this);
        initComponent();

    }

    private void initComponent() {

        groupIcon = findViewById(R.id.iv_group);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvAdminCount = findViewById(R.id.tv_admin_count);
        rvMemberList = findViewById(R.id.member_list);
        TextView tvAddMember = findViewById(R.id.tv_add_member);

        rlAddMemberView = findViewById(R.id.rl_add_member);
        tvDelete = findViewById(R.id.tv_delete);
        TextView tvExit = findViewById(R.id.tv_exit);
        MaterialToolbar toolbar = findViewById(R.id.groupDetailToolbar);

        tvDelete.setTypeface(FontUtils.robotoMedium);
        tvExit.setTypeface(FontUtils.robotoMedium);
        tvAddMember.setTypeface(FontUtils.robotoRegular);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMemberList.setLayoutManager(linearLayoutManager);
//        rvMemberList.setNestedScrollingEnabled(false);

        handleIntent();

        rvMemberList.addOnItemTouchListener(new RecyclerTouchListener(this, rvMemberList, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                GroupMember user = (GroupMember) var1.getTag(R.string.user);
                if (loggedInUserScope != null&&loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN)) {
                    groupMember = user;
                    boolean isAdmin =user.getScope().equals(CometChatConstants.SCOPE_ADMIN) ;
                    boolean isSelf = loggedInUser.getUid().equals(user.getUid());
                    boolean isOwner = loggedInUser.getUid().equals(ownerId);
                    if (!isSelf) {
                        if (!isAdmin||isOwner) {
                            registerForContextMenu(rvMemberList);
                            openContextMenu(var1);
                        }
                    }
                }
            }

            @Override
            public void onLongClick(View var1, int var2) {

            }
        }));

        rvMemberList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    getGroupMembers();
                }
            }

        });

        tvExit.setOnClickListener(view -> createDialog("Exit  Group", "Exit from group ?",
                "Exit", "Cancel", R.drawable.ic_exit_to_app));

        tvDelete.setOnClickListener(view -> createDialog("Delete Group", "Delete the group",
                "Delete", "Cancel", R.drawable.ic_delete_24dp));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_action_menu, menu);

        menu.findItem(R.id.item_make_admin).setVisible(false);

        menu.setHeaderTitle("Group Action");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.item_remove) {
            kickMember();
        }

        return super.onContextItemSelected(item);
    }

    private void createDialog(String title, String message, String positiveText, String negativeText, int drawableRes) {

        MaterialAlertDialogBuilder alert_dialog = new MaterialAlertDialogBuilder(CometChatGroupDetailScreen.this,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered);
        alert_dialog.setTitle(title);
        alert_dialog.setMessage(message);
        alert_dialog.setPositiveButton(positiveText, (dialogInterface, i) -> {

            if (positiveText.equalsIgnoreCase("Exit"))
                leaveGroup();

            else if (positiveText.equalsIgnoreCase("Delete")
                    && loggedInUserScope.equalsIgnoreCase(CometChatConstants.SCOPE_ADMIN))
                deleteGroup();

        });

        alert_dialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert_dialog.create();
        alert_dialog.show();

    }


    private void handleIntent() {
        if (getIntent().hasExtra(StringContract.IntentStrings.GUID)) {
            guid = getIntent().getStringExtra(StringContract.IntentStrings.GUID);
        }
        if (getIntent().hasExtra(StringContract.IntentStrings.MEMBER_SCOPE)) {
            loggedInUserScope = getIntent().getStringExtra(StringContract.IntentStrings.MEMBER_SCOPE);
            if (loggedInUserScope != null && loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN)) {
                rlAddMemberView.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
            }

        }
        if (getIntent().hasExtra(StringContract.IntentStrings.NAME)) {
            gName = getIntent().getStringExtra(StringContract.IntentStrings.NAME);
            tvGroupName.setText(gName);
        }
        if (getIntent().hasExtra(StringContract.IntentStrings.AVATAR)) {
            String avatar = getIntent().getStringExtra(StringContract.IntentStrings.AVATAR);
            if (avatar != null && !avatar.isEmpty())
                groupIcon.setAvatar(avatar);
            else
                groupIcon.setInitials(gName);
        }
        if (getIntent().hasExtra(StringContract.IntentStrings.GROUP_OWNER)) {
            ownerId = getIntent().getStringExtra(StringContract.IntentStrings.GROUP_OWNER);
        }
    }


    public void openAdminListScreen(View view) {
        Intent intent = new Intent(this, CometChatAdminListScreenActivity.class);
        intent.putExtra(StringContract.IntentStrings.GUID, guid);
        intent.putExtra(StringContract.IntentStrings.GROUP_OWNER, ownerId);
        intent.putExtra(StringContract.IntentStrings.MEMBER_SCOPE, loggedInUserScope);
        startActivity(intent);
    }

    public void addMembers(View view) {
        Intent intent = new Intent(this, CometChatAddMemberScreenActivity.class);
        intent.putExtra(StringContract.IntentStrings.GUID, guid);
        intent.putExtra(StringContract.IntentStrings.GROUP_MEMBER, groupMemberUids);
        intent.putExtra(StringContract.IntentStrings.GROUP_NAME, gName);
        intent.putExtra(StringContract.IntentStrings.MEMBER_SCOPE, loggedInUserScope);
        intent.putExtra(StringContract.IntentStrings.IS_ADD_MEMBER, true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupMembersRequest = null;
        if (groupMemberAdapter != null) {
            groupMemberAdapter.resetAdapter();
            groupMemberAdapter = null;

        }
        getGroupMembers();
        addGroupListener();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        removeGroupListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeGroupListener();
    }

    private void deleteGroup() {
        CometChat.deleteGroup(guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                launchUnified();
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(rvMemberList, "Group deletion failed", Snackbar.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void launchUnified() {
        Intent intent = new Intent(CometChatGroupDetailScreen.this, CometChatUnified.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void kickMember() {
        CometChat.kickGroupMember(groupMember.getUid(), guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "onSuccess: " + s);
                groupMemberUids.remove(groupMember.getUid());
                groupMemberAdapter.removeGroupMember(groupMember);
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(rvMemberList, "Can't remove " + groupMember.getName(), Snackbar.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void getGroupMembers() {
        if (groupMembersRequest == null) {
            groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(guid).setLimit(100).build();
        }
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                Log.e(TAG, "onSuccess: " + groupMembers.size());
                if (groupMembers != null && groupMembers.size() != 0) {
                    adminCount = 0;
                    groupMemberUids.clear();
                    s = new String[groupMembers.size()];
                    for (int j = 0; j < groupMembers.size(); j++) {
                        groupMemberUids.add(groupMembers.get(j).getUid());
                        if (groupMembers.get(j).getScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                            adminCount++;
                        }
                        s[j] = groupMembers.get(j).getName();
                    }
                    tvAdminCount.setText(adminCount + "");
                    if (groupMemberAdapter == null) {
                        groupMemberAdapter = new GroupMemberAdapter(CometChatGroupDetailScreen.this, groupMembers, ownerId);
                        rvMemberList.setAdapter(groupMemberAdapter);
                    } else {
                        groupMemberAdapter.addAll(groupMembers);
                    }
                }
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(rvMemberList, "Failed to retrive member list", Snackbar.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }


    private void leaveGroup() {
        CometChat.leaveGroup(guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                launchUnified();
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(rlAddMemberView, "Unable to leave group ", Snackbar.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

    public void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                Log.e(TAG, "onGroupMemberJoined: " + joinedUser.getUid());
                if (groupMemberAdapter != null)
                    groupMemberAdapter.addGroupMember(UserToGroupMember(joinedUser, false, action.getOldScope()));
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                Log.d(TAG, "onGroupMemberLeft: ");
                if (groupMemberAdapter != null)
                    groupMemberAdapter.removeGroupMember(UserToGroupMember(leftUser, false, action.getOldScope()));
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                Log.d(TAG, "onGroupMemberKicked: ");
                if (groupMemberAdapter != null)
                    groupMemberAdapter.removeGroupMember(UserToGroupMember(kickedUser, false, action.getOldScope()));
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                Log.d(TAG, "onGroupMemberScopeChanged: ");
                if (group.getGuid().equals(guid)) {
                    if (groupMemberAdapter != null) {
                        groupMemberAdapter.updateMember(UserToGroupMember(updatedUser, true, action.getNewScope()));
                        if (action.getNewScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                            adminCount = adminCount + 1;
                            tvAdminCount.setText(String.valueOf(adminCount));
                            if (updatedUser.getUid().equals(loggedInUser.getUid())) {
                                rlAddMemberView.setVisibility(View.VISIBLE);
                                loggedInUserScope = CometChatConstants.SCOPE_ADMIN;
                                tvDelete.setVisibility(View.VISIBLE);
                            } else {
                                loggedInUserScope = action.getNewScope();
                                rlAddMemberView.setVisibility(View.GONE);
                                tvDelete.setVisibility(View.GONE);
                            }
                        } else if (action.getOldScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                            adminCount = adminCount - 1;
                            tvAdminCount.setText(String.valueOf(adminCount));
                        }
                    }
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {

                if (groupMemberAdapter != null) {
                    groupMemberAdapter.addGroupMember(UserToGroupMember(userAdded, false, action.getNewScope()));

                }
            }
        });
    }

    public void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }


}
