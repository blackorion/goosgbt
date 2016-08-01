package sniper.ui;

import java.util.EventListener;

/**
 * @author Sergey Ivanov.
 */
public interface UserRequestListener extends EventListener {
    void joinAuction(String itemId);
}
