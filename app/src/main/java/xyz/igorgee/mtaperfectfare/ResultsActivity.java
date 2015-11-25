package xyz.igorgee.mtaperfectfare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final double BONUS_VALUE = 1.11;
    public static final double FARE = 2.75;

    DecimalFormat df = new DecimalFormat("###.00");

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.navigation) NavigationView navigation;
    ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.cost_text_view) TextView costTextView;
    @Bind(R.id.regular_savings_textview) TextView regularSavingsTextView;
    @Bind(R.id.weekly_savings_textview) TextView weeklySavingsTextView;
    @Bind(R.id.monthly_savings_textview) TextView monthlySavingsTextView;

    Intent results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        initializeInstances();

        results = getIntent();
        displayTotal();
    }

    private void initializeInstances() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(ResultsActivity.this, drawerLayout,
                R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.setDrawerListener(drawerToggle);
        navigation.setNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.activity_results_title));
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void displayTotal() {
        int numberOfTrips = results.getIntExtra(MainActivity.EXTRA_TRIPS, 0);
        int numberOfDays = results.getIntExtra(MainActivity.EXTRA_DAYS, 0);
        int numberOfWeeks = results.getIntExtra(MainActivity.EXTRA_WEEKS, 0);
        double amountInCard = results.getDoubleExtra(MainActivity.EXTRA_AMOUNT_IN_CARD, 0);

        double totalAmount = calculateTotal(numberOfDays, numberOfWeeks, numberOfTrips, amountInCard);
        if (totalAmount <= 0) {
            Snackbar.make(rootLayout, "Silly, you already have money!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Silly me!", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {}
                    })
                    .show();
        } else if (totalAmount >= 116.5 && numberOfWeeks == 4) {
            costTextView.setText(Html.fromHtml(String.format("<font color='#D50000'>%s%s<br/></font>%s",
                    getString(R.string.dollar_sign), df.format(116.5), getString(R.string.buy_monthly_metrocard))));
            displaySavings(regularSavingsTextView, weeklySavingsTextView, totalAmount, 31 * numberOfWeeks, 116.5);
        } else if (totalAmount / numberOfWeeks >= 31) {
            costTextView.setText(Html.fromHtml(String.format("<font color='#D50000'>%d x %s%s<br/></font>%s %d %s",
                    numberOfWeeks, getString(R.string.dollar_sign), df.format(31),
                    getString(R.string.buy_weekly_metrocard), numberOfWeeks, getString(R.string.time_s))));
            displaySavings(regularSavingsTextView, monthlySavingsTextView, totalAmount, 116.5, 31 * numberOfWeeks);
        } else if (totalAmount > 81) {
            double weekAmount = calculateTotal(numberOfDays, 1, numberOfTrips, amountInCard);
            costTextView.setText(Html.fromHtml(String.format("%s<font color='#D50000'>%s%s</font> %s",
                    getString(R.string.buy_specific_weekly), getString(R.string.dollar_sign),
                    df.format(weekAmount), getString(R.string.metrocard))));
            displaySavings(weeklySavingsTextView, monthlySavingsTextView, 31 * numberOfWeeks, 116.5, totalAmount);
        } else {
            costTextView.setText(Html.fromHtml(String.format("<font color='#D50000'>%s%s</font>",
                    getString(R.string.dollar_sign), df.format(totalAmount))));
            displaySavings(weeklySavingsTextView, monthlySavingsTextView, 31 * numberOfWeeks, 116.5, totalAmount);
        }
    }

    public double calculateTotal(int numberOfDays, int numberOfWeeks, int numberOfTrips, double amountInCard) {
        double totalAmount;
        numberOfTrips *= numberOfDays * numberOfWeeks;

        if ((FARE * numberOfTrips - amountInCard) / BONUS_VALUE >= 5.50) {
            totalAmount = ((FARE * numberOfTrips - amountInCard) / BONUS_VALUE);
        } else {
            totalAmount = ((FARE * numberOfTrips - amountInCard));
        }

        // MTA Metrocard Vending Machine only allow payments in nickel increments
        int pennies = (int) Math.round(totalAmount * 100);
        int nickelDifference = pennies % 10 - 5;

        if (nickelDifference < 0 && nickelDifference != -5) {
            pennies += -nickelDifference;
        } else if (nickelDifference > 0) {
            pennies += 5 - nickelDifference;
        }
        return pennies / 100.0;
    }

    private void displaySavings(TextView savingsView1, TextView savingsView2, double savingOriginal1,
                                double savingOriginal2, double actual) {
        regularSavingsTextView.setText("");
        weeklySavingsTextView.setText("");
        monthlySavingsTextView.setText("");

        double saving1 = savingOriginal1 - actual;
        double saving2 = savingOriginal2 - actual;
        savingsView1.setText(String.format(" %s%s", getString(R.string.dollar_sign), df.format(saving1)));
        savingsView2.setText(String.format(" %s%s", getString(R.string.dollar_sign), df.format(saving2)));
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
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.menu_contact:
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            case android.R.id.home:
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.navAbout:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.navContact:
                startActivity(new Intent(this, ContactActivity.class));
                break;
        }

        return false;
    }
}
