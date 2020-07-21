package binplus.FoodWill.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import binplus.FoodWill.Adapter.Deal_OfDay_Adapter;
import binplus.FoodWill.Adapter.Home_Icon_Adapter;
import binplus.FoodWill.Adapter.Home_adapter;
import binplus.FoodWill.Adapter.Top_Selling_Adapter;
import binplus.FoodWill.Config.BaseURL;
import binplus.FoodWill.Config.Module;
import binplus.FoodWill.Model.Category_model;
import binplus.FoodWill.Model.Deal_Of_Day_model;
import binplus.FoodWill.Model.Home_Icon_model;
import binplus.FoodWill.Model.Top_Selling_model;
import binplus.FoodWill.AppController;
import binplus.FoodWill.CustomSlider;
import binplus.FoodWill.LoginActivity;
import binplus.FoodWill.MainActivity;
import binplus.FoodWill.PaymentActivities.InitialScreenActivity;
import binplus.FoodWill.R;
import binplus.FoodWill.util.ConnectivityReceiver;
import binplus.FoodWill.util.CustomVolleyJsonRequest;
import binplus.FoodWill.util.DatabaseCartHandler;
import binplus.FoodWill.util.LoadingBar;
import binplus.FoodWill.util.RecyclerTouchListener;
import binplus.FoodWill.util.Session_management;
import binplus.FoodWill.util.WishlistHandler;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static binplus.FoodWill.Config.BaseURL.KEY_ID;


public class Home_fragment extends Fragment {
    Module module;
    private static String TAG = Home_fragment.class.getSimpleName();
    private SliderLayout imgSlider, banner_slider, featuredslider;
    private RecyclerView new_products_recycler, rv_top_selling, rv_headre_icons;
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private boolean isSubcat = false;
    Session_management session_management;
    WishlistHandler db_wish;
    LinearLayout Search_layout;
    DatabaseCartHandler db_cart;
    String getid;
    String getcat_title;
    ScrollView scrollView;
    TextView footer ;
    Button click_here ;
    int version_code=0;
    String app_link="";
    SharedPreferences sharedpreferences;

    Animation animation;



    //Home Icons
    private Home_Icon_Adapter menu_adapter;
    private List<Home_Icon_model> menu_models = new ArrayList<>();
    private List<Home_Icon_model> catList = new ArrayList<>();


    //Deal O Day
    private Deal_OfDay_Adapter deal_ofDay_adapter;
    private List<Deal_Of_Day_model> deal_of_day_models = new ArrayList<>();
    LinearLayout Deal_Linear_layout;
    FrameLayout Deal_Frame_layout, Deal_Frame_layout1;


    //Top Selling Products
    private Top_Selling_Adapter top_selling_adapter;
    private List<Top_Selling_model> top_selling_models = new ArrayList<>();


    //  private ImageView iv_Call, iv_Whatspp, iv_reviews, iv_share_via;
    private TextView txt, timer;
    Button View_all_deals, View_all_TopSell;
    Dialog loadingBar;

    private ImageView Top_Selling_Poster, Deal_Of_Day_poster;

    View view;
    public Home_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate( R.layout.fragment_home, container, false);
//
        loadingBar=new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        loadingBar.setContentView( R.layout.progressbar );
        loadingBar.setCanceledOnTouchOutside(false);
        setHasOptionsMenu(true);
        module=new Module(getActivity());
        ((MainActivity) getActivity()).setTitle(getResources().getString( R.string.app_name));
        ((MainActivity) getActivity()).updateHeader();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        session_management=new Session_management(getActivity());
        db_cart=new DatabaseCartHandler(getActivity());
        db_wish=new WishlistHandler(getActivity());
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
        //Check Internet Connection
        if (ConnectivityReceiver.isConnected()) {

    getAppSettingData();

        }

        //  View_all_deals = (Button) view.findViewById(R.id.view_all_deals);
        //View_all_TopSell = (Button) view.findViewById(R.id.view_all_topselling);
        // Deal_Frame_layout = (FrameLayout) view.findViewById(R.id.deal_frame_layout);
        // Deal_Frame_layout1 = (FrameLayout) view.findViewById(R.id.deal_frame_layout1);
        //Deal_Linear_layout = (LinearLayout) view.findViewById(R.id.deal_linear_layout);


        //Top Selling Poster
        // Top_Selling_Poster = (ImageView) view.findViewById(R.id.top_selling_imageview);

