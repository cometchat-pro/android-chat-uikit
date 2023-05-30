//package com.cometchat.chatuikit.calls.callhistorywithdetails;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.cometchat.chatuikit.calls.calldetails.CallDetailsActivity;
//import com.cometchat.chatuikit.calls.calldetails.CallDetailsConfiguration;
//import com.cometchat.chatuikit.calls.callhistory.CometChatCallHistory;
//
//public class CometChatCallHistoryWithDetails extends CometChatCallHistory {
//    CallDetailsConfiguration callDetailsConfiguration;
//
//    public CometChatCallHistoryWithDetails(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public CometChatCallHistoryWithDetails(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public CometChatCallHistoryWithDetails(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        super.setOnInfoIconClickListener((context1, user, group, baseMessage) -> {
//            if (user != null)
//                CallDetailsActivity.launch(context, user, baseMessage,callDetailsConfiguration);
//            else if (group != null)
//                CallDetailsActivity.launch(context, group, baseMessage, callDetailsConfiguration);
//        });
//    }
//
//    public void setCallDetailsConfiguration(CallDetailsConfiguration callDetailsConfiguration) {
//        if (callDetailsConfiguration != null)
//            this.callDetailsConfiguration = callDetailsConfiguration;
//    }
//
//
//}
