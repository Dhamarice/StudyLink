package zw.co.dhamarice.www.studylink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class StudyBuddy extends AppCompatActivity {


    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "My_Study_Link_Prefs";

    DatabaseHelper dbHandler = new DatabaseHelper(this);

    ImageView imgstore[] = new ImageView[1001];

    String post_id;
    String uname;
    String umail;
    String ucountry;
    String uinstitute;
    String fname;
    String uyear;
    String uprogram;
    String imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_buddy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        setuielements();


    }


    public void setuielements() {


        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.thelist);


        TextView noresult = (TextView) findViewById(R.id.txtinfo);

        //Drawable img = (Drawable) getResources().getDrawable(R.drawable.appliances);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);



        int i;
        dbHandler = new DatabaseHelper(this);
        //.dbHandler.async_data();



        if (dbHandler.getBuddies() != null) {
            Cursor cursor01 = dbHandler.getBuddies();

            if (cursor01 != null && cursor01.moveToFirst()) {

                Log.e("Product_list", "Values" + DatabaseUtils.dumpCursorToString(cursor01));


                int i2 = cursor01.getCount();
                for (i = 0; i < i2; i++) {
                    LinearLayout itmcontr = new LinearLayout(this);


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(10, 10, 10, 10);

                    itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                    itmcontr.setBackgroundColor(Color.WHITE);

                    imgstore[i] = new ImageView(this);
                    itmcontr.addView(imgstore[i]);

                    uname = cursor01.getString(3);
                    umail = cursor01.getString(0);
                    post_id = cursor01.getString(1);
                    imgurl = cursor01.getString(0);
                    ucountry = cursor01.getString(6);
                    uinstitute = cursor01.getString(3);
                    fname = cursor01.getString(0);
                    uprogram = cursor01.getString(1);
                    uyear = cursor01.getString(9);



                    //imageLoader.displayImage(imgurl, imgstore[i], options);
                    Picasso.with(this).load(imgurl)
                            .placeholder(R.drawable.progress_animation)
                            .error(R.drawable.placeholder_profile)
                            .into(imgstore[i]);
                    ViewGroup.LayoutParams imglayoutParams = imgstore[i].getLayoutParams();

                    int imglength = (int)(metrics.density *80);
                    int imgwidth = (int)(metrics.density * 80);


                    imglayoutParams.width = imglength;
                    imglayoutParams.height = imgwidth;

                    imgstore[i].setLayoutParams(imglayoutParams);

                    //Log.e("Setting Banner", imgurl);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout prdctinfo = new LinearLayout(this);
                    prdctinfo.setOrientation(LinearLayout.VERTICAL);

                    TextView UserName = new TextView(this);
                    TextView pre_text = new TextView(this);
                    UserName.setTypeface(null, Typeface.BOLD);
                    UserName.setTextSize(16);
                    UserName.setText(uname);
                    pre_text.setText(" Dhammy: Hie Rue please may I get that paper....");


                    LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
                    btnsize.setMargins(20, 0, 20, 0);
                    btnsize.gravity = Gravity.RIGHT;

                    prdctinfo.addView(UserName);
                    prdctinfo.addView(pre_text);

                    LinearLayout theBut = new LinearLayout(this);
                    theBut.setOrientation(LinearLayout.HORIZONTAL);




                    int length = (int)(metrics.density*100);
                    int width = (int)(metrics.density*35);
                    //}// else {
                    // AddToCart.setText("Auction Only");
                    // AddToCart.setOnClickListener(new View.OnClickListener() {

                    //  @Override
                    // public void onClick(View view) {
                    // do stuff
                    //  Toast.makeText( Products_List.this, "Auction Only", Toast.LENGTH_SHORT).show();
                    //}

                    //});
                    // }

                    // int length = (int)(metrics.density *100);
                    //int width = (int)(metrics.density * 35);


                    LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length,width,1);
                    btnsize2.setMargins(15, 0, 15, 0);


                    prdctinfo.addView(theBut);

                    prdctinfo.setLayoutParams(lparams);
                    itmcontr.addView(prdctinfo);
                    itmcontr.setId(Integer.parseInt(post_id));
                    itmcontr.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // do stuff
                            String id1 = Integer.toString(view.getId());
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("user_post_id", post_id);
                            editor.putString("user_name", uname);
                            editor.commit();
                            Intent i = new Intent(StudyBuddy.this, In_chat.class);
                            startActivity(i);
                        }

                    });
                    layout.addView(itmcontr, layoutParams);
                    cursor01.moveToNext();


                }
                noresult.setVisibility(View.GONE);

            } else {
                String msg="";


                    msg ="No Additional Chats to Display";

                noresult.setText(msg);
                Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
                noresult.setVisibility(View.VISIBLE);
            }
            noresult.setVisibility(View.GONE);
        }else {



            String msg="";

                msg ="No Additional Chats to Display";

            noresult.setText(msg);
            Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
            noresult.setVisibility(View.VISIBLE);
        }
    }








}
