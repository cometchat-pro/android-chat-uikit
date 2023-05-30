package com.cometchat.chatuikit.shared.cometchatuikit;

import static com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKitHelper.onMessageSent;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.calls.CallingExtension;
import com.cometchat.chatuikit.calls.callmanger.CallManager;
import com.cometchat.chatuikit.extensions.DefaultExtensions;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import java.util.List;

/**
 * The CometChatUIKit class is a utility class that provides various methods for initializing and interacting with the CometChat SDK.
 * It includes methods for initialization, authentication, login, logout, user management, and sending messages.
 * This class also handles the permission requests and VoIP settings required for making audio/video calls.
 */
public class CometChatUIKit {
    private static final String TAG = CometChatUIKit.class.getName();
    private static UIKitSettings authenticationSettings;
    private static ActivityResultLauncher<String> requestPermissionLauncher;
    private static final String callingClassName = "com.cometchat.pro.rtc.core.CometChatCalls";
    private static boolean emittedSuccess = false;
    private static boolean popUpDisplayed = false;
    private static int counter = 0;

    /**
     * Initializes the CometChat SDK with the provided authentication settings.
     *
     * @param context          The context of the calling activity or application.
     * @param authSettings     The UIKitSettings object containing the authentication settings.
     * @param callbackListener The callback listener to handle the initialization success or failure.
     */
    public static void init(Context context, UIKitSettings authSettings, CometChat.CallbackListener<String> callbackListener) {
        authenticationSettings = authSettings;
        if (!checkAuthSettings(callbackListener)) {
            return;
        }
        AppSettings.AppSettingsBuilder appSettingsBuilder = new AppSettings.AppSettingsBuilder();
        if (authenticationSettings.getRoles() != null && !authenticationSettings.getRoles().isEmpty()) {
            appSettingsBuilder.subscribePresenceForRoles(authenticationSettings.getRoles());
        } else if (authenticationSettings.getSubscriptionType().equals("ALL_USERS")) {
            appSettingsBuilder.subscribePresenceForAllUsers();
        } else if (authenticationSettings.getSubscriptionType().equals("FRIENDS")) {
            appSettingsBuilder.subscribePresenceForFriends();
        }
        appSettingsBuilder.autoEstablishSocketConnection(authenticationSettings.isAutoEstablishSocketConnection());
        appSettingsBuilder.setRegion(authenticationSettings.getRegion());
        AppSettings appSettings = appSettingsBuilder.build();
        CometChat.init(context, authenticationSettings.getAppId(), appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (callbackListener != null) {
                    emittedSuccess = false;
                    popUpDisplayed = false;
                    counter = 0;
                    initiateAfterLogin(context);
                    if (Utils.isClass(callingClassName)) {
                        requestPermissionLauncher = ((FragmentActivity) context).registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                            if (!isGranted) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.MANAGE_OWN_CALLS) != PackageManager.PERMISSION_GRANTED) {
                                    showWarning(context, context.getResources().getString(R.string.grant_phone_state_permission), s, callbackListener);
                                }
                                if (counter >= 1) {
                                    Log.e(TAG, "Permission error: please provide calling related permission in order to make calling work ");
                                    emitOnSuccess(callbackListener, s);
                                }
                                counter++;
                            } else {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.MANAGE_OWN_CALLS) == PackageManager.PERMISSION_GRANTED) {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                        openVoipSetting(context, s, callbackListener);
                                    } else {
                                        requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE);
                                        Utils.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                                    }
                                }
                            }
                        });
                        requestCallsPermission();
                    } else emitOnSuccess(callbackListener, s);
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (callbackListener != null) callbackListener.onError(e);
            }
        });
    }

    /**
     * Opens the VoIP (Voice over IP) settings screen of the device.
     * This method is called when the app requires specific permissions for making VoIP calls.
     *
     * @param context          The context of the calling activity or application.
     * @param response         The response message to be passed to the callback listener.
     * @param callbackListener The callback listener to handle the success or failure of the operation.
     */
    private static void openVoipSetting(Context context, String response, CometChat.CallbackListener<String> callbackListener) {
        if (!emittedSuccess) {
            CallManager callManager = new CallManager(context);
            if (!callManager.checkAccountConnection(context)) {
                if (!popUpDisplayed) {
                    new CometChatDialog(context, 0, CometChatTheme.getInstance(context).getTypography().getText1(), CometChatTheme.getInstance(context).getTypography().getText3(), CometChatTheme.getInstance(context).getPalette().getAccent900(), 0, CometChatTheme.getInstance(context).getPalette().getAccent700(), "To make VoIP Calling work properly, you need to allow certain " + "permission from your call account settings for this app.", "VoIP Permission", "Open Settings", "Cancel", null, CometChatTheme.getInstance(context).getPalette().getPrimary(), CometChatTheme.getInstance(context).getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            popUpDisplayed = false;
                            alertDialog.dismiss();
                            callManager.launchVoIPSetting(context);
                            new Handler().postDelayed(() -> emitOnSuccess(callbackListener, response), 200);
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            popUpDisplayed = false;
                            emitOnSuccess(callbackListener, response);
                            alertDialog.dismiss();
                        }
                    }, 0, false);
                    popUpDisplayed = true;
                }
            } else {
                emitOnSuccess(callbackListener, response);
            }
        }
    }

    /**
     * Sends a success response to the callback listener if not already emitted.
     *
     * @param callbackListener The callback listener to be notified of the success response.
     * @param response         The success response message to be passed to the callback listener.
     */
    private static void emitOnSuccess(CometChat.CallbackListener<String> callbackListener, String response) {
        if (!emittedSuccess) {
            emittedSuccess = true;
            counter = 0;
            callbackListener.onSuccess(response);
        }
    }

    /**
     * Requests the necessary permissions for making audio/video calls.
     */
    private static void requestCallsPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ANSWER_PHONE_CALLS);
        requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        requestPermissionLauncher.launch(Manifest.permission.MANAGE_OWN_CALLS);
    }

    /**
     * Shows a warning dialog with the provided message and invokes the callback listener on positive button click.
     *
     * @param context          The context of the calling activity or application.
     * @param warning          The warning message to be displayed in the dialog.
     * @param response         The response message to be passed to the callback listener.
     * @param callbackListener The callback listener to handle the positive button click.
     */
    private static void showWarning(Context context, String warning, String response, CometChat.CallbackListener<String> callbackListener) {
        String errorMessage = warning;
        if (context != null) {
            new CometChatDialog(context, 0, CometChatTheme.getInstance(context).getTypography().getText1(), CometChatTheme.getInstance(context).getTypography().getText3(), CometChatTheme.getInstance(context).getPalette().getAccent900(), 0, CometChatTheme.getInstance(context).getPalette().getAccent700(), errorMessage, "", context.getString(R.string.okay), null, null, CometChatTheme.getInstance(context).getPalette().getPrimary(), CometChatTheme.getInstance(context).getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    callbackListener.onSuccess(response);
                    alertDialog.dismiss();
                }
            }, 0, false);
        }
    }

    /**
     * Checks if the authentication settings are valid and handles the error case.
     *
     * @param onError The callback listener to handle the error case.
     * @return True if the authentication settings are valid, False otherwise.
     */
    private static boolean checkAuthSettings(CometChat.CallbackListener onError) {
        if (authenticationSettings == null) {
            if (onError != null) {
                onError.onError(new CometChatException("ERR", "Authentication null", "Populate authSettings before initializing"));
            }
            return false;
        }

        if (authenticationSettings.getAppId() == null) {
            if (onError != null) {
                onError.onError(new CometChatException("appIdErr", "APP ID null", "Populate authSettings before initializing"));
            }
            return false;
        }
        return true;
    }


    /**
     * Logs in a user with the specified UID.
     *
     * @param uid              The UID of the user to be logged in.
     * @param callbackListener The callback listener to handle the login success or failure.
     */
    public static void login(String uid, CometChat.CallbackListener<User> callbackListener) {
        if (!checkAuthSettings(callbackListener)) return;
        if (CometChatUIKit.getLoggedInUser() == null || !CometChatUIKit.getLoggedInUser().getUid().equals(uid)) {
            CometChat.login(uid, authenticationSettings.getAuthKey(), new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if (callbackListener != null) {
                        callbackListener.onSuccess(user);
                    }
                }

                @Override
                public void onError(CometChatException excep) {
                    if (callbackListener != null) {
                        callbackListener.onError(excep);
                    }
                }
            });
        } else {
            if (callbackListener != null)
                callbackListener.onSuccess(CometChatUIKit.getLoggedInUser());
        }
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The User object representing the logged-in user, or null if no user is logged in.
     */
    public static User getLoggedInUser() {
        return CometChat.getLoggedInUser();
    }

    /**
     * Logs in a user with the provided authentication token.
     *
     * @param authToken        The authentication token for the user.
     * @param callbackListener The callback listener to handle the login result.
     *                         Must implement the {@link CometChat.CallbackListener} interface.
     */
    public static void loginWithAuthToken(String authToken, CometChat.CallbackListener<User> callbackListener) {
        if (!checkAuthSettings(callbackListener)) return;
        if (CometChatUIKit.getLoggedInUser() == null) {
            CometChat.login(authToken, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if (callbackListener != null) callbackListener.onSuccess(user);
                }

                @Override
                public void onError(CometChatException e) {
                    if (callbackListener != null) callbackListener.onError(e);
                }
            });
        } else {
            if (callbackListener != null)
                callbackListener.onSuccess(CometChatUIKit.getLoggedInUser());
        }
    }

    /**
     * Logs out the currently logged-in user.
     *
     * @param callbackListener The callback listener to handle the logout result.
     *                         Must implement the {@link CometChat.CallbackListener} interface.
     */
    public static void logout(CometChat.CallbackListener<String> callbackListener) {
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                if (callbackListener != null) callbackListener.onSuccess(successMessage);
            }

            @Override
            public void onError(CometChatException e) {
                if (callbackListener != null) callbackListener.onError(e);
            }
        });
    }

    /**
     * Creates a new user in the CometChat platform.
     *
     * @param user             The user object containing the details of the user to be created.
     * @param callbackListener The callback listener to handle the create user result.
     *                         Must implement the {@link CometChat.CallbackListener} interface.
     * @throws IllegalArgumentException If the provided user object is null.
     */
    public static void createUser(User user, CometChat.CallbackListener<User> callbackListener) {

        if (!checkAuthSettings(callbackListener)) return;

        CometChat.createUser(user, authenticationSettings.getAuthKey(), new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                if (callbackListener != null) callbackListener.onSuccess(user);
            }

            @Override
            public void onError(CometChatException e) {
                if (callbackListener != null) callbackListener.onError(e);
            }
        });
    }

    private static void initiateAfterLogin(Context context) {
        if (authenticationSettings != null) {
            List<ExtensionsDataSource> extensionList = authenticationSettings.getExtensions() != null ? authenticationSettings.getExtensions() : DefaultExtensions.get();
            if (Utils.isClass(callingClassName)) extensionList.add(new CallingExtension(context));
            if (!extensionList.isEmpty()) {
                for (ExtensionsDataSource element : extensionList) {
                    element.enable();
                }
            }
        }
    }

    /**
     * Sends a custom message.
     *
     * @param customMessage The custom message to be sent.
     */
    public static void sendCustomMessage(CustomMessage customMessage) {
        onMessageSent(customMessage, MessageStatus.IN_PROGRESS);
        CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
            @Override
            public void onSuccess(CustomMessage customMessage) {
                onMessageSent(customMessage, MessageStatus.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                customMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                CometChatUIKitHelper.onMessageSent(customMessage, MessageStatus.ERROR);
            }
        });
    }

    /**
     * Sends a text message.
     *
     * @param textMessage The text message to be sent.
     */
    public static void sendTextMessage(TextMessage textMessage) {
        onMessageSent(textMessage, MessageStatus.IN_PROGRESS);
        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                onMessageSent(textMessage, MessageStatus.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                textMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.ERROR);
            }
        });
    }

    /**
     * Sends a media message.
     *
     * @param mediaMessage The media message to be sent.
     */
    public static void sendMediaMessage(MediaMessage mediaMessage) {
        onMessageSent(mediaMessage, MessageStatus.IN_PROGRESS);
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                onMessageSent(mediaMessage, MessageStatus.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                mediaMessage.setMetadata(Utils.placeErrorObjectInMetaData(e));
                CometChatUIKitHelper.onMessageSent(mediaMessage, MessageStatus.ERROR);
            }
        });
    }

}
