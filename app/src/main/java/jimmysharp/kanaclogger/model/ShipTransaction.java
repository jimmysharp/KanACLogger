package jimmysharp.kanaclogger.model;

import java.util.Date;

import rx.Observable;

public class ShipTransaction {
    private final long id;
    private final Date date;
    private final Observable<Ship> ship;
    private final Observable<CardType> cardType;
    private final long quantity;

    public ShipTransaction(long id, Date date, Observable<Ship> ship, Observable<CardType> cardType, long quantity) {
        this.id = id;
        this.date = date;
        this.ship = ship;
        this.cardType = cardType;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }
    public Date getDate() {
        return date;
    }
    public Observable<Ship> getShip() {
        return ship;
    }
    public Observable<CardType> getCardType() {
        return cardType;
    }
    public long getQuantity() {
        return quantity;
    }
}
