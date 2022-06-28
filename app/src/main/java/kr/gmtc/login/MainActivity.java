package kr.gmtc.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import kr.gmtc.login.config.LoginConfig;
import kr.gmtc.login.data.LoginManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigation();
    }

    private void initNavigation(){
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        int startDestination = R.id.loginFragment;//LoginManager.getInstance().isRegistUser() ? R.id.loginFragment : R.id.loginResgistPwFragment;

        NavOptions navOptions = null;

        if (startDestination == R.id.loginResgistPwFragment) {
            navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build();
        }

        navController.navigate(startDestination , null ,navOptions);
    }

}