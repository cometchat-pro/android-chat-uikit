package screen;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.uikit.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import constant.StringContract;

/**
 * Purpose - CometChatCreateGroup class is a fragment used to create a group. User just need to enter
 * group name. All other information like guid, groupIcon are set by this class.
 *
 * @see CometChat#createGroup(Group, CometChat.CallbackListener)
 *
 *
 */


public class CometChatCreateGroupScreen extends Fragment {

    private TextInputEditText etGroupName;

    String TAG = "CometChatCreateGroup";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comet_chat_create_group_screen, container, false);

        etGroupName = view.findViewById(R.id.group_name);

        MaterialButton createGroupBtn = view.findViewById(R.id.btn_create_group);

        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });
        return view;
    }

    /**
     * This method is used to create group when called from layout. It uses <code>Random.nextInt()</code>
     * to generate random number to use with group id and group icon. Any Random number between 10 to
     * 1000 are choosen.
     *
     */
    private void createGroup() {
        if (etGroupName.getText()!=null&&!etGroupName.getText().toString().isEmpty()) {
            int randomNum = new Random().nextInt((1000-10)+1)+10;
            Group group = new Group("group"+randomNum, etGroupName.getText().toString(), CometChatConstants.GROUP_TYPE_PUBLIC,"");
            // This url is a url of image which will be set as group icon.
            group.setIcon("https://picsum.photos/id/"+randomNum+"/200/200");
            Log.e(TAG, "createGroup: "+randomNum);
            CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    Intent intent = new Intent(getActivity(), CometChatMessageListActivity.class);
                    intent.putExtra(StringContract.IntentStrings.NAME,group.getName());
                    intent.putExtra(StringContract.IntentStrings.GROUP_OWNER,group.getOwner());
                    intent.putExtra(StringContract.IntentStrings.GUID,group.getGuid());
                    intent.putExtra(StringContract.IntentStrings.AVATAR,group.getIcon());
                    intent.putExtra(StringContract.IntentStrings.TYPE,CometChatConstants.RECEIVER_TYPE_GROUP);
                    if (getActivity()!=null)
                    getActivity().finish();

                    startActivity(intent);
                }

                @Override
                public void onError(CometChatException e) {
                    Snackbar.make(etGroupName.getRootView(),"Unable to create a group ",Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, "onError: "+e.getMessage() );
                }
            });
        }
        else {
            etGroupName.setError("Fill this Field");
        }
    }

}
