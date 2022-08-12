package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroupMemberList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {

    private static final String TAG = "CustomAdapter";
    private final GroupMember groupMember;
    private final Group group;
    private final Context context;
    private final Palette palette;
    private final Typography typography;
    private onItemClick onItemClick;

    public CustomAdapter(@NonNull Context context, @NonNull GroupMember groupMember, Group group, @NonNull List<String> options) {
        super(context, 0, options);
        this.groupMember = groupMember;
        this.group = group;
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_drop_down, parent, false);


        String title = getItem(position);
        TextView scope = convertView.findViewById(R.id.scope_title);
        scope.setTextColor(palette.getPrimary());
        scope.setTextAppearance(context, typography.getText2());
        if (title != null)
            scope.setText(title);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.scope_change_items, parent, false);


        String title = getItem(position);
        ImageView image = convertView.findViewById(R.id.icon);

        LinearLayout item_bg = convertView.findViewById(R.id.item_bg);
        convertView.setBackgroundColor(palette.getBackground());
        TextView scope = convertView.findViewById(R.id.scope);

        if (title != null)
            scope.setText(title);

        scope.setTextAppearance(context, typography.getText2());
        scope.setTextColor(palette.getAccent());

        if ((CometChatConstants.SCOPE_MODERATOR.equalsIgnoreCase(group.getScope()) && title.equalsIgnoreCase(CometChatConstants.SCOPE_ADMIN))
                || groupMember.getScope().equalsIgnoreCase(title)) {
            disableItem(item_bg, image, scope);
        } else {

            item_bg.setOnClickListener(view -> {
                image.setVisibility(View.VISIBLE);
                if (onItemClick != null)
                    onItemClick.onClickItem(groupMember, group, title);

            });
        }

        return convertView;

    }

    private void disableItem(LinearLayout item_bg, ImageView image, TextView scope) {
        scope.setTextColor(palette.getAccent600());
        item_bg.setOnClickListener(view -> {
        });

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void setClickListener(onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface onItemClick {
        void onClickItem(GroupMember groupMember, Group group, String scope);
    }
}
