package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.StatusIndicatorConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CometChatDataItem extends MaterialCardView {

    private User user;
    private Group group;
    private GroupMember groupMember;
    private LinearLayout tail_view;
    private View tail = null;
    private Palette palette;
    private Typography typography;
    private FontUtils fontUtils;
    private Context context;
    private CometChatAvatar itemAvatar;
    private TextView itemTitle, itemSubTitle, itemSeparator;
    private CometChatStatusIndicator statusIndicator;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private boolean avatarHidden, titleHidden, subTitleHidden, userPresenceHidden, separatorHidden;
    private int titleColor, subTitleColor, backGroundColor, avatarBackgroundColor, avatarTextColor, separatorColor;

    public CometChatDataItem(Context context) {
        super(context);
        initViewComponent(context, null, -1);

    }

    public CometChatDataItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);

    }

    public CometChatDataItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        View view = View.inflate(context, R.layout.cometchat_data_item, null);
        itemTitle = view.findViewById(R.id.item_title);
        itemSubTitle = view.findViewById(R.id.item_subTitle);
        statusIndicator = view.findViewById(R.id.item_presence);
        itemSeparator = view.findViewById(R.id.tv_Separator);
        itemAvatar = view.findViewById(R.id.item_avatar);
        tail_view = view.findViewById(R.id.tail_view);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DataItem,
                0, 0);
        avatarHidden = a.getBoolean(R.styleable
                .DataItem_hideAvatar, false);
        titleHidden = a.getBoolean(R.styleable
                .DataItem_hideTitle, false);
        titleColor = a.getColor(R.styleable
                .DataItem_titleColor, palette.getAccent());
        subTitleHidden = a.getBoolean(R.styleable
                .DataItem_hideSubTitle, false);
        subTitleColor = a.getColor(R.styleable
                .DataItem_subTitleColor, palette.getAccent600());
        userPresenceHidden = a.getBoolean(R.styleable
                .DataItem_hideStatusIndicator, false);
        backGroundColor = a.getColor(R.styleable.DataItem_backgroundColor, getResources().getColor(android.R.color.transparent));
        avatarBackgroundColor = a.getColor(R.styleable.DataItem_avatarBackgroundColor, palette.getAccent700());
        avatarTextColor = a.getColor(R.styleable.DataItem_avatarTextColor, palette.getAccent900());
        separatorHidden = a.getBoolean(R.styleable.DataItem_hideSeparator, true);
        separatorColor = a.getColor(R.styleable.DataItem_separatorColor, palette.getAccent100());

        hideAvatar(avatarHidden);
        hideTitle(titleHidden);
        titleColor(titleColor);
        titleTextAppearance(typography.getName());
        subTitleTextAppearance(typography.getSubtitle1());
        subTitleColor(subTitleColor);
        hideSubTitle(subTitleHidden);
        hideStatusIndicator(userPresenceHidden);
        setBackgroundColor(backGroundColor);
        setAvatarBackgroundColor(avatarBackgroundColor);
        setAvatarTextColor(avatarTextColor);
        setAvatarTextAppearance(typography.getName());
        hideSeparator(separatorHidden);
        separatorColor(separatorColor);
        addView(view);

    }

    public void setTailView(View view) {
        if (view != null) {
            this.tail = view;
            tail_view.removeAllViews();
            tail_view.addView(view);
        }

    }

    public void hideSeparator(boolean separatorHidden) {
        if (separatorHidden)
            itemSeparator.setVisibility(GONE);
        else
            itemSeparator.setVisibility(VISIBLE);

        this.separatorHidden = separatorHidden;
    }

    private void setAvatarTextAppearance(int appearance) {
        if (appearance != 0)
            getAvatar().setTextAppearance(appearance);
    }

    private void setAvatarTextColor(int color) {
        if (color != 0)
            getAvatar().setTextColor(color);
    }

    private void setAvatarBackgroundColor(int color) {
        if (color != 0)
            getAvatar().setBackgroundColor(color);
    }

    public void hideStatusIndicator(boolean isHidden) {
        if (isHidden)
            statusIndicator.setVisibility(View.GONE);
        else
            statusIndicator.setVisibility(View.VISIBLE);
    }

    public void hideSubTitle(boolean subTitleHidden) {
        if (subTitleHidden)
            itemSubTitle.setVisibility(View.GONE);
        else
            itemSubTitle.setVisibility(View.VISIBLE);

    }

    public void titleFont(String fonts) {
        if (fonts != null && !fonts.isEmpty())
            itemTitle.setTypeface(fontUtils.getTypeFace(fonts));

    }

    public void subTitleFont(String fonts) {
        if (fonts != null && !fonts.isEmpty())
            itemSubTitle.setTypeface(fontUtils.getTypeFace(fonts));

    }

    public void subTitleColor(int color) {
        if (color != 0)
            itemSubTitle.setTextColor(color);

    }

    public void subTitleTextAppearance(int appearance) {
        if (itemSubTitle != null && appearance != 0)
            itemSubTitle.setTextAppearance(context, appearance);
    }

    public void titleTextAppearance(int appearance) {
        if (itemTitle != null && appearance != 0)
            itemTitle.setTextAppearance(context, appearance);
    }

    public void titleColor(int color) {
        if (color != 0)
            itemTitle.setTextColor(color);
    }

    public void hideTitle(boolean isHidden) {
        if (isHidden)
            itemTitle.setVisibility(View.GONE);
        else
            itemTitle.setVisibility(View.VISIBLE);
    }

    public void hideAvatar(boolean isHidden) {
        if (isHidden)
            itemAvatar.setVisibility(View.GONE);
        else
            itemAvatar.setVisibility(View.VISIBLE);
    }

    public void title(String titleStr) {
        if (titleStr != null)
            itemTitle.setText(titleStr);
    }

    public void subTitle(String subTitleStr) {
        if (subTitleStr != null)
            itemSubTitle.setText(subTitleStr);
    }

    public void separatorColor(int color) {
        if (color != 0)
            itemSeparator.setBackgroundColor(color);
    }

    public void user(User user) {
        this.user = user;
        title(user.getName());
        statusIndicator(user.getStatus());
        hideSubTitle(true);
//        subTitle(user.getStatus());
//        hideSeparator(separatorHidden);

        if (user.getAvatar() == null || user.getAvatar().isEmpty())
            getAvatar().setInitials(user.getName());
        else
            getAvatar().setAvatar(user.getAvatar());

    }

    public void group(Group group) {
        this.group = group;
        title(group.getName());
        String subtitle;
        if (group.getMembersCount() > 1)
            subtitle = group.getMembersCount() + " " + getResources().getString(R.string.members).toLowerCase();
        else
            subtitle = group.getMembersCount() + " " + getResources().getString(R.string.member).toLowerCase();

        subTitle(subtitle);
        statusIndicator(group.getGroupType());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPx(context, 1));
        layoutParams.addRule(RelativeLayout.END_OF, R.id.item_avatar);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.leftMargin = Utils.convertDpToPx(context, 16);
        itemSeparator.setLayoutParams(layoutParams);
        hideSeparator(separatorHidden);
        if (group.getIcon() == null || group.getIcon().isEmpty())
            getAvatar().setInitials(group.getName());
        else
            getAvatar().setAvatar(group.getIcon());

    }

    public void groupMember(GroupMember groupMember) {
        this.groupMember = groupMember;
        if (loggedInUser.getUid().equalsIgnoreCase(groupMember.getUid()))
            title(context.getResources().getString(R.string.you));
        else
            title(groupMember.getName());

        statusIndicator(groupMember.getStatus());
        hideSeparator(separatorHidden);
        hideSubTitle(true);
        if (groupMember.getAvatar() == null || groupMember.getAvatar().isEmpty())
            getAvatar().setInitials(groupMember.getName());
        else
            getAvatar().setAvatar(groupMember.getAvatar());

    }

    public void inputData(InputData inputData) {
        if (user != null) {
            if (inputData.getSubTitle(user) instanceof String) {
                subTitle(inputData.getSubTitle(user) + "");
                hideSubTitle(false);
            } else if (inputData.getSubTitle(user) instanceof Long) {
                subTitle(Utils.getDate(context, (Long) inputData.getSubTitle(user)));
            } else if (inputData.getSubTitle(user) instanceof Integer) {
                subTitle(inputData.getSubTitle(user) + "");
            }
            hideSubTitle(false);

        } else if (group != null) {
            if (inputData.getSubTitle(group) instanceof String) {
                subTitle(inputData.getSubTitle(group) + "");
            } else if (inputData.getSubTitle(group) instanceof Long) {
                subTitle(Utils.getDate(context, (Long) inputData.getSubTitle(group)));
            } else if (inputData.getSubTitle(group) instanceof Integer) {
                subTitle(inputData.getSubTitle(group) + "");
            }
            hideSubTitle(false);

        } else if (groupMember != null) {
            if (inputData.getSubTitle(groupMember) instanceof String) {
                subTitle(inputData.getSubTitle(groupMember) + "");
            } else if (inputData.getSubTitle(groupMember) instanceof Long) {
                long g = (Long) inputData.getSubTitle(groupMember);
                subTitle(Utils.getLastMessageDate(context, (Long) inputData.getSubTitle(groupMember)));
            } else if (inputData.getSubTitle(groupMember) instanceof Integer) {
                subTitle(inputData.getSubTitle(groupMember) + "");
            }
            hideSubTitle(false);
        }

        hideTitle(!inputData.isTitle());

        hideAvatar(!inputData.isThumbnail());

        hideStatusIndicator(!inputData.isStatus());

    }

    public void style(Style style) {

        if (style.getBorder() > 0) {
            setStrokeWidth(style.getBorder());
        }
        if (style.getCornerRadius() > 0) {
            setRadius(style.getCornerRadius());
        }
        if (style.getSubTitleColor() != 0) {
            titleColor(style.getTitleColor());
        }
        if (style.getSubTitleColor() != 0) {
            subTitleColor(style.getSubTitleColor());
        }
        if (style.getTitleFont() != null && !style.getTitleFont().isEmpty()) {
            titleFont(style.getTitleFont());
        }
        if (style.getSubTitleFont() != null && !style.getSubTitleFont().isEmpty()) {
            subTitleFont(style.getSubTitleFont());
        }
        if (style.getBackground() != 0) {
            setBackgroundColor(style.getBackground());
        }
    }

    public void statusIndicator(String status) {
        if (user != null || groupMember != null) {
            statusIndicatorDimensions(12, 12);
            if (status.equalsIgnoreCase(CometChatConstants.USER_STATUS_ONLINE)) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.ONLINE);
                hideStatusIndicator(false);
            } else if (status.equalsIgnoreCase(CometChatConstants.USER_STATUS_OFFLINE)) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.OFFLINE);
                hideStatusIndicator(true);
            } else if (status.equals(UIKitConstants.SELECTED)) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.SELECTED);
                statusIndicatorDimensions(20, 20);
                hideStatusIndicator(false);
            } else
                statusIndicator.status(status);
        } else if (group != null) {
            hideStatusIndicator(false);
            statusIndicatorDimensions(14, 14);
            if (status.equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.PASSWORD);
            } else if (status.equals(CometChatConstants.GROUP_TYPE_PRIVATE)) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.PRIVATE);
            } else {
                hideStatusIndicator(true);
            }
        }
    }

    private void statusIndicatorDimensions(int weight, int height) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.convertDpToPx(context, weight), Utils.convertDpToPx(context, height));
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_avatar);
        layoutParams.addRule(RelativeLayout.ALIGN_END, R.id.item_avatar);
        layoutParams.topMargin = -15;
        statusIndicator.setLayoutParams(layoutParams);

    }

    public CometChatAvatar getAvatar() {
        return itemAvatar;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof AvatarConfiguration) {
            AvatarConfiguration avatarConfiguration = (AvatarConfiguration) configuration;
            if (avatarConfiguration.getBorderWidth() > -1)
                itemAvatar.setBorderWidth(avatarConfiguration.getBorderWidth());
            if (avatarConfiguration.getCornerRadius() > -1)
                itemAvatar.setCornerRadius(avatarConfiguration.getCornerRadius());
            if (avatarConfiguration.getOuterViewWidth() > -1)
                itemAvatar.setOuterViewSpacing(avatarConfiguration.getOuterViewWidth());
            if (avatarConfiguration.getOuterViewColor() > -1)
                itemAvatar.setOuterViewColor(avatarConfiguration.getOuterViewColor());
            if (avatarConfiguration.getWidth() > -1 && avatarConfiguration.getHeight() > -1) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                        ((int) avatarConfiguration.getWidth(), (int) avatarConfiguration.getHeight());
                itemAvatar.setLayoutParams(params);
            }
        } else if (configuration instanceof StatusIndicatorConfiguration) {
            StatusIndicatorConfiguration statusIndicatorConfiguration =
                    (StatusIndicatorConfiguration) configuration;
            if (statusIndicatorConfiguration.getBorderWidth() > -1)
                statusIndicatorConfiguration.setBorderWidth(statusIndicatorConfiguration.getBorderWidth());
            if (statusIndicatorConfiguration.getColor() > -1 && statusIndicatorConfiguration.getStatus() != null)
                statusIndicatorConfiguration.setColor(statusIndicatorConfiguration.getColor(),
                        statusIndicatorConfiguration.getStatus());
            if (statusIndicatorConfiguration.getWidth() > -1 && statusIndicatorConfiguration.getHeight() > -1) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (int) statusIndicatorConfiguration.getWidth(), (int) statusIndicatorConfiguration.getHeight());
            }
        }
    }

    public void setConfigurations(List<CometChatConfigurations> configurations) {
        if(configurations!=null && !configurations.isEmpty()) {
            for (CometChatConfigurations cometChatConfigurations : configurations) {
                setConfiguration(cometChatConfigurations);
            }
        }
    }

    public void setAvatar(String avatar, String name) {
        if (itemAvatar != null)
            itemAvatar.setAvatar(avatar, name);
    }
}
