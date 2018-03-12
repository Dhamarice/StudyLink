package zw.co.dhamarice.www.studylink;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class SignUp extends AppCompatActivity {

    private ProgressDialog pDialog;

    private static final String LEVELS_URL = "http://10.0.2.2:8012/studylink/getcategories.php";
    private static final String INDUSTRIES_URL = "http://10.0.2.2:8012/studylink/getindustries.php";
    private static final String PROGRAMS_URL = "http://10.0.2.2:8012/studylink/getprograms.php";
    private static final String REGISTER_URL = "http://10.0.2.2:8012/studylink/register.php";

    public static final String MyPREFERENCES = "My_Study_Link_Prefs";

    private Spinner country, level, program, industry;
    private EditText efirstname, esurname, eusername, epassword, eemail, einstitute,confmpass ;
    private Button signup;
    SharedPreferences shared;
    String sl_country, sl_level, sl_Industry, sl_program, sl_fname, sl_uname, sl_sname,sl_passwrd, sl_email, sl_institute, sl_econfmpass;

    private  String TAG = "SignUp class";

private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shared = this. getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);


        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        efirstname = (EditText) findViewById(R.id.firstname);
        esurname = (EditText) findViewById(R.id.surname);
        eusername = (EditText) findViewById(R.id.suusername);
        epassword = (EditText) findViewById(R.id.supassword);
        eemail = (EditText) findViewById(R.id.email);
        einstitute = (EditText) findViewById(R.id.institute);
        confmpass = (EditText) findViewById(R.id.supasswordcnf);

        signup = (Button) findViewById(R.id.btnsignupp);


//DISPLAYING COUNTRIES

        country = (Spinner) findViewById(R.id.countryspinner);


        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);
        // set the view for the Drop down list
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the ArrayAdapter to the spinner
        country.setAdapter(dataAdapter);
        country.setSelection(246);

        System.out.println("# countries found: " + countries.size());

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                final String newValue = (String) country.getItemAtPosition(position);

                sl_country = newValue;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });





        program = (Spinner) findViewById(R.id.programspinner);
        program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                final String newprogram = (String) program.getItemAtPosition(position);

                sl_program = newprogram;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        level = (Spinner) findViewById(R.id.levelspinner);
getlevels(level);
        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                final String newlevel = (String) level.getItemAtPosition(position);

                sl_level = newlevel;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });





        industry = (Spinner) findViewById(R.id.industryspinner);
        getindustries(industry);

        industry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                final String newIndustry = (String) industry.getItemAtPosition(position);

                sl_Industry = newIndustry;
getprograms(program, sl_Industry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });







        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                /*

                if(efirstname.getText().toString().contentEquals("")  || eusername.getText().toString().contentEquals("") || esurname.getText().toString().contentEquals("") ||
                        epassword.getText().toString().contentEquals("")  || eemail.getText().toString().contentEquals("") ||
                        einstitute.getText().toString().contentEquals("")  || confmpass.getText().toString().contentEquals("") ) {
                    Toast.makeText(SignUp.this, "Please fill all fields!", Toast.LENGTH_LONG).show();
                }

else if(!epassword.getText().toString().contentEquals(confmpass.getText().toString()) ) {
                    Toast.makeText(SignUp.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
                }

else {
*/
                    sl_fname = efirstname.getText().toString();
                    sl_uname = eusername.getText().toString();
                    sl_sname = esurname.getText().toString();
                    sl_passwrd = epassword.getText().toString();
                    sl_email = eemail.getText().toString();
                    sl_institute = einstitute.getText().toString();
                    sl_econfmpass = confmpass.getText().toString();

                    register(sl_fname, sl_sname, sl_uname, sl_passwrd, sl_level, sl_country, sl_institute, sl_email, sl_Industry, sl_program);


               // }

            }
        });




    }




    //Method to get all levels
    public void getlevels(final Spinner inlevel){


        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, LEVELS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);

                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");


                        List<String> spinnerArray = new ArrayList<String>();

                        if (array != null) {
                            try {


                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject object = array.getJSONObject(i);

                                    String level = object.optString("name");


                                    spinnerArray.add(level);
                                }
                            } catch (JSONException e) {
                                Log.e("Cities JSON Error: ", e.toString());
                                e.printStackTrace();
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            inlevel.setAdapter(adapter);
                            inlevel.setSelection(2);

                        } else {
                            Toast.makeText(getBaseContext(), ("Connection error..."), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                catch (JSONException e) {
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


    //Method to get all Industries
    public void getindustries(final Spinner inindustry){


        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, INDUSTRIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);

                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");


                        List<String> spinnerArray = new ArrayList<String>();

                        if (array != null) {
                            try {


                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject object = array.getJSONObject(i);

                                    String level = object.optString("name");


                                    spinnerArray.add(level);
                                }
                            } catch (JSONException e) {
                                Log.e("Cities JSON Error: ", e.toString());
                                e.printStackTrace();
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            inindustry.setAdapter(adapter);
                            inindustry.setSelection(2);

                        } else {
                            Toast.makeText(getBaseContext(), ("Connection error..."), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                catch (JSONException e) {
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



    //Method to get all Industries
    public void getprograms(final Spinner inprogram, final String the_industry){

        Log.e("getprogram",""+"getProgram called" + the_industry);
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, PROGRAMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);

                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    int success = jsonobject.getInt("success");
                    if (success == 1) {


                        JSONArray array = jsonobject.getJSONArray("posts");


                        List<String> spinnerArray = new ArrayList<String>();

                        if (array != null) {
                            try {


                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject object = array.getJSONObject(i);

                                    String level = object.optString("name");


                                    spinnerArray.add(level);
                                }
                            } catch (JSONException e) {
                                Log.e("Cities JSON Error: ", e.toString());
                                e.printStackTrace();
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getBaseContext(), android.R.layout.simple_spinner_item, spinnerArray);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            inprogram.setAdapter(adapter);
                            inprogram.setSelection(2);

                        } else {
                            Toast.makeText(getBaseContext(), ("Connection error..."), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                catch (JSONException e) {
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
                values.put("the_industry",the_industry);


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




    private void register(final String fname, final String surname,final String uname, final String pword, final String category, final String country, final String institute, final String email, final String industry,  final String program ){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                //Log.e("Zitapass", "" + pword);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    int success=jsonobject.getInt("success");
                   final String message = jsonobject.getString("message");
                    if(success==1) {


//FIREBASE REGISTRATION
                        new Thread() {
                            public void run() {
                                while (true) {
                                    try {


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                mAuth.createUserWithEmailAndPassword(sl_uname, sl_passwrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        if (task.isSuccessful()){

                                                            String user_id = mAuth.getCurrentUser().getUid();
                                                            DatabaseReference current_user_db = mDatabaseReference.child(user_id);
                                                            current_user_db.child("Name").setValue(sl_uname);


                                                            Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(getBaseContext(), Login.class);
                                                            startActivity(intent);


                                                        }

                                                    }
                                                });
                                            }
                                        });
                                        Thread.sleep(1000);

                                    }

                                    catch (Exception e){

                                        Log.e(TAG, "Thread Exception run: ",e );
                                    }
                                }
                            }
                        }.start();








                    }

                    else{
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
                values.put("firstname",fname);
                values.put("surname",surname);
                values.put("username",uname);
                values.put("password",pword);
                values.put("category",category);
                values.put("country",country);
                values.put("institute",institute);
                values.put("email",email);
                values.put("industry",industry);
                values.put("program",program);



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


