package main.java.com.biblio.pattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Pattern : Observer (Subject/Observable)
 * Gère une liste d'observateurs et les notifie lors d'événements.
 */
public class LoanSubject {
    private List<LoanObserver> observers = new ArrayList<>();

    /**
     * Attacher un observateur
     */
    public void attach(LoanObserver observer) {
        observers.add(observer);
    }

    /**
     * Détacher un observateur
     */
    public void detach(LoanObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifier tous les observateurs d'un emprunt créé
     */
    public void notifyLoanCreated(LoanEvent event) {
        for (LoanObserver observer : observers) {
            observer.onLoanCreated(event);
        }
    }

    /**
     * Notifier tous les observateurs d'un retour de livre
     */
    public void notifyLoanReturned(LoanEvent event) {
        for (LoanObserver observer : observers) {
            observer.onLoanReturned(event);
        }
    }
}
