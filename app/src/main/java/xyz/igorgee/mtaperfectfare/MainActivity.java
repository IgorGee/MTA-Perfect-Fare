package xyz.igorgee.mtaperfectfare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_TRIPS = "xyz.igorgee.mtaperfectfare.TRIPS";
    public static final String EXTRA_DAYS = "xyz.igorgee.mtaperfectfare.DAYS";
    public static final String EXTRA_WEEKS = "xyz.igorgee.mtaperfectfare.WEEKS";
    public static final String EXTRA_AMOUNT_IN_CARD = "xyz.igorgee.mtaperfectfare.AMOUNT_IN_CARD";

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.navigation) NavigationView navigation;
    ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.starting_balance) EditText startingBalanceEdit;
    @Bind(R.id.trips) EditText trips;
    @Bind(R.id.days) EditText days;
    @Bind(R.id.weeks) EditText weeks;
    @Bind(R.id.fabCalculate) FloatingActionButton fabCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeInstances();
    }

    private void initializeInstances() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.setDrawerListener(drawerToggle);
        navigation.setNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.fabCalculate)
    public void prepareTotal(View view) {
        try {
            int numberOfTrips = Integer.parseInt(trips.getText().toString());
            int numberOfDays = Integer.parseInt(days.getText().toString());
            int numberOfWeeks = Integer.parseInt(weeks.getText().toString());
            double amountInCard = Double.parseDouble(startingBalanceEdit.getText().toString());

            if (!areValidNumbers(numberOfDays, numberOfWeeks, numberOfTrips, amountInCard)) {
                Snackbar.make(rootLayout, "Please enter valid numbers.", Snackbar.LENGTH_LONG)
                        .setAction("Okay", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {}
                        }).show();
            } else {
                Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                results.putExtra(EXTRA_TRIPS, numberOfTrips);
                results.putExtra(EXTRA_DAYS, numberOfDays);
                results.putExtra(EXTRA_WEEKS, numberOfWeeks);
                results.putExtra(EXTRA_AMOUNT_IN_CARD, amountInCard);
                startActivity(results);
            }
        } catch (NumberFormatException n) {
            Snackbar.make(rootLayout, "Please enter a value for all fields.", Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {}
                    }).show();
        }
    }

    private boolean areValidNumbers(int numberOfDays, int numberOfWeeks, int numberOfTrips, double amountInCard) {
        boolean result = true;
        if (numberOfDays < 1 || numberOfDays > 7 || numberOfWeeks < 1 || numberOfWeeks > 4 || numberOfTrips < 1 ||
                amountInCard < 0) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_about:
                Snackbar.make(rootLayout, "Clicked About!", Snackbar.LENGTH_INDEFINITE).show();
                return true;
            case R.id.menu_contact:
                Snackbar.make(rootLayout, "Clicked Contact!", Snackbar.LENGTH_INDEFINITE).show();
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.navAbout:
                Snackbar.make(rootLayout, "Clicked About!", Snackbar.LENGTH_INDEFINITE).show();
                break;
            case R.id.navContact:
                Snackbar.make(rootLayout, "Clicked Contact!", Snackbar.LENGTH_INDEFINITE).show();
                break;
        }

        return false;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Changes home icon from "back" to "hamburger"
        drawerToggle.syncState();
    }
}
