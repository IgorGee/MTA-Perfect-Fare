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

    EditText trips, days, weeks, startingBalanceEdit;
    TextView totalTextView, regularSavingsTextView, weeklySavingsTextView, monthlySavingsTextView;

    DecimalFormat df = new DecimalFormat("###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trips = (EditText) findViewById(R.id.trips);
        days = (EditText) findViewById(R.id.days);
        weeks = (EditText) findViewById(R.id.weeks);
        startingBalanceEdit = (EditText) findViewById(R.id.starting_balance);
        totalTextView = (TextView) findViewById(R.id.total_text_view);
        regularSavingsTextView = (TextView) findViewById(R.id.regular_savings_textview);
        weeklySavingsTextView = (TextView) findViewById(R.id.weekly_savings_textview);
        monthlySavingsTextView = (TextView) findViewById(R.id.monthly_savings_textview);
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
                    displaySavings(regularSavingsTextView, weeklySavingsTextView,
                            totalAmount, 31 * numberOfWeeks, 116.5);
                } else if (totalAmount/numberOfWeeks >= 31){
                    totalTextView.setText("Buy a Weekly Metrocard\n" + numberOfWeeks + " Time(s)");
                    displaySavings(regularSavingsTextView, monthlySavingsTextView,
                            totalAmount, 116.5, 31 * numberOfWeeks);
                } else if (totalAmount > 81){
                    double weekAmount = calculateTotal(numberOfDays, 1, numberOfTrips, amountInCard);
                    totalTextView.setText("Every Week, Buy a\n$" + df.format(weekAmount)
                    + " Metrocard");
                    displaySavings(weeklySavingsTextView, monthlySavingsTextView,
                            31 * numberOfWeeks, 116.5, totalAmount);
                } else{
                    totalTextView.setText("Total: $" + df.format(totalAmount));
                    displaySavings(weeklySavingsTextView, monthlySavingsTextView,
                            31 * numberOfWeeks, 116.5, totalAmount);
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

    private void displaySavings(TextView savingsView1, TextView savingsView2,
                                double savingOriginal1, double savingOriginal2, double actual){

        regularSavingsTextView.setText("");
        weeklySavingsTextView.setText("");
        monthlySavingsTextView.setText("");

        double saving1 = savingOriginal1 - actual;
        double saving2 = savingOriginal2 - actual;
        savingsView1.setText(" $" + df.format(saving1));
        savingsView2.setText(" $" + df.format(saving2));
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
