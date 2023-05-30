package com.cometchat.chatuikit.shared.Interfaces;

import android.content.Context;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;

public interface OnDetailOptionClick {
    void onClick(User user, Group group, String templateId, CometChatDetailsOption option, Context context);
}
