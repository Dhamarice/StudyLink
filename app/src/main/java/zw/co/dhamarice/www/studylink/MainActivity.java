package zw.co.dhamarice.www.studylink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Tricks.ViewPagerEx;

public class MainActivity extends AppCompatActivity  implements
        ViewPagerEx.OnPageChangeListener,NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private String TAG = "MainActivity";

    ImageView studybuddie, studyroom, mycourses, discussions, share, classroom, mycalender, pastpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //toolbar.setLogo(R.drawable.hammerimageedited);
            setSupportActionBar(toolbar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();



            TabLayout tab = (TabLayout) findViewById(R.id.tabs);
            tab.setVisibility(View.GONE);



            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(MainActivity.this);

            studybuddie = (ImageView) findViewById(R.id.studybuddy);
            studybuddie.setOnClickListener(this);

            studyroom = (ImageView) findViewById(R.id.studyroom);
            studyroom.setOnClickListener(this);

            mycourses = (ImageView) findViewById(R.id.imgmycourses);
            mycourses.setOnClickListener(this);

            discussions = (ImageView) findViewById(R.id.imageDiscussion);
            discussions.setOnClickListener(this);

            share = (ImageView) findViewById(R.id.imageshare);
            share.setOnClickListener(this);

            classroom = (ImageView) findViewById(R.id.imageclass);
            classroom.setOnClickListener(this);

            mycalender = (ImageView) findViewById(R.id.imagecalender);
            mycalender.setOnClickListener(this);

            pastpaper = (ImageView) findViewById(R.id.imagepapers);
            pastpaper.setOnClickListener(this);


            studybuddie.setImageResource(R.drawable.study_buddies01);
            studyroom.setImageResource(R.drawable.study_room01);
            mycourses.setImageResource(R.drawable.mycourses);
            discussions.setImageResource(R.drawable.discussions);
            share.setImageResource(R.drawable.share);
            classroom.setImageResource(R.drawable.classroom01);
            mycalender.setImageResource(R.drawable.my_calender);
            pastpaper.setImageResource(R.drawable.past_papers);



    }
        catch(
    Exception e)

    {

        Log.e(TAG, "onCreate: ", e);
    }

}
    public void onClick (View v) {
        // TODO Auto-generated method stub
        String id1 = null;
        SharedPreferences.Editor editor = null;
        Intent intent;
        System.gc();
        switch (v.getId()) {
            case R.id.studybuddy:

                intent = new Intent(MainActivity.this, StudyBuddy.class);
                startActivity(intent);

                break;

            case R.id.studyroom:

                //finish();
                break;


            case R.id.imageDiscussion:

                break;

            case R.id.imageclass:

                break;

            case R.id.imagecalender:
                break;


            case R.id.imagepapers:

                break;

            case R.id.imageshare:

                break;

            case R.id.imgmycourses:

                break;

            default:
                Toast.makeText(this, v.toString() + "Clicked (Invalid Object Call", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


    @Override
    protected void onSaveInstanceState(Bundle outState){}

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.myaccount) {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        } else if (id == R.id.cart) {
            Intent intent = new Intent(MainActivity.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.chat) {
            Intent intent = new Intent(MainActivity.this, Chat_Webview.class);
            startActivity(intent);
        } else if (id == R.id.mytransactions) {
            Intent intent = new Intent(MainActivity.this, TransactionHistory.class);
            startActivity(intent);
        }
        else if (id == R.id.myfavourites) {
            Intent intent = new Intent(MainActivity.this, Favourites.class);
            startActivity(intent);
        }
        else if (id == R.id.stores) {
            Intent intent = new Intent(MainActivity.this, StoresFragment.class);
            startActivity(intent);
        } else if (id == R.id.category) {
            Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);

        }  else if (id == R.id.about_us) {
            Intent intent = new Intent(MainActivity.this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.contact_us) {
            Intent intent = new Intent(MainActivity.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.terms) {
            Intent intent = new Intent(MainActivity.this, TermsAndConditions.class);
            startActivity(intent);
        }
        else if (id == R.id.faqs) {
            Intent intent = new Intent(MainActivity.this, Faqs.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        */
        return true;

    }
}
