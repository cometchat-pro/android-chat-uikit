package com.cometchat.chatuikit.shared.resources.utils;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.Group;
import com.cometchat.chatuikit.R;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.helpers.Logger;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;

import kotlin.ranges.RangesKt;

public class Utils {

    private static final String TAG = "Utils";

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static boolean isDarkMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public static boolean isClass(String className) {
        try  {
            Class.forName(className);
            return true;
        }  catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String getMessagePrefix(BaseMessage lastMessage, Context context) {
        String message = "";
        if (lastMessage.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.GROUP)) {
            if (!isLoggedInUser(lastMessage.getSender())) {
                message = lastMessage.getSender().getName() + ": ";
            } else {
                message = context.getString(R.string.you) + ": ";
            }
        }
        return message;

    }

    public static boolean isLoggedInUser(User user) {
        return user.getUid().equals(CometChatUIKit.getLoggedInUser().getUid());
    }

    public static void handleView(ViewGroup layout, View view, boolean hideIfNull) {
        if (view != null) {
            layout.removeAllViews();
            removeParentFromView(view);
            layout.setVisibility(VISIBLE);
            layout.addView(view);
        } else {
            if (hideIfNull)
                layout.setVisibility(GONE);
        }
    }

    public static void removeParentFromView(View view) {
        if (view!=null && view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
    }

    public static Action getGroupActionMessage(AppEntity actionOn, Group actionFor, Group receiver, String uid){
        Action action = new Action();
        action.setActionBy(CometChatUIKit.getLoggedInUser());
        action.setActionOn(actionOn);
        action.setActionFor(actionFor);
        action.setReceiverType(CometChatConstants.RECEIVER_TYPE_GROUP);
        action.setReceiver(receiver);
        action.setCategory(CometChatConstants.CATEGORY_ACTION);
        action.setType(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER);
        action.setReceiverUid(uid);
        action.setSender(CometChatUIKit.getLoggedInUser());
        action.setSentAt(System.currentTimeMillis() / 1000);
        action.setConversationId("group_"+actionFor.getGuid());
        return action;
    }

    public static final float softTransition(float $this$softTransition, float compareWith, float allowedDiff, float scaleFactor) {
        if (scaleFactor == 0.0F) {
            return $this$softTransition;
        } else {
            float result = $this$softTransition;
            float diff;
            if (compareWith > $this$softTransition) {
                if (compareWith / $this$softTransition > allowedDiff) {
                    diff = RangesKt.coerceAtLeast($this$softTransition, compareWith) - RangesKt.coerceAtMost($this$softTransition, compareWith);
                    result = $this$softTransition + diff / scaleFactor;
                }
            } else if ($this$softTransition > compareWith && $this$softTransition / compareWith > allowedDiff) {
                diff = RangesKt.coerceAtLeast($this$softTransition, compareWith) - RangesKt.coerceAtMost($this$softTransition, compareWith);
                result = $this$softTransition - diff / scaleFactor;
            }

            return result;
        }
    }

    public static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static float dpToPixel(float dp, Resources resources) {
        float density = resources.getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }


    public static String convertTimeStampToDurationTime(long var0) {
        long var2 = var0 / 1000L;
        long var4 = var2 / 60L % 60L;
        long var6 = var2 / 60L / 60L % 24L;
        return var6 == 0L ? String.format(Locale.getDefault(), "%02d:%02d", var4, var2 % 60L) : String.format(Locale.getDefault(), "%02d:%02d:%02d", var6, var4, var2 % 60L);
    }

    public static String getDateId(long var0) {
        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(var0);
        return DateFormat.format("ddMMyyyy", var2).toString();
    }

    public static String getCallDate(long var0) {
        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(var0);
        return DateFormat.format("dd MMM yy", var2).toString();
    }

    public static String getDate(Context context, long var0) {
        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(var0 * 1000L);

        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = currentTimeStamp - (var0 * 1000);
        if (diffTimeStamp < 24 * 60 * 60 * 1000) {
            return context.getString(R.string.today);

        } else if (diffTimeStamp < 48 * 60 * 60 * 1000) {

            return context.getString(R.string.yesterday);
        } else return DateFormat.format("dd MMMM yyyy", var2).toString();
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

    /**
     * This method is used to convert user to group member. This method is used when we tries to add
     * user in a group or update group member scope.
     *
     * @param user          is object of User
     * @param isScopeUpdate is boolean which help us to check if scope is updated or not.
     * @param newScope      is a String which contains newScope. If it is empty then user is added as participant.
     * @return GroupMember
     * @see User
     * @see GroupMember
     */
    public static GroupMember UserToGroupMember(User user, boolean isScopeUpdate, String newScope) {
        GroupMember groupMember;
        if (isScopeUpdate) groupMember = new GroupMember(user.getUid(), newScope);
        else groupMember = new GroupMember(user.getUid(), CometChatConstants.SCOPE_PARTICIPANT);

        groupMember.setAvatar(user.getAvatar());
        groupMember.setName(user.getName());
        groupMember.setStatus(user.getStatus());
        return groupMember;
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

    public static String getLastMessageDate(Context context, long timestamp) {
        String lastMessageTime = new SimpleDateFormat("h:mm a").format(new Date(timestamp * 1000));
        String lastMessageDate = new SimpleDateFormat("dd MMM yyyy").format(new Date(timestamp * 1000));
        String lastMessageWeek = new SimpleDateFormat("EEE").format(new Date(timestamp * 1000));
        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = currentTimeStamp - timestamp * 1000;

        if (diffTimeStamp < 24 * 60 * 60 * 1000) {
            return lastMessageTime;

        } else if (diffTimeStamp < 48 * 60 * 60 * 1000) {

            return context.getString(R.string.yesterday);
        } else if (diffTimeStamp < 7 * 24 * 60 * 60 * 1000) {
            return lastMessageWeek;
        } else {
            return lastMessageDate;
        }

    }


    /**
     * This method is used to create group when called from layout. It uses <code>Random.nextInt()</code>
     * to generate random number to use with group id and group icon. Any Random number between 10 to
     * 1000 are choosen.
     */

    public static String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 0-62 (exclusive), random returns 0-61
            SecureRandom random = new SecureRandom();
            String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
            String CHAR_UPPER = CHAR_LOWER.toUpperCase();
            String NUMBER = "0123456789";
            String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            // debug
            System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);
            sb.append(rndChar);
        }
        return sb.toString();
    }

