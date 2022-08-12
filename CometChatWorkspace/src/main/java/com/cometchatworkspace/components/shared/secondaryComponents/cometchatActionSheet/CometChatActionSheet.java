package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * setBackground()
 * ActionItem setCornerRadius() & setBackground()
 */
public class CometChatActionSheet extends BottomSheetDialogFragment {

    private boolean isGridLayout;

    private RecyclerView recyclerViewLayout;

    private LinearLayout reactionsLayout;

    private CometChatActionSheetListener cometChatActionSheetListener;

    private String type;

    private View view;

    private int columnCount = 2;

    private BottomSheetBehavior behavior;

    private FrameLayout bottomSheet;

    private boolean reactionVisible;

    private List<ActionItem> listOfItems = new ArrayList<>();

    private ImageView layoutModeButton;

    private TextView title;

    private FontUtils fontUtils;

    private ActionSheetAdapter adapter;

    private int initialReactions = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontUtils = FontUtils.getInstance(getContext());
        if (getArguments() != null) {
            reactionVisible = getArguments().getBoolean("reactionVisible");
            String layoutMode = getArguments().getString("layoutMode");
            isGridLayout = layoutMode.equalsIgnoreCase(ActionSheet.LayoutMode.gridMode);
            listOfItems = getArguments().getParcelableArrayList("listOfItems");
            columnCount = getArguments().getInt("columnCount", columnCount);
        }
    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        if (isGridLayout)
//            view = inflater.inflate(R.layout.cometchat_action_sheet_grid,container,false);
//        else
        view = inflater.inflate(R.layout.cometchat_action_sheet_list, container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                // androidx should use: com.google.android.material.R.id.design_bottom_sheet
                bottomSheet = (FrameLayout)
                        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });

        reactionsLayout = view.findViewById(R.id.initial_reactions);
        if (reactionVisible) {
            reactionsLayout.setVisibility(View.VISIBLE);
            setupReactions();
        } else
            reactionsLayout.setVisibility(View.GONE);


        recyclerViewLayout = view.findViewById(R.id.recyclerView);
        if (recyclerViewLayout != null) {
            if (isGridLayout)
                recyclerViewLayout.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
            else
                recyclerViewLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        initializeAdapter();
        title = view.findViewById(R.id.title);
        layoutModeButton = view.findViewById(R.id.layout_mode_button);
        layoutModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutModeButton.animate().rotationBy(360).setDuration(500).start();
                changeLayoutMode();
                initializeAdapter();
            }
        });


        recyclerViewLayout.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewLayout, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                ActionItem item = (ActionItem) var1.getTag(R.string.action_item);
                if (cometChatActionSheetListener!=null)
                    cometChatActionSheetListener.onActionItemClick(item);
            }
        }));

        return view;
    }

    private void changeLayoutMode() {
        if (isGridLayout) {
            layoutModeButton.setImageResource(R.drawable.ic_list_bulleted_white_24dp);
            isGridLayout = false;
            recyclerViewLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            layoutModeButton.setImageResource(R.drawable.ic_grid_white_24dp);
            isGridLayout = true;
            recyclerViewLayout.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        }
    }

    private void initializeAdapter() {
        adapter = new ActionSheetAdapter(getContext(), listOfItems, isGridLayout);
        recyclerViewLayout.setAdapter(adapter);
        adapter.hideText(columnCount > 3);
        recyclerViewLayout.scheduleLayoutAnimation();
    }

    private void setupReactions() {
        List<Reaction> reactions = Extensions.getInitialReactions(initialReactions);
        for (Reaction reaction : reactions) {
            View vw = LayoutInflater.from(getContext()).inflate(R.layout.reaction_list_row, null);
            TextView textView = vw.findViewById(R.id.reaction);
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            params.rightMargin = 16;
            params.bottomMargin = 8;
            params.topMargin = 8;
            textView.setLayoutParams(params);
            textView.setText(reaction.getName());
            reactionsLayout.addView(vw);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cometChatActionSheetListener != null)
                        cometChatActionSheetListener.onReactionClick(reaction);
                    dismiss();
                }
            });
        }
        ImageView addEmojiView = new ImageView(getContext());
        addEmojiView.setImageDrawable(getResources().getDrawable(R.drawable.ic_reactions));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) Utils.dpToPx(getContext(), 36), (int) Utils.dpToPx(getContext(), 36));
        layoutParams.topMargin = 8;
        layoutParams.leftMargin = 16;
        addEmojiView.setLayoutParams(layoutParams);
        reactionsLayout.addView(addEmojiView);
        addEmojiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cometChatActionSheetListener != null)
                    cometChatActionSheetListener.onReactionClick(new Reaction("add_emoji", 0));
                dismiss();
            }
        });
    }

    public void setTitle(String str) {
        if (title != null)
            title.setText(str);
    }


    public void setTitleFont(String font) {
        if (title != null)
            title.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setTitleColor(@ColorInt int color) {
        if (color != 0 && title != null)
            title.setTextColor(color);
    }

    public void setBackground(@ColorInt int color) {
        bottomSheet.setBackgroundColor(color);
    }

    public void setLayoutMode(@ActionSheet.LayoutMode String mode) {
        Bundle bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putString("layoutMode", mode);
        setArguments(bundle);
    }

//    public void setTileMode(boolean isTile, int columnCount) {
//        Bundle bundle = getArguments();
//        if (bundle==null)
//            bundle = new Bundle();
//
//        bundle.putBoolean("isGridLayout",isTile);
//        bundle.putInt("columnCount",columnCount);
//        setArguments(bundle);
//    }

    public void setLayoutModeIcon(Drawable icon) {
        if (layoutModeButton != null && icon != null)
            layoutModeButton.setImageDrawable(icon);
    }

    public void setLayoutModeIconTint(@ColorInt int color) {
        if (color != 0 && layoutModeButton != null)
            layoutModeButton.setImageTintList(ColorStateList.valueOf(color));
    }

    public void hideLayoutMode(boolean isHidden) {
        if (layoutModeButton != null) {
            if (isHidden)
                layoutModeButton.setVisibility(View.GONE);
            else
                layoutModeButton.setVisibility(View.VISIBLE);
        }
    }

    public void add(ActionItem actionItem) {
        if (adapter != null)
            adapter.addActionItem(actionItem);
    }

    public void update(ActionItem actionItem) {
        if (adapter != null)
            adapter.updateActionItem(actionItem);
    }

//    // Action Item
//    public void setTitle: String)
//    public void setTitle: String)
//    public void setTitleFont: UIFont)
//    public void setTitleColor: UIColor)
//    set(icon: UIImage)
//    set(iconTint: UIColor)
//    set(iconBackground: UIColor)
//    set(iconCornerRadius: CGFloat)
//
//    set(background: UIColor)
//    set(cornerRadius: CGFloat)


    public void setList(List<ActionItem> list) {
        Bundle bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putParcelableArrayList("listOfItems", (ArrayList<? extends Parcelable>) list);
        setArguments(bundle);
    }

    public void showReactions(boolean isVisible) {
        Bundle bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putBoolean("reactionVisible", isVisible);
        setArguments(bundle);
    }
    public void initialReaction(int count) {
        initialReactions = count;
    }


    public void setEventListener(CometChatActionSheetListener actionSheetListener) {
        this.cometChatActionSheetListener = actionSheetListener;

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

}