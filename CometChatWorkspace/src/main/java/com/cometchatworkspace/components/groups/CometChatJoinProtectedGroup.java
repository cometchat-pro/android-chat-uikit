package com.cometchatworkspace.components.groups;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CometChatJoinProtectedGroup extends CometChatListBase {

    View view;
    TextInputLayout passwordInputBox;
    TextInputEditText passwordInput;
    TextView messageText;
    Context context;
    FontUtils fontUtils;
    ImageView icon;
    String title, labelText, editTextPlaceHolderText;
    int titleColor, backIconTint, joinGroupIconTint, editBoxBackgroundColor, editBoxCornerRadius, editBoxTextColor, editBoxBorderColor, editBoxBorderWidth, editTextPlaceHolderColor, backgroundColor, labelColor;
    boolean showBackButton, showLabel;
    Drawable joinGroupIcon, backButtonIcon;
    Palette palette;
    Typography typography;
    Group group;
    //for user to set error text,font and color according to his/her choice
    private String errorMessageFont = null;
    private int errorMessageColor = 0;
    private String errorText = null;
    private boolean hideError = false;

    public CometChatJoinProtectedGroup(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null, -1);
    }

    public CometChatJoinProtectedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    public CometChatJoinProtectedGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();

        view = View.inflate(context, R.layout.cometchat_join_group, null);
        messageText = view.findViewById(R.id.label);
        passwordInputBox = view.findViewById(R.id.password_input_box);
        passwordInput = view.findViewById(R.id.password_input);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatJoinGroup,
                0, 0);

        title = a.getString(R.styleable.CometChatJoinGroup_title) != null ? a.getString(R.styleable.CometChatJoinGroup_title) : getResources().getString(R.string.protected_group);
        labelText = a.getString(R.styleable.CometChatJoinGroup_labelText) != null ? a.getString(R.styleable.CometChatJoinGroup_labelText) : getResources().getString(R.string.join_group_text);
        editTextPlaceHolderText = a.getString(R.styleable.CometChatJoinGroup_editTextPlaceHolderText) != null ? a.getString(R.styleable.CometChatJoinGroup_editTextPlaceHolderText) : getResources().getString(R.string.group_password);

        backButtonIcon = a.getDrawable(R.styleable.CometChatJoinGroup_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatJoinGroup_backButtonIcon) : getResources().getDrawable(R.drawable.ic_arrow_back);
        joinGroupIcon = a.getDrawable(R.styleable.CometChatJoinGroup_joinGroupIcon) != null ? a.getDrawable(R.styleable.CometChatJoinGroup_joinGroupIcon) : getResources().getDrawable(R.drawable.ic_check);

        titleColor = a.getColor(R.styleable.CometChatJoinGroup_titleColor, palette.getAccent());
        labelColor = a.getColor(R.styleable.CometChatJoinGroup_labelColor, palette.getAccent());
        backIconTint = a.getColor(R.styleable.CometChatJoinGroup_backIconTint, palette.getPrimary());
        joinGroupIconTint = a.getColor(R.styleable.CometChatJoinGroup_joinGroupIconTint, palette.getPrimary());
        editBoxBackgroundColor = a.getColor(R.styleable.CometChatJoinGroup_editBoxBackgroundColor, 0);
        editBoxBorderColor = a.getColor(R.styleable.CometChatJoinGroup_editBoxBorderColor, 0);
        editBoxTextColor = a.getColor(R.styleable.CometChatJoinGroup_editBoxTextColor, palette.getAccent());
        editTextPlaceHolderColor = a.getColor(R.styleable.CometChatJoinGroup_editTextPlaceHolderColor, palette.getAccent600());
        backgroundColor = a.getColor(R.styleable.CometChatJoinGroup_backgroundColor, palette.getBackground());

        showBackButton = a.getBoolean(R.styleable.CometChatJoinGroup_showBackButton, true);
        showLabel = a.getBoolean(R.styleable.CometChatJoinGroup_showLabel, true);

        editBoxCornerRadius = a.getInt(R.styleable.CometChatJoinGroup_editBoxCornerRadius, 0);
        editBoxBorderWidth = a.getInt(R.styleable.CometChatJoinGroup_editBoxBorderWidth, 0);

        setStatusColor(palette.getBackground());
        setTitle(title);
        super.setTitleAppearance(typography.getHeading());
        setEditTextPlaceHolderText(editTextPlaceHolderText);
        setBackButtonIcon(backButtonIcon);
        setJoinGroupIcon(joinGroupIcon);
        setTitleColor(titleColor);
        setBackIconTint(backIconTint);
        setEditTextBackground(editBoxBackgroundColor);
        setEditTextBorderColor(editBoxBorderColor);
        setEditTextTextColor(editBoxTextColor);
        setEditTextPlaceHolderColor(editTextPlaceHolderColor);
        setShowBackButton(showBackButton);
        setShowLabel(showLabel);
        setLabelColor(labelColor);
        setLabelAppearance(typography.getSubtitle1());
        setEditTextBorderWidth(editBoxBorderWidth);
        setEditTextCornerRadius(editBoxCornerRadius);
        setEditTextTextAppearance(typography.getText1());

        if (palette.getGradientBackground() != null)
            setBackground(palette.getGradientBackground());
        else
            setBackgroundColor(backgroundColor);

        setEditTextUnderlineColor(palette.getAccent100());
        super.hideSearch(true);
        super.addListView(view);
        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {

            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
            }
        });
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.toString().length() > 0) {
                    passwordInputBox.setError(null);
                } else {
                    passwordInputBox.setError(getResources().getString(R.string.err_password_missing_message));
                    passwordInputBox.requestFocus();
                }
            }
        });

    }

    private void setEditTextUnderlineColor(int color) {
        passwordInput.setSupportBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context,color);

    }
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            Resources res = getResources();
            String text = String.format(res.getString(R.string.join_group_text), group.getName() + "");
            setLabelText(text);
        }

    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void setBackground(int color) {
        super.setBackground(color);
    }

    public void setLabelText(String labelText) {
        if (messageText != null && labelText != null)
            messageText.setText(labelText);
    }

    @Override
    public void setTitleColor(int titleColor) {
        super.setTitleColor(titleColor);
    }

    public void setLabelColor(int color) {
        if (color != 0)
            messageText.setTextColor(color);
    }

    public void setLabelAppearance(int appearance) {
        if (appearance != 0)
            messageText.setTextAppearance(context, appearance);
    }

    public void setBackIconTint(int backIconTint) {
        super.backIconTint(backIconTint);
    }

    public void setShowBackButton(boolean showBackButton) {
        super.showBackButton(showBackButton);
    }

    public void setShowLabel(boolean showLabel) {
        if (showLabel)
            messageText.setVisibility(VISIBLE);
        else
            messageText.setVisibility(GONE);
    }

    public void setJoinGroupIcon(Drawable joinGroupIcon) {
        if (icon == null) {
            icon = new ImageView(context);
            icon.setImageDrawable(joinGroupIcon);
            icon.setImageTintList(ColorStateList.valueOf(joinGroupIconTint));
            super.addMenuIcon(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (passwordInput.getText() != null && !passwordInput.getText().toString().isEmpty()) {
                        joinGroup();
                    } else {
                        passwordInputBox.setError(getResources().getString(R.string.err_password_missing_message));
                        passwordInputBox.requestFocus();
                    }
                }
            });
        }

    }

    public void joinGroup() {
        if (group.getGuid() != null && group.getGroupType() != null && passwordInput.getText() != null) {
            CometChat.joinGroup(group.getGuid(), group.getGroupType(), passwordInput.getText().toString(), new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group joinedGroup) {
                    joinedGroup.setHasJoined(true);
                    for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                        events.onGroupMemberJoin(CometChat.getLoggedInUser(), joinedGroup);
                    }
                    ((Activity) context).onBackPressed();
                }

                @Override
                public void onError(CometChatException e) {
                    for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                        events.onError(e);
                    }
                    if (e.getMessage() != null)
                        hideError(e.getMessage());
                }
            });
        }
    }

    public void setErrorMessageFont(String errorMessageFont) {
        this.errorMessageFont = errorMessageFont;
    }

    public void setErrorMessageColor(int errorMessageColor) {
        this.errorMessageColor = errorMessageColor;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    private void hideError(String error) {
        String error_message;
        String error_title = null;
        if (errorText != null)
            error_message = errorText;
        else if (getContext().getString(R.string.sdk_incorrect_password_error).equalsIgnoreCase(error)) {
            error_message = getContext().getString(R.string.please_try_another_password);
            error_title = getContext().getString(R.string.incorrect_password);
        } else
            error_message = getContext().getString(R.string.something_went_wrong);

        if (!hideError) {
            if (getContext() != null) {
                new CustomAlertDialogHelper(context, errorMessageFont, errorMessageColor, error_message, error_title, null, getContext().getString(R.string.okay), "", "", palette.getPrimary(), 0, 0, new OnAlertDialogButtonClickListener() {
                    @Override
                    public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            passwordInput.setText("");
                            alertDialog.dismiss();
                        }
                    }
                }, 0, false);
            }
        }
    }

    public void setBackButtonIcon(Drawable backButtonIcon) {
        super.backIcon(backButtonIcon);
    }

    public void setEditTextCornerRadius(float EditTextBoxRadius) {
        if (EditTextBoxRadius != 0) {
            passwordInputBox.setBoxCornerRadii(EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius);
        }
    }

    public void setEditTextBackground(@ColorInt int EditTextBoxColor) {
        if (EditTextBoxColor != 0 && passwordInputBox != null) {
            passwordInputBox.setBoxBackgroundColor(EditTextBoxColor);
        }
    }

    public void setEditTextPlaceHolderColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null) {
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
            passwordInput.setHintTextColor(ColorStateList.valueOf(color));
        }
    }

    public void setEditTextPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null && passwordInput != null) {
            passwordInput.setHint(placeHolderText);
        }
    }

    public void setEditTextTextColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null)
            passwordInput.setTextColor(color);
    }

    public void setEditTextTextFont(String fontName) {
        if (fontName != null && passwordInput != null)
            passwordInput.setTypeface(fontUtils.getTypeFace(fontName));
    }

    public void setEditTextTextAppearance(int appearance) {
        if (appearance != 0 && passwordInput != null)
            passwordInput.setTextAppearance(context, appearance);
    }

    public void setEditTextBorderColor(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null)
            passwordInputBox.setBoxStrokeColor(color);
    }

    public void setEditTextBorderWidth(int width) {
        if (passwordInputBox != null) {
            passwordInputBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            passwordInputBox.setBoxStrokeWidth(width);
        }
    }

    public void setStartIconTint(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null)
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    public void setEditTextBoxStartIcon(int res) {
        if (res != 0 && passwordInputBox != null) {
            passwordInputBox.setStartIconDrawable(res);
        }
    }


}
