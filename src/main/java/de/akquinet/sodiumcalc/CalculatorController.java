package de.akquinet.sodiumcalc;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import nz.sodium.*;

public class CalculatorController {

    private final StreamSink<Long> clickedDigitS = new StreamSink<>();
    private final StreamSink<Operator> clickedOperatorS = new StreamSink<>();
    private CellLoop<Long> displayC;

    CalculatorController() {
        Transaction.runVoid(() -> {
            displayC = new CellLoop<>();
            final CellLoop<Long> mainC = new CellLoop<>();
            final CellLoop<Long> backC = new CellLoop<>();

            final Stream<Long> updatedEnteredNumberS =
                    clickedDigitS.snapshot(displayC,
                            (digit, display) -> display * 10 + digit);

            final Cell<Tuple3<Long, Long, Long>> calculatorStateC =
                    displayC.lift(mainC, backC, Tuple::of);

            final Stream<Tuple3<Long, Long, Long>> updatedStateS =
                    clickedOperatorS.snapshot(calculatorStateC, Operator::operate);

            displayC.loop(
                    updatedEnteredNumberS
                            .orElse(updatedStateS
                                    .map(Tuple3::_1))
                            .hold(0L));
            mainC.loop(
                    updatedEnteredNumberS
                            .orElse(updatedStateS
                                    .map(Tuple3::_2))
                            .hold(0L));
            backC.loop(
                    updatedStateS
                            .map(Tuple3::_3)
                            .hold(0L));
        });
    }

    public Cell<Long> getDisplayCell() {
        return displayC;
    }

    public void pressDigit(Long digit) {
        clickedDigitS.send(digit);
    }

    public void pressOperator(Operator operator) {
        clickedOperatorS.send(operator);
    }
}
