package com.cometchatworkspace.components.messages.message_list.utils.polls;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.resources.utils.CometChatError;

public class PollsUtils {
    private static final String TAG = PollsUtils.class.getSimpleName();
    private static int id = 0;
    private static Context context;

    public static void createPollDialog(Context context, String Id, String type) {
        PollsUtils.context = context;
        Palette palette = Palette.getInstance(context);
        Typography typography = Typography.getInstance();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);

        View view = LayoutInflater.from(context).inflate(R.layout.add_polls_layout, null);
        view.setBackgroundColor(palette.getBackground());
        View option1 = view.findViewById(R.id.option_1);
        View option2 = view.findViewById(R.id.option_2);
        TextView setAnswer_text = view.findViewById(R.id.setAnswer_text);
        setTextTheme(setAnswer_text, context.getResources().getString(R.string.option), palette.getAccent500(), typography.getText2());
        TextView pollTitle = view.findViewById(R.id.poll_title);
        setTextTheme(pollTitle, context.getResources().getString(R.string.create_a_poll), palette.getPrimary(), typography.getHeading());
        EditText option1_editText = option1.findViewById(R.id.option_txt);
        View option_separator = option1.findViewById(R.id.option_separator);
        option_separator.setBackgroundColor(palette.getAccent100());
        setEditTextTheme(option1_editText, context.getResources().getString(R.string.enter_your_option) + " " + 1, palette.getAccent600(), palette.getAccent(), typography.getText1());
        EditText option2_editText = option2.findViewById(R.id.option_txt);
        View option_separator2 = option2.findViewById(R.id.option_separator);
        option_separator2.setBackgroundColor(palette.getAccent100());
        setEditTextTheme(option2_editText, context.getResources().getString(R.string.enter_your_option) + " " + 2, palette.getAccent600(), palette.getAccent(), typography.getText1());
        alertDialog.setView(view);
        Dialog dialog = alertDialog.create();
        Drawable drawable = context.getDrawable(R.drawable.curved_bg);
        drawable.setTint(palette.getAccent900());
        dialog.getWindow().setBackgroundDrawable(drawable);
        EditText questionEdt = view.findViewById(R.id.question_edt);
        View question_separator = view.findViewById(R.id.question_separator);
        question_separator.setBackgroundColor(palette.getAccent100());
        setEditTextTheme(questionEdt, context.getResources().getString(R.string.question), palette.getAccent600(), palette.getAccent(), typography.getText1());
        questionEdt.setHintTextColor(palette.getAccent600());
        questionEdt.setTextColor(palette.getAccent());
        RecyclerView optionsView = view.findViewById(R.id.rvOptions);
        optionsView.setLayoutManager(new LinearLayoutManager(context));
        PollOptionsAdapter optionsAdapter = new PollOptionsAdapter(context);
        optionsView.setAdapter(optionsAdapter);
        RecyclerViewSwipeListener swipeListener = new RecyclerViewSwipeListener(context) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                Bitmap deleteBitmap = Utils.drawableToBitmap(context.getDrawable(R.drawable.ic_delete_conversation));
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        deleteBitmap,
                        context.getResources().getColor(R.color.red),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                optionsAdapter.remove(pos);
                            }
                        }
                ));
            }

        };
        swipeListener.attachToRecyclerView(optionsView);
        TextView addOption = view.findViewById(R.id.add_options);
        setTextTheme(addOption, context.getResources().getString(R.string.add_a_new_option), palette.getPrimary(), typography.getName());

        LinearLayout optionLayout = view.findViewById(R.id.options_layout);
        ImageView addPoll = view.findViewById(R.id.create_poll);
        addPoll.setImageTintList(ColorStateList.valueOf(palette.getPrimary()));
        ImageView cancelPoll = view.findViewById(R.id.close_poll);
        cancelPoll.setImageTintList(ColorStateList.valueOf(palette.getPrimary()));
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsAdapter.add(id++);
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
                        for (int i = 0; i < optionsAdapter.getItemCount(); i++) {
                            View parentView = optionsView.getChildAt(i);
                            if (parentView != null) {
                                EditText optionsText = parentView.findViewById(R.id.option_txt);
                                if (optionsText != null) {
                                    if (!optionsText.getText().toString().trim().isEmpty())
                                        optionJson.put(optionsText.getText().toString());
                                }
                            }
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
                                        if (view != null)
                                            CometChatSnackBar.show(context, view,
                                                    CometChatError.Extension.localized(e, "polls")
                                                    , CometChatSnackBar.ERROR);
                                    }
                                });
                    } catch (Exception e) {
                        addPoll.setEnabled(true);
                        progressDialog.dismiss();
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

    private static void setTextTheme(TextView addOption, String string, int primary, int name) {
        addOption.setText(string);
        addOption.setTextColor(primary);
        addOption.setTextAppearance(context, name);
    }

    private static void setEditTextTheme(EditText editText, String text, int hintColor, int textColor, int textAppearance) {
        editText.setHint(text);
        editText.setHintTextColor(hintColor);
        editText.setTextColor(textColor);
        editText.setTextAppearance(context, textAppearance);
    }
}
