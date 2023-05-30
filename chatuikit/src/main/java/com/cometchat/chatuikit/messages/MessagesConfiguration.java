package com.cometchat.chatuikit.messages;

import android.content.Context;
import android.view.View;

import androidx.annotation.RawRes;

import com.cometchat.chatuikit.details.DetailsConfiguration;
import com.cometchat.chatuikit.messagecomposer.MessageComposerConfiguration;
import com.cometchat.chatuikit.messageheader.MessageHeaderConfiguration;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.threadedmessages.ThreadedMessagesConfiguration;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

/**
 * The MessagesConfiguration class represents the configuration settings for the Messages component in a chat UI kit.
 * It provides methods to set various configuration options such as message list configuration, style, message header view,
 * message header configuration, message composer configuration, message composer view, typing indicators disable flag,
 * message composer hide flag, message header hide flag, message list view, sound disable flag, custom sound for incoming
 * messages, custom sound for outgoing messages, threaded messages configuration, details configuration, auxiliary header
 * menu, and hide details flag.
 * This class allows customization of the Messages component by providing a fluent interface to set different configuration options.
 * Once the configuration is set, it can be applied to the Messages component to reflect the desired behavior and appearance.
 * Each configuration option can be set individually or in combination with other options as per the requirements of the chat UI.
 * The MessagesConfiguration object can be used in conjunction with the Messages component to create a fully customized chat
 * interface with specific functionality and visual elements tailored to the application's needs.
 */
public class MessagesConfiguration {
    private MessageListConfiguration messageListConfiguration;
    private MessagesStyle style;
    private Function3<Context, User, Group, View> messageHeaderView;
    private MessageHeaderConfiguration messageHeaderConfiguration;
    private MessageComposerConfiguration messageComposerConfiguration;
    private Function3<Context, User, Group, View> messageComposerView;
    private boolean disableTyping;
    private boolean hideMessageComposer;
    private boolean hideMessageHeader;
    private Function3<Context, User, Group, View> messageListView;
    private boolean disableSoundForMessages;
    private @RawRes
    int customSoundForIncomingMessages;
    private @RawRes
    int customSoundForOutgoingMessages;
    private ThreadedMessagesConfiguration threadedMessagesConfiguration;
    private DetailsConfiguration detailsConfiguration;
    private Function3<Context, User, Group, View> auxiliaryOption;
    private boolean hideDetails;

    /**
     * Sets the configuration for the message list.
     *
     * @param messageListConfiguration The configuration object specifying the settings for the message list.
     */
    public MessagesConfiguration setMessageListConfiguration(MessageListConfiguration messageListConfiguration) {
        this.messageListConfiguration = messageListConfiguration;
        return this;
    }

    /**
     * The setStyle method sets the visual style of the CometChatMessages using a provided MessagesStyle object.
     * It allows customization of attributes such as background, border width, corner radius, and border color.
     * If the style parameter is not null, the specified style attributes are applied to the object.
     * If style is null, no changes to the style will be made.
     *
     * @param style The {@link MessagesStyle} object containing the style attributes to be applied.
     *              If null, no style changes will be made.
     */
    public MessagesConfiguration setStyle(MessagesStyle style) {
        this.style = style;
        return this;
    }

    /**
     * The setAuxiliaryHeaderMenu method is a public method within the CometChatMessages class. It takes a {@link Function3 } object as a parameter, which represents a function that accepts three arguments (Context, User, and Group) and returns a View object.
     * <br>
     * Example :
     * <pre>{@code
     *  MessagesConfiguration  messagesConfiguration = new MessagesConfiguration();
     *  messagesConfiguration.setAuxiliaryHeaderMenu((context, user, group) -> {
     *  View view = View.inflate(context,R.layout.example_layout,null);
     *  return view;
     * });
     * }
     * </pre>
     *
     * @param auxiliaryOption
     */
    public MessagesConfiguration setAuxiliaryHeaderMenu(Function3<Context, User, Group, View> auxiliaryOption) {
        this.auxiliaryOption = auxiliaryOption;
        return this;
    }

