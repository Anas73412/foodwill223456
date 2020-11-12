package binplus.foodiswill.Config;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import binplus.foodiswill.Model.Product_model;
import binplus.foodiswill.R;
import binplus.foodiswill.util.DatabaseCartHandler;
import binplus.foodiswill.util.WishlistHandler;

public class Module {

    Context context;

    public Module(Context context) {
        this.context = context;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public String VolleyErrorMessage(VolleyError error)
    {
        String str_error ="";
        if (error instanceof TimeoutError) {
            str_error="Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="An Unknown error occur";
        }else if(error instanceof NoConnectionError){
            str_error="No Internet Connection";
        }

        return str_error;
    }

    public void preventMultipleClick(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }
    public void onShakeImage(RelativeLayout rel_variant, Context context) {
        Animation shake;
        shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(200);
        rel_variant.startAnimation(shake); // starts animation
    }

    public void addToCart(Context context,String cart_id,String product_id,String product_image,String cat_id,String product_name,String price,String unit_price,String unit,String mrp,String stock,String type,float qty)
    {
        DatabaseCartHandler db_cart=new DatabaseCartHandler(context);
        HashMap<String, String> mapProduct = new HashMap<String, String>();
        mapProduct.put("cart_id",cart_id);
        mapProduct.put("product_id", product_id);
        mapProduct.put("product_image", product_image);
        mapProduct.put("cat_id", cat_id);
        mapProduct.put("product_name", product_name);
        mapProduct.put("price", price);
        mapProduct.put("unit_price", unit_price);
        mapProduct.put("unit", unit);
        mapProduct.put("mrp", mrp);
        mapProduct.put("stock", stock);
        mapProduct.put("type", type);
        try {

            boolean tr = db_cart.setCart(mapProduct, qty);
            if (tr == true) {
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            } else if (tr == false) {
                Toast.makeText(context, "Cart Updated", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void addToWishlist(Context context ,String product_id ,String product_images ,String cat_id ,String product_name ,String price,
                              String product_desc ,String rewards ,String unit_value ,String unit ,String increment ,String stock , String in_stock,
                              String title ,String mrp,String product_attribute,String user_id,String hndName)

    {
       WishlistHandler db_wish = new WishlistHandler( context );
        HashMap<String, String> mapProduct = new HashMap<String, String>();
        mapProduct.put( "product_id",product_id );
        mapProduct.put( "product_images",product_images );
        mapProduct.put( "cat_id", cat_id );
        mapProduct.put( "product_name",product_name );
        mapProduct.put( "price",price );
        mapProduct.put( "product_description",product_desc );
        mapProduct.put( "rewards",rewards);
        mapProduct.put("user_id",user_id);
        mapProduct.put( "unit_value",unit_value );
        mapProduct.put( "unit", unit );
        mapProduct.put( "increment",increment );
        mapProduct.put( "stock",stock );
        mapProduct.put( "title",title);
        mapProduct.put( "mrp", mrp );
        mapProduct.put( "product_attribute",product_attribute );
        mapProduct.put( "in_stock", in_stock );
        mapProduct.put( "product_hindi_name", hndName );

        try {

            boolean tr =db_wish.setwishTable(mapProduct);
            if (tr == true) {

                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                Toast.makeText(context, "Added to Wishlist" , Toast.LENGTH_SHORT).show();
                updatewishintent();



            }
            else
            {
                Toast.makeText(context, "Something Went Wrong" , Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //  Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void showToast(String s)
    {
        Toast.makeText(context,""+s,Toast.LENGTH_SHORT).show();
    }

    public void updatewishintent() {
        Intent updates = new Intent("Grocery_wish");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }

    public Product_model addExtraOneTopSelling()
    {
        Product_model m=new Product_model();
        m.setProduct_id("00");
        m.setProduct_name("Shop By Categories");
        m.setProduct_name_hindi("");
        m.setCategory_id("01");
        m.setProduct_description("");
        m.setPrice("0");
        m.setProduct_attribute("[]");
        m.setMrp("0");
        m.setProduct_image("[\"shop_by_category.png\"]");
        m.setIn_stock("1");
        m.setUnit_value("0");
        m.setUnit("KG");
        m.setRewards("0");
        m.setStock("100");
        m.setTitle("Shop By Category");
        return m;
    }
    public Product_model addExtraOneNew()
    {
        Product_model m=new Product_model();
        m.setProduct_id("00");
        m.setProduct_name("Shop By Categories");
        m.setProduct_name_hindi("");
        m.setCategory_id("01");
        m.setProduct_description("");
        m.setPrice("0");
        m.setProduct_attribute("[]");
        m.setMrp("0");
        m.setProduct_image("[\"shop_by_category.png\"]");
        m.setIn_stock("1");
        m.setUnit_value("0");
        m.setUnit("KG");
        m.setRewards("0");
        m.setStock("100");
        m.setTitle("Shop By Category");
        return m;
    }

    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            context.startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            context.startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }


}
