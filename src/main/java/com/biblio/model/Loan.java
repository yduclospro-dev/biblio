package main.java.com.biblio.model;

import java.util.Date;

/**
 * Modèle de domaine : Emprunt
 * Représente un emprunt de livre par un utilisateur
 */
public class Loan {
    private String id;
    private String bookId;
    private String userId;
    private Date loanDate;
    private Date returnDate;

    public Loan(Builder builder) {
        this.id = builder.id;
        this.bookId = builder.bookId;
        this.userId = builder.userId;
        this.loanDate = builder.loanDate;
        this.returnDate = null;
    }

    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getUserId() { return userId; }
    public Date getLoanDate() { return loanDate; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    /**
     * Pattern : Builder - Construction d'un Loan de manière fluide
     * Permet de créer des objets Loan de manière fluide et lisible.
     * Référence : https://refactoring.guru/fr/design-patterns/builder/java/example
     */
    public static class Builder {
        private String id;
        private String bookId;
        private String userId;
        private Date loanDate;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder bookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder loanDate(Date loanDate) {
            this.loanDate = loanDate;
            return this;
        }

        public Loan build() {
            return new Loan(this);
        }
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", userId='" + userId + '\'' +
                ", loanDate=" + loanDate +
                ", returnDate=" + returnDate +
                '}';
    }
}