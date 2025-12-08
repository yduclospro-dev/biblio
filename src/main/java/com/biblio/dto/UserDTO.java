package main.java.com.biblio.dto;

/**
 * Pattern : DTO (Data Transfer Object)
 * Utilisé pour transférer les données User sans exposer l'entité directement.
 * Permet de contrôler quelles données sont exposées à la couche présentation.
 * Référence : https://www.baeldung.com/java-dto-pattern
 */
public class UserDTO {
    private String id;
    private String name;
    private String email;

    public UserDTO(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, name, email);
    }
}