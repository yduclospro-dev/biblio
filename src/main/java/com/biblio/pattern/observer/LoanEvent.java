package main.java.com.biblio.pattern.observer;

import java.util.Date;

/**
 * Événement déclenché lors d'un emprunt
 */
public class LoanEvent {
    private String bookId;
    private String userId;
    private String bookTitle;
    private String userName;
    private Date eventDate;

    public LoanEvent(String bookId, String userId, String bookTitle, String userName) {
        this.bookId = bookId;
        this.userId = userId;
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.eventDate = new Date();
    }

    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public String getBookTitle() { return bookTitle; }
    public String getUserName() { return userName; }
    public Date getEventDate() { return eventDate; }
}
