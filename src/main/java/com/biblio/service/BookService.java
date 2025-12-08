package main.java.com.biblio.service;

import main.java.com.biblio.model.Book;
import main.java.com.biblio.dto.BookDTO;
import main.java.com.biblio.repository.BookRepository;
import main.java.com.biblio.repository.DataStore;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Pattern : Service Layer
 * Contient la logique métier pour les opérations sur les Books.
 * Référence : https://medium.com/@navroops38/understanding-the-service-layer-in-software-architecture
 */
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Créer un nouveau livre en utilisant le pattern Builder
     */
    public BookDTO createBook(String title, String author, String isbn) {
        String bookId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Pattern : Builder
        Book book = new Book.Builder()
                .id(bookId)
                .title(title)
                .author(author)
                .isbn(isbn)
                .isAvailable(true)
                .build();
        
        bookRepository.save(book);
        return convertToDTO(book);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findAvailable().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchBooksByTitle(String title) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(String id) {
        Book book = bookRepository.findById(id);
        if (book == null) return null;
        return convertToDTO(book);
    }

    public void deleteBook(String id) {
        bookRepository.delete(id);
    }

    public boolean isBookAvailable(String bookId) {
        Book book = bookRepository.findById(bookId);
        return book != null && book.isAvailable();
    }

    public void markAsLoaned(String bookId) {
        Book book = bookRepository.findById(bookId);
        if (book != null) {
            book.setAvailable(false);
            DataStore.getInstance().saveToJSON();
        }
    }

    public void markAsAvailable(String bookId) {
        Book book = bookRepository.findById(bookId);
        if (book != null) {
            book.setAvailable(true);
            DataStore.getInstance().saveToJSON();
        }
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.isAvailable()
        );
    }
}