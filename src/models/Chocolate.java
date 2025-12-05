package models;

public class Chocolate extends Product {
    private String cocoaType; // Наприклад: "Молочний", "Чорний", "Білий"

    public Chocolate(String name, double price, String cocoaType) {
        super(name, price);
        this.cocoaType = cocoaType;
    }

    @Override
    public String consume() {
        return "Ви їсте шоколад (" + cocoaType + ").";
    }
}