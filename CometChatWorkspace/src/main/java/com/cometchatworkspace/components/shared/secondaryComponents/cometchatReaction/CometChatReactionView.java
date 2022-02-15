package com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.cometchat.pro.models.BaseMessage;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.listener.OnReactionClickListener;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;

public class CometChatReactionView extends LinearLayout {
    private final Context context;
    private View view;
    private CometChatMessageReaction.OnReactionClickListener clickListener;
    private BaseMessage baseMessage;

    public CometChatReactionView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CometChatReactionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CometChatReactionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void setBaseMessage(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
    }
    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.cometchat_reaction_view,null);
        LinearLayout reactionsLayout = view.findViewById(R.id.reactions_layout);
        ReactionUtils.setupReaction(context, 5, reactionsLayout, new OnReactionClickListener() {
            @Override
            public void onEmojiClicked(Reaction emojicon) {
                clickListener.onReactionClick(emojicon,baseMessage.getId());
            }
        });
    }
}
