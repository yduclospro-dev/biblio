package main.java.com.biblio.repository;

import main.java.com.biblio.model.Book;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du Repository pour Book
 */
public class BookRepositoryImpl implements BookRepository {
    private DataStore dataStore;

    public BookRepositoryImpl() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public void save(Book book) {
        dataStore.add(dataStore.getBooks(), book);
    }

    @Override
    public Book findById(String id) {
        return dataStore.findBy(dataStore.getBooks(), b -> ((Book)b).getId().equals(id));
    }

    @Override
    public Book findByTitle(String title) {
        return dataStore.findBy(dataStore.getBooks(), b -> ((Book)b).getTitle().toLowerCase().contains(title.toLowerCase()));
    }

    @Override
    public Book findByIsbn(String isbn) {
        return dataStore.findBy(dataStore.getBooks(), b -> ((Book)b).getIsbn().equals(isbn));
    }

    @Override
    public List<Book> findAll() {
        return dataStore.getAll(dataStore.getBooks());
    }

    @Override
    public void delete(String id) {
        dataStore.deleteById(dataStore.getBooks(), id);
    }

    @Override
    public List<Book> findAvailable() {
        return findAll().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
}