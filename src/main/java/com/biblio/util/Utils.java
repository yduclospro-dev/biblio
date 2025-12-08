package main.java.com.biblio.util;

import java.io.File;

/**
 * Classe utilitaire contenant les constantes et méthodes communes
 */
public class Utils {
    // Constantes pour les fichiers JSON
    public static final String DATA_DIR = "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "biblio" + File.separator + "data";
    public static final String BOOKS_FILE = DATA_DIR + File.separator + "books.json";
    public static final String USERS_FILE = DATA_DIR + File.separator + "users.json";
    public static final String LOANS_FILE = DATA_DIR + File.separator + "loans.json";
}