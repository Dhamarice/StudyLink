package zw.co.dhamarice.www.studylink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class In_chat extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final String MyPREFERENCES = "My_Study_Link_Prefs";
    DatabaseHelper dbHandler = new DatabaseHelper(this);

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabaseUsers;


    LinearLayout layout;

    private String TAG = "In_Chat";

    EditText type;
    ImageView send;
    TextView header;
    ScrollView chats;

    String Text, post_id, user_id, textdate, textmessage, dbto, dbfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_in_chat);
            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);

            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Messages");


            mAuth = FirebaseAuth.getInstance();

            mAuthStateListener = new FirebaseAuth.AuthStateListener(){


                @Override

                public  void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null) {

                        Intent intent = new Intent(getBaseContext(), Login.class);
                        startActivity(intent);
                    }
                }


            };

            layout = (LinearLayout) findViewById(R.id.chat_scroll);
            chats = (ScrollView) findViewById(R.id.scroll);

            type = (EditText) findViewById(R.id.type_in);
            send = (ImageView) findViewById(R.id.sendimage);
            header = (TextView) findViewById(R.id.txt_details);


            user_id = (shared.getString("userid", ""));


            post_id = (shared.getString("user_post_id", ""));
            header.setText((shared.getString("user_name", "")));
            header.setTypeface(null, Typeface.BOLD);
            header.setTextColor(Color.WHITE);
            header.setTextSize(18);


            send.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mFirebaseUser = mAuth.getCurrentUser();
                    mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mFirebaseUser.getUid());

                    if (!type.getText().toString().contentEquals("")) {

                        Text = type.getText().toString();


                        dbHandler.fillchats(user_id, post_id, Text);

                        final  DatabaseReference newPost = mDatabaseReference.push();

                        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                newPost.child("Content").setValue(Text);
                                newPost.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                        layout.removeAllViews();

                        type.setText("");

                        loadchats();


                    }
                }

            });


            loadchats();

        }
        catch (Exception e){

            Log.e(TAG, "onCreate: ", e );
        }

    }


    public void loadchats() {


        final SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);




        //Drawable img = (Drawable) getResources().getDrawable(R.drawable.appliances);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int) (metrics.density * 160f);


        int i;
        dbHandler = new DatabaseHelper(this);
        //.dbHandler.async_data();
        post_id = (shared.getString("user_post_id", ""));


        if (dbHandler.getChats(post_id) != null) {
            Cursor cursor01 = dbHandler.getChats(post_id);

            if (cursor01 != null && cursor01.moveToFirst()) {

                Log.e("Product_list", "Values" + DatabaseUtils.dumpCursorToString(cursor01));


               final int i2 = cursor01.getCount();
                for (i = 0; i < i2; i++) {
                    LinearLayout itmcontr = new LinearLayout(this);


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    layoutParams.setMargins(50, 40, 50, 40);

                    itmcontr.setOrientation(LinearLayout.HORIZONTAL);
                    itmcontr.setBackgroundColor(Color.WHITE);


                    //itmcontr.addView(imgstore[i]);

                    textdate = cursor01.getString(1);
                    textmessage = cursor01.getString(4);
                    dbto = cursor01.getString(0);
                    dbfrom = cursor01.getString(1);


                    int imglength = (int) (metrics.density * 80);
                    int imgwidth = (int) (metrics.density * 80);


                    //Log.e("Setting Banner", imgurl);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout prdctinfo = new LinearLayout(this);
                    prdctinfo.setOrientation(LinearLayout.VERTICAL);

                    TextView UserName = new TextView(this);
                    TextView pre_text = new TextView(this);
                    pre_text.setTypeface(null, Typeface.BOLD);
                    pre_text.setTextSize(16);
                    UserName.setText(textdate);
                    pre_text.setText(textmessage);


                    LinearLayout.LayoutParams btnsize = new LinearLayout.LayoutParams(200, 60);
                    btnsize.setMargins(20, 0, 20, 0);
                    btnsize.gravity = Gravity.RIGHT;

                    prdctinfo.addView(UserName);
                    prdctinfo.addView(pre_text);

                    LinearLayout theBut = new LinearLayout(this);
                    theBut.setOrientation(LinearLayout.HORIZONTAL);

                    if (dbfrom == user_id){

                        itmcontr.setBackgroundResource(R.drawable.default_rounded);

                    }
                    else {

                        itmcontr.setBackgroundResource(R.drawable.default_rounded_purple);

                    }



                    int length = (int) (metrics.density * 100);
                    int width = (int) (metrics.density * 35);
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


                    LinearLayout.LayoutParams btnsize2 = new LinearLayout.LayoutParams(length, width, 1);
                    btnsize2.setMargins(15, 0, 15, 0);


                    prdctinfo.addView(theBut);

                    prdctinfo.setLayoutParams(lparams);
                    itmcontr.addView(prdctinfo);


                    layout.addView(itmcontr, layoutParams);
                    chats.postDelayed(new Runnable() { @Override public void run() { chats.scrollTo(i2, i2);} }, 1000);

                    //layout.fullScroll(View.FOCUS_DOWN);
                    cursor01.moveToNext();


                }


            }
        }


    }

    public void setcontent(String content, TextView message_content){

message_content.setText(content);

    }

    /*
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Message, In_chat>
    }
    */
}