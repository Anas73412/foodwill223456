package binplus.FoodWill.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import binplus.FoodWill.Config.BaseURL;
import binplus.FoodWill.Config.Module;
import binplus.FoodWill.Config.SharedPref;
import binplus.FoodWill.GifImageView;
import binplus.FoodWill.MainActivity;
import binplus.FoodWill.networkconnectivity.NetworkConnection;
import binplus.FoodWill.networkconnectivity.NetworkError;
import binplus.FoodWill.R;
import binplus.FoodWill.util.ConnectivityReceiver;
import binplus.FoodWill.util.DatabaseCartHandler;
import binplus.FoodWill.util.Session_management;
import binplus.FoodWill.util.WishlistHandler;

import static binplus.FoodWill.MainActivity.stop_order_image;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Reward_fragment extends Fragment {
  ImageView iv_img;
  public Reward_fragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.activity_reward_points, container, false);
    iv_img=view.findViewById(R.id.iv_img);
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


      if (ConnectivityReceiver.isConnected()) {
      Glide.with(getActivity())
              .load( BaseURL.IMG_EXTRA_URL + stop_order_image)
              .placeholder( R.drawable.icon)
              .crossFade()
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .dontAnimate()
              .into(iv_img);
    }

    iv_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Fragment  fm = new Help_Fragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                .addToBackStack(null).commit();
      }
    });
    return view;

  }


}