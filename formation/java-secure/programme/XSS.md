### Scripting intersites (XSS)

Le **scripting intersites** (Cross-Site Scripting, ou XSS) est une vulnérabilité de sécurité commune dans les applications Web. Elle permet à des attaquants d'injecter du contenu malveillant (typiquement du code JavaScript) dans les pages Web consultées par d'autres utilisateurs. L'attaquant peut alors voler des informations, usurper l'identité des utilisateurs, ou exécuter des actions non désirées au nom des victimes.

#### 1. **Les bases des scripts intersites (XSS)**

Le XSS se produit lorsque des applications Web permettent l'injection de scripts malveillants dans le contenu affiché aux utilisateurs sans vérification ni filtrage des données saisies par les utilisateurs. Cela permet à un attaquant de contourner les politiques de sécurité du navigateur pour exécuter du code malveillant sur le navigateur de la victime.

##### **Principaux vecteurs d'attaque XSS** :

- **Injection de scripts dans des champs de formulaires** : Les données fournies par l'utilisateur sont insérées dans une page Web sans être correctement échappées, ce qui permet à l'attaquant d'injecter du code JavaScript malveillant.
- **URL ou liens malveillants** : Les attaquants peuvent inciter les utilisateurs à cliquer sur des liens contenant des scripts malveillants.
- **Commentaires ou messages sur des forums** : Les plateformes qui n'assurent pas la validation des contenus générés par les utilisateurs sont souvent des cibles pour l'injection de XSS.

---

#### 2. **Types de scripts intersites (XSS)**

Les vulnérabilités XSS se déclinent en trois catégories principales : **XSS réfléchi**, **XSS stocké**, et **XSS basé sur le DOM**.

##### **1. XSS réfléchi (Reflected XSS)**

Dans le cas du **XSS réfléchi**, les données malveillantes sont injectées via une requête HTTP et immédiatement renvoyées au navigateur sans être stockées sur le serveur. Ce type d'attaque nécessite que l'attaquant incite la victime à cliquer sur un lien malveillant ou à soumettre un formulaire corrompu.

**Exemple** :
- Un lien malveillant est envoyé par email ou intégré dans une URL. Lorsqu'un utilisateur clique dessus, un script injecté est exécuté.
  
##### **2. XSS stocké (Stored XSS)**

Le **XSS stocké** se produit lorsque des données malveillantes sont stockées sur le serveur et renvoyées à plusieurs utilisateurs sans validation ou filtrage approprié. C'est l'une des formes les plus dangereuses, car le code injecté affecte de nombreux utilisateurs sans interaction directe avec l'attaquant.

**Exemple** :
- Un attaquant injecte un script JavaScript dans un champ de commentaires. Chaque utilisateur qui consulte la page contenant ces commentaires exécute le script.

##### **3. XSS basé sur le DOM (DOM-based XSS)**

Dans le **XSS basé sur le DOM**, l'injection malveillante ne passe pas par le serveur mais se produit entièrement sur le client via des modifications du Document Object Model (DOM) de la page. Le code JavaScript du site est manipulé pour exécuter du code injecté par l'attaquant.

**Exemple** :
- Le script JavaScript d'une page modifie dynamiquement le contenu basé sur l'URL ou les données de l'utilisateur sans validation, permettant l'exécution de code malveillant.

---

#### 3. **Étude de cas - XSS dans les comptes Fortnite**

**Contexte** : En 2019, une vulnérabilité XSS dans le système de connexion de **Fortnite** a permis aux attaquants d'exploiter cette faille pour voler les comptes des utilisateurs.

**Description de l'attaque** :
- L'attaquant a exploité une faiblesse XSS sur le site web d'Epic Games, l'éditeur de Fortnite, en incitant des utilisateurs à cliquer sur un lien malveillant.
- Lorsque les utilisateurs cliquaient sur ce lien, le code injecté accédait aux **jetons d'authentification** de l'utilisateur, permettant ainsi à l'attaquant de prendre le contrôle de leur compte sans nécessiter de mot de passe.

