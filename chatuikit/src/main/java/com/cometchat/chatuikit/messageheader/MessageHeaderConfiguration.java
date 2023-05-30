package com.cometchat.chatuikit.messageheader;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

/**
 * The MessageHeaderConfiguration class is responsible for configuring the appearance and behavior of the message header in a chat UI.
 * <p>
 * It provides methods to customize various aspects of the message header such as subtitle, menu, avatar style, status indicator style, icons, and more.
 * <p>
 * Use the provided setter methods to configure the desired settings, and retrieve the values using the getter methods.
 */
public class MessageHeaderConfiguration {

    private Function3<Context, User, Group, View> subtitle;
    private Function3<Context, User, Group, View> menu;
    private boolean disableUsersPresence;
    private @DrawableRes
    int protectedGroupIcon;
    private @DrawableRes
    int privateGroupIcon;
    private MessageHeaderStyle style;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private View backIconView;
    private boolean hideBackIcon;
    private View listItemView;

    /**
     * Sets the subtitle view for the message header.
     * The subtitle view is a function that accepts a context, user, and group, and returns a View.
     *
     * @param subtitle The function that provides the subtitle view.
     */
    public MessageHeaderConfiguration setSubtitle(Function3<Context, User, Group, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Sets the style for the avatar.
     *
     * @param avatarStyle The style to apply to the avatar.
     * @see AvatarStyle
     */
    public MessageHeaderConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the style for the status indicator.
     *
     * @param statusIndicatorStyle The style to apply to the status indicator.
     * @see StatusIndicatorStyle
     */
    public MessageHeaderConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the menu view for the message header.
     * The menu view is a function that accepts a context, user, and group, and returns a View.
     *
     * @param menu The function that provides the menu view.
     */
    public MessageHeaderConfiguration setMenu(Function3<Context, User, Group, View> menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Disables or enables the presence indicator for users in the header.
     *
     * @param disableUsersPresence {@code true} to disable the users' presence indicator, {@code false} to enable it.
     */
    public MessageHeaderConfiguration disableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        return this;
    }

    /**
     * Sets the protected group icon for the header.
     *
     * @param protectedGroupIcon The protected group icon resource.
     */
    public MessageHeaderConfiguration setProtectedGroupIcon(int protectedGroupIcon) {
        this.protectedGroupIcon = protectedGroupIcon;
        return this;
    }

    /**
     * Sets the private group icon for the header.
     *
     * @param privateGroupIcon The private group icon resource.
     */
    public MessageHeaderConfiguration setPrivateGroupIcon(int privateGroupIcon) {
        this.privateGroupIcon = privateGroupIcon;
        return this;
    }

    /**
     * Sets the style for the header.
     *
     * @param style The message header style to be applied.
     */
    public MessageHeaderConfiguration setStyle(MessageHeaderStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the back icon for the header.
     *
     * @param backIconView The back icon view.
     */
    public MessageHeaderConfiguration setBackIconView(View backIconView) {
        this.backIconView = backIconView;
        return this;
    }

    /**
     * Hides or shows the back icon in the header.
     *
     * @param hideBackIcon {@code true} to hide the back icon, {@code false} to show it.
     */
    public MessageHeaderConfiguration hideBackIcon(boolean hideBackIcon) {
        this.hideBackIcon = hideBackIcon;
        return this;
    }

    /**
     * Sets the list item view for the header.
     *
     * @param listItemView The list item view.
     */
    public MessageHeaderConfiguration setListItemView(View listItemView) {
        this.listItemView = listItemView;
        return this;
    }

    public Function3<Context, User, Group, View> getSubtitle() {
        return subtitle;
    }

    public Function3<Context, User, Group, View> getMenu() {
        return menu;
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

    public MessageHeaderStyle getStyle() {
        return style;
    }

    public View getBackIconView() {
        return backIconView;
    }

    public boolean isHideBackIcon() {
        return hideBackIcon;
    }

    public View getListItemView() {
        return listItemView;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public StatusIndicatorStyle getStatusIndicatorStyle() {
        return statusIndicatorStyle;
    }
}
