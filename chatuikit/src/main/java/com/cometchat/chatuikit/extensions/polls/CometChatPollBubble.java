package com.cometchat.chatuikit.extensions.polls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;
import androidx.cardview.widget.CardView;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.pattern_utils.PatternUtils;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.extensions.Extensions;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.util.ArrayList;

public class CometChatPollBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChatUIKit.getLoggedInUser();
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
    private @DrawableRes
    int radioButtonIcon;
    private MaterialCardView cvMessageBubble;
    private final String TAG = "WhiteboardMessageBubble";
    private @ColorInt
    int percentageTextColor;
    private @ColorInt
    int selectedOptionBackgroundColor;
    private @StyleRes
    int optionTextAppearance;
    private @StyleRes
    int percentageTextAppearance;
    private @ColorInt
    int optionDefaultBackground;

    public CometChatPollBubble(Context context) {
        super(context);
        initComponent(context, null);
    }

    public CometChatPollBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs);
    }

    public CometChatPollBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs);
    }

    public CometChatPollBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context, attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        percentageTextColor = context.getResources().getColor(R.color.primaryTextColor);
        optionDefaultBackground = context.getResources().getColor(R.color.textColorWhite);
        optionTextAppearance = androidx.appcompat.R.style.TextAppearance_AppCompat_Medium;
        percentageTextAppearance = androidx.appcompat.R.style.TextAppearance_AppCompat_Medium;
        selectedOptionBackgroundColor = context.getResources().getColor(R.color.primary);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.PollsMessageBubble, 0, 0);
        float cornerRadius = a.getFloat(R.styleable.PollsMessageBubble_corner_radius, 0);
        int backgroundColor = a.getColor(R.styleable.PollsMessageBubble_backgroundColor, 0);
        int borderColor = a.getColor(R.styleable.PollsMessageBubble_borderColor, 0);
        int borderWidth = a.getInt(R.styleable.PollsMessageBubble_borderWidth, 0);
        radioButtonIcon = R.drawable.shimmer_circle;
        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_polls_bubble, null);

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
        cvMessageBubble.setCardElevation(0);
        cvMessageBubble.setCardBackgroundColor(Color.TRANSPARENT);
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble != null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel().toBuilder().setTopLeftCornerSize(topLeft).setTopRightCornerSize(topRight).setBottomLeftCornerSize(bottomLeft).setBottomRightCornerSize(bottomRight).build());
    }

    public void cornerRadius(float radius) {
        if (cvMessageBubble != null) cvMessageBubble.setRadius(radius);
    }

    public void setRadioButtonIcon(int radioButtonIcon) {
        if (radioButtonIcon != 0) this.radioButtonIcon = radioButtonIcon;
    }

    public MaterialCardView getBubbleView() {
        return cvMessageBubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble != null) {
            GradientDrawable gd = new GradientDrawable(orientation, colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }

    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble != null) {
            if (bgColor != 0) cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }

    public void borderColor(@ColorInt int color) {
        if (color != 0 && cvMessageBubble != null) {
            cvMessageBubble.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        if (cvMessageBubble != null) cvMessageBubble.setStrokeWidth(width);
    }

    public void setPercentageTextColor(int percentageTextColor) {
        if (percentageTextColor != 0) this.percentageTextColor = percentageTextColor;
    }

    public void setSelectedOptionBackgroundColor(int selectedOptionBackgroundColor) {
        if (selectedOptionBackgroundColor != 0)
            this.selectedOptionBackgroundColor = selectedOptionBackgroundColor;
    }

    public void setOptionTextAppearance(int optionTextAppearance) {
        if (optionTextAppearance != 0) this.optionTextAppearance = optionTextAppearance;
    }

    public void setPercentageTextAppearance(int percentageTextAppearance) {
        if (percentageTextAppearance != 0) this.percentageTextAppearance = percentageTextAppearance;
    }

    public void messageObject(BaseMessage baseMessage) {
        optionsLayout.removeAllViews();
        votesCount.setText("0 " + context.getString(R.string.votes));
        ArrayList<String> optionList = new ArrayList<>();
        try {
            JSONObject jsonObject = ((CustomMessage) baseMessage).getCustomData();
            JSONObject options = jsonObject.getJSONObject("options");
            ArrayList<String> voterInfo = Extensions.getVoterInfo(baseMessage, options.length());
            question.setText(jsonObject.getString("question"));
            int userVotedOn = Extensions.userVotedOn(baseMessage, options.length(), loggedInUser.getUid());
            for (int k = 0; k < options.length(); k++) {
                int voteCount = Extensions.getVoteCount(baseMessage);
                if (voteCount == 1)
                    votesCount.setText(voteCount + " " + context.getString(R.string.vote));
                else votesCount.setText(voteCount + " " + context.getString(R.string.votes));

                CardView cardView = new MaterialCardView(context);
                cardView.setRadius(optionsBackgroundCornerRadius);
                cardView.setBackgroundTintList(ColorStateList.valueOf(optionDefaultBackground));
                cardView.setPadding(8, 8, 8, 8);
                ViewGroup.MarginLayoutParams cardParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(0, 0, 0, optionSpacing);
                cardView.setLayoutParams(cardParams);
                cardView.setCardElevation(0);
                RelativeLayout optionsLayout = new RelativeLayout(context);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 20;
                cardView.setLayoutParams(params);
                cardView.addView(optionsLayout);

                ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
                ViewGroup.LayoutParams progressParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                progressBar.setLayoutParams(progressParam);
                progressBar.setScaleY(8);
                progressBar.setAlpha(0.3f);
                TextView textViewPercentage = new TextView(context);
                textViewPercentage.setTextAppearance(context, percentageTextAppearance);
                RelativeLayout.LayoutParams percentageParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                percentageParam.addRule(ALIGN_PARENT_END);
                textViewPercentage.setLayoutParams(percentageParam);
                TextView textViewOption = new TextView(context);
                RelativeLayout.LayoutParams optionParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                optionParam.addRule(LEFT_OF, textViewPercentage.getId());

                textViewPercentage.setPadding(26, 26, 26, 26);
                textViewOption.setPadding(26, 26, 26, 26);
                textViewOption.setTextAppearance(context, optionTextAppearance);
                textViewPercentage.setTextAppearance(context, percentageTextAppearance);

                textViewPercentage.setTextColor(percentageTextColor);
                textViewOption.setTextColor(optionsTextColor);
                if (optionsTextFont != null) {
                    textViewOption.setTypeface(fontUtils.getTypeFace(optionsTextFont));
                    textViewPercentage.setTypeface(fontUtils.getTypeFace(optionsTextFont));
                }
                String optionStr = options.getString(String.valueOf(k + 1));
                textViewOption.setText(optionStr);
                if (voteCount > 0) {
                    int percentage = Math.round((Integer.parseInt(voterInfo.get(k)) * 100) / voteCount);
                    if (percentage > 0) {
                        progressBar.setProgress(percentage);
                        cardView.addView(progressBar);
                        textViewPercentage.setText(percentage + "%");
                    }
                }
                textViewOption.setCompoundDrawablePadding(12);
                if (userVotedOn > 0)
                    textViewOption.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else
                    textViewOption.setCompoundDrawablesWithIntrinsicBounds(radioButtonIcon, 0, 0, 0);

                if (k + 1 == userVotedOn) {
                    progressBar.setProgressTintList(ColorStateList.valueOf(selectedOptionBackgroundColor));
                } else {
                    progressBar.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.accent400)));
                }
                int finalK = k;
                if (this.optionsLayout.getChildCount() != options.length()) {
                    loadingProgress.setVisibility(View.GONE);
                    optionsLayout.addView(textViewPercentage);
                    optionsLayout.addView(textViewOption);
                    this.optionsLayout.addView(cardView);
                }
                if (!baseMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                    optionsLayout.setOnClickListener(v -> {
                        try {
                            String pollsId;
                            if (jsonObject.has("id")) pollsId = jsonObject.getString("id");
                            else pollsId = baseMessage.getId() + "";
                            JSONObject pollsJsonObject = new JSONObject();
                            pollsJsonObject.put("vote", finalK + 1);
                            pollsJsonObject.put("id", pollsId);
                            CometChat.callExtension("polls", "POST", "/v2/vote", pollsJsonObject, new CometChat.CallbackListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject jsonObject1) {
                                    // Voted successfully
                                    loadingProgress.setVisibility(View.VISIBLE);
                                    votesCount.setText("0" + context.getString(R.string.votes));
                                    Log.e(TAG, "onSuccess: " + jsonObject1.toString());
                                    Toast.makeText(context, context.getString(R.string.voted_success), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(CometChatException e) {
                                    // Some error occurred
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "onErrorExtension: " + e.getMessage() + "\n" + e.getCode());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                optionList.add(options.getString(String.valueOf(k + 1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PatternUtils.setHyperLinkSupport(context, question);
    }

    public void setOptionDefaultBackground(int optionDefaultBackground) {
        if (optionDefaultBackground != 0)
            this.optionDefaultBackground = optionDefaultBackground;
    }

    public void question(String str) {
        question.setText(str);
    }

    public void questionColor(@ColorInt int color) {
        if (question != null && color != 0) question.setTextColor(color);
    }

    public void setQuestionTextAppearance(@StyleRes int appearance) {
        if (question != null && appearance != 0) question.setTextAppearance(context, appearance);
    }

    public void questionFont(String font) {
        question.setTypeface(fontUtils.getTypeFace(font));
    }

    public void spaceBetweenOptions(int dp) {
        optionSpacing = dp;
    }

    public void optionsTextColor(@ColorInt int color) {
        if (color != 0) optionsTextColor = color;
    }

    public void optionsTextFont(String font) {
        optionsTextFont = font;
    }

    public void votes(int votes) {
        if (votesCount != null) {
            if (votes == 1) votesCount.setText(votes + " " + getContext().getString(R.string.vote));
            else votesCount.setText(votes + " " + getContext().getString(R.string.votes));
        }
    }

    public void voteTextColor(@ColorInt int color) {
        if (color != 0 && votesCount != null) votesCount.setTextColor(color);
    }

    public void setVoteTextAppearance(@StyleRes int appearance) {
        if (appearance != 0 && votesCount != null) {
            votesCount.setTextAppearance(context, appearance);
        }
    }

    public void voteTextFont(String font) {
        if (votesCount != null) votesCount.setTypeface(fontUtils.getTypeFace(font));
    }

    public void optionsLayoutBackgroundColor(@ColorInt int color) {
        if (color != 0) optionsBackgroundColor = color;
    }

    public void optionsLayoutCornerRadius(int radius) {
        if (radius > -1) optionsBackgroundCornerRadius = radius;
    }

    public void addOptionView(View view) {
        if (optionsLayout != null) {
            optionsLayout.addView(view);
        }
    }

    public int getOptionCount() {
        if (optionsLayout != null) return optionsLayout.getChildCount();
        return 0;
    }

    public void setStyle(PollBubbleStyle style) {
        if (style != null) {
            setOptionTextAppearance(style.getOptionTextAppearance());
            optionsTextColor(style.getOptionTextColor());
            questionColor(style.getQuestionColor());
            setQuestionTextAppearance(style.getQuestionTextAppearance());
            voteTextColor(style.getVotesTextColor());
            setOptionDefaultBackground(style.getOptionDefaultBackgroundColor());
            setPercentageTextColor(style.getPercentageTextColor());
            setPercentageTextAppearance(style.getPercentageTextAppearance());
            setSelectedOptionBackgroundColor(style.getSelectedOptionBackgroundColor());
            cornerRadius(style.getCornerRadius());
            if (style.getDrawableBackground() != null)
                cvMessageBubble.setBackgroundDrawable(style.getDrawableBackground());
            else backgroundColor(style.getBackground());
            borderColor(style.getBorderColor());
            borderWidth(style.getBorderWidth());
        }
    }

}
