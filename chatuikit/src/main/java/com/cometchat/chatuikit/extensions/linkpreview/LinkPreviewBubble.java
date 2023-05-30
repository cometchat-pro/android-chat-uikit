package com.cometchat.chatuikit.extensions.linkpreview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.bumptech.glide.Glide;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;


public class LinkPreviewBubble extends MaterialCardView {
    private Context context;
    private ShapeableImageView image;
    private TextView title;
    private TextView subtitle;
    private ImageView videoPlayBtn;
    private LinearLayout layout;
    private ShapeableImageView shapeableImageView;

    public LinkPreviewBubble(Context context) {
        super(context);
        init(context);
    }

    public LinkPreviewBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkPreviewBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.link_message_bubble, null);
        image = view.findViewById(R.id.link_img);
        title = view.findViewById(R.id.link_title);
        subtitle = view.findViewById(R.id.link_subtitle);
        videoPlayBtn = view.findViewById(R.id.videoLink);
        shapeableImageView = view.findViewById(R.id.play_btn_bg);
        layout = view.findViewById(R.id.place_holder);
        addView(view);
    }

    public void setUrl(String url) {
        if (url != null) {
            if (url.contains("youtu.be") || url.contains("youtube"))
                playButtonVisible(View.VISIBLE);
            else playButtonVisible(View.GONE);
        }
    }

    public void title(String str) {
        if (title != null) title.setText(str);
    }

    public void titleColor(@ColorInt int color) {
        if (title != null && color != 0) title.setTextColor(color);
    }

    public void titleAppearance(@StyleRes int appearance) {
        if (title != null && appearance != 0) title.setTextAppearance(context, appearance);
    }

    public void titleFont(String font) {
        if (title != null && font != null && !font.isEmpty())
            title.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    public void subtitle(String subStr) {
        if (subtitle != null) subtitle.setText(subStr + "");
    }

    public void subtitleColor(@ColorInt int color) {
        if (subtitle != null && color != 0) {
            subtitle.setTextColor(color);
        }
    }

    public void subtitleFont(String font) {
        if (subtitle != null && font != null && !font.isEmpty())
            subtitle.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    public void subtitleAppearance(@StyleRes int appearance) {
        if (subtitle != null && appearance != 0) subtitle.setTextAppearance(context, appearance);
    }

    public void addChildView(View view) {
        layout.addView(view);
    }

    public void playButtonVisible(int visibility) {
        if (videoPlayBtn != null) {
            shapeableImageView.setVisibility(visibility);
            videoPlayBtn.setVisibility(visibility);
        }
    }

    public void image(Drawable drawable) {
        if (image != null && drawable != null) image.setImageDrawable(drawable);
    }

    public void image(String url) {
        if (image != null && url != null) Glide.with(context).load(url).into(image);
    }

    public void setPlayButtonBackgroundColor(int color) {
        if (color != 0) {
            shapeableImageView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }

    public void setPlayIconTint(int color) {
        if (color != 0) {
            image.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }

    public void setStyle(LinkPreviewBubbleStyle style) {
        if (style.getDrawableBackground() != null)
            this.setBackground(style.getDrawableBackground());
        else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
        if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
        if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
        if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
        titleColor(style.getTitleColor());
        titleFont(style.getTitleFont());
        titleAppearance(style.getTitleAppearance());
        subtitleColor(style.getSubTitleColor());
        subtitleFont(style.getSubTitleFont());
        subtitleAppearance(style.getSubTitleAppearance());
        setPlayButtonBackgroundColor(style.getPlayIconBackgroundTint());
        setPlayIconTint(style.getPlayIconBackgroundTint());
    }

}
