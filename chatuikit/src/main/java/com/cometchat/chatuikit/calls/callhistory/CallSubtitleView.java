package com.cometchat.chatuikit.calls.callhistory;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

 class CallSubtitleView extends MaterialCardView {
    private View view;
    private Context context;
    private TextView subtitle;
    private LinearLayout subtitleContainer;
    private ImageView imageView;

    public CallSubtitleView(Context context) {
        super(context);
        init(context);
    }

    public CallSubtitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CallSubtitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.call_subtitle_view, null);
        subtitle = view.findViewById(R.id.subtitle);
        subtitleContainer = view.findViewById(R.id.subtitle_container);
        imageView = view.findViewById(R.id.receipt);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));

        addView(view);
    }


    public void hideSubtitle(boolean hide) {
        subtitle.setVisibility(hide ? GONE : VISIBLE);
    }

    public void setSubtitleTextColor(@ColorInt int color) {
        if (color != 0) subtitle.setTextColor(color);

    }

    public void setSubtitleText(String text) {
        if (text != null && !text.isEmpty()) {
            subtitle.setVisibility(VISIBLE);
            subtitle.setText(text + "");
        } else subtitle.setVisibility(GONE);
    }

    public void setHelperTextFont(String font) {
        if (font != null && !font.isEmpty()) {
            subtitle.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
        }
    }

    public void setHelperTextTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) {
            subtitle.setTextAppearance(context, appearance);
        }
    }

    public void hideReceipt(boolean hide) {
        if (hide) imageView.setVisibility(GONE);
        else imageView.setVisibility(VISIBLE);
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public void setCallIcon(@DrawableRes int icon) {
        if (icon != 0) {
            imageView.setImageResource(icon);
        }
    }

    public void setIconTint(@ColorInt int color) {
        if (color != 0) {
            imageView.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    public ImageView getImageView() {
        return imageView;
    }
}