    /**
     * Sets whether to hide the details section in the Messages component.
     *
     * @param hide True to hide the details section, false otherwise.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration hideDetails(boolean hide) {
        this.hideDetails = hide;
        return this;
    }

    /**
     * Sets the configuration for the ThreadedMessages component.
     *
     * @param threadedMessagesConfiguration The ThreadedMessagesConfiguration object.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setThreadedMessagesConfiguration(ThreadedMessagesConfiguration threadedMessagesConfiguration) {
        this.threadedMessagesConfiguration = threadedMessagesConfiguration;
        return this;
    }

    /**
     * Sets the configuration for the Details component.
     *
     * @param detailsConfiguration The DetailsConfiguration object.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setDetailsConfiguration(DetailsConfiguration detailsConfiguration) {
        this.detailsConfiguration = detailsConfiguration;
        return this;
    }

    /**
     * Sets a custom message header view using the provided function.
     * <br>
     * Example :
     * <pre>{@code
     *  MessagesConfiguration  messagesConfiguration = new MessagesConfiguration();
     *  messagesConfiguration.setMessageHeaderView((context, user, group) -> {
     *             TextView textView = new TextView(context);
     *             textView.setText("custom header view");
     *             return textView;
     *         });
     * }
     * </pre>
     *
     * @param messageHeaderView A function that takes a Context, User, and Group as parameters and returns a View.
     *                          This function is responsible for creating and returning the custom message header view.
     *                          If null is passed, the existing message header view will not be modified.
     */
    public MessagesConfiguration setMessageHeaderView(Function3<Context, User, Group, View> messageHeaderView) {
        this.messageHeaderView = messageHeaderView;
        return this;
    }

    /**
     * Sets the configuration for the MessageHeader component.
     *
     * @param messageHeaderConfiguration The MessageHeaderConfiguration object.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setMessageHeaderConfiguration(MessageHeaderConfiguration messageHeaderConfiguration) {
        this.messageHeaderConfiguration = messageHeaderConfiguration;
        return this;
    }

    /**
     * Sets the configuration for the MessageComposer component.
     *
     * @param messageComposerConfiguration The MessageComposerConfiguration object.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setMessageComposerConfiguration(MessageComposerConfiguration messageComposerConfiguration) {
        this.messageComposerConfiguration = messageComposerConfiguration;
        return this;
    }

    /**
     * Sets a custom message composer view using the provided function.
     * <br>
     * Example :
     * <pre>{@code
     *  MessagesConfiguration  messagesConfiguration = new MessagesConfiguration();
     *  messagesConfiguration.setMessageComposerView(new Function3<Context, User, Group, View>() { @Override
     *  public View apply(Context context, User user, Group group) {
     *    EditText editText = new EditText(context);
     *    return editText;
     * }
     * });
     * }
     * </pre>
     *
     * @param messageComposerView A function that takes a Context, User, and Group as parameters and returns a View.
     *                            This function is responsible for creating and returning the custom message composer view.
     *                            If null is passed, the existing message composer view will not be modified.
     */
    public MessagesConfiguration setMessageComposerView(Function3<Context, User, Group, View> messageComposerView) {
        this.messageComposerView = messageComposerView;
        return this;
    }

