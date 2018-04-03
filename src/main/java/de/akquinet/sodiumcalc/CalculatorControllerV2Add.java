package de.akquinet.sodiumcalc;

import nz.sodium.*;

import static nz.sodium.Unit.UNIT;

public class CalculatorControllerV2Add implements CalculatorController {

    private static class CombinedState {
        final Long display;
        final Long back;
        final Long main;

        CombinedState(Long display, Long main, Long back) {
            this.display = display;
            this.back = back;
            this.main = main;
        }
    }


    private final StreamSink<Long> clickedDigitS = new StreamSink<>();
    private final StreamSink<Unit> clickedPlusS = new StreamSink<>();
    private final CellLoop<Long> displayC = new CellLoop<>();

    CalculatorControllerV2Add() {
        final CellLoop<Long> mainC = new CellLoop<>();
        final CellLoop<Long> backC = new CellLoop<>();

        final Stream<Long> updatedEnteredNumberS =
                clickedDigitS.snapshot(mainC,
                        (digit, main) -> main * 10 + digit);

        final Cell<CombinedState> calculatorStateC =
                displayC.lift(mainC, backC, CombinedState::new);

        final Stream<CombinedState> updatedStateFromPlusS =
                clickedPlusS.snapshot(calculatorStateC, (unit, state) -> {
                    Long newBack = state.main + state.back;
                    Long newMain = 0L;
                    return new CombinedState(newBack, newMain, newBack);
                });

        displayC.loop(
                updatedEnteredNumberS
                        .orElse(updatedStateFromPlusS
                                .map(s -> s.display))
                        .hold(0L));
        mainC.loop(
                updatedEnteredNumberS
                        .orElse(updatedStateFromPlusS
                                .map(s -> s.main))
                        .hold(0L));
        backC.loop(
                updatedStateFromPlusS
                        .map(s -> s.back)
                        .hold(0L));

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
        clickedPlusS.send(UNIT);
    }

    @Override
    public void pressMinus() {
    }

    @Override
    public void pressCompute() {
    }
}
