
package binplus.foodiswill.Adapter;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import binplus.foodiswill.Config.BaseURL;
import binplus.foodiswill.Config.Module;
import binplus.foodiswill.Fragment.Details_Fragment;
import binplus.foodiswill.LoginActivity;
import binplus.foodiswill.Model.ProductVariantModel;
import binplus.foodiswill.Model.Product_model;
import binplus.foodiswill.R;
import binplus.foodiswill.util.DatabaseCartHandler;
import binplus.foodiswill.util.DatabaseHandler;
import binplus.foodiswill.util.Session_management;
import binplus.foodiswill.util.WishlistHandler;

import static android.content.Context.MODE_PRIVATE;
import static binplus.foodiswill.Config.BaseURL.KEY_ID;


public class Product_adapter extends RecyclerView.Adapter<Product_adapter.MyViewHolder> {

    List<String> image_list;
    ProductVariantAdapter productVariantAdapter;
    private List<Product_model> modelList;
    private Context context;
    int status=0;
    String user_id ;

    private DatabaseHandler dbcart;
    private DatabaseCartHandler db_cart;
    WishlistHandler db_wish;
    String language;
    Module module;
SharedPreferences preferences;
Session_management sessionManagement ;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price,product_mrp ,discount;
        public ImageView iv_logo, wish_before ,wish_after;
        public ConstraintLayout con_product;
        public RelativeLayout rel_variant;
        TextView txtrate;
        ElegantNumberButton elegantNumberButton;
        private TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
        Button add ;
        WishlistHandler db_wish;
        int list_position=-1;
        RelativeLayout rel_click,rel_out;

        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
           // tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
          //  tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            product_mrp = (TextView) view.findViewById(R.id.product_mrp);
            discount= view.findViewById( R.id.dis );
            rel_click=view.findViewById(R.id.rel_click);
            add = view.findViewById( R.id.btn_add );
            wish_after=view.findViewById( R.id.wish_after );
            wish_before=view.findViewById( R.id.wish_before );
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            rel_variant=(RelativeLayout)view.findViewById(R.id.rel_variant);
            dialog_unit_type=(TextView)view.findViewById(R.id.unit_type);
            dialog_txtId=(TextView)view.findViewById(R.id.txtId);
            dialog_txtVar=(TextView)view.findViewById(R.id.txtVar);
            rel_out=(RelativeLayout)view.findViewById(R.id.rel_out);
            txtrate=(TextView)view.findViewById(R.id.single_varient);
            con_product=view.findViewById(R.id.con_layout_product);
            elegantNumberButton=view.findViewById( R.id.elegantButton );
            image_list=new ArrayList<>();
            module=new Module(context);
            db_wish=new WishlistHandler(context);
            wish_before.setOnClickListener(this);
            wish_after.setOnClickListener(this );
            rel_click.setOnClickListener(this);
           // rel_variant.setOnClickListener(this);
            add.setOnClickListener(this);
            db_wish = new WishlistHandler( context );

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            final int position = getAdapterPosition();

                if(id==R.id.rel_click)
            {
                Details_Fragment details_fragment=new Details_Fragment();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle args = new Bundle();
                args.putString("cat_id", modelList.get(position).getCategory_id());
                args.putString("product_id",modelList.get(position).getProduct_id());
                args.putString("product_images",modelList.get(position).getProduct_image());
                args.putString("product_name",modelList.get(position).getProduct_name());
                args.putString("product_description",modelList.get(position).getProduct_description());
                args.putString("stock",modelList.get(position).getStock());
                args.putString("in_stock",modelList.get(position).getIn_stock());
                args.putString("price",modelList.get(position).getPrice());
                args.putString("mrp",modelList.get(position).getMrp());
                args.putString("unit_value",modelList.get(position).getUnit_value());
                args.putString("unit",modelList.get(position).getUnit());
                args.putString("product_attribute",modelList.get(position).getProduct_attribute());
                args.putString("rewards",modelList.get(position).getRewards());
                args.putString("increment",modelList.get(position).getIncreament());
                args.putString("title",modelList.get(position).getTitle());
                details_fragment.setArguments(args);
                FragmentManager fragmentManager=activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel,details_fragment)
                        .addToBackStack(null).commit();

            }

