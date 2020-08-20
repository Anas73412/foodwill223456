package binplus.foodiswill;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import binplus.foodiswill.Config.BaseURL;
import binplus.foodiswill.Config.Module;
import binplus.foodiswill.util.CustomVolleyJsonRequest;
import binplus.foodiswill.util.Session_management;

public class OtpActivity extends AppCompatActivity {

    Module module;
    private static String TAG = OtpActivity.class.getSimpleName();
    private static final long START_TIME_IN_MILLI=50000;
    boolean mmTimerRunning;
    private long mTimeLeftINMILLIS=START_TIME_IN_MILLI;
    Button btn_otp_verify;
    EditText et_otp1,et_otp2,et_otp3,et_otp4,et_otp5,et_otp6;
    TextView txt_countdown;
    public String otp="";
    public String mobile="";
    public String response;
    CountDownTimer countDownTimer;
    int counter=0;
    Dialog loadingBar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        loadingBar=new Dialog(OtpActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module(OtpActivity.this);
        response=getIntent().getStringExtra("response");
        otp=getIntent().getStringExtra("otp");
        mobile=getIntent().getStringExtra("mobile");

        btn_otp_verify=(Button) findViewById(R.id.btn_otp_verify);
        txt_countdown=(TextView)findViewById(R.id.tv_countdown);
        et_otp1=(EditText)findViewById(R.id.et_otp1);
        et_otp2=(EditText)findViewById(R.id.et_otp2);
        et_otp3=(EditText)findViewById(R.id.et_otp3);
        et_otp4=(EditText)findViewById(R.id.et_otp4);
        et_otp5=(EditText)findViewById(R.id.et_otp5);
        et_otp6=(EditText)findViewById(R.id.et_otp6);

        statTimer();



        et_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                et_otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                et_otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                et_otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                et_otp5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                et_otp6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tm=txt_countdown.getText().toString().trim();
                String n1=et_otp1.getText().toString().trim();
                String n2=et_otp2.getText().toString().trim();
                String n3=et_otp3.getText().toString().trim();
                String n4=et_otp4.getText().toString().trim();
                String n5=et_otp5.getText().toString().trim();
                String n6=et_otp6.getText().toString().trim();

                if(n1.isEmpty() && n2.isEmpty() && n3.isEmpty() && n4.isEmpty() && n5.isEmpty() && n6.isEmpty())
                {
                    Toast.makeText(OtpActivity.this,"Enter Valid OTP",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(counter>=3)
                    {

                        Toast.makeText(OtpActivity.this,"Limit exceed"+otp,Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(OtpActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        if(tm.equals("0"))
                        {
                            Toast.makeText(OtpActivity.this,"timeout"+otp,Toast.LENGTH_LONG).show();
                        }
                        else
                        {

                            String enter_otp=(n1+n2+n3+n4+n5+n6).toString();

                            if(otp.equals(enter_otp))
                            {
                                makeLoginRequest(response,mobile,otp);
                            }
                            else
                            {
                                Toast.makeText(OtpActivity.this,"Wrong OTP"+otp,Toast.LENGTH_LONG).show();
                            }
                            // Toast.makeText(OtpActivity.this,"timein"+otp,Toast.LENGTH_LONG).show();
                        }

                    }

                }



                counter++;

            }
        });


    }

    private void sendCode(String number, final String otp) {

        String json_tag="json_otp";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.URL_SEND_OTP, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    boolean responce=response.getBoolean("responce");
                    if(responce==true)
                    {
                        Toast.makeText(OtpActivity.this,"true",Toast.LENGTH_LONG).show();


                    }
                    else
                    {
                        Toast.makeText(OtpActivity.this,"false",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(OtpActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }

                // Toast.makeText(OtpActivity.this,""+response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(OtpActivity.this,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);


    }



    public static String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }


    public void statTimer()
    {
        countDownTimer=new CountDownTimer(mTimeLeftINMILLIS,1000) {
            @Override
            public void onTick(long l) {

                mTimeLeftINMILLIS=l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                otp="";
                txt_countdown.setText("timout");
                txt_countdown.setTextColor( Color.RED);


            }
        }.start();
        mmTimerRunning=true;
    }

    private void updateCountDownText() {
        int minutes=(int)(mTimeLeftINMILLIS/1000)/60;
        int seconds=(int)(mTimeLeftINMILLIS/1000)%60;
        String timeLeftForamatedd=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        txt_countdown.setText(timeLeftForamatedd);

    }


    public void resetTimer()
    {

    }


    private void makeLoginRequest(String response,String mobile, final String otp) {

        // Tag used to cancel the request



        try {
            JSONObject res=new JSONObject(response);
            Boolean status = res.getBoolean("responce");
            if (status) {
                JSONObject obj = res.getJSONObject("data");
                String user_id = obj.getString("user_id");
                String user_fullname = obj.getString("user_fullname");
                String user_email = obj.getString("user_email");
                String user_phone = obj.getString("user_phone");
                String user_image = obj.getString("user_image");
                String wallet_ammount = obj.getString("wallet");
                String reward_points = obj.getString("rewards");
                String password=otp;
                Session_management sessionManagement = new Session_management(OtpActivity.this);
                sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image, wallet_ammount, reward_points, "", "", "", "", password);
                Intent i = new Intent(OtpActivity.this, MainActivity.class);
                startActivity(i);
                finish();

                //  btn_continue.setEnabled(false);

            } else {
                String error = res.getString("error");
                //btn_continue.setEnabled(true);

                Toast.makeText(OtpActivity.this, "er" + error, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