**Conséquences** :
- Les utilisateurs touchés ont vu leurs comptes compromis, avec un risque de vol de données personnelles, de paiements non autorisés ou de perte d'objets en jeu.
- Epic Games a corrigé cette vulnérabilité en renforçant la validation et la gestion des entrées utilisateurs sur leur site Web.

Cette attaque montre l'importance de protéger toutes les parties d'une application Web, surtout celles liées aux données sensibles telles que les systèmes d'authentification et de sessions.

---

#### 4. **Bonnes pratiques en matière de protection XSS**

Pour se protéger contre les attaques XSS, il existe plusieurs bonnes pratiques à suivre, aussi bien au niveau du développement que de la configuration des serveurs et des navigateurs.

##### **1. Échappement et validation des entrées utilisateur**

- **Échappement des données** : Toutes les données injectées dans une page HTML, particulièrement dans des balises ou des attributs, doivent être échappées correctement pour empêcher l'exécution de scripts.
  
  **Exemple avec un moteur de templates** (Thymeleaf, Handlebars, etc.) :
  ```html
  <p th:text="${userInput}">Utilisateur</p> <!-- Thymeleaf échappe automatiquement les caractères -->
  ```

- **Validation stricte des entrées** : Validez toutes les entrées utilisateur côté serveur (et côté client si nécessaire) pour limiter les types de données pouvant être injectées. Par exemple, les champs de texte ne devraient pas accepter de balises HTML ou JavaScript.

##### **2. Utilisation des en-têtes de sécurité**

- **Content Security Policy (CSP)** : Définissez une politique de sécurité des contenus (CSP) dans les en-têtes HTTP pour restreindre les sources à partir desquelles les scripts peuvent être chargés. Par exemple :
  
  ```http
  Content-Security-Policy: script-src 'self'
  ```
  Cette directive ne permet l'exécution que des scripts provenant du domaine de la page elle-même (`self`), limitant ainsi l'injection de scripts externes.

- **X-XSS-Protection** : Activez le filtre XSS intégré dans certains navigateurs via l'en-tête HTTP **X-XSS-Protection**.
  
  ```http
  X-XSS-Protection: 1; mode=block
  ```

##### **3. Désactiver l'exécution de scripts**

- Si vous utilisez des fonctionnalités telles que des éditeurs WYSIWYG ou des champs HTML, assurez-vous de nettoyer les entrées pour supprimer tout code JavaScript ou script malveillant. Des bibliothèques comme **OWASP Java HTML Sanitizer** peuvent aider à désinfecter les entrées HTML des utilisateurs.

##### **4. Cookies sécurisés**

- Utilisez l'attribut **HttpOnly** pour empêcher l'accès aux cookies de session via JavaScript. Cela empêche l'exécution de scripts injectés via XSS d'accéder aux informations de session de l'utilisateur.

  ```http
  Set-Cookie: sessionId=abc123; HttpOnly; Secure
  ```

##### **5. Désactiver l'évaluation de scripts dynamiques**

- Dans des environnements JavaScript comme Node.js, évitez d'utiliser des méthodes qui permettent l'évaluation de chaînes de texte comme code, telles que `eval()`, `setTimeout()`, et `setInterval()` avec des arguments de type chaîne de caractères.

---

### **Résumé des bonnes pratiques :**

1. **Échapper et valider les entrées utilisateur** avant de les afficher dans le HTML.
2. **Utiliser les en-têtes de sécurité**, notamment **CSP** et **X-XSS-Protection**, pour restreindre l'exécution des scripts.
3. **Nettoyer les données HTML** pour enlever tout script malveillant dans des entrées HTML dynamiques.
4. **Protéger les cookies de session** avec les attributs **HttpOnly** et **Secure**.
5. **Éviter l'utilisation de `eval()`** et autres méthodes similaires qui évaluent dynamiquement du code JavaScript.

Ces pratiques, lorsqu'elles sont appliquées de manière rigoureuse, permettent de réduire considérablement les risques d'attaques XSS sur une application Web.