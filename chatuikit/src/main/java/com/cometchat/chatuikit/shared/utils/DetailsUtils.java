package com.cometchat.chatuikit.shared.utils;

import android.content.Context;

import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsUtils {

    public static CometChatDetailsTemplate getPrimaryDetailsTemplate(Context context, User user1, Group group1) {

        CometChatDetailsTemplate template = new CometChatDetailsTemplate().setId(UIKitConstants.DetailsTemplate.PRIMARY).hideSectionSeparator(true);

        if (user1 != null)
            template.setOptions((user, group) -> getDetailsOptions(context, UIKitConstants.DetailsTemplate.PRIMARY, user, group));
        else if (group1 != null)
            template.setOptions((user, group) -> getDetailsOptions(context, UIKitConstants.DetailsTemplate.PRIMARY, user, group));

        return template;
    }

    public static CometChatDetailsTemplate getSecondaryDetailsTemplate(Context context, User user1, Group group1) {
        CometChatDetailsTemplate template = new CometChatDetailsTemplate().setId(UIKitConstants.DetailsTemplate.SECONDARY);
        if (user1 != null)
            template.hideSectionSeparator(true).setOptions((user, group) -> getDetailsOptions(context, UIKitConstants.DetailsTemplate.SECONDARY, user, group));
        else if (group1 != null)
            template.hideSectionSeparator(false).setTitle(context.getResources().getString(R.string.more_option)).setSectionSeparatorColor(Palette.getInstance(context).getAccent100()).setTitleColor(Palette.getInstance(context).getAccent500()).setTitleAppearance(Typography.getInstance().getText3()).setOptions((user, group) -> getDetailsOptions(context, UIKitConstants.DetailsTemplate.SECONDARY, user, group));

        return template;
    }

    public static List<CometChatDetailsTemplate> getDefaultDetailsTemplate(Context context, User user, Group group) {
        return new ArrayList<>(Arrays.asList(getPrimaryDetailsTemplate(context, user, group), getSecondaryDetailsTemplate(context, user, group)));
    }

    public static List<CometChatOption> getDefaultGroupMemberOptions(Context context, GroupMember groupMember, Group group, OnClick click) {
        List<CometChatOption> optionsList = new ArrayList<>();
        if (group != null) {
            if (CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group.getScope())) {
                if (CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(groupMember.getScope())) {
                    optionsList.addAll(getDefaultOptions(context, click));
                }
            } else if (CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group.getScope())) {
                if (!group.getOwner().equalsIgnoreCase(groupMember.getUid()) && !CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(groupMember.getScope())) {
                    optionsList.addAll(getDefaultOptions(context, click));
                } else if (group.getOwner() != null && group.getOwner().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()) && !groupMember.getUid().equals(group.getOwner())) {
                    optionsList.addAll(getDefaultOptions(context, click));
                }
            }
        }
        return optionsList;
    }

    public static List<CometChatOption> getDefaultBannedMemberOptions(Context context, Group group, OnClick onClick) {
        List<CometChatOption> options = new ArrayList<>();
        if (group != null) {
            if (CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group.getScope()) || CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group.getScope()) || group.getOwner() != null && group.getOwner().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid())) {
                options.add(new CometChatOption(UIKitConstants.GroupMemberOption.UNBAN, null, R.drawable.ic_close_24dp, Palette.getInstance(context).getError(), onClick));
            }
        }
        return options;
    }

    public static GroupMember userToGroupMember(User user, boolean isScopeUpdate, String newScope) {
        GroupMember groupMember;
        if (isScopeUpdate) groupMember = new GroupMember(user.getUid(), newScope);
        else groupMember = new GroupMember(user.getUid(), CometChatConstants.SCOPE_PARTICIPANT);

        groupMember.setAvatar(user.getAvatar());
        groupMember.setName(user.getName());
        groupMember.setStatus(user.getStatus());
        return groupMember;
    }

    private static List<CometChatOption> getDefaultOptions(Context context, OnClick click) {
        List<CometChatOption> optionsList = new ArrayList<>();
        optionsList.add(new CometChatOption(UIKitConstants.GroupMemberOption.KICK, null, R.drawable.ic_delete_conversation, Palette.getInstance(context).getError(), click));
        optionsList.add(new CometChatOption(UIKitConstants.GroupMemberOption.BAN, null, R.drawable.ic_block, context.getResources().getColor(R.color.yellow), click));
        return optionsList;
    }

    public static List<CometChatDetailsOption> getDetailsOptions(Context context, String templateId, User user, Group group) {
        List<CometChatDetailsOption> options = new ArrayList<>();
        if (templateId.equalsIgnoreCase(UIKitConstants.DetailsTemplate.PRIMARY)) {
            if (user != null) {
                if (user.getLink() != null && !user.getLink().isEmpty())
                    options.add(new CometChatDetailsOption(UIKitConstants.UserOption.VIEW_PROFILE, context.getString(R.string.view_profile), 0, 0, Palette.getInstance(context).getPrimary(), null, Typography.getInstance().getName(), 0, Palette.getInstance(context).getAccent200(), null));
            } else if (group != null) {
                List<CometChatDetailsOption> optionsTemp = new ArrayList<>(Arrays.asList(new CometChatDetailsOption(UIKitConstants.GroupOption.VIEW_GROUP_MEMBERS, context.getString(R.string.view_members), 0, R.drawable.ic_next, Palette.getInstance(context).getAccent(), null, Typography.getInstance().getName(), 0, Palette.getInstance(context).getAccent200(), null), new CometChatDetailsOption(UIKitConstants.GroupOption.ADD_GROUP_MEMBERS, context.getString(R.string.add_members), 0, R.drawable.ic_next, Palette.getInstance(context).getAccent(), null, Typography.getInstance().getName(), 0, Palette.getInstance(context).getAccent200(), null), new CometChatDetailsOption(UIKitConstants.GroupOption.BANNED_GROUP_MEMBERS, context.getString(R.string.banned_members), 0, R.drawable.ic_next, Palette.getInstance(context).getAccent(), null, Typography.getInstance().getName(), 0, Palette.getInstance(context).getAccent200(), null)));
                if (group.getScope() != null && group.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_PARTICIPANT)) {
                    options.add(optionsTemp.get(0));
                } else if (group.getScope() != null && group.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_MODERATOR)) {
                    options.add(optionsTemp.get(0));
                } else if (group.getScope() != null && group.getScope().equalsIgnoreCase(CometChatConstants.SCOPE_ADMIN) || (CometChatUIKit.getLoggedInUser() != null && CometChatUIKit.getLoggedInUser().getUid().equals(group.getOwner()))) {
                    options.add(optionsTemp.get(0));
                    options.add(optionsTemp.get(1));
                    options.add(optionsTemp.get(2));
                }
            }
            return options;
        } else if (templateId.equalsIgnoreCase(UIKitConstants.DetailsTemplate.SECONDARY)) {
            if (user != null) {
                if (user.isBlockedByMe())
                    options.add(new CometChatDetailsOption(UIKitConstants.UserOption.UNBLOCK, context.getString(R.string.unblock_user), 0, 0, Palette.getInstance(context).getPrimary(), null, Typography.getInstance().getName(), 0, 0, null));
                else
                    options.add(new CometChatDetailsOption(UIKitConstants.UserOption.BLOCK, context.getString(R.string.block_user), 0, 0, Palette.getInstance(context).getError(), null, Typography.getInstance().getName(), 0, 0, null));
            } else if (group != null) {
                if (group != null) {
                    options = new ArrayList<>(Arrays.asList(new CometChatDetailsOption(UIKitConstants.GroupOption.LEAVE_GROUP, context.getString(R.string.leave_group), 0, 0, Palette.getInstance(context).getError(), null, Typography.getInstance().getName(), 0, 0, null), new CometChatDetailsOption(UIKitConstants.GroupOption.DELETE_EXIT_GROUP, context.getString(R.string.delete_and_exit), 0, 0, Palette.getInstance(context).getError(), null, Typography.getInstance().getName(), 0, 0, null)));
                    if (!CometChatUIKit.getLoggedInUser().getUid().equals(group.getOwner()) || !CometChatConstants.SCOPE_ADMIN.equalsIgnoreCase(group.getScope())) {
                        options.remove(1);
                    }
                }
            }
            return options;
        }
        return options;
    }

}
