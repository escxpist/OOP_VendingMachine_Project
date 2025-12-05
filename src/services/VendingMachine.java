package services;

import models.Product;
import models.ProductSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VendingMachine {
    private List<ProductSlot> slots;
    private double balance;

    private final String ADMIN_PIN = "1234";
    private final Random random = new Random();

    public VendingMachine() {
        this.slots = new ArrayList<>();
        this.balance = 0.0;
    }

    // Оновлений метод додавання (перевірка ліміту вже всередині слота, але тут ми просто передаємо дані)
    public void addProduct(Product product, int quantity) {
        if (quantity > ProductSlot.MAX_CAPACITY) {
            quantity = ProductSlot.MAX_CAPACITY; // Обрізаємо зайве
        }
        slots.add(new ProductSlot(product, quantity));
    }

    public void showMenu() {
        System.out.println("\n--- Меню автомата ---");
        for (int i = 0; i < slots.size(); i++) {
            System.out.println((i + 1) + ". " + slots.get(i));
        }
        System.out.println("---------------------");
    }

    public void insertMoney(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Ви внесли: %.2f грн. Баланс: %.2f грн\n", amount, balance);
        }
    }

    public double returnChange() {
        double change = balance;
        balance = 0;
        return change;
    }

    public double getBalance() {
        return balance;
    }

    public int getSlotsCount() {
        return slots.size();
    }

    public ProductSlot getSlot(int index) {
        if (index >= 0 && index < slots.size()) {
            return slots.get(index);
        }
        return null;
    }

    public boolean processPayment(ProductSlot slot) {
        if (balance >= slot.getProduct().getPrice()) {
            balance -= slot.getProduct().getPrice();
            slot.reduceQuantity();
            return true;
        }
        return false;
    }

    public void refund(double amount) {
        balance += amount;
        System.out.printf(">> Повернення коштів на баланс: %.2f грн.\n", amount);
    }

    public boolean isStuck() {
        return random.nextDouble() < 0.2;
    }

    public boolean tryShake() {
        return random.nextDouble() < 0.5;
    }

    public boolean checkPin(String pin) {
        return ADMIN_PIN.equals(pin);
    }

    public void refillSlot(int index, int amount) {
        ProductSlot slot = getSlot(index);
        if (slot != null) {
            // Вся логіка перевірки ліміту тепер всередині slot.addQuantity
            slot.addQuantity(amount);
            System.out.println("Операцію виконано. Поточна кількість: " + slot.getQuantity());
        }
    }
}