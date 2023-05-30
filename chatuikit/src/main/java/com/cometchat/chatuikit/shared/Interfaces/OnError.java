package com.cometchat.chatuikit.shared.Interfaces;

import android.content.Context;

import com.cometchat.pro.exceptions.CometChatException;

public interface OnError {
    void onError(Context context, CometChatException cometChatException);
}
