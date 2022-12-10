package day10;

import java.util.Arrays;

public enum Command {
    NOOP("noop", 1),
    ADDX("addx", 2);

    private final String name;
    private final int nbCycle;

    public int getNbCycle() {
        return nbCycle;
    }
    Command(String name, int nbCycle) {
        this.name = name;
        this.nbCycle = nbCycle;
    }

    public static Command fromName(String name) {
        return Arrays.stream(Command.values()).filter(c -> c.name.equals(name)).findFirst().get();
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", nbCycle=" + nbCycle +
                '}';
    }
}
