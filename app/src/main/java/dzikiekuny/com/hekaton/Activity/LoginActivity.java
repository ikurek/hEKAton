package dzikiekuny.com.hekaton.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;

import dzikiekuny.com.hekaton.BuildConfig;
import dzikiekuny.com.hekaton.DatabaseConnection.DatabaseSupport;
import dzikiekuny.com.hekaton.MainActivity;
import dzikiekuny.com.hekaton.Models.UserModel;
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
                userName = Profile.getCurrentProfile().getName();
                final DatabaseSupport databaseSupport = new DatabaseSupport();

                //PREPARE REQUEST-----------------------------------------------------------------------------------------------
                JsonArrayRequest getUser = new JsonArrayRequest
                        (Request.Method.GET, "http://dzikiekuny.azurewebsites.net/tables/users?ZUMO-API-VERSION=2.0.0", null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    UserModel myUser = databaseSupport.jsonUsersParser(response, userFacebookID);

                                    if (myUser == null) {
                                        Log.e("setUpFacebookLogin", "Creating new user: " + userFacebookID);
                                        databaseSupport.insertUserAsModel(new UserModel(userName, userFacebookID, "XD"));
                                    } else {
                                        Log.e("setUpFacebookLogin", "Found user: " + myUser.getName());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }
                        });

                //REQUEST--------------------------------------------------------------------------------------------------
                DiskBasedCache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap   //TODO: zapytac igora
                BasicNetwork network = new BasicNetwork(new HurlStack());
                RequestQueue mRequestQueue = new RequestQueue(cache, network);
                mRequestQueue.start();
                mRequestQueue.add(getUser);


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
        if (Profile.getCurrentProfile() == null) {
            setUpFacebookLogin();
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

    }
}