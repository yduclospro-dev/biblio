package main.java.com.biblio.pattern.strategy;

/**
 * Stratégie de notification par console
 */
public class ConsoleNotificationStrategy implements NotificationStrategy {
    @Override
    public void send(String recipient, String message) {
        System.out.println("💻 Notification Console - Destinataire : " + recipient);
        System.out.println("   Message : " + message);
    }
}