//            else if(id==R.id.wish_before) {
//
//                    final Product_model mList = modelList.get(position);
//                    wish_after.setVisibility( View.VISIBLE );
//                    wish_before.setVisibility( View.GONE );
//
//                    module.addToWishlist(context,mList.getProduct_id(),mList.getProduct_image(),mList.getCategory_id(),mList.getProduct_name(), mList.getPrice(),
//                            mList.getProduct_description(),mList.getRewards(),mList.getUnit_value(),mList.getUnit(),mList.getIncreament(),mList.getStock(),mList.getIn_stock(),
//                            mList.getTitle(),mList.getMrp(),mList.getProduct_attribute(),user_id  );
//
//
//            }
//            else if (id == R.id.wish_after) {
//                    final Product_model mList = modelList.get(position);
//                wish_after.setVisibility( View.GONE );
//                wish_before.setVisibility( View.VISIBLE );
//               db_wish.removeItemFromWishtable(mList.getProduct_id(),user_id);
//               Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_LONG).show();
//                    updatewishlistintent();
//
//                    // list.remove(position);
//              notifyDataSetChanged();
//
//            }


        }
    }

    public Product_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(context);
        db_cart=new DatabaseCartHandler(context);
    }

    @Override
    public Product_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);
        context = parent.getContext();
        return new Product_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Product_adapter.MyViewHolder holder, final int position) {
        final ArrayList<ProductVariantModel> variantList;
        variantList=new ArrayList<>();

        final Product_model mList = modelList.get(position);
        final String getid = mList.getProduct_id();
        sessionManagement = new Session_management( context );
        user_id=sessionManagement.getUserDetails().get(KEY_ID);
        if( mList.getIn_stock().equals("0"))
        {
            holder.rel_out.setVisibility(View.VISIBLE);
            holder.wish_before.setVisibility(View.GONE);
        }
        else
        {
            holder.rel_out.setVisibility(View.GONE);
         }


        if(db_wish.isInWishtable( getid ,user_id ))
        {
            holder.wish_after.setVisibility( View.VISIBLE );
            holder.wish_before.setVisibility( View.GONE );
        }
        else
        {
            holder.wish_after.setVisibility( View.GONE );
            holder.wish_before.setVisibility( View.VISIBLE );
        }

        try
        {
            image_list.clear();
            JSONArray array=new JSONArray(mList.getProduct_image());
            //Toast.makeText(this,""+product_images,Toast.LENGTH_LONG).show();
            if(mList.getProduct_image().equals(null))
            {
                Glide.with(context)
                        .load(BaseURL.IMG_PRODUCT_URL +mList.getProduct_image() )
                        .fitCenter()
                        .placeholder(R.drawable.icon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_logo);
            }
            else
            {
                for(int i=0; i<=array.length()-1;i++)
                {
                    image_list.add(array.get(i).toString());

                }


                Glide.with(context)
                        .load(BaseURL.IMG_PRODUCT_URL +image_list.get(0) )
                        .fitCenter()
                                  .placeholder(R.drawable.icon)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.iv_logo);
            }


            // Toast.makeText(Product_Frag_details.this,""+image_list.toString(),Toast.LENGTH_LONG).show();

        }
        catch (Exception ex)
        {
            // Toast.makeText(Product_Frag_details.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
//


        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.tv_title.setText(mList.getProduct_name());
        }
        else {
            holder.tv_title.setText(mList.getProduct_name());


        }

        holder.product_mrp.setPaintFlags( holder.product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        String atr=String.valueOf(mList.getProduct_attribute());
        if(atr.equals("[]"))
        {
         status=1;

            String p=String.valueOf(mList.getPrice());
            String m=String.valueOf(mList.getMrp());
            int mm = Integer.parseInt( m );
            int pp =Integer.parseInt( p );
            holder.tv_price.setText(context.getResources().getString(R.string.currency)+ mList.getPrice());
            if(mm>pp) {
                holder.product_mrp.setText( context.getResources().getString( R.string.currency ) + mList.getMrp() );
                int discount = getDiscount( p, m );
                if(discount<=0)
                {
                    holder.discount.setVisibility(View.GONE);
                }
                else
                {
                    holder.discount.setVisibility(View.VISIBLE);
                    holder.discount.setText( "" + discount + "% "+"\nOFF" );

                }
                //Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
              //  holder.discount.setText( "" + discount + "% OFF" );
                holder.product_mrp.setVisibility( View.VISIBLE );
                //holder.discount.setVisibility( View.VISIBLE );
            }
            else
            {
                holder.product_mrp.setVisibility( View.GONE );
                holder.discount.setVisibility( View.GONE );
            }
            holder.txtrate.setVisibility(View.VISIBLE);
            holder.rel_variant.setVisibility(View.GONE);
            holder.txtrate.setText(mList.getUnit_value()+" "+mList.getUnit());

        }

        else
        {

            status=2;
            holder.rel_variant.setVisibility(View.VISIBLE);
//            String atr=String.valueOf(mList.getProduct_attribute());
            JSONArray jsonArr = null;
            try {
                jsonArr = new JSONArray(atr);
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    ProductVariantModel model=new ProductVariantModel();
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    model.setId(jsonObj.getString("id"));
                    model.setProduct_id(jsonObj.getString("product_id"));
                    model.setAttribute_value(jsonObj.getString("attribute_value"));
                    model.setAttribute_name(jsonObj.getString("attribute_name"));
                    model.setAttribute_mrp(jsonObj.getString("attribute_mrp"));
                    variantList.add(model);
                    //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            try
            {
                String atr_price=String.valueOf(variantList.get(0).getAttribute_value());
                String atr_mrp=String.valueOf(variantList.get(0).getAttribute_mrp());
                int atr_m =Integer.parseInt( atr_mrp );
                int atr_p =Integer.parseInt( atr_price );
                holder.tv_price.setText("\u20B9"+variantList.get(0).getAttribute_value().toString());
                if (atr_m>atr_p) {
                    int atr_dis = getDiscount( atr_price, atr_mrp );
                    if(atr_dis<=0)
                    {
                        holder.discount.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.discount.setVisibility(View.VISIBLE);
                        holder.discount.setText( "" + atr_dis + "%"+"\n OFF" );

                    }
                   // holder.discount.setText( "" + atr_dis + "% OFF" );

                    holder.product_mrp.setText( "\u20B9" + variantList.get(0).getAttribute_mrp().toString() );
                }
                else
                {
                    holder.discount.setVisibility( View.GONE );
                    holder.product_mrp.setVisibility( View.GONE );
                }
              holder.dialog_unit_type.setText("\u20B9"+variantList.get(0).getAttribute_value()+"/"+variantList.get(0).getAttribute_name());

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


        }

        final String product_id=String.valueOf(mList.getProduct_id());
        if(atr.equals("[]"))
        {
            boolean st=db_cart.isInCart(product_id);
            if(st==true)
            {
                holder.add.setVisibility(View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.add.setVisibility(View.VISIBLE);
               // holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility(View.GONE);
            }

        }
        else
        {
            String at_id=String.valueOf(variantList.get(0).getId());
            boolean st=db_cart.isInCart(at_id);
            if(st)
            {
                holder.list_position=0;
                holder.add.setVisibility(View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                holder.elegantNumberButton.setVisibility(View.VISIBLE);


            }
            else {
                holder.add.setVisibility(View.VISIBLE);
                holder.elegantNumberButton.setVisibility(View.GONE);
                //list_position=-1;

            }
           // Toast.makeText(context,"sz- "+variantList.size()+"\n "+list_position,Toast.LENGTH_SHORT).show();

        }


        holder.rel_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Product_model mList = modelList.get(position);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row=layoutInflater.inflate(R.layout.dialog_vairant_layout,null);
                String atr=String.valueOf(mList.getProduct_attribute());
//                JSONArray jsonArr = null;
//                variantList.clear();
//                try {
//
//                    jsonArr = new JSONArray(atr);
//                    for (int i = 0; i < jsonArr.length(); i++)
//                    {
//                        ProductVariantModel model=new ProductVariantModel();
//                        JSONObject jsonObj = jsonArr.getJSONObject(i);
//                        String atr_id=jsonObj.getString("id");
//                        String atr_product_id=jsonObj.getString("product_id");
//                        String attribute_name=jsonObj.getString("attribute_name");
//                        String attribute_value=jsonObj.getString("attribute_value");
//                        String attribute_mrp=jsonObj.getString("attribute_mrp");
//
//
//                        model.setId(atr_id);
//                        model.setProduct_id(atr_product_id);
//                        model.setAttribute_value(attribute_value);
//                        model.setAttribute_name(attribute_name);
//                        model.setAttribute_mrp(attribute_mrp);
//                variantList.add(model);
//
//                        //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));
//
//                        //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();
//                    }
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                ListView l1=(ListView)row.findViewById(R.id.list_view_varaint);
                productVariantAdapter=new ProductVariantAdapter(context,variantList);
                //productVariantAdapter.notifyDataSetChanged();
                l1.setAdapter(productVariantAdapter);


                builder.setView(row);
                final AlertDialog ddlg=builder.create();
                ddlg.show();
                l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        holder.list_position=i;
                        holder.dialog_unit_type.setText("\u20B9"+variantList.get(i).getAttribute_value()+"/"+variantList.get(i).getAttribute_name());
                        String pr=String.valueOf(variantList.get(i).getAttribute_value());
                        String mr=String.valueOf(variantList.get(i).getAttribute_mrp());
                        int m =Integer.parseInt( mr );
                        int p = Integer.parseInt( pr );

                        holder.tv_price.setText("\u20B9"+variantList.get(i).getAttribute_value().toString());
                        if (m>p) {
                            holder.product_mrp.setText( "\u20B9" + variantList.get( i ).getAttribute_mrp().toString() );

                            int atr_dis = getDiscount( pr, mr );
                            if(atr_dis<=0)
                            {
                                holder.discount.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.discount.setVisibility(View.VISIBLE);
                                holder.discount.setText( "" + atr_dis + "%"+"\n OFF" );

                            }
                            //holder.discount.setText( "" + atr_dis + "% OFF" );
                        }
                        else {
                            holder.product_mrp.setVisibility(View.GONE);
                            holder.discount.setVisibility(View.GONE);
                        }
                        String atr=String.valueOf(modelList.get(position).getProduct_attribute());
                        String product_id=String.valueOf(modelList.get(position).getProduct_id());
                        if(atr.equals("[]"))
                        {
                            boolean st=db_cart.isInCart(product_id);
                            if(st==true)
                            {
                                holder.add.setVisibility(View.GONE);
                                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                                holder.elegantNumberButton.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                holder.add.setVisibility(View.VISIBLE);
                                //  holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                                holder.elegantNumberButton.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            String at_id=String.valueOf(variantList.get(holder.list_position).getId());
                            boolean st=db_cart.isInCart(at_id);
                            if(st==true)
                            {
                                holder.add.setVisibility(View.GONE);
                                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                                holder.elegantNumberButton.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                holder.add.setVisibility(View.VISIBLE);
                                holder.elegantNumberButton.setVisibility(View.GONE);
                            }
                        }

                        ddlg.dismiss();
                    }
                });




            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product_model mList=modelList.get(position);
                if(mList.getIn_stock().equals("0"))
                {
                    Toast.makeText(context,"Out Of Stock",Toast.LENGTH_SHORT).show();
                }
                else {

                    String atr = String.valueOf(modelList.get(position).getProduct_attribute());
                    if (atr.equals("[]")) {
                        String unt = String.valueOf(mList.getUnit_value() + " " + mList.getUnit());
                        module.addToCart(context,mList.getProduct_id(),mList.getProduct_id(),mList.getProduct_image(),
                                mList.getCategory_id(),mList.getProduct_name(),mList.getPrice(),mList.getPrice(),unt,
                                mList.getMrp(),mList.getStock(),"p",(float) 1);
                        updateintent();
                        holder.add.setVisibility(View.GONE);
                        holder.elegantNumberButton.setNumber("1");
                        holder.elegantNumberButton.setVisibility(View.VISIBLE);
                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {
                        //ProductVariantModel model=variantList.get(position);

                        if(holder.list_position==-1)
                        {
                            module.onShakeImage(holder.rel_variant,context);
                        }
                        else
                        {
                            String st0=String.valueOf(variantList.get(holder.list_position).getAttribute_value());
                            String st1=String.valueOf(variantList.get(holder.list_position).getAttribute_name());
                            String st2=String.valueOf(variantList.get(holder.list_position).getAttribute_mrp());
                            String at_id=String.valueOf(variantList.get(holder.list_position).getId());
                            module.addToCart(context,at_id,mList.getProduct_id(),mList.getProduct_image(),
                                    mList.getCategory_id(),mList.getProduct_name(),st0,st0,st1,
                                    st2,mList.getStock(),"a",(float) 1);

                            updateintent();
                            holder.add.setVisibility(View.GONE);
                            holder.elegantNumberButton.setNumber("1");
                            holder.elegantNumberButton.setVisibility(View.VISIBLE);

                        }


                    }

                }
            }
        });

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                String atr=String.valueOf(modelList.get(position).getProduct_attribute());
                if(newValue<=0)
                {

                    boolean st=checkAttributeStatus(atr);
                    if(st==false)
                    {
                        db_cart.removeItemFromCart(product_id);

                    }
                    else if(st==true)
                    {

                        String at_id = String.valueOf(variantList.get(holder.list_position).getId());
                        db_cart.removeItemFromCart(at_id);
                        holder.list_position=-1;

                    }

                    holder.elegantNumberButton.setVisibility(View.GONE);
                    holder.add.setVisibility(View.VISIBLE);
                    updateintent();
                }
                else {


                    final Product_model mList = modelList.get(position);
                    float qty = Float.parseFloat(String.valueOf(newValue));

                    String satr = String.valueOf(modelList.get(position).getProduct_attribute());
                    if (satr.equals("[]")) {
                        double pr = Double.parseDouble(mList.getPrice());
                        double amt = pr * qty;
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf(mList.getUnit_value() + " " + mList.getUnit());
                        module.addToCart(context,mList.getProduct_id(),mList.getProduct_id(),mList.getProduct_image(),
                                mList.getCategory_id(),mList.getProduct_name(),String.valueOf(amt),mList.getPrice(),unt,
                                mList.getMrp(),mList.getStock(),"p",(float) qty);
                        updateintent();
                    } else {
                        //ProductVariantModel model=variantList.get(position);

                        String st0 = String.valueOf(variantList.get(holder.list_position).getAttribute_value());
                        String st1 = String.valueOf(variantList.get(holder.list_position).getAttribute_name());
                        String st2 = String.valueOf(variantList.get(holder.list_position).getAttribute_mrp());
                        String at_id = String.valueOf(variantList.get(holder.list_position).getId());
                        double pr = Double.parseDouble(st0);
                        double amt = pr * qty;
//                        Toast.makeText(context,"st0:-"+st0+"\nst1-"+st1+"\n st2 :-"+st2+"\n at_id:- "+at_id+"\n at_id1:- "+at_id1,Toast.LENGTH_LONG).show();
                        module.addToCart(context, at_id, mList.getProduct_id(), mList.getProduct_image(),
                                mList.getCategory_id(), mList.getProduct_name(), String.valueOf(amt), st0, st1,
                                st2, mList.getStock(), "a", (float) qty);

                        updateintent();
                    }
                    }


            }
        });


        //holder.tv_reward.setText(mList.getRewards());
        //holder.tv_price.setText(context.getResources().getString(R.string.currency)+ mList.getPrice()+" / "+ mList.getUnit_value()+" "+mList.getUnit());



