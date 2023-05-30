package com.cometchat.chatuikit.creategroup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * CometChatCreateGroup is a custom view that provides a UI for creating a group.
 * It extends the CometChatListBase class and includes various customization options.
 */
public class CometChatCreateGroup extends CometChatListBase {

    View view;
    private TabLayout tabLayout;
    private Context context;
    private FontUtils fontUtils;
    private ImageView icon;
    private String title;
    private int titleColor, backIconTint, createGroupIconTint, backgroundColor, tabSelectedTextColor, tabTextColor, tabIndicatorColor, tabBackgroundTint;
    private boolean showBackButton;
    private Drawable createGroupIcon, backButtonIcon, tabBackground, tabBackgroundState;
    private List<String> tabs_List = new ArrayList<>(Arrays.asList(getResources().getString(R.string.type_public), getResources().getString(R.string.type_private), getResources().getString(R.string.type_protected)));
    private TextInputLayout nameInputBox;
    private TextInputLayout passwordInputBox;
    private TextInputEditText nameInput;
    private TextInputEditText passwordInput;
    private CreateGroupViewModel createGroupViewModel;
    private CometChatTheme theme;
    private OnCreateGroup onCreateGroup;
    private OnError onError;

    /**
     * Constructs a new CometChatCreateGroup with the provided context.
     *
     * @param context The context of the calling component.
     */
    public CometChatCreateGroup(Context context) {
        super(context);
        if (!isInEditMode()) initViewComponent(context, null, -1);
    }

