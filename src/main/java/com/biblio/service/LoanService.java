package main.java.com.biblio.service;

import main.java.com.biblio.repository.DataStore;
import main.java.com.biblio.repository.LoanRepository;
import main.java.com.biblio.dto.LoanDTO;
import main.java.com.biblio.model.Loan;
import main.java.com.biblio.pattern.observer.LoanEvent;
import main.java.com.biblio.pattern.observer.LoanSubject;
import main.java.com.biblio.pattern.observer.NotificationObserver;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Pattern : Service Layer
 * re
 * Contient la logique métier pour les emprunts.
 * Utilise le pattern Observer pour déclencher les notifications.
 */
public class LoanService {
    private DataStore dataStore;
    private LoanRepository loanRepository;
    private BookService bookService;
    private UserService userService;
    private LoanSubject loanSubject;

    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.dataStore = DataStore.getInstance();
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.loanSubject = new LoanSubject();
    }

    /**
     * Créer un emprunt
     * Pattern : Observer - déclenche les notifications
     */
    public String createLoan(String bookId, String userId) {
        if (!bookService.isBookAvailable(bookId)) {
            throw new IllegalArgumentException("Le livre n'est pas disponible.");
        }

        if (userService.getUserById(userId) == null) {
            throw new IllegalArgumentException("L'utilisateur n'a pas été trouvé trouvé.");
        }

        String loanId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Loan loan = new Loan.Builder()
                .id(loanId)
                .bookId(bookId)
                .userId(userId)
                .loanDate(new Date())
                .build();

        loanRepository.save(loan);
        bookService.markAsLoaned(bookId);

        // Pattern : Observer - Notifier les observateurs
        String bookTitle = bookService.getBookById(bookId).getTitle();
        String userName = userService.getUserById(userId).getUserName();
        LoanEvent event = new LoanEvent(bookId, userId, bookTitle, userName);
        
        // Attacher un observateur de notification et déclencher
        String userEmail = userService.getUserById(userId).getEmail();
        NotificationObserver notificationObserver = new NotificationObserver("console", userEmail);
        loanSubject.attach(notificationObserver);
        loanSubject.notifyLoanCreated(event);

        return loanId;
    }

    /**
     * Pattern : Observer - déclenche les notifications
     */
    public void returnBook(String loanId) {
        Loan loan = loanRepository.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Emprunt non trouvé");
        }

        loan.setReturnDate(new Date());
        dataStore.update(dataStore.getLoans(), loan);
        bookService.markAsAvailable(loan.getBookId());

        // Pattern : Observer - Notifier les observateurs
        String bookTitle = bookService.getBookById(loan.getBookId()).getTitle();
        String userName = userService.getUserById(loan.getUserId()).getUserName();
        LoanEvent event = new LoanEvent(loan.getBookId(), loan.getUserId(), bookTitle, userName);
        
        String userEmail = userService.getUserById(loan.getUserId()).getEmail();
        NotificationObserver notificationObserver = new NotificationObserver("console", userEmail);
        loanSubject.attach(notificationObserver);
        loanSubject.notifyLoanReturned(event);
    }
    
    public LoanDTO getActiveLoanByBookAndUser(String bookId, String userId) {
        return convertToDTO(loanRepository.findActiveLoanByBookAndUser(bookId, userId));
    }

    public List<LoanDTO> getActiveLoansByUserId(String userId) {
        return loanRepository.findByUserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LoanDTO convertToDTO(Loan loan) {
        return new LoanDTO(
            loan.getId(),
            loan.getBookId(),
            loan.getUserId(),
            loan.getLoanDate(),
            loan.getReturnDate()
        );
    }
}