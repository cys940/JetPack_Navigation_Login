package kr.gmtc.login.data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.gmtc.login.FinishActivity;
import kr.gmtc.login.config.LoginConfig;
import kr.gmtc.login.config.LoginErrors;

public class LoginManager {
    private static LoginManager INSTANCE = null;
    static public LoginManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new LoginManager();

        return INSTANCE;
    }

    public interface LoginListener {
        void onSuccess();
        void onFail(String error, int numberOfAttempts);
        void onTerminate();
        void onSkip();
    }

    public enum LoginType {
        PASSWORD, PIN, PATTERN;

        public int toInt(){
            switch (this) {
                case PASSWORD: return 0;
                case PIN: return 1;
                case PATTERN: return 2;
                default: return -1;
            }
        }
    }

    private HashMap<String, User> users = new HashMap<>();
    private LoginType loginType = LoginType.PASSWORD;
    private LoginConfig loginCfg = new LoginConfig(); // 설정에 따른 값들.. prefference 에 들어갈 값들..
    private ArrayList<LoginListener> loginListeners = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private String editableUserId = null;

    public void addLoginListener(LoginListener listener){
        if (listener == null)
            return;

        synchronized (loginListeners) {
            if (!loginListeners.contains(listener))
                loginListeners.add(listener);
        }
    }

    public void removeLoginListener(LoginListener listener){
        synchronized (loginListeners) {
            loginListeners.remove(listener);
        }
    }

    public String getEditableUserId() {
        return editableUserId;
    }

    public void setEditableUserId(String editableUserId) {
        this.editableUserId = editableUserId;
    }

    public LoginConfig getLoginCfg() {
        return loginCfg;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public boolean isRegistUser(){
        return users.size() > 0;
    }

    private boolean isVaildPinLength(String userPin, int length){
        return userPin.trim().length() == length;
    }

    private boolean isVaildPw(String userPw){
        String symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
        String cases = "([a-z].*[A-Z])|([A-Z].*[a-z])";
        Pattern patternSymbol = Pattern.compile(symbol);
        Pattern patternCases = Pattern.compile(cases);

        if (patternSymbol.matcher(userPw).find() && patternCases.matcher(userPw).find())
            return true;

        return false;
    }

    public void addUser(User user){
        users.put(user.getUserId(), user);

        notifySuccess();
    }

    public void removeUser(User user){
        users.remove(user);
    }

    public User getUser(LoginType loginType, String userId, String password){
        User user = users.get(userId);

        if (user != null) {
            switch (loginType) {
                case PASSWORD:
                    if (users.get(userId).getUserPw().equals(password)) {
                        return user;
                    } else
                        return null;

                case PIN:
                    if (users.get(userId).getUserPinNm().equals(password)) {
                        return user;
                    } else
                        return null;

                case PATTERN:
                    if (users.get(userId).getUserPattern().equals(password)) {
                        return user;
                    } else
                        return null;
            }
        }

        return user;
    }

    public void tryToLogin(LoginType loginType, String userId, String password, int numberOfAttempts){
        if (getUser(loginType, userId, password) != null)
            notifySuccess();
        else {
            ++numberOfAttempts;

            if (numberOfAttempts <= 5)
                notifyFail(LoginErrors.ERR_CODE_DIFF + String.format(" ( %d / 5 )", numberOfAttempts), numberOfAttempts);
            else
                notifyTerminate();
        }
    }

    public void tryToRegistPw(String password){
        if (editableUserId == null || editableUserId.trim().isEmpty()) {
            notifyFail(LoginErrors.ERR_CODE_ID, -1);
        } else if (password == null || password.trim().isEmpty()){
            notifyFail(LoginErrors.ERR_CODE_PW_LENGTH, -1);
        } else if (!isVaildPw(password.trim()) || password.trim().length() < 10){
            notifyFail(LoginErrors.ERR_CODE_PW_REG, -1);
        } else {
            //TODO 등록하는 로직 필요
            notifySuccess();
        }
    }

    public void tryToRegistPinNm(String password){
        if (editableUserId == null || editableUserId.trim().isEmpty()) {
            notifyFail(LoginErrors.ERR_CODE_ID, -1);
        } else if (password == null || password.trim().isEmpty() || !isVaildPinLength(password, 6)){
            notifyFail(LoginErrors.ERR_CODE_PW_LENGTH, -1);
        } else {
            //TODO 등록하는 로직 필요
            notifySuccess();
        }
    }

    public void tryToRegistPattern(String password){
        if (editableUserId == null || editableUserId.trim().isEmpty()) {
            notifyFail(LoginErrors.ERR_CODE_ID, -1);
        } else if (password == null || password.trim().isEmpty() || !isVaildPinLength(password, 9)){
            notifyFail(LoginErrors.ERR_CODE_PW_LENGTH, -1);
        } else {
            //TODO 등록하는 로직 필요
            notifySuccess();
        }
    }

    public void skip(){
        notifySkip();
    }

    private void notifySuccess(){
        handler.post(() -> {
            for(int i = loginListeners.size()-1; i>=0; i--) {
                LoginListener loginListener = loginListeners.get(i);
                if(loginListener != null)
                    loginListener.onSuccess();
                else
                    loginListeners.remove(i);
            }
        });
    }

    private void notifySkip(){
        handler.post(() -> {
            for(int i = loginListeners.size()-1; i>=0; i--) {
                LoginListener loginListener = loginListeners.get(i);
                if(loginListener != null)
                    loginListener.onSkip();
                else
                    loginListeners.remove(i);
            }
        });
    }

    private void notifyFail(String error, int numberOfAttempts){
        handler.post(() -> {
            for(int i = loginListeners.size()-1; i>=0; i--) {
                LoginListener loginListener = loginListeners.get(i);
                if(loginListener != null)
                    loginListener.onFail(error, numberOfAttempts);
                else
                    loginListeners.remove(i);
            }
        });
    }

    private void notifyTerminate(){
        handler.post(() -> {
            for(int i = loginListeners.size()-1; i>=0; i--) {
                LoginListener loginListener = loginListeners.get(i);
                if(loginListener != null)
                    loginListener.onTerminate();
                else
                    loginListeners.remove(i);
            }
        });
    }

    public void loginSuccessed(Context context){
        Intent intent = new Intent(context, FinishActivity.class);
        context.startActivity(intent);
    }

    public void terminate(Context context){

    }
}