    /**
     * Sets whether typing indicators are disabled in the Messages component.
     *
     * @param disableTyping True to disable typing indicators, false otherwise.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setDisableTyping(boolean disableTyping) {
        this.disableTyping = disableTyping;
        return this;
    }

    /**
     * Sets whether to hide the message composer in the Messages component.
     *
     * @param hideMessageComposer True to hide the message composer, false otherwise.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setHideMessageComposer(boolean hideMessageComposer) {
        this.hideMessageComposer = hideMessageComposer;
        return this;
    }

    /**
     * Sets whether to hide the message header in the Messages component.
     *
     * @param hideMessageHeader True to hide the message header, false otherwise.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setHideMessageHeader(boolean hideMessageHeader) {
        this.hideMessageHeader = hideMessageHeader;
        return this;
    }

    /**
     * Sets a custom message list view using the provided function.
     * <br>
     * Example :
     * <pre>{@literal
     * MessagesConfiguration messagesConfiguration = new MessagesConfiguration();
     * messagesConfiguration.setMessageListView(new Function3<Context, User, Group, View>() { @Override
     *  public View apply(Context context, User user, Group group) {
     *     CometChatMessageList messageList = new CometChatMessageList(context);
     *     messageList.setUser(user);
     *     return messageList;
     * }
     * });
     * }
     * </pre>
     *
     * @param messageListView A function that takes a Context, User, and Group as parameters and returns a View.
     *                        This function is responsible for creating and returning the custom message list view.
     *                        If null is passed, the existing message list view will not be modified.
     */
    public MessagesConfiguration setMessageListView(Function3<Context, User, Group, View> messageListView) {
        this.messageListView = messageListView;
        return this;
    }

    /**
     * Sets whether sound is disabled for messages in the Messages component.
     *
     * @param disableSoundForMessages True to disable sound for messages, false otherwise.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setDisableSoundForMessages(boolean disableSoundForMessages) {
        this.disableSoundForMessages = disableSoundForMessages;
        return this;
    }

    /**
     * Sets the custom sound resource for incoming messages in the Messages component.
     *
     * @param customSoundForIncomingMessages The resource ID of the custom sound for incoming messages.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setCustomSoundForIncomingMessages(int customSoundForIncomingMessages) {
        this.customSoundForIncomingMessages = customSoundForIncomingMessages;
        return this;
    }

    /**
     * Sets the custom sound resource for outgoing messages in the Messages component.
     *
     * @param customSoundForOutgoingMessages The resource ID of the custom sound for outgoing messages.
     * @return The MessagesConfiguration instance.
     */
    public MessagesConfiguration setCustomSoundForOutgoingMessages(int customSoundForOutgoingMessages) {
        this.customSoundForOutgoingMessages = customSoundForOutgoingMessages;
        return this;
    }

    public boolean isHideDetails() {
        return hideDetails;
    }

    public MessageListConfiguration getMessageListConfiguration() {
        return messageListConfiguration;
    }

    public MessagesStyle getStyle() {
        return style;
    }

    public Function3<Context, User, Group, View> getMessageHeaderView() {
        return messageHeaderView;
    }

    public MessageHeaderConfiguration getMessageHeaderConfiguration() {
        return messageHeaderConfiguration;
    }

    public MessageComposerConfiguration getMessageComposerConfiguration() {
        return messageComposerConfiguration;
    }

    public Function3<Context, User, Group, View> getMessageComposerView() {
        return messageComposerView;
    }

    public boolean isDisableTyping() {
        return disableTyping;
    }

    public boolean isHideMessageComposer() {
        return hideMessageComposer;
    }

    public boolean isHideMessageHeader() {
        return hideMessageHeader;
    }

    public Function3<Context, User, Group, View> getMessageListView() {
        return messageListView;
    }

    public boolean isDisableSoundForMessages() {
        return disableSoundForMessages;
    }

    public int getCustomSoundForIncomingMessages() {
        return customSoundForIncomingMessages;
    }

    public int getCustomSoundForOutgoingMessages() {
        return customSoundForOutgoingMessages;
    }

    public ThreadedMessagesConfiguration getThreadedMessagesConfiguration() {
        return threadedMessagesConfiguration;
    }

    public DetailsConfiguration getDetailsConfiguration() {
        return detailsConfiguration;
    }

    public Function3<Context, User, Group, View> getAuxiliaryOption() {
        return auxiliaryOption;
    }
}
