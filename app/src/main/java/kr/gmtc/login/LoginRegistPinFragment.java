package kr.gmtc.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.android.material.button.MaterialButton;

import kr.gmtc.login.data.LoginManager;
import kr.gmtc.login.data.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginRegistPinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginRegistPinFragment extends Fragment
                                    implements LoginManager.LoginListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PinLockView mPinLockView;
    private IndicatorDots mPinIndicatorDots;
    private TextView mUserComment;
    private MaterialButton mUserRegist;
    private MaterialButton mUserSkip;
    private String mUserPw;
    private Handler handler = new Handler(Looper.getMainLooper());

    public LoginRegistPinFragment() {
        // Required empty public constructor
    }


    public static LoginRegistPinFragment newInstance(String param1, String param2) {
        LoginRegistPinFragment fragment = new LoginRegistPinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_login_regist_pin, container, false);

        initialize(mView);

        return mView;
    }

    private void initialize(View mView){
        mPinLockView =mView.findViewById(R.id.pin_lock_view);
        mPinIndicatorDots = mView.findViewById(R.id.indicator_dots);
        mUserComment = mView.findViewById(R.id.txt_comment);
        mUserRegist = mView.findViewById(R.id.btn_user_regist);
        mUserSkip = mView.findViewById(R.id.btn_user_skip);
        mUserComment = mView.findViewById(R.id.txt_comment);

        mPinIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
        mPinIndicatorDots.setPinLength(6);
        mPinLockView.attachIndicatorDots(mPinIndicatorDots);
        mPinLockView.setPinLength(6);
        mPinLockView.enableLayoutShuffling();

        addListener();
    }

    private void addListener(){
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                mUserPw = pin;
            }

            @Override
            public void onEmpty() {}

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                mUserPw = intermediatePin;
            }
        });
        mUserRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().tryToRegistPinNm(mUserPw);
            }
        });
        mUserSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().skip();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        LoginManager.getInstance().addLoginListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        LoginManager.getInstance().removeLoginListener(this);
    }

    @Override
    public void onSuccess() {
        Navigation.findNavController(getView()).navigate(R.id.loginRegistPatternFragment);
    }

    @Override
    public void onFail(String error, int numberOfAttempts) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mUserComment.setText(error);
                mUserComment.setTextColor(Color.RED);
            }
        });
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onSkip() {
        LoginManager.getInstance().loginSuccessed(getContext());
    }
}