package day21;

import java.util.Map;

public class MathOperation {
    private final OperationType type;
    private final String leftOperand;
    private final String rightOperand;

    public OperationType getType() {
        return type;
    }
    public String getLeftOperand() {
        return leftOperand;
    }
    public String getRightOperand() {
        return rightOperand;
    }


    public MathOperation(String type, String leftOperand, String rightOperand) {
        this.type = OperationType.from(type);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public long execute(Map<String, MonkeyJob> monkeyJobByMonkeyNames, String myMonkeyName) {
        if (leftOperand.equals(myMonkeyName) || rightOperand.equals(myMonkeyName)) {
            throw new RuntimeException("It is me ! Not a monkey !!");
        }

        long left = monkeyJobByMonkeyNames.get(leftOperand).computeNumber(monkeyJobByMonkeyNames, myMonkeyName);
        long right = monkeyJobByMonkeyNames.get(rightOperand).computeNumber(monkeyJobByMonkeyNames, myMonkeyName);

        return switch(type) {
            case MULTIPLY -> left * right;
            case PLUS -> left + right;
            case MINUS -> left - right;
            case DIVIDE -> left / right;
        };
    }

    @Override
    public String toString() {
        return "Operation{" +
                "type=" + type +
                ", leftOperand=" + leftOperand +
                ", rightOperand=" + rightOperand +
                '}';
    }
}
