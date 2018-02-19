package de.akquinet.sodiumcalc;

import java.util.function.BinaryOperator;

public enum Operator {
    PLUS(Long::sum ),
    MINUS((main,back) -> back - main),
    NONE((main,back) -> main);

    private final BinaryOperator<Long> operation;

    Operator(BinaryOperator<Long> operation) {
        this.operation = operation;
    }

    public Long operate(Long main, Long back) {
        return operation.apply(main, back);
    }
}
