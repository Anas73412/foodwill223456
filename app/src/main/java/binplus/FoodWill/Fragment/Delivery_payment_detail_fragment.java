package binplus.FoodWill.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import binplus.FoodWill.Config.BaseURL;
import binplus.FoodWill.Config.Module;
import binplus.FoodWill.Config.SharedPref;
import binplus.FoodWill.MainActivity;
import binplus.FoodWill.R;
import binplus.FoodWill.util.ConnectivityReceiver;
import binplus.FoodWill.util.DatabaseCartHandler;
import binplus.FoodWill.util.Session_management;

import static android.content.Context.MODE_PRIVATE;

import static binplus.FoodWill.MainActivity.delivery_msg;
import static binplus.FoodWill.MainActivity.extra_charges;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Delivery_payment_detail_fragment extends Fragment {

    private static String TAG = Delivery_payment_detail_fragment.class.getSimpleName();

    Module module;
    private TextView tv_timeslot, tv_address;
    private LinearLayout btn_order;

    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private String getstore_id = "";
    Dialog loadingBar ;
    TextView tvItems,tvMrp,tvDiscount,tvDelivary,tvSubTotal,tv_total ,tv_extra_charge;
    TextView reciver_name ,mobile_no ,pincode,house_no,society,txtNote ;
    private int deli_charges;
    Double total;
    SharedPreferences preferences;
    private DatabaseCartHandler db_cart;
    private Session_management sessionManagement;

    public Delivery_payment_detail_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment));
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);

        module=new Module(getActivity());
        db_cart = new DatabaseCartHandler(getActivity());
        sessionManagement = new Session_management(getActivity());

        //TextView tvItems,tvMrp,tvDiscount,tvDelivary,tvSubTotal;
        tv_timeslot = (TextView) view.findViewById(R.id.textTimeSlot);
        //tv_address = (TextView) view.findViewById(R.id.txtAddress);
        tvItems = (TextView) view.findViewById(R.id.tvItems);
        tvMrp = (TextView) view.findViewById(R.id.tvMrp);
        tv_extra_charge = (TextView) view.findViewById(R.id.tv_extra);
        tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
        tvDelivary = (TextView) view.findViewById(R.id.tvDelivary);
        tvSubTotal = (TextView) view.findViewById(R.id.tvSubTotal);
        txtNote = (TextView) view.findViewById(R.id.txtNote);
        //tv_total = (TextView) view.findViewById(R.id.textPrice);
        // tv_total = (TextView) view.findViewById(R.id.txtTotal);
        reciver_name =view.findViewById( R.id.recivername );
        mobile_no = view.findViewById( R.id.mobileno );
        pincode = view.findViewById( R.id.pincode );
        house_no = view.findViewById( R.id.Houseno );
        society = view.findViewById( R.id.Society );

        btn_order = (LinearLayout) view.findViewById(R.id.btn_order_now);

        getdate = getArguments().getString("getdate");

        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        if (language.contains("spanish")) {
            gettime = getArguments().getString("time");

            gettime=gettime.replace("PM","م");
            gettime=gettime.replace("AM","ص");

        }else {
            gettime = getArguments().getString("time");

        }
        getlocation_id = getArguments().getString("location_id");
        getstore_id = getArguments().getString("store_id");
        deli_charges = Integer.parseInt(getArguments().getString("deli_charges"));
        String name = getArguments().getString("name");
        String phone = getArguments().getString( "phone" );
        String house = getArguments().getString( "house" );
        String pin = getArguments().getString( "pin" );
        String societys = getArguments().getString( "society" );
        //   Toast.makeText( getActivity(),"charge"+deli_charges ,Toast.LENGTH_LONG ).show();

        tv_timeslot.setText(gettime);
        //tv_address.setText(getaddress);
        double extra_chrge = Double.parseDouble(extra_charges);
        total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges +extra_chrge;

