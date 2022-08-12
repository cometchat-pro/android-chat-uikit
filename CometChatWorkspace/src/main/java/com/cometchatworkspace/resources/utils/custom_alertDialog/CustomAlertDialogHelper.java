package com.cometchatworkspace.resources.utils.custom_alertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;

public class CustomAlertDialogHelper implements View.OnClickListener {
    private static final String TAG = CustomAlertDialogHelper.class.getSimpleName();

    private final OnAlertDialogButtonClickListener onAlertDialogButtonClick;

    private final View view;
    private final View infoView;
    private AlertDialog alertDialogCreater;

    private final int popupId;
    private final int colorPrimary;
    private final TextView alertMessage;
    private TextView alertTitle;
    private final FontUtils fontUtils;

    private final Palette palette;
    private final Typography typography;
    private final boolean isCancelable;
    private int positiveButtonColor = 0, negativeButtonColor = 0, neutralButtonColor = 0, textColor = 0;
    private final String alertText;
    private String alertTitleText;
    private final String positiveButtonTitle;
    private final String negativeButtonTitle;
    private final String neutralButtonTitle;
    private final String font;

    //	cc cometChat;
    public CustomAlertDialogHelper(Context context, String font, int color, String alertText, View view, String positiveTitle, String neutralTitle,
                                   String negativeTitle, OnAlertDialogButtonClickListener onAlertDialogButton, int popUpId, boolean isCancelable) {
        this.onAlertDialogButtonClick = onAlertDialogButton;
        this.positiveButtonColor = 0;
        this.neutralButtonColor = 0;
        this.negativeButtonColor = 0;
        this.isCancelable = isCancelable;
        this.view = view;
        this.alertText = alertText;
        this.font = font;
        this.textColor = color;
        this.positiveButtonTitle = positiveTitle;
        this.negativeButtonTitle = negativeTitle;
        this.neutralButtonTitle = neutralTitle;
        this.popupId = popUpId;
        fontUtils = FontUtils.getInstance(context);
        infoView = View.inflate(context, R.layout.dialog_custom_view, null);
        alertMessage = infoView.findViewById(R.id.alert_message);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();


        //todo get color from cc Sdk
//		cometChat = cc.getInstance(context_menu);
        colorPrimary = context.getResources().getColor(R.color.colorPrimaryDark);
//		colorPrimary = (int) cometChat.getCCSetting(new CCSettingMapper(SettingType.UI_SETTINGS, SettingSubType.COLOR_PRIMARY));
        setAlertDialog(context);
    }

    public CustomAlertDialogHelper(Context context, String font, int color, String alertText, String alertTitleText, View view, String positiveTitle, String neutralTitle,
                                   String negativeTitle, int positiveButtonColor, int negativeButtonColor, int neutralButtonColor, OnAlertDialogButtonClickListener onAlertDialogButton, int popUpId, boolean isCancelable) {

        this.onAlertDialogButtonClick = onAlertDialogButton;
        this.positiveButtonColor = positiveButtonColor;
        this.neutralButtonColor = neutralButtonColor;
        this.negativeButtonColor = negativeButtonColor;
        this.isCancelable = isCancelable;
        this.view = view;
        this.alertText = alertText;
        this.alertTitleText = alertTitleText;
        this.font = font;
        this.textColor = color;
        this.positiveButtonTitle = positiveTitle;
        this.negativeButtonTitle = negativeTitle;
        this.neutralButtonTitle = neutralTitle;
        this.popupId = popUpId;
        fontUtils = FontUtils.getInstance(context);
        infoView = View.inflate(context, R.layout.dialog_custom_view, null);
        alertMessage = infoView.findViewById(R.id.alert_message);
        alertTitle = infoView.findViewById(R.id.alert_title);
//        textView = new TextView(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();

        //todo get color from cc Sdk
//		cometChat = cc.getInstance(context_menu);
        colorPrimary = context.getResources().getColor(R.color.colorPrimaryDark);
//		colorPrimary = (int) cometChat.getCCSetting(new CCSettingMapper(SettingType.UI_SETTINGS, SettingSubType.COLOR_PRIMARY));
        setAlertDialog(context);
    }

    @Override
    public void onClick(View v) {
        onAlertDialogButtonClick.onButtonClick(alertDialogCreater, view, v.getId(), popupId);
    }


    private void setAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        builder.setCancelable(isCancelable);

        if (alertTitle != null && alertTitleText != null && !alertTitleText.isEmpty()) {
            alertTitle.setVisibility(View.VISIBLE);
            alertTitle.setText(alertTitleText);
            alertTitle.setTextColor(palette.getAccent());
            alertTitle.setTextAppearance(context, typography.getName());
        }
        if (!alertText.equals("")) {
            alertMessage.setText(alertText);
            if (font != null && !font.isEmpty())
                alertMessage.setTypeface(fontUtils.getTypeFace(font));
            else {
                alertMessage.setTypeface(fontUtils.getTypeFace(FontUtils.robotoRegular));
            }
            if (textColor != 0)
                alertMessage.setTextColor(textColor);
            else {
                alertMessage.setTextColor(palette.getAccent700());
            }
            alertMessage.setGravity(Gravity.CENTER_HORIZONTAL);
            alertMessage.setTextAppearance(context, typography.getText1());
            builder.setCustomTitle(infoView);

        }

        if (!positiveButtonTitle.equals("")) {
            builder.setPositiveButton(positiveButtonTitle, null);
        }
        if (!negativeButtonTitle.equals("")) {
            builder.setNegativeButton(negativeButtonTitle, null);
        }
        if (!neutralButtonTitle.equals("")) {
            builder.setNeutralButton(neutralButtonTitle, null);
        }

        int positiveColor = positiveButtonColor != 0 ? positiveButtonColor : palette.getPrimary();
        int negativeColor = negativeButtonColor != 0 ? negativeButtonColor : palette.getPrimary();
        int neutralColor = neutralButtonColor != 0 ? neutralButtonColor : palette.getPrimary();

        alertDialogCreater = builder.create();
        alertDialogCreater.show();

        Drawable drawable = context.getDrawable(R.drawable.curved_bg);
        drawable.setTint(palette.getAccent900());
        drawable.setBounds(50, 50, 50, 50);
        alertDialogCreater.getWindow().setBackgroundDrawable(drawable);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.leftMargin = Utils.convertDpToPx(context, 35);
//        params.rightMargin = Utils.convertDpToPx(context, 35);
//
//        alertDialogCreater.getWindow().setContentView(infoView, params);
        Button positiveButton = alertDialogCreater.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setId(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(positiveColor);
        positiveButton.setOnClickListener(this);

        Button negativeButton = alertDialogCreater.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setId(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(negativeColor);
        negativeButton.setOnClickListener(this);

        Button neutralButton = alertDialogCreater.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setId(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setTextColor(neutralColor);
        neutralButton.setOnClickListener(this);
    }

}