package main.java.com.biblio;

import main.java.com.biblio.service.BookService;
import main.java.com.biblio.service.UserService;
import main.java.com.biblio.service.LoanService;
import main.java.com.biblio.dto.BookDTO;
import main.java.com.biblio.dto.UserDTO;
import main.java.com.biblio.model.Loan;
import main.java.com.biblio.repository.LoanRepository;
import main.java.com.biblio.repository.LoanRepositoryImpl;
import main.java.com.biblio.repository.BookRepository;
import main.java.com.biblio.repository.BookRepositoryImpl;
import main.java.com.biblio.repository.UserRepository;
import main.java.com.biblio.repository.UserRepositoryImpl;
import java.util.List;
import java.util.Scanner;

/**
 * Application principale - Menu de gestion de bibliothèque
 * Tous les patterns implémentés sont utilisés ici :
 * 1. Singleton - DataStore
 * 2. Builder - Book.Builder
 * 3. Factory - NotificationFactory
 * 4. Strategy - NotificationStrategy
 * 5. Repository - BookRepository, UserRepository
 * 6. Service Layer - BookService, UserService, LoanService
 * 7. DTO - BookDTO, UserDTO, LoanDTO
 * 8. Observer - LoanObserver, LoanSubject, NotificationObserver
 */
public class App {
    private BookService bookService;
    private UserService userService;
    private LoanService loanService;
    private Scanner scanner;
    private boolean running = true;

    public App() {
        BookRepository bookRepository = new BookRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        LoanRepository loanRepository = new LoanRepositoryImpl();

        this.bookService = new BookService(bookRepository);
        this.userService = new UserService(userRepository);
        this.loanService = new LoanService(loanRepository, bookService, userService);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {        
        while (running) {
            displayMenu();
            int choice = readInt();
            handleMenuChoice(choice);
        }

        System.out.println("\n👋 Au revoir!");
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   MA PETITE BIBLIOTHÈQUE                 ║");
        System.out.println("╚═════════════════════════════════════════╝");
        System.out.println("\n1. S'inscrire");
        System.out.println("2. Se connecter");
        System.out.println("3. Ajouter un livre");
        System.out.println("4. Voir tous les livres");
        System.out.println("5. Rechercher un livre par titre");
        System.out.println("6. Emprunter un livre");
        System.out.println("7. Retourner un livre");
        System.out.println("8. Voir mes emprunts");
        System.out.println("9. Quitter");
        System.out.print("\nVotre choix : ");
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addUser();
                break;
            case 2:
                authenticate();
                break;
            case 3:
                addBook();
                break;
            case 4:
                listBooks();
                break;
            case 5:
                searchBooksByTitle();
                break;
            case 6:
                createLoan();
                break;
            case 7:
                returnBook();
                break;
            case 8:
                listActiveLoans();
                break;
            case 9:
                running = false;
                break;
            default:
                System.out.println("❌ Option invalide");
        }
    }

    private String repeatChar(char c, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private void listBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("📚 LISTE DES LIVRES");
        System.out.println(repeatChar('═', 60));
        
        if (books.isEmpty()) {
            System.out.println("Aucun livre trouvé");
        } else {
            for (BookDTO book : books) {
                System.out.println(book);
            }
        }
        System.out.println(repeatChar('═', 60));
    }

    private void addBook() {
        System.out.print("\nTitre: ");
        String title = scanner.nextLine();
        System.out.print("Auteur: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        try {
            BookDTO newBook = bookService.createBook(title, author, isbn);
            System.out.println("✅ Livre ajouté: " + newBook);
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void listUsers() {
        List<UserDTO> users = userService.getAllUsers();
        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("👥 LISTE DES UTILISATEURS");
        System.out.println(repeatChar('═', 60));
        
        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé");
        } else {
            for (UserDTO user : users) {
                System.out.println(user);
            }
        }
        System.out.println(repeatChar('═', 60));
    }

    private void addUser() {
        System.out.print("\nNom: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        try {
            UserDTO newUser = userService.createUser(name, email, password);
            System.out.println("✅ Utilisateur ajouté: " + newUser);
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void authenticate() {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        boolean authenticated = userService.authenticate(email, password);
        if (authenticated) {
            System.out.println("\nAuthentification réussie");
        } else {
            System.out.println("\nÉchec de l'authentification");
        }
    }

    private void createLoan() {
        listBooks();
        System.out.print("\nID du livre à emprunter: ");
        String bookId = scanner.nextLine();

        listUsers();
        System.out.print("\nID de l'utilisateur: ");
        String userId = scanner.nextLine();

        try {
            String loanId = loanService.createLoan(bookId, userId);
            System.out.println("✅ Emprunt créé avec ID: " + loanId);
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void searchBooksByTitle() {
        System.out.print("\nTitre à rechercher: ");
        String title = scanner.nextLine();

        List<BookDTO> results = bookService.searchBooksByTitle(title);
        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("🔍 RÉSULTATS DE LA RECHERCHE");
        System.out.println(repeatChar('═', 60));
        
        if (results.isEmpty()) {
            System.out.println("Aucun livre trouvé avec le titre: " + title);
        } else {
            for (BookDTO book : results) {
                System.out.println(book);
            }
        }
        System.out.println(repeatChar('═', 60));
    }

    private void returnBook() {
        listActiveLoans();
        System.out.print("\nID de l'emprunt à retourner: ");
        String loanId = scanner.nextLine();

        try {
            loanService.returnBook(loanId);
            System.out.println("✅ Livre retourné");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void listActiveLoans() {
        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("📤 EMPRUNTS ACTIFS");
        System.out.println(repeatChar('═', 60));
        
        List<Loan> loans = loanService.getActiveLoans();
        if (loans.isEmpty()) {
            System.out.println("Aucun emprunt actif");
        } else {
            for (Loan loan : loans) {
                BookDTO book = bookService.getBookById(loan.getBookId());
                UserDTO user = userService.getUserById(loan.getUserId());
                System.out.printf("[%s] %s emprunté par %s (depuis %s)%n",
                    loan.getId(),
                    book != null ? book.getTitle() : "Livre supprimé",
                    user != null ? user.getName() : "Utilisateur supprimé",
                    loan.getLoanDate()
                );
            }
        }
        System.out.println(repeatChar('═', 60));
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}