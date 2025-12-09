package main.java.com.biblio;

import main.java.com.biblio.service.BookService;
import main.java.com.biblio.service.UserService;
import main.java.com.biblio.service.LoanService;
import main.java.com.biblio.dto.BookDTO;
import main.java.com.biblio.dto.UserDTO;
import main.java.com.biblio.dto.LoanDTO;
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
    private UserDTO currentUser = null;

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
        if (!userService.adminExists()) {
            userService.createUser("admin", "admin@biblio.com", "admin", true);
        }
        while (running) {
            displayMenu();
            int choice = readInt();
            handleMenuChoice(choice);
        }

        System.out.println("\nAu revoir!");
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        MA PETITE BIBLIOTHÈQUE        ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("Utilisateur actuel : " + (currentUser == null ? "Non authentifié" : currentUser.toString()));
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
                searchBookByTitle();
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
                System.out.println("Option invalide");
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
        System.out.println("LISTE DES LIVRES");
        System.out.println(repeatChar('═', 60));
        
        if (books.isEmpty()) {
            System.out.println("Aucun livre trouvé");
        } else {
            for (BookDTO book : books) {
                System.out.println(book.toString());
            }
        }
        System.out.println(repeatChar('═', 60));
    }

    private void addBook() {
        if (currentUser == null || !currentUser.isAdmin()) {
            System.out.println("\nErreur: Seul un administrateur peut ajouter un livre.");
            pause();
            return;
        }

        System.out.print("\nTitre: ");
        String title = scanner.nextLine();
        System.out.print("Auteur: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        try {
            BookDTO newBook = bookService.createBook(title, author, isbn);
            System.out.println("Livre ajouté: " + newBook.getTitle());
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        pause();
    }

    private void addUser() {
        if (currentUser != null) {
            System.out.println("\nDéconnexion automatique de " + currentUser.getUserName() + ".");
            currentUser = null;
        }

        System.out.print("\nNom d'utilisateur: ");
        String userName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        try {
            UserDTO newUser = userService.createUser(userName, email, password, false);
            System.out.println("\nUtilisateur créé: " + newUser.getUserName());
            System.out.println("Veuillez maintenant vous connecter.");
        } catch (Exception e) {
            System.out.println("\nErreur: " + e.getMessage());
        }
        pause();
    }

    private void authenticate() {
        System.out.print("\nEmail: ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        currentUser = userService.authenticate(email, password);
        if (currentUser != null) {
            System.out.println("\nAuthentification réussie. Bienvenue " 
                + currentUser.getUserName() + " !");
        } else {
            System.out.println("\nÉchec de l'authentification : email ou mot de passe incorrect.");
        }
        pause();
    }

    private void createLoan() {
        if (!isUserAuthenticated()) {
            System.out.println("\nErreur: Vous devez être connecté pour emprunter un livre.");
            pause();
            return;
        }

        listBooks();
        System.out.print("\nTitre du livre à emprunter: ");
        String bookTitle = scanner.nextLine();

        BookDTO book = bookService.searchBookByTitle(bookTitle);
        if (book == null) {
            System.out.println("\nErreur: Livre non trouvé.");
            pause();
            return;
        }

        try {
            loanService.createLoan(book.getId(), currentUser.getId());
            System.out.println("\nLe livre " + book.getTitle() + " a bien été emprunté.");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        pause();
    }

    private void searchBookByTitle() {
        System.out.print("\nTitre à rechercher: ");
        String title = scanner.nextLine();

        BookDTO result = bookService.searchBookByTitle(title);
        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("RÉSULTATS DE LA RECHERCHE");
        System.out.println(repeatChar('═', 60));
        
        if (result == null) {
            System.out.println("Aucun livre trouvé avec le titre: " + title);
        } else {
            System.out.println(result.toString());
        }

        System.out.println(repeatChar('═', 60));
        pause();
    }

    private void returnBook() {
        if (!isUserAuthenticated()) {
            System.out.println("\nErreur: Vous devez être connecté pour retourner un livre.");
            pause();
            return;
        }

        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("EMPRUNTS ACTIFS");
        System.out.println(repeatChar('═', 60));
        
        List<LoanDTO> loans = loanService.getActiveLoansByUserId(currentUser.getId());
        if (loans.isEmpty()) {
            System.out.println("Aucun emprunt actif");
        } else {
            for (LoanDTO loan : loans) {
                BookDTO book = bookService.getBookById(loan.getBookId());
                UserDTO user = userService.getUserById(loan.getUserId());
                System.out.printf("[%s] %s emprunté par %s (depuis le %s)%n",
                    book.getTitle(),
                    book != null ? book.getTitle() : "Livre supprimé",
                    user != null ? user.getUserName() : "Utilisateur supprimé",
                    loan.getLoanDate()
                );
            }
        }

        System.out.println(repeatChar('═', 60));

        System.out.print("\nTitre du livre à retourner: ");
        String bookTitle = scanner.nextLine();

        BookDTO book = bookService.searchBookByTitle(bookTitle);
        if (book == null) {
            System.out.println("\nErreur: Livre non trouvé.");
            pause();
            return;
        }

        LoanDTO loan = loanService.getActiveLoanByBookAndUser(book.getId(), currentUser.getId());
        if (loan == null) {
            System.out.println("\nErreur: Vous n'avez pas emprunté ce livre.");
            pause();
            return;
        }

        try {
            loanService.returnBook(loan.getId());
            System.out.println("\nLivre retourné");
        } catch (Exception e) {
            System.out.println("\nErreur: " + e.getMessage());
        }
        pause();
    }

    private void listActiveLoans() {
        if (!isUserAuthenticated()) {
            System.out.println("\nErreur: Vous devez être connecté pour voir vos emprunts.");
            pause();
            return;
        }

        System.out.println("\n" + repeatChar('═', 60));
        System.out.println("EMPRUNTS ACTIFS");
        System.out.println(repeatChar('═', 60));
        
        List<LoanDTO> loans = loanService.getActiveLoansByUserId(currentUser.getId());
        if (loans.isEmpty()) {
            System.out.println("Aucun emprunt actif");
        } else {
            for (LoanDTO loan : loans) {
                BookDTO book = bookService.getBookById(loan.getBookId());
                UserDTO user = userService.getUserById(loan.getUserId());
                System.out.printf("[%s] %s emprunté par %s (depuis le %s)%n",
                    book.getTitle(),
                    book != null ? book.getTitle() : "Livre supprimé",
                    user != null ? user.getUserName() : "Utilisateur supprimé",
                    loan.getLoanDate()
                );
            }
        }
        System.out.println(repeatChar('═', 60));
        pause();
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void pause() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
        clearConsole();
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du nettoyage de la console: " + e.getMessage());
        }
    }

    private boolean isUserAuthenticated() {
        return currentUser != null;
    }
}