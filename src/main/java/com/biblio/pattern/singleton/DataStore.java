package main.java.com.biblio.pattern.singleton;

import main.java.com.biblio.model.Book;
import main.java.com.biblio.model.User;
import main.java.com.biblio.repository.JSONStorage;
import main.java.com.biblio.model.Loan;
import main.java.com.biblio.util.Utils;
import java.util.*;
import java.util.function.Predicate;

/**
 * Pattern : Singleton - Gère l'accès unique à la base de données en mémoire
 * Référence : https://refactoring.guru/fr/design-patterns/singleton/java/example
 */
public class DataStore {
    private static DataStore instance;
    
    private List<Book> books;
    private List<User> users;
    private List<Loan> loans;

    /**
     * Constructeur privé pour empêcher l'instanciation directe
     */
    private DataStore() {
        this.books = JSONStorage.loadDataFromJSON(Utils.BOOKS_FILE, Book.class);
        this.users = JSONStorage.loadDataFromJSON(Utils.USERS_FILE, User.class);
        this.loans = JSONStorage.loadDataFromJSON(Utils.LOANS_FILE, Loan.class);
    }

    /**
     * Obtenir l'instance unique du singleton
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
    public List<Loan> getLoans() { return loans; }

    public void saveToJSON() {
        JSONStorage.saveToJSON(books, Utils.BOOKS_FILE, "Livres");
        JSONStorage.saveToJSON(users, Utils.USERS_FILE, "Utilisateurs");
        JSONStorage.saveToJSON(loans, Utils.LOANS_FILE, "Emprunts");
    }
    
    public <T> void add(List<T> list, T item) {
        list.add(item);
        saveToJSON();
    }

    public <T> List<T> getAll(List<T> list) {
        return new ArrayList<>(list);
    }

    public <T> T findBy(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    public <T> void deleteById(List<T> list, String id) {
        list.removeIf(item -> {
            try {
                return item.getClass().getMethod("getId").invoke(item).equals(id);
            } catch (Exception e) {
                return false;
            }
        });
        saveToJSON();
    }

    public <T> void update(List<T> list, T newItem) {
        try {
            String newId = (String) newItem.getClass().getMethod("getId").invoke(newItem);
            for (int i = 0; i < list.size(); i++) {
                String itemId = (String) list.get(i).getClass().getMethod("getId").invoke(list.get(i));
                if (itemId.equals(newId)) {
                    list.set(i, newItem);
                    saveToJSON();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}