package com.cometchat.chatuikit.calls.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.calls.callbutton.CallButtonsConfiguration;
import com.cometchat.chatuikit.calls.callbutton.CometChatCallButtons;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.button.ButtonStyle;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallingDetailsUtils {

    public static CometChatDetailsTemplate getPrimaryDetailsTemplate(Context context, User user1, Group group1, CallButtonsConfiguration configuration) {
        CometChatDetailsTemplate template = new CometChatDetailsTemplate().setId("callControls").hideSectionSeparator(true);
        if (user1 != null)
            template.setOptions((user, group) -> getPrimaryOptions(context, user, group, configuration));
        else if (group1 != null)
            template.setOptions((user, group) -> getPrimaryOptions(context, user, group, configuration));
        return template;
    }

//    public static CometChatDetailsTemplate getSecondaryDetailsTemplate(Context context, User user1, Group group1) {
//        CometChatDetailsTemplate template = new CometChatDetailsTemplate().setId("callInfo");
//        if (user1 != null)
//            template.hideSectionSeparator(true).setTitle("").setSectionSeparatorColor(CometChatTheme.getInstance(context).getPalette().getAccent100()).setTitleColor(CometChatTheme.getInstance(context).getPalette().getAccent500()).setTitleAppearance(CometChatTheme.getInstance(context).getTypography().getText2()).setOptions((user, group) -> getSecondaryOptions(context, user, group));
//        else if (group1 != null)
//            template.hideSectionSeparator(true).setTitle("").setSectionSeparatorColor(CometChatTheme.getInstance(context).getPalette().getAccent100()).setTitleColor(CometChatTheme.getInstance(context).getPalette().getAccent500()).setTitleAppearance(CometChatTheme.getInstance(context).getTypography().getText2()).setOptions((user, group) -> getSecondaryOptions(context, user, group));
//
//        return template;
//    }

    public static List<CometChatDetailsOption> getPrimaryOptions(Context context, User user, Group group, CallButtonsConfiguration configuration) {
        List<CometChatDetailsOption> options = new ArrayList<>();
        if (user != null) {
            options.add(new CometChatDetailsOption((context1, user1, group1) -> {
                CometChatCallButtons cometChatCallButtons = new CometChatCallButtons(context1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.rightMargin = Utils.convertDpToPx(context1, 16);
                cometChatCallButtons.setLayoutParams(params);
                cometChatCallButtons.getVideoCallButton().hideButtonBackground(true);
                cometChatCallButtons.getVoiceCallButton().hideButtonBackground(true);
                cometChatCallButtons.setVideoCallIcon(R.drawable.video_icon);
                cometChatCallButtons.setVoiceCallIcon(R.drawable.call_icon);
                cometChatCallButtons.setVideoButtonText(context1.getString(R.string.video_call));
                cometChatCallButtons.setVoiceButtonText(context1.getString(R.string.call));
                Drawable drawable = context1.getDrawable(R.drawable.action_sheet_background);
                drawable.setTint(CometChatTheme.getInstance(context1).getPalette().getAccent100());
                cometChatCallButtons.setButtonStyle(new ButtonStyle().setBackground(drawable).setButtonIconTint(CometChatTheme.getInstance(context1).getPalette().getPrimary()).setButtonTextAppearance(CometChatTheme.getInstance(context1).getTypography().getSubtitle2()).setButtonTextColor(CometChatTheme.getInstance(context1).getPalette().getPrimary()));
                cometChatCallButtons.setUser(user);
                setConfiguration(cometChatCallButtons, configuration);
                return cometChatCallButtons;
            }, null));
        } else if (group != null) {
            options.add(new CometChatDetailsOption((context1, user1, group1) -> {
                CometChatCallButtons cometChatCallButtons = new CometChatCallButtons(context1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                cometChatCallButtons.setLayoutParams(params);
                cometChatCallButtons.getVideoCallButton().hideButtonBackground(true);
                cometChatCallButtons.getVoiceCallButton().hideButtonBackground(true);
                cometChatCallButtons.setVideoCallIcon(R.drawable.video_icon);
                cometChatCallButtons.setVideoButtonText(context1.getString(R.string.video_call));
                Drawable drawable = context1.getDrawable(R.drawable.action_sheet_background);
                drawable.setTint(CometChatTheme.getInstance(context1).getPalette().getAccent100());
                cometChatCallButtons.setButtonStyle(new ButtonStyle().setBackground(drawable).setButtonIconTint(CometChatTheme.getInstance(context1).getPalette().getPrimary()).setButtonTextAppearance(CometChatTheme.getInstance(context1).getTypography().getSubtitle2()).setButtonTextColor(CometChatTheme.getInstance(context1).getPalette().getPrimary()));
                cometChatCallButtons.setGroup(group);
                setConfiguration(cometChatCallButtons, configuration);
                return cometChatCallButtons;
            }, null));
        }
        return options;
    }

    private static void setConfiguration(CometChatCallButtons cometChatCallButtons, CallButtonsConfiguration configuration) {
        if (configuration != null) {
            cometChatCallButtons.setButtonStyle(configuration.getButtonStyle());
            cometChatCallButtons.setStyle(configuration.getStyle());
            cometChatCallButtons.setOnVideoCallClick(configuration.getOnVideoCallClick());
            cometChatCallButtons.setOnVoiceCallClick(configuration.getOnVoiceCallClick());
            cometChatCallButtons.setOnError(configuration.getOnError());
            cometChatCallButtons.setVoiceCallIcon(configuration.getVoiceCallIcon());
            cometChatCallButtons.setVideoCallIcon(configuration.getVideoCallIcon());
            cometChatCallButtons.hideButtonText(configuration.isHideButtonText());
            cometChatCallButtons.hideButtonIcon(configuration.isHideButtonIcon());
            cometChatCallButtons.setVideoButtonText(configuration.getVideoCallText());
            cometChatCallButtons.setVoiceButtonText(configuration.getVoiceCallText());
        }
    }

//    public static List<CometChatDetailsOption> getSecondaryOptions(Context context, User user, Group group) {
//        CometChatDetailsOption cometChatDetailsOption = new CometChatDetailsOption((context1, user1, group1) -> {
//            CometChatCallLogs cometChatCallLogs = new CometChatCallLogs(context);
//            cometChatCallLogs.setUser(user1);
//            cometChatCallLogs.setGroup(group1);
//            return cometChatCallLogs;
//        }, null);
//        return new ArrayList<>(Arrays.asList(cometChatDetailsOption));
//    }

    public static List<CometChatDetailsTemplate> getDefaultDetailsTemplate(Context context, User user, Group group, CallButtonsConfiguration callButtonsConfiguration) {
        return new ArrayList<>(Arrays.asList(getPrimaryDetailsTemplate(context, user, group, callButtonsConfiguration)));
    }

}
