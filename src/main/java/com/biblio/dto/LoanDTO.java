package main.java.com.biblio.dto;

import java.util.Date;

/**
 * Pattern : DTO (Data Transfer Object)
 * Utilisé pour transférer les données Loan sans exposer l'entité directement.
 * Permet de contrôler quelles données sont exposées à la couche présentation.
 * Référence : https://www.baeldung.com/java-dto-pattern
 */
public class LoanDTO {
    private final String id;
    private final String bookId;
    private final String userId;
    private final Date loanDate;
    private final Date returnDate;

    public LoanDTO(String id, String bookId, String userId, Date loanDate, Date returnDate) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public Date getLoanDate() { return loanDate; }
    public Date getReturnDate() { return returnDate; }
    public boolean isActive() { return returnDate == null; }

    @Override
    public String toString() {
        return "[LOAN_" + id + "] Emprunté le " + loanDate +
                (returnDate != null ? " - Retourné le " + returnDate : " - ACTIF");
    }
}