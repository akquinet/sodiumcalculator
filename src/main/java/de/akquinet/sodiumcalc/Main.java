package de.akquinet.sodiumcalc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.sodium.Transaction;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        //noinspection SpellCheckingInspection
        stage.setTitle("Socalc");

        Transaction.runVoid(() -> {
            final CalculatorControllerV2Add controllerV2Add = new CalculatorControllerV2Add();
            final CalculatorControllerV1Display controllerV1Display = new CalculatorControllerV1Display();
            final CalculatorControllerFinal controllerFinal = new CalculatorControllerFinal();

            final CalculatorView calculatorView = new CalculatorView(controllerV2Add);
            final Scene scene = new Scene(calculatorView);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        });
    }
}
