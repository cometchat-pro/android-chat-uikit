package com.cometchat.chatuikit.joinprotectedgroup;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.OnDialogButtonClickListener;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.chatuikit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A custom view for joining a protected group in CometChat.
 */
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
    private int errorStateTextAppearance = 0;
    private int errorMessageColor = 0;
    private String errorText = null;
    private boolean hideError = false;
    private OnJoinClick onJoinClick;
    private OnError onError;
    private JoinProtectedGroupViewModel joinProtectedGroupViewModel;

    /**
     * Constructs a new {@code CometChatJoinProtectedGroup} with the provided context.
     *
     * @param context The context of the view.
     */
    public CometChatJoinProtectedGroup(Context context) {
        super(context);
        if (!isInEditMode()) initViewComponent(context, null, -1);
    }

    /**
     * Constructs a new {@code CometChatJoinProtectedGroup} with the provided context and attribute set.
     *
     * @param context The context of the view.
     * @param attrs   The attribute set for the view.
     */
    public CometChatJoinProtectedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) initViewComponent(context, attrs, -1);
    }

    /**
     * Constructs a new {@code CometChatJoinProtectedGroup} with the provided context, attribute set, and default style attribute.
     *
     * @param context      The context of the view.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute for the view.
     */
    public CometChatJoinProtectedGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the view components.
     *
     * @param context      The context of the view.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style attribute for the view.
     */
    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        errorMessageColor = palette.getAccent700();
        errorStateTextAppearance = typography.getText1();
        joinProtectedGroupViewModel = new JoinProtectedGroupViewModel();
        view = View.inflate(context, R.layout.cometchat_join_group, null);
        messageText = view.findViewById(R.id.label);
        passwordInputBox = view.findViewById(R.id.password_input_box);
        passwordInput = view.findViewById(R.id.password_input);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CometChatJoinGroup, 0, 0);

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

        joinProtectedGroupViewModel = ViewModelProviders.of((FragmentActivity) context).get(joinProtectedGroupViewModel.getClass());
        joinProtectedGroupViewModel.getCometChatException().observe((AppCompatActivity) context, this::throwError);
        joinProtectedGroupViewModel.getGroupMutableLiveData().observe((AppCompatActivity) context, this::groupJoined);

        setStatusColor(palette.getBackground());
        setTitle(title);
        super.setTitleAppearance(typography.getHeading());
        setEditTextPlaceHolderText(editTextPlaceHolderText);
        super.backIcon(backButtonIcon);
        createJoinGroupIcon(joinGroupIcon);
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

        if (palette.getGradientBackground() != null) setBackground(palette.getGradientBackground());
        else setBackgroundColor(backgroundColor);

        setEditTextUnderlineColor(palette.getAccent100());
        super.hideSearch(true);
        super.addListView(view);
        super.addOnSearchListener(new OnSearch() {
            @Override
            public void onSearch(String state, String text) {

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
        passwordInput.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the tint color for the joined group icon.
     *
     * @param color The color to set for the joined group icon.
     */
    public void setJoinedGroupIconTint(int color) {
        if (icon != null && color != 0) icon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the status bar color for the view.
     *
     * @param color The color to set for the status bar.
     */
    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context, color);
    }

    /**
     * Sets the group information for the view.
     *
     * @param group The group object to set.
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            joinProtectedGroupViewModel.setGroup(group);
            Resources res = getResources();
            String text = String.format(res.getString(R.string.join_group_text), group.getName() + "");
            setDescription(text);
        }
    }

    /**
     * Sets the title of the view.
     *
     * @param title The title to set.
     */
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * Sets the background color of the view.
     *
     * @param color The color to set for the background.
     */
    @Override
    public void setBackground(int color) {
        super.setBackground(color);
    }

    /**
     * Sets the description label for the view.
     *
     * @param labelText The text to set for the description label.
     */
    public void setDescription(String labelText) {
        if (messageText != null && labelText != null) messageText.setText(labelText);
    }

    /**
     * Sets the title color of the view.
     *
     * @param titleColor The color to set for the title.
     */
    @Override
    public void setTitleColor(int titleColor) {
        super.setTitleColor(titleColor);
    }

    /**
     * Sets the color of the description label.
     *
     * @param color The color to set for the description label.
     */
    public void setLabelColor(int color) {
        if (color != 0) messageText.setTextColor(color);
    }

    /**
     * Sets the appearance of the description label.
     *
     * @param appearance The appearance to set for the description label.
     */
    public void setLabelAppearance(int appearance) {
        if (appearance != 0) messageText.setTextAppearance(context, appearance);
    }

    /**
     * Sets the tint color for the back icon.
     *
     * @param backIconTint The tint color to set for the back icon.
     */
    public void setBackIconTint(int backIconTint) {
        super.backIconTint(backIconTint);
    }

    /**
     * Sets the visibility of the back button.
     *
     * @param showBackButton {@code true} to show the back button, {@code false} to hide it.
     */
    public void setShowBackButton(boolean showBackButton) {
        super.showBackButton(showBackButton);
    }

    /**
     * Sets the visibility of the description label.
     *
     * @param showLabel {@code true} to show the description label, {@code false} to hide it.
     */
    public void setShowLabel(boolean showLabel) {
        if (showLabel) messageText.setVisibility(VISIBLE);
        else messageText.setVisibility(GONE);
    }

    private void createJoinGroupIcon(Drawable joinGroupIcon) {
        if (icon == null) {
            icon = new ImageView(context);
            icon.setImageDrawable(joinGroupIcon);
            icon.setImageTintList(ColorStateList.valueOf(joinGroupIconTint));
            super.setMenu(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (passwordInput.getText() != null && !passwordInput.getText().toString().isEmpty()) {
                        if (onJoinClick == null)
                            joinProtectedGroupViewModel.joinGroup(passwordInput.getText().toString());
                        else onJoinClick.onJoin(context, group, passwordInput.getText().toString());
                    } else {
                        passwordInputBox.setError(getResources().getString(R.string.err_password_missing_message));
                        passwordInputBox.requestFocus();
                    }
                }
            });
        }
    }

    /**
     * Sets the join group icon drawable.
     *
     * @param icon The drawable for the join group icon.
     */
    public void setJoinGroupIcon(Drawable icon) {
        if (icon != null) {
            joinGroupIcon = icon;
            this.icon = null;
            createJoinGroupIcon(joinGroupIcon);
        }
    }

    private void groupJoined(Group group) {
        getBackIcon().performClick();
    }


    private void throwError(CometChatException cometChatException) {
        if (onError == null) {
            if (cometChatException != null && cometChatException.getMessage() != null)
                hideError(cometChatException.getMessage());
        } else {
            onError.onError(context, cometChatException);
        }
    }

    /**
     * Sets the text appearance for the error state.
     *
     * @param appearance The text appearance for the error state.
     */
    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    /**
     * Sets the color for the error message.
     *
     * @param errorMessageColor The color for the error message.
     */
    public void setErrorMessageColor(int errorMessageColor) {
        this.errorMessageColor = errorMessageColor;
    }

    /**
     * Sets the error text for the view.
     *
     * @param errorText The error text to set.
     */
    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    /**
     * Sets whether to hide the error state.
     *
     * @param hideError {@code true} to hide the error state, {@code false} to show it.
     */
    public void setHideError(boolean hideError) {
        this.hideError = hideError;
    }

    private void hideError(String error) {
        String errorMessage;
        String errorTitle = null;
        if (errorText != null) errorMessage = errorText;
        else if (getContext().getString(R.string.sdk_incorrect_password_error).equalsIgnoreCase(error)) {
            errorMessage = getContext().getString(R.string.please_try_another_password);
            errorTitle = getContext().getString(R.string.incorrect_password);
        } else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!hideError) {
            if (getContext() != null) {

                new CometChatDialog(context, typography.getHeading(), errorStateTextAppearance, typography.getText3(), palette.getBackground(), palette.getAccent(), errorMessageColor, errorMessage, errorTitle, getContext().getString(R.string.okay), "", "", palette.getPrimary(), palette.getPrimary(), 0, new OnDialogButtonClickListener() {
                    @Override
                    public void onButtonClick(AlertDialog alertDialog, int which, int popupId) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            passwordInput.setText("");
                            alertDialog.dismiss();
                        }
                    }
                }, 0, false);
            }
        }
    }

    /**
     * Sets the corner radius for the edit text box.
     *
     * @param EditTextBoxRadius The corner radius to set for the edit text box.
     */
    public void setEditTextCornerRadius(float EditTextBoxRadius) {
        if (EditTextBoxRadius != 0) {
            passwordInputBox.setBoxCornerRadii(EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius);
        }
    }

    /**
     * Sets the background color for the edit text box.
     *
     * @param EditTextBoxColor The background color to set for the edit text box.
     */
    public void setEditTextBackground(@ColorInt int EditTextBoxColor) {
        if (EditTextBoxColor != 0 && passwordInputBox != null) {
            passwordInputBox.setBoxBackgroundColor(EditTextBoxColor);
        }
    }

    /**
     * Sets the placeholder color for the edit text.
     *
     * @param color The color to set for the placeholder.
     */
    public void setEditTextPlaceHolderColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null) {
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
            passwordInput.setHintTextColor(ColorStateList.valueOf(color));
        }
    }

    /**
     * Sets the placeholder text for the edit text.
     *
     * @param placeHolderText The placeholder text to set.
     */
    public void setEditTextPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null && passwordInput != null) {
            passwordInput.setHint(placeHolderText);
        }
    }

    /**
     * Sets the text color for the edit text.
     *
     * @param color The color to set for the edit text.
     */
    public void setEditTextTextColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null) passwordInput.setTextColor(color);
    }

    /**
     * Sets the font for the edit text.
     *
     * @param fontName The font name to set for the edit text.
     */
    public void setEditTextTextFont(String fontName) {
        if (fontName != null && passwordInput != null)
            passwordInput.setTypeface(fontUtils.getTypeFace(fontName));
    }

    /**
     * Sets the text appearance for the edit text.
     *
     * @param appearance The text appearance to set for the edit text.
     */
    public void setEditTextTextAppearance(int appearance) {
        if (appearance != 0 && passwordInput != null)
            passwordInput.setTextAppearance(context, appearance);
    }

    /**
     * Sets the border color for the edit text box.
     *
     * @param color The color to set for the border.
     */
    public void setEditTextBorderColor(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null) passwordInputBox.setBoxStrokeColor(color);
    }

    /**
     * Sets the border width for the edit text box.
     *
     * @param width The width to set for the border.
     */
    public void setEditTextBorderWidth(int width) {
        if (passwordInputBox != null) {
            passwordInputBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            passwordInputBox.setBoxStrokeWidth(width);
        }
    }

    /**
     * Sets the tint color for the search icon.
     *
     * @param color The color to set for the search icon.
     */
    public void setSearchIconTint(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null)
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the start icon drawable for the edit text box.
     *
     * @param res The resource ID of the drawable to set.
     */
    public void setEditTextBoxStartIcon(int res) {
        if (res != 0 && passwordInputBox != null) {
            passwordInputBox.setStartIconDrawable(res);
        }
    }

    /**
     * Sets the style for the view.
     *
     * @param style The style to set for the view.
     */
    public void setStyle(JoinProtectedGroupStyle style) {
        if (style != null) {
            super.setTitleAppearance(style.getTitleTextAppearance());
            super.setTitleFont(style.getTitleFont());
            super.setTitleColor(style.getTitleColor());
            super.backIconTint(style.getBackIconTint());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());

            setJoinedGroupIconTint(style.getJoinGroupIconTint());
            setEditTextBackground(style.getEditBoxTextColor());
            setEditTextCornerRadius(style.getEditBoxCornerRadius());
            setEditTextTextColor(style.getEditBoxTextColor());
            setEditTextBorderWidth(style.getEditBoxBorderWidth());
            setEditTextBorderColor(style.getEditBoxBorderColor());
            setEditTextPlaceHolderColor(style.getEditTextPlaceHolderColor());
            setLabelColor(style.getDescriptionColor());
            setLabelAppearance(style.getTitleTextAppearance());
            setEditTextTextAppearance(style.getPasswordTextAppearance());
        }
    }

    /**
     * Sets the click listener for the join button.
     *
     * @param onJoinClick The click listener to set for the join button.
     */
    public void setOnJoinClick(OnJoinClick onJoinClick) {
        this.onJoinClick = onJoinClick;
    }

    /**
     * Sets the error listener for handling errors.
     *
     * @param onError The error listener to set.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public interface OnJoinClick {
        void onJoin(Context context, Group group, String password);
    }
}
