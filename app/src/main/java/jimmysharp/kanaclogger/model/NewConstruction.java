package jimmysharp.kanaclogger.model;

import jimmysharp.kanaclogger.model.table.Card;

public class NewConstruction {
    private Card card;
    private int fuel;
    private int bullet;
    private int steel;
    private int bauxite;

    public NewConstruction(Card card, int fuel, int bullet, int steel, int bauxite) {
        this.card = card;
        this.fuel = fuel;
        this.bullet = bullet;
        this.steel = steel;
        this.bauxite = bauxite;
    }

    public Card getCard() {
        return card;
    }
    public int getFuel() {
        return fuel;
    }
    public int getBullet() {
        return bullet;
    }
    public int getSteel() {
        return steel;
    }
    public int getBauxite() {
        return bauxite;
    }
}
