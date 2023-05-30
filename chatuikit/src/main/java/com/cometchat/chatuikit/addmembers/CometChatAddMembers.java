package com.cometchat.chatuikit.addmembers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.users.CometChatUsers;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The custom implementation of the CometChatUsers class called CometChatAddMembers.
 * It extends the CometChatUsers class and adds additional functionality for adding members to a group.
 * <br>
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *             <com.cometchat.chatuikit.addmembers.CometChatAddMembers
 *                android:id="@+id/add_members"
 *                android:layout_width="match_parent"
 *                android:layout_height="match_parent" />
 *
 *  </LinearLayout>
 *
 *  //now in activity or fragment
 *   CometChatAddMembers addMembers = view.findViewById(R.id.add_members);
 *   addMembers.setGroup(group);
 *  }
 *  </pre>
 */
public class CometChatAddMembers extends CometChatUsers {
    private Context context;
    private AddMembersViewModel addMembersViewModel;
    private List<User> users = new ArrayList<>();
    private Palette palette;
    private Typography typography;

    public CometChatAddMembers(Context context) {
        super(context);
        init(context, null, -1);
    }

    public CometChatAddMembers(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CometChatAddMembers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        this.palette = Palette.getInstance(context);
        this.typography = Typography.getInstance();
        super.setTitle(context.getResources().getString(R.string.add_members));
        super.backIcon(context.getResources().getDrawable(R.drawable.ic_arrow_back));
        super.showBackButton(true);
        super.backIconTint(palette.getPrimary());
        addMembersViewModel = new AddMembersViewModel();
        addMembersViewModel = ViewModelProviders.of((FragmentActivity) context).get(addMembersViewModel.getClass());
        addMembersViewModel.getException().observe((AppCompatActivity) context, exceptionObserver);
        addMembersViewModel.addMemberStates().observe((AppCompatActivity) context, addMemberStates);
        super.setSelectionMode(UIKitConstants.SelectionMode.MULTIPLE);
        super.setOnSelection(userList -> {
            users = userList;
            addMembersViewModel.addMembersToGroup(userList);
        });
    }

    /**
     * The setGroup method in the CometChatAddMembers class is responsible for setting the group for which members will be added.
     *
     * @param group
     */
    public void setGroup(Group group) {
        if (group != null) {
            addMembersViewModel.setGroup(group);
        }
    }

    Observer<CometChatException> exceptionObserver = Throwable::printStackTrace;

    /**
     * Sets the style for the CometChatAddMembers view.
     *
     * @param addMemberStyle The style to be applied to the CometChatAddMembers view.
     */
    public void setStyle(AddMembersStyle addMemberStyle) {
        super.setStyle(addMemberStyle);
    }

    Observer<AddMemberStates> addMemberStates = addMemberStates -> {
        if (AddMemberStates.ERROR.equals(addMemberStates)) {
            showError();
        }
        if (AddMemberStates.SUCCESS.equals(addMemberStates)) {
            getBackIcon().performClick();
        }
    };

    private void showError() {
        String errorMessage;
        if (errorText != null) errorMessage = errorText;
        else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!hideError && errorView != null) {
            customLayout.removeAllViews();
            customLayout.addView(errorView);
            customLayout.setVisibility(VISIBLE);
        } else {
            customLayout.setVisibility(GONE);
            if (!hideError) {
                if (getContext() != null) {
                    new CometChatDialog(context, 0, errorStateTextAppearance, typography.getText3(), palette.getAccent900(), 0, errorMessageColor, errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", palette.getPrimary(), palette.getPrimary(), 0, (alertDialog, which, popupId) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            alertDialog.dismiss();
                            addMembersViewModel.addMembersToGroup(users);
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }, 0, false);
                }
            }
        }
    }
}
