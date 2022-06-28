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

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import kr.gmtc.login.data.LoginManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginRegistPatternFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginRegistPatternFragment extends Fragment
                                        implements LoginManager.LoginListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PatternLockView mPatternView;
    private TextView mUserComment;
    private MaterialButton mUserRegist;
    private MaterialButton mUserSkip;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String userPattern= null;

    public LoginRegistPatternFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginRegistPatternFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginRegistPatternFragment newInstance(String param1, String param2) {
        LoginRegistPatternFragment fragment = new LoginRegistPatternFragment();
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
        View mView = inflater.inflate(R.layout.fragment_login_regist_pattern, container, false);

        initialize(mView);

        return mView;
    }

    private void initialize(View mView){
        mPatternView = mView.findViewById(R.id.pattern_view);
        mUserComment = mView.findViewById(R.id.txt_comment);
        mUserRegist = mView.findViewById(R.id.btn_user_regist);
        mUserSkip = mView.findViewById(R.id.btn_user_skip);

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

        mUserRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().tryToRegistPattern(userPattern);
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
        LoginManager.getInstance().loginSuccessed(getContext());
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