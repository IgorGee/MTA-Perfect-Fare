package xyz.igorgee.mtaperfectfare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TRIPS = "xyz.igorgee.mtaperfectfare.TRIPS";
    public static final String EXTRA_DAYS = "xyz.igorgee.mtaperfectfare.DAYS";
    public static final String EXTRA_WEEKS = "xyz.igorgee.mtaperfectfare.WEEKS";
    public static final String EXTRA_AMOUNT_IN_CARD = "xyz.igorgee.mtaperfectfare.AMOUNT_IN_CARD";

    @Bind(R.id.trips) EditText trips;
    @Bind(R.id.days) EditText days;
    @Bind(R.id.weeks) EditText weeks;
    @Bind(R.id.starting_balance) EditText startingBalanceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void displayTotal(View view) {
        try {
            int numberOfTrips = Integer.parseInt(trips.getText().toString());
            int numberOfDays = Integer.parseInt(days.getText().toString());
            int numberOfWeeks = Integer.parseInt(weeks.getText().toString());
            double amountInCard = Double.parseDouble(startingBalanceEdit.getText().toString());

            if (!areValidNumbers(numberOfDays, numberOfWeeks, numberOfTrips, amountInCard)) {
                Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_LONG).show();
            } else {
                Intent results = new Intent(MainActivity.this, ResultsActivity.class);
                results.putExtra(EXTRA_TRIPS, numberOfTrips);
                results.putExtra(EXTRA_DAYS, numberOfDays);
                results.putExtra(EXTRA_WEEKS, numberOfWeeks);
                results.putExtra(EXTRA_AMOUNT_IN_CARD, amountInCard);
                startActivity(results);
            }
        } catch (NumberFormatException n) {
            Toast.makeText(this, "Please enter a value for all fields.", Toast.LENGTH_LONG).show();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
