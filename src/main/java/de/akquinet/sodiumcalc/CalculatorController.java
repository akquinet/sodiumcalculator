package de.akquinet.sodiumcalc;

import nz.sodium.*;

public class CalculatorController {

    private final StreamSink<Long> clickedDigitS = new StreamSink<>();
    private CellLoop<Long> displayC;

    CalculatorController() {
        Transaction.runVoid(() -> {
            displayC = new CellLoop<>();
            final Stream<Long> newDisplayS =
                    clickedDigitS.snapshot(displayC,
                            (digit, display) -> display * 10 + digit);
            displayC.loop(newDisplayS.hold(0L));
        });
    }

    public Cell<Long> getDisplayCell() {
        return displayC;
    }

    public void pressDigit(Long digit) {
        clickedDigitS.send(digit);
    }
}
