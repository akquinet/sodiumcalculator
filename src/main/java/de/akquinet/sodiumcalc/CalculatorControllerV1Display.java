package de.akquinet.sodiumcalc;

import nz.sodium.*;

public class CalculatorControllerV1Display implements CalculatorController {

    private final StreamSink<Long> clickedDigitS = new StreamSink<>();
    private CellLoop<Long> displayC;

    CalculatorControllerV1Display() {
        Transaction.runVoid(() -> {
            displayC = new CellLoop<>();
            final Stream<Long> updatedEnteredNumberS =
                    clickedDigitS.snapshot(displayC,
                            (digit, main) -> main * 10 + digit);
            displayC.loop(
                    updatedEnteredNumberS.hold(0L));
        });
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
    }

    @Override
    public void pressMinus() {
    }

    @Override
    public void pressCompute() {
    }
}
