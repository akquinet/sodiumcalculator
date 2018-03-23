package de.akquinet.sodiumcalc;

import nz.sodium.Cell;

public interface CalculatorController {
    Cell<Long> getDisplayCell();

    void pressDigit(Long digit);

    void pressPlus();

    void pressMinus();

    void pressCompute();
}
