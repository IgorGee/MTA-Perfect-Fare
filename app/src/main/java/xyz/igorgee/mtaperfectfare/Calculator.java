package xyz.igorgee.mtaperfectfare;


public class Calculator {

    public static final double BONUS_VALUE = 1.11;
    public static final double FARE = 2.75;

    public static boolean areValidNumbers(int numberOfDays, int numberOfWeeks, int numberOfTrips, double amountInCard) {
        boolean result = true;
        if (numberOfDays < 1 || numberOfDays > 7 || numberOfWeeks < 1 || numberOfWeeks > 4 || numberOfTrips < 1 ||
                amountInCard < 0) {
            result = false;
        }
        return result;
    }

    public static double calculateTotal(int numberOfDays, int numberOfWeeks, int numberOfTrips, double amountInCard) {
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
}
