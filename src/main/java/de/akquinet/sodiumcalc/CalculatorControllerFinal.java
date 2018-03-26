package de.akquinet.sodiumcalc;

import io.vavr.Tuple;
import io.vavr.Tuple4;
import nz.sodium.*;

import static nz.sodium.Unit.UNIT;

public class CalculatorControllerFinal implements CalculatorController {

    private final StreamSink<Long> clickedDigitS = new StreamSink<>();
    private final StreamSink<Operator> clickedOperatorS = new StreamSink<>();
    private final StreamSink<Unit> clickCompute = new StreamSink<>();
    private CellLoop<Long> displayC;

    CalculatorControllerFinal() {
        displayC = new CellLoop<>();
        final CellLoop<Long> mainC = new CellLoop<>();
        final CellLoop<Long> backC = new CellLoop<>();
        final CellLoop<Operator> activeOperator = new CellLoop<>();

        final Stream<Long> updatedEnteredNumberS =
                clickedDigitS.snapshot(mainC,
                        (digit, main) -> main * 10 + digit);

        final Cell<Tuple4<Long, Long, Long, Operator>> calculatorStateC =
                displayC.lift(mainC, backC, activeOperator, Tuple::of);

        final Stream<Tuple4<Long, Long, Long, Operator>> updatedStateFromOperatorS =
                clickedOperatorS.snapshot(calculatorStateC, (operator, state) -> {
                    Long newBack = state._4.operate(state._2, state._3);
                    Long newMain = 0L;
                    return Tuple.of(newBack, newMain, newBack, operator);
                });

        final Stream<Tuple4<Long, Long, Long, Operator>> updatedStateFromCompute =
                clickCompute.snapshot(calculatorStateC, (unit, state) -> {
                    Long newBack = 0L;
                    Long newMain = 0L;
                    Long newDisplay = state._4.operate(state._2, state._3);
                    return Tuple.of(newDisplay, newMain, newBack, Operator.NONE);
                });

        final Stream<Tuple4<Long, Long, Long, Operator>> updatedStateS =
                updatedStateFromCompute.orElse(updatedStateFromOperatorS);

        displayC.loop(
                updatedEnteredNumberS
                        .orElse(updatedStateS
                                .map(Tuple4::_1))
                        .hold(0L));
        mainC.loop(
                updatedEnteredNumberS
                        .orElse(updatedStateS
                                .map(Tuple4::_2))
                        .hold(0L));
        backC.loop(
                updatedStateS
                        .map(Tuple4::_3)
                        .hold(0L));
        activeOperator.loop(
                updatedStateS
                        .map(Tuple4::_4)
                        .hold(Operator.NONE));
    }

    @Override
    public Cell<Long> getDisplayCell() {
        return displayC;
    }

    @Override
    public void pressDigit(Long digit) {
        clickedDigitS.send(digit);
    }

    @Override
    public void pressPlus() {
        clickedOperatorS.send(Operator.PLUS);
    }

    @Override
    public void pressMinus() {
        clickedOperatorS.send(Operator.MINUS);
    }

    @Override
    public void pressCompute() {
        clickCompute.send(UNIT);
    }
}
