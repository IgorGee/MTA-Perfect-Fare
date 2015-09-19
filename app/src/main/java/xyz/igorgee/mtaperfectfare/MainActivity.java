package xyz.igorgee.mtaperfectfare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    public static final double BONUS_VALUE = 1.11;
    public static final double FARE = 2.75;

    EditText trips;
    EditText days;
    EditText weeks;
    EditText startingBalanceEdit;
    TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trips = (EditText) findViewById(R.id.trips);
        days = (EditText) findViewById(R.id.days);
        weeks = (EditText) findViewById(R.id.weeks);
        startingBalanceEdit = (EditText) findViewById(R.id.starting_balance);
        totalTextView = (TextView) findViewById(R.id.total_text_view);
    }

    public void displayTotal(View view) {
        try {
            int numberOfDays = Integer.parseInt(days.getText().toString());
            int numberOfWeeks = Integer.parseInt(weeks.getText().toString());
            int numberOfTrips = Integer.parseInt(trips.getText().toString());
            double amountInCard = Double.parseDouble(startingBalanceEdit.getText().toString());

            if (!areValidNumbers(numberOfDays, numberOfWeeks, numberOfTrips, amountInCard)) {
                Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_LONG).show();
            } else {
                double totalAmount = calculateTotal(numberOfDays, numberOfWeeks, numberOfTrips,
                        amountInCard);
                if (totalAmount <= 0) {
                    Toast.makeText(this, "Silly, you already have money!",
                            Toast.LENGTH_LONG).show();
                } else if (totalAmount >= 116.5 && numberOfWeeks == 4) {
                    totalTextView.setText("Buy a Monthly Metrocard");
                } else if (totalAmount/numberOfWeeks >= 31){
                    totalTextView.setText("Buy a Weekly Metrocard\n" + numberOfWeeks + " Time(s)");
                } else {
                    DecimalFormat df = new DecimalFormat("#.00");
                    totalTextView.setText("Total: $" + df.format(totalAmount));
                }
            }
        } catch (NumberFormatException n) {
            Toast.makeText(this, "Please enter a value for all fields.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean areValidNumbers(int numberOfDays, int numberOfWeeks, int numberOfTrips,
                                    double amountInCard) {
        if (numberOfDays < 1 || numberOfDays > 7 || numberOfWeeks < 1 ||
                numberOfWeeks > 4 || numberOfTrips < 1 || amountInCard < 0) {
            return false;
        } else {
            return true;
        }
    }

    private double calculateTotal(int numberOfDays, int numberOfWeeks, int numberOfTrips,
                                  double amountInCard) {
        numberOfTrips *= numberOfDays * numberOfWeeks;

        double totalAmount;

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
