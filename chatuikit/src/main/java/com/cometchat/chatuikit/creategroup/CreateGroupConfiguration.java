package com.cometchat.chatuikit.creategroup;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.Interfaces.OnError;

/**
 * Configuration class for customizing the Create Group functionality.
 */
public class CreateGroupConfiguration {
    private String title;
    private @DrawableRes
    int closeIcon;
    private @DrawableRes
    int createIcon;
    private String namePlaceHolderText;
    private String passwordPlaceHolderText;
    private CometChatCreateGroup.OnCreateGroup onCreateGroup;
    private OnError onError;
    private CometChatListBase.OnBackPress onBackPress;
    private CreateGroupStyle style;


    /**
     * Sets the title for the Create Group view.
     *
     * @param title The title to be set.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the close icon resource for the Create Group view.
     *
     * @param closeIcon The resource ID of the close icon to be set.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setCloseIcon(int closeIcon) {
        this.closeIcon = closeIcon;
        return this;
    }

    /**
     * Sets the create icon resource for the Create Group view.
     *
     * @param createIcon The resource ID of the create icon to be set.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setCreateIcon(int createIcon) {
        this.createIcon = createIcon;
        return this;
    }

    /**
     * Sets the placeholder text for the group name EditText field.
     *
     * @param namePlaceHolderText The placeholder text to be set.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setNamePlaceHolderText(String namePlaceHolderText) {
        this.namePlaceHolderText = namePlaceHolderText;
        return this;
    }

    /**
     * Sets the placeholder text for the group password EditText field.
     *
     * @param passwordPlaceHolderText The placeholder text to be set.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setPasswordPlaceHolderText(String passwordPlaceHolderText) {
        this.passwordPlaceHolderText = passwordPlaceHolderText;
        return this;
    }

    /**
     * Sets the listener for the create group action.
     *
     * @param onCreateGroup The listener to be set for the create group action.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setOnCreateGroup(CometChatCreateGroup.OnCreateGroup onCreateGroup) {
        this.onCreateGroup = onCreateGroup;
        return this;
    }

    /**
     * Sets the listener for error handling.
     *
     * @param onError The listener to be set for error handling.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the style for the Create Group view.
     *
     * @param style The style to be applied to the Create Group view.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setStyle(CreateGroupStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the listener for the back press action.
     *
     * @param onBackPress The listener to be set for the back press action.
     * @return The updated CreateGroupConfiguration object.
     */
    public CreateGroupConfiguration setOnBackPress(CometChatListBase.OnBackPress onBackPress) {
        this.onBackPress = onBackPress;
        return this;
    }

    public CreateGroupStyle getStyle() {
        return style;
    }

    public String getTitle() {
        return title;
    }

    public int getCloseIcon() {
        return closeIcon;
    }

    public int getCreateIcon() {
        return createIcon;
    }

    public String getNamePlaceHolderText() {
        return namePlaceHolderText;
    }

    public String getPasswordPlaceHolderText() {
        return passwordPlaceHolderText;
    }

    public CometChatCreateGroup.OnCreateGroup getOnCreateGroup() {
        return onCreateGroup;
    }

    public OnError getOnError() {
        return onError;
    }

    public CometChatListBase.OnBackPress getOnBackPress() {
        return onBackPress;
    }
}
