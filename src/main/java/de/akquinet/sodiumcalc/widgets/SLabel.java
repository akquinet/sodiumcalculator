package de.akquinet.sodiumcalc.widgets;

import javafx.application.Platform;
import javafx.scene.control.Label;
import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Operational;
import nz.sodium.Transaction;

import javax.swing.*;


public class SLabel extends Label
{
    public SLabel(Cell<String> text) {
        super("");
        l = Operational.updates(text).listen(t -> {
            if (Platform.isFxApplicationThread())
                setText(t);
            else
                Platform.runLater(() -> {
                    setText(t);
                });
        });
        // Set the text at the end of the transaction so SLabel works
        // with CellLoops.
        Transaction.post(
                () -> SwingUtilities.invokeLater(() -> {
                    setText(text.sample());
                })
        );
    }

    private final Listener l;
}