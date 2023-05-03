package day25;

import java.util.List;

public record Bob(List<SNAFU> snafus) {
    private long decimalSum() {
        return this.snafus.stream().map(SNAFU::toDecimal).reduce(0L, Long::sum);
    }

    public SNAFU sum() {
        return SNAFU.fromDecimal(this.decimalSum());
    }
}
