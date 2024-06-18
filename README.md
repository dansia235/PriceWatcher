# Installation de l’application PriceWatcher

## 1. Installation de l’environnement

Le déploiement de `PRICEWATCHER` a été développé avec l’IDE Apache NetBeans 11.3. NetBeans nécessite l’installation des éléments suivants sur un Système d’exploitation Windows :

- Serveur physique : 4 Go de RAM, 80 Go DD, Processeur Dual Core (Caractéristiques acceptables)
- Serveur de base de données évolué (Ex : PostgreSQL 10+)
- Serveur d’application JEE (Ex : Payara 5.193+ / GlassFish 4.0+)
- Navigateur web à jour (poste client)
- Lecteur de PDF (poste client)
- Connection internet : 100 Ko/s (Caractéristiques acceptables)

Une fois l’environnement d’exécution installé, utiliser le dossier fourni pour déployer l’application où vous trouverez les fichiers sollicités.

### Étapes d'installation

1. Installer le JDK11 : [Télécharger JDK11](https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html)
2. Installer PostgreSQL 10+ : [Télécharger PostgreSQL](https://www.postgresql.org/download/windows/) et laisser les ports par défaut

   Après l'installation de PostgreSQL, lancer l’outil pgAdmin4, connectez-vous au serveur avec votre mot de passe et créez une base de données nommée `pricewatcher_db`. Exécutez ensuite le script de création et d’initialisation des tables `pricewatcher_db_create_init.sql` qui se trouve dans le dossier fourni.

3. Installer GlassFish / Payara 5.0+ : [Télécharger Payara](https://www.payara.fish/downloads/payara-platform-community-edition/) et laisser les ports par défaut

## 2. Déploiement de l’application PriceWatcher

Pour le déploiement de l’application, veuillez d’abord vous connecter à l’interface d’administration de GlassFish / Payara à l’adresse suivante : [http://localhost:4848](http://localhost:4848) puis connectez-vous avec votre login et votre mot de passe.

### Configuration du pool de connexion JDBC

1. Développer le nœud `JDBC` et cliquer sur `JDBC Connection Pools`, puis cliquer sur `New` et remplissez les fenêtres comme suit :
   - Nom du pool : `post-gre-sql_pricewatcher_db_pricewatcherPool`
   - Type de ressource : `javax.sql.DataSource`
   - Vendeur du pilote : `Postgresql`
2. Cliquer sur `Next` : dans la prochaine fenêtre, cocher `Ping Enable` (cela vous permettra de vérifier si la connexion sur la base de données dans PostgreSQL est correcte) et en bas ajouter ou renseigner les propriétés suivantes :
   - `URL` : `jdbc:postgresql://localhost:5432/pricewatcher_db`
   - `driverClass` : `org.postgresql.Driver`
   - `Password` : (Votre mot de passe)
   - `portNumber` : `5432`
   - `databaseName` : `pricewatcher_db`
   - `User` : `postgres`
   - `serverName` : `localhost`

   Puis cliquer sur `Finish`.

3. Cliquer maintenant sur `JDBC Resources`, puis cliquer sur `New` et remplissez le formulaire comme suit :
   - `Nom JNDI` : `jdbc/pricewatcher_db`
   - `Nom du pool` : `post-gre-sql_pricewatcher_db_pricewatcherPool` (le pool créé plus haut)

   Puis cliquer sur `Ok`.

### Déploiement de l'application

1. Cliquer sur le nœud `Applications`, puis cliquer sur `Deploy` et remplissez la fenêtre comme suit :
   - `Location` : choisir `Local Packaged File or Directory That Is Accessible from GlassFish / Payara Server`
   - Cliquer sur `Browse` et naviguer jusqu’au fichier `PriceWatcher-ear-1.0.ear` qui se trouve dans le dossier de l’application fourni, puis cliquer sur `Choose File`

   En fin, cliquer sur `Ok` et patienter pendant le déploiement de l’application.

## 3. Lancement de l'application

Le lancement de l’application se fait à partir d’un navigateur web (Mozilla Firefox par exemple) sur n’importe quelle plateforme (Windows, Linux, Mac OS, Solaris, etc.) en entrant tout simplement l’adresse de l’application :

- Serveur : [https://localhost:8080/PriceWatcher-web/index.xhtml](https://localhost:8080/PriceWatcher-web/index.xhtml)
- Machine client : Remplacer `localhost` par l’adresse IP du serveur (ex : [http://192.168.1.1:8080/PriceWatcher-web/index.xhtml](http://192.168.1.1:8080/PriceWatcher-web/index.xhtml))

Une fois l’adresse validée, vous accédez à la page d’accueil.
