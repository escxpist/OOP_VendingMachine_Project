package models;

public class ProductSlot {
    private Product product;
    private int quantity;

    // Вимога №1: Ліміт 30 штук
    public static final int MAX_CAPACITY = 30;

    public ProductSlot(Product product, int quantity) {
        this.product = product;
        // При створенні обрізаємо, якщо передали більше 30
        this.quantity = Math.min(quantity, MAX_CAPACITY);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    // Оновлений метод поповнення з врахуванням ліміту
    public void addQuantity(int amount) {
        if (quantity + amount > MAX_CAPACITY) {
            System.out.println("⚠ Увага: Не можна покласти більше " + MAX_CAPACITY + " шт. Слот заповнено до максимуму.");
            quantity = MAX_CAPACITY;
        } else {
            quantity += amount;
        }
    }

    @Override
    public String toString() {
        if (quantity > 0) {
            return product.toString() + " [Наявність: " + quantity + "/" + MAX_CAPACITY + "]";
        } else {
            return product.getName() + " [НЕМАЄ В НАЯВНОСТІ]";
        }
    }
}