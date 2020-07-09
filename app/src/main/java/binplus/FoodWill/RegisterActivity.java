package binplus.FoodWill;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import binplus.FoodWill.Config.BaseURL;
import binplus.FoodWill.Config.Module;
import binplus.FoodWill.util.ConnectivityReceiver;
import binplus.FoodWill.util.CustomVolleyJsonRequest;

public class RegisterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

  Module module ;
  private static String TAG = RegisterActivity.class.getSimpleName();

  String number="" ,whtsapp="" ,fullname;
  String verfiy_number;
  private EditText et_phone, et_f_name, et_mpin, et_l_name,et_reg__con_password;
  private RelativeLayout btn_register;
  private TextView tv_phone, tv_name, tv_password, tv_email,tv_reg_con_password;
  Dialog loadingBar ;
  RadioButton r_yes ,r_no ;

  @Override
  protected void attachBaseContext(Context newBase) {



    newBase = LocaleHelper.onAttach(newBase);
    super.attachBaseContext(newBase);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // remove title

    setContentView(R.layout.activity_register);
    //  verfiy_number=getIntent().getStringExtra("mobile");
    loadingBar=new Dialog(RegisterActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
    loadingBar.setContentView( R.layout.progressbar );
    loadingBar.setCanceledOnTouchOutside(false);
    number=getIntent().getStringExtra("mobile");
    et_phone = (EditText) findViewById(R.id.et_reg_phone);
    et_f_name = (EditText) findViewById(R.id.et_reg_name);
    et_l_name = (EditText) findViewById(R.id.et_l_name);
    et_mpin = (EditText) findViewById(R.id.et_reg_password);
    r_no = findViewById(R.id.btn_no);
    r_yes = findViewById(R.id.btn_yes);
    r_no.setOnCheckedChangeListener(this);
    r_yes.setOnCheckedChangeListener(this);
    et_reg__con_password = (EditText) findViewById(R.id.et_reg__con_password);
//    et_email = (EditText) findViewById(R.id.et_reg_email);
//    tv_password = (TextView) findViewById(R.id.tv_reg_password);
    tv_phone = (TextView) findViewById(R.id.tv_reg_phone);
    module=new Module(RegisterActivity.this);
    tv_name = (TextView) findViewById(R.id.tv_reg_name);
//    tv_email = (TextView) findViewById(R.id.tv_reg_email);
    tv_reg_con_password = (TextView) findViewById(R.id.tv_reg_con_password);
    btn_register = (RelativeLayout) findViewById(R.id.btnRegister);

    et_phone.setText(number);
    et_phone.setEnabled(false);

    btn_register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        attemptRegister();
      }
    });
  }

  private void attemptRegister() {

        String getphone = et_phone.getText().toString();
    String get_f_name = et_f_name.getText().toString();
    String get_l_name = et_l_name.getText().toString();
    String getpassword = et_mpin.getText().toString();
//    String getemail = et_email.getText().toString();
//    String getconpass=et_reg__con_password.getText().toString();
    String f = String.valueOf( getphone.charAt( 0 ) );
    String plus = "[+]" ;
    //  int first = Integer.parseInt( f );

    boolean cancel = false;
    View focusView = null;

    if (TextUtils.isEmpty(getphone)) {

      focusView = et_phone;
      cancel = true;
    }
//        else if (!isPhoneValid(getphone)) {
//            tv_phone.setText(getResources().getString(R.string.phone_too_short));
//            tv_phone.setTextColor(getResources().getColor(R.color.black));
//            focusView = et_phone;
//            cancel = true;
//        }
    else if(!(getphone.length()==10))
    {
      et_phone.setText("Invalid mobile number");

      focusView = et_phone;
      cancel = true;
    }
    else if (getphone.charAt(0)<6)
    {
//        { tv_phone.setText("Invalid mobile number");
      et_phone.setError( "Invalid mobile number" );
      focusView = et_phone;
      cancel = true;

    }


    if (TextUtils.isEmpty(get_l_name)) {
      et_l_name.setError( "name is required" );
      focusView = et_l_name;
      cancel = true;
    }
    if (TextUtils.isEmpty(get_f_name)) {
      et_f_name.setError( "name is required" );
      focusView = et_f_name;
      cancel = true;
    }
//    else if (TextUtils.isEmpty(getpassword)) {
//      et_mpin.setError( "Mpin is Required" );
//      focusView = et_mpin;
//      cancel = true;
//    } else if (getpassword.length()<4) {
//      et_mpin.setError( "Mpin should be of min 4 characters" );
//
//      focusView = et_mpin;
//      cancel = true;
//    }
    else if (whtsapp.isEmpty())
    {
      cancel = true ;
      focusView = r_yes;
      Toast.makeText(RegisterActivity.this, "Please mention it is your whatsapp no or not ", Toast.LENGTH_SHORT).show();
    }
//    if (TextUtils.isEmpty(getconpass)) {
//      et_reg__con_password.setError( "Confirm Password is Required" );
//      focusView = et_reg__con_password;
//      cancel = true;
//    } else if (getconpass.length()<6) {
//      et_reg__con_password.setError(getResources().getString(R.string.password_too_short));
//
//      focusView = et_reg__con_password;
//      cancel = true;
//    }

//



    if (cancel==true) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      if (focusView != null)
        focusView.requestFocus();
    }
    else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.

      if (ConnectivityReceiver.isConnected()) {

//        if(getconpass.equals(getpassword))
//        {
          fullname = get_f_name + " "+ get_l_name;

          makeRegisterRequest(fullname, getphone, getpassword,whtsapp);
//        }
//        else
//        {
//          Toast.makeText(RegisterActivity.this,"Password must be matched",Toast.LENGTH_LONG).show();
//        }

      }
    }


  }


  /**
   * Method to make json object request where json response starts wtih
   */
  private void makeRegisterRequest(String name, String mobile,
                                   String password, String status) {

    loadingBar.show();
    // Tag used to cancel the request
    String tag_json_obj = "json_register_req";

    Map<String, String> params = new HashMap<String, String>();
    params.put("user_name", name);
    params.put("user_mobile",mobile);
//    params.put("user_email", "");
    params.put("on_whatsapp",status);
    params.put("password",password);


    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {
        Log.d(TAG, response.toString());

        loadingBar.dismiss();
        try {
          Boolean status = response.getBoolean("responce");
          if (status) {

            String msg = response.getString("message");
            Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            btn_register.setEnabled(false);

          } else {
            String error = response.getString("error");
            Toast.makeText(RegisterActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();

            btn_register.setEnabled(true);

            if(error.equalsIgnoreCase("The Mobile Number field must contain a unique value."))
            {
              Toast.makeText(RegisterActivity.this, "Mobile number already exist.", Toast.LENGTH_SHORT).show();

            }
//            else if(error.equals("The User Email field must contain a unique value."))
//            {
//              Toast.makeText(RegisterActivity.this, "Email address already exist.", Toast.LENGTH_SHORT).show();
//            }
            else
            {
              Toast.makeText(RegisterActivity.this, "Mobile number already exist.", Toast.LENGTH_SHORT).show();
            }

          }
        } catch (JSONException e) {
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
          Toast.makeText(RegisterActivity.this,""+msg,Toast.LENGTH_LONG).show();
        }
      }
    });

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    if (buttonView.getId()== R.id.btn_yes)
    {
      if (isChecked)
      {
        whtsapp = "1";
      }
      else
      {
        whtsapp = "0";
      }
      Toast.makeText(RegisterActivity.this,""+whtsapp,Toast.LENGTH_LONG).show();
    }
    else if (buttonView.getId() == R.id.btn_no)
    {
      if (isChecked)
      {
        whtsapp = "0";
      }
      else
      {
        whtsapp = "1";
      }
      Toast.makeText(RegisterActivity.this,""+whtsapp,Toast.LENGTH_LONG).show();
    }
  }
}
