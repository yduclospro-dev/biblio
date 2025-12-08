package main.java.com.biblio.pattern.observer;

/**
 * Pattern : Observer
 * Interface pour les observateurs qui écoutent les événements de la bibliothèque.
 * Référence : https://refactoring.guru/fr/design-patterns/observer/java/example
 */
public interface LoanObserver {
    /**
     * Appelé quand un emprunt est créé
     */
    void onLoanCreated(LoanEvent event);

    /**
     * Appelé quand un emprunt est retourné
     */
    void onLoanReturned(LoanEvent event);
}
