package com.cometchat.chatuikit.calls;

import android.content.Context;
import android.util.Log;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;

public class CallingExtension implements ExtensionsDataSource {
    private Context context;

    public CallingExtension(Context context) {
        this.context = context;
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> {
            Context Context = context;
            Log.e("", "enable: "+"enaballed" );
            return new CallingExtensionDecorator(Context, var1);
        });
    }

}
