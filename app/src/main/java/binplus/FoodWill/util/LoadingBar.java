package binplus.FoodWill.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.LightsLoader;

import net.bohush.geometricprogressview.GeometricProgressView;

import binplus.FoodWill.R;



/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,April,2020
 */
public class LoadingBar {
    Context context;
    Dialog dialog;
   LinearLayout rel_loader;
    GeometricProgressView  light_Loader ;


    public LoadingBar(Context context) {
        this.context = context;

        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.progessindicator);
        dialog.setCanceledOnTouchOutside(false);



    }
    public void show()

    {
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        dialog.show();
    }

    public void dismiss()
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();

        }
    }

    public boolean isShowing()
    {
       return dialog.isShowing();
    }
}
