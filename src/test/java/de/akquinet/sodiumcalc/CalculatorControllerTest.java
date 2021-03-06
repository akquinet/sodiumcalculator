package de.akquinet.sodiumcalc;

import nz.sodium.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorControllerTest {

    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
        Transaction.runVoid(() ->
                calculatorController = new CalculatorControllerFinal());
    }

    @Test
    void pressDigit123() {
        calculatorController.pressDigit(1L);
        calculatorController.pressDigit(2L);
        calculatorController.pressDigit(3L);
        assertEquals(Long.valueOf(123L), getDisplay());
    }

    private Long getDisplay() {
        return calculatorController.getDisplayCell().sample();
    }

    @Test
    void pressDigit007() {
        calculatorController.pressDigit(0L);
        calculatorController.pressDigit(0L);
        calculatorController.pressDigit(7L);
        assertEquals(Long.valueOf(7L), getDisplay());
    }

    @Test
    void pressOperator12Plus21() {
        calculatorController.pressDigit(1L);
        calculatorController.pressDigit(2L);
        calculatorController.pressPlus();
        assertEquals(Long.valueOf(12L), getDisplay());
        calculatorController.pressDigit(2L);
        calculatorController.pressDigit(1L);
        calculatorController.pressCompute();
        assertEquals(Long.valueOf(33L), getDisplay());
    }

    @Test
    void pressOperator30Minus12() {
        calculatorController.pressDigit(3L);
        calculatorController.pressDigit(0L);
        calculatorController.pressMinus();
        assertEquals(Long.valueOf(30L), getDisplay());
        calculatorController.pressDigit(1L);
        calculatorController.pressDigit(2L);
        calculatorController.pressCompute();
        assertEquals(Long.valueOf(18L), getDisplay());
    }

    @Test
    void pressOperator30Plus5Minus4() {
        calculatorController.pressDigit(3L);
        calculatorController.pressDigit(0L);
        calculatorController.pressPlus();
        assertEquals(Long.valueOf(30L), getDisplay());
        calculatorController.pressDigit(5L);
        calculatorController.pressMinus();
        assertEquals(Long.valueOf(35L), getDisplay());
        calculatorController.pressDigit(4L);
        calculatorController.pressCompute();
        assertEquals(Long.valueOf(31L), getDisplay());
    }
}
