package day21;

import java.util.Map;
import java.util.Optional;

public class MonkeyJob {
    private final Type type;
    private final Optional<Long> specificNumber;
    private final Optional<MathOperation> mathOperation;

    public MonkeyJob(Type type, Optional<Long> specificNumber, Optional<MathOperation> mathOperation) {
        this.type = type;
        this.specificNumber = specificNumber;
        this.mathOperation = mathOperation;
    }

    public long computeNumber(Map<String, MonkeyJob> monkeyJobByMonkeyNames) {
        return switch (type) {
            case SPECIFIC_NUMBER -> this.specificNumber.get();
            case MATH_OPERATION -> this.mathOperation.get().execute(monkeyJobByMonkeyNames);
        };
    }

    public static MonkeyJob from(String content) {
        Type type = Type.SPECIFIC_NUMBER;
        Optional<Long> optionalSpecificNumber = Optional.empty();
        Optional<MathOperation> optionalMathOperation = Optional.empty();
        try {
            optionalSpecificNumber = Optional.of(Long.parseLong(content));
        }
        catch (NumberFormatException e) {
            type = Type.MATH_OPERATION;
            String[] mathOperationArray = content.split(" ");
            optionalMathOperation = Optional.of(new MathOperation(mathOperationArray[1], mathOperationArray[0], mathOperationArray[2]));
        }

        return new MonkeyJob(type, optionalSpecificNumber, optionalMathOperation);
    }
}
