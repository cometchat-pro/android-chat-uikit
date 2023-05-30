package com.cometchat.chatuikit.shared.views.cometchatActionSheet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.extensions.Extensions;
import com.cometchat.chatuikit.shared.models.Reaction;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.ClickListener;
import com.cometchat.chatuikit.shared.resources.utils.recycler_touch.RecyclerTouchListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * The CometChatActionSheet class provides the implementation of a bottom sheet that displays a list of actions or options that can be performed within the app. It is part of the CometChat UI Kit library for Android and is used to display contextual actions that can be performed on a selected item or within a specific context.
 *
 * <p>The actions displayed in the action sheet are represented as a list of ActionItem objects, each of which contains an icon, a title, and an optional subtitle.</p>
 *
 * <p>Users can interact with the action sheet by selecting an item, which triggers a callback to the onActionClicked method of the CometChatActionSheetClickListener interface. The user can also dismiss the action sheet by tapping outside the sheet or by swiping it down.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * // Create a new action sheet with a title and a footer view
 * CometChatActionSheet cometChatActionSheet = new CometChatActionSheet(context);
 * cometChatActionSheet.setTitle("Title");
 *
 * // Create a new action item and add it to the sheet
 * List<ActionItem> itemList = new ArrayList<>();
 * //how to create ActionItem object to render in a list
 * ActionItem item1 = new ActionItem("id", "text",R.drawable.icon);
 *
 * //how to create ActionItem object with all the customization
 * ActionItem item2 = new ActionItem("id",R.drawable.ic_attachment,0,0,"text3",null,0,0,0,0));
 * itemList.add(item1);
 * itemList.add(item2);
 * cometChatActionSheet.setList(itemList);
 *
 * // Set the action sheet click listener
 * cometChatActionSheet.setEventListener(new CometChatActionSheetListener() { @Override
 *         public void onActionItemClick(ActionItem actionItem) {
 *         // Handle the action click event
 *     }
 * });
 *
 * // Show the action sheet
 * cometChatActionSheet.show();
 * }
 * </pre>
 *
 * @see ActionItem
 * @see CometChatActionSheetListener
 */

public class CometChatActionSheet extends BottomSheetDialogFragment {

    private boolean isGridLayout;

    private RecyclerView recyclerViewLayout;

    private LinearLayout reactionsLayout;

    private CometChatActionSheetListener cometChatActionSheetListener;

    private String type;

    private View view;

    private int columnCount = 2;

    private BottomSheetBehavior behavior;

    private FrameLayout bottomSheet;

    private boolean reactionVisible;

    private List<ActionItem> listOfItems = new ArrayList<>();

    private ImageView layoutModeButton;

    private TextView title;

    private FontUtils fontUtils;

    private ActionSheetAdapter adapter;

    private LinearLayout bottomLayout;

    private ActionSheetStyle actionSheetStyle;

    private FragmentManager fm;

    private Context context;
    private String titleText;
    private String titleTextFont;
    private int titleTextAppearance;
    private int titleTextColor;
    private int backgroundColor;
    private boolean hideTitle;
    private Drawable backgroundDrawable;
    private Drawable layoutModeButtonIcon;
    private int layoutModeButtonColor;
    private int layoutModeButtonBackgroundColor;
    private boolean hideLayoutMode;

