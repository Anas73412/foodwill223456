package binplus.foodiswill.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import binplus.foodiswill.Config.BaseURL;
import binplus.foodiswill.Config.Module;
import binplus.foodiswill.AppController;
import binplus.foodiswill.R;
import binplus.foodiswill.SmsVerificationActivity;
import binplus.foodiswill.Verfication_activity;
import binplus.foodiswill.networkconnectivity.NoInternetConnection;
import binplus.foodiswill.util.ConnectivityReceiver;
import binplus.foodiswill.util.CustomVolleyJsonRequest;

import static binplus.foodiswill.Config.BaseURL.URL_REGISTER_OTP;

public class Otp_Generate_Fragment extends Fragment implements View.OnClickListener{
    private static String TAG = Otp_Generate_Fragment.class.getSimpleName();
    EditText et_reg_number;
    Button btn_verify_number;
    Dialog loadingBar ;
    String type="";
    Module module;
    Context context;
    RadioButton btn_yes,btn_no;
    LinearLayout lin_reg,lin_pass;
    EditText et_f_name,et_l_name,et_phone;
    RelativeLayout btnRegister;
    String msg_status="";
    public Otp_Generate_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_gen_fragment, container, false);
        ((Verfication_activity) getActivity()).setTitle("Verification");

        Bundle bundle=getArguments();
        type=bundle.getString("type");
        et_reg_number=(EditText)view.findViewById(R.id.et_reg_number);
        btn_verify_number=(Button)view.findViewById(R.id.btn_verify_number);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module(getActivity());

        initViews(view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //((MainActivity) getActivity()).finish();
                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    final AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
        getMessageStatus();
        btn_verify_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int first=0;

                String phone=et_reg_number.getText().toString().trim();
                if(phone.length()>0)
                {
                    first = Integer.parseInt(phone.substring(0,1));
                }

                String f = String.valueOf( first);
//                int first = Integer.parseInt( f );
                String plus = "\u002B" ;

                if(phone.isEmpty())
                {
                    et_reg_number.setError("enter mobile number");
                    et_reg_number.requestFocus();
                }
                else if(!(phone.length()>=10))
                {
                    et_reg_number.setError("invalid mobile number");
                    et_reg_number.requestFocus();
                }
                else if (first<6)
                {
                    et_reg_number.setError("invalid mobile number");
                    et_reg_number.requestFocus();

                }

                else if(f.equals( plus ))
                {
                    et_reg_number.setError("invalid mobile number");
                    et_reg_number.requestFocus();
                }
                else
                {
                    if(ConnectivityReceiver.isConnected())
                    {
                        String otp=getRandomKey(6);
                        if(type.equals("r"))
                        {
//                            getVerificationCode(phone,otp);

                        }
                        else if(type.equals("f"))
                        {
                            getForgotVerificationCode(phone,otp);
                        }

                    }
                }

            }
        });
        return view;

    }

    private void initViews(View v) {
        btn_yes=v.findViewById(R.id.btn_yes);
        btn_no=v.findViewById(R.id.btn_no);
        lin_reg=v.findViewById(R.id.lin_reg);
        lin_pass=v.findViewById(R.id.lin_pass);
        et_f_name=v.findViewById(R.id.et_f_name);
        et_l_name=v.findViewById(R.id.et_l_name);
        et_phone=v.findViewById(R.id.et_phone);
        btn_yes.setChecked(true);
        btnRegister=v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);



    }

    private void getForgotVerificationCode(final String phone, final String otp) {

        loadingBar.show();

        String json_tag="json_verifiaction_tag";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("mobile",phone);
        params.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.URL_GEN_OTP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    //    Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();
                    boolean responce=response.getBoolean("responce");
                    if(responce)
                    {
                        loadingBar.dismiss();
                        Intent intent=new Intent(getActivity(), SmsVerificationActivity.class);
                        intent.putExtra("mobile",phone);
                        intent.putExtra("otp",otp);
                        intent.putExtra("type",type);
                        startActivity(intent);
//                          Bundle bundle=new Bundle();
//                        binplus.Jabico.Fragment fm=new Otp_verification_Fragment();
//                        bundle.putString("mobile",phone);
//                        bundle.putString("otp",otp);
//                        bundle.putString("type",type);
//                        fm.setArguments(bundle);
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.varify_container, fm)
//                                .addToBackStack(null).commit();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getActivity(),""+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getActivity(),""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);



    }


    private boolean isPhoneValid(String phoneno) {

        return phoneno.length() > 9;
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


    private void getVerificationCode(final String phone, final String name , final String otp, final int status) {
        loadingBar.show();

        String json_tag="json_verifiaction_tag";
        HashMap<String,String> mp=new HashMap<String, String>();
        mp.put("mobile",phone);
        mp.put("otp",otp);
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, URL_REGISTER_OTP, mp, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    boolean responce=response.getBoolean("responce");
                    if(responce)
                    {
                        Log.e("regiiii",""+response);
                        loadingBar.dismiss();
                        Intent intent=new Intent(getActivity(), SmsVerificationActivity.class);
                        intent.putExtra("mobile",phone);
                        intent.putExtra("otp",otp);
                        intent.putExtra("type",type);
                        intent.putExtra("name",name);
                        intent.putExtra("msg_status",msg_status);
                        intent.putExtra("status",String.valueOf(status));
                        startActivity(intent);
//                        getActivity().finish();
//                        Bundle bundle=new Bundle();
//                        binplus.Jabico.Fragment fm=new Otp_verification_Fragment();
//                        bundle.putString("mobile",phone);
//                        bundle.putString("otp",otp);
//                        bundle.putString("type",type);
//                        fm.setArguments(bundle);
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.varify_container, fm)
//                                .addToBackStack(null).commit();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(),""+response.getString("error").toString(),Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(getActivity(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getActivity(),""+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                module.showToast(""+error.getMessage());
//                if(!msg.isEmpty())
//                {
//                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
//                }
                //Toast.makeText(getActivity(),""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnRegister)
        {
            int first=0;
            String fName=et_f_name.getText().toString();
            String lName=et_l_name.getText().toString();
            String phone=et_phone.getText().toString().trim();
            if(phone.length()>0)
            {
                first = Integer.parseInt(phone.substring(0,1));
            }

            String f = String.valueOf( first);
//                int first = Integer.parseInt( f );
            String plus = "\u002B" ;

            if(phone.isEmpty())
            {
                et_phone.setError("enter mobile number");
                et_phone.requestFocus();
            }
            else if(!(phone.length()==10))
            {
                et_phone.setError("invalid mobile number");
                et_phone.requestFocus();
            }
            else if (first<6)
            {
                et_phone.setError("invalid mobile number");
                et_phone.requestFocus();

            }

            else if(f.equals( plus ))
            {
                et_phone.setError("invalid mobile number");
                et_phone.requestFocus();
            }
            else if(fName.isEmpty())
            {
                et_f_name.setError("Enter First Name");
                et_f_name.requestFocus();
            }
            else
            {
                if(ConnectivityReceiver.isConnected())
                {
                    String otp=getRandomKey(6);
                    if(type.equals("r"))
                    {
                        int status=0;
                        if(btn_yes.isChecked())
                        {
                            status=1;
                        }
                        else if(btn_no.isChecked())
                        {
                            status=0;
                        }

                        getVerificationCode(phone,fName+" "+lName,otp,status);

                    }
                }
                else
                {
                    Intent intent=new Intent(getActivity(), NoInternetConnection.class);
                    getActivity().startActivity(intent);
                }
            }

        }
    }

    public void getMessageStatus()
    {
        loadingBar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VERSTION_DATA, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try
                {
                    boolean sts=response.getBoolean("responce");

                    if(sts)
                    {
                        JSONObject object=response.getJSONObject("data");
                         msg_status=object.getString("msg_status");


                    }
                    else
                    {
                        Toast.makeText(getActivity(),""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request,json_tag);
    }
}