//        tv_total.setText("" + db_cart.getTotalAmount());
        //  tv_item.setText("" + db_cart.getWishlistCount());
        reciver_name.setText( name );
        mobile_no.setText( phone );
        house_no.setText( house );
        pincode.setText( pin );
        society.setText( societys );
        tvItems.setText(String.valueOf(db_cart.getCartCount()));
        String mrp= getTotMRp();
        String price=String.valueOf(db_cart.getTotalAmount());
        tvMrp.setText(getResources().getString(R.string.currency)+mrp);
        tv_extra_charge.setText(getResources().getString(R.string.currency)+extra_charges);
        double m=Double.parseDouble(mrp);
        double p=Double.parseDouble(price);
        double d=m-p;

        tvDiscount.setText("-"+getResources().getString(R.string.currency)+String.valueOf(d));

        double db = (m-d)+deli_charges+ extra_chrge ;
        tvDelivary.setText(getResources().getString(R.string.currency)+deli_charges);
        tvSubTotal.setText(getResources().getString(R.string.currency)+db);
        //   tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
        //         getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
        //        getResources().getString(R.string.delivery_charge) + deli_charges + "\n" +
        //        getResources().getString(R.string.total_amount) +
        //       db_cart.getTotalAmount() + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));

        txtNote.setText(Html.fromHtml(delivery_msg));

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    Fragment fm = new Payment_fragment();
                    Bundle args = new Bundle();
                    args.putString("total", String.valueOf(total));
                    args.putString("getdate", getdate);
                    args.putString("gettime", gettime);
                    args.putString("deli_charges",String.valueOf(deli_charges));
                    args.putString("getlocationid", getlocation_id);
                    args.putString("getstoreid", getstore_id);
                    args.putString( "deli_charges", String.valueOf( deli_charges ) );

                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    SharedPref.putString(getActivity(),BaseURL.TOTAL_AMOUNT, String.valueOf(total));
                } else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

        return view;
    }

//    private void attemptOrder() {
//        // retrive data from cart database
//        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
//        if (items.size() > 0) {
//            JSONArray passArray = new JSONArray();
//            for (int i = 0; i < items.size(); i++) {
//                HashMap<String, String> map = items.get(i);
//                JSONObject jObjP = new JSONObject();
//                try {
//                    jObjP.put("product_id", map.get("product_id"));
//                    jObjP.put("qty", map.get("qty"));
//                    jObjP.put("unit_value", map.get("unit_value"));
//                    jObjP.put("unit", map.get("unit"));
//                    jObjP.put("price", map.get("price"));
//
//                    passArray.put(jObjP);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//
//            if (ConnectivityReceiver.isConnected()) {
//
//                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
//                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + "\ndata:" + passArray.toString());
//
//                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, passArray);
//            }
//        }
//    }

    /**
     * Method to make json object request where json response starts wtih
     */
//    private void makeAddOrderRequest(String date, String gettime, String userid, String location, JSONArray passArray) {
//        String tag_json_obj = "json_add_order_req";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("date", date);
//        params.put("time", gettime);
//        params.put("user_id", userid);
//        params.put("location", location);
//        params.put("data", passArray.toString());
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    Boolean status = response.getBoolean("responce");
//                    if (status) {
//
//                        String msg = response.getString("data");
//
////                        db_cart.clearCart();
////                        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getWishlistCount());
//                      //  Double total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;
//                        Bundle args = new Bundle();
//                        binplus.Jabico.Fragment fm = new Payment_fragment();
//                        args.putString("msg", msg);
//
//                        args.putString("total", String.valueOf(total));
//                        fm.setArguments(args);
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                .addToBackStack(null).commit();
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }
    public String getTotMRp()
    {
        ArrayList<HashMap<String, String>> list = db_cart.getCartAll();
        float sum=0;
        for(int i=0;i<list.size();i++)
        {
            final HashMap<String, String> map = list.get(i);

            float q=Float.parseFloat(map.get("qty"));
            float m=Float.parseFloat(map.get("mrp"));

            sum=sum+(q*m);
            //   Toast.makeText(getActivity(),""+q+"\n"+m,Toast.LENGTH_LONG).show();

        }
        if(sum!=0)
        {
            return String.valueOf(sum);
        }
        else
            return "0";
        //Toast.makeText(getActivity(),""+sum,Toast.LENGTH_LONG).show();
    }

}
