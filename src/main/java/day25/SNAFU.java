package day25;

import java.util.*;

public record SNAFU(Map<Integer, Value> powerOfFiveMap) {
    private static final int DECIMAL_POWER_VALUE = 5;
    public static SNAFU from(String content) {
        Map<Integer, Value> powerOfFiveMap = new TreeMap<>();

        for (int i = 0; i < content.length(); i++) {
            powerOfFiveMap.put(i, Value.fromDigit(Character.toString(content.charAt(content.length()-i-1))));
        }

        return new SNAFU(powerOfFiveMap);
    }

    public static SNAFU fromDecimal(long decimalSum) {
        System.out.println("Converting to SNAFU the decimal value : " + decimalSum);
        Map<Integer, Value> powerOfFiveMap = new TreeMap<>();

        // First find max power of five
        int max = 0;
        //while (decimalSum / Math.pow(DECIMAL_POWER_VALUE, max) > 2) {
        while (decimalSum > maxDecimalValueWith(max)) {
            max++;
        }

        // Brut force solution
        int counter = 0;
        // FIXME : generalize this for max power of five => target puzzle might need 15 !!
        // 2 2 2 2 2 2
        // 2 2 2 2 2 1
        // 2 2 2 2 2 0
        // 2 2 2 2 2 -
        // 2 2 2 2 2 =
        // 2 2 2 2 1 2
        // 2 2 2 2 1 1
        // 2 2 2 2 1 0
        // ...
        for (Value value5 : Value.values()) {
            for (Value value4 : Value.values()) {
                for (Value value3 : Value.values()) {
                    for (Value value2 : Value.values()) {
                        for (Value value1 : Value.values()) {
                            for (Value value0 : Value.values()) {
                                String testSNAFUValue = value5.digit() + value4.digit() + value3.digit() + value2.digit() + value1.digit() + value0.digit();
                                counter++;
                                System.out.println(counter + " - Testing SNAFU value : " + testSNAFUValue);
                                if (SNAFU.from(testSNAFUValue).toDecimal() == decimalSum) {
                                    System.out.println("Found SNAFU value " + testSNAFUValue);
                                    System.out.println("Checking SNAFU stringValue conversion " + SNAFU.from(testSNAFUValue).stringValue());
                                    return SNAFU.from(testSNAFUValue);
                                }
                            }
                        }
                    }
                }
            }
        }

        /*int subSum = (int) (1 * Math.pow(DECIMAL_POWER_VALUE, max));

        for (int i = 0; i < max; i++) {
            subSum += 2 * Math.pow(5, i);
        }
        // 1 unit of max power of five is enough
        if (decimalSum < subSum) {
            powerOfFiveMap.put(max, Value.ONE);
        }
        else {
            powerOfFiveMap.put(max, Value.TWO);
        }

        // go to power of five max - 1
        for (int i = max - 1; i == 0; i--) {
            // TODO
            /*if (decimalSum % Math.pow(DECIMAL_POWER_VALUE, max) > 2 * Math.pow(DECIMAL_POWER_VALUE, max-1)) {
                break;
            }*/
        //}

        return new SNAFU(powerOfFiveMap);
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
