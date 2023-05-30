package com.cometchat.chatuikit.extensions.reaction.emojikeyboard.emojiKeyBoardAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.extensions.reaction.emojikeyboard.model.Emoji;

import java.util.List;

public class EmojiItemAdapter extends RecyclerView.Adapter<EmojiItemAdapter.myViewHolder> {
    List<Emoji> emojis;
    Context context;

    public EmojiItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EmojiItemAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiItemAdapter.myViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emoji_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiItemAdapter.myViewHolder holder, int position) {
        Emoji emoji=emojis.get(position);
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                holder.emoji.setText(emoji.getEmoji() + "");
            }
        });

        holder.itemView.setTag(R.string.emoji, emojis.get(position));
    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    public void setEmojis(List<Emoji> emojis) {
        this.emojis = emojis;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView emoji;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji = itemView.findViewById(R.id.emoji_icon);
        }
    }
}
