### 6. **Exposition aux données sensibles**

L'exposition aux données sensibles est une menace majeure dans le domaine de la sécurité informatique. Elle peut survenir à la suite de failles dans les applications, d'erreurs humaines ou de mauvaises pratiques de sécurité. Cette exposition peut inclure des informations personnelles, financières, ou encore des informations sensibles sur le système lui-même, et peut entraîner des violations de la vie privée, des fraudes ou des cyberattaques.

#### **1. Exposition des informations**

L'exposition des informations survient lorsqu'une application ou un système divulgue des données sensibles sans protection adéquate. Cela peut se produire via des interfaces publiques non sécurisées ou mal configurées.

##### **Causes fréquentes d’exposition :**
- **Stockage non sécurisé des données** : Les données sensibles, comme les mots de passe, les numéros de carte de crédit ou les informations de santé, sont stockées sans chiffrement dans des bases de données ou des fichiers accessibles.
- **Transmission non sécurisée des données** : Les informations sensibles sont transmises en clair via HTTP plutôt que via des protocoles sécurisés comme HTTPS.
- **API mal protégées** : Les API qui exposent des données sensibles ne sont pas correctement authentifiées ou autorisées, permettant à n'importe qui d’y accéder.
- **Permissions inappropriées** : Des utilisateurs ou services se voient accorder des autorisations excessives, leur permettant d'accéder à des données qu'ils ne devraient pas voir.

##### **Exemple concret :**  
Une base de données d’une application e-commerce stocke les numéros de carte de crédit en clair, sans chiffrement. Si cette base est compromise, les attaquants peuvent accéder directement aux informations financières des utilisateurs.

##### **Exemple technique :**
Dans un formulaire web, les données d'un utilisateur (nom, adresse, carte bancaire) sont envoyées via HTTP, et donc vulnérables à une interception via une attaque de type "Man-in-the-Middle" sur un réseau Wi-Fi public.