        //Deal Of Day Poster
        //   Deal_Of_Day_poster = (ImageView) view.findViewById(R.id.deal_of_day_imageview);
        footer =(TextView) view.findViewById( R.id.bottomtxt );
        click_here = view.findViewById( R.id.bottombtn );

        //Scroll View
        scrollView = (ScrollView) view.findViewById( R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);

        //Search
//        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
//        Search_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binplus.Jabico.Fragment fm = new Search_fragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//        });
        //Slider
        imgSlider = (SliderLayout) view.findViewById( R.id.home_img_slider);
        banner_slider = (SliderLayout) view.findViewById( R.id.relative_banner);
       featuredslider = (SliderLayout) view.findViewById(R.id.featured_img_slider);


        //Catogary Icons
        //rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        // rv_items.setLayoutManager(gridLayoutManager);
        // rv_items.setItemAnimator(new DefaultItemAnimator());
        // rv_items.setNestedScrollingEnabled(false);

        //DealOf the Day
        new_products_recycler = (RecyclerView) view.findViewById( R.id.new_products_recycler);
        //  GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
//        new_products_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        new_products_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), HORIZONTAL,false));
        new_products_recycler.setItemAnimator(new DefaultItemAnimator());
        new_products_recycler.setNestedScrollingEnabled(false);
        new_products_recycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));


        //Top Selling Products
        rv_top_selling = (RecyclerView) view.findViewById( R.id.top_selling_recycler);
//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
//        rv_top_selling.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rv_top_selling.setLayoutManager(new LinearLayoutManager(getActivity(), HORIZONTAL,false));
        rv_top_selling.setItemAnimator(new DefaultItemAnimator());
        rv_top_selling.setNestedScrollingEnabled(false);
        rv_top_selling.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));



        //make_menu_items Icons
        rv_headre_icons = (RecyclerView) view.findViewById( R.id.collapsing_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),4) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation( LinearLayoutManager.VERTICAL);
        rv_headre_icons.setLayoutManager(new LinearLayoutManager(getActivity(), HORIZONTAL,false));
        rv_headre_icons.setHasFixedSize(false);
        rv_headre_icons.setItemViewCacheSize(10);
        rv_headre_icons.setDrawingCacheEnabled(true);
        rv_headre_icons.setDrawingCacheQuality( View.DRAWING_CACHE_QUALITY_LOW);
        rv_headre_icons.setItemAnimator(new DefaultItemAnimator());
        click_here.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(session_management.isLoggedIn())
                {
                    Fragment fm = new Help_Fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace( R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                }
                else
                {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                }


            }

        } );

