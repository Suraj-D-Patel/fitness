package patel.d.suraj.fitness;

/**
 * Created by suraj.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    android.support.v7.widget.Toolbar toolbar;
    private GoogleSignInClient mGoogleSignInClient;
    android.support.v4.view.ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    CoordinatorLayout cnd;
    private FirebaseAuth auth;
    NetworkInfo.State mobile,wifi;
    ConnectivityManager conMan;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        cnd=(CoordinatorLayout) (findViewById(R.id.main_content));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        auth = FirebaseAuth.getInstance();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        mobile = conMan.getNetworkInfo(0).getState();
        wifi = conMan.getNetworkInfo(1).getState();

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING){}
        else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){}
        else{
            Snackbar snackbar = Snackbar
                    .make(cnd, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings", view -> {startActivity(new Intent(Settings.ACTION_SETTINGS));});
// Changing message text color
            snackbar.setActionTextColor(Color.RED);
// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.CYAN);
            snackbar.show();
        }
        // setup toolbar with Tabs
        toolbar = (Toolbar) (findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);

        android.support.design.widget.TabLayout tabs = (TabLayout) findViewById(R.id.tb);
        tabs.addTab(tabs.newTab().setText("Tab 1"));
        tabs.addTab(tabs.newTab().setText("Tab 2"));
        tabs.addTab(tabs.newTab().setText("Tab 3"));

        //Link tabs with view pager
        tabs.setupWithViewPager(mViewPager);
        getSupportActionBar().setTitle("DashBoard");
        getSupportActionBar().setDisplayHomeAsUpEnabled (true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        //toolbar.setTitle("Hello World");
        toolbar.setSubtitle("Live a Healthy Life");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.main_orange_color));
        // Set up the ViewPager with the sections adapter.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // This method will trigger on item Click of navigation menu
        navigationView.setNavigationItemSelectedListener(menuItem -> {
                            // Set item in checked state
                            // menuItem.setChecked(true);
                            // Closing drawer_image on item click
            if(menuItem.getItemId()==R.id.history) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            } else if (menuItem.getItemId() == R.id.music) {
                startActivity(new Intent(getApplicationContext(), MusicActivity.class));
            } else if (menuItem.getItemId() == R.id.about) {
                startActivity(new Intent(MainActivity.this, AboutApp_Activity.class));
            } else if (menuItem.getItemId() == R.id.contact) {
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
            } else if (menuItem.getItemId() == R.id.share) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hello check out my app click on the link : https://play.google.com/store/apps/category/HEALTH_AND_FITNESS?hl=en");
                startActivity(Intent.createChooser(share, "Share using"));
            }
            //Logic
            mDrawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threedotmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.signout) {
            pref.getString("userz", null); // getting String
            auth.signOut();

            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, task -> {
                        Toast.makeText(this, "You are Logged Out..", Toast.LENGTH_SHORT).show();
                    });
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        else if(id==android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
                return (new Fragment_Tracking());
            else if (position == 1)
                return (new Fragment_Diet_Plan());
            else if (position == 2)
                return (new Fragment_Exercises());
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tracking";
                case 1:
                    return "Diet Plan";
                case 2:
                    return "Exercises";
            }
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}