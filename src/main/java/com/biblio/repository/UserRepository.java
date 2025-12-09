package main.java.com.biblio.repository;

import main.java.com.biblio.model.User;
import java.util.List;

/**
 * Pattern : Repository Design Pattern
 * Interface qui définit les opérations d'accès aux données pour les User.
 * Référence : https://www.geeksforgeeks.org/system-design/repository-design-pattern/
 */
public interface UserRepository {
    void save(User user);
    User findById(String id);
    List<User> findAll();
    void delete(String id);
    User findByEmail(String email);
    User findByUserName(String userName);
    boolean adminExists();
}