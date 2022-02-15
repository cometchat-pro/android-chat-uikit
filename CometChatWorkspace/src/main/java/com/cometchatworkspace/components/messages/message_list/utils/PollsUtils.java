package com.cometchatworkspace.components.messages.message_list.utils;

import static android.view.View.GONE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.resources.utils.CometChatError;

public class PollsUtils {
    private static final String TAG = PollsUtils.class.getSimpleName();

    public static void createPollDialog(Context context,String Id, String type) {
        ArrayList<View> optionsArrayList = new ArrayList<>();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.add_polls_layout, null);
        alertDialog.setView(view);
        Dialog dialog = alertDialog.create();
        EditText questionEdt = view.findViewById(R.id.question_edt);
        View option1 = view.findViewById(R.id.option_1);
        View option2 = view.findViewById(R.id.option_2);
        option1.findViewById(R.id.delete_option).setVisibility(GONE);
        option2.findViewById(R.id.delete_option).setVisibility(GONE);
        MaterialCardView addOption = view.findViewById(R.id.add_options);
        LinearLayout optionLayout = view.findViewById(R.id.options_layout);
        MaterialButton addPoll = view.findViewById(R.id.add_poll);
        addPoll.setBackgroundColor(Color.parseColor(CometChatTheme.primaryColor));
        ImageView cancelPoll = view.findViewById(R.id.close_poll);
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View optionView = LayoutInflater.from(context).inflate(R.layout.polls_option, null);
                optionsArrayList.add(optionView);
                optionLayout.addView(optionView);
                optionView.findViewById(R.id.delete_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsArrayList.remove(optionView);
                        optionLayout.removeView(optionView);
                    }
                });
            }
        });
        addPoll.setEnabled(true);
        addPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionEdt.requestFocus();
                EditText option1Text = option1.findViewById(R.id.option_txt);
                EditText option2Text = option2.findViewById(R.id.option_txt);
                if (questionEdt.getText().toString().trim().isEmpty())
                    questionEdt.setError(context.getString(R.string.fill_this_field));
                else if (option1Text.getText().toString().trim().isEmpty())
                    option1Text.setError(context.getString(R.string.fill_this_field));
                else if (option2Text.getText().toString().trim().isEmpty())
                    option2Text.setError(context.getString(R.string.fill_this_field));
                else {
                    ProgressDialog progressDialog;
                    progressDialog = ProgressDialog.show(context, "",
                            context.getResources().getString(R.string.create_a_poll));
                    addPoll.setEnabled(false);
                    try {
                        JSONArray optionJson = new JSONArray();
                        optionJson.put(option1Text.getText().toString());
                        optionJson.put(option2Text.getText().toString());
                        for (View views : optionsArrayList) {
                            EditText optionsText = views.findViewById(R.id.option_txt);
                            if (!optionsText.getText().toString().trim().isEmpty())
                                optionJson.put(optionsText.getText().toString());
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("question", questionEdt.getText().toString());
                        jsonObject.put("options", optionJson);
                        jsonObject.put("receiver", Id);
                        jsonObject.put("receiverType", type);
                        CometChat.callExtension("polls", "POST", "/v2/create",
                                jsonObject, new CometChat.CallbackListener<JSONObject>() {
                                    @Override
                                    public void onSuccess(JSONObject jsonObject) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(CometChatException e) {
                                        progressDialog.dismiss();
                                        addPoll.setEnabled(true);
                                        Log.e(TAG, "onErrorCallExtension: " + e.getMessage());
                                        if (view != null)
                                            CometChatSnackBar.show(context, view,
                                                    CometChatError.Extension.localized(e, "polls")
                                                    , CometChatSnackBar.ERROR);
                                    }
                                });
                    } catch (Exception e) {
                        addPoll.setEnabled(true);
                        Log.e(TAG, "onErrorJSON: " + e.getMessage());
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        cancelPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
