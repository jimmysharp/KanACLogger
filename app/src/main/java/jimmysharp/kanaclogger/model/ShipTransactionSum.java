package jimmysharp.kanaclogger.model;

import jimmysharp.kanaclogger.model.table.CardType;
import jimmysharp.kanaclogger.model.table.Ship;

public class ShipTransactionSum {
    private final long id;
    private final Ship ship;
    private final CardType cardType;
    private final long quantity;

    public ShipTransactionSum(long id, Ship ship, CardType cardType, long quantity) {
        this.id = id;
        this.ship = ship;
        this.cardType = cardType;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }
    public CardType getCardType() {
        return cardType;
    }
    public Ship getShip() {
        return ship;
    }
    public long getQuantity() {
        return quantity;
    }
}
