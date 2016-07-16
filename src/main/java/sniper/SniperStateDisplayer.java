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
        showStatus(MainWindow.STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
        showStatus(MainWindow.STATUS_BIDDING);
    }

    @Override
    public void sniperWinning() {
        showStatus(MainWindow.STATUS_WINNING);
    }

    @Override
    public void sniperWon() {
        showStatus(MainWindow.STATUS_HAS_WON);
    }

    private void showStatus(String status) {
        SwingUtilities.invokeLater(() -> ui.showStatus(status));
    }

}
