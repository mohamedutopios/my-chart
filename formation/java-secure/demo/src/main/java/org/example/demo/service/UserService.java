package org.example.demo.service;

import org.example.demo.model.User;
import org.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Inscription de l'utilisateur
    public User registerUser(String username, String password) {
        // Aucune vérification si l'utilisateur existe déjà
        User user = new User(username, password);
        return userRepository.save(user);
    }

    // Authentification de l'utilisateur
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Message d'erreur détaillé
            throw new RuntimeException("L'utilisateur n'existe pas.");
        }
        if (!user.getPassword().equals(password)) {
            // Message d'erreur détaillé
            throw new RuntimeException("Mot de passe incorrect.");
        }
        return true;
    }
}

