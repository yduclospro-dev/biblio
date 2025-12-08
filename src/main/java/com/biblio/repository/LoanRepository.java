package main.java.com.biblio.repository;

import main.java.com.biblio.model.Loan;
import java.util.List;

/**
 * Interface Repository pour Loan
 * Pattern : Repository
 */
public interface LoanRepository {
    void save(Loan loan);
    Loan findById(String id);
    List<Loan> findAll();
    void delete(String id);
}