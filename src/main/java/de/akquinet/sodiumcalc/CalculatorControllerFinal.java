package de.akquinet.sodiumcalc;

import nz.sodium.*;

import static nz.sodium.Unit.UNIT;

public class CalculatorControllerFinal implements CalculatorController {

    private static class CombinedState {
        final Long display;
        final Long back;
        final Long main;
        final Operator activeOperator;

        CombinedState(Long display, Long main, Long back, Operator activeOperator) {
            this.display = display;
            this.back = back;
            this.main = main;
            this.activeOperator = activeOperator;
        }
    }


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

        final Cell<CombinedState> calculatorStateC =
                displayC.lift(mainC, backC, activeOperator, CombinedState::new);

        final Stream<CombinedState> updatedStateFromOperatorS =
                clickedOperatorS.snapshot(calculatorStateC, (operator, state) -> {
                    Long result = state.activeOperator.operate(state.main, state.back);
                    Long newMain = 0L;
                    return new CombinedState(result, newMain, result, operator);
                });

        final Stream<CombinedState> updatedStateFromCompute =
                clickCompute.snapshot(calculatorStateC, (unit, state) -> {
                    Long newDisplay = state.activeOperator.operate(state.main, state.back);
                    return new CombinedState(newDisplay, 0L, 0L, Operator.NONE);
                });

        final Stream<CombinedState> updatedStateS =
                updatedStateFromCompute.orElse(updatedStateFromOperatorS);

        displayC.loop(
                updatedEnteredNumberS
                        .orElse(updatedStateS
                                .map(s -> s.display))
                        .hold(0L));
        mainC.loop(
                updatedEnteredNumberS
                        .orElse(updatedStateS
                                .map(s -> s.main))
                        .hold(0L));
        backC.loop(
                updatedStateS
                        .map(s -> s.back)
                        .hold(0L));
        activeOperator.loop(
                updatedStateS
                        .map(s -> s.activeOperator)
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
