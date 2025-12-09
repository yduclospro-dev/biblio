package main.java.com.biblio.dto;

/**
 * Pattern : DTO (Data Transfer Object)
 * Utilisé pour transférer les données User sans exposer l'entité directement.
 * Permet de contrôler quelles données sont exposées à la couche présentation.
 * Référence : https://www.baeldung.com/java-dto-pattern
 */
public class UserDTO {
    private String id;
    private String userName;
    private String email;
    private boolean isAdmin;

    public UserDTO(String id, String userName, String email, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getId() { return id; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return isAdmin; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", userName, email, isAdmin ? "Admin" : "User");
    }
}