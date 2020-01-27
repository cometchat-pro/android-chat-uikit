package screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.databinding.FragmentMoreInfoScreenBinding;
import com.cometchat.pro.uikit.Avatar;

import utils.FontUtils;

public class CometChatUserInfoScreen extends Fragment {

    private Avatar notificationIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentMoreInfoScreenBinding moreInfoScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_info_screen, container, false);
        moreInfoScreenBinding.setUser(CometChat.getLoggedInUser());
        if (getActivity() != null)
            new FontUtils(getActivity());

        moreInfoScreenBinding.tvTitle.setTypeface(FontUtils.robotoMedium);
        Log.e("onCreateView: ", CometChat.getLoggedInUser().toString());
        moreInfoScreenBinding.privacyAndSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CometChatMorePrivacyScreen.class));
            }
        });
        return moreInfoScreenBinding.getRoot();
    }


}
