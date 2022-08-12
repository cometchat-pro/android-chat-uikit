package com.cometchatworkspace.components.groups.createGroup;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CometChatCreateGroup extends CometChatListBase {

    View view;

    private TabLayout tabLayout;

    Context context;
    FontUtils fontUtils;
    ImageView icon;
    String title;
    int titleColor, backIconTint, createGroupIconTint, backgroundColor, tabSelectedTextColor, tabTextColor, tabIndicatorColor, tabBackgroundTint;
    boolean showBackButton;
    Drawable createGroupIcon, backButtonIcon, tabBackground, tabBackgroundState;
    Palette palette;
    Typography typography;
    List<String> tabs_List = new ArrayList<>(Arrays.asList(getResources().getString(R.string.type_public), getResources().getString(R.string.type_private), getResources().getString(R.string.type_protected)));
    TextInputLayout nameInputBox;
    TextInputLayout passwordInputBox;
    TextInputEditText nameInput;
    TextInputEditText passwordInput;

    public CometChatCreateGroup(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null, -1);
    }

    public CometChatCreateGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    public CometChatCreateGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();

        view = View.inflate(context, R.layout.cometchat_create_group, null);
        nameInputBox = view.findViewById(R.id.name_input_box);
        passwordInputBox = view.findViewById(R.id.password_input_box);
        nameInput = view.findViewById(R.id.name_input);
        passwordInput = view.findViewById(R.id.password_input);
        tabLayout = view.findViewById(R.id.tabLayout_create_Group);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatCreateGroup,
                0, 0);

        title = a.getString(R.styleable.CometChatCreateGroup_title) != null ? a.getString(R.styleable.CometChatCreateGroup_title) : getResources().getString(R.string.new_group);

        backButtonIcon = a.getDrawable(R.styleable.CometChatCreateGroup_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatCreateGroup_title) : getResources().getDrawable(R.drawable.ic_arrow_back);
        createGroupIcon = a.getDrawable(R.styleable.CometChatCreateGroup_createGroupIcon) != null ? a.getDrawable(R.styleable.CometChatCreateGroup_title) : getResources().getDrawable(R.drawable.ic_check);

        titleColor = a.getColor(R.styleable.CometChatCreateGroup_titleColor, palette.getAccent());
        backIconTint = a.getColor(R.styleable.CometChatCreateGroup_backIconTint, palette.getPrimary());
        createGroupIconTint = a.getColor(R.styleable.CometChatCreateGroup_createGroupIconTint, palette.getPrimary());
        backgroundColor = a.getColor(R.styleable.CometChatCreateGroup_backgroundColor, palette.getBackground());
        tabTextColor = a.getColor(R.styleable.CometChatCreateGroup_tabTextColor, palette.getAccent600());
        tabSelectedTextColor = a.getColor(R.styleable.CometChatCreateGroup_tabSelectedTextColor, palette.getAccent900());
        tabIndicatorColor = a.getColor(R.styleable.CometChatCreateGroup_tabIndicatorColor, palette.getPrimary());
        tabBackgroundTint = a.getColor(R.styleable.CometChatCreateGroup_tabBackgroundTint, palette.getAccent50());

        tabBackground = a.getDrawable(R.styleable.CometChatCreateGroup_tabBackground);
        if (tabBackground==null) {
            tabBackground = getResources().getDrawable(R.drawable.tab_layout_background);
        }
        tabBackgroundState = a.getDrawable(R.styleable.CometChatCreateGroup_tabBackgroundState) != null ? a.getDrawable(R.styleable.CometChatCreateGroup_tabBackgroundState) : getResources().getDrawable(R.drawable.tab_background_state);

        showBackButton = a.getBoolean(R.styleable.CometChatCreateGroup_showBackButton, true);

        setStatusColor(palette.getBackground());

        setTitle(title);
        super.setTitleAppearance(typography.getHeading());
        setBackButtonIcon(backButtonIcon);
        setCreateGroupIcon(createGroupIcon);
        setTitleColor(titleColor);
        setBackIconTint(backIconTint);
        setShowBackButton(showBackButton);
        if (palette.getGradientBackground() != null)
            setBackground(palette.getGradientBackground());
        else
            setBackgroundColor(backgroundColor);
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
        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {

            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
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
                    nameInputBox.setErrorTextColor(ColorStateList.valueOf(palette.getError()));
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
                    passwordInputBox.setErrorTextColor(ColorStateList.valueOf(palette.getError()));
                    passwordInputBox.requestFocus();
                }
            }
        });

    }
    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context,color);

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

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void setBackground(int color) {
        super.setBackground(color);
    }


    @Override
    public void setTitleColor(int titleColor) {
        super.setTitleColor(titleColor);
    }


    public void setBackIconTint(int backIconTint) {
        super.backIconTint(backIconTint);
    }

    public void setShowBackButton(boolean showBackButton) {
        super.showBackButton(showBackButton);
    }


    public void setCreateGroupIcon(Drawable joinGroupIcon) {
        if (icon == null) {
            icon = new ImageView(context);
            icon.setImageDrawable(joinGroupIcon);
            icon.setImageTintList(ColorStateList.valueOf(createGroupIconTint));
            super.addMenuIcon(icon);
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

                    if (nameInput!=null) {
                        if (nameInput.getText().toString().isEmpty()) {
                            nameInputBox.setError(getResources().getString(R.string.err_empty_group_name_message));
                            nameInputBox.setErrorTextColor(ColorStateList.valueOf(palette.getError()));
                            nameInputBox.requestFocus();
                            return;
                        }
                        if (groupType.equalsIgnoreCase(UIKitConstants.GroupTypeConstants.PASSWORD)) {
                            if (passwordInput!=null) {
                                if (passwordInput.getText().toString().isEmpty()) {
                                    passwordInputBox.setError(getResources().getString(R.string.err_password_missing_message));
                                    passwordInputBox.setErrorTextColor(ColorStateList.valueOf(palette.getError()));
                                    passwordInputBox.requestFocus();
                                    return;
                                }
                            }
                        }
                        Group group = new Group("group_" + System.currentTimeMillis(),
                                nameInput.getText().toString(),
                                groupType,
                                passwordInput.getText().toString());
                        createGroup(group);
                    }
                }
            });
        }

    }

    private void createGroup(Group group) {

        CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                    e.onGroupCreate(group);
                }
                ((Activity) context).onBackPressed();

            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                for (CometChatGroupEvents ex : CometChatGroupEvents.groupEvents.values()) {
                    ex.onError(e);
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setTabs();

    }

    public void hidePublicGroup() {
        tabs_List.remove(getResources().getString(R.string.type_public));
    }

    public void hidePrivateGroup() {
        tabs_List.remove(getResources().getString(R.string.type_private));
    }

    public void hidePasswordGroup() {
        tabs_List.remove(getResources().getString(R.string.type_protected));
    }

    public void setEditTextCornerRadius(float EditTextBoxRadius) {
        if (EditTextBoxRadius != 0) {
            passwordInputBox.setBoxCornerRadii(EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius);
            nameInputBox.setBoxCornerRadii(EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius, EditTextBoxRadius);
        }
    }

    public void setEditTextBackground(@ColorInt int EditTextBoxColor) {
        if (EditTextBoxColor != 0 && passwordInputBox != null && nameInputBox != null) {
            passwordInputBox.setBoxBackgroundColor(EditTextBoxColor);
            nameInputBox.setBoxBackgroundColor(EditTextBoxColor);
        }
    }

    public void setEditTextPlaceHolderColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null && nameInput != null) {
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
            passwordInput.setHintTextColor(ColorStateList.valueOf(color));
            nameInputBox.setStartIconTintList(ColorStateList.valueOf(color));
            nameInput.setHintTextColor(ColorStateList.valueOf(color));
        }
    }

    public void setPasswordInputBoxPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null && passwordInput != null) {
            passwordInput.setHint(placeHolderText);
        }
    }

    public void setNameInputBoxPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null && nameInput != null) {
            nameInput.setHint(placeHolderText);
        }
    }

    public void setEditTextTextColor(@ColorInt int color) {
        if (color != 0 && passwordInput != null && nameInput != null) {
            passwordInput.setTextColor(color);
            nameInput.setTextColor(color);
        }
    }

    public void setEditTextTextFont(String fontName) {
        if (fontName != null && passwordInput != null && nameInput != null) {
            passwordInput.setTypeface(fontUtils.getTypeFace(fontName));
            nameInput.setTypeface(fontUtils.getTypeFace(fontName));
        }
    }

    public void setEditTextTextAppearance(int appearance) {
        if (appearance != 0 && passwordInput != null) {
            passwordInput.setTextAppearance(context, appearance);
            nameInput.setTextAppearance(context, appearance);
        }
    }

    public void setEditTextBorderColor(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null)
            passwordInputBox.setBoxStrokeColor(color);
        nameInputBox.setBoxStrokeColor(color);
    }

    public void setEditTextBorderWidth(int width) {
        if (passwordInputBox != null) {
            passwordInputBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            passwordInputBox.setBoxStrokeWidth(width);
            nameInputBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            nameInputBox.setBoxStrokeWidth(width);
        }
    }

    public void setPasswordInputBoxStartIconTint(@ColorInt int color) {
        if (color != 0 && passwordInputBox != null)
            passwordInputBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    public void setNameInputBoxStartIconTint(@ColorInt int color) {
        if (color != 0 && nameInputBox != null)
            nameInputBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    public void setPasswordInputBoxStartIcon(int res) {
        if (res != 0 && passwordInputBox != null) {
            passwordInputBox.setStartIconDrawable(res);
        }
    }

    public void setNameInputBoxStartIcon(int res) {
        if (res != 0 && nameInputBox != null) {
            nameInputBox.setStartIconDrawable(res);
        }
    }

    public void setBackButtonIcon(Drawable backButtonIcon) {
        super.backIcon(backButtonIcon);
    }


}
