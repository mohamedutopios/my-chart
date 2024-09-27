### 5. **Gestion des mots de passe**

La gestion des mots de passe est une composante essentielle de la sécurité d'authentification. Bien qu'il existe de nombreuses alternatives et méthodes plus sécurisées (comme l'authentification multi-facteurs ou sans mot de passe), les mots de passe restent largement utilisés. Une mauvaise gestion des mots de passe peut exposer les utilisateurs à des attaques comme le vol de compte, les attaques par force brute, ou les attaques par dictionnaire. Voici les bonnes pratiques à suivre pour renforcer la gestion des mots de passe dans un environnement sécurisé.

#### **1. Politiques de création de mots de passe**

L'une des principales faiblesses des systèmes d'authentification est l'utilisation de mots de passe faibles. Les politiques de création de mots de passe doivent donc encourager les utilisateurs à choisir des mots de passe forts et difficiles à deviner.

- **Longueur minimale** : Un mot de passe devrait avoir au moins 12 caractères pour rendre les attaques par force brute plus difficiles.
- **Complexité** : Un mot de passe doit inclure un mélange de lettres majuscules et minuscules, de chiffres, et de caractères spéciaux.
- **Interdiction de mots de passe communs** : Il faut empêcher l'utilisation de mots de passe courants comme "123456", "password", ou encore "qwerty".
- **Pas d'utilisation d’informations personnelles** : Les mots de passe ne doivent pas inclure d'informations personnelles faciles à deviner, comme le nom de l'utilisateur, son adresse, ou sa date de naissance.

**Exemple de bonne pratique** :  
Lors de la création d’un compte, l’interface exige un mot de passe d’au moins 12 caractères, avec un mélange de lettres, chiffres, et caractères spéciaux. Si l’utilisateur essaie un mot de passe trop simple, le système le rejette avec un message indiquant la nécessité d’un mot de passe plus complexe.

#### **2. Hachage et salage des mots de passe**

Le stockage sécurisé des mots de passe est primordial. Les mots de passe ne doivent jamais être stockés en clair dans une base de données. Au lieu de cela, ils doivent être "hachés" (cryptés de manière irréversible) et "salés" (avec l’ajout de données aléatoires avant le hachage).

- **Hachage sécurisé** : Utiliser des algorithmes de hachage adaptés à la gestion des mots de passe, comme **bcrypt**, **scrypt** ou **Argon2**, qui sont spécialement conçus pour ralentir les attaques par force brute.
- **Salage des mots de passe** : Ajouter un "sel" (une chaîne de caractères aléatoire) à chaque mot de passe avant de le hacher. Cela empêche les attaques par dictionnaire et les tables arc-en-ciel, car même des mots de passe identiques auront des hachages différents.

**Exemple de bonne pratique** :  
Lorsque l’utilisateur crée un mot de passe, le serveur génère un "sel" unique pour cet utilisateur, puis hache le mot de passe avec bcrypt avant de le stocker. Si la base de données est compromise, les attaquants ne pourront pas récupérer les mots de passe en clair.

#### **3. Rotation et expiration des mots de passe**

La rotation des mots de passe consiste à forcer les utilisateurs à changer leur mot de passe régulièrement, afin de limiter le risque que des mots de passe compromis soient utilisés pendant une longue période.

- **Expiration périodique** : Fixer une durée de validité des mots de passe (par exemple, 90 jours). Après cette période, les utilisateurs doivent être invités à créer un nouveau mot de passe.
- **Gestion des mots de passe anciens** : Empêcher les utilisateurs de réutiliser leurs anciens mots de passe, pour éviter qu'ils n'alternent entre deux ou trois mots de passe de manière répétée.

**Exemple de bonne pratique** :  
Un utilisateur reçoit une notification indiquant que son mot de passe va expirer dans 7 jours et qu’il doit le changer. Le système lui interdit de réutiliser les 5 derniers mots de passe déjà utilisés.

#### **4. Stockage sécurisé des mots de passe côté client**

Lors de la connexion, les mots de passe sont souvent saisis dans des formulaires sur les navigateurs. Il est important que le stockage côté client soit sécurisé, pour éviter que des scripts malveillants ou des attaques de type XSS (cross-site scripting) ne puissent récupérer les mots de passe.

- **Pas de stockage en clair dans le navigateur** : Ne jamais stocker les mots de passe en clair dans le stockage local, les cookies ou le cache du navigateur.
- **Utilisation de gestionnaires de mots de passe** : Encourager les utilisateurs à utiliser des gestionnaires de mots de passe, qui peuvent générer, stocker et remplir automatiquement des mots de passe complexes et uniques.

**Exemple de bonne pratique** :  
L’application recommande l’utilisation d’un gestionnaire de mots de passe, et vérifie que les mots de passe ne sont jamais stockés dans des cookies ou en local dans le navigateur sans chiffrement.

#### **5. Utilisation de l'authentification multi-facteurs (MFA)**

Même un mot de passe fort peut être compromis par des attaques comme le phishing ou le keylogging. L'authentification multi-facteurs (MFA) ajoute une couche de protection supplémentaire, en exigeant une seconde méthode d'authentification.

- **Exemples de facteurs supplémentaires** : Utilisation de tokens matériels (comme une clé USB de type YubiKey), de codes à usage unique envoyés par SMS, ou de l’authentification biométrique (empreinte digitale, reconnaissance faciale).

**Exemple de bonne pratique** :  
Après avoir saisi son mot de passe, un utilisateur reçoit un code à usage unique par SMS ou via une application d'authentification comme Google Authenticator. Ce code doit être entré pour compléter le processus d’authentification.

#### **6. Réinitialisation sécurisée des mots de passe**

Lorsqu'un utilisateur oublie son mot de passe, la procédure de réinitialisation doit être suffisamment sécurisée pour empêcher toute prise de contrôle malveillante du compte.

- **Lien de réinitialisation sécurisé** : Envoyer un lien de réinitialisation par email, mais s'assurer que ce lien expire après une courte période (généralement 10 à 15 minutes) et qu'il ne peut être utilisé qu'une seule fois.
- **Vérification multi-facteurs** : Pour les comptes à haut niveau de sécurité, exiger un second facteur d'authentification (comme un code SMS) pour vérifier l'identité de l'utilisateur avant de permettre la réinitialisation du mot de passe.

**Exemple de bonne pratique** :  
Un utilisateur qui oublie son mot de passe reçoit un email avec un lien de réinitialisation. Ce lien expire après 15 minutes et l'utilisateur doit répondre à une question de sécurité ou entrer un code de vérification envoyé par SMS pour compléter la réinitialisation.

#### **7. Notifications de changements de mots de passe**

Lorsqu’un mot de passe est modifié ou réinitialisé, l’utilisateur doit être averti pour éviter que des changements non autorisés ne passent inaperçus.

- **Notification par email ou SMS** : Envoyer une notification immédiate à l'utilisateur après tout changement de mot de passe, indiquant si la modification a été faite par lui ou non.
- **Proposer des mesures de sécurité en cas de modification suspecte** : Offrir la possibilité de réinitialiser rapidement le mot de passe et de verrouiller le compte en cas de modification non autorisée.

**Exemple de bonne pratique** :  
Après qu’un utilisateur a modifié son mot de passe, il reçoit un email indiquant que son mot de passe a été changé avec succès, et contenant un lien pour réinitialiser le mot de passe s’il n’est pas à l'origine du changement.

#### **8. Audits réguliers de sécurité des mots de passe**

Les systèmes doivent régulièrement auditer et analyser la sécurité des mots de passe, y compris vérifier si des mots de passe ne sont pas trop faibles ou si des utilisateurs n'ont pas changé leur mot de passe depuis longtemps.

- **Vérification de mots de passe faibles** : Identifier les utilisateurs qui ont des mots de passe trop faibles ou qui n'ont pas changé de mot de passe depuis une longue période et leur demander de les mettre à jour.
- **Surveillance des violations de données** : Comparer régulièrement les mots de passe hachés avec des bases de données publiques de mots de passe compromis pour s'assurer qu'aucun compte utilisateur n'est vulnérable.

**Exemple de bonne pratique** :  
L’administrateur d’un système effectue un audit trimestriel des mots de passe utilisateurs, en identifiant ceux qui utilisent encore des mots de passe faibles ou qui n'ont pas été changés depuis un an.

---

### **Résumé des bonnes pratiques pour la gestion des mots de passe**

- **Utilisation de mots de passe complexes** : Encourager la création de mots de passe longs et complexes avec une politique de sécurité stricte.
- **Hachage et salage des mots de passe** : Ne jamais stocker de mots de passe en clair. Utiliser des algorithmes de hachage sécurisés et ajouter des sels pour chaque mot de passe.
- **Rotation et expiration des mots de passe** : Imposer un renouvellement périodique des mots de passe et empêcher la réutilisation des anciens mots de passe.
- **Stockage sécurisé côté client** : Ne jamais stocker de mots de passe en clair sur le client. Utiliser des gestionnaires de mots de passe pour aider les utilisateurs à gérer leurs mots de passe.


- **Authentification multi-facteurs** : Ajouter un second facteur pour renforcer la sécurité en cas de compromission des mots de passe.
- **Réinitialisation sécurisée** : Vérifier l'identité avant de permettre la réinitialisation d’un mot de passe et limiter l’utilisation des liens de réinitialisation.
- **Notifications de modifications** : Informer les utilisateurs de toute modification de leur mot de passe pour détecter rapidement les accès non autorisés.
- **Audits réguliers** : Effectuer des audits de sécurité pour identifier les faiblesses dans la gestion des mots de passe et s'assurer que les mots de passe n'ont pas été compromis.

