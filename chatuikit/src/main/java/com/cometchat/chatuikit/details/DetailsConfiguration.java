package com.cometchat.chatuikit.details;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.addmembers.AddMembersConfiguration;
import com.cometchat.chatuikit.bannedmembers.BannedMembersConfiguration;
import com.cometchat.chatuikit.groupmembers.GroupMembersConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.chatuikit.transferownership.TransferOwnershipConfiguration;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.List;

/**
 * A configuration class for customizing the CometChatDetails component.
 */
public class DetailsConfiguration {
    private boolean showCloseButton = true;
    private @DrawableRes
    int closeButtonIcon;
    private boolean hideProfile;
    private Function3<Context, User, Group, View> subtitleView;
    private Function3<Context, User, Group, View> customProfileView;
    private Function3<Context, User, Group, List<CometChatDetailsTemplate>> data;
    private View menu;
    private boolean disableUsersPresence;
    private @DrawableRes
    int protectedGroupIcon;
    private @DrawableRes
    int privateGroupIcon;
    private DetailsStyle style;
    private ListItemStyle listItemStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private AvatarStyle avatarStyle;
    private OnError onError;
    private GroupMembersConfiguration groupMembersConfiguration;
    private BannedMembersConfiguration bannedMembersConfiguration;
    private AddMembersConfiguration addMembersConfiguration;
    private TransferOwnershipConfiguration transferOwnershipConfiguration;

    /**
     * Sets the error callback for the CometChatDetails.
     *
     * @param onError The error callback to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets whether to show the close button in the CometChatDetails.
     *
     * @param showCloseButton Whether to show the close button.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration showCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
        return this;
    }

    /**
     * Sets the icon for the close button in the CometChatDetails.
     *
     * @param closeButtonIcon The icon for the close button.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setCloseButtonIcon(int closeButtonIcon) {
        this.closeButtonIcon = closeButtonIcon;
        return this;
    }

    /**
     * Sets whether to hide the profile section in the CometChatDetails.
     *
     * @param hideProfile Whether to hide the profile section.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration hideProfile(boolean hideProfile) {
        this.hideProfile = hideProfile;
        return this;
    }

    /**
     * Sets the subtitle view for the CometChatDetails.
     *
     * @param subtitleView The subtitle view to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setSubtitleView(Function3<Context, User, Group, View> subtitleView) {
        this.subtitleView = subtitleView;
        return this;
    }

    /**
     * Sets the custom profile view for the CometChatDetails.
     *
     * @param customProfileView The custom profile view to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setCustomProfileView(Function3<Context, User, Group, View> customProfileView) {
        this.customProfileView = customProfileView;
        return this;
    }

    /**
     * Sets the data function for generating the list of CometChatDetailsTemplate.
     *
     * @param data The data function to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setData(Function3<Context, User, Group, List<CometChatDetailsTemplate>> data) {
        this.data = data;
        return this;
    }

    /**
     * Sets whether to disable user presence in the CometChatDetails.
     *
     * @param disableUsersPresence Whether to disable user presence.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration disableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        return this;
    }

    /**
     * Sets the icon for protected group in the CometChatDetails.
     *
     * @param protectedGroupIcon The icon for protected group.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setProtectedGroupIcon(int protectedGroupIcon) {
        this.protectedGroupIcon = protectedGroupIcon;
        return this;
    }

    /**
     * Sets the icon for private group in the CometChatDetails.
     *
     * @param privateGroupIcon The icon for private group.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setPrivateGroupIcon(int privateGroupIcon) {
        this.privateGroupIcon = privateGroupIcon;
        return this;
    }

    /**
     * Sets the style for the CometChatDetails.
     *
     * @param style The style to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setStyle(DetailsStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the ListItemStyle for the CometChatDetails.
     *
     * @param listItemStyle The ListItemStyle to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    /**
     * Sets the StatusIndicatorStyle for the CometChatDetails.
     *
     * @param statusIndicatorStyle The StatusIndicatorStyle to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the AvatarStyle for the CometChatDetails.
     *
     * @param avatarStyle The AvatarStyle to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the menu view for the CometChatDetails.
     *
     * @param menu The menu view to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Sets the GroupMembersConfiguration for the CometChatDetails.
     *
     * @param groupMembersConfiguration The GroupMembersConfiguration to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setGroupMembersConfiguration(GroupMembersConfiguration groupMembersConfiguration) {
        this.groupMembersConfiguration = groupMembersConfiguration;
        return this;
    }

    /**
     * Sets the BannedMembersConfiguration for the CometChatDetails.
     *
     * @param bannedMembersConfiguration The BannedMembersConfiguration to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setBannedMembersConfiguration(BannedMembersConfiguration bannedMembersConfiguration) {
        this.bannedMembersConfiguration = bannedMembersConfiguration;
        return this;
    }

    /**
     * Sets the AddMembersConfiguration for the CometChatDetails.
     *
     * @param addMembersConfiguration The AddMembersConfiguration to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setAddMembersConfiguration(AddMembersConfiguration addMembersConfiguration) {
        this.addMembersConfiguration = addMembersConfiguration;
        return this;
    }

    /**
     * Sets the TransferOwnershipConfiguration for the CometChatDetails.
     *
     * @param transferOwnershipConfiguration The TransferOwnershipConfiguration to be set.
     * @return The DetailsConfiguration instance.
     */
    public DetailsConfiguration setTransferOwnershipConfiguration(TransferOwnershipConfiguration transferOwnershipConfiguration) {
        this.transferOwnershipConfiguration = transferOwnershipConfiguration;
        return this;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public int getCloseButtonIcon() {
        return closeButtonIcon;
    }

    public boolean isHideProfile() {
        return hideProfile;
    }

    public Function3<Context, User, Group, View> getSubtitleView() {
        return subtitleView;
    }

    public Function3<Context, User, Group, View> getCustomProfileView() {
        return customProfileView;
    }

    public Function3<Context, User, Group, List<CometChatDetailsTemplate>> getData() {
        return data;
    }

    public boolean isDisableUsersPresence() {
        return disableUsersPresence;
    }

    public int getProtectedGroupIcon() {
        return protectedGroupIcon;
    }

    public int getPrivateGroupIcon() {
        return privateGroupIcon;
    }

    public DetailsStyle getStyle() {
        return style;
    }

    public ListItemStyle getListItemStyle() {
        return listItemStyle;
    }

    public StatusIndicatorStyle getStatusIndicatorStyle() {
        return statusIndicatorStyle;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public View getMenu() {
        return menu;
    }

    public GroupMembersConfiguration getGroupMembersConfiguration() {
        return groupMembersConfiguration;
    }

    public BannedMembersConfiguration getBannedMembersConfiguration() {
        return bannedMembersConfiguration;
    }

    public AddMembersConfiguration getAddMembersConfiguration() {
        return addMembersConfiguration;
    }

    public TransferOwnershipConfiguration getTransferOwnershipConfiguration() {
        return transferOwnershipConfiguration;
    }

    public OnError getOnError() {
        return onError;
    }
}
