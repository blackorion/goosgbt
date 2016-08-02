package sniper.ui;

import sniper.Item;

import java.util.EventListener;

/**
 * @author Sergey Ivanov.
 */
public interface UserRequestListener extends EventListener {
    void joinAuction(Item item);
}
