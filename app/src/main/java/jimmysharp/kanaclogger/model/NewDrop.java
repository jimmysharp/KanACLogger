package jimmysharp.kanaclogger.model;

import jimmysharp.kanaclogger.model.table.Card;
import jimmysharp.kanaclogger.model.table.SubMap;

public class NewDrop {
    private Card card;
    private SubMap subMap;

    public NewDrop(Card card, SubMap subMap) {
        this.card = card;
        this.subMap = subMap;
    }

    public Card getCard() {
        return card;
    }
    public SubMap getSubMap() {
        return subMap;
    }
}
