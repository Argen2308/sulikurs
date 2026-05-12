package com.course.library;

/**
 * Журнал: короткий срок выдачи.
 */
public class Magazine extends LibraryItem {

    private int issueNumber;

    public Magazine(String id, String title, int publicationYear, int totalCopies, int issueNumber) {
        super(id, title, publicationYear, totalCopies);
        this.setIssueNumber(issueNumber);
    }

    public int getIssueNumber() {
        return this.issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        if (issueNumber < 1) {
            throw new IllegalArgumentException("Номер выпуска должен быть положительным");
        }
        this.issueNumber = issueNumber;
    }

    @Override
    public int getMaxBorrowDays() {
        return 7;
    }

    @Override
    public String getCatalogLine() {
        return "[Журнал] " + this.getTitle() + ", выпуск №" + this.issueNumber + " (" + this.getPublicationYear()
                + "), свободно: " + this.getCopiesAvailable();
    }
}
