package com.cometchat.chatuikit.joinprotectedgroup;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.Interfaces.OnError;

/**
 * Configuration class for customizing the JoinProtectedGroup view.
 */
public class JoinProtectedGroupConfiguration {

    private String title;
    private @DrawableRes
    int closeIcon, joinIcon;
    private String placeHolderText;
    private String description;
    private CometChatJoinProtectedGroup.OnJoinClick onJoinClick;
    private OnError onError;
    private CometChatListBase.OnBackPress onBackPress;
    private JoinProtectedGroupStyle style;

    /**
     * Sets the title for the JoinProtectedGroup view.
     *
     * @param title The title to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the close icon resource ID for the JoinProtectedGroup view.
     *
     * @param closeIcon The close icon resource ID to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setCloseIcon(int closeIcon) {
        this.closeIcon = closeIcon;
        return this;
    }

    /**
     * Sets the join icon resource ID for the JoinProtectedGroup view.
     *
     * @param joinIcon The join icon resource ID to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setJoinIcon(int joinIcon) {
        this.joinIcon = joinIcon;
        return this;
    }

    /**
     * Sets the placeholder text for the JoinProtectedGroup view's edit text.
     *
     * @param placeHolderText The placeholder text to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setPlaceHolderText(String placeHolderText) {
        this.placeHolderText = placeHolderText;
        return this;
    }

    /**
     * Sets the description for the JoinProtectedGroup view.
     *
     * @param description The description to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the click listener for the JoinProtectedGroup view's join button.
     *
     * @param onJoinClick The click listener to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setOnJoinClick(CometChatJoinProtectedGroup.OnJoinClick onJoinClick) {
        this.onJoinClick = onJoinClick;
        return this;
    }

    /**
     * Sets the style for the JoinProtectedGroup view.
     *
     * @param style The style to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setStyle(JoinProtectedGroupStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the error listener for handling errors in the JoinProtectedGroup view.
     *
     * @param onError The error listener to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the back press listener for the JoinProtectedGroup view.
     *
     * @param onBackPress The back press listener to set.
     * @return The JoinProtectedGroupConfiguration instance.
     */
    public JoinProtectedGroupConfiguration setOnBackPress(CometChatListBase.OnBackPress onBackPress) {
        this.onBackPress = onBackPress;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public int getCloseIcon() {
        return closeIcon;
    }

    public int getJoinIcon() {
        return joinIcon;
    }

    public String getPlaceHolderText() {
        return placeHolderText;
    }

    public String getDescription() {
        return description;
    }

    public CometChatJoinProtectedGroup.OnJoinClick getOnJoinClick() {
        return onJoinClick;
    }

    public OnError getOnError() {
        return onError;
    }

    public JoinProtectedGroupStyle getStyle() {
        return style;
    }

    public CometChatListBase.OnBackPress getOnBackPress() {
        return onBackPress;
    }
}
