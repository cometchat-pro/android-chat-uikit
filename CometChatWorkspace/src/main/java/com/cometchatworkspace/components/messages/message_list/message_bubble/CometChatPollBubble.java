package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
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
import com.cometchatworkspace.resources.utils.pattern_utils.PatternUtils;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CometChatPollBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;
    private int optionsBackgroundColor = Color.WHITE;
    private int optionsTextColor = Color.BLACK;
    private String optionsTextFont = null;
    private int optionsBackgroundCornerRadius = 16;
    private int optionSpacing = 8;
    private TextView question;
    private LinearLayout optionsLayout;
    private TextView votesCount;
    private ProgressBar loadingProgress;

    private MaterialCardView cvMessageBubble;
    private LinearLayout cvMessageBubbleLayout;

    private final String TAG = "WhiteboardMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private Palette palette;
    private Typography typography;

    private int layoutId;

    public CometChatPollBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatPollBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatPollBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatPollBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography= Typography.getInstance();
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.PollsMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.PollsMessageBubble_corner_radius,0);
        int backgroundColor = a.getColor(R.styleable.PollsMessageBubble_backgroundColor,0);
        Drawable messageAvatar = a.getDrawable(R.styleable.PollsMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.PollsMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.PollsMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.PollsMessageBubble_userName);
        int color = a.getColor(R.styleable.PollsMessageBubble_userNameColor,0);
        int borderColor = a.getColor(R.styleable.PollsMessageBubble_borderColor,0);
        int borderWidth = a.getInt(R.styleable.PollsMessageBubble_borderWidth,0);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_polls_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        borderColor(borderColor);
        borderWidth(borderWidth);
    }

    private void initView(View view) {
        addView(view);

        question = view.findViewById(R.id.tv_question);
        optionsLayout = view.findViewById(R.id.options_group);
        votesCount = view.findViewById(R.id.total_votes);
        loadingProgress = view.findViewById(R.id.loading_progressBar);


        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);
        cvMessageBubble.setCardElevation(0);
          //CustomView
