package main.java.com.biblio.service;

import main.java.com.biblio.pattern.strategy.ConsoleNotificationStrategy;
import main.java.com.biblio.pattern.strategy.EmailNotificationStrategy;
import main.java.com.biblio.pattern.strategy.NotificationStrategy;

/**
 * Pattern : Strategy
 * Permet d'envoyer des notifications par différents canaux (Console, Email)
 * Référence : https://refactoring.guru/fr/design-patterns/strategy
 */
public class NotificationService {
    private NotificationStrategy strategy;

    public NotificationService() {
        // Par défaut : Console
        this.strategy = new ConsoleNotificationStrategy();
    }

    public NotificationService(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Définir la stratégie de notification
     */
    public void setNotificationStrategy(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Envoyer une notification avec la stratégie courante
     */
    public void sendNotification(String recipient, String message) {
        if (strategy != null) {
            strategy.send(recipient, message);
        }
    }

    /**
     * Envoyer une notification Console
     */
    public void sendConsoleNotification(String message) {
        setNotificationStrategy(new ConsoleNotificationStrategy());
        sendNotification("SYSTEM", message);
    }

    /**
     * Envoyer une notification Email
     */
    public void sendEmailNotification(String email, String message) {
        setNotificationStrategy(new EmailNotificationStrategy());
        sendNotification(email, message);
    }
}