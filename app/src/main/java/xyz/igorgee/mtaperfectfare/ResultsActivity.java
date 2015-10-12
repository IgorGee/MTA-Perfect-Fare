package xyz.igorgee.mtaperfectfare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class ResultsActivity extends AppCompatActivity {
    public static final double BONUS_VALUE = 1.11;
    public static final double FARE = 2.75;

    DecimalFormat df = new DecimalFormat("###.00");

    TextView costTextView, regularSavingsTextView, weeklySavingsTextView, monthlySavingsTextView;

    Intent results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        results = getIntent();

        costTextView = (TextView) findViewById(R.id.cost_text_view);
        regularSavingsTextView = (TextView) findViewById(R.id.regular_savings_textview);
        weeklySavingsTextView = (TextView) findViewById(R.id.weekly_savings_textview);
        monthlySavingsTextView = (TextView) findViewById(R.id.monthly_savings_textview);

        displayTotal();
    }

    public void displayTotal() {
        int numberOfTrips = results.getIntExtra(MainActivity.EXTRA_TRIPS, 0);
        int numberOfDays = results.getIntExtra(MainActivity.EXTRA_DAYS, 0);
        int numberOfWeeks = results.getIntExtra(MainActivity.EXTRA_WEEKS, 0);
        double amountInCard = results.getDoubleExtra(MainActivity.EXTRA_AMOUNT_IN_CARD, 0);

        double totalAmount = calculateTotal(numberOfDays, numberOfWeeks, numberOfTrips, amountInCard);
        if (totalAmount <= 0) {
            Toast.makeText(this, "Silly, you already have money!", Toast.LENGTH_LONG).show();
        } else if (totalAmount >= 116.5 && numberOfWeeks == 4) {
            costTextView.setText(Html.fromHtml(String.format("<font color='#D50000'>%s%s<br/></font>%s",
                    getString(R.string.dollar_sign), df.format(116.5), getString(R.string.buy_monthly_metrocard))));
            displaySavings(regularSavingsTextView, weeklySavingsTextView, totalAmount, 31 * numberOfWeeks, 116.5);
        } else if (totalAmount/numberOfWeeks >= 31){
            costTextView.setText(Html.fromHtml(String.format("<font color='#D50000'>%d x %s%s<br/></font>%s %d %s",
                    numberOfWeeks, getString(R.string.dollar_sign), df.format(31),
                    getString(R.string.buy_weekly_metrocard), numberOfWeeks, getString(R.string.time_s))));
            displaySavings(regularSavingsTextView, monthlySavingsTextView, totalAmount, 116.5, 31 * numberOfWeeks);
        } else if (totalAmount > 81){
            double weekAmount = calculateTotal(numberOfDays, 1, numberOfTrips, amountInCard);
            costTextView.setText(Html.fromHtml(String.format("%s<font color='#D50000'>%s%s</font> %s",
                    getString(R.string.buy_specific_weekly), getString(R.string.dollar_sign),
                            df.format(weekAmount), getString(R.string.metrocard))));
            displaySavings(weeklySavingsTextView, monthlySavingsTextView, 31 * numberOfWeeks, 116.5, totalAmount);
        } else{
            costTextView.setText(Html.fromHtml(String.format("<font color='#D50000'>%s%s</font>",
                    getString(R.string.dollar_sign), df.format(totalAmount))));
            displaySavings(weeklySavingsTextView, monthlySavingsTextView, 31 * numberOfWeeks, 116.5, totalAmount);
        }
    }

    private double calculateTotal(int numberOfDays, int numberOfWeeks, int numberOfTrips, double amountInCard) {
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
                                double savingOriginal2, double actual){
        regularSavingsTextView.setText("");
        weeklySavingsTextView.setText("");
        monthlySavingsTextView.setText("");

        double saving1 = savingOriginal1 - actual;
        double saving2 = savingOriginal2 - actual;
        savingsView1.setText(String.format(" %s%s", getString(R.string.dollar_sign), df.format(saving1)));
        savingsView2.setText(String.format(" %s%s", getString(R.string.dollar_sign), df.format(saving2)));
    }

}
