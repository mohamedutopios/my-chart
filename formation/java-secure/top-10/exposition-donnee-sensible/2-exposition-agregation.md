#### **2. Exposition par extraction de données et agrégation**

L'extraction et l'agrégation de données sont des techniques par lesquelles un attaquant compile plusieurs petites informations non sensibles, les combine et en déduit des informations sensibles.

##### **Comment cela fonctionne :**
- **Extraction de données** : Les attaquants collectent des morceaux d'informations publiques ou partiellement accessibles (par exemple, des noms d'utilisateurs, des adresses email, des dates de naissance).
- **Agrégation de données** : En combinant ces informations à partir de différentes sources ou systèmes, ils peuvent reconstituer des données plus complètes et sensibles. Par exemple, la combinaison d’une adresse email et d’une date de naissance peut permettre de deviner des questions de sécurité ou d’accéder à des comptes.

##### **Exemple concret :**  
Un attaquant recueille des informations publiques sur des profils LinkedIn (noms, entreprises, postes), des adresses email sur des forums en ligne, et des dates de naissance à partir de réseaux sociaux. En agrégeant ces informations, il peut cibler spécifiquement des individus pour des attaques de phishing ou usurper leur identité.

##### **Exemple technique :**
Une API expose les dates de naissance et les prénoms d'utilisateurs sans restriction. Ces données seules peuvent ne pas sembler critiques, mais lorsqu'elles sont combinées à d'autres informations (comme des emails), elles peuvent être utilisées pour des attaques ciblées.