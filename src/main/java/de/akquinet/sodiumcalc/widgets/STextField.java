package de.akquinet.sodiumcalc.widgets;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import nz.sodium.*;

public class STextField extends TextField
{
    public STextField(String initText) {
        this(new Stream<String>(), initText, 15);
    }
    public STextField(String initText, int width) {
        this(new Stream<String>(), initText, width);
    }
    public STextField(Stream<String> sText, String initText) {
        this(sText, initText, 15);
    }
    public STextField(Stream<String> sText, String initText, int width) {
        this(sText, initText, width, new Cell<Boolean>(true));
    }

    public STextField(String initText, int width, Cell<Boolean> enabled) {
        this(new Stream<String>(), initText, width, enabled);
    }

    public STextField(Stream<String> sText, String initText, int width, Cell<Boolean> enabled) {
        super(initText);
        setWidth(width);

        allow = sText.map(u -> 1)  // Block local changes until remote change has
                // been completed in the GUI
                .orElse(sDecrement)
                .accum(0, (d, b) -> b + d).map(b -> b == 0);

        final StreamSink<String> sUserChangesSnk = new StreamSink<String>();
        this.sUserChanges = sUserChangesSnk;
        this.text = sUserChangesSnk.gate(allow).orElse(sText).hold(initText);

        // Do it at the end of the transaction so it works with looped cells
        Transaction.post(() -> setDisable(! enabled.sample()));
        l = sText.listen(text -> {
            Platform.runLater(() -> {
                setText(text);
                sDecrement.send(-1);  // Re-allow blocked remote changes
            });
        }).append(
                Operational.updates(enabled).listen(
                        ena -> {
                            if (Platform.isFxApplicationThread())
                                this.setDisable(! ena);
                            else {
                                Platform.runLater(() -> {
                                    this.setDisable(! ena);
                                });
                            }
                        }
                )
        );
    }
    private final StreamSink<Integer> sDecrement = new StreamSink<>();
    private final Cell<Boolean> allow;
    private final Listener l;
    public final Cell<String> text;
    public final Stream<String> sUserChanges;
}