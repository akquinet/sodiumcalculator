package de.akquinet.sodiumcalc;

import nz.sodium.Cell;
import nz.sodium.StreamSink;

public class CalculatorController {

    private final StreamSink<Long> sClickedDigit = new StreamSink<>();
    private Cell<Long> displayCell;

    CalculatorController() {
        displayCell = sClickedDigit.hold(0L);
    }

    public Cell<Long> getDisplayCell() {
        return displayCell;
    }

    public void pressDigit(Long digit) {
        sClickedDigit.send(digit);
    }
}
