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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import kr.gmtc.login.data.LoginManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginPinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginPinFragment extends Fragment
                              implements LoginManager.LoginListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PinLockView mPinLockView;
    private IndicatorDots mPinIndicatorDots;
    private TextView mLoginComment;
    private TextInputLayout mLoginUserIdContainer;
    private TextInputEditText mLoginUserId;
    private TextInputEditText mLoginUserPw;
    private MaterialButton mLoginUserTry;
    private ExtendedFloatingActionButton mLoginExtendFab;
    private MaterialCheckBox mLoginTypePw;
    private MaterialCheckBox mLoginTypePin;
    private MaterialCheckBox mLoginTypePattern;
    private LinearLayout mLoginTypes;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int numberOfAttempts = 0;
    private boolean isExpand;
    private String mUserPw;

    public LoginPinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginPinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginPinFragment newInstance(String param1, String param2) {
        LoginPinFragment fragment = new LoginPinFragment();
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
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_login_pin, container, false);

        initialize(mView);

        return mView;
    }

    private void initialize(View mView){
        mLoginComment = mView.findViewById(R.id.txt_comment);
        mLoginUserIdContainer = mView.findViewById(R.id.lyt_user_id);
        mLoginUserId = mView.findViewById(R.id.edt_user_id);
        mLoginUserPw = mView.findViewById(R.id.edt_user_pw);
        mLoginUserTry = mView.findViewById(R.id.btn_user_login);
        mLoginExtendFab = mView.findViewById(R.id.fab_expand);
        mLoginTypes = mView.findViewById(R.id.lyt_collapse);
        mLoginTypePw = mView.findViewById(R.id.chk_user_type_pw);
        mLoginTypePin = mView.findViewById(R.id.chk_user_type_pin);
        mLoginTypePattern = mView.findViewById(R.id.chk_user_type_pattern);
        mPinLockView =mView.findViewById(R.id.pin_lock_view);
        mPinIndicatorDots = mView.findViewById(R.id.indicator_dots);
        mPinIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
        mPinIndicatorDots.setPinLength(6);
        mPinLockView.attachIndicatorDots(mPinIndicatorDots);
        mPinLockView.setPinLength(6);
        mPinLockView.enableLayoutShuffling();

        mLoginTypes.setVisibility(View.GONE);
        isExpand = false;

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

        mLoginUserTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick tryToLogin");
                LoginManager.getInstance().tryToLogin(
                        LoginManager.LoginType.PIN,
                        mLoginUserId.getText().toString().trim(),
                        mLoginUserPw.getText().toString().trim(),
                        numberOfAttempts
                );
            }
        });

        mLoginExtendFab.setOnClickListener(view -> handler.post(() -> {
            if (!isExpand) {
                mLoginTypes.setVisibility(View.VISIBLE);
                mLoginExtendFab.setIcon(getContext().getDrawable(R.drawable.icon_collase));
                mLoginExtendFab.shrink();
                isExpand = true;
            } else {
                mLoginTypes.setVisibility(View.GONE);
                mLoginExtendFab.setIcon(getContext().getDrawable(R.drawable.icon_expand));
                mLoginExtendFab.extend();
                isExpand = false;
            }
        }));

        mLoginTypePw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                mLoginTypePw.setChecked(isSelected);
                mLoginTypePin.setChecked(false);
                mLoginTypePattern.setChecked(false);

                LoginManager.getInstance().setLoginType(LoginManager.LoginType.PASSWORD);
                Navigation.findNavController(getView()).navigate(R.id.loginFragment);
            }
        });

        mLoginTypePin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                mLoginTypePw.setChecked(false);
                mLoginTypePin.setChecked(isSelected);
                mLoginTypePattern.setChecked(false);

                LoginManager.getInstance().setLoginType(LoginManager.LoginType.PIN);
                Navigation.findNavController(getView()).navigate(R.id.loginPinFragment);
            }
        });

        mLoginTypePattern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                mLoginTypePw.setChecked(false);
                mLoginTypePin.setChecked(false);
                mLoginTypePattern.setChecked(isSelected);

                LoginManager.getInstance().setLoginType(LoginManager.LoginType.PATTERN);
                Navigation.findNavController(getView()).navigate(R.id.loginPatternFragment);
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
        LoginManager.getInstance().loginSuccessed(getContext());
    }

    @Override
    public void onFail(String error, int numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;

        handler.post(new Runnable() {
            @Override
            public void run() {
                mLoginComment.setText(error);
                mLoginComment.setTextColor(Color.RED);
            }
        });
    }

    @Override
    public void onTerminate() {
        LoginManager.getInstance().terminate(getContext());
    }

    @Override
    public void onSkip() {

    }
}