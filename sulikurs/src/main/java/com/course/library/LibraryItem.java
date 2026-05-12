package com.course.library;

import java.util.Objects;

/**
 * Абстрактная позиция каталога: общие поля и поведение для книг и журналов.
 */
public abstract class LibraryItem implements Searchable {

    private final String id;
    private String title;
    private int publicationYear;
    private int copiesAvailable;

    public LibraryItem(String id, String title, int publicationYear, int totalCopies) {
        this.id = Objects.requireNonNull(id, "id");
        this.setTitle(title);
        this.setPublicationYear(publicationYear);
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Количество экземпляров не может быть отрицательным");
        }
        this.copiesAvailable = totalCopies;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название не может быть пустым");
        }
        this.title = title.trim();
    }

    public int getPublicationYear() {
        return this.publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        if (publicationYear < 1000 || publicationYear > 2100) {
            throw new IllegalArgumentException("Некорректный год издания");
        }
        this.publicationYear = publicationYear;
    }

    public int getCopiesAvailable() {
        return this.copiesAvailable;
    }

    /**
     * Срок выдачи в днях — разный для подклассов (полиморфизм).
     */
    public abstract int getMaxBorrowDays();

    /**
     * Краткое описание для каталога — переопределяется в наследниках.
     */
    public abstract String getCatalogLine();

    public boolean tryTakeOneCopy() {
        if (this.copiesAvailable <= 0) {
            return false;
        }
        this.copiesAvailable--;
        return true;
    }

    public void returnOneCopy() {
        this.copiesAvailable++;
    }

    @Override
    public boolean matches(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }
        String k = keyword.toLowerCase();
        return this.title.toLowerCase().contains(k) || this.id.toLowerCase().contains(k);
    }
}
