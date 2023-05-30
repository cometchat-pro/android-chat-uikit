package com.cometchat.chatuikit.shared.views.CometChatAudioBubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.resources.utils.AudioPlayer;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

/**
 * This class represents a custom view for displaying an audio message bubble in a chat application. It extends MaterialCardView for styling purposes and implements MediaPlayer for playing the audio.
 * <p>
 * The view contains a play icon, a pause icon, a title, and a subtitle. It can be customized by setting the text, text color, text font, text appearance, icon color, and icon image.
 * <p>
 * The view also has a stopPlaying() method for stopping the audio when necessary.
 *
 * @see AudioPlayer
 * @see MaterialCardView
 */
public class CometChatAudioBubble extends MaterialCardView {

    private Context context;
    private View view;
    private LinearLayout layout;
    private ImageView playIcon;
    private ImageView pauseIcon;
    private TextView title, subtitle;
    private String audioUrl;
    private OnClick onClick;
    private AudioPlayer audioPlayer;

    public CometChatAudioBubble(Context context) {
        super(context);
        init(context);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        view = View.inflate(context, R.layout.audio_bubble, null);
        audioPlayer = AudioPlayer.getAudioPlayer();
        layout = view.findViewById(R.id.parent);
        title = view.findViewById(R.id.tv_title);
        subtitle = view.findViewById(R.id.tv_subtitle);
        playIcon = view.findViewById(R.id.iv_play);
        pauseIcon = view.findViewById(R.id.iv_pause);
        playIcon.setVisibility(GONE);
        pauseIcon.setVisibility(GONE);
        title.setVisibility(GONE);
        subtitle.setVisibility(GONE);
        addView(view);
        playIcon.setOnClickListener(view -> {
            if (onClick != null)
                onClick.onClick();
            else {
                if (audioUrl != null) {
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.isMusicActive()) {
                        audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                            @Override
                            public void onAudioFocusChange(int focusChange) {
                                if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                                    stopPlaying();
                                }
                            }
                        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                    }
                    audioPlayer.resetPlayer();
                    audioPlayer.setAudioUrl(audioUrl, mediaPlayer -> {
                        playIcon.setVisibility(VISIBLE);
                        pauseIcon.setVisibility(GONE);
                    });
                    if (!audioPlayer.isPlaying()) {
                        startPlaying();
                    } else {
                        startPlaying();
                    }
                }
            }
        });
        pauseIcon.setOnClickListener(view -> stopPlaying());
    }

    /**
     * Stops the currently playing media player, if any.
     * it will be paused and the play icon will be set to visible
     * and the pause icon will be set to gone.
     */
    public void stopPlaying() {
        audioPlayer.pausePlayer();
        playIcon.setVisibility(VISIBLE);
        pauseIcon.setVisibility(GONE);
    }

    public void startPlaying() {
        audioPlayer.startPlayer();
        pauseIcon.setVisibility(VISIBLE);
        playIcon.setVisibility(GONE);
    }

    /**
     * Sets the text of the title view and makes it visible.
     * If the given text is null or empty, the title view is not displayed.
     *
     * @param text the text to be displayed in the title view
     */
    public void setTitleText(String text) {
        if (text != null && !text.isEmpty()) {
            title.setVisibility(VISIBLE);
            title.setText(text + "");
        }
    }

    /**
     * Sets the text color of the title view.
     *
     * @param color an integer representation of the color to be set
     */
    public void setTitleTextColor(@ColorInt int color) {
        if (color != 0)
            title.setTextColor(color);
    }

    /**
     * Sets the font of the title text.
     *
     * @param font The name of the font to be used.
     */
    public void setTitleTextFont(String font) {
        if (font != null && !font.isEmpty())
            title.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the appearance of the title text.
     *
     * @param appearance The resource ID of the text appearance to apply.
     */
    public void setTitleTextAppearance(@StyleRes int appearance) {
        if (appearance != 0)
            title.setTextAppearance(context, appearance);
    }

    /**
     * Sets the subtitle text to be displayed.
     *
     * @param text The text to display as the subtitle.
     */
    public void setSubtitleText(String text) {
        if (text != null && !text.isEmpty()) {
            subtitle.setVisibility(VISIBLE);
            subtitle.setText(text + "");
        }
    }

    /**
     * Sets the color of the subtitle text.
     *
     * @param color The color to apply to the subtitle text.
     */
    public void setSubtitleTextColor(@ColorInt int color) {
        if (color != 0)
            subtitle.setTextColor(color);
    }

    /**
     * Sets the font of the subtitle text.
     *
     * @param font The font name to apply to the subtitle text.
     */
    public void setSubtitleTextFont(String font) {
        if (font != null && !font.isEmpty())
            subtitle.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the appearance of the subtitle text.
     *
     * @param appearance The resource ID of the text appearance to apply.
     */
    public void setSubtitleTextAppearance(@StyleRes int appearance) {
        if (appearance != 0)
            subtitle.setTextAppearance(context, appearance);
    }

    /**
     * Sets the tint color of the play icon.
     *
     * @param color The color to apply to the play icon.
     */
    public void setPlayIconTintColor(@ColorInt int color) {
        if (color != 0) {
            playIcon.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    /**
     * Sets the tint color of the pause icon.
     *
     * @param color The color to apply to the pause icon.
     */
    public void setPauseIconTintColor(@ColorInt int color) {
        if (color != 0) {
            pauseIcon.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    /**
     * Sets the play icon of the media player.
     *
     * @param image the drawable resource ID of the play icon.
     */
    public void setPlayIcon(@DrawableRes int image) {
        if (image != 0) {
            playIcon.setImageResource(image);
        }
    }

    /**
     * Sets the pause icon image resource for the media player.
     *
     * @param image the resource ID of the pause icon image to set
     */
    public void setPauseIcon(@DrawableRes int image) {
        if (image != 0) {
            pauseIcon.setImageResource(image);
        }
    }

    /**
     * Sets the audio URL and corresponding title and subtitle texts.
     *
     * @param audioUrl     The URL of the audio file to be played.
     * @param titleText    The title text to be displayed.
     * @param subtitleText The subtitle text to be displayed.
     */
    public void setAudioUrl(String audioUrl, String titleText, String subtitleText) {
        if (audioUrl != null && !audioUrl.isEmpty()) {
            this.audioUrl = audioUrl;
            playIcon.setVisibility(VISIBLE);
        }
        setTitleText(titleText);
        setSubtitleText(subtitleText);
    }

    /**
     * Sets the style of the audio bubble.
     *
     * @param style an instance of the AudioBubbleStyle class containing the style information to be set
     */
    public void setStyle(AudioBubbleStyle style) {
        if (style != null) {
            setTitleTextColor(style.getTitleTextColor());
            setTitleTextFont(style.getTitleTextFont());
            setTitleTextAppearance(style.getTitleTextAppearance());
            setSubtitleTextColor(style.getSubtitleTextColor());
            setSubtitleTextFont(style.getSubtitleTextFont());
            setSubtitleTextAppearance(style.getSubtitleTextAppearance());
            setPlayIconTintColor(style.getPlayIconTint());
            setPauseIconTintColor(style.getPauseIconTint());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets an OnClick listener for the AudioBubble view.
     * It will be triggered when user clicks on a play button
     *
     * @param onClick the OnClick listener to be set
     */
    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public LinearLayout getView() {
        return layout;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public ImageView getDownloadImageView() {
        return playIcon;
    }
}
