package com.cometchatworkspace.components.messages.emojiKeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.emojiKeyboard.emojiKeyBoardAdapters.EmojiAdapter;
import com.cometchatworkspace.components.messages.emojiKeyboard.model.EmojiCategory;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CometChatEmojiKeyboard extends BottomSheetDialogFragment {
    private View view;
    private RecyclerView emojiListRecyclerView;
    private EmojiAdapter emojiAdapter;
    private List<EmojiCategory> emojiCategories = new ArrayList<>();
    private FragmentManager fm;
    private TabLayout tabLayout;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling;
    private TextView title, separator;
    private CardView closeBtn;
    private LinearLayout parent;
    private onClick onClick;
    //for styling
    private EmojiKeyboardStyle emojiKeyboardStyle;
    private @ColorInt
    int categoryIconTint;
    private @ColorInt
    int selectedCategoryIconTint;

    public CometChatEmojiKeyboard(Context context) {
        fm = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    public void setStyle(EmojiKeyboardStyle emojiKeyboardStyle) {
        if (emojiKeyboardStyle != null) {
            this.emojiKeyboardStyle = emojiKeyboardStyle;
            categoryIconTint = emojiKeyboardStyle.getCategoryIconTint();
            selectedCategoryIconTint = emojiKeyboardStyle.getSelectedCategoryIconTint();
            if (emojiAdapter != null)
                emojiAdapter.setStyle(emojiKeyboardStyle);
            if (title != null && closeBtn != null && parent != null && separator!=null) {
                if (emojiKeyboardStyle.getTitleColor() != 0)
                    title.setTextColor(emojiKeyboardStyle.getTitleColor());
                if (emojiKeyboardStyle.getTitleFont() != null)
                    title.setTypeface(FontUtils.getInstance(getContext()).getTypeFace(emojiKeyboardStyle.getTitleFont()));
                if (emojiKeyboardStyle.getTitleAppearance() != 0)
                    title.setTextAppearance(getContext(), emojiKeyboardStyle.getTitleAppearance());
                if (emojiKeyboardStyle.getCloseButtonTint() != 0)
                    closeBtn.setCardBackgroundColor(emojiKeyboardStyle.getCloseButtonTint());
                if (emojiKeyboardStyle.getSeparatorColor() != 0)
                    separator.setBackgroundColor(emojiKeyboardStyle.getSeparatorColor());
                if (emojiKeyboardStyle.getDrawableBackground() != null) {
                    parent.setBackground(emojiKeyboardStyle.getDrawableBackground());
                } else if (emojiKeyboardStyle.getBackground() != 0) {
                    parent.setBackgroundColor(emojiKeyboardStyle.getBackground());
                }
            }

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emojiCategories.clear();
        try {
            JSONArray jsonArray = new JSONObject(loadJSONFromAsset()).getJSONArray("emojiCategory");
            for (int i = 0; i < jsonArray.length(); i++) {
                EmojiCategory emojiCategory = new Gson().fromJson(String.valueOf(jsonArray.getJSONObject(i)), EmojiCategory.class);
                emojiCategories.add(emojiCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.emoji_keyboard_layout, container, false);

        tabLayout = view.findViewById(R.id.category_tab);
        title = view.findViewById(R.id.title);
        separator = view.findViewById(R.id.separator);
        emojiListRecyclerView = view.findViewById(R.id.emoji_list_view);
        parent = view.findViewById(R.id.emoji_parent_layout);
        closeBtn = view.findViewById(R.id.scroll_down_button);
        linearLayoutManager = new LinearLayoutManager(getContext());
        emojiListRecyclerView.setLayoutManager(linearLayoutManager);
        emojiListRecyclerView.setHasFixedSize(true);
        emojiAdapter = new EmojiAdapter(getContext());
        emojiListRecyclerView.setAdapter(emojiAdapter);
        emojiAdapter.setEmojiCategories(emojiCategories);
        setStyle(emojiKeyboardStyle);
        setTabs();
        emojiListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    isScrolling = false;
                else
                    isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.getTabAt(linearLayoutManager.findLastVisibleItemPosition()).select();
                    }
                });

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (selectedCategoryIconTint != 0)
                    tab.getIcon().setTint(selectedCategoryIconTint);
                else
                    tab.getIcon().setTint(getContext().getResources().getColor(R.color.primary));
                if (!isScrolling)
                    emojiListRecyclerView.scrollToPosition(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (categoryIconTint != 0)
                    tab.getIcon().setTint(categoryIconTint);
                else
                    tab.getIcon().setTint(getContext().getResources().getColor(R.color.accent600));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private void setTabs() {
        for (int i = 0; i < emojiCategories.size(); i++) {
            addTabIcons(emojiCategories.get(i));
            if (i == 0) {
                if (selectedCategoryIconTint != 0)
                    tabLayout.getTabAt(0).getIcon().setTint(selectedCategoryIconTint);
                else
                    tabLayout.getTabAt(0).getIcon().setTint(getContext().getResources().getColor(R.color.primary));
            } else {
                if (categoryIconTint != 0)
                    tabLayout.getTabAt(i).getIcon().setTint(categoryIconTint);
                else
                    tabLayout.getTabAt(i).getIcon().setTint(getContext().getResources().getColor(R.color.accent600));
            }
        }

    }

    private void addTabIcons(EmojiCategory emojiCategory) {
        String id = emojiCategory.getId();
        if ("people".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_smileys)));
        } else if ("animals_and_nature".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_animals)));
        } else if ("food_and_drink".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_food)));
        } else if ("activity".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_activity)));
        } else if ("travel_and_places".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_travel)));
        } else if ("objects".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_objects)));
        } else if ("symbols".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_symbols)));
        } else if ("flags".equalsIgnoreCase(id)) {
            tabLayout.addTab(tabLayout.newTab().setIcon(getContext().getResources().getDrawable(R.drawable.cc_flags)));
        } else {
            try {
                tabLayout.addTab(tabLayout.newTab().setIcon(emojiCategory.getSymbol()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onClick != null) {
            emojiAdapter.setOnClick(onClick);
        }
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getContext().getAssets().open("emoji.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json + "";
    }

    /**
     * show() method is use to populate the emoji key board Dialog in Screen
     */
    public void show() {
        show(fm, CometChatEmojiKeyboard.class.getName());
    }

    public void setOnClick(onClick onClick) {
        this.onClick = onClick;

    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public interface onClick {
        void onClick(String emoji);

        void onLongClick(String emoji);
    }


}
