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

    public UserDTO createUser(String name, String email, String password) {
        String userId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        User user = new User(userId, name, email, password);
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
    
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}