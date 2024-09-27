### Authentification défaillante

L'authentification défaillante est l'une des principales vulnérabilités qui peuvent compromettre la sécurité d'une application Web. Elle se produit lorsque les mécanismes d'authentification et de gestion des sessions sont mal implémentés, permettant à des attaquants de prendre le contrôle de comptes d'utilisateurs.

#### 1. **Gestion des sessions**

La **gestion des sessions** est un aspect critique de l'authentification. Après qu'un utilisateur se soit authentifié, un **jeton de session** est souvent créé pour identifier l'utilisateur lors de ses interactions ultérieures avec l'application. Si la gestion des sessions est mal configurée, des failles de sécurité comme le vol de session ou le détournement de session (session hijacking) peuvent survenir.

##### **Exemples de mauvaises pratiques dans la gestion des sessions :**

- **Jetons de session prévisibles ou insuffisamment sécurisés** : Si le jeton de session peut être deviné ou reproduit, un attaquant peut l'utiliser pour usurper l'identité de l'utilisateur.
- **Expiration insuffisante des sessions** : Si les sessions ne sont pas configurées pour expirer après une période d'inactivité, un attaquant pourrait utiliser un ancien jeton volé pour se reconnecter.
- **Sessions non invalidées** après déconnexion ou modification de mot de passe, permettant à un attaquant qui a volé un jeton de session de continuer à l'utiliser.

##### **Bonnes pratiques en gestion des sessions :**

- **Jetons sécurisés** : Utilisez des jetons de session cryptographiquement sécurisés (générés de manière aléatoire et impossible à deviner).
  
- **Expiration des sessions** : Configurez une expiration automatique des sessions après une période d'inactivité. Par exemple, déconnectez les utilisateurs après 15 à 30 minutes d'inactivité.

- **Invalidation des sessions** : À la déconnexion ou lors de changements critiques (comme la modification du mot de passe), invalidez toutes les sessions actives liées à l'utilisateur.

- **Cookies de session sécurisés** :
  - **HttpOnly** : Empêche le JavaScript du client d'accéder au cookie de session, réduisant les risques de vol via des attaques XSS.
  - **Secure** : Le cookie ne sera transmis que sur des connexions HTTPS, protégeant ainsi contre les attaques de type man-in-the-middle (MITM).
  - **SameSite** : Empêche les navigateurs d'envoyer des cookies dans des requêtes cross-site, limitant le risque d'attaques CSRF.

##### **Exemple de configuration sécurisée de cookies de session dans Spring Boot :**

```java
@Bean
public ServletCookieInitializer servletCookieInitializer() {
    return servletContext -> {
        servletContext.getSessionCookieConfig().setHttpOnly(true); // Empêche l'accès en JS
        servletContext.getSessionCookieConfig().setSecure(true);   // Transmission en HTTPS uniquement
        servletContext.getSessionCookieConfig().setMaxAge(1800);   // Expiration après 30 minutes
    };
}
```

#### 2. **Les bonnes pratiques contre les attaques CSRF (Cross-Site Request Forgery)**

Le **Cross-Site Request Forgery (CSRF)** est une attaque où un utilisateur authentifié est amené à exécuter une action non voulue sur un site où il est connecté. Un attaquant peut exploiter cette vulnérabilité pour exécuter des actions comme des changements de mot de passe ou des transferts d’argent en envoyant des requêtes malveillantes.

##### **Exemple d'attaque CSRF :**

- Un utilisateur est connecté à un site bancaire.
- L'attaquant incite l'utilisateur à cliquer sur un lien ou à visiter une page malveillante qui effectue une requête HTTP (par exemple, un transfert d'argent) à l'insu de l'utilisateur.
- Le site ne vérifie pas si la requête provient d'une source légitime (ou si elle a été initiée volontairement par l'utilisateur), et l'action est exécutée.

##### **Bonnes pratiques contre les attaques CSRF :**

1. **Utilisation de jetons CSRF** :
   - Générer des **jetons CSRF** uniques pour chaque session ou chaque requête qui impliquent un changement d’état (par exemple, la soumission de formulaires).
   - Ce jeton doit être inclus dans toutes les requêtes sensibles (POST, PUT, DELETE) pour vérifier que la requête provient de l'utilisateur authentifié et non d'un site tiers.

2. **Cookies SameSite** :
   - Utiliser l'attribut **SameSite** pour les cookies de session afin d'empêcher leur envoi dans les requêtes cross-origin (en dehors de votre domaine), réduisant ainsi les risques de CSRF.
   - Valeurs possibles : **Strict** (les cookies ne sont jamais envoyés pour les requêtes cross-site) ou **Lax** (les cookies ne sont pas envoyés pour les requêtes cross-site sauf pour les requêtes GET initiées par l'utilisateur).

3. **Limiter les méthodes HTTP** :
   - Limiter l'utilisation des méthodes HTTP telles que **POST**, **PUT**, **DELETE** uniquement pour les actions sensibles et non pour la navigation générale. Les requêtes GET devraient être idempotentes (sans modification d'état).

4. **Vérification des référents** :
   - Dans certains cas, vous pouvez vérifier l'en-tête **Referer** pour confirmer que la requête provient d'un domaine légitime. Cependant, cette méthode est moins fiable car certains navigateurs peuvent ne pas toujours inclure cet en-tête.

##### **Exemple d'implémentation CSRF avec Spring Security** :

Spring Security fournit une protection intégrée contre les attaques CSRF, activée par défaut dans les applications Web. Voici un exemple de configuration Spring Security qui inclut une protection CSRF :

```java
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF activé avec cookies HttpOnly
        .and()
        .authorizeRequests()
        .antMatchers("/public/**").permitAll() // URL publiques sans protection CSRF
        .anyRequest().authenticated(); // Protection CSRF pour les autres URL
    return http.build();
}
```

Le **CookieCsrfTokenRepository** stocke le jeton CSRF dans un cookie et s'assure que les requêtes POST incluent ce jeton. Le formulaire doit inclure un champ caché pour envoyer le jeton CSRF au serveur :

##### **Exemple de formulaire sécurisé contre CSRF avec Thymeleaf** :

```html
<form action="/updateProfile" method="POST">
    <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>
    <input type="text" name="username" placeholder="Nom d'utilisateur"/>
    <button type="submit">Mettre à jour</button>
</form>
```

Dans cet exemple, le champ caché `_csrf` contient le jeton CSRF et garantit que seules les requêtes provenant du formulaire authentique sont acceptées.

---

### **Résumé des bonnes pratiques :**

- **Gestion des sessions** :
  - Utiliser des jetons de session sécurisés et non prédictifs.
  - Configurer l'expiration des sessions et invalider les sessions après déconnexion.
  - Protéger les cookies avec `HttpOnly`, `Secure`, et `SameSite`.
  
- **Protection contre les attaques CSRF** :
  - Utiliser des jetons CSRF uniques pour chaque formulaire.
  - Configurer l'attribut **SameSite** pour les cookies.
  - Limiter les méthodes HTTP utilisées pour les actions sensibles.
  - Vérifier l'en-tête `Referer` lorsque cela est possible.