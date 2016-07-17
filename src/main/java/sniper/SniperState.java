package sniper;

import sniper.ui.MainWindow;

public enum SniperState {
    JOINING(MainWindow.STATUS_JOINING),
    BIDDING(MainWindow.STATUS_BIDDING),
    WINNING(MainWindow.STATUS_WINNING),
    LOST(MainWindow.STATUS_LOST),
    WON(MainWindow.STATUS_HAS_WON);

    private final String statusName;

    SniperState(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}
