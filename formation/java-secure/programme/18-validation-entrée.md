### Validation des entrées

La **validation des entrées** est un principe fondamental en sécurité des applications Web. Elle consiste à vérifier et nettoyer les données fournies par les utilisateurs ou provenant de sources externes avant qu'elles ne soient traitées par le système. Une mauvaise validation des entrées peut ouvrir la porte à de nombreuses vulnérabilités, telles que les injections SQL, les scripts intersites (XSS), les attaques par entités externes XML (XXE), et bien d'autres.

#### **Principes de validation des entrées**

1. **Validation côté serveur** :
   - **Le plus important** : La validation doit toujours être effectuée côté serveur, car c'est l'unique endroit sous le contrôle complet de l'application. Même si une validation côté client est effectuée (par exemple via JavaScript), un attaquant peut contourner facilement cette validation en envoyant directement des requêtes au serveur.
   
   - **Exemple** : 
     ```java
     @PostMapping("/submit")
     public String handleFormSubmission(@Valid @ModelAttribute("formData") FormData formData, BindingResult result) {
         if (result.hasErrors()) {
             return "errorPage";
         }
         // Traiter les données validées
         return "successPage";
     }
     ```
     Ici, Spring utilise l'annotation `@Valid` pour s'assurer que les données du formulaire sont conformes aux contraintes de validation définies sur l'objet `FormData`.

2. **Validation côté client (facultative mais utile)** :
   - Bien que la validation côté serveur soit primordiale, la validation côté client peut améliorer l'expérience utilisateur en offrant un retour rapide (sans passer par le serveur) et en réduisant la charge des serveurs.
   - **Exemple de validation côté client en HTML5** :
     ```html
     <form>
       <input type="email" name="email" required>
       <input type="submit" value="Envoyer">
     </form>
     ```
     Ici, le champ email est vérifié par le navigateur pour s'assurer qu'il contient un format d'adresse électronique valide avant l'envoi au serveur.

3. **Principe du "deny-all, allow-only" (refuser par défaut)** :
   - Par défaut, il est recommandé de **refuser toutes les entrées**, et de n'autoriser que celles qui répondent aux critères spécifiques. En d'autres termes, la validation doit être stricte et n'accepter que les données conformes aux attentes.
   - **Exemple** : Si vous attendez une adresse email, utilisez des expressions régulières ou des validateurs spécifiques pour vous assurer que seul un email valide est accepté.
     ```java
     if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
         throw new InvalidDataException("Email invalide");
     }
     ```

4. **Types de validation des entrées** :
   - **Validation de type** : Assurez-vous que les données correspondent au type attendu. Par exemple, un champ de formulaire de type "âge" doit accepter uniquement des nombres.
   - **Validation de longueur** : Limitez la longueur des données acceptées pour éviter des attaques par dépassement de mémoire tampon ou des attaques de déni de service (DoS).
     ```java
     if (username.length() > 50) {
         throw new InvalidDataException("Le nom d'utilisateur est trop long");
     }
     ```
   - **Validation de format** : Vérifiez le format des données via des expressions régulières (ex : emails, numéros de téléphone).
     ```java
     if (!phoneNumber.matches("\\d{10}")) {
         throw new InvalidDataException("Numéro de téléphone invalide");
     }
     ```

5. **Utilisation de whitelisting (listes blanches)** :
   - Utilisez des listes blanches pour limiter les entrées acceptées aux valeurs sûres. Par exemple, si vous attendez un nom de pays, n'acceptez que les valeurs qui existent dans une liste pré-établie de pays.
   - **Exemple en Java** :
     ```java
     List<String> allowedCountries = Arrays.asList("France", "USA", "Germany", "Canada");
     if (!allowedCountries.contains(country)) {
         throw new InvalidDataException("Pays non autorisé");
     }
     ```

6. **Sanitisation (nettoyage) des entrées** :
   - **Sanitisation** des entrées signifie que les données fournies sont nettoyées pour éliminer ou échapper les caractères dangereux susceptibles de provoquer des injections ou des attaques XSS.
   - Par exemple, vous pouvez utiliser une bibliothèque comme **OWASP Java HTML Sanitizer** pour nettoyer les entrées HTML.
     ```java
     String sanitizedInput = HtmlSanitizer.sanitize(unsafeInput);
     ```

7. **Éviter l'utilisation de données non validées dans les requêtes ou le code** :
   - Ne jamais utiliser directement les données utilisateur dans des requêtes SQL, du code HTML ou du code JavaScript sans une validation stricte.
   - **Exemple de mauvaise pratique (vulnérabilité SQLi)** :
     ```java
     String query = "SELECT * FROM users WHERE name = '" + userInput + "'";
     ```
     Au lieu de cela, utilisez des **requêtes préparées** :
     ```java
     PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
     stmt.setString(1, userInput);
     ```

8. **Validation basée sur le contexte** :
   - La validation doit être adaptée en fonction du contexte d'utilisation des données. Par exemple, les données utilisées dans une URL doivent être traitées différemment des données affichées dans un HTML ou stockées dans une base de données.
   - Pour des **injections SQL**, vous pouvez utiliser des ORM comme Hibernate, et pour la génération de **contenu HTML**, les moteurs de templates comme Thymeleaf s'assurent que les données sont échappées correctement.

9. **Limiter les caractères spéciaux** :
   - Pour certains champs d’entrée, comme les noms d'utilisateur ou les numéros de téléphone, il est souvent utile de limiter les caractères acceptés. Par exemple, ne permettre que des lettres, des chiffres et certains symboles autorisés.

10. **Gestion des erreurs** :
    - En cas de validation incorrecte, assurez-vous que les messages d'erreur retournés aux utilisateurs ne divulguent pas de détails internes sur le système, tels que la structure des bases de données ou des informations sensibles. Utilisez des messages d'erreur génériques pour les utilisateurs finaux.

    **Exemple** :
    ```java
    if (result.hasErrors()) {
        return "Une erreur s'est produite lors de la soumission. Veuillez vérifier les champs.";
    }
    ```

---

### **Résumé des bonnes pratiques en matière de validation des entrées** :

1. **Toujours valider côté serveur** (la validation côté client est complémentaire).
2. **Utiliser une approche par défaut restrictive** ("deny-all, allow-only").
3. **Éviter d'inclure directement les entrées utilisateur dans les requêtes SQL**, HTML, ou scripts sans validation ou échappement.
4. **Sanitiser et filtrer les entrées utilisateur** pour supprimer les caractères dangereux.
5. **Limiter la taille, le type, le format et les caractères autorisés** dans les entrées utilisateur.
6. **Utiliser des listes blanches** pour restreindre les valeurs autorisées.
7. **Messages d'erreur sécurisés** : Ne pas divulguer d'informations sensibles aux utilisateurs.

Ces bonnes pratiques permettent de minimiser les risques d'attaques liées aux entrées malveillantes, telles que les injections SQL, les attaques XSS, ou les dépassements de mémoire tampon.