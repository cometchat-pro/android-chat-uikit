package com.cometchatworkspace.components.messages.template;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;

public class TemplateUtils {
    private static HashMap<String,CometChatMessageTemplate> defaultList = new HashMap<>();
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
        defaultList.put(CometChatMessageTemplate.DefaultList.audio,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.audio)
                .setOptions(audioTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.file,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.file)
                .setOptions(fileTemplateOptions(context)));
        defaultList.put(CometChatMessageTemplate.DefaultList.location,
                new CometChatMessageTemplate()
                .setId(CometChatMessageTemplate.DefaultList.location)
                .setOptions(locationTemplateOptions(context)));
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

    public static List<ActionItem> textTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.translate_message),R.drawable.ic_translate));
        options.add(new ActionItem(context.getString(R.string.edit_message),R.drawable.ic_edit));
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.copy_message),R.drawable.ic_copy_paste));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.reply_message_privately),R.drawable.ic_reply_message_in_private));
        options.add(new ActionItem(context.getString(R.string.send_message_privately),R.drawable.ic_send_message_in_private));
        options.add(new ActionItem(context.getString(R.string.forward_message),R.drawable.ic_forward));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> imageTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.reply_message_privately),R.drawable.ic_reply_message_in_private));
        options.add(new ActionItem(context.getString(R.string.send_message_privately),R.drawable.ic_send_message_in_private));
        options.add(new ActionItem(context.getString(R.string.forward_message),R.drawable.ic_forward));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> videoTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.reply_message_privately),R.drawable.ic_reply_message_in_private));
        options.add(new ActionItem(context.getString(R.string.send_message_privately),R.drawable.ic_send_message_in_private));
        options.add(new ActionItem(context.getString(R.string.forward_message),R.drawable.ic_forward));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> audioTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.reply_message_privately),R.drawable.ic_reply_message_in_private));
        options.add(new ActionItem(context.getString(R.string.send_message_privately),R.drawable.ic_send_message_in_private));
        options.add(new ActionItem(context.getString(R.string.forward_message),R.drawable.ic_forward));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> fileTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.reply_message_privately),R.drawable.ic_reply_message_in_private));
        options.add(new ActionItem(context.getString(R.string.send_message_privately),R.drawable.ic_send_message_in_private));
        options.add(new ActionItem(context.getString(R.string.forward_message),R.drawable.ic_forward));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> whiteboardTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> documentTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> customTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        return options;
    }

    public static List<ActionItem> pollTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> stickerTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<ActionItem> locationTemplateOptions(Context context) {
        List<ActionItem> options = new ArrayList<>();
        options.add(new ActionItem(context.getString(R.string.delete_message),R.drawable.ic_delete));
        options.add(new ActionItem(context.getString(R.string.reply_message),R.drawable.ic_reply_message));
        options.add(new ActionItem(context.getString(R.string.reply_in_thread),R.drawable.ic_threaded_message));
        options.add(new ActionItem(context.getString(R.string.message_information),R.drawable.ic_info));
        return options;
    }

    public static List<CometChatMessageTemplate> update(Context context,CometChatMessageTemplate template) {
        getDefaultList(context);
        defaultList.put(template.getId(),template);
        return new ArrayList<>(defaultList.values());
    }
}
