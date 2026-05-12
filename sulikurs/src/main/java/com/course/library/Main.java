package com.course.library;

import java.util.List;
import java.util.Scanner;

/**
 * Точка входа: меню и обработка ошибок ввода и бизнес-логики.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Library library = demoLibrary();
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Консольная библиотека (курсовой проект, ООП) ===");
        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Выберите пункт: ");
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> printCatalog(library);
                    case "2" -> searchCatalog(scanner, library);
                    case "3" -> borrowFlow(scanner, library);
                    case "4" -> returnFlow(scanner, library);
                    case "5" -> printMembers(library);
                    case "6" -> loanHintFlow(scanner, library);
                    case "7" -> filterByYearFlow(scanner, library);
                    case "0" -> exit = true;
                    default -> System.out.println("Неизвестный пункт меню, попробуйте снова.");
                }
            } catch (LibraryException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода числа: введите целое число (год).");
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректные данные: " + e.getMessage());
            } finally {
                if (!"0".equals(choice)) {
                    System.out.println("--- операция завершена ---\n");
                }
            }
        }
        scanner.close();
        System.out.println("До свидания!");
    }

    private static Library demoLibrary() {
        Library library = new Library();
        library.addItem(new Book("B-001", "Философия Java", "Брюс Эккель", 2015, 2, "978-5-8459-2019-5"));
        library.addItem(new Book("B-002", "Чистый код", "Роберт Мартин", 2019, 1, "978-5-00117-424-2"));
        library.addItem(new Magazine("M-100", "Наука и жизнь", 2024, 3, 5));
        library.registerMember(new Member("1001", "Иванов Иван"));
        library.registerMember(new Member("1002", "Петрова Мария"));
        return library;
    }

    private static void printMenu() {
        System.out.println("1 — показать каталог");
        System.out.println("2 — поиск по ключевому слову");
        System.out.println("3 — выдать книгу/журнал читателю");
        System.out.println("4 — принять возврат");
        System.out.println("5 — список читателей");
        System.out.println("6 — подсказка по сроку выдачи (полиморфизм)");
        System.out.println("7 — каталог с указанного года издания (ввод числа)");
        System.out.println("0 — выход");
    }

    private static void printCatalog(Library library) {
        List<String> lines = library.listCatalogLines();
        if (lines.isEmpty()) {
            System.out.println("Каталог пуст.");
            return;
        }
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static void searchCatalog(Scanner scanner, Library library) {
        System.out.print("Ключевое слово: ");
        String kw = scanner.nextLine();
        List<LibraryItem> found = library.search(kw);
        if (found.isEmpty()) {
            System.out.println("Ничего не найдено.");
            return;
        }
        for (LibraryItem item : found) {
            System.out.println(item.getCatalogLine());
        }
    }

    private static void borrowFlow(Scanner scanner, Library library) throws LibraryException {
        System.out.print("Номер карты читателя: ");
        String card = scanner.nextLine();
        System.out.print("ID позиции (например B-001): ");
        String itemId = scanner.nextLine();
        library.borrow(card, itemId);
        System.out.println("Выдача оформлена.");
    }

    private static void returnFlow(Scanner scanner, Library library) throws LibraryException {
        System.out.print("Номер карты читателя: ");
        String card = scanner.nextLine();
        System.out.print("ID позиции: ");
        String itemId = scanner.nextLine();
        library.returnItem(card, itemId);
        System.out.println("Возврат принят.");
    }

    private static void printMembers(Library library) {
        for (Member m : library.getMembersView()) {
            System.out.println(m.getCardNumber() + " — " + m.getFullName() + "; на руках: " + m.describeLoans());
        }
    }

    private static void loanHintFlow(Scanner scanner, Library library) throws LibraryException {
        System.out.print("ID позиции: ");
        String itemId = scanner.nextLine();
        System.out.println(library.loanHint(itemId));
    }

    private static void filterByYearFlow(Scanner scanner, Library library) {
        System.out.print("Показать издания с года (целое число, например 2019): ");
        int year = Integer.parseInt(scanner.nextLine().trim());
        List<LibraryItem> items = library.itemsPublishedFromYear(year);
        if (items.isEmpty()) {
            System.out.println("Нет позиций с таким годом или новее.");
            return;
        }
        for (LibraryItem item : items) {
            System.out.println(item.getCatalogLine());
        }
    }
}
