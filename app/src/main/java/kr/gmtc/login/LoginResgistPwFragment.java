package kr.gmtc.login;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import kr.gmtc.login.data.LoginManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginResgistPwFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginResgistPwFragment extends Fragment
                                    implements LoginManager.LoginListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mLoginComment;
    private TextInputEditText mLoginUserId;
    private TextInputEditText mLoginUserPw;
    private MaterialButton mLoginUserRegist;
    private Handler handler = new Handler(Looper.getMainLooper());

    public LoginResgistPwFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginResgistPwFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginResgistPwFragment newInstance(String param1, String param2) {
        LoginResgistPwFragment fragment = new LoginResgistPwFragment();
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
        View mView = inflater.inflate(R.layout.fragment_login_resgist_pw, container, false);

        initialize(mView);

        return mView;
    }

    private void initialize(View mView){
        mLoginComment = mView.findViewById(R.id.txt_comment);
        mLoginUserId = mView.findViewById(R.id.edt_user_id);
        mLoginUserPw = mView.findViewById(R.id.edt_user_pw);
        mLoginUserRegist = mView.findViewById(R.id.btn_user_regist);

        addListener();
    }

    private void addListener(){
        mLoginUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoginManager.getInstance().setEditableUserId(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLoginUserPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLoginUserRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().tryToRegistPw(mLoginUserPw.getText().toString());
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
        Navigation.findNavController(getView()).navigate(R.id.loginRegistPinFragment);
    }

    @Override
    public void onFail(String error, int numberOfAttempts) {
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

    }

    @Override
    public void onSkip() {

    }
}