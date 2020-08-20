package binplus.foodiswill;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import binplus.foodiswill.Config.BaseURL;

import binplus.foodiswill.Config.Module;
import binplus.foodiswill.util.ConnectivityReceiver;
import binplus.foodiswill.util.CustomVolleyJsonRequest;
import binplus.foodiswill.util.Session_management;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

  Module module;
  private static String TAG = LoginActivity.class.getSimpleName();
  private RelativeLayout btn_continue, btn_register;
  private EditText et_password, et_email;
  private TextView tv_password, tv_email, btn_forgot;
  private Session_management sessionManagement;

  private RelativeLayout rel_pass,rel_otp;
  EditText et_login_otp;
  Button login_with_otp;
  private FirebaseAuth mAuth;
  boolean loginOtp=false;

  Dialog loadingBar;

  String mVerificationId;

  @Override
  protected void attachBaseContext(Context newBase) {

    newBase = LocaleHelper.onAttach(newBase);
    super.attachBaseContext(newBase);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // remove title
    setContentView(R.layout.activity_login);
    loadingBar=new Dialog(LoginActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
    loadingBar.setContentView( R.layout.progressbar );
    loadingBar.setCanceledOnTouchOutside(false);
    module=new Module(LoginActivity.this);
    // FirebaseApp.initializeApp(LoginActivity.this);
    //mAuth = FirebaseAuth.getInstance();
    et_password = (EditText) findViewById(R.id.et_login_pass);
    et_email = (EditText) findViewById(R.id.et_login_email);
    tv_password = (TextView) findViewById(R.id.tv_login_password);
    tv_email = (TextView) findViewById(R.id.tv_login_email);
    btn_continue = (RelativeLayout) findViewById(R.id.btnContinue);
    btn_register = (RelativeLayout) findViewById(R.id.btnRegister);
    btn_forgot = (TextView) findViewById(R.id.btnForgot);

    rel_pass=(RelativeLayout)findViewById(R.id.rel_password);
    rel_otp=(RelativeLayout)findViewById(R.id.rel_otp);
    et_login_otp=(EditText)findViewById(R.id.et_login_otp);
    login_with_otp=(Button)findViewById(R.id.login_with_otp);


    btn_continue.setOnClickListener(this);
    btn_register.setOnClickListener(this);
    btn_forgot.setOnClickListener(this);
    login_with_otp.setOnClickListener(this);



  }




  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.btnContinue) {
      attemptLogin();
    } else if (id == R.id.btnRegister) {
      Intent startRegister = new Intent(LoginActivity.this,Verfication_activity.class);
      //  Intent startRegister = new Intent(LoginActivity.this,RegisterActivity.class);
      startRegister.putExtra("type","r");
      startActivity(startRegister);
    } else if (id == R.id.btnForgot) {
      Intent startRegister = new Intent(LoginActivity.this, Verfication_activity.class);
      //   Intent startRegister = new Intent(LoginActivity.this,ForgotActivity.class);
      startRegister.putExtra("type","f");
      startActivity(startRegister);
//            Intent startRegister = new Intent(LoginActivity.this, ForgotActivity.class);
//            startActivity(startRegister);
    }
    else if(id==R.id.login_with_otp)
    {
      Intent startRegister = new Intent(LoginActivity.this, OtpGenerateActivity.class);
      startActivity(startRegister);
    }
  }

  private void attemptLogin() {

    tv_password.setTextColor(getResources().getColor(R.color.black));
    tv_email.setTextColor(getResources().getColor(R.color.black));
    String getpassword = et_password.getText().toString();
    String getemail = et_email.getText().toString();
    boolean cancel = false;
    View focusView = null;
    if (TextUtils.isEmpty(getpassword))
    {
      tv_password.setTextColor(getResources().getColor(R.color.black));
      focusView = et_password;
      cancel = true;
    }
    else if (getpassword.length()<4)
    {
      et_password.setError(getResources().getString(R.string.password_too_short));
      tv_password.setTextColor(getResources().getColor(R.color.black));
      focusView = et_password;
      cancel = true;
    }

    if (TextUtils.isEmpty(getemail))
    {

      tv_email.setTextColor(getResources().getColor(R.color.black));
      focusView = et_email;
      cancel = true;
    }
    else if (!isPhoneValid(getemail)) {
      tv_email.setText(getResources().getString(R.string.invalide_email_address));
      tv_email.setTextColor(getResources().getColor(R.color.black));
      focusView = et_email;
      cancel = true;
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      if (focusView != null)
        focusView.requestFocus();
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.

      if (ConnectivityReceiver.isConnected()) {
        makeLoginRequest(getemail, getpassword);
      }
    }

  }

//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() >= 6;
  }

  private boolean isPhoneValid(String phoneno) {

    return phoneno.length() == 10;
  }
  /**
   * Method to make json object request where json response starts wtih
   */
  private void makeLoginRequest(String email, final String password) {

    loadingBar.show();
    // Tag used to cancel the request
    String tag_json_obj = "json_login_req";
    Map<String, String> params = new HashMap<String, String>();
    params.put("user_phone", email);
    params.put("password", password);

    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            BaseURL.LOGIN_URL, params, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        loadingBar.dismiss();
        try {
          Boolean status = response.getBoolean("responce");
          if (status) {
            JSONObject obj = response.getJSONObject("data");
            String user_id = obj.getString("user_id");
            String user_fullname = obj.getString("user_fullname");
            String user_email = obj.getString("user_email");
            String user_phone = obj.getString("user_phone");
            String user_image = obj.getString("user_image");
            String wallet_ammount = obj.getString("wallet");
            String reward_points = obj.getString("rewards");
            Session_management sessionManagement = new Session_management(LoginActivity.this);
            sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image, wallet_ammount, reward_points, "", "", "", "", password);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

            btn_continue.setEnabled(false);

          } else {
            String error = response.getString("error");
            btn_continue.setEnabled(true);

            Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
          }
        }
        catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        loadingBar.dismiss();
        String msg=module.VolleyErrorMessage(error);
        if(!msg.equals(""))
        {
          Toast.makeText(LoginActivity.this,""+msg,Toast.LENGTH_LONG).show();
        }
      }
    });

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
  }



}
