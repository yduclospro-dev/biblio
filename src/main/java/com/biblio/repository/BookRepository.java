package main.java.com.biblio.repository;

import main.java.com.biblio.model.Book;
import java.util.List;

/**
 * Pattern : Repository Design Pattern
 * Interface qui définit les opérations d'accès aux données pour les Book.
 * Référence : https://www.geeksforgeeks.org/system-design/repository-design-pattern/
 */
public interface BookRepository {
    void save(Book book);
    Book findById(String id);
    Book findByTitle(String title);
    List<Book> findAll();
    void delete(String id);
    List<Book> findAvailable();
}