package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatCalls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchatworkspace.databinding.CometchatCallListRowBinding;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;

public class CometChatCallListItem extends RecyclerView.ViewHolder {

    private final CometchatCallListRowBinding callListRowBinding;
    FontUtils fontUtils;

    Context context;

    CometChatCallListItem(CometchatCallListRowBinding cometchatListItemBinding, Context context) {
        super(cometchatListItemBinding.getRoot());
        callListRowBinding = cometchatListItemBinding;
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
    }

    public void setTitle(String titleStr) {
        callListRowBinding.callListItemTitle.setText(titleStr);
    }

    public void hideTitle(boolean isHidden) {
        if (isHidden)
            callListRowBinding.callListItemTitle.setVisibility(View.GONE);
        else
            callListRowBinding.callListItemTitle.setVisibility(View.VISIBLE);
    }

    public void setTitleFont(String fonts) {
        callListRowBinding.callListItemTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }


    public void setTitleColor(int color) {
        callListRowBinding.callListItemTitle.setTextColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setTitleImageColor(int color) {
        callListRowBinding.callListItemTitle.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }

    public void setSubTitle(String subTitleStr) {
        callListRowBinding.callListItemSubtitle.setText(subTitleStr);
    }

    public void setSubTitleColor(int color) {
        callListRowBinding.callListItemSubtitle.setTextColor(color);
    }

    public void setSubTitleFont(String fonts) {
        callListRowBinding.callListItemSubtitle.setTypeface(fontUtils.getTypeFace(fonts));
    }

    public void setStatus(int drawable,int position) {
        if (position==0)
            callListRowBinding.callListItemSubtitle.setCompoundDrawablesWithIntrinsicBounds(drawable,0,0,0);
        else if (position==1)
            callListRowBinding.callListItemSubtitle.setCompoundDrawablesWithIntrinsicBounds(0,drawable,0,0);
        else if (position==2)
            callListRowBinding.callListItemSubtitle.setCompoundDrawablesWithIntrinsicBounds(0,0,drawable,0);
        else if (position==3)
            callListRowBinding.callListItemSubtitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,drawable);
    }

    public void setAvatar(String url, String initials) {
        callListRowBinding.callListItemAvatar.setAvatar(url);
        if(url==null)
            callListRowBinding.callListItemAvatar.setInitials(initials);
    }

    public void setAvatar(Drawable drawable) {
        callListRowBinding.callListItemAvatar.setDrawable(drawable);
    }

    public CometChatAvatar getAvatar() {
        return callListRowBinding.callListItemAvatar;
    }

    public void setTime(long timestamp) {
        if (context!=null)
            callListRowBinding.callListItemTime.setText(Utils.getLastMessageDate(context,timestamp));
    }

    public void setTimeFont(String fonts) {
        callListRowBinding.callListItemTime.setTypeface(fontUtils.getTypeFace(fonts));
    }


    public View getView() {
        return callListRowBinding.getRoot();
    }

    public void executePendingBindings() {
        callListRowBinding.executePendingBindings();
    }

    public void showButton(boolean b) {
        if(b)
            callListRowBinding.callListItemButton.setVisibility(View.VISIBLE);
        else
            callListRowBinding.callListItemButton.setVisibility(View.GONE);
    }

    public void showButtonColor(ColorStateList colorState) {
        callListRowBinding.callListItemButton.setImageTintList(colorState);
    }
}