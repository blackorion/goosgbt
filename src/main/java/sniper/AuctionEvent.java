package sniper;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static sniper.AuctionEventListener.PriceSource.FromOtherBidder;
import static sniper.AuctionEventListener.PriceSource.FromSniper;

public class AuctionEvent {
    private final Map<String, String> fields;

    public AuctionEvent(Map<String, String> elements) {
        this.fields = elements;
    }

    public String type() {
        return fields.get("Event");
    }

    public int currentPrice() {
        return Integer.parseInt(fields.get("CurrentPrice"));
    }

    public int increment() {
        return Integer.parseInt(fields.get("Increment"));
    }

    public String bidder() {
        return fields.get("Bidder");
    }

    public AuctionEventListener.PriceSource isFrom(String sniperId) {
        return bidder().equalsIgnoreCase(sniperId) ? FromSniper : FromOtherBidder;
    }

    public static AuctionEvent from(String messageBody) {
        final Map<String, String> elements = Arrays.stream(messageBody.split(";"))
                .map(el -> el.split(":"))
                .collect(Collectors.toMap(AuctionEvent::getKey, AuctionEvent::getValue));

        return new AuctionEvent(elements);
    }

    private static String getKey(String[] pair) {
        return pair[0].trim();
    }

    private static String getValue(String[] pair) {
        return pair[1].trim();
    }
}
