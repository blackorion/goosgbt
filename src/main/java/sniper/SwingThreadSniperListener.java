package sniper;

import sniper.ui.SnipersTableModel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingThreadSniperListener implements SniperListener {
    private final SnipersTableModel model;

    public SwingThreadSniperListener(SnipersTableModel model) {
        this.model = model;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        try {
            SwingUtilities.invokeAndWait(() -> model.sniperStateChanged(sniperSnapshot));
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
