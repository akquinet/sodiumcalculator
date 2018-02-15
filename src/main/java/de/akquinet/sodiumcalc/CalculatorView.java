package de.akquinet.sodiumcalc;

import de.akquinet.sodiumcalc.widgets.SButton;
import de.akquinet.sodiumcalc.widgets.SLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import nz.sodium.Cell;
import nz.sodium.Stream;

import java.util.HashMap;
import java.util.Map;

class CalculatorView extends GridPane  {

    private SLabel outputTextField;
    private Map<Integer, SButton> numberButtons;

    CalculatorView() {
        adaptBasicLayout();
        createNumberButtons();

        adaptAndAddPlusButton();
        adaptAndAddMinusButton();
        adaptAndAddEqualsButton();

        Stream<Long> sDigit1 = numberButtons.get(1).sClicked.map(u -> 1L);
        Stream<Long> sDigit2 = numberButtons.get(2).sClicked.map(u -> 2L);

        final Stream<Long> digits = sDigit1.merge(sDigit2, (n1, n2) -> n1);

        Cell<Long> display = digits.hold(0L);


        adaptAndAddTextField(display);
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

    private void adaptAndAddTextField(Cell<Long> display) {
        final Cell<String> displayString = display.map(longNumber -> "" + longNumber);
        outputTextField = new SLabel(displayString);
        outputTextField.setMinWidth(10);
        outputTextField.setAlignment(Pos.CENTER_RIGHT);
        add(outputTextField, 0,0,4,1);
    }

    private void adaptBasicLayout() {
        setAlignment(Pos.CENTER);
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(5, 5, 5, 5));
    }

    private void createNumberButtons() {
        numberButtons = new HashMap<Integer, SButton>();
        for (int i = 1; i <= 9; i++) {
            createNumberButton1To9(numberButtons, i);
        }
        createNumberButton0(numberButtons);
    }

    private void adaptAndAddEqualsButton() {
        final SButton button = new SButton("=");
        add(button, 3 , 3, 1, 2);
        button.setMaxHeight(1000);
    }

    private void createNumberButton0(Map<Integer, SButton> numberButtons) {
        final SButton button0 = new SButton("0");
        button0.setMaxWidth(1000);
        add(button0, 0,4, 3,1);
        numberButtons.put(0, button0);

    }

    private void createNumberButton1To9(Map<Integer, SButton> numberButtons, int number) {
        assert (0 < number) && (number <= 9) : number + " not in 1..9";

        final SButton button = new SButton("" + number);
        final int numberMinus1 = number - 1;
        add(button, numberMinus1 % 3, 3-(numberMinus1 / 3));
        numberButtons.put(number, button);

    }
}