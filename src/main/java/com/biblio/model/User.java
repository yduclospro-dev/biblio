package main.java.com.biblio.model;

public class User {
    private String id;
    private String userName;
    private String email;
    private String password;
    private boolean isAdmin;

    public User(Builder builder) {
        this.id = builder.id;
        this.userName = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.isAdmin = builder.isAdmin;
    }

    public String getId() { return id; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, userName, email);
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
        private boolean isAdmin;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.name = userName;
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

        public Builder isAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}