    public static String getReceiptDate(Context context, long timestamp) {
        String lastMessageTime = new SimpleDateFormat("h:mm a").format(new Date(timestamp * 1000));
        String lastMessageDate = new SimpleDateFormat("dd MMMM h:mm a").format(new Date(timestamp * 1000));
        String lastMessageWeek = new SimpleDateFormat("EEE h:mm a").format(new Date(timestamp * 1000));
        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = currentTimeStamp - timestamp * 1000;

        if (diffTimeStamp < 24 * 60 * 60 * 1000) {
            return lastMessageTime;

        } else if (diffTimeStamp < 48 * 60 * 60 * 1000) {

            return context.getString(R.string.yesterday);
        } else if (diffTimeStamp < 7 * 24 * 60 * 60 * 1000) {
            return lastMessageWeek;
        } else {
            return lastMessageDate;
        }

    }

    public static Boolean checkDirExistence(Context context, String type) {

        File audioDir = new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getResources().getString(R.string.app_name) + "/" + type + "/");

        return audioDir.isDirectory();

    }

    public static void makeDirectory(Context context, String type) {

        String audioDir = Environment.getExternalStorageDirectory().toString() + "/" + context.getResources().getString(R.string.app_name) + "/" + type + "/";

        createDirectory(audioDir);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                Logger.error(TAG, " hasPermissions() : Permission : " + permission + "checkSelfPermission : " + ActivityCompat.checkSelfPermission(context, permission));
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
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

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Files.FileColumns.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
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

                String[] contentUriPrefixesToTry = new String[]{"content://downloads/public_downloads", "content://downloads/my_downloads"};

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
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
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


    public static String getFileName(String mediaFile) {
        String[] t1 = mediaFile.substring(mediaFile.lastIndexOf("/")).split("_");
        return t1[2];
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
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
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

    public static String getOutputMediaFile(Context context) {
        File var0 = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.app_name));
        String dir;
        if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
            dir = Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name) + "/" + "audio/";
        } else {
            if (Environment.isExternalStorageManager()) {
                dir = Environment.getExternalStorageState() + "/" + context.getResources().getString(R.string.app_name) + "/" + "audio/";
            } else {
                dir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "/" + context.getResources().getString(R.string.app_name) + "/" + "audio/";
            }
        }
        createDirectory(dir);
        return dir + (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()) + ".mp3";

    }

    public static void createDirectory(String var0) {
        if (!(new File(var0)).exists()) {
            (new File(var0)).mkdirs();
        }

    }

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * 0.6f);
        int height = Math.round(image.getHeight() * 0.6f);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        intrinsicBlur.setRadius(15f);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public static float dpToPx(Context context, float valueInDp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = valueInDp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideKeyBoard(Context context, View mainLayout) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
    }

    public static void showKeyBoard(Context context, View mainLayout) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public static JSONObject placeErrorObjectInMetaData(CometChatException exception) {
        JSONObject metaData = new JSONObject();
        try {
            metaData.put("error", exception.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return metaData;
    }

    public static Call getDirectCallData(BaseMessage baseMessage) {
        Call call = null;
        String callType = CometChatConstants.CALL_TYPE_VIDEO;
        try {
            if (((CustomMessage) baseMessage).getCustomData() != null) {
                JSONObject customObject = ((CustomMessage) baseMessage).getCustomData();
                String receiverID = baseMessage.getReceiverUid();
                String receiverType = baseMessage.getReceiverType();
                if (customObject.has("callType")) {
                    callType = customObject.getString("callType");
                }
                call = new Call(receiverID, receiverType, callType);
                if (customObject.has("sessionID")) {
                    String sessionID = customObject.getString("sessionID");
                    call.setSessionId(sessionID);
                } else {
                    call.setSessionId(receiverID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return call;
    }

    public static void requestPermissions(Context context,String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }

    public static int convertDpToPx(Context context, int dp) {
        int px = Math.round(dp * context.getResources().getDisplayMetrics().density);
        return px;
    }

    public static void setStatusBarColor(Context context, @ColorInt int color) {
        if (color != 0) ((Activity) context).getWindow().setStatusBarColor(color);

        if (!isDarkMode(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

    }

}
