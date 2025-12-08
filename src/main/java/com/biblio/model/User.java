package main.java.com.biblio.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, name, email);
    }

    /**
     * Pattern : Builder - Construction d'un User de manière fluide
     * Permet de créer des objets User de manière fluide et lisible.
     * Référence : https://refactoring.guru/fr/design-patterns/builder/java/example
     */
    public static class Builder {
        private String id;
        private String name;
        private String email;
        private String password;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(id, name, email, password);
        }
    }
}