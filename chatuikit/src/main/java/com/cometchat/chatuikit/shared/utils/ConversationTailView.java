package com.cometchat.chatuikit.shared.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.views.CometChatBadge.CometChatBadge;
import com.cometchat.chatuikit.shared.views.CometChatDate.CometChatDate;
import com.google.android.material.card.MaterialCardView;

public class ConversationTailView extends MaterialCardView {

    private View view;
    private CometChatDate date;
    private CometChatBadge badge;
    private Context context;

    public ConversationTailView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public ConversationTailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public ConversationTailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        view = View.inflate(context, R.layout.tail, null);
        date = view.findViewById(R.id.date);
        badge = view.findViewById(R.id.badge);
        addView(view);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    public void setBadgeCount(int count) {
        if (count > 0)
            badge.setCount(count);
        else
            badge.setVisibility(INVISIBLE);
    }

    public CometChatDate getDate() {
        return date;
    }

    public CometChatBadge getBadge() {
        return badge;
    }
}
