package sniper;

import com.sun.istack.internal.NotNull;

/**
 * @author Sergey Ivanov.
 */
public class Item {
    public final String identifier;
    public final int stopPrice;

    public Item(@NotNull String identifier, int stopPrice) {
        this.identifier = identifier;
        this.stopPrice = stopPrice;
    }

    public boolean allowsBid(int bid) {
        return bid < stopPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (stopPrice != item.stopPrice)
            return false;
        else
            return identifier != null
                    ? identifier.equals(item.identifier)
                    : item.identifier == null;

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + stopPrice;

        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "identifier='" + identifier + '\'' +
                ", stopPrice=" + stopPrice +
                '}';
    }
}
