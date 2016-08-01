package sniper;

import sniper.ui.SnipersTableModel;

public class SniperStateDisplayer implements SniperListener {
    private final SnipersTableModel ui;

    public SniperStateDisplayer(SnipersTableModel ui) {
        this.ui = ui;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        ui.sniperStateChanged(sniperSnapshot);
    }
}
