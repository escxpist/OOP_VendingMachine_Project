package models;

public class Snack extends Product {

    public Snack(String name, double price, boolean isCrispy) {
        super(name, price);
    }

    @Override
    public String consume() {
        return "Ви отримали " + getName() + ". ";
    }
}