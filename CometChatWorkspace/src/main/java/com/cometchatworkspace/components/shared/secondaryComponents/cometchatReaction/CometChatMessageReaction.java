package com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.common.extensions.Reactions.CometChatReactionInfoActivity;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatUIKitHelper;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;

public class CometChatMessageReaction extends RelativeLayout {

    private Context context;
    private View view;

    private int activeColor;
    private int passiveColor;
    private int borderColor;
    private int backgroundColor;
    private int textColor;
    private int borderWidth;
    private float borderRadius;
    private int reactionGravity;
    private FlexboxLayout parentView;
    private MessageReactionsStyle messageReactionsStyle;
    private OnReactionClickListener eventListener;

    public CometChatMessageReaction(Context context) {
        super(context);
        initComponent(context, null);
    }

    public CometChatMessageReaction(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs);
    }

    public CometChatMessageReaction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs);
    }

    private void initComponent(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatMessageReaction,
                0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            passiveColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_passiveColor,
                    getContext().getColor(android.R.color.transparent));
            activeColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_activeColor,
                    getContext().getColor(R.color.colorPrimary));
            borderColor = a.getColor(R.styleable.CometChatMessageReaction_borderColor,
                    getContext().getColor(R.color.textColorWhite));
        }
        borderRadius = a.getFloat(R.styleable.CometChatMessageReaction_borderRadius,
                11);
        borderWidth = a.getInt(R.styleable.CometChatMessageReaction_borderWidth,
                2);
        textColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_textColor,
                0);
        backgroundColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_backgroundColor, 0);
        reactionGravity = a.getInt(R.styleable.CometChatMessageReaction_messageReaction_gravity, Gravity.END);
        view = LayoutInflater.from(context).inflate(R.layout.cometchat_message_reactions, null);
        parentView = view.findViewById(R.id.parent_view);
        addView(view);
    }


    public void setMessage(BaseMessage baseMessage) {
        parentView.setVisibility(GONE);
        HashMap<String, String> reactionOnMessage = Extensions.getReactionsOnMessage(baseMessage);
        if (reactionOnMessage != null && reactionOnMessage.size() > 0) {

            parentView.setVisibility(VISIBLE);
            parentView.removeAllViews();
            for (String str : reactionOnMessage.keySet()) {

                View view = View.inflate(context, R.layout.cc_reaction_item_layout, null);
                MaterialCardView card = view.findViewById(R.id.reaction_card);
                LinearLayout reactionLayout = view.findViewById(R.id.reactions_layout);
                TextView reaction = view.findViewById(R.id.reaction);
                card.setStrokeWidth(borderWidth);
                card.setStrokeColor(getResources().getColor(R.color.textColorWhite));
                if (!"0".equalsIgnoreCase(reactionOnMessage.get(str)))
                    reaction.setText(str + " " + reactionOnMessage.get(str));
                if (Extensions.isReactedByLoggedInUser(baseMessage.getMetadata(), str)) {
                    card.setStrokeWidth(0);
                    if (messageReactionsStyle != null) {
                        if (messageReactionsStyle.getActiveBackground() != 0)
                            reactionLayout.setBackgroundColor(messageReactionsStyle.getActiveBackground());
                        else
                            reactionLayout.setBackgroundColor(activeColor);
                        if (messageReactionsStyle.getBackground() != 0)
                            reaction.setTextColor(messageReactionsStyle.getBackground());
                        else
                            reaction.setTextColor(getResources().getColor(R.color.textColorWhite));
                    } else {
                        reaction.setTextColor(getResources().getColor(R.color.textColorWhite));
                        reactionLayout.setBackgroundColor(activeColor);
                    }

                } else {
                    card.setStrokeWidth(borderWidth);
                    if (messageReactionsStyle != null) {
                        if (messageReactionsStyle.getBackground() != 0)
                            reactionLayout.setBackgroundColor(messageReactionsStyle.getBackground());
                        if (messageReactionsStyle.getBorderWidth() != 0)
                            card.setStrokeWidth(messageReactionsStyle.getBorderWidth());
                        if (messageReactionsStyle.getBorderColor() != 0)
                            card.setStrokeColor(messageReactionsStyle.getBorderColor());
                        if (messageReactionsStyle.getTextColor() != 0)
                            reaction.setTextColor(messageReactionsStyle.getTextColor());
                    }
                }
                parentView.addView(view);

                card.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent intent = new Intent(context, CometChatReactionInfoActivity.class);
                        intent.putExtra(UIKitConstants.IntentStrings.REACTION_INFO, baseMessage.getMetadata().toString());
                        context.startActivity(intent);
                        return true;
                    }
                });
                card.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Reaction reaction = new Reaction(str, Integer.parseInt(reactionOnMessage.get(str)));
                        eventListener.onReactionClick(reaction, baseMessage.getId());
                    }
                });
            }
            if (reactionOnMessage != null && reactionOnMessage.size() > 0) {
                View addReactionView = View.inflate(context, R.layout.cc_add_reaction_layout, null);
                MaterialCardView addReaction = addReactionView.findViewById(R.id.add_reaction);
                LinearLayout addReactionLayout = addReactionView.findViewById(R.id.add_reaction_layout);
                ImageView addReactionImage = addReactionView.findViewById(R.id.add_reaction_image);
                if (messageReactionsStyle != null) {
                    if (messageReactionsStyle.getAddReactionIconBackground() != 0)
                        addReactionLayout.setBackgroundColor(messageReactionsStyle.getAddReactionIconBackground());
                    if (messageReactionsStyle.getAddReactionIconTint() != 0) {
                        addReactionImage.setImageTintList(ColorStateList.valueOf(messageReactionsStyle.getAddReactionIconTint()));
                    }
                }
                addReaction.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CometChatUIKitHelper.onMessageReact(baseMessage, new Reaction("add_emoji", 0));
                    }
                });
                parentView.addView(addReactionView);
            }
        } else {
            parentView.removeAllViews();
            parentView.setVisibility(GONE);
        }
    }

    public void setPassiveColor(@ColorInt int color) {
        this.passiveColor = color;
        invalidate();
    }

    public void setActiveColor(@ColorInt int color) {
        this.activeColor = color;
        invalidate();
    }

    public void setBorderColor(@ColorInt int color) {
        this.borderColor = color;
        invalidate();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        invalidate();
    }

    public void setBorderRadius(float radius) {
        this.borderRadius = radius;
        invalidate();
    }

    public void setReactionEventListener(OnReactionClickListener clickListener) {
        this.eventListener = clickListener;
    }

    public void setStyle(MessageReactionsStyle messageReactionsStyle) {
        this.messageReactionsStyle = messageReactionsStyle;
    }

    public interface OnReactionClickListener {
        void onReactionClick(Reaction reaction, int baseMessageID);
    }
}
