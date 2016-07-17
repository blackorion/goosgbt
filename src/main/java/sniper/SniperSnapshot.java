package sniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SniperSnapshot {
    public final String itemId;
    public final int price;
    public final int bid;
    public final SniperState state;

    public SniperSnapshot(String itemId, int price, int bid, SniperState state) {
        this.itemId = itemId;
        this.price = price;
        this.bid = bid;
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SniperSnapshot that = (SniperSnapshot) o;

        return new EqualsBuilder()
                .append(itemId, that.itemId)
                .append(price, that.price)
                .append(bid, that.bid)
                .append(state, that.state)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(itemId)
                .append(price)
                .append(bid)
                .append(state)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "SniperSnapshot{" +
                "itemId='" + itemId + '\'' +
                ", price=" + price +
                ", bid=" + bid +
                ", state=" + state +
                '}';
    }

    public static SniperSnapshot joining(String itemId) {
        return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
    }

    public SniperSnapshot bidding(Integer price, int bid) {
        return new SniperSnapshot(itemId, price, bid, SniperState.BIDDING);
    }

    public SniperSnapshot winning(Integer price) {
        return new SniperSnapshot(itemId, price, bid, SniperState.WINNING);
    }

    public SniperSnapshot newText(SniperState state) {
        return new SniperSnapshot(itemId, price, bid, state);
    }
}
