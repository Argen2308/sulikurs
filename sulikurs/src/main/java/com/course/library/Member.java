package com.course.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Читатель: учёт текущих выдач (id позиции → количество у читателя).
 */
public class Member {

    private final String cardNumber;
    private String fullName;
    private final Map<String, Integer> borrowedByItemId;

    public Member(String cardNumber, String fullName) {
        this.cardNumber = Objects.requireNonNull(cardNumber, "cardNumber").trim();
        this.setFullName(fullName);
        this.borrowedByItemId = new HashMap<>();
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("ФИО не может быть пустым");
        }
        this.fullName = fullName.trim();
    }

    public int getBorrowedCount(String itemId) {
        return this.borrowedByItemId.getOrDefault(itemId, 0);
    }

    public void registerBorrow(String itemId) {
        int n = this.borrowedByItemId.getOrDefault(itemId, 0);
        this.borrowedByItemId.put(itemId, n + 1);
    }

    public void registerReturn(String itemId) throws LibraryException {
        int n = this.borrowedByItemId.getOrDefault(itemId, 0);
        if (n <= 0) {
            throw new LibraryException("У читателя нет этой позиции на руках");
        }
        if (n == 1) {
            this.borrowedByItemId.remove(itemId);
        } else {
            this.borrowedByItemId.put(itemId, n - 1);
        }
    }

    public String describeLoans() {
        if (this.borrowedByItemId.isEmpty()) {
            return "нет выданных позиций";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : this.borrowedByItemId.entrySet()) {
            sb.append(e.getKey()).append(" ×").append(e.getValue()).append("; ");
        }
        return sb.toString();
    }
}
