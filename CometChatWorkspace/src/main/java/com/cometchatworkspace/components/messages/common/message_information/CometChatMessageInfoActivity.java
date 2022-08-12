package com.cometchatworkspace.components.messages.common.message_information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatMessageReceiptList.CometChatReceiptsList;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatAudioBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatConferenceBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatDocumentBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatFileBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatImageBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatLocationBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatPollBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatStickerBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatTextBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatVideoBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatWhiteboardBubble;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.resources.utils.CometChatError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class CometChatMessageInfoActivity extends AppCompatActivity {


    private CometChatTextBubble textMessage;
    private CometChatImageBubble imageMessage;
    private CometChatAudioBubble audioMessage;
    private CometChatVideoBubble videoMessage;
    private CometChatFileBubble fileMessage;
    private CometChatLocationBubble locationMessage;
    private CometChatPollBubble pollsMessage;
    private CometChatStickerBubble stickerMessage;
    private CometChatWhiteboardBubble whiteBoardMessage;
    private CometChatDocumentBubble documentMessage;
    private CometChatConferenceBubble meetingMessage;

    private int id;
    private String message;
    private String messageType;
    private int messageSize;
    private String messageExtension;
    private int percentage=0;
    private final String TAG = "CometChatMessageInfo";

    private SwipeRefreshLayout swipeRefreshLayout;
    private CometChatReceiptsList cometChatReceiptsList;

    private Toolbar toolbar;
    private RelativeLayout messageLayout;
    private ImageView backIcon;

    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_message_info);
        toolbar = findViewById(R.id.detail_toolbar);
        messageLayout = findViewById(R.id.message_layout);
        backIcon = findViewById(R.id.backIcon);
        textMessage = findViewById(R.id.vw_text_message);
        imageMessage = findViewById(R.id.vw_image_message);
        audioMessage = findViewById(R.id.vw_audio_message);
        fileMessage = findViewById(R.id.vw_file_message);
        locationMessage = findViewById(R.id.vw_location_message);
        pollsMessage = findViewById(R.id.vw_polls_message);
        stickerMessage = findViewById(R.id.vw_sticker_message);
        whiteBoardMessage = findViewById(R.id.vw_whiteboard_message);
        documentMessage = findViewById(R.id.vw_writeboard_message);
        meetingMessage = findViewById(R.id.vw_meeting_message);

        pollsMessage.votes(0);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        cometChatReceiptsList = findViewById(R.id.rvReceipts);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.grey));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchReceipts();
            }
        });

        handleIntent();
        fetchReceipts();

        backIcon.setOnClickListener(view -> onBackPressed());
        if(Utils.isDarkMode(this)) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            messageLayout.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            messageLayout.setBackgroundColor(getResources().getColor(R.color.light_grey));
        }
    }

    private void fetchReceipts() {
        CometChat.getMessageReceipts(id, new CometChat.CallbackListener<List<MessageReceipt>>() {
            @Override
            public void onSuccess(List<MessageReceipt> messageReceipts) {
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
        CometChat.getMessageReceipts(id, new CometChat.CallbackListener<List<MessageReceipt>>() {
                @Override
                public void onSuccess(List<MessageReceipt> messageReceipts) {
                    cometChatReceiptsList.clear();
                    cometChatReceiptsList.setMessageReceiptList(messageReceipts);
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onError(CometChatException e) {
                   CometChatSnackBar.show(CometChatMessageInfoActivity.this,
                            cometChatReceiptsList, CometChatError.localized(e), CometChatSnackBar.ERROR);
                }
        });
    }

    private void handleIntent() {
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.ID)) {
            id = getIntent().getIntExtra(UIKitConstants.IntentStrings.ID, 0);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.TEXTMESSAGE)) {
            message = getIntent().getStringExtra(UIKitConstants.IntentStrings.TEXTMESSAGE);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.IMAGE_MODERATION)) {
            boolean isImageNotSafe = getIntent()
                    .getBooleanExtra(UIKitConstants.IntentStrings.IMAGE_MODERATION, true);
//            if (isImageNotSafe)
//                sensitiveLayout.setVisibility(View.VISIBLE);
//            else
//                sensitiveLayout.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL)) {
            message = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE)) {
            messageType = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION)) {
            messageExtension = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE)) {
            messageSize = getIntent().
                    getIntExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE, 0);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.SENTAT)) {
            time = Utils.getHeaderDate(getIntent()
                    .getLongExtra(UIKitConstants.IntentStrings.SENTAT, 0) * 1000);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.CUSTOM_MESSAGE)) {
            message = getIntent().getStringExtra(UIKitConstants.IntentStrings.CUSTOM_MESSAGE);
        }

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.POLL_RESULT)) {
            percentage = getIntent().getIntExtra(UIKitConstants.IntentStrings.POLL_RESULT, 0);
        }
        if (messageType != null) {
            if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                textMessage.setVisibility(View.VISIBLE);
                textMessage.text(message);
            } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                imageMessage.setVisibility(View.VISIBLE);
                imageMessage.setImageUrl(message);
            } else if (messageType.equals(UIKitConstants.IntentStrings.STICKERS)) {
                stickerMessage.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(message);

                    stickerMessage.setImageUrl(jsonObject.getString("url"));
//                    messageSticker.setImageUrl(jsonObject.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                videoMessage.setVisibility(View.VISIBLE);
                videoMessage.setVideoUrl(message);
//                Glide.with(this).load(message).into(messageVideo);
            } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                fileMessage.setVisibility(View.VISIBLE);
                fileMessage.title(message);
                fileMessage.subtitle(Utils.getFileSize(messageSize));
                fileMessage.type(messageExtension);
            } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
                audioMessage.setVisibility(View.VISIBLE);
                audioMessage.subtitle(Utils.getFileSize(messageSize));
