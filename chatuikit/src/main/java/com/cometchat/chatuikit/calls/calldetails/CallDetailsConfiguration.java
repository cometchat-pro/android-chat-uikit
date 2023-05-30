package com.cometchat.chatuikit.calls.calldetails;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.calls.callbutton.CallButtonsConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.List;

 class CallDetailsConfiguration {

    private boolean showCloseButton = true;
    private String title;
    private @DrawableRes
    int closeButtonIcon;
    private boolean hideProfile;
    private Function3<Context, User, Group, View> subtitleView;
    private Function3<Context, User, Group, View> customProfileView;
    private Function3<Context, User, Group, List<CometChatDetailsTemplate>> data;
    private View menu;
    private CallDetailsStyle style;
    private ListItemStyle listItemStyle;
    private AvatarStyle avatarStyle;
    private OnError onError;
    private CallButtonsConfiguration configuration;

    public CallDetailsConfiguration showCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
        return this;
    }

    public CallDetailsConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    public CallDetailsConfiguration setCloseButtonIcon(int closeButtonIcon) {
        this.closeButtonIcon = closeButtonIcon;
        return this;
    }

    public CallDetailsConfiguration hideProfile(boolean hideProfile) {
        this.hideProfile = hideProfile;
        return this;
    }

    public CallDetailsConfiguration setSubtitleView(Function3<Context, User, Group, View> subtitleView) {
        this.subtitleView = subtitleView;
        return this;
    }

    public CallDetailsConfiguration setCustomProfileView(Function3<Context, User, Group, View> customProfileView) {
        this.customProfileView = customProfileView;
        return this;
    }

    public CallDetailsConfiguration setData(Function3<Context, User, Group, List<CometChatDetailsTemplate>> data) {
        this.data = data;
        return this;
    }

    public CallDetailsConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    public CallDetailsConfiguration setStyle(CallDetailsStyle style) {
        this.style = style;
        return this;
    }

    public CallDetailsConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    public CallDetailsConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    public CallDetailsConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    public CallDetailsConfiguration setCallButtonsConfiguration(CallButtonsConfiguration configuration) {
        this.configuration = configuration;
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

    public View getMenu() {
        return menu;
    }

    public CallDetailsStyle getStyle() {
        return style;
    }

    public ListItemStyle getListItemStyle() {
        return listItemStyle;
    }

    public String getTitle() {
        return title;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public CallButtonsConfiguration getCallButtonsConfiguration() {
        return configuration;
    }

    public OnError getOnError() {
        return onError;
    }
}
