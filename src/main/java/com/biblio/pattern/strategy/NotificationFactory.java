package main.java.com.biblio.pattern.strategy;

/**
 * Pattern : Factory
 * Factory pour créer les stratégies de notification
 * Référence : https://refactoring.guru/fr/design-patterns/factory-method
 */
public class NotificationFactory {
    public static NotificationStrategy create(String notificationType) {
        if (notificationType == null) {
            return new ConsoleNotificationStrategy();
        }
        
        switch (notificationType.toLowerCase()) {
            case "console":
                return new ConsoleNotificationStrategy();
            case "email":
                return new EmailNotificationStrategy();
            default:
                return new ConsoleNotificationStrategy();
        }
    }
}