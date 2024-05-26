package main.chequewriter;

public class numberstoWords {
    final static String DOLLAR = "dollar";
    final static String CENT = "cent";
    final static String DOLLARS = DOLLAR + "s";

    final static String CENTS = CENT + "s";


    public static String convertDigitsToWords(String digits) {


        if (digits == null || digits.isEmpty()) {
            return "";
        }

        String[] parts = digits.split("\\.");
        String integerPart = parts[0];
        String fractionalPart = parts.length > 1 ? parts[1] : "";

        try {
            long integerNumber = Long.parseLong(integerPart);
            String integerWords = numberToWords(integerNumber);
            String fractionalWords = convertFractionalPart(fractionalPart);
            String currency = integerNumber == 1 ? DOLLAR : DOLLARS;

            if (fractionalWords.isEmpty()) {
                return integerWords + " " + currency;
            } else {
                String cents = fractionalWords.equals("one") ? CENT : CENTS;
                return integerWords + " " + currency + " and " + fractionalWords + " " + cents;
            }
        } catch (NumberFormatException e) {
            return "Invalid input";
        }
    }

    private static String convertFractionalPart(String fractionalPart) {
        if (fractionalPart.isEmpty()) {
            return "";
        }

        try {
            int fractionalNumber = Integer.parseInt(fractionalPart);
            if (fractionalPart.length() == 1) {
                fractionalNumber *= 10; // To handle cases like .5 which should be .50
            }
            return numberToWords(fractionalNumber);
        } catch (NumberFormatException e) {
            return "Invalid input";
        }
    }

    private static  final  String[] units = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };

    private static  final  String[] tens = {
            "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };

    private static  final  String[] thousands = {
            "", "thousand", "million", "billion", "trillion"
    };

    private static String numberToWords(long number) {
        if (number == 0) {
            return "zero";
        }

        String words = "";

        int thousandIndex = 0;

        while (number > 0) {
            if (number % 1000 != 0) {
                String segment = threeDigitNumberToWords((int) (number % 1000));
                words = segment +" "+ thousands[thousandIndex] + " " + words;
            }
            number /= 1000;
            thousandIndex++;
        }

        return words.trim();
    }

    private static String threeDigitNumberToWords(int number) {
        String words = "";

        if (number >= 100) {
            words += units[number / 100] + " hundred ";
            number %= 100;
        }

        if (number >= 20) {
            words += tens[number / 10] + " ";
            number %= 10;
        }

        if (number > 0) {
            words += units[number] + " ";
        }

        return words.trim();
    }
}
