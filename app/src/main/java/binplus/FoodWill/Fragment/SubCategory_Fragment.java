package binplus.FoodWill.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binplus.FoodWill.Adapter.Home_Icon_Adapter;
import binplus.FoodWill.Adapter.Shop_Now_adapter;
import binplus.FoodWill.Config.BaseURL;
import binplus.FoodWill.Config.Module;
import binplus.FoodWill.Model.Home_Icon_model;
import binplus.FoodWill.AppController;
import binplus.FoodWill.MainActivity;
import binplus.FoodWill.R;
import binplus.FoodWill.util.ConnectivityReceiver;
import binplus.FoodWill.util.CustomVolleyJsonRequest;
import binplus.FoodWill.util.RecyclerTouchListener;
import binplus.FoodWill.util.Session_management;

public class SubCategory_Fragment extends Fragment {

    Module module;
    private static String TAG = SubCategory_Fragment.class.getSimpleName();
    private RecyclerView rv_items;
    private List<Home_Icon_model> category_modelList = new ArrayList<>();
    private Shop_Now_adapter adapter;
    Session_management session_management;
    private boolean isSubcat = false;
    String getid;
    String getcat_title;
    TextView firebase;
    ImageView img_no_product;
    Dialog loadingBar;
    Home_Icon_Adapter home_icon_adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemt_sub_categories, container, false);
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module(getActivity());
        setHasOptionsMenu(true);
        firebase=view.findViewById(R.id.firebase);
        img_no_product=view.findViewById(R.id.img_no_product);
        session_management=new Session_management(getActivity());
        String getcat_id = getArguments().getString("cat_id");
        String getcat_name = getArguments().getString("title");

        ((MainActivity) getActivity()).setTitle(getcat_name);


        if (ConnectivityReceiver.isConnected()) {
            //     makeSubGetCategoryRequest();
            makeGetCategoryRequest(getcat_id);

        }

        rv_items = (RecyclerView) view.findViewById(R.id.rv_sub);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_items.setLayoutManager(gridLayoutManager);
        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);
        rv_items.setItemViewCacheSize(10);
        rv_items.setDrawingCacheEnabled(true);
        rv_items.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(category_modelList.get(position).getStatus().equalsIgnoreCase("0"))
                {
                    module.showToast(getResources().getString(R.string.undelirable));
                }
                else {

                    getid = category_modelList.get(position).getId();
                    String title = category_modelList.get(position).getTitle();
                    Bundle args = new Bundle();
                    Fragment fm = new Product_fragment();
                    args.putString("cat_id", getid);
                    args.putString("title", title);
                    session_management.setCategoryId(getid);
                    // args.putString( "" );
                    // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;

    }

    private void makeGetCategoryRequest(final String parent_id) {
        loadingBar.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response from category", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        loadingBar.dismiss();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Home_Icon_model>>() {}.getType();
                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        if (category_modelList.size()>0) {
                            rv_items.setVisibility(View.VISIBLE);

                            home_icon_adapter=new Home_Icon_Adapter(category_modelList);
                            rv_items.setAdapter(home_icon_adapter);

                            home_icon_adapter.notifyDataSetChanged();

                        } else {
                            img_no_product.setVisibility(View.VISIBLE);
                            rv_items.setVisibility(View.GONE);
                            firebase.setVisibility(View.GONE);

                        }

                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