//        //Call And Whatsapp
//        iv_Call = (ImageView) view.findViewById(R.id.iv_call);
//        iv_Whatspp = (ImageView) view.findViewById(R.id.iv_whatsapp);
//        iv_reviews = (ImageView) view.findViewById(R.id.reviews);
//        iv_share_via = (ImageView) view.findViewById(R.id.share_via);
//
//        iv_Call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             Intent intent = new Intent( Intent.ACTION_DIAL );
//             String number = "7617855680";
//             intent.setData( Uri.parse("tel:" +number) );
//             startActivity( intent );
//            }
//        });
//        iv_Whatspp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String smsNumber = "9889887711";
//                Intent sendIntent = new Intent("android.intent.action.MAIN");
//                sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
//                sendIntent.putExtra("jid",     PhoneNumberUtils.stripSeparators(smsNumber)+"@s.whatsapp.net");//phone number without "+" prefix
//                startActivity(sendIntent);
//
//            }
//        });
//        iv_reviews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reviewOnApp();
//            }
//        });
//        iv_share_via.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareApp();
//
//            }
//        });


        //Recycler View Shop By Catogary
      /*  rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = category_modelList.get(position).getId();
                getcat_title = category_modelList.get(position).getTitle();
                Bundle args = new Bundle();
                binplus.Jabico.Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/


        //Recycler View Menu Products
        rv_headre_icons.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_headre_icons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(menu_models.get(position).getStatus().equalsIgnoreCase("0"))
                {
                    module.showToast(getResources().getString(R.string.undelirable));
                }
                else {
                    getid = menu_models.get(position).getId();
                    String title = menu_models.get(position).getTitle();

                    String parent = menu_models.get(position).getCount();

                    if (parent.equals("0")) {
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

                    } else {
                        Bundle args = new Bundle();
                        Fragment fm = new SubCategory_Fragment();
                        args.putString("cat_id", getid);
                        args.putString("title", title);
                        // args.putString( "" );
                        // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();

                    }
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        //Recycler View Deal Of Day
//        rv_deal_of_day.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_deal_of_day, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                getid = deal_of_day_models.get(position).getId();
//                Bundle args = new Bundle();
//                binplus.Jabico.Fragment fm = new Product_fragment();
//                args.putString("cat_deal", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
//        View_all_deals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle args = new Bundle();
//                binplus.Jabico.Fragment fm = new Product_fragment();
//                args.putString("cat_deal", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//        });


        //REcyclerview Top Selling
//        rv_top_selling.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_top_selling, new RecyclerTouchListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                getid = top_selling_models.get(position).getProduct_id();
//                Bundle args = new Bundle();
//                binplus.Jabico.Fragment fm = new Product_fragment();
//                args.putString("cat_top_selling", "2");
//                fm.setArguments(args);
//               // String as=top_selling_models.get(position).getColor();
//                //Toast.makeText(getActivity(),""+as,Toast.LENGTH_LONG).show();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
//        View_all_TopSell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle args = new Bundle();
//                binplus.Jabico.Fragment fm = new Product_fragment();
//                args.putString("cat_top_selling", "2");
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//
//            }
//        });


        return view;
    }


    private void makeGetSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest( BaseURL.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("sliders", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("parent", jsonObject.getString("parent"));
                                url_maps.put("leval", jsonObject.getString("leval"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                imgSlider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        Bundle args = new Bundle();
                                        Fragment fm = null;
                                        args.putString("cat_id", sub_cat);
                                        args.putString("title",name.get("slider_title"));
                                        session_management.setCategoryId(sub_cat);
                                        if(name.get("parent").equals("0"))
                                        {
                                            fm=new SubCategory_Fragment();
                                        }
                                        else {
                                            //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                           fm = new Product_fragment();
                                        }
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(req);

    }

    private void makeGetBannerSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest( BaseURL.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("banner_id", jsonObject.getString("banner_id"));
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                url_maps.put("sub_cat",jsonObject.getString("sub_cat"));
                                url_maps.put("parent",jsonObject.getString("parent"));
                                url_maps.put("leval",jsonObject.getString("leval"));

                                //   Toast.makeText(context,""+modelList.get(position).getProduct_image(),Toast.LENGTH_LONG).show();

                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        String cat_id=name.get("sub_cat").toString();
                                        String cat_title=name.get("slider_title").toString();
                                        Bundle args = new Bundle();
                                        Fragment fm =null;
                                        args.putString("cat_id",cat_id);
                                        args.putString("title",cat_title);
                                        if(name.get("parent").equals("0"))
                                        {
                                          fm=new SubCategory_Fragment();
                                        }
                                        else
                                        {
                                            fm=new Product_fragment();
                                        }
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();

                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(req);

    }


  private void makeGetFeaturedSlider() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_FEAATURED_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_url", jsonObject.getString("slider_url"));
                                url_maps.put("slider_status", jsonObject.getString("slider_status"));

                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                featuredslider.addSlider(textSliderView);
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        Intent intent=new Intent(getActivity(), InitialScreenActivity.class);
                                        startActivity(intent);

                                    }
                                });
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
      AppController.getInstance().addToRequestQueue(req);

    }


    private void makeGetCategoryRequest() {
        loadingBar.show();
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/
        category_modelList.clear();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            loadingBar.dismiss();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Home_adapter(category_modelList);

                            //   rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

//    private void make_deal_od_the_day() {
//        String tag_json_obj = "json_category_req";
//        isSubcat = false;
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("parent", "");
//        isSubcat = true;
//       /* if (parent_id != null && parent_id != "") {
//        }*/
//
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
//                BaseURL.GET_DEAL_OF_DAY_PRODUCTS, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    if (response != null && response.length() > 0) {
//                        Boolean status = response.getBoolean("responce");
//                        Gson gson = new Gson();
//                        if (status) {
//                            Type listType = new TypeToken<List<Deal_Of_Day_model>>() {
//                            }.getType();
//                            deal_of_day_models = gson.fromJson(response.getString("Deal_of_the_day"), listType);
//                            deal_ofDay_adapter = new Deal_OfDay_Adapter(deal_of_day_models, getActivity());
//                            rv_deal_of_day.setAdapter(deal_ofDay_adapter);
//                            deal_ofDay_adapter.notifyDataSetChanged();
//                            if (getActivity() != null) {
//                                if (deal_of_day_models.isEmpty()) {
//                                    //  Toast.makeText(getActivity(), "No Deal For Day", Toast.LENGTH_SHORT).show();
//                                    rv_deal_of_day.setVisibility(View.GONE);
//                                    Deal_Frame_layout.setVisibility(View.GONE);
//                                    Deal_Frame_layout1.setVisibility(View.GONE);
//                                    Deal_Linear_layout.setVisibility(View.GONE);
//                                }
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
//                        }
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
//
//    }

    private void make_top_selling() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("top-sellingspdapsdp", response.toString());
                //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Top_Selling_model>>() {
                            }.getType();
                            top_selling_models = gson.fromJson(response.getString("top_selling_product"), listType);
                            top_selling_models.add(module.addExtraOneTopSelling());
                            top_selling_adapter = new Top_Selling_Adapter(getActivity(),top_selling_models);
                            rv_top_selling.setAdapter(top_selling_adapter);
                            top_selling_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void new_products() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_NEW_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d("sels", response.toString());
                //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Deal_Of_Day_model>>() {
                            }.getType();
                            deal_of_day_models = gson.fromJson(response.getString("new_product"), listType);
                            deal_of_day_models.add(module.addExtraOneNew());
                            deal_ofDay_adapter = new Deal_OfDay_Adapter(deal_of_day_models,getActivity());
                            new_products_recycler.setAdapter(deal_ofDay_adapter);
                            deal_ofDay_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void make_menu_items() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
        menu_models.clear();
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            catList = gson.fromJson(response.getString("data"), listType);
                            for(int i=0; i<catList.size();i++)
                            {
//                                if(catList.get(i).getStatus().equalsIgnoreCase("1"))
//                                {
                                    menu_models.add(catList.get(i));
//                                }

                            }
