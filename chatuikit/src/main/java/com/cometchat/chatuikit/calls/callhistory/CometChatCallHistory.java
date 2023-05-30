package com.cometchat.chatuikit.calls.callhistory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerViewSwipeListener;
import com.cometchat.chatuikit.shared.resources.utils.sticker_header.StickyHeaderDecoration;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatListBase.CometChatListBase;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class CometChatCallHistory extends CometChatListBase {
    private Context context;

    private View view;

    private static final String TAG = CometChatCallHistory.class.getName();

    private CallHistoryViewModel callHistoryViewModel;

    private CallHistoryAdapter callHistoryAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private LinearLayout loadingLayout;

    private LinearLayout noBaseMessageView;

    public boolean hideError;

    private View emptyView = null;

    public LinearLayout customLayout;

    private TextView emptyStateText;

    public int errorStateTextAppearance = 0;

    public int errorMessageColor = 0;

    public String errorText = null;

    public View errorView = null;

    private View loadingView = null;

    private View menuView = null;

    private OnItemClickListener<BaseMessage> onItemClickListener;

    private OnError onError;

    private OnSelection onSelection;

    private CometChatTheme theme;

    private UIKitConstants.SelectionMode selectionMode = UIKitConstants.SelectionMode.NONE;

    private HashMap<BaseMessage, Boolean> hashMap = new HashMap<>();

    private Function2<Context, BaseMessage, List<CometChatOption>> options;

    private RecyclerViewSwipeListener swipeHelper;

    private ImageView loadingIcon;

    private @DrawableRes
    int submitIcon;

    private ImageView icon;

    private StickyHeaderDecoration stickyHeaderDecoration;

    public CometChatCallHistory(Context context) {
        super(context);
        init(context, null, -1);
    }

    public CometChatCallHistory(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CometChatCallHistory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        callHistoryViewModel = new CallHistoryViewModel();
        view = View.inflate(context, R.layout.cometchat_list_view, null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Users, 0, 0);

        //Start of Handling Attributes
        String title = a.getString(R.styleable.Users_title) != null ? a.getString(R.styleable.Users_title) : getResources().getString(R.string.call_history);

        int titleColor = a.getColor(R.styleable.Users_titleColor, theme.getPalette().getAccent());

        Drawable backButtonIcon = a.getDrawable(R.styleable.Users_backButtonIcon);

        boolean showBackButton = a.getBoolean(R.styleable.Users_showBackButton, false);
        int backgroundColor = a.getColor(R.styleable.Users_backgroundColor, theme.getPalette().getBackground());

        customLayout = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.list_recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        loadingLayout = view.findViewById(R.id.loading_view);
        loadingIcon = view.findViewById(R.id.loading_icon);
        noBaseMessageView = view.findViewById(R.id.no_list_view);
        emptyStateText = view.findViewById(R.id.no_list_text);
        callHistoryAdapter = new CallHistoryAdapter(context);
        stickyHeaderDecoration = new StickyHeaderDecoration(callHistoryAdapter);
        recyclerView.setAdapter(callHistoryAdapter);
        recyclerView.addItemDecoration(stickyHeaderDecoration, 0);
        callHistoryViewModel = ViewModelProviders.of((FragmentActivity) context).get(callHistoryViewModel.getClass());
        callHistoryViewModel.getMutableCallsList().observe((AppCompatActivity) context, ListObserver);
        callHistoryViewModel.getStates().observe((AppCompatActivity) context, userStates);
        callHistoryViewModel.insertAtTop().observe((AppCompatActivity) context, insertAtTop);
        callHistoryViewModel.moveToTop().observe((AppCompatActivity) context, moveToTop);
        callHistoryViewModel.updateCall().observe((AppCompatActivity) context, update);
        callHistoryViewModel.removeCall().observe((AppCompatActivity) context, remove);
        callHistoryViewModel.getCometChatException().observe((AppCompatActivity) context, exceptionObserver);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    callHistoryViewModel.fetchCalls();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }
        });
        clickEvents();
        super.setTitle(title);
        super.backIcon(backButtonIcon);
        super.showBackButton(showBackButton);
        super.setSearchTextAppearance(theme.getTypography().getText1());
        super.setTitleAppearance(theme.getTypography().getHeading());
        emptyStateTextAppearance(theme.getTypography().getHeading());
        emptyStateText(context.getResources().getString(R.string.no_call_record));
        errorStateTextAppearance(theme.getTypography().getText1());
        emptyStateTextColor(theme.getPalette().getAccent400());
        setLoadingIconTintColor(theme.getPalette().getAccent());
        hideDateHeader(true);
        super.setTitleColor(titleColor);
        super.hideSearch(true);
        if (theme.getPalette().getGradientBackground() != null)
            setBackground(theme.getPalette().getGradientBackground());
        else setBackgroundColor(backgroundColor);

        super.addListView(view);

        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                BaseMessage User = callHistoryAdapter.getBaseMessage(viewHolder.getLayoutPosition());
                getOption(User, underlayButtons);
            }
        };
        swipeHelper.attachToRecyclerView(recyclerView);
    }

    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    private void getOption(BaseMessage user, List<RecyclerViewSwipeListener.UnderlayButton> buttons) {
        List<CometChatOption> optionsArrayList;
        if (options != null) {
            optionsArrayList = options.apply(context, user);
            for (int i = 0; i < optionsArrayList.size(); i++) {
                CometChatOption cometChatOption = optionsArrayList.get(i);
                buttons.add(new RecyclerViewSwipeListener.UnderlayButton(cometChatOption.getTitle(), Utils.drawableToBitmap(getResources().getDrawable(cometChatOption.getIcon())), cometChatOption.getBackgroundColor(), pos -> {
                    if (cometChatOption.getClick() != null) {
                        cometChatOption.getClick().onClick();
                    }
                }));
            }
        }
    }

    public void hideDateHeader(boolean hide) {
        if (hide) recyclerView.removeItemDecoration(stickyHeaderDecoration);
        else recyclerView.addItemDecoration(stickyHeaderDecoration, 0);
    }

    public List<BaseMessage> getSelectedUsers() {
        List<BaseMessage> userList = new ArrayList<>();
        for (HashMap.Entry<BaseMessage, Boolean> entry : hashMap.entrySet()) {
            userList.add(entry.getKey());
        }
        return userList;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        callHistoryViewModel.fetchCalls();
        callHistoryViewModel.addListeners();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        callHistoryViewModel.removeListeners();
    }

    @Override
    public void hideSearch(boolean hideSearch) {
        Log.i(TAG, "hideSearch: search related properties are not supported!");
    }

    public void setOptions(Function2<Context, BaseMessage, List<CometChatOption>> options) {
        this.options = options;
    }

    public void emptyStateText(String message) {
        if (message != null && !message.isEmpty()) emptyStateText.setText(message);
        else emptyStateText.setText(getResources().getString(R.string.no_user));
    }

    public void emptyStateTextColor(int color) {
        if (color != 0) emptyStateText.setTextColor(color);
    }

    public void emptyStateTextFont(String font) {
        if (font != null && !font.isEmpty())
            emptyStateText.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    public void emptyStateTextAppearance(int appearance) {
        if (appearance != 0) emptyStateText.setTextAppearance(context, appearance);

    }

    public void errorStateTextAppearance(int appearance) {
        if (appearance != 0) this.errorStateTextAppearance = appearance;
    }

    public void errorStateTextColor(int errorMessageColor) {
        if (errorMessageColor != 0) this.errorMessageColor = errorMessageColor;
    }

    public void errorStateText(String errorText) {
        if (errorText != null && !errorText.isEmpty()) this.errorText = errorText;
    }

    public void setEmptyStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                emptyView = View.inflate(context, id, null);
            } catch (Exception e) {
                emptyView = null;
                e.printStackTrace();
            }
        }
    }

    public void setLoadingIconTintColor(@ColorInt int color) {
        if (color != 0 && loadingIcon != null)
            loadingIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * @setErrorStateView is method allows you to set layout, show when there is a error
     * if user want to set Error layout other wise it will load default layout
     */
    public void setErrorStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                errorView = View.inflate(context, id, null);
            } catch (Exception e) {
                errorView = null;
                e.printStackTrace();
            }
        }
    }

    public void setLoadingStateView(@LayoutRes int id) {
        if (id != 0) {
            try {
                loadingView = View.inflate(context, id, null);
            } catch (Exception e) {
                loadingView = null;
            }
        }
    }

    public void setBackground(int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(orientation, colorArray);
        setBackground(gd);
    }

    public void setSubtitleView(Function2<Context, BaseMessage, View> subtitle) {
        callHistoryAdapter.setSubtitle(subtitle);
    }

    public void setTail(Function2<Context, BaseMessage, View> tail) {
        callHistoryAdapter.setTailView(tail);
    }

    public void setListItemView(Function2<Context, BaseMessage, View> listItemView) {
        callHistoryAdapter.setCustomView(listItemView);
    }

    public void setStyle(CallHistoryStyle style) {
        if (style != null) {
            super.setTitleAppearance(style.getTitleAppearance());
            super.setTitleFont(style.getTitleFont());
            super.setTitleColor(style.getTitleColor());
            super.backIconTint(style.getBackIconTint());

            setLoadingIconTintColor(style.getLoadingIconTint());
            emptyStateTextAppearance(style.getEmptyTextAppearance());
            errorStateTextAppearance(style.getErrorTextAppearance());
            emptyStateTextFont(style.getEmptyTextFont());
            emptyStateTextColor(style.getEmptyTextColor());
            errorStateTextColor(style.getErrorTextColor());
            callHistoryAdapter.setIncomingAudioCallIconTint(style.getIncomingAudioCallIconTint());
            callHistoryAdapter.setIncomingVideoCallIcon(style.getIncomingVideoCallIconTint());
            callHistoryAdapter.setCallStatusColor(style.getCallStatusColor());
            callHistoryAdapter.setMissedCallTitleColor(style.getMissedCallTitleColor());
            callHistoryAdapter.setInfoIconTint(style.getInfoIconTint());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setBackground(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    public void setAvatarStyle(AvatarStyle style) {
        callHistoryAdapter.setAvatarStyle(style);
    }

    public void setDateStyle(DateStyle style) {
        callHistoryAdapter.setDateStyle(style);
    }

    public void setListItemStyle(ListItemStyle style) {
        callHistoryAdapter.setListItemStyle(style);
    }

    public void setOnSelection(OnSelection onSelection) {
        this.onSelection = onSelection;
    }

    public void setMessageRequestBuilder(MessagesRequest.MessagesRequestBuilder usersRequestBuilder) {
        callHistoryViewModel.setMessagesRequestBuilder(usersRequestBuilder);
    }

    public void setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        hashMap.clear();
        callHistoryAdapter.selectBaseMessage(hashMap);
        this.selectionMode = selectionMode;
        if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode) && selectionMode != null) {
            setMenuIcon(true);
        }
    }

    public void setSubmitIcon(@DrawableRes int submitIcon) {
        if (submitIcon != 0 && icon != null) {
            icon.setImageResource(submitIcon);
        }
        this.submitIcon = submitIcon;
    }

    public void setSelectionIcon(@DrawableRes int selectionIcon) {
        callHistoryAdapter.setSelectionIcon(selectionIcon);
    }

    private void setMenuIcon(boolean value) {
        if (value && menuView == null) {
            icon = new ImageView(context);
            if (submitIcon == 0) {
                icon.setImageResource(R.drawable.ic_check_primary);
                icon.setImageTintList(ColorStateList.valueOf(theme.getPalette().getPrimary()));
            } else icon.setImageResource(submitIcon);
            super.setMenu(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelection != null) {
                        onSelection.onSelection(getSelectedUsers());
                    }
                }
            });
        } else if (!value) {
            super.hideMenuIcon(true);
        }
    }

    @Override
    public void setMenu(View view) {
        this.menuView = view;
        super.setMenu(view);
    }

    public void setIncomingAudioCallIcon(int incomingAudioCallIcon) {
        callHistoryAdapter.setIncomingAudioCallIcon(incomingAudioCallIcon);
    }

    public void setIncomingVideoCallIcon(int incomingVideoCallIcon) {
        callHistoryAdapter.setIncomingVideoCallIcon(incomingVideoCallIcon);
    }

    public void setInfoIcon(int infoIcon) {
        callHistoryAdapter.setInfoIcon(infoIcon);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public CallHistoryViewModel getViewModel() {
        return callHistoryViewModel;
    }

    public CallHistoryAdapter getCallHistoryAdapter() {
        return callHistoryAdapter;
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void clickEvents() {
        callHistoryAdapter.setOnItemClickListener(new OnItemClickListener<BaseMessage>() {
            @Override
            public void OnItemClick(BaseMessage var, int position) {
                if (!UIKitConstants.SelectionMode.NONE.equals(selectionMode)) {
                    selectCall(var, selectionMode);
                }
                if (onItemClickListener != null) onItemClickListener.OnItemClick(var, position);
            }

            @Override
            public void OnItemLongClick(BaseMessage var, int position) {
                if (onItemClickListener != null) onItemClickListener.OnItemLongClick(var, position);
            }
        });
    }

    Observer<UIKitConstants.States> userStates = states -> {
        if (UIKitConstants.States.LOADING.equals(states)) {
            if (loadingView != null) {
                loadingLayout.setVisibility(GONE);
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(loadingView);
            } else loadingLayout.setVisibility(VISIBLE);
        } else if (UIKitConstants.States.LOADED.equals(states)) {
            loadingLayout.setVisibility(GONE);
            noBaseMessageView.setVisibility(View.GONE);
            customLayout.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (UIKitConstants.States.ERROR.equals(states)) {
            showError();
        } else if (UIKitConstants.States.EMPTY.equals(states)) {
            if (emptyView != null) {
                customLayout.setVisibility(VISIBLE);
                customLayout.removeAllViews();
                customLayout.addView(emptyView);
            } else {
                noBaseMessageView.setVisibility(View.VISIBLE);
            }
            recyclerView.setVisibility(View.GONE);
        } else if (UIKitConstants.States.NON_EMPTY.equals(states)) {
            noBaseMessageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            customLayout.setVisibility(GONE);
        }
    };

    public void hideError(boolean hide) {
        hideError = hide;
    }

    private boolean errorDisplayed = false;

    private void showError() {
        String errorMessage;
        if (errorText != null) errorMessage = errorText;
        else errorMessage = getContext().getString(R.string.something_went_wrong);

        if (!hideError && errorView != null) {
            customLayout.removeAllViews();
            customLayout.addView(errorView);
            customLayout.setVisibility(VISIBLE);
        } else {
            customLayout.setVisibility(GONE);
            if (!hideError) {
                if (getContext() != null) {
                    if (!errorDisplayed) {
                        errorDisplayed = true;
                        new CometChatDialog(context, 0, errorStateTextAppearance, theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, errorMessageColor, errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                            errorDisplayed = false;
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                errorDisplayed = false;
                                alertDialog.dismiss();
                                callHistoryViewModel.fetchCalls();
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                errorDisplayed = false;
                                alertDialog.dismiss();
                            }
                        }, 0, false);
                    }
                }
            }

        }
    }

    Observer<Integer> insertAtTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            callHistoryAdapter.notifyItemInserted(integer);
            scrollToTop();
        }
    };

    Observer<Integer> moveToTop = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            callHistoryAdapter.notifyItemMoved(integer, 0);
            callHistoryAdapter.notifyItemChanged(0);
            scrollToTop();
        }
    };

    private void scrollToTop() {
        if (layoutManager.findFirstVisibleItemPosition() < 5) layoutManager.scrollToPosition(0);
    }

    Observer<Integer> update = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            callHistoryAdapter.notifyItemChanged(integer);
        }
    };

    Observer<Integer> remove = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            callHistoryAdapter.notifyItemRemoved(integer);
        }
    };
    Observer<CometChatException> exceptionObserver = exception -> {
        if (onError != null) onError.onError(context, exception);
    };


    /**
     * This method helps to get Click events of CometChatUserList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public void setItemClickListener(OnItemClickListener<BaseMessage> onItemClickListener) {
        if (onItemClickListener != null) this.onItemClickListener = onItemClickListener;
    }

    public void selectCall(BaseMessage call, UIKitConstants.SelectionMode mode) {
        if (mode != null) {
            this.selectionMode = mode;
            if (UIKitConstants.SelectionMode.SINGLE.equals(selectionMode)) {
                hashMap.clear();
                hashMap.put(call, true);
                callHistoryAdapter.selectBaseMessage(hashMap);
            } else if (UIKitConstants.SelectionMode.MULTIPLE.equals(selectionMode)) {
                if (hashMap.containsKey(call)) hashMap.remove(call);
                else hashMap.put(call, true);
                callHistoryAdapter.selectBaseMessage(hashMap);
            }
        }
    }

    public void setOnInfoIconClickListener(OnInfoIconClick click) {
        callHistoryAdapter.setInfoIconClick(click);
    }

    Observer<List<BaseMessage>> ListObserver = callList -> callHistoryAdapter.setBaseMessageList(callList);

    public OnError getOnError() {
        return onError;
    }

    public interface OnSelection {
        void onSelection(List<BaseMessage> baseMessageList);
    }

    public interface OnInfoIconClick {
        void onClick(Context context, User user, Group group, BaseMessage baseMessage);
    }
}
