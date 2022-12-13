package com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.resources.utils.FontUtils;

/**
 * Purpose - UserListAdapter is a subclass of RecyclerView Adapter which is used to display
 * the list of users. It helps to organize the users in recyclerView.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 23rd March 2020
 */

public class SmartRepliesAdapter extends RecyclerView.Adapter<SmartRepliesAdapter.SmartReplyViewHolder> {

    private final Context context;

    private List<String> replyArrayList = new ArrayList<>();

    private final FontUtils fontUtils;

    private SmartRepliesStyle style;

    /**
     * It is a constructor which is used to initialize wherever we needed.
     *
     * @param context is a object of Context.
     */
    public SmartRepliesAdapter(Context context) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    /**
     * It is constructor which takes userArrayList as parameter and bind it with userArrayList in adapter.
     *
     * @param context        is a object of Context.
     * @param replyArrayList is a list of users used in this adapter.
     */
    public SmartRepliesAdapter(Context context, List<String> replyArrayList) {
        this.replyArrayList = replyArrayList;
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    @NonNull
    @Override
    public SmartReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cometchat_smartreply_row, parent, false);

        return new SmartReplyViewHolder(view);
    }

    /**
     * This method is used to bind the ReactionViewHolder contents with user at given
     * position. It set username userAvatar in respective ReactionViewHolder content.
     *
     * @param smartReplyViewHolder is a object of ReactionViewHolder.
     * @param i                    is a position of item in recyclerView.
     * @see User
     */
    @Override
    public void onBindViewHolder(@NonNull SmartReplyViewHolder smartReplyViewHolder, int i) {

        smartReplyViewHolder.cardView.setCardElevation(2);
        final String reply = replyArrayList.get(i);
        smartReplyViewHolder.cReply.setText(reply);
        if (style != null) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (style.getTextBackgroundDrawable() != null) {
                        smartReplyViewHolder.cReply.setBackground(style.getTextBackgroundDrawable());
                    } else if (style.getTextBackground() != 0) {
                        smartReplyViewHolder.cReply.setBackgroundColor(style.getTextBackground());
                    }
                    if (style.getTextAppearance() != 0) {
                        smartReplyViewHolder.cReply.setTextAppearance(context, style.getTextAppearance());
                    }
                    if (style.getTextFont() != null) {
                        smartReplyViewHolder.cReply.setTypeface(fontUtils.getTypeFace(style.getTextFont()));
                    }
                    if (style.getTextColor() != 0) {
                        smartReplyViewHolder.cReply.setTextColor(style.getTextColor());
                    }
                    if (style.getTextBorderRadius() != 0) {
                        smartReplyViewHolder.cardView.setRadius(style.getTextBorderRadius());
                    }
                    if (style.getTextBorderWidth() != 0) {
                        smartReplyViewHolder.cardView.setStrokeWidth(style.getTextBorderWidth());
                    }
                    if (style.getTextBorderColor() != 0) {
                        smartReplyViewHolder.cardView.setStrokeColor(style.getTextBorderColor());
                    }
                }
            });

        }
        smartReplyViewHolder.itemView.setTag(R.string.reply_txt, reply);


    }

    @Override
    public int getItemCount() {
        return replyArrayList.size();
    }

    public void updateList(List<String> replies) {
        this.replyArrayList = replies;
        notifyDataSetChanged();
    }

    public void setStyle(SmartRepliesStyle style) {
        this.style = style;
        notifyDataSetChanged();
    }

    class SmartReplyViewHolder extends RecyclerView.ViewHolder {

        private final TextView cReply;
        private final MaterialCardView cardView;

        SmartReplyViewHolder(View view) {
            super(view);
            cReply = view.findViewById(R.id.replyText);
            cardView = view.findViewById(R.id.card);
        }

    }
}
