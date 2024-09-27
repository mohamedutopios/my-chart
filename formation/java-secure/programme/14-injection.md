### Injection

L'**injection** est une vulnérabilité critique dans les applications qui se produit lorsqu'un attaquant parvient à introduire des données malveillantes dans un programme via des entrées utilisateur, lesquelles sont ensuite exécutées ou interprétées comme des commandes. Cela peut permettre à un attaquant d'exécuter des commandes arbitraires, d'accéder à des données sensibles, ou de compromettre entièrement un système. L'injection est souvent classée en fonction de la cible de l'attaque : **SQL**, **commande OS**, **code**, etc.

---

### 1. **Principes d'injection**

Les attaques par injection se produisent lorsque des données non fiables sont envoyées à un interpréteur (comme SQL, un shell, ou un moteur de templates) et que ces données sont traitées comme des instructions valides à exécuter. L'absence de validation, d'échappement ou de filtrage correct des entrées permet à des commandes malveillantes d'être exécutées.

##### **Exemple de base :**

Dans une application web, si une requête SQL attend une entrée utilisateur (par exemple, un identifiant de produit), et que cette entrée n'est pas correctement validée ou échappée, un attaquant peut injecter du code SQL pour manipuler la base de données.

---

### 2. **Attaques par injection**

Les attaques par injection peuvent cibler différents systèmes ou composants d'une application :

- **Injection SQL** : Manipuler des requêtes SQL pour exécuter des commandes malveillantes.
- **Injection de commandes OS** : Envoyer des commandes au système d'exploitation via des interfaces vulnérables.
- **Injection de code** : Injecter du code dans une application ou un environnement d'exécution pour exécuter des actions arbitraires.
- **Injection dans les fichiers de configuration** : Modifier ou injecter du contenu dans des fichiers de configuration interprétés par l'application.

---

### 3. **Injection SQL**

L'**injection SQL** est une attaque dans laquelle des requêtes SQL malveillantes sont insérées dans un formulaire ou une URL et exécutées par la base de données sous-jacente. Cela peut permettre à un attaquant d'accéder à des données sensibles, de manipuler des données, voire de compromettre l'ensemble du système.

##### **Exemple d'injection SQL :**

Supposons que l'application exécute cette requête pour authentifier un utilisateur :

```sql
SELECT * FROM users WHERE username = 'JohnDoe' AND password = 'password';
```

Un attaquant pourrait entrer une chaîne malveillante dans le champ `username` comme suit :

```sql
' OR '1'='1
```

La requête devient alors :

```sql
SELECT * FROM users WHERE username = '' OR '1'='1' AND password = 'password';
```

Le résultat est que la condition `1=1` est toujours vraie, ce qui donne accès à l'attaquant.

---

### 4. **Bonnes pratiques en matière d'injection SQL**

Pour prévenir les injections SQL, il existe plusieurs bonnes pratiques que les développeurs doivent suivre :

1. **Utiliser des requêtes préparées (prepared statements)** :
   - Les **requêtes préparées** sont la méthode la plus sûre pour éviter les injections SQL. Elles permettent de séparer les instructions SQL des paramètres, rendant impossible l'injection de code SQL.

   **Exemple en Java avec JDBC** :
   ```java
   String query = "SELECT * FROM users WHERE username = ? AND password = ?";
   PreparedStatement stmt = connection.prepareStatement(query);
   stmt.setString(1, username);
   stmt.setString(2, password);
   ResultSet rs = stmt.executeQuery();
   ```

2. **Valider et filtrer les entrées utilisateur** :
   - Utilisez une **validation stricte** pour toutes les données en entrée. Par exemple, si une entrée doit être un entier, assurez-vous qu'elle ne contient que des chiffres.

3. **Limiter les permissions de la base de données** :
   - Ne donnez que les **permissions minimales** nécessaires à l'utilisateur de la base de données. Par exemple, un utilisateur utilisé pour des requêtes SELECT ne devrait pas avoir des privilèges de modification (INSERT, UPDATE, DELETE).

4. **Évitez de construire des requêtes dynamiques** :
   - N'utilisez pas de concaténation de chaînes pour construire des requêtes SQL, ce qui est vulnérable aux injections.

---

### 5. **Injection de code**

L'**injection de code** se produit lorsque des données utilisateur sont insérées dans du code ou des scripts et sont exécutées comme si elles faisaient partie du programme. Cela peut se produire dans divers langages ou frameworks.

##### **Exemple d'injection de code JavaScript** :

Si une application Web insère des données utilisateur directement dans du code JavaScript sans validation, un attaquant peut injecter du code JavaScript malveillant.

```html
<script>
    var userInput = "<?php echo $_GET['user']; ?>";
</script>
```

