package com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers.listener.StickerClickListener;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers.model.Sticker;
//import com.facebook.react.fabric.mounting.mountitems.PreAllocateViewMountItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers.adapter.StickerTabAdapter;


public class StickerView extends RelativeLayout implements StickerClickListener {

    private final Context context;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private String Id;

    private String type;

    private AttributeSet attrs;

    private StickerTabAdapter adapter;
    private Palette palette;
    private Typography typography;
    private HashMap<String,List<Sticker>> stickerMap = new HashMap<>();

    private StickerClickListener stickerClickListener;

    public StickerView(Context context) {
        super(context);
        this.context = context;
        initViewComponent(context,null,-1,-1);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        this.context = context;
        initViewComponent(context,attrs,-1,-1);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        initViewComponent(context,attrs,defStyleAttr,-1);
    }


    private void initViewComponent(Context context,AttributeSet attributeSet,int defStyleAttr,int defStyleRes){
        palette=Palette.getInstance(context);
        typography=Typography.getInstance();
        View view =View.inflate(context, R.layout.cometchat_sticker_view,null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.SharedMediaView, 0, 0);
        addView(view);


        if (type!=null) {
            viewPager = this.findViewById(R.id.viewPager);
            tabLayout = view.findViewById(R.id.tabLayout);
            adapter = new StickerTabAdapter(context,((FragmentActivity)context).getSupportFragmentManager());
            for (String str : stickerMap.keySet()) {
                Bundle bundle = new Bundle();
                bundle.putString("Id",Id);
                bundle.putString("type",type);
                StickerFragment stickersFragment = new StickerFragment();
                bundle.putParcelableArrayList("stickerList", (ArrayList<? extends Parcelable>) stickerMap.get(str));
                stickersFragment.setArguments(bundle);
                stickersFragment.setStickerClickListener(stickerClickListener);
                adapter.addFragment(stickersFragment, str,stickerMap.get(str).get(0).getUrl());
            }
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            for (int i=0;i<tabLayout.getTabCount();i++) {
                tabLayout.getTabAt(i).setCustomView(createTabItemView(adapter.getPageIcon(i)));
            }
            if (palette.getPrimary() != 0) {
                Drawable wrappedDrawable = DrawableCompat.wrap(getResources().
                        getDrawable(R.drawable.tab_layout_background_active));
                DrawableCompat.setTint(wrappedDrawable, palette.getPrimary());
                tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).view.setBackground(wrappedDrawable);
                tabLayout.setSelectedTabIndicatorColor(palette.getPrimary());
            } else {
                tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).
                        view.setBackgroundColor(palette.getPrimary());
                tabLayout.setSelectedTabIndicatorColor(palette.getPrimary());
            }
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (palette.getPrimary() != 0) {
                        Drawable wrappedDrawable = DrawableCompat.wrap(getResources().
                                getDrawable(R.drawable.tab_layout_background_active));
                        DrawableCompat.setTint(wrappedDrawable, palette.getPrimary());
                        tab.view.setBackground(wrappedDrawable);
                    }
                    else
                        tab.view.setBackgroundColor(palette.getPrimary());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        }
    }

    private View createTabItemView(String imgUri) {
        ImageView imageView = new ImageView(context);
        TabLayout.LayoutParams params = new TabLayout.LayoutParams(72, 72);
        imageView.setLayoutParams(params);
        Glide.with(context).load(imgUri).into(imageView);
        return imageView;
    }

    public void setStickerClickListener(StickerClickListener stickerClickListener) {
        this.stickerClickListener = stickerClickListener;
    }
    public void setData(String uid,String receiverType,HashMap<String,List<Sticker>> stickers) {
        this.Id = uid;
        this.type = receiverType;
        this.stickerMap = stickers;
        reload();
    }


    public void reload() {
        initViewComponent(context,null,-1,-1);
    }

    @Override
    public void onClickListener(Sticker sticker) {
        stickerClickListener.onClickListener(sticker);
    }
}
