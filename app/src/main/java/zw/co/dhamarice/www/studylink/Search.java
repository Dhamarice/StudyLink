package zw.co.dhamarice.www.studylink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {


    private  String SEARCH_URL ="http://10.0.2.2:8012/studylink/search.php";

    private  String BUDDIE_URL ="http://10.0.2.2:8012/studylink/add_buddie.php";

    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "My_Study_Link_Prefs";

    SharedPreferences shared;

    EditText value;
    Button search;
    TextView byuser, byinstitute, byprogram;
    String sl_type, sl_value,user_id;

    String post_id;
    String uname;
    String umail;
    String ucountry;
    String uinstitute;
    String fname;
    String sname;
    String ucategory;
    String uindustry;
    String uprogram;
    String imgurl;

    private String TAG = "Search Activity";

    ImageView imgstore[] = new ImageView[1001];

    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        shared = this. getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

user_id = (shared.getString("userid", ""));


        value = (EditText) findViewById(R.id.searchterm);
        search = (Button) findViewById(R.id.btn_search);

        byuser = (TextView) findViewById(R.id.txtbyusername);
        byinstitute = (TextView) findViewById(R.id.txtbyinsti);
        byprogram = (TextView) findViewById(R.id.txtbyprogram);

        sl_type = "by username";
        value.setHint(sl_type);

        byuser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                value.setText("");

               sl_type = "by username";

                value.setHint(sl_type);


                Intent i = new Intent(Search.this, StudyBuddy.class);
                startActivity(i);

            }

        });


        byinstitute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                value.setText("");
                sl_type = "by institute";

                value.setHint(sl_type);



                Intent i = new Intent(Search.this, Login.class);
                startActivity(i);

            }

        });


        byprogram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                value.setText("");

                sl_type = "by program";

                value.setHint(sl_type);

            }

        });

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sl_value = value.getText().toString();

                searchMethod(sl_type, sl_value);

            }

        });


    }



    private void searchMethod(final String type, final String value){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, SEARCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + type);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    int success=jsonobject.getInt("success");
                    String message = jsonobject.getString("message");

                    if(success==1){



                        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.searchvw);

                        JSONArray array = jsonobject.getJSONArray("posts");

                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        int densityDpi = (int)(metrics.density * 160f);

                        for (int i = 0; i < array.length(); i++) {
                            try {

                                JSONObject object = array.getJSONObject(i);



                        //Drawable img = (Drawable) getResources().getDrawable(R.drawable.appliances);


                                    LinearLayout itmcontr = new LinearLayout(getBaseContext());


                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    layoutParams.setMargins(10, 10, 10, 10);

                                    itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                                    itmcontr.setBackgroundColor(Color.WHITE);

                                    imgstore[i] = new ImageView(getBaseContext());
                                    itmcontr.addView(imgstore[i]);




                                post_id = object.optString("id");
                                uname = object.optString("uname");
                                umail = object.optString("umail");
                                ucountry = object.optString("country");
                                uinstitute = object.optString("institute");
                                fname = object.optString("name");
                                sname = object.optString("surname");
                                ucategory = object.optString("category");
                                uindustry = object.optString("industry");
                                uprogram = object.optString("program");
                                 imgurl = object.optString("program");


                                    //imageLoader.displayImage(imgurl, imgstore[i], options);
                                    Picasso.with(getBaseContext()).load(imgurl)
                                            .placeholder(R.drawable.progress_animation)
                                            .error(R.drawable.placeholder_profile)
                                            .into(imgstore[i]);
                                    ViewGroup.LayoutParams imglayoutParams = imgstore[i].getLayoutParams();

                                    int imglength = (int)(metrics.density *80);
                                    int imgwidth = (int)(metrics.density * 80);


                                    imglayoutParams.width = imglength;
                                    imglayoutParams.height = imgwidth;

                                    imgstore[i].setLayoutParams(imglayoutParams);

                                    Log.e("Setting Banner", imgurl);
                                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    LinearLayout userinfo = new LinearLayout(getBaseContext());
                                userinfo.setOrientation(LinearLayout.VERTICAL);

                                    TextView userName = new TextView(getBaseContext());
                                    TextView fullname = new TextView(getBaseContext());
                                TextView tvprogram = new TextView(getBaseContext());
                                TextView tvinstitute = new TextView(getBaseContext());
                                TextView tvyear = new TextView(getBaseContext());
                                TextView tvcountry = new TextView(getBaseContext());
                                TextView tvemail = new TextView(getBaseContext());
                                userName.setText(uname);
                                fullname.setText(fname + " " + sname);
                                tvprogram.setText(uprogram + " :year 4");
                                tvinstitute.setText(uinstitute);
                                    //Price.setTextColor(getResources().getColor(R.color.colorAmber));
                                fullname.setTypeface(null, Typeface.BOLD);
                                fullname.setTextSize(13);

                                    LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
                                    btnsize.setMargins(20, 0, 20, 0);
                                    btnsize.gravity = Gravity.RIGHT;

                                userinfo.addView(userName);
                                userinfo.addView(fullname);
                                userinfo.addView(tvprogram);
                                userinfo.addView(tvinstitute);


                                    LinearLayout theBut = new LinearLayout(getBaseContext());
                                    theBut.setOrientation(LinearLayout.HORIZONTAL);

                                    Button AddToCart = new Button(getBaseContext());
                                    AddToCart.setText("Add Buddie");
                                    AddToCart.setTextColor(Color.BLACK);
                                    AddToCart.setTextSize(12);
                                    AddToCart.setId(Integer.parseInt(post_id));
                                    AddToCart.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {

                                            //display list..........


                                        }

                                    });



                                    int length = (int)(metrics.density*100);
                                    int width = (int)(metrics.density*35);



                                    LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length,width,1);
                                    btnsize2.setMargins(15, 0, 15, 0);

                                    AddToCart.setLayoutParams(btnsize2);
                                    //AddToCart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));



                                    //prdctinfo.setOrientation(LinearLayout.HORIZONTAL);

                                    //prdctinfo.addView(AddToCart);

                                userinfo.setLayoutParams(lparams);
                                    itmcontr.addView(userinfo);
                                    itmcontr.addView(AddToCart);
                                    itmcontr.setId(Integer.parseInt(post_id));
                                    itmcontr.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            new AlertDialog.Builder(Search.this)
                                                    .setTitle("Confirm Add")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            try {


                                                                db.fillbuddies(Integer.parseInt(post_id), uname, umail, fname + " " + sname, uinstitute, uprogram, "Fourth");


                                                                addbuddie(user_id, post_id);

                                                                Toast.makeText(getBaseContext(), ( uname + " Added!"), Toast.LENGTH_SHORT).show();


                                                            }
                                                            catch (Exception e){

                                                                Log.e(TAG, "onClick: Add Buddie ",e );


                                                                Toast.makeText(getBaseContext(), (uname + " is already your buddie"), Toast.LENGTH_SHORT).show();

                                                            }




                                                        }
                                                    })
                                                    .setNegativeButton("No", null)
                                                    .setMessage(Html.fromHtml("Add " + uname + " as your study buddie?"))
                                                    .show();

                                        }

                                    });
                                    layout.addView(itmcontr, layoutParams);


                                }

                            catch (JSONException e){


                                Log.e(TAG, "onResponse: ", e );
                            }

                            }


                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
                Toast.makeText(getBaseContext(), "Please check your Intenet and try again!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("type",type);
                values.put("value",value);


                return values;
            }



        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);




    }



    private void addbuddie(final String user_id, final String buddie_id){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BUDDIE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + user_id);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    int success=jsonobject.getInt("success");
                    String message = jsonobject.getString("message");
                    if(success==1){



                        JSONArray array=jsonobject.getJSONArray("posts");

                        JSONObject object=array.getJSONObject(0);






                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                volleyError.printStackTrace();
                Log.e("RUEERROR",""+volleyError);
                Toast.makeText(getBaseContext(), "Please check your Intenet and try again!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> values=new HashMap();
                values.put("user_id",user_id);
                values.put("user_buddie",buddie_id);


                return values;
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map<String,String> headers=new HashMap();
//                headers.put("Accept","application/json");
//                headers.put("Content-Type","application/json");
//                return headers;
//            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //requestQueue.add(stringRequest);




    }




}