//        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
//                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.poll);
//        if(messageTemplate!=null)
//            layoutId = messageTemplate.getView();
////        dataView = messageTemplate.getDataView();
//        if (layoutId != 0) {
//            View customView = LayoutInflater.from(context).inflate(layoutId, null);
//            cvMessageBubbleLayout.setVisibility(View.GONE);
//            if (customView.getParent() != null)
//                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
//            cvMessageBubble.addView(customView);
//            question = customView.findViewById(R.id.tv_question);
//            optionsLayout = customView.findViewById(R.id.options_group);
//            votesCount = customView.findViewById(R.id.total_votes);
//            loadingProgress = customView.findViewById(R.id.loading_progressBar);
//
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

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopLeftCornerSize(topLeft)
                    .setTopRightCornerSize(topRight)
                    .setBottomLeftCornerSize(bottomLeft)
                    .setBottomRightCornerSize(bottomRight)
                    .build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);
    }

    public MaterialCardView getBubbleView() {
        return cvMessageBubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble !=null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }
    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble !=null) {
            if (bgColor != 0)
                cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }


    public void borderColor(@ColorInt int color) {
        if (color!=0 && cvMessageBubble!=null) {
            cvMessageBubble.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setStrokeWidth(width);
    }


    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;

        optionsLayout.removeAllViews();
        votesCount.setText("0 "+context.getString(R.string.votes));
        ArrayList<String> optionList = new ArrayList<>();
        try {
            JSONObject jsonObject = ((CustomMessage) baseMessage).getCustomData();
            JSONObject options = jsonObject.getJSONObject("options");
            ArrayList<String> voterInfo = Extensions.getVoterInfo(baseMessage,options.length());
            question.setText(jsonObject.getString("question"));
            int userVotedOn = Extensions.userVotedOn(baseMessage,options.length(),loggedInUser.getUid());
            for (int k = 0; k < options.length(); k++) {
                int voteCount = Extensions.getVoteCount(baseMessage);
                if (voteCount==1) {
                    votesCount.setText(voteCount +" "+context.getString(R.string.vote));
                } else {
                    votesCount.setText(voteCount +" "+context.getString(R.string.votes));
                }
                MaterialCardView cardView = new MaterialCardView(context);
                cardView.setRadius(optionsBackgroundCornerRadius);
                cardView.setCardBackgroundColor(optionsBackgroundColor);
                cardView.setPadding(8,8,8,8);
                ViewGroup.MarginLayoutParams cardParams = new
                        ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(0,0,0,optionSpacing);
                cardView.setLayoutParams(cardParams);
                cardView.setCardElevation(0);
                RelativeLayout optionsLayout = new RelativeLayout(context);
                optionsLayout.setPadding(8,8,8,8);
                optionsLayout.setBackgroundColor(palette.getPrimary());
                optionsLayout.setBackgroundTintList(
                        ColorStateList.valueOf(palette.getSecondary()));
                cardView.addView(optionsLayout);

                ProgressBar progressBar = new ProgressBar(context,null, android.R.attr.progressBarStyleHorizontal);
                ViewGroup.LayoutParams progressParam = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                progressBar.setLayoutParams(progressParam);
                progressBar.setScaleY(8);
                progressBar.setAlpha(0.3f);
                TextView textViewPercentage = new TextView(context);
                textViewPercentage.setTextAppearance(R.style.Subtitle1);
                RelativeLayout.LayoutParams percentageParam = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                percentageParam.addRule(ALIGN_PARENT_END);
                textViewPercentage.setLayoutParams(percentageParam);
                TextView textViewOption = new TextView(context);
                RelativeLayout.LayoutParams optionParam = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                optionParam.addRule(LEFT_OF,textViewPercentage.getId());

                textViewPercentage.setPadding(16, 8, 0, 4);
                textViewOption.setPadding(16, 4, 0, 4);
                textViewOption.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
                textViewPercentage.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);

                textViewPercentage.setTextColor(optionsTextColor);
                textViewOption.setTextColor(optionsTextColor);
                if (optionsTextFont!=null) {
                    textViewOption.setTypeface(fontUtils.getTypeFace(optionsTextFont));
                    textViewPercentage.setTypeface(fontUtils.getTypeFace(optionsTextFont));
                }
                String optionStr = options.getString(String.valueOf(k + 1));
                textViewOption.setText(optionStr);
                if (voteCount>0) {
                    int percentage = Math.round((Integer.parseInt(voterInfo.get(k)) * 100) / voteCount);
                    if (percentage > 0) {
                        progressBar.setProgress(percentage);
                        cardView.addView(progressBar);
                        textViewPercentage.setText(percentage + "% ");
                    }
                }
                textViewOption.setCompoundDrawablePadding(12);
                if (userVotedOn>0)
                    textViewOption.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                else
                    textViewOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shimmer_circle,0,0,0);

                if (k+1==userVotedOn) {
                    progressBar.setProgressTintList(
                                ColorStateList.valueOf(context.getColor(R.color.primary)));
                } else {
                    progressBar.setProgressTintList(
                            ColorStateList.valueOf(context.getColor(R.color.accent100))
                    );
                }
                int finalK = k;
                if (this.optionsLayout.getChildCount()!=options.length()) {
                    loadingProgress.setVisibility(View.GONE);
                    optionsLayout.addView(textViewPercentage);
                    optionsLayout.addView(textViewOption);
                    this.optionsLayout.addView(cardView);
                }
                if (!baseMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                    optionsLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                String pollsId;
                                if (jsonObject.has("id"))
                                    pollsId = jsonObject.getString("id");
                                else
                                    pollsId = baseMessage.getId() + "";
                                JSONObject pollsJsonObject = new JSONObject();
                                pollsJsonObject.put("vote", finalK + 1);
                                pollsJsonObject.put("id", pollsId);
                                CometChat.callExtension("polls", "POST", "/v2/vote",
                                        pollsJsonObject, new CometChat.CallbackListener<JSONObject>() {
                                            @Override
                                            public void onSuccess(JSONObject jsonObject) {
                                                // Voted successfully
                                                loadingProgress.setVisibility(View.VISIBLE);
                                                votesCount.setText("0" + context.getString(R.string.votes));
                                                Log.e(TAG, "onSuccess: " + jsonObject.toString());
                                                Toast.makeText(context, context.getString(R.string.voted_success), Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(CometChatException e) {
                                                // Some error occured
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                Log.e(TAG, "onErrorExtension: " + e.getMessage() + "\n" + e.getCode());
                                            }
                                        });
                            } catch (Exception e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                            }
                        }
                    });
                }
                optionList.add(options.getString(String.valueOf(k + 1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PatternUtils.setHyperLinkSupport(context,question);

        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (messageBubbleListener!=null)
                    messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
    }

    public void question(String str) {
        question.setText(str);
    }
    public void questionColor(@ColorInt int color) {
        if (question!=null && color!=0)
        question.setTextColor(color);
    }
    public void questionFont(String font) {
        question.setTypeface(fontUtils.getTypeFace(font));
    }

    public void options(Map<String,Integer> options) {
        if (optionsLayout!=null) {
            optionsLayout.removeAllViews();
            long totalCount = options.keySet()
                    .stream()
                    .filter(x -> options.get(x)>0)
                    .mapToInt(x -> options.get(x))
                    .sum();

            Log.e(TAG, "options: "+totalCount );
            for (String optionStr : options.keySet()) {
                MaterialCardView cardView = new MaterialCardView(context);
                cardView.setRadius(optionsBackgroundCornerRadius);
                cardView.setCardBackgroundColor(optionsBackgroundColor);
                cardView.setPadding(8,8,8,8);
                ViewGroup.MarginLayoutParams cardParams = new
                        ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(0,0,0,optionSpacing);
                cardView.setLayoutParams(cardParams);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setPadding(8,8,8,8);
                cardView.addView(linearLayout);

                TextView textViewPercentage = new TextView(context);
                TextView textViewOption = new TextView(context);
                textViewPercentage.setPadding(16, 4, 0, 4);
                textViewOption.setPadding(16, 4, 0, 4);
                textViewOption.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
                textViewPercentage.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);

                textViewPercentage.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
                textViewOption.setTextColor(context.getResources().getColor(R.color.primaryTextColor));

                textViewOption.setText(optionStr);
                if (totalCount>0) {
                    int percentage = Math.round((options.get(optionStr) * 100) /
                            totalCount);
                    if (percentage > 0)
                        textViewPercentage.setText(percentage + "% ");
                }

                linearLayout.addView(textViewOption);
                linearLayout.addView(textViewPercentage);
                optionsLayout.addView(cardView);

            }
        }
    }

    public void spaceBetweenOptions(int dp) {
        optionSpacing = dp;
    }

    public void optionsTextColor(@ColorInt int color) {
        if (color!=0)
            optionsTextColor = color;
    }

    public void optionsTextFont(String font) {
        optionsTextFont = font;
    }

    public void votes(int votes) {
        if (votesCount!=null) {
            if (votes==1)
                votesCount.setText(votes+" "+getContext().getString(R.string.vote));
            else
                votesCount.setText(votes+" "+getContext().getString(R.string.votes));
        }
    }

    public void voteTextColor(@ColorInt int color) {
        if (color!=0 && votesCount!=null)
            votesCount.setTextColor(color);
    }

    public void voteTextFont(String font) {
        if (votesCount!=null)
            votesCount.setTypeface(fontUtils.getTypeFace(font));
    }

    public void optionsLayoutBackgroundColor(@ColorInt int color) {
        optionsBackgroundColor = color;
    }

    public void optionsLayoutCornerRadius(int radius) {
        optionsBackgroundCornerRadius = radius;
    }


    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    public void addOptionView(View view) {
        if (optionsLayout!=null) {
            optionsLayout.addView(view);
        }
    }

    public int getOptionCount() {
        if (optionsLayout!=null)
            return optionsLayout.getChildCount();
        return 0;
    }
}