Si un utilisateur malveillant passe une chaîne comme :

```javascript
"><script>alert('XSS')</script>
```

Cela entraînera l'exécution du code JavaScript lors du rendu de la page.

---

### 6. **Bonnes pratiques d'injection de commande OS**

Une **injection de commande OS** se produit lorsque des commandes système sont construites en utilisant des données utilisateur non fiables, et que celles-ci sont exécutées directement par le système d'exploitation.

##### **Exemple vulnérable** :

```java
String command = "ls " + userInput;
Runtime.getRuntime().exec(command);
```

Si un attaquant passe une entrée comme `; rm -rf /`, cela pourrait entraîner la suppression de tous les fichiers.

##### **Bonnes pratiques pour éviter les injections de commande OS** :

1. **Évitez d'utiliser `Runtime.exec()` ou `ProcessBuilder` avec des entrées utilisateur non validées**.
2. **Validez strictement les entrées** pour les commandes systèmes. Si l'entrée utilisateur est censée être un nom de fichier ou un paramètre, assurez-vous qu'elle respecte les contraintes de votre application.
3. **Utilisez des API natives ou des bibliothèques sécurisées** qui ne nécessitent pas l'exécution de commandes shell.
4. **Minimisez les privilèges** : Assurez-vous que l'application n'a accès qu'aux ressources minimales nécessaires.

---

### 7. **Utilisation de `Runtime.exec()`**

La méthode **`Runtime.exec()`** en Java permet d'exécuter des commandes système. Toutefois, elle est souvent vulnérable aux attaques d'injection si elle est utilisée avec des entrées utilisateur non validées.

##### **Exemple vulnérable** :

```java
String command = "ping " + userInput;
Runtime.getRuntime().exec(command);
```

Cela peut être exploité si `userInput` contient des caractères malveillants. Il est recommandé d'éviter d'utiliser cette méthode avec des entrées utilisateur non filtrées.

---

### 8. **Utilisation de `ProcessBuilder`**

`ProcessBuilder` est une autre manière d'exécuter des commandes système en Java. Il est plus sûr que `Runtime.exec()` car il permet de spécifier les arguments de la commande séparément, ce qui empêche l'injection.

##### **Exemple sécurisé avec `ProcessBuilder`** :

```java
ProcessBuilder processBuilder = new ProcessBuilder("ping", "google.com");
Process process = processBuilder.start();
```

Dans cet exemple, les arguments sont séparés, empêchant ainsi l'injection de commandes supplémentaires via `userInput`.

---

### 9. **Étude de cas - Shellshock**

**Shellshock** est une vulnérabilité majeure découverte dans le shell Bash, qui permettait à des attaquants d'exécuter des commandes arbitraires via des variables d'environnement.

##### **Contexte** :
Bash évaluait certaines variables d'environnement comme des commandes, permettant aux attaquants d'exploiter des applications exécutant Bash pour lancer des commandes arbitraires.

##### **Impact** :
Les systèmes vulnérables pouvaient être compromis à distance via des scripts CGI, permettant l'exécution de commandes malveillantes sur le serveur.

---

### 10. **Étude de cas - Injection de modèle dans Shopify menant à un RCE**

**Shopify**, une plateforme d'e-commerce, a subi une vulnérabilité où un attaquant pouvait exploiter une faille dans un moteur de templates (liquid template) pour exécuter des commandes arbitraires.

##### **Contexte** :
L'attaque exploitait une mauvaise gestion des modèles et permettait à un attaquant d'injecter du code dans un template, menant à une exécution de commande à distance (RCE).

##### **Impact** :
Les données utilisateur pouvaient être manipulées pour injecter du code dans le modèle, compromettant ainsi l'application.

---

### 11. **Les meilleures pratiques en matière d'injection**

1. **Toujours utiliser des API sécurisées** comme des requêtes préparées pour SQL ou des `ProcessBuilder` pour l'exécution de commandes OS.
2. **Valider et filtrer toutes les entrées utilisateur**.
3. **Limiter les privilèges de l'application** pour réduire les impacts d'une attaque.
4. **Ne jamais utiliser de concaténation de chaînes pour construire des commandes SQL ou OS**.
5. **Échapper les entrées utilisateur** avant de les utiliser dans un environnement où elles peuvent être interprétées comme du code (SQL, JavaScript, HTML, etc.).
6. **Désactive

z les fonctionnalités non nécessaires** comme les entités XML externes pour éviter les attaques XXE.
7. **Auditez régulièrement vos systèmes** pour détecter les vulnérabilités d'injection.

En suivant ces bonnes pratiques, vous pouvez réduire considérablement le risque d'injection dans vos applications et protéger votre infrastructure contre des attaques critiques.