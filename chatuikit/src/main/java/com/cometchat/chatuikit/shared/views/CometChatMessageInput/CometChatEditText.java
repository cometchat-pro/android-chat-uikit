package com.cometchat.chatuikit.shared.views.CometChatMessageInput;

import android.content.ContentResolver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;

public class CometChatEditText extends AppCompatEditText {

    private static final String TAG = "CometChatEditText";

    public OnEditTextMediaListener onEditTextMediaListener;
    public Context context;

    public CometChatEditText(Context context) {
        super(context);
        this.context = context;
    }

    public CometChatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CometChatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        final InputConnection ic = super.onCreateInputConnection(outAttrs);
        EditorInfoCompat.setContentMimeTypes(outAttrs, new String[]{"image/png", "image/gif"});


        final InputConnectionCompat.OnCommitContentListener callback = (inputContentInfo, flags, opts) -> {
            // read and display inputContentInfo asynchronously
            if (BuildCompat.isAtLeastNMR1() && (flags & InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                try {
                    inputContentInfo.requestPermission();
                } catch (Exception e) {
                    return false; // return false if failed
                }
            }
            ContentResolver cr = getContext().getContentResolver();
            try {
                String mimeType = cr.getType(inputContentInfo.getLinkUri());

                onEditTextMediaListener.OnMediaSelected(inputContentInfo);
            } catch (Exception e) {
                Toast.makeText(context, "Input Type not supported", Toast.LENGTH_SHORT).show();
            }

            // read and display inputContentInfo asynchronously.
            // call inputContentInfo.releasePermission() as needed.

            return true;  // return true if succeeded
        };
        return InputConnectionCompat.createWrapper(ic, outAttrs, callback);
    }

    public void setMediaSelected(OnEditTextMediaListener onEditTextMediaListener) {
        this.onEditTextMediaListener = onEditTextMediaListener;
    }

    public interface OnEditTextMediaListener {
        void OnMediaSelected(InputContentInfoCompat i);
    }
}