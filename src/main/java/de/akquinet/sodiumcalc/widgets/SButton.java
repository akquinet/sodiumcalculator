package de.akquinet.sodiumcalc.widgets;

import javafx.application.Platform;
import javafx.scene.control.Button;
import nz.sodium.*;

public class SButton extends Button {
    public SButton(String label) {
        this(label, new Cell<Boolean>(true));
    }

    public SButton(String label, Cell<Boolean> enabled) {
        super(label);
        StreamSink<Unit> sClickedSink = new StreamSink<>();
        this.sClicked = sClickedSink;
        setOnAction(event -> sClickedSink.send(Unit.UNIT));

        // Do it at the end of the transaction so it works with looped cells
        Transaction.post(() -> setDisable(! enabled.sample()));
        l = Operational.updates(enabled).listen(
                ena -> {
                    if (Platform.isFxApplicationThread())
                        this.setDisable(! ena);
                    else {
                        Platform.runLater(() -> {
                            this.setDisable(!ena);
                        });
                    }
                }
        );
    }

    private final Listener l;
    public final Stream<Unit> sClicked;
}