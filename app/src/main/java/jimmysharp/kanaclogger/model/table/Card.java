package jimmysharp.kanaclogger.model.table;

import rx.Observable;

public class Card {
    private final long id;
    private final Observable<Ship> ship;
    private final Observable<CardType> cardType;

    public Card(long id, Observable<Ship> ship, Observable<CardType> cardType) {
        this.id = id;
        this.ship = ship;
        this.cardType = cardType;
    }

    public long getId() {
        return id;
    }
    public Observable<Ship> getShipObservable() {
        return ship;
    }
    public Ship getShip(){
        return ship.toBlocking().firstOrDefault(null);
    }
    public Observable<CardType> getCardTypeObservable() {
        return cardType;
    }
    public CardType getCardType(){
        return cardType.toBlocking().firstOrDefault(null);
    }
}
