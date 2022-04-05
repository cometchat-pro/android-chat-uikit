package com.cometchatworkspace.components.shared.secondaryComponents.cometchatTags;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionSheet;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CometChatTags extends BottomSheetDialogFragment {

    private View view;
    private FrameLayout bottomSheet;
    private BottomSheetBehavior behavior;
    private FontUtils fontUtils;
    private List<String> listOfTags = new ArrayList<>();
    private HashMap<String,List<String>> listOfTagsWithCategory = new HashMap();
    private LinearLayout parentView;
    private ImageView checkButton;
    private TextView titleTv;
    private List<String> finalTags = new ArrayList<>();
    private String title;
    private Drawable checkIcon;
    private int checkIconTint;
    private Event tagsEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontUtils = FontUtils.getInstance(getContext());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCheckIcon(Drawable icon) {
        this.checkIcon = icon;
    }

    public void setCheckIconTint(@ColorInt int color) {
        this.checkIconTint = color;
    }

    public void setListOfTags(List<String> tags) {
        listOfTags = tags;
    }

    public void setListOfTags(String categoryName,List<String> tags) {
        listOfTagsWithCategory.put(categoryName,tags);
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
        view = inflater.inflate(R.layout.cometchat_tags_list, container, false);
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

        parentView = view.findViewById(R.id.parent_View);
        titleTv = view.findViewById(R.id.tv_title);
        if (title!=null)
            titleTv.setText(title);
        checkButton = view.findViewById(R.id.check_done);
        if (checkIcon!=null)
            checkButton.setImageDrawable(checkIcon);
        if (checkIconTint!=0)
            checkButton.setImageTintList(ColorStateList.valueOf(checkIconTint));

        if (!listOfTagsWithCategory.isEmpty())
            setListByCategory();
        else
            setListByTags();


        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagsEvent.onTagsFinalize(finalTags);
                dismiss();
            }
        });
        return view;
    }

    private void setListByCategory() {
        for (String key : listOfTagsWithCategory.keySet()) {
            MaterialCardView cardView = new MaterialCardView(requireContext());
            cardView.setRadius(16);
            cardView.setCardElevation(16);

            LinearLayout parentLayout = new LinearLayout(requireContext());
            parentLayout.setOrientation(LinearLayout.VERTICAL);

            TextView category = new TextView(requireContext());
            category.setTextAppearance(R.style.TextAppearance_AppCompat_Title);
            category.setText(key);
            parentLayout.addView(category);

            ChipGroup tags = new ChipGroup(requireContext());
            for (String values : listOfTagsWithCategory.get(key)) {
                addChip(values,tags);
            }
            parentLayout.addView(tags);
            cardView.addView(parentLayout);
            parentView.addView(cardView);
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(0, (int) Utils.dpToPx(getContext(),16f), 0, (int) Utils.dpToPx(getContext(),16f));
            cardView.requestLayout();

        }
    }

    private void addChip(String values, ChipGroup tags) {
        Chip tag = new Chip(requireContext());
        tag.setCheckable(true);
        Random rnd = new Random();
        int color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        tag.setChipBackgroundColor(ColorStateList.valueOf(Utils.adjustAlpha(color,0.5f)));
        tag.setText(values);
        tags.addView(tag);
        tag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    finalTags.add(compoundButton.getText().toString());
                } else {
                    finalTags.remove(compoundButton.getText().toString());
                }
                if (finalTags.isEmpty())
                    checkButton.setVisibility(View.GONE);
                else
                    checkButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setListByTags() {
        ChipGroup tags = new ChipGroup(requireContext());
        for (String values : listOfTags) {
           addChip(values,tags);
        }
        parentView.addView(tags);
    }

    public void addListener(Event event) {
        this.tagsEvent = event;
    }

    public interface Event {
        void onTagsFinalize(List<String> tags);
    }
}
