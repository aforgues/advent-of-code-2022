package day11;

import java.util.Optional;

public class Operation {
    private static final String OLD_VALUE = "old";
    private final OperationType type;
    private final Optional<Long> leftOperand;
    private final Optional<Long> rightOperand;


    public Operation(String type, String leftOperand, String rightOperand) {
        this.type = OperationType.from(type);
        this.leftOperand = leftOperand.equals(OLD_VALUE) ? Optional.empty() : Optional.of(Long.valueOf(leftOperand));
        this.rightOperand = rightOperand.equals(OLD_VALUE) ? Optional.empty() : Optional.of(Long.valueOf(rightOperand));

    }

    public long execute(long oldValue) {
        long left = leftOperand.orElse(oldValue);
        long right = rightOperand.orElse(oldValue);
        return switch(type) {
            case MULTIPLY -> left * right;
            case PLUS -> left + right;
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
