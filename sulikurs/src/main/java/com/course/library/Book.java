package com.course.library;

/**
 * Книга: увеличенный срок выдачи по сравнению с журналом.
 */
public class Book extends LibraryItem {

    private String author;
    private String isbn;

    public Book(String id, String title, String author, int publicationYear, int totalCopies, String isbn) {
        super(id, title, publicationYear, totalCopies);
        this.setAuthor(author);
        this.setIsbn(isbn);
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }
        this.author = author.trim();
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? "" : isbn.trim();
    }

    @Override
    public int getMaxBorrowDays() {
        return 21;
    }

    @Override
    public String getCatalogLine() {
        return "[Книга] " + this.getTitle() + " — " + this.author + " (" + this.getPublicationYear() + "), ISBN: "
                + this.isbn + ", свободно: " + this.getCopiesAvailable();
    }

    @Override
    public boolean matches(String keyword) {
        if (super.matches(keyword)) {
            return true;
        }
        if (keyword == null || keyword.isBlank()) {
            return false;
        }
        String k = keyword.toLowerCase();
        return this.author.toLowerCase().contains(k) || this.isbn.toLowerCase().contains(k);
    }
}
