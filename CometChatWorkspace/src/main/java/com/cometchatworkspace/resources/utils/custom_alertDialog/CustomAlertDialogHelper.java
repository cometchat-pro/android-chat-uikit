package com.cometchatworkspace.resources.utils.custom_alertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.fonts.Font;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.utils.FontUtils;

public class CustomAlertDialogHelper implements View.OnClickListener {
    private static final String TAG = CustomAlertDialogHelper.class.getSimpleName();

    private final OnAlertDialogButtonClickListener onAlertDialogButtonClick;

    private final View view;

    private final AlertDialog alertDialogCreater;

    private final int popupId;
    private final int colorPrimary;
    private TextView textView;
    private FontUtils fontUtils;

    private Palette palette;
    private Typography typography;

    //	cc cometChat;
    public CustomAlertDialogHelper(Context context, String font, int color, String title, View view, String positiveTitle, String neutralTitle,
                                   String negativeTitle, OnAlertDialogButtonClickListener onAlertDialogButton, int popUpId, boolean isCancelable) {
        onAlertDialogButtonClick = onAlertDialogButton;
        // LayoutInflater inflater = (LayoutInflater)
        // context_menu.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = view;
        fontUtils = FontUtils.getInstance(context);
        textView = new TextView(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //todo get color from cc Sdk
//		cometChat = cc.getInstance(context_menu);
        colorPrimary = context.getResources().getColor(R.color.colorPrimaryDark);
//		colorPrimary = (int) cometChat.getCCSetting(new CCSettingMapper(SettingType.UI_SETTINGS, SettingSubType.COLOR_PRIMARY));
        builder.setView(view);
        builder.setCancelable(isCancelable);
        if (!title.equals("")) {
            textView.setText(title);
            if (font != null && !font.isEmpty())
                textView.setTypeface(fontUtils.getTypeFace(font));
            else {
                textView.setTypeface(fontUtils.getTypeFace(FontUtils.robotoRegular));
            }
            if (color != 0)
                textView.setTextColor(color);
            else
                textView.setTextColor(palette.getAccent700());

            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextAppearance(context,typography.getText1());
            builder.setCustomTitle(textView);
        }

        if (!positiveTitle.equals("")) {
            builder.setPositiveButton(positiveTitle, null);
        }
        if (!negativeTitle.equals("")) {
            builder.setNegativeButton(negativeTitle, null);
        }
        if (!neutralTitle.equals("")) {
            builder.setNeutralButton(neutralTitle, null);
        }

        alertDialogCreater = builder.create();
        alertDialogCreater.show();

        this.popupId = popUpId;

        Button positiveButton = alertDialogCreater.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setId(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(colorPrimary);
        positiveButton.setOnClickListener(this);

        Button negativeButton = alertDialogCreater.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setId(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(colorPrimary);
        negativeButton.setOnClickListener(this);

        Button neutralButton = alertDialogCreater.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setId(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setTextColor(colorPrimary);
        neutralButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onAlertDialogButtonClick.onButtonClick(alertDialogCreater, view, v.getId(), popupId);
    }

}