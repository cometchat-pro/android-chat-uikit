package com.cometchat.chatuikit.extensions.sticker.keyboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.extensions.sticker.keyboard.listener.StickerClickListener;
import com.cometchat.chatuikit.extensions.sticker.keyboard.model.Sticker;
//import com.facebook.react.fabric.mounting.mountitems.PreAllocateViewMountItem;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.extensions.sticker.keyboard.adapter.StickerTabAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CometChatStickerKeyboard extends MaterialCardView implements StickerClickListener {

    private final Context context;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private StickerTabAdapter adapter;

    private HashMap<String, List<Sticker>> stickerMap = new HashMap<>();

    private StickerClickListener stickerClickListener;

    private LinearLayout loadingLayout;

    private LinearLayout noGroupView;

    private View emptyView = null;

    private LinearLayout customLayout;

    private LinearLayout stickerLayout;

    private TextView emptyStateText;

    private int errorStateTextAppearance = 0;

    private int errorMessageColor = 0;

    private String errorText = null;

    private View errorView = null;

    private View loadingView = null;

    private ImageView loadingIcon;

    private CometChatTheme cometChatTheme;

    public CometChatStickerKeyboard(Context context) {
        super(context);
        this.context = context;
        initViewComponent(context, null, -1, -1);
    }

    public CometChatStickerKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViewComponent(context, attrs, -1, -1);
    }

    public CometChatStickerKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViewComponent(context, attrs, defStyleAttr, -1);
    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        View view = View.inflate(context, R.layout.cometchat_sticker_view, null);
        addView(view);
        cometChatTheme = CometChatTheme.getInstance(context);
        viewPager = this.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new StickerTabAdapter(context, ((FragmentActivity) context).getSupportFragmentManager());
        loadingLayout = view.findViewById(R.id.loading_view);
        customLayout = view.findViewById(R.id.empty_view);
        stickerLayout = view.findViewById(R.id.stickers_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noGroupView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        emptyStateTextAppearance(cometChatTheme.getTypography().getHeading());
        emptyStateText(context.getResources().getString(R.string.no_stickers));
        errorStateTextAppearance(cometChatTheme.getTypography().getText1());
        emptyStateTextColor(cometChatTheme.getPalette().getAccent400());
        setLoadingIconTintColor(cometChatTheme.getPalette().getAccent());
        for (String str : stickerMap.keySet()) {
            Bundle bundle = new Bundle();
            StickerFragment stickersFragment = new StickerFragment();
            bundle.putParcelableArrayList("stickerList", (ArrayList<? extends Parcelable>) stickerMap.get(str));
            stickersFragment.setArguments(bundle);
            stickersFragment.setStickerClickListener(stickerClickListener);
            adapter.addFragment(stickersFragment, str, stickerMap.get(str).get(0).getUrl());
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(createTabItemView(adapter.getPageIcon(i)));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View createTabItemView(String imgUri) {
        ImageView imageView = new ImageView(context);
        TabLayout.LayoutParams params = new TabLayout.LayoutParams(72, 72);
        imageView.setLayoutParams(params);
        try {
            Glide.with(context).load(imgUri).placeholder(R.drawable.cc_progress_drawable).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

    public void setStickerClickListener(StickerClickListener stickerClickListener) {
        this.stickerClickListener = stickerClickListener;
    }

    public void setData(HashMap<String, List<Sticker>> stickers) {
        this.stickerMap = stickers;
        reload();
    }

    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.groups));
    }

    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);
    }

    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    public void setEmptyStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                emptyView = View.inflate(context, id, null);
            } catch (Exception e) {
                emptyView = null;
                e.printStackTrace();
            }
        }
    }

    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * setErrorStateView is method allows you to set layout, show when there is a error
     * if Group want to set Error layout other wise it will load default layout
     */
    public void setErrorStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                errorView = View.inflate(context, id, null);
            } catch (Exception e) {
                errorView = null;
                e.printStackTrace();
            }
        }
    }

    public void setLoadingStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                loadingView = View.inflate(context, id, null);
            } catch (Exception e) {
                loadingView = null;
            }
        }
    }

    public void setState(UIKitConstants.States states) {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            noGroupView.setVisibility(View.GONE);
            customLayout.setVisibility(GONE);
            stickerLayout.setVisibility(View.VISIBLE);
        } else if (UIKitConstants.States.ERROR.equals(states)) {
            showError();
        } else if (UIKitConstants.States.EMPTY.equals(states)) {
            if (emptyView != null) {
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(emptyView);
            } else {
                noGroupView.setVisibility(View.VISIBLE);
            }
            stickerLayout.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            noGroupView.setVisibility(View.GONE);
            stickerLayout.setVisibility(View.VISIBLE);
            customLayout.setVisibility(GONE);
        }
    }

    private void showError() {
        String errorMessage;
        if (errorText != null) errorMessage = errorText;
        else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (errorView != null) {
            customLayout.removeAllViews();
            customLayout.addView(errorView);
            customLayout.setVisibility(VISIBLE);
        } else {
            customLayout.setVisibility(GONE);
            if (getContext() != null) {
                new CometChatDialog(context, 0, errorStateTextAppearance, cometChatTheme.getTypography().getText3(), cometChatTheme.getPalette().getAccent900(), 0, errorMessageColor, errorMessage, "", "", getResources().getString(R.string.okay), "", cometChatTheme.getPalette().getPrimary(), cometChatTheme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }, 0, false);
            }
        }
    }

    public void setStyle(StickerKeyboardStyle style){
        if (style != null) {
            setLoadingIconTintColor(style.getLoadingIconTint());
            emptyStateTextAppearance(style.getEmptyTextAppearance());
            errorStateTextAppearance(style.getErrorTextAppearance());
            emptyStateTextFont(style.getEmptyTextFont());
            emptyStateTextColor(style.getEmptyTextColor());
            errorStateTextColor(style.getErrorTextColor());

            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
        }
    }

    public void reload() {
        initViewComponent(context, null, -1, -1);
    }

    @Override
    public void onClickListener(Sticker sticker) {
        stickerClickListener.onClickListener(sticker);
    }
}
