package com.cometchat.chatuikit.extensions.messagetranslation;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.extensions.Extensions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class MessageTranslationDecorator extends DataSourceDecorator {

    public MessageTranslationDecorator(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public View getTextBubbleContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, int gravity, UIKitConstants.MessageBubbleAlignment alignment) {

        if (textMessage != null) {
            if (textMessage.getMetadata() != null && textMessage.getMetadata().has("values")) {
                try {
                    if (Extensions.isMessageTranslated(textMessage.getMetadata().getJSONArray("values").getJSONObject(0), textMessage.getText())) {
                        String translatedStr = Extensions.getTranslatedMessage(textMessage);

                        if (translatedStr != null && !translatedStr.isEmpty()) {
                            LinearLayout layout = new LinearLayout(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.gravity = gravity;
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.addView(super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment));
                            View translateView = View.inflate(context, R.layout.translate_layout, null);
                            TextView translatedMessage = translateView.findViewById(R.id.translate_message);
                            TextView translateSubtitle = translateView.findViewById(R.id.translate_subtitle);
                            translatedMessage.setText(translatedStr);

                            if (gravity == Gravity.END) {
                                translatedMessage.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent900());
                                translateSubtitle.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent900());
                            } else {
                                translatedMessage.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent());
                                translateSubtitle.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent());
                            }
                            translateSubtitle.setTextAppearance(context, CometChatTheme.getInstance(context).getTypography().getCaption2());
                            layout.addView(translateView);
                            layout.setLayoutParams(params);
                            translatedMessage.setLayoutParams(params);
                            translateSubtitle.setLayoutParams(params);
                            return layout;
                        } else {
                            return super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
                }
            } else {
                return super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
            }
        }
        return checkForTranslation(context, textMessage, super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment), gravity);
    }

    @Override
    public List<CometChatMessageOption> getTextMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        List<CometChatMessageOption> optionList = super.getTextMessageOptions(context, baseMessage, group);
        optionList.add(new CometChatMessageOption("MessageTranslationDecorator", context.getString(R.string.translate_message), CometChatTheme.getInstance(context).getPalette().getAccent(), R.drawable.ic_translate, CometChatTheme.getInstance(context).getPalette().getAccent700(), CometChatTheme.getInstance(context).getTypography().getSubtitle1(), android.R.color.transparent, () -> {
            translateMessage(context, baseMessage);
        }));
        return optionList;
    }

    @Override
    public String getId() {
        return MessageTranslationDecorator.class.getName();
    }

    private View checkForTranslation(Context context, BaseMessage baseMessage, View previousView, int gravity) {

        if (baseMessage != null) {
            if (baseMessage.getMetadata() != null && baseMessage.getMetadata().has("values")) {
                try {
                    if (Extensions.isMessageTranslated(baseMessage.getMetadata().getJSONArray("values").getJSONObject(0), ((TextMessage) baseMessage).getText())) {
                        String translatedStr = Extensions.getTranslatedMessage(baseMessage);

                        if (translatedStr != null && !translatedStr.isEmpty()) {
                            LinearLayout layout = new LinearLayout(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.gravity = gravity;
                            layout.setOrientation(LinearLayout.VERTICAL);
                            Utils.removeParentFromView(previousView);
                            layout.addView(previousView);
                            View translateView = View.inflate(context, R.layout.translate_layout, null);
                            TextView translatedMessage = translateView.findViewById(R.id.translate_message);
                            TextView translateSubtitle = translateView.findViewById(R.id.translate_subtitle);
                            translatedMessage.setText(translatedStr);

                            if (gravity == Gravity.END) {
                                translatedMessage.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent900());
                                translateSubtitle.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent900());
                            } else {
                                translatedMessage.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent());
                                translateSubtitle.setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent());
                            }
                            translateSubtitle.setTextAppearance(context, CometChatTheme.getInstance(context).getTypography().getCaption2());
                            layout.addView(translateView);
                            layout.setLayoutParams(params);
                            translatedMessage.setLayoutParams(params);
                            translateSubtitle.setLayoutParams(params);
                            return layout;
                        } else {
                            return previousView;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                return previousView;
            }
        }
        return previousView;

    }

    private void translateMessage(Context context, BaseMessage baseMessage) {
        try {
            String localeLanguage = Locale.getDefault().getLanguage();
            JSONObject body = new JSONObject();
            JSONArray languages = new JSONArray();
            languages.put(localeLanguage);
            body.put("msgId", baseMessage.getId());
            body.put("languages", languages);
            body.put("text", ((TextMessage) baseMessage).getText());
            CometChat.callExtension("message-translation", "POST", "/v2/translate", body, new CometChat.CallbackListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    if (Extensions.isMessageTranslated(jsonObject, ((TextMessage) baseMessage).getText())) {
                        if (baseMessage.getMetadata() != null) {
                            JSONObject meta = baseMessage.getMetadata();
                            try {
                                meta.accumulate("values", jsonObject);
                                baseMessage.setMetadata(meta);
                                for (CometChatMessageEvents events : CometChatMessageEvents.messageEvents.values()) {
                                    events.ccMessageEdited(baseMessage, MessageStatus.SUCCESS);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            baseMessage.setMetadata(jsonObject);
                            for (CometChatMessageEvents events : CometChatMessageEvents.messageEvents.values()) {
                                events.ccMessageSent(baseMessage, MessageStatus.SUCCESS);
                            }
                        }
                    } else {
                        showError(context,context.getString(R.string.translation_error));
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    showError(context,context.getString(R.string.something_went_wrong));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showError(Context context, String message) {
        String errorMessage = message;
        CometChatTheme theme = CometChatTheme.getInstance(context);
        new CometChatDialog(context, 0, theme.getTypography().getText1(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, theme.getPalette().getAccent(), errorMessage, "", "", context.getString(R.string.okay), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                alertDialog.dismiss();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                alertDialog.dismiss();
            }
        }, 0, false);
    }
}
