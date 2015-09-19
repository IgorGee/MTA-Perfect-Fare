package xyz.igorgee.mtaperfectfare;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Igor on 9/15/2015.
 */
public class MainActivityTest {

    static final double BONUS_VALUE = 1.11;
    static final double FARE = 2.75;

    @Test
    public void calc() {
        assertEquals(calculateTotal(1, 1, 1, 0), 2.75);
        assertEquals(calculateTotal(1, 1, 1, .01), 2.75);
        assertEquals(calculateTotal(1, 1, 1, 1), 1.75);
        assertEquals(calculateTotal(2, 1, 1, 0), 5.50);
        assertEquals(calculateTotal(2, 1, 1, .01), 5.50);
        assertEquals(calculateTotal(5, 1, 1, 8.26), 5.50);
        assertEquals(calculateTotal(2, 1, 1, 1), 4.50);
        assertEquals(calculateTotal(1, 2, 3, 4), 11.30);
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
        int pennies = (int) Math.round(totalAmount*100);
        int nickelDifference = pennies % 10 - 5;

        if (nickelDifference < 0 && nickelDifference != -5){
            pennies += -nickelDifference;
        } else if (nickelDifference > 0){
            pennies += 5 - nickelDifference;
        }

        return pennies/100.0;
    }

}