package com.cometchat.chatuikit.shared.views.CometChatListBase;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringDef;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * The CometChatListBase class is a custom view that extends MaterialCardView and provides a base layout Conversations, Groups, Users, etc.
 * It serves as the base component from which these components are inherited.
 * It includes various methods to customize the appearance and functionality of the list base.
 */

public class CometChatListBase extends MaterialCardView {

    @StringDef({SearchState.Filter, SearchState.Clear, SearchState.TextChange})

    public @interface SearchState {
        String Filter = "filter";
        String Clear = "clear";
        String TextChange = "textchange";
    }

    private Context context;
    private View baseView;
    private FontUtils fontUtils;

    private LinearLayout parentView;
    private LinearLayout viewContainer;
    private TextInputEditText searchEdit;
    private TextView tvTitle;
    private TextInputLayout rlSearchBox;
    private LinearLayout optionIconsList;
    private ImageView backIcon;
    private OnSearch eventListener;
    private OnBackPress onBackPress;
    private RelativeLayout toolbar;

    public CometChatListBase(Context context) {
        super(context);
        initView(context, null);
    }

    public CometChatListBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CometChatListBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }

    private void initView(Context context, AttributeSet attributeSet) {
        // Inflate the layout for this fragment
        fontUtils = FontUtils.getInstance(context);
        this.context = context;

        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        baseView = View.inflate(context, R.layout.cometchat_list_base, null);
        addView(baseView);
        parentView = baseView.findViewById(R.id.parent_View);
        viewContainer = baseView.findViewById(R.id.view_container);
        tvTitle = baseView.findViewById(R.id.tv_title);
        rlSearchBox = baseView.findViewById(R.id.rl_search_box);
        searchEdit = baseView.findViewById(R.id.search_bar);
        optionIconsList = baseView.findViewById(R.id.icons_layout);
        backIcon = baseView.findViewById(R.id.back_icon);
        toolbar = baseView.findViewById(R.id.toolbar);
        setTitleAppearance(Typography.getInstance().getHeading());
        searchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (!textView.getText().toString().isEmpty()) {
                    if (eventListener != null)
                        eventListener.onSearch(SearchState.Filter, textView.getText().toString());
                }
                return true;
            }
            return false;
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (eventListener != null)
                    eventListener.onSearch(SearchState.TextChange, s.toString());
            }
        });
        backIcon.setOnClickListener(v -> {
            if (onBackPress != null)
                onBackPress.onBack();
            else
                ((Activity) context).onBackPressed();
        });
    }

    /**
     * Sets the background of the view using an array of colors and a gradient orientation.
     *
     * @param colorArray  An array of colors to create the gradient background.
     * @param orientation The orientation of the gradient.
     */
    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(
                orientation,
                colorArray);
        setBackground(gd);
    }

    /**
     * Shows or hides the back button based on the specified visibility.
     *
     * @param isVisible Determines whether the back button should be visible or hidden.
     */
    public void showBackButton(boolean isVisible) {
        if (isVisible) {
            backIcon.setVisibility(View.VISIBLE);
        } else {
            backIcon.setVisibility(View.GONE);
        }
    }

    /**
     * Hides or shows the toolbar based on the specified flag.
     *
     * @param hide Determines whether the toolbar should be hidden or shown.
     */
    public void hideToolbar(boolean hide) {
        if (hide) toolbar.setVisibility(GONE);
        else toolbar.setVisibility(VISIBLE);
    }

    /**
     * Sets the background color of the parent view.
     *
     * @param color The color to set as the background.
     */
    public void setBackground(@ColorInt int color) {
        parentView.setBackgroundColor(color);
    }

    /**
     * Sets the tint color of the back icon.
     *
     * @param color The color to set as the tint color.
     */
    public void backIconTint(@ColorInt int color) {
        if (color != 0 && backIcon != null)
            backIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the drawable resource as the back icon.
     *
     * @param res The drawable resource ID to set as the back icon.
     */
    public void backIcon(@DrawableRes int res) {
        if (res != 0 && backIcon != null)
            backIcon.setImageResource(res);
    }

    /**
     * Sets the drawable as the back icon.
     *
     * @param drawable The drawable to set as the back icon.
     */
    public void backIcon(Drawable drawable) {
        if (drawable != null && backIcon != null)
            backIcon.setImageDrawable(drawable);
    }

    /**
     * Sets a menu view for the toolbar.
     *
     * @param view The view representing the menu.
     */
    public void setMenu(View view) {
        if (view != null && optionIconsList != null) {
            Utils.handleView(optionIconsList, view, true);
        }
    }

    /**
     * Sets the visibility of the menu icon.
     *
     * @param value {@code true} to hide the menu icon, {@code false} to show it.
     */
    public void hideMenuIcon(boolean value) {
        optionIconsList.setVisibility(value ? GONE : VISIBLE);
    }

    /**
     * Sets the placeholder text for the search input field.
     *
     * @param placeholder The text to be set as the placeholder.
     */
    public void setSearchPlaceholderText(String placeholder) {
        if (placeholder != null) {
            rlSearchBox.getEditText().setHint(placeholder);
        }
    }

    protected View getIconAt(int i) {
        if (optionIconsList != null) {
            if (optionIconsList.getChildCount() > 0) {
                return optionIconsList.getChildAt(i);
            }
            return null;
        }
        return null;
    }

    /**
     * Sets the title text for the toolbar.
     *
     * @param title The text to be set as the title.
     */
    public void setTitle(String title) {
        if (tvTitle != null && title != null) {
            if (!title.isEmpty()) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            } else
                tvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the color of the title text in the toolbar.
     *
     * @param color The color value to be set for the title text.
     */
    public void setTitleColor(@ColorInt int color) {
        if (tvTitle != null && color != 0)
            tvTitle.setTextColor(color);
    }

    /**
     * Sets the font for the title text in the toolbar.
     *
     * @param font The name of the font to be applied to the title text.
     */
    public void setTitleFont(String font) {
        if (tvTitle != null && font != null)
            tvTitle.setTypeface(fontUtils.getTypeFace(font));
    }

    /**
     * Sets the appearance style for the title text in the toolbar.
     *
     * @param appearanceStyle The resource ID of the appearance style to be applied to the title text.
     */
    public void setTitleAppearance(int appearanceStyle) {
        if (tvTitle != null && appearanceStyle != 0)
            tvTitle.setTextAppearance(context, appearanceStyle);
    }

    /**
     * Sets the corner radius for the search box.
     *
     * @param searchBoxRadius The corner radius to be applied to the search box.
     */
    public void setSearchCornerRadius(float searchBoxRadius) {
        if (rlSearchBox != null && searchBoxRadius != 0) {
            rlSearchBox.setBoxCornerRadii(searchBoxRadius, searchBoxRadius, searchBoxRadius, searchBoxRadius);
        }
    }

    /**
     * Sets the visibility of the search box.
     *
     * @param hideSearch A boolean indicating whether to hide or show the search box. True to hide the search box, false to show it.
     */
    public void hideSearch(boolean hideSearch) {
        rlSearchBox.setVisibility(hideSearch ? GONE : VISIBLE);
    }

    /**
     * Sets the background color of the search box.
     *
     * @param searchBoxColor The color value to set as the background color of the search box.
     */
    public void setSearchBackground(@ColorInt int searchBoxColor) {
        if (searchBoxColor != 0 && rlSearchBox != null) {
            rlSearchBox.setBoxBackgroundColor(searchBoxColor);
        }
    }

    /**
     * Sets the text of the search bar.
     *
     * @param text The text to be set in the search bar.
     */
    public void setSearchText(String text) {
        if (searchEdit != null && text != null) {
            searchEdit.setText(text);
        }
    }

    /**
     * Sets the color of the search bar placeholder text.
     *
     * @param color The color to be set for the search bar placeholder text.
     */
    public void setSearchPlaceHolderColor(@ColorInt int color) {
        if (color != 0 && searchEdit != null) {
            rlSearchBox.setStartIconTintList(ColorStateList.valueOf(color));
            searchEdit.setHintTextColor(ColorStateList.valueOf(color));
        }
    }

    /**
     * Sets the color of the search bar text.
     *
     * @param color The color to be set for the search bar text.
     */
    public void setSearchTextColor(@ColorInt int color) {
        if (color != 0 && searchEdit != null)
            searchEdit.setTextColor(color);
    }

    /**
     * Sets the font for the search bar text.
     *
     * @param fontName The name of the font to be set for the search bar text.
     */
    public void setSearchTextFont(String fontName) {
        if (fontName != null && searchEdit != null)
            searchEdit.setTypeface(fontUtils.getTypeFace(fontName));
    }

    /**
     * Sets the text appearance for the search bar text.
     *
     * @param appearance The style resource ID for the text appearance.
     */
    public void setSearchTextAppearance(int appearance) {
        if (appearance != 0 && searchEdit != null)
            searchEdit.setTextAppearance(context, appearance);
    }

    /**
     * Sets the border color for the search box.
     *
     * @param color The color to set as the border color.
     */
    public void setSearchBorderColor(@ColorInt int color) {
        if (color != 0 && rlSearchBox != null)
            rlSearchBox.setBoxStrokeColor(color);
    }

    /**
     * Sets the border width for the search box.
     *
     * @param width The width of the border to set.
     */
    public void setSearchBorderWidth(int width) {
        if (rlSearchBox != null && width != -1) {
            rlSearchBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            rlSearchBox.setBoxStrokeWidth(width);
        }
    }

    /**
     * Sets the tint color for the search icon.
     *
     * @param color The color to set as the tint for the search icon.
     */
    public void setSearchIconTint(@ColorInt int color) {
        if (color != 0 && rlSearchBox != null)
            rlSearchBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the icon for the search box.
     *
     * @param res The resource ID of the icon to be set.
     */
    public void setSearchBoxIcon(int res) {
        if (res != 0 && rlSearchBox != null) {
            rlSearchBox.setStartIconDrawable(res);
        }
    }

    /**
     * Adds a ListView to the view container.
     *
     * @param view The ListView to be added.
     */
    protected void addListView(View view) {
        if (viewContainer != null && view != null)
            viewContainer.addView(view);
    }

    /**
     * Sets an event listener for search events.
     *
     * @param eventListener The event listener to be set.
     */
    public void addOnSearchListener(OnSearch eventListener) {
        if (eventListener != null)
            this.eventListener = eventListener;
    }

    /**
     * Sets an event listener for back press events.
     *
     * @param onBackPress The event listener to be set.
     */
    public void addOnBackPressListener(OnBackPress onBackPress) {
        if (onBackPress != null)
            this.onBackPress = onBackPress;
    }

    public OnBackPress getOnBackPress() {
        return onBackPress;
    }

    public ImageView getBackIcon() {
        return backIcon;
    }

    public interface OnSearch {
        void onSearch(@SearchState String state, String text);
    }

    public interface OnBackPress {
        void onBack();
    }

    public LinearLayout getView() {
        return parentView;
    }
}

