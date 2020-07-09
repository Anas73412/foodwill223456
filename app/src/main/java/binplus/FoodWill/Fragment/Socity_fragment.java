package binplus.FoodWill.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import binplus.FoodWill.Adapter.SocietyAdapter;
import binplus.FoodWill.Adapter.Socity_adapter;
import binplus.FoodWill.Config.BaseURL;
import binplus.FoodWill.Config.Module;
import binplus.FoodWill.Model.Socity_model;
import binplus.FoodWill.AppController;
import binplus.FoodWill.MainActivity;
import binplus.FoodWill.R;
import binplus.FoodWill.util.ConnectivityReceiver;
import binplus.FoodWill.util.CustomVolleyJsonArrayRequest;
import binplus.FoodWill.util.RecyclerTouchListener;
import binplus.FoodWill.util.Session_management;

import static binplus.FoodWill.Config.BaseURL.KEY_SOCITY_ID;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Socity_fragment extends Fragment {
  private static String TAG = Socity_fragment.class.getSimpleName();

  private AutoCompleteTextView et_search;
  private RecyclerView rv_socity;
  ArrayList<Socity_model> filteredList=new ArrayList<>();
  TextView tv_view_all;
  private ArrayList<Socity_model> socity_modelList = new ArrayList<>();
  private Socity_adapter adapter;
  ProgressDialog progressDialog ;
  Module module;
  Dialog loadingBar ;

  public Socity_fragment() {
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
    View view = inflater.inflate(R.layout.fragment_socity, container, false);
    loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
    loadingBar.setContentView( R.layout.progressbar );
    loadingBar.setCanceledOnTouchOutside(false);
    module=new Module(getActivity());
    //String getpincode = getArguments().getString("pincode");


    tv_view_all=(TextView)view.findViewById(R.id.tv_view_all);
    et_search = (AutoCompleteTextView) view.findViewById(R.id.et_socity_search);
    et_search.setThreshold(1);
    et_search.setAdapter(new SocietyAdapter(getActivity(),et_search.getText().toString()));
    et_search.setTextColor(getResources().getColor(R.color.green));
    rv_socity = (RecyclerView) view.findViewById(R.id.rv_socity);
    rv_socity.setLayoutManager(new LinearLayoutManager(getActivity()));

    tv_view_all.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        makeGetSocityRequest();


      }
    });
    et_search.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //   adapter.getFilter().filter(charSequence);
      }

      @Override
      public void afterTextChanged(Editable editable) {
        filter(editable.toString());
      }
    });

    // check internet connection
    if (ConnectivityReceiver.isConnected()) {
      makeGetSocityRequest();
    } else {
      ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
    }

    rv_socity.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_socity, new RecyclerTouchListener.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {

        String socity_id = filteredList.get(position).getSocity_id();
        String socity_name = filteredList.get(position).getSocity_name();
        Session_management sessionManagement = new Session_management(getActivity());
        sessionManagement.updateSocity(socity_name,socity_id);
        ((MainActivity) getActivity()).onBackPressed();
      }

      @Override
      public void onLongItemClick(View view, int position) {
      }
    }));

    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  private void filter(String s) {

    filteredList.clear();
    ArrayList<Socity_model> tempList=new ArrayList<>();
    for(Socity_model socity_model : socity_modelList)
    {
      if(socity_model.getSocity_name().toLowerCase().contains(s.toLowerCase().toString()))
      {
        tempList.add(socity_model);
      }
    }
    filteredList.addAll(tempList);
    adapter.filterList(filteredList);
  }

  private void filterAll() {

    filteredList.clear();
    ArrayList<Socity_model> tempList=new ArrayList<>();
    for(Socity_model socity_model : socity_modelList)
    {
        tempList.add(socity_model);

    }
    filteredList.addAll(tempList);
    adapter.filterList(filteredList);
  }

  /**
   * Method to make json array request where json response starts wtih
   */
  private void makeGetSocityRequest() {
    loadingBar.show();
    // Tag used to cancel the request
    String tag_json_obj = "json_socity_req";

    CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.GET,
            BaseURL.GET_SOCITY_URL, null, new Response.Listener<JSONArray>() {

      @Override
      public void onResponse(JSONArray response) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Socity_model>>() {
        }.getType();

        socity_modelList = gson.fromJson(response.toString(), listType);
        filteredList = gson.fromJson(response.toString(), listType);
        adapter = new Socity_adapter(socity_modelList);
        rv_socity.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(socity_modelList.isEmpty()){
          if(getActivity() != null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
          }
        }
        loadingBar.dismiss();
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.d(TAG, "Error: " + error.getMessage());
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
          if(getActivity() != null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
          }
        }
      }
    });

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
  }

}