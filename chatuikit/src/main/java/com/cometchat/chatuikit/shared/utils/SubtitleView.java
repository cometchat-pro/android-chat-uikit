package com.cometchat.chatuikit.shared.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.CometChatReceipt;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.Receipt;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

public class SubtitleView extends MaterialCardView {
    private View view;
    private Context context;
    private TextView typingIndicator, helperText, subtitle;
    private LinearLayout subtitleContainer;
    private CometChatReceipt cometChatReceipt;

    public SubtitleView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public SubtitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public SubtitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        view = View.inflate(context, R.layout.subtitle_view, null);
        typingIndicator = view.findViewById(R.id.typing_indicator);
        helperText = view.findViewById(R.id.helperText);
        subtitle = view.findViewById(R.id.subtitle);
        subtitleContainer = view.findViewById(R.id.subtitle_container);
        cometChatReceipt = view.findViewById(R.id.receipt);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));

        addView(view);
    }

    public void setHelperText(String text) {
        if (text != null && !text.isEmpty()) {
            helperText.setText(text);
        }
    }

    public void hideSubtitle(boolean hide) {
        subtitle.setVisibility(hide ? GONE : VISIBLE);
    }

    public void setHelperTextColor(@ColorInt int color) {
        if (color != 0) {
            helperText.setTextColor(color);
        }
    }

    public void setHelperTextFont(String font) {
        if (font != null && !font.isEmpty()) {
            helperText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
        }
    }

    public void setHelperTextTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) {
            helperText.setTextAppearance(context, appearance);
        }
    }

    public void showHelperText(boolean show) {
        if (show) {
            helperText.setVisibility(VISIBLE);
        } else {
            helperText.setVisibility(GONE);
        }
    }

    public void setTypingIndicatorColor(@ColorInt int color) {
        if (color != 0) {
            typingIndicator.setTextColor(color);
        }
    }

    public void setTypingIndicatorFont(String font) {
        if (font != null && !font.isEmpty()) {
            typingIndicator.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
        }
    }

    public void setTypingIndicatorTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) {
            typingIndicator.setTextAppearance(context, appearance);
        }
    }

    public void setTypingIndicatorText(String text) {
        if (text != null && !text.isEmpty()) {
            typingIndicator.setText(text);
        }
    }

    public void showTypingIndicator(boolean show) {
        if (show) {
            typingIndicator.setVisibility(VISIBLE);
            subtitleContainer.setVisibility(GONE);
        } else {
            typingIndicator.setVisibility(GONE);
            subtitleContainer.setVisibility(VISIBLE);
        }
    }

    public void setSubtitleTextColor(@ColorInt int color) {
        if (color != 0) subtitle.setTextColor(color);

    }

    public void setSubtitleTextFont(String font) {
        if (font != null && !font.isEmpty())
            typingIndicator.setTypeface(FontUtils.getInstance(context).getTypeFace(font));

    }

    public void setSubtitleTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) typingIndicator.setTextAppearance(context, appearance);

    }

    public void setSubtitleText(String text) {
        if (text != null && !text.isEmpty()) {
            subtitle.setVisibility(VISIBLE);
            subtitle.setText(text + "");
        } else subtitle.setVisibility(GONE);
    }

    public void setReceipt(Receipt receipt) {
        cometChatReceipt.setReceipt(receipt);
    }

    public void hideReceipt(boolean hide) {
        if (hide) cometChatReceipt.setVisibility(GONE);
        else cometChatReceipt.setVisibility(VISIBLE);
    }

    public TextView getTypingIndicator() {
        return typingIndicator;
    }

    public TextView getHelperText() {
        return helperText;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public CometChatReceipt getCometChatReceipt() {
        return cometChatReceipt;
    }
}
