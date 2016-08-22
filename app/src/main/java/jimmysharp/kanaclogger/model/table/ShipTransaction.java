package jimmysharp.kanaclogger.model.table;

import org.threeten.bp.ZonedDateTime;
import rx.Observable;

public class ShipTransaction {
    private final long id;
    private final ZonedDateTime date;
    private final Observable<Card> card;
    private final long quantity;

    public ShipTransaction(long id, ZonedDateTime date, Observable<Card> card, long quantity) {
        this.id = id;
        this.date = date;
        this.card = card;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }
    public ZonedDateTime getDate() {
        return date;
    }
    public Observable<Card> getCardObservable() {
        return card;
    }
    public Card getCard(){
        return card.toBlocking().firstOrDefault(null);
    }
    public long getQuantity() {
        return quantity;
    }
}