//        holder.tv_price.setText(context.getResources().getString(R.string.tv_pro_price) + mList.getUnit_value() + " " +
//                mList.getUnit() +" \n"+ mList.getPrice()+ context.getResources().getString(R.string.currency));
//        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
//        Double prices = Double.parseDouble(mList.getPrice());
//        Double reward = Double.parseDouble(mList.getRewards());
//        //holder.tv_total.setText("" + price * items);
     //  holder.tv_reward.setText("" + reward * items);

        holder.wish_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
//                    float stck=Float.parseFloat(modelList.get(position).getStock());
//                    if (stck<1 || modelList.get(position).getIn_stock().equals("0"))
//                    {
//                        Toast.makeText( context,"Out Of Stock",Toast.LENGTH_LONG ).show();
//                    }
//                    else
//                    {
                    holder.wish_after.setVisibility( View.VISIBLE );
                    holder.wish_before.setVisibility( View.INVISIBLE );

                    final Product_model mList = modelList.get( position );
                    module.addToWishlist(context,mList.getProduct_id(),mList.getProduct_image(),mList.getCategory_id(),mList.getProduct_name(),mList.getPrice(),
                            mList.getProduct_description(),mList.getRewards(),mList.getUnit_value(),mList.getUnit(),mList.getIncreament(),mList.getStock(),
                            mList.getIn_stock(),mList.getTitle(),mList.getMrp(),mList.getProduct_attribute(),user_id,mList.getProduct_name_hindi());

                }
                else
                {
                    Intent intent = new Intent( context, LoginActivity.class );
                    context.startActivity( intent );
                }

            }
        });

        holder.wish_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Product_model mList = modelList.get(position);
                holder.wish_after.setVisibility( View.GONE );
                holder.wish_before.setVisibility( View.VISIBLE );
                db_wish.removeItemFromWishtable(mList.getProduct_id(),user_id);
                Toast.makeText(context, "Removed from Wishlist", Toast.LENGTH_LONG).show();
                updatewishlistintent();

                // list.remove(position);
                notifyDataSetChanged();

            }
        });


