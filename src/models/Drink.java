package models;

public class Drink extends Product {

    public Drink(String name, double price, double volume) {
        super(name, price);
    }

    @Override
    public String consume() {
        return "Ви п'єте " + getName() + ".";
    }
}