//                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Home_Icon_Adapter(menu_models);
                            rv_headre_icons.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent( Intent.ACTION_VIEW, uri);
        goToMarket.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent( Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction( Intent.ACTION_SEND);
        sendIntent.putExtra( Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
//    public  boolean isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("TAG","Permission is granted");
//                return true;
//            } else {
//
//                Log.v("TAG","Permission is revoked");
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG","Permission is granted");
//            return true;
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
//                    call_action();
//                } else {
//                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//    public void call_action(){
//
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + "919889887711"));
//        startActivity(callIntent);
//    }


    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
        getActivity().unregisterReceiver(mWish);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
        getActivity().registerReceiver(mWish, new IntentFilter("Grocery_wish"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    private BroadcastReceiver mWish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    private void updateData() {

        ((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());
        ((MainActivity) getActivity()).setWishCounter("" + db_wish.getWishtableCount(session_management.getUserDetails().get(KEY_ID)));
    }

    public boolean getUpdaterInfo()
    {
        boolean st=false;
        try
        {
            PackageInfo packageInfo=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            int ver_code=packageInfo.versionCode;
            if(ver_code == version_code)
            {
                st=true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return st;
    }

    public void getAppSettingData()
    {
        loadingBar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VERSTION_DATA, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("app_setting",response.toString());
                loadingBar.dismiss();
                try
                {
                    boolean sts=response.getBoolean("responce");

                    if(sts)
                    {
                        JSONObject object=response.getJSONObject("data");
                        version_code=Integer.parseInt(object.getString("app_version"));
                        app_link=object.getString("data");

                        if(getUpdaterInfo())
                        {
                                makeGetCategoryRequest();
                                makeGetSliderRequest();
                                make_menu_items();
                                makeGetFeaturedSlider();
                                makeGetBannerSliderRequest();
                                new_products();
                                //make_deal_od_the_day();
                                make_top_selling();


                        }
                        else
                        {

                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setCancelable(false);
                            builder.setMessage("The new version of app is available please update to get access.");
                            builder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    String url = app_link;
                                    Intent in = new Intent(Intent.ACTION_VIEW);
                                    in.setData(Uri.parse(url));
                                    startActivity(in);
                                    getActivity().finish();
                                    //Toast.makeText(getActivity(),"updating",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    getActivity().finishAffinity();
                                }
                            });
                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }


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
