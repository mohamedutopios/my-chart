Voici un exemple d'une application Spring en architecture n-tiers qui met en avant plusieurs failles d'authentification précises. Cet exemple illustre les couches de l'architecture, avec les failles et le code associé pour chaque couche (DAO, Service, Controller). 

### Structure n-tiers

1. **DAO (Data Access Object)** : Gère l'accès aux données (par exemple, stockage des utilisateurs).
2. **Service** : Contient la logique métier (par exemple, authentification).
3. **Controller** : Interagit avec l'utilisateur (par exemple, reçoit les requêtes d'authentification).

---

### 1. **Faiblesse : Mots de passe stockés en clair**
   #### Explication :
   Stocker les mots de passe en clair dans la base de données est une vulnérabilité majeure. Cela permet à un attaquant qui a accès à la base de données de récupérer les mots de passe sans aucun effort.

#### Code :

**User.java (Entité)**

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // Vulnérabilité: le mot de passe est stocké en clair
    private String password;

    // Getters et setters
}
```

**UserRepository.java (DAO)**

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

**UserService.java (Service)**

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Vulnérabilité : le mot de passe n'est pas hashé avant d'être stocké
    public User registerUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Le mot de passe est stocké en clair
        return userRepository.save(user);
    }
}
```

**AuthController.java (Controller)**

```java
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");
        userService.registerUser(username, password);
        return ResponseEntity.ok("User registered successfully");
    }
}
```

---

### 2. **Faiblesse : Authentification par mot de passe faible**
   #### Explication :
   Permettre des mots de passe faibles sans validation stricte expose l'application aux attaques par force brute ou dictionnaire.

#### Code :

**UserService.java**

```java
@Service
public class UserService {

    // Vulnérabilité : pas de vérification de la complexité du mot de passe
    public User registerUser(String username, String password) {
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password is too short");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }
}
```

**Failles identifiées :** L'application n'impose pas de complexité pour le mot de passe (caractères spéciaux, chiffres, etc.), ce qui rend les mots de passe vulnérables à une attaque par dictionnaire.

---

### 3. **Faiblesse : Réutilisation des mots de passe**
   #### Explication :
   Les utilisateurs peuvent réutiliser leurs mots de passe sur plusieurs plateformes. Si une de ces plateformes est compromise, l'attaquant peut utiliser les mêmes identifiants pour d'autres services.

#### Code :

**UserService.java**

```java
@Service
public class UserService {

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Vulnérabilité : le mot de passe est comparé en clair
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
```

**AuthController.java**

```java
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");

        try {
            userService.login(username, password);
            return ResponseEntity.ok("Login successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }
}
```

**Failles identifiées :** Pas de protection contre la réutilisation des mots de passe. L'utilisateur peut utiliser le même mot de passe pour plusieurs comptes, ce qui augmente le risque d'attaque par compromission croisée.

---

### 4. **Faiblesse : Usurpation d'identité (Man-in-the-Middle)**
   #### Explication :
   Si l'authentification ne passe pas par HTTPS, les données, y compris les identifiants, peuvent être interceptées par un attaquant via une attaque MITM.

#### Code :

**application.properties**

```properties
# Vulnérabilité : le serveur ne force pas l'utilisation de HTTPS
server.port=8080
```

**Failles identifiées :** Aucune redirection automatique de HTTP vers HTTPS, ce qui rend l'application vulnérable aux attaques MITM lors de l'authentification.

---

### 5. **Faiblesse : Absence de mécanisme de gestion des tentatives de connexion**
   #### Explication :
   Ne pas limiter le nombre de tentatives de connexion permet aux attaquants de tester de nombreuses combinaisons de mots de passe (attaque par force brute).

#### Code :

**UserService.java**

```java
@Service
public class UserService {

    private static final int MAX_ATTEMPTS = 3;
    private Map<String, Integer> loginAttempts = new HashMap<>();

    public User login(String username, String password) {
        if (loginAttempts.getOrDefault(username, 0) >= MAX_ATTEMPTS) {
            throw new IllegalArgumentException("Account locked due to too many failed login attempts");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                loginAttempts.put(username, 0); // Reset on successful login
                return user;
            } else {
                loginAttempts.put(username, loginAttempts.getOrDefault(username, 0) + 1);
                throw new IllegalArgumentException("Invalid credentials");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
```

**Failles identifiées :** En l'absence d'un mécanisme de limitation des tentatives de connexion, une attaque par force brute pourrait permettre à un attaquant de deviner les mots de passe.

---

### Conclusion

Ce projet présente plusieurs failles d'authentification intentionnelles pour démontrer les vulnérabilités typiques rencontrées dans les applications web. Ces failles peuvent être exploitées par des attaquants dans des scénarios réels, et il est essentiel de les corriger en appliquant les meilleures pratiques en matière de sécurité (hashing des mots de passe, authentification multi-facteurs, validation des entrées, etc.).