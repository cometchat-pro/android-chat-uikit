package com.cometchat.chatuikit.extensions.polls;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;

import java.util.List;

public class CometChatCreatePoll extends MaterialCardView {
    Context context;
    View view;
    String id, type;
    RelativeLayout pollLayout;
    private int viewId = 0;
    TextView setAnswerTitle, title, addOptions;
    EditText questionEditText, optionOne, optionTwo;
    View optionSeparator1, optionSeparator2, questionSeparator;
    ImageView closeIcon, createPollIcon;
    @DrawableRes
    int deleteIcon;
    RecyclerView optionsView;
    private createPoll createPoll;
    private closePoll closePoll;
    PollOptionsAdapter optionsAdapter;
    CometChatTheme theme;

    public CometChatCreatePoll(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CometChatCreatePoll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CometChatCreatePoll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        view = LayoutInflater.from(context).inflate(R.layout.add_polls_layout, null);
        deleteIcon = R.drawable.ic_delete_conversation;
        setAnswerTitle = view.findViewById(R.id.answer_text);
        title = view.findViewById(R.id.poll_title);
        addOptions = view.findViewById(R.id.add_options);
        questionEditText = view.findViewById(R.id.question_edt);
        questionSeparator = view.findViewById(R.id.question_separator);
        pollLayout = view.findViewById(R.id.poll_lay);
        closeIcon = view.findViewById(R.id.close_poll);
        createPollIcon = view.findViewById(R.id.create_poll);
        View option1 = view.findViewById(R.id.option_1);
        View option2 = view.findViewById(R.id.option_2);
        optionOne = option1.findViewById(R.id.option_txt);
        optionSeparator1 = option1.findViewById(R.id.option_separator);
        optionTwo = option2.findViewById(R.id.option_txt);
        optionSeparator2 = option2.findViewById(R.id.option_separator);
        addOptions = view.findViewById(R.id.add_options);
        optionsView = view.findViewById(R.id.rvOptions);
        optionsView.setLayoutManager(new LinearLayoutManager(context));
        optionsAdapter = new PollOptionsAdapter(context);
        optionsView.setAdapter(optionsAdapter);

//        view.setBackgroundColor(palette.getBackground());
        setStyle(new CreatePollsStyle().setBackground(theme.getPalette().getGradientBackground()).setBackground(theme.getPalette().getBackground()).setAnswerTextColor(theme.getPalette().getAccent500()).setAnswerTextAppearance(theme.getTypography().getText3()).setTitleColor(theme.getPalette().getAccent()).setTitleAppearance(theme.getTypography().getHeading()).setSeparatorColor(theme.getPalette().getAccent100()).setOptionTextColor(theme.getPalette().getAccent()).setOptionHintColor(theme.getPalette().getAccent600()).setOptionTextAppearance(theme.getTypography().getText1()).setAddOptionAppearance(theme.getTypography().getName()).setAddOptionColor(theme.getPalette().getPrimary()).setQuestionHintColor(theme.getPalette().getAccent600()).setQuestionTextColor(theme.getPalette().getAccent()).setQuestionTextAppearance(theme.getTypography().getText1()).setCloseIconTint(theme.getPalette().getPrimary()).setCreateIconTint(theme.getPalette().getPrimary())

        );
        //setting for Answers title
//        setTextAppearance(null, setAnswerTitle, typography.getText3());
//        setTextColor(null, setAnswerTitle, palette.getAccent500());
        setText(null, setAnswerTitle, context.getResources().getString(R.string.option));

        //setting for Title
//        setTextAppearance(null, title, typography.getHeading());
//        setTextColor(null, title, palette.getAccent());
        setText(null, title, context.getResources().getString(R.string.create_a_poll));

        //separator color
//        setSeparatorColor(optionSeparator1, palette.getAccent100());
//        setSeparatorColor(optionSeparator2, palette.getAccent100());
//        setSeparatorColor(questionSeparator, palette.getAccent100());

        //setting for option 1
//        setTextAppearance(optionOne, null, typography.getText1());
//        setTextColor(optionOne, null, palette.getAccent());
        setHint(optionOne, null, context.getResources().getString(R.string.enter_your_option) + " 1");
//        setHintTextColor(optionOne, null, palette.getAccent600());

        //setting for option 2
//        setTextAppearance(optionTwo, null, typography.getText1());
//        setTextColor(optionTwo, null, palette.getAccent());
        setHint(optionTwo, null, context.getResources().getString(R.string.enter_your_option) + " 2");
//        setHintTextColor(optionTwo, null, palette.getAccent600());

        //setting for addOptions
//        setTextAppearance(null, addOptions, typography.getName());
//        setTextColor(null, addOptions, palette.getPrimary());
        setText(null, addOptions, context.getResources().getString(R.string.add_a_new_option));

//        //setting for Questions
//        setTextAppearance(questionEditText, null, typography.getText1());
//        setTextColor(questionEditText, null, palette.getAccent());
        setHint(questionEditText, null, context.getResources().getString(R.string.question));
//        setHintTextColor(questionEditText, null, palette.getAccent600());
//        setIconColor(createPollIcon, palette.getPrimary());
//        setIconColor(closeIcon, palette.getPrimary());


        RecyclerViewSwipeListener swipeListener = new RecyclerViewSwipeListener(context) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                Bitmap deleteBitmap = Utils.drawableToBitmap(context.getDrawable(deleteIcon));
                underlayButtons.add(new UnderlayButton("Delete", deleteBitmap, context.getResources().getColor(R.color.red), new UnderlayButtonClickListener() {
                    @Override
                    public void onClick(final int pos) {
                        optionsAdapter.remove(pos);
                    }
                }));
            }
        };
        swipeListener.attachToRecyclerView(optionsView);

        addOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsAdapter.add(viewId++);
            }
        });
        createPollIcon.setEnabled(true);
        createPollIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionEditText.requestFocus();
                if (questionEditText.getText().toString().trim().isEmpty())
                    questionEditText.setError(context.getString(R.string.fill_this_field));
                else if (optionOne.getText().toString().trim().isEmpty())
                    optionOne.setError(context.getString(R.string.fill_this_field));
                else if (optionTwo.getText().toString().trim().isEmpty())
                    optionTwo.setError(context.getString(R.string.fill_this_field));
                else {
                    createPollIcon.setEnabled(false);
                    try {
                        JSONArray optionJson = new JSONArray();
                        optionJson.put(optionOne.getText().toString());
                        optionJson.put(optionTwo.getText().toString());
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
                        if (createPoll != null) {
                            createPoll.onCreatePoll(questionEditText.getText().toString(), optionJson);
                        }
                    } catch (Exception e) {
                        createPollIcon.setEnabled(true);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closePoll == null) {
                    ((Activity) context).onBackPressed();
                } else {
                    closePoll.onClose();
                }
            }
        });

        addView(view);
    }

    public void setStyle(CreatePollsStyle style) {
        if (style != null) {
            //setting for Answers title
            setTextAppearance(null, setAnswerTitle, style.getAnswerTextAppearance());
            setTextColor(null, setAnswerTitle, style.getAnswerTextColor());
            setText(null, setAnswerTitle, context.getResources().getString(R.string.option));

            //setting for Title
            setTextAppearance(null, title, style.getTitleAppearance());
            setTextColor(null, title, style.getTitleColor());
            setText(null, title, context.getResources().getString(R.string.create_a_poll));

            //separator color
            setSeparatorColor(optionSeparator1, style.getSeparatorColor());
            setSeparatorColor(optionSeparator2, style.getSeparatorColor());
            setSeparatorColor(questionSeparator, style.getSeparatorColor());

            //setting for option 1
            setTextAppearance(optionOne, null, style.getOptionTextAppearance());
            setTextColor(optionOne, null, style.getOptionTextColor());
            setHint(optionOne, null, context.getResources().getString(R.string.enter_your_option));
            setHintTextColor(optionOne, null, style.getOptionHintColor());

            //setting for option 2
            setTextAppearance(optionTwo, null, style.getOptionTextAppearance());
            setTextColor(optionTwo, null, style.getOptionTextColor());
            setHint(optionTwo, null, context.getResources().getString(R.string.enter_your_option));
            setHintTextColor(optionTwo, null, style.getOptionHintColor());
            optionsAdapter.setOptionTextAppearance(style.getOptionTextAppearance());
            optionsAdapter.setOptionHintColor(style.getOptionHintColor());
            optionsAdapter.setOptionTextColor(style.getOptionTextColor());
            optionsAdapter.setOptionSeparatorColor(style.getSeparatorColor());
            //setting for addOptions
            setTextAppearance(null, addOptions, style.getAddOptionAppearance());
            setTextColor(null, addOptions, style.getAddOptionColor());
            setText(null, addOptions, context.getResources().getString(R.string.add_a_new_option));

            //setting for Questions
            setTextAppearance(questionEditText, null, style.getQuestionTextAppearance());
            setTextColor(questionEditText, null, style.getQuestionTextColor());
            setHint(questionEditText, null, context.getResources().getString(R.string.question));
            setHintTextColor(questionEditText, null, style.getQuestionHintColor());

            setIconColor(createPollIcon, style.getCreateIconTint());
            setIconColor(closeIcon, style.getCloseIconTint());

            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
        }
    }

    public void setUser(User user) {
        if (user != null) {
            id = user.getUid();
            type = UIKitConstants.ReceiverType.USER;
        }
    }

    public void setGroup(Group group) {
        if (group != null) {
            id = group.getGuid();
            type = UIKitConstants.ReceiverType.GROUP;
        }
    }

    public void setTitle(String title) {
        if (title != null) this.title.setText(title);
    }

    public void setQuestionPlaceholderText(String placeholderText) {
        if (placeholderText != null) questionEditText.setHint(placeholderText);
    }

    public void setAnswerHelpText(String hint) {
        if (hint != null) {
            optionOne.setHint(hint);
            optionTwo.setHint(hint);
        }
    }

    public void setAnswerPlaceholderText(String answerPlaceholderText) {
        if (answerPlaceholderText != null) {
            setAnswerTitle.setHint(answerPlaceholderText);
        }
    }

    public void setAddAnswerText(String addAnswerHint) {
        if (addAnswerHint != null) addOptions.setText(addAnswerHint);
    }

    public void setCloseIcon(@DrawableRes int resource) {
        if (resource != 0) closeIcon.setImageResource(resource);
    }

    public void setCreatePollIcon(@DrawableRes int resource) {
        if (resource != 0) createPollIcon.setImageResource(resource);
    }

    public void setDeleteIcon(int deleteIcon) {
        this.deleteIcon = deleteIcon;
    }

    public interface createPoll {
        void onCreatePoll(String question, JSONArray jsonArray);
    }

    public interface closePoll {
        void onClose();
    }

    public void setOnCreatePoll(CometChatCreatePoll.createPoll createPoll) {
        this.createPoll = createPoll;
    }

    public void setOnClosePoll(CometChatCreatePoll.closePoll closePoll) {
        this.closePoll = closePoll;
    }

    private void setTextAppearance(EditText editText, TextView textView, @StyleRes int appearance) {
        if (textView != null && appearance != 0) textView.setTextAppearance(context, appearance);
        else if (editText != null && appearance != 0)
            editText.setTextAppearance(context, appearance);

    }

    private void setTextColor(EditText editText, TextView textView, @ColorInt int color) {
        if (textView != null && color != 0) textView.setTextColor(color);
        else if (editText != null && color != 0) editText.setTextColor(color);
    }

    private void setHintTextColor(EditText editText, TextView textView, @ColorInt int color) {
        if (textView != null && color != 0) textView.setHintTextColor(color);
        else if (editText != null && color != 0) editText.setHintTextColor(color);
    }

    private void setText(EditText editText, TextView textView, String text) {
        if (textView != null && text != null) textView.setText(text);
        else if (editText != null && text != null) editText.setText(text);
    }

    private void setHint(EditText editText, TextView textView, String text) {
        if (textView != null && text != null) textView.setHint(text);
        else if (editText != null && text != null) editText.setHint(text);
    }

    private void setSeparatorColor(View view, @ColorInt int color) {
        if (view != null && color != 0) view.setBackgroundColor(color);
    }

    private void setSeparatorDrawable(View view, @DrawableRes int resource) {
        if (view != null && resource != 0)
            view.setBackground(context.getResources().getDrawable(resource));
    }

    private void setIcon(ImageView imageView, @DrawableRes int resource) {
        if (imageView != null && resource != 0) imageView.setImageResource(resource);
    }

    private void setIconColor(ImageView imageView, @ColorInt int color) {
        if (imageView != null && color != 0)
            imageView.setImageTintList(ColorStateList.valueOf(color));
    }

    private void setPollBackgroundColor(@ColorInt int color) {
        if (color != 0 && pollLayout != null) pollLayout.setBackgroundColor(color);
    }

    private void setPollBackgroundDrawable(Drawable drawable) {
        if (drawable != null && pollLayout != null) pollLayout.setBackground(drawable);
    }

}
