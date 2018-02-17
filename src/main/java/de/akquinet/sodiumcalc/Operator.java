package de.akquinet.sodiumcalc;

import io.vavr.Function1;
import io.vavr.Tuple3;

public enum Operator {
    PLUS( state -> state ), MINUS(state -> state ), COMPUTE(state -> state );

    private final Function1<Tuple3<Long,Long,Long>, Tuple3<Long,Long,Long>> operation;

    Operator(Function1<Tuple3<Long,Long,Long>, Tuple3<Long,Long,Long>> operation) {
        this.operation = operation;
    }

    public Tuple3<Long,Long,Long> operate(Tuple3<Long, Long, Long> state) {
        return operation.apply(state);
    }
}
