package dzikiekuny.com.hekaton.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.HashMap;
import java.util.Map;

import dzikiekuny.com.hekaton.BuildConfig;
import dzikiekuny.com.hekaton.MainActivity;
import dzikiekuny.com.hekaton.R;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "PartyGoer";

    //User data
    private String userID;
    private String userName;
    private String userFacebookID;
    //FaceBook
    private CallbackManager callbackManagerFromFacebook;

    //Creates facebook callback manager, login button, etc
    //Fires signInWithFacebook() if button is clicked
    private void setUpFacebookLogin() {
        //Create callback manager for facebook login
        //onSucces calls login method
        this.callbackManagerFromFacebook = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManagerFromFacebook, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "facebook:onSuccess:" + loginResult);
                userFacebookID = loginResult.getAccessToken().getUserId();
                SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("facebookid", userFacebookID);
                editor.commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Log.e("Facebook id", loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "facebook:onError", error);
            }
        });
    }


    //Create a new User and Save it in Firebase database
    //Fils with defaults to avoid NPE
    private void signUpNewUser(final String userID, String userName, String userFacebookID) {
        //Create map with user data
        final Map<String, Object> userDataInMap = new HashMap<>();
        userDataInMap.put("id", userID);
        userDataInMap.put("name", userName);
        userDataInMap.put("facebookid", userFacebookID);
        userDataInMap.put("online", 0);
        userDataInMap.put("hasroom", 0);
        //Update data in cache
        //Update user data on firebase
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManagerFromFacebook.onActivityResult(requestCode, resultCode, data);
    }

    //Startup method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        //Initialize facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setUpFacebookLogin();

    }
}