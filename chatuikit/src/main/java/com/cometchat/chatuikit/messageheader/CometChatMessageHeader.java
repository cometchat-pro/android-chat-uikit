package com.cometchat.chatuikit.messageheader;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.SubtitleView;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;
import java.util.Map;

/**
 * CometChatMessageHeader is a custom message header view for a chat application.
 * It extends the MaterialCardView class to provide a styled card-like header.
 * The header includes various customizable elements such as subtitles, menus, avatars, and status indicators.
 * <br>
 * Example :<br>
 * <pre>
 * {@code
 * CometChatMessageHeader cometChatMessageHeader = view.findViewById(R.id.comet_chat_message_header);
 * if(user != null) cometChatMessageHeader.setUser(user);
 * else if(group != null) cometChatMessageHeader.setGroup(group);
 * }
 * </pre>
 */
public class CometChatMessageHeader extends MaterialCardView {
    private View view;
    private Context context;
    private Palette palette;
    private Typography typography;
    private MessageHeaderViewModel messageHeaderViewModel;
    private Toolbar toolbar;
    private CometChatListItem listItem;
    private LinearLayout layout;
    private User user;
    private Group group;
    private Function3<Context, User, Group, View> subtitle;
    private Function3<Context, User, Group, View> menu;
    private boolean disableTyping;
    private boolean disableUsersPresence;
    private @DrawableRes
    int protectedGroupIcon;
    private @DrawableRes
    int privateGroupIcon;
    private @ColorInt
    int onlineStatusColor;
    ImageView backIcon;
    ImageView detailIcon;
    private SubtitleView subtitleView;
    private @ColorInt
    int subtitleColor;
    private @ColorInt
    int typingIndicatorColor;

    /**
     * Constructs a new CometChatMessageHeader with a given context.
     *
     * @param context The context in which the header is created.
     */
    public CometChatMessageHeader(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Constructs a new CometChatMessageHeader with a given context and attribute set.
     *
     * @param context The context in which the header is created.
     * @param attrs   The attribute set for the header.
     */
    public CometChatMessageHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Constructs a new CometChatMessageHeader with a given context, attribute set, and default style.
     *
     * @param context      The context in which the header is created.
     * @param attrs        The attribute set for the header.
     * @param defStyleAttr The default style attribute for the header.
     */
    public CometChatMessageHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Initializes the CometChatMessageHeader with the provided context and attribute set.
     *
     * @param context The context in which the header is created.
     * @param attrs   The attribute set for the header.
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        onlineStatusColor = palette.getSuccess();
        subtitleColor = palette.getAccent600();
        typingIndicatorColor = palette.getPrimary();
        privateGroupIcon = R.drawable.ic_private_group;
        protectedGroupIcon = R.drawable.ic_password_group;
        view = LayoutInflater.from(context).inflate(R.layout.cometchat_toolbar, null);
        toolbar = view.findViewById(R.id.toolbar);
        listItem = view.findViewById(R.id.headerDataItem);
        layout = view.findViewById(R.id.back_action);
        listItem.hideSeparator(true);
        messageHeaderViewModel = new MessageHeaderViewModel();
        messageHeaderViewModel = ViewModelProviders.of((FragmentActivity) context).get(messageHeaderViewModel.getClass());
        messageHeaderViewModel.getMemberCount().observe((AppCompatActivity) context, this::setMembersCount);
        messageHeaderViewModel.getUserPresenceStatus().observe((AppCompatActivity) context, this::showUserPresence);
        messageHeaderViewModel.getUpdateGroup().observe((AppCompatActivity) context, this::setGroup);
        messageHeaderViewModel.getTyping().observe((AppCompatActivity) context, this::setTypingIndicator);
        createBackIcon();
        setDefaultSubtitle();
        setAvatarStyle(new AvatarStyle());
        setListItemStyle(new ListItemStyle());
        addView(view);
    }

