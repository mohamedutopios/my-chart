
package org.example.demo.controller;


import org.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Classe interne pour les requêtes d'inscription
    static class RegisterRequest {
        public String username;
        public String password;
    }

    // Classe interne pour les requêtes de connexion
    static class LoginRequest {
        public String username;
        public String password;
    }

    // Endpoint pour l'inscription
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request.username, request.password);
            return "Inscription réussie pour l'utilisateur : " + request.username;
        } catch (Exception e) {
            return "Erreur lors de l'inscription : " + e.getMessage();
        }
    }

    // Endpoint pour la connexion
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        try {
            userService.authenticate(request.username, request.password);
            return "Authentification réussie pour l'utilisateur : " + request.username;
        } catch (Exception e) {
            return "erreur lors de l'authentification : " + e.getMessage();
        }
    }
}

