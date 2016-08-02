package sniper;

import sniper.ui.SnipersTableModel;

public enum SniperState {
    JOINING(SnipersTableModel.STATUS_JOINING),
    BIDDING(SnipersTableModel.STATUS_BIDDING),
    WINNING(SnipersTableModel.STATUS_WINNING),
    LOST(SnipersTableModel.STATUS_LOST),
    WON(SnipersTableModel.STATUS_HAS_WON);

    private final String statusName;

    SniperState(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}
