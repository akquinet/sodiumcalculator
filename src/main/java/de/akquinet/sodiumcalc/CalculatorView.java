package de.akquinet.sodiumcalc;

import de.akquinet.sodiumcalc.widgets.SButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import nz.sodium.Operational;

import static de.akquinet.sodiumcalc.Operator.COMPUTE;
import static de.akquinet.sodiumcalc.Operator.MINUS;
import static de.akquinet.sodiumcalc.Operator.PLUS;

class CalculatorView extends GridPane {

    private final CalculatorController calculatorController;
    private TextField displayTF;

    CalculatorView(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;

        adaptBasicLayout();
        createNumberButtons();

        adaptAndAddPlusButton();
        adaptAndAddMinusButton();
        adaptAndAddEqualsButton();

        adaptAndAddDisplayField();
    }

    private void adaptBasicLayout() {
        setAlignment(Pos.CENTER);
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(5, 5, 5, 5));
    }

    private void createNumberButtons() {
        for (int i = 1; i <= 9; i++) {
            createNumberButton1To9(i);
        }
        createNumberButton0();
    }

    private void adaptAndAddPlusButton() {
        final Button button = new Button("+");
        button.setOnAction(event -> calculatorController.pressOperator(PLUS));
        add(button, 3, 1);
    }

    private void adaptAndAddMinusButton() {
        final Button button = new Button("-");
        button.setOnAction(event -> calculatorController.pressOperator(MINUS));
        add(button, 3, 2);
    }

    private void adaptAndAddEqualsButton() {
        final SButton button = new SButton("=");
        button.setOnAction(event -> calculatorController.pressOperator(COMPUTE));
        add(button, 3, 3, 1, 2);
        button.setMaxHeight(1000);
    }

    private void adaptAndAddDisplayField() {
        displayTF = new TextField();
        displayTF.setPrefColumnCount(8);
        displayTF.setAlignment(Pos.CENTER_RIGHT);
        add(displayTF, 0, 0, 4, 1);
        Operational.updates(calculatorController.getDisplayCell())
                .listen(value -> displayTF.setText("" + value));
    }

    private void createNumberButton1To9(int number) {
        assert (0 < number) && (number <= 9) : number + " not in 1..9";

        final Button button = new Button("" + number);
        button.setOnAction(event -> calculatorController.pressDigit((long) number));
        final int numberMinus1 = number - 1;
        add(button, numberMinus1 % 3, 3 - (numberMinus1 / 3));
    }

    private void createNumberButton0() {
        final SButton button0 = new SButton("0");
        button0.setMaxWidth(1000);
        button0.setOnAction(event -> calculatorController.pressDigit(0L));
        add(button0, 0, 4, 3, 1);
    }
}