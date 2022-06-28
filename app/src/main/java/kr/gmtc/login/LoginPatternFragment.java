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

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import kr.gmtc.login.data.LoginManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginPatternFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginPatternFragment extends Fragment
                                  implements LoginManager.LoginListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mLoginComment;
    private PatternLockView mPatternView;
    private MaterialButton mLoginUserTry;
    private ExtendedFloatingActionButton mLoginExtendFab;
    private MaterialCheckBox mLoginTypePw;
    private MaterialCheckBox mLoginTypePin;
    private MaterialCheckBox mLoginTypePattern;
    private LinearLayout mLoginTypes;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int numberOfAttempts = 0;
    private boolean isExpand;
    private String userPattern= null;

    public LoginPatternFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginPatternFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginPatternFragment newInstance(String param1, String param2) {
        LoginPatternFragment fragment = new LoginPatternFragment();
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
        View mView = inflater.inflate(R.layout.fragment_login_pattern, container, false);

        initialize(mView);

        return mView;
    }

    private void initialize(View mView){
        mLoginComment = mView.findViewById(R.id.txt_comment);
        mPatternView = mView.findViewById(R.id.pattern_view);
        mLoginUserTry = mView.findViewById(R.id.btn_user_login);
        mLoginExtendFab = mView.findViewById(R.id.fab_expand);
        mLoginTypes = mView.findViewById(R.id.lyt_collapse);
        mLoginTypePw = mView.findViewById(R.id.chk_user_type_pw);
        mLoginTypePin = mView.findViewById(R.id.chk_user_type_pin);
        mLoginTypePattern = mView.findViewById(R.id.chk_user_type_pattern);

        mLoginTypes.setVisibility(View.GONE);
        isExpand = false;

        addListener();
    }

    private void addListener(){
        mPatternView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                userPattern = PatternLockUtils.patternToString(mPatternView, progressPattern);
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (pattern.size() < 9) {
                    mPatternView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }

                userPattern = PatternLockUtils.patternToString(mPatternView, pattern);
            }

            @Override
            public void onCleared() {
                Log.d("TAG", "Pattern has been cleared");
            }
        });

        mLoginUserTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().tryToLogin(
                        LoginManager.LoginType.PATTERN,
                        null,
                        userPattern,
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