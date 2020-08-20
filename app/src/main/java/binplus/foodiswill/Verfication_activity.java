package binplus.foodiswill;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import binplus.foodiswill.Fragment.*;
import android.view.View;
import android.widget.TextView;

public class Verfication_activity extends AppCompatActivity {
  TextView back ;

  String type="";

  Dialog loadingBar ;

  @Override
  protected void attachBaseContext(Context newBase) {



    newBase = LocaleHelper.onAttach(newBase);
    super.attachBaseContext(newBase);
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verfication_activity);

    loadingBar=new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
    loadingBar.setContentView( R.layout.progressbar );
    loadingBar.setCanceledOnTouchOutside(false);

    back = findViewById( R.id.txt_reg_back );
    back.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    } );
    type=getIntent().getStringExtra("type");
    Fragment fm=new Otp_Generate_Fragment();
    Bundle bundle=new Bundle();
    bundle.putString("type",type);
    fm.setArguments(bundle);
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().add(R.id.varify_container, fm)
            .commit();


  }


}
