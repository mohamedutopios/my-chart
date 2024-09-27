#### **3. Fuite d'informations sur le système**

La fuite d'informations sur le système consiste à révéler des détails techniques d'une application ou d'un serveur qui pourraient être exploités par un attaquant pour compromettre le système. Ces informations incluent souvent des versions de logiciels, des configurations de serveurs, des adresses IP, des chemins d’accès, ou des logs d'erreurs.

##### **Causes fréquentes :**
- **Messages d’erreur trop verbeux** : Les applications révèlent trop d'informations dans leurs messages d'erreur (ex. : les versions de serveurs, les chemins d'accès à des fichiers sur le serveur).
- **Logs non sécurisés** : Les fichiers journaux (logs) contiennent des informations sensibles ou des données techniques et ne sont pas protégés adéquatement.
- **En-têtes HTTP révélateurs** : Certains serveurs ou services envoient des en-têtes HTTP qui indiquent la version exacte du logiciel utilisé (ex. : Apache 2.4.18), ce qui permet aux attaquants de cibler des vulnérabilités connues.

##### **Exemple concret :**  
Un site web affiche des messages d'erreur complets lorsque des utilisateurs essaient d'accéder à des fichiers restreints, révélant des chemins d'accès au serveur et la version du système d'exploitation. Ces informations peuvent être utilisées pour identifier des vulnérabilités spécifiques à ces configurations.

##### **Exemple technique :**
Une requête vers un serveur retourne l'en-tête HTTP suivant :  
```
Server: Apache/2.4.18 (Ubuntu)
```
Cela révèle non seulement le serveur utilisé (Apache), mais également la version du serveur et le système d'exploitation. Un attaquant peut utiliser ces informations pour trouver et exploiter des vulnérabilités spécifiques à cette configuration.