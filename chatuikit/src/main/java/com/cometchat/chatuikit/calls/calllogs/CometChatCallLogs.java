//package com.cometchat.chatuikit.calls.calllogs;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.TextView;
//
//import com.cometchat.chatuikit.shared.constants.UIKitConstants;
//import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
//import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderDecoration;
//import com.cometchat.chatuikit.shared.views.CometChatDate.CometChatDate;
//import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
//import com.cometchat.chatuikit.shared.views.CometChatDate.Pattern;
//import com.cometchat.pro.constants.CometChatConstants;
//import com.cometchat.pro.core.Call;
//import com.cometchat.pro.core.MessagesRequest;
//import com.cometchat.pro.models.CustomMessage;
//import com.cometchat.pro.models.Group;
//import com.cometchat.pro.models.User;
//import com.cometchat.chatuikit.R;
//import com.cometchat.chatuikit.calls.callhistory.CometChatCallHistory;
//import com.cometchat.chatuikit.calls.utils.CallUtils;
//
//import java.util.Arrays;
//
//public class CometChatCallLogs extends CometChatCallHistory {
//    private Context context;
//    private StickyHeaderDecoration stickyHeaderDecoration;
//
//    public CometChatCallLogs(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public CometChatCallLogs(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public CometChatCallLogs(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//        this.context = context;
//        getRecyclerView().setNestedScrollingEnabled(false);
//        hideToolbar(true);
//        hideDateHeader(false);
//        setListItemView((context2, baseMessage) -> {
//            View view = View.inflate(context2, R.layout.call_logs_row, null);
//            CometChatDate time = view.findViewById(R.id.time);
//            TextView callStatus = view.findViewById(R.id.call_status);
//            time.setStyle(new DateStyle().setTextAppearance(CometChatTheme.getInstance(context2).getTypography().getSubtitle1()).setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent400()));
//            callStatus.setTextAppearance(context2, CometChatTheme.getInstance(context2).getTypography().getSubtitle1());
//            callStatus.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent());
//            if (baseMessage instanceof Call) {
//                time.setDate(((Call) baseMessage).getInitiatedAt(), Pattern.TIME);
//                callStatus.setText((CallUtils.getCallStatus(context, (Call) baseMessage)));
//            } else if (baseMessage instanceof CustomMessage && baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.MEETING)) {
//                time.setDate(baseMessage.getSentAt(), Pattern.TIME);
//                callStatus.setText(context.getResources().getString(R.string.conference_call));
//            }
//            return view;
//        });
//
//
//    }
//
//    public void setUser(User user) {
//        if (user != null) {
//            setMessageRequestBuilder(new MessagesRequest.MessagesRequestBuilder().setUID(user.getUid()).setCategories(Arrays.asList(UIKitConstants.MessageCategory.CALL)).setTypes(Arrays.asList(CometChatConstants.CALL_TYPE_AUDIO, CometChatConstants.CALL_TYPE_VIDEO)).setLimit(5));
//        }
//    }
//
//    public void setGroup(Group group) {
//        if (group != null) {
//            setMessageRequestBuilder(new MessagesRequest.MessagesRequestBuilder().setGUID(group.getGuid()).setCategories(Arrays.asList(UIKitConstants.MessageCategory.CUSTOM)).setTypes(Arrays.asList(UIKitConstants.MessageType.MEETING)).setLimit(5));
//        }
//    }
//}
