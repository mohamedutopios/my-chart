package org.example.demo.service;




import org.example.demo.model.User;
import org.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService2 {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Stocker les tentatives de connexion
    private ConcurrentHashMap<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;

    // Inscription de l'utilisateur
    public User registerUser(String username, String password) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Nom d'utilisateur déjà pris.");
        }

        // Valider la complexité du mot de passe
        if (!isPasswordComplex(password)) {
            throw new RuntimeException("Le mot de passe ne respecte pas les critères de complexité.");
        }

        // Hacher le mot de passe
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(username, hashedPassword);
        return userRepository.save(user);
        return null;
    }

    // Authentification de l'utilisateur
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Message d'erreur générique
            throw new RuntimeException("Nom d'utilisateur ou mot de passe incorrect.");
        }

        // Vérifier le nombre de tentatives
        int attempts = loginAttempts.getOrDefault(username, 0);
        if (attempts >= MAX_ATTEMPTS) {
            throw new RuntimeException("Compte verrouillé en raison de tentatives de connexion échouées.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            // Incrémenter les tentatives
            loginAttempts.put(username, attempts + 1);
            throw new RuntimeException("Nom d'utilisateur ou mot de passe incorrect.");
        }

        // Réinitialiser les tentatives après une connexion réussie
        loginAttempts.remove(username);
        return true;
    }

    // Méthode pour valider la complexité du mot de passe
    private boolean isPasswordComplex(String password) {
        if (password.length() < 8) {
            return false;
        }
        // Vérifier la présence de majuscules, minuscules, chiffres et caractères spéciaux
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()].*");

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
