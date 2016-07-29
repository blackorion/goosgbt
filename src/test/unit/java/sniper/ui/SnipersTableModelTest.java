package sniper.ui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import sniper.SniperSnapshot;
import sniper.SniperState;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by orion on 16.07.16.
 */
public class SnipersTableModelTest {

    private TableModelListener listener = mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();

    @Before
    public void attachModelListener() throws Exception {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(SnipersTableModel.Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));

        assertColumnEquals(0, SnipersTableModel.Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(0, SnipersTableModel.Column.LAST_PRICE, 555);
        assertColumnEquals(0, SnipersTableModel.Column.LAST_BID, 666);
        assertColumnEquals(0, SnipersTableModel.Column.SNIPER_STATE, MainWindow.STATUS_BIDDING);
    }

    @Test
    public void firesStateToTable() {
        model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));

        final ArgumentCaptor<TableModelEvent> captor = ArgumentCaptor.forClass(TableModelEvent.class);
        verify(listener).tableChanged(captor.capture());
        assertThat(captor.getValue(), samePropertyValuesAs(new TableModelEvent(model, 0)));
    }

    @Test
    public void notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");

        assertThat(model.getRowCount(), is(0));

        model.addSniper(joining);

        assertThat(model.getRowCount(), is(1));
        assertRowMatchesSnapshot(0, joining);
    }

    private void assertRowMatchesSnapshot(int rowIndex, SniperSnapshot snapshot) {
        assertColumnEquals(rowIndex, SnipersTableModel.Column.ITEM_IDENTIFIER, snapshot.itemId);
        assertColumnEquals(rowIndex, SnipersTableModel.Column.LAST_PRICE, snapshot.price);
        assertColumnEquals(rowIndex, SnipersTableModel.Column.LAST_BID, snapshot.bid);
        assertColumnEquals(rowIndex, SnipersTableModel.Column.SNIPER_STATE, snapshot.state.toString());
    }

    private void assertColumnEquals(int rowIndex, SnipersTableModel.Column column, Object expected) {
        int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

}