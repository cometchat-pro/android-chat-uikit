package com.cometchat.chatuikit.transferownership;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.chatuikit.groupmembers.CometChatGroupMembers;
import com.cometchat.chatuikit.R;

import java.util.List;

/**
 * CometChatTransferOwnership is a custom view that extends the CometChatGroupMembers view and provides a UI component
 * for transferring ownership of a group to another member. It allows the user to select a group member and initiate the
 * transfer ownership process.
 * <p>
 * This view handles the UI interactions and communicates with the TransferOwnershipViewModel to perform the transfer
 * ownership operation. It provides callbacks for handling transfer ownership events and exceptions.
 * <p>
 * To use this view, you need to set the group for which ownership transfer will be performed using the setGroup() method.
 * You can also set a listener for transfer ownership events using the setOnTransferOwnership() method.
 * <p>
 * The view internally uses the TransferOwnershipViewModel to handle the transfer ownership process and observe its
 * states and exceptions. It displays relevant UI elements and error messages based on the state and exception updates.
 * <p>
 * This view can be instantiated programmatically or defined in XML layout files.
 */
public class CometChatTransferOwnership extends CometChatGroupMembers {

    private Context context;
    private GroupMember groupMember;
    private TransferOwnershipViewModel transferOwnershipViewModel;
    private Group group;
    private CometChatTheme theme;
    private OnTransferOwnership transferOwnership;

    public CometChatTransferOwnership(Context context) {
        super(context);
        init(context, null);
    }

    public CometChatTransferOwnership(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CometChatTransferOwnership(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CometChatGroupMembers, 0, 0);
        String title = a.getString(R.styleable.CometChatGroupMembers_title) == null ? getResources().getString(R.string.transfer_ownership_text) : a.getString(R.styleable.CometChatGroupMembers_title);
        super.setTitle(title);
        transferOwnershipViewModel = new TransferOwnershipViewModel();
        transferOwnershipViewModel = ViewModelProviders.of((FragmentActivity) context).get(transferOwnershipViewModel.getClass());
        transferOwnershipViewModel.transferOwnershipStatesMutableLiveData().observe((AppCompatActivity) context, this::setTransferOwnershipState);
        transferOwnershipViewModel.getExceptionMutableLiveData().observe((AppCompatActivity) context, this::setExceptionObserver);
        super.setSelectionMode(UIKitConstants.SelectionMode.SINGLE);
        super.setTailView((context1, groupMember, group) -> {
            TextView textView = new TextView(context1);
            textView.setText(groupMember.getScope());
            textView.setTextColor(theme.getPalette().getAccent600());
            return textView;
        });
        super.setOnSelection(this::transferOwnership);
    }

    /**
     * Sets the group for which ownership transfer will be performed.
     * This method sets the group internally and updates the associated ViewModel.
     *
     * @param group The group for ownership transfer.
     */
    public void setGroup(Group group) {
        this.group = group;
        transferOwnershipViewModel.setGroup(group);
        super.setGroup(group);
    }

    /**
     * Transfers the ownership of the group to the selected group member.
     * This method is called when the user selects a member for ownership transfer.
     *
     * @param groupMembers The list of selected group members.
     */
    public void transferOwnership(List<GroupMember> groupMembers) {
        if (groupMembers.size() > 0) {
            this.groupMember = groupMembers.get(0);
            if (transferOwnership == null)
                transferOwnershipViewModel.transferOwnership(groupMember != null ? groupMember : null);
            else transferOwnership.onTransferOwnership(context, group, groupMember);
        }
    }

    private void setExceptionObserver(CometChatException exception) {
        if (getOnError() != null) getOnError().onError(context, exception);
    }

    private void setTransferOwnershipState(TransferOwnershipStates state) {
        if (TransferOwnershipStates.ERROR.equals(state)) {
            showError();
        } else if (TransferOwnershipStates.SUCCESS.equals(state)) {
            super.getBackIcon().performClick();
        }
    }

    private void showError() {
        String errorMessage;
        if (getErrorText() != null) errorMessage = getErrorText();
        else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!isHideError() && getErrorView() != null) {
            super.customLayout.removeAllViews();
            customLayout.addView(getErrorView());
            customLayout.setVisibility(VISIBLE);
        } else {
            customLayout.setVisibility(GONE);
            if (!isHideError()) {
                if (getContext() != null) {
                    new CometChatDialog(context, 0, getErrorStateTextAppearance(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, getErrorMessageColor(), errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            alertDialog.dismiss();
                            transferOwnershipViewModel.transferOwnership(groupMember != null ? groupMember : null);
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }, 0, false);
                }
            }
        }
    }

    /**
     * Sets the transfer ownership listener to handle the transfer ownership action.
     *
     * @param transferOwnership The listener for transfer ownership action.
     * @see OnTransferOwnership
     */
    public void setOnTransferOwnership(OnTransferOwnership transferOwnership) {
        this.transferOwnership = transferOwnership;
    }

    public interface OnTransferOwnership {
        void onTransferOwnership(Context context, Group group, GroupMember member);
    }
}
