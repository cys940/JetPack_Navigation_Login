package kr.gmtc.login.config;

public class LoginConfig {
    private boolean isEnableLogin = false;

    public void setEnableLogin(boolean isEnableLogin){
        this.isEnableLogin = isEnableLogin;
    }

    public boolean isEnableLogin(){
        return isEnableLogin;
    }
}
