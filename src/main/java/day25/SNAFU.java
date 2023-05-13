package day25;

import java.util.*;

public record SNAFU(Map<Integer, Value> powerOfFiveMap) {
    private static final int DECIMAL_POWER_VALUE = 5;
    //private static int counter = 0;
    public static SNAFU from(String content) {
        Map<Integer, Value> powerOfFiveMap = new TreeMap<>();

        for (int i = 0; i < content.length(); i++) {
            powerOfFiveMap.put(i, Value.fromDigit(Character.toString(content.charAt(content.length()-i-1))));
        }

        return new SNAFU(powerOfFiveMap);
    }

    public static SNAFU fromDecimal(long decimalSum) {
        System.out.println("Converting to SNAFU the decimal value : " + decimalSum);

        // First find max power of five
        int max = 0;
        //while (decimalSum / Math.pow(DECIMAL_POWER_VALUE, max) > 2) {
        while (decimalSum > maxDecimalValueWith(max)) {
            max++;
        }

        /*
        // Brut force solution
        counter = 0;
        // 2 2 2 2 2 2
        // 2 2 2 2 2 1
        // 2 2 2 2 2 0
        // 2 2 2 2 2 -
        // 2 2 2 2 2 =
        // 2 2 2 2 1 2
        // 2 2 2 2 1 1
        // 2 2 2 2 1 0
        // ...
        SNAFU result = findSnafuForDecimal(decimalSum, "", max);
        if (result != null)
            return result;

        throw new IllegalStateException("Unable to fin SNAFU value for this decimal : " + decimalSum);
        */

        Map<Integer, Value> valueByPowerOfFives = new HashMap<>();
        long remainingDecimal = decimalSum;
        for (int power = max; power >= 0; power--) {
            Value value = findSnafuForDecimalV2(remainingDecimal, power);
            valueByPowerOfFives.put(power, value);
            remainingDecimal -= value.finalValue() * Math.pow(DECIMAL_POWER_VALUE, power);
        }
        return new SNAFU(valueByPowerOfFives);
    }

    // brutforce solution (working only with test input)
    /*private static SNAFU findSnafuForDecimal(long decimalSum, String snafuDigitsPrefix, int currentPowerOfFive) {
        for (Value value : Value.values()) {
            if (currentPowerOfFive > 0) {
                SNAFU found = findSnafuForDecimal(decimalSum, snafuDigitsPrefix + value.digit(), currentPowerOfFive - 1);
                if (found != null)
                    return found;
            }
            else {
                String testSNAFUValue = snafuDigitsPrefix + value.digit();
                counter++;
                System.out.println(counter + " / " + decimalSum + " - Testing SNAFU value : " + testSNAFUValue);
                if (SNAFU.from(testSNAFUValue).toDecimal() == decimalSum) {
                    System.out.println("Found SNAFU value " + testSNAFUValue);
                    System.out.println("Checking SNAFU stringValue conversion " + SNAFU.from(testSNAFUValue).stringValue());
                    return SNAFU.from(testSNAFUValue);
                }
            }
        }
        return null;
    }*/

    private static Value findSnafuForDecimalV2(long remainingDecimal, int currentPowerOfFive) {
        // check if digit should be 0
        if (remainingDecimal == 0)
            return Value.ZERO;

        long totalMaxOfPreviousPower = maxDecimalValueWith(currentPowerOfFive - 1);
        if (Math.abs(remainingDecimal) <= totalMaxOfPreviousPower)
            return Value.ZERO;

        // check if digit should be 1 or -1 depending on remainingDecimal sign
        if (Math.abs(remainingDecimal - Math.signum(remainingDecimal) * Math.pow(DECIMAL_POWER_VALUE, currentPowerOfFive)) <= totalMaxOfPreviousPower)
            return Math.signum(remainingDecimal) > 0 ? Value.ONE : Value.MINUS;
        return Math.signum(remainingDecimal) > 0 ? Value.TWO : Value.DOUBLE_MINUS;
    }

    private static long maxDecimalValueWith(int maxPowerOfFive) {
        long maxDecimalValue = 0;
        for (int i = 0; i <= maxPowerOfFive; i++) {
            maxDecimalValue += 2 * Math.pow(DECIMAL_POWER_VALUE, i);
        }
        return maxDecimalValue;
    }

    public long toDecimal() {
        long sum = 0;
        for (Map.Entry<Integer, Value> entry : this.powerOfFiveMap.entrySet()) {
            sum += Math.pow(DECIMAL_POWER_VALUE, entry.getKey()) * entry.getValue().finalValue();
        }
        return sum;
    }

    public String stringValue() {
        StringBuilder sb = new StringBuilder();
        List<Value> reversedValues = new ArrayList<>(powerOfFiveMap.values());
        Collections.reverse(reversedValues);
        for (Value value : reversedValues) {
            sb.append(value.digit());
        }
        return sb.toString();
    }
}
