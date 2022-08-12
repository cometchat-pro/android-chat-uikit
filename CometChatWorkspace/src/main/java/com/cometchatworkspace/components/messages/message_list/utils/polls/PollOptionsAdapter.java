package com.cometchatworkspace.components.messages.message_list.utils.polls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PollOptionsAdapter extends RecyclerView.Adapter<PollOptionsAdapter.OptionViewHolder> {
    private final Context context;
    private final List<Integer> optionsArrayList = new ArrayList<>();
    private Palette palette;
    private Typography typography;

    public PollOptionsAdapter(Context context) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
    }

    public void remove(int pos) {
        optionsArrayList.remove(pos);
        notifyDataSetChanged();
    }

    public void add(int optionId) {
        optionsArrayList.add(optionId);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View optionView = LayoutInflater.from(context).inflate(R.layout.polls_option, null, false);
        OptionViewHolder viewHolder = new OptionViewHolder(optionView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.option.setHint(context.getResources().getString(R.string.enter_your_option) + " " + (position + 3));
        holder.option.setTextAppearance(context, typography.getText1());
        holder.option.setHintTextColor(palette.getAccent600());
        holder.option.setTextColor(palette.getAccent());
        holder.option_separator.setBackgroundColor(palette.getAccent100());
    }

    @Override
    public int getItemCount() {
        return optionsArrayList.size();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        public EditText option;
        public View option_separator;
        OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            option = itemView.findViewById(R.id.option_txt);
            option_separator=itemView.findViewById(R.id.option_separator);
        }
    }
}
