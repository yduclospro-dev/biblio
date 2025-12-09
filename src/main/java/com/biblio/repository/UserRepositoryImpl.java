package main.java.com.biblio.repository;

import main.java.com.biblio.model.User;
import java.util.List;

/**
 * Implémentation du Repository pour User
 */
public class UserRepositoryImpl implements UserRepository {
    private DataStore dataStore;

    public UserRepositoryImpl() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public void save(User user) {
        dataStore.add(dataStore.getUsers(), user);
    }

    @Override
    public User findById(String id) {
        return dataStore.findBy(dataStore.getUsers(), u -> u.getId().equals(id));
    }

    @Override
    public List<User> findAll() {
        return dataStore.getAll(dataStore.getUsers());
    }

    @Override
    public void delete(String id) {
        dataStore.deleteById(dataStore.getUsers(), id);
    }

    @Override
    public User findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByUserName(String userName) {
        return findAll().stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean adminExists() {
        return findAll().stream()
                .anyMatch(User::isAdmin);
    }
}