package de.akquinet.sodiumcalc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        //noinspection SpellCheckingInspection
        stage.setTitle("Socalc");

        final CalculatorView calculatorView = new CalculatorView();
        final Scene scene = new Scene(calculatorView);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
