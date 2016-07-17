package sniper;

import sniper.ui.MainWindow;

import javax.swing.*;

public class SniperStateDisplayer implements SniperListener {
    private final MainWindow ui;

    public SniperStateDisplayer(MainWindow ui) {
        this.ui = ui;
    }

    @Override
    public void sniperLost() {
        showStatus(SniperState.LOST);
    }

    @Override
    public void sniperWon() {
        showStatus(SniperState.WON);
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(()-> ui.sniperStatusChanged(sniperSnapshot));
    }

    private void showStatus(SniperState status) {
        SwingUtilities.invokeLater(() -> ui.showStatus(status));
    }
}
