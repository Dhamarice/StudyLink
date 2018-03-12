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
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private  String LOGIN_URL ="http://10.0.2.2:8012/studylink/login.php";
    //private  String LOGIN_URL ="http://localhost:8012/studylink/login.php";
    public static final String MyPREFERENCES = "My_Study_Link_Prefs";
    private String TAG = "Login Activity";

    private ProgressDialog pDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

SharedPreferences shared;
private Button signup, fgtpswd, login;
    private  String sl_uname, sl_pword;
    EditText etusername, etpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shared = this. getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Authenticating...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        etusername = (EditText) findViewById(R.id.username);
        etpassword = (EditText) findViewById(R.id.password);




        signup = (Button) findViewById(R.id.btnSignUp);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i =  new Intent(Login.this, SignUp.class);

                startActivity(i);

            }

        });

        login  = (Button) findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(etusername.getText().toString().contentEquals("")){

                    Toast.makeText(getBaseContext(), ("Please enter your username!"), Toast.LENGTH_SHORT).show();
                }

                else if(etpassword.getText().toString().contentEquals("")){

                    Toast.makeText(getBaseContext(), ("Please enter your password!"), Toast.LENGTH_SHORT).show();

                }
else {
                    sl_uname = etusername.getText().toString();
                    sl_pword = etpassword.getText().toString();

                    Log.e(TAG, "onClick: login" + "Password: " + sl_pword + "Username: " + sl_uname);

                    loginMethod(sl_uname, sl_pword);

                }

            }

        });

        }


    private void loginMethod(final String uname, final String pword){
        com.android.volley.RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                pDialog.dismiss();

                Log.e("Success",""+s);
                Log.e("Zitapass", "" + pword);
                //{"success":1,"message":"Username Successfully Added!"}

                try {
                    JSONObject jsonobject=new JSONObject(s);
                    int success=jsonobject.getInt("success");
                   final String message = jsonobject.getString("message");
                    if(success==1){



                        JSONArray array=jsonobject.getJSONArray("posts");

                        JSONObject object=array.getJSONObject(0);





                        String post_id = object.optString("id");
                        String usname = object.optString("uname");
                        String umail = object.optString("umail");
                        String ucountry = object.optString("country");
                        String uinstitute = object.optString("institute");
                        String fname = object.optString("name");
                        String sname = object.optString("surname");
                        String ucategory = object.optString("category");
                        String uindustry = object.optString("industry");
                        String uprogram = object.optString("program");


                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("userid", post_id);
                        editor.putString("uname", usname);
                        editor.putString("umail", umail);
                        editor.putString("ucountry", ucountry);
                        editor.putString("uinstitute", uinstitute);
                        editor.putString("Fname", fname);
                        editor.putString("sname", sname);
                        editor.putString("category", ucategory);
                        editor.putString("industry", uindustry);
                        editor.putString("program", uprogram);
                        editor.commit();
                        editor.apply();

                        //zvese.setVisibility(View.VISIBLE);
                        //input_container.setVisibility(View.GONE);
                        //inside.setVisibility(View.VISIBLE);


//FIREBASE LOGIN



                        new Thread() {
                            public void run() {
                                while (true) {
                                    try {


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            mAuth.signInWithEmailAndPassword(uname, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {

                                                    if(task.isSuccessful()){

                                                        final String user_id = mAuth.getCurrentUser().getUid();
                                                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                if(dataSnapshot.hasChild(user_id)){

                                                                    Toast.makeText(getBaseContext(), (message), Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(getBaseContext(), StudyBuddy.class);
                                                                    startActivity(intent);
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
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
                values.put("username",uname);
                values.put("password",pword);


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


