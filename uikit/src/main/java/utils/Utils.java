package utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.helpers.Logger;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class Utils {

    private static final String TAG = "Utils";

    public static float dpToPixel(float dp, Resources resources) {
        float density = resources.getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }


    public static String getDateId(long var0) {
        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(var0);
        return DateFormat.format("ddMMyyyy", var2).toString();
    }

    public static String getDate(long var0) {
        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(var0);
        return DateFormat.format("dd/MM/yyyy", var2).toString();
    }

    public static List<User> userSort(List<User> userList) {
        Collections.sort(userList, (user, user1) -> user.getName().toLowerCase().compareTo(user1.getName().toLowerCase()));
        return userList;
    }

    public static TextView changeToolbarFont(MaterialToolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                return (TextView) view;
            }
        }
        return null;
    }

    public static String getFileSize(int fileSize) {
        if (fileSize > 1024) {
            if (fileSize > (1024 * 1024)) {
                return fileSize / (1024 * 1024) + " MB";
            } else {
                return fileSize / 1024 + " KB";
            }
        } else {
            return fileSize + " B";
        }
    }

    public static String getLastMessage(BaseMessage lastMessage) {

        String message = null;

        switch (lastMessage.getCategory()) {

            case CometChatConstants.CATEGORY_MESSAGE:

                if (lastMessage instanceof TextMessage) {

                    if (isLoggedInUser(lastMessage.getSender()))
                        message = "You: " + ((TextMessage) lastMessage).getText();
                    else
                        message = lastMessage.getSender().getName() + ": " + ((TextMessage) lastMessage).getText();

                } else if (lastMessage instanceof MediaMessage) {

                    if (isLoggedInUser(lastMessage.getSender()))
                        message = "You sent a " + lastMessage.getType();
                    else
                        message = "You received a " + lastMessage.getType();
                }

                break;

            case CometChatConstants.CATEGORY_CUSTOM:


                if (isLoggedInUser(lastMessage.getSender()))
                    message = "You sent a " + lastMessage.getType();
                else
                    message = "You received a " + lastMessage.getType();

                break;
            case CometChatConstants.CATEGORY_ACTION:

                if (isLoggedInUser(lastMessage.getSender())) {
                    if (((Action) lastMessage).getActionOn()!=null)
                        message = "You " + ((Action) lastMessage).getAction() + " " + ((User) ((Action) lastMessage).getActionOn()).getName();
                    else
                        message = ((Action) lastMessage).getMessage();
                } else {
                    message = ((Action) lastMessage).getMessage();
                }

                break;

            case CometChatConstants.CATEGORY_CALL:
                message = "Call Message";
                break;
            default:
                message = "Tap to start conversation";
        }
        return message;
    }

    public static boolean isLoggedInUser(User user) {
        return user.getUid().equals(CometChat.getLoggedInUser().getUid());
    }

    public static GroupMember UserToGroupMember(User user, boolean isScopeUpdate, String newScope) {
        GroupMember groupMember;
        if (isScopeUpdate)
            groupMember = new GroupMember(user.getUid(), newScope);
        else
            groupMember = new GroupMember(user.getUid(), CometChatConstants.SCOPE_PARTICIPANT);

        groupMember.setAvatar(user.getAvatar());
        groupMember.setName(user.getName());
        groupMember.setStatus(user.getStatus());
        return groupMember;
    }

    @BindingAdapter(value = {"app:deliveredAt"})
    public static String getHeaderDate(TextView textView, long timestamp) {
        Calendar messageTimestamp = Calendar.getInstance();
        messageTimestamp.setTimeInMillis(timestamp);
        Calendar now = Calendar.getInstance();
//        if (now.get(5) == messageTimestamp.get(5)) {
        return DateFormat.format("hh:mm a", messageTimestamp).toString();
//        } else {
//            return now.get(5) - messageTimestamp.get(5) == 1 ? "Yesterday " + DateFormat.format("hh:mm a", messageTimestamp).toString() : DateFormat.format("d MMMM", messageTimestamp).toString() + " " + DateFormat.format("hh:mm a", messageTimestamp).toString();
//        }
    }

    public static String getHeaderDate(long timestamp) {
        Calendar messageTimestamp = Calendar.getInstance();
        messageTimestamp.setTimeInMillis(timestamp);
        Calendar now = Calendar.getInstance();
//        if (now.get(5) == messageTimestamp.get(5)) {
        return DateFormat.format("hh:mm a", messageTimestamp).toString();
//        } else {
//            return now.get(5) - messageTimestamp.get(5) == 1 ? "Yesterday " + DateFormat.format("hh:mm a", messageTimestamp).toString() : DateFormat.format("d MMMM", messageTimestamp).toString() + " " + DateFormat.format("hh:mm a", messageTimestamp).toString();
//        }
    }

    public static String getLastMessageDate(long timestamp) {
        String lastMessageTime = new SimpleDateFormat("h:mm a").format(new java.util.Date(timestamp * 1000));
        String lastMessageDate = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(timestamp * 1000));
        String lastMessageWeek = new SimpleDateFormat("EEE").format(new java.util.Date(timestamp * 1000));
        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = currentTimeStamp - timestamp * 1000;

        Log.e(TAG, "getLastMessageDate: " + 24 * 60 * 60 * 1000);
        if (diffTimeStamp < 24 * 60 * 60 * 1000) {
            return lastMessageTime;

        } else if (diffTimeStamp < 48 * 60 * 60 * 1000) {

            return "Yesterday";
        } else if (diffTimeStamp < 7 * 24 * 60 * 60 * 1000) {
            return lastMessageWeek;
        } else {
            return lastMessageDate;
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
                && permissions != null) {
            for (String permission : permissions) {
                Logger.error(TAG, " hasPermissions() : Permission : " + permission
                        + "checkSelfPermission : " + ActivityCompat.checkSelfPermission(context, permission));
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Files.FileColumns.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getImagePathFromUri(Context context, @Nullable Uri aUri) {
        String imagePath = null;
        if (aUri == null) {
            return imagePath;
        }
        if (DocumentsContract.isDocumentUri(context, aUri)) {
            String documentId = DocumentsContract.getDocumentId(aUri);
            if ("com.android.providers.media.documents".equals(aUri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(aUri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                    }
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(context, aUri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, aUri, destinationPath);
                }
                imagePath = destinationPath;
            } else if ("com.android.providers.downloads.documents".equals(aUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(documentId));
                imagePath = getImagePath(contentUri, null, context);
            }
        } else if ("content".equalsIgnoreCase(aUri.getScheme())) {
            imagePath = getImagePath(aUri, null, context);
        } else if ("file".equalsIgnoreCase(aUri.getScheme())) {
            imagePath = aUri.getPath();
        }
        return imagePath;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }

        return file;
    }


    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), "documents");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    public static String getPath(final Context context, final Uri uri) {
        String absolutePath = getImagePathFromUri(context, uri);
        return absolutePath != null ? absolutePath : uri.toString();
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }


    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    private static String getImagePath(Uri aUri, String aSelection, Context context) {
        try {
            String path = null;
            Cursor cursor = context.getContentResolver().query(aUri, null, aSelection, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    public static List<String> checkSmartReply(BaseMessage lastMessage) {
        if (lastMessage!=null && !lastMessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
            if (lastMessage.getMetadata()!=null) {
                return getSmartReplyList(lastMessage);
            }
        }
        return null;
    }

    private static List<String> getSmartReplyList(BaseMessage baseMessage){

        HashMap<String, JSONObject> extensionList = Utils.extensionCheck(baseMessage);
        if (extensionList != null && extensionList.containsKey("smartReply")) {
            JSONObject replyObject = extensionList.get("smartReply");
            List<String> replyList = new ArrayList<>();
            try {
                replyList.add(replyObject.getString("reply_positive"));
                replyList.add(replyObject.getString("reply_neutral"));
                replyList.add(replyObject.getString("reply_negative"));
            } catch (Exception e) {
                Log.e(TAG, "onSuccess: " + e.getMessage());
            }
            return replyList;
        }
        return null;
    }


    public static HashMap<String,JSONObject> extensionCheck(BaseMessage baseMessage)
    {
        JSONObject metadata = baseMessage.getMetadata();
        HashMap<String,JSONObject> extensionMap = new HashMap<>();
        try {
            if (metadata != null) {
                JSONObject injectedObject = metadata.getJSONObject("@injected");
                if (injectedObject != null && injectedObject.has("extensions")) {
                    JSONObject extensionsObject = injectedObject.getJSONObject("extensions");
                    if (extensionsObject != null && extensionsObject.has("link-preview")) {
                        JSONObject linkPreviewObject = extensionsObject.getJSONObject("link-preview");
                        JSONArray linkPreview = linkPreviewObject.getJSONArray("links");
                        if (linkPreview.length() > 0) {
                            extensionMap.put("linkPreview",linkPreview.getJSONObject(0));
                        }

                    }
                    if (extensionsObject !=null && extensionsObject.has("smart-reply")) {
                        extensionMap.put("smartReply",extensionsObject.getJSONObject("smart-reply"));
                    }
                }
                return extensionMap;
            }
            else
                return null;
        }  catch (Exception e) {
            Log.e(TAG, "isLinkPreview: "+e.getMessage() );
        }
        return null;
    }
}
