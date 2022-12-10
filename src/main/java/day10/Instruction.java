package day10;

import java.util.Optional;

public class Instruction {
    private final Command command;
    private final Optional<Integer> registerIncreaseValue;

    public Command getCommand() {
        return command;
    }
    public Optional<Integer> getRegisterIncreaseValue() {
        return registerIncreaseValue;
    }

    public Instruction(String content) {
        String[] instructionArr = content.split(" ");
        this.command = Command.fromName(instructionArr[0]);
        if (instructionArr.length > 1) {
            this.registerIncreaseValue = Optional.of(Integer.parseInt(instructionArr[1]));
        }
        else {
            this.registerIncreaseValue = Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "command=" + command +
                ", registerIncreaseValue=" + registerIncreaseValue +
                '}';
    }
}