//       holder.tv_add.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//               int sd=db_cart.getCartCount();
//
//               Toast.makeText(context,""+sd,Toast.LENGTH_LONG).show();
//
//           }
//       });

//       holder.con_product.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//
//         int y=dbcart.getCartCount();
//          //     Toast.makeText(context,""+y,Toast.LENGTH_LONG).show();
//
//
//               Bundle args = new Bundle();
//
//               Intent intent=new Intent(context, Product_details.class);
//               args.putString("cat_id",mList.getCategory_id());
//               args.putString("product_id",mList.getProduct_id());
//               args.putString("product_images",mList.getProduct_image());
//
//               args.putString("product_name",mList.getProduct_name());
//               args.putString("product_description",mList.getProduct_description());
//               args.putString("product_in_stock",mList.getIn_stock());
//               args.putString("product_size",mList.getSize());
//               args.putString("product_color",mList.getColor());
//               args.putString("product_price",mList.getPrice());
//               args.putString("product_mrp",mList.getIncreament());
//               args.putString("product_unit_value",mList.getUnit_value());
//               args.putString("product_unit",mList.getUnit());
//               args.putString("product_rewards",mList.getRewards());
//               args.putString("product_increament",mList.getIncreament());
//               args.putString("product_title",mList.getTitle());
//
//               Product_Frag_details product_frag_details=new Product_Frag_details();
//
//               product_frag_details.setArguments(args);
//               AppCompatActivity activity=(AppCompatActivity) view.getContext();
//               activity.getSupportFragmentManager().beginTransaction().replace(R.id.contentPanel,product_frag_details)
//                       .addToBackStack(null)
//                       .commit();
//
//           }
//      });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }




    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
        int d=(int)df;
       return d;
    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }
    private void updatewishlistintent() {
        Intent updates = new Intent("Grocery_wish");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }
        public boolean checkAttributeStatus(String atr)
        {
            boolean sts=false;
            if(atr.equals("[]"))
            {
                sts=false;
            }
            else
            {
                sts=true;
            }
            return sts;
        }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}