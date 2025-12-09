package main.java.com.biblio.repository;

import main.java.com.biblio.model.Book;
import main.java.com.biblio.model.User;
import main.java.com.biblio.model.Loan;
import main.java.com.biblio.util.Utils;
import java.io.*;
import java.util.*;

/**
 * Gestionnaire de persistence JSON
 * Utilise des méthodes génériques pour éviter la duplication
 */
public class JSONStorage {

    public static void initializeDataDirectory() {
        File dir = new File(Utils.DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
            System.out.println("📁 Répertoire 'data' créé");
        }
    }

    public static <T> void saveToJSON(List<T> items, String filePath, String entityName) {
        try {
            initializeDataDirectory();
            StringBuilder json = new StringBuilder();
            json.append("[\n");
            
            for (int i = 0; i < items.size(); i++) {
                T item = items.get(i);
                json.append("  ").append(objectToJSON(item));
                if (i < items.size() - 1) json.append(",");
                json.append("\n");
            }
            
            json.append("]");
            
            FileWriter fw = new FileWriter(filePath);
            fw.write(json.toString());
            fw.close();
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des " + entityName + " : " + e.getMessage());
        }
    }

    private static String objectToJSON(Object obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Book":
                Book book = (Book) obj;
                return "{\n" +
                    "    \"id\": \"" + book.getId() + "\",\n" +
                "    \"title\": \"" + escapeJSON(book.getTitle()) + "\",\n" +
                "    \"author\": \"" + escapeJSON(book.getAuthor()) + "\",\n" +
                "    \"isbn\": \"" + book.getIsbn() + "\",\n" +
                "    \"isAvailable\": " + book.isAvailable() + "\n" +
                "  }";
            case "User":
                User user = (User) obj;
                return "{\n" +
                    "    \"id\": \"" + user.getId() + "\",\n" +
                    "    \"userName\": \"" + escapeJSON(user.getUserName()) + "\",\n" +
                    "    \"email\": \"" + user.getEmail() + "\",\n" +
                    "    \"password\": \"" + user.getPassword() + "\",\n" +
                    "    \"isAdmin\": " + user.isAdmin() + "\n" +
                    "  }";
            case "Loan":
                Loan loan = (Loan) obj;
                return "{\n" +
                    "    \"id\": \"" + loan.getId() + "\",\n" +
                    "    \"bookId\": \"" + loan.getBookId() + "\",\n" +
                    "    \"userId\": \"" + loan.getUserId() + "\",\n" +
                    "    \"loanDate\": \"" + loan.getLoanDate().getTime() + "\",\n" +
                    "    \"returnDate\": \"" + (loan.getReturnDate() != null ? loan.getReturnDate().getTime() : "null") + "\"\n" +
                    "  }";
            default:
                return "{}";
        }
    }
    
    public static <T> List<T> loadDataFromJSON(String filePath, Class<T> entityClass) {
        List<T> items = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            return items;
        }

            try {
            StringBuilder content = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            br.close();

            String jsonContent = content.toString();
            int start = 0;
            while ((start = jsonContent.indexOf("{", start)) != -1) {
                int end = jsonContent.indexOf("}", start);
                String objJson = jsonContent.substring(start, end + 1);
                
                Object obj = jsonToObject(objJson, entityClass);
                if (obj != null && entityClass.isInstance(obj)) {
                    items.add(entityClass.cast(obj));
                }
                start = end + 1;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier " + filePath + " : " + e.getMessage());
        }

        return items;
    }

    private static <T> T jsonToObject(String json, Class<T> entityClass) {

        if (entityClass == Book.class) {
            String id = extractJSONValue(json, "id");
            if (!id.isEmpty()) {
                Book book = new Book.Builder()
                    .id(id)
                    .title(extractJSONValue(json, "title"))
                    .author(extractJSONValue(json, "author"))
                    .isbn(extractJSONValue(json, "isbn"))
                    .isAvailable(Boolean.parseBoolean(extractJSONValue(json, "isAvailable")))
                    .build();

                return entityClass.cast(book);
            }
        }

        if (entityClass == User.class) {
            String id = extractJSONValue(json, "id");
            if (!id.isEmpty()) {
                User user = new User.Builder()
                    .id(id)
                    .userName(extractJSONValue(json, "userName"))
                    .email(extractJSONValue(json, "email"))
                    .password(extractJSONValue(json, "password"))
                    .isAdmin(Boolean.parseBoolean(extractJSONValue(json, "isAdmin")))
                    .build();
                return entityClass.cast(user);
            }
        }

        if (entityClass == Loan.class) {            
            String id = extractJSONValue(json, "id");
            String returnDateStr = extractJSONValue(json, "returnDate");
            if (!id.isEmpty()) {
                Loan loan = new  Loan.Builder()
                    .id(id)
                    .bookId(extractJSONValue(json, "bookId"))
                    .userId(extractJSONValue(json, "userId"))
                    .loanDate(new Date(Long.parseLong(extractJSONValue(json, "loanDate"))))
                    .build();
                if (!returnDateStr.equals("null")) {
                    loan.setReturnDate(new Date(Long.parseLong(returnDateStr)));
                }
                return entityClass.cast(loan);
            }
        }

        return null;
    }

    private static String extractJSONValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int startIdx = json.indexOf(searchKey);
        if (startIdx == -1) return "";
        
        startIdx = json.indexOf(":", startIdx) + 1;
        int endIdx = json.indexOf(",", startIdx);
        if (endIdx == -1) endIdx = json.indexOf("}", startIdx);
        
        String value = json.substring(startIdx, endIdx).trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String escapeJSON(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
}