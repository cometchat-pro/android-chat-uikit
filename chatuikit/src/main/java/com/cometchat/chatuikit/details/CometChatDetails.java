package com.cometchat.chatuikit.details;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.bannedmembers.BannedMembersConfiguration;
import com.cometchat.chatuikit.bannedmembers.CometChatBannedMembers;
import com.cometchat.chatuikit.groupmembers.CometChatGroupMembers;
import com.cometchat.chatuikit.groupmembers.GroupMembersConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnDetailOptionClick;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.OnDialogButtonClickListener;
import com.cometchat.chatuikit.shared.utils.DetailsUtils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.chatuikit.transferownership.CometChatTransferOwnership;
import com.cometchat.chatuikit.transferownership.TransferOwnershipConfiguration;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.addmembers.AddMembersConfiguration;
import com.cometchat.chatuikit.addmembers.CometChatAddMembers;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * CometChatDetails is a class that extends CometChatListBase and provides functionality for displaying details
 * and options related to a user or a group.
 */
public class CometChatDetails extends CometChatListBase implements OnDetailOptionClick {

    private static final String TAG = CometChatDetails.class.getName();
    private Context context;
    private CometChatListItem cometChatListItem;
    private User user;
    private Group group;
    private Function3<Context, User, Group, View> subtitleView;
    private Function3<Context, User, Group, View> customProfileView;
    private Function3<Context, User, Group, List<CometChatDetailsTemplate>> templates;
    private boolean disableUserPresence;
    private @DrawableRes int protectedGroupIcon, privateGroupIcon;
    private @ColorInt int onlineStatusColor;
    private CometChatTheme theme;
    private RecyclerView recyclerView;
    private TemplateAdapter templateAdapter;
    private LinearLayout profileLayout;
    private int errorStateTextAppearance = 0;
    private int errorMessageColor = 0;
    private String errorText = null;
    private boolean hideError = false;
    private boolean isOwner = false;
    private final User loggedInUser = CometChatUIKit.getLoggedInUser();
    private DetailsViewModel detailsViewModel;
    private OnError errorCallBack;
    private GroupMembersConfiguration groupMembersConfiguration;
    private AddMembersConfiguration addMembersConfiguration;
    private BannedMembersConfiguration bannedMembersConfiguration;
    private TransferOwnershipConfiguration transferOwnershipConfiguration;

    /**
     * Constructs a new CometChatDetails object with the given context.
     *
     * @param context The context in which the CometChatDetails object is created.
     */
    public CometChatDetails(Context context) {
        super(context);
        if (!isInEditMode()) initViewComponent(context, null, -1);
    }

