package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.composer.audioVisualizer.AudioRecordView;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TimeAlignment;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class CometChatAudioBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;
    private TextView subtitleTv;
    private TextView titleTv;
    private ImageView button;

    private AudioRecordView audioRecordView;

    private MediaPlayer mediaPlayer;

    private final String alignment = Alignment.RIGHT;

    private final String TAG = "AudioMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private int reactionStrokeColor;
    private Palette palette;
    private Typography typography;

    private int borderWidth = 0;

    private int borderColor = 0;

    private String url;

    private int layoutId;

    public CometChatAudioBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography=Typography.getInstance();
        reactionStrokeColor = palette.getPrimary();
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.AudioMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.AudioMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.AudioMessageBubble_backgroundColor,0);

        int borderWidth = a.getInt(R.styleable.AudioMessageBubble_borderWidth,0);
        int borderColor = a.getColor(R.styleable.AudioMessageBubble_borderColor,0);

        String title = a.getString(R.styleable.AudioMessageBubble_title);
        int titleColor = a.getColor(R.styleable.AudioMessageBubble_titleColor,0);
        String subtitle = a.getString(R.styleable.AudioMessageBubble_subtitle);
        int subtitleColor = a.getColor(R.styleable.AudioMessageBubble_subtitleColor,0);

        Drawable icon = a.getDrawable(R.styleable.AudioMessageBubble_icon);
        int iconVisibility = a.getInt(R.styleable.AudioMessageBubble_iconVisibility,View.VISIBLE);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_audio_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);

        title(title);
        titleColor(titleColor);
        subtitle(subtitle);
        subtitleColor(subtitleColor);
        icon(icon);
        iconVisibility(iconVisibility);
        borderColor(borderColor);
        borderWidth(borderWidth);

    }

    private void initView(View view) {
        addView(view);
        mediaPlayer = new MediaPlayer();
        audioRecordView = view.findViewById(R.id.audio_visualizer);
        titleTv = view.findViewById(R.id.title);
        subtitleTv = view.findViewById(R.id.audiolength_tv);
        button = view.findViewById(R.id.playBtn);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);

        //CustomView
//        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
//                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.audio);
//        if(messageTemplate!=null)
//            layoutId = messageTemplate.getView();
////        dataView = messageTemplate.getDataView();
//        if (layoutId != 0) {
//            View customView = LayoutInflater.from(context).inflate(layoutId,null);
//            cvMessageBubbleLayout.setVisibility(View.GONE);
//            if (customView.getParent()!=null)
//                ((ViewGroup)customView.getParent()).removeAllViewsInLayout();
//            cvMessageBubble.addView(customView);
//            button = customView.findViewById(R.id.playBtn);
//            customView.setOnLongClickListener(new OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    messageBubbleListener.onLongCLick(baseMessage);
//                    return true;
//                }
//            });
//            customView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    messageBubbleListener.onClick(baseMessage);
//                }
//            });
//        }
    }


    public void iconVisibility(int buttonVisibility) {
        if (button!=null)
            button.setVisibility(buttonVisibility);
    }

    public void icon(Drawable buttonDrawable) {
        if (button!=null && buttonDrawable!=null)
            button.setImageDrawable(buttonDrawable);
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight).build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);
    }

    public MaterialCardView getBubbleView() {
        return cvMessageBubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble!=null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }
    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble!=null) {
            if (bgColor != 0)
                cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }

    public void audioURL(String url) {
        if (url!=null) {
            this.url = url;
            baseMessage = new BaseMessage();
            Attachment attachment = new Attachment();
            if (titleTv.getText().toString().isEmpty())
                attachment.setFileName("Audio File");
            else
                attachment.setFileName(titleTv.getText().toString());
            attachment.setFileExtension("mp3");
            attachment.setFileMimeType("audio/mpeg");
            attachment.setFileSize(0);
            attachment.setFileUrl(url);
            ((MediaMessage) baseMessage).setAttachment(attachment);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageBubbleListener!=null)
                        messageBubbleListener.onClick(baseMessage);
                }
            });
        }
    }

    public void borderColor(@ColorInt int color) {
        borderColor = color;
        if (cvMessageBubble!=null) {
            if (color!=0)
                cvMessageBubble.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        borderWidth = width;
        if (cvMessageBubble!=null)
            cvMessageBubble.setStrokeWidth(width);
    }

    public void title(String title) {
        if (titleTv!=null && title!=null && !title.isEmpty())
            titleTv.setText(title);
    }

    public void titleColor(@ColorInt int color) {
        if (color!=0 && titleTv!=null)
            titleTv.setTextColor(color);
    }

    public void subtitle(String title) {
        if (subtitleTv!=null)
            subtitleTv.setText(title);
    }

    public void subtitleColor(@ColorInt int color) {
        if (color!=0 && subtitleTv!=null)
            subtitleTv.setTextColor(color);
    }

    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;

        if (((MediaMessage)baseMessage).getAttachment()!=null) {
            subtitleTv.setText(Utils.getFileSize(((MediaMessage) baseMessage).getAttachment().getFileSize()));
            button.setVisibility(View.VISIBLE);
        } else {
            subtitleTv.setText("-");
            button.setVisibility(View.GONE);
        }

        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (messageBubbleListener!=null)
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
        MediaMessage mediaMessage = (MediaMessage) baseMessage;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaMessage.getAttachment()!=null) {
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager.isMusicActive()) {
                        audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                            @Override
                            public void onAudioFocusChange(int focusChange) {
                                if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                                    stopPlaying();
                                }
                            }
                        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                    }
                    mediaPlayer.reset();
                    try {
                        String mediaUrl = mediaMessage.getAttachment().getFileUrl();
                        mediaPlayer.setDataSource(mediaUrl);
                        mediaPlayer.prepare();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                button.setImageResource(R.drawable.ic_play_2x);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
//                        audioRecordView.recreate();
//                        int rate = Visualizer.getMaxCaptureRate();
//                        Visualizer visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
//                        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
//                            @Override
//                            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
//                            }
//
//                            @Override
//                            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
//                                float intensity = ((float) fft[0] + 128f) / 256;
//                                Log.e(TAG, "onFftDataCapture: "+visualizer.getFft(fft));
//                                audioRecordView.update(visualizer.getFft(fft));
//                            }},rate , false, true); // waveform not freq data
                        button.setImageResource(R.drawable.ic_pause_24dp);
//                        visualizer.setEnabled(true);
                    } else {
                        mediaPlayer.pause();
                        button.setImageResource(R.drawable.ic_play_2x);
                    }
                }
                messageBubbleListener.onClick(baseMessage);
            }
        });
    }

    public void stopPlaying() {
        if (mediaPlayer!=null) {
            mediaPlayer.pause();
            button.setImageResource(R.drawable.ic_play_2x);
        }
    }
    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    public void buttonBackgroundTint(int color) {
        if (button!=null && color!=0)
            button.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void buttonTint(int color) {
        if (button!=null && color!=0)
            button.setImageTintList(ColorStateList.valueOf(color));
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility==View.GONE || visibility==View.INVISIBLE) {
            if (button!=null)
                button.setImageResource(R.drawable.ic_play_2x);
            if (mediaPlayer!=null) {
                mediaPlayer.stop();
            }
        }
    }
}