    private void setDefaultSubtitle() {
        subtitleView = new SubtitleView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        subtitleView.setLayoutParams(params);
        subtitleView.hideReceipt(true);
        subtitleView.showHelperText(false);
        subtitleView.setTypingIndicatorColor(palette.getPrimary());
        subtitleView.setSubtitleTextColor(palette.getAccent600());
        subtitleView.setSubtitleTextAppearance(typography.getSubtitle2());
        listItem.setSubtitleView(subtitleView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setUser(user);
        setGroup(group);
        messageHeaderViewModel.addListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        messageHeaderViewModel.removeListeners();
    }

    private void createBackIcon() {
        backIcon = new ImageView(context);
        backIcon.setImageResource(R.drawable.ic_back);
        backIcon.setImageTintList(ColorStateList.valueOf(palette.getPrimary()));
        backIcon.setOnClickListener(view -> {
            ((Activity) context).onBackPressed();
        });
        setBackIcon(backIcon);
    }

    /**
     * Sets the subtitle view for the message header.
     * The subtitle view is a function that accepts a context, user, and group, and returns a View.
     *
     * @param subtitleView The function that provides the subtitle view.
     */
    public void setSubtitleView(Function3<Context, User, Group, View> subtitleView) {
        if (subtitleView != null) {
            this.subtitle = subtitleView;
            listItem.setSubtitleView(getSubtitle(user, group));
        }
    }

    /**
     * Gets the subtitle view for the given user and group.
     *
     * @param user  The user associated with the header.
     * @param group The group associated with the header.
     * @return The subtitle view.
     */
    public View getSubtitle(User user, Group group) {
        if (subtitle != null) {
            return subtitle.apply(context, user, group);
        }
        return null;
    }

    /**
     * Sets the menu view for the message header.
     * The menu view is a function that accepts a context, user, and group, and returns a View.
     *
     * @param menu The function that provides the menu view.
     */
    public void setMenu(Function3<Context, User, Group, View> menu) {
        if (menu != null) {
            this.menu = menu;
            listItem.setTailView(getMenu(user, group));
        }
    }

    /**
     * Gets the menu view for the given user and group.
     *
     * @param user  The user associated with the header.
     * @param group The group associated with the header.
     * @return The menu view.
     */
    public View getMenu(User user, Group group) {
        if (menu != null) {
            return menu.apply(context, user, group);
        }
        return null;
    }

    /**
     * Disables or enables the typing indicator in the header.
     *
     * @param disable {@code true} to disable the typing indicator, {@code false} to enable it.
     */
    public void disableTyping(boolean disable) {
        this.disableTyping = disable;
    }

    /**
     * Disables or enables the presence indicator for users in the header.
     *
     * @param presence {@code true} to disable the users' presence indicator, {@code false} to enable it.
     */
    public void disableUsersPresence(boolean presence) {
        this.disableUsersPresence = presence;
        listItem.hideStatusIndicator(presence);
    }

    /**
     * Hides or shows the status indicator in the header.
     *
     * @param hide {@code true} to hide the status indicator, {@code false} to show it.
     */
    public void hideStatusIndicator(boolean hide) {
        listItem.hideStatusIndicator(hide);
    }

    /**
     * Sets the color for the online status indicator in the header.
     *
     * @param onlineStatusColor The color for the online status indicator.
     */
    public void setOnlineStatusColor(int onlineStatusColor) {
        if (onlineStatusColor != 0) this.onlineStatusColor = onlineStatusColor;
    }

    /**
     * Sets the protected group icon for the header.
     *
     * @param icon The protected group icon resource.
     */
    public void setProtectedGroupIcon(@DrawableRes int icon) {
        if (icon != 0) {
            this.protectedGroupIcon = icon;
        }
    }

    /**
     * Sets the text color for the subtitle in the header.
     *
     * @param subtitleColor The color for the subtitle text.
     */
    public void setSubtitleTextColor(int subtitleColor) {
        if (subtitleColor != 0) this.subtitleColor = subtitleColor;
    }

    /**
     * Sets the color for the typing indicator in the header.
     *
     * @param typingIndicatorColor The color for the typing indicator.
     */
    public void setTypingIndicatorColor(int typingIndicatorColor) {
        if (typingIndicatorColor != 0) this.typingIndicatorColor = typingIndicatorColor;
    }

    /**
     * Sets the private group icon for the header.
     *
     * @param icon The private group icon resource.
     */
    public void setPrivateGroupIcon(@DrawableRes int icon) {
        if (icon != 0) {
            this.privateGroupIcon = icon;
        }
    }

    /**
     * Sets the avatar style for the header.
     *
     * @param avatarStyle The avatar style to be applied.
     */
    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            if (avatarStyle.getCornerRadius() < 0) {
                avatarStyle.setOuterCornerRadius(100);
            }
            if (avatarStyle.getInnerBackgroundColor() == 0) {
                avatarStyle.setInnerBackgroundColor(palette.getAccent600());
            }
            if (avatarStyle.getTextColor() == 0) {
                avatarStyle.setTextColor(palette.getAccent900());
            }
            if (avatarStyle.getTextAppearance() == 0) {
                avatarStyle.setTextAppearance(typography.getName());
            }
            listItem.setAvatarStyle(avatarStyle);
        }

    }

    /**
     * Sets the status indicator style for the header.
     *
     * @param style The status indicator style to be applied.
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        listItem.setStatusIndicatorStyle(style);
    }

    /**
     * Sets the list item style for the header.
     *
     * @param style The list item style to be applied.
     */
    public void setListItemStyle(ListItemStyle style) {
        if (style != null) {
            if (style.getTitleColor() == 0) {
                style.setTitleColor(palette.getAccent());
            }
            if (style.getTitleAppearance() == 0) {
                style.setTitleAppearance(typography.getName());
            }
            if (style.getSeparatorColor() == 0) {
                style.setSeparatorColor(palette.getAccent100());
            }
            listItem.setStyle(style);
        }
    }

    /**
     * Sets the style for the header.
     *
     * @param style The message header style to be applied.
     */
    public void setStyle(MessageHeaderStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
            if (style.getBackIconTint() != 0)
                backIcon.setImageTintList(ColorStateList.valueOf(style.getOnlineStatusColor()));
            setSubtitleTextColor(style.getSubtitleTextColor());
            subtitleView.setSubtitleTextAppearance(style.getSubtitleTextAppearance());
            subtitleView.setSubtitleTextFont(style.getSubtitleTextFont());
            setTypingIndicatorColor(style.getTypingIndicatorTextColor());
            subtitleView.setTypingIndicatorTextAppearance(style.getTypingIndicatorTextAppearance());
            subtitleView.setTypingIndicatorFont(style.getSubtitleTextFont());
            setOnlineStatusColor(style.getOnlineStatusColor());
        }
    }

    /**
     * Sets the user associated with the message.
     *
     * @param user The user associated with the message.
     */
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            messageHeaderViewModel.setUser(user);
            setListItem(user);
        }
    }

    private void setListItem(User user) {
        listItem.setAvatar(user.getAvatar(), user.getName());
        listItem.setTitle(user.getName());
        listItem.setStatusIndicatorColor(onlineStatusColor);
        showUserPresence(user.getStatus());
    }

    private void setListItem(Group group) {
        listItem.setAvatar(group.getIcon(), group.getName());
        listItem.setTitle(group.getName());
        setMembersCount(group.getMembersCount());
        if (group.getGroupType().equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
            listItem.setStatusIndicatorIcon(protectedGroupIcon);
        } else if (group.getGroupType().equals(CometChatConstants.GROUP_TYPE_PRIVATE)) {
            listItem.setStatusIndicatorIcon(privateGroupIcon);
        } else {
            listItem.hideStatusIndicator(true);
        }
    }

    /**
     * Sets the group associated with the message.
     *
     * @param group The group associated with the message.
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            messageHeaderViewModel.setGroup(group);
            setListItem(group);
        }
    }

    /**
     * Sets the back icon for the header.
     *
     * @param view The back icon view.
     */
    public void setBackIcon(View view) {
        Utils.handleView(layout, view, false);
    }

    private void setMembersCount(int count) {
        subtitleView.setSubtitleText(count > 1 ? count + " " + context.getString(R.string.members) : count + " " + context.getString(R.string.member));
    }

    private void showUserPresence(String string) {
        listItem.hideStatusIndicator(!(CometChatConstants.USER_STATUS_ONLINE.equalsIgnoreCase(string) && !disableUsersPresence));
        subtitleView.setSubtitleTextColor(CometChatConstants.USER_STATUS_ONLINE.equalsIgnoreCase(string) ? palette.getPrimary() : subtitleColor);
        subtitleView.setSubtitleText(string);
    }

    private void setTypingIndicator(HashMap<TypingIndicator, Boolean> typingHashMap) {
        if (!disableTyping) {
            if (!typingHashMap.isEmpty()) {
                for (Map.Entry map : typingHashMap.entrySet()) {
                    boolean isTyping = (boolean) map.getValue();
                    TypingIndicator typingIndicator = (TypingIndicator) map.getKey();
                    subtitleView.showTypingIndicator(isTyping);
                    subtitleView.setTypingIndicatorColor(typingIndicatorColor);
                    subtitleView.setTypingIndicatorText(typingIndicator.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER) ? context.getString(R.string.is_typing) : typingIndicator.getSender().getName() + " " + context.getString(R.string.is_typing));
                }
            }
        }
    }

    /**
     * Sets the list item view for the header.
     *
     * @param view The list item view.
     */
    public void setListItemView(View view) {
        Utils.handleView(toolbar, view, false);
    }

    /**
     * Hides or shows the back icon in the header.
     *
     * @param hide {@code true} to hide the back icon, {@code false} to show it.
     */
    public void hideBackIcon(boolean hide) {
        layout.setVisibility(hide ? GONE : VISIBLE);
    }
}