    /**
     * Constructs a new CometChatDetails object with the given context and attributes.
     *
     * @param context The context in which the CometChatDetails object is created.
     * @param attrs   The attribute set for the CometChatDetails object.
     */
    public CometChatDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) initViewComponent(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatDetails object with the given context, attributes, and default style.
     *
     * @param context      The context in which the CometChatDetails object is created.
     * @param attrs        The attribute set for the CometChatDetails object.
     * @param defStyleAttr The default style attribute for the CometChatDetails object.
     */
    public CometChatDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the view components of CometChatDetails.
     *
     * @param context      The context in which the view components are initialized.
     * @param attrs        The attribute set for the view components.
     * @param defStyleAttr The default style attribute for the view components.
     */
    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        detailsViewModel = new DetailsViewModel();
        theme = CometChatTheme.getInstance(context);
        protectedGroupIcon = R.drawable.ic_password_group;
        privateGroupIcon = R.drawable.ic_private_group;
        onlineStatusColor = theme.getPalette().getSuccess();
        errorStateTextAppearance = theme.getTypography().getText1();
        errorMessageColor = theme.getPalette().getAccent700();
        View view = View.inflate(context, R.layout.cometchat_details, null);
        recyclerView = view.findViewById(R.id.recycler_view);
        profileLayout = view.findViewById(R.id.profile_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        templateAdapter = new TemplateAdapter(context, null);
        recyclerView.setAdapter(templateAdapter);
        cometChatListItem = view.findViewById(R.id.dataItem);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CometChatDetails, 0, 0);

        boolean showBackButton = a.getBoolean(R.styleable.CometChatDetails_showBackButton, true);
        int backGroundColor = a.getColor(R.styleable.CometChatDetails_backgroundColor, theme.getPalette().getBackground());
        int backIconTint = a.getColor(R.styleable.CometChatDetails_backIconTint, theme.getPalette().getPrimary());
        int titleColor = a.getColor(R.styleable.CometChatDetails_titleColor, theme.getPalette().getAccent());
        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatDetails_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatDetails_backButtonIcon) : getResources().getDrawable(R.drawable.ic_close_24dp);
        detailsViewModel = ViewModelProviders.of((FragmentActivity) context).get(detailsViewModel.getClass());
        detailsViewModel.getActionUser().observe((AppCompatActivity) context, this::observeState);
        detailsViewModel.getBlockUser().observe((AppCompatActivity) context, this::blockSuccess);
        detailsViewModel.getUnblockUser().observe((AppCompatActivity) context, this::unblockSuccess);
        detailsViewModel.getLeaveGroup().observe((AppCompatActivity) context, this::leaveSuccess);
        detailsViewModel.getDeleteGroup().observe((AppCompatActivity) context, this::deleteSuccess);
        detailsViewModel.isKicked().observe((AppCompatActivity) context, this::kicked);
        detailsViewModel.isBanned().observe((AppCompatActivity) context, this::banned);
        detailsViewModel.getUserMutableLiveData().observe((AppCompatActivity) context, this::setUser);
        detailsViewModel.getGroupMutableLiveData().observe((AppCompatActivity) context, this::setGroup);
        detailsViewModel.getCometChatExceptionMutableLiveData().observe((AppCompatActivity) context, this::throwError);
        cometChatListItem.hideSeparator(true);
        super.setTitle(getContext().getString(R.string.details));
        super.setTitleAppearance(theme.getTypography().getHeading());
        super.setTitleColor(titleColor);
        super.backIcon(backButtonIcon);
        super.backIconTint(backIconTint);
        super.showBackButton(showBackButton);
        super.hideSearch(true);
        templateAdapter.setOnOptionClickListener(this);
        if (theme.getPalette().getGradientBackground() != null)
            setBackground(theme.getPalette().getGradientBackground());
        else setBackgroundColor(backGroundColor);
        setListItemStyle(new ListItemStyle());
        setAvatarStyle(new AvatarStyle());
        super.addListView(view);
    }

    public void kicked(boolean value) {
        if (value) {
            getBackIcon().performClick();
        } else {
            setSubtitleView(subtitleView);
            setCustomProfileView(customProfileView);
        }
    }

    public void banned(boolean value) {
        if (value) {
            getBackIcon().performClick();
        } else {
            setSubtitleView(subtitleView);
            setCustomProfileView(customProfileView);
        }
    }

    /**
     * Sets the subtitle view for the CometChatDetails.
     *
     * @param subtitleView The function that provides the subtitle view.
     */
    public void setSubtitleView(Function3<Context, User, Group, View> subtitleView) {
        this.subtitleView = subtitleView;
        if (subtitleView != null) {
            cometChatListItem.setSubtitleView(subtitleView.apply(context, user, group));
        }
    }

    /**
     * Sets the custom profile view for the CometChatDetails.
     *
     * @param profileView The function that provides the custom profile view.
     */
    public void setCustomProfileView(Function3<Context, User, Group, View> profileView) {
        this.customProfileView = profileView;
        if (profileView != null) {
            Utils.handleView(profileLayout, profileView.apply(context, user, group), false);
        }
    }

    /**
     * Hides or shows the profile layout based on the given value.
     *
     * @param value The value to determine whether to hide or show the profile layout.
     */
    public void hideProfile(boolean value) {
        profileLayout.setVisibility(value ? GONE : VISIBLE);
    }

    public void blockSuccess(Void v) {
        setData(templates);
    }

    public void unblockSuccess(Void v) {
        setData(templates);
    }

    public void leaveSuccess(Void v) {
        super.getBackIcon().performClick();
        setData(templates);
    }

    public void deleteSuccess(Void v) {
        super.getBackIcon().performClick();
        setData(templates);
    }

    public void observeState(States states) {
        if (States.SUCCESS.equals(states)) setData(templates);
        else if (States.ERROR.equals(states)) hideError(null);
    }

    /**
     * Sets the text appearance for the error state.
     *
     * @param appearance The text appearance style to be set.
     */
    private void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the color for the error message.
     *
     * @param errorMessageColor The color value to be set.
     */
    private void setErrorMessageColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error text.
     *
     * @param errorText The error text to be set.
     */
    private void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    /**
     * Sets whether to hide the error view.
     *
     * @param hideError The value to determine whether to hide the error view.
     */
    private void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    /**
     * Sets the user for the CometChatDetails.
     *
     * @param user_ The user object to be set.
     */
    public void setUser(User user_) {
        if (user_ != null) {
            this.user = user_;
            templateAdapter.setUser(user);
            cometChatListItem.setAvatar(user.getAvatar(), user.getName());
            cometChatListItem.setTitle(user.getName());
            setUserStatusData(user);
            detailsViewModel.setUser(user);
            setData(templates);
        }
    }

    /**
     * Sets the group for the CometChatDetails.
     *
     * @param group_ The group object to be set.
     */
    public void setGroup(Group group_) {
        this.group = group_;
        templateAdapter.setGroup(group);
        isOwner = group_.getOwner().equalsIgnoreCase(loggedInUser.getUid());
        cometChatListItem.setAvatar(group.getIcon(), group.getName());
        cometChatListItem.setStatusIndicatorIcon(CometChatConstants.GROUP_TYPE_PASSWORD.equalsIgnoreCase(group_.getGroupType()) ? protectedGroupIcon : privateGroupIcon);
        cometChatListItem.hideStatusIndicator(CometChatConstants.GROUP_TYPE_PUBLIC.equalsIgnoreCase(group_.getGroupType()));
        cometChatListItem.setTitle(group.getName());
        detailsViewModel.setGroup(group);
        setData(templates);
    }

    /**
     * Sets the data for the CometChatDetails.
     *
     * @param templates The function that provides the data templates.
     */
    public void setData(Function3<Context, User, Group, List<CometChatDetailsTemplate>> templates) {
        this.templates = templates;
        if (templates == null) {
            templateAdapter.setDetailTemplate(DetailsUtils.getDefaultDetailsTemplate(context, user, group));
        } else {
            templateAdapter.setDetailTemplate(getData(user, group));
        }
    }

    private List<CometChatDetailsTemplate> getData(User user, Group group) {
        List<CometChatDetailsTemplate> templates = new ArrayList<>();
        if (this.templates != null) templates = this.templates.apply(context, user, group);
        return templates;
    }

    private void performActionOnClick(CometChatDetailsOption option, String templateId) {
        if (user != null && option != null && option.getId() != null) {
            if (option.getId().equalsIgnoreCase(UIKitConstants.UserOption.BLOCK)) {
                if (option.getOnClick() == null) detailsViewModel.blockUser();
                else option.getOnClick().onClick(user, null, templateId, option, context);
            } else if (option.getId().equalsIgnoreCase(UIKitConstants.UserOption.UNBLOCK)) {
                if (option.getOnClick() == null) detailsViewModel.unblockUser();
                else option.getOnClick().onClick(user, null, templateId, option, context);
            } else if (user.getLink() != null && option.getId().equalsIgnoreCase(UIKitConstants.UserOption.VIEW_PROFILE)) {
                if (option.getOnClick() == null) viewUserProfile(user.getLink());
                else option.getOnClick().onClick(user, null, templateId, option, context);
            } else {
                if (option.getOnClick() != null)
                    option.getOnClick().onClick(user, null, templateId, option, context);
            }
        } else if (group != null && option != null && option.getId() != null) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
            if (option.getId().equalsIgnoreCase(UIKitConstants.GroupOption.VIEW_GROUP_MEMBERS)) {
                if (option.getOnClick() == null) {
                    CometChatGroupMembers groupMembers = new CometChatGroupMembers(context);
                    groupMembers.setGroup(group);
                    if (groupMembersConfiguration != null) {
                        groupMembers.setSubtitleView(groupMembersConfiguration.getSubtitle());
                        groupMembers.disableUsersPresence(groupMembersConfiguration.isDisableUsersPresence());
                        groupMembers.setListItemView(groupMembersConfiguration.getCustomView());
                        groupMembers.setMenu(groupMembersConfiguration.getMenu());
                        groupMembers.setOptions(groupMembersConfiguration.getOptions());
                        groupMembers.hideSeparator(groupMembersConfiguration.isHideSeparator());
                        groupMembers.setSearchPlaceholderText(groupMembersConfiguration.getSearchPlaceholderText());
                        groupMembers.showBackButton(groupMembersConfiguration.isShowBackButton());
                        groupMembers.backIcon(groupMembersConfiguration.getBackButtonIcon());
                        groupMembers.setSelectionMode(groupMembersConfiguration.getSelectionMode());
                        groupMembers.setOnSelection(groupMembersConfiguration.getOnSelection());
                        groupMembers.setSearchBoxIcon(groupMembersConfiguration.getBackButtonIcon());
                        groupMembers.hideSearch(groupMembersConfiguration.isHideSearch());
                        groupMembers.setTitle(groupMembersConfiguration.getTitle());
                        groupMembers.setEmptyStateView(groupMembersConfiguration.getEmptyStateView());
                        groupMembers.setErrorStateView(groupMembersConfiguration.getErrorStateView());
                        groupMembers.setLoadingStateView(groupMembersConfiguration.getLoadingStateView());
                        groupMembers.setGroupMembersRequestBuilder(groupMembersConfiguration.getMembersRequestBuilder());
                        groupMembers.setSearchRequestBuilder(groupMembersConfiguration.getMembersSearchRequestBuilder());
                        groupMembers.setAvatarStyle(groupMembersConfiguration.getAvatarStyle());
                        groupMembers.setSearchBoxIcon(groupMembersConfiguration.getSearchBoxIcon());
                        groupMembers.setListItemStyle(groupMembersConfiguration.getListItemStyle());
                        groupMembers.setStatusIndicatorStyle(groupMembersConfiguration.getStatusIndicatorStyle());
                        groupMembers.errorStateText(groupMembersConfiguration.getErrorStateText());
                        groupMembers.emptyStateText(groupMembersConfiguration.getEmptyStateText());
                        groupMembers.setStyle(groupMembersConfiguration.getStyle());
                        groupMembers.setSelectionIcon(groupMembersConfiguration.getSelectionIcon());
                        groupMembers.setSubmitIcon(groupMembersConfiguration.getSubmitIcon());
                        groupMembers.setItemClickListener(groupMembersConfiguration.getOnItemClickListener());
                        groupMembers.setOnError(groupMembersConfiguration.getOnError());
                    }
                    alertDialog.setView(groupMembers);
                    Dialog dialog = alertDialog.create();
                    groupMembers.addOnBackPressListener(dialog::dismiss);
                    dialog.show();
                } else option.getOnClick().onClick(null, group, templateId, option, context);
            } else if (option.getId().equalsIgnoreCase(UIKitConstants.GroupOption.ADD_GROUP_MEMBERS)) {
                if (option.getOnClick() == null) {
                    CometChatAddMembers addMembers = new CometChatAddMembers(context);
                    addMembers.setGroup(group);
                    if (addMembersConfiguration != null) {
                        addMembers.setSubtitle(addMembersConfiguration.getSubtitle());
                        addMembers.disableUsersPresence(addMembersConfiguration.isDisableUsersPresence());
                        addMembers.setListItemView(addMembersConfiguration.getListItemView());
                        addMembers.setMenu(addMembersConfiguration.getMenu());
                        addMembers.setOptions(addMembersConfiguration.getOptions());
                        addMembers.hideSeparator(addMembersConfiguration.isHideSeparator());
                        addMembers.setSearchPlaceholderText(addMembersConfiguration.getSearchPlaceholderText());
                        addMembers.showBackButton(addMembersConfiguration.isShowBackButton());
                        addMembers.backIcon(addMembersConfiguration.getBackButtonIcon());
                        addMembers.setSelectionMode(addMembersConfiguration.getSelectionMode());
                        addMembers.setOnSelection(addMembersConfiguration.getOnSelection());
                        addMembers.setSearchBoxIcon(addMembersConfiguration.getBackButtonIcon());
                        addMembers.hideSearch(addMembersConfiguration.isHideSearch());
                        addMembers.setTitle(addMembersConfiguration.getTitle());
                        addMembers.setEmptyStateView(addMembersConfiguration.getEmptyStateView());
                        addMembers.setErrorStateView(addMembersConfiguration.getErrorStateView());
                        addMembers.setLoadingStateView(addMembersConfiguration.getLoadingStateView());
                        addMembers.setUsersRequestBuilder(addMembersConfiguration.getUsersRequestBuilder());
                        addMembers.setSearchRequestBuilder(addMembersConfiguration.getUsersSearchRequestBuilder());
                        addMembers.setSearchBoxIcon(addMembersConfiguration.getSearchBoxIcon());
                        addMembers.setAvatarStyle(addMembersConfiguration.getAvatarStyle());
                        addMembers.setListItemStyle(addMembersConfiguration.getListItemStyle());
                        addMembers.setStatusIndicatorStyle(addMembersConfiguration.getStatusIndicatorStyle());
                        addMembers.errorStateText(addMembersConfiguration.getErrorStateText());
                        addMembers.emptyStateText(addMembersConfiguration.getEmptyStateText());
                        addMembers.setStyle(addMembersConfiguration.getStyle());
                        addMembers.setSelectionIcon(addMembersConfiguration.getSelectionIcon());
                        addMembers.setSubmitIcon(addMembersConfiguration.getSubmitIcon());
                        addMembers.setItemClickListener(addMembersConfiguration.getOnItemClickListener());
                        addMembers.setOnError(addMembersConfiguration.getOnError());
                    }
                    alertDialog.setView(addMembers);
                    Dialog dialog = alertDialog.create();
                    addMembers.addOnBackPressListener(dialog::dismiss);
                    dialog.show();
                } else option.getOnClick().onClick(null, group, templateId, option, context);
            } else if (option.getId().equalsIgnoreCase(UIKitConstants.GroupOption.BANNED_GROUP_MEMBERS)) {
                if (option.getOnClick() == null) {
                    CometChatBannedMembers cometChatBannedMembers = new CometChatBannedMembers(context);
                    cometChatBannedMembers.setGroup(group);
                    if (bannedMembersConfiguration != null) {
                        cometChatBannedMembers.setSubtitleView(bannedMembersConfiguration.getSubtitle());
                        cometChatBannedMembers.disableUsersPresence(bannedMembersConfiguration.isDisableUsersPresence());
                        cometChatBannedMembers.setListItemView(bannedMembersConfiguration.getCustomView());
                        cometChatBannedMembers.setMenu(bannedMembersConfiguration.getMenu());
                        cometChatBannedMembers.setOptions(bannedMembersConfiguration.getOptions());
                        cometChatBannedMembers.hideSeparator(bannedMembersConfiguration.isHideSeparator());
                        cometChatBannedMembers.setSearchPlaceholderText(bannedMembersConfiguration.getSearchPlaceholderText());
                        cometChatBannedMembers.showBackButton(bannedMembersConfiguration.isShowBackButton());
                        cometChatBannedMembers.backIcon(bannedMembersConfiguration.getBackButtonIcon());
                        cometChatBannedMembers.setSelectionMode(bannedMembersConfiguration.getSelectionMode());
                        cometChatBannedMembers.setOnSelection(bannedMembersConfiguration.getOnSelection());
                        cometChatBannedMembers.setSearchBoxIcon(bannedMembersConfiguration.getBackButtonIcon());
                        cometChatBannedMembers.hideSearch(bannedMembersConfiguration.isHideSearch());
                        cometChatBannedMembers.setTitle(bannedMembersConfiguration.getTitle());
                        cometChatBannedMembers.setEmptyStateView(bannedMembersConfiguration.getEmptyStateView());
                        cometChatBannedMembers.setErrorStateView(bannedMembersConfiguration.getErrorStateView());
                        cometChatBannedMembers.setLoadingStateView(bannedMembersConfiguration.getLoadingStateView());
                        cometChatBannedMembers.setBannedGroupMembersRequestBuilder(bannedMembersConfiguration.getBannedMembersRequestBuilder());
                        cometChatBannedMembers.setSearchRequestBuilder(bannedMembersConfiguration.getBannedMembersSearchRequestBuilder());
                        cometChatBannedMembers.setSearchBoxIcon(bannedMembersConfiguration.getSearchBoxIcon());
                        cometChatBannedMembers.setAvatarStyle(bannedMembersConfiguration.getAvatarStyle());
                        cometChatBannedMembers.setListItemStyle(bannedMembersConfiguration.getListItemStyle());
                        cometChatBannedMembers.setStatusIndicatorStyle(bannedMembersConfiguration.getStatusIndicatorStyle());
                        cometChatBannedMembers.errorStateText(bannedMembersConfiguration.getErrorStateText());
                        cometChatBannedMembers.emptyStateText(bannedMembersConfiguration.getEmptyStateText());
                        cometChatBannedMembers.setStyle(bannedMembersConfiguration.getStyle());
                        cometChatBannedMembers.setSelectionIcon(bannedMembersConfiguration.getSelectionIcon());
                        cometChatBannedMembers.setSubmitIcon(bannedMembersConfiguration.getSubmitIcon());
                        cometChatBannedMembers.setItemClickListener(bannedMembersConfiguration.getOnItemClickListener());
                        cometChatBannedMembers.setOnError(bannedMembersConfiguration.getOnError());
                    }
                    alertDialog.setView(cometChatBannedMembers);
                    Dialog dialog = alertDialog.create();
                    cometChatBannedMembers.addOnBackPressListener(dialog::dismiss);
                    dialog.show();
                } else option.getOnClick().onClick(null, group, templateId, option, context);
            } else if (option.getId().equalsIgnoreCase(UIKitConstants.GroupOption.LEAVE_GROUP)) {
                if (option.getOnClick() == null) {
                    if (!isOwner) {
                        leaveGroupDialog();
                    } else showTransferOwnerShipDialog();
                } else option.getOnClick().onClick(null, group, templateId, option, context);
            } else if (option.getId().equalsIgnoreCase(UIKitConstants.GroupOption.DELETE_EXIT_GROUP)) {
                if (option.getOnClick() == null) {
                    deleteGroupDialog();
                } else option.getOnClick().onClick(null, group, templateId, option, context);
            } else {
                if (option.getOnClick() != null)
                    option.getOnClick().onClick(null, group, templateId, option, context);
            }
        }
    }

    private void deleteGroupDialog() {
        if (getContext() != null) {

            new CometChatDialog(context, 0, errorStateTextAppearance, theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, errorMessageColor, getResources().getString(R.string.delete_group_text), "", getContext().getString(R.string.delete_and_exit), getResources().getString(R.string.cancel), "", theme.getPalette().getError(), theme.getPalette().getPrimary(), 0, new OnDialogButtonClickListener() {
                @Override
                public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        detailsViewModel.deleteGroup();
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, false);
        }

    }

    private void leaveGroupDialog() {

        if (getContext() != null) {

            new CometChatDialog(context, 0, errorStateTextAppearance, theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, errorMessageColor, getResources().getString(R.string.leave_group_text), "", getContext().getString(R.string.leave_group), getResources().getString(R.string.cancel), "", theme.getPalette().getError(), theme.getPalette().getPrimary(), 0, new OnDialogButtonClickListener() {
                @Override
                public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        detailsViewModel.leaveGroup();
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, false);
        }

    }

    private void viewUserProfile(String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private void hideSectionList(boolean value) {
        if (value) recyclerView.setVisibility(GONE);
        else recyclerView.setVisibility(VISIBLE);
    }

    private void showTransferOwnerShipDialog() {
        if (getContext() != null) {
            new CometChatDialog(context, 0, errorStateTextAppearance, theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, errorMessageColor, getResources().getString(R.string.transfer_ownership), "", getContext().getString(R.string.transfer_ownership_text), getResources().getString(R.string.cancel), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, new OnDialogButtonClickListener() {
                @Override
                public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        alertDialog.dismiss();
                        AlertDialog.Builder presentTransferOwnerShip = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                        CometChatTransferOwnership transferOwnership = new CometChatTransferOwnership(context);
                        transferOwnership.setGroup(group);
                        if (transferOwnershipConfiguration != null) {
                            transferOwnership.setSubtitleView(transferOwnershipConfiguration.getSubtitle());
                            transferOwnership.disableUsersPresence(transferOwnershipConfiguration.isDisableUsersPresence());
                            transferOwnership.setListItemView(transferOwnershipConfiguration.getCustomView());
                            transferOwnership.setMenu(transferOwnershipConfiguration.getMenu());
                            transferOwnership.setOptions(transferOwnershipConfiguration.getOptions());
                            transferOwnership.hideSeparator(transferOwnershipConfiguration.isHideSeparator());
                            transferOwnership.setSearchPlaceholderText(transferOwnershipConfiguration.getSearchPlaceholderText());
                            transferOwnership.showBackButton(transferOwnershipConfiguration.isShowBackButton());
                            transferOwnership.backIcon(transferOwnershipConfiguration.getBackButtonIcon());
                            transferOwnership.setSelectionMode(transferOwnershipConfiguration.getSelectionMode());
                            transferOwnership.setOnSelection(transferOwnershipConfiguration.getOnSelection());
                            transferOwnership.setSearchBoxIcon(transferOwnershipConfiguration.getBackButtonIcon());
                            transferOwnership.hideSearch(transferOwnershipConfiguration.isHideSearch());
                            transferOwnership.setTitle(transferOwnershipConfiguration.getTitle());
                            transferOwnership.setEmptyStateView(transferOwnershipConfiguration.getEmptyStateView());
                            transferOwnership.setErrorStateView(transferOwnershipConfiguration.getErrorStateView());
                            transferOwnership.setLoadingStateView(transferOwnershipConfiguration.getLoadingStateView());
                            transferOwnership.setGroupMembersRequestBuilder(transferOwnershipConfiguration.getMembersRequestBuilder());
                            transferOwnership.setSearchRequestBuilder(transferOwnershipConfiguration.getMembersSearchRequestBuilder());
                            transferOwnership.setAvatarStyle(transferOwnershipConfiguration.getAvatarStyle());
                            transferOwnership.setSearchBoxIcon(transferOwnershipConfiguration.getSearchBoxIcon());
                            transferOwnership.setListItemStyle(transferOwnershipConfiguration.getListItemStyle());
                            transferOwnership.setStatusIndicatorStyle(transferOwnershipConfiguration.getStatusIndicatorStyle());
                            transferOwnership.errorStateText(transferOwnershipConfiguration.getErrorStateText());
                            transferOwnership.emptyStateText(transferOwnershipConfiguration.getEmptyStateText());
                            transferOwnership.setStyle(transferOwnershipConfiguration.getStyle());
                            transferOwnership.setSelectionIcon(transferOwnershipConfiguration.getSelectionIcon());
                            transferOwnership.setSubmitIcon(transferOwnershipConfiguration.getSubmitIcon());
                            transferOwnership.setOnTransferOwnership(transferOwnershipConfiguration.getOnTransferOwnership());
                            transferOwnership.setItemClickListener(transferOwnershipConfiguration.getOnItemClickListener());
                            transferOwnership.setOnError(transferOwnershipConfiguration.getOnError());
                        }
                        presentTransferOwnerShip.setView(transferOwnership);
                        Dialog dialog = presentTransferOwnerShip.create();
                        transferOwnership.addOnBackPressListener(dialog::dismiss);
                        dialog.show();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }
            }, 0, false);
        }
    }

    private void hideError(String action) {
        String error_message;
        if (errorText != null) error_message = errorText;
        else error_message = getContext().getString(R.string.something_went_wrong);
        if (!hideError) {
            if (getContext() != null) {
                new CometChatDialog(context, 0, errorStateTextAppearance, theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, errorMessageColor, error_message, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, new OnDialogButtonClickListener() {
                    @Override
                    public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (user != null && UIKitConstants.UserOption.BLOCK.equals(action)) {
                                detailsViewModel.blockUser();
                            } else if (user != null && UIKitConstants.UserOption.UNBLOCK.equals(action))
                                detailsViewModel.unblockUser();
                            else if (group != null && UIKitConstants.GroupOption.LEAVE_GROUP.equals(action))
                                detailsViewModel.leaveGroup();
                            else if (group != null && UIKitConstants.GroupOption.DELETE_EXIT_GROUP.equals(action))
                                detailsViewModel.deleteGroup();
                            alertDialog.dismiss();
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            alertDialog.dismiss();
                        }
                    }
                }, 0, false);
            }
        }
    }

    /**
     * Shows or hides the close button based on the given value.
     *
     * @param show The value to determine whether to show or hide the close button.
     */
    public void showCloseButton(boolean show) {
        super.showBackButton(show);
    }

    /**
     * Sets the icon for the close button.
     *
     * @param icon The drawable resource ID of the icon to be set.
     */
    public void setCloseButtonIcon(@DrawableRes int icon) {
        super.backIcon(icon);
    }

    /**
     * Disables or enables the user presence display.
     *
     * @param disableUserPresence The value to determine whether to disable or enable user presence display.
     */
    public void disableUserPresence(boolean disableUserPresence) {
        this.disableUserPresence = disableUserPresence;
    }

    /**
     * Sets the icon for protected groups.
     *
     * @param protectedGroupIcon The drawable resource ID of the icon to be set.
     */
    public void setProtectedGroupIcon(int protectedGroupIcon) {
        if (protectedGroupIcon != 0) this.protectedGroupIcon = protectedGroupIcon;
    }

    /**
     * Sets the icon for private groups.
     *
     * @param privateGroupIcon The drawable resource ID of the icon to be set.
     */
    public void setPrivateGroupIcon(int privateGroupIcon) {
        if (privateGroupIcon != 0) this.privateGroupIcon = privateGroupIcon;
    }

    /**
     * Sets the color for the online status indicator.
     *
     * @param onlineStatusColor The color value to be set.
     */
    public void setOnlineStatusColor(int onlineStatusColor) {
        if (onlineStatusColor != 0) this.onlineStatusColor = onlineStatusColor;
    }

    private void throwError(CometChatException e) {
        if (errorCallBack != null) errorCallBack.onError(context, e);
        else e.printStackTrace();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (user != null) {
            setUser(user);
            detailsViewModel.addUserListener();
        } else if (group != null) {
            setGroup(group);
            detailsViewModel.addGroupListener();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (user != null) detailsViewModel.removeUserListener();
        else if (group != null) detailsViewModel.removeGroupListener();
    }

    /**
     * Sets the style for CometChatDetails.
     *
     * @param style The style to be set.
     */
    public void setStyle(DetailsStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
            super.setTitleFont(style.getTitleFont());
            super.setTitleAppearance(style.getTitleAppearance());
            super.setTitleColor(style.getTitleColor());
            setOnlineStatusColor(style.getOnlineStatusColor());
            super.backIconTint(style.getCloseIconTint());
        }
    }

    /**
     * Sets the style for the list item in CometChatDetails.
     *
     * @param listItemStyle The style to be set.
     */
    public void setListItemStyle(ListItemStyle listItemStyle) {
        if (listItemStyle != null) {
            if (listItemStyle.getTitleColor() == 0) {
                listItemStyle.setTitleColor(theme.getPalette().getAccent());
            }
            if (listItemStyle.getTitleAppearance() == 0) {
                listItemStyle.setTitleAppearance(theme.getTypography().getName());
            }
            if (listItemStyle.getSeparatorColor() != 0) {
                cometChatListItem.hideSeparator(false);
            }
            cometChatListItem.setStyle(listItemStyle);
        }
    }

    /**
     * Sets the style for the status indicator in CometChatDetails.
     *
     * @param statusIndicatorStyle The style to be set.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        if (statusIndicatorStyle != null) {
            cometChatListItem.setStatusIndicatorStyle(statusIndicatorStyle);
        }
    }

    /**
     * Sets the style for the avatar in CometChatDetails.
     *
     * @param avatarStyle The style to be set.
     */
    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            if (avatarStyle.getCornerRadius() < 0) {
                avatarStyle.setOuterCornerRadius(100);
            }
            if (avatarStyle.getInnerBackgroundColor() == 0) {
                avatarStyle.setInnerBackgroundColor(theme.getPalette().getAccent700());
            }
            if (avatarStyle.getTextColor() == 0) {
                avatarStyle.setTextColor(theme.getPalette().getAccent900());
            }
            if (avatarStyle.getTextAppearance() == 0) {
                avatarStyle.setTextAppearance(theme.getTypography().getName());
            }
            cometChatListItem.setAvatarStyle(avatarStyle);
        }
    }

    /**
     * Sets the user status data for the CometChatDetails.
     *
     * @param user_ The user object to be set.
     */
    private void setUserStatusData(User user_) {
        if (user_ != null && user_.equals(user)) {
            if (user.isBlockedByMe() || user.isHasBlockedMe())
                cometChatListItem.hideStatusIndicator(true);
            else
                cometChatListItem.hideStatusIndicator(!CometChatConstants.USER_STATUS_ONLINE.equalsIgnoreCase(user_.getStatus()) && !disableUserPresence);

            cometChatListItem.setStatusIndicatorColor(CometChatConstants.USER_STATUS_ONLINE.equalsIgnoreCase(user_.getStatus()) ? onlineStatusColor : 0);
        }
    }

    /**
     * Sets the error callback for the CometChatDetails.
     *
     * @param onError The error callback to be set.
     */
    public void setOnError(OnError onError) {
        this.errorCallBack = onError;
    }

    /**
     * Sets the group members configuration for the CometChatDetails.
     *
     * @param groupMembersConfiguration The configuration to be set.
     */
    public void setGroupMembersConfiguration(GroupMembersConfiguration groupMembersConfiguration) {
        this.groupMembersConfiguration = groupMembersConfiguration;
    }

    /**
     * Sets the add members configuration for the CometChatDetails.
     *
     * @param addMembersConfiguration The configuration to be set.
     */
    public void setAddMembersConfiguration(AddMembersConfiguration addMembersConfiguration) {
        this.addMembersConfiguration = addMembersConfiguration;
    }

    /**
     * Sets the banned members configuration for the CometChatDetails.
     *
     * @param bannedMembersConfiguration The configuration to be set.
     */
    public void setBannedMembersConfiguration(BannedMembersConfiguration bannedMembersConfiguration) {
        this.bannedMembersConfiguration = bannedMembersConfiguration;
    }

    /**
     * Sets the transfer ownership configuration for the CometChatDetails.
     *
     * @param transferOwnershipConfiguration The configuration to be set.
     */
    public void setTransferOwnershipConfiguration(TransferOwnershipConfiguration transferOwnershipConfiguration) {
        this.transferOwnershipConfiguration = transferOwnershipConfiguration;
    }

    @Override
    public void onClick(User user, Group group, String templateId, CometChatDetailsOption option, Context context) {
        performActionOnClick(option, templateId);
    }

}
