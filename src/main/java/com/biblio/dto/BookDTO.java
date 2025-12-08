package main.java.com.biblio.dto;

/**
 * Pattern : DTO (Data Transfer Object)
 * Utilisé pour transférer les données Book sans exposer l'entité directement.
 * Permet de contrôler quelles données sont exposées à la couche présentation.
 * Référence : https://www.baeldung.com/java-dto-pattern
 */
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    public BookDTO(String id, String title, String author, String isbn, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }

    @Override
    public String toString() {
        return String.format("[%s] %s par %s (ISBN: %s) - %s",
                id, title, author, isbn, isAvailable ? "Disponible" : "Emprunté");
    }
}