package main.java.com.biblio.repository;

import main.java.com.biblio.model.Loan;
import main.java.com.biblio.pattern.singleton.DataStore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du Repository pour Loan
 */
public class LoanRepositoryImpl implements LoanRepository {
    private DataStore dataStore;

    public LoanRepositoryImpl() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public void save(Loan loan) {
        dataStore.add(dataStore.getLoans(), loan);
    }

    @Override
    public Loan findById(String id) {
        return dataStore.findBy(dataStore.getLoans(), loan -> loan.getId().equals(id));
    }

    @Override
    public List<Loan> findAll() {
        return dataStore.getAll(dataStore.getLoans());
    }

    @Override
    public void delete(String id) {
        dataStore.deleteById(dataStore.getLoans(), id);
    }

    @Override
    public Loan findActiveLoanByBookAndUser(String bookId, String userId) {
        return dataStore.findBy(dataStore.getLoans(),
            loan -> loan.getBookId().equals(bookId) && 
            loan.getUserId().equals(userId) && 
            loan.getReturnDate() == null);
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return dataStore.getAll(dataStore.getLoans()).stream()
            .filter(loan -> loan.getUserId().equals(userId) && loan.getReturnDate() == null)
            .collect(Collectors.toList());
    }
}