    /**
     * Constructs a new CometChatCreateGroup with the provided context and attribute set.
     *
     * @param context The context of the calling component.
     * @param attrs   The attribute set containing the attributes of the view.
     */
    public CometChatCreateGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) initViewComponent(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatCreateGroup with the provided context, attribute set, and default style.
     *
     * @param context      The context of the calling component.
     * @param attrs        The attribute set containing the attributes of the view.
     * @param defStyleAttr The default style resource ID.
     */
    public CometChatCreateGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the view components.
     *
     * @param context      The context of the calling component.
     * @param attrs        The attribute set containing the attributes of the view.
     * @param defStyleAttr The default style resource ID.
     */
    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        theme = CometChatTheme.getInstance(context);
        createGroupViewModel = new CreateGroupViewModel();
        view = View.inflate(context, R.layout.cometchat_create_group, null);
        nameInputBox = view.findViewById(R.id.name_input_box);
        passwordInputBox = view.findViewById(R.id.password_input_box);
        nameInput = view.findViewById(R.id.name_input);
        passwordInput = view.findViewById(R.id.password_input);
        tabLayout = view.findViewById(R.id.tabLayout_create_Group);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CometChatCreateGroup, 0, 0);

        title = a.getString(R.styleable.CometChatCreateGroup_title) != null ? a.getString(R.styleable.CometChatCreateGroup_title) : getResources().getString(R.string.new_group);
        backButtonIcon = a.getDrawable(R.styleable.CometChatCreateGroup_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatCreateGroup_title) : getResources().getDrawable(R.drawable.ic_arrow_back);
        createGroupIcon = a.getDrawable(R.styleable.CometChatCreateGroup_createGroupIcon) != null ? a.getDrawable(R.styleable.CometChatCreateGroup_title) : getResources().getDrawable(R.drawable.ic_check);
        titleColor = a.getColor(R.styleable.CometChatCreateGroup_titleColor, theme.getPalette().getAccent());
        backIconTint = a.getColor(R.styleable.CometChatCreateGroup_backIconTint, theme.getPalette().getPrimary());
        createGroupIconTint = a.getColor(R.styleable.CometChatCreateGroup_createGroupIconTint, theme.getPalette().getPrimary());
        backgroundColor = a.getColor(R.styleable.CometChatCreateGroup_backgroundColor, theme.getPalette().getBackground());
        tabTextColor = a.getColor(R.styleable.CometChatCreateGroup_tabTextColor, theme.getPalette().getAccent600());
        tabSelectedTextColor = a.getColor(R.styleable.CometChatCreateGroup_tabSelectedTextColor, theme.getPalette().getAccent900());
        tabIndicatorColor = a.getColor(R.styleable.CometChatCreateGroup_tabIndicatorColor, theme.getPalette().getPrimary());
        tabBackgroundTint = a.getColor(R.styleable.CometChatCreateGroup_tabBackgroundTint, theme.getPalette().getAccent50());
        tabBackground = a.getDrawable(R.styleable.CometChatCreateGroup_tabBackground);
        if (tabBackground == null) {
            tabBackground = getResources().getDrawable(R.drawable.tab_layout_background);
        }
        tabBackgroundState = a.getDrawable(R.styleable.CometChatCreateGroup_tabBackgroundState) != null ? a.getDrawable(R.styleable.CometChatCreateGroup_tabBackgroundState) : getResources().getDrawable(R.drawable.tab_background_state);
        showBackButton = a.getBoolean(R.styleable.CometChatCreateGroup_showBackButton, true);

        createGroupViewModel = ViewModelProviders.of((FragmentActivity) context).get(createGroupViewModel.getClass());
        createGroupViewModel.getCreateSuccess().observe((AppCompatActivity) context, this::createSuccess);
        createGroupViewModel.getCometChatException().observe((AppCompatActivity) context, this::throwError);
        setTitle(title);
        super.setTitleAppearance(theme.getTypography().getHeading());
        setCloseButtonIcon(backButtonIcon);
        setTitleColor(titleColor);
        setBackIconTint(backIconTint);
        setShowBackButton(showBackButton);
        if (theme.getPalette().getGradientBackground() != null)
            setBackground(theme.getPalette().getGradientBackground());
        else setBackgroundColor(backgroundColor);
        super.hideSearch(true);
        super.addListView(view);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Drawable wrappedDrawable = DrawableCompat.wrap(tabBackgroundState);
                DrawableCompat.setTint(wrappedDrawable, tabIndicatorColor);
                tab.view.setBackground(wrappedDrawable);

                if (tab.getText() != null && tab.getText().toString().equalsIgnoreCase(getResources().getString(R.string.type_protected))) {
                    TranslateAnimation animate = new TranslateAnimation(0, 0, passwordInputBox.getHeight(), 0);
                    animate.setDuration(300);
                    animate.setFillAfter(true);
                    passwordInputBox.startAnimation(animate);
                    passwordInputBox.setVisibility(View.VISIBLE);

                } else {
                    TranslateAnimation animate = new TranslateAnimation(0, 0, 0, passwordInputBox.getHeight());
                    animate.setDuration(1000);
                    animate.setFillAfter(true);
                    passwordInputBox.startAnimation(animate);
                    passwordInputBox.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        super.addOnSearchListener(new OnSearch() {
            @Override
            public void onSearch(String state, String text) {

            }
        });
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.toString().length() > 0) {
                    nameInputBox.setError(null);
                } else {
                    nameInputBox.setError(getResources().getString(R.string.err_empty_group_name_message));
                    nameInputBox.setErrorTextColor(ColorStateList.valueOf(theme.getPalette().getError()));
                    nameInputBox.requestFocus();
                }
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
                    passwordInputBox.setErrorTextColor(ColorStateList.valueOf(theme.getPalette().getError()));
                    passwordInputBox.requestFocus();
                }
            }
        });

    }

    public void setTabs() {
        for (int i = 0; i < tabs_List.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs_List.get(i)));
            Drawable wrappedDrawable = DrawableCompat.wrap(tabBackgroundState);
            DrawableCompat.setTint(wrappedDrawable, tabIndicatorColor);
            Objects.requireNonNull(tabLayout.getTabAt(0)).view.setBackground(wrappedDrawable);
            tabLayout.setSelectedTabIndicatorColor(tabIndicatorColor);
            tabLayout.setTabTextColors(tabTextColor, tabSelectedTextColor);
            tabBackground.setTint(tabBackgroundTint);
            tabLayout.setBackground(tabBackground);
        }
    }

    private void showError() {
        new CometChatDialog(context, 0, 0, theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, 0, context.getString(R.string.something_went_wrong), "", "", getResources().getString(R.string.cancel), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                alertDialog.dismiss();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                alertDialog.dismiss();
            }
        }, 0, false);
    }

    private void throwError(CometChatException exception) {
        if (onError == null) showError();
        else onError.onError(context, exception);
    }

    /**
     * Triggered when the group is created successfully
     *
     * @param status The status of the group creation process.
     */
    private void createSuccess(Status status) {
        if (Status.SUCCESS.equals(status)) {
            getBackIcon().performClick();
        }
    }

    /**
     * Sets the title of the view.
     *
     * @param title The title to be set.
     */
    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * Sets the background color of the view.
     *
     * @param color The background color to be set.
     */
    @Override
    public void setBackground(int color) {
        super.setBackground(color);
    }

    /**
     * Sets the title color of the view.
     *
     * @param titleColor The title color to be set.
     */
    @Override
    public void setTitleColor(int titleColor) {
        super.setTitleColor(titleColor);
    }

    /**
     * Sets the tint color of the back icon.
     *
     * @param backIconTint The tint color to be set.
     */
    public void setBackIconTint(int backIconTint) {
        super.backIconTint(backIconTint);
    }

    /**
     * Sets whether to show the back button or not.
     *
     * @param showBackButton True to show the back button, false to hide it.
     */
    public void setShowBackButton(boolean showBackButton) {
        super.showBackButton(showBackButton);
    }

    public void createGroupIcon(Drawable joinGroupIcon) {
        if (icon == null) {
            icon = new ImageView(context);
            icon.setImageDrawable(joinGroupIcon);
            icon.setImageTintList(ColorStateList.valueOf(createGroupIconTint));
            super.setMenu(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String groupType;
                    String tabStr = tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getText().toString();
                    if (tabStr.equalsIgnoreCase(getResources().getString(R.string.type_protected))) {
                        groupType = CometChatConstants.GROUP_TYPE_PASSWORD;
                    } else if (tabStr.equalsIgnoreCase(getResources().getString(R.string.type_public))) {
                        groupType = CometChatConstants.GROUP_TYPE_PUBLIC;
                    } else {
                        groupType = CometChatConstants.GROUP_TYPE_PRIVATE;
                    }

                    if (nameInput != null) {
                        if (nameInput.getText().toString().isEmpty()) {
                            nameInputBox.setError(getResources().getString(R.string.err_empty_group_name_message));
                            nameInputBox.setErrorTextColor(ColorStateList.valueOf(theme.getPalette().getError()));
                            nameInputBox.requestFocus();
                            return;
                        }
                        if (groupType.equalsIgnoreCase(UIKitConstants.GroupType.PASSWORD)) {
                            if (passwordInput != null) {
                                if (passwordInput.getText().toString().isEmpty()) {
                                    passwordInputBox.setError(getResources().getString(R.string.err_password_missing_message));
                                    passwordInputBox.setErrorTextColor(ColorStateList.valueOf(theme.getPalette().getError()));
                                    passwordInputBox.requestFocus();
                                    return;
                                }
                            }
                        }
                        Group group = new Group("group_" + System.currentTimeMillis(), nameInput.getText().toString(), groupType, passwordInput.getText().toString());
                        if (onCreateGroup == null) createGroupViewModel.createGroup(group);
                        else onCreateGroup.onCreateGroup(context, group);
                    }
                }
            });
        }
    }

    /**
     * Sets the icon for the create group button.
     *
     * @param icon The icon drawable to be set.
     */
    public void setCreateGroupIcon(Drawable icon) {
        if (icon != null) createGroupIcon = icon;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        createGroupIcon(createGroupIcon);
        setTabs();
    }

    /**
     * Hides the public group tab.
     */
    public void hidePublicGroup() {
        tabs_List.remove(getResources().getString(R.string.type_public));
    }

    /**
     * Hides the private group tab.
     */
    public void hidePrivateGroup() {
        tabs_List.remove(getResources().getString(R.string.type_private));
    }

    /**
     * Hides the password-protected group tab.
     */
    public void hidePasswordGroup() {
        tabs_List.remove(getResources().getString(R.string.type_protected));
    }

    /**
     * Sets the corner radius of the edit text boxes.
     *
     * @param EditTextBoxRadius The corner radius value to be set.
     */
    public void setEditTextCornerRadius(float EditTextBoxRadius) {
        if (EditTextBoxRadius > -1) {
            passwordInputBox.setBoxCornerRadii(EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius);
            nameInputBox.setBoxCornerRadii(EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius);
        }
    }

    /**
     * Sets the background color of the edit text boxes.
     *
     * @param EditTextBoxColor The background color to be set.
     */
    public void setEditTextBackground(@ColorInt int EditTextBoxColor) {
        if (EditTextBoxColor != 0 && passwordInputBox != null && nameInputBox != null) {
            passwordInputBox.setBoxBackgroundColor(EditTextBoxColor);
            nameInputBox.setBoxBackgroundColor(EditTextBoxColor);
        }
    }

    /**
     * Sets the placeholder text color of the edit text boxes.
     *
     * @param color The placeholder text color to be set.
     */
    public void setEditTextPlaceHolderColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null && nameInput != null) {
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
            passwordInput.setHintTextColor(ColorStateList.valueOf(color));
            nameInputBox.setStartIconTintList(ColorStateList.valueOf(color));
            nameInput.setHintTextColor(ColorStateList.valueOf(color));
        }
    }

    /**
     * Sets the placeholder text of the password input box.
     *
     * @param placeHolderText The placeholder text to be set.
     */
    public void setPasswordInputBoxPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null && passwordInput != null) {
            passwordInput.setHint(placeHolderText);
        }
    }

    /**
     * Sets the placeholder text of the name input box.
     *
     * @param placeHolderText The placeholder text to be set.
     */
    public void setNameInputBoxPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null && nameInput != null) {
            nameInput.setHint(placeHolderText);
        }
    }

    /**
     * Sets the text color of the edit text boxes.
     *
     * @param color The text color to be set.
     */
    public void setEditTextTextColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null && nameInput != null) {
            passwordInput.setTextColor(color);
            nameInput.setTextColor(color);
        }
    }

    /**
     * Sets the font for the edit text boxes.
     *
     * @param fontName The name of the font to be set.
     */
    public void setEditTextTextFont(String fontName) {
        if (fontName != null && passwordInput != null && nameInput != null) {
            passwordInput.setTypeface(fontUtils.getTypeFace(fontName));
            nameInput.setTypeface(fontUtils.getTypeFace(fontName));
        }
    }

    /**
     * Sets the text appearance of the edit text boxes.
     *
     * @param appearance The text appearance resource ID to be set.
     */
    public void setEditTextTextAppearance(int appearance) {
        if (appearance != 0 && passwordInput != null) {
            passwordInput.setTextAppearance(context, appearance);
            nameInput.setTextAppearance(context, appearance);
        }
    }

    /**
     * Sets the border color of the edit text boxes.
     *
     * @param color The border color to be set.
     */
    public void setEditTextBorderColor(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null) passwordInputBox.setBoxStrokeColor(color);
        nameInputBox.setBoxStrokeColor(color);
    }

    /**
     * Sets the border width of the edit text boxes.
     *
     * @param width The border width to be set.
     */
    public void setEditTextBorderWidth(int width) {
        if (passwordInputBox != null) {
            passwordInputBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            passwordInputBox.setBoxStrokeWidth(width);
            nameInputBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            nameInputBox.setBoxStrokeWidth(width);
        }
    }

    /**
     * Sets the start icon tint color of the password input box.
     *
     * @param color The start icon tint color to be set.
     */
    public void setPasswordInputBoxStartIconTint(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null)
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the start icon tint color of the name input box.
     *
     * @param color The start icon tint color to be set.
     */
    public void setNameInputBoxStartIconTint(@ColorInt int color) {
        if (color != 0 && nameInputBox != null)
            nameInputBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the start icon drawable of the password input box.
     *
     * @param res The resource ID of the start icon drawable to be set.
     */
    public void setPasswordInputBoxStartIcon(int res) {
        if (res != 0 && passwordInputBox != null) {
            passwordInputBox.setStartIconDrawable(res);
        }
    }

    /**
     * Sets the start icon drawable of the name input box.
     *
     * @param res The resource ID of the start icon drawable to be set.
     */
    public void setNameInputBoxStartIcon(int res) {
        if (res != 0 && nameInputBox != null) {
            nameInputBox.setStartIconDrawable(res);
        }
    }

    /**
     * Sets the selected text color of the tabs.
     *
     * @param tabSelectedTextColor The selected text color to be set.
     */
    public void setTabSelectedTextColor(int tabSelectedTextColor) {
        if (tabSelectedTextColor != 0) this.tabSelectedTextColor = tabSelectedTextColor;
    }

    /**
     * Sets the text color of the tabs.
     *
     * @param tabTextColor The text color to be set.
     */
    public void setTabTextColor(int tabTextColor) {
        if (tabTextColor != 0) this.tabTextColor = tabTextColor;
    }

    /**
     * Sets the color of the tab indicator.
     *
     * @param tabIndicatorColor The color of the tab indicator to be set.
     */
    public void setTabIndicatorColor(int tabIndicatorColor) {
        if (tabIndicatorColor != 0) this.tabIndicatorColor = tabIndicatorColor;
    }


    /**
     * Sets the background tint color of the tabs.
     *
     * @param tabBackgroundTint The background tint color of the tabs to be set.
     */
    public void setTabBackgroundTint(int tabBackgroundTint) {
        if (tabBackgroundTint != 0) this.tabBackgroundTint = tabBackgroundTint;
    }

    /**
     * Sets the tint color of the create group icon.
     *
     * @param createGroupIconTint The tint color of the create group icon to be set.
     */
    public void setCreateGroupIconTint(int createGroupIconTint) {
        if (createGroupIconTint != 0) {
            this.createGroupIconTint = createGroupIconTint;
            if (icon != null) {
                icon.setImageTintList(ColorStateList.valueOf(createGroupIconTint));
            }
        }
    }

    /**
     * Sets the background drawable of the tabs.
     *
     * @param tabBackground The background drawable of the tabs to be set.
     */
    public void setTabBackground(Drawable tabBackground) {
        if (tabBackground != null) this.tabBackground = tabBackground;
    }

    /**
     * Sets the background state drawable of the tabs.
     *
     * @param tabBackgroundState The background state drawable of the tabs to be set.
     */
    public void setTabBackgroundState(Drawable tabBackgroundState) {
        if (tabBackgroundState != null) this.tabBackgroundState = tabBackgroundState;
    }

    /**
     * Sets the icon for the close button.
     *
     * @param backButtonIcon The icon for the close button to be set.
     */
    public void setCloseButtonIcon(Drawable backButtonIcon) {
        super.backIcon(backButtonIcon);
    }

    /**
     * Sets the style for the create group view.
     *
     * @param style The style to be applied to the create group view.
     */
    public void setStyle(CreateGroupStyle style) {
        if (style != null) {
            super.setTitleAppearance(style.getTitleTextAppearance());
            super.setTitleFont(style.getTitleTextFont());
            super.setTitleColor(style.getTitleTextColor());
            super.backIconTint(style.getBackIconTintColor());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());

            setEditTextPlaceHolderColor(style.getPlaceHolderTextColor());
            setCreateGroupIconTint(style.getCreateGroupIconTint());
            setTabSelectedTextColor(style.getTabSelectedTextColor());
            setTabTextColor(style.getTabTextColor());
            setTabIndicatorColor(style.getTabIndicatorColor());
            setTabBackgroundTint(style.getTabBackgroundTint());
            setEditTextTextFont(style.getTextFont());
            setEditTextTextAppearance(style.getTextAppearance());
            setEditTextCornerRadius(style.getEditTextCornerRadius());
            setEditTextBackground(style.getEditTextBackgroundColor());
            setTabBackground(style.getTabBackground());
            setTabBackgroundState(style.getTabBackgroundState());
        }
    }

    /**
     * Sets the listener for the create group action.
     *
     * @param onCreateGroup The listener to be set for the create group action.
     */
    public void setOnCreateGroup(OnCreateGroup onCreateGroup) {
        this.onCreateGroup = onCreateGroup;
    }

    /**
     * Sets the listener for error handling.
     *
     * @param onError The listener to be set for error handling.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public interface OnCreateGroup {
        void onCreateGroup(Context context, Group group);
    }

}
