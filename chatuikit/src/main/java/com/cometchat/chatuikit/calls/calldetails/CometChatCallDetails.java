package com.cometchat.chatuikit.calls.calldetails;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.calls.callbutton.CallButtonsConfiguration;
import com.cometchat.chatuikit.calls.utils.CallUtils;
import com.cometchat.chatuikit.calls.utils.CallingDetailsUtils;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;

class CometChatCallDetails extends CometChatListBase {

    private static final String TAG = CometChatCallDetails.class.getName();
    private Context context;
    private CometChatListItem cometChatListItem;
    private User user;
    private Group group;
    private Function3<Context, User, Group, List<CometChatDetailsTemplate>> templates;
    private @DrawableRes
    int protectedGroupIcon, privateGroupIcon;
    private CometChatTheme theme;
    private RecyclerView recyclerView;
    private TemplateAdapter templateAdapter;
    private LinearLayout profileLayout;
    private OnError errorCallBack;
    private CallDetailsViewModel callDetailsViewModel;
    private CallButtonsConfiguration callButtonsConfiguration;

    public CometChatCallDetails(Context context) {
        super(context);
        if (!isInEditMode()) initViewComponent(context, null, -1);
    }

    public CometChatCallDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) initViewComponent(context, attrs, -1);
    }

    public CometChatCallDetails(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        callDetailsViewModel = new CallDetailsViewModel();
        protectedGroupIcon = R.drawable.ic_password_group;
        privateGroupIcon = R.drawable.ic_private_group;

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
        callDetailsViewModel = ViewModelProviders.of((FragmentActivity) context).get(callDetailsViewModel.getClass());
        callDetailsViewModel.getUserMutableLiveData().observe((AppCompatActivity) context, this::setUser);
        cometChatListItem.hideSeparator(true);
        super.setTitle(getContext().getString(R.string.call_details));
        super.setTitleAppearance(theme.getTypography().getHeading());
        super.setTitleColor(titleColor);
        super.backIcon(backButtonIcon);
        super.backIconTint(backIconTint);
        super.showBackButton(showBackButton);
        super.hideSearch(true);
        if (theme.getPalette().getGradientBackground() != null)
            setBackground(theme.getPalette().getGradientBackground());
        else setBackgroundColor(backGroundColor);
        setListItemStyle(new ListItemStyle());
        setAvatarStyle(new AvatarStyle());
        super.addListView(view);
    }

    public void setSubtitleView(Function3<Context, User, Group, View> subtitleView) {
        if (subtitleView != null) {
            cometChatListItem.setSubtitleView(subtitleView.apply(context, user, group));
        }
    }

    public void setCustomProfileView(Function3<Context, User, Group, View> profileView) {
        if (profileView != null) {
            Utils.handleView(profileLayout, profileView.apply(context, user, group), false);
        }
    }

    public void hideProfile(boolean value) {
        profileLayout.setVisibility(value ? GONE : VISIBLE);
    }

    private void setUser(User user_) {
        if (user_ != null) {
            this.user = user_;
            templateAdapter.setUser(user);
            cometChatListItem.setAvatar(user.getAvatar(), user.getName());
            cometChatListItem.setTitle(user.getName());
            cometChatListItem.hideStatusIndicator(true);
            callDetailsViewModel.setUser(user);
            setData(templates);
        }
    }

    public void setCall(BaseMessage call) {
        if (call != null) {
            if (call instanceof Call) {
                setUser(CallUtils.getCallingUser((Call) call));
            } else if (call instanceof CustomMessage && UIKitConstants.MessageType.MEETING.equalsIgnoreCase(call.getType())) {
                setGroup(CallUtils.getCallingGroup((CustomMessage) call));
            } else {
                Log.e(TAG, "setCall: Invalid baseMessageType for call that to be shown in Call Details");
            }
        } else {
            Log.e(TAG, "setCall: baseMessage is null");
        }
    }

    private void setGroup(Group group_) {
        this.group = group_;
        templateAdapter.setGroup(group);
        cometChatListItem.setAvatar(group.getIcon(), group.getName());
        cometChatListItem.setStatusIndicatorIcon(CometChatConstants.GROUP_TYPE_PASSWORD.equalsIgnoreCase(group_.getGroupType()) ? protectedGroupIcon : privateGroupIcon);
        cometChatListItem.hideStatusIndicator(true);
        cometChatListItem.setTitle(group.getName());
        callDetailsViewModel.setGroup(group);
        setData(templates);
    }

    public void setData(Function3<Context, User, Group, List<CometChatDetailsTemplate>> templates) {
        this.templates = templates;
        if (templates == null) {
            templateAdapter.setDetailTemplate(CallingDetailsUtils.getDefaultDetailsTemplate(context, user, group, callButtonsConfiguration));
        } else {
            templateAdapter.setDetailTemplate(getData(user, group));
        }
    }

    private List<CometChatDetailsTemplate> getData(User user, Group group) {
        List<CometChatDetailsTemplate> templates = new ArrayList<>();
        if (this.templates != null) templates = this.templates.apply(context, user, group);
        return templates;
    }

    private void hideSectionList(boolean value) {
        if (value) recyclerView.setVisibility(GONE);
        else recyclerView.setVisibility(VISIBLE);
    }

    public void showCloseButton(boolean show) {
        super.showBackButton(show);
    }

    public void setCloseButtonIcon(@DrawableRes int icon) {
        super.backIcon(icon);
    }

    public void setProtectedGroupIcon(int protectedGroupIcon) {
        if (protectedGroupIcon != 0) this.protectedGroupIcon = protectedGroupIcon;
    }

    public void setPrivateGroupIcon(int privateGroupIcon) {
        if (privateGroupIcon != 0) this.privateGroupIcon = privateGroupIcon;
    }

    private void throwError(CometChatException e) {
        if (errorCallBack != null) errorCallBack.onError(context, e);
        else e.printStackTrace();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        callDetailsViewModel.addUserListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        callDetailsViewModel.removeListener();
    }

    public void setStyle(CallDetailsStyle style) {
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
            super.backIconTint(style.getCloseIconTint());
        }
    }

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

    public void setCallButtonsConfiguration(CallButtonsConfiguration callButtonsConfiguration) {
        if (callButtonsConfiguration != null){
            this.callButtonsConfiguration = callButtonsConfiguration;
            setData(templates);
        }
    }

    public void setOnError(OnError onError) {
        this.errorCallBack = onError;
    }

    public CometChatListItem getCometChatListItem() {
        return cometChatListItem;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TemplateAdapter getTemplateAdapter() {
        return templateAdapter;
    }
}