//                audioFileSize.setText(Utils.getFileSize(messageSize));
            } else if (messageType.equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                whiteBoardMessage.setVisibility(View.VISIBLE);
                whiteBoardMessage.subTitle(getString(R.string.you_created_whiteboard));
//                joinWhiteBoard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String boardUrl = getIntent().getStringExtra(UIKitConstants.IntentStrings.TEXTMESSAGE);
//                        Intent intent = new Intent(CometChatMessageInfoScreenActivity.this, CometChatWebViewActivity.class);
//                        intent.putExtra(UIKitConstants.IntentStrings.URL, boardUrl);
//                        startActivity(intent);
//                  }
//              });
            } else if (messageType.equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                documentMessage.setVisibility(View.VISIBLE);
                documentMessage.subtitle(getString(R.string.you_created_document));
//                joinWriteBoard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String boardUrl = getIntent().getStringExtra(UIKitConstants.IntentStrings.TEXTMESSAGE);
//                        Intent intent = new Intent(CometChatMessageInfoScreenActivity.this, CometChatWebViewActivity.class);
//                        intent.putExtra(UIKitConstants.IntentStrings.URL, boardUrl);
//                        startActivity(intent);
//                    }
//                });
            } else if (messageType.equals(UIKitConstants.IntentStrings.LOCATION)) {
                try {
                    locationMessage.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(message);
                    double LATITUDE = jsonObject.getDouble("latitude");
                    double LONGITUDE = jsonObject.getDouble("longitude");
                    String mapUrl = UIKitConstants.MapUrl.MAPS_URL + LATITUDE + "," + LONGITUDE + "&key=" + UIKitConstants.MapUrl.MAP_ACCESS_KEY;
                    locationMessage.setImage(mapUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (messageType.equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
                meetingMessage.setVisibility(View.VISIBLE);
            } else if (messageType.equals(UIKitConstants.IntentStrings.POLLS)) {
                pollsMessage.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    String questionStr = jsonObject.getString("question");
                    pollsMessage.question(questionStr);
                    JSONObject options = jsonObject.getJSONObject("options");
                    for (int i = 0; i < options.length(); i++) {
                        MaterialCardView cardView = new MaterialCardView(CometChatMessageInfoActivity.this);
                        cardView.setRadius(16);
                        cardView.setCardBackgroundColor(Color.WHITE);
                        cardView.setPadding(8,8,8,8);
                        ViewGroup.MarginLayoutParams cardParams = new
                                ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        cardParams.setMargins(0,0,0,16);
                        cardView.setLayoutParams(cardParams);
                        LinearLayout linearLayout = new LinearLayout(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout
                                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.bottomMargin = (int) Utils.dpToPx(this, 8);
                        linearLayout.setLayoutParams(layoutParams);

                        linearLayout.setPadding(8, 8, 8, 8);
                        linearLayout.setBackground(getResources()
                                .getDrawable(R.drawable.cc_message_bubble_right));
                        linearLayout.setBackgroundTintList(ColorStateList.valueOf(getResources()
                                .getColor(R.color.textColorWhite)));
                        TextView textViewPercentage = new TextView(this);
                        TextView textViewOption = new TextView(this);
                        textViewPercentage.setPadding(16, 4, 0, 4);
                        textViewOption.setPadding(16, 4, 0, 4);
                        textViewOption.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Medium);
                        textViewPercentage.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Medium);
                        textViewPercentage.setTextColor(getResources().getColor(R.color.primaryTextColor));
                        textViewOption.setTextColor(getResources().getColor(R.color.primaryTextColor));
                        String optionStr = options.getString(String.valueOf(i + 1));
                        textViewOption.setText(optionStr);
                        if (percentage > 0)
                            textViewPercentage.setText(percentage + "% ");
                        if (pollsMessage.getOptionCount() != options.length()) {
                            linearLayout.addView(textViewPercentage);
                            linearLayout.addView(textViewOption);
                            cardView.addView(linearLayout);
                            pollsMessage.addOptionView(cardView);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        addReceiptListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        CometChat.removeMessageListener(TAG);
    }
}