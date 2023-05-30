package com.cometchat.chatuikit.extensions.sticker.keyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.extensions.sticker.keyboard.listener.StickerClickListener;
import com.cometchat.chatuikit.extensions.sticker.keyboard.model.Sticker;

import java.util.ArrayList;
import java.util.List;

import com.cometchat.chatuikit.extensions.sticker.keyboard.adapter.StickersAdapter;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.ClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerTouchListener;

public class StickerFragment extends Fragment {

    private RecyclerView rvStickers;
    private StickersAdapter adapter;
    private List<Sticker> stickers = new ArrayList<>();
    StickerClickListener stickerClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stickers_view, container, false);
        rvStickers = view.findViewById(R.id.rvStickers);
        rvStickers.setLayoutManager(new GridLayoutManager(getContext(), 4));
        List<Sticker> list = this.getArguments().getParcelableArrayList("stickerList");
        stickers = list;
        adapter = new StickersAdapter(getContext(), stickers);
        rvStickers.setAdapter(adapter);

        rvStickers.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvStickers, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Sticker sticker = (Sticker) view.getTag(R.string.sticker);
                if (stickerClickListener != null)
                    stickerClickListener.onClickListener(sticker);
            }
        }));
        return view;
    }

    public void setStickerClickListener(StickerClickListener stickerClickListener) {
        this.stickerClickListener = stickerClickListener;
    }
}
