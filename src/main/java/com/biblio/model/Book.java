package main.java.com.biblio.model;

/**
 * Pattern : Builder - Construction lisible d'un objet Book
 * Permet de créer des objets Book de manière fluide et lisible.
 * Référence : https://refactoring.guru/fr/design-patterns/builder/java/example
 */
public class Book {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    private Book(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.author = builder.author;
        this.isbn = builder.isbn;
        this.isAvailable = builder.isAvailable;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return String.format("[%s] %s par %s (ISBN: %s) - %s",
                id, title, author, isbn, isAvailable ? "Disponible" : "Emprunté");
    }

    /**
     * Pattern : Builder - Construction d'un Book de manière fluide
     * 
     */
    public static class Builder {
        private String id;
        private String title;
        private String author;
        private String isbn;
        private boolean isAvailable = true;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder isAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}