import models.Chocolate;
import models.Drink;
import models.Product;
import models.ProductSlot;
import models.Snack;
import services.VendingMachine;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine();
        Scanner scanner = new Scanner(System.in);

        vm.addProduct(new Drink("Coca-Cola", 25.00, 0.5), 5);
        vm.addProduct(new Drink("Water", 15.00, 0.5), 35);
        vm.addProduct(new Snack("Chips Lay's", 40.00, true), 3);
        vm.addProduct(new Chocolate("Milka", 30.00, "Milk"), 4);
        vm.addProduct(new Chocolate("Dark Chocolate", 35.00, "Dark 80%"), 2);

        boolean isRunning = true;

        System.out.println(">>> АВТОМАТ УВІМКНЕНО <<<");

        while (isRunning) {
            System.out.printf("\n=== ГОЛОВНЕ МЕНЮ (Баланс: %.2f грн) ===\n", vm.getBalance());
            System.out.println("1. Внести гроші");
            System.out.println("2. Купити товар (Меню)");
            System.out.println("3. Забрати решту");
            System.out.println("9. Сервісне меню (Адміністратор)");
            System.out.println("0. Вихід");

            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введіть суму поповнення: ");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        vm.insertMoney(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Помилка! Введіть число.");
                    }
                    break;

                case "2":
                    handlePurchase(vm, scanner);
                    break;

                case "3":
                    double change = vm.returnChange();
                    if (change > 0) {
                        System.out.printf("Ви забрали %.2f грн.\n", change);
                    } else {
                        System.out.println("Баланс порожній.");
                    }
                    break;

                case "9":
                    handleAdminMode(vm, scanner);
                    break;

                case "0":

                    if (vm.getBalance() > 0) {
                        System.out.println("⚠  УВАГА! Ви не забрали решту (" + vm.getBalance() + " грн)!");
                        System.out.println("Спочатку оберіть пункт '3', щоб забрати гроші.");
                    } else {
                        System.out.println("До побачення!");
                        isRunning = false;
                    }
                    break;

                default:
                    System.out.println("Невідома команда.");
            }
        }
    }

    private static void handlePurchase(VendingMachine vm, Scanner scanner) {
        vm.showMenu();
        System.out.println("0. НІЧОГО НЕ КУПУВАТИ (Повернутись назад)");
        System.out.print("Оберіть номер товару: ");
        int productNum;
        try {
            productNum = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Потрібно ввести число!");
            return;
        }

        if (productNum == 0) return;

        ProductSlot slot = vm.getSlot(productNum - 1);
        if (slot == null) {
            System.out.println("Такого товару не існує!");
            return;
        }

        System.out.print("Скільки штук ви хочете купити? ");
        int quantityToBuy;
        try {
            quantityToBuy = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Введіть коректне число.");
            return;
        }

        if (quantityToBuy <= 0) {
            System.out.println("Кількість має бути більше 0.");
            return;
        }

        if (quantityToBuy > slot.getQuantity()) {
            System.out.println("Помилка: В автоматі є лише " + slot.getQuantity() + " шт.");
            return;
        }

        double totalCost = slot.getProduct().getPrice() * quantityToBuy;
        if (vm.getBalance() < totalCost) {
            System.out.printf("Помилка: Недостатньо коштів. Ціна за %d шт: %.2f грн. Ваш баланс: %.2f грн\n",
                    quantityToBuy, totalCost, vm.getBalance());
            return;
        }

        System.out.println("\n--- Починаємо видачу " + quantityToBuy + " товарів ---");

        for (int i = 1; i <= quantityToBuy; i++) {
            System.out.println("\n Видача товару №" + i + "...");
            processSingleItemLogic(vm, slot, scanner);
        }
        System.out.println("\n--- Операцію завершено ---");
    }

    private static void processSingleItemLogic(VendingMachine vm, ProductSlot slot, Scanner scanner) {
        Product product = slot.getProduct();

        vm.processPayment(slot);
        
        if (vm.isStuck()) {
            System.out.println("ОЙ! Цей товар застряг!");
            System.out.println("1. Вдарити автомат (Шанс 50%)");
            System.out.println("Будь-яка інша кнопка - Повернути гроші за цю одиницю");

            String action = scanner.nextLine();
            if (action.equals("1")) {
                System.out.println("*БАХ!*");
                if (vm.tryShake()) {
                    System.out.println("Успіх! Товар випав: " + product.getName());
                } else {
                    System.out.println("Невдача. Товар лишився всередині.");
                    vm.refund(product.getPrice());
                }
            } else {
                vm.refund(product.getPrice());
            }
        } else {
            System.out.println("Випав товар: " + product.getName());
        }
    }

    private static void handleAdminMode(VendingMachine vm, Scanner scanner) {
        System.out.print("Введіть PIN-код: ");
        String pin = scanner.nextLine();

        if (vm.checkPin(pin)) {
            System.out.println("\n*** РЕЖИМ АДМІНІСТРАТОРА ***");
            vm.showMenu();
            System.out.print("Номер товару: ");
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.print("Кількість для поповнення: ");
                int amount = Integer.parseInt(scanner.nextLine());

                vm.refillSlot(index, amount);
            } catch (Exception e) {
                System.out.println("Помилка.");
            }
        } else {
            System.out.println("Невірний PIN!");
        }
    }
}