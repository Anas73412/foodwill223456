package binplus.FoodWill.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import binplus.FoodWill.ForgotActivity;
import binplus.FoodWill.R;
import binplus.FoodWill.RegisterActivity;

public class Otp_verification_Fragment extends Fragment {

  private static String TAG = Otp_Generate_Fragment.class.getSimpleName();
  private static final long START_TIME_IN_MILLI=50000;
  boolean mmTimerRunning;
  private long mTimeLeftINMILLIS=START_TIME_IN_MILLI;
  public String otp="";
  CountDownTimer countDownTimer;
  EditText et_otp1,et_otp2,et_otp3,et_otp4,et_otp5,et_otp6;
  String otp_verify;
  String type="";
  String mobile_verify;
  public TextView tv_countdown;
  public EditText et_otp;
  Button btn_mobile_number;
  Dialog loadingBar;
  public Otp_verification_Fragment() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_otp_verification, container, false);
    //((Verfication_activity) getActivity()).setTitle("Verification");

    loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
    loadingBar.setContentView( R.layout.progressbar );
    loadingBar.setCanceledOnTouchOutside(false);

    otp_verify=getArguments().getString("otp");
    type=getArguments().getString("type");
    et_otp1=(EditText)view.findViewById(R.id.et_otp1);
    et_otp2=(EditText)view.findViewById(R.id.et_otp2);
    et_otp3=(EditText)view.findViewById(R.id.et_otp3);
    et_otp4=(EditText)view.findViewById(R.id.et_otp4);
    et_otp5=(EditText)view.findViewById(R.id.et_otp5);
    et_otp6=(EditText)view.findViewById(R.id.et_otp6);

    mobile_verify=getArguments().getString("mobile");
    tv_countdown=(TextView) view.findViewById(R.id.tv_countdown);
    //  et_otp=(EditText)view.findViewById(R.id.et_otp);
    btn_mobile_number=(Button)view.findViewById(R.id.btn_mobile_number);
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


    btn_mobile_number.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        String tm = tv_countdown.getText().toString().trim();
        String n1 = et_otp1.getText().toString().trim();
        String n2 = et_otp2.getText().toString().trim();
        String n3 = et_otp3.getText().toString().trim();
        String n4 = et_otp4.getText().toString().trim();
        String n5 = et_otp5.getText().toString().trim();
        String n6 = et_otp6.getText().toString().trim();

        if (n1.isEmpty() && n2.isEmpty() && n3.isEmpty() && n4.isEmpty() && n5.isEmpty() && n6.isEmpty()) {
          Toast.makeText( getActivity(), "Enter Valid OTP", Toast.LENGTH_LONG ).show();
        } else {

          if (tm.equals( "0" )) {
            //Toast.makeText( getActivity(), "timeout" + otp, Toast.LENGTH_LONG ).show();
          } else {

            String enter_otp = (n1 + n2 + n3 + n4 + n5 + n6).toString();

            // Toast.makeText( getActivity(), ""+enter_otp, Toast.LENGTH_LONG ).show();
            if (otp_verify.equals( enter_otp )) {

              if(type.equals("r"))
              {
                Intent intent = new Intent( getActivity(), RegisterActivity.class );
                intent.putExtra( "mobile", mobile_verify );
                startActivity( intent );
                getActivity().finish();
              }
              else if(type.equals("f"))
              {
                Intent intent = new Intent( getActivity(), ForgotActivity.class );
                intent.putExtra( "mobile", mobile_verify );
                startActivity( intent );
                getActivity().finish();
              }

            } else {
              Toast.makeText( getActivity(), "Invalid OTP ", Toast.LENGTH_LONG ).show();
            }
          }

        }
      }
    });

    return view;
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
        tv_countdown.setText("timout");
        tv_countdown.setTextColor(Color.RED);


      }
    }.start();
    mmTimerRunning=true;
  }

  private void updateCountDownText() {
    int minutes=(int)(mTimeLeftINMILLIS/1000)/60;
    int seconds=(int)(mTimeLeftINMILLIS/1000)%60;
    String timeLeftForamatedd=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
    tv_countdown.setText(timeLeftForamatedd);

  }
}
