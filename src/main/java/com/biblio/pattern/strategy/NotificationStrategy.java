package main.java.com.biblio.pattern.strategy;

/**
 * Pattern : Strategy
 * Interface pour les différentes stratégies de notification
 * Référence : https://refactoring.guru/fr/design-patterns/strategy
 */
public interface NotificationStrategy {
    void send(String recipient, String message);
}
