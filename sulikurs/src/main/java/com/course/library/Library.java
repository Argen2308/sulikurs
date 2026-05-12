package com.course.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сервис библиотеки: каталог, поиск, выдача и возврат.
 */
public class Library {

    private final List<LibraryItem> catalog;
    private final List<Member> members;

    public Library() {
        this.catalog = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addItem(LibraryItem item) {
        this.catalog.add(item);
    }

    public void registerMember(Member member) {
        this.members.add(member);
    }

    public List<LibraryItem> getCatalogView() {
        return Collections.unmodifiableList(this.catalog);
    }

    public List<Member> getMembersView() {
        return Collections.unmodifiableList(this.members);
    }

    public Optional<LibraryItem> findItemById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        String trimmed = id.trim();
        for (LibraryItem item : this.catalog) {
            if (item.getId().equalsIgnoreCase(trimmed)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public Optional<Member> findMemberByCard(String cardNumber) {
        if (cardNumber == null) {
            return Optional.empty();
        }
        String trimmed = cardNumber.trim();
        for (Member m : this.members) {
            if (m.getCardNumber().equalsIgnoreCase(trimmed)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    /**
     * Динамический полиморфизм: обход списка как {@link LibraryItem}, вызов {@link LibraryItem#getCatalogLine()}.
     */
    public List<String> listCatalogLines() {
        List<String> lines = new ArrayList<>();
        for (LibraryItem item : this.catalog) {
            lines.add(item.getCatalogLine());
        }
        return lines;
    }

    public List<LibraryItem> search(String keyword) {
        List<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : this.catalog) {
            if (item.matches(keyword)) {
                result.add(item);
            }
        }
        return result;
    }

    /** Позиции с годом издания не раньше указанного (для демонстрации разбора числа из ввода). */
    public List<LibraryItem> itemsPublishedFromYear(int minYearInclusive) {
        List<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : this.catalog) {
            if (item.getPublicationYear() >= minYearInclusive) {
                result.add(item);
            }
        }
        return result;
    }

    public void borrow(String memberCard, String itemId) throws LibraryException {
        Member member = this.findMemberByCard(memberCard)
                .orElseThrow(() -> new LibraryException("Читатель с номером карты не найден: " + memberCard));
        LibraryItem item = this.findItemById(itemId)
                .orElseThrow(() -> new LibraryException("Позиция каталога не найдена: " + itemId));
        if (!item.tryTakeOneCopy()) {
            throw new LibraryException("Нет свободных экземпляров: " + item.getTitle());
        }
        try {
            member.registerBorrow(item.getId());
        } catch (RuntimeException e) {
            item.returnOneCopy();
            throw new LibraryException("Не удалось оформить выдачу", e);
        }
    }

    public void returnItem(String memberCard, String itemId) throws LibraryException {
        Member member = this.findMemberByCard(memberCard)
                .orElseThrow(() -> new LibraryException("Читатель с номером карты не найден: " + memberCard));
        LibraryItem item = this.findItemById(itemId)
                .orElseThrow(() -> new LibraryException("Позиция каталога не найдена: " + itemId));
        member.registerReturn(item.getId());
        item.returnOneCopy();
    }

    /**
     * Сводка по сроку выдачи — снова полиморфный вызов {@link LibraryItem#getMaxBorrowDays()}.
     */
    public String loanHint(String itemId) throws LibraryException {
        LibraryItem item = this.findItemById(itemId)
                .orElseThrow(() -> new LibraryException("Позиция не найдена: " + itemId));
        return "Максимальный срок выдачи для «" + item.getTitle() + "»: " + item.getMaxBorrowDays() + " дн.";
    }
}
