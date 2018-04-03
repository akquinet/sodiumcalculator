package de.akquinet.sodiumcalc;

import nz.sodium.Cell;
import nz.sodium.CellLoop;
import nz.sodium.Stream;
import nz.sodium.StreamSink;

public class CalculatorControllerV1Display implements CalculatorController {

    private final StreamSink<Long> clickedDigitS = new StreamSink<>();
    private final CellLoop<Long> displayC = new CellLoop<>();

    CalculatorControllerV1Display() {
        final Stream<Long> updatedEnteredNumberS =
                clickedDigitS.snapshot(displayC,
                        (digit, display) -> display * 10 + digit);
        displayC.loop(
                updatedEnteredNumberS.hold(0L));
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
