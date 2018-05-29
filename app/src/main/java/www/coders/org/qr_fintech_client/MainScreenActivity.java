package www.coders.org.qr_fintech_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_RESULT = "item_code";
    public static final String my_shared_preferences = "login_information";
    SharedPreferences sharedpreferences;

    ImageView userimg;

    private ManageShopFragment manageStoreFragment;
    private ManageProductFragment manageItemFragment;
    private SendMoneyFragment sendMoneyFragment;
    private SendListFragment sendListFragment;


    private Boolean isFabOpen = false;

    private FloatingActionButton floatingButton, buyButton, sellButton, topupButton;

    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    ///
    private String id, type, user_name;
    private TextView textView_id, textView_name;
    public final static String TAG_ID = "id";
    public final static String TAG_TYPE = "type"; // 개인 0, 상인 1
    public final static String USER_NAME = "name";
    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //////////////////////////////////////////////////////////////////////////////////////

        manageStoreFragment = new ManageShopFragment();
        manageItemFragment = new ManageProductFragment();
        sendMoneyFragment = new SendMoneyFragment();
        sendListFragment = new SendListFragment();


        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        buyButton = (FloatingActionButton) findViewById(R.id.fab1);
        sellButton = (FloatingActionButton) findViewById(R.id.fab2);


        topupButton = (FloatingActionButton) findViewById(R.id.fab);
        topupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreenActivity.this, TopUpActivity.class));

                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                id = sharedpreferences.getString(TAG_ID, null);
                type = sharedpreferences.getString(TAG_TYPE, null);
                user_name = sharedpreferences.getString(USER_NAME, null);
                View headerLayout = navigationView.getHeaderView(0);

                textView_name = (TextView) headerLayout.findViewById(R.id.nav_header_name);
                textView_id = (TextView) headerLayout.findViewById(R.id.nav_header_id);
                textView_id.setText(id);
                textView_name.setText(user_name);

                /////////////////////////////////////////////////////////////////////////////////////

                manageStoreFragment = new ManageShopFragment();
                manageItemFragment = new ManageProductFragment();
                sendMoneyFragment = new SendMoneyFragment();


                fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
                rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
                rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);


                floatingButton = (FloatingActionButton) findViewById(R.id.fab);
                buyButton = (FloatingActionButton) findViewById(R.id.fab1); //내 QR 보여주기
                sellButton = (FloatingActionButton) findViewById(R.id.fab2); //물건 QR 찍기
                topupButton = (FloatingActionButton) findViewById(R.id.fab3);


                floatingButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.fab:
                                if (!isFabOpen) {
                                    topupButton.startAnimation(fab_open);
                                    sellButton.startAnimation(fab_open);
                                    buyButton.startAnimation(fab_open);
                                    floatingButton.startAnimation(rotate_forward);
                                    topupButton.setClickable(true);
                                    sellButton.setClickable(true);
                                    buyButton.setClickable(true);
                                    isFabOpen = true;
                                } else {
                                    topupButton.startAnimation(fab_close);
                                    sellButton.startAnimation(fab_close);
                                    buyButton.startAnimation(fab_close);
                                    floatingButton.startAnimation(rotate_backward);
                                    topupButton.setClickable(false);
                                    sellButton.setClickable(false);
                                    buyButton.setClickable(false);
                                    isFabOpen = false;
                                }
                                break;
                        }
                    }
                });

                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainScreenActivity.this, UserShowQRActivity.class));
                    }
                });

                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //IntentIntegrator integrator = new IntentIntegrator(MainScreenActivity.this);
                        //integrator.setCaptureActivity( qrReader.class );
                        //integrator.setOrientationLocked(false);
                        //integrator.initiateScan();
                        Intent intent = new Intent(MainScreenActivity.this, UserBuyActivity.class);
                        intent.putExtra(TAG_RESULT, "14");
                        startActivity(intent);
                    }
                });

                topupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainScreenActivity.this, TopUpActivity.class));
                    }
                });

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MainScreenActivity.super.onActivityResult(requestCode, resultCode, data);

        // QR코드/ 바코드를 스캔한 결과
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // result.getFormatName() : 바코드 종류
        // result.getContents() : 바코드 값
        Intent intent = new Intent(MainScreenActivity.this, UserBuyActivity.class);
        intent.putExtra(TAG_RESULT, result.getContents());
        startActivity(intent);
        //Toast.makeText(getApplicationContext(), result.getContents(),Toast.LENGTH_LONG).show();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            MainScreenActivity.super.onBackPressed();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return MainScreenActivity.super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_manageStore) {
            transaction.replace(R.id.container, manageStoreFragment);
        } else if (id == R.id.nav_manageItem) {
            transaction.replace(R.id.container, manageItemFragment);
        } else if (id == R.id.nav_sendMoney) {
            transaction.replace(R.id.container, sendMoneyFragment);

        } else if (id == R.id.nav_sendList) {
            transaction.replace(R.id.container, sendListFragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

