package www.coders.org.qr_fintech_client;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView userimg;

    private ManageShopFragment manageStoreFragment;
    private ManageProductFragment manageItemFragment;
    private SendMoneyFragment sendMoneyFragment;

    private Boolean isFabOpen = false;

    private FloatingActionButton floatingButton, buyButton, sellButton;

    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        manageStoreFragment = new ManageShopFragment();
        manageItemFragment = new ManageProductFragment();
        sendMoneyFragment = new SendMoneyFragment();

        floatingButton = (FloatingActionButton)findViewById(R.id.fab);
        buyButton = (FloatingActionButton)findViewById(R.id.fab1);
        sellButton = (FloatingActionButton)findViewById(R.id.fab2);

<<<<<<< HEAD
        qrButton = (FloatingActionButton)findViewById(R.id.fab);
        qrButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainScreenActivity.this, TopUpActivity.class));

            }
        });

        /*
        qrButton.setOnClickListener(new View.OnClickListener() {
=======
        floatingButton.setOnClickListener(new View.OnClickListener() {
>>>>>>> 2a6960c23cbbe398197456db6571790684251d90
            @Override
            public void onClick(View view) {

                if(isFabOpen){

                    floatingButton.startAnimation(rotate_backward);
                    buyButton.startAnimation(fab_close);
                    sellButton.startAnimation(fab_close);
                    buyButton.setClickable(false);
                    sellButton.setClickable(false);
                    isFabOpen = false;
                    Log.d("Raj", "close");

                } else {

                    floatingButton.startAnimation(rotate_forward);
                    buyButton.startAnimation(fab_open);
                    sellButton.startAnimation(fab_open);
                    buyButton.setClickable(true);
                    sellButton.setClickable(true);
                    isFabOpen = true;
                    Log.d("Raj","open");

                }

                IntentIntegrator integrator = new IntentIntegrator(MainScreenActivity.this);
                integrator.setCaptureActivity( qrReader.class );
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });*/

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "구매 버튼 클릭", Toast.LENGTH_LONG).show();
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "판매 버튼 클릭", Toast.LENGTH_LONG).show();
            }
        });

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
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
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

        } else if (id == R.id.nav_manage) {

        }

        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