    public CometChatActionSheet(Context context) {
        this.context = context;
        fm = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    private int initialReactions = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontUtils = FontUtils.getInstance(getContext());
        if (getArguments() != null) {
            reactionVisible = getArguments().getBoolean("reactionVisible");
            String layoutMode = getArguments().getString("layoutMode");
            isGridLayout = ActionSheet.LayoutMode.gridMode.equalsIgnoreCase(layoutMode);
            listOfItems = getArguments().getParcelableArrayList("listOfItems");
            columnCount = getArguments().getInt("columnCount", columnCount);
        }
    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cometchat_action_sheet_list, container, false);
        bottomLayout = view.findViewById(R.id.bottom_layout);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                // androidx should use: com.google.android.material.R.id.design_bottom_sheet
                bottomSheet = (FrameLayout)
                        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });

        reactionsLayout = view.findViewById(R.id.initial_reactions);
        if (reactionVisible) {
            reactionsLayout.setVisibility(View.VISIBLE);
            setupReactions();
        } else
            reactionsLayout.setVisibility(View.GONE);

        recyclerViewLayout = view.findViewById(R.id.recyclerView);
        if (recyclerViewLayout != null) {
            if (isGridLayout)
                recyclerViewLayout.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
            else
                recyclerViewLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        initializeAdapter();
        title = view.findViewById(R.id.title);
        layoutModeButton = view.findViewById(R.id.layout_mode_button);
        layoutModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutModeButton.animate().rotationBy(360).setDuration(500).start();
                changeLayoutMode();
                initializeAdapter();
            }
        });

        recyclerViewLayout.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewLayout, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ActionItem item = (ActionItem) view.getTag(R.string.action_item);
                if (cometChatActionSheetListener != null)
                    cometChatActionSheetListener.onActionItemClick(item);
            }
        }));

        setBackground(backgroundColor);
        setBackground(backgroundDrawable);
        setTitleColor(titleTextColor);
        setTitleFont(titleTextFont);
        setTitleAppearance(titleTextAppearance);
        setLayoutModeIconTint(layoutModeButtonColor);
        setLayoutModeIconBackgroundColor(layoutModeButtonBackgroundColor);
        hideTitle(hideTitle);
        hideLayoutMode(hideLayoutMode);
        setStyle(actionSheetStyle);
        return view;
    }

    private void changeLayoutMode() {
        if (isGridLayout) {
            layoutModeButton.setImageResource(R.drawable.cc_ic_grid);
            isGridLayout = false;
            recyclerViewLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            layoutModeButton.setImageResource(R.drawable.ic_list_bulleted_white_24dp);
            isGridLayout = true;
            recyclerViewLayout.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        }
    }

    private void initializeAdapter() {
        adapter = new ActionSheetAdapter(getContext(), listOfItems, isGridLayout);
        recyclerViewLayout.setAdapter(adapter);
        adapter.hideText(columnCount > 3);
        adapter.setStyle(actionSheetStyle);
        recyclerViewLayout.scheduleLayoutAnimation();
    }

    private void setupReactions() {
        List<Reaction> reactions = Extensions.getInitialReactions(initialReactions);
        for (Reaction reaction : reactions) {
            View vw = LayoutInflater.from(getContext()).inflate(R.layout.reaction_list_row, null);
            TextView textView = vw.findViewById(R.id.reaction);
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            params.rightMargin = 16;
            params.bottomMargin = 8;
            params.topMargin = 8;
            textView.setLayoutParams(params);
            textView.setText(reaction.getName());
            reactionsLayout.addView(vw);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cometChatActionSheetListener != null)
                        cometChatActionSheetListener.onReactionClick(reaction);
                    dismiss();
                }
            });
        }
    }

    /**
     * Displays the action sheet on the screen. This method should be called after the action sheet has been configured with the appropriate items and click listener.
     *
     * <p>The show() method calls the show() method of the FragmentManager class, passing the FragmentManager and the name of the CometChatActionSheet class as parameters. This displays the action sheet as a bottom sheet on the screen, ready for user interaction.</p>
     */
    public void show() {
        if(this.isAdded())
        {
            return; //or return false/true, based on where you are calling from
        }
        show(fm, CometChatActionSheet.class.getName());
    }

    /**
     * Sets the title of the action sheet to the specified string.
     *
     * <p>The title of the action sheet is displayed at the top of the sheet and provides a brief description of the context or purpose of the sheet.</p>
     *
     * <p>If the title view has already been initialized and the specified string is not null, the title text will be set to the new value.</p>
     *
     * @param title The string to set as the new title of the action sheet
     */
    public void setTitle(String title) {
        this.titleText = title;
        if (this.title != null && title != null)
            this.title.setText(title);
    }

    /**
     * Sets the font of the title of the action sheet to the specified font.
     *
     * <p>The font of the title of the action sheet can be changed to customize the visual appearance of the sheet.</p>
     *
     * <p>If the title view has already been initialized and the specified font is not null, the title font will be set to the new value.</p>
     *
     * @param font The font to set as the new title font of the action sheet
     */
    public void setTitleFont(String font) {
        this.titleTextFont = font;
        if (title != null && font != null)
            title.setTypeface(fontUtils.getTypeFace(font));
    }

    /**
     * Sets the text appearance of the title of the action sheet to the specified style resource.
     *
     * <p>The text appearance of the title of the action sheet can be changed to customize the visual appearance of the sheet.</p>
     *
     * <p>If the title view has already been initialized and the specified appearance is not 0, the title appearance will be set to the new value.</p>
     *
     * @param appearance The style resource to set as the new title text appearance of the action sheet
     */
    public void setTitleAppearance(@StyleRes int appearance) {
        this.titleTextAppearance = appearance;
        if (title != null && appearance != 0)
            title.setTextAppearance(getContext(), appearance);
    }

    /**
     * Sets the color of the title text of the action sheet to the specified color integer.
     *
     * <p>The color of the title text of the action sheet can be changed to customize the visual appearance of the sheet.</p>
     *
     * <p>If the title view has already been initialized and the specified color is not 0, the title text color will be set to the new value.</p>
     *
     * @param color The color integer to set as the new title text color of the action sheet
     */
    public void setTitleColor(@ColorInt int color) {
        this.titleTextColor = color;
        if (color != 0 && title != null)
            title.setTextColor(color);
    }

    /**
     * Sets the visibility of a title in a user interface.
     *
     * @param hide a boolean that specifies whether the title should be hidden or show
     * In other words, if the "hide" parameter is true, the title will be hidden;
     * if it's false, the title will be shown.
     *
     * The method assumes that there is a View object called "title" that represents the
     * title to be hidden or shown, and that this View is contained within the "bottomLayout" layout.
     */
    public void hideTitle(boolean hide) {
        this.hideTitle = hide;
        if (bottomLayout != null) {
            if (hide)
                title.setVisibility(View.GONE);
            else
                title.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the background color of a layout.
     *
     * @param color an integer representing the color to be set, in the form of a color int
     *
     * This method sets the background color of a layout to the specified color.
     * It first sets the "backgroundColor" field of the class to the provided color.
     * to the provided color using the "setBackgroundColor" method.
     *
     *  <br>
     * Example usage:
     * <pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     * // Set the background color for the Action Sheet
     * int color = ContextCompat.getColor(this, R.color.white);
     * actionSheet.setBackground(ActionSheet.LayoutMode.gridMode);
     * }</pre>
     */
    public void setBackground(@ColorInt int color) {
        this.backgroundColor = color;
        if (bottomLayout != null)
            bottomLayout.setBackgroundColor(color);
    }

    /**
     * Sets the background drawable of a layout.
     *
     * @param drawable a Drawable object representing the background to be set
     *
     * This method sets the background drawable of a layout to the specified drawable.
     * It first sets the "backgroundDrawable" field of the class to the provided drawable.
     * to the provided drawable using the "setBackground" method.
     *
     * Example usage:
     * <pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     * // Set the background for action sheet
     * Drawable background = ContextCompat.getDrawable(this, R.drawable.ic_layout_mode);
     * actionSheet.setBackground(background);
     * }</pre>
     */
    public void setBackground(Drawable drawable) {
        this.backgroundDrawable = drawable;
        if (bottomLayout != null)
            bottomLayout.setBackground(drawable);
    }

    /**
     * Sets the layout mode for an ActionSheet.
     *
     * @param mode a string representing the layout mode to be set
     *
     * This method sets the layout mode for an ActionSheet. It takes a string parameter called "mode"
     * which represents the layout mode to be set.
     *  <br>
     * Example usage:
     * <pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     * // Set the layout mode
     * actionSheet.setLayoutMode(ActionSheet.LayoutMode.gridMode);
     * }</pre>
     */
    public void setLayoutMode(@ActionSheet.LayoutMode String mode) {
        Bundle bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putString("layoutMode", mode);
        setArguments(bundle);
    }

    /**
     * Sets the icon for the layout mode button in an ActionSheet.
     *
     * @param icon a Drawable object representing the icon to be set
     *
     * This method sets the icon for the layout mode button in an ActionSheet. It first sets the
     * "layoutModeButtonIcon" field of the class to the provided icon.
     *
     * Example usage:
     * <pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     * // Set the icon for the layout mode button
     * Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_layout_mode);
     * actionSheet.setLayoutModeIcon(icon);
     * }</pre>
     */
    public void setLayoutModeIcon(Drawable icon) {
        this.layoutModeButtonIcon = icon;
        if (layoutModeButton != null && icon != null)
            layoutModeButton.setImageDrawable(icon);
    }

    /**
     * Sets the tint color for the layout mode button icon in an ActionSheet.
     *
     * @param color an integer representing the color to be set for the icon
     *
     * This method sets the tint color for the layout mode button icon in an ActionSheet. It first sets
     * the "layoutModeButtonColor" field of the class to the provided color. Then, if the color is not
     * zero and the "layoutModeButton" is not null, it sets the image tint list of the "layoutModeButton"
     * to a ColorStateList with the provided color value using the "setImageTintList" method.
     *
     * Example usage:
     * <pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     * // Set the tint color for the layout mode button icon
     * int color = ContextCompat.getColor(this, R.color.white);
     * actionSheet.setLayoutModeIconTint(color);
     * }</pre>
     */
    public void setLayoutModeIconTint(@ColorInt int color) {
        this.layoutModeButtonColor = color;
        if (color != 0 && layoutModeButton != null)
            layoutModeButton.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the background color for the layout mode button icon in an ActionSheet.
     *
     * @param color an integer representing the color to be set for the icon's background
     *
     * This method sets the background color for the layout mode button icon in an ActionSheet. It first
     * sets the "layoutModeButtonBackgroundColor" field of the class to the provided color. Then, if the
     * color is not zero and the "layoutModeButton" is not null, it sets the background color of the
     * "layoutModeButton" to the provided color value using the "setBackgroundColor" method.
     *
     * Example usage:
     *<pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     *
     * // Set the background color for the layout mode button icon
     * int color = ContextCompat.getColor(this, R.color.white);
     * actionSheet.setLayoutModeIconBackgroundColor(color);
     * }</pre>
     */
    public void setLayoutModeIconBackgroundColor(@ColorInt int color) {
        this.layoutModeButtonBackgroundColor = color;
        if (color != 0 && layoutModeButton != null)
            layoutModeButton.setBackgroundColor(color);
    }

    /**
     * Sets the visibility of the layout mode button in an ActionSheet.
     *
     * @param isHidden a boolean value indicating whether the layout mode button should be hidden or shown
     *
     * This method sets its visibility to either "GONE" or "VISIBLE" of mode button based on the provided boolean value in an ActionSheet.
     *
     * Example usage:
     *
     *<pre>{@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     *
     * // Hide the layout mode button
     * actionSheet.hideLayoutMode(true);
     *
     * }</pre>
     */
    public void hideLayoutMode(boolean isHidden) {
        this.hideLayoutMode = isHidden;
        if (layoutModeButton != null) {
            if (isHidden)
                layoutModeButton.setVisibility(View.GONE);
            else
                layoutModeButton.setVisibility(View.VISIBLE);
        }
    }

    public void add(ActionItem actionItem) {
        if (adapter != null)
            adapter.addActionItem(actionItem);
    }

    public void update(ActionItem actionItem) {
        if (adapter != null)
            adapter.updateActionItem(actionItem);
    }

    /**
     * Sets the {@link ActionSheetStyle} for this ActionSheet and applies it to the UI components.
     *
     * @param actionSheetStyle The style to apply to the ActionSheet.
     */
    public void setStyle(ActionSheetStyle actionSheetStyle) {
        this.actionSheetStyle = actionSheetStyle;
        if (actionSheetStyle != null) {
            if (actionSheetStyle.getDrawableBackground() != null) {
                setBackground(actionSheetStyle.getDrawableBackground());
            } else if (actionSheetStyle.getBackground() != 0) {
                setBackground(actionSheetStyle.getBackground());
            }
            if (actionSheetStyle.getTitleColor() != 0) {
                setTitleColor(actionSheetStyle.getTitleColor());
            }
            if (actionSheetStyle.getTitleFont() != null) {
                setTitleFont(actionSheetStyle.getTitleFont());
            }
            if (actionSheetStyle.getTitleAppearance() != 0) {
                setTitleAppearance(actionSheetStyle.getTitleAppearance());
            }
            if (actionSheetStyle.getLayoutModeIconTint() != 0) {
                setLayoutModeIconTint(actionSheetStyle.getLayoutModeIconTint());
            }
            if (actionSheetStyle.getLayoutModeIconBackgroundColor() != 0) {
                setLayoutModeIconBackgroundColor(actionSheetStyle.getLayoutModeIconBackgroundColor());
            }
            if (adapter != null) {
                adapter.setStyle(actionSheetStyle);
            }
        }
    }

    /**
     * Sets the list of action items for an ActionSheet.
     *
     * @param list a List of ActionItem objects to be displayed in the ActionSheet
     *
     * This method sets the "listOfItems" argument of the ActionSheet fragment to the provided List of
     * ActionItem objects. The List is first added to a Bundle and then passed as an argument to the
     * fragment using the "setArguments" method.
     *
     * Example usage:
     *<pre>{@code
     * // Create a List of ActionItem objects
     * List<ActionItem> itemList = new ArrayList<>();
     * itemList.add(new ActionItem("id1","Item 1", R.drawable.ic_item_1));
     * itemList.add(new ActionItem("id2","Item 2", R.drawable.ic_item_2));
     * itemList.add(new ActionItem("id3","Item 3", R.drawable.ic_item_3));
     *
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     *
     * // Set the list of items for the ActionSheet
     * actionSheet.setList(itemList);
     *
     * // Show the ActionSheet
     * actionSheet.show();
     * }</pre>
     */
    public void setList(List<ActionItem> list) {
        Bundle bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putParcelableArrayList("listOfItems", (ArrayList<? extends Parcelable>) list);
        setArguments(bundle);
    }

    private void showReactions(boolean isVisible) {
        Bundle bundle = getArguments();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putBoolean("reactionVisible", isVisible);
        setArguments(bundle);
    }

    private void initialReaction(int count) {
        initialReactions = count;
    }

    /**
     * Sets the event listener for the ActionSheet.
     *
     * @param actionSheetListener a CometChatActionSheetListener object to handle ActionSheet events
     *
     * This method sets the event listener for the ActionSheet. The listener must implement the
     * CometChatActionSheetListener interface, which provides callbacks for ActionSheet events such as
     * item click and cancel.
     *<br>
     * Example usage:
     *<pre>
     *     {@code
     * // Create an instance of the ActionSheet
     * CometChatActionSheet actionSheet = new CometChatActionSheet(context);
     *
     * // Set the event listener for the ActionSheet
     * actionSheet.setEventListener(new CometChatActionSheetListener() { @Override
     *             public void onActionItemClick(ActionItem actionItem) {
     *                // Handle the action click event
     *             }
     *         });
     * // Show the ActionSheet
     * actionSheet.show();
     * }
     * </pre>
     */
    public void setEventListener(CometChatActionSheetListener actionSheetListener) {
        this.cometChatActionSheetListener = actionSheetListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

}