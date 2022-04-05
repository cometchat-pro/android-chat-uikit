package com.cometchatworkspace.components.shared.primaryComponents;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private TextInputEditText searchEdit;    //Uses to perform search operations.
    private TextView tvTitle;
    private TextInputLayout rlSearchBox;
    private LinearLayout optionIconsList;
    private ImageView backIcon;
    private OnEventListener eventListener;

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

    public void initView(Context context, AttributeSet attributeSet) {
        // Inflate the layout for this fragment
        fontUtils = FontUtils.getInstance(context);
        this.context = context;

        baseView = View.inflate(context, R.layout.cometchat_list_base, null);
        addView(baseView);
        parentView = baseView.findViewById(R.id.parent_View);
        tvTitle = baseView.findViewById(R.id.tv_title);
        rlSearchBox = baseView.findViewById(R.id.rl_search_box);
        searchEdit = baseView.findViewById(R.id.search_bar);
        optionIconsList = baseView.findViewById(R.id.icons_layout);
        backIcon = baseView.findViewById(R.id.back_icon);

        tvTitle.setTypeface(FontUtils.getInstance(context).getTypeFace(CometChatTheme.Typography.robotoMedium));

        searchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (!textView.getText().toString().isEmpty()) {
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
                eventListener.onSearch(SearchState.TextChange, s.toString());
            }
        });
        backIcon.setOnClickListener(v -> eventListener.onBack());

    }

    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(
                orientation,
                colorArray);
        setBackground(gd);
    }

    public void showBackButton(boolean isVisible) {
        if (isVisible) {
            backIcon.setVisibility(View.VISIBLE);
        } else {
            backIcon.setVisibility(View.GONE);
        }
    }

    public void setBaseBackGroundColor(@ColorInt int color) {
        parentView.setBackgroundColor(color);
    }

    public void backIconTint(@ColorInt int color) {
        if (color != 0 && backIcon != null)
            backIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    public void backIcon(@DrawableRes int res) {
        if (res != 0 && backIcon != null)
            backIcon.setImageResource(res);
    }

    public void backIcon(Drawable res) {
        if (res != null && backIcon != null)
            backIcon.setImageDrawable(res);
    }

    public void addMenuIcon(ImageView view) {
        if (view != null && optionIconsList != null) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.rightMargin = 16;
            view.setLayoutParams(param);

            optionIconsList.addView(view);
        }
    }

    public void addSearchViewPlaceHolder(String placeholder) {
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

    public void setTitle(String title) {
        if (tvTitle != null && title != null) {
            if (!title.isEmpty()) {
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            } else
                tvTitle.setVisibility(View.GONE);
        }
    }

    public void setTitleColor(@ColorInt int color) {
        if (tvTitle != null)
            tvTitle.setTextColor(color);
    }

    public void setTitleFont(String font) {
        if (tvTitle != null)
            tvTitle.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setTitleAppearance(int appearanceStyle) {
        if (tvTitle != null)
            tvTitle.setTextAppearance(context, appearanceStyle);
    }

    public void setSearchCornerRadius(float searchBoxRadius) {
        if (searchBoxRadius != 0) {
            rlSearchBox.setBoxCornerRadii(searchBoxRadius, searchBoxRadius, searchBoxRadius, searchBoxRadius);
        }
    }

    public void hideSearch(boolean IsSearchBoxVisible) {
        if (IsSearchBoxVisible)
            rlSearchBox.setVisibility(View.GONE);
        else
            rlSearchBox.setVisibility(View.VISIBLE);
    }

    public void setSearchBackground(@ColorInt int searchBoxColor) {
        if (searchBoxColor != 0 && rlSearchBox != null) {
            rlSearchBox.setBoxBackgroundColor(searchBoxColor);
        }
    }


    public void setSearchPlaceHolderColor(@ColorInt int color) {
        if (color != 0 && searchEdit != null) {
            rlSearchBox.setStartIconTintList(ColorStateList.valueOf(color));
            searchEdit.setHintTextColor(ColorStateList.valueOf(color));
        }
    }

    public void setSearchTextColor(@ColorInt int color) {
        if (color != 0 && searchEdit != null)
            searchEdit.setTextColor(color);
    }

    public void setSearchTextFont(String fontName) {
        if (fontName != null && searchEdit != null)
            searchEdit.setTypeface(fontUtils.getTypeFace(fontName));
    }

    public void setSearchTextAppearance(int appearance) {
        if (appearance != 0 && searchEdit != null)
            searchEdit.setTextAppearance(context, appearance);
    }

    public void setSearchBorderColor(@ColorInt int color) {
        if (color != 0 && rlSearchBox != null)
            rlSearchBox.setBoxStrokeColor(color);
    }

    public void setSearchBorderWidth(int width) {
        if (rlSearchBox != null) {
            rlSearchBox.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            rlSearchBox.setBoxStrokeWidth(width);
        }
    }

    public void setStartIconTint(@ColorInt int color) {
        if (color != 0 && rlSearchBox != null)
            rlSearchBox.setStartIconTintList(ColorStateList.valueOf(color));
    }

    public void setSearchBoxStartIcon(int res) {
        if (res != 0 && rlSearchBox != null) {
            rlSearchBox.setStartIconDrawable(res);
        }
    }

    protected void addListView(View view) {
        if (parentView != null && view != null)
            parentView.addView(view);
    }

    public void addEventListener(OnEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public interface OnEventListener {
        void onSearch(@SearchState String state, String text);

        void onBack();
    }

}

