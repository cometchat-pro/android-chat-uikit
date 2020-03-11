package com.cometchat.pro.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.User;

import java.util.List;

import listeners.ClickListener;
import listeners.OnItemClickListener;
import listeners.RecyclerTouchListener;
import viewmodel.SmartReplyViewModel;

@BindingMethods(value = {@BindingMethod(type = CometChatUserList.class, attribute = "app:replylist", method = "setSmartReplyList")})
public class SmartReplyList extends RecyclerView {

    private Context context;

    private SmartReplyViewModel smartReplyViewModel;

    public SmartReplyList(@NonNull Context context) {
        super(context);
        setContext(context);
    }

    public SmartReplyList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setContext(context);
        getAttributes(attrs);
        setSmartReplyViewModel();
    }

    public SmartReplyList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContext(context);
        getAttributes(attrs);
        setSmartReplyViewModel();
    }

    private void getAttributes(AttributeSet attributeSet) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.SmartReplyList, 0, 0);
    }


    private void setContext(Context context){
        this.context=context;
    }

    private void setSmartReplyViewModel(){
        if (smartReplyViewModel==null){
            smartReplyViewModel=new SmartReplyViewModel(context,this);
        }
    }
    public void setSmartReplyList(List<String> replyList){
        if (smartReplyViewModel!=null){
            smartReplyViewModel.setSmartReplyList(replyList);
        }
    }

    public void setItemClickListener(OnItemClickListener<String> itemClickListener){

        this.addOnItemTouchListener(new RecyclerTouchListener(context, this, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                String reply=(String) var1.getTag(R.string.replyTxt);
                if (itemClickListener!=null)
                    itemClickListener.OnItemClick(reply,var2);
                else
                    throw new NullPointerException("OnItemClickListener<String> is null" );
            }

            @Override
            public void onLongClick(View var1, int var2) {
                String reply=(String) var1.getTag(R.string.replyTxt);
                 if (itemClickListener!=null)
                    itemClickListener.OnItemLongClick(reply,var2);
                 else
                    throw new NullPointerException("OnItemClickListener<String> is null" );
            }
        }));
    }

}
