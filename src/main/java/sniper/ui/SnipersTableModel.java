package sniper.ui;

import sniper.SniperSnapshot;
import sniper.SniperState;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    private SniperSnapshot snapshot = SniperSnapshot.joining("");

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return snapshot.itemId;
            case LAST_PRICE:
                return snapshot.price;
            case LAST_BID:
                return snapshot.bid;
            case SNIPER_STATE:
                return snapshot.state.toString();
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void setStatusText(SniperState state) {
        sniperStateChanged(snapshot.newText(state));
    }

    public void sniperStateChanged(SniperSnapshot snapshot) {
        this.snapshot = snapshot;

        fireTableRowsUpdated(0, 0);
    }

    public enum Column {
        ITEM_IDENTIFIER,
        LAST_PRICE,
        LAST_BID,
        SNIPER_STATE;

        public static Column at(int offset) {
            return values()[offset];
        }
    }
}
