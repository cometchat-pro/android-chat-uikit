package com.cometchatworkspace.resources.utils.custom_dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.cometchatworkspace.R;

public class CometChatDialog implements View.OnClickListener {

    private final OnDialogButtonClickListener onAlertDialogButtonClick;
    private final View infoView;
    private AlertDialog alertDialogCreator;
    private final int popupId;
    private final TextView alertMessage;
    private TextView alertTitle;
    private final boolean isCancelable;
    private final int positiveButtonColor, negativeButtonColor, neutralButtonColor;
    private final String alertText;
    private final String alertTitleText;
    private final String positiveButtonTitle;
    private final String negativeButtonTitle;
    private final String neutralButtonTitle;
    private final int alertTitleAppearance;
    private final int alertTextAppearance;
    private final int buttonAppearance;
    private final int background;
    private final int alertTitleColor, alertTextColor;


    public CometChatDialog(Context context,
                           @StyleRes int alertTitleAppearance,
                           @StyleRes int alertTextAppearance,
                           @StyleRes int buttonAppearance,
                           @ColorInt int background,
                           @ColorInt int alertTitleColor,
                           @ColorInt int alertTextColor,
                           @NonNull String alertText,
                           @NonNull String alertTitleText,
                           @NonNull String positiveTitle,
                           @NonNull String negativeTitle,
                           @NonNull String neutralTitle,
                           @ColorInt int positiveButtonColor,
                           @ColorInt int negativeButtonColor,
                           @ColorInt int neutralButtonColor,
                           OnDialogButtonClickListener onAlertDialogButton,
                           int popUpId,
                           boolean isCancelable) {

        this.onAlertDialogButtonClick = onAlertDialogButton;
        this.positiveButtonColor = positiveButtonColor;
        this.neutralButtonColor = neutralButtonColor;
        this.negativeButtonColor = negativeButtonColor;
        this.isCancelable = isCancelable;
        this.alertText = alertText;
        this.alertTitleText = alertTitleText;
        this.alertTitleAppearance = alertTitleAppearance;
        this.alertTextAppearance = alertTextAppearance;
        this.background = background;
        this.alertTitleColor = alertTitleColor;
        this.buttonAppearance = buttonAppearance;
        this.alertTextColor = alertTextColor;
        this.positiveButtonTitle = positiveTitle;
        this.negativeButtonTitle = negativeTitle;
        this.neutralButtonTitle = neutralTitle;
        this.popupId = popUpId;
        infoView = View.inflate(context, R.layout.dialog_custom_view, null);
        alertMessage = infoView.findViewById(R.id.alert_message);
        alertTitle = infoView.findViewById(R.id.alert_title);

        setDialog(context);
    }

    private void setDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(isCancelable);

        if (alertTitle != null && alertTitleText != null && !alertTitleText.isEmpty()) {
            alertTitle.setVisibility(View.VISIBLE);
            alertTitle.setText(alertTitleText);
            if (alertTitleAppearance != 0)
                alertTitle.setTextAppearance(context, alertTitleAppearance);
            if (alertTitleColor != 0)
                alertTitle.setTextColor(alertTitleColor);
        }
        if (alertMessage != null && alertText != null && !alertText.isEmpty()) {
            alertMessage.setText(alertText);
            if (alertTextAppearance != 0)
                alertMessage.setTextAppearance(context, alertTextAppearance);
            if (alertTextColor != 0)
                alertMessage.setTextColor(alertTextColor);
            alertMessage.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setCustomTitle(infoView);
        }

        if (positiveButtonTitle != null && !positiveButtonTitle.equals("")) {
            builder.setPositiveButton(positiveButtonTitle, null);
        }
        if (negativeButtonTitle != null && !negativeButtonTitle.equals("")) {
            builder.setNegativeButton(negativeButtonTitle, null);
        }
        if (neutralButtonTitle != null && !neutralButtonTitle.equals("")) {
            builder.setNeutralButton(neutralButtonTitle, null);
        }

        alertDialogCreator = builder.create();
        try {
            alertDialogCreator.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Drawable drawable = context.getDrawable(R.drawable.curved_bg);
        drawable.setTint(background);
        drawable.setBounds(50, 50, 50, 50);
        alertDialogCreator.getWindow().setBackgroundDrawable(drawable);

        Button positiveButton = alertDialogCreator.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setId(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);

        Button negativeButton = alertDialogCreator.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setId(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(this);

        Button neutralButton = alertDialogCreator.getButton(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setId(DialogInterface.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(this);

        if (buttonAppearance != 0) {
            positiveButton.setTextAppearance(context, buttonAppearance);
            negativeButton.setTextAppearance(context, buttonAppearance);
            neutralButton.setTextAppearance(context, buttonAppearance);
        }
        if (positiveButtonColor != 0)
            positiveButton.setTextColor(positiveButtonColor);
        if (negativeButtonColor != 0)
            negativeButton.setTextColor(negativeButtonColor);
        if (neutralButtonColor != 0)
            neutralButton.setTextColor(neutralButtonColor);
    }

    @Override
    public void onClick(View v) {
        onAlertDialogButtonClick.onButtonClick(alertDialogCreator, v.getId(), popupId);
    }

}