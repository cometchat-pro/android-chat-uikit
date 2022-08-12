package com.cometchatworkspace.components.messages.template;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.CometChatOptions;
import com.cometchatworkspace.resources.constants.UIKitConstants;

public class TemplateUtils {
    private static final HashMap<String,CometChatMessageTemplate> defaultList = new HashMap<>();

    public static List<CometChatMessageTemplate> remove(Context context,String id) {
        getDefaultList(context);
        defaultList.remove(id);
        return new ArrayList<>(defaultList.values());
    }

    public static List<CometChatMessageTemplate> getDefaultList(Context context) {

        defaultList.put(CometChatMessageTemplate.DefaultList.text,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.text)
                .setOptions(textTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.image,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.image)
                .setOptions(imageTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.video,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.video)
                .setOptions(videoTemplateOptions(context)));
//        defaultList.put(CometChatMessageTemplate.DefaultList.audio,
//                new CometChatMessageTemplate()
//                .setId(CometChatMessageTemplate.DefaultList.audio)
//                .setOptions(audioTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.file,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.file)
                .setOptions(fileTemplateOptions(context)));
//        defaultList.put(CometChatMessageTemplate.DefaultList.location,
//                new CometChatMessageTemplate()
//                .setId(CometChatMessageTemplate.DefaultList.location)
//                .setOptions(locationTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.whiteboard,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.whiteboard)
                .setOptions(whiteboardTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.document,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.document)
                .setOptions(documentTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.sticker,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.sticker)
                .setOptions(stickerTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.poll,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.poll)
                .setOptions(pollTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.groupAction,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.groupAction));
        defaultList.put(CometChatMessageTemplate.DefaultList.groupCall,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.groupCall));
        defaultList.put(CometChatMessageTemplate.DefaultList.callAction,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.callAction));
        return new ArrayList<>(defaultList.values());
    }

    public static HashMap<String,CometChatOptions> textTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.TRANSLATE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.TRANSLATE,
                context.getString(R.string.translate_message),
                R.drawable.ic_translate));
        options.put(UIKitConstants.DefaultOptions.EDIT,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.EDIT,
                context.getString(R.string.edit_message),
                R.drawable.ic_edit));
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
        options.put(UIKitConstants.DefaultOptions.COPY,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.COPY,
                context.getString(R.string.copy_message),
                R.drawable.ic_copy_paste));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                context.getString(R.string.reply_message_privately),
//                R.drawable.ic_reply_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                context.getString(R.string.send_message_privately),
//                R.drawable.ic_send_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.FORWARD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.FORWARD,
//                context.getString(R.string.forward_message),
//                R.drawable.ic_forward));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> imageTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                context.getString(R.string.reply_message_privately),
//                R.drawable.ic_reply_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                context.getString(R.string.send_message_privately),
//                R.drawable.ic_send_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.FORWARD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.FORWARD,
//                context.getString(R.string.forward_message),
//                R.drawable.ic_forward));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> videoTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                context.getString(R.string.reply_message_privately),
//                R.drawable.ic_reply_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                context.getString(R.string.send_message_privately),
//                R.drawable.ic_send_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.FORWARD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.FORWARD,
//                context.getString(R.string.forward_message),
//                R.drawable.ic_forward));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> audioTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                context.getString(R.string.reply_message_privately),
//                R.drawable.ic_reply_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                context.getString(R.string.send_message_privately),
//                R.drawable.ic_send_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.FORWARD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.FORWARD,
//                context.getString(R.string.forward_message),
//                R.drawable.ic_forward));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> fileTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_PRIVATELY,
//                context.getString(R.string.reply_message_privately),
//                R.drawable.ic_reply_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_PRIVATELY,
//                context.getString(R.string.send_message_privately),
//                R.drawable.ic_send_message_in_private));
//        options.put(UIKitConstants.DefaultOptions.FORWARD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.FORWARD,
//                context.getString(R.string.forward_message),
//                R.drawable.ic_forward));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> whiteboardTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> documentTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> customTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
        return options;
    }

    public static HashMap<String, CometChatOptions> pollTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> stickerTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static HashMap<String,CometChatOptions> locationTemplateOptions(Context context) {
        HashMap<String,CometChatOptions> options = new HashMap<>();
        options.put(UIKitConstants.DefaultOptions.DELETE,
                new CometChatOptions(
                UIKitConstants.DefaultOptions.DELETE,
                context.getString(R.string.delete_message),
                R.drawable.ic_delete));
//        options.put(UIKitConstants.DefaultOptions.REPLY,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY,
//                context.getString(R.string.reply_message),
//                R.drawable.ic_reply_message));
//        options.put(UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.REPLY_IN_THREAD,
//                context.getString(R.string.reply_in_thread),
//                R.drawable.ic_threaded_message));
//        options.put(UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                new CometChatOptions(
//                UIKitConstants.DefaultOptions.MESSAGE_INFORMATION,
//                context.getString(R.string.message_information),
//                R.drawable.ic_info));
        return options;
    }

    public static List<CometChatMessageTemplate> update(Context context,CometChatMessageTemplate template) {
        getDefaultList(context);
        defaultList.put(template.getId(),template);
        return new ArrayList<>(defaultList.values());
    }
}
