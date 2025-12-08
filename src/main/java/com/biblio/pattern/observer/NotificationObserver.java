package main.java.com.biblio.pattern.observer;

import main.java.com.biblio.pattern.strategy.NotificationFactory;
import main.java.com.biblio.pattern.strategy.NotificationStrategy;

/**
 * Observateur pour les notifications de prêt
 * Implémente le pattern Observer pour envoyer des notifications
 */
public class NotificationObserver implements LoanObserver {
    private NotificationStrategy strategy;
    private String userEmail;

    public NotificationObserver(String notificationType, String userEmail) {
        this.strategy = NotificationFactory.create(notificationType);
        this.userEmail = userEmail;
    }

    @Override
    public void onLoanCreated(LoanEvent event) {
        String message = String.format(
            "Vous avez emprunté le livre '%s'. Merci!",
            event.getBookTitle()
        );
        strategy.send(userEmail, message);
    }

    @Override
    public void onLoanReturned(LoanEvent event) {
        String message = String.format(
            "Vous avez retourné le livre '%s'. Merci!",
            event.getBookTitle()
        );
        strategy.send(userEmail, message);
    }
}
