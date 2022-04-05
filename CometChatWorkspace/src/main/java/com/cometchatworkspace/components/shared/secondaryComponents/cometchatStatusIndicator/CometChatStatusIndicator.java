package com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.StringDef;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchatworkspace.R;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

/**
 * Purpose - StatusIndicator is a subclass of View and it is used as component to display status
 * indicator of user. If helps to know whether user is online or offline.
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 16th January 2020
 *
 */

@BindingMethods(value ={@BindingMethod(type = CometChatStatusIndicator.class, attribute = "app:user_status", method = "setUserStatus")})
public class CometChatStatusIndicator extends MaterialCardView {

    @STATUS
    String status;

    /*
     * Constants to define shape
     * */
    @StringDef({STATUS.ONLINE, STATUS.OFFLINE, STATUS.PASSWORD, STATUS.PRIVATE, STATUS.PUBLIC})
    public @interface STATUS {
        String OFFLINE = CometChatConstants.USER_STATUS_OFFLINE;
        String ONLINE = CometChatConstants.USER_STATUS_ONLINE;
        String PRIVATE = CometChatConstants.GROUP_TYPE_PRIVATE;
        String PASSWORD = CometChatConstants.GROUP_TYPE_PASSWORD;
        String PUBLIC = CometChatConstants.GROUP_TYPE_PUBLIC;
    }

    public CometChatStatusIndicator(Context context) {
        super(context);
        init();
    }

    public CometChatStatusIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
        init();
    }

    public CometChatStatusIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(attrs);
        init();
    }

    public void borderColor(@ColorInt int color) {
        if (color!=0) {
            setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        setStrokeWidth(width);
    }

    public void cornerRadius(int radius) {
        setRadius(radius);
    }

    public void color(@ColorInt int color) {
        if (color!=0 && status.equals(STATUS.ONLINE))
            setCardBackgroundColor(color);
        else
            setVisibility(View.GONE);
    }

    public void status(@STATUS String userStatus) {
         status = userStatus;
         setValues();
    }

    private void setValues() {
        if(status.equalsIgnoreCase(STATUS.ONLINE))
            setCardBackgroundColor(getResources().getColor(R.color.online_green));
        else {
            setCardBackgroundColor(getResources().getColor(R.color.offline));
        }
        if (Utils.isDarkMode(getContext())) {
            setStrokeColor(getContext().getColor(R.color.darkModeBackground));
        } else {
            setStrokeColor(getContext().getColor(R.color.textColorWhite));
        }
        invalidate();
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StatusIndicator, 0, 0);
        String userStatus = a.getString(R.styleable.StatusIndicator_user_status);
        if (userStatus == null) {
            status = STATUS.OFFLINE;
        } else {
            if (getContext().getString(R.string.online).equalsIgnoreCase(userStatus)) {
                status = STATUS.ONLINE ;
            } else {
                status = STATUS.OFFLINE;
            }
        }
    }

    protected void init() {
        if (status.equals(STATUS.ONLINE)) {
            setVisibility(View.VISIBLE);
            setCardBackgroundColor(getResources().getColor(R.color.online_green));
        } else if(status.equals(STATUS.OFFLINE)){
            setVisibility(View.GONE);
            setCardBackgroundColor(getResources().getColor(R.color.offline));
        }
//        else if()
    }
}
