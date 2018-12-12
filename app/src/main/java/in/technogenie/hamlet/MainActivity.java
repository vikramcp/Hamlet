package in.technogenie.hamlet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import in.technogenie.hamlet.fragment.EventsFragment;
import in.technogenie.hamlet.fragment.GalleryFragment;
import in.technogenie.hamlet.fragment.HomeFragment;
import in.technogenie.hamlet.fragment.MembersFragment;
import in.technogenie.hamlet.fragment.MessagesFragment;
import in.technogenie.hamlet.fragment.PromotionsFragment;
import in.technogenie.hamlet.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    Fragment fragment;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    public static String CURRENT_TAG = Constants.TAG_HOME;
    GoogleApiClient mGoogleApiClient;
    //GoogleSignInClient mGoogleSignInClient;
    boolean mSignInClicked;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.user_name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.user_subtitle);
        //imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this /* FragmentActivity */, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API).build();

        //Listner for Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("userProfile");
        String LoginType = intent.getStringExtra("LoginType");

        Log.d("Jsondata", jsondata);
        if (jsondata != null) {
            // Loading Navigation Bar Header
            loadNavHeader(jsondata, LoginType);
        }

       // fragment = (Fragment) findViewById(R.id.frame);
       // fragment = new HomeFragment();

        // Create and set Android Fragment as default.
        Fragment androidFragment = new HomeFragment();
        this.setDefaultFragment(androidFragment);


        // initializing navigation menu
        setUpNavigationView();



    }

    // This method is used to set the default fragment that will be shown.
    private void setDefaultFragment(Fragment defaultFragment)
    {
        this.replaceFragment(defaultFragment);
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment)
    {

        Log.d(TAG, "Entering replaceFragment..");
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.frame, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();

        Log.d(TAG, "Exiting replaceFragment..");
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader(String jsonData, String LoginType) {

        JSONObject data = null;
        try {
            data = new JSONObject(jsonData);
            txtWebsite.setText(data.get("email").toString());
            txtName.setText(data.get("name").toString());

            if (LoginType.equals("Google")) {
            //Getting Google Profile Picture
            String gPicture = data.getString("gPicture");
            Picasso.get().load(gPicture).resize(200,200)
/*
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)*/
                    .into(imgProfile);
               /* Glide.with(this).load(gPicture)
                        .crossFade()
                        .thumbnail(0.15f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfile);*/

            } if (LoginType.equals("Facebook")) {
                JSONObject profile_pic_data = new JSONObject(data.get("picture").toString());
                JSONObject profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                Picasso.get().load(profile_pic_url.getString("url"))
/*                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)*/
                        .into(imgProfile);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        // name, Designation
       // txtName.setText("Vikram Cirigiri");
       // txtWebsite.setText("Zone Trainer");

        // loading header background image
/*        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);*/

        // Loading profile image
/*        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);*/

        // showing dot next to notifications label if new messages available
        //navigationView.getMenu().getItem(navItemIndex).setActionView(R.layout.menu_dot);
    }

    private void setUpNavigationView() {

        Log.d(TAG, "Entering setUpNavigationView()..");
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                     case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = Constants.TAG_HOME;
                        break;
                    case R.id.nav_members:
                        navItemIndex = 1;
                        CURRENT_TAG = Constants.TAG_MEMBERS;
                        break;
                    case R.id.nav_gallery:
                        navItemIndex = 2;
                        CURRENT_TAG = Constants.TAG_GALLERY;
                        break;
                    case R.id.nav_messages:
                        navItemIndex = 3;
                        CURRENT_TAG = Constants.TAG_MESSAGES;
                        break;

                    case R.id.nav_events:
                        navItemIndex = 4;
                        CURRENT_TAG = Constants.TAG_EVENTS;
                        break;
                    case R.id.nav_promotions:
                        navItemIndex = 5;
                        CURRENT_TAG = Constants.TAG_PROMOTIONS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_contact_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = Constants.TAG_HOME;
                        return true;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                Log.d(TAG, "Exiting setUpNavigationView()..");
                return true;
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        Log.d(TAG, "Entering loadHomeFragment()..");
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();

                Log.d(TAG, "Fragment received in loadHomeFragment():"+ fragment.getClass().getSimpleName());

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        //invalidateOptionsMenu();

        Log.d(TAG, "Exiting loadHomeFragment()..");
    }

    private Fragment getHomeFragment() {
        HomeFragment homeFragment;

        Log.d(TAG, "navItemIndex in getHomeFragment :" + navItemIndex);

        switch (navItemIndex) {
            case 0:
                // home
                homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // members fragment
                MembersFragment membersFragment = new MembersFragment();
                return membersFragment;
            case 2:
                // gallery fragment
                GalleryFragment galleryFragment = new GalleryFragment();
                return galleryFragment;
            case 3:
                // messages
                MessagesFragment messagesFragment = new MessagesFragment();
                //PhotosFragment photosFragment = new PhotosFragment();
                return messagesFragment;

            case 4:
                // Events fragment
                EventsFragment eventsFragment = new EventsFragment();
                return eventsFragment;
            case 5:
                // Promotions fragment
                PromotionsFragment promotionsFragment = new PromotionsFragment();
                return promotionsFragment;

            default:
                homeFragment = new HomeFragment();
                return homeFragment;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d(MainActivity.class.getSimpleName(), "Inside onOptionsItemSelected.");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d(MainActivity.class.getSimpleName(), "settings menu selected.");
            Toast.makeText(this, "Logut Selected", Toast.LENGTH_LONG);
            return true;
        } else if (id == R.id.action_logout) {
            Log.d(MainActivity.class.getSimpleName(), "Log out menu selected.");
            Toast.makeText(this, "Logout Selected", Toast.LENGTH_LONG);
            signout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Signout Function being called..
     */
    private void signout() {

        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
