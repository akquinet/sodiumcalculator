package de.akquinet.sodiumcalc;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class CalculatorView extends GridPane  {

    private final TextField outputTextField = new TextField();

    CalculatorView() {
        adaptBasicLayout();
        createNumberButtons();

        add(outputTextField, 0,0,4,1);
        adaptAndAddTextField();
        adaptAndAddPlusButton();
        adaptAndAddMinusButton();
        adaptAndAddEqualsButton();
    }

    private void adaptAndAddMinusButton() {
        final Button button = new Button("-");
        add(button, 3 , 2);
        //button.setOnAction(event -> );
    }

    private void adaptAndAddPlusButton() {
        final Button button = new Button("+");
        add(button, 3 , 1);
    }

    private void adaptAndAddTextField() {
        outputTextField.setEditable(false);
        outputTextField.setMinWidth(10);
        outputTextField.setPrefColumnCount(5);
        outputTextField.setAlignment(Pos.CENTER_RIGHT);
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

    private void adaptAndAddEqualsButton() {
        final Button button = new Button("=");
        add(button, 3 , 3, 1, 2);
        button.setMaxHeight(1000);
    }

    private void createNumberButton0() {
        final Button button0 = new Button("0");
        button0.setMaxWidth(1000);
        add(button0, 0,4, 3,1);

    }

    private void createNumberButton1To9(int number) {
        assert (0 < number) && (number <= 9) : number + " not in 1..9";

        final Button button = new Button("" + number);
        final int numberMinus1 = number - 1;
        add(button, numberMinus1 % 3, 3-(numberMinus1 / 3));

    }
}