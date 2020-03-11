package screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.models.Group;

import java.util.List;

import adapter.GroupListAdapter;
import listeners.ClickListener;
import listeners.OnItemClickListener;
import listeners.RecyclerTouchListener;
import screen.creategroup.CometChatCreateGroupScreenActivity;
import utils.FontUtils;

/*

* Purpose - CometChatGroupList class is a fragment used to display list of groups and perform certain action on click of item.
            It also provide search bar to search group from the list.

* Created on - 20th December 2019

* Modified on  - 16th January 2020

*/

public class CometChatGroupListScreen extends Fragment  {

    private static OnItemClickListener event;

    private RecyclerView rvGroupList;   //Uses to display list of groups.

    private GroupListAdapter groupListAdapter;

    private GroupsRequest groupsRequest;    //Uses to fetch Groups.

    private EditText etSearch;    //Uses to perform search operations on groups.

    private ImageView clearSearch;

    private ImageView ivCreateGroup;

    private static final String TAG = "CometChatGroupListScree";

    public CometChatGroupListScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_group_list_screen, container, false);
        TextView title = view.findViewById(R.id.tv_title);
        title.setTypeface(FontUtils.getInstance(getActivity()).getTypeFace(FontUtils.robotoMedium));
        rvGroupList=view.findViewById(R.id.rv_group_list);
        etSearch = view.findViewById(R.id.search_bar);
        clearSearch = view.findViewById(R.id.clear_search);

        ivCreateGroup = view.findViewById(R.id.create_group);

        ivCreateGroup.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CometChatCreateGroupScreenActivity.class);
            startActivity(intent);
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()==0) {
                    // if etSearch is empty then fetch all groups.
                    groupsRequest=null;
                    fetchGroup();
                }
                else {
                    // Search group based on text in etSearch field.
                    searchGroup(editable.toString());
                }
            }
        });
        etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH)
                {
                    searchGroup(textView.getText().toString());
                    clearSearch.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText("");
                clearSearch.setVisibility(View.GONE);
                searchGroup(etSearch.getText().toString());
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // Hide the soft keyboard
                inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(),0);
            }
        });

        //Uses to fetch next list of group if rvGroupList (RecyclerView) is scrolled in upward direction.
        rvGroupList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    fetchGroup();
                }

            }
        });

        // Used to trigger event on click of group item in rvGroupList (RecyclerView)
        rvGroupList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvGroupList, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                Group group=(Group)var1.getTag(R.string.group);
                if (event!=null) {
                    event.OnItemClick(group, var2);
                }
            }

            @Override
            public void onLongClick(View var1, int var2) {
                Group group=(Group)var1.getTag(R.string.group);
                if (event!=null)
                    event.OnItemLongClick(group,var2);
            }
        }));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     *  This method is used to retrieve list of groups present in your App_ID.
     *  For more detail please visit our official documentation {@link "https://prodocs.cometchat.com/docs/android-groups-retrieve-groups" }
     *
     * @see GroupsRequest
     */
    private void fetchGroup(){
        if (groupsRequest==null){
            groupsRequest=new GroupsRequest.GroupsRequestBuilder().setLimit(30).build();
        }
        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                setAdapter(groups);
            }
            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    /**
     *  This method is used to search groups present in your App_ID.
     *  For more detail please visit our official documentation {@link "https://prodocs.cometchat.com/docs/android-groups-retrieve-groups" }
     *
     * @param s is a string used to get groups matches with this string.
     * @see GroupsRequest
     */
    private void searchGroup(String s)
    {
        GroupsRequest groupsRequest = new GroupsRequest.GroupsRequestBuilder().setSearchKeyWord(s).setLimit(100).build();
        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                groupListAdapter.searchGroup(groups);
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }
        });
    }

    /**
     * It sets the adapter for rvGroupList.
     * @param groupList
     */
    private void setAdapter(List<Group> groupList){
        if (groupListAdapter==null){
            // if adapter is not initialized.
            groupListAdapter=new GroupListAdapter(getContext(),groupList);
            rvGroupList.setAdapter(groupListAdapter);
        }
        else {
            // if adapter is initialized, then update adapter list with groups.
             if (groupList!=null&&groupList.size()!=0)
            groupListAdapter.updateGroupList(groupList);
        }
    }

    /**
     *
     * @param groupItemClickListener An object of <code>OnItemClickListener&lt;T&gt;</code> abstract class helps to initialize with events
     *                               to perform onItemClick & onItemLongClick.
     * @see OnItemClickListener
     */
    public static void setItemClickListener(@NonNull OnItemClickListener<Group> groupItemClickListener){
        event=groupItemClickListener;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        groupsRequest=null;
        groupListAdapter=null;
        fetchGroup();

    }
}
