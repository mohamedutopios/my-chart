### Entités externes XML (XXE)

L'attaque par entités externes XML (**XXE**) est une vulnérabilité liée à la manière dont certains parseurs XML gèrent les **entités externes définies dans un DTD**. Elle permet à un attaquant d'exploiter cette fonctionnalité pour lire des fichiers arbitraires sur le système, effectuer des attaques SSRF (Server-Side Request Forgery), ou mener d'autres actions malveillantes. Voici un aperçu détaillé de ce que sont les DTDs et les entités, ainsi que la manière dont les attaques XXE fonctionnent.

---

#### 1. **La définition de type de documents (DTD) et les entités**

Un **Document Type Definition (DTD)** est une spécification définissant la structure et les éléments d'un document XML. Dans une DTD, il est possible de déclarer des **entités**, qui sont essentiellement des raccourcis pour insérer du contenu dans un document XML.

Il existe deux types d'entités :
- **Entités internes** : elles sont définies directement dans le document XML et contiennent du texte.
- **Entités externes** : elles peuvent pointer vers une ressource externe, comme un fichier ou une URL.

##### **Exemple d'entités dans un DTD :**

```xml
<!DOCTYPE note [
  <!ENTITY myInternalEntity "Ceci est une entité interne">
  <!ENTITY myExternalEntity SYSTEM "file:///etc/passwd">
]>
```

Dans cet exemple :
- **myInternalEntity** est une entité interne contenant une chaîne de texte.
- **myExternalEntity** est une entité externe qui pointe vers un fichier système (`/etc/passwd` dans ce cas, qui est un fichier sensible sur les systèmes Unix).

Le problème survient lorsque les parseurs XML sont configurés pour traiter ces entités sans validation ou contrôle approprié.

---

#### 2. **Expansion des entités**

Lorsqu'un parseur XML rencontre une entité dans un document, il tente de la **"développer"** en remplaçant l'entité par sa valeur. Pour les entités internes, cela signifie simplement l'insertion d'une chaîne de texte. Mais pour les entités externes, cela peut entraîner l'accès à des ressources locales ou distantes, ouvrant la porte à diverses attaques.

##### **Exemple d'un fichier XML avec une entité externe :**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE note [
  <!ENTITY ext SYSTEM "file:///etc/passwd">
]>
<note>
  <to>&ext;</to>
</note>
```

Dans cet exemple, le parseur XML remplacera `&ext;` par le contenu du fichier `/etc/passwd` lors de la lecture du document.

---

#### 3. **Attaque d'une entité externe (XXE)**

Une attaque **XXE** (External Entity Attack) consiste à injecter des entités externes malveillantes dans un document XML pour exploiter des failles de sécurité au sein du système de l'application cible. Les conséquences d'une attaque XXE incluent :

- **Lecture de fichiers sensibles** : L'attaquant peut lire des fichiers présents sur le serveur en injectant une entité externe pointant vers ces fichiers.
- **Attaque SSRF (Server-Side Request Forgery)** : L'attaquant peut forcer le serveur à faire des requêtes HTTP vers des systèmes internes protégés.
- **DoS (Denial of Service)** : En abusant des entités externes pour créer des boucles de récursion infinie, il est possible de provoquer une surcharge des ressources du serveur.

##### **Exemple d'attaque XXE pour lire un fichier sensible :**

Un attaquant soumet le XML suivant à une application vulnérable :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE data [
  <!ENTITY xxe SYSTEM "file:///etc/passwd">
]>
<data>
  <username>&xxe;</username>
</data>
```

Si l'application parse le XML et permet l'expansion des entités externes, elle remplacera `&xxe;` par le contenu du fichier `/etc/passwd`, exposant ainsi des informations sensibles.

##### **Exemple d'attaque SSRF via XXE** :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE root [
  <!ENTITY xxe SYSTEM "http://internal-service.local/api">
]>
<root>
  <data>&xxe;</data>
</root>
```

Dans cet exemple, le parseur XML effectue une requête HTTP vers un service interne à partir du serveur, ce qui peut permettre à un attaquant de contourner les mécanismes de sécurité réseau (pare-feu, etc.).

---

### **Protection contre les attaques XXE**

Voici les meilleures pratiques pour se protéger contre les attaques XXE.

#### 1. **Désactiver l'expansion des entités externes**

L'une des protections les plus efficaces consiste à désactiver la gestion des entités externes dans les parseurs XML. La plupart des bibliothèques XML modernes permettent de configurer les parseurs pour désactiver cette fonctionnalité.

##### **Désactiver les entités externes avec le parseur XML de Java** :

```java
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

DocumentBuilder db = dbf.newDocumentBuilder();
Document doc = db.parse(new FileInputStream("data.xml"));
```

Dans cet exemple, les entités externes sont désactivées pour éviter l'injection de fichiers ou de ressources externes dans les documents XML.

#### 2. **Validation des entrées XML**

Assurez-vous que toutes les données XML proviennent de sources fiables et que les entrées utilisateur sont validées avant d'être utilisées dans le système. Une validation rigoureuse permet de réduire les risques d'injection d'entités malveillantes.

#### 3. **Utilisation de formats de données plus sûrs**

Si possible, préférez des formats de données moins complexes et plus sûrs, comme **JSON**, qui n'a pas de gestion d'entités externes et présente moins de vulnérabilités intrinsèques que XML.

---

### **Résumé des bonnes pratiques :**

- **Désactivez les entités externes** dans les parseurs XML pour empêcher les attaques XXE.
- **Validez les entrées utilisateur** pour vous assurer que seules des données sûres sont parsées.
- **Utilisez des formats alternatifs** comme JSON si possible, car ils sont plus simples et moins sujets aux vulnérabilités liées à la gestion des entités.
- **Mettez à jour les bibliothèques XML** pour bénéficier des correctifs de sécurité.

L'application de ces pratiques permettra de protéger votre application contre les attaques par entités externes XML (XXE).