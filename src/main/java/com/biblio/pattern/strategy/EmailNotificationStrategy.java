package main.java.com.biblio.pattern.strategy;

/**
 * Stratégie de notification par email
 */
public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Email envoyé à " + recipient);
        System.out.println("Message : " + message);
    }
}
