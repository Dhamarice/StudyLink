package zw.co.dhamarice.www.studylink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {



            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);



            sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            mWaitHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    //The following code will execute after the 5 seconds.



                        try {


                            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstrun", true);


                            Log.e("ISFIRST RUN IS  ", "Equals " + isFirstRun);


                            if (isFirstRun) {
                                // do the thing for the first time


                                Log.e("Spalshscreen  ", "Starting Main Activity");
                                Intent intent = new Intent(SplashScreen.this, Login.class);
                                startActivity(intent);

                            } else {

                                Log.e("Spalshscreen  ", "Starting Main Activity");
                                Intent intent = new Intent(SplashScreen.this, Login.class);
                                startActivity(intent);


                            }

                            //Let's Finish Splash Activity since we don't want to show this when user press back button.
                            finish();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }, 5000);  // Give a 5 seconds delay.
            }

            @Override
            public void onDestroy() {
                super.onDestroy();

                //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
                mWaitHandler.removeCallbacksAndMessages(null);
            }
        }






