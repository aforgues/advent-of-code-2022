package day13;

import java.util.*;

public class Value implements Comparable<Value> {
    private final boolean isArray;
    private final List<Value> valueList;
    private Integer intValue;

    public boolean isArray() {
        return isArray;
    }
    public List<Value> getValueList() {
        return valueList;
    }
    public Integer getIntValue() {
        return intValue;
    }

    public Value(boolean isArray, List<Value> valueList, Integer intValue) {
        this.isArray = isArray;
        this.valueList = valueList;
        this.intValue = intValue;
    }

    public Value(String content) {
        this.isArray = content.startsWith("[");
        this.valueList = new ArrayList<>();
        if (! this.isArray) {
            this.intValue = new Scanner(content).nextInt();
            //System.out.println("Not an array (" + content + ") => " + this.intValue);
        }
        else {
            // extract all values between [ and last ]
            String currentContent = content.substring(1, content.length() - 1);

            //System.out.println("An array => parsing content : " + currentContent);
            while(currentContent.length() > 0) {
                char c = currentContent.charAt(0);
                // first value is still an array
                if (c == '[') {
                    String subContent = currentContent.substring(0, getMatchingIndexOfLastBracket(currentContent) + 1);
                    this.valueList.add(new Value(subContent));
                    if (subContent.length() < currentContent.length() - 1)
                        currentContent = currentContent.substring(subContent.length() + 1);
                    else
                        currentContent = "";
                }
                // first value is an integer
                else {
                    String subContent;
                    // moving to next value after the first comma
                    if (currentContent.contains(",")) {
                        subContent = currentContent.substring(0, currentContent.indexOf(","));
                        currentContent = currentContent.substring(currentContent.indexOf(",") + 1);
                    }
                    else {
                        subContent = String.valueOf(new Scanner(currentContent).nextInt());
                        currentContent = "";
                    }
                    this.valueList.add(new Value(subContent));
                }
            }
        }
    }

    public int compare(Value right) {
        // extract left Value
        Value left = this;

        System.out.println("Comparing " + left.displayValue() + " with " + right.displayValue());

        // both SubValue are Integers
        if (!left.isArray() && !right.isArray()) {
            if (Objects.equals(left.getIntValue(), right.getIntValue()))
                return 0;

            return (left.getIntValue() < right.getIntValue()) ? 1 : -1;
        }
        else {

            // Fist level is always an array
            Iterator<Value> leftIterator = left.getValueList().iterator();
            Iterator<Value> rightIterator = right.getValueList().iterator();

            // Comparing first value inside each array
            while (leftIterator.hasNext()) {
                Value leftSubValue = leftIterator.next();

                if (!rightIterator.hasNext()) {
                    System.out.println("Right side ran out of items, so inputs are not in the right order");
                    return -1;
                }

                Value rightSubValue = rightIterator.next();
                System.out.println("Comparing " + leftSubValue.displayValue() + " with " + rightSubValue.displayValue());

                // both SubValue are Integers
                if (!leftSubValue.isArray() && !rightSubValue.isArray()) {
                    int compareSub = leftSubValue.compare(rightSubValue);
                    if (compareSub == 0)
                        continue;
                    if (compareSub == 1)
                        System.out.println("Left side is smaller, so inputs are in the right order");
                    else
                        System.out.println("Right side is smaller, so inputs are not in the right order");
                    return compareSub;
                }
                // both SubValue are List
                else if (leftSubValue.isArray() && rightSubValue.isArray()) {
                    // compare all subValues of these SubValues
                    int compareSub = leftSubValue.compare(rightSubValue);
                    if (compareSub == 0)
                        continue;
                    return compareSub;
                }
                // Exactly one SubValue is an Integer
                else {
                    Value convertedLeftValue = leftSubValue;
                    Value convertedRightValue = rightSubValue;
                    if (leftSubValue.isArray) {
                        System.out.println("Mixed types; convert right " + rightSubValue.intValue + " to Array and retry comparison");
                        convertedRightValue = rightSubValue.toArray();
                    }
                    else {
                        System.out.println("Mixed types; convert left " + leftSubValue.intValue + " to Array and retry comparison");
                        convertedLeftValue = leftSubValue.toArray();
                    }
                    int compareSub = convertedLeftValue.compare(convertedRightValue);
                    if (compareSub == 0)
                        continue;
                    return compareSub;
                }
            }

            if (rightIterator.hasNext()) {
                System.out.println("Left side ran out of items, so inputs are in the right order");
                return 1;
            }
        }

        return 0;
    }

    public Value toArray() {
        if (isArray)
            return this;
        return new Value(true, List.of(this), null);
    }

    private static int getMatchingIndexOfLastBracket(final String content) {
        //System.out.println("Analysing content : " + content);

        int countOpeningBracket = 0;
        int countClosingBracket = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '[')
                countOpeningBracket++;
            if (c == ']')
                countClosingBracket++;
            if (countOpeningBracket - countClosingBracket == 0) {
                //System.out.println("Nb [ : " + countOpeningBracket + ", Nb ] : " + countClosingBracket + " => index = " + i);
                return i;
            }
        }
        return content.indexOf("]");
    }

    public String displayValue() {
        StringBuilder s = new StringBuilder();
        if (this.isArray) {
            s.append("[");
            boolean first = true;
            for (Value subValue : this.valueList) {
                if (first) {
                    first = false;
                }
                else {
                    s.append(",");
                }
                s.append(subValue.displayValue());
            }
            s.append("]");
        }
        else {
            s.append(this.intValue);
        }
        return s.toString();
    }

    @Override
    public int compareTo(Value o) {
        return o.compare(this);
    }

    @Override
    public String toString() {
        return "Value{" +
                "isArray=" + isArray +
                ", valueList=" + valueList +
                ", intValue=" + intValue +
                '}';
    }
}
