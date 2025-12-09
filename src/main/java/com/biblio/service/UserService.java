package main.java.com.biblio.service;

import main.java.com.biblio.model.User;
import main.java.com.biblio.dto.UserDTO;
import main.java.com.biblio.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Pattern : Service Layer
 * Contient la logique métier pour les opérations sur les Users.
 */
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(String userName, String email, String password, boolean isAdmin) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide.");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("L'email n'est pas au bon format.");
        }

        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("L'email est déjà utilisé.");
        }

        if (userRepository.findByUserName(userName) != null) {
            throw new IllegalArgumentException("Le nom d'utilisateur est déjà pris.");
        }

        String userId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        User user = new  User.Builder()
                .id(userId)
                .userName(userName)
                .email(email)
                .password(password)
                .isAdmin(isAdmin)
                .build();
        userRepository.save(user);
        return convertToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id);
        if (user == null) return null;
        return convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        return convertToDTO(user);
    }
    
    public void deleteUser(String id) {
        userRepository.delete(id);
    }
    
    public UserDTO authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return convertToDTO(user);
        }
        return null;
    }

    public boolean adminExists() {
        return userRepository.adminExists();
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(regex);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.isAdmin());
    }
}