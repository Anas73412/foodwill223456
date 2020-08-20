package binplus.foodiswill;

import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import binplus.foodiswill.Config.BaseURL;
import binplus.foodiswill.Config.Module;
import binplus.foodiswill.util.CustomVolleyJsonRequest;

public class OtpGenerateActivity extends AppCompatActivity {

  Module module;
  EditText et_gen_otp;
  Button btn_otp_verify;
  public String otp="";
  Dialog loadingBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_otp_generate);

    btn_otp_verify=(Button)findViewById(R.id.btn_otp_verify);
    et_gen_otp=(EditText)findViewById(R.id.et_gen_otp);
    loadingBar=new Dialog(OtpGenerateActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
    loadingBar.setContentView( R.layout.progressbar );
    loadingBar.setCanceledOnTouchOutside(false);

    module=new Module(OtpGenerateActivity.this);

    btn_otp_verify.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

//                Intent intent=new Intent(OtpGenerateActivity.this,OtpActivity.class);
//                startActivity(intent);
//                finish();

        String number=et_gen_otp.getText().toString().trim();

        otp=getRandomKey(6);

        sendCode(number,otp);


      }
    });

  }

  private void sendCode(final String number, final String otp) {
    loadingBar.show();
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
            loadingBar.dismiss();
            Intent intent=new Intent(OtpGenerateActivity.this,OtpActivity.class);
            intent.putExtra("response",String.valueOf(response));
            intent.putExtra("mobile",number);
            intent.putExtra("otp",otp);
            startActivity(intent);
            finish();

            //   Toast.makeText(OtpGenerateActivity.this,"true",Toast.LENGTH_LONG).show();


          }
          else
          {
            loadingBar.dismiss();
            Toast.makeText(OtpGenerateActivity.this,"false",Toast.LENGTH_LONG).show();
          }
        }
        catch (Exception ex)
        {        loadingBar.dismiss();
          ex.printStackTrace();
          Toast.makeText(OtpGenerateActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        // Toast.makeText(OtpActivity.this,""+response,Toast.LENGTH_LONG).show();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        String msg=module.VolleyErrorMessage(error);
        if(!msg.equals(""))
        {
          Toast.makeText(OtpGenerateActivity.this,""+msg,Toast.LENGTH_LONG).show();
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
}
